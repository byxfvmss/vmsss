package com.cjit.vms.taxdisk.single.model.busiDisk;

/**
 * 税控钥匙信息类
 * @author john
 *
 */
public class VmsTaxKeyInfo {

	private String taxKeyNo; // 税控钥匙编号(主键)
	private String taxNo; // 纳税人识别号
	private String bilTerminalFlag; // 开票终端标示
	private String ipAddress; // ip地址
	private String servletPort; // 服务器端口号
	
	public String getTaxKeyNo() {
		return taxKeyNo;
	}
	public void setTaxKeyNo(String taxKeyNo) {
		this.taxKeyNo = taxKeyNo;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getBilTerminalFlag() {
		return bilTerminalFlag;
	}
	public void setBilTerminalFlag(String bilTerminalFlag) {
		this.bilTerminalFlag = bilTerminalFlag;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getServletPort() {
		return servletPort;
	}
	public void setServletPort(String servletPort) {
		this.servletPort = servletPort;
	}
}
