package com.cjit.vms.input.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.service.InvoiceSurtaxService;

public class InvoiceSurtaxServiceImpl extends GenericServiceImpl  implements InvoiceSurtaxService {

	public List findInvoiceInSurtaxList(InputInvoiceInfo info,
			PaginationList paginationList) {
		Map parameters = new HashMap();
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
//		parameters.put("auth_inst_ids", SqlUtil.list2String(lstTmp, ",")); 
		parameters.put("auth_inst_ids", lstTmp); 
		
		parameters.put("datastatus", info.getDatastatus());
		if(null!=info.getBillDate()){
			parameters.put("bill_date", info.getBillDate());
		}
		parameters.put("vendor_name", info.getVendorName());
		parameters.put("bill_code", info.getBillCode());
		parameters.put("bill_no", info.getBillNo());
		parameters.put("fapiao_type", info.getFapiaoType());
		parameters.put("inst_id", info.getInstcode());
		parameters.put("vat_out_amt", info.getVatOutAmt());
		
		if(null==paginationList){
			return this.find("findInvoiceInSurtaxList", parameters);
		}
		return this.find("findInvoiceInSurtaxList", parameters, paginationList);
	}

	public InputInvoiceInfo findVmsInputInvoiceInfoByBillId(String bill_id) {
		HashMap params=new HashMap();
		params.put("bill_id", bill_id);
		return (InputInvoiceInfo) this.findForObject("findVmsInputInvoiceInfoByBillId", params);
	}

	public List findVmsInputInvoiceItemsByBillId(String bill_id) {
		HashMap params=new HashMap();
		params.put("bill_id", bill_id);
		return  this.find("findVmsInputInvoiceItemsByBillId", params);
	}

	public void updateVmsInputInvoiceInfoVatOut(String vat_out_amt,
			String vat_out_proportion, String remark,String bill_id) {
		HashMap params=new HashMap();
		params.put("vat_out_amt", vat_out_amt);
		params.put("vat_out_proportion", vat_out_proportion);
		params.put("remark", remark);
		params.put("bill_id", bill_id);
		this.update("updateVmsInputInvoiceInfoVatOut", params);
	}

	public void updateVmsInputInvoiceInfoDatastatus(String[] bill_id,
			String datastatus) {
		if(null==bill_id){
			return;
		}
		for(int i=0;i<bill_id.length;i++){
			String item_bill_id=bill_id[i];
			Map params=new HashMap();
			params.put("datastatus", datastatus);
			params.put("bill_id", item_bill_id);
			this.update("updateVmsInputInvoiceInfoDatastatus", params);
		}
	}
	
	public void updateVmsInputInvoiceInfoForRollBack(String[] billIds) {
		if(null==billIds){
			return;
		}
		for(int i=0;i<billIds.length;i++){
			String item_bill_id=billIds[i];
			Map params=new HashMap();
			params.put("bill_id", item_bill_id);
			this.update("updateVmsInputInvoiceInfoForRollBack", params);
		}
	}
	public void updateVmsInputInvoiceInfoForBatchRollOut(String[] billIds) {
		if(null==billIds){
			return;
		}
		for(int i=0;i<billIds.length;i++){
			String item_bill_id=billIds[i];
			Map params=new HashMap();
			params.put("bill_id", item_bill_id);
			this.update("updateVmsInputInvoiceInfoForBatchRollOut", params);
		}
	}
	
	
}
