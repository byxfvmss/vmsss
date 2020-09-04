/**
 * TableInfo
 */
package com.cjit.gjsz.interfacemanager.model;

/**
 * @author huboA
 * @table t_rpt_table_info
 */
public class ColumnInfo{

	private String columnId;
	private String tableId;
	private String columnName;
	private int order;
	private String position;
	private String dataType;
	private String dictionaryTypeId;
	private String constrainRule;
	private String constrainDescription;
	private int txtId;
	private String txtName;
	private String txtType;
	private int txtColumnId;
	private String show;
	private String tagType;
	private String modify;
	private String innerTable;
	private String typeName;
	private String importType;

	public String getTagType(){
		return tagType;
	}

	public void setTagType(String tagType){
		this.tagType = tagType;
	}

	public String getModify(){
		return modify;
	}

	public void setModify(String modify){
		this.modify = modify;
	}

	public String getInnerTable(){
		return innerTable;
	}

	public void setInnerTable(String innerTable){
		this.innerTable = innerTable;
	}

	public String getTxtType(){
		return txtType;
	}

	public void setTxtType(String txtType){
		this.txtType = txtType;
	}

	public String getTxtName(){
		return txtName;
	}

	public void setTxtName(String txtName){
		this.txtName = txtName;
	}

	public String getDataType(){
		return dataType;
	}

	public void setDataType(String dataType){
		this.dataType = dataType;
	}

	public String getColumnId(){
		return columnId;
	}

	public String getTableId(){
		return tableId;
	}

	public String getColumnName(){
		return columnName;
	}

	public int getOrder(){
		return order;
	}

	public String getPosition(){
		return position;
	}

	public String getDictionaryTypeId(){
		return dictionaryTypeId;
	}

	public String getConstrainRule(){
		return constrainRule;
	}

	public int getTxtId(){
		return txtId;
	}

	public int getTxtColumnId(){
		return txtColumnId;
	}

	public String getShow(){
		return show;
	}

	public void setColumnId(String columnId){
		this.columnId = columnId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public void setColumnName(String columnName){
		this.columnName = columnName;
	}

	public void setOrder(int order){
		this.order = order;
	}

	public void setPosition(String position){
		this.position = position;
	}

	public void setDictionaryTypeId(String dictionaryTypeId){
		this.dictionaryTypeId = dictionaryTypeId;
	}

	public void setConstrainRule(String constrainRule){
		this.constrainRule = constrainRule;
	}

	public void setTxtId(int txtId){
		this.txtId = txtId;
	}

	public void setTxtColumnId(int txtColumnId){
		this.txtColumnId = txtColumnId;
	}

	public void setShow(String show){
		this.show = show;
	}

	public String getConstrainDescription(){
		return constrainDescription;
	}

	public void setConstrainDescription(String constrainDescription){
		this.constrainDescription = constrainDescription;
	}

	public String getTypeName(){
		return typeName;
	}

	public void setTypeName(String typeName){
		this.typeName = typeName;
	}

	public String getImportType(){
		return importType;
	}

	public void setImportType(String importType){
		this.importType = importType;
	}
}
