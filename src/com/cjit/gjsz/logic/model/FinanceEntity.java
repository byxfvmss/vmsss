package com.cjit.gjsz.logic.model;

import java.math.BigInteger;
import java.util.Date;

public abstract class FinanceEntity extends Entity{

	private String actiontype;
	private String actiondesc;
	private String rptno;
	private String paytype;
	private String isref;
	private String crtuser;
	private String inptelc;
	private String rptdate;
	private String instcode;
	private String auditname;
	private Integer datastatus;
	private String importdate;
	private String businessid;
	private String payattr;
	private String exportInfo;
	private BigInteger chkamt;
	private BigInteger osamt;
	private String chkprtd;
	private Date auditdate;
	private String contrno;
	private String invoino;
	private String regno;
	private String cusmno;
	private String billno;
	private BigInteger contamt;
	private String impdate;

	public String getImpdate(){
		return impdate;
	}

	public void setImpdate(String impdate){
		this.impdate = impdate;
	}

	public String getContrno(){
		return contrno;
	}

	public void setContrno(String contrno){
		this.contrno = contrno;
	}

	public String getInvoino(){
		return invoino;
	}

	public void setInvoino(String invoino){
		this.invoino = invoino;
	}

	public String getRegno(){
		return regno;
	}

	public void setRegno(String regno){
		this.regno = regno;
	}

	public String getCusmno(){
		return cusmno;
	}

	public void setCusmno(String cusmno){
		this.cusmno = cusmno;
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

	public String getPaytype(){
		return paytype;
	}

	public void setPaytype(String paytype){
		this.paytype = paytype;
	}

	public String getIsref(){
		return isref;
	}

	public void setIsref(String isref){
		this.isref = isref;
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

	public String getPayattr(){
		return payattr;
	}

	public void setPayattr(String payattr){
		this.payattr = payattr;
	}

	public String getExportInfo(){
		return exportInfo;
	}

	public void setExportInfo(String exportInfo){
		this.exportInfo = exportInfo;
	}

	public BigInteger getChkamt(){
		return chkamt;
	}

	public void setChkamt(BigInteger chkamt){
		this.chkamt = chkamt;
	}

	public Date getAuditdate(){
		return auditdate;
	}

	public void setAuditdate(Date auditdate){
		this.auditdate = auditdate;
	}

	public String getChkprtd(){
		return chkprtd;
	}

	public void setChkprtd(String chkprtd){
		this.chkprtd = chkprtd;
	}

	public BigInteger getOsamt(){
		return osamt;
	}

	public void setOsamt(BigInteger osamt){
		this.osamt = osamt;
	}

	public String getBillno(){
		return billno;
	}

	public void setBillno(String billno){
		this.billno = billno;
	}

	public BigInteger getContamt(){
		return contamt;
	}

	public void setContamt(BigInteger contamt){
		this.contamt = contamt;
	}
}
