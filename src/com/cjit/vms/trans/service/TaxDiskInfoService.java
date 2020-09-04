package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;

public interface TaxDiskInfoService {
	
	/**
	 * 列表页面
	 * 
	 * @param TaxDiskInfo
	 * @param paginationList
	 * @return
	 */
	public List getTaxDiskInfoList(TaxDiskInfo info, PaginationList paginationList);
	/**
	 * 取得税控盘基本信息（导出用）
	 * 
	 */
	public List getTaxDiskInfoList(TaxDiskInfo info);
	/**
	 * 取得税控盘详细信息
	 * 
	 */
	public TaxDiskInfo getTaxDiskInfoDetail(String taxDiskNo,String taxpayerNo);
	/**
	 * 保存税控盘详细信息
	 * 
	 */
	public int saveTaxDiskInfo(String operType,TaxDiskInfo info);
	/**
	 * check相同的税控盘号和纳税人识别号是否存在
	 * 
	 */
	public Long CountTaxDiskInfo(String taxDiskNo, String taxpayerNo) ;
	/**
	 * 删除税控盘基本信息
	 * 
	 */
	public void deleteTaxDiskInfo(String[] selectTaxDisks);
	
	public List getInstInfoList(InstInfo info);
	
	/**
	 * 机构下拉框联动
	 * @param info
	 * @return
	 */
	public List getInstInfoTaxNoList(InstInfo info,PaginationList paginationList);
	
}
