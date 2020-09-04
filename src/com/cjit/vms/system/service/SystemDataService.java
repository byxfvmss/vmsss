package com.cjit.vms.system.service;

import java.util.List;

import com.cjit.vms.system.model.InitRunningLog;

public interface SystemDataService {

	public int[] insertDataBatch(String[] sqls);

	public Long findInitLogCount(InitRunningLog runLog);

	public List findInitLogList(InitRunningLog runLog);

	public void saveInitLog(InitRunningLog runLog);

	public void updateFreeData(String tableId, String updateColumns,
			String updateCondition);
}
