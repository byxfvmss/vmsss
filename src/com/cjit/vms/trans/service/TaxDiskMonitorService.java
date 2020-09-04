package com.cjit.vms.trans.service;

import java.math.BigDecimal;
import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.TaxDiskMonitorInfo;

public interface TaxDiskMonitorService {
	public List findTaxDiskMonitorListInfo(TaxDiskMonitorInfo info,PaginationList paginationList);
	
	public void saveTaxDiskMonitor(TaxDiskMonitorInfo info);
	
	public void updateTaxDiskMonitor(TaxDiskMonitorInfo info);
	
	public void deleteTaxDiskMonitor(String[] tax_disk_nos);
	
	public TaxDiskMonitorInfo findTaxDiskMonitorItemInfo(String taxDiskNo,String fapiaoType);
	
	public BigDecimal getMinBillLimitAmtFromTaxDisk();
		
	public List getInstInfoList(InstInfo info,PaginationList paginationList);
	
	public List getInstInfoTaxNoList(InstInfo info,PaginationList paginationList);
}
