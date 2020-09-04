package com.cjit.gjsz.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CacheManager{

	public static final String U_BASE_CONFIG_LIST = "baseConfigList";
	public static final String SUB_SYS_MENU_MAP = "subSysMenuMap";
	private static final Map cacheDescMap = new HashMap();
	static{
		cacheDescMap.put(U_BASE_CONFIG_LIST, "");
		cacheDescMap.put(SUB_SYS_MENU_MAP, "");
	}

	public static Map getCacheDescMap(){
		return cacheDescMap;
	}
	
	private static Map cache = new HashMap();

	public void clean(String key){
		Object object = cache.get(key);
		if(object != null){
			((Cacheable) object).clear();
		}
	}

	public void cleanAll(){
		for(Iterator iter = cache.keySet().iterator(); iter.hasNext();){
			Object o = iter.next();
			iter.remove();
			cache.remove(o);
		}
	}

	public boolean isIncreaseCache(String cacheKey){
		return !needReLoadL.contains(cacheKey);
	}

	private List needReLoadL = new ArrayList();

	public void register(String key, Cacheable value, boolean isIncreasing){
		// logger.debug("Register: [" + key + "]");
		cache.put(key, value);
		if(!isIncreasing){
			needReLoadL.add(key);
		}
	}

	public int getRegisterCount(){
		return cache.size();
	}

	public Iterator keyIterator(){
		return cache.keySet().iterator();
	}
	
	public static Cacheable getCacheObject(String key){
		return (Cacheable) cache.get(key);
	}
	
	public static String getParemerCacheMapValue(String paramkey){
		CacheabledMap cache = (CacheabledMap) getCacheObject("test");
		Map params = null;
		if(cache != null){
			params = (Map) cache.get("test");
			/*if(null != params){
				UBaseSysParamDO param = (UBaseSysParamDO) params.get(paramkey);
				if(param !=null)
					return param.getSelectedValue()==null?"":param.getSelectedValue();
			}*/
		}
		return "";
	}

}
