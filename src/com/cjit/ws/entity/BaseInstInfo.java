package com.cjit.ws.entity;

import java.util.Date;

public class BaseInstInfo {
	private final String TRUE = "TRUE";
	private final String CH_YES = "是";
	private final String CH_NO = "否";
	private String instId;// 机构编号 INST_ID varchar2(20)
	private String instName;// 机构名称 INST_NAME varchar2(30)
	private String instSmpName;// 机构简称 INST_SMP_NAME varchar2(20)
	private String parentInstId;// 上级机构 PARENT_INST_ID varchar2(20)
	private Integer instLayer;// 机构级别 INST_LAYER INT
	private String address;// 机构地址 ADDRESS varchar2(100)
	private String zip;// 机构邮编 ZIP varchar2(6)
	private String tel;// 机构电话 TEL varchar2(27)
	private String fax;// 机构传真 FAX varchar2(20)
	private String isBussiness = "true";// 是否业务机构 IS_BUSSINESS VARCHAR2(5)
	private Integer orderNum;// 排序值 ORDER_NUM INT
	private String description;// 描述 DESCRIPTION VARCHAR2(100)
	private Date startDate = new Date();// 启用日期 START_DATE DATE
	private Date endDate;// 终止日期 END_DATE DATE
	private Date createTime;// 创建时间 CREATE_TIME DATE
	private String enabled = "true";// 启用标识 ENABLED VARCHAR2(5)
	private String startDateStr;// 开始日期字符串
	private String endDateStr;// 结束日期字符串
	private String createTimeStr;// 创建时间字符串
	private String email;// 机构邮箱
	private String instPath;//
	private Integer instLevel;// 机构级别
	private String parentInstName;
	private String isHead;// 是否是总行
	private String account;// 纳税人账号
	private String taxperNumber;// 税务登记号
	private String taxpername;// 纳税人名称
	private String taxaddress;// 纳税人地址
	private String taxtel;// 纳税人电话
	private String taxbank;// 纳税人开户行
	private String taxPayerType; //纳税人类别
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public String getInstSmpName() {
		return instSmpName;
	}
	public void setInstSmpName(String instSmpName) {
		this.instSmpName = instSmpName;
	}
	public String getParentInstId() {
		return parentInstId;
	}
	public void setParentInstId(String parentInstId) {
		this.parentInstId = parentInstId;
	}
	public Integer getInstLayer() {
		return instLayer;
	}
	public void setInstLayer(Integer instLayer) {
		this.instLayer = instLayer;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getIsBussiness() {
		return isBussiness;
	}
	public void setIsBussiness(String isBussiness) {
		this.isBussiness = isBussiness;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInstPath() {
		return instPath;
	}
	public void setInstPath(String instPath) {
		this.instPath = instPath;
	}
	public Integer getInstLevel() {
		return instLevel;
	}
	public void setInstLevel(Integer instLevel) {
		this.instLevel = instLevel;
	}
	public String getParentInstName() {
		return parentInstName;
	}
	public void setParentInstName(String parentInstName) {
		this.parentInstName = parentInstName;
	}
	public String getIsHead() {
		return isHead;
	}
	public void setIsHead(String isHead) {
		this.isHead = isHead;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getTaxpername() {
		return taxpername;
	}
	public void setTaxpername(String taxpername) {
		this.taxpername = taxpername;
	}
	public String getTaxaddress() {
		return taxaddress;
	}
	public void setTaxaddress(String taxaddress) {
		this.taxaddress = taxaddress;
	}
	public String getTaxtel() {
		return taxtel;
	}
	public void setTaxtel(String taxtel) {
		this.taxtel = taxtel;
	}
	public String getTaxbank() {
		return taxbank;
	}
	public void setTaxbank(String taxbank) {
		this.taxbank = taxbank;
	}
	public String getTaxPayerType() {
		return taxPayerType;
	}
	public void setTaxPayerType(String taxPayerType) {
		this.taxPayerType = taxPayerType;
	}
	public String getTRUE() {
		return TRUE;
	}
	public String getCH_YES() {
		return CH_YES;
	}
	public String getCH_NO() {
		return CH_NO;
	}
	public String getTaxperNumber() {
		return taxperNumber;
	}
	public void setTaxperNumber(String taxperNumber) {
		this.taxperNumber = taxperNumber;
	}
	
}
