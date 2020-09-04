package com.cjit.gjsz.logic.model;

public class BaseSettlement extends BaseEntity{

	private String oppbank;
	private String fcyccy;

	public String getOppbank(){
		return oppbank;
	}

	public void setOppbank(String oppbank){
		this.oppbank = oppbank;
	}

	public String getFcyccy(){
		return fcyccy;
	}

	public void setFcyccy(String fcyccy){
		this.fcyccy = fcyccy;
	}
}