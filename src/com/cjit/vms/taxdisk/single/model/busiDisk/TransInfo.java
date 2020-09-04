package com.cjit.vms.taxdisk.single.model.busiDisk;

import java.math.BigDecimal;

/**
 * @author tom 开具服务 实体类  为更改交易状态服务 字段刚刚好
 *
 */
public class TransInfo {
	/* 查 开票类型、交易id 票据id 
	  查余额 票据id 交易id 票据代码 */
	/**
	 *  开具类型
	 */
	private String issueType; //开具类型
	private String transId; //交易id
	private String billId;//票据id
	private BigDecimal balance;//交易余额
	private String billCode;//发票代码
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
	
}
