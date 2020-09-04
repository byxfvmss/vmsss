package com.cjit.common.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class StringComparator implements Comparator{

	public static final String base = "t_base_income,t_base_remit,t_base_payment,t_base_export,t_base_dom_remit,t_base_dom_pay,t_company_info,t_base_settlement,t_base_purchase";
	public static final String declare = "t_decl_income,t_decl_remit,t_decl_payment";
	public static final String finance = "t_fini_export,t_fini_remit,t_fini_payment,t_fini_dom_remit,t_fini_dom_pay,t_fini_dom_export,t_fini_settlement,t_fini_purchase";

	public int compare(Object o1, Object o2){
		String str1 = (String) o1;
		String str2 = (String) o2;
		if(base.indexOf(str1) > -1){
			return -1;
		}
		if(declare.indexOf(str1) > -1){
			if(base.indexOf(str2) > -1){
				return 1;
			}else{
				return -1;
			}
		}
		if(finance.indexOf(str1) > -1){
			if(finance.indexOf(str2) > -1){
				return -1;
			}else{
				return 1;
			}
		}
		return 0;
	}

	public static void main(String[] args){
		Map map = new HashMap();
		map.put("t_fini_remit", new Product("1"));
		map.put("t_base_remit", new Product("2"));
		map.put("t_decl_income", new Product("3"));
		map.put("t_base_dom_remit", new Product("4"));
		map.put("t_fini_dom_pay", new Product("5"));
		map.put("t_base_payment", new Product("6"));
		map.put("t_base_income", new Product("7"));
		System.out.println(map);
		TreeMap map2 = new TreeMap(new StringComparator());
		map2.putAll(map);
		for(Iterator i = map2.entrySet().iterator(); i.hasNext();){
			Map.Entry entry = (Map.Entry) i.next();
			String key = (String) entry.getKey();
			System.out.println("key is:" + key);
			Product product = (Product) entry.getValue();
			System.out.println("product id is:" + product.getId());
			System.out.println("=====================================");
		}
		// String str =(String) map2.firstKey();
		// map2.entrySet()
		// Product product = map2.p
		// System.out.println("product id is:" + str);
		// System.out.println("product id is:" + product.getId());
		System.out.println(map2);
	}

	static class Product{

		private String id;

		public String getId(){
			return id;
		}

		public void setId(String id){
			this.id = id;
		}

		public Product(String id){
			this.id = id;
		}
	}
}
