package com.cjit.gjsz.logic.model;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:42:03 PM
 * @描述: [Self_Sub_GUARANTOR]被担保人信息
 */
public class Self_Sub_GUARANTOR extends SelfSubEntity{

	private String guedcode; // 被担保人代码
	private String guedname; // 被担保人中文名称
	private String guednamen; // 被担保人英文名称
	private String guedtype; // 被担保人类型
	private String guedcouncode; // 被担保人国别/地区代码

	public String getGuedcode(){
		return guedcode;
	}

	public void setGuedcode(String guedcode){
		this.guedcode = guedcode;
	}

	public String getGuedname(){
		return guedname;
	}

	public void setGuedname(String guedname){
		this.guedname = guedname;
	}

	public String getGuednamen(){
		return guednamen;
	}

	public void setGuednamen(String guednamen){
		this.guednamen = guednamen;
	}

	public String getGuedtype(){
		return guedtype;
	}

	public void setGuedtype(String guedtype){
		this.guedtype = guedtype;
	}

	public String getGuedcouncode(){
		return guedcouncode;
	}

	public void setGuedcouncode(String guedcouncode){
		this.guedcouncode = guedcouncode;
	}
}
