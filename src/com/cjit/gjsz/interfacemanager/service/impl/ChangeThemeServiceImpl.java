package com.cjit.gjsz.interfacemanager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.interfacemanager.model.FmssPathObject;
import com.cjit.gjsz.interfacemanager.model.UBaseSysParamDO;
import com.cjit.gjsz.interfacemanager.service.ChangeThemeService;

public class ChangeThemeServiceImpl extends GenericServiceImpl implements
		ChangeThemeService{

	private String bopthemepath;

	public String getBopthemepath(){
		return bopthemepath;
	}

	public void setBopthemepath(String bopthemepath){
		this.bopthemepath = bopthemepath;
	}

	public String getCurrentTheme(){
		String bopthemepath = "default";
		Map map = new HashMap();
		map.put("currentUserId", "1");
		List list = (List) this.find("getThemeType", map);
		if(CollectionUtil.isNotEmpty(list)){
			// ThemeObject theme = (ThemeObject) list.get(0);
			// bopthemepath = theme.getTheme_Path();
			UBaseSysParamDO theme = (UBaseSysParamDO) list.get(0);
			bopthemepath = theme.getSelected_value();
		}
		return bopthemepath;
	}

	public String getCrrentWebPath(){
		String uprrPath = "null";
		Map map = new HashMap();
		map.put("uprrPath", "1");
		List list = (List) this.find("uprrPath", map);
		if(CollectionUtil.isNotEmpty(list)){
			FmssPathObject web = (FmssPathObject) list.get(0);
			uprrPath = web.getUnit_login_url();
		}
		return uprrPath;
	}
}