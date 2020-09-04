package com.cjit.vms.customer.action;

import java.net.URLDecoder;
import java.util.List;

import cjit.crms.util.DictionaryCodeType;

import com.cjit.common.util.StringUtil;
import com.cjit.vms.customer.model.SubCustomer;
import com.cjit.vms.customer.service.SubCustomerService;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.util.DataUtil;

/***
 * 子公司
 */
public class SubCustomerAction extends DataDealAction {

	private CustomerService customerService;
	public SubCustomerService subCustomerService;

	private SubCustomer subCustomerSearch;// 查询条件
	private SubCustomer subCustomer;
	private List countrys;
	private List dataSourceList; //客户类型

	private String[] checkedlineNo;   //利用此数组进行批量删除操作

	/**查询子公司*/	
	public String listSubCustomer() {
		try {
			if(!StringUtil.isEmpty(subCustomerSearch.getSubCustomerName())){
				String subCustomerName = subCustomerSearch.getSubCustomerName();
				subCustomerName = URLDecoder.decode(subCustomerName,"utf-8");
				subCustomerSearch.setSubCustomerName(subCustomerName);
			}			
			subCustomerService.selectSubCustomer(subCustomerSearch,paginationList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return SUCCESS;
	}

	/**创建子公司*/
	public String createSubCustomer() {
		if (StringUtil.isNotEmpty(subCustomerSearch.getSubCustomerId())) {
			List list = subCustomerService.selectSubCustomer(subCustomerSearch, null);
			if (null != list && list.size() > 0) {
				subCustomer = (SubCustomer) list.get(0);
			}
		}
		countrys = customerService.findCountry();
		dataSourceList=DataUtil.getdataSourceList();
		return SUCCESS;
	}
	
	/**修改子公司*/
	public String editSubCustomer() {
		if (StringUtil.isNotEmpty(subCustomerSearch.getSubCustomerId())) {
			List list = subCustomerService.selectSubCustomer(subCustomerSearch, null);
			if (null != list && list.size() > 0) {
				subCustomer = (SubCustomer) list.get(0);
			}
		}
		countrys = customerService.findCountry();
		dataSourceList=DataUtil.getdataSourceList();
		return SUCCESS;
	}

	/**新增*/
	public String saveSubCustomer() {
		String resultMessages = "";
		String idResult = subCustomerService.selectSubCustomerBySubCustomerId(subCustomer.getSubCustomerId());
		if(!idResult.equals("")){
			resultMessages = "此子公司ID已存在";			
		}
		
		if(!subCustomer.getSubCustomerTaxno().equals("")){
			String noResult = subCustomerService.selectSubCustomerByTaxno(subCustomer.getSubCustomerTaxno());
			if(!noResult.equals("")){
				resultMessages = "此子公司识别号已存在";
			}
		}

		if(resultMessages.equals("")){
			subCustomer.setDataSource("1");
			subCustomerService.insertSubCustomer(subCustomer);
		}else{
			setResultMessages(resultMessages);
		}
		return SUCCESS;
	}
	
	/**保存修改*/
	public String saveEditSubCustomer() {
		String resultMessages = "";
		if (StringUtil.isNotEmpty(subCustomer.getSubCustomerId())) {
			subCustomer.setDataSource("1");
			subCustomerSearch = new SubCustomer();
			if(StringUtil.isNotEmpty(subCustomer.getSubCustomerTaxno())){  //识别号不为空则校验，为空则保存				
				subCustomerSearch.setSubCustomerTaxno(subCustomer.getSubCustomerTaxno());
				List list = subCustomerService.selectSubCustomer(subCustomerSearch, null);
				if (null != list && list.size() > 0) {   //根据条件查询子公司表中是否包含此识别号的用户，包含则校验，不包含则更新
					subCustomerSearch = (SubCustomer) list.get(0);
					if(!subCustomerSearch.getSubCustomerId().equals(subCustomer.getSubCustomerId())){   //此识别号的用户是本用户则更新，否则报错
						setResultMessages("此子公司识别号已存在");
					}else{
						subCustomerService.updateSubCustomer(subCustomer);
					}
				}else{
					subCustomerService.updateSubCustomer(subCustomer);
				}			
			}else{
				subCustomerService.updateSubCustomer(subCustomer);
			}		
			
		}
		return SUCCESS;
	}

	/**删除子公司*/
	public String deleteSubCustomer() {
		for (int i = 0; i < checkedlineNo.length; i++) {
			SubCustomer sub = new SubCustomer();
			sub.setSubCustomerId(checkedlineNo[i]);
			subCustomerService.removeSubCustomer(sub);
		}
		return SUCCESS;
	}

	//校验是否重复
	public String verifySubCustomer(String subCustomerId, String subCustomerTaxno) {
		SubCustomer sub = new SubCustomer();
		sub.setSubCustomerId(subCustomerId);
		sub.setSubCustomerTaxno(subCustomerTaxno);

		List list = subCustomerService.selectSubCustomer(sub, null);
		if (null != list && list.size() > 0) {
			return "地址别名已存在";
		}

		return null;
	}

	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public SubCustomerService getSubCustomerService() {
		return subCustomerService;
	}

	public void setSubCustomerService(SubCustomerService subCustomerService) {
		this.subCustomerService = subCustomerService;
	}

	public SubCustomer getSubCustomerSearch() {
		return subCustomerSearch;
	}

	public void setSubCustomerSearch(SubCustomer subCustomerSearch) {
		this.subCustomerSearch = subCustomerSearch;
	}
	
	public SubCustomer getSubCustomer() {
		return subCustomer;
	}

	public void setSubCustomer(SubCustomer subCustomer) {
		this.subCustomer = subCustomer;
	}
	
	public List getCountrys() {
		return countrys;
	}

	public void setCountrys(List countrys) {
		this.countrys = countrys;
	}

	public List getDataSourceList() {
		return dataSourceList;
	}

	public void setDataSourceList(List dataSourceList) {
		this.dataSourceList = dataSourceList;
	}
	public String[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(String[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}

	/**
	 * get taxpayer type name by type
	 * */
	public String getTaxpayerTypeName(String TaxpayerType){
		if("".equals(TaxpayerType) || null == TaxpayerType){
			return "";
		}else if("G".equals(TaxpayerType)){
			return "一般纳税人";
		}else if("S".equals(TaxpayerType)){
			return "小规模纳税人";
		}else{
			return "其他";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getFapiaoTypeName(String getFapiaoType){
		if("".equals(getFapiaoType) || null == getFapiaoType){
			return "";
		}else if("0".equals(getFapiaoType)){
			return "增值税专用发票";
		}else if("1".equals(getFapiaoType)){
			return "增值税普通发票";
		}else{
			return "其他";
		}
	}//发票类型
	  //0-增值税专用发票
	  //1-增值税普通发票
	public String getFapiaoTypeCode(String getFapiaoType){
		if("".equals(getFapiaoType) || null == getFapiaoType){
			return "";
		}else if("增值税专用发票".equals(getFapiaoType)){
			return "0";
		}else if("增值税普通发票".equals(getFapiaoType)){
			return "1";
		}else{
			return "";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getCustomerTypeName(String CustomerType){
		if("".equals(CustomerType) || null == CustomerType){
			return "";
		}else if("I".equals(CustomerType)){
			return "私人客户";
  
		}else if("C".equals(CustomerType)){
			return "公司客户";
		}else{
			return "其他";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getCustomerFapiaoFlagName(String CustomerFapiaoFlag){
		if("".equals(CustomerFapiaoFlag) || null == CustomerFapiaoFlag){
			return "";
		}else if("A".equals(CustomerFapiaoFlag)){
			return "自动打印";
			
		}else if("M".equals(CustomerFapiaoFlag)){
			return "手动打印";
		}else if("N".equals(CustomerFapiaoFlag)){
			return "永不打印";
		}else{
			return "其他";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getTaxpayerTypeCode(String TaxpayerType){
		if("".equals(TaxpayerType) || null == TaxpayerType){
			return "";
		}else if("一般纳税人".equals(TaxpayerType)){
			return "G";
		}else if("小规模纳税人".equals(TaxpayerType)){
			return "S";
		}else if("个体模纳税人".equals(TaxpayerType)){
			return "I";
		}else if("其他".equals(TaxpayerType)){
			return "O";
		}else{
			return "";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getCustomerTypeCode(String CustomerType){
		if("".equals(CustomerType) || null == CustomerType){
			return "";
		}else if("私人客户".equals(CustomerType)){
			return "I";
			
		}else if("公司客户".equals(CustomerType)){
			return "C";
		}else{
			return "";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getCustomerFapiaoFlagCode(String CustomerFapiaoFlag){
		if("".equals(CustomerFapiaoFlag) || null == CustomerFapiaoFlag){
			return "";
		}else if("自动打印".equals(CustomerFapiaoFlag)){
			return "A";
			
		}else if("手动打印".equals(CustomerFapiaoFlag)){
			return "M";
		}else if("永不打印".equals(CustomerFapiaoFlag)){
			return "N";
		}else{
			return "";
		}
	}
}
