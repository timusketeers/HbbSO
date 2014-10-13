package com.howbuy.framework.core.aar;

import java.io.File;

import com.howbuy.framework.core.aar.StartupConfig;

public class StartupConfigTest
{

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        new StartupConfig(new File("temp/startup.xml"));
    }

}
