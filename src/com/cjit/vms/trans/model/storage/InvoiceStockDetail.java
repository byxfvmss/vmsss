package com.cjit.vms.trans.model.storage;

import java.util.List;

/**
 * 
 * @author admin
 *
 */
public class InvoiceStockDetail {

	private String stockId;//库存id
	private String instId;	//机构id
	private String instName; //机构名称
	private String userId;	//领购人员ID
	private String userName; //领购人员中文姓名
	private String receiveInvoiceTime;	//领购日期
	private String maxMoney;	//单张发票开票金额限额
	private String invoiceType;	//发票类型
	private String distributeFlag;	//分发状态 0:否 1：是 2：部分分发
	private String createTime;	//操作时间（录入时间）
	private String createUserId;	//操作人id（录入人）
	private String createInstId; 	//操作机构id（录入人所属机构）
	private String taxpayerNo;	//纳税人识别号
	private String taxDiskNo;	//税控盘号
	private String invoiceId;	//纸质发票id
	private String invoiceDistributeId;	//分发id
	private String invoiceCode;	//发票代码
	private String invoiceNo;	//发票号码
	private String receiveStatus;	//领用状态 0：未领用  1：已领用
	private String invoiceStatus;	//发票状态 0：未使用  1：正常开票 2：已作废 3：红冲 4：被红冲
	private String opertType;	//操作类型1是作废2是红冲
	private String invalidReason;	//作废原因
	private String receiveInstId;	//领用机构id
	private String changeTime;	//操作时间
	private String startTime; //开始时间
	private String endTime;   //结束时间
	private List lstAuthInstId;
	public String getOpertType() {
		return opertType;
	}
	public void setOpertType(String opertType) {
		this.opertType = opertType;
	}
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReceiveInvoiceTime() {
		return receiveInvoiceTime;
	}
	public void setReceiveInvoiceTime(String receiveInvoiceTime) {
		this.receiveInvoiceTime = receiveInvoiceTime;
	}
	public String getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(String maxMoney) {
		this.maxMoney = maxMoney;
	}
	public String getDistributeFlag() {
		return distributeFlag;
	}
	public void setDistributeFlag(String distributeFlag) {
		this.distributeFlag = distributeFlag;
	}
	
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateInstId() {
		return createInstId;
	}
	public void setCreateInstId(String createInstId) {
		this.createInstId = createInstId;
	}
	public String getTaxpayerNo() {
		return taxpayerNo;
	}
	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getInvoiceDistributeId() {
		return invoiceDistributeId;
	}
	public void setInvoiceDistributeId(String invoiceDistributeId) {
		this.invoiceDistributeId = invoiceDistributeId;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getInvalidReason() {
		return invalidReason;
	}
	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}
	public String getReceiveInstId() {
		return receiveInstId;
	}
	public void setReceiveInstId(String receiveInstId) {
		this.receiveInstId = receiveInstId;
	}
	public String getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	
	public InvoiceStockDetail(){
		
	}
	
	
}
