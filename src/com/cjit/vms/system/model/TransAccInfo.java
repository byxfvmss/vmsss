package com.cjit.vms.system.model;

import java.math.BigDecimal;

public class TransAccInfo {
	private String transAccEntryId;
	private String transId;
	private String accEntryId;
	private BigDecimal income;
	private BigDecimal taxAmt;
	private BigDecimal totalAmt;
	private BigDecimal amt;
	private String transNumTyp;
	private String isReverse;
	private String cdFlag;
	private String currency;
	private String businessCode;
	private String accTitleCode;
	private String accTitleName;
	private String businessCName;
	private String transDate;
	private String currencyName;
	
	public String getTransAccEntryId() {
		return transAccEntryId;
	}
	public void setTransAccEntryId(String transAccEntryId) {
		this.transAccEntryId = transAccEntryId;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getAccEntryId() {
		return accEntryId;
	}
	public void setAccEntryId(String accEntryId) {
		this.accEntryId = accEntryId;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getIncome() {
		return income;
	}
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	public BigDecimal getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}
	public BigDecimal getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getTransNumTyp() {
		return transNumTyp;
	}
	public void setTransNumTyp(String transNumTyp) {
		this.transNumTyp = transNumTyp;
	}
	public String getIsReverse() {
		return isReverse;
	}
	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}
	public String getCdFlag() {
		return cdFlag;
	}
	public void setCdFlag(String cdFlag) {
		this.cdFlag = cdFlag;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public String getAccTitleCode() {
		return accTitleCode;
	}
	public void setAccTitleCode(String accTitleCode) {
		this.accTitleCode = accTitleCode;
	}
	public String getAccTitleName() {
		return accTitleName;
	}
	public void setAccTitleName(String accTitleName) {
		this.accTitleName = accTitleName;
	}
	public String getBusinessCName() {
		return businessCName;
	}
	public void setBusinessCName(String businessCName) {
		this.businessCName = businessCName;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
}
