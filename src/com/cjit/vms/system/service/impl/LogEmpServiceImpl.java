package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.system.model.LogEmp;
import com.cjit.vms.system.service.LogEmpService;

public class LogEmpServiceImpl  extends GenericServiceImpl implements LogEmpService  {

	public void saveLogEmp(LogEmp logEmp) {
		Map map =new HashMap();
		map.put("logEmp", logEmp);
		this.save("saveLogEmp", map);
	}

}
