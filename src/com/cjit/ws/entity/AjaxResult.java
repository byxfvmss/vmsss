package com.cjit.ws.entity;

import java.io.Serializable;

public class AjaxResult implements Serializable {
	
	private String msg;
	private boolean isNormal;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isNormal() {
		return isNormal;
	}
	public void setNormal(boolean isNormal) {
		this.isNormal = isNormal;
	}
	public AjaxResult(String msg, boolean isNormal) {
		super();
		this.msg = msg;
		this.isNormal = isNormal;
	}
	public AjaxResult(String msg) {
		super();
		this.msg = msg;
	}
	

}
