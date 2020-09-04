package com.cjit.vms.trans.model;

import java.util.List;

/**
 * 机构下拉框实体类
 * @author admin
 *
 */
public class InstInfo {
	
	private String instId;
	private String instName;
	private String userId;
	private String tanNo;
	private List lstAuthInstIds;
	private String taxFlag;
	
	
	public List getLstAuthInstIds() {
		return lstAuthInstIds;
	}
	public void setLstAuthInstIds(List lstAuthInstIds) {
		this.lstAuthInstIds = lstAuthInstIds;
	} 

	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTanNo() {
		return tanNo;
	}
	public void setTanNo(String tanNo) {
		this.tanNo = tanNo;
	}
	public String getTaxFlag() {
		return taxFlag;
	}
	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}
}
