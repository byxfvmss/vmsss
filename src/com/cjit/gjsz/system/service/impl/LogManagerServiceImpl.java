package com.cjit.gjsz.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cjit.logger.LogDO;
import cjit.logger.LogManagerBatch;
import cjit.logger.PageBox;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.PaginationList;
import com.cjit.fmss.api.servlet.ServletHelper;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;

/**
 * @日期: 2009-11-3 下午01:47:36
 * @描述: [LogManagerService]请在此简要描述类的功能
 */
public class LogManagerServiceImpl implements LogManagerService{

	private LogManagerBatch logManagerBatch;

	public void writeLog(LogDO logDO){
		logManagerBatch.insert(logDO);
	}

	/**
	 * <p> 方法名称: writeLog|描述: </p>
	 * @param menuId 栏目编号
	 * @param menuName 栏目名称
	 * @param logType 日志类型
	 * @param description 描述
	 */
	public void writeLog(HttpServletRequest request, User user, String menuId,
			String menuName, String logType, String description, String status){
		LogDO logDO = new LogDO(menuId, menuName, logType, description, status);
		logDO.setUserId(user.getId());
		logDO.setUserEname(user.getUsername());
		logDO.setUserCname(user.getName());
		logDO.setInstId(user.getOrgId());
		logDO.setInstCname(user.getOrgName());
		logDO.setExecTime(new Date());
		// logDO.setIp(request.getRemoteAddr());
		logDO.setIp(ServletHelper.getRemoteAddr(request));
		logDO.setBrowse(request.getHeader("User-Agent"));
		logDO.setSystemId(Constants.SYSTEM_ID);
		logManagerBatch.insert(logDO);
	}

	public void writeLog(String userId, String userEname, String userCname,
			String instId, String instCname, String menuId, String menuName,
			String ip, String browse, String logType, Date execTime,
			String description, String status){
		LogDO logDO = new LogDO();
		logDO.setUserId(userId);
		logDO.setUserEname(userEname);
		logDO.setUserCname(userCname);
		logDO.setInstId(instId);
		logDO.setInstCname(instCname);
		logDO.setMenuId(menuId);
		logDO.setMenuName(menuName);
		logDO.setIp(ip);
		logDO.setBrowse(browse);
		logDO.setLogType(logType);
		logDO.setExecTime(execTime);
		logDO.setDescription(description);
		logDO.setSystemId(Constants.SYSTEM_ID);
		logDO.setStatus(status);
		logManagerBatch.insert(logDO);
	}
	
	public LogDO getLog(String userId, String userEname, String userCname,
			String instId, String instCname, String menuId, String menuName,
			String ip, String browse, String logType, Date execTime,
			String description, String status){
		LogDO logDO = new LogDO();
		logDO.setUserId(userId);
		logDO.setUserEname(userEname);
		logDO.setUserCname(userCname);
		logDO.setInstId(instId);
		logDO.setInstCname(instCname);
		logDO.setMenuId(menuId);
		logDO.setMenuName(menuName);
		logDO.setIp(ip);
		logDO.setBrowse(browse);
		logDO.setLogType(logType);
		logDO.setExecTime(execTime);
		logDO.setDescription(description);
		logDO.setStatus(status);
		return logDO;
	}
	
	public void insertBatch(final List logList)
	{
		logManagerBatch.insertBatch(logList);
	}

	public List selectByFormWithPaging(LogDO log, PaginationList paginationList){
		PageBox pb = logManagerBatch.selectByFormWithPaging(log, paginationList
				.getPageSize(), paginationList.getCurrentPage());
		paginationList.setCurrentPage(pb.getPageObject().getPageIndex());
		if(pb.getPageObject() != null)
			paginationList.setPageSize(pb.getPageObject().getPageSize());
		else
			paginationList.setPageSize(10);
		paginationList.setRecordCount(pb.getPageObject().getItemAmount());
		paginationList.setRecordList(pb.getPageList());
		return pb.getPageList();
	}

	public List selectByFormWithPaging(Map parms, PaginationList paginationList){
		LogDO logDO = new LogDO();
		logDO.setUserId((String) parms.get("userId"));
		logDO.setUserEname((String) parms.get("userEname"));
		logDO.setUserCname((String) parms.get("userCname"));
		logDO.setInstId((String) parms.get("instId"));
		logDO.setInstCname((String) parms.get("instCname"));
		logDO.setMenuId((String) parms.get("menuId"));
		logDO.setMenuName((String) parms.get("menuName"));
		logDO.setIp((String) parms.get("ip"));
		logDO.setBrowse((String) parms.get("browse"));
		logDO.setLogType((String) parms.get("logType"));
		logDO.setExecTime((Date) parms.get("execTime"));
		logDO.setDescription((String) parms.get("description"));
		logDO.setStatus((String) parms.get("status"));
		PageBox pb = logManagerBatch.selectByFormWithPaging(logDO, paginationList
				.getPageSize(), paginationList.getCurrentPage());
		if(pb.getPageObject() != null){
			paginationList.setCurrentPage(pb.getPageObject().getPageIndex());
			paginationList.setPageSize(pb.getPageObject().getPageSize());
			paginationList.setRecordCount(pb.getPageObject().getItemAmount());
			paginationList.setRecordList(pb.getPageList());
		}else{
			paginationList.setPageSize(10);
			paginationList.setCurrentPage(1);
			paginationList.setRecordCount(1);
		}
		return pb.getPageList();
	}

	public int deleteByPrimarys(String[] ids){
		return logManagerBatch.deleteByPrimarys(ids);
	}

	public LogManagerBatch getLogManagerBatch() {
		return logManagerBatch;
	}

	public void setLogManagerBatch(LogManagerBatch logManagerBatch) {
		this.logManagerBatch = logManagerBatch;
	}

}
