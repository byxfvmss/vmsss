package com.cjit.ws.entity;

public class VmsTransType {
	private String insCod;
	private String insNam;
	
	public VmsTransType(){
	}
	
	public VmsTransType(String insCod,String insNam){
		this.insCod=insCod;
		this.insNam=insNam;
	}
	
	public String getInsCod() {
		return insCod;
	}
	public void setInsCod(String insCod) {
		this.insCod = insCod;
	}
	public String getInsNam() {
		return insNam;
	}
	public void setInsNam(String insNam) {
		this.insNam = insNam;
	}
	
}
