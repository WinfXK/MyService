package cn.winfxk.myservice;

import cn.winfxk.myservice.tool.MyMap;

import java.io.File;
import java.util.Locale;

public class Content {
    private static final MyMap<String, String> map = new MyMap<>();
    private static final String defType = "application/octet-stream";

    public static String getContentType(File file) {
        return getContentType(file.getName());
    }

    public static String getContentType(String object) {
        if (object == null || !object.contains(".")) return defType;
        int index = object.lastIndexOf(".");
        if (index >= object.length()) return defType;
        String type = object.substring(index + 1);
        return type.isEmpty() ? defType : map.containsKey(type.toLowerCase(Locale.ROOT)) ? map.get(type) : defType;
    }

    static {
        map.put("3gp", "video/3gpp");
        map.put("7z", "application/x-7z-compressed");
        map.put("aac", "audio/x-aac");
        map.put("apk", "application/vnd.android.package-archive");
        map.put("bat", "application/x-msdownload");
        map.put("bmp", "image/bmp");
        map.put("bz", "application/x-bzip");
        map.put("bz2", "application/x-bzip2");
        map.put("doc", "application/msword");
        map.put("docm", "application/vnd.ms-word.document.macroenabled.12");
        map.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        map.put("dot", "application/msword");
        map.put("dotm", "application/vnd.ms-word.template.macroenabled.12");
        map.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        map.put("flac", "audio/flac");
        map.put("flv", "video/x-flv");
        map.put("gif", "image/gif");
        map.put("htm", "text/html");
        map.put("html", "text/html");
        map.put("ico", "image/x-icon");
        map.put("jar", "application/java-archive");
        map.put("java", "text/x-java-source");
        map.put("jpe", "image/jpeg");
        map.put("jpeg", "image/jpeg");
        map.put("jpg", "image/jpeg");
        map.put("js", "application/javascript");
        map.put("json", "application/json");
        map.put("mp3", "audio/mpeg");
        map.put("mp4", "video/mp4");
        map.put("png", "image/png");
        map.put("ppt", "application/vnd.ms-powerpoint");
        map.put("pptm", "application/vnd.ms-powerpoint.presentation.macroenabled.12");
        map.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        map.put("rar", "application/x-rar-compressed");
        map.put("wav", "audio/x-wav");
        map.put("wbmp", "image/vnd.wap.wbmp");
        map.put("webm", "video/webm");
        map.put("webp", "image/webp");
        map.put("xlm", "application/vnd.ms-excel");
        map.put("xls", "application/vnd.ms-excel");
        map.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroenabled.12");
        map.put("xlsm", "application/vnd.ms-excel.sheet.macroenabled.12");
        map.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        map.put("xlt", "application/vnd.ms-excel");
        map.put("xltm", "application/vnd.ms-excel.template.macroenabled.12");
        map.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        map.put("xlw", "application/vnd.ms-excel");
        map.put("xml", "application/xml");
    }
}
