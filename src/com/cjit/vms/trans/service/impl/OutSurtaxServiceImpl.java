package com.cjit.vms.trans.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.OutSurtaxListInfo;
import com.cjit.vms.trans.service.OutSurtaxService;

public class OutSurtaxServiceImpl  extends GenericServiceImpl implements OutSurtaxService {

	public List findOutSurtaxList(OutSurtaxListInfo info,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("taxPerNumber", info.getTaxPerNumber());
		map.put("taxPerName", info.getTaxPerName());
		map.put("surtaxType", info.getSurtaxType());
		map.put("surtaxRate", info.getSurtaxRate());
		map.put("applyPeriod", info.getApplyPeriod());
		if(paginationList==null){
			return this.find("findOutSurtaxListInfo", map);
		}else{
			return this.find("findOutSurtaxListInfo", map,paginationList);
		}
	}

}
