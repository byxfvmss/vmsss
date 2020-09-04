package com.cjit.vms.taxdisk.servlet.model;

import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo;

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
	private  String unit;
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
	/**含税标志 0 不含税 1 含税

	 * 
	 */
	private  String taxlogo;
	
	public Product(BillItemInfo bill) {
		super();
		this.billroProperty = TaxSelvetUtil.Bill_row_Property_0;
		this.productName = bill.getGoodsName();
		this.productTax = bill.getTaxItem();
		this.specification =bill.getSpecandmodel();
		this.unit = bill.getGoodsUnit();
		this.productNumber = bill.getGoodsNo();
		this.price = bill.getGoodsPrice();
		this.amt = bill.getAmt();
		this.rate = bill.getTaxRate();
		this.taxamt =bill.getTaxAmt();
	//	if(Double.parseDouble(bill.getTaxRate())==0){
			taxlogo = TaxSelvetUtil.Tax_logo_0;
		//}else{
		//	taxlogo = TaxSelvetUtil.Tax_logo_1;
		//}
	}
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
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
		return unit;
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
		return taxlogo;
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
		this.unit = unit;
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
		this.taxlogo = taxlogo;
	}

	
}
