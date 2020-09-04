/**
 * 
 */
package com.cjit.gjsz.datadeal.model;

/**
 * @author ylb
 */
public class RptColumnInfo{

	private String columnId;
	private String tableId;
	private String columnName;
	private String aliasColumnId;
	private int order;
	private String position;
	private String dataType;
	private String dictionaryTypeId;
	private String consRule;
	private int txtId;
	private int txtColumnId;
	private String isShow;
	private String tagType;
	private String canModify;
	private String dataTypeVDesc;
	private String consRuleVDesc;
	private boolean dataTypeVSuccess = true;
	private boolean consRuleVSuccess = true;
	private String logColumnId;
	private String isKeyword;
	private String isEnabled;
	private String fileType;
	private String canInput;
	private String isReport;

	public RptColumnInfo(){
	}

	public RptColumnInfo(String tableId, String isShow){
		this.tableId = tableId;
		this.isShow = isShow;
	}

	public RptColumnInfo(String tableId, String isShow, String isEnabled,
			String fileType){
		this.tableId = tableId;
		this.isShow = isShow;
		this.isEnabled = isEnabled;
		this.fileType = fileType;
	}

	public RptColumnInfo(String columnId, String columnName,
			String aliasColumnId, String dictionaryTypeId, String tagType){
		this.columnId = columnId;
		this.columnName = columnName;
		this.aliasColumnId = aliasColumnId;
		this.dictionaryTypeId = dictionaryTypeId;
		this.tagType = tagType;
	}

	public String getColumnId(){
		return columnId;
	}

	public void setColumnId(String columnId){
		this.columnId = columnId;
	}

	public String getTableId(){
		return tableId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String getColumnName(){
		return columnName;
	}

	public void setColumnName(String columnName){
		this.columnName = columnName;
	}

	public int getOrder(){
		return order;
	}

	public void setOrder(int order){
		this.order = order;
	}

	public String getPosition(){
		return position;
	}

	public void setPosition(String position){
		this.position = position;
	}

	public String getDataType(){
		return dataType;
	}

	public void setDataType(String dataType){
		this.dataType = dataType;
	}

	public String getDictionaryTypeId(){
		return dictionaryTypeId;
	}

	public void setDictionaryTypeId(String dictionaryTypeId){
		this.dictionaryTypeId = dictionaryTypeId;
	}

	public String getConsRule(){
		return consRule;
	}

	public void setConsRule(String consRule){
		this.consRule = consRule;
	}

	public int getTxtId(){
		return txtId;
	}

	public void setTxtId(int txtId){
		this.txtId = txtId;
	}

	public int getTxtColumnId(){
		return txtColumnId;
	}

	public void setTxtColumnId(int txtColumnId){
		this.txtColumnId = txtColumnId;
	}

	public String getIsShow(){
		return isShow;
	}

	public void setIsShow(String isShow){
		this.isShow = isShow;
	}

	public String getAliasColumnId(){
		return aliasColumnId;
	}

	public void setAliasColumnId(String aliasColumnId){
		this.aliasColumnId = aliasColumnId;
	}

	public String getTagType(){
		return tagType;
	}

	public void setTagType(String tagType){
		this.tagType = tagType;
	}

	public String getCanModify(){
		return canModify;
	}

	public void setCanModify(String canModify){
		this.canModify = canModify;
	}

	public String getDataTypeVDesc(){
		return dataTypeVDesc;
	}

	public void setDataTypeVDesc(String dataTypeVDesc){
		this.dataTypeVDesc = dataTypeVDesc;
	}

	public String getConsRuleVDesc(){
		return consRuleVDesc;
	}

	public void setConsRuleVDesc(String consRuleVDesc){
		this.consRuleVDesc = consRuleVDesc;
	}

	public boolean isDataTypeVSuccess(){
		return dataTypeVSuccess;
	}

	public void setDataTypeVSuccess(boolean dataTypeVSuccess){
		this.dataTypeVSuccess = dataTypeVSuccess;
	}

	public boolean isConsRuleVSuccess(){
		return consRuleVSuccess;
	}

	public void setConsRuleVSuccess(boolean consRuleVSuccess){
		this.consRuleVSuccess = consRuleVSuccess;
	}

	public String getLogColumnId(){
		return logColumnId;
	}

	public void setLogColumnId(String logColumnId){
		this.logColumnId = logColumnId;
	}

	public String getIsKeyword(){
		return isKeyword;
	}

	public void setIsKeyword(String isKeyword){
		this.isKeyword = isKeyword;
	}

	public String getIsEnabled(){
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled){
		this.isEnabled = isEnabled;
	}

	public String getFileType(){
		return fileType == null ? null : fileType.trim();
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getCanInput(){
		return canInput;
	}

	public void setCanInput(String canInput){
		this.canInput = canInput;
	}

	public String getIsReport(){
		return isReport;
	}

	public void setIsReport(String isReport){
		this.isReport = isReport;
	}
}
