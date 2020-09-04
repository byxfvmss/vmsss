package com.cjit.vms.trans.model;

/**
 * 发票补打记录表
 * @author Tinna
 *
 */
public class BillPrintHistoryInfo {
	
	private String billID;
	private String printer;
	private String printTime;
	private String flag;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getBillID() {
		return billID;
	}
	public void setBillID(String billID) {
		this.billID = billID;
	}
	public String getPrinter() {
		return printer;
	}
	public void setPrinter(String printer) {
		this.printer = printer;
	}
	public String getPrintTime() {
		return printTime;
	}
	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}
	
	
	
	
	
	public BillPrintHistoryInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BillPrintHistoryInfo(String billID, String printer, String printTime,String flag) {
		super();
		this.billID = billID;
		this.printer = printer;
		this.printTime = printTime;
		this.flag=flag;
	}
	
	
}
