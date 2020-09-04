package com.cjit.vms.trans.model;

import java.math.BigDecimal;

public class RedReceiptTransInfo {
	private String billId;
	private String transId;
	private String transDate;

	private String customerName;// 客户纳税人名称
	private String customerTaxno;// 客户纳税人识别号

	private BigDecimal taxRate;
	private BigDecimal amtSum;// 合计金额
	private BigDecimal taxAmtSum;// 合计税额
	private BigDecimal sumAmt;// 价税合计
	private BigDecimal discountRate;// 折扣率
	private String fapiaoType;
	private String datastatus;
	private String name;
	private String taxno;
	private String customerAccount;
	private String transType;
	private String bankCode;
	private String customerId;
	private String transFlag;
	private String isReverse;
	private String isHandiwork;
	private BigDecimal balance;
	private String billItemId;

	// transBill
	// 金额_人民币
	private BigDecimal amtCny;
	// 税额_人民币
	private BigDecimal taxAmtCny;
	// 收入_人民币
	private BigDecimal incomeCny;

	// 冲账
	
	private String reverseTransId;
	// 金额_人民币
	private BigDecimal reverseAmtCny;
	// 税额_人民币
	private BigDecimal reverseTaxAmtCny;
	// 收入_人民币
	private BigDecimal reverseIncomeCny;

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

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

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
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

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReverseTransId() {
		return reverseTransId;
	}

	public void setReverseTransId(String reverseTransId) {
		this.reverseTransId = reverseTransId;
	}

	public BigDecimal getReverseTaxAmtCny() {
		return reverseTaxAmtCny;
	}

	public BigDecimal getReverseAmtCny() {
		return reverseAmtCny;
	}

	public void setReverseAmtCny(BigDecimal reverseAmtCny) {
		this.reverseAmtCny = reverseAmtCny;
	}

	public BigDecimal ReverseTaxAmtCny() {
		return reverseTaxAmtCny;
	}

	public void setReverseTaxAmtCny(BigDecimal reverseTaxAmtCny) {
		this.reverseTaxAmtCny = reverseTaxAmtCny;
	}

	public BigDecimal getReverseIncomeCny() {
		return reverseIncomeCny;
	}

	public void setReverseIncomeCny(BigDecimal reverseIncomeCny) {
		this.reverseIncomeCny = reverseIncomeCny;
	}

	public String getTaxno() {
		return taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTransFlag() {
		return transFlag;
	}

	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}

	public String getIsReverse() {
		return isReverse;
	}

	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}

	public String getIsHandiwork() {
		return isHandiwork;
	}

	public void setIsHandiwork(String isHandiwork) {
		this.isHandiwork = isHandiwork;
	}

	public String getBillItemId() {
		return billItemId;
	}

	public void setBillItemId(String billItemId) {
		this.billItemId = billItemId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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

	public BigDecimal getIncomeCny() {
		return incomeCny;
	}

	public void setIncomeCny(BigDecimal incomeCny) {
		this.incomeCny = incomeCny;
	}

}
