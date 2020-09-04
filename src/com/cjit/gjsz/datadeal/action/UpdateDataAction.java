/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

// import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptKeywordSendLog;
import com.cjit.gjsz.datadeal.model.RptLogInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class UpdateDataAction extends DataDealAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4511839364420984118L;

	/**
	 * 外部表更新数据的入口
	 * 
	 * @return
	 */
	public String updateData() {
		log.info("UpdateDataAction-updateData");
		this.initConfigParameters();// 初始化参数配置
		return updateData(infoTypeCode, tableId);
	}

	/**
	 * 更新数据的通用方法
	 * 
	 * @return
	 */
	public String updateData(String infoTypeCode, String tableId) {
		// 取消掉子表排序缓存信息
		request.getSession().setAttribute("orderColumnSub", null);
		request.getSession().setAttribute("orderDirectionSub", null);
		// 上级报文ID
		this.previousTableId = request.getParameter("previousTableId");
		this.request.setAttribute("previousTableId", previousTableId);
		// Object reportData = null;
		try {
			boolean checkFlag = request.getAttribute("checkFlag") != null;
			this.fromFlag = request.getParameter("fromFlag");
			request.setAttribute("fromFlag", this.fromFlag);
			this.busiDataType = (String) this.request
					.getParameter("busiDataType");
			this.infoType = (String) this.request.getParameter("infoType");
			this.fileType = request.getParameter("fileType");
			// 客户类型（对公/对私）
			String custType = "";
			// 关键字列表
			List keywordList = new ArrayList();
			// 页面输入操作类型
			String actionType = "";
			// 申报号码
			String rptNo = null;
			// 银行自身外债编号
			String cfaRptNo = null;
			// 业务编号
			String businessNo = (String) this.request
					.getParameter("businessNo");
			// 银行自身外债编号对应数据字段ID
			String rptNoColumnId = DataUtil
					.getRptNoColumnIdByFileType(fileType);
			// 原业务编号
			String oriBusinessNo = null;
			// 是否联动更新业务编号
			boolean linkageBusinessNo = false;
			if ("yes".equalsIgnoreCase(this.linkageUpdateBusinessNo)
					&& "1".equals(this.busiDataType)
					&& TableIdRela.getZjxyMap().get(this.fileType) != null) {
				linkageBusinessNo = true;
				RptData rd = new RptData();
				rd.setTableId(tableId);
				rd.setBusinessId(businessId);
				rd.setColumns(" t.businessNo ");
				List rdList = dataDealService.findRptDataReduce(rd);
				if (rdList != null) {
					RptData rdReduce = (RptData) rdList.get(0);
					if (rdReduce != null) {
						oriBusinessNo = rdReduce.getBusinessNo();
					}
				}
			}
			// 在t_rpt_log_info表中查询是否已存在当前记录的对应信息
			Long logCount = dataDealService.findRptLogInfoCount(new RptLogInfo(
					tableId, null, null, businessId, subId));
			if (logCount == null || logCount.longValue() == 0) {
				// 需先将修改前的当前记录插入t_rpt_log_info表中
				if (StringUtil.isNotEmpty(subId)) {
					this.saveInnerRptDataLog(tableId, businessId, subId,
							"init", DateUtils.serverCurrentTimeStamp());
				} else {
					this.saveRptDataLog("init", tableId, fileType, businessId,
							businessNo, true);
				}
			}
			StringBuffer updateSql = new StringBuffer("");
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			StringBuffer insertLogColumns = new StringBuffer();
			StringBuffer insertLogValues = new StringBuffer();
			insertLogColumns
					.append("logtype,tableid,filetype,userid,updatetime,businessid");
			insertLogValues.append("'update','").append(tableId).append("','")
					.append(this.fileType == null ? "" : this.fileType).append(
							"','").append(currentUser.getId()).append("','")
					.append(DateUtils.serverCurrentTimeStamp()).append("','")
					.append(businessId).append("'");
			if (subId != null && !"".equals(subId)) {
				insertLogColumns.append(",subid");
				insertLogValues.append(",'").append(subId).append("'");
			}
			// 根据报表ID获取报表信息
			// List tables = dataDealService.findRptTableInfo(new RptTableInfo(
			// tableId));
			// rptTableInfo = (RptTableInfo) tables.get(0);
			rptTableInfo = dataDealService.findRptTableInfoById(tableId,
					fileType);
			this.request.setAttribute("rptTableInfo", rptTableInfo);
			boolean businessNoIsRepeat = false;// 签约信息的业务编号是否重复
			// 物理表的列信息
			rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							this.fileType));
			// 字段按order赋别名c1,c2,c3...,并根据字段物理名和别名拼查询SQL
			int cFlag = 0;
			StringBuffer columns = new StringBuffer();// 查询数据库中该报文存储的信息所用
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				if ("table".equals(column.getDataType())) {
					continue;
				}
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				// 根据字段物理名和别名拼查询SQL
				columns.append("t.").append(column.getColumnId())
						.append(" as ").append(column.getAliasColumnId())
						.append(",");
				// 根据别名获取属性值
				String columnValue = (String) BeanUtils.getProperty(rptData,
						column.getAliasColumnId());
				// System.out.println(column.getLogColumnId() + " :: " +
				// columnValue);
				// 得到客户类型（对公/对私）
				if ("CUSTYPE".equals(column.getColumnId())) {
					custType = columnValue;
				} else if ("ACTIONTYPE".equals(column.getColumnId())) {
					actionType = columnValue;
				} else if ("RPTNO".equals(column.getColumnId())) {
					rptNo = columnValue;
				} else if (rptNoColumnId.equals(column.getColumnId())) {
					cfaRptNo = columnValue;
				} else if ("BUSINESSNO".equals(column.getColumnId())) {
					businessNo = columnValue;
				}
				// 字段值为空时添加null到更新语句
				if (columnValue == null || "".equals(columnValue.trim())) {
					if ("ACTIONTYPE".equals(column.getColumnId())) {
						updateSql.append(column.getColumnId()).append("= '',");
					} else if ("BUSINESSNO".equals(column.getColumnId())) {
						// 不允许在编辑页面将原有的业务编号清空
						continue;
					} else {
						// 判断是否需要自动生成如下编号
						boolean createIndexCode = false;
						if ("CHANGENO".equalsIgnoreCase(column.getColumnId())
								&& ("AR".equals(this.fileType)
										|| "AS".equals(this.fileType)
										|| "CB".equals(this.fileType)
										|| "DB".equals(this.fileType) || "EB"
										.equals(this.fileType))) {
							// 变动编号
							createIndexCode = true;
						} else if ("COMPLIANCENO".equalsIgnoreCase(column
								.getColumnId())
								&& "BC".equals(this.fileType)) {
							// 履约编号
							createIndexCode = true;
						} else if ("TERPAYCODE".equalsIgnoreCase(column
								.getColumnId())
								&& "FB".equals(this.fileType)) {
							// 终止支付编号
							createIndexCode = true;
						} else if ("INPAYCODE".equalsIgnoreCase(column
								.getColumnId())
								&& "FC".equals(this.fileType)) {
							// 付息编号
							createIndexCode = true;
						}
						if (createIndexCode) {
							columnValue = dataDealService.findIndexCodeForSelf(
									tableId, fileType, column.getColumnId(),
									cfaRptNo, businessNo);
							updateSql.append(column.getColumnId()).append("='")
									.append(columnValue).append("',");
							insertLogColumns.append(",").append(
									column.getLogColumnId());
							insertLogValues.append(",'").append(columnValue)
									.append("'");
						} else {
							updateSql.append(column.getColumnId()).append(
									"= null,");
						}
						if (linkageBusinessNo
								&& "BUSINESSNO".equals(column.getColumnId())) {
						}
					}
				} else {
					if ("n".equalsIgnoreCase(column.getDataType().substring(0,
							1))) {
						columnValue = columnValue.replaceAll(",", "");
						updateSql.append(column.getColumnId()).append("=")
								.append(columnValue.replaceAll("'", "''"))
								.append(",");
					} else if ("d".equalsIgnoreCase(column.getDataType()
							.substring(0, 1))) {
						columnValue = columnValue.replaceAll("-", "");
						updateSql.append(column.getColumnId()).append("='")
								.append(columnValue.replaceAll("'", "''"))
								.append("',");
					} else {
						updateSql.append(column.getColumnId()).append("='")
								.append(columnValue.replaceAll("'", "''"))
								.append("',");
					}
					if (StringUtil.isNotEmpty(column.getLogColumnId())) {
						insertLogColumns.append(", ").append(
								column.getLogColumnId());
						insertLogValues.append(", '").append(
								columnValue.replaceAll("'", "''")).append("'");
					} else if ("BUSINESSNO".equals(column.getColumnId())) {
						String relatedFileType = (String) configMap
								.get("config.related.filetype");
						if (relatedFileType != null
								&& relatedFileType.indexOf(fileType) < 0) {
							businessNoIsRepeat = this.checkBusinessNoRepeat(
									columnValue, tableId, fileType, businessId);
							if (businessNoIsRepeat) {
								column.setDataTypeVSuccess(false);
								column.setDataTypeVDesc("业务编号已存在！");
							}
						}
						insertLogColumns.append(",businessno");
						insertLogValues.append(",'").append(
								columnValue.replaceAll("'", "''")).append("'");
					}
				}
				if ("3".equals(column.getTagType())) {
					Map dictionaryMap = (Map) SystemCache
							.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
					if (dictionaryMap != null) {
						Map tableMap = (HashMap) dictionaryMap.get(tableId);
						if (tableMap != null) {
							List codeDictionaryList = (ArrayList) tableMap
									.get(column.getDictionaryTypeId());
							if (codeDictionaryList != null) {
								this.addFieldToRequest(column
										.getAliasColumnId()
										+ "_list", codeDictionaryList);
							}
						}
					}
				}
			}
			while (cFlag < largestColumnNum) {
				columns.append("'' as c").append(++cFlag).append(",");
			}
			// 保存前执行域校验
			if (!checkFlag
					&& (businessNoIsRepeat || !validateData(rptColumnList,
							rptData, true))) {
				this.message = "checkFailure";
				// 在wl8.1下放入request
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				this.request.setAttribute("tableId", this.tableId);
				this.request.setAttribute("rptData", this.rptData);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("rptTableInfo", this.rptTableInfo);
				this.request.setAttribute("message", this.message);
				this.request.setAttribute("busiDataType", this.busiDataType);
				this.request.setAttribute("infoType", this.infoType);
				this.request.setAttribute("fileType", this.fileType);
				this.request.setAttribute("businessId", this.businessId);
				return INPUT;
			}
			if (!checkFlag && DataUtil.isJCDWSBHX(infoTypeCode)) {
				// updateSql.append(" datastatus = 1,");
				updateSql.append(" datastatus = " + DataUtil.WJY_STATUS_NUM
						+ ",");
				insertLogColumns.append(", datastatus ");
				insertLogValues.append(", ").append(DataUtil.WJY_STATUS_NUM);
			}
			// 执行更新操作
			if (updateSql.length() > 0) {
				// 更新当前记录信息
				dataDealService.updateRptData(new RptData(tableId, updateSql
						.toString().substring(0, updateSql.length() - 1),
						businessId, subId, null, true));
				// 添加报文修改记录
				dataDealService.saveRptData("t_rpt_log_info", insertLogColumns
						.toString(), insertLogValues.toString());
				if (!checkFlag) {
					String updateRptNoSql = "";
					// 更新所有直接和非直接下游报表的状态为1未校验
					// 如果下游报文已经提交，则修改为未提交
					// 同时保持所有直接和非直接下游报表的申报号与当前报文申报号一致
					String zjxyTableId = (String) TableIdRela.getZjxyMap().get(
							tableId);
					if (zjxyTableId != null) {
						if (StringUtil.isEmpty(rptNo)) {
							updateRptNoSql = ", rptno = null ";
						} else {
							updateRptNoSql = ", rptno = '" + rptNo + "' ";
						}
						// dataDealService.updateRptData(new
						// RptData(zjxyTableId,
						// " datastatus = 1 "+updateRptNoSql, businessId, null,
						// null, true));
						RptData upRptData = new RptData(zjxyTableId,
								" datastatus = " + DataUtil.WJY_STATUS_NUM
										+ " " + updateRptNoSql, businessId,
								null, null, true);
						// 不将状态为已报送的下游报文状态改为1
						upRptData.setNotDataStatus(String
								.valueOf(DataUtil.YBS_STATUS_NUM));
						dataDealService.updateRptData(upRptData);
						String djxyTableId = (String) TableIdRela.getZjxyMap()
								.get(zjxyTableId);
						if (djxyTableId != null) {
							// dataDealService.updateRptData(new
							// RptData(djxyTableId,
							// " datastatus = 1 " + updateRptNoSql,
							// businessId, null, null, true));
							upRptData = new RptData(djxyTableId,
									" datastatus = " + DataUtil.WJY_STATUS_NUM
											+ " " + updateRptNoSql, businessId,
									null, null, true);
							// 不将状态为已报送的下游报文状态改为1
							upRptData.setNotDataStatus(String
									.valueOf(DataUtil.YBS_STATUS_NUM));
							dataDealService.updateRptData(upRptData);
						}
					}
				}
				if (linkageBusinessNo && StringUtil.isNotEmpty(oriBusinessNo)
						&& StringUtil.isNotEmpty(businessNo)) {
					String zjxyFileType = (String) TableIdRela.getZjxyMap()
							.get(this.fileType);
					if (StringUtil.isNotEmpty(zjxyFileType)) {
						StringBuffer sbUpdateSql = new StringBuffer();
						sbUpdateSql.append(" businessNo = '")
								.append(businessNo).append("' ");
						String updateCondition = " businessNo = '"
								+ oriBusinessNo + "' ";
						RptData upRptData = new RptData();
						upRptData.setUpdateSql(sbUpdateSql.toString());
						upRptData.setUpdateCondition(updateCondition);
						upRptData.setTableId(tableId);
						dataDealService.updateRptData(upRptData);
					}
				}
			}
			// 如果是子表，更新其对应的主表状态为1：未校验
			if (!checkFlag && !DataUtil.isJCDWSBHX(infoTypeCode)) {
				RptData rptData = new RptData();
				rptData.setTableId((String) this
						.getFieldFromSession(ScopeConstants.CURRENT_TABLE_ID));
				rptData
						.setBusiDataType((String) this
								.getFieldFromSession(ScopeConstants.CURRENT_BUSIDATATYPE));
				rptData.setInfoType((String) this
						.getFieldFromSession(ScopeConstants.CURRENT_INFOTYPE));
				rptData.setFileType((String) this
						.getFieldFromSession(ScopeConstants.CURRENT_FILETYPE));
				rptData.setBusinessId(businessId);
				// rptData.setUpdateSql(" datastatus = 1 ");
				rptData.setUpdateSql(" datastatus = " + DataUtil.WJY_STATUS_NUM
						+ " ");
				dataDealService.updateRptData(rptData);
			}
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				this.addFieldToSession(ScopeConstants.CURRENT_INFO_TYPE_CODE,
						this.infoTypeCode);
				this
						.addFieldToSession(ScopeConstants.CURRENT_TABLE_ID,
								tableId);
			} else if (DataUtil.isInner(infoTypeCode)) {
				this.addFieldToSession(ScopeConstants.CURRENT_TABLE_ID_INNER,
						tableId);
			}
			this.addFieldToSession(ScopeConstants.CURRENT_BUSINESS_ID,
					businessId);
			if (subId != null) {
				this.addFieldToSession(ScopeConstants.CURRENT_SUB_ID, subId);
			}
			this.addFieldToSession(ScopeConstants.CURRENT_BUSIDATATYPE,
					busiDataType);
			this.addFieldToSession(ScopeConstants.CURRENT_INFOTYPE, infoType);
			this.addFieldToSession(ScopeConstants.CURRENT_FILETYPE,
					this.fileType);
			User user = this.getCurrentUser();
			String menuName = "数据录入." + rptTableInfo.getInfoType();
			String style = "单据";
			if (tableIdInner != null) {
				style = "字段科目";
				menuName = "数据录入";
			}
			logManagerService.writeLog(request, user, "0001", menuName, "更新",
					"针对[机构：" + this.instCode + "，" + style + "："
							+ rptTableInfo.getTableName() + "，业务号："
							+ businessId + "]执行数据更新操作", "1");
			this.request.setAttribute("busiDataType", this.busiDataType);
			this.request.setAttribute("infoType", this.infoType);
			return SUCCESS;
		} catch (Exception e) {
			User user = this.getCurrentUser();
			String menuName = "数据录入." + rptTableInfo.getInfoType();
			String style = "单据";
			if (tableIdInner != null) {
				style = "字段科目";
				menuName = "数据录入";
			}
			logManagerService.writeLog(request, user, "0001", menuName, "更新",
					"针对[机构：" + this.instCode + "，" + style + "："
							+ rptTableInfo.getTableName() + "，业务号："
							+ businessId + "]执行数据更新操作", "0");
			e.printStackTrace();
			log.error("UpdateDateAction-updateData", e);
			return ERROR;
		}
	}

	// private void setBaseObject() throws IllegalAccessException,
	// InvocationTargetException, NoSuchMethodException {
	// if (request.getParameter("sbHxFlag") != null && "1".equals(sbHxFlag)) {
	// // 未申报/未核销，new一个rptData,设置actiontype, actiondesc, rptno的值
	// String bid = (businessId == null && rptData != null) ? BeanUtils
	// .getProperty(rptData, "businessId").toString() : businessId;
	// String baseId = (String) TableIdRela.getJcsyMap().get(tableId);
	// Object obj = searchService.getDataVerifyModel(baseId, bid);
	// if (obj != null) {
	// this.addFieldToStack(BASE_OBJECT, obj);
	// }
	// }
	// }
	private List findKeywordSendLog(String tableId, String businessId,
			StringBuffer columns) {
		List rptKwSendLog = new ArrayList();
		try {
			if (tableId != null && !"".equals(tableId) && businessId != null
					&& !"".equals(businessId)) {
				rptKwSendLog = dataDealService.findRptKeywordSendlog(tableId,
						businessId);
				if (rptKwSendLog == null || rptKwSendLog.size() == 0) {
					// 不存在报文关键字报送记录
					// 查询数据库中该报文存储的信息
					List rpDBList = dataDealService
							.findRptData(new RptData(tableId, columns
									.toString().substring(0,
											columns.toString().length() - 1),
									instCode, dataStatus, businessId,
									orderColumn, orderDirection));
					if (rpDBList != null && rpDBList.size() > 0) {
						RptData rdDB = (RptData) rpDBList.get(0);
						String insertColumns = "tableid,businessid,columnid,columnvalue";
						if (rdDB != null) {
							int cFlag = 0;
							for (Iterator i = rptColumnList.iterator(); i
									.hasNext();) {
								RptColumnInfo column = (RptColumnInfo) i.next();
								// 赋别名c1,c2,c3
								column.setAliasColumnId("c" + (++cFlag));
								// 获取并对比关键字段信息
								if ("1".equals(column.getIsKeyword())) {
									String columnValue = (String) BeanUtils
											.getProperty(rdDB, column
													.getAliasColumnId());
									RptKeywordSendLog rptKey = new RptKeywordSendLog();
									rptKey.setTableId(tableId);
									rptKey.setBusinessId(businessId);
									rptKey.setColumnId(column.getColumnId());
									rptKey.setColumnValue(columnValue);
									rptKwSendLog.add(rptKey);
									StringBuffer insertValues = new StringBuffer();
									insertValues.append("'").append(tableId)
											.append("','").append(businessId)
											.append("','").append(
													rptKey.getColumnId())
											.append("','").append(columnValue)
											.append("'");
									dataDealService.saveRptData(
											"t_rpt_keyword_sendlog",
											insertColumns, insertValues
													.toString());
								}
							}
						}
					}
				} else {
					// 存在报文关键字报送记录
					return rptKwSendLog;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rptKwSendLog;
	}

	private void keywordChanged(String tableId, String businessId) {
		if (tableId != null && !"".equals(tableId) && businessId != null
				&& !"".equals(businessId)) {
			String updateTime = DateUtils.serverCurrentDateTime();
			StringBuffer insertColumns = new StringBuffer();
			StringBuffer insertValues = new StringBuffer();
			insertColumns.append("tableId,businessId,updateTime");
			insertValues.append("'").append(tableId).append("','").append(
					businessId).append("','").append(updateTime).append("'");
			dataDealService.saveRptData("t_rpt_keyword_change", insertColumns
					.toString(), insertValues.toString());
		}
	}
}