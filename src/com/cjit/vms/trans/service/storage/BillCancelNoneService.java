package com.cjit.vms.trans.service.storage;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.storage.InvoiceStockDetail;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
public interface BillCancelNoneService {
	
	public List getInvoiceStockDetailList(InvoiceStockDetail stockDetail,PaginationList PaginationList);

	public List getInstInfoList(InstInfo info,PaginationList paginationList);
	
	public void updateBillNoneCancel(InvoiceStockDetail stockDetail);
	
	public List getInvoiceStockDetailOfList(InvoiceStockDetail stockDetail);
	
	public void revokeBillNoneCancel(InvoiceStockDetail stockDetail);
	
	public String billNoneInvoiceReason(InvoiceStockDetail stockDetail);
	
	/**
	 * @param billCode
	 * @param billNo
	 * @return   根据发票号码 发票代码 在票据票里是否存在 有则不执行发票作废
	 */
	public List findInvoiceByBillCodeAndBillNo(String billCode,String billNo);
	/**
	 * @param billCode
	 * @param billNo  根据发票号码 发票代码  是否存在空白作废的票
	 * @return
	 */
	public List findYBankInvoiceByCodeAndNo(String billCode,String billNo);
	
	/**
	 * @param billCode
	 * @param billNo
	 * @param list
	 * @return  校验作废 发票代码 发票号码 根据发票号码 发票代码 及机构
	 */
	public String checkBillCodeAndBillNo(String billCode,String billNo,List<Organization> list);
	/**
	 * @return  根据发票代码 发票号码 根据发票号码 发票代码 及机构  发现是否存在 
	 */
	public List<PaperAutoInvoice> findpaperAutoInvoicebyBusId(String invoiceNo,List<Organization> auth_inst_ids,String invoiceCode);
	
	/**
	 * @param stockDetail
	 * 执行空白作废  电子库存表 数量 -1 分发表数量-1 领用 当前表数量-1 库存详情表+1 票据使用票增加作废记录   并执行删除领用当前表的信息
	 */
	public void saveBillBankCancel(InvoiceStockDetail stockDetail);
	/**
	 * @param stockDetail
	 * 2019更新库存发票状态，红冲、作废
	 */
	public void updateInvioceStatus(InvoiceStockDetail stockDetail);
	/**
	 * 2019撤销操作
	 * @param stockDetail
	 */
    public void undo (String paperInvoiceId);
    /**
	 * 2019撤销操作,发票处理，撤销红冲发票成功也需要更改发票代码和发票号对应的发票信息状态为正常开具状态
	 * @param stockDetail
	 */
    public void undoBillInfoStatus(String paperInvoiceId);



}
