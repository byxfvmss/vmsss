package com.cjit.vms.trans.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.SpecialTicket;
import com.cjit.vms.trans.model.TransInfo;

public interface BillInfoService {

	public List findBillInfoList(BillInfo billInfo, String userID,
			PaginationList paginationList);
	
	public List selectBillInfoListAudit(BillInfo billInfo, String userID,
			PaginationList paginationList,boolean flag);
	
	public List findBillInfoListNew(BillInfo billInfo, String userID,
			PaginationList paginationList);

	
	
	public List findBillInfoList(BillInfo billInfo);

	public BillInfo findBillInfo(String billId);

	public List findBillItemInfoList(BillItemInfo billItemInfo);
	
	public BillItemInfo findBillItemInfo(String billItemId);

	public void saveBillInfo(BillInfo billInfo, boolean isUpdate);


	public void saveBillItemInfo(BillItemInfo billItemInfo, boolean isUpdate);
	
	public void savePreBillItemInfo(BillItemInfo billItemInfo, boolean isUpdate);
	
	public void updatePreBillItemInfo(BillItemInfo billItemInfo, boolean isUpdate);
	
	public void deleteBillInfo(String billId);
	
	public void deleteBillItemInfo(String billId, String billItemId);
	
	public List findtransId(String billId);
	
	public void setBillStatus(String billId);
	
	
	public List findTaxpayerTypeDatas(String customer_taxno, String tableId);//czl
	
	public List findTaxpayerTypeDatas2(String customer_taxno, String tableId);//czl
	
	public List findBillSubDatas(String billId,String subTableId);
	
	public List findBillInfoList(BillInfo billInfo, String userID,
			String[] billIds);
	public BillInfo findTransInfoList(TransInfo  transInfo);
	
	public List findBillInfoForEmsList(BillInfo billInfo, PaginationList paginationList);
	
	public BillInfo findBillInfoForEms(BillInfo billInfo);
	
	public int findz(String type, String tableId);
	
	public int findp(String type, String tableId);

	public void updateBillInfo(Map map);

	public List findRedReceiptList(String sqlId, RedReceiptApplyInfo applyInfo, String userID,
			PaginationList paginationList);
	
	public BillInfo findBillInfo1(String billId);
	
	public void saveBillInfo1(BillInfo billInfo, boolean isUpdate);
	
	public RedReceiptApplyInfo findByBillId(String billId);
	
//	public List findRedReceiptTrans(String billId,PaginationList paginationList);

	public List findBillItemInfoList1(BillItemInfo billItemInfo);
	
	public void saveSpecialTicket(SpecialTicket specialTicket);
	
	public void deleteApplyInfo(String billNo);
	
	public RedReceiptApplyInfo findListByBillId(String billId,Map map);
	
	public List findSpecialTicketById(Map map);
	
	public void updateSpecialTicket(SpecialTicket specialTicket);
	
	public List findReleaseTrans(Map map);
	//结束	
	
	/**
	 * 发票打印页面查看发票对应的交易信息
	 */
	public List inputBillTrans(String billCode,String billNo);
	
	/**
	 * 发票打印页面导出EXCEL-NEW
	 */
	public List billsToExcelNew(BillInfo billInfo);
	
	public void updateBillInfoByBillNo(Map map);
	public void updateBillByBillId(String  billIds,String dataStatus);
	public void updateBillByBillIdsup(String  billIds,String dataStatus);
	public List findBillInfosNew(String[] selectIds);
	
	public List findBillInfoByIDFaPiaoType(String[] billId,String fapiaoType);
	
	/**
	 * 进入发票编辑列表页   ys
	 * @param billInfo
	 * @param userID
	 * @param paginationList
	 * @return
	 */
	public List selectBillInfoList(BillInfo billInfo, String userID,
			PaginationList paginationList,boolean flag);
	public List selectBillInfoList(BillInfo billInfo, String userID);

	//ys
	public BillInfo findBillById(String billId);
	//ys
	public void UpdateRemarkAndPayee(String billId,String remark, String payee);
	
	public BillInfo findBillInfoByIDFaPiaoType(BillInfo billInfo);

	public List findEmsInfoForExport(BillInfo billInfo);
	// 票据审核画面case追加      at lee start
	/**
	 * 票据审核画面    状态更新
	 * 
	 * @param billId
	 * @param dataStatus
	 * 
	 * @author lee
	 */
	public void updateBillInfoDataStatus(String billId, String dataStatus);
	public void updateBillInfoDataStatus(String billId, String dataStatus, String cancelReason);
	// 票据审核画面case追加      at lee end

	/**
	 * 票据审核画面    审核通过
	 * 
	 * @param billId
	 * @param dataStatus
	 * @param reviewer
	 * 
	 * @author leixu
	 */
	public void updateBillInfoApprovedStatus(String billId, String dataStatus, String reviewer);

	/**
	 * 票据勾稽列表
	 * @return 
	 * 
	 */
	public List findBillInfoCheckList(BillInfo info, PaginationList paginationList);
	public BillInfo getBillInfoDetail(String billId);
	public List getBillItemInfoList(String billId,PaginationList paginationList);
	public List getTransInfoCheckYlist(String billId,PaginationList paginationList);
	public List getTransInfoCheckNlist(Map m,PaginationList paginationList);
	public int saveBillInfoCheck(Map confirmMap,Map cancelMap,String billId,Map newData,Map oldIdDataChange );

	public TransInfo findTransInById(String transId);
	
	public List selBillAmtByBillId(BillInfo bill);

	public void updateBillRemarkById(String remark, String billId);
	
	/**
	 * @param billCode
	 * @param billNo  更改作废状态
	 */
	public void updatebillCancelStatus(String billCode,String billNo);
	
	/***
	 * 
	 * @param billId billId和（billCode、billNo）必须选一个
	 * @param billCode
	 * @param billNo
	 * @param dataStatus
	 * @param cancelReason
	 */
	public void updateBillStatus(String billId,String billCode,String billNo,String dataStatus,String cancelReason);
}
