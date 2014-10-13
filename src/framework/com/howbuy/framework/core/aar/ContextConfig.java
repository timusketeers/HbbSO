package com.howbuy.framework.core.aar;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.howbuy.framework.core.utils.CollectionUtils;
import com.howbuy.framework.core.utils.StringUtils;
import com.howbuy.framework.core.utils.XmlReader;

/**
 * ContextConfig对应META-INF/context.xml文件中的配置.
 * @author li.zhang
 * 2014-8-26
 */
public final class ContextConfig
{
    /** id属性的属性名.**/
    private static final String ID_ATTR_NAME = "id";
    
    /** key为id, value为对应的aar-bean节点. **/
    private Map<String, Element> configMap = new HashMap<String, Element>();
    
    /**
     * 构造方法.
     * @param contextXmlFile META-INFO/context.xml文件
     * @throws Exception 
     */
    public ContextConfig(File contextXmlFile) throws Exception
    {
        String xmlContent = XmlReader.readXml(contextXmlFile);
        parse(xmlContent);
    }

    /**
     * 解析context.xml文件.
     * @param xmlContent META-INFO/context.xml文件的内容.
     * @throws Exception 
     */
    private void parse(String xmlContent) throws Exception
    {
        //使用dom4j解析xml.
        Document document = DocumentHelper.parseText(xmlContent);
        Element ctxNode = document.getRootElement();
        List<?> elements = ctxNode.elements();
        if (CollectionUtils.isEmpty(elements))
        {
            return;
        }
        
        for (int i = 0 ; i < elements.size(); i++)
        {
            Element aarBeanNode = (Element)elements.get(i);
            parseAarBeanNode(aarBeanNode);
        }
    }
    
    /**
     * 解析context.xml文件中的aar-bean这个节点.
     * @param aarBeanNode
     * @throws Exception 
     */
    private void parseAarBeanNode(Element aarBeanNode) throws Exception
    {
        Map<String, Element> maps = new HashMap<String, Element>();
        
        //解析aar-bean节点的各个属性.
        Map<String, String> attrsMap = new HashMap<String, String>();
        List<?> attrs = aarBeanNode.attributes();
        if (null != attrs)
        {
            for (int i = 0; i < attrs.size(); i++)
            {
                Attribute attr = (Attribute)attrs.get(i);
                attrsMap.put(attr.getName(), attr.getText());
            }
        }
        
        String idAttrVal = attrsMap.get(ContextConfig.ID_ATTR_NAME);
        if (StringUtils.isEmpty(idAttrVal))
        {
            return;
        }
        
        maps.putAll(this.configMap);
        maps.put(idAttrVal, aarBeanNode);
        
        configMap = maps;
    }
}
