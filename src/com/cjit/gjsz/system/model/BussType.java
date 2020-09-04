package com.cjit.gjsz.system.model;

import java.io.Serializable;

/**
 * @作者: lihaiboA
 * @日期: Sep 30, 2013 10:39:18 AM
 * @描述: [BussType]业务类型实体类
 */
public class BussType implements Serializable{

	private static final long serialVersionUID = -1L;
	private String bussTypeCode = "";
	private String bussTypeName = "";
	private String isEnabled = "1";

	public String getBussTypeCode(){
		return bussTypeCode;
	}

	public void setBussTypeCode(String bussTypeCode){
		this.bussTypeCode = bussTypeCode;
	}

	public String getBussTypeName(){
		return bussTypeName;
	}

	public void setBussTypeName(String bussTypeName){
		this.bussTypeName = bussTypeName;
	}

	public String getIsEnabled(){
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled){
		this.isEnabled = isEnabled;
	}
}
