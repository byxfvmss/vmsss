package com.cjit.gjsz.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.interfacemanager.service.impl.AppContextHolder;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.VerifyService;
import com.cjit.gjsz.logic.model.AddRunBank;
import com.cjit.gjsz.logic.model.CompanyInfo;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class VerifyServiceImpl extends GenericServiceImpl implements
		VerifyService {

	private UserInterfaceConfigService userInterfaceConfigService;
	private VerifyConfig verifyConfig;
	private List verifylList;
	private String instCode;

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public VerifyModel verify(Object rptData, String tableId, String instCode,
			String interfaceVer, String isCluster) {
		this.instCode = instCode;
		verifylList = new ArrayList();
		verifylList.add(rptData);
		VerifyModel result = executeVerify(tableId, interfaceVer, isCluster);
		return result;
	}

	public VerifyModel verify(String[] businessId, String tableId,
			String interfaceVer, String isCluster) {
		String businessIds = StringUtil
				.getStringFromArrayByDiv(businessId, "'");
		verifylList = getDatas(businessIds, (String) SearchModel.SEARCH_MAP
				.get(tableId));
		return executeVerify(tableId, interfaceVer, isCluster);
	}

	private VerifyModel executeVerify(String tableId, String interfaceVer,
			String isCluster) {
		// List dictionarys =
		// userInterfaceConfigService.getAllDictionarysByCache();
		List dictionarys = AppContextHolder.getAllDictionarysByCache();
		DataVerify verfify = null;
		// FAL ---
		if (StringUtil.equals("T_FAL_A01_1", tableId)
				|| StringUtil.equals("T_FAL_A01_2", tableId)) {
			verfify = new Fal_A01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_A02_1", tableId)
				|| StringUtil.equals("T_FAL_A02_2", tableId)
				|| StringUtil.equals("T_FAL_A02_3", tableId)) {
			verfify = new Fal_A02_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_B01", tableId)) {
			verfify = new Fal_B01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_B02", tableId)) {
			verfify = new Fal_B02_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_B03", tableId)) {
			verfify = new Fal_B03_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_B04", tableId)) {
			verfify = new Fal_B04_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_B05", tableId)) {
			verfify = new Fal_B05_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_B06", tableId)) {
			verfify = new Fal_B06_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_C01", tableId)) {
			verfify = new Fal_C01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_D01", tableId)) {
			verfify = new Fal_D01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_D02", tableId)) {
			verfify = new Fal_D02_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_D03", tableId)) {
			verfify = new Fal_D03_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_D04", tableId)) {
			verfify = new Fal_D04_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_D05_1", tableId)
				|| StringUtil.equals("T_FAL_D05_2", tableId)) {
			verfify = new Fal_D05_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_D06_1", tableId)) {
			verfify = new Fal_D06_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_D07", tableId)) {
			verfify = new Fal_D07_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_D09", tableId)) {
			verfify = new Fal_D09_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_E01", tableId)) {
			verfify = new Fal_E01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_F01", tableId)) {
			verfify = new Fal_F01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_G01", tableId)) {
			verfify = new Fal_G01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_G02", tableId)) {
			verfify = new Fal_G02_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_H01", tableId)) {
			verfify = new Fal_H01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_H02", tableId)) {
			verfify = new Fal_H02_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_I01", tableId)) {
			verfify = new Fal_I01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_I02", tableId)) {
			verfify = new Fal_I02_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_I03", tableId)) {
			verfify = new Fal_I03_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_X01", tableId)) {
			verfify = new Fal_X01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_Z01", tableId)) {
			verfify = new Fal_Z01_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_Z02", tableId)) {
			verfify = new Fal_Z02_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_FAL_Z03", tableId)) {
			verfify = new Fal_Z03_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		}
		// CFA ---
		/* 银行自身业务信息 */
		if (StringUtil.equals("T_CFA_A_EXDEBT", tableId)) {
			// 外债
			verfify = new Self_A_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_CFA_B_EXGUARAN", tableId)) {
			// 对外担保
			verfify = new Self_B_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_CFA_C_DOFOEXLO", tableId)) {
			// 国内外汇贷款
			verfify = new Self_C_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_CFA_D_LOUNEXGU", tableId)) {
			// 境外担保项下境内贷款
			verfify = new Self_D_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_CFA_E_EXPLRMBLO", tableId)) {
			// 外汇质押人民币贷款
			verfify = new Self_E_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		} else if (StringUtil.equals("T_CFA_F_STRDE", tableId)) {
			// 商业银行人民币结构性存款
			verfify = new Self_F_DataVerify(dictionarys, verifylList,
					interfaceVer, isCluster);
		}
		/* 银行代客业务 */
		else if (StringUtil.equals("T_CFA_QFII_ACCOUNT", tableId)) {
			// 合格境外机构投资者境内证券投资-账户开户专有信息
			verfify = new QfiiAccountDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_QFII_ASSETS_DEBT", tableId)) {
			// 合格境外机构投资者境内证券投资-资产负债表信息
			verfify = new QfiiDebtDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_QFII_ASSETS_DEBT_MONTH", tableId)) {
			// 合格境外机构投资者境内证券投资-机构月度资产负债信息
			verfify = new QfiiDebtMonthDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_QFII_CHANGES", tableId)) {
			// 合格境外机构投资者境内证券投资-机构外汇账户收支信息
			verfify = new QfiiChangesDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_QFII_CHANGES_SPECIAL", tableId)) {
			// 合格境外机构投资者境内证券投资-机构人民币特殊账户收支信息
			verfify = new QfiiChangesSpecialDataVerify(dictionarys,
					verifylList, interfaceVer);
		} else if (StringUtil.equals("T_CFA_QFII_PROFIT_LOSS", tableId)) {
			// 合格境外机构投资者境内证券投资-损益表信息
			verfify = new QfiiProfitLossDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_QFII_REMIT", tableId)) {
			// 合格境外机构投资者境内证券投资-资金汇出入及结购汇明细信息
			verfify = new QfiiRemitDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_QDII_ACCOUNT", tableId)) {
			// 合格境内机构投资者境外证券投资-账户信息
			verfify = new QdiiAccountDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_QDII_INVEST", tableId)) {
			// 合格境内机构投资者境外证券投资-境外证券投资信息
			verfify = new QdiiInvestDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_QDII_REMIT", tableId)) {
			// 合格境内机构投资者境外证券投资-资金汇出入及结购汇明细信息
			verfify = new QdiiRemitDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_QDII_TRUSTEE_ACCOUNT", tableId)) {
			// 合格境内机构投资者境外证券投资-境内外币托管账户信息
			verfify = new QdiiTrusteeAccountDataVerify(dictionarys,
					verifylList, interfaceVer);
		} else if (StringUtil.equals("T_CFA_BESTIR_ACCOUNT_CLOESD", tableId)) {
			// 境内个人参与境外上市公司股权激励计划-境内专用外汇账户关户资金处置信息
			verfify = new BestirAccountClosedDataVerify(dictionarys,
					verifylList, interfaceVer);
		} else if (StringUtil.equals("T_CFA_BESTIR_CHANGES", tableId)) {
			// 境内个人参与境外上市公司股权激励计划-境内专用外汇账户收支信息
			verfify = new BestirChangesDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_RQFII_ASSETS_DEBT", tableId)) {
			// 人民币合格境外机构投资者境内证券投资-资产负债表信息
			verfify = new RqfiiDebtDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_RQFII_ASSETS_DEBT_MONTH", tableId)) {
			// 人民币合格境外机构投资者境内证券投资-机构月度资产负债信息
			verfify = new RqfiiDebtMonthDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_RQFII_CHANGES", tableId)) {
			// 人民币合格境外机构投资者境内证券投资-境内人民币账户收支情况信息
			verfify = new RqfiiChangesDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_RQFII_INCOME_EXPEND", tableId)) {
			// 人民币合格境外机构投资者境内证券投资-境内证券投资资金汇出入信息
			verfify = new RqfiiIncomeExpendDataVerify(dictionarys, verifylList,
					interfaceVer);
		} else if (StringUtil.equals("T_CFA_RQFII_INCOME_EXPEND_BUY", tableId)) {
			// 人民币合格境外机构投资者境内证券投资-人民币合格境外机构投资者资金汇出入及购汇明细信息
			verfify = new RqfiiIncomeExpendBuyDataVerify(dictionarys,
					verifylList, interfaceVer);
		} else if (StringUtil.equals("T_CFA_RQFII_PROFIT_LOSS", tableId)) {
			// 人民币合格境外机构投资者境内证券投资-损益表信息
			verfify = new RqfiiProfitLossDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		// 基础信息校验
		if (StringUtil.equals("t_base_income", tableId)) {
			verfify = new BaseIncomeDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_base_remit", tableId)) {
			verfify = new BaseRemitDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_base_payment", tableId)) {
			verfify = new BasePaymentDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_base_export", tableId)) {
			verfify = new BaseExportDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_base_dom_remit", tableId)) {
			verfify = new BaseDomRemitDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_base_dom_pay", tableId)) {
			verfify = new BaseDomPaymentDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_base_settlement", tableId)) {
			verfify = new BaseSettlementDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_base_purchase", tableId)) {
			verfify = new BasePurchaseDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		// 申报信息校验
		if (StringUtil.equals("t_decl_income", tableId)) {
			verfify = new DeclareIncomeDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_decl_remit", tableId)) {
			verfify = new DeclareRemitDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_decl_payment", tableId)) {
			verfify = new DeclarePaymentDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		// 管理信息校验
		if (StringUtil.equals("t_fini_export", tableId)) {
			verfify = new FinanceExportDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_fini_remit", tableId)) {
			verfify = new FinanceRemitDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_fini_payment", tableId)) {
			verfify = new FinancePaymentDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_fini_dom_remit", tableId)) {
			verfify = new FinanceDomRemitDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_fini_dom_pay", tableId)) {
			verfify = new FinanceDomPaymentDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_fini_dom_export", tableId)) {
			verfify = new FinanceDomExportDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_customs_decl", tableId)) {
			verfify = new CustomDeclareDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_export_info", tableId)) {
			verfify = new ExportInfoDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_fini_settlement", tableId)) {
			verfify = new FinanceSettlementDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		if (StringUtil.equals("t_fini_purchase", tableId)) {
			verfify = new FinancePurchaseDataVerify(dictionarys, verifylList,
					interfaceVer);
		}
		// 单位基本信息校验
		if (StringUtil.equals("t_company_info", tableId)) {
			List companyList = null;
			if (verifylList != null) {
				CompanyInfo companyInfo = (CompanyInfo) verifylList.get(0);
				if (companyInfo != null) {
					companyList = this.getCompanyInfosByCustCode(companyInfo
							.getBusinessid(), companyInfo.getCustcode(),
							this.instCode);
				}
			}
			verfify = new CompanyInfoDataVerify(dictionarys, verifylList,
					companyList);
		}
		if (StringUtil.equals("t_invcountrycode_info", tableId)) {
			verfify = new InvcountrycodeInfoDataVerify(dictionarys, verifylList);
		}
		if (StringUtil.equals("t_company_openinfo", tableId)) {
			verfify = new CompanyOpenInfoDataVerify(dictionarys, verifylList);
		}
		verfify.setVerifyConfig(verifyConfig);
		VerifyModel result = verfify.execute();
		if ((result.getFatcher() != null && !result.getFatcher().isEmpty())
				|| (result.getChildren() != null && result.getChildren().size() > 0)) {
			return result;
		}
		return null;
	}

	public List getDatas(String businessIds, String sql) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find(sql, map);
	}

	public List subList(String branchcode, String bussId) {
		Map map = new HashMap();
		map.put("branchcode", branchcode);
		map.put("ids", bussId);
		return this.find("getCompanyOpenSubIdList", map);
	}

	public List getBaseIncomes(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getBaseIncomes", map);
	}

	public List getBaseRemits(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getBaseRemits", map);
	}

	public List getBasePayments(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getBasePayments", map);
	}

	public List getBaseExports(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getBaseExports", map);
	}

	public List getBaseDomRemits(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getBaseDomRemits", map);
	}

	public List getBaseDomPayments(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getBaseDomPayments", map);
	}

	public List getDeclareIncomes(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getDeclareIncomes", map);
	}

	public List getDeclareRemits(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getDeclareRemits", map);
	}

	public List getDeclarePayments(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getDeclarePayments", map);
	}

	public List getFinanceExports(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getFinanceExports", map);
	}

	public List getFinanceRemits(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getFinanceRemits", map);
	}

	public List getFinancePayments(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getFinancePayments", map);
	}

	public List getFinanceDomRemits(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getFinanceDomRemits", map);
	}

	public List getFinanceDomPayments(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getFinanceDomPayments", map);
	}

	public List getFinanceDomExports(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getFinanceDomExports", map);
	}

	public List getCustomDeclares(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getCustomDeclares", map);
	}

	public List getCompanyInfos(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getCompanyInfos", map);
	}

	private List getCompanyInfosByCustCode(String businessId, String custCode,
			String instCode) {
		Map map = new HashMap();
		map.put("businessId", businessId);
		map.put("custCode", custCode);
		map.put("instCode", instCode);
		return find("getCompanyInfosByCustCode", map);
	}

	public List getCompanyOpenInfos(String businessIds) {
		Map map = new HashMap();
		map.put("ids", businessIds);
		return find("getCompanyOpenInfos", map);
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public String[] getBusinessIds(String orgId, String tableId) {
		// String sql = "select BUSINESSID from " + tableId + " where INSTCODE =
		// '" + orgId
		// + "' and DATASTATUS in (1, 2)";
		String sql = "select BUSINESSID from " + tableId
				+ " where INSTCODE = '" + orgId + "' and DATASTATUS in ("
				+ DataUtil.WJY_STATUS_NUM + "," + DataUtil.JYWTG_STATUS_NUM
				+ ") ";
		Map map = new HashMap();
		List list = new ArrayList();
		if (StringUtil.equalsIgnoreCase(tableId, "t_customs_decl")) {
			// String sql1 = " select child.SUBID as BUSINESSID from
			// t_customs_decl child, t_fini_dom_remit parent where
			// child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE = '"
			// + orgId + "' and parent.DATASTATUS in (1, 2)";
			String sql1 = " select child.SUBID as BUSINESSID from t_customs_decl child, t_fini_dom_remit parent where child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE = '"
					+ orgId
					+ "' and parent.DATASTATUS in ("
					+ DataUtil.WJY_STATUS_NUM
					+ ","
					+ DataUtil.JYWTG_STATUS_NUM
					+ ")";
			// String sql2 = " select child.SUBID as BUSINESSID from
			// t_customs_decl child, t_fini_remit parent where child.BUSINESSID
			// = parent.BUSINESSID and parent.INSTCODE = '"
			// + orgId + "' and parent.DATASTATUS in (1, 2)";
			String sql2 = " select child.SUBID as BUSINESSID from t_customs_decl child, t_fini_remit parent where child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE = '"
					+ orgId
					+ "' and parent.DATASTATUS in ("
					+ DataUtil.WJY_STATUS_NUM
					+ ","
					+ DataUtil.JYWTG_STATUS_NUM
					+ ")";
			map.put("value", sql1);
			list.addAll(find("getBusinessIds", map));
			map.put("value", sql2);
			list.addAll(find("getBusinessIds", map));
		} else if (StringUtil.equalsIgnoreCase(tableId, "t_export_info")) {
			// String sql11 = " select child.SUBID as BUSINESSID from
			// t_export_info child, t_fini_dom_export parent where
			// child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE = '"
			// + orgId + "' and parent.DATASTATUS in (1, 2)";
			String sql11 = " select child.SUBID as BUSINESSID from t_export_info child, t_fini_dom_export parent where child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE = '"
					+ orgId
					+ "' and parent.DATASTATUS in ("
					+ DataUtil.WJY_STATUS_NUM
					+ ","
					+ DataUtil.JYWTG_STATUS_NUM
					+ ")";
			// String sql22 = " select child.SUBID as BUSINESSID from
			// t_export_info child, t_fini_export parent where child.BUSINESSID
			// = parent.BUSINESSID and parent.INSTCODE= '"
			// + orgId + "' and parent.DATASTATUS in (1, 2)";
			String sql22 = " select child.SUBID as BUSINESSID from t_export_info child, t_fini_export parent where child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE= '"
					+ orgId
					+ "' and parent.DATASTATUS in ("
					+ DataUtil.WJY_STATUS_NUM
					+ ","
					+ DataUtil.JYWTG_STATUS_NUM
					+ ")";
			map.put("value", sql11);
			list.addAll(find("getBusinessIds", map));
			map.put("value", sql22);
			list.addAll(find("getBusinessIds", map));
		} else if (StringUtil.equalsIgnoreCase(tableId, "t_company_openinfo")) {
			// String sql33 = " select child.SUBID as BUSINESSID from
			// t_company_openinfo child, t_company_info parent where
			// child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE = '"
			// + orgId + "' and parent.DATASTATUS in (1, 2)";
			String sql33 = " select child.SUBID as BUSINESSID from t_company_openinfo child, t_company_info parent where child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE = '"
					+ orgId
					+ "' and parent.DATASTATUS in ("
					+ DataUtil.WJY_STATUS_NUM
					+ ","
					+ DataUtil.JYWTG_STATUS_NUM
					+ ")";
			map.put("value", sql33);
			list.addAll(find("getBusinessIds", map));
		} else if (StringUtil
				.equalsIgnoreCase(tableId, "t_invcountrycode_info")) {
			// String sql33 = " select child.SUBID as BUSINESSID from
			// t_invcountrycode_info child, t_company_info parent where
			// child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE = '"
			// + orgId + "' and parent.DATASTATUS in (1, 2)";
			String sql33 = " select child.SUBID as BUSINESSID from t_invcountrycode_info child, t_company_info parent where child.BUSINESSID = parent.BUSINESSID and parent.INSTCODE = '"
					+ orgId
					+ "' and parent.DATASTATUS in ("
					+ DataUtil.WJY_STATUS_NUM
					+ ","
					+ DataUtil.JYWTG_STATUS_NUM
					+ ")";
			map.put("value", sql33);
			list.addAll(find("getBusinessIds", map));
		} else {
			map.put("value", sql);
			list.addAll(find("getBusinessIds", map));
		}
		return (String[]) list.toArray(new String[0]);
	}

	public void updateStatus(String businessId, String tableId, String status) {
		StringBuffer sql = new StringBuffer("update " + tableId
				+ " set DATASTATUS = '" + status + "' where BUSINESSID = '"
				+ businessId + "'");
		Map map = new HashMap();
		List list = new ArrayList();
		if (StringUtil.equalsIgnoreCase(tableId, "t_customs_decl")) {
			String sql1 = " select child.BUSINESSID as BUSINESSID from t_customs_decl child, t_fini_dom_remit parent where child.BUSINESSID = parent.BUSINESSID and child.SUBID = "
					+ businessId;
			String sql2 = " select child.BUSINESSID as BUSINESSID from t_customs_decl child, t_fini_remit parent where child.BUSINESSID = parent.BUSINESSID and child.SUBID = "
					+ businessId;
			map.put("value", sql1);
			list.addAll(find("getBusinessIds", map));
			map.put("value", sql2);
			list.addAll(find("getBusinessIds", map));
			String[] ids = (String[]) list.toArray(new String[0]);
			String updateIds = StringUtil.getStringFromArrayByDiv(ids, "'");
			StringBuffer update1 = new StringBuffer(
					"update t_fini_dom_remit set DATASTATUS = '" + status
							+ "' where BUSINESSID in (" + updateIds + ")");
			map.put("value", update1.toString());
			update("updateStatus", map);
			StringBuffer update2 = new StringBuffer(
					"update t_fini_remit set DATASTATUS = '" + status
							+ "' where BUSINESSID in (" + updateIds + ")");
			map.put("value", update2.toString());
			update("updateStatus", map);
		} else if (StringUtil.equalsIgnoreCase(tableId, "t_export_info")) {
			String sql11 = " select child.BUSINESSID as BUSINESSID from t_export_info child, t_fini_dom_export parent where child.BUSINESSID = parent.BUSINESSID and child.SUBID = "
					+ "'" + businessId + "'";
			String sql22 = " select child.BUSINESSID as BUSINESSID from t_export_info child, t_fini_export parent where child.BUSINESSID = parent.BUSINESSID and child.SUBID = "
					+ "'" + businessId + "'";
			map.put("value", sql11);
			list.addAll(find("getBusinessIds", map));
			map.put("value", sql22);
			list.addAll(find("getBusinessIds", map));
			String[] ids = (String[]) list.toArray(new String[0]);
			String updateIds = StringUtil.getStringFromArrayByDiv(ids, "'");
			StringBuffer update1 = new StringBuffer(
					"update t_fini_dom_export set DATASTATUS = '" + status
							+ "' where BUSINESSID in (" + updateIds + ")");
			map.put("value", update1.toString());
			update("updateStatus", map);
			StringBuffer update2 = new StringBuffer(
					"update t_fini_export set DATASTATUS = '" + status
							+ "' where BUSINESSID in (" + updateIds + ")");
			map.put("value", update2.toString());
			update("updateStatus", map);
		} else if (StringUtil.equalsIgnoreCase(tableId, "t_company_openinfo")) {
			String sql33 = " select child.BUSINESSID as BUSINESSID from t_company_openinfo child, t_company_info parent where child.BUSINESSID = parent.BUSINESSID and child.SUBID = "
					+ "'" + businessId + "'";
			map.put("value", sql33);
			list.addAll(find("getBusinessIds", map));
			String[] ids = (String[]) list.toArray(new String[0]);
			String updateIds = StringUtil.getStringFromArrayByDiv(ids, "'");
			StringBuffer update1 = new StringBuffer(
					"update t_company_info set DATASTATUS = '" + status
							+ "' where BUSINESSID in (" + updateIds + ")");
			map.put("value", update1.toString());
			update("updateStatus", map);
		} else if (StringUtil
				.equalsIgnoreCase(tableId, "t_invcountrycode_info")) {
			String sql33 = " select child.BUSINESSID as BUSINESSID from t_invcountrycode_info child, t_company_info parent where child.BUSINESSID = parent.BUSINESSID and child.SUBID = "
					+ "'" + businessId + "'";
			map.put("value", sql33);
			list.addAll(find("getBusinessIds", map));
			String[] ids = (String[]) list.toArray(new String[0]);
			String updateIds = StringUtil.getStringFromArrayByDiv(ids, "'");
			StringBuffer update1 = new StringBuffer(
					"update t_company_info set DATASTATUS = '" + status
							+ "' where BUSINESSID in (" + updateIds + ")");
			map.put("value", update1.toString());
			update("updateStatus", map);
		} else {
			map.put("value", sql);
			update("updateStatus", map);
		}
	}

	public void updateAddBank(AddRunBank addRunBank) {
		Map map = new HashMap();
		map.put("addRunBank", addRunBank);
		update("updateAddBank", map);
	}

	public boolean checkBusinessNoRepeat(String businessNo, String tableId,
			String fileType, String businessId) {
		SelfDataVerify dataVerify = null;
		/* FAL */
		if (StringUtil.equals("T_FAL_A01_1", tableId)
				|| StringUtil.equals("T_FAL_A01_2", tableId)) {
			dataVerify = new Fal_A01_DataVerify();
		} else if (StringUtil.equals("T_FAL_A02_1", tableId)
				|| StringUtil.equals("T_FAL_A02_2", tableId)
				|| StringUtil.equals("T_FAL_A02_3", tableId)) {
			dataVerify = new Fal_A02_DataVerify();
		} else if (StringUtil.equals("T_FAL_B01", tableId)) {
			dataVerify = new Fal_B01_DataVerify();
		} else if (StringUtil.equals("T_FAL_B02", tableId)) {
			dataVerify = new Fal_B02_DataVerify();
		} else if (StringUtil.equals("T_FAL_B03", tableId)) {
			dataVerify = new Fal_B03_DataVerify();
		} else if (StringUtil.equals("T_FAL_B04", tableId)) {
			dataVerify = new Fal_B04_DataVerify();
		} else if (StringUtil.equals("T_FAL_B05", tableId)) {
			dataVerify = new Fal_B05_DataVerify();
		} else if (StringUtil.equals("T_FAL_B06", tableId)) {
			dataVerify = new Fal_B06_DataVerify();
		} else if (StringUtil.equals("T_FAL_C01", tableId)) {
			dataVerify = new Fal_C01_DataVerify();
		} else if (StringUtil.equals("T_FAL_D01", tableId)) {
			dataVerify = new Fal_D01_DataVerify();
		} else if (StringUtil.equals("T_FAL_D02", tableId)) {
			dataVerify = new Fal_D02_DataVerify();
		} else if (StringUtil.equals("T_FAL_D03", tableId)) {
			dataVerify = new Fal_D03_DataVerify();
		} else if (StringUtil.equals("T_FAL_D04", tableId)) {
			dataVerify = new Fal_D04_DataVerify();
		} else if (StringUtil.equals("T_FAL_D05_1", tableId)
				|| StringUtil.equals("T_FAL_D05_2", tableId)) {
			dataVerify = new Fal_D05_DataVerify();
		} else if (StringUtil.equals("T_FAL_D06_1", tableId)) {
			dataVerify = new Fal_D06_DataVerify();
		} else if (StringUtil.equals("T_FAL_D07", tableId)) {
			dataVerify = new Fal_D07_DataVerify();
		} else if (StringUtil.equals("T_FAL_D09", tableId)) {
			dataVerify = new Fal_D09_DataVerify();
		} else if (StringUtil.equals("T_FAL_E01", tableId)) {
			dataVerify = new Fal_E01_DataVerify();
		} else if (StringUtil.equals("T_FAL_F01", tableId)) {
			dataVerify = new Fal_F01_DataVerify();
		} else if (StringUtil.equals("T_FAL_G01", tableId)) {
			dataVerify = new Fal_G01_DataVerify();
		} else if (StringUtil.equals("T_FAL_G02", tableId)) {
			dataVerify = new Fal_G02_DataVerify();
		} else if (StringUtil.equals("T_FAL_H01", tableId)) {
			dataVerify = new Fal_H01_DataVerify();
		} else if (StringUtil.equals("T_FAL_H02", tableId)) {
			dataVerify = new Fal_H02_DataVerify();
		} else if (StringUtil.equals("T_FAL_I01", tableId)) {
			dataVerify = new Fal_I01_DataVerify();
		} else if (StringUtil.equals("T_FAL_I02", tableId)) {
			dataVerify = new Fal_I02_DataVerify();
		} else if (StringUtil.equals("T_FAL_I03", tableId)) {
			dataVerify = new Fal_I03_DataVerify();
		} else if (StringUtil.equals("T_FAL_X01", tableId)) {
			dataVerify = new Fal_X01_DataVerify();
		} else if (StringUtil.equals("T_FAL_Z01", tableId)) {
			dataVerify = new Fal_Z01_DataVerify();
		} else if (StringUtil.equals("T_FAL_Z02", tableId)) {
			dataVerify = new Fal_Z02_DataVerify();
		} else if (StringUtil.equals("T_FAL_Z03", tableId)) {
			dataVerify = new Fal_Z03_DataVerify();
		}
		/* 银行自身业务信息 */
		if (StringUtil.equals("T_CFA_A_EXDEBT", tableId)) {
			// 外债
			dataVerify = new Self_A_DataVerify();
		} else if (StringUtil.equals("T_CFA_B_EXGUARAN", tableId)) {
			// 对外担保
			dataVerify = new Self_B_DataVerify();
		} else if (StringUtil.equals("T_CFA_C_DOFOEXLO", tableId)) {
			// 国内外汇贷款
			dataVerify = new Self_C_DataVerify();
		} else if (StringUtil.equals("T_CFA_D_LOUNEXGU", tableId)) {
			// 境外担保项下境内贷款
			dataVerify = new Self_D_DataVerify();
		} else if (StringUtil.equals("T_CFA_E_EXPLRMBLO", tableId)) {
			// 外汇质押人民币贷款
			dataVerify = new Self_E_DataVerify();
		} else if (StringUtil.equals("T_CFA_F_STRDE", tableId)) {
			// 商业银行人民币结构性存款
			dataVerify = new Self_F_DataVerify();
		}
		boolean isRepeat = dataVerify.checkBusinessNoRepeat(businessNo,
				tableId, fileType, businessId);
		return isRepeat;
	}

	public VerifyConfig getVerifyConfig() {
		return verifyConfig;
	}

	public void setVerifyConfig(VerifyConfig verifyConfig) {
		this.verifyConfig = verifyConfig;
	}
}
