package com.cjit.gjsz.logic.model;

import java.math.BigInteger;

public abstract class DeclareEntity extends Entity{

	private String actiontype;
	private String actiondesc;
	private String rptno;
	private String country;
	private String paytype;
	private String txcode;
	private BigInteger tc1amt;
	private String txrem;
	private String txcode2;
	private BigInteger tc2amt;
	private String tx2rem;
	private String isref;
	private String billno;
	private String crtuser;
	private String inptelc;
	private String rptdate;
	private String instcode;
	private String auditname;
	private Integer datastatus;
	private String importdate;
	private String impdate;
	private String businessid;
	private String regno;
	private String payattr;

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

	public String getCountry(){
		return country;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getPaytype(){
		return paytype;
	}

	public void setPaytype(String paytype){
		this.paytype = paytype;
	}

	public String getTxcode(){
		return txcode;
	}

	public void setTxcode(String txcode){
		this.txcode = txcode;
	}

	public BigInteger getTc1amt(){
		return tc1amt;
	}

	public void setTc1amt(BigInteger tc1amt){
		this.tc1amt = tc1amt;
	}

	public String getTxrem(){
		return txrem;
	}

	public void setTxrem(String txrem){
		this.txrem = txrem;
	}

	public String getTxcode2(){
		return txcode2;
	}

	public void setTxcode2(String txcode2){
		this.txcode2 = txcode2;
	}

	public BigInteger getTc2amt(){
		return tc2amt;
	}

	public void setTc2amt(BigInteger tc2amt){
		this.tc2amt = tc2amt;
	}

	public String getTx2rem(){
		return tx2rem;
	}

	public void setTx2rem(String tx2rem){
		this.tx2rem = tx2rem;
	}

	public String getIsref(){
		return isref;
	}

	public void setIsref(String isref){
		this.isref = isref;
	}

	public String getBillno(){
		return billno;
	}

	public void setBillno(String billno){
		this.billno = billno;
	}

	public String getCrtuser(){
		return crtuser;
	}

	public void setCrtuser(String crtuser){
		this.crtuser = crtuser;
	}

	public String getInptelc(){
		return inptelc;
	}

	public void setInptelc(String inptelc){
		this.inptelc = inptelc;
	}

	public String getRptdate(){
		return rptdate;
	}

	public void setRptdate(String rptdate){
		this.rptdate = rptdate;
	}

	public String getInstcode(){
		return instcode;
	}

	public void setInstcode(String instcode){
		this.instcode = instcode;
	}

	public String getAuditname(){
		return auditname;
	}

	public void setAuditname(String auditname){
		this.auditname = auditname;
	}

	public Integer getDatastatus(){
		return datastatus;
	}

	public void setDatastatus(Integer datastatus){
		this.datastatus = datastatus;
	}

	public String getImportdate(){
		return importdate;
	}

	public void setImportdate(String importdate){
		this.importdate = importdate;
	}

	public String getBusinessid(){
		return businessid;
	}

	public void setBusinessid(String businessid){
		this.businessid = businessid;
	}

	public String getImpdate(){
		return impdate;
	}

	public void setImpdate(String impdate){
		this.impdate = impdate;
	}

	public String getRegno(){
		return regno;
	}

	public void setRegno(String regno){
		this.regno = regno;
	}

	public String getPayattr(){
		return payattr;
	}

	public void setPayattr(String payattr){
		this.payattr = payattr;
	}
}
