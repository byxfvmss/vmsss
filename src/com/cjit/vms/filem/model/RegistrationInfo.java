package com.cjit.vms.filem.model;

/**
 * 注册码信息 id="ZCMDR"
 * 
 * @author Administrator
 * 
 */
public class RegistrationInfo {

	private String yylxdm;// 应用类型代码
	private String zcmxx;// 注册码信息

	private String returncode;// 返回代码
	private String returnmsg;// 返回信息

	public String getYylxdm() {
		return yylxdm;
	}

	public void setYylxdm(String yylxdm) {
		this.yylxdm = yylxdm;
	}

	public String getZcmxx() {
		return zcmxx;
	}

	public void setZcmxx(String zcmxx) {
		this.zcmxx = zcmxx;
	}

	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public String getReturnmsg() {
		return returnmsg;
	}

	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
	}
}
