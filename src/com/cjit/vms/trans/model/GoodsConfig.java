package com.cjit.vms.trans.model;

public class GoodsConfig {
	private String goodsName;// GOODS_NAME VARCHAR2(50) N 商品名称
	private String goodsNo;// GOODS_NO VARCHAR2(20) N 发票商品编号
	private String taxType;
	private String taxName;

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
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

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

}
