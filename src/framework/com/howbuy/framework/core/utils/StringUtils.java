package com.howbuy.framework.core.utils;

public final class StringUtils
{
    /**
     * 判断字符串是否为空.
     * @param str 字符串
     * @return
     */
    public static boolean isEmpty(String str)
    {
        boolean isEmpty = false;
        if (null == str || "".equals(str.trim()))
        {
            isEmpty = true;
        }
        
        return isEmpty;
    }
}
