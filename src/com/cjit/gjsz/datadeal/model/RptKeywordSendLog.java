package com.cjit.gjsz.datadeal.model;

public class RptKeywordSendLog{

	private String tableId;
	private String businessId;
	private String columnId;
	private String columnValue;

	public String getTableId(){
		return tableId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String getBusinessId(){
		return businessId;
	}

	public void setBusinessId(String businessId){
		this.businessId = businessId;
	}

	public String getColumnId(){
		return columnId;
	}

	public void setColumnId(String columnId){
		this.columnId = columnId;
	}

	public String getColumnValue(){
		return columnValue;
	}

	public void setColumnValue(String columnValue){
		this.columnValue = columnValue;
	}
}
