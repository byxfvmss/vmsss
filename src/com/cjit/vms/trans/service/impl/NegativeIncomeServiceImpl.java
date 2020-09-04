package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.service.NegativeIncomeService;

public class NegativeIncomeServiceImpl extends GenericServiceImpl implements
		NegativeIncomeService {

	public List findTransInfoForNegativeIncome(TransInfo transInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds=transInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("transInfo", transInfo);

		return find("findTransInfoForNegativeIncome", map, paginationList);
	}

	public List findTransInfoForNegativeIncomeExport(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		map.put("export", "export");
		List instIds=transInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);


		return find("findTransInfoForNegativeIncomeExport", map);
	}

	public List findTransInfoForNegativeIncomeDetail(TransInfo transInfo, PaginationList paginationList) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		List instIds=transInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);

		return find("findTransInfoForNegativeIncomeExport", map, paginationList);
	}

	public void offsetTrans(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		
		update("offsetTrans", map);
	}

	public void cancelNegativeIncome(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		
		update("cancelNegativeIncome", map);
	}
	
	public void saveTransNegaInfo(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		
		save("insertTransNegaInfo", map);
	}
	
	public List findAllCustomer() {
		
		return find("findCustomer", new HashMap());
	}

	public Customer findCustomerByTaxno(String customerTaxno) {
		Map map = new HashMap();
		map.put("customerTaxno", customerTaxno);
		List list = find("findCustomer", map);
		if (list!=null && list.size()>0) {
			return (Customer)list.get(0);
		}
		return null;
	}

}
