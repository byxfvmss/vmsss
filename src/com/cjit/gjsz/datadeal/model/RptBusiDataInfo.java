package com.cjit.gjsz.datadeal.model;

/**
 * @作者: lihaiboA
 * @日期: Aug 2, 2012 2:59:47 PM
 */
public class RptBusiDataInfo{

	private String busiDataType;// 数据采集范围（自身业务/代客业务）
	private String busiInfoID;// 业务类型ID
	private String busiInfoName;// 业务类型名称
	private String isShow;// 是否展示
	private String isEnabled;// 是否激活：1-已激活，0-未激活

	public RptBusiDataInfo(){
	}

	public RptBusiDataInfo(String busiDataType, String isShow, String isEnabled){
		this.busiDataType = busiDataType;
		this.isShow = isShow;
		this.isEnabled = isEnabled;
	}

	public String getBusiDataType(){
		return busiDataType;
	}

	public void setBusiDataType(String busiDataType){
		this.busiDataType = busiDataType;
	}

	public String getBusiInfoID(){
		return busiInfoID;
	}

	public void setBusiInfoID(String busiInfoID){
		this.busiInfoID = busiInfoID;
	}

	public String getBusiInfoName(){
		return busiInfoName;
	}

	public void setBusiInfoName(String busiInfoName){
		this.busiInfoName = busiInfoName;
	}

	public String getIsShow(){
		return isShow;
	}

	public void setIsShow(String isShow){
		this.isShow = isShow;
	}

	public String getIsEnabled(){
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled){
		this.isEnabled = isEnabled;
	}
}
