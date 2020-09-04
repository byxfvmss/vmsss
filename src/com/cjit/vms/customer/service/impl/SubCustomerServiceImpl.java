package com.cjit.vms.customer.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.customer.model.CustomerAddress;
import com.cjit.vms.customer.model.CustomerReceiver;
import com.cjit.vms.customer.model.SubCustomer;
import com.cjit.vms.customer.service.CustomerReceiveService;
import com.cjit.vms.customer.service.SubCustomerService;
import com.cjit.vms.system.model.Customer;

public class SubCustomerServiceImpl extends GenericServiceImpl implements SubCustomerService{

	@Override
	public List selectSubCustomer(SubCustomer subCustomer,PaginationList paginationList) {
		Map map = new HashMap();
		map.put("subCustomer", subCustomer);
		if (null!=paginationList) {
			find("selectSubCustomer", map,paginationList);
		}
		return find("selectSubCustomer", map);
	}

	@Override
	public void updateSubCustomer(SubCustomer subCustomer) {
		Map map = new HashMap();
		map.put("subCustomer", subCustomer);
		update("updateSubCustomer", map);
	}

	@Override
	public void removeSubCustomer(SubCustomer subCustomer) {
		Map map = new HashMap();
		map.put("subCustomer", subCustomer);
		delete("deleteSubCustomer", map);
	}

	@Override
	public void insertSubCustomer(SubCustomer subCustomer) {
		Map map = new HashMap();
		map.put("subCustomer", subCustomer);
		save("insertSubCustomer", map);
	}
	@Override
	public String selectSubCustomerBySubCustomerId(String subCustomerId){
		Map map=new HashMap();		
		SubCustomer subCustomer=new SubCustomer();
		subCustomer.setSubCustomerId(subCustomerId);
		map.put("subCustomer", subCustomer);
		List list= find("selectSubCustomerBySubCustomerId", map);
		String result = "";
		if(list!=null && list.size()>0){
			result="1";
		}

		return result;
	};
	
	@Override
	public String selectSubCustomerByTaxno(String taxno){
		Map map=new HashMap();		
		SubCustomer subCustomer=new SubCustomer();
		subCustomer.setSubCustomerTaxno(taxno);
		map.put("subCustomer", subCustomer);
		List list= find("selectSubCustomerByTaxno", map);
		String result = "";
		if(list!=null && list.size()>0){
			result="1";
		}

		return result;
	};
	
	
	
	
}
