package com.cjit.vms.taxdisk.aisino.single.model;

/**
 * 容器类型
 * @author john
 *
 */
public class ContainerType {

	private String productName;//商品名称
	private boolean includeTax;//true为含税价
	private double taxRate;//税率
	private double value;//金额
	private double price;//单价
	private String productUnit;//商品单位
	private String productSpec;//商品规格	
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public boolean isIncludeTax() {
		return includeTax;
	}
	public void setIncludeTax(String includeTax) {
		if("Y".equals(includeTax)){
			this.includeTax = true;
		}else if("N".equals(includeTax)){
			this.includeTax = false;
		}
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		try {
			this.taxRate = Double.parseDouble(taxRate);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public double getValue() {
		return value;
	}
	public void setValue(String value) {
		try {
			this.value = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(String price) {
		try {
			this.price = Double.parseDouble(price);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getProductUnit() {
		return productUnit;
	}
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}
	public String getProductSpec() {
		return productSpec;
	}
	public void setProductSpec(String productSpec) {
		this.productSpec = productSpec;
	}
}
