package com.cjit.vms.system.model;

public class UserColComments {
	private Integer id;
	private String tableName;
	private String column_Name;
	private String comments;
	
	public UserColComments(){
		
	}
	
	public UserColComments(String column_Name, String comments) {
		this.column_Name = column_Name;
		this.comments = comments;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getColumn_Name() {
		return column_Name;
	}

	public void setColumn_Name(String column_Name) {
		this.column_Name = column_Name;
	}
	
}
