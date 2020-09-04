/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptSendCommit;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.logic.model.Fal_A01Entity;
import com.cjit.gjsz.logic.model.Fal_A02Entity;
import com.cjit.gjsz.logic.model.Fal_B01Entity;
import com.cjit.gjsz.logic.model.Fal_B02Entity;
import com.cjit.gjsz.logic.model.Fal_B03Entity;
import com.cjit.gjsz.logic.model.Fal_B04Entity;
import com.cjit.gjsz.logic.model.Fal_B05Entity;
import com.cjit.gjsz.logic.model.Fal_B06Entity;
import com.cjit.gjsz.logic.model.Fal_C01Entity;
import com.cjit.gjsz.logic.model.Fal_D01Entity;
import com.cjit.gjsz.logic.model.Fal_D02Entity;
import com.cjit.gjsz.logic.model.Fal_D03Entity;
import com.cjit.gjsz.logic.model.Fal_D04Entity;
import com.cjit.gjsz.logic.model.Fal_D05Entity;
import com.cjit.gjsz.logic.model.Fal_D06Entity;
import com.cjit.gjsz.logic.model.Fal_D07Entity;
import com.cjit.gjsz.logic.model.Fal_D09Entity;
import com.cjit.gjsz.logic.model.Fal_E01Entity;
import com.cjit.gjsz.logic.model.Fal_F01Entity;
import com.cjit.gjsz.logic.model.Fal_G01Entity;
import com.cjit.gjsz.logic.model.Fal_G02Entity;
import com.cjit.gjsz.logic.model.Fal_H01Entity;
import com.cjit.gjsz.logic.model.Fal_H02Entity;
import com.cjit.gjsz.logic.model.Fal_I01Entity;
import com.cjit.gjsz.logic.model.Fal_I02Entity;
import com.cjit.gjsz.logic.model.Fal_I03Entity;
import com.cjit.gjsz.logic.model.Fal_X01Entity;
import com.cjit.gjsz.logic.model.Fal_Z01Entity;
import com.cjit.gjsz.logic.model.Fal_Z02Entity;
import com.cjit.gjsz.logic.model.Fal_Z03Entity;
import com.cjit.gjsz.logic.model.VerifyModel;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class CheckDataAction1 extends DataDealAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -131863529087103061L;
	// 记录批量校验的结果
	private int pass = 0;
	private int faild = 0;
	private int total = 0;
	Object reportData = null;

	/**
	 * 逻辑校验入口，除了加入校验的逻辑，更新rptColumnList之外，与editData的程序逻辑基本一致
	 * 
	 * @return
	 */
	public String checkData() {
		log.info("CheckDataAction1-checkData");
		if (!sessionInit(true)) {
			return SUCCESS;
		}
		if (DataUtil.isJCDWSBHX(infoTypeCode)) {
			subId = null;
		}
		try {
			if (tableId == null
					|| !tableId.equals(DataUtil.getTableIdByFileType(fileType))) {
				tableId = DataUtil.getTableIdByFileType(fileType);
			}
			// 根据报表ID获取报表信息
			// List tables = dataDealService.findRptTableInfo(new RptTableInfo(
			// tableId));
			// rptTableInfo = (RptTableInfo) tables.get(0);
			rptTableInfo = dataDealService.findRptTableInfoById(tableId,
					fileType);
			// 以下程序获取数据
			// 循环列信息，作各种处理
			rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							this.fileType));
			if (StringUtil.isEmpty(tableId) && StringUtil.isNotEmpty(fileType)) {
				tableId = DataUtil.getTableIdByFileType(fileType);
			}
			if (StringUtil.isEmpty(infoTypeCode) && tableId != null
					&& tableId.indexOf("_SUB_") > 0) {
				// 此处查询子表信息
			} else {
				if (!this.tableId.equals(DataUtil
						.getTableIdByFileType(fileType))
						&& !"1".equals(fileType)) {
					tableId = DataUtil.getTableIdByFileType(fileType);
				}
			}
			int cFlag = 0;
			StringBuffer columns = new StringBuffer();
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				if ("table".equals(column.getDataType())) {
					continue;
				}
				column.setAliasColumnId("c" + (++cFlag));
				columns.append("t.").append(column.getColumnId())
						.append(" as ").append(column.getAliasColumnId())
						.append(",");
				// 若是下拉框，则根据字典类型取字典信息，形成下拉框内容集，并将内容集放在request中，名为
				// 别名_list,例如c1_list
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
			// 根据物理表名，机构号，业务主键等获取数据
			if (businessIds != null) {
				pass = faild = 0;
				for (int t = 0; t < businessIds.length; t++) {
					String dataStatusCondition = "";
					if (StringUtil.isNotEmpty(dataStatus)) {
						dataStatusCondition = " t.dataStatus = " + dataStatus
								+ " ";
					}
					List rptDataList = null;
					if (DataUtil.isJCDWSBHX(infoTypeCode)) {
						rptDataList = dataDealService.findRptData(new RptData(
								tableId, columns.toString().substring(0,
										columns.toString().length() - 1), null,
								dataStatusCondition, businessIds[t],
								orderColumn, orderDirection), paginationList);
					} else {
						rptDataList = dataDealService.findInnerRptData(
								new RptData(tableId,
										columns.toString()
												.substring(
														0,
														columns.toString()
																.length() - 1),
										businessIds[t], subId, orderColumn,
										orderDirection), paginationList);
					}
					if (rptDataList != null && rptDataList.size() == 1) {
						rptData = (RptData) rptDataList.get(0);
						// 先执行域校验,记录条数
						if (!validateData(rptColumnList, rptData, false)) {
							this.faild++;
							message = "数据逻辑校验未通过，请查看校验结果！";
							// dataDealService.updateRptData(new
							// RptData(tableId,
							// " datastatus = 2 ", businessIds[t], subId,
							// null, true));
							dataDealService.updateRptData(new RptData(tableId,
									" datastatus = "
											+ DataUtil.JYWTG_STATUS_NUM,
									businessIds[t], subId, null, true));
							continue;
						}
						/*******************************************************
						 * 执行逻辑校验开始 begain
						 ******************************************************/
						// 根据tableId和数据构造响应的类
						if (StringUtil.equals("T_FAL_A01_1", tableId)
								|| StringUtil.equals("T_FAL_A01_2", tableId)) {
							reportData = new Fal_A01Entity();
						} else if (StringUtil.equals("T_FAL_A02_1", tableId)
								|| StringUtil.equals("T_FAL_A02_2", tableId)
								|| StringUtil.equals("T_FAL_A02_3", tableId)) {
							reportData = new Fal_A02Entity();
						} else if (StringUtil.equals("T_FAL_B01", tableId)) {
							reportData = new Fal_B01Entity();
						} else if (StringUtil.equals("T_FAL_B02", tableId)) {
							reportData = new Fal_B02Entity();
						} else if (StringUtil.equals("T_FAL_B03", tableId)) {
							reportData = new Fal_B03Entity();
						} else if (StringUtil.equals("T_FAL_B04", tableId)) {
							reportData = new Fal_B04Entity();
						} else if (StringUtil.equals("T_FAL_B05", tableId)) {
							reportData = new Fal_B05Entity();
						} else if (StringUtil.equals("T_FAL_B06", tableId)) {
							reportData = new Fal_B06Entity();
						} else if (StringUtil.equals("T_FAL_C01", tableId)) {
							reportData = new Fal_C01Entity();
						} else if (StringUtil.equals("T_FAL_D01", tableId)) {
							reportData = new Fal_D01Entity();
						} else if (StringUtil.equals("T_FAL_D02", tableId)) {
							reportData = new Fal_D02Entity();
						} else if (StringUtil.equals("T_FAL_D03", tableId)) {
							reportData = new Fal_D03Entity();
						} else if (StringUtil.equals("T_FAL_D04", tableId)) {
							reportData = new Fal_D04Entity();
						} else if (StringUtil.equals("T_FAL_D05_1", tableId)
								|| StringUtil.equals("T_FAL_D05_2", tableId)) {
							reportData = new Fal_D05Entity();
						} else if (StringUtil.equals("T_FAL_D06_1", tableId)) {
							reportData = new Fal_D06Entity();
						} else if (StringUtil.equals("T_FAL_D07", tableId)) {
							reportData = new Fal_D07Entity();
						} else if (StringUtil.equals("T_FAL_D09", tableId)) {
							reportData = new Fal_D09Entity();
						} else if (StringUtil.equals("T_FAL_E01", tableId)) {
							reportData = new Fal_E01Entity();
						} else if (StringUtil.equals("T_FAL_F01", tableId)) {
							reportData = new Fal_F01Entity();
						} else if (StringUtil.equals("T_FAL_G01", tableId)) {
							reportData = new Fal_G01Entity();
						} else if (StringUtil.equals("T_FAL_G02", tableId)) {
							reportData = new Fal_G02Entity();
						} else if (StringUtil.equals("T_FAL_H01", tableId)) {
							reportData = new Fal_H01Entity();
						} else if (StringUtil.equals("T_FAL_H02", tableId)) {
							reportData = new Fal_H02Entity();
						} else if (StringUtil.equals("T_FAL_I01", tableId)) {
							reportData = new Fal_I01Entity();
						} else if (StringUtil.equals("T_FAL_I02", tableId)) {
							reportData = new Fal_I02Entity();
						} else if (StringUtil.equals("T_FAL_I03", tableId)) {
							reportData = new Fal_I03Entity();
						} else if (StringUtil.equals("T_FAL_X01", tableId)) {
							reportData = new Fal_X01Entity();
						} else if (StringUtil.equals("T_FAL_Z01", tableId)) {
							reportData = new Fal_Z01Entity();
						} else if (StringUtil.equals("T_FAL_Z02", tableId)) {
							reportData = new Fal_Z02Entity();
						} else if (StringUtil.equals("T_FAL_Z03", tableId)) {
							reportData = new Fal_Z03Entity();
						}
						BeanUtils.setProperty(reportData, "tableId", tableId);
						// if (StringUtil.equals("T_CFA_A_EXDEBT", tableId)) {
						// reportData = new Self_A_EXDEBT();
						// } else if (StringUtil.equals("T_CFA_B_EXGUARAN",
						// tableId)) {
						// reportData = new Self_B_EXGUARAN();
						// } else if (StringUtil.equals("T_CFA_C_DOFOEXLO",
						// tableId)) {
						// reportData = new Self_C_DOFOEXLO();
						// } else if (StringUtil.equals("T_CFA_D_LOUNEXGU",
						// tableId)) {
						// reportData = new Self_D_LOUNEXGU();
						// } else if (StringUtil.equals("T_CFA_E_EXPLRMBLO",
						// tableId)) {
						// reportData = new Self_E_EXPLRMBLO();
						// } else if (StringUtil.equals("T_CFA_F_STRDE",
						// tableId)) {
						// reportData = new Self_F_STRDE();
						// }
						// /* 银行代客业务 */
						// else if (StringUtil.equals("T_CFA_QFII_ACCOUNT",
						// tableId)
						// || StringUtil.equals("T_CFA_QFII_ASSETS_DEBT",
						// tableId)
						// || StringUtil
						// .equals("T_CFA_QFII_ASSETS_DEBT_MONTH",
						// tableId)
						// || StringUtil.equals("T_CFA_QFII_CHANGES",
						// tableId)
						// || StringUtil.equals(
						// "T_CFA_QFII_CHANGES_SPECIAL", tableId)
						// || StringUtil.equals("T_CFA_QFII_PROFIT_LOSS",
						// tableId)
						// || StringUtil.equals("T_CFA_QFII_REMIT",
						// tableId)
						// || StringUtil.equals("T_CFA_QDII_ACCOUNT",
						// tableId)
						// || StringUtil.equals("T_CFA_QDII_INVEST",
						// tableId)
						// || StringUtil.equals("T_CFA_QDII_REMIT",
						// tableId)
						// || StringUtil.equals(
						// "T_CFA_QDII_TRUSTEE_ACCOUNT", tableId)
						// || StringUtil.equals(
						// "T_CFA_BESTIR_ACCOUNT_CLOESD", tableId)
						// || StringUtil.equals("T_CFA_BESTIR_CHANGES",
						// tableId)
						// || StringUtil.equals("T_CFA_RQFII_ASSETS_DEBT",
						// tableId)
						// || StringUtil.equals(
						// "T_CFA_RQFII_ASSETS_DEBT_MONTH",
						// tableId)
						// || StringUtil.equals("T_CFA_RQFII_CHANGES",
						// tableId)
						// || StringUtil.equals(
						// "T_CFA_RQFII_INCOME_EXPEND", tableId)
						// || StringUtil.equals(
						// "T_CFA_RQFII_INCOME_EXPEND_BUY",
						// tableId)
						// || StringUtil.equals("T_CFA_RQFII_PROFIT_LOSS",
						// tableId)) {
						// saveToMap = true;
						// }
						// if (saveToMap) {
						// Map mapData = new HashMap();
						// for (Iterator i = rptColumnList.iterator(); i
						// .hasNext();) {
						// RptColumnInfo column = (RptColumnInfo) i.next();
						// if ("table".equals(column.getDataType())) {
						// continue;
						// }
						// String value = BeanUtils.getProperty(rptData,
						// column.getAliasColumnId());
						// mapData.put(column.getColumnId(), value);
						// }
						// // ????
						// if (!DataUtil.isJCDWSBHX(infoTypeCode)) {
						// mapData.put("subid", BeanUtils.getProperty(
						// rptData, "subId"));
						// }
						// mapData.put("businessid", businessId);
						// reportData = mapData;
						// } else {
						// 通过column的循环，把RptData中的值复制到对应的Model中
						for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
							RptColumnInfo column = (RptColumnInfo) i.next();
							if ("table".equals(column.getDataType())) {
								continue;
							}
							// BeanUtils.setValue(reportData,(column.getColumnId().toLowerCase()),BeanUtils.getValue(rptData,
							// column.getAliasColumnId()));
							if ("".equals(BeanUtils.getProperty(rptData, column
									.getAliasColumnId()))) {
								// BeanUtils.setProperty(reportData,(column.getColumnId().toLowerCase()),null);
								continue;
							} else {
								if (BeanUtils.getProperty(rptData, column
										.getAliasColumnId()) != null) {
									BeanUtils.setProperty(reportData, (column
											.getColumnId().toLowerCase()),
											BeanUtils.getProperty(rptData,
													column.getAliasColumnId()));
								}
							}
						}
						if (!DataUtil.isJCDWSBHX(infoTypeCode)) {
							BeanUtils.setProperty(reportData, "subid",
									BeanUtils.getProperty(rptData, "subId"));
						}
						// 复制businessId的值
						if (businessId == null
								|| "".equalsIgnoreCase(businessId)) {
							businessId = (String) this
									.getFieldFromSession(ScopeConstants.CURRENT_BUSINESS_ID);
						}
						businessId = businessIds[t];
						BeanUtils.setProperty(reportData, "businessid",
								businessId);
						if (StringUtil.isNotEmpty(rptData.getFileType())) {
							BeanUtils.setProperty(reportData, "filetype",
									rptData.getFileType());
						}
						if (StringUtil.isNotEmpty(rptData.getInstCode())) {
							BeanUtils.setProperty(reportData, "instcode",
									rptData.getInstCode());
						}
						// }// end save
						// end
						// 手工输入申报号校验提示信息
						StringBuffer verifyRptNoMessage = new StringBuffer();
						// 根据报文是否曾经报送，校验操作类型
						String verifyActionTypeMessage = null;
						// 判断对公/对私是否变化，并校验操作类型
						StringBuffer verifyCusTypeMessage = new StringBuffer();
						// 判断该报文是否曾经上报过
						List listSendCommit = dataDealService
								.findRptSendCommit(tableId, businessId, null,
										null, 1);
						if (listSendCommit != null
								&& listSendCommit.size() == 1) {
							RptSendCommit rsc = (RptSendCommit) listSendCommit
									.get(0);
							if (rsc != null) {
								String actionType = BeanUtils.getProperty(
										reportData, "actiontype");
								// 该报文曾报送并反馈无误,则操作类型不能是A-新建
								if ("A".equals(actionType)) {
									verifyActionTypeMessage = "[操作类型]不能是新建。";
								}
							}
						}
						// 判断是否需要创建变动编号类辅申报号码是否需要创建
						/*
						 * String indexCode = null; String indexNoColumn = null;
						 * if ("AR".equals(rptData.getFileType()) ||
						 * "AS".equals(rptData.getFileType()) ||
						 * "CB".equals(rptData.getFileType()) ||
						 * "DB".equals(rptData.getFileType()) ||
						 * "EB".equals(rptData.getFileType())) { // 变动编号
						 * indexCode = BeanUtils.getProperty(reportData,
						 * "changeno"); indexNoColumn = "changeno"; } else if
						 * ("BC".equals(rptData.getFileType())) { // 履约编号
						 * indexCode = BeanUtils.getProperty(reportData,
						 * "complianceno"); indexNoColumn = "complianceno"; }
						 * else if ("FB".equals(rptData.getFileType())) { //
						 * 终止支付编号 indexCode = BeanUtils.getProperty(reportData,
						 * "terpaycode"); indexNoColumn = "terpaycode"; } else
						 * if ("FC".equals(rptData.getFileType())) { // 付息编号
						 * indexCode = BeanUtils.getProperty(reportData,
						 * "inpaycode"); indexNoColumn = "inpaycode"; } if
						 * (StringUtil.isEmpty(indexCode) &&
						 * StringUtil.isNotEmpty(indexNoColumn)) { String
						 * businessNo = BeanUtils.getProperty( reportData,
						 * "businessno"); if (StringUtil.isNotEmpty(businessNo)) {
						 * indexCode = dataDealService
						 * .findIndexCodeForSelf(tableId, rptData
						 * .getFileType(), indexNoColumn, null, businessNo);
						 * dataDealService.updateRptData(new RptData( tableId, " " +
						 * indexNoColumn + " = '" + indexCode + "' ",
						 * businessId, null, null, true)); } }
						 */
						// --以下程序获取逻辑校验结果，更新rptColumnList,并根据校验结果更新数据状态
						// 校验结果
						// VerifyModel vm = verifyService.verify(new String[] {
						// rptData
						// .getBusinessId() }, tableId);
						VerifyModel vm = this.getVerifyService().verify(
								reportData, tableId, instCode, interfaceVer,
								configIsCluster);
						// 主表校验结果
						Map mainResult = null;
						List innerResults = null;
						if (vm != null) {
							mainResult = vm.getFatcher();
							innerResults = vm.getChildren();
						}
						// 若已进行申报号校验，则将校验结果补充进主表校验结果中
						if (verifyRptNoMessage != null
								&& verifyRptNoMessage.toString().length() > 0) {
							if (mainResult == null) {
								mainResult = new HashMap();
							}
							mainResult.put("RPTNO", verifyRptNoMessage
									.toString());
						}
						// 若操作类型不正确，则将校验结果补充进主表校验结果中
						if (verifyActionTypeMessage != null) {
							if (mainResult == null) {
								mainResult = new HashMap();
							}
							mainResult.put("ACTIONTYPE",
									verifyActionTypeMessage);
						}
						// 若对公/对私发生改变，且操作类型不是删除时，则将校验结果补充进主表校验结果中
						if (verifyCusTypeMessage != null
								&& verifyCusTypeMessage.toString().length() > 0) {
							if (mainResult == null) {
								mainResult = new HashMap();
							}
							mainResult.put("CUSTYPE", verifyCusTypeMessage
									.toString());
						}
						if (mainResult != null && mainResult.size() != 0) {
							for (Iterator i = rptColumnList.iterator(); i
									.hasNext();) {
								RptColumnInfo column = (RptColumnInfo) i.next();
								String consRuleVDesc = (String) mainResult
										.get(column.getColumnId().toUpperCase());
								// 如果这一列逻辑校验失败，则置其校验状态为false
								if (consRuleVDesc != null) {
									column.setConsRuleVSuccess(false);
									column.setConsRuleVDesc(consRuleVDesc);
								}
							}
							if (mainResult
									.get(ScopeConstants.CHECK_RESULT_OPENINFO) != null) {
								session
										.put(
												ScopeConstants.CHECK_RESULT_OPENINFO,
												mainResult
														.get(ScopeConstants.CHECK_RESULT_OPENINFO));
							}
						}
						// 子表的校验结果
						if (innerResults != null && innerResults.size() != 0) {
							Map checkResultInner = new HashMap();
							// 数据属性校验
							session.put(ScopeConstants.CHECK_RESULT_INNER,
									checkResultInner);
							for (Iterator i = innerResults.iterator(); i
									.hasNext();) {
								Map innerResult = (HashMap) i.next();
								String subId = (String) innerResult
										.get("SUBID");
								StringBuffer text = new StringBuffer();
								text.append("<ol>");
								for (Iterator j = innerResult.keySet()
										.iterator(); j.hasNext();) {
									String wrongColumnId = (String) j.next();
									if (!"SUBID".equals(wrongColumnId)) {
										text
												.append(
														"<li>"
																+ innerResult
																		.get(wrongColumnId))
												.append("\n</li>");
									}
								}
								text.append("</ol>");
								checkResultInner.put(subId, text.toString());
								// 新加的
								this.request.setAttribute("subId", subId);
								this.request.setAttribute("cris" + subId, text
										.toString());
								session.put("cris" + subId, text.toString());
								// request.getSession().getServletContext()
								// .setAttribute("cris" + subId,
								// text.toString());
							}
						}
						if ((mainResult != null && mainResult.size() != 0)
								|| (innerResults != null && innerResults.size() != 0)) {
							faild++;
							message = "数据逻辑校验未通过，请查看校验结果！";
							// dataDealService.updateRptData(new
							// RptData(tableId,
							// " datastatus = 2 ", businessIds[t], subId,
							// null, true));
							dataDealService.updateRptData(new RptData(tableId,
									" datastatus = "
											+ DataUtil.JYWTG_STATUS_NUM,
									businessIds[t], subId, null, true));
							User user = this.getCurrentUser();
							String menuName = "数据录入."
									+ rptTableInfo.getInfoType();
							logManagerService.writeLog(request, user, "0001",
									menuName, "校验", "针对[机构：" + this.instCode
											+ "，单据："
											+ rptTableInfo.getTableName()
											+ "，业务号：" + this.businessId
											+ "]执行数据逻辑校验操作", "0");
						} else {
							// TODO: 校验通过，直接将状态改为已审核。
							pass++;
							message = "数据逻辑校验通过！";
							User user = this.getCurrentUser();
							String menuName = "数据录入."
									+ rptTableInfo.getInfoType();
							logManagerService.writeLog(request, user, "0001",
									menuName, "校验", "针对[机构：" + this.instCode
											+ "，单据："
											+ rptTableInfo.getTableName()
											+ "，业务号：" + this.businessId
											+ "]执行数据逻辑校验操作", "1");
							if ("yes"
									.equalsIgnoreCase(this.configOverleapAudit)) {
								// 忽略审核操作
								dataDealService.updateRptData(new RptData(
										tableId, " datastatus = "
												+ DataUtil.SHYTG_STATUS_NUM
												+ ", modifyuser = '"
												+ user.getId() + "' ",
										businessIds[t], subId, null, true));
								if (fileType != null
										&& TableIdRela.getZjxyMap().get(
												fileType) != null) {
									// 视情况，若记录为签约信息且无业务编号，则自动用业务ID进行赋值
									RptData rd = new RptData();
									rd.setTableId(tableId);
									rd
											.setUpdateSql(" businessno = businessid ");
									rd
											.setUpdateCondition(" businessid = '"
													+ businessIds[t]
													+ "' and (businessno is null or businessno = '')");
									dataDealService.updateRptData(rd);
								}
							} else {
								if (!"yes"
										.equalsIgnoreCase(this.configOverleapCommit)) {
									// 未忽略提交操作
									dataDealService
											.updateRptData(new RptData(
													tableId,
													" datastatus = "
															+ DataUtil.JYYTG_STATUS_NUM,
													businessIds[t], subId,
													null, true));
								} else {
									// 忽略提交操作
									dataDealService
											.updateRptData(new RptData(
													tableId,
													" datastatus = "
															+ DataUtil.YTJDSH_STATUS_NUM
															+ ", modifyuser = '"
															+ user.getId()
															+ "' ",
													businessIds[t], subId,
													null, true));
								}
							}
						}
					}
				}
			}
			total = pass + faild;
			session.put(ScopeConstants.CHECK_RESULT_PASS_COUNT, Integer
					.toString(pass));
			session.put(ScopeConstants.CHECK_RESULT_TOTAL_COUNT, Integer
					.toString(total));
			session.put(ScopeConstants.SESSION_CHECK_BUSINESSIDS,
					this.businessIds);
			// 在wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("rptData", this.rptData);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("rptTableInfo", this.rptTableInfo);
			this.request.setAttribute("beginDate", this.beginDate);
			this.request.setAttribute("endDate", this.endDate);
			this.request.setAttribute("dataStatus", this.dataStatus);
			this.request.setAttribute("searchLowerOrg", this.searchLowerOrg);
			log.info("CheckDataAction1-checkData-success");
			return SUCCESS;
		} catch (Exception e) {
			log.error("CheckDataAction1-checkData", e);
			return ERROR;
		}
	}

	public void isFinished() {
		if (businessId != null && fileType != null) {
			this.response.setContentType("text/html; charset=UTF-8");
			String res = null;
			String tableId = DataUtil.getTableIdByFileType(fileType);
			Long result = dataDealService.findRptInfoByBusi(new RptData(
					tableId, null, businessId, null, null, null));
			if (result.longValue() == 0) {
				res = "0";
			} else {
				res = "1";
			}
			try {
				this.response.getWriter().write(res);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					this.response.getWriter().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public int getFaild() {
		return faild;
	}

	public void setFaild(int faild) {
		this.faild = faild;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}