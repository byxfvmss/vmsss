package com.cjit.vms.trans.model;

import java.util.ArrayList;
import java.util.List;

public class DiskRegInfo {
	private String instId;//机构
	private String taxDiskNo;//税控盘号
	private String taxpayerNo;// 纳税人识别号
	private String registeredInfo;//注册码信息
	private List lstAuthInstId = new ArrayList();
	
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getTaxpayerNo() {
		return taxpayerNo;
	}
	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getRegisteredInfo() {
		return registeredInfo;
	}
	public void setRegisteredInfo(String registeredInfo) {
		this.registeredInfo = registeredInfo;
	}
}
