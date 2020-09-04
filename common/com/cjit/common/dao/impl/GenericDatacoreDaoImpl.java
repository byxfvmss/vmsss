package com.cjit.common.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cjit.common.dao.GenericDatacoreDao;

public class GenericDatacoreDaoImpl extends SqlMapClientDaoSupport implements
		GenericDatacoreDao {

	public List find(String query, Map parameters) {
		List list = this.getSqlMapClientTemplate().queryForList(query,
				parameters);
		return list;
	}
}
