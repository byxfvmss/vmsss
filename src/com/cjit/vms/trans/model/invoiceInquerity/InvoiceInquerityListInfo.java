package com.cjit.vms.trans.model.invoiceInquerity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 发票申请单列表
 * @author moran
 *
 */
public class InvoiceInquerityListInfo {
	private String applicationFormId;//申请表id
	private String applyName; //申请人
	private String applyTime;	//申请时间
	private String productType; //应税服务名称
	private String customerNo;	//纳税人识别号
	private String customerCname;	//纳税人识别号	
	private String invoiceType; //发票类型
	private BigDecimal amount = new BigDecimal("0"); // 金额
	private BigDecimal amountTax = new BigDecimal("0"); // 税额
	private BigDecimal totalPriceandTax = new BigDecimal("0"); // 价税合计
	private BigDecimal taxRate= new BigDecimal("0");	//税率
	private String formStatus;	//申请表单状态0是校验失败1是校验成功
	private String monthly; 	//月度
	private String taxpayerNo;	//纳税人识别号
	private String createTime;	//创建时间
	private String createUserId;	//操作人ID	

	
	public String getCustomerCname() {
		return customerCname;
	}
	public void setCustomerCname(String customerCname) {
		this.customerCname = customerCname;
	}
	public String getApplicationFormId() {
		return applicationFormId;
	}
	public void setApplicationFormId(String applicationFormId) {
		this.applicationFormId = applicationFormId;
	}

	
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAmountTax() {
		return amountTax;
	}
	public void setAmountTax(BigDecimal amountTax) {
		this.amountTax = amountTax;
	}
	public BigDecimal getTotalPriceandTax() {
		return totalPriceandTax;
	}
	public void setTotalPriceandTax(BigDecimal totalPriceandTax) {
		this.totalPriceandTax = totalPriceandTax;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public String getFormStatus() {
		return formStatus;
	}
	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}
	public String getMonthly() {
		return monthly;
	}
	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}
	public String getTaxpayerNo() {
		return taxpayerNo;
	}
	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public InvoiceInquerityListInfo() {
		super();
	}
	
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

}
