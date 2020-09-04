package com.cjit.gjsz.interfacemanager.service.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.interfacemanager.service.DataCoreDataService;

public class DataCoreDataServiceImpl extends GenericServiceImpl implements
		DataCoreDataService {

	// 使用jdbc进行sql处理
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List findSourceDataList(String sql, Object[] args) {
		List list = this.jdbcTemplate.queryForList(sql, args);
		return list;
	}
}
