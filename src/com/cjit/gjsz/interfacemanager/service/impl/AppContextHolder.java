package com.cjit.gjsz.interfacemanager.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.cjit.common.util.SpringContextUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;

public class AppContextHolder implements InitializingBean {

	private static final Log log = LogFactory.getLog(AppContextHolder.class);
	protected static UserInterfaceConfigService userInterfaceConfigService = (UserInterfaceConfigService) SpringContextUtil
			.getBean("userInterfaceConfigService");
	private static Map dictionaryMap = new HashMap();
	private static Map dictionaryMap2 = new HashMap();
	private static List dictionarys = new ArrayList();

	public static Map[] initDictionaryMap() {
		if (dictionaryMap.size() == 0 || dictionaryMap2.size() == 0) {
			register();
		}
		return new Map[] { dictionaryMap, dictionaryMap2 };
	}

	public static Map[] refreshDictionaryMap() {
		dictionaryMap.clear();
		dictionaryMap2.clear();
		register();
		return new Map[] { dictionaryMap, dictionaryMap2 };
	}

	public static List getAllDictionarysByCache() {
		if (dictionarys.size() == 0) {
			register();
		}
		return dictionarys;
	}

	/**
	 * 系统启动时加载数据字典信息
	 */
	public void afterPropertiesSet() throws Exception {
		if (dictionaryMap.size() == 0 || dictionaryMap2.size() == 0) {
			register();
		}
	}

	public static void register() {
		String[] tableId = null;
		Map tableMap = null;
		Map tableMap2 = null;
		Map tableMap3 = null;
		List tableTypeList = null;
		List OTHERS = new ArrayList();
		List COUNTRYS = new ArrayList();
		List CURRENCY = new ArrayList();
		// 缓存字典信息
		// VMS-CANCEL
		//dictionarys = userInterfaceConfigService.getAllDictionarys();
		dictionarys = new ArrayList();
		// 20100531 彰化要求对地区代码排序
		log.info("排序前dictionarys:" + dictionarys.size());
		for (Iterator i = dictionarys.iterator(); i.hasNext();) {
			Dictionary dictionary = (Dictionary) i.next();
			if ("COUNTRY".equals(dictionary.getType())) {
				// 国家排序
				COUNTRYS.add(dictionary);
			} else if ("CURRENCY".equals(dictionary.getType())) {
				// 币种排序
				CURRENCY.add(dictionary);
			} else {
				OTHERS.add(dictionary);
			}
		}
		log.info("OTHERS:" + OTHERS.size());
		log.info("COUNTRYS:" + COUNTRYS.size());
		Collections.sort(CURRENCY, new java.util.Comparator() {

			public int compare(Object o1, Object o2) {
				Dictionary dic1 = (Dictionary) o1;
				Dictionary dic2 = (Dictionary) o2;
				return dic1.getValueStandardLetter().compareTo(
						dic2.getValueStandardLetter());
			}
		});
		OTHERS.addAll(COUNTRYS);
		OTHERS.addAll(CURRENCY);
		log.info("排序后dictionarys:" + OTHERS.size());
		dictionarys = OTHERS;
		// 排序后的
		for (Iterator i = dictionarys.iterator(); i.hasNext();) {
			Dictionary dictionary = (Dictionary) i.next();
			if (dictionary.getTableId() != null) {
				tableId = dictionary.getTableId().split(",");
				for (int j = 0; j < tableId.length; j++) {
					tableId[j] = tableId[j].trim();
					tableMap = (HashMap) dictionaryMap.get(tableId[j]);
					if (tableMap == null) {
						tableMap = new HashMap();
						dictionaryMap.put(tableId[j], tableMap);
					}
					tableMap.put(dictionary.getType() + "_"
							+ dictionary.getValueStandardNum(), dictionary
							.getName());
					tableMap2 = (HashMap) dictionaryMap2.get(tableId[j]);
					if (tableMap2 == null) {
						tableMap2 = new HashMap();
						dictionaryMap2.put(tableId[j], tableMap2);
					}
					tableTypeList = (ArrayList) tableMap2.get(dictionary
							.getType());
					if (tableTypeList == null) {
						tableTypeList = new ArrayList();
						tableMap2.put(dictionary.getType(), tableTypeList);
					}
					tableTypeList.add(dictionary);
				}
			}
		}
	}
}