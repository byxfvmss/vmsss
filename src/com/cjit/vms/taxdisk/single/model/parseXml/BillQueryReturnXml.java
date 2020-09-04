package com.cjit.vms.taxdisk.single.model.parseXml;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.single.model.BaseDiskModel;

public class BillQueryReturnXml extends BaseDiskModel{
public static void main(String[] args) {
	String data="fapiao_type_ch  					, fapiaoType  							,fplxdm	 		,发票类型代码			,是,  ;"+
	"bill_code_ch  						, billCode  								,fpdm				,发票代码					,是,  ;"+
	"bill_no  									, billNo  									,fphm				,发票号码					,是,  ;"+
	"bill_status_ch  					, billStatus  							,fpzt				,发票状态					,是, 0：已开具的正数发票 1:已开具的负数发票2 :未开具发票的作废发票3：已开正数票的作废发票4:已开负数票的作废发票  ;"+
	"upLoad_flag_ch  					, uploadFlag  							,scbz				,上传标志					,是, 0：未上传 1:已上传  ;"+
	"issue_date_ch  						, issueDate  								,kprq				,开票日期					,是, YYYYMMDD  ;"+
	"issue_time_ch  						, issueTime 		 						,kpsj	    	,开票时间					,否, HHMMSS  ;"+
	"special_ticket_flag_ch  	, specialTicketFlag  				,tspz	    	,特殊票种标识			,是,专票区分是否稀土发票 普票区分是否农产品收购或销售发票  ;"+
	"tax_disk_no_ch						, taxDiskNo  								,skpbh			,税控盘编号			 ,是,  ;"+
	"tax_control_code_ch				, taxConTrolCode  					,skm				,税控码					 ,否,  ;"+
	"check_code_ch							, checkCode  								,jym				,校验码					,否,  ;"+
	"payee_ch									, payee  										,skr				,收款人					,否,  ;"+
	"reviewer_ch								, reviewer  								,fhr				,复核人		      ,否,  ;"+
	"darwer_ch									, darwer  									,kpr				,开票人		      ,否,  ;"+
	"ori_bill_code_ch					, oriBillCode  							,yfpdm			,原发票代码		  ,否,	负数发票时有效  ;"+
	"ori_bill_no_ch						, oriBillNo  								,yfphm			,原发票号码		  ,否,	负数发票时有效  ;"+
	"cancel_date_ch						, cancelDate  							,zfrq				,作废日期				 ,否,		作废发票时有效，YYYYMMDD  ;"+
	"cancel_people_ch					, cancelPeople  						,zfr				,作废人				  ,否,		作废发票时有效  ;"+
	"sign_param_ch 						, signParam  								,qmcs				,签名参数				 ,否,  ;"+
	"sign_value_ch							, signValue  								,qmz				,签名值					,否,  ;"+
	"has_neg_amt								, hasNegAmt  								,ykfsje			,已开负数金额		 ,否,  ;"+
	"return_code_ch						, returnCode  							,returncode	,返回代码				 ,是,	00000000成功，其它失败  ;"+
	"return_msg_ch	 						, returnMsg  								,returnmsg	,返回信息				 ,是, ;";
	  String[] datas=data.split(";");
	  StringBuffer entityString= new StringBuffer();
	  for(int i=0;i<datas.length;i++){
	    String[] infos=datas[i].split(",");
	     entityString.append("/*\r\n*"+infos[3].trim()+"  是否必须："+infos[4].trim()+"\r\n"+infos[5].trim()).append("\r\n*/").append("\r\n").append("private static final String ").append(infos[0].trim()).append("="+"\"").append(infos[2].trim()).append("\";\r\n");
	  }
	  for(int i=0;i<datas.length;i++){
	    String[] infos=datas[i].split(",");
	    //entityString.append("/*\r\n*"+infos[3].trim()+" 是否必须："+infos[4].trim()+"\r\n"+infos[5].trim()).append("\r\n*/").append("\r\n").append("private String ").append(infos[1].trim()+";\r\n");

	  }
	  System.out.println(entityString);
}
public BillQueryReturnXml  (String StringXml) throws Exception{
	  Document doc =StringToDocument(StringXml);
	  Element body=getBodyElement(doc);
	  Element output=body.getChild(out_put_ch);
	  Element billInfo=output.getChild(bill_info_ch);
	  List  billList=billInfo.getChildren(group_ch);
	
	  this.fapiaoType=output.getChildText(fapiao_type_ch);
	  this.billCode=output.getChildText(bill_code_ch);
	  this.billNo=output.getChildText(bill_no);
	  this.billStatus=output.getChildText(bill_status_ch);
	  this.uploadFlag=output.getChildText(upLoad_flag_ch);
	  this.issueDate=output.getChildText(issue_date_ch);
	  this.issueTime=output.getChildText(issue_time_ch);
	  this.specialTicketFlag=output.getChildText(special_ticket_flag_ch);
	  this.taxDiskNo=output.getChildText(tax_disk_no_ch);
	  this.taxConTrolCode=output.getChildText(tax_control_code_ch);
	  this.checkCode=output.getChildText(check_code_ch);
	  this.payee=output.getChildText(payee_ch);
	  this.reviewer=output.getChildText(reviewer_ch);
	  this.darwer=output.getChildText(darwer_ch);
	  this.oriBillCode=output.getChildText(ori_bill_code_ch);
	  this.oriBillNo=output.getChildText(ori_bill_no_ch);
	  this.cancelDate=output.getChildText(cancel_date_ch);
	  this.cancelPeople=output.getChildText(cancel_people_ch);
	  this.signParam=output.getChildText(sign_param_ch);
	  this.signValue=output.getChildText(sign_value_ch);
	  this.hasNegAmt=output.getChildText(has_neg_amt);
	  this.returnCode=output.getChildText(return_code_ch);
	  this.returnMsg=output.getChildText(return_msg_ch);
	}
/***
 * 发票信息
 */
private static final String bill_info_ch="fpxx";
private static final String group_ch="group";
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

*/
private String billNo;
/*
*发票状态 是否必须：是
0：已开具的正数发票 1:已开具的负数发票2 :未开具发票的作废发票3：已开正数票的作废发票4:已开负数票的作废发票
*/
private String billStatus;
/*
*上传标志 是否必须：是
0：未上传 1:已上传
*/
private String uploadFlag;
/*
*开票日期 是否必须：是
YYYYMMDD
*/
private String issueDate;
/*
*开票时间 是否必须：否
HHMMSS
*/
private String issueTime;
/*
*特殊票种标识 是否必须：是
专票区分是否稀土发票 普票区分是否农产品收购或销售发票
*/
private String specialTicketFlag;
/*
*税控盘编号 是否必须：是

*/
private String taxDiskNo;
/*
*税控码 是否必须：否

*/
private String taxConTrolCode;
/*
*校验码 是否必须：否

*/
private String checkCode;
/*
*收款人 是否必须：否

*/
private String payee;
/*
*复核人 是否必须：否

*/
private String reviewer;
/*
*开票人 是否必须：否

*/
private String darwer;
/*
*原发票代码 是否必须：否
负数发票时有效
*/
private String oriBillCode;
/*
*原发票号码 是否必须：否
负数发票时有效
*/
private String oriBillNo;
/*
*作废日期 是否必须：否
作废发票时有效，YYYYMMDD
*/
private String cancelDate;
/*
*作废人 是否必须：否
作废发票时有效
*/
private String cancelPeople;
/*
*签名参数 是否必须：否

*/
private String signParam;
/*
*签名值 是否必须：否

*/
private String signValue;
/*
*已开负数金额 是否必须：否

*/
private String hasNegAmt;
/*
*返回代码 是否必须：是
00000000成功，其它失败
*/
private String returnCode;
/*
*返回信息 是否必须：是

*/
private String returnMsg;
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
public String getBillStatus() {
	return billStatus;
}
public void setBillStatus(String billStatus) {
	this.billStatus = billStatus;
}
public String getUploadFlag() {
	return uploadFlag;
}
public void setUploadFlag(String uploadFlag) {
	this.uploadFlag = uploadFlag;
}
public String getIssueDate() {
	return issueDate;
}
public void setIssueDate(String issueDate) {
	this.issueDate = issueDate;
}
public String getIssueTime() {
	return issueTime;
}
public void setIssueTime(String issueTime) {
	this.issueTime = issueTime;
}
public String getSpecialTicketFlag() {
	return specialTicketFlag;
}
public void setSpecialTicketFlag(String specialTicketFlag) {
	this.specialTicketFlag = specialTicketFlag;
}
public String getTaxDiskNo() {
	return taxDiskNo;
}
public void setTaxDiskNo(String taxDiskNo) {
	this.taxDiskNo = taxDiskNo;
}
public String getTaxConTrolCode() {
	return taxConTrolCode;
}
public void setTaxConTrolCode(String taxConTrolCode) {
	this.taxConTrolCode = taxConTrolCode;
}
public String getCheckCode() {
	return checkCode;
}
public void setCheckCode(String checkCode) {
	this.checkCode = checkCode;
}
public String getPayee() {
	return payee;
}
public void setPayee(String payee) {
	this.payee = payee;
}
public String getReviewer() {
	return reviewer;
}
public void setReviewer(String reviewer) {
	this.reviewer = reviewer;
}
public String getDarwer() {
	return darwer;
}
public void setDarwer(String darwer) {
	this.darwer = darwer;
}
public String getOriBillCode() {
	return oriBillCode;
}
public void setOriBillCode(String oriBillCode) {
	this.oriBillCode = oriBillCode;
}
public String getOriBillNo() {
	return oriBillNo;
}
public void setOriBillNo(String oriBillNo) {
	this.oriBillNo = oriBillNo;
}
public String getCancelDate() {
	return cancelDate;
}
public void setCancelDate(String cancelDate) {
	this.cancelDate = cancelDate;
}
public String getCancelPeople() {
	return cancelPeople;
}
public void setCancelPeople(String cancelPeople) {
	this.cancelPeople = cancelPeople;
}
public String getSignParam() {
	return signParam;
}
public void setSignParam(String signParam) {
	this.signParam = signParam;
}
public String getSignValue() {
	return signValue;
}
public void setSignValue(String signValue) {
	this.signValue = signValue;
}
public String getHasNegAmt() {
	return hasNegAmt;
}
public void setHasNegAmt(String hasNegAmt) {
	this.hasNegAmt = hasNegAmt;
}
public String getReturnCode() {
	return returnCode;
}
public void setReturnCode(String returnCode) {
	this.returnCode = returnCode;
}
public String getReturnMsg() {
	return returnMsg;
}
public void setReturnMsg(String returnMsg) {
	this.returnMsg = returnMsg;
}



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

*/
private static final String bill_no="fphm";
/*
*发票状态  是否必须：是
0：已开具的正数发票 1:已开具的负数发票2 :未开具发票的作废发票3：已开正数票的作废发票4:已开负数票的作废发票
*/
private static final String bill_status_ch="fpzt";
/*
*上传标志  是否必须：是
0：未上传 1:已上传
*/
private static final String upLoad_flag_ch="scbz";
/*
*开票日期  是否必须：是
YYYYMMDD
*/
private static final String issue_date_ch="kprq";
/*
*开票时间  是否必须：否
HHMMSS
*/
private static final String issue_time_ch="kpsj";
/*
*特殊票种标识  是否必须：是
专票区分是否稀土发票 普票区分是否农产品收购或销售发票
*/
private static final String special_ticket_flag_ch="tspz";
/*
*税控盘编号  是否必须：是

*/
private static final String tax_disk_no_ch="skpbh";
/*
*税控码  是否必须：否

*/
private static final String tax_control_code_ch="skm";
/*
*校验码  是否必须：否

*/
private static final String check_code_ch="jym";
/*
*收款人  是否必须：否

*/
private static final String payee_ch="skr";
/*
*复核人  是否必须：否

*/
private static final String reviewer_ch="fhr";
/*
*开票人  是否必须：否

*/
private static final String darwer_ch="kpr";
/*
*原发票代码  是否必须：否
负数发票时有效
*/
private static final String ori_bill_code_ch="yfpdm";
/*
*原发票号码  是否必须：否
负数发票时有效
*/
private static final String ori_bill_no_ch="yfphm";
/*
*作废日期  是否必须：否
作废发票时有效，YYYYMMDD
*/
private static final String cancel_date_ch="zfrq";
/*
*作废人  是否必须：否
作废发票时有效
*/
private static final String cancel_people_ch="zfr";
/*
*签名参数  是否必须：否

*/
private static final String sign_param_ch="qmcs";
/*
*签名值  是否必须：否

*/
private static final String sign_value_ch="qmz";
/*
*已开负数金额  是否必须：否

*/
private static final String has_neg_amt="ykfsje";
/*
*返回代码  是否必须：是
00000000成功，其它失败
*/
private static final String return_code_ch="returncode";
/*
*返回信息  是否必须：是

*/
private static final String return_msg_ch="returnmsg";



}
