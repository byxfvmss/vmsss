package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.billInvalid.BillCancelInfo;


public interface BillSupplementPrintService {

	public List findBillSupplementPrintInfoList(BillCancelInfo billCancelInfo,
			String id, PaginationList paginationList);

	public List findBillSupplementPrintQuery(BillCancelInfo billCancelInfo);

}
