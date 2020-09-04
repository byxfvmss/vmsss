package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.model.TransInfo;

public interface NegativeIncomeService {

	public List findTransInfoForNegativeIncome(TransInfo transInfo, PaginationList paginationList);

	public List findTransInfoForNegativeIncomeExport(TransInfo transInfo);

	public List findTransInfoForNegativeIncomeDetail(TransInfo transInfo, PaginationList paginationList);

	public void offsetTrans(TransInfo transInfo);

	public void saveTransNegaInfo(TransInfo transInfo);
	
	public List findAllCustomer();

	public Customer findCustomerByTaxno(String customerTaxno);

	public void cancelNegativeIncome(TransInfo transInfo);

}
