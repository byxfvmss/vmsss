package com.cjit.vms.taxdisk.aisino.single.util;  
  
import java.io.StringReader;  
import java.io.StringWriter;  
import javax.xml.bind.JAXBContext;  
import javax.xml.bind.JAXBException;  
import javax.xml.bind.Marshaller;  
import javax.xml.bind.Unmarshaller;  
  
import org.apache.commons.lang.StringUtils;  
  
/** 
 * 使用Jaxb2.0实现XML和Java对象的互相转换
 *  
 * 特别支持Root对象是List的情形. 
 *  
 * @author 
 */  
public class XmlUtil {  
    // 多线程安全的Context.  
    private JAXBContext jaxbContext;  
  
    /**
     * 所有需要序列化的Root对象的类型
     * @param types
     */
    public XmlUtil(Class types) {  
        try {  
            jaxbContext = JAXBContext.newInstance(types);  
        } catch (JAXBException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /**
     * java对象转Xml 
     * @param root
     * @param encoding 编码
     * @return
     */
    public String toXml(Object root, String encoding) {  
        try {  
            StringWriter writer = new StringWriter();  
            createMarshaller(encoding).marshal(root, writer);  
            return writer.toString();  
        } catch (JAXBException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * Xml转java对象
     */  
    @SuppressWarnings("unchecked")  
    public <T> T fromXml(String xml) {  
        try {  
            StringReader reader = new StringReader(xml);  
            return (T) createUnmarshaller().unmarshal(reader);  
        } catch (JAXBException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * Xml转java对象, 支持大小写敏感或不敏感. 
     */  
    @SuppressWarnings("unchecked")  
    public <T> T fromXml(String xml, boolean caseSensitive) {  
        try {  
            String fromXml = xml;  
            if (!caseSensitive)  
                fromXml = xml.toLowerCase();  
            StringReader reader = new StringReader(fromXml);  
            return (T) createUnmarshaller().unmarshal(reader);  
        } catch (JAXBException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * 创建Marshaller, 设定encoding(可为Null). 
     */  
    public Marshaller createMarshaller(String encoding) {  
        try {  
            Marshaller marshaller = jaxbContext.createMarshaller();  
  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
  
            if (StringUtils.isNotBlank(encoding)) {  
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);  
            }  
            return marshaller;  
        } catch (JAXBException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * 创建UnMarshaller. 
     */  
    public Unmarshaller createUnmarshaller() {  
        try {  
            return jaxbContext.createUnmarshaller();  
        } catch (JAXBException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
}  