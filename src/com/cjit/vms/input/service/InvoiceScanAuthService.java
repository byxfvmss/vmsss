package com.cjit.vms.input.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceItem;
import com.cjit.vms.input.model.InputTransInfo;
import com.cjit.vms.trans.model.UBaseInst;

public interface InvoiceScanAuthService {
	/**
	 * 票据查询
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 */
	public List findListInvoiceScanAuth(InputInvoiceInfo info,	PaginationList paginationList);
	
	/**
	 * 获取票据信息
	 * 
	 * @param billId
	 * @return
	 */
	public InputInvoiceInfo findInvoiceScanAuthByBillId(String billId); 
	
	/**
	 * 获取商品列表
	 * 
	 * @param billId
	 * @return
	 */
	public List findInvoiceScanAuthItemsByBillId(String billId);

	/**
	 * 获取票据交易数据
	 * 
	 * @param billId
	 * @return
	 */
	public InputTransInfo findInvoiceScanAuthTransInfoByBillId(String bill_code,String bill_no);

//	public void updateVmsInputInvoiceInfoForScanAuth(InputInvoiceInfo inputInvoiceInfo, List lstGoodAdded,String o_bill_id); 
	public void updateVmsInputInvoiceInfoForScanAuth(InputInvoiceInfo inputInvoiceInfo, String o_bill_id); 
	
	public void updateVmsInputInvoiceInfoForAuthSubmit(String datastatus, String[] billIds);
	public void saveVmsInputInvoiceInfoImport(List dataList);
	
	
	/**
	 * 新增商品信息
	 * @param item
	 */
	public void insertVmsInputInvoiceItem(InputInvoiceItem item);
	/**
	 * 编辑更新商品信息
	 * @param item 
	 */
	public void updateVmsInputInvoiceItem(InputInvoiceItem item);
	/**
	 * 删除商品信息
	 * @param item
	 */
	public void deleteVmsInputInvoiceItem(InputInvoiceItem item);
	/**
	 * 获取新增商品信息ID
	 * 
	 * @return
	 */
	public String findSequenceBillItemId();

	public InputInvoiceInfo findInvoiceScanAuthByBillCodeAndBillNo(
			String billCode, String billNo); 
	public UBaseInst findUbaseInstByTaxNo(String taxNo);
	
	public void saveInputinvoiceData(Map<String,String> map);
	public void saveInputinvoiceInfo(Map<String,String> map);
	public List<String> findinputInvoiceCompareinvoiceData(Map<String,String> map);
	
	public void updateInputInvoiceYconformFlg(Map<String,String> map);
	 
	
}
