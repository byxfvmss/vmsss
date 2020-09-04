package com.cjit.vms.trans.model.createBill;

import java.util.List;

public class BillPreInfo extends BillInfo{
	protected List lstAuthInstId;
	protected String billBeginDate;
	protected String billEndDate;
	protected String type;
	
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
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

}
