package com.cjit.gjsz.autotask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;

public class AutoTaskInfoService extends GenericServiceImpl {

	public List queryAutoTaskInfo(String runDate, String type,
			PaginationList paginationList) {
		Map params = new HashMap();
		params.put("runDate", runDate);
		params.put("type", type);
		return this.find("queryAutoTaskInfo", params, paginationList);
	}
	
	
}
