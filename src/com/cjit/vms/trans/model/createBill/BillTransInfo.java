package com.cjit.vms.trans.model.createBill;

import java.math.BigDecimal;

public class BillTransInfo {
	private String transId; // 交易ID
	private String billId; // 票据ID
	private String billItemId; // 交易明细ID
	// 1-单笔：取销项交易信息表中“金额_人民币”字段；2-批量单笔：取销项交易信息表中“金额_人民币”字段；3-合并：取销项交易信息表中“金额_人民币”字段；4-拆分：取用户输入的拆分后的“金额”字段
	private BigDecimal amtCny; // 金额_人民币
	// 1-单笔：取销项交易信息表中“金额_人民币”字段；2-批量单笔：取销项交易信息表中“金额_人民币”字段；3-合并：取销项交易信息表中“金额_人民币”字段；4-拆分：取用户输入的拆分后的“金额”字段
	private BigDecimal taxAmtCny; // 税额_人民币
	// 1-单笔：取销项交易信息表中“金额_人民币”字段；2-批量单笔：取销项交易信息表中“金额_人民币”字段；3-合并：取销项交易信息表中“金额_人民币”字段；4-拆分：取用户输入的拆分后的“金额”字段
	private BigDecimal incomeCny; // 收入_人民币

	private BigDecimal balance; //

	public BillTransInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public BillTransInfo(String billId, String billItemId, TransInfo transInfo) {
		this(transInfo);
		this.billId = billId;
		this.billItemId = billItemId;
	}
	
	public BillTransInfo ( TransInfo transInfo) {
		this.setTransId(transInfo.getTransId());
		this.amtCny = transInfo.getBalance();
		this.taxAmtCny = transInfo.getTaxCnyBalance();
		// BigDecimal amtCnyDec = this.amtCny;
		this.incomeCny = transInfo.getBalanceIncomeCny();
		this.balance = transInfo.getAmtCny().subtract(transInfo.getBalance());
	}

	public String getTransId() {
		return transId;
	}

	public String getBillId() {
		return billId;
	}

	public String getBillItemId() {
		return billItemId;
	}

	public BigDecimal getAmtCny() {
		return amtCny;
	}

	public BigDecimal getTaxAmtCny() {
		return taxAmtCny;
	}

	public BigDecimal getIncomeCny() {
		return incomeCny;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public void setBillItemId(String billItemId) {
		this.billItemId = billItemId;
	}

	public void setAmtCny(BigDecimal amtCny) {
		this.amtCny = amtCny;
	}

	public void setTaxAmtCny(BigDecimal taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}

	public void setIncomeCny(BigDecimal incomeCny) {
		this.incomeCny = incomeCny;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
