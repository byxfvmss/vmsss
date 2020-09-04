package com.cjit.vms.trans.model;

import java.text.DecimalFormat;
import java.util.List;

public class InSurtaxListInfo {
	private String taxPerNumber;//纳税人识别号12
	private String taxPerName;//   纳税人名称13
	private String surtaxType; //附加税类型14
	private String surtaxName; // 附件税名称15
	private String surtaxRate;// 附加税税率16
	private String taxAmtCny;//税额17
	private String gatherSurtax;//汇总附加税18
	
	private String surtaxAmt;//明细附加税19
	private String surtax1AmtCny;
	private String surtax2AmtCny;
	private String surtax3AmtCny;
	private String surtax4AmtCny;
	
	private String diffSurtax;//附加税差异
	private String instCode;//机构号21
	private String instName;
	
	private String applyPeriod;//申报周期
	
	//查询条件
	List lstAuthInstId;
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getTaxPerNumber() {
		return taxPerNumber;
	}
	public void setTaxPerNumber(String taxPerNumber) {
		this.taxPerNumber = taxPerNumber;
	}
	public String getTaxPerName() {
		return taxPerName;
	}
	public void setTaxPerName(String taxPerName) {
		this.taxPerName = taxPerName;
	}
	public String getSurtaxType() {
		return surtaxType;
	}
	public void setSurtaxType(String surtaxType) {
		this.surtaxType = surtaxType;
	}
	public String getSurtaxName() {
		return surtaxName;
	}
	public void setSurtaxName(String surtaxName) {
		this.surtaxName = surtaxName;
	}
	public String getSurtaxRate() {
		return this.surtaxRate;
	}
	public void setSurtaxRate(String surtaxRate) {
		this.surtaxRate = surtaxRate;
	}
	public String getTaxAmtCny() {
		try{
			this.taxAmtCny = new DecimalFormat("#.00").format(Double.parseDouble(this.taxAmtCny));
		}catch(Exception e){
			this.taxAmtCny = "0.00";
		}
		return this.taxAmtCny;
	}
	public void setTaxAmtCny(String taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}
	public String getGatherSurtax() {
		try{
			this.gatherSurtax = new DecimalFormat("#.00").format(Double.parseDouble(this.gatherSurtax));
		}catch(Exception e){
			this.gatherSurtax = "0.00";
		}
		return this.gatherSurtax;
	}
	public void setGatherSurtax(String gatherSurtax) {
		this.gatherSurtax = gatherSurtax;
	}
	public String getSurtaxAmt() {
		if("1".equals(this.surtaxType)){
			if(Double.parseDouble(this.surtax1AmtCny)>0){
				this.surtaxAmt = new DecimalFormat("#.00").format(Double.parseDouble(this.surtax1AmtCny));
			} else {
				this.surtaxAmt = "0.00";
			}
		}
		if("2".equals(this.surtaxType)){
			if(Double.parseDouble(this.surtax2AmtCny)>0){
				this.surtaxAmt = new DecimalFormat("#.00").format(Double.parseDouble(this.surtax2AmtCny));
			} else {
				this.surtaxAmt = "0.00";
			}
		}
		if("3".equals(this.surtaxType)){
			if(Double.parseDouble(this.surtax3AmtCny)>0){
				this.surtaxAmt = new DecimalFormat("#.00").format(Double.parseDouble(this.surtax3AmtCny));
			} else {
				this.surtaxAmt = "0.00";
			}
		}
		if("4".equals(this.surtaxType)){
			if(Double.parseDouble(this.surtax4AmtCny)>0){
				this.surtaxAmt = new DecimalFormat("#.00").format(Double.parseDouble(this.surtax4AmtCny));
			} else {
				this.surtaxAmt = "0.00";
			}
		}
		return this.surtaxAmt;
	}
	public void setSurtaxAmt(String surtaxAmt) {
		this.surtaxAmt = surtaxAmt;
	}
	public String getSurtax1AmtCny() {
		return surtax1AmtCny;
	}
	public void setSurtax1AmtCny(String surtax1AmtCny) {
		this.surtax1AmtCny = surtax1AmtCny;
	}
	public String getSurtax2AmtCny() {
		return surtax2AmtCny;
	}
	public void setSurtax2AmtCny(String surtax2AmtCny) {
		this.surtax2AmtCny = surtax2AmtCny;
	}
	public String getSurtax3AmtCny() {
		return surtax3AmtCny;
	}
	public void setSurtax3AmtCny(String surtax3AmtCny) {
		this.surtax3AmtCny = surtax3AmtCny;
	}
	public String getSurtax4AmtCny() {
		return surtax4AmtCny;
	}
	public void setSurtax4AmtCny(String surtax4AmtCny) {
		this.surtax4AmtCny = surtax4AmtCny;
	}
	public String getDiffSurtax() {
		try{
			return ""+new DecimalFormat("#.00").format(Double.parseDouble(this.gatherSurtax)-Double.parseDouble(this.getSurtaxAmt()));
		}catch(Exception e){
			return "0.00";
		}
	}
	public void setDiffSurtax(String diffSurtax) {
		this.diffSurtax = diffSurtax;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public String getApplyPeriod() {
		return applyPeriod;
	}
	public void setApplyPeriod(String applyPeriod) {
		this.applyPeriod = applyPeriod;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
}
