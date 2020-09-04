package com.cjit.gjsz.logic.model;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:41:47 PM
 * @描述: [Self_Sub_FOGUARANTOR]境外担保人信息表
 */
public class Self_Sub_FOGUARANTOR extends SelfSubEntity{

	private String fogucode; // 境外担保人代码
	private String foguname; // 境外担保人中文名称
	private String fogunamen; // 境外担保人英文名称
	private String fogurecode; // 境外担保人注册地国家/地区代码
	private String guaranteetype; // 担保方式

	public String getFogucode(){
		return fogucode;
	}

	public void setFogucode(String fogucode){
		this.fogucode = fogucode;
	}

	public String getFoguname(){
		return foguname;
	}

	public void setFoguname(String foguname){
		this.foguname = foguname;
	}

	public String getFogunamen(){
		return fogunamen;
	}

	public void setFogunamen(String fogunamen){
		this.fogunamen = fogunamen;
	}

	public String getFogurecode(){
		return fogurecode;
	}

	public void setFogurecode(String fogurecode){
		this.fogurecode = fogurecode;
	}

	public String getGuaranteetype(){
		return guaranteetype;
	}

	public void setGuaranteetype(String guaranteetype){
		this.guaranteetype = guaranteetype;
	}
}
