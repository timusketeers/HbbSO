package com.howbuy.framework.core.utils;

import java.util.Collection;

/**
 * 集合相关的util工具类.
 * @author li.zhang
 * 2014-8-26
 *
 */
public class CollectionUtils
{
    /**
     * 判断集合是否为空.
     * @param collection 集合
     * @return
     */
    public static boolean isEmpty(Collection<?> collection)
    {
        boolean isEmpty = false;
        if (null == collection || 0 == collection.size())
        {
            isEmpty = true;
        }
        return isEmpty;
    }
    
    public static boolean isEmpty(Object[] arrays)
    {
        boolean isEmpty = false;
        if (null == arrays || 0 == arrays.length)
        {
            isEmpty = true;
        }
        
        return isEmpty;
    }
}
