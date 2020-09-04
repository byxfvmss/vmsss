package com.cjit.vms.taxdisk.servlet.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.servlet.model.parseXml.BaseReturnXml;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;

public class BillPrint extends BaseModel{
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
	/**
	 * 开票 终端标识
	 */
	private String billTerminalFlag;
	private String fapiaoType;
	
	/**
	 * 发票代码
	 */
	private static final String  bill_code_ch="fpdm";
	/**
	 * 发票号码
	 */
	private static final String  bill_no_ch="fphm";
	/** 
	 * 打印类型 0：发票打印
	 */
	private static final String  print_type_ch="dylx";
	/**
	 * 打印方式 0：每次点击都弹出打印设置框
		1：只在第一次点击时弹出打印设置框

	 */
	private static final String  print_way_ch="dyfs";
	/**
	 *  发票类型代码
	 */
	private static final String  fapiao_type_ch="fplxdm";
	/**
	 * 开票终端标识
	 */
	protected static final String bill_terminal_flag="kpzdbs";
	
	public BillPrint(BillInfo  billInfo,VmsTaxKeyInfo taxKeyInfo) {
		super();
		this.billCode = billInfo.getBillCode();
		this.billNo = billInfo.getBillNo();
		this.printType = TaxSelvetUtil.Print_Type_0;
		this.printWay = TaxSelvetUtil.print_Way_1;
		this.billTerminalFlag=taxKeyInfo.getBilTerminalFlag();
		this.applyTypeCode=TaxSelvetUtil.apply_type_ch;
		this.Id=TaxSelvetUtil.id_print;
		this.comment=TaxSelvetUtil.comment_bill_print;
		this.fapiaoType="0".equals(billInfo.getFapiaoType())?"004":"007";
	}
	
	public BillPrint() {
		super();
		// TODO Auto-generated constructor stub
	}

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
		addChildElementText(elements,fapiao_type_ch,fapiaoType);
		addChildElementText(elements,bill_code_ch,billCode);
		addChildElementText(elements,bill_no_ch,billNo);
		addChildElementText(elements,print_type_ch,printType);
		addChildElementText(elements,print_way_ch,printWay);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,fileName);
		return outString;
	}

	/**
	 * @param paramSet
	 * @param fileName 输出文件 打印返回文件
	 * @throws Exception
	 */
	public void outBillPrintXmlFile(String paramSet,String fileName) throws Exception{
		
		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,fileName);
	}


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

	public String getBillTerminalFlag() {
		return billTerminalFlag;
	}

	public void setBillTerminalFlag(String billTerminalFlag) {
		this.billTerminalFlag = billTerminalFlag;
	}
	
	
}
