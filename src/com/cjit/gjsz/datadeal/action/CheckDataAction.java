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
import org.apache.commons.lang.StringUtils;

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
import com.cjit.gjsz.logic.model.Self_A_EXDEBT;
import com.cjit.gjsz.logic.model.Self_B_EXGUARAN;
import com.cjit.gjsz.logic.model.Self_C_DOFOEXLO;
import com.cjit.gjsz.logic.model.Self_D_LOUNEXGU;
import com.cjit.gjsz.logic.model.Self_E_EXPLRMBLO;
import com.cjit.gjsz.logic.model.Self_F_STRDE;
import com.cjit.gjsz.logic.model.VerifyModel;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class CheckDataAction extends DataDealAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -131863529087103061L;
	private final String OK_NEW = "ok_new";
	private final String OK_UPDATE = "ok_update";
	private final String FAIL_CREATE = "fail_create";
	private final String FAIL_EDIT = "fail_edit";
	Object reportData = null;

	/**
	 * 逻辑校验入口，除了加入校验的逻辑，更新rptColumnList之外，与editData的程序逻辑基本一致 modify by panshaobo
	 * Date:2010-11-12 FUNC:重写错误校验结束跳转和数据保存方式，全部保存到request中，使用chain方式跳转
	 * 
	 * @return
	 */
	public String checkData() {
		log.info("CheckDataAction-checkData");
		// 不从session拿id
		if (!sessionInit(false)) {
			return SUCCESS;
		}
		if (DataUtil.isJCDWSBHX(infoTypeCode)) {
			subId = null;
		}
		try {
			setInfoToRequest();
			// 报表数据
			rptTableInfo = dataDealService.findRptTableInfoById(tableId,
					fileType);
			rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							this.fileType));
			// 字段按order赋别名c1,c2,c3...,并根据字段物理名和别名拼查询SQL
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
				// 加工数值型数据，去除数据中多余的逗号
				if ("n".equalsIgnoreCase(column.getDataType().substring(0, 1))) {
					String value = (String) BeanUtils.getProperty(rptData,
							column.getAliasColumnId());
					value = value.replaceAll(",", "");
					BeanUtils.setProperty(rptData, column.getAliasColumnId(),
							value);
				}
			}
			while (cFlag < largestColumnNum) {
				columns.append("'' as c").append(++cFlag).append(",");
			}
			// 执行域校验,不成功则回到信息编辑页面（校验错误的提示描述在rptColumnList的元素中都已包含）
			if (!validateData(rptColumnList, rptData, false)) {
				this.message = "checkFailure";
				// 修改方式更新校验状态
				// if(!StringUtils.isEmpty(businessId))
				// dataDealService.updateRptData(new RptData(tableId,
				// " datastatus = " + DataUtil.JYWTG_STATUS_NUM,
				// businessId, subId, null, true));
				// 在wl8.1下放入request
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				this.request.setAttribute("rptData", this.rptData);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("rptTableInfo", this.rptTableInfo);
				this.request.setAttribute("message", "checkFailure");
				this.request.setAttribute("businessId", this.businessId);
				log.info("CheckDataAction-checkData-validateData-failure");
				if (!StringUtils.isEmpty(businessId))
					return FAIL_EDIT;
				else
					return FAIL_CREATE;
			}
			/*******************************************************************
			 * 执行逻辑校验开始 begain
			 ******************************************************************/
			boolean saveToMap = false; // 使用map方式传值
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
			/* 银行自身业务信息 */
			if (StringUtil.equals("T_CFA_A_EXDEBT", tableId)) {
				reportData = new Self_A_EXDEBT();
			} else if (StringUtil.equals("T_CFA_B_EXGUARAN", tableId)) {
				reportData = new Self_B_EXGUARAN();
			} else if (StringUtil.equals("T_CFA_C_DOFOEXLO", tableId)) {
				reportData = new Self_C_DOFOEXLO();
			} else if (StringUtil.equals("T_CFA_D_LOUNEXGU", tableId)) {
				reportData = new Self_D_LOUNEXGU();
			} else if (StringUtil.equals("T_CFA_E_EXPLRMBLO", tableId)) {
				reportData = new Self_E_EXPLRMBLO();
			} else if (StringUtil.equals("T_CFA_F_STRDE", tableId)) {
				reportData = new Self_F_STRDE();
			}
			/* 银行代客业务 */
			else if (StringUtil.equals("T_CFA_QFII_ACCOUNT", tableId)
					|| StringUtil.equals("T_CFA_QFII_ASSETS_DEBT", tableId)
					|| StringUtil.equals("T_CFA_QFII_ASSETS_DEBT_MONTH",
							tableId)
					|| StringUtil.equals("T_CFA_QFII_CHANGES", tableId)
					|| StringUtil.equals("T_CFA_QFII_CHANGES_SPECIAL", tableId)
					|| StringUtil.equals("T_CFA_QFII_PROFIT_LOSS", tableId)
					|| StringUtil.equals("T_CFA_QFII_REMIT", tableId)
					|| StringUtil.equals("T_CFA_QDII_ACCOUNT", tableId)
					|| StringUtil.equals("T_CFA_QDII_INVEST", tableId)
					|| StringUtil.equals("T_CFA_QDII_REMIT", tableId)
					|| StringUtil.equals("T_CFA_QDII_TRUSTEE_ACCOUNT", tableId)
					|| StringUtil
							.equals("T_CFA_BESTIR_ACCOUNT_CLOESD", tableId)
					|| StringUtil.equals("T_CFA_BESTIR_CHANGES", tableId)
					|| StringUtil.equals("T_CFA_RQFII_ASSETS_DEBT", tableId)
					|| StringUtil.equals("T_CFA_RQFII_ASSETS_DEBT_MONTH",
							tableId)
					|| StringUtil.equals("T_CFA_RQFII_CHANGES", tableId)
					|| StringUtil.equals("T_CFA_RQFII_INCOME_EXPEND", tableId)
					|| StringUtil.equals("T_CFA_RQFII_INCOME_EXPEND_BUY",
							tableId)
					|| StringUtil.equals("T_CFA_RQFII_PROFIT_LOSS", tableId)) {
				saveToMap = true;
			}
			if (saveToMap) {
				Map mapData = new HashMap();
				for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) i.next();
					if ("table".equals(column.getDataType())) {
						continue;
					}
					String value = BeanUtils.getProperty(rptData, column
							.getAliasColumnId());
					mapData.put(column.getColumnId(), value);
				}
				// ????
				if (!DataUtil.isJCDWSBHX(infoTypeCode)) {
					mapData.put("subid", BeanUtils
							.getProperty(rptData, "subId"));
				}
				mapData.put("businessid", businessId);
				reportData = mapData;
			} else {
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
									.getColumnId().toLowerCase()), BeanUtils
									.getProperty(rptData, column
											.getAliasColumnId()));
						}
					}
				}
				// ????
				if (!DataUtil.isJCDWSBHX(infoTypeCode)) {
					BeanUtils.setProperty(reportData, "subid", BeanUtils
							.getProperty(rptData, "subId"));
				}
				if (!StringUtils.isEmpty(businessId)) {
					BeanUtils.setProperty(reportData, "businessid", businessId);
				} else {
					if (StringUtil.isNotEmpty(this.instCode)) {
						BeanUtils.setProperty(reportData, "instcode",
								this.instCode);
					}
				}
				if (StringUtil.isNotEmpty(this.fileType)) {
					BeanUtils
							.setProperty(reportData, "filetype", this.fileType);
				}
				if (StringUtil.isNotEmpty(this.rptData.getInstCode())) {
					BeanUtils.setProperty(reportData, "instcode", this.rptData
							.getInstCode());
				}
			}// end save
			// end
			// 当配置申报号可以手工输入时，进行校验
			StringBuffer verifyRptNoMessage = new StringBuffer();
			// 根据报文是否曾经报送，校验操作类型
			String verifyActionTypeMessage = null;
			// 判断对公/对私是否变化，并校验操作类型
			StringBuffer verifyCusTypeMessage = new StringBuffer();
			// 判断该报文是否曾经上报过
			if (!StringUtils.isEmpty(businessId)) {
				List listSendCommit = dataDealService.findRptSendCommit(
						tableId, businessId, null, null, 1);
				if (listSendCommit != null && listSendCommit.size() == 1) {
					RptSendCommit rsc = (RptSendCommit) listSendCommit.get(0);
					if (rsc != null) {
						String actionType = BeanUtils.getProperty(reportData,
								"actiontype");
						// 该报文曾报送并反馈无误,则操作类型不能是A-新建
						if ("A".equals(actionType)) {
							verifyActionTypeMessage = "[操作类型]不能是新建。";
						}
					}
				}
			}
			// 以下程序获取逻辑校验结果，更新rptColumnList,并根据校验结果更新数据状态
			// 校验结果
			VerifyModel vm = this.getVerifyService().verify(reportData,
					tableId, instCode, interfaceVer, configIsCluster);
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
				mainResult.put("RPTNO", verifyRptNoMessage.toString());
			}
			// 若操作类型不正确，则将校验结果补充进主表校验结果中
			if (verifyActionTypeMessage != null) {
				if (mainResult == null) {
					mainResult = new HashMap();
				}
				mainResult.put("ACTIONTYPE", verifyActionTypeMessage);
			}
			// 若对公/对私发生改变，且操作类型不是删除时，则将校验结果补充进主表校验结果中
			if (verifyCusTypeMessage != null
					&& verifyCusTypeMessage.toString().length() > 0) {
				if (mainResult == null) {
					mainResult = new HashMap();
				}
				mainResult.put("CUSTYPE", verifyCusTypeMessage.toString());
			}
			if (mainResult != null && mainResult.size() != 0) {
				for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) i.next();
					String consRuleVDesc = (String) mainResult.get(column
							.getColumnId().toUpperCase());
					// 如果这一列逻辑校验失败，则置其校验状态为false
					if (consRuleVDesc != null) {
						column.setConsRuleVSuccess(false);
						column.setConsRuleVDesc(consRuleVDesc);
					}
				}
				// if(mainResult.get(ScopeConstants.CHECK_RESULT_INVCOUNTRYCODE)
				// != null){
				// session.put(ScopeConstants.CHECK_RESULT_INVCOUNTRYCODE,
				// mainResult.get(ScopeConstants.CHECK_RESULT_INVCOUNTRYCODE));
				// }
				// if(mainResult.get(ScopeConstants.CHECK_RESULT_OPENINFO) !=
				// null){
				// session.put(ScopeConstants.CHECK_RESULT_OPENINFO,
				// mainResult
				// .get(ScopeConstants.CHECK_RESULT_OPENINFO));
				// }
			}
			// 子表的校验结果
			if (innerResults != null && innerResults.size() != 0) {
				Map checkResultInner = new HashMap();
				// 数据属性校验
				session
						.put(ScopeConstants.CHECK_RESULT_INNER,
								checkResultInner);
				for (Iterator i = innerResults.iterator(); i.hasNext();) {
					Map innerResult = (HashMap) i.next();
					String subId = (String) innerResult.get("SUBID");
					String innerTableId = (String) innerResult
							.get("INNERTABLEID");
					StringBuffer text = new StringBuffer();
					text.append("<ol>");
					for (Iterator j = innerResult.keySet().iterator(); j
							.hasNext();) {
						String wrongColumnId = (String) j.next();
						if (!"SUBID".equals(wrongColumnId)
								&& !"INNERTABLEID".equals(wrongColumnId)) {
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
					if (!checkResultInner.containsKey(innerTableId)) {
						checkResultInner.put(innerTableId, innerTableId);
					}
					// /checkResultInnerVcountry.put(subId,
					// text.toString());
					// 新加的
					this.request.setAttribute("subId", subId);
					this.request.setAttribute("cris" + subId, text.toString());
					this.addFieldToSession("cris" + subId, text.toString());
				}
			}
			if ((mainResult != null && mainResult.size() != 0)
					|| (innerResults != null && innerResults.size() != 0)) {
				this.message = "checkFailure";
				if (!StringUtils.isEmpty(businessId)) {
					// dataDealService.updateRptData(new RptData(tableId,
					// " datastatus = " + DataUtil.JYWTG_STATUS_NUM,
					// businessId, subId, null, true));
					User user = this.getCurrentUser();
					String menuName = "数据录入." + rptTableInfo.getInfoType();
					logManagerService.writeLog(request, user, "0001", menuName,
							"校验", "针对[机构：" + this.instCode + "，单据："
									+ rptTableInfo.getTableName() + "，业务号："
									+ this.businessId + "]执行数据逻辑校验操作", "0");
				}
			} else {
				message = "checkPass";
				User user = this.getCurrentUser();
				if (!StringUtils.isEmpty(businessId)) {
					String menuName = "数据录入." + rptTableInfo.getInfoType();
					logManagerService.writeLog(request, user, "0001", menuName,
							"校验", "针对[机构：" + this.instCode + "，单据："
									+ rptTableInfo.getTableName() + "，业务号："
									+ this.businessId + "]执行数据逻辑校验操作", "1");
					if ("yes".equalsIgnoreCase(this.configOverleapAudit)) {
						// 忽略审核操作
						dataDealService.updateRptData(new RptData(tableId,
								" datastatus = " + DataUtil.SHYTG_STATUS_NUM
										+ ", modifyuser = '" + user.getId()
										+ "' ", businessId, subId, null, true));
						if (fileType != null
								&& TableIdRela.getZjxyMap().get(fileType) != null) {
							// 视情况，若记录为签约信息且无业务编号，则自动用业务ID进行赋值
							RptData rd = new RptData();
							rd.setTableId(tableId);
							rd.setUpdateSql(" businessno = businessid ");
							rd
									.setUpdateCondition(" businessid = '"
											+ businessId
											+ "' and (businessno is null or businessno = '')");
							dataDealService.updateRptData(rd);
						}
						message = "checkPassNoAudit";
					} else {
						if (!"yes".equalsIgnoreCase(this.configOverleapCommit)) {
							// 未忽略提交操作
							dataDealService.updateRptData(new RptData(tableId,
									" datastatus = "
											+ DataUtil.JYYTG_STATUS_NUM,
									businessId, subId, null, true));
						} else {
							// 忽略提交操作(记录最后经办人员为当前登录用户)
							dataDealService.updateRptData(new RptData(tableId,
									" datastatus = "
											+ DataUtil.YTJDSH_STATUS_NUM
											+ ", modifyuser = '" + user.getId()
											+ "' ", businessId, subId, null,
									true));
						}
					}
				}
			}
			this.judgeCannotNext(rptData, tableId);
			// 在wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("rptData", this.rptData);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("rptTableInfo", this.rptTableInfo);
			this.request.setAttribute("beginDate", this.beginDate);
			this.request.setAttribute("endDate", this.endDate);
			log.info("CheckDataAction-checkData-success");
			if (message.equals("checkPass")
					|| message.equals("checkPassNoAudit")) {
				if (!"yes".equalsIgnoreCase(this.configForbidSave)) {
					if (!StringUtils.isEmpty(businessId))
						return OK_UPDATE;
					else
						return OK_NEW;
				} else {
					return SUCCESS;
				}
			} else {
				if (!StringUtils.isEmpty(businessId))
					return FAIL_EDIT;
				else
					return FAIL_CREATE;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("CheckDataAction-checkData", e);
			return ERROR;
		}
	}

	private void setInfoToRequest() {
		this.fromFlag = request.getParameter("fromFlag");
		request.setAttribute("fromFlag", this.fromFlag);
		// 上级报文ID
		this.previousTableId = request.getParameter("previousTableId");
		this.request.setAttribute("previousTableId", previousTableId);
		this.request.setAttribute("rptNo", request.getParameter("cfaRptNo"));
		this.request.setAttribute("businessNo", request
				.getParameter("businessNo"));
		this.request.setAttribute("checkFlag", "1");
	}

	public String checkBusinessNo() {
		String fileType = request.getParameter("fileType");
		String businessId = request.getParameter("businessId");
		// String isHandiwork = request.getParameter("isHandiwork");
		String businessNo = request.getParameter("businessNo");
		String checkReason = "";
		String tableId = DataUtil.getTableIdByFileType(fileType);
		boolean isExists = this.checkBusinessNoRepeat(businessNo, tableId,
				fileType, businessId);
		if (isExists) {
			checkReason = "EXISTS";
		} else {
			checkReason = "";
		}
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(checkReason);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}