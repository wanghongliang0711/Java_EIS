package com.mic.eis.util;

/**
 * @author blake.wang
 * @date 2020-07-20 13:10
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{


    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }


    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

}
