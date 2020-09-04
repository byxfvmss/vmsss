package com.cjit.vms.system.model;

/**
 * 发票商品
 * 
 * @author Larry
 */
public class GoodsInfo {

	// 数据属性
	private String goodsName;// GOODS_NAME VARCHAR2(50) N 商品名称
	private String goodsNo;// GOODS_NO VARCHAR2(20) N 发票商品编号
	private String taxNo;// TAX_NO VARCHAR2(20) N 纳税人识别号
	private String model;//MODEL 规格型号
	private String unit; //UNIT 单位
	private String transType;// TRANS_TYPE VARCHAR2(50) N 交易类型
	private String transName;// BUSINESS_CNAME VARCHAR2(50) 交易名称
//	private String taxType;//纳税人类型
	private String taxRate;//商品税率
	private String goodsFullName;
	

	public GoodsInfo() {

	}

	public GoodsInfo(String goodsName, String goodsNo, String taxNo,
			String transType) {
		this.goodsName = goodsName;
		this.goodsNo = goodsNo;
		this.taxNo = taxNo;
		this.transType = transType;
	}
	public GoodsInfo(String goodsName, String goodsNo, String taxNo,
			String transType, String taxRate) {
		this.goodsName = goodsName;
		this.goodsNo = goodsNo;
		this.taxNo = taxNo;
		this.transType = transType;
		this.taxRate = taxRate;
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

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
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

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getTaxRate() {
		if(null == this.taxRate){
			return "";
		}
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getGoodsFullName() {
		return goodsFullName;
	}

	public void setGoodsFullName(String goodsFullName) {
		this.goodsFullName = goodsFullName;
	}

}
