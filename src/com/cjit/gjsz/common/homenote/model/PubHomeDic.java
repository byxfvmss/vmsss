package com.cjit.gjsz.common.homenote.model;

/**
 * @作者 李海波A
 * @日期 Sep 6, 2011
 */
public class PubHomeDic{

	private int dicId;
	private String dicType;
	private String dicTypeName;
	private String dicName;
	private String dicValue;
	private String dicListTarget;
	private String dicEditTarget;
	private String dicMenuId;

	public int getDicId(){
		return dicId;
	}

	public void setDicId(int dicId){
		this.dicId = dicId;
	}

	public String getDicType(){
		return dicType;
	}

	public void setDicType(String dicType){
		this.dicType = dicType;
	}

	public String getDicTypeName(){
		return dicTypeName;
	}

	public void setDicTypeName(String dicTypeName){
		this.dicTypeName = dicTypeName;
	}

	public String getDicName(){
		return dicName;
	}

	public void setDicName(String dicName){
		this.dicName = dicName;
	}

	public String getDicValue(){
		return dicValue;
	}

	public void setDicValue(String dicValue){
		this.dicValue = dicValue;
	}

	public String getDicListTarget(){
		return dicListTarget;
	}

	public void setDicListTarget(String dicListTarget){
		this.dicListTarget = dicListTarget;
	}

	public String getDicEditTarget(){
		return dicEditTarget;
	}

	public void setDicEditTarget(String dicEditTarget){
		this.dicEditTarget = dicEditTarget;
	}

	public String getDicMenuId(){
		return dicMenuId;
	}

	public void setDicMenuId(String dicMenuId){
		this.dicMenuId = dicMenuId;
	}
}
