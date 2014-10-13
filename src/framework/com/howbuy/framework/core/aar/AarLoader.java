package com.howbuy.framework.core.aar;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个aar包对应一个类加载器,负责加载这个aar应用包的所有类.
 * @author li.zhang
 * 2014-8-27
 *
 */
public class AarLoader
{
    /** 这个aar包的类加载器. 一个aar包对应一个aarLoader. **/
    private final URLClassLoader aarLoader;
    
    /** hbbLoaders, key为hbb插件的名称, value为插件类加载器. **/
    private Map<String, ClassLoader> hbbLoaders = new HashMap<String, ClassLoader>();
    
    /**
     * 构造方法.
     * @param aarLibPath
     */
    public AarLoader(URL[] aarLibPath)
    {
        URLClassLoader loader = new URLClassLoader(aarLibPath);
        this.aarLoader = loader;
    }
    
    /**
     * 注册插件类加载器.
     * @param pluginName hbb插件的名称
     * @param loader 类加载器
     */
    public void registerPluginLoader(String pluginName, ClassLoader loader)
    {
        Map<String, ClassLoader> map = new HashMap<String, ClassLoader>();
        map.putAll(hbbLoaders);
        
        if (null != map.get(pluginName))
        {
            return;
        }
        
        map.put(pluginName, loader);
        
        hbbLoaders = map;
    }
    
    
    /**
     * 注销注册的插件类加载器.
     * @param pluginName hbb插件名称.
     * @return 注销的插件类加载器
     */
    public ClassLoader deRegisterLoader(String pluginName)
    {
        ClassLoader loader = null;
        Map<String, ClassLoader> map = new HashMap<String, ClassLoader>();
        map.putAll(hbbLoaders);
        
        loader = map.remove(pluginName);
        hbbLoaders = map;
        
        return loader;
    }

    public URLClassLoader getAarLoader()
    {
        return aarLoader;
    }
}
