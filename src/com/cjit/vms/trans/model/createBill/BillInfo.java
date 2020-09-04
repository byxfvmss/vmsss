package com.cjit.vms.trans.model.createBill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BillInfo {
	private String billId; // 票据ID
	private String billCode; // 发票代码
	private String billNo; // 发票号码
	private String billDate; // 开票日期
	private String customerName; // 客户纳税人名称
	private String customerTaxno; // 客户纳税人识别号
	private String customerAddressandphone; // 客户地址电话
	private String customerBankandaccount; // 客户银行账号
	private BigDecimal amtSum = new BigDecimal("0"); // 合计金额
	private BigDecimal taxAmtSum = new BigDecimal("0"); // 合计税额
	private BigDecimal sumAmt = new BigDecimal("0"); // 价税合计
	private String remark; // 备注
	private String drawer; // 开票人
	private String reviewer; // 复核人
	private String payee; // 收款人
	private String name; // 我方纳税人名称
	private String taxno; // 我方纳税人识别号
	private String addressandphone; // 我方地址电话
	private String bankandaccount; // 我方银行账号
	private String instcode; // 所属机构 合并开票时根据机构进行汇总开票 该字段同时也是用户权限判断字段
	private String cancelInitiator; // 撤销发起人
	private String cancelAuditor; // 撤销审核人
	private String taxDiskNo; // 税控盘号
	private String machineNo; // 开票机号
	private String noticeNo; // 通知单编号
	private String oriBillCode; // 原始票据代码
	private String oriBillNo; // 原始票据号码
	private String datastatus; // 状态 1-编辑待提交 2-提交待审核 3-审核通过 4-无需审核 5-已开具 7-开具失败
								// 8-已打印 9-打印失败 10-已快递 11-已签收 12-已抄报 13-作废待审核
								// 14-作废已审核 15-已作废 16-红冲待审核 17-红冲已审核 18-已红冲
								// 19-已收回 99-生效完成
	private String description; // 操作说明
	private String isHandiwork; // 是否手工录入 1-自动开票;2-人工审核;3-人工开票
	private String issueType; // 开具类型1-单笔;2-合并;3-拆分
	private String fapiaoType; // 发票类型 0-增值税专用发票;1-增值税普通发票
	private String operatestatus; // 原票据状态 现在改字段用于存放一张票据整个流程记录 比如01030507
	private String applyDate; // 申请日期
	private String balance; // 发票余额
	private String cancelReason; // 退回原因
	private String customerId; // 客户ID
	private String insureId;// 保单号
	private String cherNum;// 保单号
	private String repNum;// 旧保单号
	private String ttmpRcno;// 投保单号
	private String feeTyp;// 费用类型
	private String billFreq;// 交费频率
	private Integer polYear;// 保单年度
	private String dsouRce;// 数据来源
	private String chanNel;// 渠道
	private Integer premTerm;// 期数
	private String hissBeginDte;// 开始
	private String hissEndDte;// 结束
	private String altref;// 收入会计科目
	private String[] selectTransIds;

	private String hissDte;// 承保日期
//	private String hissDteStr;// 承保日期
	private Date instFrom;// 交费起始日期
	private String instFromStr;// 交费起始日期
	private Date instTo;// 交费终止日期
	private String instToStr;// 交费终止日期
	private Date occDate;// 生效日期
	private String occDateStr;// 生效日期

	private String planLongDesc;// 主险名称

	private String premTermArrayMin;

	private String premTermArrayMax;

	private String hisToryFlag;

	public BillInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getBillId() {
		return billId;
	}

	public String getBillCode() {
		return billCode;
	}

	public String getBillNo() {
		return billNo;
	}

	public String getBillDate() {
		return billDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String FapiaoType() {
		return customerTaxno;
	}

	public String getCustomerAddressandphone() {
		return customerAddressandphone;
	}

	public String getCustomerBankandaccount() {
		return customerBankandaccount;
	}

	public BigDecimal getAmtSum() {
		return amtSum;
	}

	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}

	public BigDecimal getSumAmt() {
		return sumAmt;
	}

	public String getRemark() {
		return remark;
	}

	public String getDrawer() {
		return drawer;
	}

	public String getReviewer() {
		return reviewer;
	}

	public String getPayee() {
		return payee;
	}

	public String getName() {
		return name;
	}

	public String getTaxno() {
		return taxno;
	}

	public String getAddressandphone() {
		return addressandphone;
	}

	public String getBankandaccount() {
		return bankandaccount;
	}

	public String getInstcode() {
		return instcode;
	}

	public String getCancelInitiator() {
		return cancelInitiator;
	}

	public String getCancelAuditor() {
		return cancelAuditor;
	}

	public String getTaxDiskNo() {
		return taxDiskNo;
	}

	public String getMachineNo() {
		return machineNo;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public String getOriBillCode() {
		return oriBillCode;
	}

	public String getOriBillNo() {
		return oriBillNo;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public String getDescription() {
		return description;
	}

	public String getIsHandiwork() {
		return isHandiwork;
	}

	public String getIssueType() {
		return issueType;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public String getOperatestatus() {
		return operatestatus;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public String getBalance() {
		return balance;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}

	public void setCustomerAddressandphone(String customerAddressandphone) {
		this.customerAddressandphone = customerAddressandphone;
	}

	public void setCustomerBankandaccount(String customerBankandaccount) {
		this.customerBankandaccount = customerBankandaccount;
	}

	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}

	public void setAmtSumStr(String amtSum) {
		if (null == amtSum || "".equals(amtSum) || "null".equals(amtSum)) {
			amtSum = "0";
		}
		this.amtSum = new BigDecimal(amtSum);
	}

	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}

	public void setTaxAmtSumStr(String taxAmtSum) {
		if (null == taxAmtSum || "".equals(taxAmtSum) || "null".equals(taxAmtSum)) {
			taxAmtSum = "0";
		}
		this.taxAmtSum = new BigDecimal(taxAmtSum);
	}

	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}

	public void setSumAmtStr(String sumAmt) {
		if (null == sumAmt || "".equals(sumAmt) || "null".equals(sumAmt)) {
			sumAmt = "0";
		}
		this.sumAmt = new BigDecimal(sumAmt);
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public void setAddressandphone(String addressandphone) {
		this.addressandphone = addressandphone;
	}

	public void setBankandaccount(String bankandaccount) {
		this.bankandaccount = bankandaccount;
	}

	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}

	public void setCancelInitiator(String cancelInitiator) {
		this.cancelInitiator = cancelInitiator;
	}

	public void setCancelAuditor(String cancelAuditor) {
		this.cancelAuditor = cancelAuditor;
	}

	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public void setOriBillCode(String oriBillCode) {
		this.oriBillCode = oriBillCode;
	}

	public void setOriBillNo(String oriBillNo) {
		this.oriBillNo = oriBillNo;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIsHandiwork(String isHandiwork) {
		this.isHandiwork = isHandiwork;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public void setOperatestatus(String operatestatus) {
		this.operatestatus = operatestatus;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getInsureId() {
		return insureId;
	}

	public void setInsureId(String insureId) {
		this.insureId = insureId;
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

	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
	}

	public Integer getPolYear() {
		return polYear;
	}

	public void setPolYear(Integer polYear) {
		this.polYear = polYear;
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

	public Integer getPremTerm() {
		return premTerm;
	}

	public void setPremTerm(Integer premTerm) {
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

	public String getAltref() {
		return altref;
	}

	public void setAltref(String altref) {
		this.altref = altref;
	}

	public String[] getSelectTransIds() {
		return selectTransIds;
	}

	public void setSelectTransIds(String[] selectTransIds) {
		this.selectTransIds = selectTransIds;
	}

	public String getHissDte() {
		return hissDte;
	}

	public void setHissDte(String hissDte) {
		this.hissDte = hissDte;
	}

//	public String getHissDteStr() {
//		return hissDteStr;
//	}
//
//	public void setHissDteStr(String hissDteStr) {
//		this.hissDteStr = hissDteStr;
//	}

	public Date getInstFrom() {
		return instFrom;
	}

	public void setInstFrom(Date instFrom) {
		this.instFrom = instFrom;
	}

	public String getInstFromStr() {
		return instFromStr;
	}

	public void setInstFromStr(String instFromStr) {
		this.instFromStr = instFromStr;
	}

	public Date getInstTo() {
		return instTo;
	}

	public void setInstTo(Date instTo) {
		this.instTo = instTo;
	}

	public String getInstToStr() {
		return instToStr;
	}

	public void setInstToStr(String instToStr) {
		this.instToStr = instToStr;
	}

	public String getCustomerTaxno() {
		return customerTaxno;
	}

	public Date getOccDate() {
		return occDate;
	}

	public void setOccDate(Date occDate) {
		this.occDate = occDate;
	}

	public String getOccDateStr() {
		return occDateStr;
	}

	public void setOccDateStr(String occDateStr) {
		this.occDateStr = occDateStr;
	}

	public String getPlanLongDesc() {
		return planLongDesc;
	}

	public void setPlanLongDesc(String planLongDesc) {
		this.planLongDesc = planLongDesc;
	}

	public String getPremTermArrayMin() {
		return premTermArrayMin;
	}

	public void setPremTermArrayMin(String premTermArrayMin) {
		this.premTermArrayMin = premTermArrayMin;
	}

	public String getPremTermArrayMax() {
		return premTermArrayMax;
	}

	public void setPremTermArrayMax(String premTermArrayMax) {
		this.premTermArrayMax = premTermArrayMax;
	}

	public String getHisToryFlag() {
		return hisToryFlag;
	}

	public void setHisToryFlag(String hisToryFlag) {
		this.hisToryFlag = hisToryFlag;
	}
}