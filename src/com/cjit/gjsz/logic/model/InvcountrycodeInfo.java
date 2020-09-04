package com.cjit.gjsz.logic.model;

public class InvcountrycodeInfo extends Entity{

	private String invcountrycode;
	private String businessid;
	private String subid;

	public String getSubid(){
		return subid;
	}

	public void setSubid(String subid){
		this.subid = subid;
	}

	public String getInvcountrycode(){
		return invcountrycode;
	}

	public void setInvcountrycode(String invcountrycode){
		this.invcountrycode = invcountrycode;
	}

	public String getBusinessid(){
		return businessid;
	}

	public void setBusinessid(String businessid){
		this.businessid = businessid;
	}
}
