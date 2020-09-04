package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;

public interface BillTrackService {

	public List findBillInfoList(BillInfo billInfo, PaginationList paginationList);

	public List findTransByBillId(String billId, PaginationList paginationList);

	public BillInfo findBillInfoById(String billId);

	public List findBillItemByBillId(String billId);

	public void setBillDataStatus(String billIds, String dataStatus);

	public List findBillItemByBillIds(String[] selectBillIds, PaginationList paginationList);

	public void deleteBillInfoById(String billId);

	public void updateTransInfoStatus(String dataStatus, String billId);

	public void updateBillInfoStatus(BillInfo billInfo);

	public void deleteTransBillInfo(String billId);

	public void deleteBillItemInfo(String billId);

	public List findBillBySelect(String[] billIds);

	public List findBillInfoList(BillInfo billInfo);

	public String findRegisteredInfo(String taxDisNo);

	public void updatebillInfoIssueResult(BillInfo bill);

	public List findInvalidPaperInvoice();

	public void updatePaperInvoiceStatus(PaperInvoiceUseDetail invalidInvoice);

	public BillInfo findBillInfoById1(String billId);
	/**
	 * 
	 * @date:2019年11月20日
	 * @param billInfo
	 * @return
	 * @Description:
	 */
	public List findInvoiceInfoList(BillInfo billInfo);
}
