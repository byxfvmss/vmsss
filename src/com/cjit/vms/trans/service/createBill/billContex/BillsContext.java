package com.cjit.vms.trans.service.createBill.billContex;

import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.trans.util.DataUtil;

public class BillsContext {

	List<BillContext> billListZhuanPiao = new ArrayList<BillContext>();
	List<BillContext> billListPuPiao = new ArrayList<BillContext>();

	public List<BillContext> getBillListByFapiaoType(String fapiaoType) {
		if (DataUtil.VAT_TYPE_0.equals(fapiaoType)) {
			if (null == billListZhuanPiao) {
				billListZhuanPiao = new ArrayList<BillContext>();
			}
			return billListZhuanPiao;
		}
		if (DataUtil.VAT_TYPE_1.equals(fapiaoType)) {
			if (null == billListPuPiao) {
				billListPuPiao = new ArrayList<BillContext>();
			}
			return billListPuPiao;
		}
		return new ArrayList<BillContext>();
	}

	public List<BillContext> getAllBillList() {
		List<BillContext> list = new ArrayList<BillContext>();
		list.addAll(getBillListByFapiaoType(DataUtil.VAT_TYPE_0));
		list.addAll(getBillListByFapiaoType(DataUtil.VAT_TYPE_1));
		return list;
	}
}
