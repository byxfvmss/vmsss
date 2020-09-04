package com.cjit.vms.trans.model.storage;
/**
 * 纸质发票使用明细表映射类
 * 
 * @author jobell
 */
public class PaperInvoiceUseDetail { 
	// 数据库属性
	private String paperInvoiceId;
	private String paperInvoiceStockId;
	private String paperInvoiceDistributeId;
	private String invoiceCode; // 发票代码
	private String invoiceNo; // 发票号码
	private String receiveStatus;
	private String invoiceStatus;
	private String invalidReason;
	private String receiveInstId;
	private String receiveUserId;
	private String changeTime;
	private String instId; // 机构ID
	private String instName; // 机构名称
	private String invoiceBeginNo;//发票起始号码
	private String invoiceEndNo;//发票终止号码
	private String distributeNum;//分发张数
	private String invoiceStatus0; // 发票状态  0：未使用
	private String invoiceStatus1; // 发票状态  1：正常开票
	private String invoiceStatus2; // 发票状态  2：已作废
	private String invoiceStatus3; // 发票状态  3：红冲
	private String invoiceStatus4; // 发票状态  4：被红冲
	private String invoiceType;//发票类型
	private String receiveInvoiceTime; //2019 领购时间
	
	public String getReceiveInvoiceTime() {
		return receiveInvoiceTime;
	}
	public void setReceiveInvoiceTime(String receiveInvoiceTime) {
		this.receiveInvoiceTime = receiveInvoiceTime;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getPaperInvoiceId() {
		return paperInvoiceId;
	}
	public void setPaperInvoiceId(String paperInvoiceId) {
		this.paperInvoiceId = paperInvoiceId;
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
	public String getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public String getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
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
	public String getInvoiceStatus0() {
		return invoiceStatus0;
	}
	public void setInvoiceStatus0(String invoiceStatus0) {
		this.invoiceStatus0 = invoiceStatus0;
	}
	public String getInvoiceStatus1() {
		return invoiceStatus1;
	}
	public void setInvoiceStatus1(String invoiceStatus1) {
		this.invoiceStatus1 = invoiceStatus1;
	}
	public String getInvoiceStatus2() {
		return invoiceStatus2;
	}
	public void setInvoiceStatus2(String invoiceStatus2) {
		this.invoiceStatus2 = invoiceStatus2;
	}
	public String getInvoiceStatus3() {
		return invoiceStatus3;
	}
	public void setInvoiceStatus3(String invoiceStatus3) {
		this.invoiceStatus3 = invoiceStatus3;
	}
	public String getInvoiceStatus4() {
		return invoiceStatus4;
	}
	public void setInvoiceStatus4(String invoiceStatus4) {
		this.invoiceStatus4 = invoiceStatus4;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
}
