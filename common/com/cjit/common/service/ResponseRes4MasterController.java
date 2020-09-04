package com.cjit.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cjit.fmss.dataexchange.ExchangeManager;

/**
 * @author xieli
 */
public class ResponseRes4MasterController extends AbstractController{

	// private Logger log =
	// Logger.getLogger(ResponseRes4MasterController.class);
	private DataSource dataSource;

	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse resp) throws Exception{
		String reqType = req.getParameter("type");
		if("sql".equalsIgnoreCase(reqType)){
			String sql = req.getParameter("sql");
			ExchangeManager ex = ExchangeManager.getInstance(dataSource);
			resp.setContentType("text/xml;charset=gbk");
			ex.getSqlResultData(sql, resp.getOutputStream());
		}
		// todo support more type
		return null;
	}

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
		this.dataSource = jdbcTemplate.getDataSource();
	}
	// OutputStreamWriter writer = new
	// OutputStreamWriter(resp.getOutputStream(), Charset.forName("GBK"));
}
