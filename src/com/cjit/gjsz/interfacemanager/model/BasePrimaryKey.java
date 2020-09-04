/**
 * TableInfo
 */
package com.cjit.gjsz.interfacemanager.model;

/**
 * @author huboA
 * @table t_rpt_table_info
 */
public class BasePrimaryKey{

	private String reportId;
	private int dataStatus;

	public String getReportId(){
		return reportId;
	}

	public void setReportId(String reportId){
		this.reportId = reportId;
	}

	public int getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(int dataStatus){
		this.dataStatus = dataStatus;
	}
}
