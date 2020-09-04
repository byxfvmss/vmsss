package com.cjit.vms.trans.service.storage;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.storage.PaperInvoiceDistribute;
import com.cjit.vms.trans.model.storage.PaperInvoiceRbHistory;

public interface PaperInvoiceTrackService {
	/**
	 * 根据纸质发票库存ID，获取该库存的分发明细
	 * 
	 * @param storeId
	 * @return List<PaperInvoiceDistribute> 
	 * @author jobell
	 */
	public List findPaperInvoiceDistributeByInstIds(PaperInvoiceDistribute invoiceDistribute,PaginationList paginationList);
	/**
	 * 根据纸质发票分发ID，获取分发详情
	 * 
	 * @param distribute_id
	 * @return
	 */
	public PaperInvoiceDistribute findPaperInvoiceDistributeByDistributeId(String distribute_id);
	/**
	 * @param billCode
	 * @param billNo
	 * @return  领用详情中的发票号码 发票代码
	 */
	public List findReREByBillCodeAndBillNo(String billCode,String billNo);

	
	/**
	 * 保存纸质发票领用退还履历
	 * 
	 * @param history
	 * @return
	 * @author jobell
	 */
	public int savePaperInvoiceRbHistory(PaperInvoiceRbHistory history);
	/**
	 * 取得领用退还履历明细
	 * 
	 * @param lstDistribute
	 */
	public List findPaperInvoiceRbHistoryList(String paperInvoiceDistributeId,PaginationList paginationList);
	/**
	 * 节后开始 退还
	 */
	public PaperInvoiceDistribute findPaperInvoiceDistributeByDistribute(String receiveUserId,String paperInvoiceDistributeId);
	/**
	 * 根据发票的区间,获取发票领用人的数量
	 * 
	 * @param invoiceCode
	 * @param invoiceBeginNo
	 * @param invoiceEndNo
	 * @return
	 */
	public List findReceiveUserByInvoiceRange(String invoiceCode,String invoiceBeginNo,String invoiceEndNo);
	public Long findMayBackNumByInvoiceRange(String invoiceCode,String invoiceBeginNo, String invoiceEndNo);
	/**
	 * @param paperInvoiceRbHistory 退还处理
	 */
	public void savePaperInvoiceBack(PaperInvoiceRbHistory paperInvoiceRbHistory);
	/**
	 * @param receiveUserId
	 * @return 查询当前 领用信息
	 */
	public List findYUseInvoiceByUser(String receiveUserId);
	/**
	 * @param billCode
	 * @param billNo 发现未打印 或者未作废的、已领用 当前使用发票号码 
	 * @return
	 */
	public String findYEmptyAndPrintBillNo(String billCode,String billNo,String receiveUserId);


}
