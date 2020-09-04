package com.cjit.vms.customer.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.customer.model.CustomerAddress;
import com.cjit.vms.customer.model.CustomerReceiver;
import com.cjit.vms.customer.service.CustomerReceiveService;

public class CustomerReceiveServiceImpl extends GenericServiceImpl implements
		CustomerReceiveService {

	@Override
	public List selectCustomerReceiver(CustomerReceiver customerReceiver,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("customerReceiver", customerReceiver);
		if (null!=paginationList) {
			find("selectCustomerReceiver", map,paginationList);
		}
		return find("selectCustomerReceiver", map);
	}

	@Override
	public void updateCustomerReceiver(CustomerReceiver customerReceiver) {
		Map map = new HashMap();
		map.put("customerReceiver", customerReceiver);
		update("updateCustomerReceiver", map);
	}

	@Override
	public void removeCustomerReceiver(CustomerReceiver customerReceiver) {
		Map map = new HashMap();
		map.put("customerReceiver", customerReceiver);
		delete("deleteCustomerReceiver", map);
	}

	@Override
	public void insertCustomerReceiver(CustomerReceiver customerReceiver) {
		Map map = new HashMap();
		map.put("customerReceiver", customerReceiver);
		save("insertCustomerReceiver", map);
	}

	@Override
	public List selectCustomerAddress(CustomerAddress customerAddress,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("customerAddress", customerAddress);
		if (null!=paginationList) {
			find("selectCustomerAddress", map,paginationList);
		}
		return find("selectCustomerAddress", map);
	}

	@Override
	public void updateCustomerAddress(CustomerAddress customerAddress) {
		Map map = new HashMap();
		map.put("customerAddress", customerAddress);
		update("updateCustomerAddress", map);
	}

	@Override
	public void removeCustomerAddress(CustomerAddress customerAddress) {
		Map map = new HashMap();
		map.put("customerAddress", customerAddress);
		delete("deleteCustomerAddress", map);
	}

	@Override
	public void insertCustomerAddress(CustomerAddress customerAddress) {
		Map map = new HashMap();
		map.put("customerAddress", customerAddress);
		save("insertCustomerAddress", map);
	}

}
