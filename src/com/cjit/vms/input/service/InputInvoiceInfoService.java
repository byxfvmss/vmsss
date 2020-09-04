package com.cjit.vms.input.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceNew;

public interface InputInvoiceInfoService {
	
	/**
	 * 列表页面
	 * 
	 * @param InputInvoiceInfo
	 * @param paginationList
	 * @return
	 */
	public List getInputInvoiceInfoList(InputInvoiceNew info, PaginationList paginationList);
	/**
	 * 取得详细信息
	 * 
	 */
	public InputInvoiceInfo getInputInvoiceInfoDetail(String billId);
	/**
	 * 取得商品列表
	 * 
	 */
	public List getInputInvoiceItemList(String billId, PaginationList paginationList);
	/**
	 * 取得交易列表
	 * 
	 */
	public List getInputInvoiceTransList(String billId, PaginationList paginationList);
	
	public List getInputInvoiceInfoListNew(InputInvoiceInfo info, PaginationList paginationList);
	
	//获取行业类型
	public List findIndustryTypeList();
}
