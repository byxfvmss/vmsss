package com.cjit.vms.input.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 进项税交易信息类
 * 
 * @author Larry/
 */
public class InputTrans {
	private String transBeginDate;// 交易开始日期
	private String transEndDate;// 交易结束日期

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
	
	private String vendorId;//供应商ID
	private String vendorCname;//供应商name
	private String vendorTaxno; //供应商识别号
	private String billId;
	
	private String transDate;
	private String userId;// 当前用户ID
	private String batchNo;//批次

	private List lstAuthInstId;
	
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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


	public String getVendorCName() {
		return vendorCname;
	}

	public void setVendorCname(String vendorCname) {
		this.vendorCname = vendorCname;
	}
	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getVendorCname() {
		return vendorCname;
	}

	public String getVendorTaxno() {
		return vendorTaxno;
	}

	public void setVendorTaxno(String vendorTaxno) {
		this.vendorTaxno = vendorTaxno;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	

}
