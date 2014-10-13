package com.howbuy.framework;

import java.io.File;
import java.io.FileFilter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.howbuy.framework.core.aar.AarResolver;
import com.howbuy.framework.core.registry.AarRegistry;
import com.howbuy.framework.core.utils.CollectionUtils;
import com.howbuy.framework.core.utils.LogUtils;

/**
 * 初始化器.
 * @author li.zhang
 *
 */
public final class SystemInitializer
{
    /** LOGGER **/
    private static final Logger LOGGER = Logger.getLogger(SystemInitializer.class);
    
    /** aar plugin插件的存放目录.. **/
    private static final String DEFAULT_PLUGINS_PATH = "../plugins";
    
    /** log4j.properties文件的位置. **/
    private static final String DEFAULT_LOG4J_FILE = "../log4j.properties";
    
    /** aar plugin插件存放目录. **/
    private final String pluginsDir;
    
    private final String log4jFile;
    
    /**
     * 默认构造方法.
     */
    public SystemInitializer()
    {
        this(DEFAULT_PLUGINS_PATH, DEFAULT_LOG4J_FILE);
    }
    
    /**
     * 构造方法.
     * @param pluginsDir
     */
    public SystemInitializer(String pluginsDir, String log4jFile)
    {
        this.pluginsDir = pluginsDir;
        this.log4jFile = log4jFile;
    }
    
    /**
     * 初始化所有的aar包.
     */
    public void init()
    {
        //1.初始化日志.
        initLogger(log4jFile);
        
        //2.加载插件.
        loadPlugins();
        
    }

    /**
     * 初始化日志.
     * 
     * 这里的日志配置会侵入到aar应用包中的日志打印.
     * 
     * 考虑到，HbbSO应用下可能会有多个aar应用包，而每个aar应用包可能都有自己与众不同的日志打印风格.
     * 
     * 而我们的系统有可能同时调用多个aar包，这样就造成log4j.jar本身不能很好的支持我们的这种日志打印要求.
     * 
     * 因为, log4j所能做到的是:
     *    
     *    log4j.properties文件中的配置通过PropertyConfigurator.configure("log4j.properties");这句来生效.
     *    
     *  这句代码会导致log4j.properties中的配置被存储在一个类似于map的结构中，日志如何打印这些日志打印决策类的东东
     *  
     *  都是从这个map结构中拿的. 所以虽然，我们可以通过:
     *       PropertyConfigurator.configure("old_log4j.properties")
     *       PropertyConfigurator.configure("new_log4j.properties")
     *   
     *   这两句来更新日志的配置，从而实现HbbSO和aar应用包的日志输出在不同的目录并有不同的输出格式。但是却会造成一个同步的问题.
     *   
     *   因为任何一个时刻日志的配置生效的只有一个。 如果HbbSO异步调用aar应用包此时log4j就无能为力。另外,如果HbbSO同时调用多个
     *   
     *   aar应用包也会有类似的问题.
     */
    private void initLogger(String log4jFile)
    {
        PropertyConfigurator.configure(log4jFile);
    }

    /**
     * 加载aar包和其中的hbb业务插件.
     */
    private void loadPlugins()
    {
        LOGGER.info("Load plugins begin.........");
        
        AarRegistry registry = AarRegistry.getInstance();
        
        File pluginsDir = new File(this.pluginsDir);
        if (!pluginsDir.exists())
        {
            return;
        }
        
        File[] aars = pluginsDir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                boolean accept = false;
                if (pathname.getName().endsWith(".aar"))
                {
                    accept = true;
                }
                
                return accept;
            }
        });
        
        if (CollectionUtils.isEmpty(aars))
        {
            return;
        }
        
        String aarName = null;
        AarResolver resolver = null;
        for (File aar : aars)
        {
            try
            {
                aarName = aar.getName();
                aarName = aarName.substring(0, aarName.lastIndexOf(".aar"));
                resolver = new AarResolver(aar, true, true);//这里直接执行了解析aar包的逻辑.
                registry.registerAar(aarName, resolver);
            }
            catch (Exception e)
            {
                LogUtils.printStackTrace(LOGGER, e);
            }
        }
        
        LOGGER.info("Load plugins end...........");
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        new SystemInitializer().init();
    }
}
