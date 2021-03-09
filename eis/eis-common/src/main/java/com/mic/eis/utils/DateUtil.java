package com.mic.eis.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * @author calisto
 * @date 2020-07-07 11:22 上午
 */
@Slf4j
public class DateUtil {

    /**
     * 获取时间，先取网络时间，再取本地时间
     * @return
     */
    public static Date getCurrentTime() {
//        Date networkTime = getNetworkTime();
//        return networkTime == null ? new Date() : networkTime;
        // 内网， 不连接外网， 直接获取 本地时间
        return new Date();

    }

    /**
     * 获取网络时间
    * @return
     */
    private static Date getNetworkTime() {
        String webUrl = "https://www.baidu.com";
        URL url;
        try {
            url = new URL(webUrl);
            URLConnection conn=url.openConnection();
            conn.connect();
            Date date=new Date(conn.getDate());
            return date;
        } catch (Exception e) {
            log.error("获取网络时间失败...");
            e.printStackTrace();
        }
        return null;
    }
}
