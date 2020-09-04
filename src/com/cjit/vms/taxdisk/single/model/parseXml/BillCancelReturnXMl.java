package com.cjit.vms.taxdisk.single.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.single.model.BaseDiskModel;

import cjit.crms.util.json.JsonUtil;

public class BillCancelReturnXMl extends BaseDiskModel{
	public BillCancelReturnXMl(){
		
	}

public BillCancelReturnXMl   (String StringXml) throws Exception{
	  Document doc =StringToDocument(StringXml);
	  Element body=getBodyElement(doc);
	  Element output=body.getChild(out_put_ch);
	  this.fapiaoType=output.getChildText(fapiao_type_ch);
	  this.billCode=output.getChildText(bill_code_ch);
	  this.billNo=output.getChildText(bill_no_ch);
	  this.cancelDate=output.getChildText(cancel_date_ch);
	  this.returnCode=output.getChildText(return_code_ch);
	  this.returnmsg=output.getChildText(return_msg_ch);
	  
}

/*
*发票类型代码  是否必须：是

*/
private static final String fapiao_type_ch="fplxdm";
/*
*发票代码  是否必须：否
空白发票作废为空 度根据发票类型不同
*/
private static final String bill_code_ch="fpdm";
/*
*发票号码  是否必须：否
空白发票作废为空
*/
private static final String bill_no_ch="fphm";
/*
*作废日期  是否必须：是
作废成功税控盘返回，格式YYYYMMDD
*/
private static final String cancel_date_ch="zfrq";
/*
*返回代码  是否必须：是
00000000成功，其它失败
*/
private static final String return_code_ch="returncode";
/*
*返回信息  是否必须：是

*/
private static final String return_msg_ch="returnmsg";


/*
*发票类型代码 是否必须：是

*/
private String fapiaoType;
/*
*发票代码 是否必须：否
空白发票作废为空 度根据发票类型不同
*/
private String billCode;
/*
*发票号码 是否必须：否
空白发票作废为空
*/
private String billNo;
/*
*作废日期 是否必须：是
作废成功税控盘返回，格式YYYYMMDD
*/
private String cancelDate;
/*
*返回代码 是否必须：是
00000000成功，其它失败
*/
private String returnCode;
/*
*返回信息 是否必须：是

*/
private String returnmsg;
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
public String getCancelDate() {
	return cancelDate;
}
public void setCancelDate(String cancelDate) {
	this.cancelDate = cancelDate;
}
public String getReturnCode() {
	return returnCode;
}
public void setReturnCode(String returnCode) {
	this.returnCode = returnCode;
}
public String getReturnmsg() {
	return returnmsg;
}
public void setReturnmsg(String returnmsg) {
	this.returnmsg = returnmsg;
}

}