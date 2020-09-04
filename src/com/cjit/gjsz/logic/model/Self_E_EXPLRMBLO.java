package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:38:02 PM
 * @描述: [Self_E_EXPLRMBLO]外汇质押人民币贷款信息
 */
public class Self_E_EXPLRMBLO extends SelfEntity{

	private String explrmblono; // 外汇质押人民币贷款编号
	private String creditorcode; // 债权人代码
	private String debtorcode; // 债务人代码
	private String debtorname; // 债务人中文名称
	private String valuedate; // 贷款起息日
	private String credconcurr; // 贷款签约币种
	private BigDecimal credconamount; // 贷款签约金额
	private String maturity; // 贷款到期日
	// private String buscode; // 银行业务参号
	private String buocmonth;// 报告期
	private String changeno; // 变动编号
	private BigDecimal monbeloadbal; // 月初贷款余额
	private BigDecimal credwithamount; // 本月提款金额
	private BigDecimal credrepayamount; // 本月还本金额
	private BigDecimal picamount; // 本月付息费金额
	private BigDecimal monenloadbal; // 月末贷款余额

	public String getExplrmblono(){
		return explrmblono;
	}

	public void setExplrmblono(String explrmblono){
		this.explrmblono = explrmblono;
	}

	public String getCreditorcode(){
		return creditorcode;
	}

	public void setCreditorcode(String creditorcode){
		this.creditorcode = creditorcode;
	}

	public String getDebtorcode(){
		return debtorcode;
	}

	public void setDebtorcode(String debtorcode){
		this.debtorcode = debtorcode;
	}

	public String getDebtorname(){
		return debtorname;
	}

	public void setDebtorname(String debtorname){
		this.debtorname = debtorname;
	}

	public String getValuedate(){
		return valuedate;
	}

	public void setValuedate(String valuedate){
		this.valuedate = valuedate;
	}

	public String getCredconcurr(){
		return credconcurr;
	}

	public void setCredconcurr(String credconcurr){
		this.credconcurr = credconcurr;
	}

	public BigDecimal getCredconamount(){
		return credconamount;
	}

	public void setCredconamount(BigDecimal credconamount){
		this.credconamount = credconamount;
	}

	public String getMaturity(){
		return maturity;
	}

	public void setMaturity(String maturity){
		this.maturity = maturity;
	}

	// public String getBuscode(){
	// return buscode;
	// }
	// public void setBuscode(String buscode){
	// this.buscode = buscode;
	// }
	public String getBuocmonth(){
		return buocmonth;
	}

	public void setBuocmonth(String buocmonth){
		this.buocmonth = buocmonth;
	}

	public String getChangeno(){
		return changeno;
	}

	public void setChangeno(String changeno){
		this.changeno = changeno;
	}

	public BigDecimal getMonbeloadbal(){
		return monbeloadbal;
	}

	public void setMonbeloadbal(BigDecimal monbeloadbal){
		this.monbeloadbal = monbeloadbal;
	}

	public BigDecimal getCredwithamount(){
		return credwithamount;
	}

	public void setCredwithamount(BigDecimal credwithamount){
		this.credwithamount = credwithamount;
	}

	public BigDecimal getCredrepayamount(){
		return credrepayamount;
	}

	public void setCredrepayamount(BigDecimal credrepayamount){
		this.credrepayamount = credrepayamount;
	}

	public BigDecimal getPicamount(){
		return picamount;
	}

	public void setPicamount(BigDecimal picamount){
		this.picamount = picamount;
	}

	public BigDecimal getMonenloadbal(){
		return monenloadbal;
	}

	public void setMonenloadbal(BigDecimal monenloadbal){
		this.monenloadbal = monenloadbal;
	}
}
