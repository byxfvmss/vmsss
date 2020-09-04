package com.cjit.gjsz.access.model;

public class TableRelation{

	private String tableName;
	private String parentSql;

	public String getTableName(){
		return tableName;
	}

	public void setTableName(String tableName){
		this.tableName = tableName;
	}

	public String getParentSql(){
		return parentSql;
	}

	public void setParentSql(String parentSql){
		this.parentSql = parentSql;
	}
}
