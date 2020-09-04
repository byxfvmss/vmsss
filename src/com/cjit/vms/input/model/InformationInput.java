package com.cjit.vms.input.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InformationInput {

	// -------------交易---------------------------------
	private String dealNo; //交易日期
	private String transDate; //交易日期
	private String bankCode; //交易机构
	private String bankName; //交易机构名称
	private BigDecimal amtCny;//金额_人民币
	private BigDecimal taxAmtCny;//税额_人民币
	private BigDecimal incomeCny;//支出_人民币
	private String billCode; // 发票代码
	private String billNo; //发票号码
	
	//----------------供应商----------------------------------
	private String  vendorName;// 销方纳税人名称
	private String  vendorTaxNo;// 销方纳税人识别号
	private String vendorId; // 供应商编号
	
	// --------------------页面查询条件----------------------------------------------------
	private String billStartDate;
	private String billEndDate;
	private String transBeginDate;
	private String transEndDate;
	private String instCode;//机构
	private String datastatus;//状态
	private String fapiaoType;// 发票类型
	
	// 修复bug 交易查询结果中无机构权限控制的体现 start
	private List lstAuthInstId = new ArrayList();//机构list
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	// 修复bug 交易查询结果中无机构权限控制的体现 end
	
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public BigDecimal getAmtCny() {
		return amtCny;
	}
	public void setAmtCny(BigDecimal amtCny) {
		this.amtCny = amtCny;
	}
	public BigDecimal getTaxAmtCny() {
		return taxAmtCny;
	}
	public void setTaxAmtCny(BigDecimal taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}
	public BigDecimal getIncomeCny() {
		return incomeCny;
	}
	public void setIncomeCny(BigDecimal incomeCny) {
		this.incomeCny = incomeCny;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillStartDate() {
		return billStartDate;
	}
	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}
	public String getBillEndDate() {
		return billEndDate;
	}
	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}
	public String getTransBeginDate() {
		return transBeginDate;
	}
	public void setTransBeginDate(String transBeginDate) {
		this.transBeginDate = transBeginDate;
	}
	public String getTransEndDate() {
		return transEndDate;
	}
	public void setTransEndDate(String transEndDate) {
		this.transEndDate = transEndDate;
	}
	public String getDealNo() {
		return dealNo;
	}
	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorTaxNo() {
		return vendorTaxNo;
	}
	public void setVendorTaxNo(String vendorTaxNo) {
		this.vendorTaxNo = vendorTaxNo;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getDatastatus() {
		return datastatus;
	}
	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	
	 
}
