package com.cjit.vms.customer.service;

import java.util.List;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.customer.model.SubCustomer;

public interface SubCustomerService {
	public List selectSubCustomer(SubCustomer subCustomer,PaginationList paginationList);
	public void updateSubCustomer(SubCustomer subCustomer);
	public void removeSubCustomer(SubCustomer subCustomer);
	public void insertSubCustomer(SubCustomer subCustomer);
	public String selectSubCustomerBySubCustomerId(String subCustomerId);
	public String selectSubCustomerByTaxno(String taxno);
	
}
