package com.cjit.ws.entity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cjit.common.util.StringUtil;
import com.cjit.vms.trans.util.DataUtil;

/**
 * 票据信息类
 * 
 * @author Larry
 */
public class BillInfo {

	// 数据库属性
	private String billId;// 票据ID
	private String billCode;// 票据代码
	private String billNo;// 票据号码
	private String billDate;// 开票日期
	private String printDate;
	//private String pri
	
	
	public String getPrintDate() {
		return printDate;
	}

	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}

	private String customerName;// 客户纳税人名称
	private String customerTaxno;// 客户纳税人识别号
	private String customerAddressandphone;// 客户地址电话
	private String customerBankandaccount;// 客户银行账号
	private BigDecimal amtSum;// 合计金额
	private BigDecimal taxAmtSum;// 合计税额
	private BigDecimal sumAmt;// 价税合计
	private String amtSumStrs;// 合计金额
	private String taxAmtSumStrs;// 合计税额
	private String sumAmtStrs;// 价税合计
	private String remark;// 备注
	private String drawer;// 开票人(数据库存储用户ID)
	private String reviewer;// 复核人(数据库存储人员ID)
	private String payee;// 收款人(数据库存储人员姓名)
	private String name;// 我方纳税人名称
	private String taxno;// 我方纳税人识别号
	private String addressandphone;// 我方地址电话
	private String bankandaccount;// 我方银行账号
	private String instCode;// 所属机构
	private String cancelInitiator;// 撤销发起人
	private String cancelAuditor;// 撤销审核人
	private String cancelDate;// 撤销开票日期
	private String machineNo;// 开票机号
	private String noticeNo; // 通知单编号
	private String oriBillCode;// 原发票代码
	private String oriBillNo;// 原发票号码
	private String dataStatus;// 状态
	private String description;// 操作说明
	private String isHandiwork;// 是否手工录入
	private String fapiaoType;// 发票类型
	private String applyDate;  //申请开票日期
	// 辅助属性
	private String billBeginDate;// 开票日期起始
	private String billEndDate;// 开票日期终止
	private String applyBeginDate; //申请开票日期起始
	private String applyEndDate;   //申请开票日期终止
	private String searchFlag;// 查询标志
	private String drawerName;// 开票人姓名
	private String reviewerName;// 复核人姓名
	private String orderBy;// 查询排序
	private String transId;// 交易ID
	private List billItemList; // 票据明细信息
	private BigDecimal sumAmtBegin;// 价税合计最小
	private BigDecimal sumAmtEnd;// 价税合计最大
	private String bankName;// 我方开票机构
	//VMS_BILL_ITEM_INFO表辅助属性ys
	private BigDecimal taxRate;// 税率
	//VMS_CUSTOMER_INFO表辅助属性ys noticeNo
	private String bankCode;// 交易发生机构
	// VMS_TRANS_INFO表辅助属性ys
	private String customerId;// 客户号
	private BigDecimal balance;// 未开票金额
	//U_BASE_INST 表辅助属性 ys
	private String instId;
	// VMS_CUSTOMER_INFO表辅助属性czl
	private String customerEMail;// 客户邮箱地址

	// VMS_EMS_INFO表辅助属性czl
	private String customerLinkman;// 客户联系人
	private String addressee;// 客户收件人
	private String addresseePhone;// 客户收件人电话
	private String addresseeAddress;// 客户收件地址
	private String addresseeAddressdetail;// 客户详细收件地址
	private String addresseeZipcode;// 客户收件邮编
	private String fedexExpress;// 快递公司
	private String emsNo;// 快递单号
	private String emsStatus;// 快递状态
	private String sender;// 寄件人
	// private String statusView;//快递状态说明
	private String operateStatus;
	private String issueType;// 开具类型
	private String cancelInitiatorName;// 撤销发起人
	private String cancelAuditorName;// 撤销审核人

	private String taxDiskNo;
	private String userId;// 用户ID
	private List lstAuthInstId = new ArrayList();//机构list
	private List authInstList = new ArrayList();//机构list
	private String gjType;//勾稽状态
	
	//-------------------------票据对应的商品信息属性---------------------------
	private String goodsName;
	private String payeeName;
	private String startDate;
	private String endDate;
	private String open;
	private String insureId;
	private String cancelReason;
	
	private Object[] customerList;


	/**
	 * Abel:大都会添加字段
	 */
	private String cherNum;//保单号
	private String repNum;//旧保单号
	private String ttmpRcno;//投保单号
	private String feeTyp;//费用类型
	private String billFreq;//交费频率
	private String polYear;//保单年度
	private String hissDte;//承保日期
	private String dsouRce;//数据来源
	private String chanNel;//渠道
	private String chanNels;//渠道
	private String premTerm;//期数
	private String hissBeginDte;//开始
	private String hissEndDte;//结束
	private String instFrom;
	private String instTo;
	private String instName;
	private String[] bill_id;
	private String expressType;
	private String  certificateInformation;
	private String  proxyName;
	private String  planLongDesc;//主险名称
	private String proDuctName;//产品名称
	private String hisToryFlag;//销项历史交易状态
	
	//添加字段  2016-06-28
	
	public String getDsouRceCh(){
		return DataUtil.getDsouRce(dsouRce);
	}

	public String getBillFreqCh(){
		return DataUtil.getBillFreq(billFreq);
	}

	public String getFeeTypCh(){
		return DataUtil.getFeeTyp(feeTyp);
	}

	public String getChanNelCh(){
		return DataUtil.getChanNel(chanNel);
	}
	
	public String getHisToryFlag() {
		return hisToryFlag != null ? hisToryFlag : "";
	}

	public void setHisToryFlag(String hisToryFlag) {
		if (hisToryFlag != null) {
			this.hisToryFlag = hisToryFlag;
		}
	}

	public String getPlanLongDesc() {
		return planLongDesc != null ? planLongDesc : "";
	}

	public void setPlanLongDesc(String planLongDesc) {
		if (planLongDesc != null) {
			this.planLongDesc = planLongDesc;
		}
	}

	public String getProDuctName() {
		return proDuctName != null ? proDuctName : "";
	}

	public void setProDuctName(String proDuctName) {
		if (proDuctName != null) {
			this.proDuctName = proDuctName;
		}
	}


	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public BillInfo() {

	}

	public BillInfo(String billId, String billNo, String dataStatus,
		String searchFlag, String orderBy, String transId) {
		this.billId = billId;
		this.billNo = billNo;
		this.dataStatus = dataStatus;
		this.searchFlag = searchFlag;
		this.orderBy = orderBy;
		this.transId = transId;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillCode() {
		return billCode == null ? "" : billCode.trim();
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getBillNo() {
		return billNo == null ? "" : billNo.trim();
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillDate() {
		return billDate == null ? "" : billDate.trim();
	}

	public String getBillDateYM() {
		return billDate == null ? "" : billDate.trim().replaceAll("-", "")
				.substring(0, 6);
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getCustomerName() {
		return customerName == null ? "" : customerName.trim();
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerTaxno() {
		return customerTaxno == null ? "" : customerTaxno.trim();
	}

	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}

	public String getCustomerAddressandphone() {
		return customerAddressandphone == null ? "" : customerAddressandphone
				.trim();
	}

	public void setCustomerAddressandphone(String customerAddressandphone) {
		this.customerAddressandphone = customerAddressandphone;
	}

	public String getCustomerBankandaccount() {
		return customerBankandaccount == null ? "" : customerBankandaccount
				.trim();
	}

	public void setCustomerBankandaccount(String customerBankandaccount) {
		this.customerBankandaccount = customerBankandaccount;
	}

	public BigDecimal getAmtSum() {
		return amtSum;
	}

	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}

	public void setAmtSumStr(String amtSum) {
		if (StringUtil.isNotEmpty(amtSum)) {
			this.amtSum = new BigDecimal(amtSum);
		}
	}

	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}

	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}

	public void setTaxAmtSumStr(String taxAmtSum) {
		if (StringUtil.isNotEmpty(taxAmtSum)) {
			this.taxAmtSum = new BigDecimal(taxAmtSum);
		}
	}

	public BigDecimal getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}

	public void setSumAmtStr(String sumAmt) {
		if (StringUtil.isNotEmpty(sumAmt)) {
			this.sumAmt = new BigDecimal(sumAmt);
		}
	}
	
	public String getRemark() {
		return remark == null ? "" : remark.trim();
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDrawer() {
		return drawer == null ? "" : drawer.trim();
	}

	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}

	public String getReviewer() {
		return reviewer == null ? "" : reviewer.trim();
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String getPayee() {
		return payee == null ? "" : payee.trim();
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getName() {
		return name==null?"":name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxno() {
		return taxno==null?"":taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public String getAddressandphone() {
		return addressandphone==null?"":addressandphone;
	}

	public void setAddressandphone(String addressandphone) {
		this.addressandphone = addressandphone;
	}

	public String getBankandaccount() {
		return bankandaccount==null?"":bankandaccount;
	}

	public void setBankandaccount(String bankandaccount) {
		this.bankandaccount = bankandaccount;
	}

	public String getInstCode() {
		return instCode==null?"":instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getCancelInitiator() {
		return cancelInitiator == null ? "" : cancelInitiator.trim();
	}

	public void setCancelInitiator(String cancelInitiator) {
		this.cancelInitiator = cancelInitiator;
	}

	public String getCancelAuditor() {
		return cancelAuditor==null?"":cancelAuditor;
	}

	public void setCancelAuditor(String cancelAuditor) {
		this.cancelAuditor = cancelAuditor;
	}

	public String getCancelDate() {
		return cancelDate==null?"":cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getMachineNo() {
		return machineNo==null?"":machineNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	public String getNoticeNo() {
		
		return noticeNo==null?"":noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getOriBillCode() {
		return oriBillCode == null ? "" : oriBillCode.trim();
	}

	public void setOriBillCode(String oriBillCode) {
		this.oriBillCode = oriBillCode;
	}

	public String getOriBillNo() {
		return oriBillNo == null ? "" : oriBillNo.trim();
	}

	public void setOriBillNo(String oriBillNo) {
		this.oriBillNo = oriBillNo;
	}

	public String getDataStatus() {
		return dataStatus==null?"":dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getDescription() {
		return description == null ? "" : description.trim();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsHandiwork() {
		return isHandiwork==null?"":isHandiwork;
	}

	public void setIsHandiwork(String isHandiwork) {
		this.isHandiwork = isHandiwork;
	}

	public String getBillBeginDate() {
		return billBeginDate==null?"":billBeginDate;
	}

	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public String getBillEndDate() {
		return billEndDate==null?"":billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getSearchFlag() {
		return searchFlag==null?"":searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getDrawerName() {
		return drawerName==null?"":drawer;
	}

	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
	}

	public String getOrderBy() {
		return orderBy==null?"":orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public List getBillItemList() {
		return billItemList;
	}

	public void setBillItemList(List billItemList) {
		this.billItemList = billItemList;
	}

	public BigDecimal getSumAmtBegin() {
		return sumAmtBegin;
	}

	public void setSumAmtBegin(BigDecimal sumAmtBegin) {
		this.sumAmtBegin = sumAmtBegin;
	}

	public BigDecimal getSumAmtEnd() {
		return sumAmtEnd;
	}

	public void setSumAmtEnd(BigDecimal sumAmtEnd) {
		this.sumAmtEnd = sumAmtEnd;
	}

	public String getReviewerName() {
		return reviewerName==null?"":reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getBankName() {
		return bankName==null?"":bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCustomerEMail() {
		return customerEMail==null?"":customerEMail;
	}

	public void setCustomerEMail(String customerEMail) {
		this.customerEMail = customerEMail;
	}

	public String getCustomerLinkman() {
		return customerLinkman==null?"":customerLinkman;
	}

	public void setCustomerLinkman(String customerLinkman) {
		this.customerLinkman = customerLinkman;
	}

	public String getAddressee() {
		return addressee==null?"":addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getAddresseePhone() {
		return addresseePhone==null?"":addresseePhone;
	}

	public void setAddresseePhone(String addresseePhone) {
		this.addresseePhone = addresseePhone;
	}

	public String getAddresseeAddress() {
		return addresseeAddress==null?"":addresseeAddress;
	}

	public void setAddresseeAddress(String addresseeAddress) {
		this.addresseeAddress = addresseeAddress;
	}

	public String getAddresseeAddressdetail() {
		return addresseeAddressdetail==null?"":addresseeAddressdetail;
	}

	public void setAddresseeAddressdetail(String addresseeAddressdetail) {
		this.addresseeAddressdetail = addresseeAddressdetail;
	}

	public String getAddresseeZipcode() {
		return addresseeZipcode==null?"":addresseeZipcode;
	}

	public void setAddresseeZipcode(String addresseeZipcode) {
		this.addresseeZipcode = addresseeZipcode;
	}

	public String getFedexExpress() {
		return fedexExpress==null?"":fedexExpress;
	}

	public void setFedexExpress(String fedexExpress) {
		this.fedexExpress = fedexExpress;
	}

	public String getEmsNo() {
		return emsNo==null?"":emsNo;
	}

	public void setEmsNo(String emsNo) {
		this.emsNo = emsNo;
	}

	public String getEmsStatus() {
		return emsStatus==null?"":emsStatus;
	}

	public void setEmsStatus(String emsStatus) {
		this.emsStatus = emsStatus;
	}

	public String getSender() {
		return sender==null?"":sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getIssueType() {
		return issueType==null?"":issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getOperateStatus() {
		return operateStatus==null?"":operateStatus;
	}

	public void setOperateStatus(String operateStatus) {
		this.operateStatus = operateStatus;
	}

	// public String getStatusView() {
	// return statusView;
	// }
	//
	// public void setStatusView(String statusView) {
	// this.statusView = statusView;
	// }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTaxDiskNo() {
		return taxDiskNo==null?"":taxDiskNo;
	}

	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}

	public String getGoodsName() { 
		return goodsName==null?"":goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getApplyDate() {
		return applyDate==null?"":applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getApplyBeginDate() {
		return applyBeginDate==null?"":applyBeginDate;
	}

	public void setApplyBeginDate(String applyBeginDate) {
		this.applyBeginDate = applyBeginDate;
	}

	public String getApplyEndDate() {
		return applyEndDate;
	}

	public void setApplyEndDate(String applyEndDate) {
		this.applyEndDate = applyEndDate;
	}

	public String getCancelInitiatorName() {
		return cancelInitiatorName==null?"":cancelInitiatorName;
	}

	public void setCancelInitiatorName(String cancelInitiatorName) {
		this.cancelInitiatorName = cancelInitiatorName;
	}

	public String getCancelAuditorName() {
		return cancelAuditorName==null?"":cancelAuditorName;
	}

	public void setCancelAuditorName(String cancelAuditorName) {
		this.cancelAuditorName = cancelAuditorName;
	}

	public String getBankCode() {
		return bankCode==null?"":bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	public String getGjType() {
		return gjType;
	}

	public void setGjType(String gjType) {
		this.gjType = gjType;
	}

	public String getPayeeName() {
		return payeeName==null?"":payee;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCancelReason() {
		return cancelReason==null?"":cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getAmtSumStrs() {
		return amtSumStrs;
	}

	public String getTaxAmtSumStrs() {
		return taxAmtSumStrs;
	}

	public String getSumAmtStrs() {
		return sumAmtStrs;
	}

	public void setAmtSumStrs(String amtSumStrs) {
		this.amtSumStrs = amtSumStrs;
	}

	public void setTaxAmtSumStrs(String taxAmtSumStrs) {
		this.taxAmtSumStrs = taxAmtSumStrs;
	}

	public void setSumAmtStrs(String sumAmtStrs) {
		this.sumAmtStrs = sumAmtStrs;
	}

	public Object[] getCustomerList() {
		return customerList;
	}

	public void setCustomerList(Object[] customerList) {
		this.customerList = customerList;
	}

	public List getAuthInstList() {
		return authInstList;
	}

	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}




	public void setExpressType(String expressType) {
		this.expressType = expressType;
	}

	public String getCertificateInformation() {
		return certificateInformation;
	}

	public void setCertificateInformation(String certificateInformation) {
		this.certificateInformation = certificateInformation;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String getCherNum() {
		return cherNum;
	}

	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}

	public String getRepNum() {
		return repNum;
	}

	public void setRepNum(String repNum) {
		this.repNum = repNum;
	}

	public String getTtmpRcno() {
		return ttmpRcno;
	}

	public void setTtmpRcno(String ttmpRcno) {
		this.ttmpRcno = ttmpRcno;
	}


	
	public String getFeeTyp() {
		return feeTyp;
	}

	public void setFeeTyp(String feeTyp) {
		this.feeTyp = feeTyp;
	}

	public String getBillFreq() {
		return billFreq;
	}

	public String getPolYear() {
		return polYear;
	}

	public void setPolYear(String polYear) {
		this.polYear = polYear;
	}

	public String getHissDte() {
		return hissDte;
	}

	public void setHissDte(String hissDte) {
		this.hissDte = hissDte;
	}

	public String getDsouRce() {
		return dsouRce;
	}

	public void setDsouRce(String dsouRce) {
		this.dsouRce = dsouRce;
	}

	public String getChanNel() {
		return chanNel;
	}

	public void setChanNel(String chanNel) {
		this.chanNel = chanNel;
	}

	public String getChanNels() {
		return chanNels;
	}

	public void setChanNels(String chanNels) {
		this.chanNels = chanNels;
	}

	public String getPremTerm() {
		return premTerm;
	}

	public void setPremTerm(String premTerm) {
		this.premTerm = premTerm;
	}

	public String getHissBeginDte() {
		return hissBeginDte;
	}

	public void setHissBeginDte(String hissBeginDte) {
		this.hissBeginDte = hissBeginDte;
	}

	public String getHissEndDte() {
		return hissEndDte;
	}

	public void setHissEndDte(String hissEndDte) {
		this.hissEndDte = hissEndDte;
	}

	public String getInstFrom() {
		return instFrom;
	}

	public void setInstFrom(String instFrom) {
		this.instFrom = instFrom;
	}

	public String getInstTo() {
		return instTo;
	}

	public void setInstTo(String instTo) {
		this.instTo = instTo;
	}

	public String[] getBill_id() {
		return bill_id;
	}

	public void setBill_id(String[] bill_id) {
		this.bill_id = bill_id;
	}

	public String getExpressType() {
		return expressType;
	}

	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
	}

	public String getInstName() {
		return instName;
	}


	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getInsureId() {
		return insureId;
	}

	public void setInsureId(String insureId) {
		this.insureId = insureId;
	}
}
