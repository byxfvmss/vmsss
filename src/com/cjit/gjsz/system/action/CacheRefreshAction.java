package com.cjit.gjsz.system.action;

import java.io.IOException;
import java.util.Map;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.datadeal.action.DataDealAction;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;

public class CacheRefreshAction extends DataDealAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1111111111111111111L;
	private LogManagerService logManagerService;
	//private InterfaceVerService interfaceVerService;
	private String success = "";
	private SystemCache systemCache;

	public String cacheRefresh(){
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		try{
			// 受权机构列表
			getAuthInstList(authInstList);
			// 部分信息若在值栈里找不到，则从SESSION中取
			if(infoTypeCode == null){
				infoTypeCode = (String) this
						.getFieldFromSession(ScopeConstants.CURRENT_INFO_TYPE_CODE);
			}
			if(instCode == null){
				instCode = (String) this
						.getFieldFromSession(ScopeConstants.CURRENT_INST_CODE);
				if(instCode == null){
					instCode = ((Organization) authInstList.get(0)).getId();
				}
			}
			// 把部分信息放在session中
			this.addFieldToSession(ScopeConstants.CURRENT_INST_CODE, instCode);
			this.addFieldToSession(ScopeConstants.CURRENT_INFO_TYPE_CODE,
					infoTypeCode);
			// 把报表列信息和查询SQL放在SESSION中
			Map[] mapSQL = dataDealService
					.initRptColumnSqlMapNew(largestColumnNum);
			this
					.addFieldToSession(
							ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP,
							mapSQL[0]);
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP, mapSQL[1]);
			// 将字典信息放在SESSION中
//			Map[] mapDict = userInterfaceConfigService.refreshDictionaryMap();
//			this.addFieldToSession(ScopeConstants.SESSION_DICTIONARY_MAP,
//					mapDict[0]);
//			this.addFieldToSession(
//					ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE, mapDict[1]);
			// 将参数配置项记录信息放在SESSION中
			configMap = userInterfaceConfigService.initConfigParameters();
			this.addFieldToSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
					configMap);
			this.setConfigParametersBaseList(configMap);
			this.setConfigParametersDataDeal(configMap);
			// 依所施行接口规范版本不同，对系统基础数据进行修改
			this.updateDataByInterface();
			// -----refreshCache--begin----
			String realPath = this.servletContext.getRealPath("/");
			systemCache.setConfigMap(configMap);
			systemCache.setRealPath(realPath);
			systemCache.registerParams();
			// -----refreshCache--end----
			logManagerService.writeLog(request, currentUser, "0006", "刷新缓存",
					"刷新", "刷新CFA缓存", "1");
			return SUCCESS;
		}catch (IOException e){
			e.printStackTrace();
			logManagerService.writeLog(request, currentUser, "0006", "刷新缓存",
					"刷新", "刷新CFA缓存", "0");
			return ERROR;
		}catch (Exception ex){
			ex.printStackTrace();
			logManagerService.writeLog(request, currentUser, "0006", "刷新缓存",
					"刷新", "刷新CFA缓存", "0");
			return ERROR;
		}
	}

	/**
	 * 依所施行接口规范版本不同，对系统基础数据进行修改
	 */
	private void updateDataByInterface(){
	}

	public String cacheList(){
		this.request.setAttribute("success", this.success);
		return SUCCESS;
	}

	public void setLogManagerService(LogManagerService logManagerService){
		this.logManagerService = logManagerService;
	}

	//public void setInterfaceVerService(InterfaceVerService interfaceVerService){
	//	this.interfaceVerService = interfaceVerService;
	//}

	public String getSuccess(){
		return success;
	}

	public void setSuccess(String success){
		this.success = success;
	}

	public SystemCache getSystemCache(){
		return systemCache;
	}

	public void setSystemCache(SystemCache systemCache){
		this.systemCache = systemCache;
	}
}
