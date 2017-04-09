package com.clayoverwind.javaplayer.util;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public class PathUtil {
    public static String FILE_SPLITOR = System.getProperty("file.separator");

    public static String transferToCurrentSystemPath(String path) {
        path = path.replace("/", FILE_SPLITOR).replace("\\", FILE_SPLITOR);
        if (path.startsWith("\\")) {
            path = path.substring(1);
        }
        return path;
    }
}