/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.dataserch.model.QueryCondition;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class DataLowerStatusAction extends DataDealAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 471918397139434183L;
	private String reasionInfo = "";
	private String rejectRptno;
	private List rptColumnListQuery = new ArrayList();
	// private QueryCondition queryCondition; // 查询条件
	private String tableSelectId; // 选中的table
	private String queryConditionResult; // 查询条件的结果
	public static final Map REJECT_RPTNO = new LinkedHashMap();
	static {
		REJECT_RPTNO.put("是", "是");
		REJECT_RPTNO.put("否", "否");
	}

	/**
	 * 数据打回
	 * 
	 * @return
	 */
	public String dataLowerStatus() {
		this.initConfigParameters();
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
			if ("menu".equals(fromFlag)) {
				request.getSession().setAttribute("orderColumn", orderColumn);
				request.getSession().setAttribute("orderDirection",
						orderDirection);
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
				// 第一次进入时，不含有选中id
				if (StringUtils.isEmpty(this.tableUniqueSelectId)) {
					if (this.fileType == null
							|| (!this.fileType.startsWith(this.infoType) && !"ALL"
									.equals(this.infoType))) {
						this.fileType = ((RptTableInfo) rptTableList.get(0))
								.getFileType();
						this.tableId = DataUtil
								.getTableIdByFileType(this.fileType);
					}
					if (this.tableId != null && this.fileType != null) {
						if (!this.tableId.equalsIgnoreCase(DataUtil
								.getTableIdByFileType(this.fileType))) {
							this.tableId = DataUtil
									.getTableIdByFileType(this.fileType);
						}
						this.tableUniqueSelectId = this.tableId + "#"
								+ this.fileType;
					} else {
						tableUniqueSelectId = ((RptTableInfo) rptTableList
								.get(0)).getUniqueTable();
					}
				} else {
					// 选择了其他类型的单据时,还原为第一个
					if (this.tableUniqueSelectId
							.split(DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1]
							.equals("1")) {
						boolean isThisInfo = false;
						for (int i = 0; i < rptTableList.size(); i++) {
							if (((RptTableInfo) rptTableList.get(i))
									.getUniqueTable().equals(
											this.tableUniqueSelectId)) {
								isThisInfo = true;
								break;
							}
						}
						if (!isThisInfo)
							tableUniqueSelectId = ((RptTableInfo) rptTableList
									.get(0)).getUniqueTable();
					} else if (!this.tableUniqueSelectId
							.split(DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1]
							.startsWith(this.infoType)
							&& !"ALL".equals(this.infoType)) {
						tableUniqueSelectId = ((RptTableInfo) rptTableList
								.get(0)).getUniqueTable();
					}
				}
				this.tableId = this.tableUniqueSelectId
						.split(DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[0];
				this.fileType = this.tableUniqueSelectId
						.split(DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1];
			} else {
				RptTableInfo rti = new RptTableInfo();
				rti.setTableId(DataUtil.getTableIdByFileType(this.infoType));
				rti.setFileType("ZZ");
				rti.setTableName("未授权");
				rptTableList.add(rti);
				this.fileType = "ZZ";
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				this.request.setAttribute("tableId", this.tableId);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("orderColumn", this.orderColumn);
				this.request
						.setAttribute("orderDirection", this.orderDirection);
				this.request
						.setAttribute("paginationList", this.paginationList);
				this.request.getSession().setAttribute("paginationList",
						paginationList);
				this.request.setAttribute("configSearchAllOrg",
						this.configSearchAllOrg);
				return INPUT;
			}
			// 打回（更新数据状态为1/5）
			if (request.getParameter("dataLowerStatus") != null) {
				doDataLowerStatus();
				this.queryCondition = (QueryCondition) this.request
						.getSession().getAttribute("queryCondition");
				this.paginationList = (PaginationList) this.request
						.getSession().getAttribute("paginationList");
				if (this.paginationList == null) {
					this.paginationList = new PaginationList();
				}
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
			dataStatusList = commonService
					.getDataStatusListForPageListDatasLowerStatus();
			resetDataStatusList = commonService
					.getResetDataStatusListForPageListLowerStatus();
			// 根据信息类型和是否显示获取报表信息
			// rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
			// DataUtil.getInfoType(infoTypeCode, this.interfaceVer), "1",
			// "1"));
			String columns = getColumnsSql(dataDealService
					.createTablueUniqueId(tableId, fileType));
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
			// 所有状态，相当于选择状态为5/6/7（审核通过/已经报送/已经生成）
			if (dataStatus == null || "".equals(dataStatus)) {
				// dataStatusCondition = " t.datastatus in(5,6) ";
				dataStatusCondition = " t.datastatus in("
						+ DataUtil.SHYTG_STATUS_NUM + ","
						+ DataUtil.YBS_STATUS_NUM + ","
						+ DataUtil.YSC_STATUS_NUM + ") ";
			}
			// 特定状态
			else {
				dataStatusCondition = " t.datastatus in(" + dataStatus + ")";
			}
			// 对于操作类型为“删除”且数据状态为“已报送”的基础、申报、核销信息，都不查询出来
			if (!"1".equals(this.fileType)) {
				dataStatusCondition += " and ((t.actionType = 'D' and t.datastatus != "
						+ DataUtil.YBS_STATUS_NUM
						+ ") or t.actionType != 'D') ";
			}
			// if(("AR".equals(this.fileType) || "AS".equals(this.fileType))
			// && StringUtil.isNotEmpty(columns)){
			// columns += ",(select min(c.filetype) from T_CFA_A_EXDEBT c where
			// (c.exdebtcode = t.exdebtcode or c.businessno = t.businessno) and
			// c.filetype <> '"
			// + this.fileType + "') as cFileType";
			// dataStatusCondition += " and exists (select c.businessid from
			// T_CFA_A_EXDEBT c "
			// + " where (c.exdebtcode = t.exdebtcode or c.businessno =
			// t.businessno) "
			// + " and c.filetype <> '"
			// + this.fileType
			// + "' and c.fileType in (select distinct filetype from
			// t_rela_tables "
			// + " where objtype = 'R' and objId in (select v.role_id from
			// v_role_user v where v.user_id = '"
			// + userId + "')))";
			// }
			/**
			 * 修正分页信息无排序 pagefalg为执行分页查询标识 将orderColumn、orderDirection
			 * 缓存到session中。
			 */
			String pagefalg = (String) request.getParameter("pagefalg");
			if (pagefalg == null) {
				request.getSession().removeAttribute("orderColumn");
				request.getSession().removeAttribute("orderDirection");
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
			if (!check()) {
				// weblogic8.1
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				this.request.setAttribute("tableId", this.tableId);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("orderColumn", this.orderColumn);
				this.request
						.setAttribute("orderDirection", this.orderDirection);
				this.request
						.setAttribute("paginationList", this.paginationList);
				this.request.getSession().setAttribute("paginationList",
						paginationList);
				this.request.setAttribute("configSearchAllOrg",
						this.configSearchAllOrg);
				return INPUT;
			}
			if (StringUtil.isNotEmpty(infoType)) {
				RptData rptData2 = new RptData(tableId, columns, instCode,
						dataStatusCondition, null, orderColumn, orderDirection);
				rptData2.setBeginDate(getBeginDate());
				rptData2.setEndDate(getEndDate());
				rptData2.setFileType(this.fileType);
				if ("1".equals(infoTypeCode)) {
					// 改为查询交易日期
					rptData2.setBuocMonthFrom(rptData2.getBeginDate());
					rptData2.setBuocMonthTo(rptData2.getEndDate());
					// rptData2.setTradeDateFrom(rptData2.getBeginDate());
					// rptData2.setTradeDateTo(rptData2.getEndDate());
					rptData2.setBeginDate(null);
					rptData2.setEndDate(null);
				}
				if ("AR".equals(this.fileType) || "AS".equals(this.fileType)
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
				this.queryConditionResult = createSqlCondition(queryCondition,
						null, null, userId);
				rptData2.setSearchCondition(this.queryConditionResult);
				dataDealService.findRptData(rptData2, paginationList);
			}
			// 将部分字段值用字典表中的对应文字替换
			this.setSelectTagValue(paginationList.getRecordList(), tableId,
					rptColumnList);
			// 在wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			this.request.setAttribute("paginationList", this.paginationList);
			this.request.getSession().setAttribute("paginationList",
					paginationList);
			this.request.getSession().setAttribute("queryCondition",
					queryCondition);
			this.request.setAttribute("dataStatus", this.dataStatus);
			this.request.setAttribute("searchLowerOrg", this.searchLowerOrg);
			this.request.setAttribute("configSearchAllOrg",
					this.configSearchAllOrg);
			this.request.setAttribute("tableUniqueSelectId",
					this.tableUniqueSelectId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataLowerStatusAction-dataLowerStatus", e);
			return ERROR;
		}
	}

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
		return this.dataLowerStatus();
	}

	/**
	 * <p>
	 * 执行打回动作
	 * </p>
	 * <p>
	 * 当下游报表状态为6-已报送时，不对其进行连带带回。2011-4-13 lihaiboA mantis 25668
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void doDataLowerStatus() throws Exception {
		if (businessIds != null) {
			int i = 0;
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			// 根据报表ID获取报表信息
			// List tables = dataDealService.findRptTableInfo(new RptTableInfo(
			// tableId));
			// RptTableInfo _rptTableInfo = (RptTableInfo) tables.get(0);
			RptTableInfo _rptTableInfo = dataDealService.findRptTableInfoById(
					tableId, fileType);
			String menuName = "数据打回." + _rptTableInfo.getInfoType();
			String statusName = commonService.getStatusNameByStatusId(this
					.getResetDataStatus());
			RptData rptDataTemp = null;
			boolean isLinkXyRptData = false;// 是否联动打回下游报文数据
			if ("yes".equalsIgnoreCase(this.configLowerStatusLinkage)) {
				isLinkXyRptData = true;
			}
			try {
				for (; i < businessIds.length; i++) {
					// 更新报表自身状态
					StringBuffer sql = new StringBuffer(" datastatus = "
							+ resetDataStatus + " ");
					if (StringUtil.equalsIgnoreCase(rejectRptno, "否")) {
						sql.append(", RPTNO = '' ");
					}
					if (DataUtil.isJCDW(infoTypeCode)
							&& StringUtil.isNotEmpty(this.reasionInfo)) {
						rptDataTemp = new RptData();
						rptDataTemp.setTableId(_rptTableInfo.getTableId());
						rptDataTemp.setBusinessId(businessIds[i]);
						List list = dataDealService
								.findRptDataReduce(rptDataTemp);
						if (list != null && list.size() == 1) {
							rptDataTemp = (RptData) list.get(0);
						}
						if (String.valueOf(DataUtil.YBS_STATUS_NUM).equals(
								rptDataTemp.getDataStatus())) {
							sql.append(", ACTIONDESC = '").append(
									this.reasionInfo).append("' ");
						}
					}
					// 判断将打回的单据状态是否为已报送
					boolean rptDataYBS = false;
					RptData rdReduce = new RptData(tableId, null,
							businessIds[i], null, null, true, null);
					if (StringUtil.isNotEmpty(this.fileType)) {
						rdReduce.setRptNoColumnId(DataUtil
								.getRptNoColumnIdByFileType(this.fileType));
						rdReduce.setByeRptNoColumnId(DataUtil
								.getByeRptNoColumnIdByFileType(this.fileType));
					}
					List list = dataDealService.findRptDataReduce(rdReduce);
					if (CollectionUtil.isNotEmpty(list)) {
						RptData rd = (RptData) list.get(0);
						if (rd != null) {
							if (String.valueOf(DataUtil.DSC_STATUS_NUM).equals(
									rd.getDataStatus())) {
								// 已进入生成报文环节的单据不可打会
								continue;
							} else if (String.valueOf(DataUtil.YBS_STATUS_NUM)
									.equals(rd.getDataStatus())) {
								// 手工打会已报送状态的记录
								rptDataYBS = true;
								if ("1".equals(resetDataStatus)) {
									// 若该记录的操作类型为A，先将记录的操作类型字段值清空
									rd.setUpdateCondition(" actiontype = 'A' ");
									rd.setUpdateSql(" actiontype = null ");
									rd.setTableId(tableId);
									dataDealService.updateRptData(rd);
								}
							} else if (String.valueOf(DataUtil.YSC_STATUS_NUM)
									.equals(rd.getDataStatus())) {
								// 手工打回已生成状态的记录
								// 根据申报号码，判断是否存在未处理的反馈记录。若存在则修改Has_Reject为1
								String dataNo = null;
								if (StringUtil.isNotEmpty(rd.getRptNo())) {
									dataNo = rd.getRptNo();
								}
								if (StringUtil.isNotEmpty(rd.getByeRptNo())
										&& StringUtil.isNotEmpty(dataNo)) {
									dataNo += ',' + rd.getByeRptNo();
								}
								if (StringUtil.isNotEmpty(dataNo)) {
									this.updateErrorFeedbackHasReject1(dataNo,
											null, null, null);
								}
							}
						}
					}
					RptData rd = new RptData(_rptTableInfo.getTableId(), sql
							.toString(), businessIds[i], null, null, true);
					rd.setReasionInfo(this.reasionInfo);
					if (rptDataYBS) {
						// 对于打回已报送的记录，记录其当前各字段的值，以便将来在接收错误反馈时还原上次正确报送的数据
						this.saveRptDataLog("datalower", _rptTableInfo
								.getTableId(), _rptTableInfo.getFileType(),
								businessIds[i], null, true);
					}
					dataDealService.updateRptDataForLowerStatus(rd);
					if (!isLinkXyRptData) {
						logManagerService.writeLog(request, currentUser,
								"0010.0040", menuName, "打回", "针对[机构："
										+ this.instCode + "，单据："
										+ _rptTableInfo.getTableName()
										+ "，业务号：" + businessIds[i]
										+ "]执行打回操作,目标状态为[" + statusName + "]",
								"1");
						// 配置不执行联动打回，则循环下一条所选单据
						continue;
					}
					String businessNo = null;
					// 对于存在下游报表的，更新所有直接和间接下游报表状态
					String xyFileType = (String) TableIdRela.getZjxyMap().get(
							_rptTableInfo.getFileType());
					if (xyFileType != null) {
						// businessNo = dataDealService
						// .findBusinessNoByBusinessId(_rptTableInfo
						// .getTableId(), _rptTableInfo
						// .getFileType(), businessIds[i]);
					} else {
						// 若是变动类信息，则更新所有变动编号大于当前编号，状态大于0且不是8、99的记录。
						if ("AR,AS,CB,DB,EB,BC,FB,FC".indexOf(_rptTableInfo
								.getFileType()) >= 0) {
							// 由于存在风险，暂不实现
						}
						continue;
					}
					String updateCondition = " filetype <> '"
							+ _rptTableInfo.getFileType() + "' ";
					if (businessNo != null) {
						updateCondition += " and businessNo = '" + businessNo
								+ "' ";
						if (xyFileType.indexOf(",") > 0) {
							String[] xyFileTypes = xyFileType.split(",");
							for (int x = 0; x < xyFileTypes.length; x++) {
								this.updateErrorFeedbackHasReject1(null,
										xyFileTypes[x], tableId, businessNo);
							}
						} else {
							this.updateErrorFeedbackHasReject1(null,
									xyFileType, tableId, businessNo);
						}
					} else {
						continue;
					}
					// 如果是打回成1状态，则所有直接和间接下游报表状态也都得打回成1（同时将iscommit置为0）
					// if ("1".equals(resetDataStatus)) {
					if (String.valueOf(DataUtil.WJY_STATUS_NUM).equals(
							resetDataStatus)) {
						sql = new StringBuffer(" datastatus = "
								+ resetDataStatus + " ");
						RptData upRptData = new RptData(_rptTableInfo
								.getTableId(), sql.toString(), null, null,
								null, true);
						upRptData.setUpdateCondition(updateCondition);
						upRptData.setNotDataStatus(String
								.valueOf(DataUtil.YBS_STATUS_NUM)
								+ ","
								+ String.valueOf(DataUtil.LOCKED_STATUS_NUM)
								+ ","
								+ String.valueOf(DataUtil.DELETE_STATUS_NUM));
						dataDealService.updateRptData(upRptData);
					}
					// 如果是打回成5状态，则所有状态为7的直接和间接下游报表状态也都得打回成5
					// else if ("5".equals(resetDataStatus)) {
					else if (String.valueOf(DataUtil.SHYTG_STATUS_NUM).equals(
							resetDataStatus)) {
						sql = new StringBuffer(" datastatus = "
								+ resetDataStatus + " ");
						RptData upRptData = new RptData(_rptTableInfo
								.getTableId(), sql.toString(), null, null,
								null, true);
						// updateCondition += " and datastatus in (6,7) ";
						updateCondition += " and datastatus in ("
								+ DataUtil.SHYTG_STATUS_NUM + ","
								+ DataUtil.YSC_STATUS_NUM + ") ";
						upRptData.setUpdateCondition(updateCondition);
						upRptData.setNotDataStatus(String
								.valueOf(DataUtil.YBS_STATUS_NUM)
								+ ","
								+ String.valueOf(DataUtil.LOCKED_STATUS_NUM)
								+ ","
								+ String.valueOf(DataUtil.DELETE_STATUS_NUM));
						dataDealService.updateRptData(upRptData);
					}
					// DFHL:增加打回状态校验通过开始
					// 如果是打回成3状态，则所有状态为3以上的直接和间接下游报表状态也都得打回成3
					// else if ("3".equals(resetDataStatus)) {
					else if (String.valueOf(DataUtil.JYYTG_STATUS_NUM).equals(
							resetDataStatus)) {
						sql = new StringBuffer(" datastatus = "
								+ resetDataStatus + " ");
						RptData upRptData = new RptData(_rptTableInfo
								.getTableId(), sql.toString(), null, null,
								null, true);
						// updateCondition += " and datastatus in (4,5,6,7) ";
						updateCondition += " and datastatus in ("
								+ DataUtil.YTJDSH_STATUS_NUM + ","
								+ DataUtil.SHWTG_STATUS_NUM + ","
								+ DataUtil.SHYTG_STATUS_NUM + ","
								+ DataUtil.YSC_STATUS_NUM + ") ";
						upRptData.setUpdateCondition(updateCondition);
						upRptData.setNotDataStatus(String
								.valueOf(DataUtil.YBS_STATUS_NUM)
								+ ","
								+ String.valueOf(DataUtil.LOCKED_STATUS_NUM)
								+ ","
								+ String.valueOf(DataUtil.DELETE_STATUS_NUM));
						dataDealService.updateRptData(upRptData);
					}
					logManagerService.writeLog(request, currentUser,
							"0010.0040", menuName, "打回", "针对[机构："
									+ this.instCode + "，单据："
									+ _rptTableInfo.getTableName() + "，业务号："
									+ businessIds[i] + "]执行打回操作,目标状态为["
									+ statusName + "]", "1");
				}
			} catch (Exception e) {
				logManagerService.writeLog(request, currentUser, "0010.0040",
						menuName, "打回", "针对[机构：" + this.instCode + "，单据："
								+ _rptTableInfo.getTableName() + "，业务号："
								+ businessIds[i] + "]执行打回操作,目标状态为["
								+ statusName + "]", "0");
				log.error("DataLowerStatusAction-doDataLowerStatus", e);
				throw e;
			}
		}
	}

	private void updateErrorFeedbackHasReject1(String dataNo, String filetype,
			String tableId, String businessNo) {
		RptData errorFeedback = new RptData();
		if (StringUtil.isNotEmpty(dataNo)) {
			// 直接根据DATA_NO的值修改反馈记录
			errorFeedback.setUpdateCondition(" DATA_NO = '" + dataNo
					+ "' and HAS_REJECT = '0' ");
		} else if (StringUtil.isNotEmpty(filetype)
				&& StringUtil.isNotEmpty(tableId)
				&& StringUtil.isNotEmpty(businessNo)) {
			// 联动修改下游单据对应反馈记录
			String kyeRptNo = DataUtil.getRptNoColumnIdByFileType(filetype);
			String byeRptNo = DataUtil.getByeRptNoColumnIdByFileType(filetype);
			errorFeedback.setUpdateCondition(" exists (select 1 from "
					+ tableId + " where DATA_NO = " + kyeRptNo + " || ',' || "
					+ byeRptNo + " and businessNo = '" + businessNo
					+ "' and fileType = '" + filetype
					+ "') and HAS_REJECT = '0' ");
		}
		errorFeedback.setUpdateSql(" HAS_REJECT = '1' ");
		errorFeedback.setTableId("T_ERROR_FEEDBACK");
		dataDealService.updateRptData(errorFeedback);
	}

	public String getRejectRptno() {
		return rejectRptno;
	}

	public void setRejectRptno(String rejectRptno) {
		this.rejectRptno = rejectRptno;
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
		StringBuffer sb = new StringBuffer();
		String infoType = this.request.getParameter("infoType");
		if (StringUtil.isNotEmpty(infoType) && infoType.length() > 0) {
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if (currentUser != null) {
				userId = currentUser.getId();
			}
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(null, "1", "1"));
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
			String busiDataType = this.getBusiDataType(this.infoType,
					busiDataInfoList);
			if (rptTableList != null && rptTableList.size() > 0) {
				if ("2".equals(busiDataType)) {
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
				} else {
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
							}
						}
					}
				}
			}
		} else {
			if (StringUtil.isNotEmpty(this.tableUniqueSelectId)
					&& this.tableUniqueSelectId.indexOf("#") > 0) {
				fileType = this.tableUniqueSelectId.split("#")[1];
				tableSelectId = this.tableUniqueSelectId.split("#")[0];
			}
		}
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
	// public QueryCondition getQueryCondition(){
	// return queryCondition;
	// }
	// public void setQueryCondition(QueryCondition queryCondition){
	// this.queryCondition = queryCondition;
	// }
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
		// DataLowerStatusAction.operatorsMap = operatorsMap;
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
