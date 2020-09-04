package com.cjit.vms.input.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InformationBills {

	// -------------票据---------------------------------
	private String billId;
	private String billCode; // 发票代码
	private String billNo; //发票号码
	private String  billDate;// 开票日期
	private String instCode;// 开票机构
	private String instName;// 开票机构名称
	private BigDecimal amtSum;// 开票金额
	private BigDecimal taxAmtSum;// 税额
	private String faPiaoType;// 发票种类
	private String vendorName;// 供应商名称
	private String vendorTaxNo;// 供应商纳税识别号
	private String dataStatus; // 状态
	private String identifyDate;// 认证日期
	private String scanDate;// 扫描日期

	private List lstAuthInstId = new ArrayList();//机构list
	
	
	// --------------------页面查询条件----------------------------------------------------
	private String billStartDate;
	private String billEndDate;
	private String identifyDateBegin;
	private String identifyDateEnd;
	
	
	public String getIdentifyDateBegin() {
		return identifyDateBegin;
	}
	public void setIdentifyDateBegin(String identifyDateBegin) {
		this.identifyDateBegin = identifyDateBegin;
	}
	public String getIdentifyDateEnd() {
		return identifyDateEnd;
	}
	public void setIdentifyDateEnd(String identifyDateEnd) {
		this.identifyDateEnd = identifyDateEnd;
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
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public BigDecimal getAmtSum() {
		return amtSum;
	}
	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}
	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}
	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}
	public String getFaPiaoType() {
		return faPiaoType;
	}
	public void setFaPiaoType(String faPiaoType) {
		this.faPiaoType = faPiaoType;
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
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getIdentifyDate() {
		return identifyDate;
	}
	public void setIdentifyDate(String identifyDate) {
		this.identifyDate = identifyDate;
	}
	public String getScanDate() {
		return scanDate;
	}
	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
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
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	
	 
	 
}
