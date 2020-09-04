package com.cjit.gjsz.interfacemanager.model;

public class ConfigParameter{

	private String configName;
	private String configValue;
	private String configDesc;
	private String configProj;
	private String createDate;

	public String getConfigName(){
		return configName;
	}

	public void setConfigName(String configName){
		this.configName = configName;
	}

	public String getConfigValue(){
		return configValue;
	}

	public void setConfigValue(String configValue){
		this.configValue = configValue;
	}

	public String getConfigDesc(){
		return configDesc;
	}

	public void setConfigDesc(String configDesc){
		this.configDesc = configDesc;
	}

	public String getConfigProj(){
		return configProj;
	}

	public void setConfigProj(String configProj){
		this.configProj = configProj;
	}

	public String getCreateDate(){
		return createDate;
	}

	public void setCreateDate(String createDate){
		this.createDate = createDate;
	}
}
