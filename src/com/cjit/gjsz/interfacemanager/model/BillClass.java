/**
 * BillClass
 */
package com.cjit.gjsz.interfacemanager.model;

/**
 * @author Dylan
 * @table T_BILL_CLASS
 */
public class BillClass{

	private int id;
	private String billType;
	private String billName;
	private String tableCode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
}
