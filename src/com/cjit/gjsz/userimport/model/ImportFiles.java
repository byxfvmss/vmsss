package com.cjit.gjsz.userimport.model;

public class ImportFiles{

	private String iFileName;
	private String iFileDesc;
	private String iFilePath;
	private String orgId;

	public String getIFileName(){
		return iFileName;
	}

	public void setIFileName(String fileName){
		iFileName = fileName;
	}

	public String getIFileDesc(){
		return iFileDesc;
	}

	public void setIFileDesc(String fileDesc){
		iFileDesc = fileDesc;
	}

	public String getIFilePath(){
		return iFilePath;
	}

	public void setIFilePath(String filePath){
		iFilePath = filePath;
	}

	public String getOrgId(){
		return orgId;
	}

	public void setOrgId(String orgId){
		this.orgId = orgId;
	}
}
