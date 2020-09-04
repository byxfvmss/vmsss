package com.cjit.vms.taxdisk.single.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.tools.AjaxReturn;

/**
 * @author tom 发票打印辅助类
 *
 */
public interface BillPrintDiskAssistService {
	
	
	/**
	 * @param ids 以，拼接的ids 集合
	 * @return 以发票代码 发票号码  升序的排列集合
	 */
	public List findBillPrintList(String ids);
	
	/**
	 * @param billCode 发票代码 
	 * @param billNo  发票号码
	 * @param fapiaoType 发票类型    查看  VMS_PAPER_INVOICE_RB_DETAIL 表
	 * @return 已领用 返回true 未领用 返回 false；
	 */
	public boolean checkBillCodeYRe(String billCode,String billNo,String fapiaoType);
	/**
	 * @param ids
	 * @return　得 到校验（已领用 、顺序排序的ids）通过的 ids 发票代码、发票号码 段 封装到message中 
	 */
	public String  getBillPrintString(String ids);
	/**
	 * @param ids
	 * @return 校验限定值   以打印   printLimitValue = Integer.valueOf(
						paramConfigVmssService.findvaluebyName("单次打印限值（张）"))
						写个似的的 sql 交验	小于限定值返回true 大于返回false
	 */
	public boolean checkLimtStock(int length);
	
	
	/**更改票据 状态(打印)
	 * @param billId
	 */
	public void updateBillDiskStatus(String billId,String status);
	

	/**
	 * @param billId
	 * @return BillInfo
	 */
	public BillInfo findBillInfo(String billId);
	/**
	 * @param billInfo
	 * @return List
	 */
	public List findBillInfoList(BillInfo billInfo); 
	/**
	 * @param billId
	 * @param returnCode
	 * @param returnMsg
	 * @return 更改打印结果信息 返回Message
	 * @return SucessMsg 返回成功结果
	 */
	public AjaxReturn updateBillPrintResult(String billId,String returnMsg,boolean flag);
	
} 
