package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:38:40 PM
 * @描述: [Self_C_DOFOEXLO]国内外汇贷款信息
 */
public class Self_C_DOFOEXLO extends SelfEntity{

	private String dofoexlocode; // 国内外汇贷款编号
	private String creditorcode; // 债权人代码
	private String debtorcode; // 债务人代码
	private String debtorname; // 债务人中文名称
	private String dofoexlotype; // 国内外汇贷款类型
	private String lenproname; // 转贷项目名称
	private String lenagree; // 转贷协议号
	private String valuedate; // 起息日
	private String maturity; // 到期日
	private String currence; // 贷款币种
	private BigDecimal contractamount; // 签约金额
	private BigDecimal anninrate; // 年化利率值
	private String buscode; // 银行业务参号
	private String changeno; // 变动编号
	private BigDecimal loanopenbalan; // 期初余额
	private String changedate; // 变动日期
	private String withcurrence; // 提款币种
	private BigDecimal withamount; // 提款金额
	private BigDecimal settamount; // 结汇金额
	private String useofunds; // 资金用途
	private String princurr; // 还本币种
	private BigDecimal repayamount; // 还本金额
	private BigDecimal prepayamount; // 购汇还本金额
	private String inpaycurr; // 付息币种
	private BigDecimal inpayamount; // 付息金额
	private BigDecimal pinpayamount; // 购汇付息金额
	private BigDecimal endbalan; // 期末余额

	public String getDofoexlocode(){
		return dofoexlocode;
	}

	public void setDofoexlocode(String dofoexlocode){
		this.dofoexlocode = dofoexlocode;
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

	public String getDofoexlotype(){
		return dofoexlotype;
	}

	public void setDofoexlotype(String dofoexlotype){
		this.dofoexlotype = dofoexlotype;
	}

	public String getLenproname(){
		return lenproname;
	}

	public void setLenproname(String lenproname){
		this.lenproname = lenproname;
	}

	public String getLenagree(){
		return lenagree;
	}

	public void setLenagree(String lenagree){
		this.lenagree = lenagree;
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

	public String getCurrence(){
		return currence;
	}

	public void setCurrence(String currence){
		this.currence = currence;
	}

	public BigDecimal getContractamount(){
		return contractamount;
	}

	public void setContractamount(BigDecimal contractamount){
		this.contractamount = contractamount;
	}

	public BigDecimal getAnninrate(){
		return anninrate;
	}

	public void setAnninrate(BigDecimal anninrate){
		this.anninrate = anninrate;
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

	public BigDecimal getLoanopenbalan(){
		return loanopenbalan;
	}

	public void setLoanopenbalan(BigDecimal loanopenbalan){
		this.loanopenbalan = loanopenbalan;
	}

	public String getChangedate(){
		return changedate;
	}

	public void setChangedate(String changedate){
		this.changedate = changedate;
	}

	public String getWithcurrence(){
		return withcurrence;
	}

	public void setWithcurrence(String withcurrence){
		this.withcurrence = withcurrence;
	}

	public BigDecimal getWithamount(){
		return withamount;
	}

	public void setWithamount(BigDecimal withamount){
		this.withamount = withamount;
	}

	public BigDecimal getSettamount(){
		return settamount;
	}

	public void setSettamount(BigDecimal settamount){
		this.settamount = settamount;
	}

	public String getUseofunds(){
		return useofunds;
	}

	public void setUseofunds(String useofunds){
		this.useofunds = useofunds;
	}

	public String getPrincurr(){
		return princurr;
	}

	public void setPrincurr(String princurr){
		this.princurr = princurr;
	}

	public BigDecimal getRepayamount(){
		return repayamount;
	}

	public void setRepayamount(BigDecimal repayamount){
		this.repayamount = repayamount;
	}

	public BigDecimal getPrepayamount(){
		return prepayamount;
	}

	public void setPrepayamount(BigDecimal prepayamount){
		this.prepayamount = prepayamount;
	}

	public String getInpaycurr(){
		return inpaycurr;
	}

	public void setInpaycurr(String inpaycurr){
		this.inpaycurr = inpaycurr;
	}

	public BigDecimal getInpayamount(){
		return inpayamount;
	}

	public void setInpayamount(BigDecimal inpayamount){
		this.inpayamount = inpayamount;
	}

	public BigDecimal getPinpayamount(){
		return pinpayamount;
	}

	public void setPinpayamount(BigDecimal pinpayamount){
		this.pinpayamount = pinpayamount;
	}

	public BigDecimal getEndbalan(){
		return endbalan;
	}

	public void setEndbalan(BigDecimal endbalan){
		this.endbalan = endbalan;
	}
}
