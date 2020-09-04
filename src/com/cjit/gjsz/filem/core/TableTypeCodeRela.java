/**
 * 
 */
package com.cjit.gjsz.filem.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yulubin
 */
public class TableTypeCodeRela{

	private static Map rela = new HashMap();
	static{
		// BOP
		rela.put("t_base_income", "A");
		rela.put("t_base_remit", "B");
		rela.put("t_base_payment", "C");
		rela.put("t_base_export", "D");
		rela.put("t_base_dom_remit", "E");
		rela.put("t_base_dom_pay", "F");
		rela.put("t_decl_income", "G");
		rela.put("t_decl_remit", "H");
		rela.put("t_decl_payment", "K");
		rela.put("t_fini_export", "M");
		rela.put("t_fini_remit", "N");
		rela.put("t_fini_payment", "P");
		rela.put("t_fini_dom_remit", "Q");
		rela.put("t_fini_dom_export", "R");
		rela.put("t_fini_dom_pay", "S");
		rela.put("t_company_info", "U");
		// JSH
		rela.put("t_base_settlement", "D");
		rela.put("t_base_purchase", "E");
		rela.put("t_fini_settlement", "F");
		rela.put("t_fini_purchase", "G");
	}

	public static Map getRela(){
		return rela;
	}
}
