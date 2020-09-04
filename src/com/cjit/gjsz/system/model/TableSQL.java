package com.cjit.gjsz.system.model;

public class TableSQL {

	//
	private String tableId;// 表单ID
	private String fileType;// 文件接口类型
	private String initSQL;// 初始化脚本
	private String traceSQL;// 追溯脚本
	private String tableName;// 表单名称
	private String isSingleSummary;// 是否单行汇总记录(指定机构指定月只报送一笔汇总记录)
	private String summaryColumns;// 汇总字段配置(如$country:A0111;$currency:A0112)
	// 
	private String infoType;// 业务类型
	private String existsInitSQL;
	private String existsTraceSQL;

	public TableSQL() {
	}

	public TableSQL(String tableId, String fileType) {
		this.tableId = tableId;
		this.fileType = fileType;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getInitSQL() {
		return initSQL;
	}

	public void setInitSQL(String initSQL) {
		this.initSQL = initSQL;
	}

	public String getTraceSQL() {
		return traceSQL;
	}

	public void setTraceSQL(String traceSQL) {
		this.traceSQL = traceSQL;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIsSingleSummary() {
		return isSingleSummary;
	}

	public void setIsSingleSummary(String isSingleSummary) {
		this.isSingleSummary = isSingleSummary;
	}

	public String getSummaryColumns() {
		return summaryColumns;
	}

	public void setSummaryColumns(String summaryColumns) {
		this.summaryColumns = summaryColumns;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getExistsInitSQL() {
		if (initSQL != null) {
			existsInitSQL = "已配置";
		} else {
			existsInitSQL = "";
		}
		return existsInitSQL;
	}

	public String getExistsTraceSQL() {
		if (traceSQL != null) {
			existsTraceSQL = "已配置";
		} else {
			existsTraceSQL = "";
		}
		return existsTraceSQL;
	}
}
