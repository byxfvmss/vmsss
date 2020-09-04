package com.cjit.vms.taxdisk.servlet.model.parseXml;

public class Bill {
	private String 	billCode;
	private  String startBillNo;
	private  String endBillNo;
	private  String billCount;
	private  String surplusCount;
	private  String BuyDate;
	private  String BuyPeople;
	public String getBillCode() {
		return billCode;
	}
	public String getStartBillNo() {
		return startBillNo;
	}
	public String getEndBillNo() {
		return endBillNo;
	}
	public String getBillCount() {
		return billCount;
	}
	public String getSurplusCount() {
		return surplusCount;
	}
	public String getBuyDate() {
		return BuyDate;
	}
	public String getBuyPeople() {
		return BuyPeople;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public void setStartBillNo(String startBillNo) {
		this.startBillNo = startBillNo;
	}
	public void setEndBillNo(String endBillNo) {
		this.endBillNo = endBillNo;
	}
	public void setBillCount(String billCount) {
		this.billCount = billCount;
	}
	public void setSurplusCount(String surplusCount) {
		this.surplusCount = surplusCount;
	}
	public void setBuyDate(String buyDate) {
		BuyDate = buyDate;
	}
	public void setBuyPeople(String buyPeople) {
		BuyPeople = buyPeople;
	}
	
}
