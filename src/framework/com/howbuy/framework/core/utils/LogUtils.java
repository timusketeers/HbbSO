package com.howbuy.framework.core.utils;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import com.howbuy.framework.core.constant.Constants;

public class LogUtils
{
    /**
     * 打印出异常堆栈
     * @param out
     * @param e
     */
    public static void printStackTrace(PrintStream out, Throwable e)
    {
        StringBuilder message = new StringBuilder();
        message.append(e.toString()).append(System.getProperty(Constants.LINE_SEPARATOR));
        
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++)
        {
            message.append("\tat " + trace[i]).append(System.getProperty(Constants.LINE_SEPARATOR));
        }
        
        synchronized (out)
        {
            out.println(message.toString());
        }
    }
    
    /**
     * 打印出异常堆栈
     * @param log 
     * @param e
     */
    public static void printStackTrace(Logger logger, Throwable e)
    {
        StringBuilder message = new StringBuilder();
        message.append(e.toString()).append(System.getProperty(Constants.LINE_SEPARATOR));
        
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++)
        {
            message.append("\tat " + trace[i]).append(System.getProperty(Constants.LINE_SEPARATOR));
        }
        
        synchronized (logger)
        {
            logger.error(message.toString());
        }
    }
}
