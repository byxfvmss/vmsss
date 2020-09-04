package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:38:58 PM
 * @描述: [Self_B_EXGUARAN]对外担保信息
 */
public class Self_B_EXGUARAN extends SelfEntity{

	private String exguarancode; // 对外担保编号
	private String guarantorcode; // 担保人代码
	private String contractdate; // 签约日期
	private BigDecimal guaranamount; // 保函金额
	private String guarancurr; // 保函币种
	private String maturity; // 到期日
	private String guarantype; // 担保类型
	private String maindebtcurr; // 主债务币种
	private BigDecimal maindebtamount; // 主债务金额
	private String guappcode; // 担保申请人代码
	private String guappname; // 担保申请人中文名称
	private String guappnamen; // 担保申请人英文名称
	private String appdocuno; // 核准文件号
	private String wabachandate; // 担保责任余额变动日期
	private BigDecimal basere; // 担保责任余额
	private String actiontype; // 操作类型
	private String actiondesc; // 删除原因
	private String complianceno; // 履约编号
	private String buscode; // 银行业务参号
	private String bencode; // 受益人代码
	private String bename; // 受益人中文名称
	private String benamen; // 受益人英文名称
	private String guperdate; // 履约日期
	private String gupercurr; // 履约币种
	private BigDecimal guperamount; // 履约金额
	private BigDecimal pguperamount; // 购汇履约金额

	public String getExguarancode(){
		return exguarancode;
	}

	public void setExguarancode(String exguarancode){
		this.exguarancode = exguarancode;
	}

	public String getGuarantorcode(){
		return guarantorcode;
	}

	public void setGuarantorcode(String guarantorcode){
		this.guarantorcode = guarantorcode;
	}

	public String getContractdate(){
		return contractdate;
	}

	public void setContractdate(String contractdate){
		this.contractdate = contractdate;
	}

	public BigDecimal getGuaranamount(){
		return guaranamount;
	}

	public void setGuaranamount(BigDecimal guaranamount){
		this.guaranamount = guaranamount;
	}

	public String getGuarancurr(){
		return guarancurr;
	}

	public void setGuarancurr(String guarancurr){
		this.guarancurr = guarancurr;
	}

	public String getMaturity(){
		return maturity;
	}

	public void setMaturity(String maturity){
		this.maturity = maturity;
	}

	public String getGuarantype(){
		return guarantype;
	}

	public void setGuarantype(String guarantype){
		this.guarantype = guarantype;
	}

	public String getMaindebtcurr(){
		return maindebtcurr;
	}

	public void setMaindebtcurr(String maindebtcurr){
		this.maindebtcurr = maindebtcurr;
	}

	public BigDecimal getMaindebtamount(){
		return maindebtamount;
	}

	public void setMaindebtamount(BigDecimal maindebtamount){
		this.maindebtamount = maindebtamount;
	}

	public String getGuappcode(){
		return guappcode;
	}

	public void setGuappcode(String guappcode){
		this.guappcode = guappcode;
	}

	public String getGuappname(){
		return guappname;
	}

	public void setGuappname(String guappname){
		this.guappname = guappname;
	}

	public String getGuappnamen(){
		return guappnamen;
	}

	public void setGuappnamen(String guappnamen){
		this.guappnamen = guappnamen;
	}

	public String getAppdocuno(){
		return appdocuno;
	}

	public void setAppdocuno(String appdocuno){
		this.appdocuno = appdocuno;
	}

	public String getWabachandate(){
		return wabachandate;
	}

	public void setWabachandate(String wabachandate){
		this.wabachandate = wabachandate;
	}

	public BigDecimal getBasere(){
		return basere;
	}

	public void setBasere(BigDecimal basere){
		this.basere = basere;
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

	public String getComplianceno(){
		return complianceno;
	}

	public void setComplianceno(String complianceno){
		this.complianceno = complianceno;
	}

	public String getBuscode(){
		return buscode;
	}

	public void setBuscode(String buscode){
		this.buscode = buscode;
	}

	public String getBencode(){
		return bencode;
	}

	public void setBencode(String bencode){
		this.bencode = bencode;
	}

	public String getBename(){
		return bename;
	}

	public void setBename(String bename){
		this.bename = bename;
	}

	public String getBenamen(){
		return benamen;
	}

	public void setBenamen(String benamen){
		this.benamen = benamen;
	}

	public String getGuperdate(){
		return guperdate;
	}

	public void setGuperdate(String guperdate){
		this.guperdate = guperdate;
	}

	public String getGupercurr(){
		return gupercurr;
	}

	public void setGupercurr(String gupercurr){
		this.gupercurr = gupercurr;
	}

	public BigDecimal getGuperamount(){
		return guperamount;
	}

	public void setGuperamount(BigDecimal guperamount){
		this.guperamount = guperamount;
	}

	public BigDecimal getPguperamount(){
		return pguperamount;
	}

	public void setPguperamount(BigDecimal pguperamount){
		this.pguperamount = pguperamount;
	}
}
