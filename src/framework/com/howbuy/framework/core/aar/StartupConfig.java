package com.howbuy.framework.core.aar;

import java.io.File;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.howbuy.framework.core.utils.CollectionUtils;
import com.howbuy.framework.core.utils.XmlReader;

/**
 * 表示一个startupConfig,这个类封装了aar包的startup.xml这个文件中的信息.
 * @author li.zhang
 * 2014-8-25
 *
 */
public class StartupConfig
{
    /** aar包启动时的初始化类. **/
    private String initClass;
    
    /** 初始化类中的执行方法. **/
    private String methodName;
    
    /** 参数列表封装. **/
    private Arg[] args;
    
    /**
     * 构造方法
     * @param hbbJar hbbJar
     * @throws Exception 
     */
    public StartupConfig(File startupXmlFile) throws Exception
    {
        parse(startupXmlFile);
    }
    
    /**
     * 解析startup.xml文件.
     * @param startupXmlFile
     * @throws Exception
     */
    private void parse(File startupXmlFile) throws Exception
    {
        String content = XmlReader.readXml(startupXmlFile);
        Document document = DocumentHelper.parseText(content);
        Element startupNode = document.getRootElement();
        Element initClassNode = startupNode.element("aar-init-class");
        Element execMethodNode = startupNode.element("exec-method");
        
        this.initClass = initClassNode.getTextTrim();

        parseExecMethodNode(execMethodNode);
    }

    /**
     * 解析exec-method节点.
     * @param execMethodNode
     */
    private void parseExecMethodNode(Element execMethodNode)
    {
        Attribute methodNameAttr = execMethodNode.attribute("name");
        this.methodName = methodNameAttr.getValue();
        
        //解析args节点.
        Element argsNode = execMethodNode.element("args");
        parseArgs(argsNode);
    }

    /**
     * 解析args节点.
     * @param argsNode
     */
    private void parseArgs(Element argsNode)
    {
        if (null == argsNode)
        {
            return;
        }
        Element sequenceNode = argsNode.element("sequence");
        parseSequence(sequenceNode);
    }

    /**
     * 解析sequenceNode节点.
     * @param sequenceNode
     */
    private void parseSequence(Element sequenceNode)
    {
        List<?> argNodeList = sequenceNode.elements("arg");
        if (CollectionUtils.isEmpty(argNodeList))
        {
            return;
        }
        
        Arg[] argArray = new Arg[argNodeList.size()];
        for (int i = 0; i < argNodeList.size(); i++)
        {
            argArray[i] = new Arg();
            Element argNode = (Element)argNodeList.get(i);
            parseArg(argNode, argArray[i]);
        }
        
        this.args = argArray;
    }

    /**
     * 解析arg节点.
     * @param argNode
     * @param arg 参数封装.
     */
    private void parseArg(Element argNode, Arg arg)
    {
        Element argTypeNode = argNode.element("arg-type");
        Element argValueNode = argNode.element("arg-value");
        
        arg.setArgType(argTypeNode.getTextTrim());
        arg.setArgValue(argValueNode.getTextTrim());
    }
    
    public String getInitClass()
    {
        return initClass;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public Arg[] getArgs()
    {
        return args;
    }
}
