/**
 * 
 */
package com.cjit.gjsz.filem.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yulubin
 */
public class InnerTableSign{

	private static Map rootSignMap = new HashMap();
	private static Map subSignMap = new HashMap();
	static{
		// bop
		rootSignMap.put("t_customs_decl", "CUSTOMS");
		rootSignMap.put("t_export_info", "REFNOS");
		rootSignMap.put("t_company_openinfo", "BANKINFOS");
		rootSignMap.put("t_invcountrycode_info", "INVCOUNTRY");
		subSignMap.put("t_customs_decl", "CUSTOM");
		subSignMap.put("t_export_info", "REFNO");
		subSignMap.put("t_company_openinfo", "BANKINFO");
		subSignMap.put("t_invcountrycode_info", "INVCOUNTRY");
		// cfa
		// 项目信息
		rootSignMap.put("T_CFA_SUB_PROJECT_INFO", "PROJECTS");
		subSignMap.put("T_CFA_SUB_PROJECT_INFO", "PROJECTS");
		// 债权人信息
		rootSignMap.put("T_CFA_SUB_CREDITOR_INFO", "CREDITORS");
		subSignMap.put("T_CFA_SUB_CREDITOR_INFO", "CREDITOR");
		// 被担保人信息
		rootSignMap.put("T_CFA_SUB_GUARANTOR_INFO", "GUARANTORES");
		subSignMap.put("T_CFA_SUB_GUARANTOR_INFO", "GUARANTOR");
		// 受益人信息
		rootSignMap.put("T_CFA_SUB_BENEFICIARY_INFO", "BENEFICIARYS");
		subSignMap.put("T_CFA_SUB_BENEFICIARY_INFO", "BENEFICIARY");
		// 履约信息
		rootSignMap.put("T_CFA_SUB_GUPER_INFO", "COMPLIANCES");
		subSignMap.put("T_CFA_SUB_GUPER_INFO", "COMPLIANCE");
		// 境外担保人信息表
		rootSignMap.put("T_CFA_SUB_FOGUARANTOR_INFO", "FOGUCODEINFOS");
		subSignMap.put("T_CFA_SUB_FOGUARANTOR_INFO", "FOGUCODEINFO");
		// 质押外汇金额信息
		rootSignMap.put("T_CFA_SUB_EXPLCURR_INFO", "EXPLCURRINFOS");
		subSignMap.put("T_CFA_SUB_EXPLCURR_INFO", "EXPLCURRINFO");
		// 质押外汇余额信息
		rootSignMap.put("T_CFA_SUB_EXPLBALA_INFO", "EXPLBALAINFOS");
		subSignMap.put("T_CFA_SUB_EXPLBALA_INFO", "EXPLBALAINFO");
		// fal
		// 股票信息
		rootSignMap.put("T_FAL_A01_1_STOCKINFO", "STOCKINFOS");
		subSignMap.put("T_FAL_A01_1_STOCKINFO", "STOCKINFO");
		// 股票信息
		rootSignMap.put("T_FAL_A02_2_STOCKINFO", "STOCKINFOS");
		subSignMap.put("T_FAL_A02_2_STOCKINFO", "STOCKINFO");
		// 投资信息
		rootSignMap.put("T_FAL_Z03_INVEST", "INVESTS");
		subSignMap.put("T_FAL_Z03_INVEST", "INVEST");
	}

	public static Map getRootSignMap(){
		return rootSignMap;
	}

	public static Map getSubSignMap(){
		return subSignMap;
	}
}
