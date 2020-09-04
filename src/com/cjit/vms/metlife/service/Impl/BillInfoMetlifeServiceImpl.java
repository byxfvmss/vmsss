package com.cjit.vms.metlife.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.metlife.service.BillInfoMetlifeService;
import com.cjit.vms.trans.model.BillInfo;

public class BillInfoMetlifeServiceImpl extends GenericServiceImpl implements BillInfoMetlifeService{

	@Override
	public List findTransInfoByBillInfo(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		return this.find("findTransInfoByBillInfo", map);
	}

}
