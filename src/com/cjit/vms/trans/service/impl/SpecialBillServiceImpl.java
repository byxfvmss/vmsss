package com.cjit.vms.trans.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;

import com.cjit.vms.input.model.SpecialBillWithhold;
import com.cjit.vms.trans.service.SpecialBillService;

public class SpecialBillServiceImpl extends GenericServiceImpl implements
		SpecialBillService {

	@Override
	public List selectSpecialBillWithhold(Map parameters,
			PaginationList paginationList) {
		if (null != paginationList) {
			return find("selectSpecialBillWithhold", parameters, paginationList);
		}

		return find("selectSpecialBillWithhold", parameters);
	}
	
	public List selectSpecialbillDeduction(SpecialBillWithhold specialBillWithhold,PaginationList paginationList){
		Map map = new HashMap();		
		map.put("taxNo", specialBillWithhold.getTaxNo());
		map.put("writeData", specialBillWithhold.getWriteData());
		map.put("payData", specialBillWithhold.getPayData());
		map.put("billNo", specialBillWithhold.getBillNo());
		map.put("taxInstChn", specialBillWithhold.getTaxInstChn());
		map.put("dataStatus", specialBillWithhold.getDataStatus());
		return find("findSpecialbillDeduction", map, paginationList);
	}
	public List selectSpecialbillDeductionExport(SpecialBillWithhold specialBillWithhold){
		Map map=new HashMap();
		
		return  find("selectSpecialbillDeductionExport",map);
	}

	@Override
	public void insertSpecialBillWithhold(List parameters) {
		if (null != parameters) {
			for (int i = 0; i < parameters.size(); i++) {
				Map bean = (Map) parameters.get(i);
				save("insertSpecialBillWithhold", bean);
			}
		}
	}

	@Override
	public void updateSpecialBillWithholdStatus(Map parameters) {
		update("updateSpecialBillWithholdStatus", parameters);
	}

	@Override
	public void updateSpecialBillWithhold(Map parameters) {
		update("updateSpecialBillWithhold", parameters);
	}
	
	public void updateSpecialbillDeduction(Map parameters){
		update("updateSpecialBillDeduction", parameters);
	}


}
