package com.cjit.vms.input.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputTrans;
import com.cjit.vms.trans.model.BillItemInfo;

public interface InputInnovationService {
	public List findInnovationList(InputInvoiceInfo inputInvoiceInfo,PaginationList paginationList);
	public List findInnovationList(InputInvoiceInfo inputInvoiceInfo);
	public InputInvoiceInfo findInputInvoiceInfo(InputInvoiceInfo inputInvoiceInfo);
	public List findInputInvoiceList(String billId,String vendId);
	public List findBillItemInfoList(BillItemInfo billItemInfo);
	/**
	 * @param billId
	 * @param vendId
	 * @return 未购机列表
	 */
	public List findInputNOInvoiceList(String billId,String vendId,String dealNo,String bankCode,PaginationList paginationList);
	
	/**
	 * @param billId 
	 * @param dealNo 撤销钩稽
	 */
	public void deleteInputInvoice(String billId,String dealNo);
	public void insertInputInvoice(String billId,String dealNo);
	public InputTrans findInputTransById (String billId,String dealNo);
	public void updateInputTrans(String billId,String status);
}
