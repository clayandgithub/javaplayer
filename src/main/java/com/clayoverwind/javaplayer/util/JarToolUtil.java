package com.clayoverwind.javaplayer.util;

import java.io.File;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public class JarToolUtil {

    public static String getJarFilePath() {
        File file = getFile();
        return file == null ? null : file.getAbsolutePath();
    }

    public static String getJarDir() {
        File file = getFile();
        return file == null ? null : getFile().getParent();
    }

    private static File getFile() {
        String path = JarToolUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8"); // 转换处理中文及空格
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return new File(path);
    }
}
