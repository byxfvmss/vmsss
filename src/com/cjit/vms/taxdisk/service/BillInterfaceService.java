package com.cjit.vms.taxdisk.service;

import java.util.Map;

import com.cjit.vms.taxdisk.tools.AjaxReturn;

public interface BillInterfaceService {

	/**
	 *  创建发票开具报文
	 */
	public AjaxReturn createBillissue(Map params) throws Exception;

	/**
	 *  更新发票开具结果
	 */
	public AjaxReturn updateBillIssueResult(Map params) throws Exception;

	/**
	 *  创建发票打印报文
	 */
	public AjaxReturn createBillPrint(Map params) throws Exception;

	/**
	 *  更新发票打印结果
	 */
	public AjaxReturn updateBillPrintResult(Map params) throws Exception;

	/**
	 *  创建发票作废报文
	 */
	public AjaxReturn createBillCancel(Map params) throws Exception;

	/**
	 *  更新发票作废结果
	 */
	public AjaxReturn updateBillCancelResult(Map params) throws Exception;

	/**
	 * 创建税目报文
	 */
	public AjaxReturn createTaxItemInfo(Map params) throws Exception;

	/**
	 *  向税务软件同步税目信息
	 */	
	public AjaxReturn saveTaxItemInfo(Map params) throws Exception;

	/**
	 * 创建查询税控阈值信息报文
	 */	
	public AjaxReturn createTaxMonitor(Map params) throws Exception;

	/**
	 * 保存税控阈值信息
	 */
	public AjaxReturn saveTaxMonitor(Map params) throws Exception;

	/**
	 *  创建库存统计信息报文
	 */
	public AjaxReturn createStockInfo(Map params) throws Exception;

	/**
	 *  保存库存统计信息
	 */
	public AjaxReturn saveStockInfo(Map params) throws Exception;

	/**
	 *  创建库存分发报文
	 */
	public AjaxReturn createStockIssue(Map params) throws Exception;

	/**
	 *  保存库存分发
	 */
	public AjaxReturn saveStockIssue(Map params) throws Exception;

	/**
	 *  创建库存收回报文
	 */
	public AjaxReturn createStockRecover(Map params) throws Exception;

	/**
	 *  保存库存收回
	 */
	public AjaxReturn saveStockRecover(Map params) throws Exception;

	/**
	 *  创建税控软件基本信息报文
	 */
	public AjaxReturn createTaxInfo(Map params) throws Exception;
	
	/**
	 *  校验税控软件基本信息
	 */
	public AjaxReturn checkTaxInfo(Map params) throws Exception;

	/**
	 *  保存税控软件基本信息
	 */
	public AjaxReturn saveTaxInfo(Map params) throws Exception;
	/**
	 *  创建注册码基本信息报文
	 */
	public AjaxReturn createRegistInfo(Map params) throws Exception;
	/**
	 * @param params
	 * @return
	 * @throws Exception  验证注册码信息
	 */
	public AjaxReturn checkRegistInfo(Map params) throws Exception;
	
	/**
	 *  创建当前发票号码码信息
	 */
	public AjaxReturn createCurBillNoInfo(Map params) throws Exception;
	/**
	 * @param params
	 * @return
	 * @throws Exception  验证当前发票号码信息
	 */
	public AjaxReturn checkCurBillNoInfo(Map params) throws Exception;
	
	

}
