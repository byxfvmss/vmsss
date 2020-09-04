package com.cjit.vms.trans.model.trans;

public class ConnCust {
	private String customerCode; //查询客户号
	private String customerName; //查询客户名
	private String transIds; //交易ID表
	private String transId; //交易ID
	private String orgCustomerId; //原客户号
	private String currCustomerId;//修改客户号
	private String orgCustomerTaxNo;
	private String currCustomerTaxNo;
	private String orgCustomerName;
	private String currCustomerName;
	private String operDate;
	private String operUser;
	private String conCustremark; //备注
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getTransIds() {
		return transIds;
	}
	public void setTransIds(String transIds) {
		this.transIds = transIds;
	}
	public String getOrgCustomerId() {
		return orgCustomerId;
	}
	public void setOrgCustomerId(String orgCustomerId) {
		this.orgCustomerId = orgCustomerId;
	}
	public String getCurrCustomerId() {
		return currCustomerId;
	}
	public void setCurrCustomerId(String currCustomerId) {
		this.currCustomerId = currCustomerId;
	}
	public String getOrgCustomerTaxNo() {
		return orgCustomerTaxNo;
	}
	public void setOrgCustomerTaxNo(String orgCustomerTaxNo) {
		this.orgCustomerTaxNo = orgCustomerTaxNo;
	}
	public String getCurrCustomerTaxNo() {
		return currCustomerTaxNo;
	}
	public void setCurrCustomerTaxNo(String currCustomerTaxNo) {
		this.currCustomerTaxNo = currCustomerTaxNo;
	}
	public String getOrgCustomerName() {
		return orgCustomerName;
	}
	public void setOrgCustomerName(String orgCustomerName) {
		this.orgCustomerName = orgCustomerName;
	}
	public String getCurrCustomerName() {
		return currCustomerName;
	}
	public void setCurrCustomerName(String currCustomerName) {
		this.currCustomerName = currCustomerName;
	}
	public String getOperDate() {
		return operDate;
	}
	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}
	public String getOperUser() {
		return operUser;
	}
	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}
	public String getConCustremark() {
		return conCustremark;
	}
	public void setConCustremark(String conCustremark) {
		this.conCustremark = conCustremark;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
}
