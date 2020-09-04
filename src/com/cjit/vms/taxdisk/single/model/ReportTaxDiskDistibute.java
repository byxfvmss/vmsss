package com.cjit.vms.taxdisk.single.model;

import org.jdom.Document;
import org.jdom.Element;


/**
 * @author tom
 *
 */
public class ReportTaxDiskDistibute  extends BaseDiskModel{
public static void main(String[] args) {
	String data="tax_no_ch	                ,    taxNo             ,nsrsbh	 ,纳税人识别号 , 	 是,  ;"+
	"report_tax_disk_no_ch	    ,    reportTaxDiskNo   ,bspbh	   ,报税盘编号	  , 	是,  ;"+
	"report_tax_disk_pwd_ch	  ,    reportTaxDiskPwd  ,bspkl	   ,报税盘口令  	, 	是,  ;"+
	"tax_disk_no_ch	          ,    taxDiskNo         ,skpbh	   ,税控盘编号	  ,	  是,  ;"+
	"tax_disk_pwd_ch	          ,    taxDiskPwd        ,skpkl	   ,税控盘口令	  , 	是,  ;"+
	"data_cert_pwd_ch	        ,    dataCertPwd       ,keypwd	 ,数字证书密码 ,	 是,  ;"+
	"fapiao_type_ch	          ,    fapiaoType        ,fplxdm	 ,发票类型代码 ,	 是,  ;"+
	"bill_code_ch	            ,    billCode          ,fpdm	   ,发票代码	   ,   是,	长度根据发票类型确定  ;"+
	"begin_no	                ,    biginNo           ,qshm	   ,起始号码	   ,   是,  ;"+
	"re_purch_num              ,    rePurChNum        ,fpfs	   ,领购份数	   ,   是,	整数  ;";
String className="ReportTaxDiskDistibute";
new ReportTaxDiskDistibute().CreateXmlMethodBase(className, data);
}
/**
*纳税人识别号  是否必须：是

*/
private static final String tax_no_ch="nsrsbh";
/**
*报税盘编号  是否必须：是

*/
private static final String report_tax_disk_no_ch="bspbh";
/**
*报税盘口令  是否必须：是

*/
private static final String report_tax_disk_pwd_ch="bspkl";
/**
*税控盘编号  是否必须：是

*/
private static final String tax_disk_no_ch="skpbh";
/**
*税控盘口令  是否必须：是

*/
private static final String tax_disk_pwd_ch="skpkl";
/**
*数字证书密码  是否必须：是

*/
private static final String data_cert_pwd_ch="keypwd";
/**
*发票类型代码  是否必须：是

*/
private static final String fapiao_type_ch="fplxdm";
/**
*发票代码  是否必须：是
长度根据发票类型确定
*/
private static final String bill_code_ch="fpdm";
/**
*起始号码  是否必须：是

*/
private static final String begin_no="qshm";
/**
*领购份数  是否必须：是
整数
*/
private static final String re_purch_num="fpfs";
/**
*纳税人识别号 是否必须：是

*/
private String taxNo;
/**
*报税盘编号 是否必须：是

*/
private String reportTaxDiskNo;
/**
*报税盘口令 是否必须：是

*/
private String reportTaxDiskPwd;
/**
*税控盘编号 是否必须：是

*/
private String taxDiskNo;
/**
*税控盘口令 是否必须：是

*/
private String taxDiskPwd;
/**
*数字证书密码 是否必须：是

*/
private String dataCertPwd;
/**
*发票类型代码 是否必须：是

*/
private String fapiaoType;
/**
*发票代码 是否必须：是
长度根据发票类型确定
*/
private String billCode;
/**
*起始号码 是否必须：是

*/
private String biginNo;
/**
*领购份数 是否必须：是
整数
*/
private String rePurChNum;
public String getTaxNo() {
	return taxNo;
}
public void setTaxNo(String taxNo) {
	this.taxNo = taxNo;
}
public String getReportTaxDiskNo() {
	return reportTaxDiskNo;
}
public void setReportTaxDiskNo(String reportTaxDiskNo) {
	this.reportTaxDiskNo = reportTaxDiskNo;
}
public String getReportTaxDiskPwd() {
	return reportTaxDiskPwd;
}
public void setReportTaxDiskPwd(String reportTaxDiskPwd) {
	this.reportTaxDiskPwd = reportTaxDiskPwd;
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
public String getDataCertPwd() {
	return dataCertPwd;
}
public void setDataCertPwd(String dataCertPwd) {
	this.dataCertPwd = dataCertPwd;
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
public String getBiginNo() {
	return biginNo;
}
public void setBiginNo(String biginNo) {
	this.biginNo = biginNo;
}
public String getRePurChNum() {
	return rePurChNum;
}
public void setRePurChNum(String rePurChNum) {
	this.rePurChNum = rePurChNum;
}

public String createReportTaxDiskDistibuteXML () throws Exception{
	  Element root =CreateDoocumentHeard();
	  Document Doc = new Document(root);
	  Element elements =CreateBodyElement();
	  Element input=createInputElement();
	  addChildElementText(input,tax_no_ch,taxNo);
	  addChildElementText(input,report_tax_disk_no_ch,reportTaxDiskNo);
	  addChildElementText(input,report_tax_disk_pwd_ch,reportTaxDiskPwd);
	  addChildElementText(input,tax_disk_no_ch,taxDiskNo);
	  addChildElementText(input,tax_disk_pwd_ch,taxDiskPwd);
	  addChildElementText(input,data_cert_pwd_ch,dataCertPwd);
	  addChildElementText(input,fapiao_type_ch,fapiaoType);
	  addChildElementText(input,bill_code_ch,billCode);
	  addChildElementText(input,begin_no,biginNo);
	  addChildElementText(input,re_purch_num,rePurChNum);
	  elements.addContent(input);
	  root.addContent(elements);
	  String outString=CreateDocumentFormt(Doc, path_ch,filename);
	  System.out.println(outString);
	  return outString;
	}

/**
*返回代码  是否必须：是
0成功，其它失败
*/
private static final String return_code_ch="returncode";
/**
*返回信息  是否必须：是

*/
private static final String return_msg_ch="returnmsg";
private static final String filename="报税盘发票分发.xml";

}
