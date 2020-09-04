package com.cjit.vms.input.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputTrans;
import com.cjit.vms.input.service.InputInnovationService;
import com.cjit.vms.trans.model.BillItemInfo;

public class InputInnovationServiceImpl extends GenericServiceImpl implements InputInnovationService {

	/* (non-Javadoc) 返回钩稽列表详情
	 * @see com.cjit.vms.trans.service.input.InputInnovationService#findInnovationList(com.cjit.vms.trans.model.InputInvoiceInfo, com.cjit.common.util.PaginationList)
	 */
	public List findInnovationList(InputInvoiceInfo inputInvoiceInfo,PaginationList paginationList) {
		Map map =new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		List instIds=inputInvoiceInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		return find("findInputInnovation",map,paginationList);
	}

	public List findInnovationList(InputInvoiceInfo inputInvoiceInfo) {
		Map map =new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		List instIds=inputInvoiceInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		return find("findInputInnovation",map);
		}
	public List findBillItemInfoList(BillItemInfo billItemInfo) {
		Map map = new HashMap();
		map.put("billItem", billItemInfo);
		return find("findBillItemInfo", map);
	}
	public InputInvoiceInfo findInputInvoiceInfo(InputInvoiceInfo input) {
		Map map =new HashMap();
		map.put("billId", input.getBillId());
		List instIds=input.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		List list=find("findInputInnovation",map);
		InputInvoiceInfo  inputInvoiceInfo=new InputInvoiceInfo();
		if(list!=null){
			 inputInvoiceInfo=(InputInvoiceInfo)list.get(0);
		}
		return inputInvoiceInfo;
	}

	public List findInputInvoiceList(String billId,String vendId) {
		Map map=new HashMap();
		map.put("billId",billId);
		map.put("vendId", vendId);
		return find("findInnovationbyVendorId",map);
	}

	public void deleteInputInvoice(String billId, String dealNo) {
		Map map=new HashMap();
		map.put("billId", billId);
		map.put("dealNo", dealNo);
		delete("deleteTransInvoice", map);
	}

	/* (non-Javadoc)
	 * return wei
	 */
	public List findInputNOInvoiceList(String billId, String vendId,String dealNo,String bankCode,PaginationList paginationList) {
		Map map=new HashMap();
		map.put("vendId", vendId);
		map.put("billId", billId);
		map.put("dealNo", dealNo);
		map.put("bankCode", bankCode);
		return find("findInnovationNobyVendorId",map,paginationList);
	}

	public void insertInputInvoice(String billId, String dealNo) {
		Map map=new HashMap();
		map.put("billId", billId);
		map.put("dealNo", dealNo);
		save("insertTransInvoice", map);
	}

	public InputTrans findInputTransById(String billId, String dealNo) {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		map.put("billId", billId);
		map.put("dealNo", dealNo);
		List list=find("findInputtrnsById", map);
		InputTrans inputTrans =new InputTrans();
		if(list.size()>0){
			inputTrans=(InputTrans)list.get(0);
		}
		return inputTrans;
	}

	public void updateInputTrans(String billId,String status) {
		Map map=new HashMap();
		map.put("billId", billId);
		map.put("status", status);
		update("updateInputTransAudit", map);
		// TODO Auto-generated method stub
		
	}

}
