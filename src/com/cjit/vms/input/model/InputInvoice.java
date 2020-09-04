package com.cjit.vms.input.model;

import java.math.BigDecimal;


/**
 * 进项税发票类
 * 
 * @author Larry
 */
public class InputInvoice {
	//认证属性
	public static String notAuth = "未认证";
	public static String auth = "认证通过";
	public static String lastAuthDenied = "本次认证通过";
	public static String authDenied = "未通过认证";
	//发票类型
	public static String normalBill = "普通发票";
	public static String vatBill = "增值税专用发票";
	
//	private String bookingBeginDate; //记帐日期起始日
//	private String bookingEndDate; //记帐日期结束日
//	private String id; // 业务流水号
//	private String billType; // 发票种类
//	private String billCode; // 发票代码
//	private String billNo; // 发票号码
//	private String billDate; // 开票日期
//	private String billAmt; // 金额
//	private String billTax; // 税额
//	private String taxRate; // 税率
//	private String vendorName; // 供应商名称
//	private String vendorTaxno; // 供应商纳税人识别号
//	private String vendorAccount; // 供应商开户行及账号
//	private String bookingDate; // 记账日期
//	private String bookingItem; // 记账科目
//	private String department; // 所属部门
//	private String bussVouchersCode; // 业务凭证编号
//	private String contractCode; // 合同管理编号
//	private String deductionCode; // 抵扣联管理编号
//	private String authenticationFlag; // 认证结果
//	private String authenticationDate; // 认证日期
//	private String importDate; // 扫描日期
//	private String operator; // 扫描人
//	private String instCode; // 所属机构
//	private String deductionFlag; // 进项不可抵扣标识
//	private String outamt; // 转出金额
//	private String bussType; // 交易种类  vms_business_tb
//	private String multirelaFlag; // 是否勾稽
//	private byte[] image; // 影像
//	private String authenticationFlagView; //认证结果描述
//	private String billTypeView; //发票类型描述
//	private String businessCname;//交易类型中文描述
//	private String billTaxAmount;//千分位转换
//	private String outamtAmount;//千分位转换
//	private String billAmtAmount;//千分位转换
//	private String instName;//所属机构
//	private String bookingItemView;//记帐科目中文描述
//	private String deductedFlag;//是否已抵扣
//	private BigDecimal deductedAlart;//抵扣预警时间
//	private String deductedAlartFlag;//抵扣预警标示
	
	private String  billId;// 票据ID

	private String  billCode;// 发票代码

	private String  billNo;// 发票号码

	private String  billDate;// 开票日期

	private String  name;// 购方纳税人名称

	private String  taxNo;// 购方纳税人识别号

	private String  addressAndPhone;// 购方地址电话

	private String  bankAndAccount;// 购方银行账号

	private BigDecimal  amtSum;// 合计金额

	private BigDecimal  taxAmtSum;// 合计税额

	private BigDecimal  sumAmt;// 价税合计

	private String  remark;// 备注

	private String  drawer;// 开票人

	private String  reivewer;// 复核人

	private String  payee;// 收款人

	private String  vendorName;// 销方纳税人名称

	private String  vendorTaxNo;// 销方纳税人识别号

	private String  vendorAddressAndPhone;// 销方地址电话

	private String  vendorBankAndAccount;// 销方银行账号

	private String  instCode;// 所属机构

	private String  noticeNo;// 通知单编号

	private String  dataStatus;// 状态 1-已扫描未认证2-认证未收到回执3-首次认证通过4-首次认证未通过5-再次认证通过6-再次认证未通过7-税务局当场认证通过8-税务局当场认证未通过9-票退回10-已抵扣11-不可抵扣12-红冲待审核13-红冲已审核14-已红冲
	private String  description;// 操作说明

	private String  vatOutProportion;// 转出比例

	private BigDecimal  vatOutAmt;// 转出金额

	private String  conformFlg;// 是否勾稽 1-勾稽2-不勾稽

	private BigDecimal  balance;// 发票余额

	private String  faPiaoType;// 发票类型 0-增值税专用发票 1-增值税普通发票
	private String  scanDate;// 扫描日期

	private String  scanOperator;// 扫描人

	private String  identifyDate;// 认证通过日期
	
	// --------------------非数据库字段-----------------------
	private String billBeginDate; //开票日期起始日
	private String billEndDate; //开票日期结束日
	private BigDecimal deductedAlart;//u_base_sys_param表中的抵扣预警时间参数
	private String deductedAlartFlag;//抵扣预警标示
	private String deductedDays;//超出预警的天数
	
	private String cancelReason;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getAddressAndPhone() {
		return addressAndPhone;
	}

	public void setAddressAndPhone(String addressAndPhone) {
		this.addressAndPhone = addressAndPhone;
	}

	public String getBankAndAccount() {
		return bankAndAccount;
	}

	public void setBankAndAccount(String bankAndAccount) {
		this.bankAndAccount = bankAndAccount;
	}

	public BigDecimal getAmtSum() {
		return amtSum;
	}

	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}

	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}

	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}

	public BigDecimal getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(BigDecimal sumAmt) {
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

	public String getReivewer() {
		return reivewer;
	}

	public void setReivewer(String reivewer) {
		this.reivewer = reivewer;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorTaxNo() {
		return vendorTaxNo;
	}

	public void setVendorTaxNo(String vendorTaxNo) {
		this.vendorTaxNo = vendorTaxNo;
	}

	public String getVendorAddressAndPhone() {
		return vendorAddressAndPhone;
	}

	public void setVendorAddressAndPhone(String vendorAddressAndPhone) {
		this.vendorAddressAndPhone = vendorAddressAndPhone;
	}

	public String getVendorBankAndAccount() {
		return vendorBankAndAccount;
	}

	public void setVendorBankAndAccount(String vendorBankAndAccount) {
		this.vendorBankAndAccount = vendorBankAndAccount;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVatOutProportion() {
		return vatOutProportion;
	}

	public void setVatOutProportion(String vatOutProportion) {
		this.vatOutProportion = vatOutProportion;
	}

	public BigDecimal getVatOutAmt() {
		return vatOutAmt;
	}

	public void setVatOutAmt(BigDecimal vatOutAmt) {
		this.vatOutAmt = vatOutAmt;
	}

	public String getConformFlg() {
		return conformFlg;
	}

	public void setConformFlg(String conformFlg) {
		this.conformFlg = conformFlg;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getFaPiaoType() {
		return faPiaoType;
	}

	public void setFaPiaoType(String faPiaoType) {
		this.faPiaoType = faPiaoType;
	}

	public String getScanDate() {
		return scanDate;
	}

	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
	}

	public String getScanOperator() {
		return scanOperator;
	}

	public void setScanOperator(String scanOperator) {
		this.scanOperator = scanOperator;
	}

	public String getIdentifyDate() {
		return identifyDate;
	}

	public void setIdentifyDate(String identifyDate) {
		this.identifyDate = identifyDate;
	}

	public BigDecimal getDeductedAlart() {
		return deductedAlart;
	}

	public void setDeductedAlart(BigDecimal deductedAlart) {
		this.deductedAlart = deductedAlart;
	}

	public String getDeductedAlartFlag() {
		return deductedAlartFlag;
	}

	public void setDeductedAlartFlag(String deductedAlartFlag) {
		this.deductedAlartFlag = deductedAlartFlag;
	}

	public String getDeductedDays() {
		return deductedDays;
	}

	public void setDeductedDays(String deductedDays) {
		this.deductedDays = deductedDays;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
}
