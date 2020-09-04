package com.cjit.vms.input.model;

import java.math.BigDecimal;

public class InputInvoiceItem {
	private String billId;//票据id
	private String billItemId;//票据明细id
	private String goodsName;//商品名称
	private String specandmodel;//规格型号
	private String goodsUnit;//单位
	private String goodsNo;//商品数量
	private String goodsPrice;//商品单价
	private String amt;//金额
	private String taxRate;//税率
	private BigDecimal taxAmt;//税额
	private String rowNature;// 票据行性质  0-正常行;	1-折扣行;	2-被折扣行'
	private String disItemId;//被折扣明细id
	private String discountRate;//折扣率
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillItemId() {
		return billItemId;
	}
	public void setBillItemId(String billItemId) {
		this.billItemId = billItemId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getSpecandmodel() {
		return specandmodel;
	}
	public void setSpecandmodel(String specandmodel) {
		this.specandmodel = specandmodel;
	}
	public String getGoodsUnit() {
		return goodsUnit;
	}
	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public BigDecimal getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}
	public String getRowNature() {
		return rowNature;
	}
	public void setRowNature(String rowNature) {
		this.rowNature = rowNature;
	}
	public String getDisItemId() {
		return disItemId;
	}
	public void setDisItemId(String disItemId) {
		this.disItemId = disItemId;
	}
	public String getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}
	
}
