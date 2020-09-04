package com.cjit.ws;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
public class Client {
	    /**
	     * 通过genClient.bat文件生成的webservice客户端调用webservice服务的步骤如下：
	     * 1.创建service对象 
	     * 2.创建url对象 
	     * 3.创建call对象，
	     * 4.调用webservice的方法
	*/

	    public static void main(String[] args) {
	    	try {
	            // 1.创建service对象，通过axis自带的类创建
	            org.apache.axis.client.Service service = new org.apache.axis.client.Service();

	            // 2.创建url对象
	            String wsdlUrl = "http://10.0.22.161:7001/services/MinShengVATInvoice?wsdl";//请求服务的URL
	            URL url = new URL(wsdlUrl);//通过URL类的构造方法传入wsdlUrl地址创建URL对象

	            // 2.创建服务方法的调用者对象call，设置call对象的属性
	            Call call = (Call) service.createCall();
	            call.setTargetEndpointAddress(url);//给call对象设置请求的URL属性
	            call.setOperationName(new QName("http://f1print.lis.sinosoft.com", "dealData"));//给call对象设置调用方法名属性
	            call.addParameter("xml", XMLType.XSD_STRING, ParameterMode.IN);// 给call对象设置方法的参数名、参数类型、参数模式
	            call.setReturnType(XMLType.SOAP_STRING);// 设置调用方法的返回值类型

	            //4.通过invoke方法调用webservice
	            String name="ZhangSan";
	            String res = (String) call.invoke(new Object[] {Client.releaseXML()});//调用服务方法
	            System.out.println(res);
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (ServiceException e) {
	            e.printStackTrace();
	        } catch (RemoteException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static String releaseXML() {
			SAXReader reader = new SAXReader();
			Document document;
			String documentStr = null;
			try {
				document = reader.read(new File("D:/back1.xml"));
				documentStr = document.asXML();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			return documentStr;
		}

		public String request(String responseXml) {
			// TODO Auto-generated method stub
			return null;
		}
		
}
