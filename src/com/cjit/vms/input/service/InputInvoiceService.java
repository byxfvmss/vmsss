package com.cjit.vms.input.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.InformationBills;
import com.cjit.vms.input.model.InputInvoice;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputVatInfo;
import com.cjit.vms.input.model.InformationInput;

public interface InputInvoiceService {

	public List findInputInvoiceList(InputInvoice inputInvoice,
			PaginationList paginationList);
	public InputInvoice findInputInvoiceDetail(String id);
	public List findInputItemList(String id);

	public InputVatInfo findInputInvoiceInfo(String inVatId);

	public void saveInputInvoiceInfo(InputInvoice inputInvoice);

	public void deleteInputInvoiceInfo(String inVatId, String billCode,
			String billNo);
	
	public List findBussTypeList();
	
	public void importInputInvoice(List dataList);
	
	public BigDecimal findDeductedAlart();
	public List findInputInformation(InformationInput informationInput,
			PaginationList paginationList);
 
	
	public InformationInput findInputInformationById(String id);
	
	public List findInputBillsQuery(InformationBills informationBills,
			PaginationList paginationList);
	
	public InformationBills findInputBillsById(String billCode);
	
	public InputInvoiceInfo findInputInformationViewImg(String dealNo);
	
	public List findInputBillsByTrans(String billCode,String billNo);
	public List findInputBillsByTrans(String id);
	
	public List findInputInvoiceItemByBill(String billId);
 
	//xhy
	public List findInputInvoiceInfoList(Map map,PaginationList paginationList);         
	public List redReceiptDetail(Map map);
	public List findItemInfo(Map map);
	public List findInvoiceInfoList(Map map);
	public void updateInputInvoiceInfo(InputInvoiceInfo inputInvoiceInfo);
	public List listInputTransItem(Map map);
}
