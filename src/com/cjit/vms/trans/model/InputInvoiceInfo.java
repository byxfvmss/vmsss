package com.cjit.vms.trans.model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.metlife.model.AccountingEntriesInfo;


public class InputInvoiceInfo extends AccountingEntriesInfo{
	private String billId;//票据id
	private String billCode;//发票代码
	private String billNo;//发票号码
	private String billDate;//开票日期
	private String name;//购方纳税人名称
	private String taxno;//购方纳税人识别号
	private String addressandphone;//购方地址电话
	private String bankandaccount;//购方银行账号
	private BigDecimal amtSum;//合计金额
	private BigDecimal taxAmtSum;//合计税额
	private BigDecimal sumAmt;//价税合计
	private String remark;//备注
	private String drawer;//开票人
	private String reviewer;//复核人
	private String payee;//收款人
	private String vendorName;//销方纳税人名称
	private String vendorTaxno;//销方纳税人识别号
	private String vendorAddressandphone;//销方地址电话
	private String vendorBankandaccount;//销方银行账号
	private String instcode;//所属机构
	private String noticeNo;//通知单编号
	private String datastatus;//状态           1-已扫描未认证	2-认证未收到回执	3-首次认证通过	4-首次认证未通过	5-再次认证通过	6-再次认证未通过	7-税务局当场认证通过	8-税务局当场认证未通过	9-票退回	10-已抵扣	11-不可抵扣	12-红冲待审核	13-红冲已审核	14-已红冲'
	private String description;//操作说明
	private String vatOutProportion;//转出比例
	private BigDecimal vatOutAmt;//转出金额
	private String conformFlg;//是否勾稽     1-勾稽 	2-不勾稽
	private String balance;//发票余额
	private String fapiaoType;// 发票类型     0-增值税专用发票	1-增值税普通发票'
	private String scanDate;//扫描日期
	private String scanOperator;//扫描人
	private String identifyDate;//认证通过日期
	private String operateStatus;//原票据状态 1-已扫描未认证	2-认证未收到回执	3-首次认证通过	4-首次认证未通过	5-再次认证通过	6-再次认证未通过	7-税务局当场认证通过	8-税务局当场认证未通过	9-票退回	10-已抵扣	11-不可抵扣  	14-已红冲'
	private String urlBillImage;//进项票据图样url地址
	
	private String verifyData;//认证时间
	private String verifyDataRemain;//认证时间剩余
	private String vendorAddress;
	private String vendorPhone;
	private String datastatusName;
	
	private String instName; //机构名
	private String instId; //机构id
	private String billStartDate;//开票开始日期
	private String billEndDate;//开票结束日期
//	private String urlBillImage;//票据地址
	private List lstAuthInstId = new ArrayList();//机构list
	private String userId;
	
	private String dealNo;
	
	private String cancelReason;
	
	
	/** 
	 *  createTime:2016.3
	 * 	author:沈磊
	 *	content:会计分录  metlife
	*/
	//metlife 进项转出 
	private String subjectType;//标的物类型
	private String status;//状态
	private String taName;//
	
	private String vtorId;//VTOR_ID财务月ID
	
	private String accountPeriod;//VTOR_ACCOUNT_PERIOD财务月

	private String accountPeriodStrart;//VTOR_ACCOUNT_PERIOD_START 开始日期

	private String accountPeriodEnd;//VTOR_ACCOUNT_PERIOD_END 终止日期

	private BigDecimal transferRatio;//VTOR_TRANSFER_OUT_RATIO 转出比例
	private String expenseDocnum;
	private BigDecimal taxExemptAmt;//VTOI_TAX_EXEMPT_AMT
	private BigDecimal taxAbleAmt;//VTOI_TAXABLE_AMT
	private BigDecimal vtoiSumAmt;//VTOI_SUM_AMT
	private BigDecimal transferOutAmt;//VTOI_TRANSFER_OUT_AMT
	private String getTransferRatioToString;
	private String expenseDocnuma;
	
	
	
	public String getExpenseDocnuma() {
		return expenseDocnuma;
	}
	public void setExpenseDocnuma(String expenseDocnuma) {
		this.expenseDocnuma = expenseDocnuma;
	}
	public String getExpenseDocnum() {
		return expenseDocnum;
	}
	public void setExpenseDocnum(String expenseDocnum) {
		this.expenseDocnum = expenseDocnum;
	}
	public BigDecimal getTaxExemptAmt() {
		return taxExemptAmt;
	}
	public void setTaxExemptAmt(BigDecimal taxExemptAmt) {
		this.taxExemptAmt = taxExemptAmt;
	}
	public BigDecimal getTaxAbleAmt() {
		return taxAbleAmt;
	}
	public void setTaxAbleAmt(BigDecimal taxAbleAmt) {
		this.taxAbleAmt = taxAbleAmt;
	}
	public BigDecimal getVtoiSumAmt() {
		return vtoiSumAmt;
	}
	public void setVtoiSumAmt(BigDecimal vtoiSumAmt) {
		this.vtoiSumAmt = vtoiSumAmt;
	}
	public BigDecimal getTransferOutAmt() {
		return transferOutAmt;
	}
	public void setTransferOutAmt(BigDecimal transferOutAmt) {
		this.transferOutAmt = transferOutAmt;
	}
	public String getGetTransferRatioToString() {
		return this.transferRatio.toPlainString();
	}
	public void setGetTransferRatioToString(String getTransferRatioToString) {
		this.getTransferRatioToString = getTransferRatioToString;
	}
	public String getTaName() {
		return taName;
	}
	public void setTaName(String taName) {
		this.taName = taName;
	}
	public String getVtorId() {
		return vtorId;
	}
	public void setVtorId(String vtorId) {
		this.vtorId = vtorId;
	}
	public String getAccountPeriod() {
		return accountPeriod;
	}
	public void setAccountPeriod(String accountPeriod) {
		this.accountPeriod = accountPeriod;
	}
	public String getAccountPeriodStrart() {
		return accountPeriodStrart;
	}
	public void setAccountPeriodStrart(String accountPeriodStrart) {
		this.accountPeriodStrart = accountPeriodStrart;
	}
	public String getAccountPeriodEnd() {
		return accountPeriodEnd;
	}
	public void setAccountPeriodEnd(String accountPeriodEnd) {
		this.accountPeriodEnd = accountPeriodEnd;
	}
	
	public BigDecimal getTransferRatio() {
		return transferRatio;
	}
	public void setTransferRatio(BigDecimal transferRatio) {
		this.transferRatio = transferRatio;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDealNo() {
		return dealNo;
	}
	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}
	public String getDatastatusName() {
		if(this.datastatus==null){
			return "";
		}
		if("1".equals(datastatus)){
			return "已扫描未认证";
		}
		if("2".equals(datastatus)){
			return "认证未收到回执";
		}
		if("3".equals(datastatus)){
			return "首次认证通过";
		}
		if("4".equals(datastatus)){
			return "首次认证未通过";
		}
		if("5".equals(datastatus)){
			return "再次认证通过";
		}
		if("6".equals(datastatus)){
			return "再次认证未通过";
		}
		if("7".equals(datastatus)){
			return "税务局当场认证通过";
		}
		if("8".equals(datastatus)){
			return "税务局当场认证未通过";
		}
		if("9".equals(datastatus)){
			return "票退回";
		}
		if("10".equals(datastatus)){
			return "已抵扣";
		}
		if("11".equals(datastatus)){
			return "不可抵扣";
		}
		if("12".equals(datastatus)){
			return "红冲待审核";
		}
		if("13".equals(datastatus)){
			return "红冲已审核";
		}
		if("14".equals(datastatus)){
			return "已红冲";
		}
		if("15".equals(datastatus)){
			return "认证提交";
		}
		if("16".equals(datastatus)){
			return "转出提交";
		}
		return datastatusName;
	}
	public void setDatastatusName(String datastatusName) {
		this.datastatusName = datastatusName;
	}
	public String getVendorAddress() {
//		return StringUtils.substringBeforeLast(vendorAddressandphone, " ");
		return this.vendorAddress;
	}
	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}
	public String getVendorPhone() {
//		return StringUtils.substringAfterLast(vendorAddressandphone, " ");
		return this.vendorPhone;
	}
	public void setVendorPhone(String vendorPhone) {
		this.vendorPhone = vendorPhone;
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
	public BigDecimal getAmtSum() {
		return amtSum;
	}
	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}
	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
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
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorTaxno() {
		return vendorTaxno;
	}
	public void setVendorTaxno(String vendorTaxno) {
		this.vendorTaxno = vendorTaxno;
	}
	public String getVendorAddressandphone() {
		return vendorAddressandphone;
	}
	public void setVendorAddressandphone(String vendorAddressandphone) {
		this.vendorAddressandphone = vendorAddressandphone;
	}
	public String getVendorBankandaccount() {
		return vendorBankandaccount;
	}
	public void setVendorBankandaccount(String vendorBankandaccount) {
		this.vendorBankandaccount = vendorBankandaccount;
	}
	public String getInstcode() {
		return instcode;
	}
	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}
	public String getNoticeNo() {
		return noticeNo;
	}
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
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
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getVerifyData() {
		return verifyData;
	}
	public void setVerifyData(String verifyData) {
		this.verifyData = verifyData;
	}
	public String getOperateStatus() {
		return operateStatus;
	}
	public void setOperateStatus(String operateStatus) {
		this.operateStatus = operateStatus;
	}
	public String getInstName() {
		return instName;
	}
	public String getBillStartDate() {
		return billStartDate;
	}
	public String getBillEndDate() {
		return billEndDate;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}
	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}
	public String getUrlBillImage() {
		return urlBillImage;
	}
	public void setUrlBillImage(String urlBillImage) {
		this.urlBillImage = urlBillImage;
	}
	public String getVerifyDataRemain() {
		return verifyDataRemain;
	}
	public void setVerifyDataRemain(String verifyDataRemain) {
		this.verifyDataRemain = verifyDataRemain;
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
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	
}
