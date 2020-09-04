package com.cjit.gjsz.system.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;

public class Configuration extends BaseListAction{

	private String message;
	private UserInterfaceConfigService userInterfaceConfigService;
	private SystemCache systemCache;
	// config信息
	private String autoCheckRunTime;
	private List dataStatusList;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String configuration(){
		Map configMap = null;
		if("yes".equalsIgnoreCase(this.configIsCluster)){
			configMap = getUserInterfaceConfigService().initConfigParameters();
		}else{
			configMap = (HashMap) this
					.getFieldFromSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP);
			if(configMap == null){
				configMap = getUserInterfaceConfigService()
						.initConfigParameters();
				this.addFieldToSession(
						ScopeConstants.SESSION_CONFIG_PARAMETER_MAP, configMap);
			}
		}
		// 编辑查看数据首次展现时 展开 or 隐藏
		this.setAutoShow((String) configMap.get("config.autoShow"));
		if(StringUtil.isEmpty(this.getAutoShow()))
			this.setAutoShow("no");
		// 自动校验执行时间
		this.setAutoCheckRunTime((String) configMap
				.get("config.autoCheck.runtime"));
		// 错误反馈报文记录目标状态
		this.changeDataStatus = (String) configMap
				.get("config.changeDataStatus.forErrFeedBack");
		if(StringUtil.isEmpty(this.changeDataStatus)){
			this.changeDataStatus = String.valueOf(DataUtil.YSC_STATUS_NUM);
		}
		initDataStatusList();
		return SUCCESS;
	}

	public String save(){
		// 校验
		if(!validateConfig())
			return SUCCESS;
		// 保存
		userInterfaceConfigService.updateConfig("config.autoShow", this
				.getAutoShow());
		userInterfaceConfigService.updateConfig("config.autoCheck.runtime",
				this.autoCheckRunTime);
		userInterfaceConfigService
				.updateConfig("config.changeDataStatus.forErrFeedBack",
						this.changeDataStatus);
		initDataStatusList();
		// 刷新缓存
		Map configMap = getUserInterfaceConfigService().initConfigParameters();
		this.addFieldToSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
				configMap);
		this.message = "saveSuccess";
		this.request.setAttribute("message", this.message);
		try{
			systemCache.registerParams();
		}catch (IOException e){
			e.printStackTrace();
		}
		return SUCCESS;
	}

	private boolean validateConfig(){
		if(StringUtil.isNotEmpty(this.autoCheckRunTime)){
			this.autoCheckRunTime = StringUtils.replace(this.autoCheckRunTime,
					"；", ";");
			this.autoCheckRunTime = StringUtils.replace(this.autoCheckRunTime,
					"：", ":");
			if(this.autoCheckRunTime.indexOf(";") >= 0){
				String[] runTimes = this.autoCheckRunTime.split(";");
				if(runTimes != null && runTimes.length > 0){
					for(int i = 0; i < runTimes.length; i++){
						if(StringUtil.isNotBlank(runTimes[i])){
							if(!DateUtils.isValidDate(runTimes[i], "HH:mm")){
								this.message = "notHHmm";
								return false;
							}else{
								int nHour = Integer.valueOf(
										(runTimes[i].split(":"))[0]).intValue();
								int nMinute = Integer.valueOf(
										(runTimes[i].split(":"))[1]).intValue();
								if(nHour < 0 || nHour > 23 || nMinute < 0
										|| nMinute > 59){
									this.message = runTimes[i];
									return false;
								}
							}
						}
					}
				}
			}else{
				if(!DateUtils.isValidDate(this.autoCheckRunTime, "HH:mm")){
					this.message = "notHHmm";
					return false;
				}else{
					int nHour = Integer.valueOf(
							(this.autoCheckRunTime.split(":"))[0]).intValue();
					int nMinute = Integer.valueOf(
							(this.autoCheckRunTime.split(":"))[1]).intValue();
					if(nHour < 0 || nHour > 23 || nMinute < 0 || nMinute > 59){
						this.message = this.autoCheckRunTime;
						return false;
					}
				}
			}
		}
		return true;
	}

	private void initDataStatusList(){
		this.dataStatusList = new ArrayList();
		SelectTag s1 = new SelectTag(String.valueOf(DataUtil.WJY_STATUS_NUM),
				DataUtil.WJY_STATUS_CH);
		this.dataStatusList.add(s1);
		SelectTag s7 = new SelectTag(String.valueOf(DataUtil.YSC_STATUS_NUM),
				DataUtil.YSC_STATUS_CH);
		this.dataStatusList.add(s7);
	}

	public UserInterfaceConfigService getUserInterfaceConfigService(){
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public SystemCache getSystemCache(){
		return systemCache;
	}

	public void setSystemCache(SystemCache systemCache){
		this.systemCache = systemCache;
	}

	public String getAutoCheckRunTime(){
		return autoCheckRunTime;
	}

	public void setAutoCheckRunTime(String autoCheckRunTime){
		this.autoCheckRunTime = autoCheckRunTime;
	}

	public List getDataStatusList(){
		return dataStatusList;
	}

	public void setDataStatusList(List dataStatusList){
		this.dataStatusList = dataStatusList;
	}
}
