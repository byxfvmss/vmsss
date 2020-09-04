package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.trans.util.DataUtil;

public class CustomerServiceImpl extends GenericServiceImpl implements
		CustomerService {

	public List findCustomerList(Customer customer) {
		Map map = new HashMap();
		map.put("customer", customer);
		return find("findCustomer", map);
	}

	public Customer findCustomer(String id, String taxNo, String account,
			String custCode, String custOffice) {
		Customer customer = new Customer();
		
		customer.setCustomerID(id.toLowerCase());
		customer.setCustomerTaxno(taxNo);
		customer.setCustomerAccount(account);

		Map map = new HashMap();
		map.put("customer", customer);
		List list = this.find("findFirstCustomer", map);
		if(null!=list&&list.size()>0){
			customer = (Customer) list.get(0);
			return customer;
		}else{
			return null;
		}
	}	
	public Customer findCustomer(String id) {
		Customer customer = new Customer();
		customer.setCustomerID(id);
		 Map map=new HashMap();
		map.put("customer", customer);
		List list = find("findCustomerByCode", map);
			customer = new Customer();
		if (list != null && list.size() > 0) {
			 customer=(Customer) list.get(0);
		} else {
			customer = null;
		}
		return customer;
	}


	public void deleteCustomer(String custId, String custCode) {
		Map params = new HashMap();
		params.put("customerId", custId);
		params.put("customerCode", custCode);
		this.delete("deleteCustomer", params);
	}

	public List findCustomerList(Customer customer,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("customer", customer);
		return find("findCustomer", map,paginationList);
	}
	
	
	public List findCountry() {
		return find("findCountry", new HashMap());
	}

	public void saveCustomer(Customer customer){
		Map map=new HashMap();
		map.put("customer", customer);
		this.save("saveCustomer", map);
	}
	public void updateCustomer(Customer customer){
		Map map=new HashMap();
		map.put("customer", customer);
		this.save("updateCustomer", map);
	}

	public void deleteAll(List list) {
		// TODO Auto-generated method stub
		if(list.size()>0){
			Map map=new HashMap();
			map.put("customerIDs",list);
				this.delete("deleteAllCustomer", map);
			}
		
		
		
	}

	public Customer findCustomerByTaxNo(String taxNo) {
		Map map=new HashMap();
		
		Customer customer=new Customer();
		customer.setCustomerTaxno(taxNo);
		map.put("customer", customer);
		List list= find("findCustomerByTaxno", map);
		 customer=new Customer();
		
		if(list!=null &&list.size()>0){
			customer=(Customer)list.get(0);
		}
		return customer;
	}

	public List findTransByCustomerId(String customerId) {
		Map map=new HashMap();
		map.put("customerId", customerId);
		return find("findTransByCustomerId", map);
	}
	
	public List findTransByCustomers(String customerName) {
		Map map=new HashMap();
		map.put("customerName", customerName);
		return find("findTransByCustomerIds", map);
	}

	public String findCountryByCode(String code) {
		Map map=new HashMap();
		map.put("code", code);
		List list=find("findcountryBycode",map);
		String country="";
		if(list!=null && list.size()>0){
			country=(String) list.get(0);
		}
		return country;
	}

	public void copyCustomerToCustommerTemp(Customer customer) {

		Map map=new HashMap();
		map.put("customer", customer);
		save("copyCustomerToCustomerTemp", map);
	}

	public List findCustomerAudit(Customer customer,PaginationList paginationList) {
		Map map=new HashMap();
		map.put("customer", customer);
		List list=find("findCustomerAudit", map, paginationList);
		return list;
	}

	public Customer findCustomerAuditById(String id) {
		Map map=new HashMap();
		// TODO Auto-generated method stub
		map.put("id", id);
		List list=find("findCustomerAuditBycode", map);
		Customer customer =null;
		if(list.size()>0 && list.size()==1){
			customer=(Customer)list.get(0);
		}
		return customer;
	}

	public void updatecustomerAuditstatus(List customerIdList,
			String dataAuditStatus) {
		// TODO Auto-generated method stub
	Map map=new HashMap();
	map.put("customerIdList", customerIdList);
	map.put("dataAuditStatus", dataAuditStatus);
	if(customerIdList.size()>0 && StringUtil.isNotEmpty(dataAuditStatus)){
		update("updatecustomerAuditstatus", map);
	}
	}

	public void copycustomerTempToCustomer(List list) {
		// TODO Auto-generated method stub
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Customer customer=new Customer();
				customer.setCustomerID((String) list.get(i));
				Map map =new HashMap();
				map.put("customer", customer);
				if(StringUtil.isNotEmpty(customer.getCustomerID())){
					save("copycustomertempToCustomer", map);
				}
		}
		
	}
	}

	public void updateCustomerAtferAudit(List list) {
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Customer customer =new Customer();
				customer.setCustomerID((String)list.get(i));
				Map map=new HashMap();
				map.put("customer", customer);
				update("updateCustomerAtferAudit", map);
			}
		}
	}

	public void deleteCustomerTempAfterAudit(List list) {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		map.put("customerIds", list);
		if(list.size()>0){
			delete("deleteCustomerTemp", map);
		}
		
	}

	public String findCustomerTempByTaxNo(String taxNo) {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		map.put("taxNo", taxNo);
		List list=find("findCustomerTempByTaxno", map);
		String result="";
		if(list.size()>0&&list!=null){
			result=(String)list.get(0);
		}
		return result;
		
	}

	public void saveCustomerData(Map map) {
		this.save("saveCustomerData", map);
	}

	public void saveCustomerInfo(Map map) {
		
		this.save("saveCustomerInfo", map);
	}

	public void deleteCustomerByTaxNo(String taxNo) {
		Map map=new HashMap();
		map.put("taxNo", taxNo);
		delete("deletecustomerByTaxNo", map);
	}

	

	public void updateCutomerbyTaxNo(Customer customer) {
			Map map=new HashMap();
			map.put("customer", customer);
			this.update("updateCustomerByTaxNo", map);
	}

	@Override
	public List findCustomerByIds(List customerIds) {
		Map map = new HashMap();
		map.put("customerIds", customerIds);
		return this.find("findCustomerByIds", map);
	}

	@Override
	public List findCustomerById(String customerId) {
		Map map = new HashMap();
		map.put("customerId", customerId);
		return find("findCustomerById", map);
	}
}
