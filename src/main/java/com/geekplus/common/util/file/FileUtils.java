package com.geekplus.common.util.file;

import com.geekplus.common.config.WebAppConfig;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.common.util.string.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * 文件处理工具类
 *
 * @author
 */
public class FileUtils
{
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";
    private static Logger log= LoggerFactory.getLogger(FileUtils.class);
    private MimetypesFileTypeMap mtftp;

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归删除文件夹和文件
     *
     * @param filePath 文件
     * @return
     */
    public static int deleteFileByRecursion(String filePath)
    {
        int delCount = 0;
        File file = new File(filePath);
        if(file.isDirectory() && file.listFiles().length>0){
            File[] files=file.listFiles();
            for(File fileItem : files){
                deleteFileByRecursion(fileItem.getPath());
            }
        }else if(file.isDirectory() && file.listFiles().length==0){
            file.delete();
            delCount++;
        }
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            delCount++;
        }
        return delCount;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static int deleteFileCategory(String filePath){
        int flag=0;
        File file = new File(filePath);
        // 路径为文件夹且为空则进行删除
        if (file.isDirectory() && file.listFiles().length==0)
        {
            file.delete();
            flag = 1;
        }
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = 1;
        }
        //File[] listFiles = file.listFiles();
//        if(listFiles != null)
//        {
//            for(File f: listFiles)
//            {
//                if(f.isDirectory())
//                {
//                    flag=deleteFileCategory(f.getAbsolutePath());
//                }
//                else
//                {
//                    f.delete();
//                    flag=true;
//                }
//            }
//        }
        return flag;
    }
    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 检查文件是否可下载
     *
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    public static boolean checkAllowDownload(String resource)
    {
        // 禁止目录上跳级别
        if (StringUtils.contains(resource, ".."))
        {
            return false;
        }

        // 检查允许下载的文件规则
        if (ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource)))
        {
            return true;
        }

        // 不在允许下载的文件规则
        return false;
    }

    /**
     * 下载文件名重新编码
     *
     * @param request 请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 下载文件名重新编码
     *
     * @param response 响应对象
     * @param realFileName 真实文件名
     * @return
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException
    {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.setHeader("Content-disposition", contentDispositionValue.toString());
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException
    {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    /** * 读取某个文件夹下的所有文件夹和文件 */
    public static List<Map> readFileList(String filepath) throws IOException {
        List<Map> listFile=new ArrayList<>();

        File file = new File(filepath);
        if (!file.isDirectory()) {
            Map<String,Object> map=new Hashtable<>();
            //Map<String,Object> map=new Hashtable<>();
            //type=file为文件，type=folder为文件夹
            map.put("type","file");
            map.put("path",file.getPath());
            //map.put("time",file.getFreeSpace());
            map.put("filePath",file.getAbsolutePath().replace(WebAppConfig.getProfile()+File.separator,""));
            map.put("fileName",file.getName());
            listFile.add(map);
        } else {
            String[] fileList = file.list();
            for (int i = 0; i < fileList.length; i++) { //\\反斜杠是转译字符
                Map<String,Object> mapp=new Hashtable<>();
                File readFile = new File(filepath + File.separator + fileList[i]);
                if(readFile.isDirectory()){
                    //Map<String,Object> map=new Hashtable<>();
                    //type=file为文件，type=folder为文件夹
                    mapp.put("type","folder");
                }else {
                    //Map<String,Object> map=new Hashtable<>();
                    //type=file为文件，type=folder为文件夹
                    mapp.put("type", "file");
                }
                mapp.put("path",file.getPath());
                mapp.put("filePath",readFile.getAbsolutePath().replace(WebAppConfig.getProfile()+File.separator,""));
                mapp.put("fileName",readFile.getName());
                listFile.add(mapp);
            }
        }
//        sortRes = listFile.stream().sorted(Comparator.comparing(Map::map.get("fileName"))).collect(Collectors.toList());
        //取到文件夹内所有文件，在根据后缀过滤(按.分割)，然后路径加上文件名就是一个图片的地址了。
        //返回值用list或者拼接的字符串去前台解析也可以。
        return listFile;
    }

    /** * 读取某个文件夹下的所有图片文件 */
    public static List<String> readFileImage(String filepath,String folder) throws FileNotFoundException, IOException {
        List<String> listImage=new ArrayList<>();
        try {
            File file = new File(filepath+folder);
            if (!file.isDirectory()) {
//                System.out.println("文件");
//                System.out.println("path=" + file.getPath());
//                System.out.println("absolutePath=" + file.getAbsolutePath());
//                System.out.println("name=" + file.getName());
                if(isImage(file.getPath())) {
                    listImage.add(Constant.RESOURCE_PREFIX + folder + File.separator +file.getName());
                }
            } else {
                String[] fileList = file.list();
                for (int i = 0; i < fileList.length; i++) { //\\反斜杠是转译字符
                    File readFile = new File(filepath + folder + File.separator + fileList[i]);
                    if (!readFile.isDirectory()) {
//                        System.out.println("path=" + readFile.getPath());
//                        System.out.println("absolutePath=" + readFile.getAbsolutePath());
//                        System.out.println("name=" + readFile.getName());
                        if(isImage(readFile.getPath())) {
                            listImage.add(Constant.RESOURCE_PREFIX + folder + File.separator +readFile.getName());
                        }
                    } else if (readFile.isDirectory()) {
                        readFileImage(filepath+ folder, File.separator + fileList[i]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("readFile() Exception:" + e.getMessage());
        }
        //取到文件夹内所有文件，在根据后缀过滤(按.分割)，然后路径加上文件名就是一个图片的地址了。
        //返回值用list或者拼接的字符串去前台解析也可以。
        return listImage;
    }

    //判断是否为图片
    public static boolean isImage(String fileAllPath){
        String imgFormat=fileAllPath.split("\\.",-1)[1];
        String[] extensions= MimeTypeUtils.IMAGE_EXTENSION;
        return Arrays.asList(extensions).contains(imgFormat);
    }

//    public FileUtils(){
//        mtftp = new MimetypesFileTypeMap();
//        /* 不添加下面的类型会造成误判 详见：http://stackoverflow.com/questions/4855627/java-mimetypesfiletypemap-always-returning-application-octet-stream-on-android-e*/
//        mtftp.addMimeTypes("image png tif gif jpg jpeg bmp");
//    }
    //判断是否为图片
    public static boolean isImageFile(MultipartFile file){
//        String mimetype= mtftp.getContentType(file);
        String fileNameOriginal = file.getOriginalFilename();// 文件原名
        String fileExtension=FileTypeUtils.getFileType(fileNameOriginal);
        boolean isImage1=false;
        boolean isImage2=false;
        String mimeType = ServletUtil.getRequest().getServletContext().getMimeType(fileNameOriginal);
        if (mimeType.startsWith("image/")) {
            // It's an image.
            isImage1=true;
        }
        // 判断文件扩展名是否为图片类型
        if(Arrays.asList(MimeTypeUtils.IMAGE_EXTENSION).contains(fileExtension)){
            isImage2=true;
        }

        return isImage1||isImage2;
    }

    //判断是否为视频
    public static boolean isVideoFile(MultipartFile file) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String fileExtension=FileTypeUtils.getFileType(fileName);
        boolean isVideo1=false;
        boolean isVideo2=false;
        // 判断文件类型是否为视频类型
        if (contentType != null && contentType.startsWith("video/")) {
            isVideo1=true;
        }

        // 判断文件扩展名是否为视频类型
        if(Arrays.asList(MimeTypeUtils.VIDEO_EXTENSION).contains(fileExtension)){
            isVideo2=true;
        }

        return isVideo1||isVideo2;
    }

    //判断是否为音频
    public static boolean isAudioFile(MultipartFile file) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String fileExtension=FileTypeUtils.getFileType(fileName);
        boolean isAudio1=false;
        boolean isAudio2=false;
        // 判断文件类型是否为音频类型
        if (contentType != null && contentType.startsWith("audio/")) {
            isAudio1=true;
        }

        // 判断文件扩展名是否为音频类型
        //if (fileName != null && fileName.endsWith(".mp3"))
        //if (str.equalsIgnoreCase(fileExtension))
        if(Arrays.asList(MimeTypeUtils.AUDIO_EXTENSION).contains(fileExtension)){
            isAudio2=true;
        }

        return isAudio1||isAudio2;
    }

    public static final File getExistFileCategory(String filePath) {
        File file = new File(filePath);
        if (!file.getParentFile().exists())
        {
            //做一个二次判断的保险，防止没有创建成功
            if (!file.getParentFile().getParentFile().exists())
            {
                file.getParentFile().getParentFile().mkdirs();
            }
            file.getParentFile().mkdirs();
//            getExistFileCategory(file.getParentFile().getPath());
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        else{
//            file.delete();
//            file.createNewFile();
//        }
        return file;
    }

    //apache tika的文件解析类库
//    public static String getFileTypeByTika(String fileName) {
//        return null;
//    }

    /**
     * @Author geekplus
     * @Description //递归遍历处文件中的所有文件信息组成List
     * @Param
     * @Throws
     * @Return {@link }
     */
    public static List<Map<String,Object>> getAllFileDirectoryInfo(File file) {
        //File file=new File(filePath);
        File[] fileList = file.listFiles();
        //File file = new File(filePath);
        List<Map<String,Object>> mapList=new ArrayList<>();
        if (fileList == null || fileList.length == 0) {
            return null;
        }
        try {
            for (File f : fileList) {
                //这里将列出所有的文件夹,如果是文件目录为0文件为1
                int isFolder=f.isDirectory()?0:1;
                //这里将列出所有的文件
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createTime = formatter.format(f.lastModified());
                Path filePath = Paths.get(f.getAbsolutePath());
                BasicFileAttributes attributes;
                attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                long modificationTime = attributes.lastModifiedTime().toMillis();
                String updateTime = formatter.format(modificationTime);
                Map mapKV=new HashMap<String,Object>();
                if(!f.isHidden()) {
                    //System.out.println("file==>" + f.getAbsolutePath());
                    String fileAbPathName = f.getAbsolutePath();//.replaceAll(WebAppConfig.getProfile(),Constant.RESOURCE_PREFIX);
                    String systemPrimaryFile = f.getParentFile().getParent();
                    mapKV.put("fileName", f.getName());
                    mapKV.put("filePath", fileAbPathName.replaceAll(systemPrimaryFile,""));
                    mapKV.put("fileUrl", f.getPath().replaceAll(systemPrimaryFile,""));
                    if(isFolder==1){
                        mapKV.put("fileType",MimeTypeUtils.getFileExtensionType(f.getName()));
                    }
                    mapKV.put("isFolder", isFolder);
                    mapKV.put("totalSpace", f.getTotalSpace());
                    mapKV.put("freeSpace", f.getFreeSpace());
                    mapKV.put("parentCategory", "/");
                    mapKV.put("fileSize", f.length());
                    mapKV.put("createTime", createTime);
                    mapKV.put("updateTime", updateTime);
                    mapList.add(mapKV);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mapList;
    }

    /**
     * @Author geekplus
     * @Description //递归遍历处文件中的所有音乐文件信息组成List
     * @Param
     * @Throws
     * @Return {@link }
     */
    public static void getAllMusicFileInfo(File file,List<Map<String,Object>> mapList) {
        //File file=new File(filePath);
        File[] fileList = file.listFiles();
        //File file = new File(filePath);
        if (fileList == null || fileList.length == 0) {
            return;
        }
        try {
            for (File f : fileList) {
                if (f.isDirectory()) {
                    //这里将列出所有的文件夹,如果是文件目录，则递归调用
                    //System.out.println("Dir==>" + f.getAbsolutePath());
                    getAllMusicFileInfo(f,mapList);
                } else if(f.isFile()) {
                    //这里将列出所有的文件夹,如果是文件目录为0文件为1
                    //int isFolder = f.isDirectory() ? 0 : 1;
                    //这里将列出所有的文件
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createTime = formatter.format(f.lastModified());
                    Path filePath = Paths.get(f.getAbsolutePath());
                    BasicFileAttributes attributes;
                    attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                    long modificationTime = attributes.lastModifiedTime().toMillis();
                    String updateTime = formatter.format(modificationTime);
                    Map mapKV = new HashMap<String, Object>();
                    if (!f.isHidden()) {
                        //System.out.println("file==>" + f.getAbsolutePath());
                        String fileAbPathName = f.getAbsolutePath();//.replaceAll(WebAppConfig.getProfile(),Constant.RESOURCE_PREFIX);
                        String systemPrimaryFile = f.getParentFile().getParent();
                        mapKV.put("fileName", f.getName());
                        mapKV.put("filePath", fileAbPathName.replaceAll(systemPrimaryFile, ""));
                        mapKV.put("fileUrl", Constant.RESOURCE_PREFIX + f.getPath().replaceAll(WebAppConfig.getProfile(), ""));
                        //if (isFolder == 1)
                        mapKV.put("fileType", MimeTypeUtils.getFileExtensionType(f.getName()));
                        //mapKV.put("isFolder", isFolder);
                        mapKV.put("totalSpace", f.getTotalSpace());
                        mapKV.put("freeSpace", f.getFreeSpace());
                        mapKV.put("parentCategory", f.getParentFile().getPath().replaceAll(WebAppConfig.getProfile(),""));
                        mapKV.put("fileSize", f.length());
                        mapKV.put("createTime", createTime);
                        mapKV.put("updateTime", updateTime);
                        //mapKV.put("musicInfo", ReadMusicInfo.getMusicInfo(fileAbPathName));
                        mapList.add(mapKV);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
      * @Author geekplus
      * @Description //递归遍历处文件中的所有文件信息组成List
      * @Param
      * @Throws
      * @Return {@link }
      */
    public static void getDirectoryAllFileInfo(File file,List<Map<String,Object>> list) {
        //File file=new File(filePath);
        File[] fileList = file.listFiles();
        //File file = new File(filePath);
        //List<String> avatarList=new ArrayList<>();
        if (fileList == null || fileList.length == 0) {
            return;
        }
        try {
            for (File f : fileList) {
                if (f.isDirectory()) {
                    //这里将列出所有的文件夹,如果是文件目录，则递归调用
                    //System.out.println("Dir==>" + f.getAbsolutePath());
                    getDirectoryAllFileInfo(f,list);
                } else if(f.isFile()){
                    //这里将列出所有的文件
                    List<Map<String,Object>> mapList=new ArrayList<>();
                    Map mapKV=new HashMap<String,Object>();
                    String fileName=f.getName();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createTime = formatter.format(f.lastModified());
                    Path filePath = Paths.get(f.getAbsolutePath());
                    BasicFileAttributes attributes;
                    attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                    long modificationTime = attributes.lastModifiedTime().toMillis();
                    //System.out.println("文件的修改时间为: " + modificationTime);
                    if(!f.isHidden()) {
                        //System.out.println("file==>" + f.getAbsolutePath());
                        String fileAbPathName = f.getAbsolutePath();//.replaceAll(WebAppConfig.getProfile(),Constant.RESOURCE_PREFIX);
                        mapKV.put("fileName", fileName);
                        mapKV.put("filePath", fileAbPathName);
                        mapKV.put("fileUrl", f.getPath());
                        mapKV.put("totalSpace", f.getTotalSpace());
                        mapKV.put("freeSpace", f.getFreeSpace());
                        mapKV.put("parentCategory", f.getParentFile().getParent());
                        mapKV.put("fileSize", f.length());
                        mapKV.put("createTime", f.lastModified());
                        mapKV.put("updateTime", modificationTime);
                        list.add(mapKV);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
      * @Author geekplus
      * @Description //递归遍历处文件中的所有图片文件信息组成List
      * @Param [file, list]
      * @Throws
      * @Return {@link}
      */
    public static void getDirectoryAllFile(File file,List<String> list) {
        //File file = new File(filePath);
        //List<String> avatarList=new ArrayList<>();
        File fileList[] = file.listFiles();
        if (fileList == null || fileList.length == 0) {
            return;
        }
        for (File f : fileList) {
            if (f.isDirectory()) {
                //这里将列出所有的文件夹,如果是文件目录，则递归调用
                //System.out.println("Dir==>" + f.getAbsolutePath());
                getDirectoryAllFile(f,list);
            } else {
                //这里将列出所有的文件
                //System.out.println("file==>" + f.getAbsolutePath());
                if(isImage(f.getAbsolutePath())) {
                    String filePathName=f.getAbsolutePath().replaceAll(WebAppConfig.getProfile(),Constant.RESOURCE_PREFIX);
                    list.add(filePathName);
                }
            }
        }
    }

    /**
      * @Author geekplus
      * @Description // 删除制定日期时间戳之前的文件
      * // 获取当前时间
      * long currentTimeMillis = System.currentTimeMillis();
      * // 计算七天前的时间戳
      * long sevenDaysAgo = currentTimeMillis - (7 * 24 * 60 * 60 * 1000);
      * @Param
      * @Throws
      * @Return {@link }
      */
    public static void deleteFilesOlderThan(File folder, long thresholdTime) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是子文件夹，递归调用该方法
                    if(file.listFiles().length>0){
                        deleteFilesOlderThan(file, thresholdTime);
                    }else{
                        if (file.delete()) {
                            log.info("已删除文件夹: " + file.getAbsolutePath());
                        }
                    }
                } else {
                    // 如果是文件，检查其最后修改时间是否早于阈值时间
                    long lastModified = file.lastModified();
                    if (lastModified < thresholdTime) {
                        // 如果是早于阈值时间的文件，删除它
                        if (file.delete()) {
                            log.info("已删除文件: " + file.getAbsolutePath());
                        } else {
                            log.info("无法删除文件: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
    //实现删除指定天数之前的文件及文件夹
    public void deleteFilesBeforeDays(String path, int days) throws Exception {
        LocalDate thresholdDate = LocalDate.now().minusDays(days);
        Files.walkFileTree(Paths.get(path), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                LocalDate lastModifiedDate = attrs.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (lastModifiedDate.isBefore(thresholdDate)) {
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (Files.list(dir).count() == 0) {
                    Files.delete(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
    //判断文件类型是否为byte字节
    public static boolean isStringType(Object object){

        // 步骤 1: 获取对象类型
        Class<?> objectClass = object.getClass();

        // 步骤 2: 判断是否为String类型
        boolean isString = String.class.isAssignableFrom(objectClass);
        //if (obj.getClass() == String.class){}
        return isString;
    }
    //判断上传的文件是否为空
    public static boolean isFileEmpty(MultipartFile file) {
        return file.isEmpty();
    }
}
