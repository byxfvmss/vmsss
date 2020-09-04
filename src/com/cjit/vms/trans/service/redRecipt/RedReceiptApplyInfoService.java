package com.cjit.vms.trans.service.redRecipt;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.SpecialTicket;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;

public interface RedReceiptApplyInfoService {
	public List findRedReceiptList(RedReceiptApplyInfo redReceiptApplyInfo,PaginationList paginationList);
	public List findRedReceiptList(RedReceiptApplyInfo redReceiptApplyInfo);
	public List findTransByBillId(String billId, PaginationList paginationList);
	public Long findInvalidInvoiceCount(String dataStatus, String fapiaoType);
	public String findRegisteredInfo(String taxDiskNo);
	public BillInfo findBillInfoById(String billId);
	public List findInvalidPaperInvoice(String dataStatus, String fapiaoType);
	public List findBillItemByBillId(String billId);
	public void updatebillInfoIssueResult(BillInfo billInfo);
	public void updatePaperInvoiceStatus(PaperInvoiceUseDetail invalidInvoice);
	public void updateTransInfoStatus(String dataStatus, String billId);
	public void updateBillInfoStatus(BillInfo billInfo);
	public TaxDiskInfo findTaxDiskInfoByTaxDiskNo(String taxDiskNo);
	
	public List findRedReceiptApplyList(String sqlId, RedReceiptApplyInfo applyInfo, String userID,PaginationList paginationList);
	public RedReceiptApplyInfo findByBillId(String billId);
	public List findRedReceiptTrans(String billId,PaginationList paginationList);
	public List findRedReceiptTrans(String billId,PaginationList paginationList,String ids);
	public List findRedReceiptTransByIds(String billId,PaginationList paginationList,String[] ids);
	public void saveBillInfo1(BillInfo billInfo, boolean isUpdate);
	public List findRedReceiptList(String sqlId, RedReceiptApplyInfo applyInfo, String userID,PaginationList paginationList);
	public BillInfo findBillInfo1(String billId);
	public RedReceiptApplyInfo findListByBillId(String billId,Map map);
	public List findReleaseTrans(Map map);
	public List findBillItemInfoList(BillItemInfo billItemInfo);
	public void deleteBillItemInfo(String billId, String billItemId);
	public void saveBillInfo(BillInfo billInfo, boolean isUpdate);
	public void saveBillItemInfo(BillItemInfo billItemInfo, boolean isUpdate);
	public void saveSpecialTicket(SpecialTicket specialTicket);
	public void deleteApplyInfo(String billNo);
	public void deleteApplyInfo(String billCode ,String billNo);
	public void deleteBillInfo(String billId);
	public List findBillItemInfoList1(BillItemInfo billItemInfo);
	public void updateRedBill(BillInfo billInfo);
	public List findRedBillByOriBillId(String billId);
}
