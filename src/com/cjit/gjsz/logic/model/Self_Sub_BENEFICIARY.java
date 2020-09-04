package com.cjit.gjsz.logic.model;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:40:34 PM
 * @描述: [Self_Sub_BENEFICIARY]受益人信息
 */
public class Self_Sub_BENEFICIARY extends SelfSubEntity{

	private String bencode; // 受益人代码
	private String bename; // 受益人中文名称
	private String benamen; // 受益人英文名称
	private String bentype; // 受益人类型
	private String bencountrycode; // 受益人国别/地区

	public String getBencode(){
		return bencode;
	}

	public void setBencode(String bencode){
		this.bencode = bencode;
	}

	public String getBename(){
		return bename;
	}

	public void setBename(String bename){
		this.bename = bename;
	}

	public String getBenamen(){
		return benamen;
	}

	public void setBenamen(String benamen){
		this.benamen = benamen;
	}

	public String getBentype(){
		return bentype;
	}

	public void setBentype(String bentype){
		this.bentype = bentype;
	}

	public String getBencountrycode(){
		return bencountrycode;
	}

	public void setBencountrycode(String bencountrycode){
		this.bencountrycode = bencountrycode;
	}
}
