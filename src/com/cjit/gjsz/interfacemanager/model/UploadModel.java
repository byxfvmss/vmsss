/**
 * TableInfo
 */
package com.cjit.gjsz.interfacemanager.model;

import java.io.File;

/**
 * @author huboA
 * @table t_rpt_table_info
 */
public class UploadModel{

	private int id;
	private File file;
	private String fileContentType;
	private String fileFileName;
	private String resetDataStatus;

	public int getId(){
		return id;
	}

	public File getFile(){
		return file;
	}

	public String getFileContentType(){
		return fileContentType;
	}

	public String getFileFileName(){
		return fileFileName;
	}

	public void setId(int id){
		this.id = id;
	}

	public void setFile(File file){
		this.file = file;
	}

	public void setFileContentType(String fileContentType){
		this.fileContentType = fileContentType;
	}

	public void setFileFileName(String fileFileName){
		this.fileFileName = fileFileName;
	}

	public String getResetDataStatus(){
		return resetDataStatus;
	}

	public void setResetDataStatus(String resetDataStatus){
		this.resetDataStatus = resetDataStatus;
	}
}
