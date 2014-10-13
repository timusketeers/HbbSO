package com.howbuy.framework.core.serialize.encode;

import java.util.List;

public class TestObject
{
    private int age;
    
    private boolean ok;
    
    private String name;
    
    private String nickName;
    
    private String[] family = null;
    
    private List<String> members;

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<String> getMembers()
    {
        return members;
    }

    public void setMembers(List<String> members)
    {
        this.members = members;
    }

    public boolean isOk()
    {
        return ok;
    }

    public void setOk(boolean ok)
    {
        this.ok = ok;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String[] getFamily()
    {
        return family;
    }

    public void setFamily(String[] family)
    {
        this.family = family;
    }
}