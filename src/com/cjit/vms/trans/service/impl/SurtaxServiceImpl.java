package com.cjit.vms.trans.service.impl;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.service.SurtaxService;

public class SurtaxServiceImpl extends GenericServiceImpl implements
		SurtaxService {

	public List selectSurtaxAMT(Map parameters, PaginationList paginationList) {

		if (null == paginationList) {
			return this.find("selectSurtaxAMT",parameters);
		}
		
		return this.find("selectSurtaxAMT", parameters,paginationList);
	}

}
