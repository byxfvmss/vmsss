package com.cjit.vms.system.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.Customer;

public interface CustomerService {

	public List findCustomerList(Customer customer);
	public List findCustomerList(Customer customer,PaginationList paginationList);
	public Customer findCustomer(String id, String taxNo, String account, String custCode, String custOffice);
	/**
	 * @param id 根据 客户id 找到客户表的客户信息
	 * @return
	 */
	public Customer findCustomer(String id);
	public List findCountry();
	public Customer findCustomerByTaxNo(String texNo);
	public void deleteCustomer(String custId, String custCode);
	public void updateCustomer(Customer customer);
	public void saveCustomer(Customer customer);
	public void deleteAll(List list);
	public List findTransByCustomerId(String customerId);
	public List findTransByCustomers(String customerName);
	
	public String findCountryByCode(String code);
	
	/**
	 * @param customer 将客户信息复制到 客户临时表里
	 */
	public void copyCustomerToCustommerTemp(Customer customer);
	public List findCustomerAudit(Customer customer,PaginationList paginationList);
	public Customer findCustomerAuditById(String id);
	public void updatecustomerAuditstatus(List customerIdList,String dataAuditStatus);
	/**
	 * @param customer 审核 新增成功
	 */
	public  void copycustomerTempToCustomer(List list);
	
	/**
	 * @param list 审核 修改通过
	 */
	public void updateCustomerAtferAudit(List list);
	public void deleteCustomerTempAfterAudit(List list);
	
	public  String findCustomerTempByTaxNo(String taxNo);
	/**
	 * @param customer 存到客户正式表
	 */
	public void saveCustomerInfo(Map map);
	/**
	 * @param customer 存进客户标准表里
	 */
	public void saveCustomerData(Map map);
	
	public void deleteCustomerByTaxNo(String taxNo);
	/**
	 * @param customer 发票快递专用
	 */
	public void updateCutomerbyTaxNo(Customer customer);

	/**
	 * 根据客户id列表查询客户
	 * @param customerIds
	 * @return
	 */
	public List findCustomerByIds(List customerIds);
	
	/**
	 * 根据客户ID查询客户
	 * @return
	 */
	public List findCustomerById(String customerId);
}
