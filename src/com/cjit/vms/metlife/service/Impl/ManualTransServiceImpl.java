package com.cjit.vms.metlife.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.ManualTrans;
import com.cjit.vms.metlife.service.ManualTransService;

public class ManualTransServiceImpl extends GenericServiceImpl implements ManualTransService {

	@Override
	public List findManualTrans(ManualTrans trans,PaginationList paginationList) {
		Map map = new HashMap();
		map.put("trans", trans);
		if(paginationList == null){
			return this.find("findManualTrans", map);
		}
		return this.find("findManualTrans", map,paginationList);
	}
	
	
}
