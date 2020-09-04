package com.cjit.vms.trans.model.storage;

import java.util.Date;

/**
 * 纸质发票库存明细表映射类
 * 
 * @author jobell
 */
public class PaperAutoInvoice {
	// 数据库属性
	private String autoInvoiceId;	//	电子发票库存表ID
	private String taxpayerNo;//   纳税人识别号
	private String taxDiskNo;// 税控盘号
	private String	userId;//领购人员
	private String	receiveInvoiceTime;	//领购日期
	private String	invoiceType	;// 发票类型
	private String	currentInvoiceCode;//当前发票代码
	private String	currentInvoiceNo;//当前发票号码
	private String	invoiceCode;//发票代码
	private String	invoiceBeginNo;	//发票起始号码 
	private String	invoiceEndNo;//发票终止号码
	private String	invoiceNum;//发票份数
	private String	surplusNum;//剩余份数
	private String	instId;//机构id
	public String getAutoInvoiceId() {
		return autoInvoiceId;
	}
	public String getTaxpayerNo() {
		return taxpayerNo;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public String getUserId() {
		return userId;
	}
	
	public String getInvoiceType() {
		return invoiceType;
	}
	public String getCurrentInvoiceCode() {
		return currentInvoiceCode;
	}
	public String getCurrentInvoiceNo() {
		return currentInvoiceNo;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public String getInvoiceBeginNo() {
		return invoiceBeginNo;
	}
	public String getInvoiceEndNo() {
		return invoiceEndNo;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public String getSurplusNum() {
		return surplusNum;
	}
	public String getInstId() {
		return instId;
	}
	public void setAutoInvoiceId(String autoInvoiceId) {
		this.autoInvoiceId = autoInvoiceId;
	}
	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public void setCurrentInvoiceCode(String currentInvoiceCode) {
		this.currentInvoiceCode = currentInvoiceCode;
	}
	public void setCurrentInvoiceNo(String currentInvoiceNo) {
		this.currentInvoiceNo = currentInvoiceNo;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public void setInvoiceBeginNo(String invoiceBeginNo) {
		this.invoiceBeginNo = invoiceBeginNo;
	}
	public void setInvoiceEndNo(String invoiceEndNo) {
		this.invoiceEndNo = invoiceEndNo;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public void setSurplusNum(String surplusNum) {
		this.surplusNum = surplusNum;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getReceiveInvoiceTime() {
		return receiveInvoiceTime;
	}
	public void setReceiveInvoiceTime(String receiveInvoiceTime) {
		this.receiveInvoiceTime = receiveInvoiceTime;
	}
	
	
	
}
