package com.cjit.vms.taxServer.model.parseXMl;

public class CurrentReturnXml extends BaseReturnXml{
	
	
/**
 * 发票代码
 */
private String billCode;
/**
 * 发票号码
 */
private String billNo;
public String getBillCode() {
	return billCode;
}
public String getBillNo() {
	return billNo;
}
public void setBillCode(String billCode) {
	this.billCode = billCode;
}
public void setBillNo(String billNo) {
	this.billNo = billNo;
}



}
