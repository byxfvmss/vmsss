package com.cjit.vms.taxdisk.single.model.busiDisk;

import java.math.BigDecimal;

public class BillCancelTiansInfo {
 
	private String transId;			//交易id
	private BigDecimal  balance;		//未开票金额(价税合计)
	private BigDecimal  amtCny;			//金额_人民币(价税合计)
	private BigDecimal  taxCnyBalance;	//未开票税额 
	private BigDecimal  taxAmtCny;   //税额
	private String  issueType;		//开具类型1-单笔;2-合并;3-拆分
	private BigDecimal sumAmt;//合计金额
	private BigDecimal taxAmtSum;// 合计税额

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
	public BigDecimal getTaxCnyBalance() {
		return taxCnyBalance;
	}
	public void setTaxCnyBalance(BigDecimal taxCnyBalance) {
		this.taxCnyBalance = taxCnyBalance;
	}
	public BigDecimal getTaxAmtCny() {
		return taxAmtCny;
	}
	public void setTaxAmtCny(BigDecimal taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public BigDecimal getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}
	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}
	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}
	
	
}
