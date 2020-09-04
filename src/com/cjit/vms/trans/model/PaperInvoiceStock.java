package com.cjit.vms.trans.model;

/**
 * 纸质发票库存明细表映射类
 * 
 * @author jobell
 */
public class PaperInvoiceStock {
	// 数据库属性
	private String paperInvoiceStockId;//库存ID
	private String instId;//机构ID
	private String userId;//领购人员
	private String receiveInvoiceTime;//领购日期
	private String maxMoney;//单张发票开票金额限额
	private String invoiceType;//发票类型
	private String distributeFlag;//分发状态 0:否 1：是 2：部分分发
	private String createTime;//操作时间（录入时间）
	private String createUserId;//操作人ID（录入人）
	private String createInstId;//操作机构ID（录入人所属机构）
	private String taxDiskNo;//税控盘号
	private String taxpayerNo;//纳税人识别号
	//辅助属性 receiveInvoiceTime taxDiskNo taxpayerNo
	
	public String getPaperInvoiceStockId() {
		return paperInvoiceStockId;
	}
	public void setPaperInvoiceStockId(String paperInvoiceStockId) {
		this.paperInvoiceStockId = paperInvoiceStockId;
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
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getDistributeFlag() {
		return distributeFlag;
	}
	public void setDistributeFlag(String distributeFlag) {
		this.distributeFlag = distributeFlag;
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
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public String getTaxpayerNo() {
		return taxpayerNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}
	
}
