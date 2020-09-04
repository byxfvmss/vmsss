package com.cjit.vms.taxServer.model;

public class Product {

	/**
	 * 发票行性质
	 */
	private  String billroProperty;
	/**
	 * 商品名称
	 */
	private  String productName;
	/**
	 * 商品税目
	 */
	private  String productTax;
	/**规格型号
	 * 
	 */
	private  String specification;
	/**单位
	 * 
	 */
	private  String Unit;
	/**商品数量
	 * 
	 */
	private  String productNumber;
	/**
	 * 单价
	 */
	private  String price;
	/**
	 * 金额
	 */
	private  String amt;
	/**
	 * 税率
	 */
	private  String rate;
	/**
	 * 税额
	 */
	private  String taxamt;
	/**含税标志
	 * 
	 */
	private  String Taxlogo;
	public String getBillroProperty() {
		return billroProperty;
	}
	public String getProductName() {
		return productName;
	}
	public String getProductTax() {
		return productTax;
	}
	public String getSpecification() {
		return specification;
	}
	public String getUnit() {
		return Unit;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public String getPrice() {
		return price;
	}
	public String getAmt() {
		return amt;
	}
	public String getRate() {
		return rate;
	}
	public String getTaxamt() {
		return taxamt;
	}
	public String getTaxlogo() {
		return Taxlogo;
	}
	public void setBillroProperty(String billroProperty) {
		this.billroProperty = billroProperty;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setProductTax(String productTax) {
		this.productTax = productTax;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public void setTaxamt(String taxamt) {
		this.taxamt = taxamt;
	}
	public void setTaxlogo(String taxlogo) {
		Taxlogo = taxlogo;
	}
	
}
