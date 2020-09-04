package com.cjit.vms.taxdisk.single.service;

import java.util.List;

import com.cjit.vms.trans.model.TaxDiskMonitorInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.taxdisk.single.model.parseXml.TaxDiskInfoQueryReturnXml;
import com.cjit.vms.taxdisk.tools.AjaxReturn;

public interface PageTaxInvoiceDiskAssitService {
	/**
	 * @param disk
	 * @param fapiaoType
	 * @return 创建监控 信息查询的 xml
	 * @throws Exception
	 */
	public String createMonDataQuery(String diskNo,String fapiaoType) throws Exception;
	/**
	 * @param disk 更改税控盘信息
	 */
	public void updateTaxDiskInfo(TaxDiskInfoQueryReturnXml disk);
	
	
	/**
	 * @param taxDiskMonitorInfo
	 * @return 根据税控盘号 及发票类型 确定唯一性
	 */
	public String findMonTaxDiskinfosByDiskNo(TaxDiskMonitorInfo taxDiskMonitorInfo);
	/**
	 * @param taxDiskMonitorInfo 增加或更改 税控盘监控信息表
	 */
	public  void addOrupdateMonTaxDiskInfoByDiskinfo(TaxDiskMonitorInfo taxDiskMonitorInfo);
	public AjaxReturn saveTaxDiskMonInfoQuery(List list);
	/**
	 * @return  创建税种税目xml
	 */
	public String CreateTaxItemXml(String diskNo,String fapiaoType) throws Exception;

	/**
	 * @param taxInfo 根据税目id 与纳税人识别号 发票类型 确定唯一性
	 * @return
	 */
	public String findTaxIdByIdAndTaxNo(VmsTaxInfo taxInfo);
	
	/**
	 * @param list  保存税目信息
	 */
	public AjaxReturn saveAndUpdateTaxInfo(List list);
	public String CreateBuyBillInfoQuery(String diskNo,String fapiaoType)throws Exception;

}
