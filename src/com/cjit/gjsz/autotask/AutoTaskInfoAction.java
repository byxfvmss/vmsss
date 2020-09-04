package com.cjit.gjsz.autotask;

import com.cjit.gjsz.common.action.BaseListAction;

public class AutoTaskInfoAction extends BaseListAction{

	private static final long serialVersionUID = 1L;
	private AutoTaskInfoService autoTaskInfoService;
	
	private String runDate;
	private String type;
	public String showInfo()
	{
		getAutoTaskInfoService().queryAutoTaskInfo(runDate,type,paginationList);
		return SUCCESS;
	}

	public AutoTaskInfoService getAutoTaskInfoService() {
		return autoTaskInfoService;
	}

	public void setAutoTaskInfoService(AutoTaskInfoService autoTaskInfoService) {
		this.autoTaskInfoService = autoTaskInfoService;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
