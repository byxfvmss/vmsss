/**
 * 
 */
package com.cjit.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @author yulubin
 */
public class DocXmlUtil{

	public static Document fromStringToDocument(String s)
			throws DocumentException{
		return DocumentHelper.parseText(s);
	}

	public static String fromDocumentToString(Document d){
		return d.asXML();
	}

	public static Document fromFileToDocument(String fileName)
			throws DocumentException{
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new File(fileName));
		return document;
	}

	public static File fromDocumentToFile1(Document document, String fileName)
			throws Exception{
		File file = new File(fileName);
		if(file.exists()){
			file.delete();
		}
		XMLWriter output = new XMLWriter(new FileOutputStream(file));
		output.write(document);
		output.close();
		return file;
	}

	public static File fromDocumentToFile(Document document, String fileName,
			String encoding) throws Exception{
		File file = new File(fileName);
		if(file.exists()){
			file.delete();
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(encoding);
		format.setTrimText(false);// 保留属性值中存在的连续空格
		XMLWriter output = new XMLWriter(new FileOutputStream(file), format);
		output.setEscapeText(false);
		output.write(document);
		output.close();
		return file;
	}

	public static File fromStringToFile(String fileContent, String fileName,
			String encoding) throws Exception{
		Document document = DocXmlUtil.fromStringToDocument(fileContent);
		return DocXmlUtil.fromDocumentToFile(document, fileName, encoding);
	}

	public static void setValueByElementId(Document document, String id,
			String attName, String value) throws Exception{
		Element element = document.elementByID(id);
		if(element == null){
			return;
		}
		Attribute att = element.attribute(attName);
		att.setValue(value);
	}

	public static void setValueByElementId(String s, String id, String attName,
			String value) throws Exception{
		Document document = DocXmlUtil.fromStringToDocument(s);
		Element element = document.elementByID(id);
		if(element == null){
			return;
		}
		Attribute att = element.attribute(attName);
		att.setValue(value);
	}

	public static String getValueByElementId(Document document, String id,
			String attName){
		Element element = document.elementByID(id);
		if(element == null){
			return null;
		}
		Attribute att = element.attribute(attName);
		return att.getValue();
	}

	public static String getValueByElementId(String s, String id, String attName)
			throws Exception{
		Document document = DocXmlUtil.fromStringToDocument(s);
		Element element = document.elementByID(id);
		if(element == null){
			return null;
		}
		Attribute att = element.attribute(attName);
		return att.getValue();
	}

	public static void getAllElements(List elements, Element parent){
		elements.add(parent);
		for(Iterator i = parent.elementIterator(); i.hasNext();){
			Element e = (Element) i.next();
			getAllElements(elements, e);
		}
	}

	public static List getAllElements(Document document){
		List elements = new ArrayList();
		Element root = document.getRootElement();
		getAllElements(elements, root);
		return elements;
	}

	public static void main(String[] args){
		try{
			Document d = DocXmlUtil
					.fromFileToDocument("E:\\压力测试系统\\tree2.xml");
			List es = DocXmlUtil.getAllElements(d);
			for(int i = 0; i < es.size(); i++){
				Element e = (Element) es.get(i);
				Attribute idAtt = e.attribute("ID");
				if(idAtt != null){
					if("1".equals(idAtt.getValue())
							|| "3".equals(idAtt.getValue())
							|| "17".equals(idAtt.getValue())){
						Attribute curValueAtt = e.attribute("curValue");
						curValueAtt.setValue("yulubin");
					}
				}
			}
			System.out.println(DocXmlUtil.fromDocumentToString(d));
			DocXmlUtil
					.fromDocumentToFile(
							d,
							"E:\\压力测试系统\\abc.xml",
							CharacterEncoding.GB18030);
		}catch (Exception e){
		}
	}
}
