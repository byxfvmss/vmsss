/**
 * 
 */
package com.cjit.gjsz.datadeal.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptLogInfo;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.datadeal.util.DataValidater;
import com.cjit.gjsz.filem.core.ReceiveReport;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.system.model.SysData;
import com.opensymphony.util.BeanUtils;

/**
 * @author yulubin
 */
public class DataDealServiceImpl extends GenericServiceImpl implements
		DataDealService {

	public Long findRptInfoByBusi(RptData rptData) {
		Map map = new HashMap();
		map.put("rptData", rptData);
		return (Long) this.find("findIsFinished", map).get(0);
	}

	// t_rpt_table_info:分页查询
	public List findRptTableInfo(RptTableInfo info,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("rptTableInfo", info);
		return find("findRptTableInfo", map, paginationList);
	}

	// t_rpt_table_info:普通查询
	public List findRptTableInfo(RptTableInfo info, String userId) {
		Map map = new HashMap();
		map.put("rptTableInfo", info);
		map.put("userId", userId);
		if (StringUtil.isNotEmpty(info.getIsShow())) {
			map.put("isShow", info.getIsShow());
		}
		return find("findRptTableInfo", map);
	}

	// 根据数据表物理名称查询数据表信息
	public RptTableInfo findRptTableInfoById(String tableId, String fileType) {
		Map map = new HashMap();
		map.put("rptTableInfo", new RptTableInfo(tableId, fileType));
		List list = find("findRptTableInfo", map);
		if (list != null && list.size() == 1) {
			return (RptTableInfo) list.get(0);
		} else {
			return new RptTableInfo(tableId);
		}
	}

	// 根据数据表物理名称查询数据表信息
	public RptTableInfo findRptTableInfoBySaveTableId(String saveTableId) {
		Map map = new HashMap();
		RptTableInfo myInfo = new RptTableInfo();
		myInfo.setSafeTableId(saveTableId);
		map.put("rptTableInfo", myInfo);
		List list = find("findRptTableInfo", map);
		if (list != null && list.size() == 1) {
			return (RptTableInfo) list.get(0);
		} else {
			return null;
		}
	}

	// 根据报表物理表名和机构号查询记录数
	public Long findRptDataCountByTableIdAndInstCode(String tableId,
			String instCode, String fileType) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("instCode", instCode);
		map.put("fileType", fileType);
		return this.getRowCount("findRptDataCountByTableIdAndInstCode", map);
	}

	// 根据报表物理表名和机构号查询记录数
	public Long findRptDataCountByTableIdAndInstCode(RptData rptData) {
		Map map = new HashMap();
		map.put("rptData", rptData);
		return this.getRowCount("findRptDataCountByTableIdAndInstCodeNew", map);
	}

	// 根据报表物理表名和机构号查询各个状态记录数
	public List findRptDataStatusCountByTableIdAndInstCode(String tableId,
			String instCode, String searchLowerOrg, String fileType,
			String userId, String linkBussType) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("instCode", instCode);
		map.put("searchLowerOrg", searchLowerOrg);
		map.put("fileType", fileType);
		map.put("userId", userId);
		map.put("linkBussType", linkBussType);
		if ("AR".equalsIgnoreCase(fileType) || "AS".equalsIgnoreCase(fileType)) {
			String joinTable = " inner join T_CFA_A_EXDEBT c on c.businessno = t.businessno and c.filetype <> '"
					+ fileType + "' ";
			map.put("joinTable", joinTable);
		}
		return this.find("findRptDataStatusCountByTableIdAndInstCode", map);
	}

	// 根据报表物理表名和机构号查询各个状态记录数
	public List findRptDataStatusCountByTableIdAndInstCode(RptData rptData) {
		Map map = new HashMap();
		map.put("rptData", rptData);
		return this.find("findRptDataStatusCountByTableIdAndInstCodeNew", map);
	}

	public List findRptDataStatusCountByInfoTypeAndInstCode(String infoType,
			String instCode, String searchLowerOrg, String userId,
			String linkBussType, String buocMonth) {
		Map map = new HashMap();
		if (infoType != null && !"ALL".equals(infoType)) {
			map.put("infoType", infoType);
		}
		map.put("instCode", instCode);
		map.put("searchLowerOrg", searchLowerOrg);
		map.put("userId", userId);
		map.put("linkBussType", linkBussType);
		map.put("buocMonth", buocMonth);
		return this.find("findRptDataStatusCountByInfoTypeAndInstCode", map);
	}

	/**
	 * 根据报表物理表名，查询列，以及其它条件查询物理数据
	 * 
	 * @param tableId
	 * @param columns
	 * @param startDate
	 * @param endDate
	 * @param instCode
	 * @param dataStatus
	 * @param businessNo
	 * @return
	 */
	public List findRptData(RptData rptData, PaginationList paginationList) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.find("findRptData", params, paginationList);
	}

	public List findRptData(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		if (rptData.getTableId() != null) {
			if (rptData.getTableId().toLowerCase().indexOf("t_stob") > -1) {
				List list = this.find("findRptDataStob", params);
				return list;
			} else if (rptData.getTableId().toLowerCase().indexOf("_sub_") > -1) {
				List list = this.find("findInnerRptData", params);
				return list;
			}
		}
		List list = this.find("findRptData", params);
		return list;
	}

	public Long findRptDataCount(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.getRowCount("findRptDataCount", params);
	}

	public List findSysData(SysData sysData, PaginationList paginationList) {
		Map params = new HashMap();
		params.put("sysData", sysData);
		return this.find("findSysData", params, paginationList);
	}

	public List findSysData(SysData sysData) {
		Map params = new HashMap();
		params.put("sysData", sysData);
		return this.find("findSysData", params);
	}

	/**
	 * 按照表名和业务主键集查询数据
	 * 
	 * @param tableId
	 * @param businessIds
	 * @return
	 */
	public List findRptDataByTableIdAndBusinessIds(String tableId,
			List businessIds, String columns) {
		Map params = new HashMap();
		params.put("tableId", tableId);
		params.put("businessIds", businessIds);
		params.put("columns", columns);
		return this.find("findRptDataByTableIdAndBusinessIds", params);
	}

	/**
	 * 分页查询：查询内嵌表物理数据
	 * 
	 * @param tableId
	 * @param columns
	 * @param businessId
	 * @param paginationList
	 * @return
	 */
	public List findInnerRptData(RptData rptData, PaginationList paginationList) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.find("findInnerRptData", params, paginationList);
	}

	public List findInnerRptData(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.find("findInnerRptData", params);
	}

	/**
	 * 根据关联物理报表（直接上游报表）表名，报表物理表名，查询列，以及其它条件查询物理数据（数据中包含关联报表数据和报表数据）
	 * 
	 * @param relaTableId
	 * @param tableId
	 * @param columns
	 * @param startDate
	 * @param endDate
	 * @param instCode
	 * @param relaDataStatus
	 * @param dataStatus
	 * @param businessNo
	 * @return
	 */
	public List findRelaRptData(RptData rptData, PaginationList paginationList) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.find("findRelaRptData", params, paginationList);
	}

	public List findRelaRptData(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.find("findRelaRptData", params);
	}

	/**
	 * 根据关联物理报表（直接上游报表）表名，报表物理表名，查询列，以及其它条件查询物理数据（数据中包含关联报表数据和报表数据）
	 * 
	 * @param relaTableId
	 * @param tableId
	 * @param columns
	 * @param startDate
	 * @param endDate
	 * @param instCode
	 * @param relaDataStatus
	 * @param dataStatus
	 * @param businessNo
	 * @return
	 */
	public List findRelaRptDataNew(RptData rptData,
			PaginationList paginationList) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.find("findRelaRptDataNew", params, paginationList);
	}

	public List findRelaRptDataNew(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.find("findRelaRptDataNew", params);
	}

	public List findShownColumnNameList(String tableId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		return this.find("findShownColumnNameList", map);
	}

	public List findRptColumnInfo(RptColumnInfo rptColumnInfo) {
		Map map = new HashMap();
		map.put("rptColumnInfo", rptColumnInfo);
		return this.find("findRptColumnInfo", map);
	}

	public List findRptDataX(String columns, String startDate, String endDate,
			String instCode, String tableId, String dataStatus,
			String otherCondition, PaginationList paginationList) {
		Map param = new HashMap();
		param.put("columns", columns);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("instCode", instCode);
		param.put("tableId", tableId);
		param.put("dataStatus", dataStatus);
		param.put("otherCondition", otherCondition);
		return this.find("findRptDataX", param, paginationList);
	}

	public List findRptData2(String columns, String startDate, String endDate,
			String instCode, String tableId, String dataStatus,
			String otherCondition, PaginationList paginationList) {
		Map param = new HashMap();
		param.put("columns", columns);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("instCode", instCode);
		param.put("tableId", tableId);
		param.put("dataStatus", dataStatus);
		param.put("otherCondition", otherCondition);
		return this.find("findRptData2", param, paginationList);
	}

	public int saveRptData(String tableId, String insertColumns,
			String insertValues) {
		Map param = new HashMap();
		param.put("tableId", tableId);
		param.put("insertColumns", insertColumns);
		param.put("insertValues", insertValues);
		return this.save("saveRptData", param);
	}

	public int saveSysData(SysData sysData) {
		Map param = new HashMap();
		param.put("sysData", sysData);
		return this.save("saveSysData", param);
	}
	
	public int updateSysData(SysData sysData) {
		Map param = new HashMap();
		param.put("sysData", sysData);
		return this.save("updateSysData", param);
	}

	public void saveRptDataBatch(List data) {
		this.getDao().insertBatch("saveRptData", data);
	}

	public int updateRptData2(RptData rptData, String busiType) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		int a = 0;
		if (!"3".equals(busiType)) {
			a = this.save("updateRptData", params);
		}
		if (StringUtil.isNotEmpty(rptData.getReasionInfo())) {
			// 如果为审核不通过，且审核理由不为空
			if (StringUtil.isNotEmpty(busiType)) {
				// 更新审核拒绝原因
				params.put("busiType", busiType);
				this.delete("deleteRefuseReasion", params);
				this.save("insertRefuseReasionInfo", params);
			}
		} else {
			// 审核通过，删除所有对应审核拒绝和打回原因
			this.delete("deleteRefuseReasion", params);
		}
		return a;
	}

	public String getRefuseCheckInfo(RptData rptData, String busiType) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		params.put("busiType", busiType);
		List aaa = this.find("findRefuseReasionInfo", params);
		if (aaa.size() > 0) {
			return aaa.get(0).toString();
		} else {
			return "";
		}
	}

	public int updateRptDataForLowerStatus(RptData rptData) {
		Map params = new HashMap();
		if (StringUtil.isNotEmpty(rptData.getBusinessId())) {
			rptData.setRptNo(null);
		}
		params.put("rptData", rptData);
		// 更新业务数据
		int a = this.save("updateRptData", params);
		params.put("busiType", "2");
		// 删除原打回记录
		// this.delete("deleteLowerStatusLog", params);
		this.delete("deleteRefuseReasion", params);
		// 添加打回记录
		// this.save("insertLowerStatusLog", params);
		this.save("insertRefuseReasionInfo", params);
		return a;
	}

	public String getRptCheckInfo(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		List aaa = this.find("findRptDataCheckReasionInfo", params);
		if (aaa.size() > 0) {
			return aaa.get(0).toString();
		} else {
			return "";
		}
	}

	public List findLowerStatusLogReasion(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.find("findLowerStatusLogReasion", params);
	}

	public int updateRptData(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.save("updateRptData", params);
	}

	public void updateRptDataBatch(List data) {
		this.getDao().updateBatch("updateRptData", data);
	}

	public List findCodeDictionaryList(String codeType, String codeSym) {
		Map param = new HashMap();
		param.put("codeType", codeType);
		param.put("codeSym", codeSym);
		return this.find("findCodeDictionaryList", param);
	}

	public Map[] initRptColumnSqlMap(int largestColumnNum) {
		return this.initRptColumnSqlMap(largestColumnNum, "1");
	}

	public Map[] initRptColumnSqlMap(int largestColumnNum, String isShow) {
		HashMap rptColumnListMap = new HashMap();
		HashMap rptColumnSqlMap = new HashMap();
		List rptTables = this.findRptTableInfo(new RptTableInfo(), "");
		for (Iterator i = rptTables.iterator(); i.hasNext();) {
			RptTableInfo rptTable = (RptTableInfo) i.next();
			// 根据表名查找列信息，用于打印报表表头
			List rptColumnList = this
					.findRptColumnInfo(new RptColumnInfo(rptTable.getTableId(),
							isShow, "1", rptTable.getFileType()));
			// 拼查询列
			StringBuffer columns = new StringBuffer();
			int cFlag = 0;
			for (Iterator j = rptColumnList.iterator(); j.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) j.next();
				String columnId = column.getColumnId();
				// 取别名
				column.setAliasColumnId("c" + ++cFlag);
				// 拼查询SQL
				columns.append("t.").append(columnId).append(" as ").append(
						column.getAliasColumnId()).append(",");
			}
			while (cFlag < largestColumnNum) {
				columns.append(" '' as c").append(++cFlag).append(",");
			}
			rptColumnListMap.put(rptTable.getUniqueTable(), rptColumnList);
			rptColumnSqlMap.put(rptTable.getUniqueTable(), columns.toString()
					.substring(0, columns.toString().length() - 1));
		}
		return new HashMap[] { rptColumnListMap, rptColumnSqlMap };
	}

	public Map[] initRptColumnSqlMapNew(int largestColumnNum) {
		HashMap rptColumnListMap = new HashMap();
		HashMap rptColumnSqlMap = new HashMap();
		List rptTables = this.findRptTableInfo(new RptTableInfo(), "");
		for (Iterator i = rptTables.iterator(); i.hasNext();) {
			RptTableInfo rptTable = (RptTableInfo) i.next();
			List rptColumnList = null;
			StringBuffer columns = new StringBuffer();
			int cFlag = 0;
			// 基础
			if (TableIdRela.getJcsyMap().get(rptTable.getTableId()) == null) {
				// 根据表名查找列信息，用于打印报表表头
				rptColumnList = this
						.findRptColumnInfo(new RptColumnInfo(rptTable
								.getTableId(), "1", "1", rptTable.getFileType()));
				// 拼查询列
				for (Iterator j = rptColumnList.iterator(); j.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) j.next();
					if ("table".equals(column.getDataType())) {
						continue;
					}
					String columnId = column.getColumnId();
					// 取别名
					column.setAliasColumnId("c" + ++cFlag);
					// 拼查询SQL
					columns.append("t.").append(columnId).append(" as ")
							.append(column.getAliasColumnId()).append(",");
				}
			}
			// 申报/核销,需特殊处理查询SQL
			else {
				// 基础表的SQL
				rptColumnList = this.findRptColumnInfo(new RptColumnInfo(
						(String) TableIdRela.getJcsyMap().get(
								rptTable.getTableId()), "1", "1", rptTable
								.getFileType()));
				for (Iterator j = rptColumnList.iterator(); j.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) j.next();
					if ("table".equals(column.getDataType())) {
						continue;
					}
					String columnId = column.getColumnId();
					// 取别名
					column.setAliasColumnId("c" + ++cFlag);
					// 拼查询SQL
					columns.append("jt.").append(columnId).append(" as ")
							.append(column.getAliasColumnId()).append(",");
				}
				List rptColumnList2 = this
						.findRptColumnInfo(new RptColumnInfo(rptTable
								.getTableId(), "1", "1", rptTable.getFileType()));
				for (Iterator j = rptColumnList2.iterator(); j.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) j.next();
					if ("table".equals(column.getDataType())) {
						continue;
					}
					String columnId = column.getColumnId();
					// 取别名
					column.setAliasColumnId("c" + ++cFlag);
					// 拼查询SQL
					columns.append("t.").append(columnId).append(" as ")
							.append(column.getAliasColumnId()).append(",");
				}
				rptColumnList.addAll(rptColumnList2);
			}
			while (cFlag < largestColumnNum) {
				columns.append(" '' as c").append(++cFlag).append(",");
			}
			if (StringUtil.isNotEmpty(rptTable.getFileType())) {
				rptColumnListMap.put(rptTable.getTableId() + "#"
						+ rptTable.getFileType(), rptColumnList);
				rptColumnSqlMap.put(rptTable.getTableId() + "#"
						+ rptTable.getFileType(), columns.toString().substring(
						0, columns.toString().length() - 1));
			} else {
				rptColumnListMap.put(rptTable.getTableId(), rptColumnList);
				rptColumnSqlMap.put(rptTable.getTableId(), columns.toString()
						.substring(0, columns.toString().length() - 1));
			}
		}
		return new HashMap[] { rptColumnListMap, rptColumnSqlMap };
	}

	public void deleteRptData(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		this.delete("deleteRptData", params);
	}

	// DFHL:报文生成序号生成开始
	public int getSerialNo(String tableId, String customId, String rptTitle,
			String curDate) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("tableId", tableId);
		// 根据用户所在的机构号，去映射表中寻找对应的申报号
		if (StringUtil.isEmpty(rptTitle) && StringUtil.isNotEmpty(customId)) {
			Map mm = new HashMap();
			mm.put("customid", customId);
			List l = this.find("getRptTitleByCustomId", mm);
			if (CollectionUtils.isNotEmpty(l)) {
				rptTitle = (String) l.get(0);
			}
		}
		if (StringUtil.isNotEmpty(rptTitle)) {
			map.put("customId", rptTitle);
		} else {
			map.put("customId", customId);
		}
		map.put("curDate", curDate);
		Long l = this.getRowCount("getSerialNo", map);
		if (StringUtil.isNotEmpty(rptTitle)) {
			insertSerialNo(tableId, rptTitle, curDate);
		} else {
			insertSerialNo(tableId, customId, curDate);
		}
		if (l == null)
			return 0;
		return l.intValue();
	}

	private void insertSerialNo(String tableId, String customId, String curDate) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("customId", customId);
		map.put("curDate", curDate);
		this.save("saveSerialNo", map);
	}

	// DFHL:报文生成序号生成结束
	// 判断某条记录是否曾经上报过 且接收过反馈（is_receive = '1'）
	public boolean isRptHasSendCommit(String tableId, String businessId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		if (businessId == null || "".equals(businessId))
			return false;
		map.put("businessId", businessId);
		Long lcount = (Long) this.find("getRptSendCommit", map).get(0);
		return (lcount != null && lcount.intValue() > 0) ? true : false;
	}

	// 判断某条记录是否曾经生成过报文（在t_rpt_send_commit表中有对应记录）
	public boolean isRptFileGen(String tableId, String businessId,
			String fileName) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		if (businessId == null || "".equals(businessId))
			return false;
		map.put("businessId", businessId);
		if (StringUtil.isNotEmpty(fileName) && fileName.length() > 3) {
			String systemCode = fileName.substring(0, 3);
			map.put("systemCode", systemCode);
		}
		Long lcount = (Long) this.find("getRptFileGen", map).get(0);
		return (lcount != null && lcount.intValue() > 0) ? true : false;
	}

	// 记录上报记录
	public void insertRptSendCommit(String tableId, String businessId,
			String packName, String fileName, int isReceive) {
		// if (isRptHasSendCommit(tableId, businessId))
		// return;
		if (isRptFileGen(tableId, businessId, fileName))
			return;
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("businessId", businessId);
		map.put("packName", packName);
		map.put("fileName", fileName);
		map.put("isReceive", String.valueOf(isReceive));
		this.save("insertRptSendCommit", map);
	}

	// 记录上报记录
	public void insertRptSendCommitBatch(List sendMapList) {
		this.insertBatch("insertRptSendCommit", sendMapList);
	}

	// 修改报文报送记录
	public void updateRptSendCommitIsReceive(String packName,
			String[] fileNames, String tableId, String businessId, int isReceive) {
		String updateSet = " is_receive = '" + isReceive + "' ";
		String updateWhere = "";
		if (packName != null && !"".equals(packName)) {
			// 解析控制类反馈文件时，修改未发生错误报文文件对应报文数据的接收标识为1
			updateWhere = " packName = '" + packName + "' ";
			if (fileNames != null && fileNames.length > 0) {
				for (int i = 0; i < fileNames.length; i++) {
					if (fileNames[i] != null && !"".equals(fileNames[i])) {
						updateWhere += " and fileName <> '" + fileNames[i]
								+ "' ";
					}
				}
			}
		} else if (tableId != null && !"".equals(tableId) && businessId != null
				&& !"".equals(businessId)) {
			// 解析报文类反馈文件时，修改报文文件中未发生错误报文数据的接收标识为1
			updateWhere = " tableId = '" + tableId + "' and businessId = '"
					+ businessId + "' ";
		}
		Map map = new HashMap();
		map.put("updateSet", updateSet);
		map.put("updateWhere", updateWhere);
		this.update("updateRptSendCommit", map);
	}

	// 修改报文报送记录
	public void updateRptSendCommitPackFile(String tableId, String businessId,
			String packName, String fileName, int isReceive) {
		// 报文重新生成报文时，修改对应包名和报文XML文件名
		String updateSet = " packName = '" + packName + "', fileName = '"
				+ fileName + "' ";
		if (isReceive != -1) {
			updateSet += ", is_receive = '" + isReceive + "' ";
		}
		String updateWhere = "";
		if (tableId != null && !"".equals(tableId) && businessId != null
				&& !"".equals(businessId)) {
			updateWhere = " tableId = '" + tableId + "' and businessId = '"
					+ businessId + "' ";
		}
		Map map = new HashMap();
		map.put("updateSet", updateSet);
		map.put("updateWhere", updateWhere);
		this.update("updateRptSendCommit", map);
	}

	// 查询报文报送记录
	public List findRptSendCommit(String tableId, String businessId,
			String packName, String fileName, int isReceive) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("businessId", businessId);
		map.put("packName", packName);
		map.put("fileName", fileName);
		if (isReceive != -1) {
			map.put("isReceive", String.valueOf(isReceive));
		}
		return find("findRptSendCommit", map);
	}

	public List findRptSendCommitSuccess(String packName, String[] fileNames) {
		Map map = new HashMap();
		map.put("packName", packName);
		if (fileNames != null && fileNames.length > 0) {
			String searchCondition = "";
			for (int i = 0; i < fileNames.length; i++) {
				if (fileNames[i] != null && !"".equals(fileNames[i])) {
					searchCondition += " and fileName <> '" + fileNames[i]
							+ "' ";
				}
			}
			map.put("searchCondition", searchCondition);
		}
		return find("findRptSendCommitSuccess", map);
	}

	/**
	 * 查询报文修改记录(可有翻页)
	 * 
	 * @param rptLogInfo.tableId
	 * @param paginationList
	 * @return List<RptLogInfo>
	 * @author lihaiboA
	 */
	public List findRptLogInfo(RptLogInfo rptLogInfo,
			PaginationList paginationList) {
		Map params = new HashMap();
		params.put("rptLogInfo", rptLogInfo);
		List list = null;
		if (paginationList != null)
			list = this.find("findRptLogInfo", params, paginationList);
		else
			list = this.find("findRptLogInfo", params);
		return list;
	}

	public Long findRptLogInfoCount(RptLogInfo rptLogInfo) {
		Map params = new HashMap();
		params.put("rptLogInfo", rptLogInfo);
		return this.getRowCount("findRptLogInfoCount", params);
	}

	/**
	 * 查询报文修改记录
	 * 
	 * @param rptData.tableId
	 * @param rptData.columns
	 * @param rptData.businessId
	 * @param rptData.subId
	 * @return List<RptLogInfo>
	 */
	public List findRptDataToLogInfo(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		List list = this.find("findRptDataToLogInfo", params);
		return list;
	}

	/**
	 * 新增报文修改记录
	 * 
	 * @param rptLogInfo
	 * @return int
	 */
	public int insertRptLogInfo(RptLogInfo rptLogInfo) {
		StringBuffer insertLogColumns = new StringBuffer();
		StringBuffer insertLogValues = new StringBuffer();
		insertLogColumns
				.append("logtype,tableid,filetype,userid,updatetime,businessno,businessid");
		insertLogValues.append("'").append(rptLogInfo.getLogtype()).append(
				"','").append(rptLogInfo.getTableid()).append("','").append(
				rptLogInfo.getFiletype()).append("','").append(
				rptLogInfo.getUserid()).append("','").append(
				rptLogInfo.getUpdatetime()).append("','").append(
				rptLogInfo.getBusinessno()).append("','").append(
				rptLogInfo.getBusinessid()).append("'");
		if (rptLogInfo.getSubid() != null && !"".equals(rptLogInfo.getSubid())) {
			insertLogColumns.append(",subid");
			insertLogValues.append(",'").append(rptLogInfo.getSubid()).append(
					"'");
		}
		if (rptLogInfo.getDatastatus() != null
				&& !"".equals(rptLogInfo.getDatastatus())) {
			insertLogColumns.append(",datastatus");
			insertLogValues.append(",").append(rptLogInfo.getDatastatus());
		}
		if (rptLogInfo.getColumn01() != null
				&& !"".equals(rptLogInfo.getColumn01())) {
			insertLogColumns.append(",column01");
			insertLogValues.append(",'").append(rptLogInfo.getColumn01())
					.append("'");
		}
		if (rptLogInfo.getColumn02() != null
				&& !"".equals(rptLogInfo.getColumn02())) {
			insertLogColumns.append(",column02");
			insertLogValues.append(",'").append(rptLogInfo.getColumn02())
					.append("'");
		}
		if (rptLogInfo.getColumn03() != null
				&& !"".equals(rptLogInfo.getColumn03())) {
			insertLogColumns.append(",column03");
			insertLogValues.append(",'").append(rptLogInfo.getColumn03())
					.append("'");
		}
		if (rptLogInfo.getColumn04() != null
				&& !"".equals(rptLogInfo.getColumn04())) {
			insertLogColumns.append(",column04");
			insertLogValues.append(",'").append(rptLogInfo.getColumn04())
					.append("'");
		}
		if (rptLogInfo.getColumn05() != null
				&& !"".equals(rptLogInfo.getColumn05())) {
			insertLogColumns.append(",column05");
			insertLogValues.append(",'").append(rptLogInfo.getColumn05())
					.append("'");
		}
		if (rptLogInfo.getColumn06() != null
				&& !"".equals(rptLogInfo.getColumn06())) {
			insertLogColumns.append(",column06");
			insertLogValues.append(",'").append(rptLogInfo.getColumn06())
					.append("'");
		}
		if (rptLogInfo.getColumn07() != null
				&& !"".equals(rptLogInfo.getColumn07())) {
			insertLogColumns.append(",column07");
			insertLogValues.append(",'").append(rptLogInfo.getColumn07())
					.append("'");
		}
		if (rptLogInfo.getColumn08() != null
				&& !"".equals(rptLogInfo.getColumn08())) {
			insertLogColumns.append(",column08");
			insertLogValues.append(",'").append(rptLogInfo.getColumn08())
					.append("'");
		}
		if (rptLogInfo.getColumn09() != null
				&& !"".equals(rptLogInfo.getColumn09())) {
			insertLogColumns.append(",column09");
			insertLogValues.append(",'").append(rptLogInfo.getColumn09())
					.append("'");
		}
		if (rptLogInfo.getColumn10() != null
				&& !"".equals(rptLogInfo.getColumn10())) {
			insertLogColumns.append(",column10");
			insertLogValues.append(",'").append(rptLogInfo.getColumn10())
					.append("'");
		}
		if (rptLogInfo.getColumn11() != null
				&& !"".equals(rptLogInfo.getColumn11())) {
			insertLogColumns.append(",column11");
			insertLogValues.append(",'").append(rptLogInfo.getColumn11())
					.append("'");
		}
		if (rptLogInfo.getColumn12() != null
				&& !"".equals(rptLogInfo.getColumn12())) {
			insertLogColumns.append(",column12");
			insertLogValues.append(",'").append(rptLogInfo.getColumn12())
					.append("'");
		}
		if (rptLogInfo.getColumn13() != null
				&& !"".equals(rptLogInfo.getColumn13())) {
			insertLogColumns.append(",column13");
			insertLogValues.append(",'").append(rptLogInfo.getColumn13())
					.append("'");
		}
		if (rptLogInfo.getColumn14() != null
				&& !"".equals(rptLogInfo.getColumn14())) {
			insertLogColumns.append(",column14");
			insertLogValues.append(",'").append(rptLogInfo.getColumn14())
					.append("'");
		}
		if (rptLogInfo.getColumn15() != null
				&& !"".equals(rptLogInfo.getColumn15())) {
			insertLogColumns.append(",column15");
			insertLogValues.append(",'").append(rptLogInfo.getColumn15())
					.append("'");
		}
		if (rptLogInfo.getColumn16() != null
				&& !"".equals(rptLogInfo.getColumn16())) {
			insertLogColumns.append(",column16");
			insertLogValues.append(",'").append(rptLogInfo.getColumn16())
					.append("'");
		}
		if (rptLogInfo.getColumn17() != null
				&& !"".equals(rptLogInfo.getColumn17())) {
			insertLogColumns.append(",column17");
			insertLogValues.append(",'").append(rptLogInfo.getColumn17())
					.append("'");
		}
		if (rptLogInfo.getColumn18() != null
				&& !"".equals(rptLogInfo.getColumn18())) {
			insertLogColumns.append(",column18");
			insertLogValues.append(",'").append(rptLogInfo.getColumn18())
					.append("'");
		}
		if (rptLogInfo.getColumn19() != null
				&& !"".equals(rptLogInfo.getColumn19())) {
			insertLogColumns.append(",column19");
			insertLogValues.append(",'").append(rptLogInfo.getColumn19())
					.append("'");
		}
		if (rptLogInfo.getColumn20() != null
				&& !"".equals(rptLogInfo.getColumn20())) {
			insertLogColumns.append(",column20");
			insertLogValues.append(",'").append(rptLogInfo.getColumn20())
					.append("'");
		}
		if (rptLogInfo.getColumn21() != null
				&& !"".equals(rptLogInfo.getColumn21())) {
			insertLogColumns.append(",column21");
			insertLogValues.append(",'").append(rptLogInfo.getColumn21())
					.append("'");
		}
		if (rptLogInfo.getColumn22() != null
				&& !"".equals(rptLogInfo.getColumn22())) {
			insertLogColumns.append(",column22");
			insertLogValues.append(",'").append(rptLogInfo.getColumn22())
					.append("'");
		}
		if (rptLogInfo.getColumn23() != null
				&& !"".equals(rptLogInfo.getColumn23())) {
			insertLogColumns.append(",column23");
			insertLogValues.append(",'").append(rptLogInfo.getColumn23())
					.append("'");
		}
		if (rptLogInfo.getColumn24() != null
				&& !"".equals(rptLogInfo.getColumn24())) {
			insertLogColumns.append(",column24");
			insertLogValues.append(",'").append(rptLogInfo.getColumn24())
					.append("'");
		}
		if (rptLogInfo.getColumn25() != null
				&& !"".equals(rptLogInfo.getColumn25())) {
			insertLogColumns.append(",column25");
			insertLogValues.append(",'").append(rptLogInfo.getColumn25())
					.append("'");
		}
		if (rptLogInfo.getColumn26() != null
				&& !"".equals(rptLogInfo.getColumn26())) {
			insertLogColumns.append(",column26");
			insertLogValues.append(",'").append(rptLogInfo.getColumn26())
					.append("'");
		}
		if (rptLogInfo.getColumn27() != null
				&& !"".equals(rptLogInfo.getColumn27())) {
			insertLogColumns.append(",column27");
			insertLogValues.append(",'").append(rptLogInfo.getColumn27())
					.append("'");
		}
		if (rptLogInfo.getColumn28() != null
				&& !"".equals(rptLogInfo.getColumn28())) {
			insertLogColumns.append(",column28");
			insertLogValues.append(",'").append(rptLogInfo.getColumn28())
					.append("'");
		}
		if (rptLogInfo.getColumn29() != null
				&& !"".equals(rptLogInfo.getColumn29())) {
			insertLogColumns.append(",column29");
			insertLogValues.append(",'").append(rptLogInfo.getColumn29())
					.append("'");
		}
		if (rptLogInfo.getColumn30() != null
				&& !"".equals(rptLogInfo.getColumn30())) {
			insertLogColumns.append(",column30");
			insertLogValues.append(",'").append(rptLogInfo.getColumn30())
					.append("'");
		}
		if (rptLogInfo.getColumn31() != null
				&& !"".equals(rptLogInfo.getColumn31())) {
			insertLogColumns.append(",column31");
			insertLogValues.append(",'").append(rptLogInfo.getColumn31())
					.append("'");
		}
		if (rptLogInfo.getColumn32() != null
				&& !"".equals(rptLogInfo.getColumn32())) {
			insertLogColumns.append(",column32");
			insertLogValues.append(",'").append(rptLogInfo.getColumn32())
					.append("'");
		}
		if (rptLogInfo.getColumn33() != null
				&& !"".equals(rptLogInfo.getColumn33())) {
			insertLogColumns.append(",column33");
			insertLogValues.append(",'").append(rptLogInfo.getColumn33())
					.append("'");
		}
		if (rptLogInfo.getColumn34() != null
				&& !"".equals(rptLogInfo.getColumn34())) {
			insertLogColumns.append(",column34");
			insertLogValues.append(",'").append(rptLogInfo.getColumn34())
					.append("'");
		}
		if (rptLogInfo.getColumn35() != null
				&& !"".equals(rptLogInfo.getColumn35())) {
			insertLogColumns.append(",column35");
			insertLogValues.append(",'").append(rptLogInfo.getColumn35())
					.append("'");
		}
		if (rptLogInfo.getColumn36() != null
				&& !"".equals(rptLogInfo.getColumn36())) {
			insertLogColumns.append(",column36");
			insertLogValues.append(",'").append(rptLogInfo.getColumn36())
					.append("'");
		}
		if (rptLogInfo.getColumn37() != null
				&& !"".equals(rptLogInfo.getColumn37())) {
			insertLogColumns.append(",column37");
			insertLogValues.append(",'").append(rptLogInfo.getColumn37())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn38())) {
			insertLogColumns.append(",column38");
			insertLogValues.append(",'").append(rptLogInfo.getColumn38())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn39())) {
			insertLogColumns.append(",column39");
			insertLogValues.append(",'").append(rptLogInfo.getColumn39())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn40())) {
			insertLogColumns.append(",column40");
			insertLogValues.append(",'").append(rptLogInfo.getColumn40())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn41())) {
			insertLogColumns.append(",column41");
			insertLogValues.append(",'").append(rptLogInfo.getColumn41())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn42())) {
			insertLogColumns.append(",column42");
			insertLogValues.append(",'").append(rptLogInfo.getColumn42())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn43())) {
			insertLogColumns.append(",column43");
			insertLogValues.append(",'").append(rptLogInfo.getColumn43())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn44())) {
			insertLogColumns.append(",column44");
			insertLogValues.append(",'").append(rptLogInfo.getColumn44())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn45())) {
			insertLogColumns.append(",column45");
			insertLogValues.append(",'").append(rptLogInfo.getColumn45())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn46())) {
			insertLogColumns.append(",column46");
			insertLogValues.append(",'").append(rptLogInfo.getColumn46())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn47())) {
			insertLogColumns.append(",column47");
			insertLogValues.append(",'").append(rptLogInfo.getColumn47())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn48())) {
			insertLogColumns.append(",column48");
			insertLogValues.append(",'").append(rptLogInfo.getColumn48())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn49())) {
			insertLogColumns.append(",column49");
			insertLogValues.append(",'").append(rptLogInfo.getColumn49())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn50())) {
			insertLogColumns.append(",column50");
			insertLogValues.append(",'").append(rptLogInfo.getColumn50())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn51())) {
			insertLogColumns.append(",column51");
			insertLogValues.append(",'").append(rptLogInfo.getColumn51())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn52())) {
			insertLogColumns.append(",column52");
			insertLogValues.append(",'").append(rptLogInfo.getColumn52())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn53())) {
			insertLogColumns.append(",column53");
			insertLogValues.append(",'").append(rptLogInfo.getColumn53())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn54())) {
			insertLogColumns.append(",column54");
			insertLogValues.append(",'").append(rptLogInfo.getColumn54())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn55())) {
			insertLogColumns.append(",column55");
			insertLogValues.append(",'").append(rptLogInfo.getColumn55())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn56())) {
			insertLogColumns.append(",column56");
			insertLogValues.append(",'").append(rptLogInfo.getColumn56())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn57())) {
			insertLogColumns.append(",column57");
			insertLogValues.append(",'").append(rptLogInfo.getColumn57())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn58())) {
			insertLogColumns.append(",column58");
			insertLogValues.append(",'").append(rptLogInfo.getColumn58())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn59())) {
			insertLogColumns.append(",column59");
			insertLogValues.append(",'").append(rptLogInfo.getColumn59())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn60())) {
			insertLogColumns.append(",column60");
			insertLogValues.append(",'").append(rptLogInfo.getColumn60())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn61())) {
			insertLogColumns.append(",column61");
			insertLogValues.append(",'").append(rptLogInfo.getColumn61())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn62())) {
			insertLogColumns.append(",column62");
			insertLogValues.append(",'").append(rptLogInfo.getColumn62())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn63())) {
			insertLogColumns.append(",column63");
			insertLogValues.append(",'").append(rptLogInfo.getColumn63())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn64())) {
			insertLogColumns.append(",column64");
			insertLogValues.append(",'").append(rptLogInfo.getColumn64())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn66())) {
			insertLogColumns.append(",column66");
			insertLogValues.append(",'").append(rptLogInfo.getColumn66())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn66())) {
			insertLogColumns.append(",column66");
			insertLogValues.append(",'").append(rptLogInfo.getColumn66())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn67())) {
			insertLogColumns.append(",column67");
			insertLogValues.append(",'").append(rptLogInfo.getColumn67())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn68())) {
			insertLogColumns.append(",column68");
			insertLogValues.append(",'").append(rptLogInfo.getColumn68())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn69())) {
			insertLogColumns.append(",column69");
			insertLogValues.append(",'").append(rptLogInfo.getColumn69())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumn70())) {
			insertLogColumns.append(",column70");
			insertLogValues.append(",'").append(rptLogInfo.getColumn70())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm01())) {
			insertLogColumns.append(",columnm01");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm01())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm02())) {
			insertLogColumns.append(",columnm02");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm02())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm03())) {
			insertLogColumns.append(",columnm03");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm03())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm04())) {
			insertLogColumns.append(",columnm04");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm04())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm05())) {
			insertLogColumns.append(",columnm05");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm05())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm11())) {
			insertLogColumns.append(",columnm11");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm11())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm12())) {
			insertLogColumns.append(",columnm12");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm12())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm13())) {
			insertLogColumns.append(",columnm13");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm13())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm14())) {
			insertLogColumns.append(",columnm14");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm14())
					.append("'");
		}
		if (StringUtil.isNotEmpty(rptLogInfo.getColumnm15())) {
			insertLogColumns.append(",columnm15");
			insertLogValues.append(",'").append(rptLogInfo.getColumnm15())
					.append("'");
		}
		Map param = new HashMap();
		param.put("tableId", "t_rpt_log_info");
		param.put("insertColumns", insertLogColumns.toString());
		param.put("insertValues", insertLogValues.toString());
		System.out.println("Insert into t_rpt_log_info ");
		System.out.println(insertLogColumns.toString());
		System.out.println("values (" + insertLogValues.toString() + ") ");
		return this.save("saveRptData", param);
	}

	/**
	 * 判断申报号是否已存在
	 * 
	 * @param rptData.tableId
	 * @param rptData.rptNo
	 * @param rptData.businessId
	 * @return boolean
	 * @author lihaiboA
	 */
	public boolean judgeRptNoRepeat(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		List list = this.find("judgeRptNoRepeat", params);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 查询仅包含业务ID、数据状态和申报号的数据
	 * 
	 * @param rptData.tableId
	 * @param rptData.rptNo
	 * @param rptData.businessId
	 * @return List
	 * @author lihaiboA
	 */
	public List findRptDataReduce(RptData rptData) {
		Map params = new HashMap();
		if (rptData != null) {
			StringBuffer sbColumns = new StringBuffer();
			if (StringUtil.isNotEmpty(rptData.getRptNoColumnId())) {
				sbColumns.append(rptData.getRptNoColumnId()).append(" rptNo, ");
			} else {
				sbColumns.append(" '' rptNo, ");
			}
			if (StringUtil.isNotEmpty(rptData.getByeRptNoColumnId())) {
				sbColumns.append(rptData.getByeRptNoColumnId()).append(
						" byeRptNo ");
			} else {
				sbColumns.append(" '' byeRptNo ");
			}
			if (StringUtil.isNotEmpty(rptData.getColumns())) {
				sbColumns.append(", ").append(rptData.getColumns());
			}
			rptData.setColumns(sbColumns.toString());
		}
		params.put("rptData", rptData);
		List list = this.find("findRptDataReduce", params);
		return list;
	}

	private String getColumnsForReduce(String keyRptNoColumnId,
			String byeRptNoColumnId) {
		StringBuffer sbColumns = new StringBuffer();
		if (StringUtil.isNotEmpty(keyRptNoColumnId)) {
			sbColumns.append(keyRptNoColumnId).append(" rptNo, ");
		} else {
			sbColumns.append(" '' rptNo, ");
		}
		if (StringUtil.isNotEmpty(byeRptNoColumnId)) {
			sbColumns.append(byeRptNoColumnId).append(" byeRptNo ");
		} else {
			sbColumns.append(" '' byeRptNo ");
		}
		return sbColumns.toString();
	}

	/**
	 * 查询仅包含业务ID、数据状态和申报号的数据(单位基本信息)
	 * 
	 * @param rptData.tableId
	 * @param rptData.rptNo
	 * @param rptData.businessId
	 * @return List
	 * @author lihaiboA
	 */
	public List findRptDataReduceCompany(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		List list = this.find("findRptDataReduceCompany", params);
		return list;
	}

	/**
	 * 根据单位基本信息查询仅包含业务ID、数据状态和申报号的基础信息数据
	 * 
	 * @param rptData.tableId
	 * @param rptData.custCode
	 * @param rptData.instCode
	 * @return List
	 * @author lihaiboA
	 */
	public List findBaseRptDataReduceByCompany(RptData rptData) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		List list = this.find("findBaseRptDataReduceByCompany", params);
		return list;
	}

	/**
	 * 判断组织机构代码是否已存在
	 * 
	 * @param rptData.tableId
	 * @param rptData.rptNo
	 * @param rptData.businessId
	 * @return boolean
	 * @author lihaiboA
	 */
	public boolean judgeCustCodeRepeat(String tableId, String custCode) {
		Map params = new HashMap();
		params.put("tableId", tableId);
		params.put("custCode", custCode);
		List list = this.find("judgeCustCodeRepeat", params);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	public List findRptKeywordSendlog(String tableId, String businessId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("businessId", businessId);
		List list = this.find("findRptKeywordSendlog", map);
		return list;
	}

	public void deleteRptKeywordChange(String tableId, String businessId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("businessId", businessId);
		this.delete("deleteRptKeywordChange", map);
	}

	public long findRptKeywordChangeCount(String tableId, String businessId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("businessId", businessId);
		Long lcount = (Long) this.find("findRptKeywordChangeCount", map).get(0);
		return (lcount != null && lcount.longValue() > 0) ? lcount.longValue()
				: 0;
	}

	public void deleteRptSendCommit(String tableId, String businessId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("businessId", businessId);
		this.delete("deleteRptSendCommit", map);
	}

	// 修改数据表信息
	public int updateRptTableInfo(String busiTableId, String updateSql) {
		Map map = new HashMap();
		map.put("busiTableId", busiTableId);
		map.put("updateSql", updateSql);
		return this.save("updateRptTableInfo", map);
	}

	// 修改数据列信息
	public int updateRptColumnInfo(String tableId, String columnId,
			String updateSql) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("columnId", columnId);
		map.put("updateSql", updateSql);
		return this.save("updateRptColumnInfo", map);
	}

	public List findRptBusiDataInfo(RptBusiDataInfo rptBusiDataInfo) {
		Map map = new HashMap();
		map.put("rptBusiDataInfo", rptBusiDataInfo);
		return find("findRptBusiDataInfo", map);
	}

	public List findRptBusiDataInfoWithAll(RptBusiDataInfo rptBusiDataInfo) {
		List busiDataInfoList = this.findRptBusiDataInfo(rptBusiDataInfo);
		RptBusiDataInfo all = new RptBusiDataInfo("1", "1", "1");
		all.setBusiInfoID("ALL");
		all.setBusiInfoName("全部");
		busiDataInfoList.add(0, all);
		return busiDataInfoList;
	}

	public List findRptCfaContract(RptData rptData,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("rptData", rptData);
		if ("A".equals(rptData.getInfoType())) {
			return find("findRptCfaAContract", map, paginationList);
		} else if ("B".equals(rptData.getInfoType())) {
			return find("findRptCfaBContract", map, paginationList);
		} else if ("C".equals(rptData.getInfoType())) {
			return find("findRptCfaCContract", map, paginationList);
		} else if ("D".equals(rptData.getInfoType())) {
			return find("findRptCfaDContract", map, paginationList);
		} else if ("E".equals(rptData.getInfoType())) {
			return find("findRptCfaEContract", map, paginationList);
		} else if ("F".equals(rptData.getInfoType())) {
			return find("findRptCfaFContract", map, paginationList);
		} else {
			return null;
		}
	}

	public String findMaxIndexCode(RptData rptData) {
		Map map = new HashMap();
		map.put("rptData", rptData);
		String maxCode = "";
		try {
			maxCode = (String) this.find("findMaxIndexCode", map).get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return maxCode;
	}

	public String createTablueUniqueId(String tableId, String fileType) {
		if (StringUtil.isNotEmpty(fileType)) {
			if (StringUtil.isEmpty(tableId)) {
				tableId = DataUtil.getTableIdByFileType(fileType);
			} else {
				if (!DataUtil.getTableIdByFileType(fileType).equals(tableId)) {
					if (tableId.indexOf("_SUB_") > 0
							|| tableId.endsWith("_STOCKINFO")
							|| tableId.endsWith("_INVEST")) {
						return tableId;
					} else {
						return new StringBuffer(tableId).append(
								DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL).append("1")
								.toString();
					}
				}
			}
			return new StringBuffer(tableId).append(
					DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL).append(fileType)
					.toString();
		} else {
			return tableId;
		}
	}

	/**
	 * 自动更新DOFOEXLOCODE（国内外汇贷款编号）
	 * 
	 * @param tableId
	 * @param businessId
	 * @return String
	 */
	public String autoUpdateDofoecloCode(String tableId, String businessId) {
		String dofoecloCode = null;
		Map map = new HashMap();
		if (DataUtil.CFA_SELF_TABLE_C.equalsIgnoreCase(tableId)) {
			map.put("cBusinessId", businessId);
		} else if (DataUtil.CFA_SELF_TABLE_D.equalsIgnoreCase(tableId)) {
			map.put("dBusinessId", businessId);
		}
		this.update("autoUpdateDofoecloCode", map);
		map.put("tableId", tableId);
		dofoecloCode = (String) this.find("findDofoecloCode", map).get(0);
		return dofoecloCode;
	}

	public List findRptDataByTableIdAndInstCodes(String tableId,
			String fileType, List instCodes, String datastatus, String columns,
			String searchCondition, String orderBy) {
		Map params = new HashMap();
		params.put("tableId", tableId);
		params.put("fileType", fileType);
		params.put("instCodes", instCodes);
		params.put("datastatus", datastatus);
		params.put("columns", columns);
		params.put("searchCondition", searchCondition);
		params.put("orderBy", orderBy);
		return this.find("findRptDataByTableIdAndInstCodes", params);
	}

	public void lockDatas(String sendPackName) {
		// 1.对已生成状态的数据进行锁定
		// 获得报表列表
		Map params = new HashMap();
		params.put("packName", sendPackName);
		List tableList = this.find("getTableListFromSendCommit", params);
		params.clear();
		// 循环锁定
		RptData rptData = new RptData();
		rptData.setDataStatus(String.valueOf(DataUtil.YSC_STATUS_NUM));// 将已生成
		StringBuffer sbUpdate = new StringBuffer();
		sbUpdate.append(" datastatus = ").append(DataUtil.LOCKED_STATUS_NUM);// 转为锁定状态
		rptData.setUpdateSql(sbUpdate.toString());
		StringBuffer sbCondition = null;
		for (int i = 0; i < tableList.size(); i++) {
			// 关联报文发送记录表，根据报送文件名得到该报文包含数据业务ID
			// LIKE:businessid in (select businessid from t_rpt_send_commit
			// where packname = '123' and tableid='t_base_account')
			String tableId = (String) tableList.get(i);
			sbCondition = new StringBuffer();
			sbCondition
					.append(
							" businessid in (select businessid from t_rpt_send_commit where packname = '")
					.append(sendPackName).append("' and tableid='").append(
							tableId).append("') ");
			rptData.setTableId(tableId);
			rptData.setUpdateCondition(sbCondition.toString());
			params.put("rptData", rptData);
			this.save("updateRptData", params);
			params.clear();
		}
		// 2.标记该xml已经上传至MTS服务目录
		String sbUpdateSet = " is_sendmts = '1' ";
		StringBuffer sbUpdateWhere = new StringBuffer();
		sbUpdateWhere.append(" packname = '").append(sendPackName).append("' ");
		Map map = new HashMap();
		map.put("updateSet", sbUpdateSet);
		map.put("updateWhere", sbUpdateWhere.toString());
		this.save("updateRptSendCommit", map);
	}

	// 判断某条记录是否曾经上报过 且接收过反馈（is_receive = '1'）
	public boolean isFileSendMts(String packName, String fileName) {
		Map map = new HashMap();
		map.put("packName", packName);
		map.put("fileName", fileName);
		map.put("isSendMts", "1");
		List list = (List) this.find("findRptSendCommit", map);
		return (list != null && list.size() > 0) ? true : false;
	}

	public List findUnsettledReport(String tableId, String fileType,
			String notFileType, String dataStatus, String instCode,
			String modifyUser, String noModifyUser, String rptTitle) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("fileType", fileType);
		map.put("notFileType", notFileType);
		map.put("dataStatus", dataStatus);
		map.put("instCode", instCode);
		map.put("modifyUser", modifyUser);
		map.put("noModifyUser", noModifyUser);
		map.put("rptTitle", rptTitle);
		return (List) this.find("findUnsettledReport", map);
	}

	public RptData findRptDataByRptNoAndBusinessNo(String tableId,
			String fileType, String rptNo, String businessNo) {
		if (StringUtils.isEmpty(rptNo) && StringUtils.isEmpty(businessNo))
			return null;
		Map params = new HashMap();
		params.put("tableId", tableId);
		StringBuffer sqlWhere = new StringBuffer();
		if (!StringUtils.isEmpty(rptNo)) {
			sqlWhere.append(DataUtil.getRptNoColumnIdByFileType(fileType))
					.append("='").append(rptNo).append("'");
		} else {
			sqlWhere.append("businessNo='").append(businessNo).append("'");
		}
		sqlWhere.append(" and filetype!='").append(fileType).append("'");
		params.put("sqlWhere", sqlWhere.toString());
		params.put("orderBy", " order by filetype asc ");
		List datas = (List) this.find("getBusinessIdByRptNoAndBusinessNo",
				params);
		if (datas.isEmpty())
			return null;
		else
			return (RptData) datas.get(0);
	}

	public RptData findRptDataByRelatedBusinessId(String tableId,
			String fileType, String businessId) {
		if (StringUtils.isEmpty(businessId))
			return null;
		// 查询关联数据
		RptData data = new RptData();
		data.setTableId(tableId);
		data.setBusinessId(businessId);
		data.setColumns("t." + DataUtil.getRptNoColumnIdByFileType(fileType)
				+ " as rptNo,t.businessNo as businessNo ");
		List m = this.findRptData(data);
		if (m.isEmpty())
			return null;
		data = (RptData) m.get(0);
		return findRptDataByRptNoAndBusinessNo(tableId, fileType, data
				.getRptNo(), data.getBusinessNo());
	}

	public String findRptDataStatus(String tableId, String businessId) {
		if (StringUtils.isEmpty(businessId) || StringUtils.isEmpty(tableId))
			return null;
		Map params = new HashMap();
		params.put("tableId", tableId);
		params.put("businessId", businessId);
		List m = this.find("findRptDataStatusByBusinessId", params);
		if (m.isEmpty())
			return null;
		else
			return (String) m.get(0);
	}

	public String findRptNoByBusinessNo(String rptCfaNoColumnId,
			String tableId, String businessNo) {
		if (StringUtils.isEmpty(rptCfaNoColumnId)
				|| StringUtils.isEmpty(tableId)
				|| StringUtils.isEmpty(businessNo)) {
			return null;
		}
		Map params = new HashMap();
		params.put("rptCfaNoColumnId", rptCfaNoColumnId);
		params.put("tableId", tableId);
		params.put("businessNo", businessNo);
		List m = this.find("findRptNoByBusinessNo", params);
		if (m.isEmpty())
			return null;
		else
			return (String) m.get(0);
	}

	public String findBusinessNoByBusinessId(String tableId, String fileType,
			String businessId) {
		Map params = new HashMap();
		params.put("tableId", tableId);
		params.put("fileType", fileType);
		params.put("businessId", businessId);
		List m = this.find("findBusinessNoByBusinessId", params);
		if (m.isEmpty())
			return null;
		else
			return (String) m.get(0);
	}

	/**
	 * <p>
	 * 方法名称: validateData|描述: 域校验
	 * </p>
	 * <p>
	 * 本注释补充于2011-5-9 LihaiboA
	 * </p>
	 * 
	 * @param rptColumnList
	 *            列信息
	 * @param rptData
	 *            数据
	 * @param isSkipBlanks
	 *            是否忽略空白
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validateData(List rptColumnList, RptData rptData,
			boolean isSkipBlanks) throws Exception {
		// 是否需要进行逻辑规则校验
		boolean result = true;
		boolean needToValidateConsRule = true;
		StringBuffer sb = null;
		// 数据属性校验
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			sb = new StringBuffer();
			RptColumnInfo column = (RptColumnInfo) i.next();
			if (column.getDataType().equalsIgnoreCase("table")
					&& StringUtil.isNotEmpty(rptData.getBusinessId())) {
				String vSubResult = this.validateSubData(column.getColumnId(),
						rptData.getBusinessId(), isSkipBlanks);
				if (StringUtil.isNotEmpty(vSubResult)) {
					column.setDataTypeVSuccess(false);
					column.setDataTypeVDesc(vSubResult);
					needToValidateConsRule = false;
					result = false;
				}
				continue;
			}
			// 根据列的别名从rptData中取对应属性的值，并取数据属性校验规则
			String cData = (String) BeanUtils.getValue(rptData, column
					.getAliasColumnId());
			String dataType = column.getDataType();
			if (dataType != null && dataType.toLowerCase().startsWith("n")) {
				if (cData != null && cData.startsWith(".")) {
					cData = "0" + cData;
				}
				if (cData != null && cData.indexOf(",") > 0) {
					cData = cData.replaceAll(",", "");
				}
			}
			// 数据属性校验结果，并设置结果
			boolean dataTypeVSuccess = DataValidater.validateDataType(cData,
					dataType, sb, isSkipBlanks, column);
			column.setDataTypeVSuccess(dataTypeVSuccess);
			column.setDataTypeVDesc(sb.toString());
			// 只要出现了数据属性校验错误，则不进行逻辑规则校验
			if (!dataTypeVSuccess) {
				needToValidateConsRule = false;
				result = false;
			}
		}
		// 逻辑规则校验
		if (needToValidateConsRule) {
		}
		return result;
	}

	/**
	 * <p>
	 * 方法名称: validateSubData|描述:
	 * </p>
	 * 
	 * @param tableId
	 * @param businessId
	 * @param isSkipBlanks
	 * @return
	 * @throws Exception
	 */
	private String validateSubData(String tableId, String businessId,
			boolean isSkipBlanks) throws Exception {
		String result = "";
		SearchService service = (SearchService) SpringContextUtil
				.getBean("searchService");
		List subList = service.getFalChildren(tableId, businessId);
		if (subList != null && subList.size() > 0) {
			List rptColumnList = this.findRptColumnInfo(new RptColumnInfo(
					tableId, null, "1", null));
			for (int s = 0; s < subList.size(); s++) {
				Object sub = subList.get(s);
				StringBuffer sb = new StringBuffer();
				String rowResult = null;
				for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) i.next();
					Object objData = BeanUtils.getValue(sub, column
							.getColumnId().toLowerCase());
					String cData = (objData == null ? "" : objData.toString());
					String dataType = column.getDataType();
					if (dataType != null
							&& dataType.toLowerCase().startsWith("n")) {
						if (cData != null && cData.startsWith(".")) {
							cData = "0" + cData;
						}
						if (cData != null && cData.indexOf(",") > 0) {
							cData = cData.replaceAll(",", "");
						}
					}
					// 数据属性校验结果，并设置结果
					boolean dataTypeVSuccess = DataValidater.validateDataType(
							cData, dataType, sb, isSkipBlanks, column);
					if (!dataTypeVSuccess && sb != null
							&& sb.toString().length() > 0) {
						if (rowResult == null) {
							rowResult = "第" + (s + 1) + "笔：";
						}
						rowResult += "[" + column.getColumnName() + "]"
								+ sb.toString() + "；";
					}
				}
				if (StringUtil.isNotEmpty(rowResult)) {
					result += rowResult;
				}
			}
		}
		return result;
	}

	public List findPackNameListByInstCode(List instList) {
		Map param = new HashMap();
		param.put("instList", instList);
		return this.find("findPackNameListByInstCode", param);
	}

	public List findPackNameListByUserId(String userId) {
		Map param = new HashMap();
		param.put("userId", userId);
		return this.find("findPackNameListByInstCode", param);
	}

	public ReceiveReport findReceiveReportByDataNumber(String dataNo) {
		Map map = new HashMap();
		ReceiveReport receiveReport = new ReceiveReport();
		receiveReport.setDataNumber(dataNo);
		map.put("receiveReport", receiveReport);
		List list = this.find("findReceiveReportByDataNumber", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (ReceiveReport) list.get(0);
		}
		return null;
	}

	public void updateRptDatastatus(String tableId, final List rptDataList) {
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("update ").append(tableId).append(" set datastatus=? ");
		if (rptDataList != null) {
			RptData rd = (RptData) rptDataList.get(0);
			if (rd != null) {
				if (StringUtil.isNotEmpty(rd.getModifyUser())) {
					sbSQL.append(", modifyuser=? ");
				}
				if (StringUtil.isNotEmpty(rd.getBusinessNo())) {
					sbSQL.append(", businessno=? ");
				}
			}
		}
		sbSQL.append(" where businessid=?");
		final String sql = sbSQL.toString();
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			public void setValues(PreparedStatement pStatement, int i)
					throws SQLException {
				int nIndex = 1;
				RptData rptData = (RptData) rptDataList.get(i);
				pStatement.setString(nIndex++, rptData.getDataStatus());
				if (sql.indexOf("modifyuser=") > 0) {
					pStatement.setString(nIndex++, rptData.getModifyUser());
				}
				if (sql.indexOf("businessno=") > 0) {
					pStatement.setString(nIndex++, rptData.getBusinessNo());
				}
				pStatement.setString(nIndex++, rptData.getBusinessId());
			}

			public int getBatchSize() {
				return rptDataList.size();
			}
		});
	}

	public void batchLowerStatusLinkage(String tableId, final List rptDataList) {
		StringBuffer sbSQL = new StringBuffer();
		sbSQL
				.append("update ")
				.append(tableId)
				.append(
						" t set t.datastatus = ? where t.fileType <> ? and exists (select 1 from ")
				.append(tableId)
				.append(
						" c where c.businessno = t.businessno and c.businessid = ?)")
				.append(" and t.datastatus <> ")
				.append(DataUtil.YBS_STATUS_NUM)
				.append(" and t.datastatus <> ").append(
						DataUtil.LOCKED_STATUS_NUM).append(
						" and t.datastatus <> ").append(
						DataUtil.DELETE_STATUS_NUM);
		getJdbcTemplate().batchUpdate(sbSQL.toString(),
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement pStatement, int i)
							throws SQLException {
						RptData rptData = (RptData) rptDataList.get(i);
						pStatement.setString(1, rptData.getDataStatus());
						pStatement.setString(2, rptData.getFileType());
						pStatement.setString(3, rptData.getBusinessId());
					}

					public int getBatchSize() {
						return rptDataList.size();
					}
				});
	}

	public List findRptCountGroupbyStatus(RptData rptData) {
		if (StringUtils.isEmpty(rptData.getTableId()))
			return null;
		Map params = new HashMap();
		params.put("rptData", rptData);
		return this.find("findRptCountGroupbyStatus", params);
	}

	public void updateRptDataStatusByInstCodes(String tableId,
			String updateSql, int dataStatus, String[] instCodes,
			String whereCondition, String rptTitle) {
		Map params = new HashMap();
		params.put("tableId", tableId);
		params.put("updateSql", updateSql);
		params.put("dataStatus", String.valueOf(dataStatus));
		params.put("whereCondition", whereCondition);
		if (StringUtils.isNotEmpty(rptTitle)) {
			params.put("instCodes", null);
			params.put("rptTitle", rptTitle);
			this.update("updateRptDataStatusByInstCodes", params);
		}
		if (instCodes != null && instCodes.length > 0) {
			params.put("instCodes", instCodes);
			params.put("rptTitle", null);
			this.update("updateRptDataStatusByInstCodes", params);
		}
	}

	public String findIndexCodeForSelf(String tableId, String fileType,
			String columnId, String rptNo, String businessNo) {
		String indexCode = "0001";
		if (StringUtil.isNotEmpty(tableId) && StringUtil.isNotEmpty(fileType)
				&& StringUtil.isNotEmpty(columnId)
				&& StringUtil.isNotEmpty(businessNo)) {
			StringBuffer sbSearchCondition = new StringBuffer();
			sbSearchCondition.append(" fileType = '").append(fileType).append(
					"' and businessNo = '").append(businessNo).append("' ");
			if (StringUtil.isNotEmpty(rptNo)) {
				String rptNoColumnId = DataUtil
						.getRptNoColumnIdByFileType(fileType);
				sbSearchCondition.append(" and ").append(rptNoColumnId).append(
						" = '").append(rptNo).append("' ");
			}
			RptData rptData = new RptData();
			rptData.setTableId(tableId);
			rptData.setColumnId(columnId);
			rptData.setSearchCondition(sbSearchCondition.toString());
			String maxIndexCode = this.findMaxIndexCode(rptData);
			if (StringUtil.isNumLegal(maxIndexCode)) {
				int nIndex = Integer.valueOf(maxIndexCode).intValue() + 1;
				if (nIndex < 10) {
					indexCode = "000" + String.valueOf(nIndex);
				} else if (nIndex < 100) {
					indexCode = "00" + String.valueOf(nIndex);
				} else if (nIndex < 1000) {
					indexCode = "0" + String.valueOf(nIndex);
				} else if (nIndex < 10000) {
					indexCode = String.valueOf(nIndex);
				} else {
					return null;
				}
			}
		}
		return indexCode;
	}

	/**
	 * 根据包名，获取此报文包所包括的业务表名称
	 * 
	 * @param packName
	 * @return List
	 */
	public List findTableListFromSendCommit(String packName) {
		Map params = new HashMap();
		params.put("packName", packName);
		return this.find("getTableListFromSendCommit", params);
	}

	/**
	 * 删除审核不通过、打回原因
	 * 
	 * @param rptData
	 * @param busiType
	 */
	public void deleteRefuseReasion(RptData rptData, String busiType,
			String deleteCondition) {
		Map params = new HashMap();
		params.put("rptData", rptData);
		params.put("busiType", busiType);
		params.put("deleteCondition", deleteCondition);
		this.delete("deleteRefuseReasion", params);
	}

	/**
	 * 依据报送记录，添加打回原因（为解析ErrorFiles目录信息所用）
	 * 
	 * @param reasion
	 * @param busiType
	 * @param packName
	 */
	public void insertRefuseReasionFromSendCommit(String reasion,
			String busiType, String packName) {
		Map params = new HashMap();
		params.put("reasion", reasion);
		params.put("busiType", busiType);
		params.put("packName", packName);
		this.save("insertRefuseReasionFromSendCommit", params);
	}

	public List findBussTypeList(String userId) {
		Map param = new HashMap();
		param.put("userId", userId);
		return this.find("findBussTypeListByUserId", param);
	}

	public List findBussTypeList(String code, String name) {
		Map param = new HashMap();
		param.put("bussTypeCode", code);
		param.put("bussTypeName", name);
		return this.find("findBussTypeList", param);
	}

	public void saveBussType(String code, String name) {
		Map param = new HashMap();
		param.put("bussTypeCode", code);
		param.put("bussTypeName", name);
		param.put("isEnabled", "1");
		this.save("insertBussType", param);
	}

	public void updateBussType(String code, String name, String enabled) {
		Map param = new HashMap();
		param.put("bussTypeCode", code);
		param.put("bussTypeName", name);
		param.put("isEnabled", enabled);
		this.update("updateBussType", param);
	}

	public void modifyRefuseReasion(String busiType, final List rptDataList) {
		String sqlDel = "delete from t_refuse_resion where busi_type = '"
				+ busiType + "' and businessid = ? and busi_table_id = ? ";
		getJdbcTemplate().batchUpdate(sqlDel,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement pStatement, int i)
							throws SQLException {
						RptData rptData = (RptData) rptDataList.get(i);
						pStatement.setString(1, rptData.getBusinessId());
						pStatement.setString(2, rptData.getTableId());
					}

					public int getBatchSize() {
						return rptDataList.size();
					}
				});
		String sqlAdd = "insert into t_refuse_resion (businessid, busi_table_id, reasion, busi_type) "
				+ "values (?,?,?,'" + busiType + "')";
		getJdbcTemplate().batchUpdate(sqlAdd,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement pStatement, int i)
							throws SQLException {
						RptData rptData = (RptData) rptDataList.get(i);
						pStatement.setString(1, rptData.getBusinessId());
						pStatement.setString(2, rptData.getTableId());
						pStatement.setString(3, rptData.getReasionInfo());
					}

					public int getBatchSize() {
						return rptDataList.size();
					}
				});
	}

	public void updateRptSendCommitIsReceiveBatch(final List rptDataList,
			String isReceive, String fileName) {
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("update t_rpt_send_commit set is_receive = '").append(
				isReceive).append("' where tableId = ? and businessId = ? ");
		if (StringUtil.isNotEmpty(fileName)) {
			sbSQL.append(" and fileName = '").append(fileName).append("' ");
		}
		getJdbcTemplate().batchUpdate(sbSQL.toString(),
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement pStatement, int i)
							throws SQLException {
						RptData rptData = (RptData) rptDataList.get(i);
						pStatement.setString(1, rptData.getTableId());
						pStatement.setString(2, rptData.getBusinessId());
					}

					public int getBatchSize() {
						return rptDataList.size();
					}
				});
	}
}
