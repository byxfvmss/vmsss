package com.cjit.vms.trans.model;

/**
 * 交易种类
 * 
 * @author Dylan
 */
public class TransType {

	String id;
	String verificationType;
	String inlandFlag;
	String verificationName;
	String taxRate;
	String remark;	
	String goodsName;
	String goodsNo;
	String taxType;
	String inlandFlagName;
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

	public String getVerificationName() {
		return verificationName;
	}
	public void setVerificationName(String verificationName) {
		this.verificationName = verificationName;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public String getInlandFlagName() {
		if(this.inlandFlag.equals("Y")){
			this.inlandFlagName = "境内";
		}else if(this.inlandFlag.equals("N")){
			this.inlandFlagName = "境外";
		}
		return this.inlandFlagName;
	}
	public void setInlandFlagName(String inlandFlagName) {
		if(this.inlandFlag.equals("Y")){
			this.inlandFlagName = "境内";
		}else if(this.inlandFlag.equals("N")){
			this.inlandFlagName = "境外";
		}else{
			this.inlandFlagName = inlandFlagName;
		}
	}
}
