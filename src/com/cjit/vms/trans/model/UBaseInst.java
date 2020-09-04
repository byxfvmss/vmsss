package com.cjit.vms.trans.model;

public class UBaseInst {
	private String instId;       		//机构编号
	private String instName;     		//机构名称
	private String instSmpName;  		//机构简称
	private String parentInstId; 		//上级机构
	private int instLayer;           	//机构级别
	private String  address;     		//机构地址
	private String  zip;      			//机构邮编
	private String  tel;      			//机构电话
	private String  fax;      			//机构传真
	private String  isBussiness; 		//是否业务机构
	private int  orderNum;       	    //排序值
	private String  description; 		//描述
	private String  startDate;          //启用日期
	private String  endDate;          	//终止日期
	private String  createTime;         //创建时间
	private String  enabled;        	//启用标识
	private String  instRegion;  		//	
	private String  email;       	    //
	private String  instPath;    		//机构编号路径
	private int  instLevel;          	//机构在数据库中的物理级别
	private String  isHead;            	//是否是总行 true:是 false:否
	private String  taxperName;  		//纳税人名称
	private String  taxperNumber;		//纳税人识别号，即税务登记号
	private String  account;     		//纳税人账号
	private String  taxAddress;  		//纳税人地址
	private String  taxTel;      		//纳税人电话
	private String  taxBank;      		//纳税人开户行
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
	public int getInstLayer() {
		return instLayer;
	}
	public void setInstLayer(int instLayer) {
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
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getInstRegion() {
		return instRegion;
	}
	public void setInstRegion(String instRegion) {
		this.instRegion = instRegion;
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
	public int getInstLevel() {
		return instLevel;
	}
	public void setInstLevel(int instLevel) {
		this.instLevel = instLevel;
	}
	public String getIsHead() {
		return isHead;
	}
	public void setIsHead(String isHead) {
		this.isHead = isHead;
	}
	public String getTaxperName() {
		return taxperName;
	}
	public void setTaxperName(String taxperName) {
		this.taxperName = taxperName;
	}
	public String getTaxperNumber() {
		return taxperNumber;
	}
	public void setTaxperNumber(String taxperNumber) {
		this.taxperNumber = taxperNumber;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getTaxAddress() {
		return taxAddress;
	}
	public void setTaxAddress(String taxAddress) {
		this.taxAddress = taxAddress;
	}
	public String getTaxTel() {
		return taxTel;
	}
	public void setTaxTel(String taxTel) {
		this.taxTel = taxTel;
	}
	public String getTaxBank() {
		return taxBank;
	}
	public void setTaxBank(String taxBank) {
		this.taxBank = taxBank;
	}

	
}
