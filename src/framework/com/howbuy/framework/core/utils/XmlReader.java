package com.howbuy.framework.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class XmlReader
{
    /**
     * 从xml文件中读取出字符串.
     * @param xmlFile
     * @return
     */
    public static String readXml(File xmlFile)
    {
        String content = null;
        StringBuilder builder = new StringBuilder();
        FileInputStream input = null;
        try
        {
            int i = 0;
            input = new FileInputStream(xmlFile);
            byte[] buffer = new byte[1024 * 8 * 4];//4k字节
            while (-1 != (i = input.read(buffer))) 
            {  
                builder.append(new String(buffer, 0, i, "utf-8"));
            }  
            
            content = builder.toString();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeStream(input);
        }
        
        return content;
    }
    
    /**
     * 从输入流中读取出字符串.
     * @param xmlInput
     * @return
     */
    public static String readXml(InputStream xmlInput)
    {
        String content = null;
        StringBuilder builder = new StringBuilder();
        try
        {
            int i = 0;
            byte[] buffer = new byte[1024 * 8 * 4];//4k字节
            while (-1 != (i = xmlInput.read(buffer))) 
            {  
                builder.append(new String(buffer, 0, i, "utf-8"));
            }  
            
            content = builder.toString();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeStream(xmlInput);
        }
        
        return content;
    }
}
