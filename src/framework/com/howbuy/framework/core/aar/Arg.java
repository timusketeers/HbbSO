package com.howbuy.framework.core.aar;

/**
 * 参数对应的封装.
 * @author li.zhang
 * 2014-8-27
 *
 */
public class Arg
{
    /** 参数类型. **/
    private String argType;
    
    /** 参数值. 其实通用情况下argValue应该是java.lang.Object类型, 这里做了简化处理  //TODO li.zhang这里可以考虑进一步实现. **/
    private String argValue;

    public String getArgType()
    {
        return argType;
    }

    public void setArgType(String argType)
    {
        this.argType = argType;
    }

    public String getArgValue()
    {
        return argValue;
    }

    public void setArgValue(String argValue)
    {
        this.argValue = argValue;
    }
}
