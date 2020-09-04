package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.ExchangeRate;

public interface ExchangeRateService {

	public List findExchangeRateList(ExchangeRate exchangeRate,	PaginationList paginationList);
	
	public List findExchangeRate(ExchangeRate exchangeRate, String method);

	public ExchangeRate findExchangeRateById(int exchangeRateId);

	public void insertExchangeRate(ExchangeRate exchangeRate);

	public void updateExchangeRate(ExchangeRate exchangeRate);

	public void deleteExchangeRate(String[] selectExchangeRateId);

	public List findAllCurrency();

}
