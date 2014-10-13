/***********************************************************************
 * Module:  ObjectEncoder.java
 * Author:  li.zhang
 * Purpose: Defines the Class ObjectEncoder
 ***********************************************************************/
package com.howbuy.framework.core.serialize.encode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.howbuy.framework.core.serialize.encode.ObjectEncoder;

/** 
 * 编码器
 * @author li.zhang
 */
public class ObjectEncoderTest
{
    public static void main(String[] args) throws Exception
    {
        //1.测试string类型.
        String str = "hello world";
        ObjectEncoder encoder = new ObjectEncoder();
        
        byte[] bytes = encoder.encode(str);
        for (int i = 0; i < bytes.length; i++)
        {
            System.out.print(bytes[i] + " ");
        }
        System.out.println();
        
        for (byte b : str.getBytes("utf-8"))
        {
            System.out.print(b + " ");
        }
        System.out.println();
        
        //2. 测试基本类型
        bytes = encoder.encode(115);
        for (byte b : bytes)
        {
            System.out.print(b + " ");
        }
        System.out.println();
        
        bytes = encoder.encode(false);
        for (byte b : bytes)
        {
            System.out.print(b + " ");
        }
        System.out.println();
        
        //3. 数组测试.
        System.out.println("数据测试================================");
        String[] strArray = {"zhangsan", "lisi", "wangwu"};
        bytes = encoder.encode(strArray);
        for (byte b : bytes)
        {
            System.out.print(b + " ");
        }
        System.out.println();
        
        //4. collection测试
        System.out.println("ArrayList测试================================");
        List<String> list = new ArrayList<String>();
        list.add("English");
        list.add("Chinese");
        list.add("Math");
        bytes = encoder.encode(list);
        for (byte b : bytes)
        {
            System.out.print(b + " ");
        }
        System.out.println();
        
        System.out.println("HashSet测试================================");
        Set<String> set = new HashSet<String>();
        set.add("animal");
        set.add("fruit");
        set.add("basketball");
        set.add("football");
        bytes = encoder.encode(set);
        for (byte b : bytes)
        {
            System.out.print(b + " ");
        }
        System.out.println();
        
        //5.普通对象测试
        System.out.println("Object测试================================");
        TestObject obj = new TestObject();
        obj.setAge(22);
        obj.setName("testObj");
        
        List<String> members = new ArrayList<String>();
        members.add("son");
        members.add("daughter");
        members.add("wife");
        members.add("parent");
        obj.setMembers(members);
        bytes = encoder.encode(obj);
        for (byte b : bytes)
        {
            System.out.print(b + " ");
        }
        System.out.println();
        
        System.out.println("===========================================");
        System.out.println(new ArrayList<String>().getClass().getName());
        System.out.println(new HashMap<String, Object>().getClass().getCanonicalName());
        System.out.println(new byte[10].getClass().getName());
        System.out.println(new Byte[10].getClass().getName());
    }
}

