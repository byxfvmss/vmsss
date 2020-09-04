package com.cjit.gjsz.logic.model;

import java.math.BigInteger;

public abstract class BaseEntity extends Entity{

	private String tradedate;
	private String actiontype;
	private String actiondesc;
	private String rptno;
	private String custype;
	private String idcode;
	private String custcod;
	private String custnm;
	private String oppuser;
	private String txccy;
	private BigInteger txamt;
	private Double exrate;
	private BigInteger lcyamt;
	private String lcyacc;
	private BigInteger fcyamt;
	private String fcyacc;
	private BigInteger othamt;
	private String othacc;
	private String method;
	private String buscode;
	private String inchargeccy;
	private BigInteger inchargeamt;
	private String outchargeccy;
	private BigInteger outchargeamt;
	private String actuccy;
	private BigInteger actuamt;

	public String getTradedate(){
		return tradedate;
	}

	public void setTradedate(String tradedate){
		this.tradedate = tradedate;
	}

	public String getActuccy(){
		return actuccy;
	}

	public void setActuccy(String actuccy){
		this.actuccy = actuccy;
	}

	public BigInteger getActuamt(){
		return actuamt;
	}

	public void setActuamt(BigInteger actuamt){
		this.actuamt = actuamt;
	}

	public String getActiontype(){
		return actiontype;
	}

	public void setActiontype(String actiontype){
		this.actiontype = actiontype;
	}

	public String getActiondesc(){
		return actiondesc;
	}

	public void setActiondesc(String actiondesc){
		this.actiondesc = actiondesc;
	}

	public String getRptno(){
		return rptno;
	}

	public void setRptno(String rptno){
		this.rptno = rptno;
	}

	public String getCustype(){
		return custype;
	}

	public void setCustype(String custype){
		this.custype = custype;
	}

	public String getIdcode(){
		return idcode;
	}

	public void setIdcode(String idcode){
		this.idcode = idcode;
	}

	public String getCustcod(){
		return custcod;
	}

	public void setCustcod(String custcod){
		this.custcod = custcod;
	}

	public String getCustnm(){
		return custnm;
	}

	public void setCustnm(String custnm){
		this.custnm = custnm;
	}

	public String getOppuser(){
		return oppuser;
	}

	public void setOppuser(String oppuser){
		this.oppuser = oppuser;
	}

	public String getTxccy(){
		return txccy;
	}

	public void setTxccy(String txccy){
		this.txccy = txccy;
	}

	public BigInteger getTxamt(){
		return txamt;
	}

	public void setTxamt(BigInteger txamt){
		this.txamt = txamt;
	}

	public Double getExrate(){
		return exrate;
	}

	public void setExrate(Double exrate){
		this.exrate = exrate;
	}

	public BigInteger getLcyamt(){
		return lcyamt;
	}

	public void setLcyamt(BigInteger lcyamt){
		this.lcyamt = lcyamt;
	}

	public String getLcyacc(){
		return lcyacc;
	}

	public void setLcyacc(String lcyacc){
		this.lcyacc = lcyacc;
	}

	public BigInteger getFcyamt(){
		return fcyamt;
	}

	public void setFcyamt(BigInteger fcyamt){
		this.fcyamt = fcyamt;
	}

	public String getFcyacc(){
		return fcyacc;
	}

	public void setFcyacc(String fcyacc){
		this.fcyacc = fcyacc;
	}

	public BigInteger getOthamt(){
		return othamt;
	}

	public void setOthamt(BigInteger othamt){
		this.othamt = othamt;
	}

	public String getOthacc(){
		return othacc;
	}

	public void setOthacc(String othacc){
		this.othacc = othacc;
	}

	public String getMethod(){
		return method;
	}

	public void setMethod(String method){
		this.method = method;
	}

	public String getBuscode(){
		return buscode;
	}

	public void setBuscode(String buscode){
		this.buscode = buscode;
	}

	public String getInchargeccy(){
		return inchargeccy;
	}

	public void setInchargeccy(String inchargeccy){
		this.inchargeccy = inchargeccy;
	}

	public BigInteger getInchargeamt(){
		return inchargeamt;
	}

	public void setInchargeamt(BigInteger inchargeamt){
		this.inchargeamt = inchargeamt;
	}

	public String getOutchargeccy(){
		return outchargeccy;
	}

	public void setOutchargeccy(String outchargeccy){
		this.outchargeccy = outchargeccy;
	}

	public BigInteger getOutchargeamt(){
		return outchargeamt;
	}

	public void setOutchargeamt(BigInteger outchargeamt){
		this.outchargeamt = outchargeamt;
	}
}
