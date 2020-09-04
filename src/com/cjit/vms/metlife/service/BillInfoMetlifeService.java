package com.cjit.vms.metlife.service;

import java.util.List;

import com.cjit.vms.trans.model.BillInfo;

public interface BillInfoMetlifeService {

	public List findTransInfoByBillInfo(String billId);
}
