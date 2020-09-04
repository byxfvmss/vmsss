package com.cjit.vms.trans.model.storage;

import java.util.List;

public class InvoiceAlertListInfo {
	private String instId;
	private String instName;
	private String invoiceType;
	private String alertNum;
	private String unusedInvoiceNum;
	private List lstAuthInstId;
	private String alertFlag = "0";
	
	public String userId;//用户ID 用作查询权限
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAlertFlag() {
		if(Integer.parseInt(unusedInvoiceNum) <	Integer.parseInt(alertNum)){
			return "1";
		}
		return "0";
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getAlertNum() {
		return alertNum;
	}

	public void setAlertNum(String alertNum) {
		this.alertNum = alertNum;
	}

	public String getUnusedInvoiceNum() {
		return unusedInvoiceNum;
	}

	public void setUnusedInvoiceNum(String unusedInvoiceNum) {
		this.unusedInvoiceNum = unusedInvoiceNum;
	}
}
