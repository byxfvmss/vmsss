package com.cjit.gjsz.interfacemanager.service.impl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.interfacemanager.service.FmssDataService;

public class FmssDataServiceImpl extends GenericServiceImpl implements
		FmssDataService{

	// 使用jdbc进行sql处理
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public boolean isHoliday(String date){
		StringBuffer sbSQL = new StringBuffer();
		sbSQL
				.append("select count(*) from u_base_holiday ")
				.append(
						" where holiday_type in (select holiday_type from u_base_holiday_type where enable = '1') ")
				.append(" and holiday_value = '").append(date).append("' ");
		int i = this.jdbcTemplate.queryForInt(sbSQL.toString());
		if(i > 0){
			return true;
		}
		return false;
	}
}
