package com.howbuy.framework.core.aar.plugin;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;

import com.howbuy.framework.core.aar.AarLoader;
import com.howbuy.framework.core.aar.plugin.HbbConfig;
import com.howbuy.framework.core.aar.plugin.HbbLoader;

public class HbbLoaderTest
{

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        //1. hbb_**.jar测试.
        HbbLoader hbbLoader = new HbbLoader(new File("temp/hbb_homo.jar"), null);
        
        HbbConfig config = hbbLoader.getHbbConfig();
        String className = config.getHbbClassName();
        String methodName = config.getMethodName();
        String[] argTypes = config.getArgTypes();
        Class<?>[] argsClzz = new Class<?>[argTypes.length];
        for (int i = 0; i < argTypes.length; i++)
        {
            argsClzz[i] = Class.forName(argTypes[i], true, hbbLoader.getLoader());
        }
        
        Class<?> clzz = Class.forName(className, true, hbbLoader.getLoader());
        
        Object hbbInstant = clzz.newInstance();
        Method method = clzz.getDeclaredMethod(methodName, argsClzz);
        method.invoke(hbbInstant, "zhangsan", "boy", 20);
        
        System.out.println("========================================================");
        
        //2.hbb_**.xml测试.
        URL[] aarLibUrls = new URL[1];
        aarLibUrls[0] = new File("temp/aarlib/hbb_homo.jar").toURI().toURL();
        AarLoader aarLoader = new AarLoader(aarLibUrls);
        HbbLoader hbbLoader02 = new HbbLoader(new File("temp/hbb_homo.xml"), aarLoader);
        HbbConfig config02 = hbbLoader02.getHbbConfig();
        String className02 = config02.getHbbClassName();
        String methodName02 = config02.getMethodName();
        String[] argTypes02 = config02.getArgTypes();
        Class<?>[] argsClzz02 = new Class<?>[argTypes02.length];
        for (int i = 0; i < argTypes02.length; i++)
        {
            argsClzz02[i] = Class.forName(argTypes02[i], true, hbbLoader02.getLoader());
        }
        
        Class<?> clzz02 = Class.forName(className02, true, hbbLoader02.getLoader());
        
        Object hbbInstant02 = clzz02.newInstance();
        Method method02 = clzz02.getDeclaredMethod(methodName02, argsClzz02);
        method02.invoke(hbbInstant02, "zhangsan", "boy", 20);
    }

}
