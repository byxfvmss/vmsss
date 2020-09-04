package com.cjit.vms.trans.service.storage;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.storage.InvoiceAlertListInfo;

public interface InvoiceAlertService {

	public List findInvoiceAlertList(InvoiceAlertListInfo info,PaginationList paginationList); 
	
	public InvoiceAlertListInfo findInstInvoiceAlert(String inst_id,String invoice_type);
	
	public void saveInstInvoiceAlertValue(String inst_id,String invoice_type,Integer alert_num);
}
