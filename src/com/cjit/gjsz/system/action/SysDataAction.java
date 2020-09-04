package com.cjit.gjsz.system.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.datadeal.action.DataDealAction;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.util.DataValidater;
import com.cjit.gjsz.system.model.SysData;

/**
 * 系统管理处参数配置功能
 * 
 * @author Lihaibo
 */
public class SysDataAction extends DataDealAction {

	private static final long serialVersionUID = 1L;

	private SysData sysData;
	private String tableId = "";
	private String message = "";
	private String isSave;
	private String dataDate;
	private String orgNum;
	private String orgNam;
	private String fOrgId;
	private String innerId;
	private String tableNumber;
	private String tableName;
	private String id;
	private String[] sysIds;

	public String search() {
		if ("1".equals(isSave)) {
			message = "保存成功！";
		}
		this.request.setAttribute("message", this.message);
		try {
			this.rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, "1", "1",
							null));
			// 循环列信息，作各种处理
			int cFlag = 0;
			StringBuffer columns = new StringBuffer();
			StringBuffer searchCondition = new StringBuffer()
					.append(" and t.IS_ENABLED = '1' ");
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				if (column.getTagType().startsWith("n")) {
					if ("oracle".equalsIgnoreCase(this.getDbType())) {
						columns.append("to_char(t.");
					} else if ("db2".equalsIgnoreCase(this.getDbType())) {
						columns.append("char(t.");
					} else if ("sqlserver".equalsIgnoreCase(this.getDbType())) {
						columns.append("conver(varchar(50), t.");
					} else {
						columns.append("(t.");
					}
					columns.append(column.getColumnId()).append(") as ")
							.append(column.getAliasColumnId()).append(",");
				} else {
					columns.append("t.").append(column.getColumnId()).append(
							" as ").append(column.getAliasColumnId()).append(
							",");
				}
			}
			columns
					.append(" t.IS_ENABLED as isEnabled, t.MODIFY_TIME as modifyTime, t.ID as id ");
			if ("ORGTAB".equals(tableId)) {
				orderColumn = " t.DATA_DATE, t.ORG_NUM, t.F_ORG_ID ";
				orderDirection = "asc";
				if (StringUtil.isNotEmpty(this.dataDate)) {
					searchCondition.append(" and DATA_DATE = '").append(
							this.dataDate).append("' ");
				}
				if (StringUtil.isNotEmpty(this.orgNum)) {
					searchCondition.append(" and ORG_NUM like '%").append(
							this.orgNum).append("%' ");
				}
				if (StringUtil.isNotEmpty(this.orgNam)) {
					searchCondition.append(" and ORG_NAM like '%").append(
							this.orgNam).append("%' ");
				}
				if (StringUtil.isNotEmpty(this.fOrgId)) {
					searchCondition.append(" and F_ORG_ID like '%").append(
							this.fOrgId).append("%' ");
				}
				if (StringUtil.isNotEmpty(this.innerId)) {
					searchCondition.append(" and INNER_ID like '%").append(
							this.innerId).append("%' ");
				}
			} else if ("FALBUSANDCONTAB".equals(tableId)) {
				orderColumn = " t.DATA_DATE, t.TABLE_NUMBER ";
				orderDirection = "asc";
				if (StringUtil.isNotEmpty(this.dataDate)) {
					searchCondition.append(" and DATA_DATE = '").append(
							this.dataDate).append("' ");
				}
				if (StringUtil.isNotEmpty(this.tableNumber)) {
					searchCondition.append(" and TABLE_NUMBER like '%").append(
							this.tableNumber).append("%' ");
				}
				if (StringUtil.isNotEmpty(this.tableName)) {
					searchCondition.append(" and TABLE_NAME like '%").append(
							this.tableName).append("%' ");
				}
			}
			SysData sysData = new SysData(tableId, columns.toString(),
					searchCondition.toString(), orderColumn, orderDirection);
			dataDealService.findSysData(sysData, paginationList);
			// 将部分字段值用字典表中的对应文字替换
			this.setSelectTagValueForSys(paginationList.getRecordList(),
					tableId, rptColumnList);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("paginationList", this.paginationList);
			this.request.setAttribute("tableId", this.tableId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SysDataAction-search", e);
		}
		return ERROR;
	}

	public String create() {
		try {
			SysData sysData = new SysData();
			this.rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, "1", "1",
							null));
			int cFlag = 0;
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				if ("3".equals(column.getTagType())) {
					this.initComboboxDatas(column);
				}
			}
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("sysData", sysData);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SysDataAction-create", e);
		}
		return ERROR;
	}

	public String edit() {
		try {
			this.rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, "1", "1",
							null));
			// 循环列信息，作各种处理
			int cFlag = 0;
			StringBuffer columns = new StringBuffer();
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				if (column.getTagType().startsWith("n")) {
					if ("oracle".equalsIgnoreCase(this.getDbType())) {
						columns.append("to_char(t.");
					} else if ("db2".equalsIgnoreCase(this.getDbType())) {
						columns.append("char(t.");
					}
					columns.append(column.getColumnId()).append(") as ")
							.append(column.getAliasColumnId()).append(",");
				} else {
					columns.append("t.").append(column.getColumnId()).append(
							" as ").append(column.getAliasColumnId()).append(
							",");
				}
				if ("3".equals(column.getTagType())) {
					this.initComboboxDatas(column);
				}
			}
			columns
					.append(" t.IS_ENABLED as isEnabled, t.MODIFY_TIME as modifyTime, t.ID as id ");
			if ("ORGTAB".equals(tableId)) {
				orderColumn = " t.DATA_DATE, t.ORG_NUM, t.F_ORG_ID ";
				orderDirection = "asc";
			} else if ("FALBUSANDCONTAB".equals(tableId)) {
				orderColumn = " t.DATA_DATE, t.TABLE_NUMBER ";
				orderDirection = "asc";
			}
			SysData sysData = new SysData(tableId, columns.toString(),
					" and t.ID = '" + id + "' ", orderColumn, orderDirection);
			List sysDataList = dataDealService.findSysData(sysData);
			if (sysDataList != null && sysDataList.size() == 1) {
				sysData = (SysData) sysDataList.get(0);
			}
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("sysData", sysData);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SysDataAction-edit", e);
		}
		return ERROR;
	}

	public String save() {
		log.info("SaveDataAction-saveData");
		try {
			this.rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, "1", "1",
							null));
			boolean validateData = true;
			StringBuffer insertColumns = new StringBuffer();
			StringBuffer insertValues = new StringBuffer();
			int cFlag = 0;
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				// 根据别名获取属性值
				String columnValue = (String) BeanUtils.getProperty(sysData,
						column.getAliasColumnId());
				String dataType = column.getDataType();
				if (dataType != null && dataType.toLowerCase().startsWith("n")) {
					if (columnValue != null && columnValue.startsWith(".")) {
						columnValue = "0" + columnValue;
					}
					if (columnValue != null && columnValue.indexOf(",") > 0) {
						columnValue = columnValue.replaceAll(",", "");
					}
				}
				StringBuffer sb = new StringBuffer();
				// 数据属性校验结果，并设置结果
				boolean dataTypeVSuccess = DataValidater.validateDataType(
						columnValue, dataType, sb, false, column);
				column.setDataTypeVSuccess(dataTypeVSuccess);
				column.setDataTypeVDesc(sb.toString());
				if (!dataTypeVSuccess) {
					validateData = false;
					continue;
				}
				if (columnValue == null || "".equals(columnValue.trim())) {
					continue;
				} else {
					if ("n".equalsIgnoreCase(column.getDataType().substring(0,
							1))) {
						columnValue = columnValue.replaceAll(",", "");
						insertColumns.append(column.getColumnId()).append(",");
						insertValues.append(columnValue.replaceAll("'", "''"))
								.append(",");
					} else if ("d".equalsIgnoreCase(column.getDataType()
							.substring(0, 1))) {
						columnValue = columnValue.replaceAll("-", "");
						insertColumns.append(column.getColumnId()).append(",");
						insertValues.append("'").append(
								columnValue.replaceAll("'", "''")).append("',");
					} else {
						insertColumns.append(column.getColumnId()).append(",");
						insertValues.append("'").append(
								columnValue.replaceAll("'", "''")).append("',");
					}
				}
			}
			if (!validateData) {
				// 预校验存在问题，构造下拉框数据后返回编辑界面
				this.initComboboxDatas();
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("tableId", this.tableId);
				this.request.setAttribute("sysData", sysData);
				return ERROR;
			}
			insertColumns.append("IS_ENABLED,MODIFY_TIME,ID");
			insertValues.append("'1','").append(
					DateUtils.serverCurrentDate("yyyy-MM-dd HH:mm:ss")).append(
					"','").append(createBusinessId()).append("'");
			sysData.setTableId(tableId);
			sysData.setInsertColumns(insertColumns.toString());
			sysData.setInsertValues(insertValues.toString());
			dataDealService.saveSysData(sysData);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("sysData", sysData);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SysDataAction-save", e);
		}
		return ERROR;
	}

	public String update() {
		log.info("SaveDataAction-update");
		try {
			this.rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, "1", "1",
							null));
			boolean validateData = true;
			StringBuffer insertColumns = new StringBuffer();
			StringBuffer insertValues = new StringBuffer();
			int cFlag = 0;
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				// 根据别名获取属性值
				String columnValue = (String) BeanUtils.getProperty(sysData,
						column.getAliasColumnId());
				String dataType = column.getDataType();
				if (dataType != null && dataType.toLowerCase().startsWith("n")) {
					if (columnValue != null && columnValue.startsWith(".")) {
						columnValue = "0" + columnValue;
					}
					if (columnValue != null && columnValue.indexOf(",") > 0) {
						columnValue = columnValue.replaceAll(",", "");
					}
				}
				StringBuffer sb = new StringBuffer();
				// 数据属性校验结果，并设置结果
				boolean dataTypeVSuccess = DataValidater.validateDataType(
						columnValue, dataType, sb, false, column);
				column.setDataTypeVSuccess(dataTypeVSuccess);
				column.setDataTypeVDesc(sb.toString());
				if (!dataTypeVSuccess) {
					validateData = false;
					continue;
				}
				if (columnValue == null || "".equals(columnValue.trim())) {
					continue;
				} else {
					if ("n".equalsIgnoreCase(column.getDataType().substring(0,
							1))) {
						columnValue = columnValue.replaceAll(",", "");
						insertColumns.append(column.getColumnId()).append(",");
						insertValues.append(columnValue.replaceAll("'", "''"))
								.append(",");
					} else if ("d".equalsIgnoreCase(column.getDataType()
							.substring(0, 1))) {
						columnValue = columnValue.replaceAll("-", "");
						insertColumns.append(column.getColumnId()).append(",");
						insertValues.append("'").append(
								columnValue.replaceAll("'", "''")).append("',");
					} else {
						insertColumns.append(column.getColumnId()).append(",");
						insertValues.append("'").append(
								columnValue.replaceAll("'", "''")).append("',");
					}
				}
			}
			if (!validateData) {
				// 预校验存在问题，构造下拉框数据后返回编辑界面
				this.initComboboxDatas();
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("tableId", this.tableId);
				this.request.setAttribute("sysData", sysData);
				return ERROR;
			}
			insertColumns.append("IS_ENABLED,MODIFY_TIME,ID");
			insertValues.append("'1','").append(
					DateUtils.serverCurrentDate("yyyy-MM-dd HH:mm:ss")).append(
					"','").append(createBusinessId()).append("'");
			sysData.setTableId(tableId);
			sysData.setInsertColumns(insertColumns.toString());
			sysData.setInsertValues(insertValues.toString());
			dataDealService.saveSysData(sysData);
			StringBuffer updateSets = new StringBuffer().append(
					" IS_ENABLED = '0', MODIFY_TIME = '").append(
					DateUtils.serverCurrentDate("yyyy-MM-dd HH:mm:ss")).append(
					"' ");
			StringBuffer updateCondition = new StringBuffer().append(" ID = '")
					.append(sysData.getId()).append("' ");
			sysData.setTableId(tableId);
			sysData.setUpdateSets(updateSets.toString());
			sysData.setUpdateCondition(updateCondition.toString());
			dataDealService.updateSysData(sysData);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("sysData", sysData);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SysDataAction-update", e);
		}
		return ERROR;
	}

	public String delete() {
		try {
			for (int i = 0; i < sysIds.length; i++) {
				StringBuffer updateSets = new StringBuffer().append(
						" IS_ENABLED = '0', MODIFY_TIME = '").append(
						DateUtils.serverCurrentDate("yyyy-MM-dd HH:mm:ss"))
						.append("' ");
				StringBuffer updateCondition = new StringBuffer().append(
						" ID = '").append(sysIds[i]).append("' ");
				SysData sysData = new SysData();
				sysData.setTableId(tableId);
				sysData.setUpdateSets(updateSets.toString());
				sysData.setUpdateCondition(updateCondition.toString());
				dataDealService.updateSysData(sysData);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SysDataAction-delete", e);
		}
		return ERROR;
	}

	private void setSelectTagValueForSys(List recordList, String tableId,
			List rptColumnList) {
		Map dictionaryMap = (Map) SystemCache
				.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP);
		Map tableMap = null;
		Map publicMap = null;
		if (dictionaryMap != null) {
			tableMap = (HashMap) dictionaryMap.get(tableId);
			publicMap = (HashMap) dictionaryMap.get("PUBLIC");
		}
		List sysDatas = recordList;
		// 对每条记录，循环其所有字段，若字段tagType为3（下拉框），则将字段值设置为字典表里对应的中文描述显示
		for (Iterator i = sysDatas.iterator(); i.hasNext();) {
			SysData sysData = (SysData) i.next();
			for (Iterator j = rptColumnList.iterator(); j.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) j.next();
				if ("3".equals(column.getTagType())) {
					Object codeValue = com.opensymphony.util.BeanUtils
							.getValue(sysData, column.getAliasColumnId());
					String codeName = null;
					if (tableMap != null) {
						codeName = (String) tableMap.get(column
								.getDictionaryTypeId()
								+ "_" + codeValue);
					}
					if (StringUtil.isEmpty(codeName) && publicMap != null) {
						codeName = (String) publicMap.get(column
								.getDictionaryTypeId()
								+ "_" + codeValue);
					}
					com.opensymphony.util.BeanUtils.setValue(sysData, column
							.getAliasColumnId(), codeName == null ? ""
							: codeName);
				}
			}
		}
	}

	/**
	 * 为编辑界面而构造下拉框数据
	 */
	private void initComboboxDatas() {
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			if ("3".equals(column.getTagType())) {
				this.initComboboxDatas(column);
			}
		}
	}

	private void initComboboxDatas(RptColumnInfo column) {
		Map dictionaryMap = (Map) SystemCache
				.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
		if (dictionaryMap != null) {
			boolean addTableMap = false;
			Map tableMap = (HashMap) dictionaryMap.get(tableId);
			List codeDictionaryList = null;
			if (tableMap != null) {
				codeDictionaryList = (ArrayList) tableMap.get(column
						.getDictionaryTypeId());
				if (codeDictionaryList != null) {
					this.addFieldToRequest(column.getColumnId() + "_list",
							codeDictionaryList);
					addTableMap = true;
				}
			}
			if (!addTableMap) {
				Map publicMap = (HashMap) dictionaryMap.get("PUBLIC");
				if (publicMap != null) {
					codeDictionaryList = (ArrayList) publicMap.get(column
							.getDictionaryTypeId());
					if (codeDictionaryList != null) {
						this.addFieldToRequest(column.getColumnId() + "_list",
								codeDictionaryList);
					}
				}
			}
		}
	}

	public SysData getSysData() {
		return sysData;
	}

	public void setSysData(SysData sysData) {
		this.sysData = sysData;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getIsSave() {
		return isSave;
	}

	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public String getOrgNum() {
		return orgNum;
	}

	public void setOrgNum(String orgNum) {
		this.orgNum = orgNum;
	}

	public String getOrgNam() {
		return orgNam;
	}

	public void setOrgNam(String orgNam) {
		this.orgNam = orgNam;
	}

	public String getFOrgId() {
		return fOrgId;
	}

	public void setFOrgId(String orgId) {
		fOrgId = orgId;
	}

	public String getInnerId() {
		return innerId;
	}

	public void setInnerId(String innerId) {
		this.innerId = innerId;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getSysIds() {
		return sysIds;
	}

	public void setSysIds(String[] sysIds) {
		this.sysIds = sysIds;
	}

}
