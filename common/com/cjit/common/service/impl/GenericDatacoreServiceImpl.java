package com.cjit.common.service.impl;

import java.util.List;
import java.util.Map;

import com.cjit.common.dao.GenericDatacoreDao;
import com.cjit.common.service.GenericDatacoreService;

public class GenericDatacoreServiceImpl implements GenericDatacoreService {

	protected String dbType = ""; // 数据库类型标志
	private GenericDatacoreDao dao;

	public void setDao(GenericDatacoreDao dao) {
		this.dao = dao;
	}

	public List find(final String query, final Map parameters) {
		return dao.find(query, parameters);
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
}
