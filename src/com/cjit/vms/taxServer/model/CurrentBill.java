package com.cjit.vms.taxServer.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxServer.model.parseXMl.CurrentReturnXml;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;

public class CurrentBill extends BaseModel{
	/**
	 * 开票终端标识
	 */
	protected static final String bill_terminal_flag="kpzdbs";
	/**
	 * 当前发票代码
	 */
	private static final String curr_bill_code = "dqfpdm";
	/**
	 * 当前前发票号码
	 */
	private static final String curr_bill_no = "dqfphm";
	/**
	 * 发票类型代码
	 */
	public static final String fapiao_Type_code="fplxdm";
	private static final String paramXmlFile="当前未开票号码.xml";
	/**
	 * 开票终端标识
	 */
	protected String billTerminalFlag;
	/**
	 * 发票类型代码
	 */
	protected String fapiaoTypeCode;
	public String getBillTerminalFlag() {
		return billTerminalFlag;
	}
	public String getFapiaoTypeCode() {
		return fapiaoTypeCode;
	}
	public void setBillTerminalFlag(String billTerminalFlag) {
		this.billTerminalFlag = billTerminalFlag;
	}
	public void setFapiaoTypeCode(String fapiaoTypeCode) {
		this.fapiaoTypeCode = fapiaoTypeCode;
	}
/**
 * @return 创建xml 文件 返回xml字符串
 * @throws Exception
 */
public String createCurrentBillXml() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		addChildElementText(elements,bill_terminal_flag,billTerminalFlag);
		addChildElementText(elements,fapiao_Type_code,fapiaoTypeCode);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,paramXmlFile);
		return outString;
	}
	
/**
 * @param paramSetXml
 * @return 将返回的xml 字符串转化为实体类 CurrentReturnXml 
 * @throws Exception
 */
public CurrentReturnXml ParserCurrentBillXml(String paramSetXml) throws Exception{
	Document doc =StringToDocument(paramSetXml);
	Element body=getBodyElement(doc);
	String returncode =body.getChildText(TaxSelvetUtil.return_code_ch);
	String returnmsg=body.getChildText(TaxSelvetUtil.return_Msg_ch);
	Element returndata=body.getChild(TaxSelvetUtil.return_data_ch);
	CurrentReturnXml result=new CurrentReturnXml();
	if(returncode.equals("0")){
	String billNo=returndata.getChildText(curr_bill_no);
	String billCode=returndata.getChildText(curr_bill_code);
	result.setBillNo(billNo);
	result.setBillCode(billCode);
	}
	result.setReturncode(returncode);
	result.setReturnmsg(returnmsg);
	return result;
	
}
/**
 * @param paramSet
 * @throws Exception 输出 当前票号的xml 文件
 */
public void outCurrentBillXmlFile(String paramSet) throws Exception{
	
	CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,paramXmlFile);
}

}
