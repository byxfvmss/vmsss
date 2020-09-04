package com.cjit.vms.trans.model;

import java.util.List;

/**
 * 附加税税率维护表情报表
 * 
 * @author lee
 */
public class VmsSurtaxInfo {
	private String taxpayerId; // 纳税人识别号
	private String taxperName; // 纳税人名称
	private String surtaxType; // 附加税类型
	private String surtaxName; // 附加税名称
	private String surtaxRate; // 附加税税率
	private String surtaxStrDt; // 附加税起始日期
	private String surtaxEndDt; // 附加税终止日期
	private String user_id;//用户ＩＤ
	private List lstAuthInstId;
	
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	} 

	public String getTaxpayerId() {
		return taxpayerId;
	}
	public void setTaxpayerId(String taxpayerId) {
		this.taxpayerId = taxpayerId;
	}
	public String getTaxperName() {
		return taxperName;
	}
	public void setTaxperName(String taxperName) {
		this.taxperName = taxperName;
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
	public String getSurtaxStrDt() {
		return surtaxStrDt;
	}
	public void setSurtaxStrDt(String surtaxStrDt) {
		this.surtaxStrDt = surtaxStrDt;
	}
	public String getSurtaxEndDt() {
		return surtaxEndDt;
	}
	public void setSurtaxEndDt(String surtaxEndDt) {
		this.surtaxEndDt = surtaxEndDt;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	
}
