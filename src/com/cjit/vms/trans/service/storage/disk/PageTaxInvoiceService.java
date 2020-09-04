package com.cjit.vms.trans.service.storage.disk;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.PaperInvoiceStock;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.TaxDiskMonitorInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceStockDetail;

public interface PageTaxInvoiceService {
 public List findPageTaxInvoice(PaperInvoiceListInfo paperInvoiceListInfo,PaginationList paginationList);
 /**
 * @param taxDiskNo
 * @param taxpayerNo 根据纳税人识别号 税控盘号  发票类型 查询当前稅控盘信息
 * @return
 */
public TaxDiskInfo findTaxDiskInfoByTaxNoAndDiskNo(String taxDiskNo);
 
 /**
 * @param taxDiskNo 纳税人识别号 稅控盘号 删除 当前稅控盘信息
 * @param taxpayerNo
 */
public void delTaxDiskInfoByTaxNoAndDiskNo(String taxDiskNo,String taxpayerNo);
 //
 /**
 * @param taxDiskInfo 保存当前税控盘信息
 */
public void addOrUpdateTaxDiskInfo(TaxDiskInfo taxDiskInfo);

 /**
 * @param taxNo
 * @return 根据纳税人识别号得到机构ID
 */
public String getInstIdbyTaxNo(String taxNo);
 //
 /**
 * @param paperInvoiceStock 保存纸质同步表票据库存信息
 */
public void savePaperAutoInvoice(PaperAutoInvoice paperAutoInvoice);

 /**
 * @param receiveInvoiceTime
 * @param taxDiskNo  invoiceBeginNo invoiceCode taxDiskNo taxpayerNo invoiceType
 * @param taxpayerNo 根据 发票类型   税控盘号 纳税人识别号 发票代码 发票起始 号码 确定 纸质票据信息并删除
 */
public void deletePaperAutoInvoice(String invoiceType,String taxDiskNo, String taxpayerNo,String invoiceCode,String  invoiceBeginNo);

/**
 * @param taxId
 * @return 税目 索引
 */
public VmsTaxInfo findVmsTaxInfoById(String taxId,String taxNo,String fapiaoType);
 /**
 * @param taxid 根据税目索引号删除 税目信息
 */ 
public void deleteVmsTaxInfoById(VmsTaxInfo vmsTaxInfo);
public void saveVmsTaxInfo(VmsTaxInfo vmsTaxInfo);
public List findPaperAutoInvoiceDetial(String invoiceType,String taxDiskNo, String taxpayerNo,String invoiceCode,String  invoiceBeginNo,PaginationList paginationList);
/**
 * @param taxDiskMonitorInfo
 * @return 根据税控盘号 及发票类型 确定唯一性
 */
public String findMonTaxDiskinfosByDiskNo(TaxDiskMonitorInfo taxDiskMonitorInfo);
/**
 * @param taxDiskMonitorInfo 增加或更改 税控盘监控信息表
 */
public  void addOrupdateMonTaxDiskInfoByDiskinfo(TaxDiskMonitorInfo taxDiskMonitorInfo);



/*
 *同步数据回传至vms_auto_invoices 表中 
 * */
/**
 * @return 找到唯一的一条稅控盘数据
 */
public List<PaperAutoInvoice> findpaperAutoInvoicebyBusId(String invoiceNo,List<String> auth_inst_ids,String invoiceCode);

/**
 * @param paperAutoInvoice  更新数据
 */
public void updatepaperAutoInvoicebybusId(PaperAutoInvoice paperAutoInvoice);

/**
 *  已开具作废回传
 */
public void updateIssueCancleNum(String invoiceBeginNo,String invoiceEndNo,String currentInvoiceCode);
public void updateinvoiceEmptyCurrentNo(String invoiceBeginNo,String invoiceEndNo,String currentInvoiceCode,String currentBillNo);
/**
 * @param invoiceBeginNo
 * @param invoiceEndNo
 * @param currentInvoiceCode 红冲数量的回传
 */
public void updateInvoiceRedNum(String invoiceBeginNo,String invoiceEndNo,String currentInvoiceCode);
/**
 * @param invoiceBeginNo
 * @param invoiceEndNo
 * @param currentInvoiceCode 正常开具数据的回传
 */
public void updateissueinvoiceNum(String invoiceBeginNo,String invoiceEndNo,String currentInvoiceCode,String currentBillNo);
/**
 * @param invoiceBeginNo
 * @param invoiceEndNo
 * @param currentInvoiceCode  空白作废数量的回传
 */
public void updateinvoiceEmptyNum(String invoiceBeginNo,String invoiceEndNo,String currentInvoiceCode);

/**
 * @param billCode
 * @param invoiceBeginNo
 * @param billNo
 *  开具之后更改分发表当前发票号码
 */
public void updateDistributeAfterIssue(String billCode,String invoiceBeginNo,String invoiceEndNo, String billNo);

/**
 * @param billNo
 * @param billBeginNo
 * @param biilCode
 * 
 * 开具之后 更改 库存详情的当前票据号码
 */
public void updateStorckdetialAfterIssue(String billNo,String billBeginNo,String biilCode);
/**
 * 回写库存表
 */
public void copyAutoInvoiceTostock();
/**
 * 回写库存详情表
 */
public void copyAutoInvoiceToStockDetail();

/**
 * 回写更新库存详情表
 */
public void updateSynStockDetial();
}
