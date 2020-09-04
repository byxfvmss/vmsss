package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.ParamInSurtaxListInfo;
import com.cjit.vms.trans.service.ParamInSurtaxService;
import com.cjit.vms.trans.util.SqlUtil;

public class ParamInSurtaxServiceImpl extends GenericServiceImpl implements ParamInSurtaxService {
	
	public List findParamInSurtaxListInfo(ParamInSurtaxListInfo info,PaginationList paginationList){
		Map params = new HashMap();
		params.put("data_dt", info.getDataDt());
		params.put("taxpayer_id", info.getTaxpayerId());
		params.put("inst_id", info.getInstId());
		params.put("inst_name", info.getInstName());
		params.put("taxpayer_name", info.getTaxpayerName());
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		params.put("auth_inst_ids", lstTmp); 
		if(null==paginationList){
			return this.find("findParamInSurtaxListInfo", params);
		}
		return this.find("findParamInSurtaxListInfo", params, paginationList);
	}

	public ParamInSurtaxListInfo findParamInSurtaxItemInfo(
			ParamInSurtaxListInfo info) {
		Map params = new HashMap();
		params.put("inst_id", info.getInstId());
		return (ParamInSurtaxListInfo) this.findForObject("findParamInSurtaxItemInfo", params);
	}

	public void saveParamInSurtaxInfo(ParamInSurtaxListInfo info) {
		Map params = new HashMap();
		params.put("vat_out_proportion", info.getVatOutProportion());
		params.put("taxpayer_id", info.getTaxpayerId());
		this.update("updateParamInSurtaxInfoVatOutProportion", params);
	}
}
