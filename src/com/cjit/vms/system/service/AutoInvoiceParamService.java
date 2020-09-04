package com.cjit.vms.system.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.AutoInvoiceParam;

public interface AutoInvoiceParamService {

	public List findAutoInvoiceParamList(AutoInvoiceParam aip, PaginationList paginationList);

	public AutoInvoiceParam findAutoInvoiceParam(AutoInvoiceParam aip);

	public void saveAutoInvoiceParam(AutoInvoiceParam aip, boolean isUpdate);
	
	public void deleteAutoInvoiceParam(String paramId);
}
