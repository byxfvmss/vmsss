package com.cjit.gjsz.dataserch.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.ListOrderedMap;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.common.model.ExportSheet;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.filem.service.AutoDealRptService;
import com.cjit.gjsz.interfacemanager.service.DataCoreDataService;
import com.cjit.gjsz.system.model.TableSQL;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.BussSystemService;

public class TraceDataAction extends BaseListAction {

	private static final long serialVersionUID = 1L;
	private BussSystemService bussSystemService;
	private DataCoreDataService dataCoreDataService;
	private DataDealService dataDealService;
	private AutoDealRptService autoDealRptService;
	protected String tableId = "";
	protected String businessId = "";

	public String search() {
		try {
			User user = this.getCurrentUser();
			String userId = user.getId();
			String operate = this.request.getParameter("operate");
			this.tableId = this.request.getParameter("tableId");
			this.fileType = this.request.getParameter("fileType");
			this.businessId = this.request.getParameter("businessId");
			String buocMonth = null;
			String instCode = null;
			String businessNo = null;
			// 查询追溯脚本及汇总配置
			String traceSQL = null;
			String isSingleSummary = null;
			String summaryColumns = null;
			Map mapSummaryColumns = null;
			List sumColumns = null;
			TableSQL tableSQL = new TableSQL(tableId, fileType);
			List tableSQLList = bussSystemService.findTableSQLList(tableSQL,
					userId);
			if (tableSQLList != null) {
				tableSQL = (TableSQL) tableSQLList.get(0);
				traceSQL = tableSQL.getTraceSQL();
				isSingleSummary = tableSQL.getIsSingleSummary();
				summaryColumns = tableSQL.getSummaryColumns();
			}
			if (StringUtil.isNotEmpty(summaryColumns)) {
				mapSummaryColumns = new HashMap();
				sumColumns = new ArrayList();
				String[] configColumns = summaryColumns.split(";");
				for (int i = 0; i < configColumns.length; i++) {
					String[] configInfo = configColumns[i].split(":");
					mapSummaryColumns.put(configInfo[1], configInfo);
					sumColumns.add(configInfo[1]);
				}
			}
			// 查询准备追溯的业务数据
			Object obj = this.autoDealRptService.getDataVerifyModel(
					this.tableId, this.businessId);
			if (obj != null) {
				buocMonth = BeanUtils.getProperty(obj, "buocmonth");
				instCode = BeanUtils.getProperty(obj, "instcode");
				businessNo = BeanUtils.getProperty(obj, "businessno");
				if (CollectionUtil.isNotEmpty(sumColumns)
						&& mapSummaryColumns != null) {
					for (int i = 0; i < sumColumns.size(); i++) {
						String name = sumColumns.get(i).toString()
								.toLowerCase();
						String value = BeanUtils.getProperty(obj, name);
						String[] configInfo = (String[]) mapSummaryColumns
								.get(sumColumns.get(i));
						mapSummaryColumns.put(configInfo[0], value);
					}
				}
			}
			List rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							this.fileType));
			List subTableIdList = new ArrayList();
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				if (column.getDataType().equals("table")) {
					subTableIdList.add(column.getColumnId());
					continue;
				}
			}
			if (StringUtil.isNotEmpty(traceSQL)) {
				String lastDayOfMonth = null;
				if (buocMonth != null) {
					lastDayOfMonth = DateUtils.getLastDayOfYearMonth(buocMonth);
				}
				if (traceSQL.indexOf("$date") > 0
						&& StringUtil.isNotEmpty(lastDayOfMonth)) {
					traceSQL = traceSQL.replaceAll("\\$date", "'"
							+ lastDayOfMonth + "'");
				}
				if (traceSQL.indexOf("$buocmonth") > 0
						&& StringUtil.isNotEmpty(buocMonth)) {
					traceSQL = traceSQL.replaceAll("\\$buocmonth", "'"
							+ buocMonth + "'");
				}
				if (traceSQL.indexOf("$bank") > 0
						&& StringUtil.isNotEmpty(instCode)) {
					traceSQL = traceSQL.replaceAll("\\$bank", "'" + instCode
							+ "'");
				}
				if (traceSQL.indexOf("$businessno") > 0
						&& StringUtil.isNotEmpty(businessNo)) {
					traceSQL = traceSQL.replaceAll("\\$businessno", "'"
							+ businessNo + "'");
				}
				// 替换汇总字段
				if (mapSummaryColumns != null) {
					java.util.Iterator it = mapSummaryColumns.entrySet()
							.iterator();
					while (it.hasNext()) {
						java.util.Map.Entry entry = (java.util.Map.Entry) it
								.next();
						String strKey = (String) entry.getKey();
						Object objValue = entry.getValue();
						if (traceSQL.indexOf(strKey) > 0) {
							String value = "";
							if (objValue != null) {
								value = objValue.toString();
							}
							traceSQL = traceSQL.replaceAll("\\" + strKey, "'"
									+ value + "'");
						}
					}
				}
			} else {
				return SUCCESS;
			}
			ExportSheet[] expSheets = null;
			if (!"1".equals(isSingleSummary)) {
				// 追溯源数据
				Object[] args = null;
				List sourceDataList = dataCoreDataService.findSourceDataList(
						traceSQL, args);
				List titleList = new ArrayList();
				if (CollectionUtil.isNotEmpty(sourceDataList)) {
					ListOrderedMap sourceDateMap = (ListOrderedMap) sourceDataList
							.get(0);
					if (sourceDateMap != null && sourceDateMap.size() > 0) {
						for (int i = 0; i < sourceDateMap.size(); i++) {
							String key = (String) sourceDateMap.get(i);
							titleList.add(key);
						}
					}
				}
				ExportSheet sheet = new ExportSheet();
				sheet.setSheetName(tableId);
				sheet.setTitleList(titleList);
				sheet.setSourceDataList(sourceDataList);
				expSheets = new ExportSheet[] { sheet };
			} else {
				// 各字段汇总
				String[] columns = traceSQL.split(";;");
				if (columns != null && columns.length > 0) {
					expSheets = new ExportSheet[columns.length];
					for (int c = 0; c < columns.length; c++) {
						String[] configs = columns[c].split("::");
						// 追溯源数据
						Object[] args = null;
						List sourceDataList = dataCoreDataService
								.findSourceDataList(configs[1], args);
						List titleList = new ArrayList();
						if (CollectionUtil.isNotEmpty(sourceDataList)) {
							ListOrderedMap sourceDateMap = (ListOrderedMap) sourceDataList
									.get(0);
							if (sourceDateMap != null
									&& sourceDateMap.size() > 0) {
								for (int i = 0; i < sourceDateMap.size(); i++) {
									String key = (String) sourceDateMap.get(i);
									titleList.add(key);
								}
							}
						}
						ExportSheet sheet = new ExportSheet();
						sheet.setSheetName(configs[0]);
						sheet.setTitleList(titleList);
						sheet.setSourceDataList(sourceDataList);
						expSheets[c] = sheet;
					}
				}
			}
			if ("export".equalsIgnoreCase(operate)) {
				this.exportToExcel(expSheets, "追溯明细信息");
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String trace() {
		return null;
	}

	public BussSystemService getBussSystemService() {
		return bussSystemService;
	}

	public void setBussSystemService(BussSystemService bussSystemService) {
		this.bussSystemService = bussSystemService;
	}

	public DataCoreDataService getDataCoreDataService() {
		return dataCoreDataService;
	}

	public void setDataCoreDataService(DataCoreDataService dataCoreDataService) {
		this.dataCoreDataService = dataCoreDataService;
	}

	public DataDealService getDataDealService() {
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService) {
		this.dataDealService = dataDealService;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public AutoDealRptService getAutoDealRptService() {
		return autoDealRptService;
	}

	public void setAutoDealRptService(AutoDealRptService autoDealRptService) {
		this.autoDealRptService = autoDealRptService;
	}
}
