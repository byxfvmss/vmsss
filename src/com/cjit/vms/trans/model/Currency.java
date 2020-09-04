package com.cjit.vms.trans.model;

public class Currency {

	private String currency;   //币种代码
	private String currencySymbol;  //币种符号
	private String currencyName;    //币种名称
	private String currencySCode;   //币种两位代码
	
	public Currency() {
		
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getCurrencySCode() {
		return currencySCode;
	}
	public void setCurrencySCode(String currencySCode) {
		this.currencySCode = currencySCode;
	}
	
	
}
