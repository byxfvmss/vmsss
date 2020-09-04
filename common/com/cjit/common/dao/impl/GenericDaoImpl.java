package com.cjit.common.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cjit.common.dao.GenericDao;
import com.cjit.common.util.PaginationList;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 说明:通用DAO实现类（针对Hibernate）
 * 
 * @version
 * @since 2008-6-30 下午11:25:45
 */
public class GenericDaoImpl extends SqlMapClientDaoSupport implements
		GenericDao {

	public int save(String statements, Map map) {
		Integer id = (Integer) this.getSqlMapClientTemplate().insert(
				statements, map);
		if (id != null) {
			return id.intValue();
		} else {
			return 0;
		}
	}

	public void update(String statements, Map map) {
		this.getSqlMapClientTemplate().update(statements, map);
	}

	public void delete(String statements, Serializable id) {
		this.getSqlMapClientTemplate().delete(statements, id);
	}

	public void delete(String statements, Map map) {
		this.getSqlMapClientTemplate().delete(statements, map);
	}

	public void deleteAll(String statements, Serializable[] ids) {
		if (ids != null && ids.length > 0) {
			int size = ids.length;
			for (int i = size - 1; i >= 0; i++) {
				delete(statements, ids[i]);
			}
		}
	}

	public List find(final String hql, final Map parameters,
			final PaginationList paginationList) {
		int startIndex = (paginationList.getCurrentPage() - 1)
				* paginationList.getPageSize();
		List list = this.getSqlMapClientTemplate().queryForList(hql,
				parameters, startIndex, paginationList.getPageSize());
		Long count;
		if ("true".equals(paginationList.getShowCount())) {
			count = this.getRowCount(hql + "Count", parameters);
		} else {
			count = 10000L;
		}
		paginationList.setRecordCount(count.longValue());
		paginationList.setRecordList(list);
		if (list == null || list.size() == 0) {
			long recordCount = paginationList.getRecordCount();
			long pageSize = paginationList.getPageSize();
			long currentPage = paginationList.getCurrentPage();
			long nCount = recordCount % pageSize;
			long maxPage = 0;
			if (nCount == 0) {
				maxPage = recordCount / pageSize;
			} else {
				maxPage = (recordCount - nCount) / pageSize + 1;
			}
			if (currentPage > maxPage) {
				currentPage = maxPage;
				paginationList.setCurrentPage(Integer.valueOf(
						String.valueOf(currentPage)).intValue());
				startIndex = (paginationList.getCurrentPage() - 1)
						* paginationList.getPageSize();
				list = this.getSqlMapClientTemplate().queryForList(hql,
						parameters, startIndex, paginationList.getPageSize());
				count = this.getRowCount(hql + "Count", parameters);
				paginationList.setRecordCount(count.longValue());
				paginationList.setRecordList(list);
			}
		}
		return list;
	}

	public List findLargeData(final String hql, final Map parameters,
			final PaginationList paginationList) {
		int startIndex = (paginationList.getCurrentPage() - 1)
				* paginationList.getPageSize();
		if (startIndex < 0) {
			startIndex = 0;
			paginationList.setCurrentPage(1);
		}
		// List list = this.getSqlMapClientTemplate().queryForList(hql,
		// parameters, startIndex, paginationList.getPageSize());
		Map para = new HashMap();
		para.putAll(parameters);
		para.put("startIndex", String.valueOf(startIndex));
		para.put("endIndex",
				String.valueOf(startIndex + paginationList.getPageSize()));
		List list = this.getSqlMapClientTemplate().queryForList(hql, para);
		Long count;
		if ("true".equals(paginationList.getShowCount())) {
			count = this.getRowCount(hql + "Count", parameters);
		} else {
			count = 10000L;
		}
		paginationList.setRecordCount(count.longValue());
		paginationList.setRecordList(list);
		if (list == null || list.size() == 0) {
			long recordCount = paginationList.getRecordCount();
			long pageSize = paginationList.getPageSize();
			long currentPage = paginationList.getCurrentPage();
			long nCount = recordCount % pageSize;
			long maxPage = 0;
			if (nCount == 0) {
				maxPage = recordCount / pageSize;
			} else {
				maxPage = (recordCount - nCount) / pageSize + 1;
			}
			if (currentPage > maxPage) {
				currentPage = maxPage;
				paginationList.setCurrentPage(Integer.valueOf(
						String.valueOf(currentPage)).intValue());
				startIndex = (paginationList.getCurrentPage() - 1)
						* paginationList.getPageSize();
				list = this.getSqlMapClientTemplate().queryForList(hql,
						parameters, startIndex, paginationList.getPageSize());
				count = this.getRowCount(hql + "Count", parameters);
				paginationList.setRecordCount(count.longValue());
				paginationList.setRecordList(list);
			}
		}
		return list;
	}

	public Object get(String statements, Serializable id) {
		return null;
	}

	public List find(String query, Map parameters) {
		List list = this.getSqlMapClientTemplate().queryForList(query,
				parameters);
		return list;
	}

	public Long getResultCount(String statements) {
		return (Long) this.getSqlMapClientTemplate().queryForObject(statements);
	}

	public Long getRowCount(String query, Map parameters) {
		return (Long) this.getSqlMapClientTemplate().queryForObject(query,
				parameters);
	}

	public void insertBatch(final String statements, final List data) {
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor excutor)
					throws SQLException {
				excutor.startBatch();
				for (int i = 0; i < data.size(); i++) {
					excutor.insert(statements, data.get(i));
				}
				excutor.executeBatch();
				return null;
			}
		});
	}

	public void updateBatch(final String statements, final List data) {
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor excutor)
					throws SQLException {
				excutor.startBatch();
				for (int i = 0; i < data.size(); i++) {
					excutor.update(statements, data.get(i));
				}
				excutor.executeBatch();
				return null;
			}
		});
	}

	public Double findForDouble(String query, Map parameters) {
		return (Double) this.getSqlMapClientTemplate().queryForObject(query,
				parameters);
	}

	public Object findForObject(String query, Map parameters) {
		return this.getSqlMapClientTemplate().queryForObject(query, parameters);
	}
	//增加了删除批处理
	public void deleteBatch(final String statements, final List data) {
			this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
				public Object doInSqlMapClient(SqlMapExecutor excutor)
				throws SQLException {
					excutor.startBatch();
					for (int i = 0; i < data.size(); i++) {
						excutor.delete(statements, data.get(i));
					}
					excutor.executeBatch();
					return null;
				}
			});
		}
}
