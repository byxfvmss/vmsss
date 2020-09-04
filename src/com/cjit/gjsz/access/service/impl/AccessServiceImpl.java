package com.cjit.gjsz.access.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.access.service.AccessService;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.interfacemanager.util.ImportDateUtil;

/**
 * 定义：列的类型 1代表String（不区分char和varchar） 2代表decimal 3代表int 4代表datetime
 * @author zhaoqian
 */
public class AccessServiceImpl extends GenericServiceImpl implements
		AccessService{

	private UserInterfaceConfigService userInterfaceConfigService;
	private JdbcTemplate jdbcTemplate;
	public static final String TYPE_STRING = "1";
	public static final String TYPE_DECIMAL = "2";
	public static final String TYPE_INT = "3";
	public static final String TYPE_DATETIME = "4";// 假的date meta表是date
	// 但是实际列用的char(8)
	public static final String TYPE_REALDATETIME = "5";
	public static final String TYPE_TABLE = "6";

	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List expFromSqlServer(String selectSql){
		try{
			// select from sqlServer
			List datas = jdbcTemplate.queryForList(selectSql);
			System.out.println("selectSql:" + selectSql);
			System.out.println("data size:" + datas.size());
			return datas;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public String getInsertSql(String columnString, String tableId,
			Map contentMap, String typeString){
		String[] types = typeString.split(",");
		StringBuffer sb = new StringBuffer();
		sb.append("insert into " + tableId + "(" + columnString + ") values(");
		String[] columns = columnString.split(",");
		for(int j = 0; j < types.length; j++){
			if(contentMap.get(columns[j]) == null){
				sb.append("null,");
			}else{
				if(types[j].equals(TYPE_STRING)){
					String tmpString = (String) contentMap.get(columns[j]);
					tmpString = tmpString != null ? tmpString.trim() : "";
					tmpString = StringUtil.replace(tmpString, "'", "’");
					tmpString = StringUtil.replace(tmpString, "‘", "’");
					sb.append("'" + tmpString + "',");
				}else if(types[j].equals(TYPE_DECIMAL)){
					BigDecimal tmpBigDecimal = (BigDecimal) contentMap
							.get(columns[j]);
					sb.append(tmpBigDecimal.toString() + ",");
				}else if(types[j].equals(TYPE_INT)){
					Integer tmpInt = (Integer) (contentMap.get(columns[j]));
					sb.append(tmpInt.toString() + ",");
				}else if(types[j].equals(TYPE_DATETIME)){
					String tmpStr = (String) contentMap.get(columns[j]);
					sb.append("'" + tmpStr + "',");
				}else if(types[j].equals(TYPE_REALDATETIME)){
					Date tmpDate = (Date) (contentMap.get(columns[j]));
					String tmpDateStr = DateUtils.toString(tmpDate,
							"yyyy-MM-dd HH:mm:ss");
					sb.append("'" + tmpDateStr + "',");
				}else if(types[j].equals(TYPE_TABLE)){
					String tmpTableStr = (String) contentMap.get(columns[j]);
					sb.append("'" + tmpTableStr + "',");
				}
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}

	public String getSelectSql(String columns, String tableId, List orgIds,
			String beginDate, String endDate, String parentSql){
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(columns);
		sb.append(" from ");
		sb.append(tableId);
		if((parentSql != null) && !(parentSql.equals(""))){
			parentSql = parentSql.trim();
			String[] fromSql = parentSql.split("from");
			sb.append(" where BUSINESSID in (select BUSINESSID from "
					+ fromSql[1] + ")");
		}else{
			sb.append(" where 1=1 ");
			// 主表有日期insertValues.append("to_date('"+date1+"','yyyy-mm-dd'))")
			// sb.append(" and AUDITDATE >= #" + beginDate);
			// sb.append("# and AUDITDATE <= #" + endDate);
			// sb.append("#");
			if(!beginDate.trim().equals("00:00:00"))
				sb.append("  and AUDITDATE >= to_date('" + beginDate
						+ "','yyyy-mm-dd hh24:mi:ss')");
			if(!endDate.trim().equals("23:59:59"))
				sb.append("and AUDITDATE <= to_date('" + endDate
						+ "','yyyy-mm-dd hh24:mi:ss')");
			sb.append("  and DATASTATUS in(5,6)");
		}
		if((orgIds != null) && (orgIds.size() > 0)){
			sb.append(" and INSTCODE in (");
			for(int i = 0; i < orgIds.size(); i++){
				sb.append("'");
				sb.append((String) orgIds.get(i));
				sb.append("'");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		return sb.toString();
	}

	public String[] getMetaData(String tableId){
		StringBuffer columnSb = new StringBuffer();
		StringBuffer valueSb = new StringBuffer();
		String[] sqls = new String[2];
		ColumnInfo columnInfo = new ColumnInfo();
		columnInfo.setTableId(tableId);
		List columnInfos = userInterfaceConfigService
				.getColumnInfos(columnInfo);
		if(CollectionUtil.isNotEmpty(columnInfos)){
			String[] dateTypes = ImportDateUtil.getDateType(columnInfos); // 得到数据类型
			System.out.println(dateTypes.length + 6);
			for(int i = 0; i < dateTypes.length; i++){
				ColumnInfo tmpColumnInfo = (ColumnInfo) columnInfos.get(i);
				columnSb.append(tmpColumnInfo.getColumnId());
				columnSb.append(",");
				String[] tmpType = dateTypes[i].split(",");
				// 数据类型有4种： n,22,0----- s,32------ n,10------- d
				if(tmpType[0].equals("n")){
					if(tmpType.length > 2){
						valueSb.append(TYPE_DECIMAL + ",");
					}else{
						valueSb.append(TYPE_INT + ",");
					}
				}else if(tmpType[0].equals("s")){
					valueSb.append(TYPE_STRING + ",");
				}else if(tmpType[0].equals("d")){
					valueSb.append(TYPE_DATETIME + ",");
				}else{
					valueSb.append(TYPE_TABLE + ",");
				}
			}
			// 以下为子表都有的列
			if(tableId.equalsIgnoreCase("t_customs_decl")
					|| tableId.equalsIgnoreCase("t_export_info")
					|| tableId.equalsIgnoreCase("t_company_openinfo")
					|| tableId.equalsIgnoreCase("t_invcountrycode_info")){
				columnSb.append("BUSINESSID,SUBID");
				valueSb.append(TYPE_STRING + "," + TYPE_STRING);
			}else{// 以下为主表都有的列
				columnSb
						.append("INSTCODE,AUDITNAME,DATASTATUS,AUDITDATE,IMPORTDATE,BUSINESSID");
				valueSb.append(TYPE_STRING + "," + TYPE_STRING + "," + TYPE_INT
						+ "," + TYPE_REALDATETIME + "," + TYPE_REALDATETIME
						+ "," + TYPE_STRING);
			}
		}
		sqls[0] = columnSb.toString();
		sqls[1] = valueSb.toString();
		System.out.println(sqls[0]);
		System.out.println(sqls[1]);
		return sqls;
	}

	public boolean updateData(String sql){
		try{
			jdbcTemplate.update(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public UserInterfaceConfigService getUserInterfaceConfigService(){
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}
}
