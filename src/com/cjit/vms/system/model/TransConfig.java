package com.cjit.vms.system.model;

public class TransConfig {
	private String transId;
	private String transName;
	private String verificationId;
	private String verificationName;
	private String remark;
	private String taxType;
	private String taxTypeName;
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public String getVerificationId() {
		return verificationId;
	}
	public void setVerificationId(String verificationId) {
		this.verificationId = verificationId;
	}
	public String getVerificationName() {
		return verificationName;
	}
	public void setVerificationName(String verificationName) {
		this.verificationName = verificationName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public String getTaxTypeName() {
		return taxTypeName;
	}
	public void setTaxTypeName(String taxTypeName) {
		this.taxTypeName = taxTypeName;
	}
}
