package com.cjit.vms.taxdisk.single.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cjit.crms.util.json.JsonUtil;
import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.ParamSet;
import com.cjit.vms.taxdisk.single.service.BillPrintDiskAssistService;
import  com.cjit.vms.taxdisk.single.service.util.Message;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.util.DataUtil;

public class BillPrintDiskAssistServiceImpl extends GenericServiceImpl implements
		BillPrintDiskAssistService {

	@Override
	public boolean checkBillCodeYRe(String billCode, String billNo,
			String fapiaoType) {
		Map map = new HashMap();
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		map.put("fapiaoType", fapiaoType);
		List list = find("checkBillCodeYRe", map);
		if(list.size()==0){
			return false;
		}
		return true;
	}

	@Override
	public List findBillPrintList(String ids) {
		String[] id=ids.split(",");
		Map map = new HashMap();
		map.put("id", id);
		List list =find("findBillPrintList", map);
		return list;
	}

	@Override
	public String getBillPrintString(String ids) {
		List list =	findBillPrintList(ids);
		BillInfo billInfo=(BillInfo)list.get(0);
		String fapiaoType=billInfo.getFapiaoType();
		String endNo="";
		String startNo=billInfo.getBillNo();
		Message message=new Message();
		boolean flag=false;
		int i=0;
		try {
			for(i=0;i<list.size()-1;i++){
				billInfo=(BillInfo)list.get(i);
				BillInfo bill=(BillInfo)list.get(i+1);
				if(billInfo.getBillCode().equals(bill.getBillCode())){
					if(Integer.parseInt(billInfo.getBillNo()+1)==Integer.parseInt(bill.getBillNo())){
						boolean result=checkBillCodeYRe(billInfo.getBillCode(),billInfo.getBillNo(),fapiaoType);
						if(result){
							endNo=bill.getBillNo();
						}else{
							flag=true;
							break;
						}
					}else{
						boolean result=checkBillCodeYRe(billInfo.getBillCode(),billInfo.getBillNo(),fapiaoType);
						if(result){
							endNo=billInfo.getBillNo();
							break;
						}else{
							flag=true;
							break;
						}
					}
				}
			}
			if(checkLimtStock(i)){
				message.setReturnCode(Message.error);
				message.setReturnMsg(Message.single_print_limit_error);
			}else if(endNo.isEmpty()){
				message.setReturnCode(Message.error);
				message.setReturnMsg("发票代码"+billInfo.getBillCode()+"发票号码"+billInfo.getBillNo()+"未领用不能开票");
			}else if(flag){
				message.setReturnCode(Message.success);
				message.setReturnMsg("发票代码"+billInfo.getBillCode()+"发票号码区间"+startNo+"-"
						+endNo+"是连续的"+"发票号码"+Integer.parseInt(endNo)+1+"未领用是否打印?");
			}else{
				message.setReturnCode(Message.success);
				message.setReturnMsg("发票代码"+billInfo.getBillCode()+"发票号码区间"+startNo+"-"
						+endNo+"是连续的是否打印?");
			}
		} catch (Exception e) {
			message.setReturnCode(Message.error);
			message.setReturnMsg(Message.system_exception);
		}
		return JsonUtil.toJsonString(message);
	}

	@Override
	public boolean checkLimtStock(int length) {
		String itemCname = "单次打印限值（张）";
		Map map =new HashMap();
		map.put("itemCname", itemCname);
		List list =find("findvaluebyNameDisk", map);
		ParamSet paramSet  =(ParamSet)list.get(0);
		Integer selectedValue =Integer.valueOf(paramSet.getSelectedValue());
		if(length>selectedValue){
			return false;
		}
		return true;
	}
	
	@Override
	public void updateBillDiskStatus(String billId,String status){
		Map  map = new HashMap();
		map.put("billId", billId);
		map.put("status", status);
		update("updateBillDiskStatus", map);
	}

	@Override
	public AjaxReturn updateBillPrintResult(String billId, 
			String returnMsg,boolean flag) {
		AjaxReturn message =null;
		try {
			if(flag){
				updateBillDiskStatus(billId,DataUtil.BILL_STATUS_8);
				message=new AjaxReturn(true);
			}else{
				updateBillDiskStatus(billId,DataUtil.BILL_STATUS_9);
				message=new AjaxReturn(false, returnMsg);
			}
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_update_print_error);
			return message;
		}
		return message;
	}
	
	@Override
	public BillInfo findBillInfo(String billId) {
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		 
		List list = findBillInfoList(billInfo);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public List findBillInfoList(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		return find("findBillInfo", map);
	}
	
}
