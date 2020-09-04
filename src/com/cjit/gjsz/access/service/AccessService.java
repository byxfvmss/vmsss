package com.cjit.gjsz.access.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.GenericService;

public interface AccessService extends GenericService{

	/**
	 * 返回实际数据
	 * @param tableId
	 * @param orgCode
	 * @param beginDate
	 * @param endDate
	 * @param metaData
	 * @return
	 */
	public List expFromSqlServer(String selectSql);

	/**
	 * 返回insert语句
	 * @param columnString
	 * @param tableId
	 * @param contentMap
	 * @param typeString
	 * @return
	 */
	public String getInsertSql(String columnString, String tableId,
			Map contentMap, String typeString);

	/**
	 * 根据表名来获取该表的元数据 返回 列名和列数据类型
	 * @param tableId
	 * @return
	 */
	public String[] getMetaData(String tableId);

	/**
	 * 返回select语句
	 * @param columns
	 * @param tableId
	 * @param conditions
	 * @return
	 */
	public String getSelectSql(String columns, String tableId, List orgIds,
			String beginDate, String endDate, String parentSql);

	public boolean updateData(String sql);
}
