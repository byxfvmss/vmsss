package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:37:06 PM
 * @描述: [Self_F_STRDE]商业银行人民币结构性存款信息
 */
public class Self_F_STRDE extends SelfEntity{

	private String strdecode;// 人民币结构性存款编号
	private String branchcode;// 金融机构标识码
	private String clientcode;// 客户代码
	private String clientname;// 客户名称
	private String contractdate;// 签约日期
	private String contract;// 合同号
	private BigDecimal contractamount;// 签约金额
	private String maturity;// 到期日
	private String lincame;// 挂钩指标
	private String lincamethod;// 挂钩指标计算方法
	private BigDecimal aginraup;// 约定的利率上限
	private BigDecimal aginralo;// 约定的利率下限
	private String aginraloinpay;// 利息给付方式
	private String tertype;// 终止类型
	private String terpaycode;// 终止支付编号
	private String terdate;// 终止日期
	private BigDecimal terpayamtormb;// 终止支付金额合计折人民币
	private BigDecimal terrmbpayam;// 终止人民币支付金额
	private String terpaycurr;// 终止外币支付币种
	private BigDecimal terpaycurram;// 终止外币支付金额
	private String inpaycode;// 付息编号
	private String inpaymonth;// 付息年月
	private BigDecimal inpayrmbam;// 付息人民币支付金额
	private String inpaycurr;// 付息外币支付币种
	private BigDecimal inpaycurram;// 付息外币支付金额
	private String buocmonth;// 报告期
	private String currency;// 币种
	private BigDecimal moexamusd;// 本月汇出金额折美元
	private BigDecimal moamreusd;// 本月汇入金额折美元
	private BigDecimal mopfexamusd;// 本月购汇金额折美元
	private BigDecimal mosettamusd;// 本月结汇金额折美元

	public String getStrdecode(){
		return strdecode;
	}

	public void setStrdecode(String strdecode){
		this.strdecode = strdecode;
	}

	public String getBranchcode(){
		return branchcode;
	}

	public void setBranchcode(String branchcode){
		this.branchcode = branchcode;
	}

	public String getClientcode(){
		return clientcode;
	}

	public void setClientcode(String clientcode){
		this.clientcode = clientcode;
	}

	public String getClientname(){
		return clientname;
	}

	public void setClientname(String clientname){
		this.clientname = clientname;
	}

	public String getContractdate(){
		return contractdate;
	}

	public void setContractdate(String contractdate){
		this.contractdate = contractdate;
	}

	public String getContract(){
		return contract;
	}

	public void setContract(String contract){
		this.contract = contract;
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

	public String getLincame(){
		return lincame;
	}

	public void setLincame(String lincame){
		this.lincame = lincame;
	}

	public String getLincamethod(){
		return lincamethod;
	}

	public void setLincamethod(String lincamethod){
		this.lincamethod = lincamethod;
	}

	public BigDecimal getAginraup(){
		return aginraup;
	}

	public void setAginraup(BigDecimal aginraup){
		this.aginraup = aginraup;
	}

	public BigDecimal getAginralo(){
		return aginralo;
	}

	public void setAginralo(BigDecimal aginralo){
		this.aginralo = aginralo;
	}

	public String getAginraloinpay(){
		return aginraloinpay;
	}

	public void setAginraloinpay(String aginraloinpay){
		this.aginraloinpay = aginraloinpay;
	}

	public String getTertype(){
		return tertype;
	}

	public void setTertype(String tertype){
		this.tertype = tertype;
	}

	public String getTerpaycode(){
		return terpaycode;
	}

	public void setTerpaycode(String terpaycode){
		this.terpaycode = terpaycode;
	}

	public String getTerdate(){
		return terdate;
	}

	public void setTerdate(String terdate){
		this.terdate = terdate;
	}

	public BigDecimal getTerpayamtormb(){
		return terpayamtormb;
	}

	public void setTerpayamtormb(BigDecimal terpayamtormb){
		this.terpayamtormb = terpayamtormb;
	}

	public BigDecimal getTerrmbpayam(){
		return terrmbpayam;
	}

	public void setTerrmbpayam(BigDecimal terrmbpayam){
		this.terrmbpayam = terrmbpayam;
	}

	public String getTerpaycurr(){
		return terpaycurr;
	}

	public void setTerpaycurr(String terpaycurr){
		this.terpaycurr = terpaycurr;
	}

	public BigDecimal getTerpaycurram(){
		return terpaycurram;
	}

	public void setTerpaycurram(BigDecimal terpaycurram){
		this.terpaycurram = terpaycurram;
	}

	public String getInpaycode(){
		return inpaycode;
	}

	public void setInpaycode(String inpaycode){
		this.inpaycode = inpaycode;
	}

	public String getInpaymonth(){
		return inpaymonth;
	}

	public void setInpaymonth(String inpaymonth){
		this.inpaymonth = inpaymonth;
	}

	public BigDecimal getInpayrmbam(){
		return inpayrmbam;
	}

	public void setInpayrmbam(BigDecimal inpayrmbam){
		this.inpayrmbam = inpayrmbam;
	}

	public String getInpaycurr(){
		return inpaycurr;
	}

	public void setInpaycurr(String inpaycurr){
		this.inpaycurr = inpaycurr;
	}

	public BigDecimal getInpaycurram(){
		return inpaycurram;
	}

	public void setInpaycurram(BigDecimal inpaycurram){
		this.inpaycurram = inpaycurram;
	}

	public String getBuocmonth(){
		return buocmonth;
	}

	public void setBuocmonth(String buocmonth){
		this.buocmonth = buocmonth;
	}

	public String getCurrency(){
		return currency;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public BigDecimal getMoexamusd(){
		return moexamusd;
	}

	public void setMoexamusd(BigDecimal moexamusd){
		this.moexamusd = moexamusd;
	}

	public BigDecimal getMoamreusd(){
		return moamreusd;
	}

	public void setMoamreusd(BigDecimal moamreusd){
		this.moamreusd = moamreusd;
	}

	public BigDecimal getMopfexamusd(){
		return mopfexamusd;
	}

	public void setMopfexamusd(BigDecimal mopfexamusd){
		this.mopfexamusd = mopfexamusd;
	}

	public BigDecimal getMosettamusd(){
		return mosettamusd;
	}

	public void setMosettamusd(BigDecimal mosettamusd){
		this.mosettamusd = mosettamusd;
	}
}
