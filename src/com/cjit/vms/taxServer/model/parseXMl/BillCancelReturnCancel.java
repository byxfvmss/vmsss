package com.cjit.vms.taxServer.model.parseXMl;

public class BillCancelReturnCancel extends BaseReturnXml{
	/**
	 * 作废日期
	 */
	private String billCode;
	private String billNo;
	private String billCancelDate;

	public String getBillCancelDate() {
		return billCancelDate;
	}

	public void setBillCancelDate(String billCancelDate) {
		this.billCancelDate = billCancelDate;
	}

	public String getBillCode() {
		return billCode;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	

}
