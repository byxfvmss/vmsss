package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:38:23 PM
 * @描述: [Self_D_LOUNEXGU]境外担保项下境内贷款信息
 */
public class Self_D_LOUNEXGU extends SelfEntity{

	private String lounexgucode; // 外保内贷编号
	private String creditorcode; // 债权人代码
	private String debtorcode; // 债务人代码
	private String debtorname; // 债务人中文名称
	private String debtortype; // 债务人类型
	private String cfeogudad; // 中资企业境外担保项下贷款业务批准文件号
	private String cfeogudcurr; // 中资企业境外担保项下境内贷款额度币种
	private BigDecimal cfeogudamount; // 中资企业境外担保项下境内贷款额度金额
	private String credcurrcode; // 贷款币种
	private BigDecimal credconamount; // 贷款签约金额
	private String valuedate; // 起息日
	private String maturity; // 到期日
	private String dofoexlocode; // 国内外汇贷款编号
	private String tradedate; // 交易日期
	private String buscode; // 银行业务参号
	private String changeno; // 变动编号
	private String changedate; // 变动日期
	private BigDecimal credwithamount; // 提款金额
	private BigDecimal credrepayamount; // 还本金额
	private BigDecimal picamount; // 付息费金额
	private BigDecimal credprinbala; // 贷款余额
	private BigDecimal guarantlibala; // 担保责任余额
	private BigDecimal guperamount; // 担保履约金额

	public String getLounexgucode(){
		return lounexgucode;
	}

	public void setLounexgucode(String lounexgucode){
		this.lounexgucode = lounexgucode;
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

	public String getDebtortype(){
		return debtortype;
	}

	public void setDebtortype(String debtortype){
		this.debtortype = debtortype;
	}

	public String getCfeogudad(){
		return cfeogudad;
	}

	public void setCfeogudad(String cfeogudad){
		this.cfeogudad = cfeogudad;
	}

	public String getCfeogudcurr(){
		return cfeogudcurr;
	}

	public void setCfeogudcurr(String cfeogudcurr){
		this.cfeogudcurr = cfeogudcurr;
	}

	public BigDecimal getCfeogudamount(){
		return cfeogudamount;
	}

	public void setCfeogudamount(BigDecimal cfeogudamount){
		this.cfeogudamount = cfeogudamount;
	}

	public String getCredcurrcode(){
		return credcurrcode;
	}

	public void setCredcurrcode(String credcurrcode){
		this.credcurrcode = credcurrcode;
	}

	public BigDecimal getCredconamount(){
		return credconamount;
	}

	public void setCredconamount(BigDecimal credconamount){
		this.credconamount = credconamount;
	}

	public String getValuedate(){
		return valuedate;
	}

	public void setValuedate(String valuedate){
		this.valuedate = valuedate;
	}

	public String getMaturity(){
		return maturity;
	}

	public void setMaturity(String maturity){
		this.maturity = maturity;
	}

	public String getDofoexlocode(){
		return dofoexlocode;
	}

	public void setDofoexlocode(String dofoexlocode){
		this.dofoexlocode = dofoexlocode;
	}

	public String getTradedate(){
		return tradedate;
	}

	public void setTradedate(String tradedate){
		this.tradedate = tradedate;
	}

	public String getBuscode(){
		return buscode;
	}

	public void setBuscode(String buscode){
		this.buscode = buscode;
	}

	public String getChangeno(){
		return changeno;
	}

	public void setChangeno(String changeno){
		this.changeno = changeno;
	}

	public String getChangedate(){
		return changedate;
	}

	public void setChangedate(String changedate){
		this.changedate = changedate;
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

	public BigDecimal getCredprinbala(){
		return credprinbala;
	}

	public void setCredprinbala(BigDecimal credprinbala){
		this.credprinbala = credprinbala;
	}

	public BigDecimal getGuarantlibala(){
		return guarantlibala;
	}

	public void setGuarantlibala(BigDecimal guarantlibala){
		this.guarantlibala = guarantlibala;
	}

	public BigDecimal getGuperamount(){
		return guperamount;
	}

	public void setGuperamount(BigDecimal guperamount){
		this.guperamount = guperamount;
	}
}
