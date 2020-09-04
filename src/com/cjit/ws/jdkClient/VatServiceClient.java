
package com.cjit.ws.jdkClient;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

public class VatServiceClient {
	/*public static void main(String[] args) throws Exception {
		URL wsdlURL = new URL("http://10.3.40.10:7001/ls/services/VatService?wsdl");
		QName SERVICE_NAME = new QName("http://service.esb.soa.pub.ebao.com/", "VatService");
		QName VatServicePort = new QName("http://service.esb.soa.pub.ebao.com/", "VatServicePort");
		
		SAXReader saxReader = new SAXReader();  
		org.dom4j.Document document = saxReader.read(new File("F:\\request.xml"));
		String text = document.asXML();
		
		VatService_Service vatService = new VatService_Service(wsdlURL,SERVICE_NAME);
		VatServicePortType vatServicePortType = vatService.getVatServicePort();
		String aaString = vatServicePortType.vatService(text);
		System.out.println(aaString);
	}*/
	
	/*
     * 个险专属的方法，里面有些如命名空间或者调用方法个险若有修改，则需检查生成的客户端所有java类都需要修改
     */
    public String invoke(String path,String requestXml,String qName,String method) throws MalformedURLException {
        URL wsdlURL = new URL(path);
		QName SERVICE_NAME = new QName(qName, method);
		QName VatServicePort = new QName(qName, "VatServicePort");
		
		VatService_Service vatService = new VatService_Service(wsdlURL,SERVICE_NAME);
		VatServicePortType vatServicePortType = vatService.getVatServicePort(VatServicePort);
		String aaString = vatServicePortType.vatService(requestXml);
		return aaString;
	}
}
