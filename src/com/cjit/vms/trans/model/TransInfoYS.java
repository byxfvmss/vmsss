package com.cjit.vms.trans.model;

import java.math.BigDecimal;
import java.util.List;

import com.cjit.common.util.StringUtil;

/**
 * 交易信息类
 */
public class TransInfoYS {

	// 数据库属性
	private String transId;// 交易ID
	private String transDate;// 交易时间（MUFG:VALDAT）
	private String transType;// 交易类型
	private String customerAccount;// 客户账号（关联客户信息）
	private String customerId;// 客户账号（关联客户信息）
	private String taxFlag;// 含税标志 0-是 1-否
	private BigDecimal taxRate;// 税率（MUFG:GSRTRW）
	private BigDecimal amtCny;// 金额_人民币
	private BigDecimal taxAmtCny;// 税额_人民币
	private String isReverse;// 是否冲账 1-是 0-否
	private String bankCode;// 交易发生机构（关联我方开票机构信息）
	private String remark;// 备注
	private String orgCurrCode;// 本体交易信息（MUFG）
	private String orgAccNo;// 本体交易信息（MUFG）
	private BigDecimal amtCcy;// 手续费等记账的金额（原币种）（MUFG）
	private String reverseTransId;// 冲账对应交易ID
	private String transBusId;// 交易业务编号
	private BigDecimal shortAndOver;// 尾差
	private BigDecimal balance;// 未开票金额
	private String dataStatus;// 状态
	private BigDecimal inComeCny;// 收入_人民币
	private String transFapiaoFlag;//是否打票 A-自动打印/M-手动打印/N-用不打印
	private String fapiaoType; // FAPIAO_TYPE VARCHAR2(1) N 发票类型
	private String vatRateCode; // VAT_RATE_CODE VARCHAR2(3) N 增值税种类
	// 辅助属性
	private String customerName;// 客户名称
	private String customerTaxno;// 客户纳税人识别号
	private String taxpayerType;  // 客户纳税人类别   
	private String customerAddress;// 客户地址
	private String customerTel;// 客户电话
	private String bankName;// 交易发生机构名称
	private String departName;// 交易发生部门名称
	private String transTypeName;// 交易类型名称
	private String billId;// 对应票据ID
	private String transBeginDate;// 交易日期起始
	private String transEndDate;// 交易日期终止
	private String billBeginDate;// 开票日期起始
	private String billEndDate;// 开票日期终止
	private List billList; // 对应票据
	private String drawerName;// 开票人姓名
	private String searchFlag;// 查询标志
	private String userId;// 当前用户ID
	private BigDecimal transBillAmt;// 交易对应票据中的金额
	private BigDecimal amtMin;// 交易金额范围
	private BigDecimal amtMax;// 交易金额范围
	private BigDecimal balanceMin;// 未开票金额范围
	private BigDecimal balanceMax;// 未开票金额范围
	//VMS_BUSINESS_TB
	private String businessCname;// 交易类型名称
	// ----- new-------
	private String billDate;// 对应票据开票日期
	private String billCode; // 对应发票代码
	private String billType; // 发票类型
	private BigDecimal sumAmt;// 价税合计
	private String billNo; // 发票类型

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount == null ? "" : customerAccount
				.trim();
	}

	public String getTaxFlag() {
		return taxFlag == null ? "" : taxFlag.trim();
	}

	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}

	public BigDecimal getTaxRate() {
		return taxRate == null ? new BigDecimal(0) : taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getIsReverse() {
		return isReverse;
	}

	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrgCurrCode() {
		return orgCurrCode;
	}

	public void setOrgCurrCode(String orgCurrCode) {
		this.orgCurrCode = orgCurrCode;
	}

	public String getOrgAccNo() {
		return orgAccNo;
	}

	public void setOrgAccNo(String orgAccNo) {
		this.orgAccNo = orgAccNo;
	}

	public BigDecimal getAmtCcy() {
		return amtCcy == null ? new BigDecimal(0) : amtCcy;
	}

	public void setAmtCcy(BigDecimal amtCcy) {
		this.amtCcy = amtCcy;
	}

	public String getReverseTransId() {
		return reverseTransId;
	}

	public void setReverseTransId(String reverseTransId) {
		this.reverseTransId = reverseTransId;
	}

	public BigDecimal getShortAndOver() {
		return shortAndOver == null ? new BigDecimal(0) : shortAndOver;
	}

	public void setShortAndOver(BigDecimal shortAndOver) {
		this.shortAndOver = shortAndOver;
	}

	public BigDecimal getBalance() {
		return balance == null ? new BigDecimal(0) : balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getCustomerName() {
		return customerName;
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

	public String getCustomerAddress() {
		return customerAddress == null ? "" : customerAddress.trim();
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerTel() {
		return customerTel == null ? "" : customerTel.trim();
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getTransTypeName() {
		return transTypeName;
	}

	public void setTransTypeName(String transTypeName) {
		this.transTypeName = transTypeName;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getTransBeginDate() {
		return transBeginDate;
	}

	public void setTransBeginDate(String transBeginDate) {
		this.transBeginDate = transBeginDate;
	}

	public String getTransEndDate() {
		return transEndDate;
	}

	public void setTransEndDate(String transEndDate) {
		this.transEndDate = transEndDate;
	}

	public String getBillBeginDate() {
		return billBeginDate;
	}

	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public List getBillList() {
		return billList;
	}

	public void setBillList(List billList) {
		this.billList = billList;
	}

	public String getDrawerName() {
		return drawerName;
	}

	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getTransBillAmt() {
		return transBillAmt == null ? new BigDecimal(0) : transBillAmt;
	}

	public void setTransBillAmt(BigDecimal transBillAmt) {
		this.transBillAmt = transBillAmt;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getTaxpayerType() {
		return taxpayerType;
	}

	public void setTaxpayerType(String taxpayerType) {
		this.taxpayerType = taxpayerType;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public BigDecimal getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getTransBusId() {
		return transBusId;
	}

	public void setTransBusId(String transBusId) {
		this.transBusId = transBusId;
	}

	public BigDecimal getAmtCny() {
		return amtCny;
	}

	public void setAmtCny(BigDecimal amtCny) {
		this.amtCny = amtCny;
	}

	public BigDecimal getTaxAmtCny() {
		return taxAmtCny;
	}

	public void setTaxAmtCny(BigDecimal taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getInComeCny() {
		return inComeCny;
	}

	public void setInComeCny(BigDecimal inComeCny) {
		this.inComeCny = inComeCny;
	}

	public BigDecimal getAmtMin() {
		return amtMin;
	}

	public void setAmtMin(BigDecimal amtMin) {
		this.amtMin = amtMin;
	}

	public void setAmtMinStr(String amtMin) {
		if (StringUtil.isNotEmpty(amtMin)) {
			this.amtMin = new BigDecimal(amtMin);
		} else {
			this.amtMin = null;
		}
	}
	
	public BigDecimal getAmtMax() {
		return amtMax;
	}

	public void setAmtMax(BigDecimal amtMax) {
		this.amtMax = amtMax;
	}

	public void setAmtMaxStr(String amtMax) {
		if (StringUtil.isNotEmpty(amtMax)) {
			this.amtMax = new BigDecimal(amtMax);
		} else {
			this.amtMax = null;
		}
	}
	
	public BigDecimal getBalanceMin() {
		return balanceMin;
	}

	public void setBalanceMin(BigDecimal balanceMin) {
		this.balanceMin = balanceMin;
	}

	public void setBalanceMinStr(String balanceMin) {
		if (StringUtil.isNotEmpty(balanceMin)) {
			this.balanceMin = new BigDecimal(balanceMin);
		} else {
			this.balanceMin = null;
		}
	}
	
	public BigDecimal getBalanceMax() {
		return balanceMax;
	}

	public void setBalanceMax(BigDecimal balanceMax) {
		this.balanceMax = balanceMax;
	}

	public void setBalanceMaxStr(String balanceMax) {
		if (StringUtil.isNotEmpty(balanceMax)) {
			this.balanceMax = new BigDecimal(balanceMax);
		} else {
			this.balanceMax = null;
		}
	}
	
	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getBusinessCname() {
		return businessCname;
	}

	public void setBusinessCname(String businessCname) {
		this.businessCname = businessCname;
	}

	public String getTransFapiaoFlag() {
		return transFapiaoFlag;
	}

	public void setTransFapiaoFlag(String transFapiaoFlag) {
		this.transFapiaoFlag = transFapiaoFlag;
	}

	public String getVatRateCode() {
		return vatRateCode;
	}

	public void setVatRateCode(String vatRateCode) {
		this.vatRateCode = vatRateCode;
	}

}
