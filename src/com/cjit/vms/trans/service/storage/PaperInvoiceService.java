package com.cjit.vms.trans.service.storage;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.PaperInvoiceStock;
import com.cjit.vms.trans.model.storage.PaperInvoiceDistribute;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceRbHistory;
import com.cjit.vms.trans.model.storage.PaperInvoiceStockDetail;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;

/**
 * @author tom
 *
 */
public interface PaperInvoiceService {

	/**
	 * 根据纸质发票库存ID，获取库存发票信息
	 * 
	 * @param storeId
	 * @return
	 * @author jobell
	 */
	public List findPaperInvoiceStoreByStoreIds(String[] storeIds);

	/**
	 * 保存纸质发票分发结果
	 * 
	 * @param lstDistribute
	 * @author jobell
	 */
	public int savePaperInvoiceDistribute(List lstDistribute);

	/**
	 * 
	 * 
	 * @date:2019年10月21日
	 * @param invoiceStockId
	 * @return
	 * @Description:根据库存id删除未使用的发票信息
	 */
	public void deletePaperInvoiceStock(String invoiceStockId, String invoiceNum);

	/**
	 * @date:2019
	 * @Description:根据发票代码和发票号查询发票状态
	 * @param invoiceCode
	 * @param invoiceNo
	 */
	public String getStockPaperInvoiceStatus(String invoiceCode, String invoiceNo);

	/**
	 * @date:2019
	 * @Description:更新发票数量
	 * @param invoiceCode
	 * @param invoiceNo
	 */
	public void updateStockPaperInvoiceNum(String invoiceCode, String invoiceNo);

	/**
	 * 
	 * @date:2019年12月10日
	 * @param invoiceCode
	 * @param invoiceBeginNo
	 * @param invoiceEndNo
	 * @Description:新增发票库存信息需要判断是否存在发票代码和发票号 
	 */
	public  List<String>  iFInvoiceExists(String invoiceCode, String invoiceBeginNo, String invoiceEndNo);
	/**
	 * 
	 * @date:2019年12月10日
	 * @param invoiceCode
	 * @param paperInvoiceStockId 
	 * @return
	 * @Description:根据库存id和发票代码查询发票库存id
	 */
	public List<PaperInvoiceStockDetail>   invoiceExistsByIdAndCode(String invoiceCode, String paperInvoiceStockId);
	/**
	 * 
	 * @date:2019年12月11日
	 * @param invoiceCode
	 * @param invoiceNo
	 * @return
	 * @Description:
	 */
	public PaperInvoiceUseDetail invoiceNoIfExists(String invoiceCode, String invoiceNo);

	/**
	 * 纸质发票一览初期化表示
	 * 
	 * @param PaperInfo
	 * @param paginationList
	 * @return
	 * @author cylenve
	 */
	public List getPaperInvoiceListInfo(PaperInvoiceListInfo PaperInfo, PaginationList paginationList);

	/**
	 * 纸质发票管理一览用 excel帐票出力 sheet2 【发票使用情况】
	 * 
	 * @param PaperInfo
	 * @return
	 * @author cylenve
	 */
	public List exportPaperInvoiceUserInfoSheet2(PaperInvoiceListInfo PaperInfo);

	/**
	 * 纸质发票管理一览用 excel帐票出力 sheet3【发票领用与归还统计】
	 * 
	 * @param PaperInfo
	 * @return
	 * @author cylenve
	 */
	public List exportPaperInvoiceUserInfoSheet3(PaperInvoiceListInfo PaperInfo);

	/**
	 * 纸质发票使用明细件数的取得
	 * 
	 * @param invoiceCode 发票代码
	 * @param invoiceNo   发票号码
	 * @return
	 * @author cylenve
	 */
	public Long getPaperInvoiceUseDetailCnt(String invoiceCode, String invoiceNo);

	/**
	 * 空白作废发票信息的更新
	 * 
	 * @param invoiceCode   发票代码
	 * @param invoiceNo     发票号码
	 * @param invalidReason 作废原因
	 * @return
	 * @author cylenve
	 */
	public void updateInvalidPaperInvoiceUseDetail(String invoiceCode, String invoiceNo, String invalidReason);

	/**
	 * 保存纸质发票信息
	 * 
	 * @param lstDistribute
	 */
	public int savePaperInvoiceStock(String operType, List lstStock, String receiveInvoiceTime);

	/**
	 * 验证纸质发票代码是否存在
	 * 
	 * @param lstDistribute
	 */
	public Long CountPaperInvoiceCode(String stockId, String invoiceCode);

	/**
	 * 取得纸质发票代码主体信息
	 * 
	 * @param lstDistribute
	 */
	public PaperInvoiceStock getPaperInvoiceStock(String invoiceStockId);

	/**
	 * 取得纸质发票代码信息
	 * 
	 * @param lstDistribute
	 */
	public List getPaperInvoiceStockDetail(String invoiceStockId);

	/**
	 * @param beginNo
	 * @param endNo   已打印的数量
	 * @return
	 */
	public long findYPrintInvoiceNum(int beginNo, int endNo, String billCode);

	/**
	 * @param beginNo
	 * @param endNo
	 * @return 查询 已作废的数量
	 */
	public long findYBancelInvoiceNum(int beginNo, int endNo, String billCode);

}
