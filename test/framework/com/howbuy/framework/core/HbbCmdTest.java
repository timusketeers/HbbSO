package com.howbuy.framework.core;

import com.howbuy.framework.SystemInitializer;
import com.howbuy.framework.core.HbbCmd;

public class HbbCmdTest
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        //1.初始化系统环境.
        new SystemInitializer("plugins", "log4j.properties").init();
        
        //2.执行某个hbb业务块的逻辑代码.
        Thread t = new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                HbbCmd.execHbb("jso-fds-clearing", "hbb_cleardisack");
            }
        });
        
        /**
         * 启动一个守护线程,aar包中的线程都会是这个线程的子线程，父线程销毁的时候，子线程随之销毁。我们以此来管理线程.
         * 
         * 测试的时候,需要关注一个场景，随着同一个业务模块多次被加载执行，需要关注是否产生了僵死的线程.
         * 
         * 比较担心aar中有启动线程的动作, 如果我们这里的线程作为父线程死亡的时候，我们期待aar中的子线程随之死亡.这个是个测试重点.
         */
        t.setDaemon(true);
        t.start();
    }

}
