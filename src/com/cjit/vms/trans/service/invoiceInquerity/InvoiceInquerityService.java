package com.cjit.vms.trans.service.invoiceInquerity;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.invoiceInquerity.InvoiceInquerityListInfo;
import com.cjit.vms.trans.model.invoiceInquerity.TransactionAmount;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
/**
 *  2019发票申请管理
 * @author moran
 *
 */
public interface InvoiceInquerityService {
	
	/**
	 * @ 发票申请单列表 
	 * @param InvoiceInquerityInfo
	 * @param paginationList
	 * @return
	 * @serialData
	 */
	public List getInvoiceInquerityListInfo(InvoiceInquerityListInfo InvoiceInquerityInfo, PaginationList paginationList);

	/**
	 * @ 保存发票申请单信息
	 * @param saveInvoiceInquerityInfo
	 * @return
	 */
	public void saveInvoiceInquerityInfo(InvoiceInquerityListInfo saveInvoiceInquerityInfo);
	
	/**
	 *   @ 删除申请单记录 逻辑删除
	 * @param saveInvoiceInquerityInfo
	 */
	public void deleteInvoiceInquerity(String applicationFormId );
	/**
	 * 
	 * @date:2019年11月25日
	 * @param productType
	 * @param applyTime
	 * @Description:根据月度和交易渠道获取总金额
	 */
	public List<TransactionAmount> getAmountByChannelAndMonth(String productType,String applyTime );
	
	
	

}
