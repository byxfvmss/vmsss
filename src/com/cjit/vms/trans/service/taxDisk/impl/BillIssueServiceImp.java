package com.cjit.vms.trans.service.taxDisk.impl;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.trans.model.taxDisk.BillCancel;
import com.cjit.vms.trans.service.taxDisk.BillIssueDiskService;
import com.cjit.vms.trans.util.taxDisk.TaxDiskUtil;
public class BillIssueServiceImp extends GenericServiceImpl implements BillIssueDiskService{
	public String createBillIssueXml(Map map){
		 List list=find("findPaperInvoiceForBlankCancel", map);
		 BillCancel billCancel=null;
		 String StringXml="";
		 if(list.size()==1){
			 billCancel=(BillCancel) list.get(0);
		 }
		if(billCancel==null){
				StringXml="";
		}else{
			
		}
		return "";
	}
	/**
	 * @return 作废的服务 
	 * @throws Exception 
	 */
	public String  CreateBlankCancelXml(BillCancel bill) throws Exception{
		
		bill.setComment(TaxDiskUtil.comment_bill_Canccel);
		bill.setId(TaxDiskUtil.id_bill_Canccel);
		bill.setApplyTypeCode(TaxDiskUtil.Application_type_code_1);
		return  bill.createBillCancelXMl();
	}
	
}
