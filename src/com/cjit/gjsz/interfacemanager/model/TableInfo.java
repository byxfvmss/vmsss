/**
 * TableInfo
 */
package com.cjit.gjsz.interfacemanager.model;

/**
 * @author huboA
 * @table t_rpt_table_info
 */
public class TableInfo{

	private String id;
	private String type;
	private String tableId;
	private String tableName;
	private int show;
	private int orderBy;
	private String personalCode;
	private String publicCode;
	private String fileType;
	private String importType;
	private String saveTableId;

	public String getImportType(){
		return importType;
	}

	public void setImportType(String importType){
		this.importType = importType;
	}

	public String getFileType(){
		return fileType;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getPersonalCode(){
		return personalCode;
	}

	public String getPublicCode(){
		return publicCode;
	}

	public void setPersonalCode(String personalCode){
		this.personalCode = personalCode;
	}

	public void setPublicCode(String publicCode){
		this.publicCode = publicCode;
	}

	public int getOrderBy(){
		return orderBy;
	}

	public void setOrderBy(int orderBy){
		this.orderBy = orderBy;
	}

	public int getShow(){
		return show;
	}

	public void setShow(int show){
		this.show = show;
	}

	public String getId(){
		return id;
	}

	public String getType(){
		return type;
	}

	public String getTableId(){
		return tableId;
	}

	public String getTableName(){
		return tableName;
	}

	public void setId(String id){
		this.id = id;
	}

	public void setType(String type){
		this.type = type;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public void setTableName(String tableName){
		this.tableName = tableName;
	}

	public String getSaveTableId() {
		return saveTableId;
	}

	public void setSaveTableId(String saveTableId) {
		this.saveTableId = saveTableId;
	}
}
