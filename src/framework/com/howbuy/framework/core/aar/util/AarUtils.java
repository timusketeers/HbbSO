package com.howbuy.framework.core.aar.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class AarUtils
{
    /**
     * 将aarFile解压到toDir中.
     * @param aarFile
     * @param toDir
     * @throws Exception
     */
    public static void unAar(File aarFile, File toDir) throws Exception
    {  
        ZipFile aar = new ZipFile(aarFile);  
        Enumeration<? extends ZipEntry> entries = aar.entries();  
        while (entries.hasMoreElements()) 
        {  
            ZipEntry entry = entries.nextElement();  
            if (entry.isDirectory())
            {
                continue;
            }
            
            InputStream in = aar.getInputStream(entry);  
            File file = new File(toDir, entry.getName());
            if (!file.getParentFile().exists())
            {
                if (!file.getParentFile().mkdirs())
                {
                    throw new IOException("mkdirs failed to create " + file.getParentFile().toString());  
                }
            }

            int i = 0;
            OutputStream out = new FileOutputStream(file);  
            byte[] buffer = new byte[1024 * 8 * 8];//8k字节
            while (-1 != (i = in.read(buffer))) 
            {  
                out.write(buffer, 0, i);  
            }  
            
            out.close();  
            in.close();
        }  
        
        aar.close();  
    }  
    
    /**
     * 递归删除某个文件或者文件夹.
     * @param file 代表文件或者文件夹
     */
    public static void deleteFile(File file)
    {
        if (!file.isDirectory())
        {
            file.delete();
            return;
        }
        
        File[] childen = file.listFiles();
        if (null == childen || 0 == childen.length)
        {
            file.delete();
            return;
        }
        
        //先删除所有子目录或子文件.
        for (int i = 0; i < childen.length; i++)
        {
            deleteFile(childen[i]);
        }
        
        // 再删除父文件.
        deleteFile(file);
    }
}
