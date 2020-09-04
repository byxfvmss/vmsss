package com.cjit.vms.taxdisk.single.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import cjit.crms.util.StringUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.BillCancel;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillCancelTiansInfo;
import com.cjit.vms.taxdisk.single.model.parseXml.BillCancelReturnXMl;
import com.cjit.vms.taxdisk.single.service.BillCancelDiskAssitService;
import com.cjit.vms.taxdisk.tools.Message;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;


public class BillCancelDiskAssitServiceImpl  extends GenericServiceImpl implements BillCancelDiskAssitService {

	/**
	 * 根据BillId获取票据信息
	 * 
	 */
	@Override
	public BillInfo findBillInfo(String billId) {
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		 
		List list = this.findBillInfoList(billInfo);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}
	public List findBillInfoList(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		return find("findBillInfo", map);
	}
	/**
	 * 根据taxDiskNo获取税控盘信息
	 */
	@Override
	public TaxDiskInfo findTaxDiskInfo(String taxDiskNo) {

		Map param = new HashMap();
		param.put("taxDiskNo", taxDiskNo);
		return (TaxDiskInfo) this.findForObject(
				"findTaxDiskInfoXmlByTaxDiskNo", param);

	}

	/* 
	 * 更改票据状态
	 */
	@Override
	public AjaxReturn updateBillCancelResult(String billId,String returnMsg,boolean flag) throws Exception {
		AjaxReturn message=null;
		
		try {
			if(flag){
			Map map=new HashMap();
			map.put("id", billId);
			update("updateBillCancelResult", map);
			openBillCancelTrans(billId);
			message=new AjaxReturn(true);
			}else{
				message=new AjaxReturn(false, returnMsg);
				return message;
			}
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_update_bill_cancel_datastauas_error);
		}
		return message;
		
	}
	
	/* 
	 * 发票作废交易信息
	 */
	@Override
	public List billCancelTransInfo(String billId){
		Map map = new HashMap();
		map.put("billId", billId);
		return find("billCancelTiansInfo", map);
	}
	
	//释放交易
	@Override
	public void openBillCancelTrans(String billId){
		List list = 	billCancelTransInfo(billId); 
		for(int i=0;i<list.size();i++){
			BillCancelTiansInfo billCancelTiansInfo =(BillCancelTiansInfo)list.get(i); 
			//拆分时更改数据
			BigDecimal balance = billCancelTiansInfo.getBalance().add(billCancelTiansInfo.getSumAmt());
			BigDecimal taxCnyBalance = billCancelTiansInfo.getTaxCnyBalance().add(billCancelTiansInfo.getTaxAmtSum());
			String transId=billCancelTiansInfo.getTransId();
			if("3".equals(billCancelTiansInfo.getIssueType())){
			//开具类型 为拆分时
				if(balance.compareTo(billCancelTiansInfo.getAmtCny())==0){
					//置状态为未开票1   
					updateBillDataStatusChai(transId,balance,taxCnyBalance,"1");
				}else{
					//置状态为开票中3
					updateBillDataStatusChai(transId,balance,taxCnyBalance,"3");
				}
			}else{
			//开具类型为单笔 合并时--置状态为未开票1
				updateBillDataStatus(transId,"1");
			}
		}
	}
	
	//根据 billId 更改状态
	public void updateBillDataStatusChai(String transId,BigDecimal balance,BigDecimal taxCnyBalance,String dataStatus){
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("dataStatus", dataStatus);
		map.put("balance", balance);
		map.put("taxCnyBalance", taxCnyBalance);
		update("updateBillDataStatusChai", map);
	} 
	public void updateBillDataStatus(String transId,String dataStatus){
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("dataStatus", dataStatus);
		update("updateBillDataDiskStatus", map);
	} 
	
	
}
