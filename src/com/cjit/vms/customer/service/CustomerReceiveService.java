package com.cjit.vms.customer.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.customer.model.CustomerAddress;
import com.cjit.vms.customer.model.CustomerReceiver;

public interface CustomerReceiveService {
	public List selectCustomerReceiver(CustomerReceiver customerReceiver,PaginationList paginationList);
	public void updateCustomerReceiver(CustomerReceiver customerReceiver);
	public void removeCustomerReceiver(CustomerReceiver customerReceiver);
	public void insertCustomerReceiver(CustomerReceiver customerReceiver);
	
	public List selectCustomerAddress(CustomerAddress customerAddress,PaginationList paginationList);
	public void updateCustomerAddress(CustomerAddress customerAddress);
	public void removeCustomerAddress(CustomerAddress customerAddress);
	public void insertCustomerAddress(CustomerAddress customerAddress);
	
}
