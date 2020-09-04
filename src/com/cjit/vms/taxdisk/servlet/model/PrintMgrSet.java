package com.cjit.vms.taxdisk.servlet.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
public class PrintMgrSet extends BaseModel {
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
	 * @return 创建 xml 字符串  输出文件
	 * @throws Exception
	 */
	public PrintMgrSet() {
		super();
	}

	public PrintMgrSet(String fapiaoType, String top, String left) {
		super();
		this.fapiaoType = fapiaoType;
		this.top = top;
		this.left = left;
		this.applyTypeCode=TaxSelvetUtil.apply_type_ch;
		this.comment=TaxSelvetUtil.comment_page_mgr;
		this.Id=TaxSelvetUtil.id_print_mgr;
		
	}
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
