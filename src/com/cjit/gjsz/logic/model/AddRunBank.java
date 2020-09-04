package com.cjit.gjsz.logic.model;

public class AddRunBank{

	private String tableId;
	private String columnId;
	private String businessid;
	private Long value;

	public String getTableId(){
		return tableId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String getColumnId(){
		return columnId;
	}

	public void setColumnId(String columnId){
		this.columnId = columnId;
	}

	public String getBusinessid(){
		return businessid;
	}

	public void setBusinessid(String businessid){
		this.businessid = businessid;
	}

	public Long getValue(){
		return value;
	}

	public void setValue(Long value){
		this.value = value;
	}
}
