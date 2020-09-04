package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.BillPrintHistoryInfo;

public interface BillPrintHistoryService {

	// 添加补打记录
	public int saveBillPrintHistoryInfo(BillPrintHistoryInfo bphInfo);

	// 根据ID查看补打信息
	public BillPrintHistoryInfo findBillPrintHistoryInfoByID(String billID);

	// 根据ID查看补打信息
	public List<BillPrintHistoryInfo> findBillPrintHistoryListByID(String billID);

}
