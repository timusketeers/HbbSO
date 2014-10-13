package com.howbuy.framework.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO相关的util类.
 * @author li.zhang
 * 2014-8-26
 *
 */
public final class IOUtils
{
    /**
     * 关闭流
     * @param input input
     */
    public static void closeStream(final Object stream)
    {
        if (null == stream)
        {
            return;
        }
        
        if (InputStream.class.isInstance(stream))
        {
            InputStream input = (InputStream)stream;
            closeInput(input);
        }
        else if (OutputStream.class.isInstance(stream))
        {
            OutputStream output = (OutputStream)stream;
            closeOutput(output);
        }
    }

    private static void closeInput(InputStream input)
    {
        if (null == input)
        {
            return;
        }
        
        try
        {
            input.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void closeOutput(OutputStream output)
    {
        if (null == output)
        {
            return;
        }
        
        try
        {
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
