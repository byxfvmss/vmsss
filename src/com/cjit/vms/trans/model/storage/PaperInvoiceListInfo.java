package com.cjit.vms.trans.model.storage;

import java.util.Date;
import java.util.List;

/**
 * 纸质发票管理一览用model
 * 
 * @author lizw
 */
public class PaperInvoiceListInfo {
	private String paperInvoiceId; // 库存ID
	private String instId;// 机构ID	
	private String instName;// 机构名称
	private String userId; // 领购人员 
	private String invoiceType; // 发票类型
	private Date receiveInvoiceTime; // 领购日期	
	private Date receiveInvoiceEndTime; // 领购日期	
	private String maxMoney; // 单张发票开票金额限额	
	private String distributeFlag; // 分发状态	
	private Integer invoiceNum; // 总张数	
	private Integer userdNum; // 已用张数	
	private Integer unUserdNum; // 未用张数
	private Integer userRatioNum; // 已用百分比(%)
	
	
	/*发票分发明细一览页面用*/
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
	private String receiveInstName;//领用部门名称
	private String receiveUserName;//领用人名称
	private String createInstName;//领用部门名称
	private String createUserName;//领用人名称
	private String hasReceiveNum;
	private String taxpayerNo; // 纳税人识别号
	private String receiveInvoiceTimeA;
	private String receiveInvoiceEndTimeA;
	/*发票领用退还明细页面用*/
	private String operatorFlag;
	private String userID;
	private String taxDiskNo;
	private String 	normalMakeInvoice;//正常开票数量
	private String 	blankWasteCancel;//空白作废数量
	private String 	issuedCancel;//开具作废数量
	private String 	redHedge;//红冲数量
	private String 	cancellation;//发票作废数量
	private String 	redRush;//2019红冲数量
	private String 	blankVoid;//空白作废
	
	
	private String balanceNum;
	private String unReceiveNum;
	private List lstAuthInstId;
	private String billNoField;
	private String faPiaoType;
	public String getCancellation() {
		return cancellation;
	}
	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}
	public String getRedRush() {
		return redRush;
	}
	public void setRedRush(String redRush) {
		this.redRush = redRush;
	}
	public String getBlankVoid() {
		return blankVoid;
	}
	public void setBlankVoid(String blankVoid) {
		this.blankVoid = blankVoid;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getOperatorFlag() {
		return operatorFlag;
	}
	public void setOperatorFlag(String operatorFlag) {
		this.operatorFlag = operatorFlag;
	}
	public String getPaperInvoiceId() {
		return paperInvoiceId;
	}
	public void setPaperInvoiceId(String paperInvoiceId) {
		this.paperInvoiceId = paperInvoiceId;
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
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public Date getReceiveInvoiceTime() {
		return receiveInvoiceTime;
	}
	public void setReceiveInvoiceTime(Date receiveInvoiceTime) {
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
	public Integer getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(Integer invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public Integer getUserdNum() {
		return userdNum;
	}
	public void setUserdNum(Integer userdNum) {
		this.userdNum = userdNum;
	}
	public Integer getUnUserdNum() {
		return unUserdNum;
	}
	public void setUnUserdNum(Integer unUserdNum) {
		this.unUserdNum = unUserdNum;
	}
	public Integer getUserRatioNum() {
		return userRatioNum;
	}
	public void setUserRatioNum(Integer userRatioNum) {
		this.userRatioNum = userRatioNum;
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
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public Date getReceiveInvoiceEndTime() {
		return receiveInvoiceEndTime;
	}
	public void setReceiveInvoiceEndTime(Date receiveInvoiceEndTime) {
		this.receiveInvoiceEndTime = receiveInvoiceEndTime;
	}
	public String getTaxpayerNo() {
		return taxpayerNo;
	}
	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getReceiveInvoiceTimeA() {
		return receiveInvoiceTimeA;
	}
	public String getReceiveInvoiceEndTimeA() {
		return receiveInvoiceEndTimeA;
	}
	public void setReceiveInvoiceTimeA(String receiveInvoiceTimeA) {
		this.receiveInvoiceTimeA = receiveInvoiceTimeA;
	}
	public void setReceiveInvoiceEndTimeA(String receiveInvoiceEndTimeA) {
		this.receiveInvoiceEndTimeA = receiveInvoiceEndTimeA;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getBalanceNum() {
		return balanceNum;
	}
	public void setBalanceNum(String balanceNum) {
		this.balanceNum = balanceNum;
	}
	public String getNormalMakeInvoice() {
		return normalMakeInvoice;
	}
	public String getBlankWasteCancel() {
		return blankWasteCancel;
	}
	public String getIssuedCancel() {
		return issuedCancel;
	}
	public String getRedHedge() {
		return redHedge;
	}
	public void setNormalMakeInvoice(String normalMakeInvoice) {
		this.normalMakeInvoice = normalMakeInvoice;
	}
	public void setBlankWasteCancel(String blankWasteCancel) {
		this.blankWasteCancel = blankWasteCancel;
	}
	public void setIssuedCancel(String issuedCancel) {
		this.issuedCancel = issuedCancel;
	}
	public void setRedHedge(String redHedge) {
		this.redHedge = redHedge;
	}
	public String getUnReceiveNum() {
		return unReceiveNum;
	}
	public void setUnReceiveNum(String unReceiveNum) {
		this.unReceiveNum = unReceiveNum;
	}
	public String getBillNoField() {
		return billNoField;
	}
	public void setBillNoField(String billNoField) {
		this.billNoField = billNoField;
	}
	public String getFaPiaoType() {
		return faPiaoType;
	}
	public void setFaPiaoType(String faPiaoType) {
		this.faPiaoType = faPiaoType;
	}
	
	
}
