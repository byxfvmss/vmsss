package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.DiskRegInfo;
import com.cjit.vms.trans.model.InstInfo;

public interface DiskRegInfoService {
	
	/**
	 * 列表页面
	 * 
	 * @param DiskRegInfo
	 * @param paginationList
	 * @return
	 */
	public List getTaxDiskInfoList(DiskRegInfo info, PaginationList paginationList);
	/**
	 * 取得税控盘注册信息导出列表
	 * 
	 * @param DiskRegInfo
	 * @return
	 */
	public List getTaxDiskInfoList(DiskRegInfo info);
	/**
	 * 取得税控盘注册信息
	 * 
	 */
	public List getDiskRegInfoDetail(String taxDiskNo,String taxpayerNo);
	/**
	 * 保存税控盘注册信息
	 * 
	 */
	public int saveDiskRegInfo(String operType,DiskRegInfo info);
	/**
	 * check相同的税控盘号和纳税人识别号是否存在
	 * 
	 */
	public Long CountDiskRegInfo(String taxDiskNo, String taxpayerNo) ;
	/**
	 * 删除税控盘基本信息
	 * 
	 */
	public void deleteDiskRegInfo(String[] selectTaxDisks);
	
	
	public List getInstInfoList(InstInfo info,PaginationList paginationList);
	
	public List getInstInfoTaxNoList(InstInfo info,PaginationList paginationList);
	
}
