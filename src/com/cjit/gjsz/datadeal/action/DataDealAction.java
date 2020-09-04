/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptLogInfo;
import com.cjit.gjsz.datadeal.model.RptStatusCountInfo;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.datadeal.service.CommonService;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.datadeal.util.DataValidater;
import com.cjit.gjsz.dataserch.model.QueryCondition;
import com.cjit.gjsz.filem.core.ReceiveReport;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyService;
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
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;
import com.opensymphony.util.BeanUtils;

/**
 * @author yulubin
 */
public class DataDealAction extends BaseListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3159133403861244225L;
	public static final String VALIDATE_DATA_FAILD_FLAG = "validateDataFailed";
	public static final String SAVE_OR_UPDATE_DATA_SUCCESS_FLAG = "saveOrUpdateDataSuccess";
	public static final String BASE_OBJECT = "baseObject";
	public static final String DECL_OBJECT = "declObject";
	public static final String FINI_OBJECT = "finiObject";
	protected static final String INNER_FLAG = "inner_flag";
	protected final String RELATED = "related";// 返回related的页面
	protected DataDealService dataDealService;
	protected CommonService commonService;
	protected VerifyService verifyService;
	protected UserInterfaceConfigService userInterfaceConfigService;
	protected SearchService searchService;
	protected LogManagerService logManagerService;
	protected CacheManager cacheManager;
	protected RptTableInfo rptTableInfo = new RptTableInfo();
	protected RptData rptData = null;
	// 前台标志位
	protected String saveFlag;
	protected String sbHxFlag = null;
	private static int busFlag = 1;
	private static String timeStamp = "";

	public String getSbHxFlag() {
		return sbHxFlag;
	}

	public void setSbHxFlag(String sbHxFlag) {
		this.sbHxFlag = sbHxFlag;
	}

	protected String beginDate;// 审核起止时间
	protected String endDate;

	public String getBeginDate() {
		if (beginDate == null) {
			// 从session中获取查询起止日期
			String sessionBeginDate = (String) this.request.getSession()
					.getAttribute("beginDate");
			if (sessionBeginDate != null && !"".equals(sessionBeginDate)) {
				beginDate = sessionBeginDate;
			}
		} else {
			request.getSession().setAttribute("beginDate", beginDate);
		}
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		if (endDate == null) {
			// 从session中获取查询起止日期
			String sessionEndDate = (String) this.request.getSession()
					.getAttribute("endDate");
			if (sessionEndDate != null && !"".equals(sessionEndDate)) {
				endDate = sessionEndDate;
			}
		} else {
			request.getSession().setAttribute("endDate", endDate);
		}
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	// 报表数据表最大字段数（该属性的值必须大于等于字段数最多的报表的字段数）
	protected int largestColumnNum;
	// t_rpt_log_info表中最到column列数
	protected int logColumnNum;
	// 基础信息首页面的表格内容
	protected List basicInfoList = new ArrayList();
	// 受权机构列表
	protected List authInstList = new ArrayList();
	// 业务信息集
	protected List busiDataInfoList = new ArrayList();
	// 报表集
	protected List rptTableList = new ArrayList();
	// 报表的列信息集
	protected List rptColumnList = new ArrayList();
	protected List dataStatusList = new ArrayList();
	protected List resetDataStatusList = new ArrayList();
	// 机构号
	protected String instCode;
	// 报表对应的物理表名
	protected String tableId;
	protected String tableIdInner;
	// 信息类型[1：基础信息；2：申报信息；3：核销信息；5：单位基本信息]
	protected String infoTypeCode;
	protected String infoTypeCodeInner;
	// 数据状态
	protected String dataStatus;
	// 业务主键
	protected String businessId;
	// 内嵌表业务主键
	protected String subId;
	// 操作类型
	protected String actionType;
	// 操作描述
	protected String actionDesc;
	// rptno
	protected String rptNo;
	// 批次号
	protected String batchNo;
	protected String innerCreateFlag;
	protected String[] businessIds;
	protected String[] actionTypes;
	protected String[] rptNos;
	protected String verifyMsg;
	protected String message;
	protected String resetDataStatus;
	// 子表保存后是否回到子表编辑页面 yes/no 默认no
	protected String subReturnCreate;
	// 是否联动修改业务编号字段（修改签约信息业务编号后自动修改变动信息业务编号） yes/no 默认no
	protected String linkageUpdateBusinessNo;
	// 通过导入EXCEL新增记录时is_handiwork的默认值设置 默认插入空 可配为Y
	protected String importExcelInsertIsHandiworkValue;
	// 页面来源标志
	protected String fromFlag;
	// 上级报文ID
	protected String previousTableId;
	// 基础信息报文ID
	protected String baseTableId;
	// 其它表业务主键ID
	protected String otherId;
	// 排序字段
	protected String orderColumn;
	// 排序方式
	protected String orderDirection;
	// 排序字段 子表
	protected String orderColumnSub;
	// 排序方式 子表
	protected String orderDirectionSub;
	protected List recordList;
	protected String pageFlag;

	/*
	 * protected String createRptNo(String inst, Date tdate, String tbid, String
	 * actype){ try{ String data = userInterfaceConfigService.createAutokey(new
	 * com.cjit.gjsz.interfacemanager.model.KeyInfo( inst, tdate, tbid,
	 * actype)); return data; }catch (Exception ex){ ex.printStackTrace(); }
	 * return null; }
	 */
	/**
	 * 根据报表ID取rptColumnList,以及所有字段的查询SQL。从SESSION缓存中取
	 */
	protected String getColumnsSql(String tableUniqueId) {
		// String suffixKey = "";
		// if(StringUtil.isNotEmpty(fileType) && tableId.indexOf("_SUB_") < 0){
		// suffixKey = ":" + fileType;
		// }
		Map rptColumnListMap = (HashMap) this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP);
		if (rptColumnListMap != null) {
			rptColumnList = (ArrayList) rptColumnListMap.get(tableUniqueId);
		}
		Map rptColumnSqlMap = (HashMap) this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP);
		if (rptColumnSqlMap == null) {
			return "";
		} else {
			return (String) rptColumnSqlMap.get(tableUniqueId);
		}
	}

	protected void setSqlAboutContractInfo(RptData rptData, String userId) {
		String columns = rptData.getColumns();
		if (StringUtil.isNotEmpty(columns)) {
			columns += ",c.filetype as cFileType ";
			columns += ",c.teamId as cTeamId ";
			if ("AR".equals(this.fileType) || "AS".equals(this.fileType)) {
				rptData.setUserId(userId);
				columns += ",c.debtorcode as cDebtorCode,c.contractamount as cContractAmount,c.valuedate as cValueDate ";
			} else if ("CB".equals(this.fileType)) {
				columns += ",c.debtorname as cDebtorName,c.contractamount as cContractAmount,c.valuedate as cValueDate ";
			} else if ("DB".equals(this.fileType) || "EB".equals(this.fileType)) {
				columns += ",c.debtorname as cDebtorName,c.credconamount as cCredConAmount,c.valuedate as cValueDate ";
			} else if ("FB".equals(this.fileType) || "FC".equals(this.fileType)) {
				columns += ",c.contractamount as cContractAmount ";
			}
			rptData.setColumns(columns);
		}
		if ("BB".equals(this.fileType) || "BC".equals(this.fileType)) {
			// 当前查询BB/BC报文，需关联BA信息
			rptData
					.setJoinTable(" inner join "
							+ tableId
							+ " c on c.businessno = t.businessno and c.filetype = 'BA' ");
		} else if ("FB".equals(this.fileType) || "FC".equals(this.fileType)) {
			// 当前查询FB/FC报文，需关联FA信息
			rptData
					.setJoinTable(" inner join "
							+ tableId
							+ " c on c.businessno = t.businessno and c.filetype = 'FA' ");
		} else {
			rptData.setJoinTable(" inner join " + tableId
					+ " c on c.businessno = t.businessno and c.filetype <> '"
					+ this.fileType + "' ");
		}
	}

	/**
	 * 将列表数据中，存在字典信息的数据用字典中对应的文字替换，用于列表显示
	 * 
	 * @param paginationList
	 * @param tableId
	 * @param rptColumnList
	 */
	protected void setSelectTagValue(List rptDatas, String tableId,
			List rptColumnList) {
		Map dictionaryMap = (Map) SystemCache
				.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP);
		List bussTypeList = null;// 业务类型信息
		if (dictionaryMap != null) {
			Map tableMap = (HashMap) dictionaryMap.get(tableId);
			Map publicMap = (HashMap) dictionaryMap.get("PUBLIC");
			if ((tableMap != null || publicMap != null) && rptDatas != null) {
				// 循环报表数据集，对每条记录，循环其所有字段，若字段tagType为3（下拉框），则将字段值设置为字典表里对应的中文描述显示
				for (Iterator i = rptDatas.iterator(); i.hasNext();) {
					RptData rptData = (RptData) i.next();
					for (Iterator j = rptColumnList.iterator(); j.hasNext();) {
						RptColumnInfo column = (RptColumnInfo) j.next();
						if ("3".equals(column.getTagType())) {
							Object codeValue = BeanUtils.getValue(rptData,
									column.getAliasColumnId());
							String codeName = null;
							if ("BUOCMONTH".equalsIgnoreCase(column
									.getColumnId())) {
								continue;
							} else if ("TEAMID".equalsIgnoreCase(column
									.getColumnId())) {
								if (CollectionUtil.isEmpty(bussTypeList)) {
									User currentUser = (User) this
											.getFieldFromSession(Constants.USER);
									bussTypeList = dataDealService
											.findBussTypeList(currentUser
													.getId());
								}
								for (Iterator k = bussTypeList.iterator(); k
										.hasNext();) {
									Dictionary dict = (Dictionary) k.next();
									if (dict.getValueStandardNum().equals(
											codeValue)) {
										codeName = dict.getName();
										break;
									}
								}
							} else if ("N/A".equalsIgnoreCase(String
									.valueOf(codeValue))) {
								codeName = String.valueOf(codeValue);
							} else {
								if (!DataUtil.isColumnFromJt(column
										.getColumnId(), tableId)) {
									if (tableMap != null) {
										codeName = (String) tableMap.get(column
												.getDictionaryTypeId()
												+ "_" + codeValue);
									}
									if (StringUtil.isEmpty(codeName)
											&& publicMap != null) {
										codeName = (String) publicMap
												.get(column
														.getDictionaryTypeId()
														+ "_" + codeValue);
									}
								} else {
									Map JcTableMap = (HashMap) dictionaryMap
											.get(TableIdRela.getJcsyMap().get(
													tableId));
									codeName = (String) JcTableMap.get(column
											.getDictionaryTypeId()
											+ "_" + codeValue);
								}
							}
							BeanUtils.setValue(rptData, column
									.getAliasColumnId(), codeName == null ? ""
									: codeName);
						}
						if ("AR".equals(rptData.getFileType())
								|| "AS".equals(rptData.getFileType())) {
							if ("BUSINESSNO".equals(column.getColumnId())) {
								Object oBussNo = BeanUtils.getValue(rptData,
										column.getAliasColumnId());
								if (oBussNo != null) {
									rptData.setBusinessNo(String
											.valueOf(oBussNo));
								}
							} else if ("EXDEBTCODE"
									.equals(column.getColumnId())) {
								Object oExdebtCode = BeanUtils.getValue(
										rptData, column.getAliasColumnId());
								if (oExdebtCode != null) {
									rptData.setRptNo(String
											.valueOf(oExdebtCode));
								}
							}
						}
					}
					// 对于非签约信息，查找对应签约信息的业务团队名称
					if (this.relatedFileType != null
							&& rptData.getFileType() != null
							&& this.relatedFileType.indexOf(rptData
									.getFileType()) >= 0
							&& StringUtil.isNotEmpty(rptData.getCTeamId())) {
						if (CollectionUtil.isEmpty(bussTypeList)) {
							User currentUser = (User) this
									.getFieldFromSession(Constants.USER);
							bussTypeList = dataDealService
									.findBussTypeList(currentUser.getId());
						}
						for (Iterator k = bussTypeList.iterator(); k.hasNext();) {
							Dictionary dict = (Dictionary) k.next();
							if (dict.getValueStandardNum().equals(
									rptData.getCTeamId())) {
								rptData.setCTeamIdName(dict.getName());
								break;
							}
						}
					}
					// 判断单据状态是否为未校验
					if (String.valueOf(DataUtil.WJY_STATUS_NUM).equals(
							rptData.getDataStatus())) {
						RptData rd = new RptData();
						rd.setBusinessId(rptData.getBusinessId());
						rd.setTableId(tableId);
						String lowerStatusReasion = dataDealService
								.getRefuseCheckInfo(rd, "2");
						if (StringUtil.isNotEmpty(lowerStatusReasion)) {
							rptData.setDataStatusDesc("（打回）");
						}
					}
					// 判断单据状态是否为已生成
					else if (String.valueOf(DataUtil.YSC_STATUS_NUM).equals(
							rptData.getDataStatus())
							&& "1".equals(this.busiDataType)
							&& !"1".equals(rptData.getFileType())) {
						String keyRptNoColumnId = DataUtil
								.getRptNoColumnIdByFileType(rptData
										.getFileType());
						String byeRptNoColumnId = DataUtil
								.getByeRptNoColumnIdByFileType(rptData
										.getFileType());
						RptData rd = new RptData();
						rd.setTableId(tableId);
						rd.setBusinessId(rptData.getBusinessId());
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
							if (receiveReport != null
									&& receiveReport.getId() > 0
									&& "0".equals(receiveReport.getHasReject())) {
								rptData.setDataStatusDesc("（反馈错误）");
								rptData.setErrorDesc(receiveReport
										.getErrorMemo());
							}
						}
					}
					// 查询显示外债变动、余额信息对应的签约信息类型
					if (("AR".equals(rptData.getFileType()) || "AS"
							.equals(rptData.getFileType()))) {
						String fileTypeName = null;
						if (StringUtil.isNotEmpty(rptData.getCFileType())) {
							fileTypeName = DataUtil
									.getTableNameByFileType(rptData
											.getCFileType());
						} else if (StringUtil.isNotEmpty(rptData
								.getBusinessNo())
								|| StringUtil.isNotEmpty(rptData.getRptNo())) {
							RptData rd = dataDealService
									.findRptDataByRptNoAndBusinessNo(
											"T_CFA_A_EXDEBT", rptData
													.getFileType(), rptData
													.getRptNo(), rptData
													.getBusinessNo());
							if (rd != null && rd.getFileType() != null) {
								fileTypeName = DataUtil
										.getTableNameByFileType(rd
												.getFileType());
							}
						}
						if (fileTypeName != null
								&& fileTypeName.indexOf("-") > 0) {
							rptData.setFileTypeDesc(fileTypeName.substring(0,
									fileTypeName.indexOf("-")));
						}
					}
				}
			}
		}
	}

	protected boolean sessionInit(boolean getBusinessIdFromSession) {
		// 受权机构列表
		try {
			getAuthInstList(authInstList);
		} catch (java.lang.NullPointerException npex) {
			return false;
		}
		if (CollectionUtil.isEmpty(authInstList)) {
			return false;
		}
		// 部分信息若在值栈里找不到，则从SESSION中取
		if (infoTypeCode == null) {
			infoTypeCode = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_INFO_TYPE_CODE);
		}
		if (infoTypeCodeInner == null) {
			infoTypeCodeInner = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_INFO_TYPE_CODE_INNER);
		}
		// 校验过来永远不用从session获得businessId
		if (businessId == null && getBusinessIdFromSession
				&& this.request.getAttribute("checkFlag") == null) {
			businessId = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_BUSINESS_ID);
		}
		if (subId == null && getBusinessIdFromSession) {
			subId = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_SUB_ID);
		}
		if (tableId == null) {
			tableId = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_TABLE_ID);
		}
		if (tableIdInner == null) {
			tableIdInner = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_TABLE_ID_INNER);
		}
		if (instCode == null) {
			instCode = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_INST_CODE);
			if (instCode == null || "".equals(instCode)) {
				instCode = ((Organization) authInstList.get(0)).getId();
			}
		}
		// if(searchLowerOrg == null){
		// searchLowerOrg = (String) this
		// .getFieldFromSession(ScopeConstants.CURRENT_SEARCH_LOWER_ORG);
		// }
		if (busiDataType == null || "".equals(busiDataType)) {
			busiDataType = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_BUSIDATATYPE);
		}
		if (infoType == null || "".equals(infoType)) {
			infoType = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_INFOTYPE);
		}
		if (fileType == null || "".equals(fileType)) {
			fileType = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_FILETYPE);
		}
		// 把部分信息放在session中
		if (!"".equalsIgnoreCase(businessId)) {
			this.addFieldToSession(ScopeConstants.CURRENT_BUSINESS_ID,
					businessId);
			this.addFieldToSession(ScopeConstants.CURRENT_BUSINESS_ID2,
					businessId);
		}
		this.addFieldToSession(ScopeConstants.CURRENT_SUB_ID, subId);
		this.addFieldToSession(ScopeConstants.CURRENT_TABLE_ID, tableId);
		this.addFieldToSession(ScopeConstants.CURRENT_TABLE_ID_INNER,
				tableIdInner);
		this.addFieldToSession(ScopeConstants.CURRENT_INST_CODE, instCode);
		// this.addFieldToSession(ScopeConstants.CURRENT_SEARCH_LOWER_ORG,
		// searchLowerOrg);
		this.addFieldToSession(ScopeConstants.CURRENT_INFO_TYPE_CODE,
				infoTypeCode);
		this.addFieldToSession(ScopeConstants.CURRENT_INFO_TYPE_CODE_INNER,
				infoTypeCodeInner);
		this.addFieldToSession(ScopeConstants.CURRENT_BUSIDATATYPE,
				busiDataType);
		this.addFieldToSession(ScopeConstants.CURRENT_INFOTYPE, infoType);
		this.addFieldToSession(ScopeConstants.CURRENT_FILETYPE, fileType);
		// 把报表列信息和查询SQL放在SESSION中
		if (this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP) == null) {
			Map[] map = dataDealService
					.initRptColumnSqlMapNew(largestColumnNum);
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP, map[0]);
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP, map[1]);
		}
		// 将字典信息放在SESSION中
		// if(this.getFieldFromSession(ScopeConstants.SESSION_DICTIONARY_MAP) ==
		// null){
		// Map[] map = userInterfaceConfigService.initDictionaryMap();
		// this.addFieldToSession(ScopeConstants.SESSION_DICTIONARY_MAP,
		// map[0]);
		// this.addFieldToSession(
		// ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE, map[1]);
		// }
		// 将参数配置项记录信息放在SESSION中
		this.initConfigParameters();
		return true;
	}

	// 生成业务主键（在插入数据时需要，由时间戳生成）
	protected String createBusinessId() {
		String temp = DateUtils.serverCurrentTimeStamp();
		if (!timeStamp.equals(temp)) {
			timeStamp = temp;
			busFlag = 1;
			return timeStamp + busFlag++;
		} else
			return temp + busFlag++;
	}

	// 获取当前用户拥有权限的机构集
	protected void getAuthInstList(List authInstList) {
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		if (currentUser != null) {
			if (authInstList == null) {
				authInstList = new ArrayList();
			}
			authInstList.addAll(currentUser.getOrgs());
		} else {
			throw new java.lang.NullPointerException();
		}
	}

	/**
	 * <p>
	 * 方法名称: validateDataForEditView|描述:
	 * 在进入编辑或展示界面时，针对状态为校验未通过的记录重新校验，以便在界面展示错误字段信息
	 * </p>
	 * 
	 * @param rptColumnList
	 * @param rptData
	 * @param tableId
	 * @param fileType
	 * @throws Exception
	 */
	protected void validateDataForEditView(List rptColumnList, RptData rptData,
			String tableId, String fileType) throws Exception {
		Object reportData = null;
		/** FAL * */
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
		/** CFA * */
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
		if (StringUtil.isNotEmpty(rptData.getBusinessId())) {
			org.apache.commons.beanutils.BeanUtils.setProperty(reportData,
					"businessid", rptData.getBusinessId());
		}
		if (StringUtil.isNotEmpty(rptData.getInstCode())) {
			org.apache.commons.beanutils.BeanUtils.setProperty(reportData,
					"instcode", rptData.getInstCode());
		}
		if (StringUtil.isNotEmpty(fileType)) {
			org.apache.commons.beanutils.BeanUtils.setProperty(reportData,
					"filetype", fileType);
		}
		boolean result = true;// 数据预校验结果
		// 数据属性校验
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			if ("table".equals(column.getDataType())) {
				continue;
			}
			StringBuffer sb = new StringBuffer();
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
					dataType, sb, false, column);
			column.setDataTypeVSuccess(dataTypeVSuccess);
			column.setDataTypeVDesc(sb.toString());
			// 只要出现了数据属性校验错误，则不进行逻辑规则校验
			if (!dataTypeVSuccess) {
				result = false;
			}
			// 当前字段无预校验错误且有值
			if (result && !StringUtils.isEmpty(cData)) {
				org.apache.commons.beanutils.BeanUtils.setProperty(reportData,
						column.getColumnId().toLowerCase(), cData);
			}
		}
		// 无预校验错误则执行逻辑校验
		if (result && reportData != null) {
			VerifyService verifyService = (VerifyService) SpringContextUtil
					.getBean("verifyService");
			VerifyModel vm = verifyService.verify(reportData, tableId,
					instCode, interfaceVer, configIsCluster);
			Map mainResult = null;
			List innerResults = null;
			if (vm != null) {
				mainResult = vm.getFatcher();
				innerResults = vm.getChildren();
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
			}
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
					// 新加的
					this.request.setAttribute("subId", subId);
					this.request.setAttribute("cris" + subId, text.toString());
					this.addFieldToSession("cris" + subId, text.toString());
				}
			}
		}
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
	protected boolean validateData(List rptColumnList, RptData rptData,
			boolean isSkipBlanks) throws Exception {
		log.info("DataDealAction-validateData");
		return dataDealService.validateData(rptColumnList, rptData,
				isSkipBlanks);
	}

	/**
	 * <p>
	 * 方法名称: checkBusinessNoRepeat|描述:
	 * </p>
	 * 
	 * @param businessNo
	 * @param tableId
	 * @param fileType
	 * @param businessId
	 * @return
	 */
	protected boolean checkBusinessNoRepeat(String businessNo, String tableId,
			String fileType, String businessId) {
		log.info("DataDealAction-checkBusinessNoRepeat");
		return this.getVerifyService().checkBusinessNoRepeat(businessNo,
				tableId, fileType, businessId);
	}

	/**
	 * 获取：AR-变动编号/AS-变动编号/BC-履约编号/CB-变动编号/DB-变动编号/EB-变动编号/FB-终止支付编号/FC-付息编号
	 */
	// protected String getIndexCodeForSelf(String tableId, String fileType,
	// String columnId, String rptNo, String businessNo){
	// String indexCode = "0001";
	// if(StringUtil.isNotEmpty(tableId) && StringUtil.isNotEmpty(fileType)
	// && StringUtil.isNotEmpty(columnId)
	// && StringUtil.isNotEmpty(businessNo)){
	// StringBuffer sbSearchCondition = new StringBuffer();
	// sbSearchCondition.append(" fileType = '").append(fileType).append(
	// "' and businessNo = '").append(businessNo).append("' ");
	// if(StringUtil.isNotEmpty(rptNo)){
	// String rptNoColumnId = DataUtil
	// .getRptNoColumnIdByFileType(fileType);
	// sbSearchCondition.append(" and ").append(rptNoColumnId).append(
	// " = '").append(rptNo).append("' ");
	// }
	// RptData rptData = new RptData();
	// rptData.setTableId(tableId);
	// rptData.setColumnId(columnId);
	// rptData.setSearchCondition(sbSearchCondition.toString());
	// String maxIndexCode = dataDealService.findMaxIndexCode(rptData);
	// if(StringUtil.isNumLegal(maxIndexCode)){
	// int nIndex = Integer.valueOf(maxIndexCode).intValue() + 1;
	// if(nIndex < 10){
	// indexCode = "000" + String.valueOf(nIndex);
	// }else if(nIndex < 100){
	// indexCode = "00" + String.valueOf(nIndex);
	// }else if(nIndex < 1000){
	// indexCode = "0" + String.valueOf(nIndex);
	// }else if(nIndex < 10000){
	// indexCode = String.valueOf(nIndex);
	// }else{
	// return null;
	// }
	// }
	// }
	// return indexCode;
	// }
	public List getBasicInfoList() {
		return basicInfoList;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public RptTableInfo getRptTableInfo() {
		return rptTableInfo;
	}

	public void setRptTableInfo(RptTableInfo rptTableInfo) {
		this.rptTableInfo = rptTableInfo;
	}

	public DataDealService getDataDealService() {
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService) {
		this.dataDealService = dataDealService;
	}

	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}

	public List getAuthInstList() {
		return authInstList;
	}

	public String getInfoTypeCode() {
		return infoTypeCode;
	}

	public void setInfoTypeCode(String infoTypeCode) {
		this.infoTypeCode = infoTypeCode;
	}

	public List getRptColumnList() {
		return rptColumnList;
	}

	public void setRptColumnList(List rptColumnList) {
		this.rptColumnList = rptColumnList;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public RptData getRptData() {
		return rptData;
	}

	public void setRptData(RptData rptData) {
		this.rptData = rptData;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public int getLargestColumnNum() {
		return largestColumnNum;
	}

	public void setLargestColumnNum(int largestColumnNum) {
		this.largestColumnNum = largestColumnNum;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public List getBusiDataInfoList() {
		return busiDataInfoList;
	}

	public void setBusiDataInfoList(List busiDataInfoList) {
		this.busiDataInfoList = busiDataInfoList;
	}

	public List getRptTableList() {
		return rptTableList;
	}

	public void setRptTableList(List rptTableList) {
		this.rptTableList = rptTableList;
	}

	public String getInnerCreateFlag() {
		return innerCreateFlag;
	}

	public void setInnerCreateFlag(String innerCreateFlag) {
		this.innerCreateFlag = innerCreateFlag;
	}

	public VerifyService getVerifyService() {
		if (verifyService == null) {
			return (VerifyService) SpringContextUtil.getBean("verifyService");
		} else
			return verifyService;
	}

	public void setVerifyService(VerifyService verifyService) {
		this.verifyService = verifyService;
	}

	public String[] getBusinessIds() {
		return businessIds;
	}

	public void setBusinessIds(String[] businessIds) {
		this.businessIds = businessIds;
	}

	public String getVerifyMsg() {
		return verifyMsg;
	}

	public void setVerifyMsg(String verifyMsg) {
		this.verifyMsg = verifyMsg;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getResetDataStatus() {
		return resetDataStatus;
	}

	public void setResetDataStatus(String resetDataStatus) {
		this.resetDataStatus = resetDataStatus;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public List getDataStatusList() {
		return dataStatusList;
	}

	public void setDataStatusList(List dataStatusList) {
		this.dataStatusList = dataStatusList;
	}

	public List getResetDataStatusList() {
		return resetDataStatusList;
	}

	public void setResetDataStatusList(List resetDataStatusList) {
		this.resetDataStatusList = resetDataStatusList;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public String getTableIdInner() {
		return tableIdInner;
	}

	public void setTableIdInner(String tableIdInner) {
		this.tableIdInner = tableIdInner;
	}

	public String getInfoTypeCodeInner() {
		return infoTypeCodeInner;
	}

	public void setInfoTypeCodeInner(String infoTypeCodeInner) {
		this.infoTypeCodeInner = infoTypeCodeInner;
	}

	public List getRecordList() {
		return recordList;
	}

	public void setRecordList(List recordList) {
		this.recordList = recordList;
	}

	public String getRptNo() {
		return rptNo;
	}

	public void setRptNo(String rptNo) {
		this.rptNo = rptNo;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	public String getSaveFlag() {
		return saveFlag;
	}

	public void setSaveFlag(String saveFlag) {
		this.saveFlag = saveFlag;
	}

	public int getLogColumnNum() {
		return logColumnNum;
	}

	public void setLogColumnNum(int logColumnNum) {
		this.logColumnNum = logColumnNum;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	// 生成业务主键（在插入数据时需要，由时间戳生成）
	protected String createSubId(String serverTime, int nIndex) {
		if (serverTime != null && !"".equals(serverTime) && nIndex >= 0) {
			return String.valueOf(serverTime + nIndex);
		} else {
			return createBusinessId();
		}
	}

	/**
	 * <p>
	 * 方法名称: judgeCannotNext|描述: 判断是否可点击下一步
	 * </p>
	 * 
	 * @param rptData
	 * @param tableId
	 */
	protected void judgeCannotNext(RptData rptData, String tableId) {
		log.info("DataDealAction-judgeCannotNext");
		if (rptData != null) {
			rptData.setCanNext(false);
		}
	}

	protected void initConfigParameters() {
		if ("yes".equalsIgnoreCase(this.configIsCluster)) {
			configMap = userInterfaceConfigService.initConfigParameters();
			this.addFieldToSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
					configMap);
		} else {
			CacheabledMap cache = (CacheabledMap) CacheManager
					.getCacheObject("paramCache");
			if (cache != null) {
				configMap = (Map) cache.get("configMap");
				this.addFieldToSession(
						ScopeConstants.SESSION_CONFIG_PARAMETER_MAP, configMap);
			} else {
				configMap = (HashMap) this
						.getFieldFromSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP);
				if (configMap == null) {
					configMap = userInterfaceConfigService
							.initConfigParameters();
					this.addFieldToSession(
							ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
							configMap);
				}
			}
		}
		this.setConfigParametersBaseList(configMap);
		this.setConfigParametersDataDeal(configMap);
		this.request.setAttribute("interfaceVer", this.interfaceVer);
	}

	protected void setConfigParametersDataDeal(Map map) {
		if (map != null) {
			// ALL
			// 报关单/核销单 子表保存后是否回到子表编辑页面 yes/no 默认no
			String subReturnCreate = (String) map
					.get("config.sub.return.create");
			if (StringUtils.isNotEmpty(subReturnCreate)) {
				this.subReturnCreate = subReturnCreate;
			}
			// 是否联动修改业务编号字段（修改签约信息业务编号后自动修改变动信息业务编号） yes/no 默认no
			String linkageUpdateBusinessNo = (String) map
					.get("config.updateBusinessNo.linkage");
			if (StringUtils.isNotEmpty(linkageUpdateBusinessNo)) {
				this.linkageUpdateBusinessNo = linkageUpdateBusinessNo;
			}
			// 通过导入EXCEL新增记录时is_handiwork的默认值设置 默认插入空 可配为Y
			String importExcelInsertIsHandiworkValue = (String) map
					.get("config.importExcel.insert.isHandiwork.value");
			if (StringUtils.isNotEmpty(importExcelInsertIsHandiworkValue)) {
				this.importExcelInsertIsHandiworkValue = importExcelInsertIsHandiworkValue;
			}
		}
	}

	/**
	 * <p>
	 * 方法名称: checkCusTypeChange|描述: <br>
	 * 对比当前数据的CUSTYPE字段值和上次成功报送时的CUSTYPE字段值是否相同<br>
	 * 若对公/对私发生变化，判断操作类型是否为删除<br>
	 * 如果不是删除，则校验不通过。否则校验通过
	 * </p>
	 * 
	 * @param tableId
	 * @param businessId
	 * @param cusType
	 *            当前收付汇款人类型
	 * @param actionType
	 *            操作类型
	 * @param sbMessage
	 */
	protected void checkCusTypeChange(String tableId, String businessId,
			String cusType, String actionType, StringBuffer sbMessage) {
		RptLogInfo rptLogInfo = new RptLogInfo();
		rptLogInfo.setTableid(tableId);
		rptLogInfo.setBusinessid(businessId);
		rptLogInfo.setDatastatus(String.valueOf(DataUtil.YBS_STATUS_NUM));
		List logList = dataDealService.findRptLogInfo(rptLogInfo, null);
		if (logList != null && logList.size() > 0) {
			rptLogInfo = (RptLogInfo) logList.get(0);
			if (rptLogInfo != null) {
				String sendCusType = rptLogInfo.getColumn11();
				if (("C".equals(sendCusType) && !"C".equals(cusType))
						|| (!"C".equals(sendCusType) && "C".equals(cusType))) {
					// 对公/对私发生改变
					if (!"D".equals(actionType)) {
						// 操作类型不为删除
						sbMessage.append("当对公/对私发生改变时，必需报送删除操作！");
					}
				}
			}
		}
	}

	protected List getListDesc(String tb, String inst, String searchLowerOrg,
			String fileType, String showDataStatus, String userId,
			List rptDataStatusList, String linkBussType) {
		List result = new ArrayList();
		List lst = null;
		if ("AR".equalsIgnoreCase(fileType) || "AS".equalsIgnoreCase(fileType)
				|| "1".equalsIgnoreCase(fileType)) {
			lst = dataDealService.findRptDataStatusCountByTableIdAndInstCode(
					tb, inst, searchLowerOrg, fileType, userId, linkBussType);
		} else {
			lst = new ArrayList();
			if (CollectionUtil.isNotEmpty(rptDataStatusList)) {
				for (Iterator i = rptDataStatusList.iterator(); i.hasNext();) {
					RptStatusCountInfo m = (RptStatusCountInfo) i.next();
					if (fileType.equalsIgnoreCase(m.getFileType())) {
						lst.add(m);
					}
				}
			}
		}
		for (Iterator i = lst.iterator(); i.hasNext();) {
			RptStatusCountInfo m = (RptStatusCountInfo) i.next();
			int nDataStatus = m.getDataStatus();
			String sDataStatus = Integer.toString(nDataStatus);
			if (StringUtil.isNotEmpty(showDataStatus)
					&& showDataStatus.indexOf(sDataStatus) >= 0) {
				switch (nDataStatus) {
				// 1 未校验
				case DataUtil.WJY_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.WJY_STATUS_CH)
							.append(m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 2 校验未通过
				case DataUtil.JYWTG_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.JYWTG_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 3 校验已通过
				case DataUtil.JYYTG_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.JYYTG_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 4 已提交待审核
				case DataUtil.YTJDSH_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.YTJDSH_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 5 审核未通过
				case DataUtil.SHWTG_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.SHWTG_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 6 审核已通过
				case DataUtil.SHYTG_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.SHYTG_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 7 已生成
				case DataUtil.YSC_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.YSC_STATUS_CH)
							.append(m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 8 已报送
				case DataUtil.YBS_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.YBS_STATUS_CH)
							.append(m.getCount()).append("条").append(";")
							.toString()));
					break;
				default:
					break;
				}
			} else {
				continue;
			}
		}
		return result;
	}

	/**
	 * <p>
	 * 方法名称: saveDeleteLog|描述: 添加删除记录
	 * </p>
	 * 
	 * @param tableId
	 *            所要删除表的表名（主表或子表）
	 * @param fileType
	 *            业务类型
	 * @param businessId
	 *            主表ID
	 * @param businessNo
	 *            业务编号
	 * @param isMain
	 *            是否在删除主表
	 */
	protected void saveDeleteLog(String tableId, String fileType,
			String businessId, String businessNo, boolean isMain) {
		try {
			this.saveRptDataLog("delete", tableId, fileType, businessId,
					businessNo, isMain);
		} catch (Exception ex) {
			log.error("DeleteDatasAction-saveDeleteLog:" + tableId + "-"
					+ fileType + "-" + businessId, ex);
			ex.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法名称: saveRptDataLog|描述: 添加记录日志
	 * </p>
	 * 
	 * @param tableId
	 *            表名（主表或子表）
	 * @param fileType
	 *            业务类型
	 * @param businessId
	 *            主表ID
	 * @param businessNo
	 *            业务编号
	 * @param isMain
	 *            记录的是否为主表
	 */
	protected void saveRptDataLog(String logType, String tableId,
			String fileType, String businessId, String businessNo,
			boolean isMain) {
		try {
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			// 
			rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							fileType));
			// 当前更新时间
			String updateTime = DateUtils.serverCurrentTimeStamp();
			// 记录子表ID
			List subTableIds = new ArrayList();
			// 循环列信息，作各种处理
			StringBuffer columns = new StringBuffer();
			columns.append("'").append(logType).append("' as logtype,'")
					.append(tableId).append("' as tableid,'").append(fileType)
					.append("' as filetype,'").append(currentUser.getId())
					.append("' as userid,'").append(updateTime).append(
							"' as updatetime, t.businessid as businessid");
			if (!"1".equals(fileType) && !"SUB".equals(fileType)
					&& !"".equals(fileType)) {
				columns.append(",t.businessno as businessno");
			}
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				// 根据字段物理名和别名拼查询SQL
				if (StringUtil.isNotEmpty(column.getLogColumnId())) {
					columns.append(", t.").append(column.getColumnId()).append(
							" as ").append(column.getLogColumnId());
				}
				if ("table".equalsIgnoreCase(column.getDataType())
						&& subTableIds != null) {
					subTableIds.add(column.getColumnId());
				}
			}
			if (isMain) {
				// 删除的是主表
				columns.append(",'' as subid,t.datastatus as datastatus");
			} else {
				// 删除的是子表
				columns.append(",t.subid as subid,'0' as datastatus");
			}
			RptData rptDataTemp = new RptData();
			rptDataTemp.setTableId(tableId);
			rptDataTemp.setFileType(fileType);
			rptDataTemp.setColumns(columns.toString());
			rptDataTemp.setBusinessId(businessId);
			rptDataTemp.setBusinessNo(businessNo);
			// 根据物理表名，机构号，业务主键等获取数据
			List rptDataList = dataDealService
					.findRptDataToLogInfo(rptDataTemp);
			if (rptDataList != null && rptDataList.size() > 0) {
				for (int i = 0; i < rptDataList.size(); i++) {
					RptLogInfo rptLogInfo = (RptLogInfo) rptDataList.get(i);
					if (rptLogInfo != null) {
						dataDealService.insertRptLogInfo(rptLogInfo);
						// 判断是否包含子表
						if (subTableIds != null && subTableIds.size() > 0
								&& "datalower".equals(logType)) {
							for (int s = 0; s < subTableIds.size(); s++) {
								// 子表ID
								String subTableId = (String) subTableIds.get(s);
								this.saveInnerRptDataLog(subTableId,
										businessId, null, logType, updateTime);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			log.error("DataDealAction-saveRptDataLog:" + tableId + "-"
					+ fileType + "-" + businessId, ex);
			ex.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法名称: saveInnerRptDataLog|描述: 添加删除记录
	 * </p>
	 * 
	 * @param innerTableId
	 *            所要记录的子表表名
	 * @param businessId
	 *            主表ID
	 * @param subId
	 *            子表ID
	 * @param logType
	 *            操作类型
	 * @param updateTime
	 *            修改时间
	 */
	protected void saveInnerRptDataLog(String innerTableId, String businessId,
			String subId, String logType, String updateTime) {
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		// 查询获取子表列信息
		rptColumnList = dataDealService.findRptColumnInfo(new RptColumnInfo(
				innerTableId, null, "1", "SUB"));
		// 循环列信息，作各种处理
		StringBuffer columns = new StringBuffer();
		columns.append("'").append(logType).append("' as logtype,'").append(
				innerTableId).append("' as tableid,'").append(
				currentUser.getId()).append("' as userid,'").append(updateTime)
				.append("' as updatetime,'").append(businessId).append(
						"' as businessid,");
		if (StringUtil.isNotEmpty(subId)) {
			columns.append("'").append(subId).append("' as subid");
		} else {
			columns.append(" subid as subid");
		}
		if ("delete".equalsIgnoreCase(logType)) {
			columns.append(",'0' as datastatus");
		} else {
			columns.append(",'' as datastatus");
		}
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			// 根据字段物理名和别名拼查询SQL
			if (column.getColumnId() != null && column.getLogColumnId() != null) {
				columns.append(", t.").append(column.getColumnId()).append(
						" as ").append(column.getLogColumnId().toLowerCase());
			}
		}
		for (int i = 1; i <= logColumnNum; i++) {
			String columnId = "column";
			if (i < 10) {
				columnId = columnId + "0" + i;
			} else {
				columnId = columnId + i;
			}
			if (columns.toString().toLowerCase().indexOf(columnId) < 0) {
				columns.append(",'' as ").append(columnId);
			}
		}
		String strColumns = columns.toString();
		if (strColumns.toLowerCase().indexOf("columnm01") < 0) {
			strColumns += ", '' as columnm01 ";
		}
		if (strColumns.toLowerCase().indexOf("columnm02") < 0) {
			strColumns += ", '' as columnm02 ";
		}
		if (strColumns.toLowerCase().indexOf("columnm03") < 0) {
			strColumns += ", '' as columnm03 ";
		}
		if (strColumns.toLowerCase().indexOf("columnm04") < 0) {
			strColumns += ", '' as columnm04 ";
		}
		if (strColumns.toLowerCase().indexOf("columnm05") < 0) {
			strColumns += ", '' as columnm05 ";
		}
		if (strColumns.toLowerCase().indexOf("columnm11") < 0) {
			strColumns += ", '' as columnm11 ";
		}
		if (strColumns.toLowerCase().indexOf("columnm12") < 0) {
			strColumns += ", '' as columnm12 ";
		}
		if (strColumns.toLowerCase().indexOf("columnm13") < 0) {
			strColumns += ", '' as columnm13 ";
		}
		if (strColumns.toLowerCase().indexOf("columnm14") < 0) {
			strColumns += ", '' as columnm14 ";
		}
		if (strColumns.toLowerCase().indexOf("columnm15") < 0) {
			strColumns += ", '' as columnm15 ";
		}
		RptData rptDataTemp = new RptData();
		rptDataTemp.setTableId(innerTableId);
		rptDataTemp.setColumns(strColumns);
		rptDataTemp.setBusinessId(businessId);
		rptDataTemp.setSubId(subId);
		System.out.println(columns.toString());
		// 根据物理表名，机构号，业务主键等获取数据
		List rptDataList = dataDealService.findRptDataToLogInfo(rptDataTemp);
		if (rptDataList != null && rptDataList.size() > 0) {
			for (int i = 0; i < rptDataList.size(); i++) {
				RptLogInfo rptLogInfo = (RptLogInfo) rptDataList.get(i);
				if (rptLogInfo != null) {
					dataDealService.insertRptLogInfo(rptLogInfo);
				}
			}
		}
	}

	protected void deleteXyRptData(String tableId, String fileType,
			String businessId, String xyFileType, String updateSql) {
		// 查询删除表单的业务编号
		String businessNo = dataDealService.findBusinessNoByBusinessId(tableId,
				fileType, businessId);
		if (StringUtil.isEmpty(businessNo)) {
			return;
		}
		String[] xyFileTypes = new String[1];
		if (xyFileType.indexOf(",") > 0 && xyFileType.length() > 3) {
			xyFileTypes = xyFileType.split(",");
		} else {
			xyFileTypes = new String[] { xyFileType };
		}
		for (int x = 0; x < xyFileTypes.length; x++) {
			String updateCondition = " filetype = '" + xyFileTypes[x]
					+ "' and businessNo = '" + businessNo + "' ";
			RptData upRptData = new RptData(tableId, updateSql, null, null,
					null, true);
			upRptData.setUpdateCondition(updateCondition);
			upRptData.setNotDataStatus(String.valueOf(DataUtil.YBS_STATUS_NUM)
					+ "," + String.valueOf(DataUtil.LOCKED_STATUS_NUM) + ","
					+ String.valueOf(DataUtil.DELETE_STATUS_NUM));
			saveDeleteLog(tableId, xyFileTypes[x], null, businessNo, true);
			dataDealService.updateRptData(upRptData);
		}
	}

	public static final int TYPE_MORE = 1; // 大于
	public static final int TYPE_LESS = 2; // 小于
	public static final int TYPE_EQUAL = 3; // 等于
	public static final int TYPE_LIKE = 4; // like
	public static final int TYPE_MORE_EQUAL = 5;// 大于等于
	public static final int TYPE_LESS_EQUAL = 6;// 小于等于
	protected static Map operatorsMap;
	static {
		operatorsMap = new LinkedMap();
		operatorsMap.put(new Integer(TYPE_LIKE), "like");
		operatorsMap.put(new Integer(TYPE_EQUAL), "=");
		operatorsMap.put(new Integer(TYPE_MORE), ">");
		operatorsMap.put(new Integer(TYPE_LESS), "<");
		operatorsMap.put(new Integer(TYPE_MORE_EQUAL), ">=");
		operatorsMap.put(new Integer(TYPE_LESS_EQUAL), "<=");
	}
	protected QueryCondition queryCondition; // 查询条件

	public QueryCondition getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(QueryCondition queryCondition) {
		this.queryCondition = queryCondition;
	}

	protected String createSqlCondition(QueryCondition qc, String bDate,
			String eDate, String userId) {
		String returnStr = "";
		StringBuffer sb = new StringBuffer();
		if (qc != null) {
			if (qc.getValueFirst() != null) {
				qc.setValueFirst(qc.getValueFirst().trim());
				if (!StringUtil.isEmpty(qc.getValueFirst())) {
					// 先判断该列是否是字典项
					if (qc.getColumnIdFirst().startsWith("common.")) {
						sb.append(getCommonSql(qc.getColumnIdFirst(), qc
								.getOpFirst(), qc.getValueFirst()));
					} else {
						if (!CONTRACTTYPE.equals(qc.getColumnIdFirst())) {
							if (checkDic(qc.getColumnIdFirst())) {
								sb.append(getDicSql(qc.getColumnIdFirst(), qc
										.getOpFirst(), qc.getValueFirst()));
							} else {
								sb.append(getNoDicSql(qc.getColumnIdFirst(), qc
										.getOpFirst(), qc.getValueFirst()));
							}
						} else {
							sb.append(getContractTypeSQL((String) operatorsMap
									.get(new Integer(qc.getOpFirst())), qc
									.getValueFirst()));
						}
					}
				}
			}
			if (qc.getValueSecond() != null) {
				qc.setValueSecond(qc.getValueSecond().trim());
				if (!StringUtil.isEmpty(qc.getValueSecond())) {
					if (qc.getColumnIdSecond().startsWith("common.")) {
						sb.append(getCommonSql(qc.getColumnIdSecond(), qc
								.getOpSecond(), qc.getValueSecond()));
					} else {
						if (!CONTRACTTYPE.equals(qc.getColumnIdSecond())) {
							if (checkDic(qc.getColumnIdSecond())) {
								sb.append(getDicSql(qc.getColumnIdSecond(), qc
										.getOpSecond(), qc.getValueSecond()));
							} else {
								sb.append(getNoDicSql(qc.getColumnIdSecond(),
										qc.getOpSecond(), qc.getValueSecond()));
							}
						} else {
							sb.append(getContractTypeSQL((String) operatorsMap
									.get(new Integer(qc.getOpSecond())), qc
									.getValueSecond()));
						}
					}
				}
			}
			if (qc.getValueThird() != null) {
				qc.setValueThird(qc.getValueThird().trim());
				if (!StringUtil.isEmpty(qc.getValueThird())) {
					if (qc.getColumnIdThird().startsWith("common.")) {
						sb.append(getCommonSql(qc.getColumnIdThird(), qc
								.getOpThird(), qc.getValueThird()));
					} else {
						if (!CONTRACTTYPE.equals(qc.getColumnIdThird())) {
							if (checkDic(qc.getColumnIdThird())) {
								sb.append(getDicSql(qc.getColumnIdThird(), qc
										.getOpThird(), qc.getValueThird()));
							} else {
								sb.append(getNoDicSql(qc.getColumnIdThird(), qc
										.getOpThird(), qc.getValueThird()));
							}
						} else {
							sb.append(getContractTypeSQL((String) operatorsMap
									.get(new Integer(qc.getOpThird())), qc
									.getValueThird()));
						}
					}
				}
			}
		}
		if (!StringUtil.isEmpty(bDate)) {
			sb.append(" and t.AUDITDATE>='" + bDate + "'");
		}
		if (!StringUtil.isEmpty(eDate)) {
			Date tmpend = DateUtils.getAfterData(DateUtils.stringToDate(eDate,
					DateUtils.ORA_DATES_FORMAT), 1);
			String endStr = DateUtils.toString(tmpend,
					DateUtils.ORA_DATES_FORMAT);
			sb.append(" and t.AUDITDATE<='" + endStr + "'");
		}
		// 增加所有选项
		if (StringUtil.isEmpty(instCode)) {// 所有
			if ((authInstList != null) && (authInstList.size() > 0)) {
				sb
						.append(
								" and t.instCode in (select fk_orgId from t_user_org where fk_userId = '")
						.append(userId).append("') ");
			} else {
				// 没有有权限的机构
				sb.append(" and 1=0");
			}
		}
		if (sb.length() > 3) {
			returnStr = sb.toString().replaceFirst("and", "");
		}
		return returnStr;
	}

	protected String getCommonSql(String columId, int op, String values) {
		if (columId.equals("common.----------"))
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(" and ");
		sb.append(columId.substring("common.".length()) + " ");
		sb.append(operatorsMap.get(new Integer(op)));
		// String typeF = getColumnType(tableId, columId);
		if (op == TYPE_LIKE) {
			sb.append(" '%" + values + "%'");
		} else {
			sb.append(" '" + values + "'");
		}
		return sb.toString();
	}

	/**
	 * 判断该列是否是字典项 true:表示字典项 false:不是字典项
	 * 
	 * @return
	 */
	protected boolean checkDic(String columnId) {
		ColumnInfo columnInfo = getColumnInfo(tableId, columnId);
		if (columnInfo != null && columnInfo.getTagType() != null
				&& columnInfo.getTagType().length() > 0) {
			if (columnInfo.getTagType().equals("3")
					&& !"BUOCMONTH".equals(columnInfo.getColumnId())) {
				return true;
			}
		}
		return false;
	}

	protected ColumnInfo getColumnInfo(String tableId, String columnId) {
		ColumnInfo columnInfo = new ColumnInfo();
		columnInfo.setColumnId(columnId);
		columnInfo.setTableId(tableId);
		return userInterfaceConfigService.getColumnInfo(columnInfo);
	}

	/**
	 * 字典项的SQL
	 * 
	 * @param columId
	 * @param op
	 * @param values
	 * @return
	 */
	protected String getDicSql(String columId, int op, String values) {
		ColumnInfo columnInfo = getColumnInfo(tableId, columId);
		StringBuffer sb = new StringBuffer();
		sb.append(" and t.");
		sb.append(columId + " in (");
		if (!"TEAMID".equalsIgnoreCase(columId)) {
			sb.append(" select code_value_standard_num ").append(
					" from t_code_dictionary ").append(" where code_type = '")
					.append(columnInfo.getDictionaryTypeId()).append(
							"' and code_name ");
		} else {
			sb.append(" select BUSS_TYPE_CODE ").append(
					" from T_RPT_BUSS_TYPE ").append(" where BUSS_TYPE_NAME ");
		}
		sb.append(operatorsMap.get(new Integer(op)));
		if (op == TYPE_LIKE) {
			sb.append(" '%" + values + "%')");
		} else {
			sb.append(" '" + values + "')");
		}
		return sb.toString();
	}

	protected String getNoDicSql(String columId, int op, String values) {
		StringBuffer sb = new StringBuffer();
		String typeF = getColumnType(tableId, columId);
		if (typeF.equals(TYPE_STRING) || "TRADEDATE".equals(columId)
				|| "RPTDATE".equals(columId) || "IMPDATE".equals(columId)) {
			sb.append(" and t.");
			sb.append(columId + " ");
			sb.append(operatorsMap.get(new Integer(op)));
			if (op == TYPE_LIKE) {
				sb.append(" '%" + values + "%'");
			} else {
				sb.append(" '" + values + "'");
			}
		} else if (typeF.equals(TYPE_DATE)) {
			if (this.getDbType().equals("db2")) {
				sb.append(" and SUBSTR(CHAR(t.").append(columId).append(
						"),1,10) ");
				sb.append(operatorsMap.get(new Integer(op)));
				String datetemp = values.substring(0, 4) + "-"
						+ values.substring(4, 6) + "-" + values.substring(6, 8);
				if (op == TYPE_LIKE) {
					sb.append(" '%" + datetemp + "%'");
				} else {
					sb.append(" '" + datetemp + "'");
				}
			} else if (this.getDbType().equals("oracle")) {
				sb.append(" and to_char(t.").append(columId).append(
						",'yyyymmdd') ");
				sb.append(operatorsMap.get(new Integer(op)));
				if (op == TYPE_LIKE) {
					sb.append(" '%" + values + "%'");
				} else {
					sb.append(" '" + values + "'");
				}
			}
		} else {
			sb.append(" and t.");
			sb.append(columId + " ");
			sb.append(operatorsMap.get(new Integer(op)));
			sb.append(" " + values);
		}
		return sb.toString();
	}

	/**
	 * 获取列类型
	 * 
	 * @param newcolumnInfo
	 * @return
	 */
	protected String getColumnType(ColumnInfo newcolumnInfo) {
		String returnType = TYPE_STRING;
		if (newcolumnInfo != null) {
			String type = newcolumnInfo.getDataType();
			if ((type != null) && (type.length() > 0)) {
				String[] tmpType = type.split(",");
				if (tmpType[0].equals("n")) {
					returnType = TYPE_NUM;
				} else if (tmpType[0].equals("s")) {
					returnType = TYPE_STRING;
				} else if (tmpType[0].equals("d")) {
					returnType = TYPE_DATE;
				}
			}
		}
		return returnType;
	}

	private String getColumnType(String tableId, String columnId) {
		if ("BUSINESSNO".equals(columnId)) {
			return TYPE_STRING;
		}
		ColumnInfo ci = getColumnInfo(tableId, columnId);
		return getColumnType(ci);
	}

	/**
	 * 验证三条查询条件是否合法
	 */
	protected boolean check() {
		boolean flag = true;
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		if (queryCondition != null) {
			if (!StringUtil.isEmpty(queryCondition.getValueFirst())) {
				if (StringUtil.checkStr(queryCondition.getValueFirst())) {
					this.addActionMessage("查询条件一含有非法字符");
					return false;
				}
				flag1 = checkOneValue(queryCondition.getColumnIdFirst(),
						queryCondition.getOpFirst(), "查询条件一:");
			}
			if (!StringUtil.isEmpty(queryCondition.getValueSecond())) {
				if (StringUtil.checkStr(queryCondition.getValueSecond())) {
					this.addActionMessage("查询条件二含有非法字符");
					return false;
				}
				flag2 = checkOneValue(queryCondition.getColumnIdSecond(),
						queryCondition.getOpSecond(), "查询条件二:");
			}
			if (!StringUtil.isEmpty(queryCondition.getValueThird())) {
				if (StringUtil.checkStr(queryCondition.getValueThird())) {
					this.addActionMessage("查询条件三含有非法字符");
					return false;
				}
				flag3 = checkOneValue(queryCondition.getColumnIdThird(),
						queryCondition.getOpThird(), "查询条件三:");
			}
		}
		flag = flag1 && flag2 && flag3;
		return flag;
	}

	private boolean checkOneValue(String columnId, int op, String beginError) {
		boolean flag = true;
		String error = "";
		ColumnInfo ciF = getColumnInfo(this.tableId, columnId);
		String type = getColumnType(ciF);
		if (TYPE_NUM.equals(type)) {
			if (op == TYPE_LIKE) {
				error = error + beginError + "\"" + ciF.getColumnName() + "\""
						+ "为数字型,不可以选择"
						+ operatorsMap.get(new Integer(TYPE_LIKE)) + ";";
				flag = false;
			}
		} else if (TYPE_STRING.equals(type)) {
			// if ((op == TYPE_MORE) || (op == TYPE_LESS) || (op ==
			// TYPE_MORE_EQUAL)
			// || (op == TYPE_LESS_EQUAL)) {
			// error = error+beginError+"\""+ ciF.getColumnName()+"\"" +
			// "为文本型,不可以选择"
			// + operatorsMap.get(new Integer(op)) + ";";
			// flag = false;
			// }
		} else if (TYPE_DATE.equals(type)) {
		}
		if (!(error.equals(""))) {
			this.addActionMessage(error);
		}
		return flag;
	}

	public String getFromFlag() {
		return fromFlag;
	}

	public void setFromFlag(String fromFlag) {
		this.fromFlag = fromFlag;
	}

	public String getSubReturnCreate() {
		return subReturnCreate;
	}

	public void setSubReturnCreate(String subReturnCreate) {
		this.subReturnCreate = subReturnCreate;
	}

	public String getOrderColumnSub() {
		return orderColumnSub;
	}

	public void setOrderColumnSub(String orderColumnSub) {
		this.orderColumnSub = orderColumnSub;
	}

	public String getOrderDirectionSub() {
		return orderDirectionSub;
	}

	public void setOrderDirectionSub(String orderDirectionSub) {
		this.orderDirectionSub = orderDirectionSub;
	}

	public String getPreviousTableId() {
		return previousTableId;
	}

	public void setPreviousTableId(String previousTableId) {
		this.previousTableId = previousTableId;
	}

	public String getBaseTableId() {
		return baseTableId;
	}

	public void setBaseTableId(String baseTableId) {
		this.baseTableId = baseTableId;
	}

	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

	public String[] getActionTypes() {
		return actionTypes;
	}

	public void setActionTypes(String[] actionTypes) {
		this.actionTypes = actionTypes;
	}

	public String[] getRptNos() {
		return rptNos;
	}

	public void setRptNos(String[] rptNos) {
		this.rptNos = rptNos;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public String getImportExcelInsertIsHandiworkValue() {
		return importExcelInsertIsHandiworkValue;
	}

	public void setImportExcelInsertIsHandiworkValue(
			String importExcelInsertIsHandiworkValue) {
		this.importExcelInsertIsHandiworkValue = importExcelInsertIsHandiworkValue;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}
}
