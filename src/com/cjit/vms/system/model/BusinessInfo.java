package com.cjit.vms.system.model;

import java.math.BigDecimal;

/**
 * 交易种类
 * 
 * @author Larry
 */
public class BusinessInfo {
	
	private String businessId;//主键ID
	private String businessCode;//交易码
	private String businessCName;//交易中文名称
	private BigDecimal taxRate;//税率
	private String hasTax;//是否含税，0-否，1-是
	private String autoPrint;// 是否自动开票，0-否，1-是
	private String businessParentCode;// 上级交易码
	private Integer businessLayer;// 交易级别
	private Integer orderNum;// 排序值
	private String isHead;//是为true 否为false
	private String businessParentName;// 上级交易名称
	private String businessPath;// 交易路径信息
	private String isUse;//1启用2.失效
	private String sql;//配置条件
	private String discountRate;//折扣率
	private String serialNo;//编号
	private String math;//公式
	private String taxRateId;
	private String businessNote;//描述
	
	public String getBusinessNote() {
		return businessNote;
	}

	public void setBusinessNote(String businessNote) {
		this.businessNote = businessNote;
	}

	/**
	 * <p>
	 * 构造函数名称: |描述:无参构造函数
	 * </p>
	 */
	public BusinessInfo() {
	};

	/**
	 * <p>
	 * 构造函数名称: |描述:有参构造函数，设置ID
	 * </p>
	 * 
	 * @param instId
	 */
	public BusinessInfo(String id, String code) {
		this.businessId = id;
		this.businessCode = code;
	}
	
	public String getTaxRateId() {
		return taxRateId;
	}

	public void setTaxRateId(String taxRateId) {
		this.taxRateId = taxRateId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getMath() {
		return math;
	}

	public void setMath(String math) {
		this.math = math;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getBusinessPath() {
		return businessPath;
	}

	public void setBusinessPath(String businessPath) {
		this.businessPath = businessPath;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getBusinessCName() {
		return businessCName;
	}

	public void setBusinessCName(String businessCName) {
		this.businessCName = businessCName;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getHasTax() {
		return hasTax;
	}

	public void setHasTax(String hasTax) {
		this.hasTax = hasTax;
	}

	public String getAutoPrint() {
		return autoPrint;
	}

	public void setAutoPrint(String autoPrint) {
		this.autoPrint = autoPrint;
	}

	public String getBusinessParentName() {
		return businessParentName;
	}

	public void setBusinessParentName(String businessParentName) {
		this.businessParentName = businessParentName;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public String getIsHead() {
		return isHead;
	}

	public void setIsHead(String isHead) {
		this.isHead = isHead;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getBusinessLayer() {
		return businessLayer;
	}

	public void setBusinessLayer(Integer businessLayer) {
		this.businessLayer = businessLayer;
	}

	public String getBusinessParentCode() {
		return businessParentCode;
	}

	public void setBusinessParentCode(String businessParentCode) {
		this.businessParentCode = businessParentCode;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

//	// 数据属性
//	private String businessId;// ID
//	private String businessCode;// 交易码
//	private String businessCName;// 交易名称
//	private BigDecimal taxRate;// 税率
//	private String hasTax;// 是否含税 1-含 0-不含
//	private String autoPrint;// 是否自动开票，0-否，1-是
//
//	public Business() {
//
//	}
//
//	public Business(String id, String code) {
//		this.businessId = id;
//		this.businessCode = code;
//	}
//
//	public String getBusinessId() {
//		return businessId;
//	}
//
//	public void setBusinessId(String businessId) {
//		this.businessId = businessId;
//	}
//
//	public String getBusinessCode() {
//		return businessCode;
//	}
//
//	public void setBusinessCode(String businessCode) {
//		this.businessCode = businessCode;
//	}
//
//	public String getBusinessCName() {
//		return businessCName;
//	}
//
//	public void setBusinessCName(String businessCName) {
//		this.businessCName = businessCName;
//	}
//
//	public BigDecimal getTaxRate() {
//		return taxRate;
//	}
//
//	public void setTaxRate(BigDecimal taxRate) {
//		this.taxRate = taxRate;
//	}
//
//	public String getHasTax() {
//		return hasTax;
//	}
//
//	public void setHasTax(String hasTax) {
//		this.hasTax = hasTax;
//	}
//
//	public String getAutoPrint() {
//		return autoPrint;
//	}
//
//	public void setAutoPrint(String autoPrint) {
//		this.autoPrint = autoPrint;
//	}
}
