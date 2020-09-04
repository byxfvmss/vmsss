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
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.model.TableDataVO;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.service.OrgConfigeService;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.User;
import com.opensymphony.util.BeanUtils;

/**
 * @author yulubin
 */
public class CreateDataAction extends DataDealAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7309392356238637838L;
	private OrgConfigeService orgconfigeservice;
	private String businessNo;

	/**
	 * 外部表新增数据的入口
	 * 
	 * @return
	 */
	public String createData() {
		if (!sessionInit(false)) {
			return SUCCESS;
		}
		setCheckResion();
		this.businessId = null;
		this.request.setAttribute("configOverleapCommit",
				this.configOverleapCommit);
		// 区分关联数据信息
		if (configMap != null && !configMap.isEmpty()) {
			String relatedFileType = (String) configMap
					.get("config.related.filetype");
			if (relatedFileType != null
					&& relatedFileType.indexOf(this.fileType) > -1) {
				return createDataRelated();
			}
		}
		return createData(infoTypeCode, tableId);
	}

	private String createDataRelated() {
		initData();
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
		RptData data = dataDealService.findRptDataByRptNoAndBusinessNo(tableId,
				fileType, this.rptNo, this.businessNo);
		if (data == null) {
			log.error("数据异常，未找到对应数据");
			tableData[0] = null;
		} else {
			tableData[0] = createDataNew(this.tableId, data.getFileType(), data
					.getBusinessId(), false);
		}
		// 2.查询变动数据
		tableData[1] = createDataNew(this.tableId, this.fileType, null, true);
		// 共享数据
		setInfoToRequest();
		this.request.setAttribute("tableData", tableData);
		return RELATED;
	}

	private void initData() {
		if (this.request.getAttribute("checkFlag") != null) {
			this.rptNo = (String) request.getAttribute("rptNo");
			this.businessNo = (String) request.getAttribute("businessNo");
		} else {
			this.businessNo = request.getParameter("businessNo");
		}
	}

	private void setInfoToRequest() {
		// wl8.1下放入request , jd=渣打银行开关参数
		this.fromFlag = "createNew";
		this.request.setAttribute("fromFlag", this.fromFlag);
		this.request.setAttribute("infoTypeCode", this.infoTypeCode);
		this.request.setAttribute("busiDataType", this.busiDataType);
		this.request.setAttribute("infoType", this.infoType);
		this.request.setAttribute("fileType", this.fileType);
		this.request.setAttribute("cfaRptNo", this.rptNo);
		this.request.setAttribute("businessNo", this.businessNo);
		this.request.setAttribute("businessId", "");// 创建数据时不需要businessid
		if (!(DataUtil.isJCDWSBHX(infoTypeCode))) {
			innerCreateFlag = "1";
		}
	}

	/**
	 * 设置审核不通过理由
	 */
	private void setCheckResion() {
		String reasioninfoStr = "";
		if (businessId != null && !businessId.equals("") && tableId != null
				&& !tableId.equals("")) {
			RptData rt = new RptData();
			rt.setBusinessId(businessId);
			rt.setTableId(tableId);
			List rpts = dataDealService.findRptDataReduce(rt);
			if (rpts.size() > 0) {
				RptData rtt = (RptData) rpts.get(0);
				if (rtt.getDataStatus() != null
						&& rtt.getDataStatus().equals("4")) {
					reasioninfoStr = dataDealService.getRptCheckInfo(rt);
				}
			}
		}
		this.request.setAttribute("reasioninfoStr", EditDataAction
				.filter(reasioninfoStr));
	}

	/**
	 * 新增数据的通用方法
	 * 
	 * @param infoTypeCode
	 * @param tableId
	 * @return
	 */
	public String createData(String infoTypeCode, String tableId) {
		try {
			// 获取当前用户信息
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			this.fromFlag = "createNew";
			this.request.setAttribute("fromFlag", this.fromFlag);
			// 从页面获取 数据采集范围和业务类型
			this.busiDataType = (String) this.request
					.getParameter("busiDataType");
			if (StringUtil.isEmpty(this.infoType)) {
				if (StringUtil.isNotEmpty(this.fileType)
						&& this.fileType.length() == 2) {
					this.infoType = this.fileType.substring(0, 1);
				} else if (StringUtil.isNotEmpty(tableId)) {
					// FIXME 代客业务 从tableid解析infotype
				}
			}
			rptTableInfo = dataDealService.findRptTableInfoById(tableId,
					fileType);
			if (!StringUtils.isEmpty(this.message)
					&& this.message.equals("checkFailure")) {
				rptColumnList = (List) this.request
						.getAttribute("rptColumnList");
				rptData = (RptData) this.request.getAttribute("rptData");
				rptData.setCanNext(false);
				rptData.setIsHandiwork("Y");
				setBaseObject(); // DFHL:增加基础信息
				setDeclObject(infoTypeCode); // 查询对应申报信息
				setFiniObject(tableId); // 查询对应核销信息
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
								boolean addTableMap = false;
								Map tableMap = (HashMap) dictionaryMap
										.get(tableId);
								List codeDictionaryList = null;
								if (tableMap != null) {
									codeDictionaryList = (ArrayList) tableMap
											.get(column.getDictionaryTypeId());
									if (codeDictionaryList != null) {
										this.addFieldToRequest(column
												.getAliasColumnId()
												+ "_list", codeDictionaryList);
										addTableMap = true;
									}
								}
								if (!addTableMap) {
									Map publicMap = (HashMap) dictionaryMap
											.get("PUBLIC");
									if (publicMap != null) {
										codeDictionaryList = (ArrayList) publicMap
												.get(column
														.getDictionaryTypeId());
										if (codeDictionaryList != null) {
											this.addFieldToRequest(column
													.getAliasColumnId()
													+ "_list",
													codeDictionaryList);
										}
									}
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
			}
			if (rptData == null) {
				rptData = new RptData();
				rptData.setIsHandiwork("Y");
			}
			// 根据报表ID获取物理表的列信息
			rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							fileType));
			// 循环列信息，作各种处理
			String actionTypeAliasColumnId = null;
			String actionDescAliasColumnId = null;
			String rptNoAliasColumnId = null;
			String businessNoAliasColumnId = null;
			String branchRptNoAliasColumnId = null;
			// String buocMonthAliasColumnId = null;
			String fal_objCode = null;

			int cFlag = 0;
			for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				if ("table".equals(column.getDataType())) {
					continue;
				}
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				// 获取 actiontype, actiondesc 二列各自对应的别名
				if ("ACTIONTYPE".equalsIgnoreCase(column.getColumnId())) {
					actionTypeAliasColumnId = column.getAliasColumnId();
				} else if ("ACTIONDESC".equalsIgnoreCase(column.getColumnId())) {
					actionDescAliasColumnId = column.getAliasColumnId();
				} else if ("BUSINESSNO".equalsIgnoreCase(column.getColumnId())) {
					businessNoAliasColumnId = column.getAliasColumnId();
				} else if ("BUOCMONTH".equalsIgnoreCase(column.getColumnId())) {
					if (StringUtil.isEmpty(this.buocMonth)) {
						// this.buocMonth =
						// DateUtils.serverCurrentDate("yyyyMM");// 当前年月
						this.buocMonth = DateUtils.getPreMonth();// 上个月
					}
					BeanUtils.setValue(rptData, column.getAliasColumnId(),
							this.buocMonth);

				} else if ("A".equals(this.infoType)) {
					// 外债编号
					if ("EXDEBTCODE".equalsIgnoreCase(column.getColumnId())) {
						rptNoAliasColumnId = column.getAliasColumnId();
					}
					// 债务人代码
					if ("DEBTORCODE".equalsIgnoreCase(column.getColumnId())) {
						branchRptNoAliasColumnId = column.getAliasColumnId();
					}
				} else if ("B".equals(this.infoType)) {
					// 对外担保编号
					if ("EXGUARANCODE".equalsIgnoreCase(column.getColumnId())) {
						rptNoAliasColumnId = column.getAliasColumnId();
					}
					// 担保人代码
					if ("GUARANTORCODE".equalsIgnoreCase(column.getColumnId())) {
						branchRptNoAliasColumnId = column.getAliasColumnId();
					}
				} else if ("C".equals(this.infoType)) {
					// 国内外汇贷款编号
					if ("DOFOEXLOCODE".equalsIgnoreCase(column.getColumnId())) {
						rptNoAliasColumnId = column.getAliasColumnId();
					}
					// 债权人代码
					if ("CREDITORCODE".equalsIgnoreCase(column.getColumnId())) {
						branchRptNoAliasColumnId = column.getAliasColumnId();
					}
				} else if ("D".equals(this.infoType)) {
					// 外保内贷编号
					if ("LOUNEXGUCODE".equalsIgnoreCase(column.getColumnId())) {
						rptNoAliasColumnId = column.getAliasColumnId();
					}
					// 债权人代码
					if ("CREDITORCODE".equalsIgnoreCase(column.getColumnId())) {
						branchRptNoAliasColumnId = column.getAliasColumnId();
					}
				} else if ("E".equals(this.infoType)) {
					// 外汇质押人民币贷款编号
					if ("EXPLRMBLONO".equalsIgnoreCase(column.getColumnId())) {
						rptNoAliasColumnId = column.getAliasColumnId();
					}
					// 债权人代码
					if ("CREDITORCODE".equalsIgnoreCase(column.getColumnId())) {
						branchRptNoAliasColumnId = column.getAliasColumnId();
					}
				} else if ("F".equals(this.infoType)) {
					// 人民币结构性存款编号
					if ("STRDECODE".equalsIgnoreCase(column.getColumnId())) {
						rptNoAliasColumnId = column.getAliasColumnId();
					}
					// 金融机构标识码
					if ("BRANCHCODE".equalsIgnoreCase(column.getColumnId())) {
						branchRptNoAliasColumnId = column.getAliasColumnId();
					}
				}
				if ("OBJCODE".equalsIgnoreCase(column.getColumnId())) {
					fal_objCode = column.getAliasColumnId();
				}
				// 若是下拉框，则根据字典类型取字典信息，形成下拉框内容集，并将内容集放在request中，名为
				// 别名_list,例如c1_list
				// modify by panshaobo 将币种默认值判断，移到下拉菜单列，仅有这列才可能需要币种列判断，减少判断次数
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
							boolean addTableMap = false;
							Map tableMap = (HashMap) dictionaryMap.get(tableId);
							List codeDictionaryList = null;
							if (tableMap != null) {
								codeDictionaryList = (ArrayList) tableMap
										.get(column.getDictionaryTypeId());
								if (codeDictionaryList != null) {
									this.addFieldToRequest(column
											.getAliasColumnId()
											+ "_list", codeDictionaryList);
									addTableMap = true;
								}
							}
							if (!addTableMap) {
								Map publicMap = (HashMap) dictionaryMap
										.get("PUBLIC");
								if (publicMap != null) {
									codeDictionaryList = (ArrayList) publicMap
											.get(column.getDictionaryTypeId());
									if (codeDictionaryList != null) {
										this.addFieldToRequest(column
												.getAliasColumnId()
												+ "_list", codeDictionaryList);
									}
								}
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
							}
						}
					}
					// 添加默认值 Begin
					if ("AJ".equals(this.fileType)) {
						if ("CREDITORTYPE".equals(column.getColumnId())) {
							// 债权人类型代码 应为资本市场所对应的债权人类型代码。
							BeanUtils.setValue(rptData, column
									.getAliasColumnId(), "20001800");
						}
					}
					if ("AK".equals(this.fileType)) {
						if ("CREDITORTYPE".equals(column.getColumnId())) {
							// 债权人类型代码 应为资本市场所对应的债权人类型代码。
							BeanUtils.setValue(rptData, column
									.getAliasColumnId(), "20001800");
						}
					}
					if ("EA".equals(this.fileType)) {
						if ("CREDCONCURR".equals(column.getColumnId())) {
							// 贷款签约币种 默认为人民币
							BeanUtils.setValue(rptData, column
									.getAliasColumnId(), "CNY");
						}
					}
					if ("FD".equals(this.fileType)) {
						if ("CURRENCY".equals(column.getColumnId())) {
							// 币种 默认美元
							BeanUtils.setValue(rptData, column
									.getAliasColumnId(), "USD");
						}
					}
					if ("T_CFA_A_EXDEBT".equalsIgnoreCase(this.tableId)
							&& "DEBTYPE".equalsIgnoreCase(column.getColumnId())) {
						// 外债签约-债务类型
						BeanUtils.setValue(rptData, column.getAliasColumnId(),
								DataUtil.getDebtTypeByFileType(this.fileType));
					}
					// 代客业务
					if ("CURRENCE_CODE".equals(column.getColumnId())) {
						if ("T_CFA_BESTIR_CHANGES".equals(this.tableId)
								|| "T_CFA_QDII_INVEST".equals(this.tableId)) {
							// 币种 默认美元
							BeanUtils.setValue(rptData, column
									.getAliasColumnId(), "USD");
						} else if ("T_CFA_RQFII_CHANGES".equals(this.tableId)
								|| "T_CFA_RQFII_ASSETS_DEBT_MONTH"
										.equals(this.tableId)
								|| "T_CFA_RQFII_ASSETS_DEBT"
										.equals(this.tableId)
								|| "T_CFA_RQFII_PROFIT_LOSS"
										.equals(this.tableId)
								|| "T_CFA_QFII_ASSETS_DEBT_MONTH"
										.equals(this.tableId)
								|| "T_CFA_QFII_CHANGES_SPECIAL"
										.equals(this.tableId)
								|| "T_CFA_QFII_ASSETS_DEBT"
										.equals(this.tableId)
								|| "T_CFA_QFII_PROFIT_LOSS"
										.equals(this.tableId)) {
							// 币种 默认人民币
							BeanUtils.setValue(rptData, column
									.getAliasColumnId(), "CNY");
						}
					}
					// 添加默认值 End
				}
			}
			if (request.getParameter("sbHxFlag") != null
					&& "1".equals(sbHxFlag)) {
				rptData = new RptData();
				rptData.setIsHandiwork("Y");
				rptData.setIsHaveSendCommit(dataDealService.isRptHasSendCommit(
						this.tableId, businessId)); // added
				// by
				// wangxin
				// 20100301
				BeanUtils.setValue(rptData, "businessId", businessId);
				BeanUtils.setValue(rptData, actionTypeAliasColumnId, null);
				BeanUtils
						.setValue(rptData, actionDescAliasColumnId, actionDesc);
				BeanUtils.setValue(rptData, rptNoAliasColumnId, rptNo);
				BeanUtils.setValue(rptData, businessNoAliasColumnId,
						this.businessNo);
				businessId = null;
				// 如果是增加申报/核销信息，则将SESSION中的业务主键businessId清掉
				session.remove(ScopeConstants.CURRENT_BUSINESS_ID);
				// 在wl8.1下放入request
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				this.request.setAttribute("rptData", this.rptData);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("rptTableInfo", this.rptTableInfo);
				this.request.setAttribute("tableId", this.tableId);
				this.request.setAttribute("busiDataType", this.busiDataType);
				this.request.setAttribute("infoType", this.infoType);
				this.request.setAttribute("fileType", this.fileType);
				this.request.setAttribute("cfaRptNo", this.rptNo);
				this.request.setAttribute("businessNo", this.businessNo);
				return SUCCESS;
			}
			if (!(DataUtil.isJCDWSBHX(infoTypeCode))) {
				innerCreateFlag = "1";
			}
			if ("yes".equalsIgnoreCase(this.configSelfAutoInputBranchCode)
					// && StringUtil.isNotEmpty(branchRptNoAliasColumnId)
					&& StringUtil.isNotEmpty(fal_objCode)
					&& StringUtil.isNotEmpty(this.instCode)) {
				// 在自身业务主单据中自动填充12位金融机构标识码字段
				com.cjit.gjsz.filem.model.t_org_config t_org = orgconfigeservice
						.findAll(this.instCode);
				if (t_org != null && StringUtil.isNotEmpty(t_org.getRptNo())) {
					// BeanUtils.setValue(this.rptData,
					// branchRptNoAliasColumnId,
					// t_org.getRptNo());
					BeanUtils.setValue(this.rptData, fal_objCode, t_org
							.getRptNo());
				}
			}
			// 在wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("rptData", this.rptData);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("rptTableInfo", this.rptTableInfo);
			this.request.setAttribute("busiDataType", this.busiDataType);
			this.request.setAttribute("infoType", this.infoType);
			this.request.setAttribute("fileType", this.fileType);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	// DFHL:增加基础信息
	private void setBaseObject() {
		if (request.getParameter("sbHxFlag") != null && "1".equals(sbHxFlag)) {
			// 未申报/未核销，new一个rptData,设置actiontype, actiondesc, rptno的值
			String bid = (StringUtil.isEmpty(businessId) && rptData != null) ? BeanUtils
					.getValue(rptData, "businessId").toString()
					: businessId;
			String baseId = (String) TableIdRela.getJcsyMap().get(tableId);
			if (baseId != null) {
				Object obj = searchService.getDataVerifyModel(baseId, bid);
				if (obj != null) {
					this.addFieldToStack(BASE_OBJECT, obj);
				}
			}
		}
	}

	// 查询对应申报信息
	private void setDeclObject(String infoTypeCode) {
		String declTableId = null;
		if ("3".equals(infoTypeCode)) {
			// 直接上游单据名称
			declTableId = (String) TableIdRela.getZjsyMap().get(tableId);
		} else if ("1".equals(infoTypeCode)) {
			// 直接下游单据名称
			declTableId = (String) TableIdRela.getZjxyMap().get(tableId);
		}
		if (declTableId != null
				&& "2".equals(DataUtil.getInfoTypeCodeByTableId(declTableId))) {
			String bid = (StringUtil.isEmpty(businessId) && rptData != null) ? BeanUtils
					.getValue(rptData, "businessId").toString()
					: businessId;
			Object obj = searchService.getDataVerifyModel(declTableId, bid);
			if (obj != null) {
				this.addFieldToStack(DECL_OBJECT, obj);
			}
		}
	}

	// 查询对应核销信息（仅主表）
	private void setFiniObject(String tableId) {
		String finiTableId = (String) TableIdRela.getZzxyMap().get(tableId);
		if (finiTableId != null
				&& "3".equals(DataUtil.getInfoTypeCodeByTableId(finiTableId))) {
			String bid = (StringUtil.isEmpty(businessId) && rptData != null) ? BeanUtils
					.getValue(rptData, "businessId").toString()
					: businessId;
			Object obj = searchService.getDataVerifyModel(finiTableId, bid);
			if (obj != null) {
				this.addFieldToStack(FINI_OBJECT, obj);
			}
		}
	}

	/**
	 * 新增数据的通用方法
	 * 
	 * @param infoTypeCode
	 * @param tableId
	 * @return
	 */
	public TableDataVO createDataNew(String tableId, String fileType,
			String bussId, boolean checkFail) {
		TableDataVO dataVo = new TableDataVO();
		boolean failSave = checkFail && !StringUtils.isEmpty(this.message)
				&& message.equals("checkFailure");// 保存或校验失败
		// table数据
		RptTableInfo tableInfo = dataDealService.findRptTableInfoById(tableId,
				fileType);
		dataVo.setRptTableInfo(tableInfo);
		// 列数据
		if (failSave) {
			dataVo.setRptColumnList((List) this.request
					.getAttribute("rptColumnList"));
		} else {
			dataVo.setRptColumnList(dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							fileType)));
		}
		// 循环列信息，作各种处理
		Map dictionaryMap = (Map) SystemCache
				.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
		String rptNoAliasColumnId = null;
		String businessNoAliasColumnId = null;
		String branchRptNoAliasColumnId = null;
		// String teamIdAliasColumnId = null;
		int cFlag = 0;
		Map dictListMapTemp = new HashMap();
		StringBuffer columns = new StringBuffer();
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
			// 获取 actiontype, actiondesc 二列各自对应的别名
			if ("BUSINESSNO".equalsIgnoreCase(column.getColumnId())) {
				businessNoAliasColumnId = column.getAliasColumnId();
			} else if ("TEAMID".equalsIgnoreCase(column.getColumnId())) {
				// teamIdAliasColumnId = column.getAliasColumnId();
			} else if ("A".equals(this.infoType)) {
				// 外债编号
				if ("EXDEBTCODE".equalsIgnoreCase(column.getColumnId())) {
					rptNoAliasColumnId = column.getAliasColumnId();
				}
			} else if ("B".equals(this.infoType)) {
				// 对外担保编号
				if ("EXGUARANCODE".equalsIgnoreCase(column.getColumnId())) {
					rptNoAliasColumnId = column.getAliasColumnId();
				}
				// 担保人代码
				if ("BC".equals(fileType)
						&& "GUARANTORCODE".equalsIgnoreCase(column
								.getColumnId())) {
					branchRptNoAliasColumnId = column.getAliasColumnId();
				}
			} else if ("C".equals(this.infoType)) {
				// 国内外汇贷款编号
				if ("DOFOEXLOCODE".equalsIgnoreCase(column.getColumnId())) {
					rptNoAliasColumnId = column.getAliasColumnId();
				}
			} else if ("D".equals(this.infoType)) {
				// 外保内贷编号
				if ("LOUNEXGUCODE".equalsIgnoreCase(column.getColumnId())) {
					rptNoAliasColumnId = column.getAliasColumnId();
				}
			} else if ("E".equals(this.infoType)) {
				// 外汇质押人民币贷款编号
				if ("EXPLRMBLONO".equalsIgnoreCase(column.getColumnId())) {
					rptNoAliasColumnId = column.getAliasColumnId();
				}
			} else if ("F".equals(this.infoType)) {
				// 人民币结构性存款编号
				if ("STRDECODE".equalsIgnoreCase(column.getColumnId())) {
					rptNoAliasColumnId = column.getAliasColumnId();
				}
				// 金融机构标识码
				if (("FB".equals(fileType) || "FC".equals(fileType))
						&& "BRANCHCODE".equalsIgnoreCase(column.getColumnId())) {
					branchRptNoAliasColumnId = column.getAliasColumnId();
				}
			}
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
				// 添加默认值
				if ("EA".equals(this.fileType)) {
					if ("CREDCONCURR".equals(column.getColumnId())) {
						// 贷款签约币种 默认为人民币
						if (rptData == null) {
							rptData = new RptData();
						}
						BeanUtils.setValue(rptData, column.getAliasColumnId(),
								"CNY");
					}
				}
				if ("FD".equals(this.fileType)) {
					if ("CURRENCY".equals(column.getColumnId())) {
						// 币种 默认美元
						if (rptData == null) {
							rptData = new RptData();
						}
						BeanUtils.setValue(rptData, column.getAliasColumnId(),
								"USD");
					}
				}
			}
			// DFHL: 申报/核销操作类型为“新建”，“修改/删除原因”为灰色，不能编辑和录入
			if ("ACTIONDESC".equalsIgnoreCase(column.getColumnId())
					&& request.getParameter("sbHxFlag") != null
					&& "1".equals(sbHxFlag)) {
				column.setCanModify("0");
			}
		}
		dataVo.setDictListMap(dictListMapTemp);
		// 获取rptdata数据
		if (failSave)
			dataVo.setRptData((RptData) this.request.getAttribute("rptData"));
		else {
			while (cFlag < largestColumnNum) {
				columns.append("'' as c").append(++cFlag).append(",");
			}
			// 根据物理表名，机构号，业务主键等获取数据
			List rptDataList = null;
			if (StringUtil.isNotEmpty(bussId)) {
				RptData params = new RptData();
				params.setTableId(tableId);
				params.setBusinessId(bussId);
				params.setColumns(StringUtils
						.removeEnd(columns.toString(), ","));
				rptDataList = dataDealService.findRptData(params);
			}
			if (CollectionUtil.isNotEmpty(rptDataList)) {
				// 签约信息
				RptData cRd = (RptData) rptDataList.get(0);
				if (cRd != null) {
					// Object teamId = BeanUtils
					// .getValue(cRd, teamIdAliasColumnId);
					// if(teamId != null){
					// this.setTeamId(String.valueOf(teamId));
					// }
				}
				dataVo.setRptData(cRd);
			} else {
				// 下游信息
				RptData rd = new RptData();
				// BeanUtils.setValue(rd, teamIdAliasColumnId, this.teamId);
				dataVo.setRptData(rd);
			}
		}
		RptData rptData = dataVo.getRptData();
		if (!StringUtils.isEmpty(bussId)) {
			rptData.setIsHaveSendCommit(dataDealService.isRptHasSendCommit(
					tableId, bussId));
		} else
			rptData.setIsHaveSendCommit(false);
		BeanUtils.setValue(rptData, rptNoAliasColumnId, rptNo);
		if ("yes".equalsIgnoreCase(this.configSelfAutoInputBranchCode)
				&& StringUtil.isNotEmpty(branchRptNoAliasColumnId)
				&& StringUtil.isNotEmpty(this.instCode)) {
			// 在自身业务主单据中自动填充12位金融机构标识码字段
			com.cjit.gjsz.filem.model.t_org_config t_org = orgconfigeservice
					.findAll(this.instCode);
			if (t_org != null && StringUtil.isNotEmpty(t_org.getRptNo())) {
				BeanUtils.setValue(rptData, branchRptNoAliasColumnId, t_org
						.getRptNo());
			}
		}
		BeanUtils.setValue(rptData, businessNoAliasColumnId, this.businessNo);
		return dataVo;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public OrgConfigeService getOrgconfigeservice() {
		return orgconfigeservice;
	}

	public void setOrgconfigeservice(OrgConfigeService orgconfigeservice) {
		this.orgconfigeservice = orgconfigeservice;
	}
}
