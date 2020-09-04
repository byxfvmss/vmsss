package com.cjit.gjsz.filem.model;

//组织机构代码名称对照表
public class CustomerConfigEntity{

	// Customer ID
	private String custId = "";
	// 组织机构代码
	private String custCode = "";
	// 组织机构名称
	private String custName = "";
	// 机构代码
	private String instCode = "";
	// 维护人员姓名
	private String userName = "";
	// 维护时间
	private String modifyTime = "";

	public String getCustId(){
		return custId == null ? "" : custId.trim();
	}

	public void setCustId(String custId){
		this.custId = custId;
	}

	public String getCustCode(){
		return custCode == null ? "" : custCode.trim();
	}

	public void setCustCode(String custCode){
		this.custCode = custCode;
	}

	public String getCustName(){
		return custName == null ? "" : custName.trim();
	}

	public void setCustName(String custName){
		this.custName = custName;
	}

	public String getInstCode(){
		return instCode == null ? "" : instCode.trim();
	}

	public void setInstCode(String instCode){
		this.instCode = instCode;
	}

	public String getUserName(){
		return userName == null ? "" : userName.trim();
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getModifyTime(){
		return modifyTime == null ? "" : modifyTime.trim();
	}

	public String getModifyTimeView(){
		if(modifyTime != null && modifyTime.length() == 17){
			String year = modifyTime.substring(0, 4);
			String month = modifyTime.substring(4, 6);
			String day = modifyTime.substring(6, 8);
			String hour = modifyTime.substring(8, 10);
			String minute = modifyTime.substring(10, 12);
			String second = modifyTime.substring(12, 14);
			return year + "-" + month + "-" + day + " " + hour + ":" + minute
					+ ":" + second;
		}
		return modifyTime;
	}

	public void setModifyTime(String modifyTime){
		this.modifyTime = modifyTime;
	}
}