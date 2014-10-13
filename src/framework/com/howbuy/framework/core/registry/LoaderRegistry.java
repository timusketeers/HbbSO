package com.howbuy.framework.core.registry;

import java.util.HashMap;
import java.util.Map;

/**
 * classLoader类的注册工厂.
 * @author li.zhang
 *
 */
public final class LoaderRegistry
{
    /** key为"aar包的名称 + ':' + hbb包的名称", value为对应的插件类加载器. **/
    private static Map<String, ClassLoader> loaderMap = new HashMap<String, ClassLoader>();
    
    /**
     * 私有构造方法.
     */
    private LoaderRegistry()
    {
        
    }
    
    /**
     * 注册插件类加载器.
     * @param pluginName
     * @param loader
     */
    public static void registerLoader(String pluginName, ClassLoader loader)
    {
        Map<String, ClassLoader> map = new HashMap<String, ClassLoader>();
        map.putAll(loaderMap);
        
        if (null != map.get(pluginName))
        {
            return;
        }
        
        map.put(pluginName, loader);
        
        loaderMap = map;
    }
    
    /**
     * 注销注册的插件类加载器.
     * @param pluginName 插件名称.
     * @return 注销的插件类加载器
     */
    public static ClassLoader deRegisterLoader(String pluginName)
    {
        ClassLoader loader = null;
        Map<String, ClassLoader> map = new HashMap<String, ClassLoader>();
        map.putAll(loaderMap);
        
        loader = map.remove(pluginName);
        loaderMap = map;
        
        return loader;
    }
}
