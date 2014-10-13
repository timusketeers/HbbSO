package com.howbuy.framework.core.aar.plugin;

/**
 * 插件接口.
 * @author li.zhang
 *
 */
public interface IPlugin
{
    /**
     * 得到插件名称.
     * @return 插件名称.
     */
    public String getPluginName();
    
    /**
     * 执行插件核心业务.
     */
    public void execute();
}
