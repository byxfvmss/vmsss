package com.cjit.vms.trans.service.billInvalid;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.billInvalid.BillCancelInfo;

public interface BillCancelService {
	public List findBillCancelInfoList(BillCancelInfo billCancelInfo, String userID, PaginationList paginationList);
	
	public void updateBillCancelOperateStatus(Map params);
	
	public void updateBillCanceldataStatus(Map params);
	
	public List findBillCancelAuditingList(BillCancelInfo billCancelInfo, String userID, PaginationList paginationList);
	
	public List findBillCancelList(BillCancelInfo billCancelInfo, String userID, PaginationList paginationList);
	
	public BillCancelInfo findBillCancelInfo(String billId);
	
	public List findBillCancelInfoQuery(BillCancelInfo billCancelInfo, String userID,String reqExportSource);

	public String getRegisteredInfo(String taxDiskNo);

	public TaxDiskInfo getTaxDiskInfo(String taxDiskNo);
}
