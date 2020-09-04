package com.cjit.vms.trans.model;

public class TaxDiffCheckAccountInfo {
		private String instcode;
		private String contractNo;
		private String goodsNo;
		private String expiryDate;
		private String customerCname;
		private String customerId;
		private Double glConfirmAmt;
		private Double taxConfirmAmt;
		private Double devAmt;
		private String goodsName;
		private String receiveInstId;
		private String receiveInstName;
		private String currency;
		private String transId;
		
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getTransId() {
			return transId;
		}
		public void setTransId(String transId) {
			this.transId = transId;
		}
		public String getReceiveInstId() {
			return receiveInstId;
		}
		public void setReceiveInstId(String receiveInstId) {
			this.receiveInstId = receiveInstId;
		}
		public String getReceiveInstName() {
			return receiveInstName;
		}
		public void setReceiveInstName(String receiveInstName) {
			this.receiveInstName = receiveInstName;
		}
		
		public String getGoodsName() {
			return goodsName;
		}
		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}
		public String getInstcode() {
			return instcode;
		}
		public void setInstcode(String instcode) {
			this.instcode = instcode;
		}
		public String getContractNo() {
			return contractNo;
		}
		public void setContractNo(String contractNo) {
			this.contractNo = contractNo;
		}
		public String getGoodsNo() {
			return goodsNo;
		}
		public void setGoodsNo(String goodsNo) {
			this.goodsNo = goodsNo;
		}
		public String getExpiryDate() {
			return expiryDate;
		}
		public void setExpiryDate(String expiryDate) {
			this.expiryDate = expiryDate;
		}
		public String getCustomerCname() {
			return customerCname;
		}
		public void setCustomerCname(String customerCname) {
			this.customerCname = customerCname;
		}
		public String getCustomerId() {
			return customerId;
		}
		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}
		public Double getGlConfirmAmt() {
			return glConfirmAmt;
		}
		public void setGlConfirmAmt(Double glConfirmAmt) {
			this.glConfirmAmt = glConfirmAmt;
		}
		public Double getTaxConfirmAmt() {
			return taxConfirmAmt;
		}
		public void setTaxConfirmAmt(Double taxConfirmAmt) {
			this.taxConfirmAmt = taxConfirmAmt;
		}
		public Double getDevAmt() {
			return devAmt;
		}
		public void setDevAmt(Double devAmt) {
			this.devAmt = devAmt;
		}
		
		
		
}
