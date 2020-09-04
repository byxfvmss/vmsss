package com.cjit.vms.trans.service.taxDisk.util;

public class Message {
	/**
	 * 机构纳税人识别号与税控盘纳税人识别号不一致
	 */
	public static final String tax_no_and_inst_tax_no_not="机构纳税人识别号与税控盘纳税人识别号不一致";
	/**
	 * "机构号纳税人识别号为空 不能税控盘操作";
	 */
	public static final String blank_inst_tax_no="机构号纳税人识别号为空 不能税控盘操作";
	/**
	 * "系统异常"
	 */
	public static final String system_exception="系统异常";
	/**
	 *  成功 0  
	 */
	public static final String success_ch="0";
	/**
	 *  失败 1
	 */
	public static final String  failure_ch="1";
	
	/**
	 * 返回代码
	 */
	private String returnCode;
	/**
	 * 返回信息
	 */
	private String returnMsg;
	/**
	 * 税控盘号
	 */
	private String taxDiskNo;
	/**
	 * 税控盘密码
	 */
	private String taxDiskpwd;
	/**
	 * 证书密码
	 */
	private String taxcertPwd;
	/**
	 * xml 字符串
	 */
	private String stringXml;
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getTaxDiskpwd() {
		return taxDiskpwd;
	}
	public void setTaxDiskpwd(String taxDiskpwd) {
		this.taxDiskpwd = taxDiskpwd;
	}
	public String getTaxcertPwd() {
		return taxcertPwd;
	}
	public void setTaxcertPwd(String taxcertPwd) {
		this.taxcertPwd = taxcertPwd;
	}
	public String getStringXml() {
		return stringXml;
	}
	public void setStringXml(String stringXml) {
		this.stringXml = stringXml;
	}
	
}
