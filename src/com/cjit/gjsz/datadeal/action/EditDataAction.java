/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.model.TableDataVO;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.core.ReceiveReport;
import com.cjit.gjsz.filem.service.ReceiveReportService;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.User;
import com.opensymphony.util.BeanUtils;

/**
 * @author yulubin
 */
public class EditDataAction extends DataDealAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5490767028994400781L;
	private String errorId;

	/**
	 * 外部表编辑数据的入口
	 * 
	 * @return
	 */
	public String editData() {
		if (!sessionInit(true)) {
			return SUCCESS;
		}
		this.request.setAttribute("configForbidSave", this.configForbidSave);
		this.request.setAttribute("configOverleapCommit",
				this.configOverleapCommit);
		// 从反馈接收页面进入编辑页面逻辑 Begin
		if (StringUtil.isNotEmpty(errorId)) {
			ReceiveReportService receiveReportService = (ReceiveReportService) SpringContextUtil
					.getBean("receiveReportService");
			ReceiveReport receiveReport = new ReceiveReport();
			receiveReport.setId(Integer.valueOf(errorId).intValue());
			receiveReport = receiveReportService
					.getReceiveReport(receiveReport);
			if (receiveReport != null
					&& StringUtil.isNotEmpty(receiveReport.getDataNumber())
					&& StringUtil
							.isNotEmpty(receiveReport.getRptSendFileName())) {
				// 解析获取报送文件类型
				this.fileType = receiveReport.getRptSendFileName().substring(3,
						5);
				this.tableId = DataUtil.getTableIdByFileType(fileType);
				// 反馈记录中的业务数据主键
				String dataNo = receiveReport.getDataNumber();
				// 主申报号
				String keyRptNo = null;
				// 副申报号
				String byeRptNo = null;
				// 主申报号对应字段名称
				String keyRptNoColumnId = null;
				// 副申报号对应字段名称
				String byeRptNoColumnId = null;
				if (dataNo.length() >= 28) {
					// 主申报号
					keyRptNo = DataUtil.getKeyRptNoForBussNo(dataNo);
					// 副申报号
					byeRptNo = DataUtil.getByeRptNoForBussNo(dataNo);
					// 主申报号对应字段名称
					keyRptNoColumnId = DataUtil
							.getRptNoColumnIdByFileType(fileType);
					// 副申报号对应字段名称
					byeRptNoColumnId = DataUtil
							.getByeRptNoColumnIdByFileType(fileType);
				} else if (dataNo.trim().length() == 19
						&& dataNo.indexOf(",") > 0) {
					// BRANCHCODE
					keyRptNo = dataNo.split(",")[0];
					// BUOCMONTH
					byeRptNo = dataNo.split(",")[1];
					// 主申报号对应字段名称
					keyRptNoColumnId = "BRANCHCODE";
					// 副申报号对应字段名称
					byeRptNoColumnId = "BUOCMONTH";
				}
				RptData rd = new RptData();
				rd.setTableId(tableId);
				rd.setRptNoColumnId(keyRptNoColumnId);
				rd.setRptNo(keyRptNo);
				rd.setByeRptNoColumnId(byeRptNoColumnId);
				rd.setByeRptNo(byeRptNo);
				rd.setFileType(fileType);
				List reduceList = dataDealService.findRptDataReduce(rd);
				if (CollectionUtil.isNotEmpty(reduceList)) {
					rd = (RptData) reduceList.get(0);
					if (rd != null && StringUtil.isNotEmpty(rd.getBusinessId())) {
						this.businessId = rd.getBusinessId();
						// 更新报表自身状态 并记录打回
						StringBuffer sql = new StringBuffer(" datastatus = "
								+ DataUtil.WJY_STATUS_NUM + " ");
						dataDealService
								.updateRptDataForLowerStatus(new RptData(
										tableId, sql.toString(),
										this.businessId, null, null, true,
										"反馈接收 编辑修改"));
						if (StringUtil.isNotEmpty(receiveReport.getErrorMemo())) {
							dataDealService.updateRptData2(new RptData(tableId,
									null, this.businessId, null, null, true,
									receiveReport.getErrorMemo()), "3");
						}
						// 修改反馈信息,标记为已打回
						receiveReport.setHasReject("1");
						receiveReportService.updateReceiveReport(receiveReport);
						// 是否联动打回下游报文数据
						String xyFileType = (String) TableIdRela.getZjxyMap()
								.get(this.fileType);
						if ("yes"
								.equalsIgnoreCase(this.configLowerStatusLinkage)
								&& xyFileType != null) {
							String businessNo = dataDealService
									.findBusinessNoByBusinessId(tableId,
											this.fileType, this.businessId);
							if (businessNo != null) {
								String updateCondition = " filetype <> '"
										+ this.fileType
										+ "' and businessNo = '" + businessNo
										+ "' ";
								RptData upRptData = new RptData(tableId, sql
										.toString(), null, null, null, true);
								upRptData.setUpdateCondition(updateCondition);
								upRptData
										.setNotDataStatus(String
												.valueOf(DataUtil.YBS_STATUS_NUM)
												+ ","
												+ String
														.valueOf(DataUtil.LOCKED_STATUS_NUM)
												+ ","
												+ String
														.valueOf(DataUtil.DELETE_STATUS_NUM));
								dataDealService.updateRptData(upRptData);
							}
						}
					}
				}
			}
		}
		// 从反馈接收页面进入编辑页面逻辑 End
		// 设置审核不通过理由 or 设置数据打回原因
		setCheckResion();
		if (DataUtil.isJCDWSBHX(infoTypeCode)) {
			// 当进入基础、申报、核销、单位信息页面进行编辑查看时，清除子表排序信息
			request.getSession().setAttribute("orderColumnSub", null);
			request.getSession().setAttribute("orderDirectionSub", null);
		}
		// 区分关联数据信息
		if (configMap != null && !configMap.isEmpty()) {
			String relatedFileType = (String) configMap
					.get("config.related.filetype");
			if (this.fileType != null && relatedFileType != null
					&& relatedFileType.indexOf(this.fileType) > -1) {
				return editDataRelated();
			}
		}
		return editData(infoTypeCode, tableId);
	}

	private String editDataRelated() {
		// 公共数据
		if (StringUtil.isEmpty(this.infoType)) {
			if (StringUtil.isNotEmpty(this.fileType)
					&& this.fileType.length() == 2) {
				this.infoType = this.fileType.substring(0, 1);
			} else if (StringUtil.isNotEmpty(tableId)) {
			}
		}
		TableDataVO[] tableData = new TableDataVO[2];
		// 1.查询签约数据
		RptData data = dataDealService.findRptDataByRelatedBusinessId(tableId,
				fileType, businessId);
		if (data == null) {
			log.error("数据异常，未找到对应数据");
			// return ERROR;
			tableData[0] = null;
		} else {
			tableData[0] = editDataNew(this.tableId, data.getFileType(), data
					.getBusinessId(), false);
		}
		// 2.查询变动数据
		tableData[1] = editDataNew(this.tableId, this.fileType, businessId,
				true);
		// 共享数据
		setInfoToRequest();
		this.request.setAttribute("tableData", tableData);
		return RELATED;
	}

	private void setInfoToRequest() {
		// 当从列表点击编辑按钮进入编辑页面时才会有此属性，用来判断是否给填报人和申报日期字段在页面赋默认值。
		// 该属性请勿在编辑页面hidden
		String setDefault = request.getParameter("setDefault");
		request.setAttribute("setDefault", setDefault);
		this.fromFlag = request.getParameter("fromFlag");
		this.request.setAttribute("fromFlag", this.fromFlag);
		this.request.setAttribute("infoTypeCode", this.infoTypeCode);
		this.request.setAttribute("busiDataType", this.busiDataType);
		this.request.setAttribute("infoType", this.infoType);
		this.request.setAttribute("fileType", this.fileType);
		this.request.setAttribute("cfaRptNo", this.rptNo);
		this.request.setAttribute("message", message);
		this.request.setAttribute("businessId", this.businessId);
		if (!(DataUtil.isJCDWSBHX(infoTypeCode))) {
			innerCreateFlag = "1";
		}
		if (!StringUtils.isEmpty(businessId)) {
			this.addFieldToSession(ScopeConstants.CURRENT_BUSINESS_ID,
					businessId);
		}
	}

	/**
	 * 外部表查看数据的入口
	 * 
	 * @return
	 */
	public String viewData() {
		try {
			if (!sessionInit(true)) {
				return SUCCESS;
			}
			setCheckResion();
			if (StringUtil.isBlank(infoTypeCode)
					&& StringUtil.isNotEmpty(tableId)) {
				infoTypeCode = DataUtil.getInfoTypeCodeByTableId(tableId);
			}
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				// 当进入基础、申报、核销、单位信息页面进行编辑查看时，清除子表排序信息
				request.getSession().setAttribute("orderColumnSub", null);
				request.getSession().setAttribute("orderDirectionSub", null);
			}
			return viewData(infoTypeCode, tableId);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public static String filter(String message) {
		if (message == null) {
			return (null);
		}
		char content[] = new char[message.length()];
		message.getChars(0, message.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			default:
				result.append(content[i]);
			}
		}
		return (result.toString());
	}

	/**
	 * <p>
	 * 设置审核不通过理由
	 * </p>
	 * <p>
	 * 设置数据打回原因
	 * </p>
	 */
	private void setCheckResion() {
		String reasioninfoStr = "";
		String lowerStatusReasion = "";
		String receiveErrorMemo = "";
		if (businessId != null && !businessId.equals("") && tableId != null
				&& !tableId.equals("")
				&& !tableId.toLowerCase().startsWith("_sub_")) {
			RptData rt = new RptData();
			rt.setBusinessId(businessId);
			rt.setTableId(tableId);
			List rpts = dataDealService.findRptDataReduce(rt);
			if (rpts.size() > 0) {
				RptData rtt = (RptData) rpts.get(0);
				if (rtt.getDataStatus() != null) {
					int nDataStatus = Integer.valueOf(rtt.getDataStatus())
							.intValue();
					if (nDataStatus == DataUtil.WJY_STATUS_NUM
							|| nDataStatus == DataUtil.JYWTG_STATUS_NUM
							|| nDataStatus == DataUtil.JYYTG_STATUS_NUM
							|| nDataStatus == DataUtil.SHWTG_STATUS_NUM) {
						// 审核不通过理由
						reasioninfoStr = dataDealService.getRefuseCheckInfo(rt,
								"1");
					}
					if (nDataStatus == DataUtil.WJY_STATUS_NUM
							|| nDataStatus == DataUtil.JYYTG_STATUS_NUM
							|| nDataStatus == DataUtil.SHYTG_STATUS_NUM) {
						// 打回原因
						lowerStatusReasion = dataDealService
								.getRefuseCheckInfo(rt, "2");
						// 反馈错误信息
						receiveErrorMemo = dataDealService.getRefuseCheckInfo(
								rt, "3");
					}
				}
			}
		}
		this.request.setAttribute("reasioninfoStr", filter(reasioninfoStr));
		this.request.setAttribute("lowerStatusReasion",
				filter(lowerStatusReasion));
		this.request.setAttribute("receiveErrorMemo", receiveErrorMemo);
	}

	private TableDataVO editDataNew(String tableId, String fileType,
			String businessId, boolean checkFail) {
		TableDataVO dataVo = new TableDataVO();
		boolean failSave = checkFail && !StringUtils.isEmpty(this.message)
				&& message.equals("checkFailure");// 保存或校验失败
		// table数据
		RptTableInfo tableInfo = dataDealService.findRptTableInfoById(tableId,
				fileType);
		dataVo.setRptTableInfo(tableInfo);
		// 列数据
		if (failSave)
			dataVo.setRptColumnList((List) this.request
					.getAttribute("rptColumnList"));
		else
			dataVo.setRptColumnList(dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							fileType)));
		if (checkFail) {
			List xyRptColumns = (List) this.request
					.getAttribute("rptColumnList");
			if (xyRptColumns != null && xyRptColumns.size() > 0) {
				dataVo.setRptColumnList(xyRptColumns);
			}
		}
		// 循环列信息，作各种处理
		Map dictionaryMap = (Map) SystemCache
				.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
		int cFlag = 0;
		Map dictListMapTemp = new HashMap();
		StringBuffer columns = new StringBuffer();
		String businessNoAliasColumnId = null;
		for (Iterator i = dataVo.getRptColumnList().iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			if ("table".equals(column.getDataType())) {
				continue;
			}
			// 赋别名c1,c2,c3
			column.setAliasColumnId("c" + (++cFlag));
			// 根据字段物理名和别名拼查询SQL
			columns.append("t.").append(column.getColumnId()).append(" as ")
					.append(column.getAliasColumnId()).append(",");
			// 若是下拉框，则根据字典类型取字典信息，形成下拉框内容集，并将内容集放在request中，名为
			// 别名_list,例如c1_list
			if ("3".equals(column.getTagType())) {
				if ("TEAMID".equalsIgnoreCase(column.getColumnId())) {
					User currentUser = (User) this
							.getFieldFromSession(Constants.USER);
					List bussTypeList = dataDealService
							.findBussTypeList(currentUser.getId());
					dictListMapTemp.put(column.getAliasColumnId() + "_list",
							bussTypeList);
				} else {
					if (dictionaryMap != null) {
						Map tableMap = (HashMap) dictionaryMap.get(tableId);
						if (tableMap != null) {
							List codeDictionaryList = (ArrayList) tableMap
									.get(column.getDictionaryTypeId());
							if (codeDictionaryList != null) {
								dictListMapTemp.put(column.getAliasColumnId()
										+ "_list", codeDictionaryList);
							}
						}
					}
				}
			}
			if ("BUSINESSNO".equalsIgnoreCase(column.getColumnId())) {
				businessNoAliasColumnId = column.getAliasColumnId();
			}
		}
		dataVo.setDictListMap(dictListMapTemp);
		// 获取rptdata数据
		if (failSave) {
			RptData rd = (RptData) this.request.getAttribute("rptData");
			if (rd != null && StringUtil.isEmpty(rd.getBusinessId())
					&& StringUtil.isNotEmpty(businessId)) {
				rd.setBusinessId(businessId);
			}
			dataVo.setRptData(rd);
		} else {
			while (cFlag < largestColumnNum) {
				columns.append("'' as c").append(++cFlag).append(",");
			}
			// 根据物理表名，机构号，业务主键等获取数据
			List rptDataList = null;
			if (!StringUtil.isEmpty(businessId)) {
				RptData params = new RptData();
				params.setTableId(tableId);
				params.setBusinessId(businessId);
				params.setColumns(StringUtils
						.removeEnd(columns.toString(), ","));
				rptDataList = dataDealService.findRptData(params);
			}
			// 基础/单位
			if (CollectionUtil.isNotEmpty(rptDataList)) {
				RptData rd = (RptData) rptDataList.get(0);
				dataVo.setRptData(rd);
				if (rd != null
						&& StringUtil.isNotEmpty(businessNoAliasColumnId)) {
					Object objBusiNo = BeanUtils.getValue(rd,
							businessNoAliasColumnId);
					if (objBusiNo != null) {
						String businessNo = objBusiNo.toString();
						this.request.setAttribute("businessNo", businessNo);
					}
				}
			} else {
				dataVo.setRptData(new RptData());
			}
		}
		RptData rptData = dataVo.getRptData();
		if (!StringUtils.isEmpty(businessId)) {
			rptData.setIsHaveSendCommit(dataDealService.isRptHasSendCommit(
					tableId, businessId));
		} else {
			rptData.setIsHaveSendCommit(false);
		}
		if (String.valueOf(DataUtil.JYWTG_STATUS_NUM).equals(
				rptData.getDataStatus())) {
			try {
				this.validateDataForEditView(dataVo.getRptColumnList(),
						rptData, tableId, fileType);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return dataVo;
	}

	/**
	 * 编辑/修改信息的通用方法
	 * 
	 * @return
	 */
	public String editData(String infoTypeCode, String tableId) {
		try {
			// 获取当前用户信息
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			// 是否限制可编辑字段范围 yes/no
			this.fromFlag = request.getParameter("fromFlag");
			this.request.setAttribute("fromFlag", this.fromFlag);
			// 当从列表点击编辑按钮进入编辑页面时才会有此属性，用来判断是否给填报人和申报日期字段在页面赋默认值。
			// 该属性请勿在编辑页面hidden
			String setDefault = request.getParameter("setDefault");
			request.setAttribute("setDefault", setDefault);
			// 根据报表ID获取报表信息
			// List tables = dataDealService.findRptTableInfo(new RptTableInfo(
			// tableId));
			// rptTableInfo = (RptTableInfo) tables.get(0);
			rptTableInfo = dataDealService.findRptTableInfoById(tableId,
					fileType);
			// wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			// 当数据校验失败时，会传递DataDealAction.VALIDATE_DATA_FAILD_FLAG(
			// validateDataFailed)参数，将不会进行更新，直接从session中取字段信息和数据信息，并返回到编辑页面
			if (!StringUtils.isEmpty(this.message)
					&& this.message.equals("checkFailure")) {
				rptColumnList = (List) this.request
						.getAttribute("rptColumnList");
				rptData = (RptData) this.request.getAttribute("rptData");
				if (!StringUtils.isEmpty(businessId)) {
					rptData.setIsHaveSendCommit(dataDealService
							.isRptHasSendCommit(this.tableId, businessId));
				}
				rptData.setCanNext(false);
				for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) i.next();
					// 若是下拉框，则根据字典类型取字典信息，形成下拉框内容集，并将内容集放在request中，名为
					// 别名_list,例如c1_list
					if ("3".equals(column.getTagType())) {
						if ("BUOCMONTH".equalsIgnoreCase(column.getColumnId())) {
							List yearMonthList = initBuocMonthSelectList();
							this.addFieldToRequest(column.getAliasColumnId()
									+ "_list", yearMonthList);
						} else if ("TEAMID".equalsIgnoreCase(column
								.getColumnId())) {
							List bussTypeList = dataDealService
									.findBussTypeList(currentUser.getId());
							this.addFieldToRequest(column.getAliasColumnId()
									+ "_list", bussTypeList);
						} else {
							Map dictionaryMap = (Map) SystemCache
									.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
							if (dictionaryMap != null) {
								Map tableMap = (HashMap) dictionaryMap
										.get(tableId);
								Map publicMap = (HashMap) dictionaryMap
										.get("PUBLIC");
								List codeDictionaryList = null;
								if (tableMap != null) {
									codeDictionaryList = (ArrayList) tableMap
											.get(column.getDictionaryTypeId());
								}
								if (CollectionUtil.isEmpty(codeDictionaryList)
										&& publicMap != null) {
									codeDictionaryList = (ArrayList) publicMap
											.get(column.getDictionaryTypeId());
								}
								if ("T_FAL_Z02".equalsIgnoreCase(this.tableId)
										&& "TABLECODE".equalsIgnoreCase(column
												.getColumnId())
										&& CollectionUtil
												.isNotEmpty(codeDictionaryList)) {
									List codeDicTableInfoList = new ArrayList();
									for (Iterator c = codeDictionaryList
											.iterator(); c.hasNext();) {
										Dictionary cd = (Dictionary) c.next();
										Dictionary cdTableInfo = new Dictionary();
										cdTableInfo.setValueBank(cd
												.getValueBank());
										cdTableInfo.setValueStandardNum(cd
												.getValueStandardNum());
										cdTableInfo.setName(cd.getValueBank());
										codeDicTableInfoList.add(cdTableInfo);
									}
									this.addFieldToRequest(column
											.getAliasColumnId()
											+ "_list", codeDicTableInfoList);
								} else if (codeDictionaryList != null) {
									this.addFieldToRequest(column
											.getAliasColumnId()
											+ "_list", codeDictionaryList);
								}
							}
						}
					}
				}
				if (!(DataUtil.isJCDWSBHX(infoTypeCode))) {
					innerCreateFlag = "1";
				}
				// 在wl8.1下放入request
				saveFlag = "false";
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				this.request.setAttribute("rptData", this.rptData);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("rptTableInfo", this.rptTableInfo);
				this.request.setAttribute("message", this.message);
				this.request.setAttribute("beginDate", this.beginDate);
				this.request.setAttribute("endDate", this.endDate);
				this.request.setAttribute("businessId", this.businessId);
				return SUCCESS;
			} else {
				this.request.getSession().removeAttribute(
						ScopeConstants.CHECK_RESULT_INNER);
			}
			// 校验成功则作如下处理
			if (tableId.startsWith("T_CFA_SUB")
					|| tableId.endsWith("_STOCKINFO")
					|| tableId.endsWith("_INVEST")) {
				rptColumnList = dataDealService
						.findRptColumnInfo(new RptColumnInfo(tableId, null,
								"1", "SUB"));
			} else {
				rptColumnList = dataDealService
						.findRptColumnInfo(new RptColumnInfo(tableId, null,
								"1", this.fileType));
			}
			// 循环列信息，作各种处理
			int cFlag = 0;
			StringBuffer columns = new StringBuffer();
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				if (column.getDataType().equals("table")) {
					continue;
				}
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				// 根据字段物理名和别名拼查询SQL
				if (column.getTagType().startsWith("n")) {
					// 将汇率字段转换为字符型数值
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
				// 若是下拉框，则根据字典类型取字典信息，形成下拉框内容集，并将内容集放在request中，名为
				// 别名_list,例如c1_list
				if ("3".equals(column.getTagType())) {
					if ("BUOCMONTH".equalsIgnoreCase(column.getColumnId())) {
						List yearMonthList = initBuocMonthSelectList();
						this.addFieldToRequest(column.getAliasColumnId()
								+ "_list", yearMonthList);
					} else if ("TEAMID".equalsIgnoreCase(column.getColumnId())) {
						List bussTypeList = dataDealService
								.findBussTypeList(currentUser.getId());
						this.addFieldToRequest(column.getAliasColumnId()
								+ "_list", bussTypeList);
					} else {
						Map dictionaryMap = (Map) SystemCache
								.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
						if (dictionaryMap != null) {
							Map tableMap = (HashMap) dictionaryMap.get(tableId);
							Map publicMap = (HashMap) dictionaryMap
									.get("PUBLIC");
							List codeDictionaryList = null;
							if (tableMap != null) {
								codeDictionaryList = (ArrayList) tableMap
										.get(column.getDictionaryTypeId());
							}
							if (CollectionUtil.isEmpty(codeDictionaryList)
									&& publicMap != null) {
								codeDictionaryList = (ArrayList) publicMap
										.get(column.getDictionaryTypeId());
							}
							if ("T_FAL_Z02".equalsIgnoreCase(this.tableId)
									&& "TABLECODE".equalsIgnoreCase(column
											.getColumnId())
									&& CollectionUtil
											.isNotEmpty(codeDictionaryList)) {
								List codeDicTableInfoList = new ArrayList();
								for (Iterator c = codeDictionaryList.iterator(); c
										.hasNext();) {
									Dictionary cd = (Dictionary) c.next();
									Dictionary cdTableInfo = new Dictionary();
									cdTableInfo.setValueBank(cd.getValueBank());
									cdTableInfo.setValueStandardNum(cd
											.getValueStandardNum());
									cdTableInfo.setName(cd.getValueBank());
									codeDicTableInfoList.add(cdTableInfo);
								}
								this.addFieldToRequest(column
										.getAliasColumnId()
										+ "_list", codeDicTableInfoList);
							} else if (codeDictionaryList != null) {
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
			// 根据物理表名，机构号，业务主键等获取数据
			List rptDataList = null;
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				rptDataList = dataDealService.findRptData(new RptData(tableId,
						columns.toString().substring(0,
								columns.toString().length() - 1), null,
						dataStatus, businessId, orderColumn, orderDirection),
						paginationList);
			} else {
				rptDataList = dataDealService.findInnerRptData(new RptData(
						tableId, columns.toString().substring(0,
								columns.toString().length() - 1), businessId,
						subId, orderColumn, orderDirection), paginationList);
			}
			rptData = (RptData) rptDataList.get(0);
			if (String.valueOf(DataUtil.JYWTG_STATUS_NUM).equals(
					rptData.getDataStatus())
					&& !"1".equals(this.fileType)
					&& !"SUB".equals(this.fileType)) {
				this.validateDataForEditView(rptColumnList, rptData, tableId,
						fileType);
			}
			rptData.setIsHaveSendCommit(dataDealService.isRptHasSendCommit(
					this.tableId, businessId));
			if (StringUtils.isEmpty(message)
					&& request.getParameter(SAVE_OR_UPDATE_DATA_SUCCESS_FLAG) != null) {
				saveFlag = "true";
				// 保存数据成功
				message = "saveSuccess";
			}
			if ("1".equals(this.infoTypeCode) && this.rptData != null) {
				saveFlag = "true";
			}
			if (!"".equalsIgnoreCase(businessId)) {
				this.addFieldToSession(ScopeConstants.CURRENT_BUSINESS_ID,
						businessId);
			}
			this.judgeCannotNext(rptData, tableId);
			// 在wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("rptData", this.rptData);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("rptTableInfo", this.rptTableInfo);
			this.request.setAttribute("message", message);
			this.request.setAttribute("beginDate", this.beginDate);
			this.request.setAttribute("endDate", this.endDate);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("EditDataAction-editData", e);
			return ERROR;
		}
	}

	/**
	 * 查看数据（不可编辑修改）的通用方法
	 * 
	 * @return
	 */
	public String viewData(String infoTypeCode, String tableId) {
		if (request.getParameter("logicVerify") != null) {
			this.addFieldToRequest("logicVerify", "1");
		} else if (request.getParameter("dataAudit") != null) {
			this.addFieldToRequest("dataAudit", "1");
		} else if (request.getParameter("dataLowerStatus") != null) {
			this.addFieldToRequest("dataLowerStatus", "1");
		} else if (request.getParameter("searchDatas") != null) {
			this.addFieldToRequest("searchDatas", "1");
		}
		// 区分关联数据信息
		if (configMap != null && !configMap.isEmpty()) {
			String relatedFileType = (String) configMap
					.get("config.related.filetype");
			if (relatedFileType != null
					&& relatedFileType.indexOf(this.fileType) > -1) {
				return editDataRelated();
			}
		}
		if (request.getParameter("hasErrorFeedBack") != null) {
			String errorDesc = this.getErrorFeedBackDesc(tableId,
					this.fileType, businessId);
			this.addFieldToRequest("hasErrorFeedBack", "1");
			this.addFieldToRequest("receiveErrorMemo", errorDesc);
		}
		return editData(infoTypeCode, tableId);
	}

	private String getErrorFeedBackDesc(String tableId, String fileType,
			String businessId) {
		String errorDesc = "";
		String keyRptNoColumnId = DataUtil.getRptNoColumnIdByFileType(fileType);
		String byeRptNoColumnId = DataUtil
				.getByeRptNoColumnIdByFileType(fileType);
		RptData rd = new RptData();
		rd.setTableId(tableId);
		rd.setBusinessId(businessId);
		rd.setRptNoColumnId(keyRptNoColumnId);
		rd.setByeRptNoColumnId(byeRptNoColumnId);
		List listReduce = null;
		listReduce = dataDealService.findRptDataReduce(rd);
		if (listReduce != null && listReduce.size() == 1) {
			rd = (RptData) listReduce.get(0);
			String keyRptNo = rd.getRptNo();
			String byeRptNo = rd.getByeRptNo();
			String dataNo = keyRptNo;// 反馈信息表业务数据主键
			if (StringUtil.isNotEmpty(byeRptNoColumnId)
					&& StringUtil.isNotEmpty(byeRptNo)) {
				dataNo += "," + byeRptNo;
			}
			ReceiveReport receiveReport = dataDealService
					.findReceiveReportByDataNumber(dataNo);
			if (receiveReport != null && receiveReport.getId() > 0
					&& "0".equals(receiveReport.getHasReject())) {
				errorDesc = receiveReport.getErrorMemo();
			}
		}
		return errorDesc;
	}

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
}