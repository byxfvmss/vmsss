package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.BusinessInfo;
import com.cjit.vms.system.service.BusinessInfoService;

public class BusinessInfoServiceImpl extends GenericServiceImpl implements
		BusinessInfoService {

	public List findBusinessInfoList(BusinessInfo business,
			PaginationList paginationList) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("business", business);
		if (null == paginationList) {
			return find("findBusinessInfoList",map);
		}
		return find("findBusinessInfoList", map,paginationList);
	}

	public void insertBusinessInfo(BusinessInfo business) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("business", business);
		save("insertBusinessInfo", map);
	}

	public void updateBusinessInfo(BusinessInfo business) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("business", business);
		update("updateBusinessInfo", map);
	}

	public void deleteBusinessInfo(String businessId) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("businessId", businessId);
		delete("deleteBusinessInfo", map);
	}

	public List findBusinessInfo(BusinessInfo business) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("business", business);
		
		return find("findBusinessInfo", map);
	}

	
}
