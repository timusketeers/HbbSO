package com.howbuy.framework.core.aar;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.howbuy.framework.core.aar.plugin.HbbLoader;
import com.howbuy.framework.core.aar.util.AarUtils;
import com.howbuy.framework.core.utils.CollectionUtils;


/**
 * aar包解析器.
 * @author li.zhang
 * 2014-8-25
 *
 */
public final class AarResolver
{
    /** aar文件. **/
    private File aarFile;
    
    /** 标识aar包是否已经被解析. **/
    private boolean resolved;
    
    /** aar解压后对应的文件. **/
    private File unaarFile;
    
    /** 这个属性对应aar包下的startup/startup.xml文件. **/
    private StartupConfig startupConfig;
    
    /** 这个属性对应aar包下的META-INFO/context.xml文件的封装. **/
    private ContextConfig contextXmlConfig;
    
    /** aarLibPath指的是aar包下lib目录下的所有jar包对应的url路径. 该aar下的所有hbb插件共享其aarLibPath下的类库. **/
    private URL[] aarLibPath;
    
    /** aar应用包的类加载器. **/
    private AarLoader aarLoader;
    
    /** 
     * 每个元素对应modules文件夹下的一个hbb_**.jar包
     * 
     * 或者一个hbb_**.xml文件(考虑到有些业务插件直接调用aar lib包下的类的方法，只需在hbb_**.xml文件中进行配置说明，无需提供jar代码的情况.). 
     * 
     * modules文件夹下的每个hbb_**.jar或者hbb_**.xml均是一个独立的业务插件.
     */
    private File[] hbbs;
    
    /** key为对应的hbb插件的名称, value为hbb插件的加载器. **/
    private Map<String, HbbLoader> hbbMaps;
    
    /**
     * 构造方法.
     * @param aarFile aar文件.
     * @throws Exception 
     */
    public AarResolver(File aarFile) throws Exception
    {
        this(aarFile, true, false);
    }
    
    /**
     * @param aarFile aar文件
     * @param resolve 标识是否在构造方法中就解析.
     */
    public AarResolver(File aarFile, boolean prepare, boolean resolve) throws Exception
    {
        this.aarFile = aarFile;
        
        String aarFileName = aarFile.getName();
        String destFileName = aarFileName.substring(0, aarFileName.lastIndexOf(".aar"));
        File unaarFile = new File(aarFile.getParentFile(), destFileName);
        if (!unaarFile.exists())
        {
            unaarFile.mkdirs();
        }
        this.unaarFile = unaarFile;
        
        //1.解析前的准备工作.
        if (prepare)
        {
            prepareResolveAar();
        }
        
        //2.解析aar包.
        if (resolve)
        {
            resolveAarFile();
        }
    }

    /**
     * 解析aar包
     * @throws Exception
     */
    public void resolveAarFile() throws Exception
    {
        //1.如果aar应用包已经解析过,直接返回.
        if (this.resolved)
        {
            return;
        }
        
        //2.解析aar包下的startup/startup.xml文件.
        File startupXmlFile = new File(unaarFile, "startup/startup.xml");
        parseStartupXml(startupXmlFile);
        
        //3.解析aar包下META-INF/context.xml文件.
        File contextXmlFile = new File(unaarFile, "META-INF/context.xml");
        parseContextXml(contextXmlFile);
        
        //4.配置aar下的lib库url路径.
        File aarlib = new File(unaarFile, "lib");
        initAarLoader(aarlib);
        
        //5.解析aar包下对应的hbb插件.
        File modules = new File(unaarFile, "modules");
        loadHbbs(modules);//这里其实就是构造所有hbb组成的内存结构.
        
        //6.aar执行环境初始化.
        initAarContext(this.startupConfig, this.aarLoader.getAarLoader());
        
        this.resolved = true;
    }
    
    /**
     * 执行aar应用包中所有hbb中的业务逻辑.
     * @return 返回各个hbb执行的结果. key为hbb业务模块名称, value为执行结果.
     * @throws Exception 
     */
    public Map<String, Object> execHbbs() throws Exception
    {
        Map<String, Object> execRslt = null;
        
        //如果aar应用包还没有解析过,直接抛出异常.
        if (!this.resolved)
        {
            throw new Exception("Please resolve current aar first, current aar is " + aarFile.getName());
        }
        
        if (null == this.hbbMaps || 0 == this.hbbMaps.size())
        {
            return execRslt;
        }
        
        execRslt = new HashMap<String, Object>();
        Iterator<String> iterator = this.hbbMaps.keySet().iterator();
        while (iterator.hasNext())
        {
            String hbbName = iterator.next();
            HbbLoader loader = this.hbbMaps.get(hbbName);
            execRslt.put(hbbName, loader.execHbb());
        }
        
        return execRslt;
    }
    
    /**
     * 执行当aar包下名称为hbbName这个业务模块中的业务逻辑.
     * @param hbbName 业务模块名称,形如hbb_div等..
     * @param args  
     * @return 返回hbbName这个hbb业务模块的执行结果.
     * @throws Exception 
     */
    public Object execHbb(String hbbName, String... args) throws Exception
    {
        //如果aar应用包还没有解析过,直接抛出异常.
        if (!this.resolved)
        {
            throw new Exception("Please resolve current aar first, current aar is " + aarFile.getName());
        }
        
        HbbLoader hbbLoader = hbbMaps.get(hbbName);
        return hbbLoader.execHbb(args);
    }

    /**
     * 解析aar包前的准备动作.
     * @throws Exception 
     */
    private void prepareResolveAar() throws Exception
    {
        if (null == this.unaarFile || !this.unaarFile.exists())
        {
            return;
        }
        
        //1.先删除解开的aar包.
        AarUtils.deleteFile(this.unaarFile);
        
        //2.将aarFile解压到unaarFile.
        AarUtils.unAar(aarFile, unaarFile);
    }

    /**
     * 解析startup/startup.xml文件.这个文件中配置了用于初始化这个aar应用包的信息.
     * @param startupXmlFile
     * @throws Exception
     */
    private void parseStartupXml(File startupXmlFile) throws Exception
    {
        this.startupConfig = new StartupConfig(startupXmlFile);
    }

    /**
     * 解析META-INF/context.xml文件
     * @throws Exception 
     */
    private void parseContextXml(File contextXmlFile) throws Exception
    {
        this.contextXmlConfig = new ContextConfig(contextXmlFile);
    }
    
    /**
     * 初始化aar类加载器
     * @param aarlib
     * @throws Exception 
     */
    private void initAarLoader(File aarlib) throws Exception
    {
        if (!aarlib.exists())
        {
            return;
        }
        
        File[] jarsUnderLib = aarlib.listFiles(new FileFilter()
        {
            
            @Override
            public boolean accept(File pathname)
            {
                boolean accept = false;
                if (pathname.getName().endsWith(".jar"))
                {
                    accept = true;
                }
                return accept;
            }
        });
        
        URL[] urls = new URL[jarsUnderLib.length];
        if (CollectionUtils.isEmpty(jarsUnderLib))
        {
            return;
        }
        
        for (int i = 0; i < jarsUnderLib.length; i++)
        {
            File jar = jarsUnderLib[i];
            urls[i] = jar.toURI().toURL();
        }
        
        this.aarLoader = new AarLoader(urls);
        
        //得到该aar的公共类库的类路径集合.
        this.aarLibPath = urls;
    }
    
    /**
     * 加载hbb插件.
     * @param dir 表示从哪个目录加载hbb_***.jar文件.
     * @throws Exception 
     */
    private void loadHbbs(File dir) throws Exception
    {
        //1.加载hbb前的准备工作..
        prepareLoadHbbs(dir);
        
        //2.分别加载aar包下的各个hbb插件.
        if (CollectionUtils.isEmpty(this.hbbs))
        {
            return;
        }
        
        String hbbName = null;
        Map<String, HbbLoader> hbbMaps = new HashMap<String, HbbLoader>();
        for (File hbb : this.hbbs)
        {
            hbbName = hbb.getName();
            hbbName = hbbName.substring(0, hbbName.lastIndexOf("."));
            hbbMaps.put(hbbName, new HbbLoader(hbb, this.aarLoader));
        }
        
        this.hbbMaps = hbbMaps;
    }

    /**
     * 准备动作
     * @param dir 表示从哪个目录加载hbb_***.jar文件.
     */
    private void prepareLoadHbbs(File dir)
    {
        if (null == dir || !dir.exists())
        {
            return;
        }
        
        if (!dir.isDirectory())
        {
            return;
        }
        
        File[] hbbs = dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                boolean accept = false;
                String fileName = pathname.getName();
                //过滤出形如hbb_**.jar或者hbb_**.xml形式的文件.
                if (fileName.startsWith("hbb_") && (fileName.endsWith(".jar") || fileName.endsWith(".xml")))
                {
                    accept = true;
                }
                
                return accept;
            }
        });
        
        this.hbbs = hbbs;
    }

    /**
     * 初始化aar应用包的执行环境.
     * 例如: 初始化spring配置、初始化hibernate配置以及初始化log4j日志等..
     * @param config config
     * @throws Exception 
     */
    private void initAarContext(final StartupConfig config, final ClassLoader loader)
        throws Exception
    {
        String initClass = config.getInitClass();
        String methodName = config.getMethodName();
        Arg[] args = config.getArgs();
        
        Class<?>[] argsClzz = null;
        Object[] argsValue = null;
        if (!CollectionUtils.isEmpty(args))
        {
            argsClzz = new Class<?>[args.length];
            argsValue = new Object[args.length];
            
            for (int i = 0; i < args.length; i++)
            {
                argsClzz[i] = Class.forName(args[i].getArgType(), true, loader);
                argsValue[i] = args[i].getArgValue();
            }
        }
        
        Class<?> clzz = Class.forName(initClass, true, loader);
        
        Object instance = clzz.newInstance();
        Method method = clzz.getDeclaredMethod(methodName, argsClzz);
        method.invoke(instance, argsValue);
    }

    public File getAarFile()
    {
        return aarFile;
    }
    
    public StartupConfig getStartupConfig()
    {
        return startupConfig;
    }

    public ContextConfig getContextXmlConfig()
    {
        return contextXmlConfig;
    }

    public Map<String, HbbLoader> getHbbMaps()
    {
        return hbbMaps;
    }
    
    public URL[] getAarLibPath()
    {
        return aarLibPath;
    }

    public AarLoader getAarLoader()
    {
        return aarLoader;
    }
}
