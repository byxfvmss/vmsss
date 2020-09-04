package com.cjit.vms.input.model;

public class VendorInfo {

	private String vendorId;// vendor_id varchar2(50)
	private String vendorCName;// vendor_cname varchar2(100) not null,/*供应商中文名称*/
	private String vendorEName;// vendor_ename varchar2(100) not null,/*供应商英文名称*/
	private String vendorTaxNo;// vendor_taxno varchar2(25) not null,/*供应商纳税人识别号，即税务登记号*/
	private String vendorAccount;// vendor_account varchar2(50) not null,/*账号，主键*/
	private String vendorCBank;// vendor_cbank varchar2(100) not null,/*供应商开户银行中文名称*/
	private String vendorEBank;// vendor_ebank varchar2(100) not null,/*供应商开户银行英文名称*/
	private String vendorPhone;// vendor_phone varchar2(50) not null,/*供应商电话*/
	private String vendorEmail;// vendor_email varchar2(100),/*供应商地址*/
	private String vendorAddress;// vendor_address varchar2(100),/*供应商地址*/
	private String vendorLinkman;// vendor_linkman varchar2(100) not null,/*供应商联系人*/
	private String addressee;// addressee varchar2(100),/*供应商收件人*/
	private String addresseePhone;// addressee_phone varchar2(50),/*供应商收件人电话*/
	private String addresseeAddress;// addressee_address varchar2(100),/*供应商收件地址*/
	private String addresseeAddressdetail;// addressee_addressdetail varchar2(100),/*供应商详细收件地址*/
	private String addresseeZipcode;// addressee_zipcode varchar2(100),/*供应商收件邮编*/
	private String taxpayerType;  //供应商纳税人类别  S/G/O/I    S-小规模纳税人    G-一般纳税人    O-其他    I-个体纳税人
	private String dataOperationLabel;//DATA_OPERATION_LABEL	VARCHAR2(1)	N			数据操作标志
	private String dataAuditStatus;//DATA_AUDIT_STATUS	VARCHAR2(1)	N			数据审核状态
	private String batchNo;//DATA_AUDIT_STATUS	VARCHAR2(1)	N			数据审核状态
	
	/**
	 * <p>构造函数名称: |描述:无参构造函数 </p>
	 */	
	public VendorInfo() {
		
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorCName() {
		return vendorCName;
	}

	public void setVendorCName(String vendorCName) {
		this.vendorCName = vendorCName;
	}

	public String getVendorEName() {
		return vendorEName;
	}

	public void setVendorEName(String vendorEName) {
		this.vendorEName = vendorEName;
	}

	public String getVendorTaxNo() {
		return vendorTaxNo;
	}

	public void setVendorTaxNo(String vendorTaxNo) {
		this.vendorTaxNo = vendorTaxNo;
	}

	public String getVendorAccount() {
		return vendorAccount;
	}

	public void setVendorAccount(String vendorAccount) {
		this.vendorAccount = vendorAccount;
	}

	public String getVendorCBank() {
		return vendorCBank;
	}

	public void setVendorCBank(String vendorCBank) {
		this.vendorCBank = vendorCBank;
	}

	public String getVendorEBank() {
		return vendorEBank;
	}

	public void setVendorEBank(String vendorEBank) {
		this.vendorEBank = vendorEBank;
	}

	public String getVendorPhone() {
		return vendorPhone;
	}

	public void setVendorPhone(String vendorPhone) {
		this.vendorPhone = vendorPhone;
	}

	public String getVendorEmail() {
		return vendorEmail;
	}

	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public String getVendorLinkman() {
		return vendorLinkman;
	}

	public void setVendorLinkman(String vendorLinkman) {
		this.vendorLinkman = vendorLinkman;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getAddresseePhone() {
		return addresseePhone;
	}

	public void setAddresseePhone(String addresseePhone) {
		this.addresseePhone = addresseePhone;
	}

	public String getAddresseeAddress() {
		return addresseeAddress;
	}

	public void setAddresseeAddress(String addresseeAddress) {
		this.addresseeAddress = addresseeAddress;
	}

	public String getAddresseeAddressdetail() {
		return addresseeAddressdetail;
	}

	public void setAddresseeAddressdetail(String addresseeAddressdetail) {
		this.addresseeAddressdetail = addresseeAddressdetail;
	}

	public String getAddresseeZipcode() {
		return addresseeZipcode;
	}

	public void setAddresseeZipcode(String addresseeZipcode) {
		this.addresseeZipcode = addresseeZipcode;
	}

	public String getTaxpayerType() {
		return taxpayerType;
	}

	public void setTaxpayerType(String taxpayerType) {
		this.taxpayerType = taxpayerType;
	}

	public String getDataOperationLabel() {
		return dataOperationLabel;
	}

	public void setDataOperationLabel(String dataOperationLabel) {
		this.dataOperationLabel = dataOperationLabel;
	}

	public String getDataAuditStatus() {
		return dataAuditStatus;
	}

	public void setDataAuditStatus(String dataAuditStatus) {
		this.dataAuditStatus = dataAuditStatus;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
}
