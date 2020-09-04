package com.cjit.vms.trans.model.config;

/**
 * 交易类型(VMS_TRANS_TYPE)
 * 
 * @author Dylan
 * 
 */
public class TransTypeInfo {

	private String transTypeId;
	private String transTypeName;
	private String transTypeFullName;
	private String itemCode;
	private String goodsId;
	private String itemName;
	private String goodsName;
	private String remark;
	public String getTransTypeId() {
		return transTypeId;
	}
	public void setTransTypeId(String transTypeId) {
		this.transTypeId = transTypeId;
	}
	public String getTransTypeName() {
		return transTypeName;
	}
	public void setTransTypeName(String transTypeName) {
		this.transTypeName = transTypeName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getTransTypeFullName() {
		return transTypeId+"-"+transTypeName;
	}
	public void setTransTypeFullName(String transTypeFullName) {
		this.transTypeFullName = transTypeFullName;
	}
}
