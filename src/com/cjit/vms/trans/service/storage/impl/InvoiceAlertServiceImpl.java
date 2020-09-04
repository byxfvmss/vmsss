package com.cjit.vms.trans.service.storage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.storage.InvoiceAlertListInfo;
import com.cjit.vms.trans.service.storage.InvoiceAlertService;
import com.cjit.vms.trans.util.SqlUtil;

public class InvoiceAlertServiceImpl extends GenericServiceImpl implements InvoiceAlertService {

	public List findInvoiceAlertList(InvoiceAlertListInfo info,
			PaginationList paginationList) {
		
		Map param = new HashMap();
		param.put("invoice_type", info.getInvoiceType());
		if(StringUtils.isNotEmpty(info.getInstId())){
			List list =  new ArrayList();
			list.add(info.getInstId());
			param.put("auth_inst_ids", list);
		}else{
			List lstTmp=new ArrayList();
			List instIds=info.getLstAuthInstId();
			for(int i=0;i<instIds.size();i++){
				Organization org=(Organization)instIds.get(i);
				lstTmp.add(org.getId());
			}
			param.put("auth_inst_ids", lstTmp);
		}
		return this.find("findInvoiceAlertList",param , paginationList);
	}

	public InvoiceAlertListInfo findInstInvoiceAlert(String inst_id,
			String invoice_type) {
		Map param = new HashMap();
		param.put("inst_id", inst_id);
		param.put("invoice_type", invoice_type);
		return (InvoiceAlertListInfo) this.findForObject("findInstInvoiceAlert", param);
	}

	public void saveInstInvoiceAlertValue(String inst_id, String invoice_type,
			Integer alert_num) {
		Map param = new HashMap();
		param.put("inst_id", inst_id);
		param.put("invoice_type", invoice_type);
		param.put("alert_num", alert_num);
		Object info=this.findForObject("findInstInvoiceAlertForCheckExist", param);
		if(info==null){
			this.save("saveInstInvoiceAlertValue", param);
		}else{
			this.update("updateInstInvoiceAlertValue", param);
		}
	}
}
