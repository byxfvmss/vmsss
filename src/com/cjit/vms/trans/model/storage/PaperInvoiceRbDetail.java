package com.cjit.vms.trans.model.storage;

/**
 * 纸质发票领用退还明细表映射类
 * 
 * @author jobell
 */
public class PaperInvoiceRbDetail {
	private String paperInvoiceRbHistoryId;
	private String invoiceCode;
	private String invoiceNo;
	public String getPaperInvoiceRbHistoryId() {
		return paperInvoiceRbHistoryId;
	}
	public void setPaperInvoiceRbHistoryId(String paperInvoiceRbHistoryId) {
		this.paperInvoiceRbHistoryId = paperInvoiceRbHistoryId;
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
}
