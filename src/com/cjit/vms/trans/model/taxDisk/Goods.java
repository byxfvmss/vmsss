package com.cjit.vms.trans.model.taxDisk;

import org.jdom.Element;


/**
 * @author tom 商品 开具的子类
 *
 */
public class Goods extends BaseDiskModel{
	
	

/*
*商品名称
*/
public static final String goods_name_ch="spmc";
/*
*发票行性质
*/
public static final String bill_property_ch="fphxz";
/*
*商品税目
*/
public static final String goods_tax_item_ch="spsm";
/*
*规格型号
*/
public static final String specification_ch="ggxh";
/*
*单位
*/
public static final String unit_ch="dw";
/*
*商品数量
*/
public static final String goods_num_ch="spsl";
/*
*单价
*/
public static final String unit_price_ch="dj";
/*
*金额
*/
public static final String amt_ch="je";
/*
*税率
*/
public static final String rate_ch="sl";
/*
*税额
*/
public static final String tax_amt_ch="se";
/*
*含税标志
*/
public static final String tax_logo_ch="hsbz";

/*
*商品名称
*/
private  String goodsName;
/*
*发票行性质
*/
private  String billProperty;
/*
*商品税目
*/
private  String goodsTaxItem;
/*
*规格型号
*/
private  String specification;
/*
*单位
*/
private  String unit;
/*
*商品数量
*/
private  String goodsNum;
/*
*单价
*/
private  String unitPrice;
/*
*金额
*/
private  String amt;
/*
*税率
*/
private  String rate;
/*
*税额
*/
private  String taxAmt;
/*
*含税标志
*/
private  String taxLogo;
public String getGoodsName() {
	return goodsName;
}
public void setGoodsName(String goodsName) {
	this.goodsName = goodsName;
}
public String getBillProperty() {
	return billProperty;
}
public void setBillProperty(String billProperty) {
	this.billProperty = billProperty;
}
public String getGoodsTaxItem() {
	return goodsTaxItem;
}
public void setGoodsTaxItem(String goodsTaxItem) {
	this.goodsTaxItem = goodsTaxItem;
}
public String getSpecification() {
	return specification;
}
public void setSpecification(String specification) {
	this.specification = specification;
}
public String getUnit() {
	return unit;
}
public void setUnit(String unit) {
	this.unit = unit;
}
public String getGoodsNum() {
	return goodsNum;
}
public void setGoodsNum(String goodsNum) {
	this.goodsNum = goodsNum;
}
public String getUnitPrice() {
	return unitPrice;
}
public void setUnitPrice(String unitPrice) {
	this.unitPrice = unitPrice;
}
public String getAmt() {
	return amt;
}
public void setAmt(String amt) {
	this.amt = amt;
}
public String getRate() {
	return rate;
}
public void setRate(String rate) {
	this.rate = rate;
}
public String getTaxAmt() {
	return taxAmt;
}
public void setTaxAmt(String taxAmt) {
	this.taxAmt = taxAmt;
}
public String getTaxLogo() {
	return taxLogo;
}
public void setTaxLogo(String taxLogo) {
	this.taxLogo = taxLogo;
}

public Goods(String goodsName, String billProperty, String goodsTaxItem,
		String specification, String unit, String goodsNum, String unitPrice,
		String amt, String rate, String taxAmt, String taxLogo) {
	super();
	this.goodsName = goodsName;
	this.billProperty = billProperty;
	this.goodsTaxItem = goodsTaxItem;
	this.specification = specification;
	this.unit = unit;
	this.goodsNum = goodsNum;
	this.unitPrice = unitPrice;
	this.amt = amt;
	this.rate = rate;
	this.taxAmt = taxAmt;
	this.taxLogo = taxLogo;
}
public void createGoodsElement(Element e){
	addChildElementText(e, goods_name_ch,goodsName);
	addChildElementText(e, bill_property_ch, billProperty);
	addChildElementText(e, goods_tax_item_ch,goodsTaxItem);
	addChildElementText(e, specification_ch, specification);
	addChildElementText(e, unit_ch, unit);
	addChildElementText(e, goods_num_ch, goodsNum);
	addChildElementText(e, unit_price_ch, unitPrice);
	addChildElementText(e, amt_ch, amt);
	addChildElementText(e, rate_ch, rate);
	addChildElementText(e, tax_amt_ch, taxAmt);
	addChildElementText(e, tax_logo_ch, taxLogo);
	
}

}
