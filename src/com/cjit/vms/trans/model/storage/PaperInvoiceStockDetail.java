package com.cjit.vms.trans.model.storage;

/**
 * 纸质发票库存明细表映射类
 * 
 * @author jobell
 */
public class PaperInvoiceStockDetail {
	// 数据库属性
	private String paperInvoiceStockId;
	private String invoiceCode;
	private String invoiceBeginNo;
	private String invoiceEndNo;
	private String invoiceNum;
	private String userdNum;
	private String hasDistributeNum;
	private String currentbillNo;
	
	//辅助属性
	public String getPaperInvoiceStockId() {
		return paperInvoiceStockId;
	}
	public void setPaperInvoiceStockId(String paperInvoiceStockId) {
		this.paperInvoiceStockId = paperInvoiceStockId;
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
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public String getUserdNum() {
		return userdNum;
	}
	public void setUserdNum(String userdNum) {
		this.userdNum = userdNum;
	}
	public String getHasDistributeNum() {
		return hasDistributeNum;
	}
	public void setHasDistributeNum(String hasDistributeNum) {
		this.hasDistributeNum = hasDistributeNum;
	}
	public String getCurrentbillNo() {
		return currentbillNo;
	}
	public void setCurrentbillNo(String currentbillNo) {
		this.currentbillNo = currentbillNo;
	}
}
