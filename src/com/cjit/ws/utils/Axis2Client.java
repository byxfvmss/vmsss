package com.cjit.ws.utils;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;


public class Axis2Client {

	/*
	 * axis2方式调用服务端方法
	 * @param path,webservice请求地址
	 * @param requestXml 请求报文
	 * @param qName 命名空间
	 * @param method 调用服务端的方法
	 * 
	 */
	public String invokeRPCClient(String path,String requestXml,String qName,String method) throws AxisFault {
		// 使用RPC方式调用WebService
		RPCServiceClient serviceClient = new RPCServiceClient();
		Options options = serviceClient.getOptions();
		// 指定调用WebService的URL
		EndpointReference targetEPR = new EndpointReference(path);
		options.setTo(targetEPR);
		
		// 调用方法的参数值
		Object[] entryArgs = new Object[] {requestXml};
		// 调用方法返回值的数据类型的Class对象
		Class[] classes = new Class[] {String.class};
		// 调用方法名及WSDL文件的命名空间
		// 命名空间是http://localhost:8080/axis2/services/CalculateService?wsdl中wsdl:definitions标签targetNamespace属性
		QName opName = new QName(qName, method);
		// 执行方法获取返回值
		// 没有返回值的方法使用serviceClient.invokeRobust(opName, entryArgs)
		String responseXml = serviceClient.invokeBlocking(opName, entryArgs, classes)[0].toString();
		
		return responseXml;
	}
}
