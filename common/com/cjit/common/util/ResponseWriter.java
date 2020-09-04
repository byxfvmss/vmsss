/**
 * 
 */
package com.cjit.common.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

/**
 * @author��yulubin
 * @time��2008-10-31 ����11:10:59
 * @description��
 */
public class ResponseWriter{

	/**
	 * ajax��Ӧ����xml��ʽд�����
	 * @param content
	 * @throws Exception
	 */
	public static void writeXml(String content) throws Exception{
		write(content, "xml");
	}

	/**
	 * ajax��Ӧ����plain��ʽд�����
	 * @param content
	 * @throws Exception
	 */
	public static void writePlain(String content) throws Exception{
		write(content, "plain");
	}

	/**
	 * ajax��Ӧ�Ĺ��÷���������ָ����ʽд�����
	 * @param content
	 * @param type
	 * @throws Exception
	 */
	public static void write(String content, String type) throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/" + type + "; charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter out = response.getWriter();
		out.write(content);
		out.close();
	}
}
