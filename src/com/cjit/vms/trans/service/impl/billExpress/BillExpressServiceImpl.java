package com.cjit.vms.trans.service.impl.billExpress;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.billExpress.BillExpress;
import com.cjit.vms.trans.service.billExpress.BillExpressService;

public class BillExpressServiceImpl extends GenericServiceImpl implements
		BillExpressService {

	@Override
	public List selectBillExpress(BillExpress billExpress,List authInstIds,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billExpress", billExpress);
		map.put("auth_inst_ids", authInstIds);
		if (null != paginationList) {
			find("selectBillExpress", map,paginationList);
		}
		return 	find("selectBillExpress", map);
	}

	@Override
	public void updateBillExpressInfo(BillExpress billExpress) {
		Map map = new HashMap();
		map.put("billExpress", billExpress);
		update("updateBillExpressInfo", map);
	}
	
	@Override
	public void updateBillExpressInfoList(List<BillExpress> billExpressList) {

		for (int i = 0; i <billExpressList.size(); i++) {
			Map map = new HashMap();
			map.put("billExpress", billExpressList.get(i));
			update("updateBillExpressInfo", map);
		}
		
	}
	
	@Override
	public void updateBillExpressInfoFinish(List<BillExpress> billExpressList) {
		for (int i = 0; i <billExpressList.size(); i++) {
			Map map = new HashMap();
			map.put("billExpress", billExpressList.get(i));
			update("updateBillExpressInfoFinish", map);
		}

		
	}
}
