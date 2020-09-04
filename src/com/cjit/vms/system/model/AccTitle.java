package com.cjit.vms.system.model;

public class AccTitle {
	
	private String accTitleId;
	
	private String accTitleCode;
	
	private String accTitleName;
	
	private String parentAccTitleCode;//父科目code
	
	private String parentAccTitleName;//父科目名称

	public String getAccTitleId() {
		return accTitleId;
	}

	public void setAccTitleId(String accTitleId) {
		this.accTitleId = accTitleId;
	}

	public String getAccTitleCode() {
		return accTitleCode;
	}

	public void setAccTitleCode(String accTitleCode) {
		this.accTitleCode = accTitleCode;
	}

	public String getAccTitleName() {
		return accTitleName;
	}

	public void setAccTitleName(String accTitleName) {
		this.accTitleName = accTitleName;
	}

	public String getParentAccTitleCode() {
		return parentAccTitleCode;
	}

	public void setParentAccTitleCode(String parentAccTitleCode) {
		this.parentAccTitleCode = parentAccTitleCode;
	}

	public String getParentAccTitleName() {
		return parentAccTitleName;
	}

	public void setParentAccTitleName(String parentAccTitleName) {
		this.parentAccTitleName = parentAccTitleName;
	}
	
	
}
