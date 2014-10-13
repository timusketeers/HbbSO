package com.howbuy.framework.core.aar;

import java.io.File;

import com.howbuy.framework.core.aar.AarResolver;

public class AarResolverTest
{

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        new AarResolver(new File("plugins/fds-online.aar"), true, true);
    }
}
