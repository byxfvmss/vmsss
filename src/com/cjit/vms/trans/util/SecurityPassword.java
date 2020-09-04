package com.cjit.vms.trans.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cjit.vms.trans.util.SecurityRuleLoader;

public class SecurityPassword {
	
	private static final Log log = LogFactory.getLog(SecurityPassword.class);
	private static final Map ruleMap = new LinkedHashMap();
	static {
		if (!SecurityRuleLoader.isValid()) {
			log.info("set to default password rules [a-z]+[A-Z]+[\\d]+[@#$%]+");
			ruleMap.put("[a-z]+", "必须包含小写字母");
			ruleMap.put("[A-Z]+", "必须包含大写字母");
			ruleMap.put("[\\d]+", "必须包含数字");
			ruleMap.put("[@#$%]+", "必须包含@#$%");
		} else {
			buildRules();
		}
	}
	
	public static void buildRules() {
		for (Iterator iterator = SecurityRuleLoader.getRules().iterator(); iterator.hasNext();) {
			SecurityRule rule = (SecurityRule) iterator.next();
			ruleMap.put(rule.getValue(), rule.getMessage());
		}
	}
	
	public static boolean isSecurity(String s) {
		boolean a = true;
		for (Iterator iterator = ruleMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			a &= Pattern.compile(entry.getKey().toString()).matcher(s).find();
		}
		return a;
	}

	public static String securityMessage() {
		return securityMessage("\n");
	}
	
	public static String securityMessage(String separator) {
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = ruleMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			sb.append(entry.getValue() + separator);
		}
		return sb.toString().endsWith(separator) ? sb.substring(0, sb.length() - separator.length()) : sb.toString();
	}
	
	/**
	 * 敏感字符过滤 
	 */
	public static String filterStr(String params){
		String str = "";
 		try{
			if(StringUtils.isNotEmpty(params)){
 				str=params.replaceAll("'", "''").replaceAll("/|<|>|%|&|;|(|)|/+", "");
			}
		}catch(Exception e){
 			log.error(e.getMessage());
		}
  		return str;
	}
}
