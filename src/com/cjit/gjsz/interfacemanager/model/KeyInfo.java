/**
 * TableInfo
 */
package com.cjit.gjsz.interfacemanager.model;

import java.util.Date;

import com.cjit.common.util.DateUtils;

/**
 * @table t_rpt_table_info
 */
public class KeyInfo{

	public static final String[] tables = {"t_base_income", "t_base_remit",
			"t_base_payment", "t_base_export", "t_base_dom_remit",
			"t_base_dom_pay", "t_base_settlement", "t_base_purchase"};
	// 机构号，日期，报表表号，对公对私标志
	private String orgId; // 机构号码
	private String customType;
	private String maxType;
	private String tableId;
	private Date currentDate;
	private String personalCode;
	private String publicCode;
	private String importType;
	private String fileType;
	private String rptNoColumnId;

	public KeyInfo(){
	}

	/**
	 * 机构号，日期，报表表号，对公对私标志
	 * @param orgId
	 * @param currentDate
	 * @param tableid
	 * @param fileType
	 * @param rptNoColumnId
	 * @param importType
	 */
	public KeyInfo(String orgId, Date currentDate, String tableId,
			String fileType, String rptNoColumnId, String importType){
		this.orgId = orgId;
		this.currentDate = currentDate;
		this.tableId = tableId;
		this.fileType = fileType;
		this.rptNoColumnId = rptNoColumnId;
		this.importType = importType;
	}

	public String getMaxType(){
		return maxType;
	}

	public void setMaxType(String maxType){
		this.maxType = maxType;
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

	public String getOrgId(){
		return orgId;
	}

	public String getCustomType(){
		return customType;
	}

	public String getTableId(){
		return tableId;
	}

	public String getCurrentDate(){
		return DateUtils
				.toString(currentDate, DateUtils.ORA_DATE_FORMAT);
	}

	public void setOrgId(String orgId){
		this.orgId = orgId;
	}

	public void setCustomType(String customType){
		this.customType = customType;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public void setCurrentDate(Date currentDate){
		this.currentDate = currentDate;
	}

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

	public String getRptNoColumnId(){
		return rptNoColumnId;
	}

	public void setRptNoColumnId(String rptNoColumnId){
		this.rptNoColumnId = rptNoColumnId;
	}
}
