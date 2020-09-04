/**
 * 
 */
package com.cjit.gjsz.datadeal.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yulubin
 */
public class TableIdRela{

	private static final Map jcSbMap = new HashMap();
	private static final Map jcHxMap = new HashMap();
	private static final Map zjsyMap = new HashMap();
	private static final Map zjxyMap = new HashMap();
	private static final Map jcsyMap = new HashMap();
	private static final Map zibzhubMap = new HashMap();
	private static final Map zzxyMap = new HashMap();
	static{
		// 基础-》申报
		jcSbMap.put("t_base_income", "t_decl_income");
		jcSbMap.put("t_base_remit", "t_decl_remit");
		jcSbMap.put("t_base_payment", "t_decl_payment");
		// 基础-》核销
		jcHxMap.put("t_base_income", "t_fini_export");
		jcHxMap.put("t_base_remit", "t_fini_remit");
		jcHxMap.put("t_base_payment", "t_fini_payment");
		jcHxMap.put("t_base_export", "t_fini_dom_export");
		jcHxMap.put("t_base_dom_remit", "t_fini_dom_remit");
		jcHxMap.put("t_base_dom_pay", "t_fini_dom_pay");
		jcHxMap.put("t_base_settlement", "t_fini_settlement");
		jcHxMap.put("t_base_purchase", "t_fini_purchase");
		// 直接上游
		// 申报-》基础
		zjsyMap.put("t_decl_income", "t_base_income");
		zjsyMap.put("t_decl_remit", "t_base_remit");
		zjsyMap.put("t_decl_payment", "t_base_payment");
		// 核销-》基础/申报
		zjsyMap.put("t_fini_export", "t_decl_income");
		zjsyMap.put("t_fini_remit", "t_decl_remit");
		zjsyMap.put("t_fini_payment", "t_decl_payment");
		zjsyMap.put("t_fini_dom_export", "t_base_export");
		zjsyMap.put("t_fini_dom_remit", "t_base_dom_remit");
		zjsyMap.put("t_fini_dom_pay", "t_base_dom_pay");
		zjsyMap.put("t_fini_settlement", "t_base_settlement");
		zjsyMap.put("t_fini_purchase", "t_base_purchase");
		// 顶层上游（基础上游）
		// 申报-》基础
		jcsyMap.put("t_decl_income", "t_base_income");
		jcsyMap.put("t_decl_remit", "t_base_remit");
		jcsyMap.put("t_decl_payment", "t_base_payment");
		// 核销-》基础
		jcsyMap.put("t_fini_export", "t_base_income");
		jcsyMap.put("t_fini_remit", "t_base_remit");
		jcsyMap.put("t_fini_payment", "t_base_payment");
		jcsyMap.put("t_fini_dom_export", "t_base_export");
		jcsyMap.put("t_fini_dom_remit", "t_base_dom_remit");
		jcsyMap.put("t_fini_dom_pay", "t_base_dom_pay");
		jcsyMap.put("t_fini_settlement", "t_base_settlement");
		jcsyMap.put("t_fini_purchase", "t_base_purchase");
		// 直接下游
		// 基础-》申报
		zjxyMap.put("t_base_income", "t_decl_income");
		zjxyMap.put("t_base_remit", "t_decl_remit");
		zjxyMap.put("t_base_payment", "t_decl_payment");
		// 基础/申报-》核销
		zjxyMap.put("t_decl_income", "t_fini_export");
		zjxyMap.put("t_decl_remit", "t_fini_remit");
		zjxyMap.put("t_decl_payment", "t_fini_payment");
		zjxyMap.put("t_base_export", "t_fini_dom_export");
		zjxyMap.put("t_base_dom_remit", "t_fini_dom_remit");
		zjxyMap.put("t_base_dom_pay", "t_fini_dom_pay");
		zjxyMap.put("t_base_settlement", "t_fini_settlement");
		zjxyMap.put("t_base_purchase", "t_fini_purchase");
		// 子表-》主表
		zibzhubMap.put("t_invcountrycode_info", "t_company_info");
		zibzhubMap.put("t_company_openinfo", "t_company_info");
		zibzhubMap.put("t_customs_decl", "t_fini_dom_remit");
		zibzhubMap.put("t_customs_decl", "t_fini_remit");
		zibzhubMap.put("t_export_info", "t_fini_dom_export");
		zibzhubMap.put("t_export_info", "t_fini_export");
		zibzhubMap.put("t_stob_sh", "t_stob_wg");
		zibzhubMap.put("t_stob_sh", "t_stob_wr");
		// 最终下游
		zzxyMap.put("t_base_income", "t_fini_export");
		zzxyMap.put("t_base_remit", "t_fini_remit");
		zzxyMap.put("t_base_payment", "t_fini_payment");
		zzxyMap.put("t_base_export", "t_fini_dom_export");
		zzxyMap.put("t_base_dom_remit", "t_fini_dom_remit");
		zzxyMap.put("t_base_dom_pay", "t_fini_dom_pay");
		zzxyMap.put("t_decl_income", "t_fini_export");
		zzxyMap.put("t_decl_remit", "t_fini_remit");
		zzxyMap.put("t_decl_payment", "t_fini_payment");
		zzxyMap.put("t_base_settlement", "t_fini_settlement");
		zzxyMap.put("t_base_purchase", "t_fini_purchase");
		// CFA
		zjxyMap.put("AA", "AR");
		zjxyMap.put("AB", "AR");
		zjxyMap.put("AC", "AR");
		zjxyMap.put("AD", "AR");
		zjxyMap.put("AE", "AR");
		zjxyMap.put("AF", "AR");
		zjxyMap.put("AG", "AR");
		zjxyMap.put("AH", "AR");
		zjxyMap.put("AI", "AR");
		zjxyMap.put("AJ", "AR");
		zjxyMap.put("AK", "AR");
		zjxyMap.put("AQ", "AR");
		zjxyMap.put("AL", "AS");
		zjxyMap.put("AM", "AS");
		zjxyMap.put("AN", "AS");
		zjxyMap.put("AP", "AS");
		zjxyMap.put("BA", "BB,BC");
		zjxyMap.put("CA", "CB");
		zjxyMap.put("DA", "DB");
		zjxyMap.put("EA", "EB");
		zjxyMap.put("FA", "FB,FC");
	}

	public static Map getJcSbMap(){
		return jcSbMap;
	}

	public static Map getJcHxMap(){
		return jcHxMap;
	}

	public static Map getZjsyMap(){
		return zjsyMap;
	}

	public static Map getZjxyMap(){
		return zjxyMap;
	}

	public static Map getZibzhubMap(){
		return zibzhubMap;
	}

	public static Map getJcsyMap(){
		return jcsyMap;
	}

	public static Map getZzxyMap(){
		return zzxyMap;
	}
}
