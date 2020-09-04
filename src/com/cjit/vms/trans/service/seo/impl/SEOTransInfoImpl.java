package com.cjit.vms.trans.service.seo.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.service.seo.SEOTransInfoService;

public class SEOTransInfoImpl extends GenericServiceImpl implements
		SEOTransInfoService {

	/**
	 * 交易查询
	 */
	@Override
	public List findSEOInfo() {

		Map param = new HashMap();

		return find("findSEOInfo", param);
	}

}
