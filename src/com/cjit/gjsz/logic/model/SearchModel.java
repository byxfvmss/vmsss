package com.cjit.gjsz.logic.model;

import org.apache.commons.collections.map.LinkedMap;

public class SearchModel {

	public static final LinkedMap SEARCH_MAP = new LinkedMap();
	static {
		// BOP //////////////////////////////////////////////////
		// 基础信息
		SEARCH_MAP.put("t_base_income", "getBaseIncomes");
		SEARCH_MAP.put("t_base_remit", "getBaseRemits");
		SEARCH_MAP.put("t_base_payment", "getBasePayments");
		SEARCH_MAP.put("t_base_export", "getBaseExports");
		SEARCH_MAP.put("t_base_dom_remit", "getBaseDomRemits");
		SEARCH_MAP.put("t_base_dom_pay", "getBaseDomPayments");
		SEARCH_MAP.put("t_base_settlement", "getBaseSettlements");
		SEARCH_MAP.put("t_base_purchase", "getBasePurchases");
		// 申报信息
		SEARCH_MAP.put("t_decl_income", "getDeclareIncomes");
		SEARCH_MAP.put("t_decl_remit", "getDeclareRemits");
		SEARCH_MAP.put("t_decl_payment", "getDeclarePayments");
		// 管理信息
		SEARCH_MAP.put("t_fini_export", "getFinanceExports");
		SEARCH_MAP.put("t_fini_remit", "getFinanceRemits");
		SEARCH_MAP.put("t_fini_payment", "getFinancePayments");
		SEARCH_MAP.put("t_fini_dom_remit", "getFinanceDomRemits");
		SEARCH_MAP.put("t_fini_dom_pay", "getFinanceDomPayments");
		SEARCH_MAP.put("t_fini_dom_export", "getFinanceDomExports");
		SEARCH_MAP.put("t_fini_settlement", "getFinanceSettlements");
		SEARCH_MAP.put("t_fini_purchase", "getFinancePurchases");
		SEARCH_MAP.put("t_customs_decl", "getCustomDeclares");
		SEARCH_MAP.put("t_export_info", "getExportInfos");
		// 单位基本信息
		SEARCH_MAP.put("t_company_info", "getCompanyInfos");
		SEARCH_MAP.put("t_company_openinfo", "getCompanyOpenInfos");
		SEARCH_MAP.put("t_invcountrycode_info", "getInvcountrycodeInfos");
		// CFA //////////////////////////////////////////////////
		// 资本项目-自身业务
		SEARCH_MAP.put("T_CFA_A_EXDEBT", "getCfaAExdebt");
		SEARCH_MAP.put("T_CFA_B_EXGUARAN", "getCfaBExguaran");
		SEARCH_MAP.put("T_CFA_C_DOFOEXLO", "getCfaCDofoexlo");
		SEARCH_MAP.put("T_CFA_D_LOUNEXGU", "getCfaDLounexgu");
		SEARCH_MAP.put("T_CFA_E_EXPLRMBLO", "getCfaEExplrmblo");
		SEARCH_MAP.put("T_CFA_F_STRDE", "getCfaFStrde");
		// 资本项目-子表
		SEARCH_MAP.put("T_CFA_SUB_BENEFICIARY_INFO", "getCfaSubBENEFICIARY");
		SEARCH_MAP.put("T_CFA_SUB_CREDITOR_INFO", "getCfaSubCREDITOR");
		SEARCH_MAP.put("T_CFA_SUB_EXPLBALA_INFO", "getCfaSubEXPLBALA");
		SEARCH_MAP.put("T_CFA_SUB_EXPLCURR_INFO", "getCfaSubEXPLCURR");
		SEARCH_MAP.put("T_CFA_SUB_FOGUARANTOR_INFO", "getCfaSubFOGUARANTOR");
		SEARCH_MAP.put("T_CFA_SUB_GUARANTOR_INFO", "getCfaSubGUARANTOR");
		SEARCH_MAP.put("T_CFA_SUB_GUPER_INFO", "getCfaSubGUPER");
		SEARCH_MAP.put("T_CFA_SUB_PROJECT_INFO", "getCfaSubPROJECT");
		// 资本项目-机构对照表
		SEARCH_MAP.put("T_ORG_CONFIG", "getOrgConfigs");
		// FAL //////////////////////////////////////////////////
		// 外债交易
		SEARCH_MAP.put("T_FAL_A01_1", "getFalA01_1");
		SEARCH_MAP.put("T_FAL_A01_2", "getFalA01_2");
		SEARCH_MAP.put("T_FAL_A02_1", "getFalA02_1");
		SEARCH_MAP.put("T_FAL_A02_2", "getFalA02_2");
		SEARCH_MAP.put("T_FAL_A02_3", "getFalA02_3");
		SEARCH_MAP.put("T_FAL_B01", "getFalB01");
		SEARCH_MAP.put("T_FAL_B02", "getFalB02");
		SEARCH_MAP.put("T_FAL_B03", "getFalB03");
		SEARCH_MAP.put("T_FAL_B04", "getFalB04");
		SEARCH_MAP.put("T_FAL_B05", "getFalB05");
		SEARCH_MAP.put("T_FAL_B06", "getFalB06");
		SEARCH_MAP.put("T_FAL_C01", "getFalC01");
		SEARCH_MAP.put("T_FAL_D01", "getFalD01");
		SEARCH_MAP.put("T_FAL_D02", "getFalD02");
		SEARCH_MAP.put("T_FAL_D03", "getFalD03");
		SEARCH_MAP.put("T_FAL_D04", "getFalD04");
		SEARCH_MAP.put("T_FAL_D05_1", "getFalD05_1");
		SEARCH_MAP.put("T_FAL_D05_2", "getFalD05_2");
		SEARCH_MAP.put("T_FAL_D06_1", "getFalD06_1");
		SEARCH_MAP.put("T_FAL_D07", "getFalD07");
		SEARCH_MAP.put("T_FAL_D09", "getFalD09");
		SEARCH_MAP.put("T_FAL_E01", "getFalE01");
		SEARCH_MAP.put("T_FAL_F01", "getFalF01");
		SEARCH_MAP.put("T_FAL_G01", "getFalG01");
		SEARCH_MAP.put("T_FAL_G02", "getFalG02");
		SEARCH_MAP.put("T_FAL_H01", "getFalH01");
		SEARCH_MAP.put("T_FAL_H02", "getFalH02");
		SEARCH_MAP.put("T_FAL_I01", "getFalI01");
		SEARCH_MAP.put("T_FAL_I02", "getFalI02");
		SEARCH_MAP.put("T_FAL_I03", "getFalI03");
		SEARCH_MAP.put("T_FAL_X01", "getFalX01");
		SEARCH_MAP.put("T_FAL_Z01", "getFalZ01");
		SEARCH_MAP.put("T_FAL_Z02", "getFalZ02");
		SEARCH_MAP.put("T_FAL_Z03", "getFalZ03");
		// 子表
		SEARCH_MAP.put("T_FAL_A01_1_STOCKINFO", "getFalA01_1STOCKINFO");
		SEARCH_MAP.put("T_FAL_A02_2_STOCKINFO", "getFalA02_2STOCKINFO");
		SEARCH_MAP.put("T_FAL_Z03_INVEST", "getFalZ03_INVEST");
	}
	private String sql;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	private String typeId;
	private String type;
	private String tableId;
	private String tableCode;
	private String orgId;
	private String currentDate;
	private String success;
	private String searchCondition;
	private String orderBy;
	private String sumColumn;

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getType() {
		return type;
	}

	public String getTableId() {
		return tableId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
		this.tableId = "T_FAL_" + tableCode;
		this.tableId = this.tableId.replaceAll("-", "_");
	}

	public String getSumColumn() {
		return sumColumn;
	}

	public void setSumColumn(String sumColumn) {
		this.sumColumn = sumColumn;
	}
}
