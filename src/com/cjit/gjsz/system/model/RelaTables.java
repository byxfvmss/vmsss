package com.cjit.gjsz.system.model;

import java.io.Serializable;

public class RelaTables implements Serializable{

	private static final long serialVersionUID = -1L;
	private String objId;
	private String tableId;
	private String fileType;
	private String objType;
	private String spare1;
	private String spare2;
	private String spare3;
	private String spare4;
	private String spare5;
	private String tableName;
	private String isRela;

	public String getObjId(){
		return objId;
	}

	public void setObjId(String objId){
		this.objId = objId;
	}

	public String getTableId(){
		return tableId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String getFileType(){
		return fileType;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getObjType(){
		return objType;
	}

	public void setObjType(String objType){
		this.objType = objType;
	}

	public String getSpare1(){
		return spare1;
	}

	public void setSpare1(String spare1){
		this.spare1 = spare1;
	}

	public String getSpare2(){
		return spare2;
	}

	public void setSpare2(String spare2){
		this.spare2 = spare2;
	}

	public String getSpare3(){
		return spare3;
	}

	public void setSpare3(String spare3){
		this.spare3 = spare3;
	}

	public String getSpare4(){
		return spare4;
	}

	public void setSpare4(String spare4){
		this.spare4 = spare4;
	}

	public String getSpare5(){
		return spare5;
	}

	public void setSpare5(String spare5){
		this.spare5 = spare5;
	}

	public String getTableName(){
		return tableName;
	}

	public void setTableName(String tableName){
		this.tableName = tableName;
	}

	public String getIsRela(){
		return isRela;
	}

	public void setIsRela(String isRela){
		this.isRela = isRela;
	}
}
