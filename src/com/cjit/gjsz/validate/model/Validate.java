package com.cjit.gjsz.validate.model;

import java.io.Serializable;

public class Validate implements Serializable{

	private int id; // 主键
	private String tablecn;// 
	private String tableId;// 区分收支余和开关户
	private String errfield;
	private String errfieldcn;
	private String errdesc;
	private String userId;
	private String operateDate;
	private String businessid;
	private String bussno;
	private String column;

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public String getTableId(){
		return tableId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String getErrfield(){
		return errfield;
	}

	public void setErrfield(String errfield){
		this.errfield = errfield;
	}

	public String getErrfieldcn(){
		return errfieldcn;
	}

	public void setErrfieldcn(String errfieldcn){
		this.errfieldcn = errfieldcn;
	}

	public String getErrdesc(){
		return errdesc;
	}

	public void setErrdesc(String errdesc){
		this.errdesc = errdesc;
	}

	public String getUserId(){
		return userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getOperateDate(){
		return operateDate;
	}

	public void setOperateDate(String operateDate){
		this.operateDate = operateDate;
	}

	public String getBusinessid(){
		return businessid;
	}

	public void setBusinessid(String businessid){
		this.businessid = businessid;
	}

	public String getBussno(){
		return bussno;
	}

	public void setBussno(String bussno){
		this.bussno = bussno;
	}

	public String getTablecn(){
		return tablecn;
	}

	public void setTablecn(String tablecn){
		this.tablecn = tablecn;
	}

	public String getColumn(){
		return column;
	}

	public void setColumn(String column){
		this.column = column;
	}
}
