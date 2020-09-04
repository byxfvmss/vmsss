package com.cjit.gjsz.logic.model;

public class FalSubEntity {
	private int datastatus;// 数据状态
	private String businessid;// 主表业务ID
	private String subid;// 本表业务ID

	public int getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(int datastatus) {
		this.datastatus = datastatus;
	}

	public String getBusinessid() {
		return businessid;
	}

	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}

	public String getSubid() {
		return subid;
	}

	public void setSubid(String subid) {
		this.subid = subid;
	}
}
