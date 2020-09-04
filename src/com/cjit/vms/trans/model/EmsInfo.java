package com.cjit.vms.trans.model;

public class EmsInfo {
	private String billDate;//开票日期
	private String billCode;//发票代码
	private String billNo;//发票号码
	private String customerName;//客户纳税人名称
	private String customerTaxno;//客户纳税人识别号
	private String customerLinkman;//客户联系人
	private String addressee;//客户收件人
	private String addresseePhone;//客户收件人电话
	private String customerEMail;//客户邮件
	private String addresseeAddress;//客户收件地址
	private String addresseeAddressdetail;//客户详细收件地址
	private String addresseeZipcode;//客户收件邮编
	private String fedexExpress;//快递公司
	private String emsNo;//快递单号
	private String emsStatus;//快递状态
	private String sender;//寄件人
	public EmsInfo() {
		
	}
	public EmsInfo(String billDate, String billCode, String billNo,
			String customerName, String customerTaxno, String customerPhone,
			String customerEmail, String customerAddress,
			String customerLinkman, String addressee, String addresseePhone,
			String addresseeAddress, String addresseeAddressdetail,
			String addresseeZipcode, String fedexExpress, String emsNo,
			String emsStatus, String sender) {
		this.billDate = billDate;
		this.billCode = billCode;
		this.billNo = billNo;
		this.customerName = customerName;
		this.customerTaxno = customerTaxno;
		this.customerLinkman = customerLinkman;
		this.addressee = addressee;
		this.addresseePhone = addresseePhone;
		this.addresseeAddress = addresseeAddress;
		this.addresseeAddressdetail = addresseeAddressdetail;
		this.addresseeZipcode = addresseeZipcode;
		this.fedexExpress = fedexExpress;
		this.emsNo = emsNo;
		this.emsStatus = emsStatus;
		this.sender = sender;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerTaxno() {
		return customerTaxno;
	}
	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}
	public String getCustomerLinkman() {
		return customerLinkman;
	}
	public void setCustomerLinkman(String customerLinkman) {
		this.customerLinkman = customerLinkman;
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
	public String getCustomerEMail() {
		return customerEMail;
	}
	public void setCustomerEMail(String customerEMail) {
		this.customerEMail = customerEMail;
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
	public String getFedexExpress() {
		return fedexExpress;
	}
	public void setFedexExpress(String fedexExpress) {
		this.fedexExpress = fedexExpress;
	}
	public String getEmsNo() {
		return emsNo;
	}
	public void setEmsNo(String emsNo) {
		this.emsNo = emsNo;
	}
	public String getEmsStatus() {
		return emsStatus;
	}
	public void setEmsStatus(String emsStatus) {
		this.emsStatus = emsStatus;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
}
