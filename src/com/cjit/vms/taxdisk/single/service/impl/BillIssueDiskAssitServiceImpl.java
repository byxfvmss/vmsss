package com.cjit.vms.taxdisk.single.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.BillCancel;
import com.cjit.vms.taxdisk.single.model.BillIssue;
import com.cjit.vms.taxdisk.single.model.Goods;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TransInfo;
import com.cjit.vms.taxdisk.single.service.BillIssueDiskAssitService;
import  com.cjit.vms.taxdisk.tools.Message;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;

public class BillIssueDiskAssitServiceImpl extends GenericServiceImpl implements BillIssueDiskAssitService  {

	@Override
	public List<BillItemInfo> findBillItemByBillIdDisk(String billId) {
		Map map =new HashMap();
		map.put("billId", billId);
		return find("findBillItemByBillIdDisk", map);
	}

	@Override
	public BillInfo findBillInfoById(String billId) {
		Map map=new HashMap();
		map.put("billId", billId);
		BillInfo bill=null;
		List list=find("findBillInfoDiskById",map);
		if(list.size()==1){
			bill=(BillInfo) list.get(0);
		}
		return  bill;
	}

	@Override
	public AjaxReturn createBillIssueXml(TaxDiskInfo disk, String billId,String MachineNo) {
		AjaxReturn message=null;
		BillIssue bill=null;
		try {
			BillInfo billInfo=findBillInfoById(billId);
			List<BillItemInfo> list=findBillItemByBillIdDisk(billId);
			List<Goods> goodsList=new ArrayList<Goods>();
			if(list.size()>0){
				for (int i=0;i<list.size();i++){
					BillItemInfo billItem=list.get(i);
					Goods goods=new Goods(billItem);
					goodsList.add(goods);
				}
			}
			bill=new BillIssue(billInfo,disk);
			bill.setGoodslist(goodsList);
			bill.setIssueMachineNo(MachineNo);
					message=new AjaxReturn(true); 
					Map map=new HashMap();
					map.put("StringXml",bill.createBillIssueXml());
					message.setAttributes(map);
				  
		} catch (Exception e) {
			message=new AjaxReturn(false,Message.system_exception_bill_issue_Xml_error); 
			return message;
		}
		return message;
	}
	public String getfapiaoTypeCh(String fapiaoType){
		return fapiaoType=="0"?"004":"007";
	}

	@Override
	public AjaxReturn createBillBalanlCancel(TaxDiskInfo disk,String billCode,String billNo,String fapiaoType,String userId) {
		AjaxReturn message=null;
		try {
				BillInfo bi=new BillInfo();
				bi.setBillCode(billCode);
				bi.setBillNo(billNo);
				bi.setFapiaoType(fapiaoType);
				bi.setSumAmt("");
			BillCancel bill=new BillCancel(bi, disk, userId, TaxDiskUtil.cancel_Type_0);
			String stringXml=bill.createBillCancelXMl();
			message=new AjaxReturn(true); 
			Map map=new HashMap();
			map.put("StringXml",stringXml);
			message.setAttributes(map);
		} catch (Exception e) {
			message=new AjaxReturn(false,Message.system_exception_bill_cancel_Xml_error);
			return message;
		}
		return message;
	}

	@Override
	public AjaxReturn updateBillIssueResult(String billCode, String billNo,
			 String billId,String diskNo,String MachineNo,String userId,String billDate,String returnmsg,boolean falg) {
		AjaxReturn message=null;
		try {
			Map map=new HashMap();
			map.put("billCode", billCode);
			map.put("billNo", billNo);
			map.put("billId", billId);
			map.put("datastatusTwo","5");
			map.put("MachineNo", MachineNo);
			map.put("drawer", userId);
			map.put("billDate", billDate);
			map.put("diskNo", diskNo);
			//更改票据状态
			try {
				if(!falg){
					map.put("datastatus", "7");
					update("updateBillInfoStatusByBillId", map);
					message=new AjaxReturn(false, returnmsg);
					return message;
				}
				
			} catch (Exception e) {
				message=new AjaxReturn(false, Message.system_exception_update_bill_issue_datastauas_error);
				return message;

				}
			update("updateBillInfoForIssueDisk", map);
			//更改交易状态
			List translist=findTransInfoForDisk(billId);
			TransInfo trans=(TransInfo) translist.get(0);
			boolean flag=false;
			//拆分
			
			if(trans.getIssueType().equals(DataUtil.ISSUE_TYPE_3)){
				List billList=findBillInfoListByTransId(trans.getTransId());
				for (Iterator iterator = billList.iterator(); iterator.hasNext();) {
					TransInfo bill = (TransInfo) iterator.next();
					if(bill.getBalance().compareTo(new BigDecimal("0"))==0){
						if(bill.getBillCode().isEmpty()){
							flag=true;
							break;
						}
						
					}else{
							flag=true;
							break;
					}
				}
				if(flag){
					
				}else{
					updateTranDatastatusByTransId(trans.getTransId(),  DataUtil.TRANS_STATUS_99); 
				}
			}else{
				for (Iterator iterator = translist.iterator(); iterator.hasNext();) {
					TransInfo tran = (TransInfo) iterator.next();
					updateTranDatastatusByTransId(tran.getTransId(),  DataUtil.TRANS_STATUS_99);
					
				}
			}
			message=new AjaxReturn(true, "成功");
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_update_bill_issue_datastauas_error);
			e.printStackTrace();
		}
		return message;
		
	}

	@Override
	public List findTransInfoForDisk(String billId) {
		Map map=new HashMap();
		map.put("billId", billId);
		return find("findTranslistByBillIdForDisk", map);
	}

	@Override
	public void updateTranDatastatusByTransId(String transId, String datastatus) {
		Map map =new HashMap();
		map.put("transId",transId );
		map.put("datastatus",datastatus);
		update("updateTranDatastatusByTransId", map);
	}

	@Override
	public List findBillInfoListByTransId(String transId) {
		Map map=new HashMap();
		map.put("transId", transId);
		
		return find("findBillInfoBytransForDisk",map);
	}
	public String findBillDiskByNoAndTypeForBlankCancel(String billCode,String billNo,String fapiaoType){
		Map map=new HashMap();
		map.put("billCode",billCode);
		map.put("billNo",billNo);
		map.put("fapiaoType",fapiaoType);
		map.put("datastus","2" );
		List list=find("findBillDiskByNoAndTypeForBlankCancel", map);
		if(list.size()==1){
			return (String) list.get(0);
		}
		return null;
		
		
	}
	
}
