package com.cjit.vms.taxdisk.single.service;

import com.cjit.vms.taxdisk.tools.AjaxReturn;

/**
 * @author tom 发票打印
 *
 */
public interface BillPrintDiskService {
	/**
	 * @param diskNo  稅控盘编号
	 * @param billId  票据id
	 * @return
	 */
	public String createBillPrintXml(String diskNo,String billId)throws Exception ;
	
	/**
	 * @param StringXml   打印结果返回字符串
	 * @param billId 票据id 
	 * @return message json 字符串
	 */
	public AjaxReturn  updateBillPrintResult(String StringXml,String billId)throws Exception ;
	
}
