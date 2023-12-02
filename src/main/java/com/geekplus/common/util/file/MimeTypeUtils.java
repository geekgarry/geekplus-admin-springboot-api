package com.geekplus.common.util.file;

import java.util.Arrays;

/**
 * 媒体类型工具类
 *
 * @author
 */
public class MimeTypeUtils
{
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] IMAGE_EXTENSION = { "bmp", "gif", "jpg", "jpeg", "png", "JPG", "JPEG", "PNG", "webp",
            "tiff", "tif", "psd", "svg", "PSD", "dxf", "cdr"};

    public static final String[] VIDEO_EXTENSION = {"mp4", "avi", "mov", "mpg", "mpeg", "3gp", "wmv", "asf", "rm", "rmvb"};

    public static final String[] AUDIO_EXTENSION = {"mp3", "flac", "wav", "wma", "ape", "midi", "aac", "mov", "mid"};

    public static final String[] DOC_EXTENSION = { "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt" };

    public static final String[] COMPRESS_EXTENSION = { "rar", "zip", "zipx", "gz", "tgz", "bz2", "tar", "txz", "xz", "7z", "iso", "cab", "ace", "sqx" };

    public static final String[] FLASH_EXTENSION = { "swf", "flv" };

    public static final String[] MEDIA_EXTENSION = { "swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb" };

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // pdf
            "pdf" };

    //根据文件名判断文件类型
    public static String getFileExtensionType(String fileName){
        String fileExtension=FileTypeUtils.getFileType(fileName);
        String fileType="";
//        for (String str : IMAGE_EXTENSION)
//        {
//            if (str.equalsIgnoreCase(fileExtension))
//            {
//                fileType="image";
//            }
//        }
        if(Arrays.asList(IMAGE_EXTENSION).contains(fileExtension)){
            fileType="image";
        }else if(Arrays.asList(VIDEO_EXTENSION).contains(fileExtension)){
            fileType="video";
        }else if(Arrays.asList(AUDIO_EXTENSION).contains(fileExtension)){
            fileType="audio";
        }else if(Arrays.asList(FLASH_EXTENSION).contains(fileExtension)){
            fileType="flash";
        }else if(Arrays.asList(DOC_EXTENSION).contains(fileExtension)){
            fileType="document";
        }else if(Arrays.asList(COMPRESS_EXTENSION).contains(fileExtension)){
            fileType="compress";
        }else if("pdf".equals(fileExtension)){
            fileType="pdf";
        }else {
            fileType=fileExtension;
        }
        return fileType;
    }

    public static String getExtension(String prefix)
    {
        switch (prefix)
        {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }
}
