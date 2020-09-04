/**
 * XmlUtil
 */
package com.cjit.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
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
 * @author huboA
 */
public class XmlUtil{

	/**
	 * 通过URL解析xml
	 * @param url
	 * @return
	 * @throws DocumentException
	 */
	// TODO:(fwy)解析反馈文件出错
	public static Document parse(URL url) throws DocumentException{
		SAXReader reader = new SAXReader();
		reader.setEncoding(CharacterEncoding.GB18030);
		Document document = reader.read(url);
		return document;
	}

	/**
	 * 通过File解析xml
	 * @param file
	 * @return
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	public static Document parse(File file) throws DocumentException,
			MalformedURLException{
		return parse(file.toURL());
	}

	/**
	 * 通过InputStream解析xml
	 * @param inputStream
	 * @return
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	public static Document parse(InputStream inputStream)
			throws DocumentException, MalformedURLException{
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		return document;
	}

	/**
	 * 生成一个xml
	 * @param document
	 * @param file
	 * @throws IOException
	 */
	public static void write(Document document, String file) throws IOException{
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
		writer.write(document);
		writer.close();
	}

	/**
	 * 根据名称得到所有Element
	 * @param document
	 * @param findElement
	 * @return
	 * @throws DocumentException
	 */
	public static List getElements(Document document, String findElement)
			throws DocumentException{
		Element root = document.getRootElement();
		return root.elements(findElement);
	}

	/**
	 * 根据名称得到单个Element
	 * @param document
	 * @param findElement
	 * @return
	 * @throws DocumentException
	 */
	public static Element getElement(Document document, String findElement)
			throws DocumentException{
		Element root = document.getRootElement();
		return root.element(findElement);
	}

	/**
	 * 根据名称得到单个Element的所有Attribute
	 * @param document
	 * @param findElement
	 * @return
	 * @throws DocumentException
	 */
	public static List getAttributes(Document document, String findElement)
			throws DocumentException{
		Element elem = getElement(document, findElement);
		return elem.attributes();
	}

	/**
	 * 根据名称得到单个Element的单个Attribute
	 * @param document
	 * @param findElement
	 * @param findAttribute
	 * @return
	 * @throws DocumentException
	 */
	public static Attribute getAttribute(Document document, String findElement,
			String findAttribute) throws DocumentException{
		Element elem = getElement(document, findElement);
		return elem.attribute(findAttribute);
	}

	/**
	 * 递归反射，获取方法调用的最后结果 只适用于不需要参数的方法的调用 可以无限级调用
	 * @param obj 对象
	 * @param methodName 方法名
	 * @return 方法调用的结果
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @author huangshiqiang
	 * @date 2007-10-25
	 * @comment 飞月并不是想象中的那么简单，反射也如此
	 */
	public static Object getMethodResult(Object obj, String methodName)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException{
		String name;
		if(methodName.indexOf(".") > 0){
			name = methodName.substring(0, methodName.indexOf("."));
			methodName = methodName.substring(methodName.indexOf(".") + 1);
			Method method = obj.getClass().getDeclaredMethod(name, null);
			obj = method.invoke(obj, null);
			obj = getMethodResult(obj, methodName);
		}else{
			Method method = obj.getClass().getDeclaredMethod(methodName, null);
			obj = method.invoke(obj, null);
		}
		return obj;
	}
	
	/**
	 * 校验xml格式是否正确
	 * @param strXML 字符串类型的xml格式数据
	 * @return
	 */
	public static boolean validateXML(String strXML){
		try{
			Document rootDocument = DocumentHelper.parseText(strXML);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
