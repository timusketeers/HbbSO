package com.howbuy.framework.core.aar.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.howbuy.framework.core.aar.AarLoader;
import com.howbuy.framework.core.utils.CollectionUtils;

/**
 * hbb插件加载器. 一个hbb加载器加载一个hbb插件.
 * @author li.zhang
 * 2014-8-26
 *
 */
public class HbbLoader
{
    /** loader是该hbb插件的类加载器. **/
    private URLClassLoader loader;
    
    /** 对应hbb_**.jar包中的hbb_**.xml文件的封装. **/
    private HbbConfig hbbConfig;
    
    /**
     * 构造方法
     * @param hbbFile 可能是modules/hbb_**.xml也可能是modules/hbb_**.jar文件
     * @param parentLoader 该hbb对应的aar应用包的类加载器
     */
    public HbbLoader(File hbbFile, AarLoader parentLoader) throws Exception
    {
        //1.初始化hbb类加载器.
        initLoader(hbbFile, parentLoader);
        
        //得到hbb_**.xml的输入流.
        InputStream hbbXmlInput = extractHbbXmlInput(hbbFile);
        
        //2.解析hbb_**.xml文件.
        parseHbbXml(hbbXmlInput);
        
        //3.
        loadHbb();
    }

    /**
     * 统一从modules文件下的hbb_**.jar或者hbb_**.xml中提取出hbb_**.xml文件的输入流.
     * @param hbbFile
     * @return
     * @throws Exception
     */
    private InputStream extractHbbXmlInput(File hbbFile) throws Exception
    {
        InputStream hbbXmlInput = null;
        String fileName = hbbFile.getName();
        
        //如果hbbFile为hbb_**.xml文件.
        if (fileName.endsWith(".xml"))
        {
            hbbXmlInput = new FileInputStream(hbbFile);
        }
        else
        {
            //否则hbbFile为hbb_**.jar文件.
            JarFile hbbJar = new JarFile(hbbFile);
            Enumeration<JarEntry> entrys = hbbJar.entries();
            JarEntry hbbXmlEntry = null;
            while (entrys.hasMoreElements())
            {
                JarEntry entry = entrys.nextElement();
                String entryName = entry.getName();
                if (null == entryName || !entryName.startsWith("hbb_") || !entryName.endsWith(".xml"))
                {
                    continue;
                }
                
                hbbXmlEntry = entry;
                break;
            }
            
            if (null == hbbXmlEntry)
            {
                throw new Exception("Invalid hbb jar, can not find the xml config of this hbb, hbb name is " + hbbJar.getName());
            }
            
            hbbXmlInput = hbbJar.getInputStream(hbbXmlEntry);
        }
        
        return hbbXmlInput;
    }
    
    /**
     * 初始化hbb插件的类加载器.
     * @param hbbFile hbbFile
     * @throws Exception
     */
    private void initLoader(File hbbFile, AarLoader aarLoader) throws Exception
    {
        URL[] urls = new URL[1];
        urls[0] = hbbFile.toURI().toURL();
        
        //在此指定hbbLoader的父加载器为aarLoader.
        ClassLoader parent = null;
        if (null == aarLoader)
        {
            parent = getClass().getClassLoader();
        }
        else
        {
            parent = aarLoader.getAarLoader();
        }
        
        URLClassLoader loader = new URLClassLoader(urls, parent);
        this.loader = loader;
    }
    
    /**
     * 解析hbb_**.xml文件.
     * @param hbbXmlInput hbb_**.xml
     * @throws Exception
     */
    private void parseHbbXml(InputStream hbbXmlInput) throws Exception
    {
        this.hbbConfig = new HbbConfig(hbbXmlInput);
    }

    /**
     * 加载hbb业务插件.
     * @throws Exception
     */
    private void loadHbb() throws Exception
    {
        
    }
    
    /**
     * 执行hbb中的业务逻辑.
     * @param args 要传递给业务插件的参数值列表,都是string类型.
     * @throws Exception 
     */
    public Object execHbb(String... args) throws Exception
    {
        ClassLoader hbbLoader = getLoader();
        HbbConfig config = getHbbConfig();
        String className = config.getHbbClassName();
        String methodName = config.getMethodName();
        String[] argTypes = config.getArgTypes();
        
        Class<?>[] argsClzz = null;
        if (!CollectionUtils.isEmpty(argTypes))
        {
            argsClzz = new Class<?>[argTypes.length];
            for (int i = 0; i < argTypes.length; i++)
            {
                argsClzz[i] = Class.forName(argTypes[i], true, hbbLoader);
            }
        }
        
        Class<?> clzz = Class.forName(className, true, hbbLoader);
        
        Object hbbInstant = clzz.newInstance();
        Method method = clzz.getDeclaredMethod(methodName, argsClzz);
        
        Object[] arguments = null;
        if (null != argsClzz && 0 < argsClzz.length)
        {
            arguments = new Object[argsClzz.length];
            for (int i = 0; i < arguments.length; i++)
            {
                arguments[i] = args[i];
            }
        }
        
        return method.invoke(hbbInstant, arguments);
    }

    public URLClassLoader getLoader()
    {
        return loader;
    }

    public HbbConfig getHbbConfig()
    {
        return hbbConfig;
    }
}
