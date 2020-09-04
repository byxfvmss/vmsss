package com.cjit.vms.taxServer.model;

import org.jdom.Document;
import org.jdom.Element;
public class PrintMgrSet extends BaseModel {
	/*<?xml version="1.0" encoding="gbk"?>
	<business id="20003" comment="打印页边距设置">
	<body yylxdm="1">
	<fplxdm>发票类型代码</fplxdm>
	<top>打印上边距</top>
	<left>打印左边距</left>
	</body>
	</business>*/
	
	/**
	 * @return 创建 xml 字符串  输出文件
	 * @throws Exception
	 */
	public String createPrintMgrXML() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		addChildElementText(elements,top_ch,top);
		addChildElementText(elements,fapiao_type_ch,fapiaoType);
		addChildElementText(elements,left_ch,left);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,printXml);
		return outString;
	}
	
	public void outBillPrintXmlFile(String paramSet) throws Exception{
		
		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,printXml);
	}

	
	private static final String  fapiao_type_ch="fplxdm";
	private static final String  top_ch="top";
	private static final String  printXml="打印边距设置.xml";
	private static final String  left_ch="left";
	/**
	 * 发票类型代码
	 */
	private String fapiaoType;
	/**
	 * 打印上边距
	 */
	private String top;
	/**
	 * 打印左边距
	 */
	private String left;
	/**
	 * @return 发票类型代码
	 */
	public String getFapiaoType() {
		return fapiaoType;
	}
	/**
	 * @return 打印上边距
	 */
	public String getTop() {
		return top;
	}
	/**
	 * @return 打印左边距
	 */
	public String getLeft() {
		return left;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	
	
}
