package com.cjit.vms.input.model;

import java.math.BigDecimal;

public class InputTransInfo {
	private String bankCode;//交易发生机构
	private String remark;//其他
	private String dealNo;//交易编号
	private String billCode;//发票代码
	private String billNo;//发票号码
	private BigDecimal amtCny;//金额_人民币
	private BigDecimal taxAmtCny;//税额_人民币
	private BigDecimal incomeCny;//支出_人民币
	private BigDecimal surtax1AmtCny;//附加税1（城市建设）金额
	private BigDecimal surtax2AmtCny;//附加税2（教育附加）金额
	private BigDecimal surtax3AmtCny;//附加税3（地方教育附加）金额
	private BigDecimal surtax4AmtCny;//附加税4（其他）金额
	private String vendorId;//供应商id
	private String transDate;//交易时间
	
	private String vendorName;
	private String bankName;
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDealNo() {
		return dealNo;
	}
	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public BigDecimal getAmtCny() {
		return amtCny;
	}
	public void setAmtCny(BigDecimal amtCny) {
		this.amtCny = amtCny;
	}
	public BigDecimal getTaxAmtCny() {
		return taxAmtCny;
	}
	public void setTaxAmtCny(BigDecimal taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}
	public BigDecimal getIncomeCny() {
		return incomeCny;
	}
	public void setIncomeCny(BigDecimal incomeCny) {
		this.incomeCny = incomeCny;
	}
	public BigDecimal getSurtax1AmtCny() {
		return surtax1AmtCny;
	}
	public void setSurtax1AmtCny(BigDecimal surtax1AmtCny) {
		this.surtax1AmtCny = surtax1AmtCny;
	}
	public BigDecimal getSurtax2AmtCny() {
		return surtax2AmtCny;
	}
	public void setSurtax2AmtCny(BigDecimal surtax2AmtCny) {
		this.surtax2AmtCny = surtax2AmtCny;
	}
	public BigDecimal getSurtax3AmtCny() {
		return surtax3AmtCny;
	}
	public void setSurtax3AmtCny(BigDecimal surtax3AmtCny) {
		this.surtax3AmtCny = surtax3AmtCny;
	}
	public BigDecimal getSurtax4AmtCny() {
		return surtax4AmtCny;
	}
	public void setSurtax4AmtCny(BigDecimal surtax4AmtCny) {
		this.surtax4AmtCny = surtax4AmtCny;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
}
