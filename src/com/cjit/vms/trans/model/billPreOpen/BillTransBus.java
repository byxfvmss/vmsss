package com.cjit.vms.trans.model.billPreOpen;

public class BillTransBus {

	private String billId;
	private String transBusId;
	private String updateUser;
	private String updateDatetime ;
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getTransBusId() {
		return transBusId;
	}
	public void setTransBusId(String transBusId) {
		this.transBusId = transBusId;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(String updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	
}
