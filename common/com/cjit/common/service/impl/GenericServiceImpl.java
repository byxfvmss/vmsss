package com.cjit.common.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cjit.common.dao.GenericDao;
import com.cjit.common.service.GenericService;
import com.cjit.common.util.PaginationList;

/**
 * 说明:通用DAO实现类（针对Hibernate）
 * 
 * @version
 * @since 2008-6-30 下午11:25:45
 */
public class GenericServiceImpl implements GenericService {

	private GenericDao dao;
	private JdbcTemplate jdbcTemplate;
	protected String configIsCluster;

	public String getConfigIsCluster() {
		return configIsCluster;
	}

	public void setConfigIsCluster(String configIsCluster) {
		this.configIsCluster = configIsCluster;
	}

	public void setDao(GenericDao dao) {
		this.dao = dao;
	}

	public Double findForDouble(String query, Map parameters) {
		return dao.findForDouble(query, parameters);
	}

	public Object findForObject(String query, Map parameters) {
		return dao.findForObject(query, parameters);
	}

	public void delete(String statements, Serializable id) {
		dao.delete(statements, id);
	}

	public void delete(String statements, Map map) {
		dao.delete(statements, map);
	}

	public void deleteAll(String statements, Serializable[] id) {
		dao.deleteAll(statements, id);
	}

	public List find(String query, Map parameters) {
		return dao.find(query, parameters);
	}

	public List find(String hql, Map parameters, PaginationList paginationList) {
		return dao.find(hql, parameters, paginationList);
	}
	
	public List findLargeData(String hql, Map parameters,
			PaginationList paginationList) {
		return dao.findLargeData(hql, parameters, paginationList);
	}

	public Long getResultCount(String statements) {
		return dao.getResultCount(statements);
	}

	public Long getRowCount(String query, Map parameters) {
		return dao.getRowCount(query, parameters);
	}

	public int save(String statements, Map map) {
		return dao.save(statements, map);
	}

	public void update(String statements, Map map) {
		dao.update(statements, map);
	}

	public GenericDao getDao() {
		return dao;
	}

	public void insertBatch(String hql, List data) {
		this.dao.insertBatch(hql, data);
	}

	public void updateBatch(String hql, List data) {
		this.dao.updateBatch(hql, data);
	}
	
	public void updateRptDataBatch(String hql, List data) {
		this.dao.updateBatch(hql, data);
	}
	//删除批处理	
	public void deleteBatch(String hql, List data) {
			this.dao.deleteBatch(hql, data);
		}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
