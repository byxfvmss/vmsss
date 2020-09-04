package com.cjit.vms.input.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.input.model.InputTransInfo;
import com.cjit.vms.input.service.InvoiceDeductionService;
import com.cjit.vms.trans.util.SqlUtil;

public class InvoiceDeductionServiceImpl extends GenericServiceImpl implements InvoiceDeductionService {

	public List findInvoiceDeductionList(InputInvoiceInfo info,
			PaginationList paginationList) {
		Map params = new HashMap();
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());  
		}
//		params.put("auth_inst_ids", SqlUtil.arr2String(lstTmp.toArray(),",")); 
		
		params.put("bill_date", info.getBillDate());
		params.put("vendor_name", info.getVendorName());
		params.put("bill_code", info.getBillCode());
		params.put("fapiao_type", info.getFapiaoType());
		params.put("inst_id", info.getInstcode());
		params.put("bill_no", info.getBillNo());
		params.put("datastatus", info.getDatastatus());
//		params.put("identify_date", info.getIdentifyDate());
		if(paginationList==null){
			return this.find("findInvoiceDeductionList", params);
		}
		return this.find("findInvoiceDeductionList", params, paginationList);
		
	}

	public void saveRollbackInvoiceDeduction(String[] billId) {
		for(int i=0;i<billId.length;i++){
			String o_bill_id=billId[i];
			Map params = new HashMap();
			InputInvoiceInfo inputInvoiceInfo = new InputInvoiceInfo();
			inputInvoiceInfo.setBillId(o_bill_id);
			params.put("info", inputInvoiceInfo); 
			this.update("updateVmsInputInvoiceInfoForDeductionRollback", params);
		}
	}

	public InputInvoiceInfo findInvoiceDeductionByBillId(String billId) {
		Map params = new HashMap();
		params.put("bill_id", billId);
		return (InputInvoiceInfo) this.findForObject("findInputInvoiceInfoForDeductionByBillId", params);
	}

	public InputTransInfo findInvoiceDeductionTransInfoByBillCodeAndBillNo(
			String bill_code, String bill_no) {
		Map params = new HashMap();
		params.put("bill_code", bill_code);
		params.put("bill_no", bill_no);
		return (InputTransInfo) this.findForObject("findInvoiceDeductionTransInfoByBillCodeAndBillNo", params);
	}

	public List findInvoiceDeductionItemsByBillId(String billId) {
		HashMap params=new HashMap();
		params.put("bill_id", billId);
		return  this.find("findVmsInputInvoiceItemsByBillId", params);
	}

	public void updateVmsInputInvoiceInfoForDeduction(
			InputInvoiceInfo inputInvoiceInfo, String o_bill_id) {
		Map params = new HashMap();
		inputInvoiceInfo.setBillId(o_bill_id);
		params.put("info", inputInvoiceInfo); 
		this.update("updateVmsInputInvoiceInfoForDeduction", params);
	}
	
	public List findInvoiceDeductionListNew(InputInvoiceNew info,
			PaginationList paginationList){
		Map params = new HashMap();
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
//		for(int i=0;i<instIds.size();i++){
//			Organization org=(Organization)instIds.get(i);
//			lstTmp.add(org.getId());  
//		}
//		params.put("auth_inst_ids", SqlUtil.arr2String(lstTmp.toArray(),",")); 
		params.put("bill_date", info.getBillDate());
		params.put("vendor_name", info.getVendorName());
		params.put("bill_code", info.getBillCode());
//		params.put("fapiao_type", info.getFapiaoType());
		params.put("industry_type", info.getIndustryType());
		params.put("bill_no", info.getBillNo());
		params.put("dataStatus", info.getDataStatus());
//		params.put("identify_date", info.getIdentifyDate());
		if(paginationList==null){
			return this.find("findInvoiceDeductionListNew", params);
		}
		return this.find("findInvoiceDeductionListNew", params, paginationList);
	}
	
	public InputInvoiceNew findInputTransByIdCodeNoAndNo(String billNoAndBillCode){
		Map map = new HashMap();
		map.put("no_code", billNoAndBillCode);
		List list = find("findByBillNoBillCodeDeduc", map);
		if (list != null && list.size() > 0){
			InputInvoiceNew inputInvoiceNew = (InputInvoiceNew) list.get(0);
			return inputInvoiceNew;
		}
		return null;
	}
	
	public void updateInvoiceInfoForDeductionNew(Map idMap){
		this.update("updateInvoiceInfoForDeductionNew", idMap);
		this.update("updateInvoiceInfoForDeductionNewTemp", idMap);
		
		
	}
	
	public void saveInputInvoice(Map<String, List> map) {
		List<InputInvoiceNew> list = map.get("insertList");
		Map inputInvoiceMap = new HashMap<String, String>();
		for(InputInvoiceNew inputInvoiceNew:list){
			inputInvoiceMap.put("porpuse_code",inputInvoiceNew.getPorpuseCode());
			inputInvoiceMap.put("inst_id",inputInvoiceNew.getInstId());
			inputInvoiceMap.put("inst_name", inputInvoiceNew.getInstName());
			inputInvoiceMap.put("bill_code", inputInvoiceNew.getBillCode());
			inputInvoiceMap.put("bill_no", inputInvoiceNew.getBillNo());
			inputInvoiceMap.put("amt", inputInvoiceNew.getAmt());
			inputInvoiceMap.put("tax_rate", inputInvoiceNew.getTaxRate());
			inputInvoiceMap.put("tax_amt", inputInvoiceNew.getTaxAmt());
			inputInvoiceMap.put("amt_tax_sum", inputInvoiceNew.getAmtTaxSum());
			inputInvoiceMap.put("data_status", inputInvoiceNew.getDataStatus());
			inputInvoiceMap.put("direction_id", inputInvoiceNew.getDirectionId());
			inputInvoiceMap.put("transfer_amt", inputInvoiceNew.getTransferAmt());
			inputInvoiceMap.put("deduc_amt", inputInvoiceNew.getDeducAmt());
			this.save("saveInputInvoiceDeducNew", inputInvoiceMap);
		}

	}
	
	// 获取行业类型
	public List findIndustryTypeList()
	{
		Map map = new HashMap();
		List list = find("industryTypeList", map);
		return list;
	}
	
}
