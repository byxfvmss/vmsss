package com.cjit.gjsz.logic.model;

public class CompanyOpenInfo extends Entity{

	private String branchcode;
	private String contact;
	private String tel;
	private String fax;
	private String subid;

	public String getSubid(){
		return subid;
	}

	public void setSubid(String subid){
		this.subid = subid;
	}

	public String getBranchcode(){
		return branchcode;
	}

	public void setBranchcode(String branchcode){
		this.branchcode = branchcode;
	}

	public String getContact(){
		return contact;
	}

	public void setContact(String contact){
		this.contact = contact;
	}

	public String getTel(){
		return tel;
	}

	public void setTel(String tel){
		this.tel = tel;
	}

	public String getFax(){
		return fax;
	}

	public void setFax(String fax){
		this.fax = fax;
	}
}
