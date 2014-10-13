package com.howbuy.framework.core.serialize.decode;

import java.util.ArrayList;
import java.util.List;

import com.howbuy.framework.core.serialize.decode.ObjectDecoder;
import com.howbuy.framework.core.serialize.encode.ObjectEncoder;
import com.howbuy.framework.core.serialize.encode.TestObject;

public class ObjectDecoderTest
{

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        // TODO Auto-generated method stub
        ObjectDecoder decoder = new ObjectDecoder();
        ObjectEncoder encoder = new ObjectEncoder();
        
        TestObject obj3 = new TestObject();
        obj3.setAge(22);
        obj3.setName("testObj");
        List<String> members = new ArrayList<String>();
        members.add("son");
        members.add("daughter");
        members.add("wife");
        members.add("parent");
        obj3.setMembers(members);
        TestObject rs = (TestObject)decoder.decode(encoder.encode(obj3));
        System.out.println(rs);
    }

}
