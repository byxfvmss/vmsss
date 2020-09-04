package com.cjit.vms.trans.service.impl;

import com.cjit.common.util.PaginationList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;

import com.cjit.vms.trans.model.TaxDiffCheckAccountInfo;
import com.cjit.vms.trans.service.TaxDiffCheckAccountService;


public class TaxDiffCheckAccountServiceImpl extends GenericServiceImpl implements TaxDiffCheckAccountService{

	@Override
	public List findTaxDiffCheckAccountService(Map map,
			PaginationList paginationList) {
		return find("findTaxDiffCheckAccount",map,paginationList);
		
		
	}

	@Override
	public List findlistForeignCurrencyTransDiff(Map map,
			PaginationList paginationList) {
		
		return find("findlistForeignCurrencyTransDiff",map,paginationList);
	}




}
