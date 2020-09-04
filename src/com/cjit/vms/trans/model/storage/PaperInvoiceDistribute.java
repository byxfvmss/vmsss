package com.cjit.vms.trans.model.storage;

import java.util.List;


/**
 * 纸质发票分发表映射类
 * 
 * @author jobell
 */
public class PaperInvoiceDistribute {
	
	// 数据库属性
	private String paperInvoiceStockId;//库存ID
	private String paperInvoiceDistributeId;//分发ID
	private String receiveInstId;//领用部门ID
	private String receiveUserId;//领用人ID
	private String invoiceCode;//发票代码
	private String invoiceBeginNo;//发票起始号码
	private String invoiceEndNo;//发票终止号码
	private String distributeNum;//分发张数
	private String createTime;//操作时间（分发时间）
	private String createUserId;//操作人（操作分发的人）
	private String createInstId;//操作机构ID（操作分发人所属机构）
	private String hasReceiveNum;
	//辅助字段
	private String receiveInstName;//领用部门名称
	private String receiveUserName;//领用人名称
	private String createInstName;//领用部门名称
	private String createUserName;//领用人名称
	
	private String invoiceNo;
	private String userId;
	private List lstAuthInstId;
	private String unReceiveNum;//未领用张 数   --- 节后
	private String balanceNum;
	private String currentbillNo;
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getPaperInvoiceStockId() {
		return paperInvoiceStockId;
	}
	public void setPaperInvoiceStockId(String paperInvoiceStockId) {
		this.paperInvoiceStockId = paperInvoiceStockId;
	}
	public String getPaperInvoiceDistributeId() {
		return paperInvoiceDistributeId;
	}
	public void setPaperInvoiceDistributeId(String paperInvoiceDistributeId) {
		this.paperInvoiceDistributeId = paperInvoiceDistributeId;
	}
	public String getReceiveInstId() {
		return receiveInstId;
	}
	public void setReceiveInstId(String receiveInstId) {
		this.receiveInstId = receiveInstId;
	}
	public String getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public String getInvoiceBeginNo() {
		return invoiceBeginNo;
	}
	public void setInvoiceBeginNo(String invoiceBeginNo) {
		this.invoiceBeginNo = invoiceBeginNo;
	}
	public String getInvoiceEndNo() {
		return invoiceEndNo;
	}
	public void setInvoiceEndNo(String invoiceEndNo) {
		this.invoiceEndNo = invoiceEndNo;
	}
	public String getDistributeNum() {
		return distributeNum;
	}
	public void setDistributeNum(String distributeNum) {
		this.distributeNum = distributeNum;
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
	public String getReceiveInstName() {
		return receiveInstName;
	}
	public void setReceiveInstName(String receiveInstName) {
		this.receiveInstName = receiveInstName;
	}
	public String getReceiveUserName() {
		return receiveUserName;
	}
	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}
	public String getCreateInstName() {
		return createInstName;
	}
	public void setCreateInstName(String createInstName) {
		this.createInstName = createInstName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getHasReceiveNum() {
		return hasReceiveNum;
	}
	public void setHasReceiveNum(String hasReceiveNum) {
		this.hasReceiveNum = hasReceiveNum;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBalanceNum() {
		return balanceNum==null?"0":balanceNum;
	}

	public String getUnReceiveNum() {
		return unReceiveNum;
	}
	public void setUnReceiveNum(String unReceiveNum) {
		this.unReceiveNum = unReceiveNum;
	}
	public void setBalanceNum(String balanceNum) {
		this.balanceNum = balanceNum;
	}
	public String getCurrentbillNo() {
		return currentbillNo;
	}
	public void setCurrentbillNo(String currentbillNo) {
		this.currentbillNo = currentbillNo;
	}
	
}
