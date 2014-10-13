package com.howbuy.framework.core.registry;

import java.util.HashMap;
import java.util.Map;

import com.howbuy.framework.core.aar.AarResolver;

/**
 * aar包注册管理中心.
 * @author li.zhang
 * 2014-8-25
 *
 */
public final class AarRegistry
{
    /** aarRegistry实例,单例. **/
    private static AarRegistry INSTANCE = new AarRegistry();
    
    /** key为aar文件名, value为对应的aar应用包的解析器. **/
    private Map<String, AarResolver> aars = new HashMap<String, AarResolver>();
    
    /**
     * 私有构造方法.
     */
    private AarRegistry()
    {
        
    }
    
    /**
     * 单例.
     * @return
     */
    public static AarRegistry getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * 注册aar应用包对应的解析器.
     * @param aarName 对应的aar包的名称,去掉了文件后缀.
     * @param resolver
     */
    public void registerAar(String aarName, AarResolver resolver)
    {
        Map<String, AarResolver> aarsMap = new HashMap<String, AarResolver>();
        aarsMap.putAll(this.aars);
        aarsMap.put(aarName, resolver);
        
        this.aars = aarsMap;
    }
    
    /**
     * 注销aar应用包对应的解析器.
     * @param aarName 对应的aar应用包的名称，去掉了文件后缀.
     */
    public synchronized AarResolver unregisterAar(String aarName)
    {
        AarResolver resolver = this.aars.remove(aarName);
        return resolver;
    }
    
    /**
     * 查询aar应用包对应的解析器.
     * @param aarName 应用包
     * @return
     */
    public AarResolver queryAarResolver(String aarName)
    {
        return this.aars.get(aarName);
    }
}
