package com.cjit.vms.trans.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.SpecialBillWithhold;


public interface SpecialBillService {

	public List selectSpecialBillWithhold(Map parameters,PaginationList paginationList);

	public List selectSpecialbillDeduction(SpecialBillWithhold specialBillWithhold,PaginationList paginationList);
	public List selectSpecialbillDeductionExport(SpecialBillWithhold specialBillWithhold);
	public void insertSpecialBillWithhold(List parameters);
	public void updateSpecialBillWithholdStatus(Map parameters);
	public void updateSpecialBillWithhold(Map parameters);

	public void updateSpecialbillDeduction(Map parameters);

}
