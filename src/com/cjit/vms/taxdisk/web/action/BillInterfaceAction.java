package com.cjit.vms.taxdisk.web.action;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.vms.taxdisk.service.BillInterfaceService;
import com.cjit.vms.taxdisk.service.impl.AssionDiskBillInterfaceServiceImpl;
import com.cjit.vms.taxdisk.service.impl.BWDiskBillInterfaceServiceImpl;
import com.cjit.vms.taxdisk.service.impl.BWServletBillInterfaceServiceImpl;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.service.TransInfoService;

public class BillInterfaceAction extends DataDealAction {
	private static final long serialVersionUID = 1L;

	private BillInterfaceService assionDiskBillInterfaceService;
	private BillInterfaceService bwDiskBillInterfaceService;
	private BillInterfaceService bwServletBillInterfaceService;
	private static DataDealService dataDealService;
	private static TransInfoService transInfoService;
	private TaxDiskInfoQueryService taxDiskInfoQueryService;
	private static Map<String, BillInterfaceService> billInterfaceServiceMap = null;
	private BillInterfaceService getBillInterfaceService() {
		if (billInterfaceServiceMap == null) {
			billInterfaceServiceMap = new HashMap<String, BillInterfaceService>();
			billInterfaceServiceMap.put(AssionDiskBillInterfaceServiceImpl.INTERFACE_TYPE, assionDiskBillInterfaceService);
			billInterfaceServiceMap.put(BWDiskBillInterfaceServiceImpl.INTERFACE_TYPE, bwDiskBillInterfaceService);
			billInterfaceServiceMap.put(BWServletBillInterfaceServiceImpl.INTERFACE_TYPE, bwServletBillInterfaceService);
		
		}
		return billInterfaceServiceMap.get(BWServletBillInterfaceServiceImpl.INTERFACE_TYPE);
	}

	private Map buildParams() {
		return buildParams(null);
	}

	private Map buildParams(Map<String, Boolean> filterMap) {
		Map params = new HashMap();
		params.put("instCode", this.getCurrentUser().getOrgId());
		params.put("userId", this.getCurrentUser().getId());
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();
			boolean filterFlag = true;
			if (filterMap != null) {
				filterFlag = filterMap.get(name);
			}
			if (filterFlag) {
				String value = request.getParameter(name);
				params.put(name, value);
			}
		}

		return params;
	}

	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
	}

	/**
	 *  创建发票开具报文
	 */
	public void createBillissue() throws Exception {
		returnResult(getBillInterfaceService().createBillissue(buildParams()));
	}

	/**
	 *  更新发票开具结果
	 */
	public void updateBillIssueResult() throws Exception {
		returnResult(getBillInterfaceService().updateBillIssueResult(buildParams()));
	}

	/**
	 *  创建发票打印报文
	 */
	public void createBillPrint() throws Exception {
		returnResult(getBillInterfaceService().createBillPrint(buildParams()));
	}

	/**
	 *  更新发票打印结果
	 */
	public void updateBillPrintResult() throws Exception {
		returnResult(getBillInterfaceService().updateBillPrintResult(buildParams()));
	}

	/**
	 *  创建发票作废报文
	 */
	public void createBillCancel() throws Exception {
		returnResult(getBillInterfaceService().createBillCancel(buildParams()));
	}

	/**
	 *  更新发票作废结果
	 */
	public void updateBillCancelResult() throws Exception {
		returnResult(getBillInterfaceService().updateBillCancelResult(buildParams()));
	}

	/**
	 * 创建税目报文
	 */
	public void createTaxItemInfo() throws Exception {
		returnResult(getBillInterfaceService().createTaxItemInfo(buildParams()));
	}

	/**
	 *  向税务软件同步税目信息
	 */	
	public void saveTaxItemInfo() throws Exception {
		returnResult(getBillInterfaceService().saveTaxItemInfo(buildParams()));
	}

	/**
	 * 创建查询税控阈值信息报文
	 */	
	public void createTaxMonitor() throws Exception {
		returnResult(getBillInterfaceService().createTaxMonitor(buildParams()));
	}

	/**
	 * 保存税控阈值信息
	 */
	public void saveTaxMonitor() throws Exception {
		returnResult(getBillInterfaceService().saveTaxMonitor(buildParams()));
	}

	/**
	 *  创建库存统计信息报文
	 */
	public void createStockInfo() throws Exception {
		returnResult(getBillInterfaceService().createStockInfo(buildParams()));
	}

	/**
	 *  保存库存统计信息
	 */
	public void saveStockInfo() throws Exception {
		returnResult(getBillInterfaceService().saveStockInfo(buildParams()));
	}

	/**
	 *  创建库存分发报文
	 */
	public void createStockIssue() throws Exception {
		returnResult(getBillInterfaceService().createStockIssue(buildParams()));
	}

	/**
	 *  保存库存分发
	 */
	public void saveStockIssue() throws Exception {
		returnResult(getBillInterfaceService().saveStockIssue(buildParams()));
	}

	/**
	 *  创建库存收回报文
	 */
	public void createStockRecover() throws Exception {
		returnResult(getBillInterfaceService().createStockRecover(buildParams()));
	}

	/**
	 *  保存库存收回
	 */
	public void saveStockRecover() throws Exception {
		returnResult(getBillInterfaceService().saveStockRecover(buildParams()));
	}

	/**
	 *  创建税控软件基本信息报文
	 */
	public void createTaxInfo() throws Exception {
		System.out.println("99");
		returnResult(getBillInterfaceService().createTaxInfo(buildParams()));
	}
	
	/**
	 *  校验税控软件基本信息    
	 */
	public void checkTaxInfo() throws Exception {
		returnResult(getBillInterfaceService().checkTaxInfo(buildParams()));
	}

	/**
	 * @throws Exception 创建  注册 xml
	 */
	public void createRegistInfo() throws Exception {
		returnResult(getBillInterfaceService().createRegistInfo(buildParams()));
	}
	/**2
	 * @throws Exception 校验 注册 码信息
	 */
	public void checkRegistInfo() throws Exception {
		returnResult(getBillInterfaceService().checkRegistInfo(buildParams()));
	}
	/**
	 * @throws Exception  创建查询当前发票号码 
	 */
	public void createCurBillNoInfo()throws Exception{
		returnResult(getBillInterfaceService().createCurBillNoInfo(buildParams()));
	}
	/**
	 * @throws Exception  校验当前发票号码返回报文 
	 */
	public void checkCurBillNoInfo()throws Exception{
		returnResult(getBillInterfaceService().checkCurBillNoInfo(buildParams()));
	}
	/**
	 *  保存税控软件基本信息
	 */
	public void saveTaxInfo() throws Exception {
		returnResult(getBillInterfaceService().saveTaxInfo(buildParams()));
	}

	public void setAssionDiskBillInterfaceService(BillInterfaceService assionDiskBillInterfaceService) {
		this.assionDiskBillInterfaceService = assionDiskBillInterfaceService;
	}

	public void setBwDiskBillInterfaceService(BillInterfaceService bwDiskBillInterfaceService) {
		this.bwDiskBillInterfaceService = bwDiskBillInterfaceService;
	}

	public BillInterfaceService getBwServletBillInterfaceService() {
		return bwServletBillInterfaceService;
	}

	public void setBwServletBillInterfaceService(
			BillInterfaceService bwServletBillInterfaceService) {
		this.bwServletBillInterfaceService = bwServletBillInterfaceService;
	}

	public BillInterfaceService getBwDiskBillInterfaceService() {
		return bwDiskBillInterfaceService;
	}

	public TaxDiskInfoQueryService getTaxDiskInfoQueryService() {
		return taxDiskInfoQueryService;
	}

	public void setTaxDiskInfoQueryService(TaxDiskInfoQueryService taxDiskInfoQueryService) {
		this.taxDiskInfoQueryService = taxDiskInfoQueryService;
	}
	
	public static DataDealService getDataDealService() {
		return dataDealService;
	}

	public static void setDataDealService(DataDealService dataDealService) {
		BillInterfaceAction.dataDealService = dataDealService;
	}
	
	public TransInfoService getTransInfoService() {
		return transInfoService;
	}

	public void setTransInfoService(TransInfoService transInfoService) {
		BillInterfaceAction.transInfoService = transInfoService;
	}

}
