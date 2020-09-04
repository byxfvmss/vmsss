package com.cjit.vms.trans.model.config;

/**
 * 科目信息(vms_trans_verification)
 * 
 * @author Dylan
 * 
 */
public class ItemInfo {

	private String itemCode;
	private String itemName;
	private String parentCode;
	private String path;
	private String taxRate;
	private String taxNo;
	private String instCode;
	private String remark;
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
