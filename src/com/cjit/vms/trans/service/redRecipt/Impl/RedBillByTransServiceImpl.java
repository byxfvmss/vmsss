package com.cjit.vms.trans.service.redRecipt.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.redRecipt.RedBillByTransService;

public class RedBillByTransServiceImpl extends GenericServiceImpl implements RedBillByTransService {

	@Override
	public List selectReverseTrans(TransInfo transInfo,PaginationList paginationList) {
		Map parameters = new HashMap();
		findLargeData("", parameters, paginationList);
		return null;
	}

}
