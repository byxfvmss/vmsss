package com.cjit.vms.system.model;

import java.math.BigDecimal;
import java.util.List;
public class Monitor {

	private String instName;     //机构名称
	private BigDecimal tranAmt;			//交易总额
	private BigDecimal tranTaxAmt;		//交易税金
	private BigDecimal income;			//收入
	private BigDecimal billAmt;			//已开票收入
	private BigDecimal billTaxAmt;		//已开票税金
	private BigDecimal billIncome;		//已开票收入
	private String beginDate;
	private String endDate;
	private String transTyp;
	private String transName;
	/**
	 * @return the transName  instName customerName,
	 */
	//修改版 
	private BigDecimal  balance;//  未开票金额 balance balanceTax AmtSum
	private BigDecimal  balanceTax;//未开票稅额
	private BigDecimal  zAmtSum;//专票金额
	private BigDecimal  pAmtSum;//普票金额
	private BigDecimal  zTaxAmtSum;//专票税额
	private BigDecimal  pTaxAmtSum;//普票稅额
	private BigDecimal  amtSum;//合计金额
	private BigDecimal  sumAmt;//价稅合计金额
	private BigDecimal  taxAmtSum;//合计稅额  
	//二次修改TaxAmtSum AmtSum diffAmtSum diffSumAmt
	private BigDecimal  diffTaxAmtSum;
	private BigDecimal  diffAmtSum;
	private BigDecimal  diffSumAmt;
	private List lstAuthInstId;
	private String taxNo;
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	} 


	public String getTransName() {
		return transName;
	}
	/**
	 * @param transName the transName to set
	 */
	public void setTransName(String transName) {
		this.transName = transName;
	}
	private String customerCode;
	private String customerName;//客户名称
	private String number;
	private String instId; //机构代码
	private String flag;//首次标记
	private String userId;
	
	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}
	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
	 * @return the instName
	 */
	public String getInstName() {
		return instName;
	}
	/**
	 * @param instName the instName to set
	 */
	public void setInstName(String instName) {
		this.instName = instName;
	}
	/**
	 * @return the beginDate
	 */
	public String getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the transTyp
	 */
	public String getTransTyp() {
		return transTyp;
	}
	/**
	 * @param transTyp the transTyp to set
	 */
	public void setTransTyp(String transTyp) {
		this.transTyp = transTyp;
	}

	/**
	 * @return the instId
	 */
	public String getInstId() {
		return instId;
	}
	/**
	 * @param instId the instId to set
	 */
	public void setInstId(String instId) {
		this.instId = instId;
	}
	/**
	 * @return the tranAmt
	 */
	public BigDecimal getTranAmt() {
		return tranAmt;
	}
	/**
	 * @param tranAmt the tranAmt to set
	 */
	public void setTranAmt(BigDecimal tranAmt) {
		this.tranAmt = tranAmt;
	}
	/**
	 * @return the tranTaxAmt
	 */
	public BigDecimal getTranTaxAmt() {
		return tranTaxAmt;
	}
	/**
	 * @param tranTaxAmt the tranTaxAmt to set
	 */
	public void setTranTaxAmt(BigDecimal tranTaxAmt) {
		this.tranTaxAmt = tranTaxAmt;
	}
	/**
	 * @return the income
	 */
	public BigDecimal getIncome() {
		return income;
	}
	/**
	 * @param income the income to set
	 */
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	/**
	 * @return the billAmt
	 */
	public BigDecimal getBillAmt() {
		return billAmt;
	}
	/**
	 * @param billAmt the billAmt to set
	 */
	public void setBillAmt(BigDecimal billAmt) {
		this.billAmt = billAmt;
	}
	/**
	 * @return the billTaxAmt
	 */
	public BigDecimal getBillTaxAmt() {
		return billTaxAmt;
	}
	/**
	 * @param billTaxAmt the billTaxAmt to set
	 */
	public void setBillTaxAmt(BigDecimal billTaxAmt) {
		this.billTaxAmt = billTaxAmt;
	}
	/**
	 * @return the billIncome
	 */
	public BigDecimal getBillIncome() {
		return billIncome;
	}
	/**
	 * @param billIncome the billIncome to set
	 */
	public void setBillIncome(BigDecimal billIncome) {
		this.billIncome = billIncome;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public BigDecimal getBalance() {
		return balance==null ? new BigDecimal(0): balance;
	}
	public BigDecimal getBalanceTax() {
		return balanceTax==null?new BigDecimal(0):balanceTax ;
	}
	public BigDecimal getzAmtSum() {
		return zAmtSum==null?new BigDecimal(0):zAmtSum;
	}
	public BigDecimal getpAmtSum() {
		return pAmtSum==null?new BigDecimal(0):pAmtSum;
	}
	public BigDecimal getzTaxAmtSum() {
		return zTaxAmtSum==null?new BigDecimal(0):zTaxAmtSum;
	}
	public BigDecimal getpTaxAmtSum() {
		return pTaxAmtSum==null?new BigDecimal(0):pTaxAmtSum;
	}
	public BigDecimal getAmtSum() {
		return amtSum==null?new BigDecimal(0):amtSum;
	}
	public BigDecimal getSumAmt() {
		return sumAmt==null?new BigDecimal(0):sumAmt;
	}
	public BigDecimal getTaxAmtSum() {
		return taxAmtSum==null?new BigDecimal(0):taxAmtSum;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public void setBalanceTax(BigDecimal balanceTax) {
		this.balanceTax = balanceTax;
	}
	public void setzAmtSum(BigDecimal zAmtSum) {
		this.zAmtSum = zAmtSum;
	}
	public void setpAmtSum(BigDecimal pAmtSum) {
		this.pAmtSum = pAmtSum;
	}
	public void setzTaxAmtSum(BigDecimal zTaxAmtSum) {
		this.zTaxAmtSum = zTaxAmtSum;
	}
	public void setpTaxAmtSum(BigDecimal pTaxAmtSum) {
		this.pTaxAmtSum = pTaxAmtSum;
	}
	
	public BigDecimal getDiffTaxAmtSum() {
		return diffTaxAmtSum==null?new BigDecimal(0):diffTaxAmtSum;
	}
	public BigDecimal getDiffAmtSum() {
		return diffAmtSum==null?new BigDecimal(0):diffAmtSum;
	}
	public BigDecimal getDiffSumAmt() {
		return diffSumAmt==null?new BigDecimal(0):diffSumAmt;
	}
	public void setDiffTaxAmtSum(BigDecimal diffTaxAmtSum) {
		this.diffTaxAmtSum = diffTaxAmtSum;
	}
	public void setDiffAmtSum(BigDecimal diffAmtSum) {
		this.diffAmtSum = diffAmtSum;
	}
	public void setDiffSumAmt(BigDecimal diffSumAmt) {
		this.diffSumAmt = diffSumAmt;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}
	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}
	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}
	
}
