package com.cjit.vms.system.model;

import com.cjit.vms.trans.util.DataUtil;

public class Customer {
	
	private String customerID;// 客户ID
	private String customerCName;// 客户中文名称
	private String customerTaxno;// 客户纳税人识别号，即税务登记号
	private String customerAccount;// 账号，主键
	private String customerCBank;// 客户开户银行中文名称
	private String customerPhone;// 客户电话
	private String customerEMail;// 客户邮箱地址
	private String customerAddress;// 客户地址
	private String taxPayerType;// 0:纳税人主体  1:开票网点 2:其他管理机构
	private String taxPayerTypeName;// 
	private String customerNationality;//国籍
	private String countrySName;//国籍中文
	private String countryName;//国籍中文
	private String ghoClass;//gho 类
	private String datasSource;//数据来源
	private String batchNo;//批次
	private String   linkName;						//	LINK_NAME	VARCHAR2(30)	Y			联系人名称
	private String   linkPhone;						//	LINK_PHONE	VARCHAR2(15)	Y			联系人电话
	private String 	 linkAddress;						//	LINK_ADDRESS	NVARCHAR2(50)	Y			联系人地址
	private String   customerZipCode;//CUSTOMER_ZIP_CODE	VARCHAR2(10)	Y			客户邮编
	private String payee;
	private String reviewer;
	//临时表
	//DATA_OPERATION_LABEL	VARCHAR2(1)	N			
	//DATA_AUDIT_STATUS	VARCHAR2(1)	N			
	
	/**
	 * @return the payee
	 */
	public String getPayee() {
		return payee;
	}

	/**
	 * @param payee the payee to set
	 */
	public void setPayee(String payee) {
		this.payee = payee;
	}

	/**
	 * @return the reviewer
	 */
	public String getReviewer() {
		return reviewer;
	}

	/**
	 * @param reviewer the reviewer to set
	 */
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	/**
	 * 数据操作标志
	 */
	private String dataOperationLabel;
	/**
	 * 数据审核状态
	 */
	private String dataAuditStatus;
	public String getTaxPayerTypeName() {
		return DataUtil.getTaxpayerTypeCH(taxPayerType);
	}

	public void setTaxPayerTypeName(String taxPayerType) {
		this.taxPayerTypeName = DataUtil.getTaxpayerTypeCH(taxPayerType);
	}

	private String vatInvoice;// MUFG:增值税专用发票，"Y":Yes "N":No
	private String fapiaoType;
	private String fapiaoTypeName;
	private String customerType; //客户类型
	private String customerTypeName;
	private String customerFapiaoFlag;
	private String customerFapiaoFlagName;

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerFapiaoFlag() {
		return customerFapiaoFlag;
	}

	public void setCustomerFapiaoFlag(String customerFapiaoFlag) {
		this.customerFapiaoFlag = customerFapiaoFlag;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerCName() {
		return customerCName;
	}

	public void setCustomerCName(String customerCName) {
		this.customerCName = customerCName;
	}

	public String getCustomerTaxno() {
		return customerTaxno;
	}

	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}

	public String getCustomerAccount() {
		return null==customerAccount?"":customerAccount.trim();
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getCustomerCBank() {
		return null==customerCBank?"":customerCBank.trim();
	}

	public void setCustomerCBank(String customerCBank) {
		this.customerCBank = customerCBank;
	}

	public String getCustomerPhone() {
		return null==customerPhone?"":customerPhone.trim();
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerEMail() {
		return customerEMail;
	}

	public void setCustomerEMail(String customerEMail) {
		this.customerEMail = customerEMail;
	}

	public String getCustomerAddress() {
		return customerAddress == null ? "" : customerAddress.trim();
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getTaxPayerType() {
		return taxPayerType;
	}

	public void setTaxPayerType(String taxPayerType) {
		this.taxPayerType = taxPayerType;
	}

	public String getVatInvoice() {
		return vatInvoice;
	}

	public void setVatInvoice(String vatInvoice) {
		this.vatInvoice = vatInvoice;
	}

	public String getCustomerFapiaoFlagName() {
		return DataUtil.getcustomerFapiaoFlagCH(customerFapiaoFlag);
	}

	public void setCustomerFapiaoFlagName(String customerFapiaoFlag) {
		this.customerFapiaoFlagName = DataUtil.getcustomerFapiaoFlagCH(customerFapiaoFlag);
	}

	public String getFapiaoTypeName() {
		return  DataUtil.getFapiaoTypeCH(fapiaoType);
	}

	public String getCustomerTypeName() {
		return DataUtil.getCustomerTypeCH(customerType);
	}

	public void setFapiaoTypeName(String fapiaoTypeName) {
		this.fapiaoTypeName = fapiaoTypeName;
	}

	public void setCustomerTypeName(String customerTypeName) {
		this.customerTypeName = customerTypeName;
	}

	public String getCustomerNationality() {
		return customerNationality;
	}

	public void setCustomerNationality(String customerNationality) {
		this.customerNationality = customerNationality;
	}

	public String getCountrySName() {
		return countrySName;
	}

	public void setCountrySName(String countrySName) {
		this.countrySName = countrySName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getGhoClass() {
		return ghoClass;
	}

	public void setGhoClass(String ghoClass) {
		this.ghoClass = ghoClass;
	}

	public String getDatasSource() {
		return datasSource;
	}

	public void setDatasSource(String datasSource) {
		this.datasSource = datasSource;
	}

	/**
	 * @return 数据操作标志
	 */
	public String getDataOperationLabel() {
		return dataOperationLabel;
	}

	/**
	 * @param dataOperationLabel 数据操作标志
	 */
	public void setDataOperationLabel(String dataOperationLabel) {
		this.dataOperationLabel = dataOperationLabel;
	}

	/** 
	 * @return 数据审核状态
	 */
	public String getDataAuditStatus() {
		return dataAuditStatus;
	}

	/**
	 * @param dataAuditStatus  数据审核状态
	 */
	public void setDataAuditStatus(String dataAuditStatus) {
		this.dataAuditStatus = dataAuditStatus;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getCustomerZipCode() {
		return customerZipCode;
	}

	public void setCustomerZipCode(String customerZipCode) {
		this.customerZipCode = customerZipCode;
	}

	
	
}
