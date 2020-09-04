package com.cjit.vms.taxdisk.single.model.busiDisk;

import java.util.ArrayList;
import java.util.List;

public class TaxDiskInfo {
	private String instId;//机构
	private String taxDiskNo;//税控盘号
	private String billMachineNo;//开票机号
	private String taxDiskVersion; //税控盘版本号
	private String taxDiskDate; // 税控盘时钟
	private String taxpayerNo;// 纳税人识别号
	private String taxpayerNam;//纳税人名称
	private String taxDiskPsw;//税控盘口令
	private String taxCertPsw;//证书口令
	private String taxBureauNum;//税务机关代码
	private String taxBureauNam;//税务机关名称
	private String diskBillType;//发票类型代码
	private String diskCustType;//企业类型
	private String retainInfo;//保留信息
	private String enableDt;//启用时间 
	private List lstAuthInstId = new ArrayList();
	private String user_id;//用户ID
	private List taxperLists = new ArrayList();
	
	public List getTaxperLists() {
		return taxperLists;
	}
	public void setTaxperLists(List taxperLists) {
		this.taxperLists = taxperLists;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getBillMachineNo() {
		return billMachineNo;
	}
	public void setBillMachineNo(String billMachineNo) {
		this.billMachineNo = billMachineNo;
	}
	public String getTaxDiskVersion() {
		return taxDiskVersion;
	}
	public void setTaxDiskVersion(String taxDiskVersion) {
		this.taxDiskVersion = taxDiskVersion;
	}
	public String getTaxDiskDate() {
		return taxDiskDate;
	}
	public void setTaxDiskDate(String taxDiskDate) {
		this.taxDiskDate = taxDiskDate;
	}
	public String getTaxpayerNo() {
		return taxpayerNo;
	}
	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}
	public String getTaxpayerNam() {
		return taxpayerNam;
	}
	public void setTaxpayerNam(String taxpayerNam) {
		this.taxpayerNam = taxpayerNam;
	}
	public String getTaxDiskPsw() {
		return taxDiskPsw;
	}
	public void setTaxDiskPsw(String taxDiskPsw) {
		this.taxDiskPsw = taxDiskPsw;
	}
	public String getTaxCertPsw() {
		return taxCertPsw;
	}
	public void setTaxCertPsw(String taxCertPsw) {
		this.taxCertPsw = taxCertPsw;
	}
	public String getTaxBureauNum() {
		return taxBureauNum;
	}
	public void setTaxBureauNum(String taxBureauNum) {
		this.taxBureauNum = taxBureauNum;
	}
	public String getTaxBureauNam() {
		return taxBureauNam;
	}
	public void setTaxBureauNam(String taxBureauNam) {
		this.taxBureauNam = taxBureauNam;
	}
	public String getDiskBillType() {
		return diskBillType;
	}
	public void setDiskBillType(String diskBillType) {
		this.diskBillType = diskBillType;
	}
	public String getDiskCustType() {
		return diskCustType;
	}
	public void setDiskCustType(String diskCustType) {
		this.diskCustType = diskCustType;
	}
	public String getRetainInfo() {
		return retainInfo;
	}
	public void setRetainInfo(String retainInfo) {
		this.retainInfo = retainInfo;
	}
	public String getEnableDt() {
		return enableDt;
	}
	public void setEnableDt(String enableDt) {
		this.enableDt = enableDt;
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
}
