package com.cjit.vms.trans.service.billTransRelate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.TransBillInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;

public interface BillTransRelateService {
	/***
	 * 查询可钩稽的发票
	 * 
	 * @param billInfo
	 * @param lstAuthInstId
	 * @param paginationList
	 * @param relateStatus
	 * @return
	 */
	public List selectBillTransReateList(BillInfo billInfo, List lstAuthInstId,
			String applyDateStart, String applyDateEnd, String relateStatus,
			PaginationList paginationList);

	/***
	 * 
	 * @param billInfo
	 * @param paginationList
	 * @return
	 */
	public List selectTransReatedList(BillInfo billInfo,
			PaginationList paginationList);
	
	
	
	public List selectBillItemInfoList(String billId, PaginationList paginationList);

	/***
	 * 查询
	 * 
	 * @param billInfo
	 * @param paginationList
	 * @return
	 */
	public List selectTransNotReateList(BillInfo billInfo,String relateGoodsId,String relateTransId, List lstAuthInstId,
			PaginationList paginationList);

	/***
	 * 查询票据交易对应表
	 * 
	 * @param billId
	 * @return
	 */
	public List selectTransBillInfo(String billId, String transId,
			String billItemId);

	/***
	 * 删除票据交易时回写交易金额及状态
	 */
	public void updateTransAmtAndStatusReturn(TransBillInfo transBillInfo);

	/***
	 * 追加票据交易时回写交易金额及状态
	 */
	public void updateTransAmtAndStatusGet(TransBillInfo transBillInfo);

	/***
	 * 删除票据交易信息
	 * 
	 * @param billId
	 * @return
	 */
	public boolean deleteBillTransAndUpdateTrans(String billId,String transId,String billItemId) ;

	/***
	 * w保存票据交易关联信息
	 * 
	 * @param billTrans
	 */
	public void saveBillTrans(TransBillInfo billTrans);

	
	/***
	 * 删除票据交易关联信息
	 * @param billId
	 * @param transId
	 * @param billItemId
	 */
	public void deleteBillTrans(String billId,String transId,String billItemId);
	/***
	 * 处理票据交易关联
	 */
	public String saveBillTransRelate(String billId, String[] relateAmt,
			String[] relateTransId, String[] relateTaxRate,String[] relateItemIds)throws Exception ;
	
	public void updateBillRelateStatus(String billId,String relateStatus);
}
