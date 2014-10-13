package com.howbuy.framework.core.aar.plugin;


/**
 * 业务插件接口.
 * @author li.zhang
 *
 */
public abstract class AbstractHbPlugin implements IPlugin
{
    /**
     * 构造方法
     */
    protected AbstractHbPlugin()
    {
        super();
        initPlugin();
    }

    /**
     * 初始化插件.
     */
    protected void initPlugin()
    {
        //1.初始化插件的数据源.
        initDataSource();
        
        //2.初始化流程链.
        initFlowChain();
        
        //3.额外的初始化动作.
        extraInitial();
    }

    /**
     * 初始化插件的数据源.这里需要有默认实现的代码.
     */
    protected void initDataSource()
    {
        
    }
    
    /**
     * 初始化流程链.这个方法暴露给子类进行业务流程的编排.
     */
    protected void initFlowChain()
    {
        
    }
    
    /**
     * 这个方法暴露给子类，处理一些额外的特殊的初始化动作.
     */
    protected abstract void extraInitial();
    
    /**
     * 执行插件核心业务.
     */
    public void execute()
    {
        
    }
}
