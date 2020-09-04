package com.cjit.gjsz.system.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cjit.logger.LogDO;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;

public interface LogManagerService{

	/**
	 * <p> 方法名称: writeLog|描述: </p>
	 * @param logDO 日志对象
	 */
	public abstract void writeLog(LogDO logDO);

	/**
	 * <p> 方法名称: writeLog|描述: </p>
	 * @param menuId 栏目编号
	 * @param menuName 栏目名称
	 * @param logType 日志类型
	 * @param description 描述
	 */
	public void writeLog(HttpServletRequest request, User user, String menuId,
			String menuName, String logType, String description, String status);

	/**
	 * <p> 方法名称: writeLog|描述: </p>
	 * @param userId 用户编号
	 * @param userEname 用户登录名
	 * @param userCname 用户中文
	 * @param instId 机构编号
	 * @param instCname 机构名称
	 * @param menuId 栏目编号
	 * @param menuName 栏目名称
	 * @param ip 用户IP
	 * @param browse 用户浏览器
	 * @param logType 日志类型
	 * @param execTime 执行时间
	 * @param description 描述
	 * @param status 执行状态
	 */
	public abstract void writeLog(String userId, String userEname,
			String userCname, String instId, String instCname, String menuId,
			String menuName, String ip, String browse, String logType,
			Date execTime, String description, String status);

	public LogDO getLog(String userId, String userEname, String userCname,
			String instId, String instCname, String menuId, String menuName,
			String ip, String browse, String logType, Date execTime,
			String description, String status);
	
	public abstract List selectByFormWithPaging(LogDO log,
			PaginationList paginationList);

	public abstract List selectByFormWithPaging(Map parms,
			PaginationList paginationList);

	public abstract int deleteByPrimarys(String[] ids);
	public void insertBatch(final List logList);
}