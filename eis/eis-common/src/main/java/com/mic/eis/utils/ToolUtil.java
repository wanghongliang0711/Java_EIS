package com.mic.eis.utils;

import java.io.File;

/**
 * 高频方法集合类
 * @author calisto
 * @date 2020-01-19 4:46 下午
 */
public class ToolUtil {

    /**
     * 获取临时目录
     *
     * @author zmr
     */
    public static String getTempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获取当前项目工作目录
     *
     * @return
     * @author zmr
     */
    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取临时下载目录
     *
     * @return
     * @author zmr
     */
    public static String getDownloadPath() {
        return getTempPath() + File.separator + "download" + File.separator;
    }
}
