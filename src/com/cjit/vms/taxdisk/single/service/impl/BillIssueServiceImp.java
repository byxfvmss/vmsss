package com.cjit.vms.taxdisk.single.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.parseXml.BillIssueReturnXML;
import com.cjit.vms.taxdisk.single.model.parseXml.BuyBillInfoQueryReturnXML;
import com.cjit.vms.taxdisk.single.service.BillIssueDiskAssitService;
import com.cjit.vms.taxdisk.single.service.BillIssueDiskService;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;
public class BillIssueServiceImp extends GenericServiceImpl implements BillIssueDiskService{
	private TaxDiskInfoQueryService taxDiskInfoQueryService;
	private BillIssueDiskAssitService billIssueDiskAssitService;
	@Override
	public long findBillBalanceCancelNum(String diskNo, String fapiaoType) {
		 Map map =new HashMap();
		 map.put("diskNo", diskNo);
		 map.put("fapiaoType", fapiaoType);
		 long num=getRowCount("findBillSurCancelNum", map);
		return num;
	}

	@Override
	public boolean checkStockNum(String diskNo, String fapiaoType,int num) {
		if(findBillBalanceCancelNum(diskNo,fapiaoType)>num){
			return true;
		}
		return false;
	}

	@Override
	public String createBillIssueXml(String StringXml,String diskNo,String billId,String userId) throws Exception {
		BuyBillInfoQueryReturnXML bill=new BuyBillInfoQueryReturnXML(StringXml);
		 String fapiaoType=bill.getFapiaoType()=="004"?"0":"1";
		String billCode=findBillDiskByNoAndTypeForBlankCancel(bill.getCurBillCode(), bill.getCurBillNo(), fapiaoType);
		TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo(diskNo);

		if(billCode.isEmpty()){
		//	billIssueDiskAssitService.createBillIssueXml(disk, billId);
		}else{
			billIssueDiskAssitService.createBillBalanlCancel(disk, bill.getCurBillCode(), bill.getCurBillNo(), fapiaoType, userId);
		}
		return null;
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
	
	public TaxDiskInfoQueryService getTaxDiskInfoQueryService() {
		return taxDiskInfoQueryService;
	}

	public void setTaxDiskInfoQueryService(
			TaxDiskInfoQueryService taxDiskInfoQueryService) {
		this.taxDiskInfoQueryService = taxDiskInfoQueryService;
	}

	public BillIssueDiskAssitService getBillIssueDiskAssitService() {
		return billIssueDiskAssitService;
	}

	public void setBillIssueDiskAssitService(
			BillIssueDiskAssitService billIssueDiskAssitService) {
		billIssueDiskAssitService = billIssueDiskAssitService;
	}

	@Override
	public void updateBillIssueXml(String StringXml,String id) throws Exception {
		BillIssueReturnXML bill=new BillIssueReturnXML(StringXml);
		String falg=bill.getId();
		if (falg.equals(TaxDiskUtil.id_bill_issue)) {
			
		} else {

		}
		
	}
	
}
