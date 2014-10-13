package com.howbuy.framework.core.aar.plugin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.howbuy.framework.core.utils.CollectionUtils;
import com.howbuy.framework.core.utils.XmlReader;

/**
 * 表示一个hbbConfig,这个类封装了hbb_***.xml这个文件中的信息.
 * @author li.zhang
 * 2014-8-25
 *
 */
public class HbbConfig
{
    /** 插件编号. 唯一标识一个插件. **/
    private String moduleType;
    
    /** 插件版本. 默认1.0. **/
    private String moduleVersion;
    
    /** 代表hbb要执行的类的入口. **/
    private String hbbClassName;
    
    /** 代表hbbClassName这个类中要执行的方法名. **/
    private String methodName;
    
    /** 代表hbbClassName这个类中要执行的方法的参数类型列表. 每个成员存放的是类的全限定名.**/
    private String[] argTypes;
    
    /**
     * 构造方法
     * @param hbbJar hbbJar
     * @throws Exception 
     */
    public HbbConfig(InputStream hbbXmlInput) throws Exception
    {
        parse(hbbXmlInput);
    }
    
    private void parse(InputStream hbbXmlInput) throws Exception
    {
        String content = XmlReader.readXml(hbbXmlInput);
        Document document = DocumentHelper.parseText(content);
        Element hbbNode = document.getRootElement();
        
        Element modTypeNode = hbbNode.element("module-type");
        Element modVerNode = hbbNode.element("module-version");
        Element hbbNameNode = hbbNode.element("hbb-name");
        Element execMethodNode = hbbNode.element("exec-method");
        
        this.moduleType = modTypeNode.getTextTrim();
        this.moduleVersion = modVerNode.getTextTrim();
        this.hbbClassName = hbbNameNode.getTextTrim();
        
        parseMethodNode(execMethodNode);
    }

    /**
     * 解析method节点.
     * @param execMethodNode exec-method元素节点
     */
    private void parseMethodNode(Element execMethodNode)
    {
        Attribute methodNameAttr = execMethodNode.attribute("name");
        this.methodName = methodNameAttr.getValue();
        
        Element argsNode = execMethodNode.element("args");
        parseArgs(argsNode);
    }

    private void parseArgs(Element argsNode)
    {
        if (null == argsNode)
        {
            return;
        }
        
        Element sequenceNode = argsNode.element("sequence");
        parseSequence(sequenceNode);
    }

    private void parseSequence(Element sequenceNode)
    {
        if (null == sequenceNode)
        {
            return;
        }
        
        List<?> argTypeList = sequenceNode.elements("arg-type");
        if (CollectionUtils.isEmpty(argTypeList))
        {
            return;
        }
        
        String[] argTypes = null;
        List<Element> argList = new ArrayList<Element>();
        for (int i = 0; i < argTypeList.size(); i++)
        {
            Element argTypeNode = (Element)argTypeList.get(i);
            argList.add(argTypeNode);
        }
        
        argTypes = new String[argList.size()];
        for (int j = 0; j < argList.size(); j++)
        {
            argTypes[j] = argList.get(j).getTextTrim();
        }
        
        this.argTypes = argTypes;
    }

    public String getModuleType()
    {
        return moduleType;
    }

    public String getModuleVersion()
    {
        return moduleVersion;
    }

    public String getHbbClassName()
    {
        return hbbClassName;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public String[] getArgTypes()
    {
        return argTypes;
    }
}
