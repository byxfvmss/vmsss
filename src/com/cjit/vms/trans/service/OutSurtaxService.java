package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.OutSurtaxListInfo;


public interface OutSurtaxService {
	public List findOutSurtaxList(OutSurtaxListInfo info,PaginationList paginationList);
}
