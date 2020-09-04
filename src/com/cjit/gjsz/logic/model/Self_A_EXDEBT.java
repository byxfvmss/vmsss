package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:39:17 PM
 * @描述: [Self_A_EXDEBT]外债信息
 */
public class Self_A_EXDEBT extends SelfEntity{

	private String exdebtcode;// 外债编号
	private String limittype;// 账户类型
	private String debtorcode;// 债务人代码
	private String debtype;// 债务类型
	private String debtyperema;// 债务类型备注
	private String contractdate;// 签约日期
	private String valuedate;// 起息日
	private String contractcurr;// 签约币种
	private BigDecimal contractamount;// 签约金额
	private String maturity;// 到期日
	private String floatrate;// 是否浮动利率
	private BigDecimal anninrate;// 年化利率值
	private BigDecimal inpriamount;// 利息本金化金额
	private String creditorcode;// 债权人代码
	private String creditorname;// 债权人中文名称
	private String creditornamen;// 债权人英文名称
	private String creditortype;// 债权人类型代码
	private String crehqcode;// 债权人总部所在国家（地区）代码
	private String opercode;// 债权人经营地所在国家（地区）代码
	private String inprterm;// 是否有利息本金化条款
	private String spapreboindex;// 是否经外汇局特批不需占用指标
	private String appcode;// 申请人代码
	private String appname;// 申请人名称
	private String inltcabuscode;// 承继的远期信用证承兑银行业务参号
	private String isincode;// 国际证券识别编码
	private String buscode;// 银行业务参号
	private String changeno;// 变动编号
	private String changtype;// 变动类型
	private String chdate;// 变动日期
	private String chcurrency;// 变动币种
	private BigDecimal chamount;// 变动金额
	private BigDecimal fairvalue;// 公允价值
	private BigDecimal accoamount;// 外债余额

	public String getExdebtcode(){
		return exdebtcode;
	}

	public void setExdebtcode(String exdebtcode){
		this.exdebtcode = exdebtcode;
	}

	public String getLimittype(){
		return limittype;
	}

	public void setLimittype(String limittype){
		this.limittype = limittype;
	}

	public String getDebtorcode(){
		return debtorcode;
	}

	public void setDebtorcode(String debtorcode){
		this.debtorcode = debtorcode;
	}

	public String getDebtype(){
		return debtype;
	}

	public void setDebtype(String debtype){
		this.debtype = debtype;
	}

	public String getDebtyperema(){
		return debtyperema;
	}

	public void setDebtyperema(String debtyperema){
		this.debtyperema = debtyperema;
	}

	public String getContractdate(){
		return contractdate;
	}

	public void setContractdate(String contractdate){
		this.contractdate = contractdate;
	}

	public String getValuedate(){
		return valuedate;
	}

	public void setValuedate(String valuedate){
		this.valuedate = valuedate;
	}

	public String getContractcurr(){
		return contractcurr;
	}

	public void setContractcurr(String contractcurr){
		this.contractcurr = contractcurr;
	}

	public BigDecimal getContractamount(){
		return contractamount;
	}

	public void setContractamount(BigDecimal contractamount){
		this.contractamount = contractamount;
	}

	public String getMaturity(){
		return maturity;
	}

	public void setMaturity(String maturity){
		this.maturity = maturity;
	}

	public String getFloatrate(){
		return floatrate;
	}

	public void setFloatrate(String floatrate){
		this.floatrate = floatrate;
	}

	public BigDecimal getAnninrate(){
		return anninrate;
	}

	public void setAnninrate(BigDecimal anninrate){
		this.anninrate = anninrate;
	}

	public BigDecimal getInpriamount(){
		return inpriamount;
	}

	public void setInpriamount(BigDecimal inpriamount){
		this.inpriamount = inpriamount;
	}

	public String getCreditorcode(){
		return creditorcode;
	}

	public void setCreditorcode(String creditorcode){
		this.creditorcode = creditorcode;
	}

	public String getCreditorname(){
		return creditorname;
	}

	public void setCreditorname(String creditorname){
		this.creditorname = creditorname;
	}

	public String getCreditornamen(){
		return creditornamen;
	}

	public void setCreditornamen(String creditornamen){
		this.creditornamen = creditornamen;
	}

	public String getCreditortype(){
		return creditortype;
	}

	public void setCreditortype(String creditortype){
		this.creditortype = creditortype;
	}

	public String getCrehqcode(){
		return crehqcode;
	}

	public void setCrehqcode(String crehqcode){
		this.crehqcode = crehqcode;
	}

	public String getOpercode(){
		return opercode;
	}

	public void setOpercode(String opercode){
		this.opercode = opercode;
	}

	public String getInprterm(){
		return inprterm;
	}

	public void setInprterm(String inprterm){
		this.inprterm = inprterm;
	}

	public String getSpapreboindex(){
		return spapreboindex;
	}

	public void setSpapreboindex(String spapreboindex){
		this.spapreboindex = spapreboindex;
	}

	public String getAppcode(){
		return appcode;
	}

	public void setAppcode(String appcode){
		this.appcode = appcode;
	}

	public String getAppname(){
		return appname;
	}

	public void setAppname(String appname){
		this.appname = appname;
	}

	public String getInltcabuscode(){
		return inltcabuscode;
	}

	public void setInltcabuscode(String inltcabuscode){
		this.inltcabuscode = inltcabuscode;
	}

	public String getIsincode(){
		return isincode;
	}

	public void setIsincode(String isincode){
		this.isincode = isincode;
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

	public String getChangtype(){
		return changtype;
	}

	public void setChangtype(String changtype){
		this.changtype = changtype;
	}

	public String getChdate(){
		return chdate;
	}

	public void setChdate(String chdate){
		this.chdate = chdate;
	}

	public String getChcurrency(){
		return chcurrency;
	}

	public void setChcurrency(String chcurrency){
		this.chcurrency = chcurrency;
	}

	public BigDecimal getChamount(){
		return chamount;
	}

	public void setChamount(BigDecimal chamount){
		this.chamount = chamount;
	}

	public BigDecimal getFairvalue(){
		return fairvalue;
	}

	public void setFairvalue(BigDecimal fairvalue){
		this.fairvalue = fairvalue;
	}

	public BigDecimal getAccoamount(){
		return accoamount;
	}

	public void setAccoamount(BigDecimal accoamount){
		this.accoamount = accoamount;
	}
}
