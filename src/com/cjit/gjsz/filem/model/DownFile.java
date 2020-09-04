package com.cjit.gjsz.filem.model;

import java.util.Date;

public class DownFile{

	private String fileName;
	private String filePath;
	private String fileDesc;
	private Date fileCreateDate;
	private String sendMts;

	public String getFileName(){
		return fileName;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getFilePath(){
		return filePath;
	}

	public void setFilePath(String filePath){
		this.filePath = filePath;
	}

	public String getFileDesc(){
		return fileDesc;
	}

	public void setFileDesc(String fileDesc){
		this.fileDesc = fileDesc;
	}

	public Date getFileCreateDate(){
		return fileCreateDate;
	}

	public void setFileCreateDate(Date fileCreateDate){
		this.fileCreateDate = fileCreateDate;
	}

	public String getSendMts(){
		return sendMts;
	}

	public String getSendMtsDesc(){
		if("1".equals(sendMts)){
			return "已上传";
		}else{
			return "未上传";
		}
	}

	public void setSendMts(String sendMts){
		this.sendMts = sendMts;
	}

	public String getFilePackPath(){
		if(this.filePath != null && this.filePath.length() > 25){
			return this.filePath.substring(0, 25);
		}else{
			return "";
		}
	}
}
