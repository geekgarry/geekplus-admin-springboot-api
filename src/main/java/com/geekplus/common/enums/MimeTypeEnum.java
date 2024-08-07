package com.geekplus.common.enums;

import com.geekplus.common.util.string.StringUtils;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/7/24 10:03 AM
 * description: 做什么的？
 */
public enum MimeTypeEnum {
    AAC("acc", "AAC音频", "audio/aac"),

    ACP("acp", "acp音频", "audio/x-mei-aac"),

    AIFF("aiff", "aiff音频", "audio/aiff"),

    AIFC("aifc", "aifc音频", "audio/aiff"),

    AIF("aif", "aif音频", "audio/aiff"),

    AU("au", "au音频", "audio/basic"),

    LA1("la1", "la1音频", "audio/x-liquid-file"),

    LAVS("lavs", "lavs音频", "audio/x-liquid-secure"),

    LMSFF("lmsff", "lmsff音频", "audio/x-la-lms"),

    M4A("m4a", "M4A音频", "audio/x-m4a"),

    M3U("m3u", "M3U音频", "audio/mpegurl"),

    MP2("mp2", "MP2音频", "audio/mp2"),

    FLAC("flac", "FLAC音频", "audio/flac"),

    MP3("mp3", "MP3音频", "audio/mpeg"),

    MP3_AUDIO("mp3", "MP3音频", "audio/mp3"),

    MP4_AUDIO("mp4", "MP4音频", "audio/mp4"),

    WAV("wav", "波形音频格式", "audio/wav"),

    WMA("wma", "wma音频格式", "audio/x-ms-wma"),

    WAX("wax", "wax音频格式", "audio/x-ms-wax"),

    WEBA("weba", "WEBM音频", "audio/webm"),

    MID("mid", "乐器数字接口（MIDI）", "audio/midi"),

    MIDI("midi", "乐器数字接口（MIDI）", "audio/midi"),

    ABW("abw", "AbiWord文件", "application/x-abiword"),

    ARC("arc", "存档文件", "application/x-freearc"),

    AVI("avi", "音频视频交错格式", "video/x-msvideo"),

    AZW("azw", "亚马逊Kindle电子书格式", "application/vnd.amazon.ebook"),

    BIN("bin", "任何类型的二进制数据", "application/octet-stream"),

    BMP("bmp", "Windows OS / 2位图图形", "image/bmp"),

    BZ("bz", "BZip存档", "application/x-bzip"),

    BZ2("bz2", "BZip2存档", "application/x-bzip2"),

    CSH("csh", "C-Shell脚本", "application/x-csh"),

    CSS("css", "级联样式表（CSS）", "text/css"),

    CSV("csv", "逗号分隔值（CSV）", "text/csv"),

    DOC("doc", "微软Word文件", "application/msword"),

    DOCX("docx", "Microsoft Word（OpenXML）", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),

    EOT("eot", "MS Embedded OpenType字体", "application/vnd.ms-fontobject"),

    EPUB("epub", "电子出版物（EPUB）", "application/epub+zip"),

    GZ("gz", "GZip压缩档案", "application/gzip"),

    GIF("gif", "图形交换格式（GIF）", "image/gif"),

    HTM("htm", "超文本标记语言（HTML）", "text/html"),

    HTML("html", "超文本标记语言（HTML）", "text/html"),

    ICO("ico", "图标格式", "image/vnd.microsoft.icon"),

    ICS("ics", "iCalendar格式", "text/calendar"),

    JAR("jar", "Java存档", "application/java-archive"),

    JPEG("jpeg", "JPEG图像", "image/jpeg"),

    JPG("jpg", "JPEG图像", "image/jpeg"),

    JS("js", "JavaScript", "text/javascript"),

    JSON("json", "JSON格式", "application/json"),

    JSONLD("jsonld", "JSON-LD格式", "application/ld+json"),

    MJS("mjs", "JavaScript模块", "text/javascript"),

    MPEG("mpeg", "MPEG视频", "video/mpeg"),

    MP4("mp4", "MP4视频", "video/mp4"),

    MPKG("mpkg", "苹果安装程序包", "application/vnd.apple.installer+xml"),

    ODP("odp", "OpenDocument演示文稿文档", "application/vnd.oasis.opendocument.presentation"),

    ODS("ods", "OpenDocument电子表格文档", "application/vnd.oasis.opendocument.spreadsheet"),

    ODT("odt", "OpenDocument文字文件", "application/vnd.oasis.opendocument.text"),

    OGA("oga", "OGG音讯", "audio/ogg"),

    OGV("ogv", "OGG视频", "video/ogg"),

    OGX("ogx", "OGG", "application/ogg"),

    OPUS("opus", "OPUS音频", "audio/opus"),

    OTF("otf", "otf字体", "font/otf"),

    PNG("png", "便携式网络图形", "image/png"),

    PDF("pdf", "Adobe 可移植文档格式（PDF）", "application/pdf"),

    PHP("php", "php", "application/x-httpd-php"),

    PPT("ppt", "Microsoft PowerPoint", "application/vnd.ms-powerpoint"),

    PPTX("pptx", "Microsoft PowerPoint（OpenXML）", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),

    RAR("rar", "RAR档案", "application/vnd.rar"),

    RTF("rtf", "富文本格式", "application/rtf"),

    SH("sh", "Bourne Shell脚本", "application/x-sh"),

    SVG("svg", "可缩放矢量图形（SVG）", "image/svg+xml"),

    SWF("swf", "小型Web格式（SWF）或Adobe Flash文档", "application/x-shockwave-flash"),

    TAR("tar", "磁带存档（TAR）", "application/x-tar"),

    TIF("tif", "标记图像文件格式（TIFF）", "image/tiff"),

    TIFF("tiff", "标记图像文件格式（TIFF）", "image/tiff"),

    TS("ts", "MPEG传输流", "video/mp2t"),

    TTF("ttf", "ttf字体", "font/ttf"),

    TXT("txt", "文本（通常为ASCII或ISO 8859- n", "text/plain"),

    VSD("vsd", "微软Visio", "application/vnd.visio"),

    WEBM("webm", "WEBM视频", "video/webm"),

    WEBP("webp", "WEBP图像", "image/webp"),

    WOFF("woff", "Web开放字体格式（WOFF）", "font/woff"),

    WOFF2("woff2", "Web开放字体格式（WOFF）", "font/woff2"),

    XHTML("xhtml", "XHTML", "application/xhtml+xml"),

    XLS("xls", "微软Excel", "application/vnd.ms-excel"),

    XLSX("xlsx", "微软Excel（OpenXML）", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),

    XML("xml", "XML", "application/xml"),

    XUL("xul", "XUL", "application/vnd.mozilla.xul+xml"),

    ZIP("zip", "ZIP", "application/zip"),

    MIME_3GP("3gp", "3GPP audio/video container", "video/3gpp"),

    MIME_3GP_WITHOUT_VIDEO("3gp", "3GPP audio/video container doesn't contain video", "audio/3gpp2"),

    MIME_3G2("3g2", "3GPP2 audio/video container", "video/3gpp2"),

    MIME_3G2_WITHOUT_VIDEO("3g2", "3GPP2 audio/video container  doesn't contain video", "audio/3gpp2"),

    MIME_7Z("7z", "7-zip存档", "application/x-7z-compressed"),

    MHTML("mhtml", "Message文件", "message/rfc822"),

    NOS_TYPE("907", "907文件", "drawing/907"),

    SLK_TYPE("907", "slk文件", "drawing/x-slk"),

    TOP_TYPE("top", "top文件", "drawing/x-top"),

    JAVA_TYPE("java", "Java文件", "java/*"),

    CLAZZ_TYPE("class", "Java文件", "java/*"),

    DWF("dwf", "dwf文件", "Model/vnd.dwf");

    //扩展名
    private final String extension;
    //说明
    private final String explain;
    //contentType/mime类型
    private final String mimeType;

    public String getExtension() {
        return extension;
    }

    public String getExplain() {
        return explain;
    }

    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param extension 上传的文件扩展名
     * @param explain   类型说明
     * @param mimeType  Mime对应的类型
     */
    MimeTypeEnum(String extension, String explain, String mimeType) {
        this.extension = extension;
        this.explain = explain;
        this.mimeType = mimeType;
    }

    /**
     * 通过扩展名获取枚举类型
     *
     * @param extension 扩展名
     * @return 枚举类
     */
    public static MimeTypeEnum getByExtension(String extension) {
        if (StringUtils.isEmpty(extension)) {
            return null;
        }
        for (MimeTypeEnum typesEnum : MimeTypeEnum.values()) {
            if (extension.equalsIgnoreCase(typesEnum.getExtension())) {
                return typesEnum;
            }
        }
        return null;
    }

    /**
     * 通过Mime类型获取扩展名
     *
     * @param mimeType mime类型
     * @return 枚举类
     */
    public static String getExtensionByMimeType(String mimeType) {
        if (StringUtils.isEmpty(mimeType)) {
            return null;
        }
        for (MimeTypeEnum typesEnum : MimeTypeEnum.values()) {
            if (mimeType.equalsIgnoreCase(typesEnum.getMimeType())) {
                return typesEnum.getExtension();
            }
        }
        return null;
    }

    /**
     * Content-Type常用对照
     * 根据后缀获取Mime
     *
     * @param fileType 扩展名
     * @return mime类型
     */
    public static String getContentType(String fileType) {
        MimeTypeEnum mimeTypeEnum = MimeTypeEnum.getByExtension(fileType);
        if (mimeTypeEnum != null) {
            return mimeTypeEnum.getMimeType();
        }
        return "application/octet-stream";
    }
}
