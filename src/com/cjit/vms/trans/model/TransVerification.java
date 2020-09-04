package com.cjit.vms.trans.model;

public class TransVerification {
	private String id;
	private String verificationType;
	private String goodsNo;
	private String goodsName;
	private String inlandFlag;
	private String inlandFlagName;
	private String verificationName;
	private String verificationFullName;
	private String taxRate;
	private String taxRateName;
	private String taxType;
	private String taxTypeName;
	
	private String ramark;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVerificationType() {
		return verificationType;
	}
	public void setVerificationType(String verificationType) {
		this.verificationType = verificationType;
	}
	public String getInlandFlag() {
		return inlandFlag;
	}
	public void setInlandFlag(String inlandFlag) {
		this.inlandFlag = inlandFlag;
	}
	
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getRamark() {
		return ramark;
	}
	public void setRamark(String ramark) {
		this.ramark = ramark;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getVerificationName() {
		return verificationName;
	}
	public void setVerificationName(String verificationName) {
		this.verificationName = verificationName;
	}
	public String getInlandFlagName() {
		return inlandFlagName;
	}
	public void setInlandFlagName(String inlandFlagName) {
		this.inlandFlagName = inlandFlagName;
	}
	public String getTaxRateName() {
		return taxRateName;
	}
	public void setTaxRateName(String taxRateName) {
		this.taxRateName = taxRateName;
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
	public String getVerificationFullName() {
		return verificationFullName;
	}
	public void setVerificationFullName(String verificationFullName) {
		this.verificationFullName = verificationFullName;
	}
	
}
