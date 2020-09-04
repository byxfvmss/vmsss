package com.cjit.vms.trans.service.redRecipt;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.createBill.TransInfo;

public interface RedBillByTransService {
	public List selectReverseTrans(TransInfo transInfo,PaginationList paginationList);
}
