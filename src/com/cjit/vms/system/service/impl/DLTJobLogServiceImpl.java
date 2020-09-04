package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.system.service.DLTJobLogService;

public class DLTJobLogServiceImpl extends GenericServiceImpl implements
		DLTJobLogService {

	public List findDLTJobLog(String runDay,String jobName ) {
		Map map = new HashMap();
		map.put("runDay", runDay);
		map.put("jobName", jobName);
		return find("findDLTJobLog", map);
	}

}
