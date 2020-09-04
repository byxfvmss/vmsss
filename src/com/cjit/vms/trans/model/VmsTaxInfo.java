package com.cjit.vms.trans.model;

import java.util.List;

/**
 * 税目信息表情报表
 * 
 * @author lee
 */
public class VmsTaxInfo {
	private String taxId; // 税目ID
	private String taxno; // 纳税人识别号
	private String fapiaoType; // 发票类型
	private String taxRate; // 税率
	private String user_id;//用户ID
	private List lstAuthInstId;
	
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	} 

	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getTaxno() {
		return taxno;
	}
	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	
}
