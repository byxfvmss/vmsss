/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

// import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
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
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class SaveDataAction extends DataDealAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7444627251635020624L;

	/**
	 * 外部表保存数据的入口
	 * 
	 * @return
	 */
	public String saveData() {
		return saveData(infoTypeCode, tableId);
	}

	/**
	 * 保存信息（插入记录）的通用方法
	 * 
	 * @return
	 */
	public String saveData(String infoTypeCode, String tableId) {
		log.info("SaveDataAction-saveData");
		this.initConfigParameters();// 初始化参数配置
		// 取消掉子表排序缓存信息
		request.getSession().setAttribute("orderColumnSub", null);
		request.getSession().setAttribute("orderDirectionSub", null);
		// 上级报文ID
		this.previousTableId = request.getParameter("previousTableId");
		this.request.setAttribute("previousTableId", previousTableId);
		String _businessId_temp = "";
		// Object reportData = null;
		rptTableInfo = new RptTableInfo();
		try {
			// modify by panshaobo 如果从校验action过来，则新增的数据状态应该是校验已通过
			boolean checkFlag = request.getAttribute("checkFlag") != null;
			int status = DataUtil.WJY_STATUS_NUM;
			if (checkFlag) {
				// 判断是否忽略掉审核及提交操作
				if ("yes".equalsIgnoreCase(this.configOverleapAudit)) {
					status = DataUtil.SHYTG_STATUS_NUM;// 审核通过
				} else if ("yes".equalsIgnoreCase(this.configOverleapCommit)) {
					status = DataUtil.YTJDSH_STATUS_NUM;// 已提交待审核
				} else {
					status = DataUtil.JYYTG_STATUS_NUM;// 校验已通过
				}
			}
			this.fromFlag = request.getParameter("fromFlag");
			request.setAttribute("fromFlag", this.fromFlag);
			this.busiDataType = (String) this.request
					.getParameter("busiDataType");
			this.infoType = (String) this.request.getParameter("infoType");
			this.fileType = request.getParameter("fileType");
			request.setAttribute("fileType", this.fileType);
			// 获取当前用户信息
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if (currentUser != null && !"".equals(currentUser.getId())) {
				userId = currentUser.getId();
			}
			StringBuffer insertColumns = new StringBuffer(
					"instcode,businessid,datastatus,filetype,");
			StringBuffer insertValues = null;
			StringBuffer insertLogColumns = new StringBuffer();
			StringBuffer insertLogValues = new StringBuffer();
			String createdBusinessId = null;
			String createdSubId = null;
			boolean isCreate = false;
			// 根据报表ID获取报表信息
			if ("".equalsIgnoreCase(businessId)) {
				businessId = (String) this
						.getFieldFromSession(ScopeConstants.CURRENT_BUSINESS_ID2);
				isCreate = true;
			}
			// List tables = dataDealService.findRptTableInfo(new RptTableInfo(
			// tableId));
			// rptTableInfo = (RptTableInfo) tables.get(0);
			rptTableInfo = dataDealService.findRptTableInfoById(tableId,
					fileType);
			if (DataUtil.isJCDW(infoTypeCode)) {
				createdBusinessId = createBusinessId();
				insertValues = new StringBuffer("'").append(instCode).append(
						"','").append(createdBusinessId).append("',").append(
						status).append(",'").append(fileType).append("',");
				_businessId_temp = createdBusinessId;
				insertLogColumns
						.append("logtype,tableid,filetype,userid,updatetime,businessid");
				insertLogValues.append("'insert','").append(tableId).append(
						"','").append(this.fileType).append("','").append(
						currentUser.getId()).append("','").append(
						DateUtils.serverCurrentTimeStamp()).append("','")
						.append(createdBusinessId).append("'");
			} else if (DataUtil.isSBHX(infoTypeCode)) {
				createdBusinessId = (String) BeanUtils.getProperty(rptData,
						"businessId");// 继承自基础信息的
				insertValues = new StringBuffer("'").append(instCode).append(
						"','").append(createdBusinessId).append(
						"'," + status + ",");
				_businessId_temp = createdBusinessId;
				insertLogColumns
						.append("logtype,tableid,userid,updatetime,businessid");
				insertLogValues.append("'insert','").append(tableId).append(
						"','").append(currentUser.getId()).append("','")
						.append(DateUtils.serverCurrentTimeStamp()).append(
								"','").append(createdBusinessId).append("'");
			} else {
				createdSubId = createBusinessId();
				insertColumns = new StringBuffer("businessid,subid,");
				insertValues = new StringBuffer("'").append(businessId).append(
						"','").append(createdSubId).append("',");
				_businessId_temp = createdSubId;
				insertLogColumns
						.append("logtype,tableid,userid,updatetime,businessid,subid");
				insertLogValues.append("'insert','").append(tableId).append(
						"','").append(currentUser.getId()).append("','")
						.append(DateUtils.serverCurrentTimeStamp()).append(
								"','").append(businessId).append("','").append(
								createdSubId).append("'");
			}
			insertLogColumns.append(", datastatus");
			insertLogValues.append(", ").append(status);
			boolean businessNoIsRepeat = false;// 签约信息的业务编号是否重复
			// 物理表的列信息
			rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							this.fileType));
			// 字段按order赋别名c1,c2,c3...,并根据字段物理名和别名拼查询SQL
			int cFlag = 0;
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				if ("table".equals(column.getDataType())) {
					continue;
				}
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				// 根据别名获取属性值
				String columnValue = (String) BeanUtils.getProperty(rptData,
						column.getAliasColumnId());
				if ("3".equals(column.getTagType())) {
					if ("TEAMID".equalsIgnoreCase(column.getColumnId())) {
						List bussTypeList = dataDealService
								.findBussTypeList(currentUser.getId());
						this.addFieldToRequest(column.getAliasColumnId()
								+ "_list", bussTypeList);
						if (StringUtil.isEmpty(columnValue)
								&& StringUtil.isNotEmpty(this.teamId)) {
							columnValue = this.teamId;// 用于下游信息保存
						}
					} else {
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
				// 拼插入语句，字段值为空时不添加到插入语句
				if (columnValue == null || "".equals(columnValue.trim())) {
					if ("ACTIONTYPE".equalsIgnoreCase(column.getColumnId())) {
						insertColumns.append(column.getColumnId()).append(",");
						insertValues.append("'',");
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
						} else if ("CHANGE_ID".equalsIgnoreCase(column
								.getColumnId())
								&& "1".equals(this.fileType)) {
							// 变动编号
							createIndexCode = true;
						}
						if (createIndexCode) {
							// String cfaRptNo = (String)
							// this.request.getParameter("cfaRptNo");
							String businessNo = (String) this.request
									.getParameter("businessNo");
							columnValue = dataDealService.findIndexCodeForSelf(
									tableId, fileType, column.getColumnId(),
									null, businessNo);
							insertColumns.append(column.getColumnId()).append(
									",");
							insertValues.append("'").append(columnValue)
									.append("',");
							insertLogColumns.append(",").append(
									column.getLogColumnId());
							insertLogValues.append(",'").append(columnValue)
									.append("'");
						}
					}
					continue;
				}
				if ("n".equalsIgnoreCase(column.getDataType().substring(0, 1))) {
					columnValue = columnValue.replaceAll(",", "");
					insertColumns.append(column.getColumnId()).append(",");
					insertValues.append(columnValue.replaceAll("'", "''"))
							.append(",");
				} else if ("d".equalsIgnoreCase(column.getDataType().substring(
						0, 1))) {
					columnValue = columnValue.replaceAll("-", "");
					insertColumns.append(column.getColumnId()).append(",");
					insertValues.append(columnValue.replaceAll("'", "''"))
							.append(",");
				} else {
					insertColumns.append(column.getColumnId()).append(",");
					insertValues.append("'").append(
							columnValue.replaceAll("'", "''")).append("',");
				}
				if (StringUtil.isNotEmpty(column.getLogColumnId())) {
					insertLogColumns.append(",")
							.append(column.getLogColumnId());
					insertLogValues.append(",'").append(
							columnValue.replaceAll("'", "''")).append("'");
				} else if ("BUSINESSNO".equals(column.getColumnId())) {
					String relatedFileType = (String) configMap
							.get("config.related.filetype");
					if (relatedFileType != null
							&& relatedFileType.indexOf(fileType) < 0) {
						businessNoIsRepeat = this.checkBusinessNoRepeat(
								columnValue, tableId, fileType, null);
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
			// 保存前执行域校验
			if (!checkFlag
					&& (businessNoIsRepeat || !validateData(rptColumnList,
							rptData, true))) {
				this.message = "checkFailure";
				// 在wl8.1下放入request
				if (isCreate) {
					this.businessId = null;
					this.request.setAttribute("businessId", this.businessId);
				}
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
			// 往"t_base_incom"表中插入导入数据的时间 方便查找
			// if (tableId.equals("t_base_income")) {
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				Date date = new Date();
				if ("oracle".equalsIgnoreCase(this.getDbType())) {
					String strDate = DateUtils.toString(date,
							DateUtils.ORA_DATES_FORMAT);
					insertColumns.append("IMPORTDATE,");
					insertValues.append("to_date('" + strDate
							+ "','yyyy-mm-dd'),");
				} else if ("db2".equalsIgnoreCase(this.getDbType())) {
					String strDate = DateUtils.toString(date,
							DateUtils.ORA_DATE_TIMES_FORMAT);
					insertColumns.append("IMPORTDATE,");
					insertValues.append("timestamp('" + strDate + "'),");
				}
			}
			// }
			// 向所有表中插入importdate字段的值
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				insertColumns.append("modifyuser,is_handiwork,");
				insertValues.append("'" + userId + "','Y',");
			}
			// 执行保存操作
			dataDealService.saveRptData(tableId, insertColumns.toString()
					.substring(0, insertColumns.length() - 1), insertValues
					.toString().substring(0, insertValues.length() - 1));
			// 添加报文修改记录
			dataDealService.saveRptData("t_rpt_log_info", insertLogColumns
					.toString(), insertLogValues.toString());
			// 如果是子表，更新其对应的主表状态为1：未校验
			if (!DataUtil.isJCDWSBHX(infoTypeCode)) {
				// RptData rptData = new RptData();
				rptData.setTableId(null);
				rptData.setBusinessId(null);
				rptData.setUpdateSql(null);
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
				// rptData.setUpdateSql(" datastatus = 1, modifyuser = '" +
				// userId+ "' ");
				rptData.setUpdateSql(" datastatus = " + DataUtil.WJY_STATUS_NUM
						+ " ,modifyuser='" + userId + "'");
				dataDealService.updateRptData(rptData);
			}
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				this
						.addFieldToSession(ScopeConstants.CURRENT_TABLE_ID,
								tableId);
			} else if (DataUtil.isInner(infoTypeCode)) {
				this.addFieldToSession(ScopeConstants.CURRENT_TABLE_ID_INNER,
						tableId);
			}
			if (createdBusinessId != null) {
				this.addFieldToSession(ScopeConstants.CURRENT_BUSINESS_ID,
						createdBusinessId);
			} else {
				this.addFieldToSession(ScopeConstants.CURRENT_BUSINESS_ID,
						businessId);
			}
			if (createdSubId != null) {
				this.addFieldToSession(ScopeConstants.CURRENT_SUB_ID,
						createdSubId);
			}
			this.addFieldToSession(ScopeConstants.CURRENT_BUSIDATATYPE,
					busiDataType);
			this.addFieldToSession(ScopeConstants.CURRENT_INFOTYPE, infoType);
			this.addFieldToSession(ScopeConstants.CURRENT_FILETYPE,
					this.fileType);
			String _bussinessId = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_BUSINESS_ID);
			User user = this.getCurrentUser();
			String menuName = "数据录入." + rptTableInfo.getInfoType();
			String style = "单据";
			if (tableIdInner != null) {
				style = "字段科目";
				menuName = "数据录入";
			}
			logManagerService.writeLog(request, user, "0001", menuName, "保存",
					"针对[机构：" + this.instCode + "，" + style + "："
							+ rptTableInfo.getTableName() + "，业务号："
							+ _bussinessId + "]执行数据保存操作", "1");
			if ("yes".equals(this.subReturnCreate) && "".equals(infoTypeCode)) {
				// 当配置了"子表保存后是否回到子表编辑页面"开关，返回到子表的编辑页面
				return "create";
			}
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
			logManagerService.writeLog(request, user, "0001", menuName, "保存",
					"针对[机构：" + this.instCode + "，" + style + "："
							+ rptTableInfo.getTableName() + "，业务号："
							+ _businessId_temp + "]执行数据保存操作", "0");
			e.printStackTrace();
			log.error("SaveDataAction-saveData", e);
			return ERROR;
		}
	}
}
