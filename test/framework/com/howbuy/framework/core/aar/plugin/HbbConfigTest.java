package com.howbuy.framework.core.aar.plugin;

import java.io.File;
import java.io.FileInputStream;

import com.howbuy.framework.core.aar.plugin.HbbConfig;

public class HbbConfigTest
{

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        new HbbConfig(new FileInputStream(new File("temp/hbb_homo.xml")));
    }

}
