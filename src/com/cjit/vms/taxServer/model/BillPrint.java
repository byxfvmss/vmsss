package com.cjit.vms.taxServer.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxServer.model.parseXMl.BaseReturnXml;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;

public class BillPrint extends CurrentBill{
	/**
	 * @param fileName
	 * @return 输出文件  返回xml 文件
	 * @throws Exception
	 */
	public String createPrintBillXml(String fileName) throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		addChildElementText(elements,bill_terminal_flag,billTerminalFlag);
		addChildElementText(elements,fapiao_Type_code,fapiaoTypeCode);
		addChildElementText(elements,bill_code_ch,billCode);
		addChildElementText(elements,bill_no_ch,billNo);
		addChildElementText(elements,print_type_ch,printType);
		addChildElementText(elements,print_way_ch,printWay);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,fileName);
		return outString;
	}
	/**
	 * @param billPrintXml
	 * @return   打印数据模型转化为实体类
	 * @throws Exception
	 */
	public  BaseReturnXml ParsertBillPrintXml(String billPrintXml) throws Exception{
		Document doc =StringToDocument(billPrintXml);
		Element body=getBodyElement(doc);
		String returncode =body.getChildText(TaxSelvetUtil.return_code_ch);
		String returnmsg=body.getChildText(TaxSelvetUtil.return_Msg_ch);
		
		BaseReturnXml result=new BaseReturnXml();
		result.setReturncode(returncode);
		result.setReturnmsg(returnmsg);
		
		return result;
		}
	/**
	 * @param paramSet
	 * @param fileName 输出文件 打印返回文件
	 * @throws Exception
	 */
	public void outBillPrintXmlFile(String paramSet,String fileName) throws Exception{
		
		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,fileName);
	}

	/**
	 * 发票代码
	 */
	private String billCode;
	/**
	 * 发票号码
	 */
	private String billNo;
	/**
	 * 打印类型
	 */
	private String printType;
	/**
	 * 打印方式
	 */
	private String printWay;
	
	private static final String  bill_code_ch="fpdm";
	private static final String  bill_no_ch="fphm";
	private static final String  print_type_ch="dylx";
	private static final String  print_way_ch="dyfs";
	public String getBillCode() {
		return billCode;
	}
	public String getBillNo() {
		return billNo;
	}
	public String getPrintType() {
		return printType;
	}
	public String getPrintWay() {
		return printWay;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public void setPrintType(String printType) {
		this.printType = printType;
	}
	public void setPrintWay(String printWay) {
		this.printWay = printWay;
	}
	
	
}
