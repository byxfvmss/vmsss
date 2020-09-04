package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.vms.trans.model.Currency;
import com.cjit.vms.trans.model.ExchangeRate;
import com.cjit.vms.trans.service.ExchangeRateService;

public class ExchangeRateServiceImpl extends GenericServiceImpl implements ExchangeRateService {

	public void deleteExchangeRate(String[] exchangeRateIds) {
		Map map = new HashMap();
		if (exchangeRateIds.length == 1) {
			map.put("exchangeRateId", exchangeRateIds[0]);
		} else {
			map.put("exchangeRateIds", exchangeRateIds);
		}
		this.delete("deleteExchangeRate", map);
	}

	public List findExchangeRate(ExchangeRate exchangeRate, String method) {
		Map map = new HashMap();
		if (method != null && "edit".equals(method)) {
			map.put("editExchangeRateId", exchangeRate.getExchangeRateId() + "");
			exchangeRate.setExchangeRateId(0);
		}
		map.put("exchangeRate", exchangeRate);
		return this.find("findExchangeRate", map);
	}

	public ExchangeRate findExchangeRateById(int exchangeRateId) {
		ExchangeRate exchangeRate = new ExchangeRate();
		exchangeRate.setExchangeRateId(exchangeRateId);
		Map map = new HashMap();
		map.put("exchangeRate", exchangeRate);
		List list = this.find("findExchangeRate", map);
		if (list != null && list.size() == 1) {
			return (ExchangeRate)list.get(0);
		}
		return null;
	}

	public List findExchangeRateList(ExchangeRate exchangeRate,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("exchangeRate", exchangeRate);
		
		return this.find("findExchangeRate", map, paginationList);
	}

	public void insertExchangeRate(ExchangeRate exchangeRate) {
		Map map = new HashMap();
		map.put("exchangeRate", exchangeRate);
		this.save("insertExchangeRate", map);
	}

	public void updateExchangeRate(ExchangeRate exchangeRate) {
		Map map = new HashMap();
		map.put("exchangeRate", exchangeRate);
		this.update("updateExchangeRate", map);
	}

	public List findAllCurrency() {
		List currencyList = new ArrayList();
		
		List list = find("findAllCurrency", new HashMap());
		
		for (int i=0; i<list.size(); i++) {
			Currency currency = (Currency) list.get(i);
			SelectTag st = new SelectTag(currency.getCurrency(), currency.getCurrency() + " - " + currency.getCurrencyName());
			currencyList.add(st);
		}
		return currencyList;
	}

}
