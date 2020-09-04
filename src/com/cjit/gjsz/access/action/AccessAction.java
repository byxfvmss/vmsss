package com.cjit.gjsz.access.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.DictionaryUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.access.model.AccessMetaData;
import com.cjit.gjsz.access.model.TableRelation;
import com.cjit.gjsz.access.service.AccessService;
import com.cjit.gjsz.access.service.impl.AccessServiceImpl;
import com.cjit.gjsz.access.util.AccessUtil;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.DatabaseService;
import com.cjit.gjsz.system.service.LogManagerService;

/**
 * 导入、导出、清理业务数据 sqlServer<——————>access 整库备份
 * @author zhaoqian
 */
public class AccessAction extends BaseListAction{

	private static final long serialVersionUID = 685839115352763002L;
	private AccessUtil accessUtil = new AccessUtil();
	private AccessService accessService;
	private DataDealService dataDealService;
	private DatabaseService databaseService;
	private LogManagerService logManagerService;
	// private List orgIds;// 待导出的机构ID
	private String beginDate;
	private String endDate;
	private List tableSelectId;// 界面所选表id
	private List orgSelectId;// 界面所选机构
	private List tableName = new ArrayList(); // 转换为表Id的List
	private Map tableToOrgMap = new HashMap();// 转换为table ID和Org对应的Map
	private List authInstList = new ArrayList();
	private List rptTableList = new ArrayList();// 表名list
	private String infoTypeCode;// 基础信息 申报信息 核销信息
	private String fileId;// 用uuid生成
	private String add; // 如果是查询打印，需要人为的造一个核销下拉列表的记录，否则数据清理不使用。
	private File uploadFile;// 数据还原上传的ACCESS文件

	/**
	 * To 业务数据备份 init方法
	 * @return
	 */
	public String initExport(){
		// 受权机构
		getAuthInstList(authInstList);
		loadTableNameByType();
		return SUCCESS;
	}

	/**
	 * To 业务数据还原 init方法
	 * @return
	 */
	public String initImport(){
		return SUCCESS;
	}

	/**
	 * TO 业务数据清理 init方法
	 * @return
	 */
	public String initClean(){
		// 获取配置参数
		initConfigParameters();
		initExport();
		this.request.setAttribute("interfaceVer", this.interfaceVer);
		return SUCCESS;
	}

	private void initConfigParameters(){
		configMap = (HashMap) this
				.getFieldFromSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP);
		if(configMap == null){
			UserInterfaceConfigService userInterfaceConfigService = (UserInterfaceConfigService) SpringContextUtil
					.getBean("userInterfaceConfigService");
			if(userInterfaceConfigService != null){
				configMap = userInterfaceConfigService.initConfigParameters();
				this.addFieldToSession(
						ScopeConstants.SESSION_CONFIG_PARAMETER_MAP, configMap);
			}
		}
		this.setConfigParametersBaseList(configMap);
	}

	/**
	 * 整库备份
	 * @return
	 */
	public String backup(){
		try{
			InputStream is = this.getClass().getResourceAsStream(
					"/config/config.properties");
			Properties prop = new Properties();
			prop.load(is);
			String path = (String) prop.get("backupPath");
			// String path = this.servletContext.getRealPath("/database");
			databaseService.backup(path);
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"数据备份", "数据库备份成功.已备份至：" + path, "1");
			initExport();
			this.addActionMessage("数据库备份成功.已备份至：" + path);
			return SUCCESS;
		}catch (Exception ex){
			ex.printStackTrace();
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"数据备份", "数据库备份失败,请重启服务后重试.", "0");
			this.addActionMessage("数据库备份失败,请重启服务后重试.");
			return ERROR;
		}
	}

	/**
	 * ajax调用 返回表名列表
	 * @return
	 */
	public String asyLoadTableNameAndId(){
		StringBuffer sb = new StringBuffer();
		loadTableNameByType();
		if(rptTableList != null){
			for(int i = 0; i < rptTableList.size(); i++){
				RptTableInfo tableInfo = (RptTableInfo) rptTableList.get(i);
				sb.append(tableInfo.getTableId().trim());
				sb.append(",");
				sb.append(tableInfo.getTableName().trim());
				sb.append(",");
			}
			if(sb.length() > 1){
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		String tableNames = sb.toString();
		try{
			this.response.setContentType("text/html; charset=UTF-8");
			System.out.println("tableNames:" + tableNames);
			this.response.getWriter().print(tableNames);
			this.response.getWriter().close();
		}catch (IOException ex){
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	// 获取当前用户拥有权限的机构集
	private void getAuthInstList(List authInstList){
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		authInstList.addAll(currentUser.getOrgs());
	}

	/**
	 * 返回表名列表
	 * @return
	 */
	private boolean loadTableNameByType(){
		// 根据信息类型获取is_show为1的报表列表
		RptTableInfo t = new RptTableInfo();
		t.setInfoType(DictionaryUtil.getInfoType(infoTypeCode == null ? "1"
				: infoTypeCode, this.interfaceVer));
		t.setIsShow("1");
		rptTableList = dataDealService.findRptTableInfo(t, "");
		if(StringUtil.equalsIgnoreCase(infoTypeCode, "3")
				&& !StringUtil.equalsIgnoreCase(add, "0")){
			if(StringUtil.isEmpty(this.interfaceVer)){
				RptTableInfo rtpTable = new RptTableInfo();
				rtpTable.setTableId("t_fini_export_shenbao_out");
				rtpTable.setTableName("出口收汇核销专用联信息申报表（境外收入）");
				rptTableList.add(rtpTable);
			}
		}
		return true;
	}

	/**
	 * 导出业务数据 导出的顺序是先导出主表，再导出子表。同时以此顺序插入ACCESS的元数据表
	 * @return
	 */
	public String exportData(){
		String newFilePath = "";
		try{
			// 处理界面传来的表名和机构 把他们对应起来
			for(int i = 0; i < tableSelectId.size(); i++){
				if(tableName.contains(tableSelectId.get(i))){
					List orgIdList = (List) tableToOrgMap.get(tableSelectId
							.get(i));
					orgIdList.add(orgSelectId.get(i));
					tableToOrgMap.put(tableSelectId.get(i), orgIdList);
				}else{
					tableName.add(tableSelectId.get(i));
					List orgIdList = new ArrayList();
					orgIdList.add(orgSelectId.get(i));
					tableToOrgMap.put(tableSelectId.get(i), orgIdList);
				}
			}
			String blankPath = getBlankAccessFileRealPath();
			fileId = StringUtil.generateUUID();
			newFilePath = blankPath.replaceAll("blank.mdb", fileId + ".mdb");
			accessUtil.copyBlankMdbFile(blankPath, newFilePath);
			accessUtil.connetAccessDB(newFilePath);
			List childExpList = new ArrayList();
			// 导出父表
			for(int i = 0; i < tableName.size(); i++){
				String tmpTableNameStr = ((String) (tableName.get(i))).trim();
				String parentSql = exportOneTable(tmpTableNameStr, "");
				if(tmpTableNameStr.equalsIgnoreCase("t_fini_dom_remit")
						|| tmpTableNameStr.equalsIgnoreCase("t_fini_remit")){
					TableRelation tr = new TableRelation();
					tr.setTableName("t_customs_decl");
					tr.setParentSql(parentSql);
					childExpList.add(tr);
				}else if(tmpTableNameStr.equalsIgnoreCase("t_fini_dom_export")
						|| tmpTableNameStr.equalsIgnoreCase("t_fini_export")){
					TableRelation tr = new TableRelation();
					tr.setTableName("t_export_info");
					tr.setParentSql(parentSql);
					childExpList.add(tr);
				}else if(tmpTableNameStr.equalsIgnoreCase("t_company_info")){
					TableRelation tr = new TableRelation();
					tr.setTableName("t_company_openinfo");
					tr.setParentSql(parentSql);
					childExpList.add(tr);
					TableRelation tr2 = new TableRelation();
					tr2.setTableName("t_invcountrycode_info");
					tr2.setParentSql(parentSql);
					childExpList.add(tr2);
				}
			}
			// 导出子表
			for(int j = 0; j < childExpList.size(); j++){
				TableRelation tmpTr = (TableRelation) childExpList.get(j);
				tmpTr.getParentSql();
				exportOneTable(tmpTr.getTableName(), tmpTr.getParentSql());
			}
		}catch (Exception e){
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"导出", "导出业务数据", "0");
			e.printStackTrace();
			this.addActionMessage("业务数据导出失败，请重试");
			return ERROR;
		}finally{
			try{
				accessUtil.closeConnection();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		try{
			// 下载导出的access文件
			downLoadAccessFile(newFilePath);
			// 删除指定的access文件
			accessUtil.deleteOldMdbFile(newFilePath);
		}catch (Exception e){
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"导出", "导出业务数据", "0");
			e.printStackTrace();
			this.addActionMessage("业务数据下载失败，请重试");
			return ERROR;
		}
		this.addActionMessage("业务数据导出成功");
		User user = this.getCurrentUser();
		logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
				"导出", "导出业务数据", "1");
		return SUCCESS;
	}

	/**
	 * 导入业务数据 步骤：先删除要导入这段时间的数据(删除的顺序为先删除子表后删除主表)，然后插入数据(插入的顺序为先主表后子表)
	 * @return
	 */
	public String importData(){
		File file = null;
		if(uploadFile == null){
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"上传", "上传Access文件", "0");
			this.addActionMessage("Access文件上传失败");
			// return ERROR;
		}
		String newFilePath = "";
		String blankPath = getBlankAccessFileRealPath();
		fileId = StringUtil.generateUUID();
		newFilePath = blankPath.replaceAll("blank.mdb", fileId + ".mdb");
		file = new File(newFilePath);
		uploadFile.renameTo(file);
		System.out.println("import--uploadFile-- rename:" + newFilePath);
		ResultSet rs = null;
		try{
			accessUtil.connetAccessDB(newFilePath);
			List metaDataList = accessUtil
					.getMetaData("select TYPE,CONDITION from t_access_info where 1=1");
			if(metaDataList.size() > 0){
				int size = metaDataList.size() - 1;
				for(int i = size; i >= 0; i--){// 先删除子表，再删除主表
					AccessMetaData amd = (AccessMetaData) metaDataList.get(i);
					// 存储的时候把单引号换成了双引号 恢复的时候要把双引号换成单引号
					String selectSql = amd.getCondition().trim().replaceAll(
							"\"", "\'");
					// SQL Server使用的时间语句，用单引号，不用#
					String selectSQLServerSql = selectSql.replaceAll("#", "'");
					// 去掉"select"
					String subStr = selectSQLServerSql.substring(6,
							selectSQLServerSql.length());
					// column[0]为列名 column[1]为from后面的表名+查询条件
					String[] column = subStr.split("from");
					String deleteSql = "delete from " + column[1];
					System.out
							.println("import--delete--SQLSERVER:" + deleteSql);
					// 删除要复原的重复数据
					accessService.updateData(deleteSql);
				}
				for(int i = 0; i < metaDataList.size(); i++){// 先插入主表，再删除子表
					AccessMetaData amd = (AccessMetaData) metaDataList.get(i);
					// 存储的时候把单引号换成了双引号 恢复的时候要把双引号换成单引号
					String selectSql = amd.getCondition().trim().replaceAll(
							"\"", "\'");
					System.out.println("import--select--ACCESS:" + selectSql);
					// SQL Server使用的时间语句，用单引号，不用#
					String selectSQLServerSql = selectSql.replaceAll("#", "'");
					// 去掉"select"
					String subStr = selectSQLServerSql.substring(6,
							selectSQLServerSql.length());
					// column[0]为列名 column[1]为from后面的表名+查询条件
					String[] column = subStr.split("from");
					String[] tableAndwhere = column[1].split("where");
					String tableId = tableAndwhere[0].trim();
					String typeStr = amd.getType();
					rs = accessUtil.executeQuery(selectSql);
					if(rs != null){
						while(rs.next()){
							String insertSql = this.getInsertSqlServerSql(
									column[0].trim(), tableId, rs, typeStr);
							System.out.println("import--insert--SQLSERVER:"
									+ insertSql);
							// 进行insert
							accessService.updateData(insertSql);
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			this.addActionMessage("业务数据还原失败，原因：Access数据文件不正确");
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"还原", "还原业务数据", "0");
			// return ERROR;
		}finally{
			try{
				if(rs != null){
					rs.close();
				}
				accessUtil.closeConnection();
				if(file != null){
					boolean delFlag = file.delete();
					System.out.println("import--uploadFile-- delete:"
							+ newFilePath + ":" + delFlag);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		this.addActionMessage("业务数据还原成功!");
		User user = this.getCurrentUser();
		logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
				"还原", "还原业务数据", "1");
		return SUCCESS;
	}

	/**
	 * 清理数据 步骤：根据主表获得子表列表。先清理子表。再清理主表
	 * @return
	 */
	public String cleanData(){
		try{
			// 处理界面传来的表名和机构 把他们对应起来
			for(int i = 0; i < tableSelectId.size(); i++){
				if(tableName.contains(tableSelectId.get(i))){
					List orgIdList = (List) tableToOrgMap.get(tableSelectId
							.get(i));
					orgIdList.add(orgSelectId.get(i));
					tableToOrgMap.put(tableSelectId.get(i), orgIdList);
				}else{
					tableName.add(tableSelectId.get(i));
					List orgIdList = new ArrayList();
					orgIdList.add(orgSelectId.get(i));
					tableToOrgMap.put(tableSelectId.get(i), orgIdList);
				}
			}
			List childExpList = new ArrayList();
			// 只是获取子表的语句，不处理主表
			for(int i = 0; i < tableName.size(); i++){
				String tmpTableNameStr = ((String) (tableName.get(i))).trim();
				String parentSql = getCleanOneTableSql(tmpTableNameStr, "");
				if(tmpTableNameStr.equalsIgnoreCase("t_fini_dom_remit")
						|| tmpTableNameStr.equalsIgnoreCase("t_fini_remit")){
					TableRelation tr = new TableRelation();
					tr.setTableName("t_customs_decl");
					tr.setParentSql(parentSql);
					childExpList.add(tr);
				}else if(tmpTableNameStr.equalsIgnoreCase("t_fini_dom_export")
						|| tmpTableNameStr.equalsIgnoreCase("t_fini_export")){
					TableRelation tr = new TableRelation();
					tr.setTableName("t_export_info");
					tr.setParentSql(parentSql);
					childExpList.add(tr);
				}else if(tmpTableNameStr.equalsIgnoreCase("t_company_info")){
					TableRelation tr = new TableRelation();
					tr.setTableName("t_company_openinfo");
					tr.setParentSql(parentSql);
					childExpList.add(tr);
					TableRelation tr2 = new TableRelation();
					tr2.setTableName("t_invcountrycode_info");
					tr2.setParentSql(parentSql);
					childExpList.add(tr2);
				}
			}
			// 先清理子表
			for(int j = 0; j < childExpList.size(); j++){
				TableRelation tmpTr = (TableRelation) childExpList.get(j);
				tmpTr.getParentSql();
				cleanOneTable(tmpTr.getTableName(), tmpTr.getParentSql());
			}
			// 后清理主表
			for(int i = 0; i < tableName.size(); i++){
				cleanOneTable((String) tableName.get(i), "");
			}
		}catch (Exception e){
			e.printStackTrace();
			this.addActionMessage("业务数据清理失败，请重试");
			initClean();
			return ERROR;
		}
		this.addActionMessage("业务数据清理成功!");
		initClean();
		return SUCCESS;
	}

	/**
	 * 获取空ACCESS文件路径
	 * @return
	 */
	private String getBlankAccessFileRealPath(){
		String realPath = this.servletContext
				.getRealPath("/data/accessfile/blank.mdb");
		return realPath.trim();
	}

	/**
	 * 导出的access文件下载
	 * @param newFilePath
	 * @throws IOException
	 */
	private void downLoadAccessFile(String newFilePath) throws IOException{
		InputStream inStream = new FileInputStream(newFilePath);
		this.response.reset();
		this.response.setContentType("bin");
		this.response.addHeader("Content-Disposition",
				"attachment; filename=\""
						+ URLEncoder.encode(fileId + ".mdb", "UTF-8") + "\"");
		// 循环取出流中的数据
		byte[] b = new byte[1024];
		int len;
		this.response.getOutputStream().flush();
		while((len = inStream.read(b)) != -1){
			this.response.getOutputStream().write(b, 0, len);
		}
		this.response.getOutputStream().close();
		inStream.close();
	}

	/**
	 * 从SqlServer导出一张表到Access 注意：selectSql是用"#"来比较日期，是ACCESS的语法
	 * sqlServerselectSql是用单引号比较日期，是SQL Server的语法
	 * @param tableId
	 */
	private String exportOneTable(String tableId, String parentSql){
		try{
			String[] metaData = accessService.getMetaData(tableId);
			List orgIdList = (List) tableToOrgMap.get(tableId);
			String _beginDate = beginDate + " 00:00:00";
			String _endDate = endDate + " 23:59:59";
			String selectSql = accessService.getSelectSql(metaData[0], tableId,
					orgIdList, _beginDate, _endDate, parentSql);
			String sqlServerselectSql = selectSql.replaceAll("#", "'");
			System.out.println("export--SQLServer--select:"
					+ sqlServerselectSql);
			List datas = accessService.expFromSqlServer(sqlServerselectSql);
			for(int i = 0; i < datas.size(); i++){
				Map contentMap = (Map) datas.get(i);
				String insertSql = accessService.getInsertSql(metaData[0],
						tableId, contentMap, metaData[1]);
				System.out.println("export--ACCESS--insert:" + insertSql);
				accessUtil.executeUpdateSql(insertSql);
			}
			// 处理一下sql语句里面的单引号，改成双引号
			String tmpInsertSql = selectSql.replaceAll("\\'", "\"");
			String endInsertSql = "insert into t_access_info(CONDITION,TYPE) values('"
					+ tmpInsertSql + "','" + metaData[1] + "')";
			System.out.println("export--ACCESS--insert metadata:"
					+ endInsertSql);
			// 插入元数据信息到ACCESS
			accessUtil.executeUpdateSql(endInsertSql);
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"插入", "插入元数据信息到ACCESS", "1");
			return sqlServerselectSql;
		}catch (Exception e){
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"插入", "插入元数据信息到ACCESS", "0");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 清空一张表的符合条件的数据
	 * @param tableId
	 * @param parentSql
	 * @return
	 */
	private String cleanOneTable(String tableId, String parentSql){
		try{
			String[] metaData = accessService.getMetaData(tableId);
			List orgIdList = (List) tableToOrgMap.get(tableId);
			String _beginDate = beginDate + " 00:00:00";
			String _endDate = endDate + " 23:59:59";
			String selectSql = accessService.getSelectSql(metaData[0], tableId,
					orgIdList, _beginDate, _endDate, parentSql);
			String sqlServerselectSql = selectSql.replaceAll("#", "'");
			System.out
					.println("clean--SQLServer--select:" + sqlServerselectSql);
			// String[] sqls = sqlServerselectSql.split("from");
			String str = sqlServerselectSql.substring(sqlServerselectSql
					.indexOf("from") + 4);
			String deleteSql = "delete from " + str;
			System.out.println("clean--SQLServer--delete data:" + deleteSql);
			accessService.updateData(deleteSql);
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"数据清理", "业务数据清理", "1");
			return deleteSql;
		}catch (Exception e){
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0006.0005", "系统管理.数据清理",
					"数据清理", "业务数据清理", "0");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 与cleanOneTable的区别的是只获取delete语句，但是不执行
	 * @param tableId
	 * @param parentSql
	 * @return
	 */
	private String getCleanOneTableSql(String tableId, String parentSql){
		try{
			String[] metaData = accessService.getMetaData(tableId);
			List orgIdList = (List) tableToOrgMap.get(tableId);
			String _beginDate = beginDate + " 00:00:00";
			String _endDate = endDate + " 23:59:59";
			String selectSql = accessService.getSelectSql(metaData[0], tableId,
					orgIdList, _beginDate, _endDate, parentSql);
			String sqlServerselectSql = selectSql.replaceAll("#", "'");
			String[] sqls = sqlServerselectSql.split("from");
			String deleteSql = "delete from " + sqls[1];
			return deleteSql;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 用于业务数据导入时 拼Sql Server的insert语句
	 * @param columnString
	 * @param tableId
	 * @param rs
	 * @param typeString
	 * @return
	 */
	private String getInsertSqlServerSql(String columnString, String tableId,
			ResultSet rs, String typeString){
		try{
			String[] types = typeString.split(",");
			StringBuffer sb = new StringBuffer();
			sb.append("insert into " + tableId + "(" + columnString
					+ ") values(");
			String[] columns = columnString.split(",");
			for(int j = 0; j < types.length; j++){
				if((types[j].equals(AccessServiceImpl.TYPE_STRING))
						|| (types[j].equals(AccessServiceImpl.TYPE_DATETIME))
						|| (types[j].equals(AccessServiceImpl.TYPE_TABLE))){
					String tmpString = rs.getString(columns[j]);
					if(tmpString != null){
						sb.append("'" + tmpString + "',");
					}else{
						sb.append("null,");
					}
				}else if(types[j].equals(AccessServiceImpl.TYPE_DECIMAL)){
					BigDecimal tmpBigDecimal = rs.getBigDecimal(columns[j]);
					if(tmpBigDecimal != null){
						sb.append(tmpBigDecimal.toString() + ",");
					}else{
						sb.append("null,");
					}
				}else if(types[j].equals(AccessServiceImpl.TYPE_INT)){
					Integer tmpInt = Integer
							.valueOf(rs.getInt(columns[j]) + "");
					sb.append(tmpInt.toString() + ",");
				}else if(types[j].equals(AccessServiceImpl.TYPE_REALDATETIME)){
					Date tmpDate = rs.getDate(columns[j]);
					String tmpDateStr = DateUtils.toString(tmpDate,
							"yyyy-MM-dd HH:mm:ss");
					if(tmpDateStr.equals("")){
						sb.append("null,");
					}else{
						sb.append("'" + tmpDateStr + "',");
					}
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(");");
			return sb.toString();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public AccessUtil getAccessUtil(){
		return accessUtil;
	}

	public void setAccessUtil(AccessUtil accessUtil){
		this.accessUtil = accessUtil;
	}

	public String getBeginDate(){
		return beginDate;
	}

	public void setBeginDate(String beginDate){
		this.beginDate = beginDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getFileId(){
		return fileId;
	}

	public void setFileId(String fileId){
		this.fileId = fileId;
	}

	public List getTableName(){
		return tableName;
	}

	public void setTableName(List tableName){
		this.tableName = tableName;
	}

	public AccessService getAccessService(){
		return accessService;
	}

	public void setAccessService(AccessService accessService){
		this.accessService = accessService;
	}

	public DataDealService getDataDealService(){
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService){
		this.dataDealService = dataDealService;
	}

	public List getAuthInstList(){
		return authInstList;
	}

	public void setAuthInstList(List authInstList){
		this.authInstList = authInstList;
	}

	public List getRptTableList(){
		return rptTableList;
	}

	public void setRptTableList(List rptTableList){
		this.rptTableList = rptTableList;
	}

	public List getTableSelectId(){
		return tableSelectId;
	}

	public void setTableSelectId(List tableSelectId){
		this.tableSelectId = tableSelectId;
	}

	public List getOrgSelectId(){
		return orgSelectId;
	}

	public void setOrgSelectId(List orgSelectId){
		this.orgSelectId = orgSelectId;
	}

	public Map getTableToOrgMap(){
		return tableToOrgMap;
	}

	public void setTableToOrgMap(Map tableToOrgMap){
		this.tableToOrgMap = tableToOrgMap;
	}

	public File getUploadFile(){
		return uploadFile;
	}

	public void setUploadFile(File uploadFile){
		this.uploadFile = uploadFile;
	}

	public String getInfoTypeCode(){
		return infoTypeCode;
	}

	public void setInfoTypeCode(String infoTypeCode){
		this.infoTypeCode = infoTypeCode;
	}

	public DatabaseService getDatabaseService(){
		return databaseService;
	}

	public void setDatabaseService(DatabaseService databaseService){
		this.databaseService = databaseService;
	}

	public String getAdd(){
		return add;
	}

	public void setAdd(String add){
		this.add = add;
	}

	public void setLogManagerService(LogManagerService logManagerService){
		this.logManagerService = logManagerService;
	}
}
