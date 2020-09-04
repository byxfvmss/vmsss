package com.cjit.vms.trans.model;

import java.text.DecimalFormat;
import java.util.List;

public class OutSurtaxListInfo {
	private String taxPerNumber;//纳税人识别号12
	private String taxPerName;//   纳税人名称13
	private String surtaxType; //附加税类型14
	private String surtaxName; // 附件税名称15
	private String surtaxRate;// 附加税税率16
	private String taxAmtCny;//销项税17
	//汇总附加税18    18:VMS_TRANS_INFO.TAXAMT税额*VMS_Surtax  附加税税率维护表.Surtax_RATE
	private String gatherSurtax;//18
	
	private String surtaxAmt;//明细附加税19
	private String surtax1AmtCny;//明细附加税19
	private String surtax2AmtCny;
	private String surtax3AmtCny;
	private String surtax4AmtCny;
	private String diffSurtax;//附加税差异
	private String instCode;//机构号21
	private String instName;//机构名称
	
	private String applyPeriod;//申报周期
	
	
	public String getSurtaxAmt() {
		if("1".equals(this.surtaxType)){
			if(Double.parseDouble(this.surtax1AmtCny) > 0){
				this.surtaxAmt = new DecimalFormat("0.00").format(Double.parseDouble(this.surtax1AmtCny));
			} else {
				this.surtaxAmt = "0.00";
			}
		}
		if("2".equals(this.surtaxType)){
			if(Double.parseDouble(this.surtax2AmtCny) > 0){
				this.surtaxAmt = new DecimalFormat("0.00").format(Double.parseDouble(this.surtax2AmtCny));
			} else {
				this.surtaxAmt = "0.00";
			}
		}
		if("3".equals(this.surtaxType)){
			if(Double.parseDouble(this.surtax3AmtCny) > 0){
				this.surtaxAmt = new DecimalFormat("0.00").format(Double.parseDouble(this.surtax3AmtCny));
			} else {
				this.surtaxAmt = "0.00";
			}
		}
		if("4".equals(this.surtaxType)){
			if(Double.parseDouble(this.surtax4AmtCny) > 0){
				this.surtaxAmt =  new DecimalFormat("0.00").format(Double.parseDouble(this.surtax4AmtCny));
			} else {
				this.surtaxAmt = "0.00";
			}
		}
		return this.surtaxAmt;
	}
	public void setSurtaxAmt(String surtaxAmt) {
		this.surtaxAmt = surtaxAmt;
	}
	public String getApplyPeriod() {
		return applyPeriod;
	}
	public void setApplyPeriod(String applyPeriod) {
		this.applyPeriod = applyPeriod;
	}
	//查询条件
	List lstAuthInstId;
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getGatherSurtax() {
		try{
			this.gatherSurtax = new DecimalFormat("0.00").format(Double.parseDouble(this.gatherSurtax))+"";
		}catch(Exception e){
			this.diffSurtax = "0.00";
		}
		return this.gatherSurtax;
	}
	public void setGatherSurtax(String gatherSurtax) {
		this.gatherSurtax = gatherSurtax;
	}
	
	
	public String getDiffSurtax() {
		try{
			if("1".equals(this.surtaxType)){
				this.diffSurtax = new DecimalFormat("0.00").format(Double.parseDouble(this.gatherSurtax)-Double.parseDouble(this.surtax1AmtCny))+"";
			}else if("2".equals(this.surtaxType)){
				this.diffSurtax = new DecimalFormat("0.00").format(Double.parseDouble(this.gatherSurtax)-Double.parseDouble(this.surtax2AmtCny))+"";
			}else if("3".equals(this.surtaxType)){
				this.diffSurtax = new DecimalFormat("0.00").format(Double.parseDouble(this.gatherSurtax)-Double.parseDouble(this.surtax3AmtCny))+"";
			}else if("4".equals(this.surtaxType)){
				this.diffSurtax = new DecimalFormat("0.00").format(Double.parseDouble(this.gatherSurtax)-Double.parseDouble(this.surtax4AmtCny))+"";
			}
		}catch(Exception e){
			this.diffSurtax= "0.00";
		}
		return this.diffSurtax;
	}
	public void setDiffSurtax(String diffSurtax) {
		this.diffSurtax = diffSurtax;
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
		return surtaxRate;
	}
	public void setSurtaxRate(String surtaxRate) {
		this.surtaxRate = surtaxRate;
	}
	public String getTaxAmtCny() {
		try{
			this.taxAmtCny = new DecimalFormat("0.00").format(Double.parseDouble(this.taxAmtCny))+"";
		} catch (Exception e){
			this.taxAmtCny = "0.00";
		}
		return this.taxAmtCny;
	}
	public void setTaxAmtCny(String taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getSurtax1AmtCny() {
		return this.surtax1AmtCny;
	}
	public void setSurtax1AmtCny(String surtax1AmtCny) {
		this.surtax1AmtCny = surtax1AmtCny;
	}
	public String getSurtax2AmtCny() {
		return this.surtax2AmtCny;
	}
	public void setSurtax2AmtCny(String surtax2AmtCny) {
		this.surtax2AmtCny = surtax2AmtCny;
	}
	public String getSurtax3AmtCny() {
		return this.surtax3AmtCny;
	}
	public void setSurtax3AmtCny(String surtax3AmtCny) {
		this.surtax3AmtCny = surtax3AmtCny;
	}
	public String getSurtax4AmtCny() {
		return this.surtax4AmtCny;
	}
	public void setSurtax4AmtCny(String surtax4AmtCny) {
		this.surtax4AmtCny = surtax4AmtCny;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
}
