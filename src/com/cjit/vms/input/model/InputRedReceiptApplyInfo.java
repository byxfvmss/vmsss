package com.cjit.vms.input.model;

import java.math.BigDecimal;

/**
 * @author XHY
 * 
 */
public class InputRedReceiptApplyInfo {

	private String billId;
	private String transId;
	private String transDate;
	private String billDate;// 开票日期
	private String billCode;// 发票代码
	private String billNo;// 发票号码
	private String oriBillCode;// 原发票代码
	private String oriBillNo;// 原发票号码
	private String vendorName;// 客户纳税人名称
	private String vendorTaxno;// 客户纳税人识别号
	private String goodsName;// 商品名称
	private String specandmodel;// 规格型号
	private String goodsUnit;// 单位
	private BigDecimal goodsNo;// 商品数量
	private BigDecimal goodsPrice;
	private BigDecimal taxRate;
	private BigDecimal amtSum;// 合计金额
	private BigDecimal taxAmtSum;// 合计税额
	private BigDecimal sumAmt;// 价税合计
	private BigDecimal discountRate;// 折扣率
	private String fapiaoType;
	private String datastatus;
	private String name;
	private String taxno;
	private String customerAccount;
	private String transType;
	private String bankCode;
	private String customerId;
	private String transFlag;
	private String isReverse;
	private String operateStatus;
	private String instcode;
	private String instName;
	private BigDecimal vatOutAmt;
	private String identifyDate;
	
	private String billBeginDate;// 开票日期起始
	private String billEndDate;// 开票日期终止
	private String transBeginDate;
	private String transEndDate;
	// 24：发票类型 来源于VMS_BILL_ITEM_INFO
	// 25：状态 来源于VMS_BILL_ITEM_INFO
	
	
	/**
	 * @return the transDate
	 */
	public String getTransDate() {
		return transDate;
	}

	/**
	 * @return the billId
	 */
	public String getBillId() {
		return billId;
	}

	/**
	 * @param billId the billId to set
	 */
	public void setBillId(String billId) {
		this.billId = billId;
	}

	/**
	 * @return the transId
	 */
	public String getTransId() {
		return transId;
	}

	/**
	 * @param transId the transId to set
	 */
	public void setTransId(String transId) {
		this.transId = transId;
	}

	/**
	 * @param transDate
	 *            the transDate to set
	 */
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	/**
	 * @return the billDate
	 */
	public String getBillDate() {
		return billDate;
	}

	/**
	 * @param billDate
	 *            the billDate to set
	 */
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	
	/**
	 * @return the billCode
	 */
	public String getBillCode() {
		return billCode;
	}

	/**
	 * @param billCode the billCode to set
	 */
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	/**
	 * @return the billNo
	 */
	public String getBillNo() {
		return billNo;
	}

	/**
	 * @param billNo the billNo to set
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorTaxno() {
		return vendorTaxno;
	}

	public void setVendorTaxno(String vendorTaxno) {
		this.vendorTaxno = vendorTaxno;
	}

	/**
	 * @return the goodsName
	 */
	public String getGoodsName() {
		return goodsName;
	}

	/**
	 * @param goodsName
	 *            the goodsName to set
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/**
	 * @return the specandmodel
	 */
	public String getSpecandmodel() {
		return specandmodel;
	}

	/**
	 * @param specandmodel
	 *            the specandmodel to set
	 */
	public void setSpecandmodel(String specandmodel) {
		this.specandmodel = specandmodel;
	}

	/**
	 * @return the goodsUnit
	 */
	public String getGoodsUnit() {
		return goodsUnit;
	}

	/**
	 * @param goodsUnit
	 *            the goodsUnit to set
	 */
	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	/**
	 * @return the goodsNo
	 */
	public BigDecimal getGoodsNo() {
		return goodsNo;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	/**
	 * @param goodsNo
	 *            the goodsNo to set
	 */
	public void setGoodsNo(BigDecimal goodsNo) {
		this.goodsNo = goodsNo;
	}

	/**
	 * @return the amtSum
	 */
	public BigDecimal getAmtSum() {
		return amtSum;
	}

	/**
	 * @param amtSum
	 *            the amtSum to set
	 */
	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}

	/**
	 * @return the taxAmtSum
	 */
	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}

	/**
	 * @param taxAmtSum
	 *            the taxAmtSum to set
	 */
	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}

	/**
	 * @return the sumAmt
	 */
	public BigDecimal getSumAmt() {
		return sumAmt;
	}

	/**
	 * @param sumAmt
	 *            the sumAmt to set
	 */
	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}

	/**
	 * @return the discountRate
	 */
	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discountRate
	 *            the discountRate to set
	 */
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public String getBillBeginDate() {
		return billBeginDate;
	}

	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getTransBeginDate() {
		return transBeginDate;
	}

	public void setTransBeginDate(String transBeginDate) {
		this.transBeginDate = transBeginDate;
	}

	public String getTransEndDate() {
		return transEndDate;
	}

	public void setTransEndDate(String transEndDate) {
		this.transEndDate = transEndDate;
	}
	
	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getOriBillCode() {
		return oriBillCode;
	}

	public void setOriBillCode(String oriBillCode) {
		this.oriBillCode = oriBillCode;
	}

	public String getOriBillNo() {
		return oriBillNo;
	}

	public void setOriBillNo(String oriBillNo) {
		this.oriBillNo = oriBillNo;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxno() {
		return taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTransFlag() {
		return transFlag;
	}

	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}

	public String getIsReverse() {
		return isReverse;
	}

	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}

	public String getOperateStatus() {
		return operateStatus;
	}

	public void setOperateStatus(String operateStatus) {
		this.operateStatus = operateStatus;
	}

	public String getInstcode() {
		return instcode;
	}

	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}

	public BigDecimal getVatOutAmt() {
		return vatOutAmt;
	}

	public void setVatOutAmt(BigDecimal vatOutAmt) {
		this.vatOutAmt = vatOutAmt;
	}

	public String getIdentifyDate() {
		return identifyDate;
	}

	public void setIdentifyDate(String identifyDate) {
		this.identifyDate = identifyDate;
	}
	
	
}