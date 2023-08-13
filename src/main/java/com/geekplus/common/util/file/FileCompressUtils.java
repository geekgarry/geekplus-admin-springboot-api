/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 8/4/23 03:24
 * description: 做什么的？
 */
package com.geekplus.common.util.file;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileCompressUtils {
    private static final int BUFFER_SIZE = 2 * 1024;
    private static final String FILE_END = ".zip";

    /**
     * 把字节流打成压缩包并输出到客户端浏览器中
     */
    public static void downloadZipStream(HttpServletResponse response, List<Map<String, byte[]>> compressInfoList, String zipFileName) throws Exception {
        response.reset(); //重置一下输出流
        response.setCharacterEncoding("UTF-8");
        //response.setContentType("application/x-msdownload"); // 不同类型的文件对应不同的MIME类型
        response.setContentType("application/x-download");
        // 对文件名进行编码处理中文问题
        zipFileName = new String(zipFileName.getBytes(), StandardCharsets.UTF_8);
        //将Content-Disposition暴露给前端
        //response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        // inline在浏览器中直接显示，不提示用户下载,attachment弹出对话框，提示用户进行下载保存本地,默认为inline方式
        response.setHeader("Content-Disposition", "attachment;filename=" + zipFileName + FILE_END);
        //response.setContentType(MediaType.APPLICATION_OCTET_STREAM.getType());
        //response.setContentType("application/octet-stream; charset=UTF-8");
        //response.setHeader("Accept-Ranges", "bytes");
        // 告知浏览器文件的大小
        //response.addHeader("Content-Length", String.valueOf(compressInfoList.toArray().length));

        // --设置成这样可以不用保存在本地，再输出，通过response流输出,直接输出到客户端浏览器中。
        ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
        for (Map<String, byte[]> stringMap : compressInfoList) {
            for (Map.Entry<String, byte[]> entry : stringMap.entrySet()) {
                zip.putNextEntry(new ZipEntry(entry.getKey()));
                //Files.copy(Paths.get(entry.getKey()), zip);
                zip.write(entry.getValue(), 0, entry.getValue().length);
                zip.closeEntry();
                zip.flush();
            }
        }
        zip.close();
    }

    /**
     * 生成zip文件
     */
    public static void genCode(HttpServletResponse response, List<Map<String, byte[]>> data) throws IOException
    {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"geekplus.zip\"");
        response.addHeader("Content-Length", "" + data.toArray().length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        // inline在浏览器中直接显示，不提示用户下载,attachment弹出对话框，提示用户进行下载保存本地,默认为inline方式
        // --设置成这样可以不用保存在本地，再输出，通过response流输出,直接输出到客户端浏览器中。
        // --设置成这样可以不用保存在本地，再输出，通过response流输出,直接输出到客户端浏览器中。
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

        for (Map<String, byte[]> byteMap : data) {
            for (Map.Entry<String, byte[]> entry : byteMap.entrySet()) {
                // 添加一个新的ZipEntry到压缩包中
                zos.putNextEntry(new ZipEntry(entry.getKey()));
                // 创建压缩文件
                //ZipOutputStream zip = new ZipOutputStream(outputStream);
                zos.write(entry.getValue(), 0, entry.getValue().length);
                zos.flush();
                zos.closeEntry();
            }
        }
        zos.close();
        //IOUtils.write(data, response.getOutputStream());
    }

    public static void main(String[] args) throws Exception {
        //将文件压缩到一个zip文件
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File("D://test//testZipFile.zip")));
        File srcFile = new File("F:\\01小工具们\\Fish3.270221\\Fish-v327-0221");
        toZip(srcFile,zos,true);
    }

    /**
     * 把文件打成压缩包并输出到客户端浏览器中
     */
    public static void downloadZipFiles(HttpServletResponse response, File srcFile, String zipFileName, boolean keepDirStructure) throws Exception {
        response.reset(); //重置一下输出流
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-msdownload"); // 不同类型的文件对应不同的MIME类型
        // 对文件名进行编码处理中文问题
        zipFileName = new String(zipFileName.getBytes(), StandardCharsets.UTF_8);
        // inline在浏览器中直接显示，不提示用户下载,attachment弹出对话框，提示用户进行下载保存本地,默认为inline方式
        response.setHeader("Content-Disposition", "attachment;filename=" + zipFileName + FILE_END);

        // --设置成这样可以不用保存在本地，再输出，通过response流输出,直接输出到客户端浏览器中。
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        toZip(srcFile, zos, keepDirStructure);
    }


    /**
     * 压缩文件或是文件夹为一个zip包
     *
     * @param srcFile          要压缩的文件或文件夹
     * @param zos
     * @param keepDirStructure
     * @throws RuntimeException
     */
    public static void toZip(File srcFile, ZipOutputStream zos, boolean keepDirStructure) throws Exception {
        if (!srcFile.isFile()) {
            //若路径是一个文件夹,则压缩文件夹下的所有文件,同时保持原文件结构
            File[] files = srcFile.listFiles();
            for (File file : files) {
                compress(file, zos, file.getName(), keepDirStructure);
            }
        } else {
            //如果路径是一个文件,则直接生成一个压缩包
            compress(srcFile, zos, srcFile.getName(), keepDirStructure);
        }

        zos.close();
        System.out.println("压缩完成");
    }

    /**
     * 主要的递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }
}
