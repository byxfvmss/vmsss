package com.cjit.vms.taxdisk.single.service;

import java.util.List;

import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo;
import com.cjit.vms.taxdisk.tools.AjaxReturn;

/**
 * @author tom 开具辅助 service 
 *
 */
public interface BillIssueDiskAssitService  {
	/**
	 * @param billId
	 * @return 商品详情列别
	 */
	public List<BillItemInfo> findBillItemByBillIdDisk(String billId);
	/**
	 * @param billId
	 * @return 根据 id 找到对应的票据
	 */
	public BillInfo findBillInfoById(String billId);
	/**
	 * @param disk
	 * @param billId
	 * @param MachineNo 开票机号
	 * @return  创建 开具的xml
	 */
	public  AjaxReturn createBillIssueXml(TaxDiskInfo disk,String billId,String MachineNo);
	
	/**
	 * @param disk
	 * @param billId
	 * @return 创建空白作废的xml
	 */
	public AjaxReturn createBillBalanlCancel(TaxDiskInfo disk,String billCode,String billNo,String fapiaoType,String userId);
	
	/**
	 * @param billCode 发票代码
	 * @param billNo   发票号码
	 * @param billId   票据id
	 * @param datastatus 票据状态
	 * @param diskNo    税控盘号
	 * @param MachineNo  开票机号
	 * @param userId  	用户id 更改开票人用
	 * @param billDate  开票日期
	 * @return message字符串
	 */
	public AjaxReturn updateBillIssueResult(String billCode, String billNo,
			 String billId,String diskNo,String MachineNo,String userId,String billDate,String resturnmsg,boolean flag);
	/**
	 * @param Id 票据id vms_trans_bill表 vms_bill_info 表
	 * @return 交易的集合  vms_trans_info 表   查 开票类型、交易id 票据id 
	 */
	public List findTransInfoForDisk(String billId);
	/**
	 * @param transId 交易 id 找到对应的List 
	 * @return   交易的与额等于0所有的票据 的发票代码均不为空 查余额 票据id 交易id 票据代码 
	 */
	public List findBillInfoListByTransId(String transId);

	/**
	 * @param transId 交易Id
	 * @param datastatus 交易 状态
	 * 
	 */
	public void updateTranDatastatusByTransId(String transId,String datastatus);
	/**
	 * @param billCode
	 * @param billNo
	 * @param fapiaoType
	 * @return 找到是否存空白作废的 发票
	 */
	public String findBillDiskByNoAndTypeForBlankCancel(String billCode,String billNo,String fapiaoType);
	

}
