package com.howbuy.framework.core;

import com.howbuy.framework.core.aar.AarResolver;
import com.howbuy.framework.core.registry.AarRegistry;

/**
 * hbb业务模块执行器.
 * @author li.zhang
 * 2014-8-28
 *
 */
public final class HbbCmd
{
    /**
     * 执行aar应用包下的hbb业务插件.
     * @param aar
     * @param hbb
     * @param args
     */
    public static Object execHbb(String aar, String hbb, String... args)
    {
        Object execRslt = null;
        AarRegistry registry = AarRegistry.getInstance();
        AarResolver resolver = registry.queryAarResolver(aar);
        if (null == resolver)
        {
            return execRslt;
        }
        
        try
        {
            execRslt = resolver.execHbb(hbb, args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return execRslt;
    }
}
