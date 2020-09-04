package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.IntegrityCheckAccount;

public interface IntegrityCheckAccountService {
	
	public List getCheckAccountList(IntegrityCheckAccount account,PaginationList paginationList);
	
	
	public List getCheckGoodsInfoList(String instCode,PaginationList paginationList);
	
	
	public List getCheckTransInfoList(IntegrityCheckAccount account,PaginationList paginationList);


}
