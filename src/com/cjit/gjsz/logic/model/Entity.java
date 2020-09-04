package com.cjit.gjsz.logic.model;

import java.util.Date;

public abstract class Entity{

	private String businessid;
	private Date auditdate;

	public String getBusinessid(){
		return businessid;
	}

	public void setBusinessid(String businessid){
		this.businessid = businessid;
	}

	public Date getAuditdate(){
		return auditdate;
	}

	public void setAuditdate(Date auditdate){
		this.auditdate = auditdate;
	}
}
