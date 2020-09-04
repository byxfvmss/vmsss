package com.cjit.vms.trans.model;

import java.util.List;

public class IntegrityCheckAccount {
	
	private String instCode;//机构
	private String instName;//机构名称
	private String goodsNo;//商品编号
	private String goodsName;//商品名称
	private String transId;//交易ID
	private String customerId;//客户号
	private String amount;//金额
	private String netPrice;//净价
	private String taxAmt;//税额
	private String devAmt;//差额
	private List instIdList;
	private String customerCname;//客户纳税人中文名称

	public IntegrityCheckAccount(){
		
	}
	
	public String getCustomerCname() {
		return customerCname;
	}

	public void setCustomerCname(String customerCname) {
		this.customerCname = customerCname;
	}

	public List getInstIdList() {
		return instIdList;
	}

	public void setInstIdList(List instIdList) {
		this.instIdList = instIdList;
	}

	
	public String getInstCode() {
		return instCode;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public String getTransId() {
		return transId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public String getAmount() {
		return amount;
	}
	public String getNetPrice() {
		return netPrice;
	}
	public String getTaxAmt() {
		return taxAmt;
	}
	public String getDevAmt() {
		return devAmt;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setNetPrice(String netPrice) {
		this.netPrice = netPrice;
	}
	public void setTaxAmt(String taxAmt) {
		this.taxAmt = taxAmt;
	}
	public void setDevAmt(String devAmt) {
		this.devAmt = devAmt;
	}
	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}
	
}
