package com.cjit.vms.system.model;

import java.sql.Date;


/**
 * @author tom 导入日志管理
 *
 */
public class LogEmp {
	/*ID	NUMBER	N			
	SYS	VARCHAR2(50)	N			业务类别：INPUT_TRANS,INPUT_BILL,OUTPUT_TRANS,OUTPUT_BILL,CUSTOMER,VENDOR
	START_DATE	TIMESTAMP(6)	N			开始日期
	END_DATE	TIMESTAMP(6)	N			结束日期
	BATCH_NO	VARCHAR2(50)	N			批次
	SUCCESS_NO	NUMBER(6)	N			成功条数
	FAILED_NO	NUMBER(6)	N			失败条数
	
	FAILED_LOG	VARCHAR2(4000)	Y			失败日志*/
	private String id;
	private String sys;
	private String startDate;
	private String endDate;
	private String batchNo;
	private String successNo;
	private String failedNo;
	private String failedLog;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSys() {
		return sys;
	}
	public void setSys(String sys) {
		this.sys = sys;
	}
	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getSuccessNo() {
		return successNo;
	}
	public void setSuccessNo(String successNo) {
		this.successNo = successNo;
	}
	public String getFailedNo() {
		return failedNo;
	}
	public void setFailedNo(String failedNo) {
		this.failedNo = failedNo;
	}
	public String getFailedLog() {
		return failedLog;
	}
	public void setFailedLog(String failedLog) {
		this.failedLog = failedLog;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	


	

	
	
}
