package com.cjit.vms.input.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.EditSerial;
import com.cjit.vms.input.model.InputTrans;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.system.model.Customer;

public interface InputTransService {
	public List findBussTypeList();

	public List inputTransList(InputTrans inputTrans,
			PaginationList paginationList);

	public InputTrans findInputTransById(String id, List transKindList,
			List transTypeList);
	public InputTrans findInputTransByIdCodeNo(String id, String code,
			String no);

	public void saveInputTrans(InputTrans inputTrans);

	public void importInputInvoice(List dataList);
	
	public List selectInvoiceInfo(Map parameters);
	
	public void deleteInputTrans(String ids);
	
	public List inputTransListExport(InputTrans inputTrans,
			PaginationList paginationList, String sql);
	/**
	 * @param map  根据 交易编号 批次号找到对应的 交易标准接口表的信息
	 * @return string
	 */
	public List<String> findInputTransDataByDealNoAndBatchNo(Map<String,String> map);
	public List<String> findInputTransDataComparebillNo(Map<String,String> map);
	public void saveInputTransData(Map<String,String> map);
	public void saveInputInvoiceNewData(Map<String,String> map);
	public void saveInputTransInfo(Map<String,String> map);
	public void saveInputTransCopyData(Map<String,String> map);
	/**
	 * @param map 存中间表
	 */
	public void saveTransInvoice(Map<String,String> map);
	public List<InputTrans> findbillcodeInInputInvoice(Map<String,String> map);
	
	//20160512 中科软添加
	//获取行业类型
	public List findIndustryTypeList();
	
	public List inputInvoiceNewList(InputInvoiceNew inputInvoiceNew,
			PaginationList paginationList);
	
	public List<String> findInputInvoiceNewDataByDealNoAndDealCode(Map<String,String> map);   //判断发票代码、号码是否重复
	public List<String> findInputInstByInstCode(Map<String,String> map);   //获取机构信息
	
	public List<String> findInvoiceNewByCodeNo(String billCode,String billNo);
	
	public void saveInvoiceNew(InputInvoiceNew inputInvoiceNew);
	
	public void deleteInputTransNew(Map finby);
	
	/**李松加
	 * @param id
	 * @param transKindList
	 * @param transTypeList
	 * @return
	 */
	public InputInvoiceNew findInputTransByBillNoAndCode(String billNoAndCode);
	
	public void saveInputTransNew(InputInvoiceNew inputInvoiceNew);
	
	public List<String> findDup(Map<String,String> map);
	
	public void saveInputTransEdit(InputInvoiceNew inputInvoiceNew,String userId);
	
	public  List<String> findInputTransEditById(String billNoAndCode,PaginationList paginationList);
}