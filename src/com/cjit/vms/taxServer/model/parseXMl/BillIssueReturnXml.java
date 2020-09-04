package com.cjit.vms.taxServer.model.parseXMl;

public class BillIssueReturnXml extends BaseReturnXml {
	/*<?xml version="1.0" encoding="gbk"?>
	<business id="10008" comment="发票开具">
	<body yylxdm="1">
	<returncode>返回代码</returncode>
	<returnmsg>返回信息</returnmsg>
	<returndata>
	<fpdm>发票代码</fpdm>
	<fphm>发票号码</fphm>
	<kprq>开票日期</kprq>
	<skm>税控码</skm>
	<jym>校验码</jym>
	<ewm>二维码</ewm>
	</returndata>*/	
	private String billCode;
	private String billNo;
	private String billIssueDate;
	private String taxPwd;
	private String checkCode;
	public String getBillCode() {
		return billCode;
	}
	public String getBillNo() {
		return billNo;
	}
	public String getBillIssueDate() {
		return billIssueDate;
	}
	public String getTaxPwd() {
		return taxPwd;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public void setBillIssueDate(String billIssueDate) {
		this.billIssueDate = billIssueDate;
	}
	public void setTaxPwd(String taxPwd) {
		this.taxPwd = taxPwd;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
	
}
