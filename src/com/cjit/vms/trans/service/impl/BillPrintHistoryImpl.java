package com.cjit.vms.trans.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillPrintHistoryInfo;
import com.cjit.vms.trans.service.BillPrintHistoryService;

public class BillPrintHistoryImpl extends GenericServiceImpl implements
		BillPrintHistoryService {

	/**
	 * 添加补打记录信息
	 */
	@Override
	public int saveBillPrintHistoryInfo(BillPrintHistoryInfo bphInfo) {

		try {
			Map param = new HashMap();
			param.put("billPrintHistoryInfo", bphInfo);
			this.save("saveBillPrintHistory", param);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 查看补打信息 ID
	 */
	@Override
	public BillPrintHistoryInfo findBillPrintHistoryInfoByID(String billID) {
		Map param = new HashMap();
		param.put("billID", billID);
		return (BillPrintHistoryInfo) this.findForObject(
				"findBillPrintHistoryByID", param);

	}
	
	/**
	 * 查看补打信息 ID List
	 */
	@Override
	public List<BillPrintHistoryInfo> findBillPrintHistoryListByID(String billID) {
		Map param = new HashMap();
		param.put("billID", billID.trim());
		List list=find("findBillPrintHistoryList", param);
		return list;
	}

}
