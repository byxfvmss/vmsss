package com.cjit.vms.trans.service;

import java.math.BigDecimal;
import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.InputInvoiceInfo;

public interface InvoiceSurtaxService {
	
	public List findInvoiceInSurtaxList(InputInvoiceInfo info,
			PaginationList paginationList);

	public InputInvoiceInfo findVmsInputInvoiceInfoByBillId(String bill_id);

	public List findVmsInputInvoiceItemsByBillId(String bill_id);
	
	/**
	 * 更新进项发票转出
	 * @param vat_out_amt
	 * @param vat_out_proportion
	 */
	public void updateVmsInputInvoiceInfoVatOut(String vat_out_amt,String vat_out_proportion,String remark,String bill_id);
	
	/**
	 * 转出提交
	 * @param bill_id
	 * @param datastatus
	 */
	public void updateVmsInputInvoiceInfoDatastatus(String[] bill_id,String datastatus);
	
	/**
	 * 撤回数据
	 * @param billIds
	 */
	public void updateVmsInputInvoiceInfoForRollBack(String[] billIds) ;
	/**
	 * 批量转出
	 * @param billIds
	 */
	public void updateVmsInputInvoiceInfoForBatchRollOut(String[] billIds);

}
