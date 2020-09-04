package com.cjit.vms.metlife.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.ManualTrans;

public interface ManualTransService {
	
	public List findManualTrans(ManualTrans trans,PaginationList paginationList);
	
}
