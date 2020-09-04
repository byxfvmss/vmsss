package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.system.model.InitRunningLog;
import com.cjit.vms.system.service.SystemDataService;

public class SystemDataServiceImpl extends GenericServiceImpl implements
		SystemDataService {

	// 使用jdbc进行sql处理
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public int[] insertDataBatch(String[] sqls) {
		int[] ints = this.jdbcTemplate.batchUpdate(sqls);
		return ints;
	}
	
	public Long findInitLogCount(InitRunningLog runLog){
		Map param = new HashMap();
		param.put("runLog", runLog);
		return this.getRowCount("findInitLogCount", param);
	}

	public List findInitLogList(InitRunningLog runLog){
		Map param = new HashMap();
		param.put("runLog", runLog);
		return this.find("findInitLogList", param);
	}

	public void saveInitLog(InitRunningLog runLog){
		Map param = new HashMap();
		param.put("runLog", runLog);
		this.save("saveInitLog", param);
	}
	
	public void updateFreeData(String tableId, String updateColumns,
			String updateCondition){
		Map param = new HashMap();
		param.put("tableId", tableId);
		param.put("updateColumns", updateColumns);
		param.put("updateCondition", updateCondition);
		this.save("updateFreeData", param);
	}
}
