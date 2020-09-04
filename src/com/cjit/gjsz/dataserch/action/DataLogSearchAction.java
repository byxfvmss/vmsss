package com.cjit.gjsz.dataserch.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.DictionaryUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptLogInfo;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.User;
import com.opensymphony.util.BeanUtils;

public class DataLogSearchAction extends BaseListAction{

	private static final long serialVersionUID = -1428571428571428571L;
	private DataDealService dataDealService;
	private UserInterfaceConfigService userInterfaceConfigService;
	// 机构code
	private String instCode;
	// 操作起止日期
	private String beginDate;
	private String endDate;
	// 基础信息 申报信息 核销信息
	private String infoTypeCode;
	// 选中的table
	private String tableSelectId;
	// 选中的业务ID
	private String businessSelectId;
	// 操作人员ID
	private String userSelectId;
	// 选中的业务编号
	private String businessSelectNo;
	// 选中的操作类型
	private String logTypeKey;
	// 初始化标志
	private String initFlag;
	// 排序字段
	private String orderColumnLog;
	// 排序方式
	private String orderDirectionLog;
	// 最长行
	private int largestColumnNum;
	// 最大列数
	private String logColumnNum;
	// 受权机构列表
	private List authInstList = new ArrayList();
	// 表名list
	private List rptTableList = new ArrayList();
	// 列名list
	private List rptColumnList = new ArrayList();
	// 查询结果
	private List recordList = new ArrayList();
	// 业务信息集
	private List busiDataInfoList = new ArrayList();
	// 操作类型
	private Map logTypeMap = new LinkedMap();

	/**
	 * 数据历史查询
	 * @return String
	 */
	public String list(){
		try{
			initConfigParameters();
			if(this.configMap != null){
				this.request.setAttribute("interfaceVer",
						(String) this.configMap.get("config.interface.ver"));
			}
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if(currentUser != null){
				userId = currentUser.getId();
			}
			// 获取有权限的机构
			getAuthInstList(authInstList);
			// 返回基础信息的单据列表
			loadTableNameByType(userId);
			// 操作类型列表
			loadLogTypeList();
			if("1".equals(initFlag)){
				initSession();
				this.infoType = "A";
				// this.fileType = "AA";
				// this.tableSelectId = "T_CFA_A_EXDEBT";
			}
			//
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(null, "1", "1"));
			// 
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
			if(rptTableList != null && rptTableList.size() > 0){
				if(this.fileType == null
						|| !this.fileType.startsWith(this.infoType)){
					this.tableSelectId = ((RptTableInfo) rptTableList.get(0))
							.getTableId();
					this.fileType = ((RptTableInfo) rptTableList.get(0))
							.getFileType();
				}else{
					for(int i = 0; i < rptTableList.size(); i++){
						if(((RptTableInfo) rptTableList.get(i)).getFileType()
								.equals(this.fileType)){
							this.tableSelectId = ((RptTableInfo) rptTableList
									.get(i)).getTableId();
						}
					}
				}
			}
			// load列名列表
			if(rptTableList.size() > 0){
				rptColumnList = dataDealService
						.findRptColumnInfo(new RptColumnInfo(tableSelectId,
								null, "1", this.fileType));
			}
			String pagefalg = (String) request.getParameter("pagefalg");
			if(pagefalg == null){
				request.getSession().removeAttribute("orderColumnLog");
				request.getSession().removeAttribute("orderDirectionLog");
			}
			if(orderColumnLog == null && orderDirectionLog == null){
				orderColumnLog = (String) request.getSession().getAttribute(
						"orderColumnLog");
				orderDirectionLog = (String) request.getSession().getAttribute(
						"orderDirectionLog");
			}else{
				request.getSession().setAttribute("orderColumnLog",
						orderColumnLog);
				request.getSession().setAttribute("orderDirectionLog",
						orderDirectionLog);
			}
			// 构造查询条件
			RptLogInfo rptLogInfo = new RptLogInfo();
			if(("".equals(instCode) || instCode == null)
					&& StringUtil.isNotEmpty(tableSelectId)){
				StringBuffer searchCondition = new StringBuffer();
				searchCondition
						.append(" exists (select 1 from ")
						.append(tableSelectId)
						.append(
								" where t.businessid = businessid and exists (select 1 from t_user_org where instCode = fk_orgId and fk_userId = '")
						.append(userId).append("')) ");
				rptLogInfo.setSearchCondition(searchCondition.toString());
			}
			rptLogInfo.setFiletype(this.fileType);
			rptLogInfo.setTableid(tableSelectId);
			rptLogInfo.setBusinessid(businessSelectId);
			rptLogInfo.setUserid(userSelectId);
			rptLogInfo.setLogtype(logTypeKey);
			rptLogInfo.setBusinessno(businessSelectNo);
			if(beginDate != null && !"".equals(beginDate)){
				String beginTime = beginDate.replaceAll("-", "");
				rptLogInfo.setUpdatetimeBegin(beginTime + "000000000");
			}
			if(endDate != null && !"".equals(endDate)){
				String endTime = endDate.replaceAll("-", "");
				rptLogInfo.setUpdatetimeEnd(endTime + "235959999");
			}
			rptLogInfo.setOrderColumn(orderColumnLog);
			rptLogInfo.setOrderDirection(orderDirectionLog);
			// 查询主表记录
			recordList = dataDealService.findRptLogInfo(rptLogInfo,
					paginationList);
			// 将部分字段值用字典表中的对应文字替换
			this.setSelectTagValue(recordList, tableSelectId, rptColumnList);
			// weblogic8.1
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumnLog", this.orderColumnLog);
			this.request.setAttribute("orderDirectionLog",
					this.orderDirectionLog);
			this.request
					.setAttribute("businessSelectId", this.businessSelectId);
			this.request.setAttribute("userSelectId", this.userSelectId);
			this.request.setAttribute("logTypeKey", this.logTypeKey);
			this.request.setAttribute("recordList", this.recordList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			// weblogic8.1
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumnLog", this.orderColumnLog);
			this.request.setAttribute("orderDirectionLog",
					this.orderDirectionLog);
			this.request
					.setAttribute("businessSelectId", this.businessSelectId);
			this.request.setAttribute("userSelectId", this.userSelectId);
			this.request.setAttribute("logTypeKey", this.logTypeKey);
			this.request.setAttribute("recordList", this.recordList);
			return ERROR;
		}
	}

	private void initSession(){
		// 把报表列信息和查询SQL放在SESSION中
		if(this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP2) == null){
			Map[] map = dataDealService.initRptColumnSqlMap(largestColumnNum,
					"1");
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP2, map[0]);
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP2, map[1]);
		}
		// 将字典信息放在SESSION中
		// if(this.getFieldFromSession(ScopeConstants.SESSION_DICTIONARY_MAP) ==
		// null){
		// Map[] map = userInterfaceConfigService.initDictionaryMap();
		// this.addFieldToSession(ScopeConstants.SESSION_DICTIONARY_MAP,
		// map[0]);
		// this.addFieldToSession(
		// ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE, map[1]);
		// }
		// weblogic8.1
		this.request.setAttribute("rptColumnList", this.rptColumnList);
		this.request.setAttribute("orderColumnLog", this.orderColumnLog);
		this.request.setAttribute("orderDirectionLog", this.orderDirectionLog);
		this.request.setAttribute("recordList", this.recordList);
	}

	private void initConfigParameters(){
		// 将参数配置项记录信息放在SESSION中
		if("yes".equalsIgnoreCase(this.configIsCluster)){
			configMap = userInterfaceConfigService.initConfigParameters();
			this.addFieldToSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
					configMap);
		}else{
			CacheabledMap cache = (CacheabledMap) CacheManager
					.getCacheObject("paramCache");
			if(cache != null){
				configMap = (Map) cache.get("configMap");
				this.addFieldToSession(
						ScopeConstants.SESSION_CONFIG_PARAMETER_MAP, configMap);
			}else{
				configMap = (HashMap) this
						.getFieldFromSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP);
				if(configMap == null){
					configMap = userInterfaceConfigService
							.initConfigParameters();
					this.addFieldToSession(
							ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
							configMap);
				}
			}
		}
		this.setConfigParametersBaseList(configMap);
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
	private boolean loadTableNameByType(String userId){
		// 根据信息类型获取is_show为1的报表列表
		RptTableInfo t = new RptTableInfo();
		t.setInfoType(DictionaryUtil.getInfoType(infoTypeCode == null ? "1"
				: infoTypeCode, this.interfaceVer));
		t.setIsShow("1");
		rptTableList = dataDealService.findRptTableInfo(t, userId);
		if(StringUtil.equalsIgnoreCase(infoTypeCode, "3")){
			if(StringUtil.isEmpty(this.interfaceVer)){
				RptTableInfo rtpTable = new RptTableInfo();
				rtpTable.setTableId("t_fini_export_shenbao_out");
				rtpTable.setTableName("出口收汇核销专用联信息申报表（境外收入）");
				rptTableList.add(rtpTable);
				RptTableInfo rtpTable_sub1 = new RptTableInfo();
				rtpTable_sub1.setTableId("t_customs_decl");
				rtpTable_sub1.setTableName("_报关单信息");
				rptTableList.add(rtpTable_sub1);
				RptTableInfo rtpTable_sub2 = new RptTableInfo();
				rtpTable_sub2.setTableId("t_export_info");
				rtpTable_sub2.setTableName("_出口收汇核销单号码");
				rptTableList.add(rtpTable_sub2);
			}
		}else if(StringUtil.equalsIgnoreCase(infoTypeCode, "5")){
			RptTableInfo rtpTable_sub3 = new RptTableInfo();
			rtpTable_sub3.setTableId("t_company_info");
			rtpTable_sub3.setTableName("基本信息");
			rptTableList.add(rtpTable_sub3);
			RptTableInfo rtpTable_sub1 = new RptTableInfo();
			rtpTable_sub1.setTableId("t_company_openinfo");
			rtpTable_sub1.setTableName("_开户信息");
			rptTableList.add(rtpTable_sub1);
			RptTableInfo rtpTable_sub2 = new RptTableInfo();
			rtpTable_sub2.setTableId("t_invcountrycode_info");
			rtpTable_sub2.setTableName("_投资国别代码");
			rptTableList.add(rtpTable_sub2);
		}
		return true;
	}

	private void loadLogTypeList(){
		logTypeMap.put("insert", "添加");
		logTypeMap.put("update", "修改");
		logTypeMap.put("delete", "删除");
		logTypeMap.put("send", "发送报文");
		// logTypeMap.put("success", "接收成功");
		this.request.setAttribute("logTypeMap", logTypeMap);
	}

	/**
	 * 根据报表ID取rptColumnList,以及所有字段的查询SQL。从SESSION缓存中取
	 */
	private String getColumnsSql(){
		if(rptColumnList != null && rptColumnList.size() > 0){
			// 循环列信息，作各种处理
			StringBuffer columns = new StringBuffer();
			columns.append("t.logtype as logtype, ").append(
					"t.tableid as tableid, ").append("t.userid as userid, ")
					.append("t.updatetime as updatetime, ").append(
							"t.businessid as businessid, ").append(
							"t.subid as subid, ").append(
							"t.datastatus as datastatus");
			for(int i = 1; i <= Integer.valueOf(logColumnNum).intValue(); i++){
				String columnId = "column";
				if(i < 10){
					columnId = columnId + "0" + i;
				}else{
					columnId = columnId + i;
				}
				if(columns.toString().toLowerCase().indexOf(columnId) < 0){
					columns.append(", t.").append(columnId).append(" as ")
							.append(columnId);
				}
			}
			return columns.toString();
		}
		return "";
	}

	// 将部分字段值用字典表中的对应文字替换
	private void setSelectTagValue(List recordList, String tableId,
			List rptColumnList){
		Map dictionaryMap = (Map) SystemCache
				.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP);
		if(dictionaryMap != null){
			Map tableMap = (HashMap) dictionaryMap.get(tableId);
			if(tableMap != null){
				List rptLogInfos = recordList;
				// 循环报表数据集，对每条记录，循环其所有字段，若字段tagType为3（下拉框），则将字段值设置为字典表里对应的中文描述显示
				for(Iterator i = rptLogInfos.iterator(); i.hasNext();){
					RptLogInfo rptLogInfo = (RptLogInfo) i.next();
					// 设置机构名称
					// for (int j = 0; j < this.authInstList.size(); j++) {
					// Organization orga = (Organization) authInstList.get(j);
					// if (orga.getId().equals(rptData.getInstCode())) {
					// rptData.setInstName(orga.getName());
					// }
					// }
					for(Iterator j = rptColumnList.iterator(); j.hasNext();){
						RptColumnInfo column = (RptColumnInfo) j.next();
						if("3".equals(column.getTagType())){
							String codeName = (String) tableMap.get(column
									.getDictionaryTypeId()
									+ "_"
									+ BeanUtils.getValue(rptLogInfo, column
											.getLogColumnId()));
							BeanUtils.setValue(rptLogInfo, column
									.getLogColumnId(), codeName == null ? ""
									: codeName);
						}
					}
				}
			}
		}
	}

	public String showDetail(){
		try{
			List subTableList = null;
			Map subTableMap = null;
			// load列名列表
			if(tableSelectId != null){
				rptColumnList = dataDealService
						.findRptColumnInfo(new RptColumnInfo(tableSelectId,
								null, "1", this.fileType));
				if(rptColumnList != null && rptColumnList.size() > 0){
					subTableList = new ArrayList();
					for(Iterator j = rptColumnList.iterator(); j.hasNext();){
						RptColumnInfo rptColumnInfo = (RptColumnInfo) j.next();
						if(rptColumnInfo != null
								&& "table".equals(rptColumnInfo.getDataType())){
							subTableList.add(rptColumnInfo);
						}
					}
				}
			}
			// 构造查询条件
			RptLogInfo rptLogInfo = new RptLogInfo();
			rptLogInfo.setTableid(tableSelectId);
			rptLogInfo.setBusinessid(businessSelectId);
			// 查询主表记录
			recordList = dataDealService.findRptLogInfo(rptLogInfo, null);
			// 将部分字段值用字典表中的对应文字替换
			this.setSelectTagValue(recordList, tableSelectId, rptColumnList);
			if(subTableList != null && subTableList.size() > 0){
				subTableMap = new HashMap();
				for(int i = 0; i < subTableList.size(); i++){
					RptColumnInfo rptColumnInfo = (RptColumnInfo) subTableList
							.get(i);
					// 子表ID
					String subTableId = rptColumnInfo.getColumnId();
					// 构造查询条件
					RptLogInfo subRptLogInfo = new RptLogInfo();
					subRptLogInfo.setTableid(subTableId);
					subRptLogInfo.setBusinessid(businessSelectId);
					// 子表列信息
					List subColumnList = dataDealService
							.findRptColumnInfo(new RptColumnInfo(subTableId,
									null, "1", null));
					// 子表数据记录
					List subRecordList = dataDealService.findRptLogInfo(
							subRptLogInfo, null);
					Object[] subObjs = new Object[2];
					subObjs[0] = subColumnList;
					subObjs[1] = subRecordList;
					subTableMap.put(subTableId, subObjs);
				}
			}
			this.request.setAttribute("subTableList", subTableList);
			this.request.setAttribute("subTableMap", subTableMap);
			// weblogic8.1
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumnLog", this.orderColumnLog);
			this.request.setAttribute("orderDirectionLog",
					this.orderDirectionLog);
			this.request
					.setAttribute("businessSelectId", this.businessSelectId);
			this.request.setAttribute("userSelectId", this.userSelectId);
			this.request.setAttribute("recordList", this.recordList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			// weblogic8.1
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumnLog", this.orderColumnLog);
			this.request.setAttribute("orderDirectionLog",
					this.orderDirectionLog);
			this.request
					.setAttribute("businessSelectId", this.businessSelectId);
			this.request.setAttribute("userSelectId", this.userSelectId);
			this.request.setAttribute("recordList", this.recordList);
			return ERROR;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	public DataDealService getDataDealService(){
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService){
		this.dataDealService = dataDealService;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService(){
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public String getInstCode(){
		return instCode;
	}

	public void setInstCode(String instCode){
		this.instCode = instCode;
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

	public String getInfoTypeCode(){
		return infoTypeCode;
	}

	public void setInfoTypeCode(String infoTypeCode){
		this.infoTypeCode = infoTypeCode;
	}

	public String getTableSelectId(){
		return tableSelectId;
	}

	public void setTableSelectId(String tableSelectId){
		this.tableSelectId = tableSelectId;
	}

	public String getInitFlag(){
		return initFlag;
	}

	public void setInitFlag(String initFlag){
		this.initFlag = initFlag;
	}

	public String getLogColumnNum(){
		return logColumnNum;
	}

	public void setLogColumnNum(String logColumnNum){
		this.logColumnNum = logColumnNum;
	}

	public String getOrderColumnLog(){
		return orderColumnLog;
	}

	public void setOrderColumnLog(String orderColumn){
		this.orderColumnLog = orderColumn;
	}

	public String getOrderDirectionLog(){
		return orderDirectionLog;
	}

	public void setOrderDirectionLog(String orderDirection){
		this.orderDirectionLog = orderDirection;
	}

	public List getRecordList(){
		return recordList;
	}

	public void setRecordList(List recordList){
		this.recordList = recordList;
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

	public List getRptColumnList(){
		return rptColumnList;
	}

	public void setRptColumnList(List rptColumnList){
		this.rptColumnList = rptColumnList;
	}

	public int getLargestColumnNum(){
		return largestColumnNum;
	}

	public void setLargestColumnNum(int largestColumnNum){
		this.largestColumnNum = largestColumnNum;
	}

	public String getBusinessSelectId(){
		return businessSelectId;
	}

	public void setBusinessSelectId(String businessSelectId){
		this.businessSelectId = businessSelectId;
	}

	public String getUserSelectId(){
		return userSelectId;
	}

	public void setUserSelectId(String userId){
		this.userSelectId = userId;
	}

	public List getBusiDataInfoList(){
		return busiDataInfoList;
	}

	public void setBusiDataInfoList(List busiDataInfoList){
		this.busiDataInfoList = busiDataInfoList;
	}

	public String getLogTypeKey(){
		return logTypeKey;
	}

	public void setLogTypeKey(String logTypeKey){
		this.logTypeKey = logTypeKey;
	}

	public Map getLogTypeMap(){
		return logTypeMap;
	}

	public String getBusinessSelectNo(){
		return businessSelectNo;
	}

	public void setBusinessSelectNo(String businessSelectNo){
		this.businessSelectNo = businessSelectNo;
	}
}
