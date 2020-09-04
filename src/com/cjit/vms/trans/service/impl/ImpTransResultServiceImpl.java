package com.cjit.vms.trans.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.VmsTransInfo;
import com.cjit.vms.trans.service.ImpDataService;
import com.cjit.vms.trans.service.ImpTransResultService;

public class ImpTransResultServiceImpl extends GenericServiceImpl implements
		ImpTransResultService {

	public List getImpResultListInfo(String impTime,
			PaginationList paginationList) {
		
	Map map = new HashMap();
		
		// 导入时间
		if (impTime != null && !"".equals(impTime)){
			map.put("impTime", impTime.trim().substring(0, 10));
		}
		if (paginationList == null){
			return find("findImpTransResultListInfo", map);
		}
		return find("findImpTransResultListInfo", map, paginationList);
		
		
	}

	public List expImpResultListInfo(String impTime,
			PaginationList paginationList) {
		// TODO Auto-generated method stub
		return null;
	}

}
