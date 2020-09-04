package com.cjit.vms.trans.model.storage;

import java.util.List;


/**
 * 纸质发票领用退还表映射类
 * 
 * @author jobell
 */
public class PaperInvoiceRbHistory {
	
	// 数据库属性
	private String invoiceCode;//发票代码
	private String invoiceBeginNo;//发票起始号码
	private String invoiceEndNo;//发票终止号码
	private String receiveInstId;//领用机构ID
	private String receiveUserId;//领用人
	private String operatorFlag;//操作标记 0：领用 1：退还
	private String createTime;//操作时间
	private String createUserId;//操作人
	private String createInstId;//操作人机构
	private String paperInvoiceDistributeId;
	private String paperInvoiceRbHistoryId;
	private String paperInvoiceStockId;

	private String instId; // 机构ID
	private String instName; // 机构名称
	private String invoiceNum;// 张数
	private String balanceNum;
	private List<PaperInvoiceRbDetail> listDetil;
	private String returnNum;//退还数量
	
	//辅助字段
	private String receive_num;
	
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
	public String getOperatorFlag() {
		return operatorFlag;
	}
	public void setOperatorFlag(String operatorFlag) {
		this.operatorFlag = operatorFlag;
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
//	public String getPaper_invoice_distribute_id() {
//		return paper_invoice_distribute_id;
//	}
//	public void setPaper_invoice_distribute_id(String paper_invoice_distribute_id) {
//		this.paper_invoice_distribute_id = paper_invoice_distribute_id;
//	}
	public String getReceive_num() {
		return receive_num;
	}
	public void setReceive_num(String receive_num) {
		this.receive_num = receive_num;
	}
//	public String getPaper_invoice_rb_history_id() {
//		return paper_invoice_rb_history_id;
//	}
//	public void setPaper_invoice_rb_history_id(String paper_invoice_rb_history_id) {
//		this.paper_invoice_rb_history_id = paper_invoice_rb_history_id;
//	}
//	public String getPaper_invoice_stock_id() {
//		return paper_invoice_stock_id;
//	}
//	public void setPaper_invoice_stock_id(String paper_invoice_stock_id) {
//		this.paper_invoice_stock_id = paper_invoice_stock_id;
//	}
	public String getPaperInvoiceDistributeId() {
		return paperInvoiceDistributeId;
	}
	public void setPaperInvoiceDistributeId(String paperInvoiceDistributeId) {
		this.paperInvoiceDistributeId = paperInvoiceDistributeId;
	}
	public String getPaperInvoiceRbHistoryId() {
		return paperInvoiceRbHistoryId;
	}
	public void setPaperInvoiceRbHistoryId(String paperInvoiceRbHistoryId) {
		this.paperInvoiceRbHistoryId = paperInvoiceRbHistoryId;
	}
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
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public List<PaperInvoiceRbDetail> getListDetil() {
		return listDetil;
	}
	public void setListDetil(List<PaperInvoiceRbDetail> listDetil) {
		this.listDetil = listDetil;
	}
	public String getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(String returnNum) {
		this.returnNum = returnNum;
	}
	public String getBalanceNum() {
		return balanceNum==null?"0":balanceNum;
	}
	public void setBalanceNum(String balanceNum) {
		this.balanceNum = balanceNum;
	}
	
}
