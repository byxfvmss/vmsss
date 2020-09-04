package com.cjit.vms.trans.model;

import java.math.BigDecimal;
import java.util.List;


public class ParamInSurtaxListInfo {
	private String dataDt;//数据时间
	private String instId;
	private String instName;//机构名称
	private String taxpayerId; //纳税人识别号
	private String taxpayerName; //纳税人名称
	private BigDecimal taxfreeIncome;//免税收入
	private BigDecimal assessableIncome;//征税收入
	private BigDecimal vatOutProportion;//转出比例
	private BigDecimal vatOutAmt;//转出金额
	private String proportionFlg ;//标记
	private String proportionFlgName;
	
	private List lstAuthInstId;
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getDataDt() {
		return dataDt;
	}
	public void setDataDt(String dataDt) {
		this.dataDt = dataDt;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public String getTaxpayerId() {
		return taxpayerId;
	}
	public void setTaxpayerId(String taxpayerId) {
		this.taxpayerId = taxpayerId;
	}
	public String getTaxpayerName() {
		return taxpayerName;
	}
	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}
	public BigDecimal getTaxfreeIncome() {
		return taxfreeIncome;
	}
	public void setTaxfreeIncome(BigDecimal taxfreeIncome) {
		this.taxfreeIncome = taxfreeIncome;
	}
	public BigDecimal getAssessableIncome() {
		return assessableIncome;
	}
	public void setAssessableIncome(BigDecimal assessableIncome) {
		this.assessableIncome = assessableIncome;
	}
	public BigDecimal getVatOutProportion() {
		return vatOutProportion;
	}
	public void setVatOutProportion(BigDecimal vatOutProportion) {
		this.vatOutProportion = vatOutProportion;
	}
	public BigDecimal getVatOutAmt() {
		return vatOutAmt;
	}
	public void setVatOutAmt(BigDecimal vatOutAmt) {
		this.vatOutAmt = vatOutAmt;
	}
	public String getProportionFlg() {
		return proportionFlg;
	}
	public void setProportionFlg(String proportionFlg) {
		this.proportionFlg = proportionFlg;
	}
	public String getProportionFlgName() {
		return proportionFlgName;
	}
	public void setProportionFlgName(String proportionFlgName) {
		this.proportionFlgName = proportionFlgName;
	}
}
