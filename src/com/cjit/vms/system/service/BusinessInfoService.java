package com.cjit.vms.system.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.BusinessInfo;

public interface BusinessInfoService {

	public List findBusinessInfoList(BusinessInfo business,PaginationList paginationList);
	
	public List findBusinessInfo(BusinessInfo business);
	
	public void insertBusinessInfo(BusinessInfo business);
	
	public void updateBusinessInfo(BusinessInfo business);
	
	public void deleteBusinessInfo(String businessId);
}
