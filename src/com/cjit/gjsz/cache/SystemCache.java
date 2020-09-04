package com.cjit.gjsz.cache;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.DisposableBean;

import com.cjit.common.constant.ScopeConstants;
import com.cjit.gjsz.filem.service.OrgConfigeService;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;

public class SystemCache implements DisposableBean{

	private CacheManager cacheManager;
	private CacheabledMap paramCache = new CacheabledMap(false);
	private UserInterfaceConfigService userInterfaceConfigService;
	private OrgConfigeService orgConfigeService;
	private String realPath = null;
	private Map configMap = null;
	private Map configMtsMap = null;
	public static Map byteCache = new HashMap();
	private static CacheabledMap sessionCache = new CacheabledMap(false);

	public final void runCacheRegister() throws IOException{
		// VMS-CANCEL
		//registerParams();
	}

	public void registerParams() throws IOException{
		this.configMap = userInterfaceConfigService.initConfigParameters();
		configMtsMap = userInterfaceConfigService.initConfigMts();
		paramCache.put("configMap", this.configMap);
		List orgConfigList = orgConfigeService.findAll("", "", "", null);
		paramCache.put("orgConfigList", orgConfigList);
		paramCache.put("realPath", this.realPath);
		byteCache.clear();
		cacheManager.register("paramCache", paramCache, false);
		// 写入字典项
		Map[] map = userInterfaceConfigService.initDictionaryMap();
		sessionCache.put(ScopeConstants.SESSION_DICTIONARY_MAP, map[0]);
		sessionCache.put(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE, map[1]);
	}

	public static Object getSessionCache(String key){
		return sessionCache.get(key);
	}

	public void initConfigMts(){
		configMtsMap = userInterfaceConfigService.initConfigMts();
	}

	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}

	public void destroy() throws Exception{
		cacheManager.cleanAll();
	}

	public UserInterfaceConfigService getUserInterfaceConfigService(){
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public OrgConfigeService getOrgConfigeService(){
		return orgConfigeService;
	}

	public void setOrgConfigeService(OrgConfigeService orgConfigeService){
		this.orgConfigeService = orgConfigeService;
	}

	public String getRealPath(){
		return realPath;
	}

	public void setRealPath(String realPath){
		this.realPath = realPath;
	}

	public Map getConfigMap(){
		return configMap;
	}

	public void setConfigMap(Map configMap){
		this.configMap = configMap;
	}

	public Map getConfigMtsMap(){
		return configMtsMap;
	}

	public void setConfigMtsMap(Map configMtsMap){
		this.configMtsMap = configMtsMap;
	}
}
