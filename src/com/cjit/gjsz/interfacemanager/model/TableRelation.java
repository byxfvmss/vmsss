/**
 * TableRelation
 */
package com.cjit.gjsz.interfacemanager.model;

/**
 * @author huboA
 * @table t_rpt_table_rela
 */
public class TableRelation{

	private String tableId;
	private String subTableId;
	private String tableColumn;
	private int subRelation;

	public String getTableColumn(){
		return tableColumn;
	}

	public int getSubRelation(){
		return subRelation;
	}

	public void setTableColumn(String tableColumn){
		this.tableColumn = tableColumn;
	}

	public void setSubRelation(int subRelation){
		this.subRelation = subRelation;
	}

	public String getTableId(){
		return tableId;
	}

	public String getSubTableId(){
		return subTableId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public void setSubTableId(String subTableId){
		this.subTableId = subTableId;
	}
}
