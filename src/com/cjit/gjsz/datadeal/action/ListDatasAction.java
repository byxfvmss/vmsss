/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.dataserch.model.QueryCondition;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class ListDatasAction extends DataDealAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8137467701081432489L;

	/**
	 * 外部表数据列表显示入口
	 * 
	 * @return
	 */
	public String listDatas() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		if (!StringUtils.isEmpty(getTableUniqueSelectId())) {
			this.tableId = getTableUniqueSelectId().split(
					DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[0];
			this.fileType = getTableUniqueSelectId().split(
					DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1];
		}
		if ("basic".equals(this.pageFlag)) {
			// 从“数据录入”菜单页面点击单据“查看”图标进入列表页面
			this.request.getSession().setAttribute(
					ScopeConstants.SESSION_MENU_FLAG, "basic");
		} else {
			this.request.getSession().setAttribute(
					ScopeConstants.SESSION_MENU_FLAG, "verify");
		}
		this.request
				.setAttribute("configSearchAllOrg", this.configSearchAllOrg);
		this.request.setAttribute("configForbidAdd", this.configForbidAdd);
		this.request
				.setAttribute("configForbidDelete", this.configForbidDelete);
		this.request.setAttribute("configForbidSave", this.configForbidSave);
		this.request.setAttribute("configOverleapCommit",
				this.configOverleapCommit);
		this.request.setAttribute("configOverleapAudit",
				this.configOverleapAudit);
		this.request.setAttribute("relatedFileType", this.relatedFileType);
		return listDatas(infoTypeCode, tableId);
	}

	// DFHL: 增加查询开始
	protected List rptColumnListQuery = new ArrayList();

	public List getRptColumnListQuery() {
		return rptColumnListQuery;
	}

	public void setRptColumnListQuery(List rptColumnListQuery) {
		this.rptColumnListQuery = rptColumnListQuery;
	}

	private void addCommonColumnListQuery() {
		rptColumnListQuery = new ArrayList();
		if (this.tableId == null && this.fileType == null) {
			return;
		}
		List listColumn = dataDealService.findRptColumnInfo(new RptColumnInfo(
				tableId, null, "1", this.fileType));
		for (int i = 0; i < listColumn.size(); i++) {
			rptColumnListQuery.add(listColumn.get(i));
		}
		if ("AR".equalsIgnoreCase(this.fileType)
				|| "AS".equalsIgnoreCase(this.fileType)) {
			RptColumnInfo rci = new RptColumnInfo();
			rci.setColumnId(CONTRACTTYPE);
			rci.setColumnName("签约类型");
			rptColumnListQuery.add(rci);
		}
	}

	// protected QueryCondition queryCondition; // 查询条件
	// public QueryCondition getQueryCondition(){
	// return queryCondition;
	// }
	// public void setQueryCondition(QueryCondition queryCondition){
	// this.queryCondition = queryCondition;
	// }
	public static final String TYPE_STRING = "1";// 字符串的
	public static final String TYPE_NUM = "2";// 数字的
	public static final String TYPE_DATE = "3";// Date的

	public Map getOperatorsMap() {
		return operatorsMap;
	}

	public void setOperatorsMap(Map operatorsMap) {
		// ListDatasAction.operatorsMap = operatorsMap;
	}

	public String searchPanelStatus = "none";

	/**
	 * 判断该列是否是字典项 true:表示字典项 false:不是字典项
	 * 
	 * @return protected boolean checkDic(String columnId){ ColumnInfo
	 *         columnInfo = getColumnInfo(tableId, columnId); if(columnInfo !=
	 *         null && columnInfo.getTagType() != null &&
	 *         columnInfo.getTagType().length() > 0){
	 *         if(columnInfo.getTagType().equals("3")){ return true; } } return
	 *         false; }
	 */
	/**
	 * 字典项的SQL
	 * 
	 * @param columId
	 * @param op
	 * @param values
	 * @return protected String getDicSql(String columId, int op, String
	 *         values){ ColumnInfo columnInfo = getColumnInfo(tableId, columId);
	 *         StringBuffer sb = new StringBuffer(); sb.append(" and t.");
	 *         sb.append(columId + " in ("); sb .append(" select
	 *         code_value_standard_num from t_code_dictionary where code_name
	 *         "); sb.append(operatorsMap.get(new Integer(op))); if(op ==
	 *         TYPE_LIKE){ sb.append(" '%" + values + "%'"); }else{ sb.append(" '" +
	 *         values + "'"); } sb.append(" and code_type='" +
	 *         columnInfo.getDictionaryTypeId() + "')"); return sb.toString(); }
	 */
	/**
	 * 非字典项的SQL
	 * 
	 * @param columId
	 * @param op
	 * @param values
	 * @return protected String getNoDicSql(String columId, int op, String
	 *         values){ StringBuffer sb = new StringBuffer(); String typeF =
	 *         getColumnType(tableId, columId); if(typeF.equals(TYPE_STRING) ||
	 *         "TRADEDATE".equals(columId) || "RPTDATE".equals(columId) ||
	 *         "IMPDATE".equals(columId)){ sb.append(" and t.");
	 *         sb.append(columId + " "); sb.append(operatorsMap.get(new
	 *         Integer(op))); if(op == TYPE_LIKE){ sb.append(" '%" + values +
	 *         "%'"); }else{ sb.append(" '" + values + "'"); } }else
	 *         if(typeF.equals(TYPE_DATE)){ if(this.getDbType().equals("db2")){
	 *         sb.append(" and SUBSTR(CHAR(t.").append(columId).append( "),1,10)
	 *         "); sb.append(operatorsMap.get(new Integer(op))); String datetemp =
	 *         values.substring(0, 4) + "-" + values.substring(4, 6) + "-" +
	 *         values.substring(6, 8); if(op == TYPE_LIKE){ sb.append(" '%" +
	 *         datetemp + "%'"); }else{ sb.append(" '" + datetemp + "'"); }
	 *         }else if(this.getDbType().equals("oracle")){ sb.append(" and
	 *         to_char(t.").append(columId).append( ",'yyyymmdd') ");
	 *         sb.append(operatorsMap.get(new Integer(op))); if(op ==
	 *         TYPE_LIKE){ sb.append(" '%" + values + "%'"); }else{ sb.append(" '" +
	 *         values + "'"); } } }else{ sb.append(" and t."); sb.append(columId + "
	 *         "); sb.append(operatorsMap.get(new Integer(op))); sb.append(" " +
	 *         values); } return sb.toString(); }
	 */
	/*
	 * protected String getCommonSql(String columId, int op, String values){
	 * if(columId.equals("common.----------")) return ""; StringBuffer sb = new
	 * StringBuffer(); sb.append(" and ");
	 * sb.append(columId.substring("common.".length()) + " ");
	 * sb.append(operatorsMap.get(new Integer(op))); // String typeF =
	 * getColumnType(tableId, columId); if(op == TYPE_LIKE){ sb.append(" '%" +
	 * values + "%'"); }else{ sb.append(" '" + values + "'"); } return
	 * sb.toString(); }
	 */
	/**
	 * 验证单个查询条件是否合法
	 * 
	 * @param columnId
	 * @param op
	 * @return private boolean checkOneValue(String columnId, int op, String
	 *         beginError){ boolean flag = true; String error = ""; ColumnInfo
	 *         ciF = getColumnInfo(this.tableId, columnId); String type =
	 *         getColumnType(ciF); if(TYPE_NUM.equals(type)){ if(op ==
	 *         TYPE_LIKE){ error = error + beginError + "\"" +
	 *         ciF.getColumnName() + "\"" + "为数字型,不可以选择" + operatorsMap.get(new
	 *         Integer(TYPE_LIKE)) + ";"; flag = false; } }else
	 *         if(TYPE_STRING.equals(type)){ // if ((op == TYPE_MORE) || (op ==
	 *         TYPE_LESS) || (op == // TYPE_MORE_EQUAL) // || (op ==
	 *         TYPE_LESS_EQUAL)) { // error = error+beginError+"\""+
	 *         ciF.getColumnName()+"\"" + // "为文本型,不可以选择" // +
	 *         operatorsMap.get(new Integer(op)) + ";"; // flag = false; // }
	 *         }else if(TYPE_DATE.equals(type)){ } if(!(error.equals(""))){
	 *         this.addActionMessage(error); } return flag; }
	 */
	/**
	 * 验证三条查询条件是否合法 private boolean check(){ boolean flag = true; boolean flag1 =
	 * true; boolean flag2 = true; boolean flag3 = true; if(queryCondition !=
	 * null){ if(!StringUtil.isEmpty(queryCondition.getValueFirst())){
	 * if(StringUtil.checkStr(queryCondition.getValueFirst())){
	 * this.addActionMessage("查询条件一含有非法字符"); return false; } flag1 =
	 * checkOneValue(queryCondition.getColumnIdFirst(),
	 * queryCondition.getOpFirst(), "查询条件一:"); }
	 * if(!StringUtil.isEmpty(queryCondition.getValueSecond())){
	 * if(StringUtil.checkStr(queryCondition.getValueSecond())){
	 * this.addActionMessage("查询条件二含有非法字符"); return false; } flag2 =
	 * checkOneValue(queryCondition.getColumnIdSecond(),
	 * queryCondition.getOpSecond(), "查询条件二:"); }
	 * if(!StringUtil.isEmpty(queryCondition.getValueThird())){
	 * if(StringUtil.checkStr(queryCondition.getValueThird())){
	 * this.addActionMessage("查询条件三含有非法字符"); return false; } flag3 =
	 * checkOneValue(queryCondition.getColumnIdThird(),
	 * queryCondition.getOpThird(), "查询条件三:"); } } flag = flag1 && flag2 &&
	 * flag3; return flag; }
	 */
	/*
	 * private String createSqlCondition(QueryCondition qc, String bDate, String
	 * eDate, String userId){ String returnStr = ""; StringBuffer sb = new
	 * StringBuffer(); if(qc != null){
	 * if(!StringUtil.isEmpty(qc.getValueFirst())){ // 先判断该列是否是字典项
	 * if(qc.getColumnIdFirst().startsWith("common.")){
	 * sb.append(getCommonSql(qc.getColumnIdFirst(), qc .getOpFirst(),
	 * qc.getValueFirst())); }else{
	 * if(!CONTRACTTYPE.equals(qc.getColumnIdFirst())){
	 * if(checkDic(qc.getColumnIdFirst())){
	 * sb.append(getDicSql(qc.getColumnIdFirst(), qc .getOpFirst(),
	 * qc.getValueFirst())); }else{ sb.append(getNoDicSql(qc.getColumnIdFirst(),
	 * qc .getOpFirst(), qc.getValueFirst())); } }else{
	 * sb.append(getContractTypeSQL((String) operatorsMap .get(new
	 * Integer(qc.getOpFirst())), qc .getValueFirst())); } } }
	 * if(!StringUtil.isEmpty(qc.getValueSecond())){
	 * if(qc.getColumnIdSecond().startsWith("common.")){
	 * sb.append(getCommonSql(qc.getColumnIdSecond(), qc .getOpSecond(),
	 * qc.getValueSecond())); }else{
	 * if(!CONTRACTTYPE.equals(qc.getColumnIdSecond())){
	 * if(checkDic(qc.getColumnIdSecond())){
	 * sb.append(getDicSql(qc.getColumnIdSecond(), qc .getOpSecond(),
	 * qc.getValueSecond())); }else{
	 * sb.append(getNoDicSql(qc.getColumnIdSecond(), qc .getOpSecond(),
	 * qc.getValueSecond())); } }else{ sb.append(getContractTypeSQL((String)
	 * operatorsMap .get(new Integer(qc.getOpSecond())), qc .getValueSecond())); } } }
	 * if(!StringUtil.isEmpty(qc.getValueThird())){
	 * if(qc.getColumnIdThird().startsWith("common.")){
	 * sb.append(getCommonSql(qc.getColumnIdThird(), qc .getOpThird(),
	 * qc.getValueThird())); }else{
	 * if(!CONTRACTTYPE.equals(qc.getColumnIdThird())){
	 * if(checkDic(qc.getColumnIdThird())){
	 * sb.append(getDicSql(qc.getColumnIdThird(), qc .getOpThird(),
	 * qc.getValueThird())); }else{ sb.append(getNoDicSql(qc.getColumnIdThird(),
	 * qc .getOpThird(), qc.getValueThird())); } }else{
	 * sb.append(getContractTypeSQL((String) operatorsMap .get(new
	 * Integer(qc.getOpThird())), qc .getValueThird())); } } } }
	 * if(!StringUtil.isEmpty(bDate)){ sb.append(" and t.AUDITDATE>='" + bDate +
	 * "'"); } if(!StringUtil.isEmpty(eDate)){ Date tmpend =
	 * DateUtils.getAfterData(DateUtils.stringToDate(eDate,
	 * DateUtils.ORA_DATES_FORMAT), 1); String endStr =
	 * DateUtils.toString(tmpend, DateUtils.ORA_DATES_FORMAT); sb.append(" and
	 * t.AUDITDATE<='" + endStr + "'"); } // 增加所有选项
	 * if(StringUtil.isEmpty(instCode)){// 所有 if((authInstList != null) &&
	 * (authInstList.size() > 0)){ sb .append( " and t.instCode in (select
	 * fk_orgId from t_user_org where fk_userId = '") .append(userId).append("')
	 * "); }else{ // 没有有权限的机构 sb.append(" and 1=0"); } } if(sb.length() > 3){
	 * returnStr = sb.toString().replaceFirst("and", ""); }
	 * System.out.println("SQL:" + returnStr); return returnStr; }
	 */
	/**
	 * 获取列类型
	 * 
	 * @param newcolumnInfo
	 * @return public String getColumnType(ColumnInfo newcolumnInfo){ String
	 *         returnType = TYPE_STRING; if(newcolumnInfo != null){ String type =
	 *         newcolumnInfo.getDataType(); if((type != null) && (type.length() >
	 *         0)){ String[] tmpType = type.split(",");
	 *         if(tmpType[0].equals("n")){ returnType = TYPE_NUM; }else
	 *         if(tmpType[0].equals("s")){ returnType = TYPE_STRING; }else
	 *         if(tmpType[0].equals("d")){ returnType = TYPE_DATE; } } } return
	 *         returnType; }
	 */
	/*
	 * public ColumnInfo getColumnInfo(String tableId, String columnId){
	 * ColumnInfo columnInfo = new ColumnInfo();
	 * columnInfo.setColumnId(columnId); columnInfo.setTableId(tableId); return
	 * userInterfaceConfigService.getColumnInfo(columnInfo); }
	 */
	/*
	 * public String getColumnType(String tableId, String columnId){
	 * if("BUSINESSNO".equals(columnId)){ return TYPE_STRING; } ColumnInfo ci =
	 * getColumnInfo(tableId, columnId); return getColumnType(ci); }
	 */
	// DFHL: 增加查询结束
	public String getSearchPanelStatus() {
		return searchPanelStatus;
	}

	public void setSearchPanelStatus(String searchPanelStatus) {
		this.searchPanelStatus = searchPanelStatus;
	}

	/**
	 * 数据列表显示通用方法
	 * 
	 * @return
	 */
	public String listDatas(String infoTypeCode, String tableId) {
		try {
			// long lTime1 = Calendar.getInstance().getTimeInMillis();
			if ("1".equals(request.getParameter("noedit"))
					|| "1".equals(request.getAttribute("noedit"))) {
				this.addFieldToRequest("noedit", "1");
			}
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if (currentUser != null) {
				userId = currentUser.getId();
			}
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				this.request.getSession().removeAttribute("beginDate");
				this.request.getSession().removeAttribute("endDate");
			}
			// 子表是否可编辑标记
			String subCanModify = this.request.getParameter("subCanModify");
			this.request.setAttribute("subCanModify", subCanModify);
			dataStatusList = commonService.getDataStatusListForPageListDatas();
			// 从页面获取 数据采集范围和业务类型
			this.busiDataType = (String) this.request
					.getParameter("busiDataType");
			if (StringUtil.isEmpty(this.infoType)) {
				if (StringUtil.isNotEmpty(this.fileType)
						&& this.fileType.length() == 2) {
					this.infoType = this.fileType.substring(0, 1);
				} else if (StringUtil.isNotEmpty(tableId)) {
					// FIXME 代客业务 从tableid解析infotype
				}
			}
			// 根据信息类型和是否显示获取报表信息
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(this.busiDataType,
							"1", "1"));
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
			this.busiDataType = this.getBusiDataType(this.infoType,
					busiDataInfoList);
			if (rptTableList != null && rptTableList.size() > 0) {
				if (this.fileType == null
						|| (!this.fileType.startsWith(this.infoType) && !"ALL"
								.equals(this.infoType))) {
					this.fileType = ((RptTableInfo) rptTableList.get(0))
							.getFileType();
				}
			} else {
				RptTableInfo rti = new RptTableInfo();
				rti.setTableId(DataUtil.getTableIdByFileType(this.infoType));
				rti.setFileType("ZZ");
				rti.setTableName("未授权");
				rptTableList.add(rti);
				this.fileType = "ZZ";
			}
			if (StringUtil.isEmpty(tableId) && StringUtil.isNotEmpty(fileType)) {
				tableId = DataUtil.getTableIdByFileType(fileType);
				this.tableId = tableId;
			}
			if (StringUtil.isEmpty(infoTypeCode)
					&& tableId != null
					&& (tableId.indexOf("_SUB_") > 0
							|| tableId.endsWith("_STOCKINFO") || tableId
							.endsWith("_INVEST"))) {
				// 此处查询子表信息
				if (StringUtil.isEmpty(this.businessId)) {
					getColumnsSql(dataDealService.createTablueUniqueId(tableId,
							fileType));
					this.request.setAttribute("rptColumnList",
							this.rptColumnList);
					return SUCCESS;
				}
			} else {
				if (this.tableId == null
						|| !this.tableId.equals(DataUtil
								.getTableIdByFileType(fileType))) {
					if (!"1".equals(fileType)) {
						tableId = DataUtil.getTableIdByFileType(fileType);
						this.tableId = tableId;
					} else if (!DataUtil.isDaiKeTableId(tableId)) {
						if (rptTableList != null && rptTableList.size() > 0) {
							this.tableId = ((RptTableInfo) rptTableList.get(0))
									.getTableId();
							tableId = this.tableId;
						}
					}
				}
			}
			addCommonColumnListQuery();
			String columns = getColumnsSql(dataDealService
					.createTablueUniqueId(tableId, fileType));
			String dataStatusCondition = "";
			if (request.getParameter("checkfalg") != null
					&& request.getSession().getAttribute("paginationList") != null) {
				paginationList = (PaginationList) request.getSession()
						.getAttribute("paginationList");
				queryCondition = (QueryCondition) request.getSession()
						.getAttribute("queryCondition");
			}
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				if (orderColumn == null && orderDirection == null) {
					orderColumn = (String) request.getSession().getAttribute(
							"orderColumn");
					orderDirection = (String) request.getSession()
							.getAttribute("orderDirection");
				} else {
					request.getSession().setAttribute("orderColumn",
							orderColumn);
					request.getSession().setAttribute("orderDirection",
							orderDirection);
				}
				if (orderColumn != null
						&& orderColumn.indexOf("datastatus") < 0
						&& columns != null && columns.indexOf(orderColumn) < 0) {
					orderColumn = null;
					orderDirection = null;
					request.getSession().setAttribute("orderColumn", null);
					request.getSession().setAttribute("orderDirection", null);
				}
				orderColumnSub = null;
				orderDirectionSub = null;
			} else {
				if (orderColumnSub == null && orderDirectionSub == null) {
					orderColumnSub = (String) request.getSession()
							.getAttribute("orderColumnSub");
					orderDirectionSub = (String) request.getSession()
							.getAttribute("orderDirectionSub");
				} else {
					request.getSession().setAttribute("orderColumnSub",
							orderColumnSub);
					request.getSession().setAttribute("orderDirectionSub",
							orderDirectionSub);
				}
			}
			// 判断当前单据是否可新增
			RptTableInfo tableInfo = this.getRptTableInfo(rptTableList,
					tableId, fileType);
			if (tableInfo != null && "0".equals(tableInfo.getCanInput())) {
				this.request.setAttribute("tableCanInput", "0");
			}
			if (!check()) {
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				this.request.setAttribute("tableId", this.tableId);
				this.request.setAttribute("dataStatus", this.dataStatus);
				this.request.setAttribute("beginDate", this.beginDate);
				this.request.setAttribute("endDate", this.endDate);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("orderColumn", this.orderColumn);
				this.request
						.setAttribute("orderDirection", this.orderDirection);
				request.getSession().setAttribute("orderColumnSub",
						this.orderColumnSub);
				request.getSession().setAttribute("orderDirectionSub",
						this.orderDirectionSub);
				this.request
						.setAttribute("paginationList", this.paginationList);
				this.request.setAttribute("busiDataType", this.busiDataType);
				this.request.setAttribute("infoType", this.infoType);
				this.request.setAttribute("fileType", this.fileType);
				return SUCCESS;
			}
			String strWhereSQL = createSqlCondition(queryCondition, null, null,
					userId);
			// 基础/单位基本信息
			if (DataUtil.isJCDW(infoTypeCode)) {
				if (StringUtil.isNotEmpty(tableId)) {
					// 所有状态，相当于选择状态为1、2、3、5的
					if (dataStatus == null || "".equals(dataStatus)) {
						dataStatusCondition += " t.datastatus in("
								+ DataUtil.WJY_STATUS_NUM + ","
								+ DataUtil.JYWTG_STATUS_NUM + ","
								+ DataUtil.JYYTG_STATUS_NUM + ","
								+ DataUtil.SHWTG_STATUS_NUM + ") ";
					}
					// 特定状态
					else {
						dataStatusCondition += " t.datastatus in(" + dataStatus
								+ ")";
					}
					RptData rptData2 = new RptData(tableId, columns, instCode,
							dataStatusCondition, null, orderColumn,
							orderDirection);
					rptData2.setBuocMonthFrom(getBeginDate());
					rptData2.setBuocMonthTo(getEndDate());
					rptData2.setFileType(this.fileType);
					if ("1".equals(infoTypeCode)) {
						// 改为查询交易日期
						// rptData2.setTradeDateFrom(rptData2.getBeginDate());
						// rptData2.setTradeDateTo(rptData2.getEndDate());
						// rptData2.setBeginDate(null);
						// rptData2.setEndDate(null);
					}
					if ("AR".equals(this.fileType)
							|| "AS".equals(this.fileType)
							|| "BB".equals(this.fileType)
							|| "BC".equals(this.fileType)
							|| "CB".equals(this.fileType)
							|| "DB".equals(this.fileType)
							|| "EB".equals(this.fileType)
							|| "FB".equals(this.fileType)
							|| "FC".equals(this.fileType)) {
						// 补充涉及签约信息部分字段的查询
						// this.setSqlAboutContractInfo(rptData2, userId);
					}
					if ("on".equals(searchLowerOrg)) {
						rptData2.setSearchLowerOrg(searchLowerOrg);
						rptData2.setUserId(userId);
					}
					if ("yes".equalsIgnoreCase(super.linkBussType)) {
						rptData2.setLinkBussType(super.linkBussType);
						rptData2.setUserId(userId);
					}
					rptData2.setSearchCondition(strWhereSQL);
					dataDealService.findRptData(rptData2, paginationList);
					// DFHL:增加开始和结束日期开始
					this.request.getSession().setAttribute("beginDate",
							rptData2.getBeginDate());
					this.request.getSession().setAttribute("endDate",
							rptData2.getEndDate());
					this.request.getSession().setAttribute("dataStatus",
							dataStatus);
					this.request.getSession().setAttribute("searchLowerOrg",
							searchLowerOrg);
					this.request.getSession().setAttribute("paginationList",
							paginationList);
					this.request.getSession().setAttribute("queryCondition",
							queryCondition);
					// 将部分字段值用字典表中的对应文字替换
					this.setSelectTagValue(paginationList.getRecordList(),
							tableId, rptColumnList);
				}
			}
			// 内嵌表
			else {
				if ("".equalsIgnoreCase(businessId)) {
					businessId = (String) this
							.getFieldFromSession(ScopeConstants.CURRENT_BUSINESS_ID);
					if ("".equalsIgnoreCase(businessId)) {
						businessId = (String) this
								.getFieldFromSession(ScopeConstants.CURRENT_BUSINESS_ID2);
					}
				}
				recordList = dataDealService.findInnerRptData(new RptData(
						tableId, columns, businessId, null, orderColumnSub,
						orderDirectionSub));
				// 将部分字段值用字典表中的对应文字替换
				this.setSelectTagValue(recordList, tableId, rptColumnList);
				// 在wl8.1下放入request
				this.request.setAttribute("recordList", this.recordList);
				this.request
						.setAttribute("orderColumnSub", this.orderColumnSub);
				this.request.setAttribute("orderDirectionSub",
						this.orderDirectionSub);
			}
			// 在wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("dataStatus", this.dataStatus);
			this.request.setAttribute("beginDate", this.beginDate);
			this.request.setAttribute("endDate", this.endDate);
			this.request.setAttribute("searchLowerOrg", searchLowerOrg);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			request.getSession().setAttribute("orderColumnSub",
					this.orderColumnSub);
			request.getSession().setAttribute("orderDirectionSub",
					this.orderDirectionSub);
			this.request.setAttribute("paginationList", this.paginationList);
			this.request.setAttribute("busiDataType", this.busiDataType);
			this.request.setAttribute("infoType", this.infoType);
			this.request.setAttribute("fileType", this.fileType);
			if (this.tableUniqueSelectId == null) {
				if (this.tableId != null && this.fileType != null) {
					this.tableUniqueSelectId = this.tableId + "#"
							+ this.fileType;
				}
			}
			this.request.setAttribute("tableUniqueSelectId",
					this.tableUniqueSelectId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("listDatas", e);
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("dataStatus", this.dataStatus);
			this.request.setAttribute("beginDate", this.beginDate);
			this.request.setAttribute("endDate", this.endDate);
			this.request.setAttribute("searchLowerOrg", searchLowerOrg);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			request.getSession().setAttribute("orderColumnSub",
					this.orderColumnSub);
			request.getSession().setAttribute("orderDirectionSub",
					this.orderDirectionSub);
			this.request.setAttribute("paginationList", this.paginationList);
			return SUCCESS;
		}
	}

	/**
	 * 外部表数据列表显示入口
	 * 
	 * @return
	 */
	public String goBack() {
		if (!sessionInit(true)) {
			return SUCCESS;
		}
		this.request
				.setAttribute("configSearchAllOrg", this.configSearchAllOrg);
		this.request.setAttribute("configForbidAdd", this.configForbidAdd);
		this.request
				.setAttribute("configForbidDelete", this.configForbidDelete);
		this.request.setAttribute("configForbidSave", this.configForbidSave);
		this.request.setAttribute("configOverleapCommit",
				this.configOverleapCommit);
		this.request.setAttribute("configOverleapAudit",
				this.configOverleapAudit);
		this.request.setAttribute("relatedFileType", this.relatedFileType);
		return goBack(infoTypeCode, tableId);
	}

	public String goBack(String infoTypeCode, String tableId) {
		try {
			if ("1".equals(request.getParameter("noedit"))
					|| "1".equals(request.getAttribute("noedit"))) {
				this.addFieldToRequest("noedit", "1");
			}
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if (currentUser != null) {
				userId = currentUser.getId();
			}
			if (StringUtil.isEmpty(this.infoType)
					&& StringUtil.isNotEmpty(this.fileType)) {
				this.infoType = this.fileType.substring(0, 1);
			}
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				if (orderColumn == null && orderDirection == null) {
					orderColumn = (String) request.getSession().getAttribute(
							"orderColumn");
					orderDirection = (String) request.getSession()
							.getAttribute("orderDirection");
				} else {
					request.getSession().setAttribute("orderColumn",
							orderColumn);
					request.getSession().setAttribute("orderDirection",
							orderDirection);
				}
				orderColumnSub = null;
				orderDirectionSub = null;
			} else {
				if (orderColumnSub == null && orderDirectionSub == null) {
					orderColumnSub = (String) request.getSession()
							.getAttribute("orderColumnSub");
					orderDirectionSub = (String) request.getSession()
							.getAttribute("orderDirectionSub");
				} else {
					request.getSession().setAttribute("orderColumnSub",
							orderColumnSub);
					request.getSession().setAttribute("orderDirectionSub",
							orderDirectionSub);
				}
			}
			this.beginDate = (String) this.request.getSession().getAttribute(
					"beginDate");
			this.endDate = (String) this.request.getSession().getAttribute(
					"endDate");
			this.dataStatus = (String) this.request.getSession().getAttribute(
					"dataStatus");
			this.searchLowerOrg = (String) this.request.getSession()
					.getAttribute("searchLowerOrg");
			this.queryCondition = (QueryCondition) this.request.getSession()
					.getAttribute("queryCondition");
			this.paginationList = (PaginationList) this.request.getSession()
					.getAttribute("paginationList");
			if (this.paginationList == null) {
				this.paginationList = new PaginationList();
			}
			String strWhereSQL = createSqlCondition(queryCondition, null, null,
					userId);
			dataStatusList = commonService.getDataStatusListForPageListDatas();
			// 根据信息类型和是否显示获取报表信息
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(this.busiDataType,
							"1", "1"));
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
			// DFHL: 增加查询列开始
			addCommonColumnListQuery();
			// DFHL: 增加查询列结束
			if (StringUtil.isEmpty(tableId) && StringUtil.isNotEmpty(fileType)) {
				tableId = DataUtil.getTableIdByFileType(fileType);
			}
			String columns = getColumnsSql(dataDealService
					.createTablueUniqueId(tableId, fileType));
			// 判断当前单据是否可新增
			RptTableInfo tableInfo = this.getRptTableInfo(rptTableList,
					tableId, fileType);
			if (tableInfo != null && "0".equals(tableInfo.getCanInput())) {
				this.request.setAttribute("tableCanInput", "0");
			}
			// 查询是否提交为0,即未提交的信息 or 是否提交为空,即关联查询出、并未编辑保存的申报、核销
			String dataStatusCondition = " FILETYPE = '" + this.fileType + "' ";
			// 基础/单位基本信息
			if (DataUtil.isJCDW(infoTypeCode)) {
				// 所有状态，相当于选择状态为1-4的
				if (dataStatus == null || "".equals(dataStatus)) {
					dataStatusCondition += " and t.datastatus in("
							+ DataUtil.WJY_STATUS_NUM + ","
							+ DataUtil.JYWTG_STATUS_NUM + ","
							+ DataUtil.JYYTG_STATUS_NUM + ","
							+ DataUtil.SHWTG_STATUS_NUM + ") ";
				}
				// 特定状态
				else {
					dataStatusCondition += " and t.datastatus in(" + dataStatus
							+ ")";
				}
				// DFHL:增加开始和结束日期开始
				RptData rptData2 = new RptData(tableId, columns, instCode,
						dataStatusCondition, null, orderColumn, orderDirection);
				rptData2.setBeginDate(getBeginDate());
				rptData2.setEndDate(getEndDate());
				rptData2.setFileType(this.fileType);
				if ("1".equals(infoTypeCode)) {
					// 改为查询交易日期
					rptData2.setTradeDateFrom(rptData2.getBeginDate());
					rptData2.setTradeDateTo(rptData2.getEndDate());
					rptData2.setBeginDate(null);
					rptData2.setEndDate(null);
				}
				if ("on".equals(searchLowerOrg)) {
					rptData2.setSearchLowerOrg(searchLowerOrg);
					rptData2.setUserId(userId);
				}
				if ("yes".equalsIgnoreCase(super.linkBussType)) {
					rptData2.setLinkBussType(super.linkBussType);
					rptData2.setUserId(userId);
				}
				rptData2.setSearchCondition(strWhereSQL);
				dataDealService.findRptData(rptData2, paginationList);
				// DFHL:增加开始和结束日期开始
				this.request.getSession().setAttribute("beginDate",
						rptData2.getBeginDate());
				this.request.getSession().setAttribute("endDate",
						rptData2.getEndDate());
				this.request.getSession()
						.setAttribute("dataStatus", dataStatus);
				this.request.getSession().setAttribute("searchLowerOrg",
						searchLowerOrg);
				this.request.getSession().setAttribute("paginationList",
						paginationList);
				this.request.getSession().setAttribute("queryCondition",
						queryCondition);
				// 将部分字段值用字典表中的对应文字替换
				this.setSelectTagValue(paginationList.getRecordList(), tableId,
						rptColumnList);
			}
			// 内嵌表
			else {
				if ("".equalsIgnoreCase(businessId)) {
					businessId = (String) this
							.getFieldFromSession(ScopeConstants.CURRENT_BUSINESS_ID);
					if ("".equalsIgnoreCase(businessId)) {
						businessId = (String) this
								.getFieldFromSession(ScopeConstants.CURRENT_BUSINESS_ID2);
					}
				}
				recordList = dataDealService.findInnerRptData(new RptData(
						tableId, columns, businessId, null, orderColumnSub,
						orderDirectionSub));
				// 将部分字段值用字典表中的对应文字替换
				this.setSelectTagValue(recordList, tableId, rptColumnList);
				// 在wl8.1下放入request
				this.request.setAttribute("recordList", this.recordList);
			}
			// 在wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("fileType", this.fileType);
			this.request.setAttribute("dataStatus", this.dataStatus);
			this.request.setAttribute("beginDate", this.beginDate);
			this.request.setAttribute("endDate", this.endDate);
			this.request.setAttribute("searchLowerOrg", this.searchLowerOrg);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			request.getSession().setAttribute("orderColumnSub",
					this.orderColumnSub);
			request.getSession().setAttribute("orderDirectionSub",
					this.orderDirectionSub);
			this.request.setAttribute("paginationList", this.paginationList);
			this.request.setAttribute("queryCondition", this.queryCondition);
			if (this.tableUniqueSelectId == null) {
				if (this.tableId != null && this.fileType != null) {
					this.tableUniqueSelectId = this.tableId + "#"
							+ this.fileType;
				}
			}
			this.request.setAttribute("tableUniqueSelectId",
					this.tableUniqueSelectId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	private RptTableInfo getRptTableInfo(List tableInfoList, String tableId,
			String fileType) {
		if (tableInfoList != null && tableInfoList.size() > 0
				&& tableId != null && fileType != null) {
			for (int i = 0; i < tableInfoList.size(); i++) {
				RptTableInfo tableInfo = (RptTableInfo) tableInfoList.get(i);
				if (tableId.equalsIgnoreCase(tableInfo.getTableId())
						&& fileType.equals(tableInfo.getFileType())) {
					return tableInfo;
				}
			}
		}
		return null;
	}
}
