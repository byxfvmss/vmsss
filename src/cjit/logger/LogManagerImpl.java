package cjit.logger;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.cjit.common.util.DateUtils;

/**
 * 需要spring2.0.jar、commons-collections-3.2.jar、log4j-1.2.13.jar的支持 注意连接池的配置
 */
public class LogManagerImpl extends JdbcDaoSupport implements LogManagerBatch{

	public static final String TABLE_NAME = "U_BASE_SYS_LOG";
	public static final String FORM_TYPE = "LOG";
	public static final String BLANK = " ";
	private static IdGenerator idGenerator; // id生成器
	private static final Logger logger = Logger.getLogger(LogManagerImpl.class);
	private static String dbType = null;

	/**
	 * <p> 方法名称: insert|描述: 新增一条日志记录 </p>
	 * @param log 日志对象
	 */
	public long insert(final LogDO log){
		try{
			idGenerator = IdGenerator.getInstance(FORM_TYPE, this
					.getDataSource());
			log.setLogId(idGenerator.getNextKey());
			getJdbcTemplate().update(new PreparedStatementCreator(){

				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException{
					String sql = "INSERT INTO"
							+ BLANK
							+ TABLE_NAME
							+ BLANK
							+ "(log_id,user_id,user_ename,user_cname,inst_id,inst_cname,menu_id,menu_name,ip,\"BROWSE\",log_type,exec_time,description,system_id,status)"
							+ BLANK + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement stmt = con.prepareStatement(sql);
					int nIndex = 1;
					stmt.setLong(nIndex++, log.getLogId());
					stmt.setString(nIndex++, log.getUserId());
					stmt.setString(nIndex++, log.getUserEname());
					stmt.setString(nIndex++, log.getUserCname());
					stmt.setString(nIndex++, log.getInstId());
					stmt.setString(nIndex++, log.getInstCname());
					stmt.setString(nIndex++, log.getMenuId());
					stmt.setString(nIndex++, log.getMenuName());
					stmt.setString(nIndex++, log.getIp());
					stmt.setString(nIndex++, log.getBrowse());
					stmt.setString(nIndex++, log.getLogType());
					stmt.setTimestamp(nIndex++, new java.sql.Timestamp(log
							.getExecTime().getTime()));
					stmt.setCharacterStream(nIndex++, new StringReader(log
							.getDescription()), log.getDescription().length());
					// description字段改为了Clob类型
					// stmt.setString(13, log.getDescription());
					stmt.setString(nIndex++, log.getSystemId());
					stmt.setString(nIndex++, log.getStatus());
					return stmt;
				}
			});
			return log.getLogId();
		}catch (Exception e){
			logger.error(e);
			return -1;
		}
	}

	/**
	 * <p> 方法名称: insert|描述: 批量增加日志记录 </p>
	 * @param log 日志对象列表
	 */
	public void insertBatch(final List logList){
		idGenerator = IdGenerator.getInstance(FORM_TYPE, this.getDataSource());
		for(Iterator it = logList.iterator(); it.hasNext();){
			((LogDO) it.next()).setLogId(idGenerator.getNextKey());
		}
		String sql = "INSERT INTO"
				+ BLANK
				+ TABLE_NAME
				+ BLANK
				+ "(log_id,user_id,user_ename,user_cname,inst_id,inst_cname,menu_id,menu_name,ip,\"BROWSE\",log_type,exec_time,description,system_id,status)"
				+ BLANK + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter(){

			public void setValues(PreparedStatement pStatement, int i)
					throws SQLException{
				LogDO log = (LogDO) logList.get(i);
				int nIndex = 1;
				pStatement.setLong(nIndex++, log.getLogId());
				pStatement.setString(nIndex++, log.getUserId());
				pStatement.setString(nIndex++, log.getUserEname());
				pStatement.setString(nIndex++, log.getUserCname());
				pStatement.setString(nIndex++, log.getInstId());
				pStatement.setString(nIndex++, log.getInstCname());
				pStatement.setString(nIndex++, log.getMenuId());
				pStatement.setString(nIndex++, log.getMenuName());
				pStatement.setString(nIndex++, log.getIp());
				pStatement.setString(nIndex++, log.getBrowse());
				pStatement.setString(nIndex++, log.getLogType());
				pStatement.setTimestamp(nIndex++, new java.sql.Timestamp(log
						.getExecTime().getTime()));
				pStatement.setCharacterStream(nIndex++, new StringReader(log
						.getDescription()), log.getDescription().length());
				// description字段改为了Clob类型
				// stmt.setString(13, log.getDescription());
				pStatement.setString(nIndex++, log.getSystemId());
				pStatement.setString(nIndex++, log.getStatus());
			}

			public int getBatchSize(){
				return logList.size();
			}
		});
	}

	/**
	 * <p> 方法名称: selectByFormWithPaging|描述: 根据日志对象及分页信息，返回指定的日志记录集 </p>
	 * @param log 日志对象
	 * @param pageSize 每页大小
	 * @param pageNum 当前页码
	 * @return PageBox
	 */
	public PageBox selectByFormWithPaging(final LogDO log, int pageSize,
			int pageNum){
		PageBox pageBox = new PageBox();
		try{
			PageObject pageObject = new PageObject();
			List pageList = new LinkedList();
			pageObject.setPageSize(pageSize);
			pageObject.setPageIndex(pageNum);
			Map condition = buildQueryCondition(log);
			String conditionSql = (String) condition.get("sql");
			Object[] parms = (Object[]) condition.get("parms");
			String _oriSqlCount = "SELECT count(*) as amount FROM" + BLANK
					+ TABLE_NAME + BLANK + "WHERE" + BLANK + conditionSql;
			// 查询总记录数
			int itemAmount = getJdbcTemplate().queryForInt(_oriSqlCount, parms);
			pageObject.setItemAmount(itemAmount);
			String oriSqlQuery = buildPageSql(conditionSql, pageSize, pageNum,
					itemAmount);
			logger.info("querySql>" + oriSqlQuery);
			if(pageObject.getBeginIndex() <= pageObject.getItemAmount()){
				// 查询数据
				Object[] params = reBuildParam(parms);
				pageList = getJdbcTemplate().query(oriSqlQuery, params,
						new RowMapper(){

							public Object mapRow(ResultSet resultSet, int rowNum)
									throws SQLException{
								LogDO log = fillLogDO(resultSet);
								return log;
							}
						});
			}
			pageBox.setPageObject(pageObject);
			pageBox.setPageList(pageList);
		}catch (Exception e){
			e.printStackTrace();
			logger.error(e);
		}
		return pageBox;
	}

	public List query(String sql, Object[] parms){
		List pageList = null;
		Object[] params = reBuildParam(parms);
		pageList = getJdbcTemplate().query(sql, params, new RowMapper(){

			public Object mapRow(ResultSet resultSet, int rowNum)
					throws SQLException{
				LogDO log = fillLogDO(resultSet);
				return log;
			}
		});
		return pageList;
	}

	public int queryForInt(String sql, Object[] parms){
		return getJdbcTemplate().queryForInt(sql, parms);
	}

	/**
	 * 方法名称: deleteByPrimary|描述: 根据主键ID,删除日志记录
	 * @param id 主键ID
	 */
	public int deleteByPrimary(final long id){
		try{
			return getJdbcTemplate().update(
					"DELETE FROM" + BLANK + TABLE_NAME + BLANK
							+ "WHERE  log_id=" + id);
		}catch (Exception e){
			logger.error(e);
			return -1;
		}
	}

	/**
	 * 方法名称: deleteByPrimarys|描述: 根据主键ID,删除日志记录
	 * @param idS 主键ID集合
	 */
	public int deleteByPrimarys(final String[] ids){
		try{
			if(ids == null || ids.length == 0)
				return -1;
			StringBuffer _temp = new StringBuffer();
			for(int i = 0; i < ids.length; i++){
				_temp.append(",").append(ids[i]);
			}
			return getJdbcTemplate().update(
					"DELETE FROM" + BLANK + TABLE_NAME + BLANK
							+ "WHERE  log_id in("
							+ _temp.toString().substring(1) + ")");
		}catch (Exception e){
			logger.error(e);
			return -1;
		}
	}

	/**
	 * 方法名称: getLogByPrimary|描述: 根据主键ID,返回指定的日志记录
	 * @param id 主键ID
	 */
	public LogDO getLogByPrimary(final long id){
		try{
			Object o = getJdbcTemplate().queryForObject(
					"SELECT  * FROM" + BLANK + TABLE_NAME + BLANK
							+ "WHERE  log_id=" + id, new RowMapper(){

						public Object mapRow(ResultSet resultSet, int row)
								throws SQLException{
							LogDO log = fillLogDO(resultSet);
							return log;
						}
					});
			return (LogDO) o;
		}catch (Exception e){
			logger.error(e);
			return new LogDO();
		}
	}

	/**
	 * 填充日志对象
	 * @param resultSet
	 * @return LogDO
	 * @throws SQLException
	 */
	private LogDO fillLogDO(ResultSet resultSet) throws SQLException{
		LogDO log = new LogDO();
		log.setLogId(resultSet.getLong("log_id"));
		log.setUserId(resultSet.getString("user_id"));
		log.setUserEname(resultSet.getString("user_ename"));
		log.setUserCname(resultSet.getString("user_cname"));
		log.setInstId(resultSet.getString("inst_id"));
		log.setInstCname(resultSet.getString("inst_cname"));
		log.setMenuId(resultSet.getString("menu_id"));
		log.setMenuName(resultSet.getString("menu_name"));
		log.setIp(resultSet.getString("ip"));
		log.setBrowse(resultSet.getString("BROWSE"));
		log.setLogType(resultSet.getString("log_type"));
		try{
			log.setExecTime(resultSet.getTimestamp("exec_time"));
		}catch (Exception e){
			logger.error(e);
		}
		log.setDescription(resultSet.getString("description"));
		log.setStatus(resultSet.getString("status"));
		return log;
	}

	/**
	 * 构造分页SQL
	 * @param oriSql
	 * @param pageSize
	 * @param pageNum
	 * @param itemAmount
	 * @return String
	 */
	private String buildPageSql(String conditionSql, int pageSize, int pageNum,
			int itemAmount){
		StringBuffer sql = new StringBuffer();
		String _oriSqlQuery = "SELECT * FROM" + BLANK + TABLE_NAME + BLANK
				+ "WHERE" + BLANK + conditionSql;
		// DB2, Derby, H2, HSQL, Informix, MS-SQL, MySQL, Oracle, PostgreSQL,
		// Sybase
		int startRow = pageSize * (pageNum - 1) + 1;
		int endRow = pageSize * pageNum;
		if(endRow > itemAmount){
			endRow = itemAmount;
		}
		if("oracle".equalsIgnoreCase(getDbType())){
			_oriSqlQuery = "SELECT t.*,row_number() over(order by exec_time desc) RWN from"
					+ BLANK
					+ TABLE_NAME
					+ " t"
					+ BLANK
					+ "WHERE"
					+ BLANK
					+ conditionSql;
			sql.append("SELECT * FROM ( SELECT A.*,RWN RN FROM (")
					.append(BLANK).append(_oriSqlQuery).append(BLANK).append(
							") A WHERE ROWNUM <=").append(endRow).append(BLANK)
					.append(") WHERE RN >=").append(startRow).append(BLANK);
		}else if("MS-SQL".equalsIgnoreCase(getDbType())
				|| "Microsoft SQL Server".equalsIgnoreCase(getDbType())){
			sql.append("SELECT top").append(BLANK).append(pageSize).append(
					BLANK).append("*").append(BLANK).append("FROM").append(
					BLANK).append(TABLE_NAME).append(BLANK).append(
					"WHERE log_id<=(SELECT min(log_id) FROM (SELECT top")
					.append(BLANK).append(startRow).append(BLANK).append(
							"log_id FROM").append(BLANK).append(TABLE_NAME)
					.append(BLANK).append("WHERE").append(BLANK).append(
							conditionSql).append(BLANK).append(
							"ORDER BY log_id DESC) ttt ) and ").append(BLANK)
					.append(conditionSql).append(BLANK).append(
							"ORDER BY log_id desc");
		}else if("informix".equalsIgnoreCase(getDbType())){
			sql.append("SELECT").append(BLANK).append("SKIP").append(BLANK)
					.append(startRow - 1).append(BLANK).append("FIRST").append(
							BLANK).append(pageSize).append(BLANK)
					.append("FROM").append(BLANK).append(TABLE_NAME).append(
							BLANK).append("WHERE").append(BLANK).append(
							conditionSql);
		}else if("db2".equalsIgnoreCase(getDbType())
				|| "DB2/NT".equalsIgnoreCase(getDbType())){
			sql
					.append(
							"select * from (select t.*, rownumber() over() rownum from (select * from ")
					.append(TABLE_NAME).append(BLANK).append("where").append(
							conditionSql).append(" ) t) temp").append(
							" where temp.rownum >= ").append(startRow).append(
							" and temp.rownum <= ").append(endRow);
		}else if("mysql".equalsIgnoreCase(getDbType())){
			sql.append(_oriSqlQuery).append(BLANK).append("LIMIT")
					.append(BLANK).append(startRow - 1).append(",").append(
							pageSize);
		}else{
			return _oriSqlQuery;
		}
		return sql.toString();
	}

	/**
	 * 构造查询条件及参数
	 * @param log
	 * @return Map
	 */
	private Map buildQueryCondition(LogDO log){
		Map _map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append(BLANK);
		List _temp = new ArrayList();
		logger.info("=========Filter Parms==============");
		if(log.getUserId() != null && log.getUserId().trim().length() > 0
				&& log.getUserCname() != null
				&& log.getUserCname().trim().length() > 0
				&& log.getUserId().equals(log.getUserCname().trim())){
			sql.append("(user_id = ? ").append(BLANK).append("OR")
					.append(BLANK).append("user_cname LIKE ? )").append(BLANK)
					.append("AND").append(BLANK);
			_temp.add(log.getUserId());
			_temp.add(log.getUserCname());
			logger.info("user_id LIKE>" + log.getUserId());
			logger.info("user_cname LIKE>" + log.getUserCname());
		}else{
			if(log.getUserId() != null && log.getUserId().trim().length() > 0){
				sql.append("user_id=?").append(BLANK).append("AND").append(
						BLANK);
				_temp.add(log.getUserId());
				logger.info("user_id LIKE>" + log.getUserId());
			}
			if(log.getUserCname() != null
					&& log.getUserCname().trim().length() > 0){
				sql.append("user_cname LIKE ?").append(BLANK).append("AND")
						.append(BLANK);
				_temp.add(log.getUserCname());
				logger.info("user_cname LIKE>" + log.getUserCname());
			}
		}
		if(log.getUserEname() != null && log.getUserEname().trim().length() > 0){
			sql.append("user_ename=?").append(BLANK).append("AND")
					.append(BLANK);
			_temp.add(log.getUserEname());
			logger.info("user_ename LIKE>" + log.getUserEname());
		}
		if(log.getInstId() != null && log.getInstId().trim().length() > 0){
			sql.append("inst_id LIKE ?").append(BLANK).append("AND").append(
					BLANK);
			_temp.add(log.getInstId());
			logger.info("inst_id LIKE>" + log.getInstId());
		}
		if(log.getInstCname() != null && log.getInstCname().trim().length() > 0){
			sql.append("inst_cname LIKE ?").append(BLANK).append("AND").append(
					BLANK);
			_temp.add(log.getInstCname());
			logger.info("inst_cname LIKE>" + log.getInstCname());
		}
		if(log.getMenuId() != null && log.getMenuId().trim().length() > 0){
			sql.append("menu_id LIKE ?").append(BLANK).append("AND").append(
					BLANK);
			_temp.add(log.getMenuId());
			logger.info("menu_id LIKE>" + log.getMenuId());
		}
		if(log.getMenuName() != null && log.getMenuName().trim().length() > 0){
			sql.append("menu_name LIKE ?").append(BLANK).append("AND").append(
					BLANK);
			_temp.add(log.getMenuName());
			logger.info("menu_name LIKE>" + log.getMenuName());
		}
		if(log.getIp() != null && log.getIp().trim().length() > 0){
			sql.append("ip LIKE ?").append(BLANK).append("AND").append(BLANK);
			_temp.add(log.getIp());
			logger.info("ip LIKE>" + log.getIp());
		}
		if(log.getBrowse() != null && log.getBrowse().trim().length() > 0){
			sql.append("\"BROWSE\" LIKE ?").append(BLANK).append("AND").append(
					BLANK);
			_temp.add(log.getBrowse());
			logger.info("\"BROWSE\" LIKE>" + log.getBrowse());
		}
		if(log.getLogType() != null && log.getLogType().trim().length() > 0){
			sql.append("log_type LIKE ?").append(BLANK).append("AND").append(
					BLANK);
			_temp.add(log.getLogType());
			logger.info("log_type LIKE>" + log.getLogType());
		}
		if(log.getExecTime() != null){
			// sql.append("exec_time LIKE
			// ?").append(BLANK).append("AND").append(BLANK);
			// _temp.add(log.getExecTime());
			sql.append("to_char(exec_time,'yyyy-MM-dd') LIKE ?").append(BLANK)
					.append("AND").append(BLANK);
			_temp.add(DateUtils.toString(log.getExecTime(),
					DateUtils.ORA_DATES_FORMAT));
			logger.info("exec_time LIKE>" + log.getExecTime());
		}
		if(log.getDescription() != null
				&& log.getDescription().trim().length() > 0){
			sql.append("description LIKE ?").append(BLANK).append("AND")
					.append(BLANK);
			_temp.add(log.getDescription());
			logger.info("description LIKE>" + log.getDescription());
		}
		if(log.getStatus() != null && log.getStatus().trim().length() > 0){
			sql.append("status LIKE ?").append(BLANK).append("AND").append(
					BLANK);
			_temp.add(log.getStatus());
			logger.info("status LIKE>" + log.getStatus());
		}
		// added by wangxin 20100413 ���˻�
		if(log.getStrInstIds() != null && !"".equals(log.getStrInstIds())){
			sql.append("(inst_id in (").append(log.getStrInstIds())
					.append("))").append(BLANK).append("AND").append(BLANK);
			// _temp.add(log.getStrInstIds());
			logger.info("inst_id in " + log.getStrInstIds());
		}
		if(!"".equals(log.getUId()) && log.getInstId() == null
				&& log.getInstIds() == null){
			sql
					.append(
							"(inst_id in (select fk_orgId from t_user_org where fk_userId = '")
					.append(log.getUId()).append("'))").append(BLANK).append(
							"AND").append(BLANK);
		}
		logger.info("===============End=================");
		// sql.append("1=1").append(BLANK);
		_map.put("sql", StringUtils.removeEnd(sql.toString(), "AND" + BLANK));
		_map.put("parms", _temp.toArray());
		return _map;
	}

	private Object[] reBuildParam(Object[] param){
		// sqlServer版本,参数会嵌套使用两次,所以这儿特殊处理一下
		if("MS-SQL".equalsIgnoreCase(getDbType())
				|| "Microsoft SQL Server".equalsIgnoreCase(getDbType())){
			List params = new ArrayList();
			CollectionUtils.addAll(params, param);
			CollectionUtils.addAll(params, params.toArray());
			return params.toArray();
		}else{
			return param;
		}
	}

	private String getDbType(){
		if(null == dbType){
			Connection con = null;
			try{
				con = this.getDataSource().getConnection();
				dbType = con.getMetaData().getDatabaseProductName();
			}catch (CannotGetJdbcConnectionException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (SQLException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try{
					if(con != null){
						con.close();
						con = null;
					}
				}catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
		return dbType;
	}

	public Map selectAllByParams(LogDO paramLogDO){
		return new HashMap();
	}
}
