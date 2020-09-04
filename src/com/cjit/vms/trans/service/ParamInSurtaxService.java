package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.ParamInSurtaxListInfo;

public interface ParamInSurtaxService {
	public List findParamInSurtaxListInfo(ParamInSurtaxListInfo info,PaginationList paginationList);
	
	public ParamInSurtaxListInfo findParamInSurtaxItemInfo(ParamInSurtaxListInfo info);
	
	public void saveParamInSurtaxInfo(ParamInSurtaxListInfo info);
}
