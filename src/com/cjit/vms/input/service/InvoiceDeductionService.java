package com.cjit.vms.input.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.input.model.InputTransInfo;

public interface InvoiceDeductionService {

	public List findInvoiceDeductionList(InputInvoiceInfo info,	PaginationList paginationList);

	public void saveRollbackInvoiceDeduction(String[] billId);

	public InputInvoiceInfo findInvoiceDeductionByBillId(String o_bill_id);

	public InputTransInfo findInvoiceDeductionTransInfoByBillCodeAndBillNo(String bill_code, String bill_no);

	public List findInvoiceDeductionItemsByBillId(String o_bill_id);

	public void updateVmsInputInvoiceInfoForDeduction(InputInvoiceInfo inputInvoiceInfo, String o_bill_id);
	
	public List findInvoiceDeductionListNew(InputInvoiceNew info,	PaginationList paginationList);
	
	public InputInvoiceNew findInputTransByIdCodeNoAndNo(String billNoAndBillCode);
	
	public void updateInvoiceInfoForDeductionNew(Map idMap);
	
	public void saveInputInvoice(Map<String, List> map) ;

	//获取行业类型
	public List findIndustryTypeList();
}
