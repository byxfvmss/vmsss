/**
 * TableInfo
 */
package com.cjit.gjsz.interfacemanager.model;

import org.apache.commons.collections.map.LinkedMap;

/**
 * @author huboA
 * @table t_rpt_table_info
 */
public class PageManager{

	private String typeId;
	private String type;
	private String importType;
	private String tableId;
	private String[] txtIds;
	private String[] txtColumnIds;
	private String[] dictionaryTypeIds;
	private String[] columnIds;
	private String[] modifys;
	private String[] shows;
	private String[] orders;
	private String orgId;
	private String errorMessage;
	private String currentDate;
	private String success;
	private String verify;
	public static final LinkedMap VALUES = new LinkedMap();
	public static final LinkedMap IMPORT_VALUE = new LinkedMap();
	static{
		VALUES.put("1", "是");
		VALUES.put("0", "否");
		IMPORT_VALUE.put("DR", "DR");
		IMPORT_VALUE.put("FT", "FT");
	}

	public String getCurrentDate(){
		return currentDate;
	}

	public void setCurrentDate(String currentDate){
		this.currentDate = currentDate;
	}

	public void setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public String getOrgId(){
		return orgId;
	}

	public void setOrgId(String orgId){
		this.orgId = orgId;
	}

	public String getTypeId(){
		return typeId;
	}

	public void setTypeId(String typeId){
		this.typeId = typeId;
	}

	public String[] getColumnIds(){
		return columnIds;
	}

	public void setColumnIds(String[] columnIds){
		this.columnIds = columnIds;
	}

	public String[] getDictionaryTypeIds(){
		return dictionaryTypeIds;
	}

	public void setDictionaryTypeIds(String[] dictionaryTypeIds){
		this.dictionaryTypeIds = dictionaryTypeIds;
	}

	public String[] getTxtIds(){
		return txtIds;
	}

	public void setTxtIds(String[] txtIds){
		this.txtIds = txtIds;
	}

	public String[] getTxtColumnIds(){
		return txtColumnIds;
	}

	public void setTxtColumnIds(String[] txtColumnIds){
		this.txtColumnIds = txtColumnIds;
	}

	public String getType(){
		return type;
	}

	public String getTableId(){
		return tableId;
	}

	public void setType(String type){
		this.type = type;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String getSuccess(){
		return success;
	}

	public void setSuccess(String success){
		this.success = success;
	}

	public String[] getModifys(){
		return modifys;
	}

	public void setModifys(String[] modifys){
		this.modifys = modifys;
	}

	public String[] getShows(){
		return shows;
	}

	public void setShows(String[] shows){
		this.shows = shows;
	}

	public String[] getOrders(){
		return orders;
	}

	public void setOrders(String[] orders){
		this.orders = orders;
	}

	public String getVerify(){
		return verify;
	}

	public void setVerify(String verify){
		this.verify = verify;
	}

	public String getImportType(){
		return importType;
	}

	public void setImportType(String importType){
		this.importType = importType;
	}
}
