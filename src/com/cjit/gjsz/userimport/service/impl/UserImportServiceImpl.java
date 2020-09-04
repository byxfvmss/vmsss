package com.cjit.gjsz.userimport.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.userimport.service.UserImportService;

public class UserImportServiceImpl extends GenericServiceImpl implements
		UserImportService{

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void delete(String statements, Serializable id){
	}

	public void delete(String statements, Map map){
	}

	public void deleteAll(String statements, Serializable[] id){
	}

	public List find(String query, Map parameters){
		return super.find(query, parameters);
	}

	public List find(String hql, Map parameters, PaginationList paginationList){
		return null;
	}

	public Long getResultCount(String statements){
		return null;
	}

	public Long getRowCount(String query, Map parameters){
		return null;
	}

	public int save(String statements, Map map){
		return 0;
	}

	public void update(String statements, Map map){
	}

	public List getBatsByOrgId(String orgId){
		Map parameters = new HashMap();
		parameters.put("orgId", orgId);
		return this.find("getBatsByOrg", parameters);
	}

	public List getFilesByOrgId(String orgId){
		Map parameters = new HashMap();
		parameters.put("orgId", orgId);
		return this.find("getFilesByOrg", parameters);
	}
}
