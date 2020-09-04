package com.cjit.vms.taxdisk.single.model;

import org.jdom.Document;
import org.jdom.Element;


public class PrintMgrSet extends BaseDiskModel {
	/*
	*发票类型代码  是否必须：是

	*/
	private static final String fapiao_type_ch="fplxdm";
	/*
	*打印上边距  是否必须：是
	单位：毫米
	*/
	private static final String top_ch="top";
	/*
	*打印左边距  是否必须：是
	单位：毫米
	*/
	private static final String left_ch="left";
	private static final String filename="打印边距设置.xml";



	/*
	*发票类型代码 是否必须：是

	*/
	private String fapiaoType;
	/*
	*打印上边距 是否必须：是
	单位：毫米
	*/
	private String top;
	/*
	*打印左边距 是否必须：是
	单位：毫米
	*/
	private String left;
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}

	/*<?xml version="1.0" encoding="gbk"?>
	<business comment="页边距设置" id="YBJSZ">
	<body yylxdm="1">
	<input>
	<fplxdm>发票类型代码</fplxdm>
	<top>打印上边距</top>
	<left>打印左边距</left>
	</input>
	</body>
	</business>*/
	public String CreatePrintMgSetXML() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		Element input=createInputElement();
		addChildElementText(input, fapiao_type_ch, fapiaoType);
		addChildElementText(input, top_ch, top);
		addChildElementText(input, left_ch, left);
		elements.addContent(input);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,filename);
		System.out.println(outString);
		return outString;
	}
	
	public PrintMgrSet(String fapiaoType, String top, String left) {
		super();
		this.fapiaoType = fapiaoType;
		this.top = top;
		this.left = left;
	}
	public static void main(String[] args) throws Exception {
		String Stringxml="<?xml version=\"1.0\" encoding=\"gbk\"?>"+
		"<business comment=\"页边距设置\" id=\"YBJSZ\">"+
		"<body yylxdm=\"1\">"+
		"<output>"+
		"  <fplxdm>发票类型代码</fplxdm>"+
		"<returncode>返回代码</returncode>"+
		"<returnmsg>返回信息</returnmsg>"+
		"</output>"+
		"</body>"+
		"</business>";
		String data="fapiao_type_ch	, fapiaoType  , fplxdm	    , 发票类型代码		, 是  , ;"+
"return_code_ch	, returnCode  , returncode	, 返回代码		  ,  是,	 00000000成功，其它失败 ;"+
"return_msg_ch 	, returnMsg   , returnmsg	  , 返回信息		  ,  是, ;";
		String className="PrintMgrSetReturnXMl";
		new PrintMgrSet().CreatParseXMl(Stringxml, data, className);
	}
	public PrintMgrSet() {
		super();
		// TODO Auto-generated constructor stub
	}

}
