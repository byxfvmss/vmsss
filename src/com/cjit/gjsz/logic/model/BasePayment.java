package com.cjit.gjsz.logic.model;

public class BasePayment extends BaseEntity{

	private String lcbgno;
	private String issdate;
	private Long tenor;

	public String getLcbgno(){
		return lcbgno;
	}

	public void setLcbgno(String lcbgno){
		this.lcbgno = lcbgno;
	}

	public String getIssdate(){
		return issdate;
	}

	public void setIssdate(String issdate){
		this.issdate = issdate;
	}

	public Long getTenor(){
		return tenor;
	}

	public void setTenor(Long tenor){
		this.tenor = tenor;
	}
}
