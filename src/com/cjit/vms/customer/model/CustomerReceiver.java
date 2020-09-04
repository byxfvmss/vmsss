package com.cjit.vms.customer.model;

public class CustomerReceiver {

	private String id;
	private String customerId;
	private String receiverType;
	private String receiverTypeName;
	private String documentsType;
	private String documentsTypeName;
	private String documentsCode;
	private String receiverName;
	private String remark;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getDocumentsType() {
		return documentsType;
	}

	public void setDocumentsType(String documentsType) {
		this.documentsType = documentsType;
	}

	public String getDocumentsCode() {
		return documentsCode;
	}

	public void setDocumentsCode(String documentsCode) {
		this.documentsCode = documentsCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverTypeName() {
		return receiverTypeName;
	}

	public void setReceiverTypeName(String receiverTypeName) {
		this.receiverTypeName = receiverTypeName;
	}

	public String getDocumentsTypeName() {
		return documentsTypeName;
	}

	public void setDocumentsTypeName(String documentsTypeName) {
		this.documentsTypeName = documentsTypeName;
	}

}
