package com.cjit.vms.trans.model.config;

import java.util.List;

/**
 * 交易认定模型
 * @author Dylan
 *
 */
public class VerificationInfo {
	private String goodsId;
	private String goodsCode;
	private String goodsName;
	private String model;
	private String unit;
	private String itemCode;
	private String itemName;
	private String parentCode;
	private String path;
	private String taxRate;
	private String taxNo;
	private String instCode;
	private String instName;
	private String transTypeId;
	private String transTypeName;
	private String goodsIdAndName;
	private String[] transTypeList;
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
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public String getGoodsIdAndName() {
		this.goodsIdAndName = goodsId+" - "+goodsName;
		return goodsIdAndName;
	}
	public void setGoodsIdAndName(String goodsIdAndName) {
		this.goodsIdAndName = goodsIdAndName;
	}
	public String[] getTransTypeList() {
		return transTypeList;
	}
	public void setTransTypeList(String[] transTypeList) {
		this.transTypeList = transTypeList;
	}

}
