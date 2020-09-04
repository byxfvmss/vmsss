package com.cjit.vms.trans.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.TaxDiffCheckAccountInfo;

public interface TaxDiffCheckAccountService {
	public List findTaxDiffCheckAccountService(Map map,
			PaginationList paginationList);
	public List findlistForeignCurrencyTransDiff(Map map,
			PaginationList paginationList);
}
