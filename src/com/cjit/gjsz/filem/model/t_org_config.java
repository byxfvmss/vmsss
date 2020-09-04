package com.cjit.gjsz.filem.model;

public class t_org_config{

	// 机构编码和机构申报号
	private String org_Id;
	private String name;
	private String rptNo;
	private String rptTitle;

	public String getOrg_Id(){
		return org_Id;
	}

	public void setOrg_Id(String org_Id){
		this.org_Id = org_Id;
	}

	public String getRptNo(){
		return rptNo;
	}

	public void setRptNo(String rptNo){
		this.rptNo = rptNo;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getRptTitle(){
		return rptTitle;
	}

	public void setRptTitle(String rptTitle){
		this.rptTitle = rptTitle;
	}
}
