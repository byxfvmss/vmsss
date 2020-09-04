package com.cjit.vms.trans.model.billExpress;

public class BillExpress {
	/*** 票据ID ***/
	private String billId;
	/*** 发票代码 ***/
	private String billCode;
	/*** 发票号码 ***/
	private String billNo;
	/*** 开票日期 ***/
	private String billDate;
	/*** 客户纳税人名称 ***/
	private String customerName;
	/*** 客户纳税人识别号 ***/
	private String customerTaxno;
	/*** 客户地址电话 ***/
	private String customerAddressandphone;
	/*** 客户银行账号 ***/
	private String customerBankandaccount;
	/*** 合计金额 ***/
	private String amtSum;
	/*** 合计税额 ***/
	private String taxAmtSum;
	/*** 价税合计 ***/
	private String sumAmt;
	/*** 备注 ***/
	private String remark;
	/*** 开票人 ***/
	private String drawer;
	/*** 复核人 ***/
	private String reviewer;
	/*** 收款人 ***/
	private String payee;
	/*** 我方纳税人名称 ***/
	private String name;
	/*** 我方纳税人识别号 ***/
	private String taxno;
	/*** 我方地址电话 ***/
	private String addressandphone;
	/*** 我方银行账号 ***/
	private String bankandaccount;
	/*** 所属机构 合并开票时根据机构进行汇总开票 该字段同时也是用户权限判断字段 ***/
	private String instcode;
	/*** 撤销发起人 ***/
	private String cancelInitiator;
	/*** 撤销审核人 ***/
	private String cancelAuditor;
	/*** 税控盘号 ***/
	private String taxDiskNo;
	/*** 开票机号 ***/
	private String machineNo;
	/*** 通知单编号 ***/
	private String noticeNo;
	/*** 原始票据代码 ***/
	private String oriBillCode;
	/*** 原始票据号码 ***/
	private String oriBillNo;
	/***
	 * 状态 1-编辑待提交 2-提交待审核 3-审核通过 4-无需审核 5-已开具 7-开具失败 8-已打印 9-打印失败 10-已快递 11-已签收
	 * 12-已抄报 13-作废待审核 14-作废已审核 15-已作废 16-红冲待审核 17-红冲已审核 18-已红冲 19-已收回 99-生效完成
	 ***/
	private String datastatus;
	/*** 操作说明 ***/
	private String description;
	/*** 是否手工录入 1-自动开票;2-人工审核;3-人工开票 ***/
	private String isHandiwork;
	/*** 开具类型1-单笔;2-合并;3-拆分 ***/
	private String issueType;
	/*** 发票类型 0-增值税专用发票;1-增值税普通发票 ***/
	private String fapiaoType;
	/*** 原票据状态 现在改字段用于存放一张票据整个流程记录 比如01030507 ***/
	private String operatestatus;
	/*** 申请日期 ***/
	private String applyDate;
	/*** 发票余额 ***/
	private String balance;
	/*** 退回原因 ***/
	private String cancelReason;
	/*** 客户ID ***/
	private String customerId;
	
	
	/***		***/
	private String receiveType;
	/***		***/
	private String receiveStatus;
	/***		***/
	private String customerAddressId;
	/***		***/
	private String customerReceiverId;
	/***		***/
	private String updateUser;
	/***		***/
	private String updateDatetime;

	/***
	 * 收件人信息
	 */
	private String receiverType;
	private String receiverTypeName;
	private String documentsType;
	private String documentsTypeName;
	private String documentsCode;
	private String receiverName;
	private String receiverRemark;

	/***
	 * 寄送地址
	 */
	private String contactPerson;
	private String contactPhone;
	private String contactEmail;
	private String addressTag;
	private String receiver;
	private String receiverPhone;
	private String receiverAddress;
	private String postCode;
	private String addressRemark;

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
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

	public String getCustomerAddressandphone() {
		return customerAddressandphone;
	}

	public void setCustomerAddressandphone(String customerAddressandphone) {
		this.customerAddressandphone = customerAddressandphone;
	}

	public String getCustomerBankandaccount() {
		return customerBankandaccount;
	}

	public void setCustomerBankandaccount(String customerBankandaccount) {
		this.customerBankandaccount = customerBankandaccount;
	}

	public String getAmtSum() {
		return amtSum;
	}

	public void setAmtSum(String amtSum) {
		this.amtSum = amtSum;
	}

	public String getTaxAmtSum() {
		return taxAmtSum;
	}

	public void setTaxAmtSum(String taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}

	public String getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(String sumAmt) {
		this.sumAmt = sumAmt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDrawer() {
		return drawer;
	}

	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxno() {
		return taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public String getAddressandphone() {
		return addressandphone;
	}

	public void setAddressandphone(String addressandphone) {
		this.addressandphone = addressandphone;
	}

	public String getBankandaccount() {
		return bankandaccount;
	}

	public void setBankandaccount(String bankandaccount) {
		this.bankandaccount = bankandaccount;
	}

	public String getInstcode() {
		return instcode;
	}

	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}

	public String getCancelInitiator() {
		return cancelInitiator;
	}

	public void setCancelInitiator(String cancelInitiator) {
		this.cancelInitiator = cancelInitiator;
	}

	public String getCancelAuditor() {
		return cancelAuditor;
	}

	public void setCancelAuditor(String cancelAuditor) {
		this.cancelAuditor = cancelAuditor;
	}

	public String getTaxDiskNo() {
		return taxDiskNo;
	}

	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}

	public String getMachineNo() {
		return machineNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getOriBillCode() {
		return oriBillCode;
	}

	public void setOriBillCode(String oriBillCode) {
		this.oriBillCode = oriBillCode;
	}

	public String getOriBillNo() {
		return oriBillNo;
	}

	public void setOriBillNo(String oriBillNo) {
		this.oriBillNo = oriBillNo;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsHandiwork() {
		return isHandiwork;
	}

	public void setIsHandiwork(String isHandiwork) {
		this.isHandiwork = isHandiwork;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getOperatestatus() {
		return operatestatus;
	}

	public void setOperatestatus(String operatestatus) {
		this.operatestatus = operatestatus;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public String getCustomerAddressId() {
		return customerAddressId;
	}

	public void setCustomerAddressId(String customerAddressId) {
		this.customerAddressId = customerAddressId;
	}

	public String getCustomerReceiverId() {
		return customerReceiverId;
	}

	public void setCustomerReceiverId(String customerReceiverId) {
		this.customerReceiverId = customerReceiverId;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(String updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getReceiverTypeName() {
		return receiverTypeName;
	}

	public void setReceiverTypeName(String receiverTypeName) {
		this.receiverTypeName = receiverTypeName;
	}

	public String getDocumentsType() {
		return documentsType;
	}

	public void setDocumentsType(String documentsType) {
		this.documentsType = documentsType;
	}

	public String getDocumentsTypeName() {
		return documentsTypeName;
	}

	public void setDocumentsTypeName(String documentsTypeName) {
		this.documentsTypeName = documentsTypeName;
	}

	public String getDocumentsCode() {
		return documentsCode;
	}

	public void setDocumentsCode(String documentsCode) {
		this.documentsCode = documentsCode;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverRemark() {
		return receiverRemark;
	}

	public void setReceiverRemark(String receiverRemark) {
		this.receiverRemark = receiverRemark;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getAddressTag() {
		return addressTag;
	}

	public void setAddressTag(String addressTag) {
		this.addressTag = addressTag;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAddressRemark() {
		return addressRemark;
	}

	public void setAddressRemark(String addressRemark) {
		this.addressRemark = addressRemark;
	}

}
