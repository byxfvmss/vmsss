package com.cjit.vms.trans.model;

import java.math.BigDecimal;

/**
 * vms_trans_bill
 * 
 * @author Leixu
 */
public class TransBillInfo {

	// 数据库属性
	private String transId; // TRANS_ID
	private String billId; // BILL_ID
	private String billItemId; // BILL_ITEM_ID
	private BigDecimal amtCny; // AMT_CNY
	private BigDecimal taxAmtCny; // TAX_AMT_CNY
	private BigDecimal incomeCny; // INCOME_CNY
	private BigDecimal balance;

	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillItemId() {
		return billItemId;
	}
	public void setBillItemId(String billItemId) {
		this.billItemId = billItemId;
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
