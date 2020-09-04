package com.cjit.vms.taxServer.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxServer.model.parseXMl.BillCancelReturnCancel;

/**
 * @author tom
 * 发票作废
 */
public class BillCancel  extends CurrentBill{
	/**
	 * 作废类型
	 */
	private static final String bill_cancel_type_ch="zflx";
	/**
	 * 发票代码
	 */
	private static final String bill_code_ch="fpdm";
	/**
	 * 发票号码
	 */
	private static final String bill_no_ch="fphm";
	/**
	 * 合计金额
	 */
	private static final String amt_sum_ch="hjje";
	private static final String bill_cancel_date_ch="hjje";
	/**
	 * 作废人
	 */
	private static final String cancel_people_ch="zfr";
	private static final String BillEmptyCancelXmlFile="发票空白作废.xml";
	private static final String BillCancelXmlFile="发票作废.xml";
	/**
	 *  作废类型
	 */
	private String billCancelType;
	/**
	 *  发票代码
	 */
	private String billCode;
	/**
	 * 发票号码
	 */
	private String billNo;
	/**
	 * 合计金额
	 */
	private String AmtSum;
	/**
	 * 作废人
	 */
	private String CancelPeople;
	public String getBillCancelType() {
		return billCancelType;
	}
	public String getBillCode() {
		return billCode;
	}
	public String getBillNo() {
		return billNo;
	}
	public String getAmtSum() {
		return AmtSum;
	}
	public String getCancelPeople() {
		return CancelPeople;
	}
	public void setBillCancelType(String billCancelType) {
		this.billCancelType = billCancelType;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public void setAmtSum(String amtSum) {
		AmtSum = amtSum;
	}
	public void setCancelPeople(String cancelPeople) {
		CancelPeople = cancelPeople;
	}
	/**
	 * @return 发票空白作废字符串 并输出xml
	 * 文件
	 * @throws Exception
	 */
	public String createBillCancelXml(String falg) throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		BillCancelDataAssemble(elements);//
		root.addContent(elements);
		String outString="";
		if(falg.endsWith("0")){
		 outString=CreateDocumentFormt(Doc, path_ch,BillEmptyCancelXmlFile);
		}else{
			outString=CreateDocumentFormt(Doc, path_ch,BillCancelXmlFile);
		}
		return outString;
	}
	/**
	 * @param elements 向body里装子元素
	 */
	public void BillCancelDataAssemble(Element elements){
		addChildElementText(elements,bill_terminal_flag,billTerminalFlag);
		addChildElementText(elements,fapiao_Type_code,fapiaoTypeCode);
		addChildElementText(elements,bill_cancel_type_ch,billCancelType);
		addChildElementText(elements,bill_code_ch,billCode);
		addChildElementText(elements,bill_no_ch,billNo);
		addChildElementText(elements,amt_sum_ch,AmtSum);
		addChildElementText(elements,cancel_people_ch,CancelPeople);
		
	}
	/**
	 * @param BillCancelXml
	 * @return    返回实体类 BillCancelReturnCancel 
	 * @throws Exception
	 */
	public BillCancelReturnCancel ParserBillCancelXml(String BillCancelXml) throws Exception{
		Document doc =StringToDocument(BillCancelXml);
		Element body=getBodyElement(doc);
		String returncode =body.getChildText(returncode_ch);
		String returnmsg=body.getChildText(returnmsg_ch);
		Element returndata=body.getChild(returndata_ch);
		String billNo=returndata.getChildText(bill_no_ch);
		String billCode=returndata.getChildText(bill_code_ch);
		String billCancelDate=returndata.getChildText(bill_cancel_date_ch);
		BillCancelReturnCancel result=new BillCancelReturnCancel();
		result.setReturncode(returncode);
		result.setReturnmsg(returnmsg);
		result.setBillNo(billNo);
		result.setBillCode(billCode);
		result.setBillCancelDate(billCancelDate);
		return result;
		
	}
	/**
	 * @param paramSet
	 * @param falg 是否是空白作废 empty 为空白作废
	 * @throws Exception 
	 */
	public void outBillCancelXmlFile(String paramSet,String falg) throws Exception{
		if(falg.equals("0")){
		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,BillEmptyCancelXmlFile);
		}else{
			CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,BillCancelXmlFile);
		}
	}
	/*<?xml version="1.0" encoding="gbk"?>
	<business id="10009" comment="发票作废">
	<body yylxdm="1">
	<returncode>返回代码</returncode>
	<returnmsg>返回信息</returnmsg>
	<returndata>
	<fpdm>发票代码</fpdm>
	<fphm>发票号码</fphm>
	<zfrq>作废日期</zfrq>
	</returndata>
	</body>
*/

}
