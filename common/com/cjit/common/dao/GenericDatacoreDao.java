package com.cjit.common.dao;

import java.util.List;
import java.util.Map;

public interface GenericDatacoreDao {

	/**
	 * 根据预设SQL查询结果集
	 * @param query sql语句
	 * @param parameters 要传的参数集合
	 * @return List 查询结果集合
	 */
	public List find(final String query, final Map parameters);
}
