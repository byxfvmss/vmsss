/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.dataserch.model.QueryCondition;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class DataAuditAction extends DataDealAction {

	/**
	 * 
	 */
	private String reasionInfo = "";
	private static final long serialVersionUID = 3710373059964741845L;
	private List rptColumnListQuery = new ArrayList();
	// private QueryCondition queryCondition; // 查询条件
	private String tableSelectId; // 选中的table
	private String queryConditionResult; // 查询条件的结果
	// 是否只能审核别人最后修改的报表
	private String checkOthers;

	public String getCheckOthers() {
		if (configMap != null) {
			String confCheckOthers = (String) configMap.get("checkOthers");
			if (StringUtils.isNotEmpty(confCheckOthers)) {
				this.checkOthers = confCheckOthers;
			}
		}
		return checkOthers;
	}

	public void setCheckOthers(String checkOthers) {
		this.checkOthers = checkOthers;
	}

	/**
	 * 数据审核
	 * 
	 * @return
	 */
	public String dataAudit() {
		if (!sessionInit(true)) {
			return SUCCESS;
		}
		try {
			// 获取当前用户信息
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if (currentUser != null) {
				userId = currentUser.getId();
			}
			if ("yes".equalsIgnoreCase(this.configDeleteNeedAudit)) {
				this.request.setAttribute("configDeleteNeedAudit", "yes");
				dataStatusList = commonService
						.getDataStatusListForPageListDatasAudit();
			}
			//
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(null, "1", "1"));
			// 
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
			this.busiDataType = this.getBusiDataType(this.infoType,
					busiDataInfoList);
			if (rptTableList != null && rptTableList.size() > 0) {
				String fromFlag = this.request.getParameter("fromFlag");
				if (!"uprr".equals(fromFlag)) {
					if (this.fileType == null
							|| (!this.fileType.startsWith(this.infoType) && !"ALL"
									.equals(this.infoType))) {
						this.tableSelectId = ((RptTableInfo) rptTableList
								.get(0)).getTableId();
						this.fileType = ((RptTableInfo) rptTableList.get(0))
								.getFileType();
					} else {
						for (int i = 0; i < rptTableList.size(); i++) {
							if (((RptTableInfo) rptTableList.get(i))
									.getFileType().equals(this.fileType)) {
								this.tableSelectId = ((RptTableInfo) rptTableList
										.get(i)).getTableId();
								break;
							}
						}
					}
					this.tableId = this.tableSelectId;
					if ("menu".equals(fromFlag)) {
						request.getSession().setAttribute("orderColumn",
								orderColumn);
						request.getSession().setAttribute("orderDirection",
								orderDirection);
					}
				} else {
					// 从FMSS首页"需处理任务"处进入
					this.tableSelectId = this.tableId;
					if (this.authInstList != null && currentUser != null) {
						String modifyUser = "";
						String noModifyUser = "";
						if ("yes".equals(this.getCheckOthers())) {
							noModifyUser = currentUser.getId();
						} else {
							modifyUser = currentUser.getId();
						}
						for (int i = 0; i < this.authInstList.size(); i++) {
							Organization org = (Organization) this.authInstList
									.get(i);
							List unsettledRptList = dataDealService
									.findUnsettledReport(
											this.tableId,
											null,
											null,
											String
													.valueOf(DataUtil.YTJDSH_STATUS_NUM),
											org.getId(), modifyUser,
											noModifyUser, null);
							if (unsettledRptList != null
									&& unsettledRptList.size() > 0) {
								RptData rd = (RptData) unsettledRptList.get(0);
								this.fileType = rd.getFileType();
								this.instCode = org.getId();
								break;
							}
						}
					}
					if (this.fileType != null
							&& this.tableId != null
							&& !this.tableId.equalsIgnoreCase(DataUtil
									.getTableIdByFileType(this.fileType))) {
						this.fileType = null;
					}
				}
			} else {
				RptTableInfo rti = new RptTableInfo();
				rti.setTableId(DataUtil.getTableIdByFileType(this.infoType));
				rti.setFileType("ZZ");
				rti.setTableName("未授权");
				rptTableList.add(rti);
				this.fileType = "ZZ";
				// weblogic8.1
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				this.request.setAttribute("tableId", this.tableId);
				this.request.setAttribute("infoType", this.infoType);
				this.request.setAttribute("fileType", this.fileType);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("orderColumn", this.orderColumn);
				this.request
						.setAttribute("orderDirection", this.orderDirection);
				this.request
						.setAttribute("paginationList", this.paginationList);
				this.request.setAttribute("configSearchAllOrg",
						this.configSearchAllOrg);
				return INPUT;
			}
			// 审核（更新数据状态为4/5）
			if (request.getParameter("dataAudit") != null) {
				doDataAudit();
				this.queryCondition = (QueryCondition) this.request
						.getSession().getAttribute("queryCondition");
				this.paginationList = (PaginationList) this.request
						.getSession().getAttribute("paginationList");
				if (this.paginationList == null) {
					this.paginationList = new PaginationList();
				}
			}
			if (orderColumn == null && orderDirection == null) {
				orderColumn = (String) request.getSession().getAttribute(
						"orderColumn");
				orderDirection = (String) request.getSession().getAttribute(
						"orderDirection");
			} else {
				request.getSession().setAttribute("orderColumn", orderColumn);
				request.getSession().setAttribute("orderDirection",
						orderDirection);
			}
			// this.tableSelectId = tableId;
			if ("2".equalsIgnoreCase(this.busiDataType)) {
				rptColumnList = dataDealService
						.findRptColumnInfo(new RptColumnInfo(tableId, null,
								"1", null));
			} else {
				rptColumnList = dataDealService
						.findRptColumnInfo(new RptColumnInfo(tableId, null,
								"1", this.fileType));
			}
			rptColumnListQuery.clear();
			if (CollectionUtil.isNotEmpty(rptColumnList)) {
				for (int i = 0; i < rptColumnList.size(); i++) {
					RptColumnInfo rpt = new RptColumnInfo();
					RptColumnInfo rci = (RptColumnInfo) rptColumnList.get(i);
					if (!"table".equals(rci.getDataType())) {
						org.springframework.beans.BeanUtils.copyProperties(rci,
								rpt);
						rptColumnListQuery.add(rpt);
					}
				}
				if ("AR".equalsIgnoreCase(this.fileType)
						|| "AS".equalsIgnoreCase(this.fileType)) {
					RptColumnInfo rci = new RptColumnInfo();
					rci.setColumnId(CONTRACTTYPE);
					rci.setColumnName("签约类型");
					rptColumnListQuery.add(rci);
				}
			}
			resetDataStatusList = commonService
					.getResetDataStatusListForPageListDatasAudit();
			// 根据信息类型和是否显示获取报表信息
			// rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
			// DataUtil.getInfoType(infoTypeCode, this.interfaceVer), "1",
			// "1"));
			// 根据表名查找列信息，用于打印报表表头
			rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, "1", "1",
							this.fileType));
			String columns = this.getColumnsSql(dataDealService
					.createTablueUniqueId(this.tableId, this.fileType));
			if (columns == null) {
				columns = " '' ";
			} else {
				if (orderColumn != null
						&& orderColumn.indexOf("datastatus") < 0
						&& columns.indexOf(orderColumn) < 0) {
					orderColumn = null;
					orderDirection = null;
					request.getSession().setAttribute("orderColumn", null);
					request.getSession().setAttribute("orderDirection", null);
				}
			}
			String dataStatusCondition = null;
			if (!check()) {
				// weblogic8.1
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				this.request.setAttribute("tableId", this.tableId);
				this.request.setAttribute("infoType", this.infoType);
				this.request.setAttribute("fileType", this.fileType);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("orderColumn", this.orderColumn);
				this.request
						.setAttribute("orderDirection", this.orderDirection);
				this.request
						.setAttribute("paginationList", this.paginationList);
				this.request.setAttribute("configSearchAllOrg",
						this.configSearchAllOrg);
				return INPUT;
			}
			// 判断删除操作是否需要审核
			if ("yes".equalsIgnoreCase(this.configDeleteNeedAudit)) {
				if (StringUtils.isEmpty(this.dataStatus)) {
					dataStatusCondition = " (t.datastatus = "
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " or t.datastatus < "
							+ DataUtil.DELETE_STATUS_NUM + ") ";
				} else if (String.valueOf(DataUtil.DELETE_STATUS_NUM)
						.equalsIgnoreCase(this.dataStatus)) {
					dataStatusCondition = " t.datastatus < "
							+ DataUtil.DELETE_STATUS_NUM + " ";
				} else if (String.valueOf(DataUtil.YTJDSH_STATUS_NUM)
						.equalsIgnoreCase(this.dataStatus)) {
					dataStatusCondition = " t.datastatus = "
							+ DataUtil.YTJDSH_STATUS_NUM + " ";
				}
			} else {
				// 只显示状态为4（已提交待审核）
				dataStatusCondition = " t.datastatus = "
						+ DataUtil.YTJDSH_STATUS_NUM + " ";
			}
			if (!"2".equalsIgnoreCase(this.busiDataType)) {
				dataStatusCondition += " and t.fileType = '" + this.fileType
						+ "' ";
			}
			RptData rptData = new RptData(tableId, columns, instCode,
					dataStatusCondition, null, orderColumn, orderDirection);
			if ("yes".equals(this.getCheckOthers()) && currentUser != null) {
				rptData.setModifyUser(currentUser.getId());
			}
			rptData.setFileType(this.fileType);
			if ("AR".equals(this.fileType) || "AS".equals(this.fileType)
					|| "CB".equals(this.fileType) || "DB".equals(this.fileType)
					|| "EB".equals(this.fileType) || "FB".equals(this.fileType)
					|| "FC".equals(this.fileType)) {
				// 补充涉及签约信息部分字段的查询
				// this.setSqlAboutContractInfo(rptData, userId);
			}
			if ("on".equals(searchLowerOrg)) {
				rptData.setSearchLowerOrg(searchLowerOrg);
				rptData.setUserId(userId);
			}
			if ("yes".equalsIgnoreCase(super.linkBussType)) {
				rptData.setLinkBussType(super.linkBussType);
				rptData.setUserId(userId);
			}
			this.queryConditionResult = createSqlCondition(queryCondition,
					null, null, userId);
			rptData.setSearchCondition(this.queryConditionResult);
			dataDealService.findRptData(rptData, paginationList);
			// 将部分字段值用字典表中的对应文字替换
			this.setSelectTagValue(paginationList.getRecordList(), tableId,
					rptColumnList);
			// 在wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("infoType", this.infoType);
			this.request.setAttribute("fileType", this.fileType);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			this.request.setAttribute("paginationList", this.paginationList);
			this.request.getSession().setAttribute("paginationList",
					paginationList);
			this.request.getSession().setAttribute("queryCondition",
					queryCondition);
			this.request.setAttribute("configSearchAllOrg",
					this.configSearchAllOrg);
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				this
						.addFieldToSession(ScopeConstants.CURRENT_TABLE_ID,
								tableId);
			}
			this.request.setAttribute("searchLowerOrg", this.searchLowerOrg);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataAuditAction-dataAudit", e);
			return ERROR;
		}
	}

	/**
	 * 执行审核动作
	 * 
	 * @throws Exception
	 */
	private void doDataAudit() throws Exception {
		if (businessIds != null) {
			String auditDate = DateUtils.serverCurrentDateTime();
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			// 根据报表ID获取报表信息
			// List tables = dataDealService.findRptTableInfo(new RptTableInfo(
			// tableId));
			// RptTableInfo _rptTableInfo = (RptTableInfo) tables.get(0);
			if (!"2".equals(this.busiDataType)) {
			}
			RptTableInfo _rptTableInfo = dataDealService.findRptTableInfoById(
					tableId, fileType);
			boolean isContractInfo = false;// 当前审核记录是否为银行自身外债签约信息
			if (fileType != null) {
				if (TableIdRela.getZjxyMap().get(fileType) != null) {
					isContractInfo = true;
				}
			}
			String menuName = "信息审核." + _rptTableInfo.getInfoType();
			String statusName = commonService.getStatusNameByStatusId(this
					.getResetDataStatus());
			int i = 0;
			try {
				for (; i < businessIds.length; i++) {
					String busiType = null;
					if (Integer.valueOf(resetDataStatus).intValue() == DataUtil.SHWTG_STATUS_NUM) {
						busiType = "1";
					}
					String aimDataStatus = resetDataStatus;
					if ("yes".equalsIgnoreCase(this.configDeleteNeedAudit)) {
						// 当配置需要对删除操作进行审核时
						RptData rd = new RptData();
						rd.setTableId(tableId);
						rd.setBusinessId(businessIds[i]);
						// 查询待审核记录
						List listReduce = dataDealService.findRptDataReduce(rd);
						if (listReduce != null && listReduce.size() == 1) {
							rd = (RptData) listReduce.get(0);
							if (Integer.valueOf(rd.getDataStatus()).intValue() < 0) {
								// 待审核记录状态小于0，说明其为删除待审核的记录
								if (String.valueOf(DataUtil.SHWTG_STATUS_NUM)
										.equals(resetDataStatus)) {
									// 删除审核不通过，重置为原状态
									aimDataStatus = " 0 - datastatus ";
								} else {
									// 删除审核通过
									aimDataStatus = String
											.valueOf(DataUtil.DELETE_STATUS_NUM);
									saveDeleteLog(_rptTableInfo.getTableId(),
											_rptTableInfo.getFileType(),
											businessIds[i], null, true);
									String xyFileType = (String) TableIdRela
											.getZjxyMap()
											.get(_rptTableInfo.getFileType());
									if (xyFileType != null) {
										String updateSql = " datastatus = "
												+ DataUtil.DELETE_STATUS_NUM
												+ ", modifyUser = '"
												+ currentUser.getId() + "' ";
										deleteXyRptData(_rptTableInfo
												.getTableId(), _rptTableInfo
												.getFileType(), businessIds[i],
												xyFileType, updateSql);
									}
								}
							}
						}
					}
					StringBuffer sbUpdate = new StringBuffer();
					if ("oracle".equalsIgnoreCase(this.getDbType())) {
						sbUpdate
								.append(" datastatus = ")
								.append(aimDataStatus)
								.append(", auditdate = to_timestamp('")
								.append(auditDate)
								.append(
										"','yyyy-mm-dd hh24:mi:ssxff') , auditname = '")
								.append(currentUser.getName()).append("' ");
					} else if ("db2".equalsIgnoreCase(this.getDbType())) {
						sbUpdate.append(" datastatus = ").append(aimDataStatus)
								.append(", auditdate = timestamp('").append(
										auditDate).append("') , auditname = '")
								.append(currentUser.getName()).append("' ");
					}
					RptData updateRd = new RptData(_rptTableInfo.getTableId(),
							sbUpdate.toString(), businessIds[i], null, null,
							true, reasionInfo);
					// 对进行审核前的记录状态做限制，以防多用户同时审核同笔记录时后审核结果覆盖先审核结果
					StringBuffer updateCondition = new StringBuffer();
					updateCondition.append(" (datastatus = ").append(
							DataUtil.YTJDSH_STATUS_NUM).append(
							" or datastatus < 0) ");
					updateRd.setUpdateCondition(updateCondition.toString());
					dataDealService.updateRptData2(updateRd, busiType);
					if (isContractInfo) {
						// RptData rd = new RptData();
						// rd.setTableId(_rptTableInfo.getTableId());
						// rd.setUpdateSql(" businessno = businessid ");
						// rd
						// .setUpdateCondition(" businessid = '"
						// + businessIds[i]
						// + "' and (businessno is null or businessno = '')");
						// dataDealService.updateRptData(rd);
					}
					logManagerService.writeLog(request, currentUser, "审核",
							menuName, "审核", "针对[机构：" + this.instCode + "，单据："
									+ _rptTableInfo.getTableName() + "，业务号："
									+ businessIds[i] + "]执行审核操作,目标状态为["
									+ statusName + "]", "1");
				}
				request.setAttribute("overFlag", "true");
			} catch (Exception e) {
				logManagerService.writeLog(request, currentUser, "审核",
						menuName, "审核", "针对[机构：" + this.instCode + "，单据："
								+ _rptTableInfo.getTableName() + "，业务号："
								+ businessIds[i] + "]执行审核操作,目标状态为["
								+ statusName + "]", "0");
				throw e;
			}
		}
	}

	public String getReasionInfo() {
		return reasionInfo;
	}

	public void setReasionInfo(String reasionInfo) {
		this.reasionInfo = reasionInfo;
	}

	/**
	 * 异步获得列名和列ID
	 * 
	 * @return
	 */
	public String asyLoadColumnName() {
		// 获取当前用户信息
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		String userId = "";
		if (currentUser != null) {
			userId = currentUser.getId();
		}
		if ("yes".equalsIgnoreCase(this.configDeleteNeedAudit)) {
			this.request.setAttribute("configDeleteNeedAudit", "yes");
			dataStatusList = commonService
					.getDataStatusListForPageListDatasAudit();
		}
		//
		busiDataInfoList = dataDealService
				.findRptBusiDataInfo(new RptBusiDataInfo(null, "1", "1"));
		// 
		rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(this
				.getInfoTypeName(this.infoType, busiDataInfoList), "1", "1"),
				userId);
		this.busiDataType = this.getBusiDataType(this.infoType,
				busiDataInfoList);
		if (rptTableList != null && rptTableList.size() > 0) {
			if ("2".equals(this.busiDataType)) {
				// 代客业务
				if (this.tableSelectId == null || this.tableId == null
						|| !this.tableId.equalsIgnoreCase(this.tableSelectId)) {
					for (int i = 0; i < rptTableList.size(); i++) {
						((RptTableInfo) rptTableList.get(i)).setFileType(String
								.valueOf(i + 1));
						if (((RptTableInfo) rptTableList.get(i)).getFileType()
								.equals(this.fileType)) {
							this.tableSelectId = ((RptTableInfo) rptTableList
									.get(i)).getTableId();
						}
					}
					if (this.tableSelectId == null) {
						this.tableSelectId = ((RptTableInfo) rptTableList
								.get(0)).getTableId();
						this.fileType = "1";
					}
					this.tableId = this.tableSelectId;
				}
			} else {
				// 自身业务
				String fromFlag = this.request.getParameter("fromFlag");
				if (!"uprr".equals(fromFlag)) {
					if (this.fileType == null
							|| !this.fileType.startsWith(this.infoType)) {
						this.tableSelectId = ((RptTableInfo) rptTableList
								.get(0)).getTableId();
						this.fileType = ((RptTableInfo) rptTableList.get(0))
								.getFileType();
					} else {
						for (int i = 0; i < rptTableList.size(); i++) {
							if (((RptTableInfo) rptTableList.get(i))
									.getFileType().equals(this.fileType)) {
								this.tableSelectId = ((RptTableInfo) rptTableList
										.get(i)).getTableId();
								break;
							}
						}
					}
					this.tableId = this.tableSelectId;
				} else {
					// 从FMSS首页"需处理任务"处进入
					this.tableSelectId = this.tableId;
					if (this.authInstList != null && currentUser != null) {
						String modifyUser = "";
						String noModifyUser = "";
						if ("yes".equals(this.getCheckOthers())) {
							noModifyUser = currentUser.getId();
						} else {
							modifyUser = currentUser.getId();
						}
						for (int i = 0; i < this.authInstList.size(); i++) {
							Organization org = (Organization) this.authInstList
									.get(i);
							List unsettledRptList = dataDealService
									.findUnsettledReport(
											this.tableId,
											null,
											null,
											String
													.valueOf(DataUtil.YTJDSH_STATUS_NUM),
											org.getId(), modifyUser,
											noModifyUser, null);
							if (unsettledRptList != null
									&& unsettledRptList.size() > 0) {
								RptData rd = (RptData) unsettledRptList.get(0);
								this.fileType = rd.getFileType();
								this.instCode = org.getId();
								break;
							}
						}
					}
					if (this.fileType != null
							&& this.tableId != null
							&& !this.tableId.equalsIgnoreCase(DataUtil
									.getTableIdByFileType(this.fileType))) {
						this.fileType = null;
					}
				}
			}
		}
		// //////////////////////////
		StringBuffer sb = new StringBuffer();
		List columnInfos = dataDealService.findRptColumnInfo(new RptColumnInfo(
				tableSelectId, null, "1", this.fileType));
		for (int i = 0; i < columnInfos.size(); i++) {
			RptColumnInfo tmpColumnInfo = (RptColumnInfo) columnInfos.get(i);
			sb.append(tmpColumnInfo.getColumnId().trim());
			sb.append(",");
			sb.append(tmpColumnInfo.getColumnName().trim());
			sb.append(",");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		String columnNames = sb.toString();
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			System.out.println("columnName:" + columnNames);
			this.response.getWriter().print(columnNames);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// TODO:(fwy)高级查询时，下拉列表不动的解决方法
		// return SUCCESS;
		return null;
	}

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
	public ColumnInfo getColumnInfo(String tableId, String columnId) {
		ColumnInfo columnInfo = new ColumnInfo();
		columnInfo.setColumnId(columnId);
		columnInfo.setTableId(tableId);
		return userInterfaceConfigService.getColumnInfo(columnInfo);
	}

	/*
	 * public String getColumnType(String tableId, String columnId){ ColumnInfo
	 * ci = getColumnInfo(tableId, columnId); return getColumnType(ci); }
	 */
	/**
	 * 验证单个查询条件是否合法
	 * 
	 * @param columnId
	 * @param op
	 * @return private boolean checkOneValue(String columnId, int op, String
	 *         beginError){ boolean flag = true; String error = ""; ColumnInfo
	 *         ciF = getColumnInfo(this.tableSelectId, columnId); String type =
	 *         getColumnType(ciF); if(type.equals(TYPE_NUM)){ if(op ==
	 *         TYPE_LIKE){ error = error + beginError + "\"" +
	 *         ciF.getColumnName() + "\"" + "为数字型,不可以选择" + operatorsMap.get(new
	 *         Integer(TYPE_LIKE)) + ";"; flag = false; } }else
	 *         if(type.equals(TYPE_STRING)){ // if ((op == TYPE_MORE) || (op ==
	 *         TYPE_LESS) || (op == // TYPE_MORE_EQUAL) // || (op ==
	 *         TYPE_LESS_EQUAL)) { // error = error+beginError+"\""+
	 *         ciF.getColumnName()+"\"" + // "为文本型,不可以选择" // +
	 *         operatorsMap.get(new Integer(op)) + ";"; // flag = false; // }
	 *         }else if(type.equals(TYPE_DATE)){ } if(!(error.equals(""))){
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
	/**
	 * 判断该列是否是字典项 true:表示字典项 false:不是字典项
	 * 
	 * @return private boolean checkDic(String columnId){ ColumnInfo columnInfo =
	 *         getColumnInfo(tableSelectId, columnId);
	 *         if((columnInfo.getTagType() != null) &&
	 *         (columnInfo.getTagType().length() > 0)){
	 *         if(columnInfo.getTagType().equals("3")){ return true; } } return
	 *         false; }
	 */
	/**
	 * 非字典项的SQL
	 * 
	 * @param columId
	 * @param op
	 * @param values
	 * @return private String getNoDicSql(String columId, int op, String
	 *         values){ StringBuffer sb = new StringBuffer(); String typeF =
	 *         getColumnType(tableSelectId, columId);
	 *         if(typeF.equals(TYPE_STRING) || "TRADEDATE".equals(columId) ||
	 *         "RPTDATE".equals(columId) || "IMPDATE".equals(columId)){
	 *         sb.append(" and t."); sb.append(columId + " ");
	 *         sb.append(operatorsMap.get(new Integer(op))); if(op ==
	 *         TYPE_LIKE){ sb.append(" '%" + values + "%'"); }else{ sb.append(" '" +
	 *         values + "'"); } }else if(typeF.equals(TYPE_DATE)){
	 *         if(this.getDbType().equals("db2")){ sb.append(" and
	 *         SUBSTR(CHAR(t.").append(columId).append( "),1,10) ");
	 *         sb.append(operatorsMap.get(new Integer(op))); String datetemp =
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
	/**
	 * 字典项的SQL
	 * 
	 * @param columId
	 * @param op
	 * @param values
	 * @return private String getDicSql(String columId, int op, String values){
	 *         ColumnInfo columnInfo = getColumnInfo(tableSelectId, columId);
	 *         StringBuffer sb = new StringBuffer(); sb.append(" and t.");
	 *         sb.append(columId + " in ("); sb .append(" select
	 *         code_value_standard_num from t_code_dictionary where code_name
	 *         "); sb.append(operatorsMap.get(new Integer(op))); if(op ==
	 *         TYPE_LIKE){ sb.append(" '%" + values + "%'"); }else{ sb.append(" '" +
	 *         values + "'"); } sb.append(" and code_type='" +
	 *         columnInfo.getDictionaryTypeId() + "')"); return sb.toString(); }
	 */
	/*
	 * private String createSqlCondition(QueryCondition qc, String bDate, String
	 * eDate, String userId){ String returnStr = ""; StringBuffer sb = new
	 * StringBuffer(); if(qc != null){ // -----去除多余空格-----
	 * qc.setValueFirst(qc.getValueFirst().trim());
	 * if(!StringUtil.isEmpty(qc.getValueFirst())){
	 * if(!CONTRACTTYPE.equals(qc.getColumnIdFirst())){ // 先判断该列是否是字典项
	 * if(checkDic(qc.getColumnIdFirst())){
	 * sb.append(getDicSql(qc.getColumnIdFirst(), qc .getOpFirst(),
	 * qc.getValueFirst())); }else{ sb.append(getNoDicSql(qc.getColumnIdFirst(),
	 * qc .getOpFirst(), qc.getValueFirst())); } }else{
	 * sb.append(getContractTypeSQL((String) operatorsMap .get(new
	 * Integer(qc.getOpFirst())), qc .getValueFirst())); } } } // 增加所有选项
	 * if(StringUtil.isEmpty(instCode)){// 所有 if((authInstList != null) &&
	 * (authInstList.size() > 0)){ sb .append( " and t.instCode in (select
	 * fk_orgId from t_user_org where fk_userId = '") .append(userId).append("')
	 * "); }else{ // 没有有权限的机构 sb.append(" and 1 = 0 "); } } if(sb.length() > 3){
	 * returnStr = sb.toString().replaceFirst("and", ""); } return returnStr; }
	 */
	/**
	 * 返回
	 * 
	 * @return
	 */
	public String goBack() {
		this.paginationList = (PaginationList) request.getSession()
				.getAttribute("paginationList");
		if (orderColumn == null && orderDirection == null) {
			orderColumn = (String) request.getSession().getAttribute(
					"orderColumn");
			orderDirection = (String) request.getSession().getAttribute(
					"orderDirection");
		} else {
			request.getSession().setAttribute("orderColumn", orderColumn);
			request.getSession().setAttribute("orderDirection", orderDirection);
		}
		this.queryCondition = (QueryCondition) this.request.getSession()
				.getAttribute("queryCondition");
		return this.dataAudit();
	}

	public String getTableSelectId() {
		return tableSelectId;
	}

	public void setTableSelectId(String tableSelectId) {
		this.tableSelectId = tableSelectId;
	}

	public Map getOperatorsMap() {
		return operatorsMap;
	}

	public void setOperatorsMap(Map operatorsMap) {
		// DataAuditAction.operatorsMap = operatorsMap;
	}

	public String getQueryConditionResult() {
		return queryConditionResult;
	}

	public void setQueryConditionResult(String queryConditionResult) {
		this.queryConditionResult = queryConditionResult;
	}

	public List getRptColumnListQuery() {
		return rptColumnListQuery;
	}

	public void setRptColumnListQuery(List rptColumnListQuery) {
		this.rptColumnListQuery = rptColumnListQuery;
	}
}