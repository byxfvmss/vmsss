package com.cjit.vms.trans.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import cjit.crms.util.StringUtil;

import com.cjit.vms.trans.util.DataUtil;

public class ImpTransResultInfo {
	private int rowno; // 序号
	private String imptime; // 导入日期
	private String batchId; //
	private String terminal; // 终端号
	private String transId; // 交易ID
	private String customerId; // 客户ID
	private String transactionDate; // 交易时间
	private BigDecimal amtCny; // 金额_人民币
	private BigDecimal taxAmtCny; // 税额_人民币
	private BigDecimal incomeCny; // 收入_人民币
	private String vatRateCode; // 增值税种类
	private BigDecimal taxRate; // 税率
	private String productIeType; // 交易种类TYPE
	private String ieItem; // 交易种类ITEM
	private final DecimalFormat numberFormart = new DecimalFormat("#0.00");

	public int getRowno() {
		return rowno;
	}

	public void setRowno(int rowno) {
		this.rowno = rowno;
	}

	public String getImptime() {
		return imptime;
	}

	public void setImptime(String imptime) {
		this.imptime = imptime;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getAmtCny() {

		return numberFormart.format(amtCny==null?new BigDecimal(0):amtCny);
	}

	public void setAmtCny(BigDecimal amtCny) {
		this.amtCny = amtCny;
	}

	public String getTaxAmtCny() {
		return numberFormart.format(taxAmtCny==null?new BigDecimal(0):taxAmtCny);
	}

	public void setTaxAmtCny(BigDecimal taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}

	public String getIncomeCny() {
		return numberFormart.format(incomeCny==null?new BigDecimal(0):incomeCny);
	}

	public void setIncomeCny(BigDecimal incomeCny) {
		this.incomeCny = incomeCny;
	}

	public String getVatRateCode() {

		return DataUtil.getVatRateCodeCH(vatRateCode);
	}

	public void setVatRateCode(String vatRateCode) {
		this.vatRateCode = vatRateCode;
	}

	public String getTaxRate() {
		return numberFormart.format(taxRate==null?new BigDecimal(0):taxRate);
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getProductIeType() {
		return productIeType;
	}

	public void setProductIeType(String productIeType) {
		this.productIeType = productIeType;
	}

	public String getIeItem() {
		return ieItem;
	}

	public void setIeItem(String ieItem) {
		this.ieItem = ieItem;
	}

}
