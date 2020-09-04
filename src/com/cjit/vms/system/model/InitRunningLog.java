package com.cjit.vms.system.model;

/**
 * 初始化跑入数据日志类
 * 
 * @author Larry
 */
public class InitRunningLog {

	// 数据表中字段属性
	private String userId;// 初始化执行用户ID
	private String initTime;// 初始化执行开始时间
	private String runTime;// 初始化阶段执行时间
	private String instCode;// 机构ID
	private String dataDate;// 数据日期
	private String description;// 执行描述
	private String tableId;// 表单ID
	// 辅助属性
	private String isRecent;// 是否查询最近的记录

	public InitRunningLog() {
	}

	public InitRunningLog(String userId, String initTime, String runTime,
			String instCode, String dataDate, String description, String tableId) {
		this.userId = userId;
		this.initTime = initTime;
		this.runTime = runTime;
		this.instCode = instCode;
		this.dataDate = dataDate;
		this.description = description;
		this.tableId = tableId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInitTime() {
		return initTime;
	}

	public void setInitTime(String initTime) {
		this.initTime = initTime;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getIsRecent() {
		return isRecent;
	}

	public void setIsRecent(String isRecent) {
		this.isRecent = isRecent;
	}
}
