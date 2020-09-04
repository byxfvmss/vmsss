package com.cjit.gjsz.userimport.model;

public class ImportBats{

	private String bFileName;
	private String bFilePath;
	private String orgId;

	public String getBFileName(){
		return bFileName;
	}

	public void setBFileName(String fileName){
		bFileName = fileName;
	}

	public String getBFilePath(){
		return bFilePath;
	}

	public void setBFilePath(String filePath){
		bFilePath = filePath;
	}

	public String getOrgId(){
		return orgId;
	}

	public void setOrgId(String orgId){
		this.orgId = orgId;
	}
}
