package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;

public interface TaxDiskPasswdService {

	public List findTaxDiskPasswdInfoList(TaxDiskInfo info,	PaginationList paginationList);

	public void deleteTaxDiskPasswd(String[] checked_tax_disk_no);

	public void saveTaxDiskInfo(TaxDiskInfo info);

	public void updateTaxDiskInfo(TaxDiskInfo info);

	public TaxDiskInfo findTaxDiskInfoDetail(String taxDiskNo, String taxpayerNo);
	
	public Long CountTaxDiskInfo(String taxDiskNo, String taxpayerNo);
	
	public List getInstInfoTaxNoList(InstInfo info,PaginationList paginationList);

}
