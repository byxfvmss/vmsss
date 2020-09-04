package com.cjit.vms.taxdisk.single.model;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import cjit.crms.util.json.JsonUtil;

import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.parseXml.BillPrintReturnXML;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;

public class BillPrint extends BaseDiskModel {
	/*
	*纳税人识别号  是否必须：是

	*/
	private static final String tax_no_ch="nsrsbh";
	/*
	*税控盘编号  是否必须：是

	*/
	private static final String tax_disk_no_ch="skpbh";
	/*
	*税控盘口令  是否必须：是

	*/
	private static final String tax_disk_pwd_ch="skpkl";
	/*
	*税务数字证书密码  是否必须：是

	*/
	private static final String tax_cert_pwd_ch="keypwd";
	/*
	*发票类型代码  是否必须：是

	*/
	private static final String fapiao_type_ch="fplxdm";
	/*
	*发票代码  是否必须：是

	*/
	private static final String bill_code_ch="fpdm";
	/*
	*发票号码  是否必须：是
	单张打印时为8，批量打印时，起始号码+终止号码=16位
	*/
	private static final String bill_no_ch="fphm";
	/*
	*打印类型  是否必须：是
	0：发票打印，1：清单打印
	*/
	private static final String print_type_ch="dylx";
	/*
	*打印方式  是否必须：是
	0：批量打印 1：单张打印
	*/
	private static final String print_way_ch="dyfs";
	private static final String filename="发票打印.xml";



/*
*纳税人识别号 是否必须：是

*/
private String taxNo;
/*
*税控盘编号 是否必须：是

*/
private String taxDiskNo;
/*
*税控盘口令 是否必须：是

*/
private String taxDiskPwd;
/*
*税务数字证书密码 是否必须：是

*/
private String taxCertPwd;
/*
*发票类型代码 是否必须：是

*/
private String fapiaoType;
/*
*发票代码 是否必须：是

*/
private String billCode;
/*
*发票号码 是否必须：是
单张打印时为8，批量打印时，起始号码+终止号码=16位
*/
private String billNo;
/*
*打印类型 是否必须：是
0：发票打印，1：清单打印
*/
private String printType;
/*
*打印方式 是否必须：是
0：批量打印 1：单张打印
*/
private String printWay;
public String getTaxNo() {
	return taxNo;
}
public void setTaxNo(String taxNo) {
	this.taxNo = taxNo;
}
public String getTaxDiskNo() {
	return taxDiskNo;
}
public void setTaxDiskNo(String taxDiskNo) {
	this.taxDiskNo = taxDiskNo;
}
public String getTaxDiskPwd() {
	return taxDiskPwd;
}
public void setTaxDiskPwd(String taxDiskPwd) {
	this.taxDiskPwd = taxDiskPwd;
}
public String getTaxCertPwd() {
	return taxCertPwd;
}
public void setTaxCertPwd(String taxCertPwd) {
	this.taxCertPwd = taxCertPwd;
}
public String getFapiaoType() {
	return fapiaoType;
}
public void setFapiaoType(String fapiaoType) {
	this.fapiaoType = fapiaoType;
}
public String getBillCode() {
	return billCode;
}
public void setBillCode(String billCode) {
	this.billCode = billCode;
}
public String getBillNo() {
	return billNo;
}
public void setBillNo(String billNo) {
	this.billNo = billNo;
}
public String getPrintType() {
	return printType;
}
public void setPrintType(String printType) {
	this.printType = printType;
}
public String getPrintWay() {
	return printWay;
}
public void setPrintWay(String printWay) {
	this.printWay = printWay;
}

public String createBillPrintXml() throws Exception{
	Element root =CreateDoocumentHeard();
	Document Doc = new Document(root);
	Element elements =CreateBodyElement();
	Element input=createInputElement();
	addChildElementText(input, tax_no_ch, taxNo);
	addChildElementText(input, tax_disk_no_ch, taxDiskNo);
	addChildElementText(input, tax_disk_pwd_ch, taxDiskPwd);
	addChildElementText(input, tax_cert_pwd_ch, taxCertPwd);
	addChildElementText(input, fapiao_type_ch, fapiaoType);
	addChildElementText(input, bill_code_ch, billCode);
	addChildElementText(input, bill_no_ch, billNo);
	addChildElementText(input, print_type_ch, printType);
	addChildElementText(input, print_way_ch, printWay);
	elements.addContent(input);
	root.addContent(elements);
	String outString=CreateDocumentFormt(Doc, path_ch,filename);
	System.out.println(outString);
	return outString;
}

//构造函数
public BillPrint(String taxNo, String taxDiskNo, String taxDiskPwd,
		String taxCertPwd, String fapiaoType, String billCode, String billNo,
		String printType, String printWay) {
	super();
	this.taxNo = taxNo;
	this.taxDiskNo = taxDiskNo;
	this.taxDiskPwd = taxDiskPwd;
	this.taxCertPwd = taxCertPwd;
	this.fapiaoType = fapiaoType;
	this.billCode = billCode;
	this.billNo = billNo;
	this.printType = printType;
	this.printWay = printWay;
}
public BillPrint(BillInfo bill,TaxDiskInfo disk,String printType) {
	super();
	this.taxNo = disk.getTaxpayerNo();
	this.taxDiskNo = disk.getTaxDiskNo();;
	this.taxDiskPwd = disk.getTaxDiskPsw();
	this.taxCertPwd = disk.getTaxCertPsw();
	this.fapiaoType = bill.getFapiaoType()=="0"?"004":"007";
	this.billCode =  bill.getBillCode();
	this.billNo =bill.getBillNo();
	this.printType =  TaxDiskUtil.print_Type_0;
	this.printWay =TaxDiskUtil.print_Way_1; 
	this.applyTypeCode=TaxDiskUtil.Application_type_code_1;
	this.Id=TaxDiskUtil.id_bill_print;
	this.comment=TaxDiskUtil.comment_bill_print;
}

public static void main(String[] args) throws Exception {
		String StringXml="<?xml version=\"1.0\" encoding=\"gbk\"?>"+
		"<business comment=\"发票打印\" id=\"FPDY\">"+
		"<body yylxdm=\"1\">"+
		"<output>"+
		"<dyfpfs count=\"1\">"+
		"<group xh=\"1\">"+
		"<fpdm>发票类型代码</fpdm>"+
		"<fphm>发票号码</fphm>"+
		"</group>"+
		"</dyfpfs>"+
		"<returncode>00000000</returncode>"+
		"<returnmsg>成功</returnmsg>"+
		"</output>"+
		"</body>"+
		"</business>";
		
System.out.println(JsonUtil.toJsonString(new BillPrintReturnXML(StringXml)));
	

}


public BillPrint() {
	super();
	// TODO Auto-generated constructor stub
}

}
