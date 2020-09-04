/**
 * TableInfo
 */
package com.cjit.gjsz.interfacemanager.model;

import java.util.Date;

import com.cjit.common.util.DateUtils;

/**
 * @author huboA
 * @table t_rpt_table_info
 */
public class LoadData{

	private int id;
	private Date loadDate;
	private String orgId;
	private String orgName;
	private int userInterfaceId;
	private String userInterfaceName;
	private String loadStatus;
	private int status;
	private Date startDate;
	private Date endDate;

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getUserInterfaceId(){
		return userInterfaceId;
	}

	public void setUserInterfaceId(int userInterfaceId){
		this.userInterfaceId = userInterfaceId;
	}

	public String getOrgId(){
		return orgId;
	}

	public Date getStartDate(){
		return startDate;
	}

	public Date getEndDate(){
		return endDate;
	}

	public void setOrgId(String orgId){
		this.orgId = orgId;
	}

	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}

	public int getId(){
		return id;
	}

	public Date getLoadDate(){
		return loadDate;
	}

	public String getOrgName(){
		return orgName;
	}

	public String getUserInterfaceName(){
		return userInterfaceName;
	}

	public void setId(int id){
		this.id = id;
	}

	public void setLoadDate(Date loadDate){
		this.loadDate = loadDate;
	}

	public String getLoadDateString(){
		return DateUtils.toString(loadDate, DateUtils.ORA_DATES_FORMAT);
	}

	public void setOrgName(String orgName){
		this.orgName = orgName;
	}

	public void setUserInterfaceName(String userInterfaceName){
		this.userInterfaceName = userInterfaceName;
	}

	public String getLoadStatus(){
		return loadStatus;
	}

	public void setLoadStatus(String loadStatus){
		this.loadStatus = loadStatus;
	}
}
