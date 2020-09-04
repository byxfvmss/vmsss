/**
 * UserInterfaceConfigService Service
 */
package com.cjit.gjsz.interfacemanager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Element;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.cache.SpringContextUtils;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.interfacemanager.model.BasePrimaryKey;
import com.cjit.gjsz.interfacemanager.model.BillClass;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.model.ConfigParameter;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.model.KeyInfo;
import com.cjit.gjsz.interfacemanager.model.LoadData;
import com.cjit.gjsz.interfacemanager.model.TableInfo;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.interfacemanager.util.ImportDateUtil;
import com.cjit.gjsz.system.model.Mts;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.RoleUser;
import com.cjit.gjsz.system.service.OrganizationService;

/**
 * @author huboA
 */
public class UserInterfaceConfigServiceImpl extends GenericServiceImpl
		implements UserInterfaceConfigService {

	private OrganizationService organizationService;
	protected static CacheManager cacheManager;

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = (CacheManager) SpringContextUtils
				.getBean("cacheManager");
	}

	/*
	 * private Cache cache; public void setCache(Cache cache){ this.cache =
	 * cache; }
	 */
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public List getTableInfos(TableInfo tableInfo) {
		Map map = new HashMap();
		map.put("tableInfo", tableInfo);
		return this.find("getTableInfos", map);
	}

	public TableInfo getTableInfo(String tableId) {
		Map map = new HashMap();
		TableInfo tableInfo = new TableInfo();
		tableInfo.setTableId(tableId);
		map.put("tableInfo", tableInfo);
		List list = this.find("getTableInfo", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (TableInfo) list.get(0);
		}
		return null;
	}

	public List getTableInfosByType(TableInfo tableInfo) {
		Map map = new HashMap();
		map.put("tableInfo", tableInfo);
		return this.find("getTableInfosByType", map);
	}

	public List getColumnInfos(ColumnInfo columnInfo) {
		Map map = new HashMap();
		map.put("columnInfo", columnInfo);
		return this.find("getColumnInfos", map);
	}

	public List getColumnInfosByInsert(ColumnInfo columnInfo) {
		Map map = new HashMap();
		map.put("columnInfo", columnInfo);
		return this.find("getColumnInfosByInsert", map);
	}

	public Dictionary getDictionary(int dictionaryId) {
		Map map = new HashMap();
		map.put("dictionaryId", new Integer(dictionaryId));
		List list = this.find("getDictionary", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (Dictionary) list.get(0);
		}
		return null;
	}

	public void updateDictionary(Dictionary dictionary) {
		Map map = new HashMap();
		map.put("dictionary", dictionary);
		this.update("updateDictionary", map);
	}

	public List getDictionarys(ColumnInfo columnInfo) {
		Map map = new HashMap();
		ColumnInfo tmp = new ColumnInfo();
		tmp.setTableId("%" + columnInfo.getTableId() + "%");
		map.put("columnInfo", tmp);
		return this.find("getDictionaryNames", map);
	}
	
	public List<Dictionary> getDictionarys(String codeType, String codeSym){
		Map map = new HashMap();
		map.put("codeType", codeType);
		map.put("codeSym", codeSym);
		return this.find("getDictionarys", map);
	}
	public List<Dictionary> getDictionarys1(String codeType, String codeSym,String backupnum){
		Map<String,String> map = new HashMap<String,String>();
		map.put("codeType", codeType);
		map.put("codeSym", codeSym);
		map.put("backupNum", backupnum);
		return this.find("getDictionarys", map);
	}
	public List getDictionarys(ColumnInfo columnInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		ColumnInfo tmp = new ColumnInfo();
		if (StringUtil.isNotEmpty(columnInfo.getTableId())) {
			tmp.setTableId("%" + columnInfo.getTableId() + "%");
		}
		if (StringUtil.isNotEmpty(columnInfo.getTypeName())) {
			tmp.setTypeName("%" + columnInfo.getTypeName() + "%");
		}
		map.put("columnInfo", tmp);
		return this.find("getDictionarys", map, paginationList);
	}

	public List getAllDictionarys(ColumnInfo columnInfo) {
		Map map = new HashMap();
		if (StringUtil.isNotEmpty(columnInfo.getTableId())) {
			ColumnInfo tmp = new ColumnInfo();
			tmp.setTableId("%" + columnInfo.getTableId() + "%");
			map.put("columnInfo", tmp);
		}
		return this.find("getDictionarys", map);
	}

	public List getAllDictionarysByCache() {
		CacheabledMap cache = (CacheabledMap) cacheManager
				.getCacheObject("AllDictionary");
		Map params = null;
		if (cache != null) {
			params = (Map) cache.get("PAPAMETER_CACHE_MAP");
			if (params != null) {
				Element element = (Element) params.get("PARAM_SYS_THEME_PATH");
				if (element == null) {
					Map map = new HashMap();
					List list = this.find("getDictionarys", map);
					element = new Element("AllDictionary", list);
					return (List) element.getValue();
				}
			}
		}
		return null;
	}

	public List getAllDictionarys() {
		Map map = new HashMap();
		return this.find("getDictionarys", map);
	}

	public List getSwitchCodesByCache() {
		// Element element = cache.get("AllSwitchCodes");
		// if(element == null){
		// Map map = new HashMap();
		// List list = this.find("getSwitchCodes", map);
		// element = new Element("AllSwitchCodes", list);
		// cache.put(element);
		// }
		// return (List) element.getValue();
		return null;
	}

	public void removeDictionarysByCache() {
		// cache.remove("AllDictionary");
		getAllDictionarysByCache();
	}

	public ColumnInfo getColumnInfo(ColumnInfo columnInfo) {
		Map map = new HashMap();
		map.put("columnInfo", columnInfo);
		List list = this.find("getColumnInfo", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (ColumnInfo) list.get(0);
		}
		return null;
	}

	public ColumnInfo getColumnInfoByUserInterface(ColumnInfo columnInfo) {
		Map map = new HashMap();
		map.put("columnInfo", columnInfo);
		List list = this.find("getColumnInfoByUserInterface", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (ColumnInfo) list.get(0);
		}
		return null;
	}

	public void updateColumnInfo(ColumnInfo columnInfo) {
		Map map = new HashMap();
		map.put("columnInfo", columnInfo);
		this.update("updateColumnInfo", map);
	}

	public TableInfo getTableInfosByTypeId(TableInfo tableInfo) {
		Map map = new HashMap();
		map.put("tableInfo", tableInfo);
		List list = this.find("getTableInfosByTypeId", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (TableInfo) list.get(0);
		}
		return null;
	}

	public String createRptNoPrefix(String orgId) {
		Organization org = new Organization();
		org.setId(orgId);
		org = organizationService.getOrganization(org);
		if (org == null) {
			return "ErrorOrgId";
		} else if (org.getCustomId() != null
				&& org.getCustomId().length() != 12) {
			return "ErrorOrgId";
		}
		return org.getCustomId();
	}

	public String createAutokey(String objCode, String buocMonth,
			String fileType) {
		if (StringUtil.isEmpty(objCode) || StringUtil.isEmpty(buocMonth)
				|| StringUtil.isEmpty(fileType)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		// 截取后7位取最大值函数
		String intercept4Bit = " MAX(SUBSTR("
				+ DataUtil.getRptNoColumnIdByFileType(fileType) + " ,LENGTH("
				+ DataUtil.getRptNoColumnIdByFileType(fileType) + ")-6,7))";
		sb.append("SELECT " + intercept4Bit + " FROM "
				+ DataUtil.getTableIdByFileType(fileType));
		sb.append(" where ").append(
				DataUtil.getRptNoColumnIdByFileType(fileType)).append(
				" LIKE '" + objCode
						+ DataUtil.getRptCodeByFileType(fileType, 5)
						+ buocMonth + "%' ");
		sb.append(" and ");
		sb.append(" ( ");
		sb.append("  cast(SUBSTR("
				+ DataUtil.getRptNoColumnIdByFileType(fileType)
				+ ", 24, 7) as int) between 0000001 and 9999999 ");
		sb.append(" ) ");
		Map map = new HashMap();
		map.put("value", sb.toString());
		List list = find("getLastFlowNumber", map);
		String key = null;
		if (CollectionUtil.isNotEmpty(list)) {
			key = (String) list.iterator().next();
			if (StringUtil.equalsIgnoreCase("null", key)) {
				key = null;
			}
		}
		KeyInfo keyInfo = new KeyInfo();
		keyInfo.setMaxType(key);
		String snoCode = objCode + DataUtil.getRptCodeByFileType(fileType, 5)
				+ buocMonth + ImportDateUtil.getKey(keyInfo);
		return snoCode;
	}

	public String createAutokey(KeyInfo keyInfo) {
		if (!StringUtil.isEmpty(keyInfo.getImportType())
				&& !StringUtil.equalsIgnoreCase(keyInfo.getImportType(), "A")) {
			return "";
		}
		try {
			TableInfo tableInfo = getTableInfo(keyInfo.getTableId());
			keyInfo.setPersonalCode(tableInfo.getPersonalCode());
			keyInfo.setPublicCode(tableInfo.getPublicCode());
			Organization org = new Organization();
			org.setId(keyInfo.getOrgId());
			String orgCustomId = this.createRptNoPrefix(keyInfo.getOrgId());
			if (StringUtil.isNotEmpty(orgCustomId)
					&& !"ErrorOrgId".equals(orgCustomId)) {
				org.setCustomId(orgCustomId);
				StringBuffer sb = new StringBuffer();
				// 得到申报号最后4位流水号
				String strLastFlowNumber = this.getLastFlowNumber(org, keyInfo
						.getCurrentDate(), keyInfo);
				if (strLastFlowNumber == null) {
					return null;
				}
				sb.append(keyInfo.getFileType() + orgCustomId
						+ keyInfo.getCurrentDate() + strLastFlowNumber);
				return sb.toString();
			} else {
				return "ErrorOrgId";
			}
		} catch (RuntimeException re) {
			return "ErrorOrgId";
		}
	}

	public String getLastFlowNumber(Organization org, String currentDate,
			KeyInfo keyInfo) {
		StringBuffer sb = new StringBuffer();
		// 截取后6位取最大值函数
		String intercept4Bit = " MAX(SUBSTR(" + keyInfo.getRptNoColumnId()
				+ " ,LENGTH(" + keyInfo.getRptNoColumnId() + ")-5,6))";
		sb.append("SELECT " + intercept4Bit + " FROM " + keyInfo.getTableId());
		sb.append(" where ").append(keyInfo.getRptNoColumnId()).append(
				" LIKE '" + keyInfo.getFileType() + org.getCustomId()
						+ currentDate + "%' ");
		sb.append(" and ");
		sb.append(" ( ");
		sb.append("  cast(SUBSTR(" + keyInfo.getRptNoColumnId()
				+ ", 23, 6) as int) between 000001 and 999999 ");
		sb.append(" ) ");
		Map map = new HashMap();
		map.put("value", sb.toString());
		List list = find("getLastFlowNumber", map);
		String key = null;
		if (CollectionUtil.isNotEmpty(list)) {
			key = (String) list.iterator().next();
			if (StringUtil.equalsIgnoreCase("null", key)) {
				key = null;
			}
		}
		keyInfo.setMaxType(key);
		return ImportDateUtil.getKey(keyInfo);
	}

	public boolean isHasBusinessKey(String tableId, String businessId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("busniessId", businessId);
		Long size = this.getRowCount("getBusinessKey", map);
		if (size != null && size.longValue() > 0) {
			return true;
		}
		return false;
	}

	public boolean isHasSubKey(String tableId, String subKey, String subId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("subKey", subKey);
		map.put("subId", subId);
		Long size = this.getRowCount("getSubKey", map);
		if (size != null && size.longValue() > 0) {
			return true;
		}
		return false;
	}

	public Long subKeySize(String tableId, String subKey, String subId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("subKey", subKey);
		map.put("subId", subId);
		return this.getRowCount("getSubKey", map);
	}

	public Long subKeySize(String tableId, String subKey, String subId,
			String bussKey, String bussId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("subKey", subKey);
		map.put("subId", subId);
		map.put("bussKey", bussKey);
		map.put("bussId", bussId);
		return this.getRowCount("getSubKeyExt", map);
	}

	public List subList(String column, String tableId, String subKey,
			String subId, String bussKey, String bussId) {
		Map map = new HashMap();
		map.put("column", column);
		map.put("tableId", tableId);
		map.put("subKey", subKey);
		map.put("subId", subId);
		map.put("bussKey", bussKey);
		map.put("bussId", bussId);
		return this.find("getSubList", map);
	}

	public List findTableRelation(String tableId) {
		Map map = new HashMap();
		map.put("subTableId", tableId);
		return this.find("findTableRelation", map);
	}

	public String getBaseBusinessKey(String tableId, String businessId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("busniessId", businessId);
		List list = this.find("getBusinessKeyInfo", map);
		if (CollectionUtil.isNotEmpty(list)) {
			String reportId = (String) list.iterator().next();
			if (StringUtil.isNotBlank(reportId)
					&& !(StringUtil.equalsIgnoreCase("null", reportId))) {
				return reportId;
			}
		}
		return null;
	}

	public String getCompanyKey(String tableId, String businessId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("busniessId", businessId);
		List list = this.find("getCompanyInfo", map);
		if (CollectionUtil.isNotEmpty(list)) {
			String reportId = (String) list.iterator().next();
			if (StringUtil.isNotBlank(reportId)
					&& !(StringUtil.equalsIgnoreCase("null", reportId))) {
				return reportId;
			}
		}
		return null;
	}

	public BasePrimaryKey getBaseRecord(String tableId, String pk,
			String businessId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("pk", pk);
		map.put("busniessId", businessId);
		List list = this.find("getBaseRecord", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (BasePrimaryKey) list.get(0);
		}
		return null;
	}

	public List getImportList(LoadData loadData) {
		Map map = new HashMap();
		map.put("loadData", loadData);
		return this.find("getImportList", map);
	}

	public void saveLoadData(LoadData loadData) {
		Map map = new HashMap();
		map.put("loadData", loadData);
		this.save("saveLoadData", map);
	}

	public void updateLoadData(LoadData loadData) {
		Map map = new HashMap();
		map.put("loadData", loadData);
		this.save("updateLoadData", map);
	}

	public void deleteLoadData(LoadData loadData) {
		Map map = new HashMap();
		map.put("loadData", loadData);
		this.delete("deleteLoadData", map);
	}

	public LoadData getLoadData(LoadData loadData) {
		Map map = new HashMap();
		map.put("loadData", loadData);
		List list = this.find("getLoadData", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (LoadData) list.get(0);
		}
		return null;
	}

	public List getLoadDatas(LoadData loadData) {
		Map map = new HashMap();
		map.put("loadData", loadData);
		return this.find("getLoadDatas", map);
	}

	/**
	 * 将字典信息以key:tableId,value:(key:type_valueStandardNum,value:name)
	 * 的形式重新包装到MAP中返回
	 * 
	 * @return
	 */
	public Map[] initDictionaryMap() {
		/*
		 * HashMap dictionaryMap = new HashMap(); String[] tableId = null; Map
		 * tableMap = null; HashMap dictionaryMap2 = new HashMap(); Map
		 * tableMap2 = null; List tableTypeList = null; List OTHERS = new
		 * ArrayList(); List COUNTRYS = new ArrayList(); List CURRENCY = new
		 * ArrayList(); // 缓存字典信息 List dictionarys =
		 * this.getAllDictionarysByCache(); //20100531 彰化要求对地区代码排序
		 * System.out.println("排序前dictionarys:" + dictionarys.size()); for
		 * (Iterator i = dictionarys.iterator(); i.hasNext();) { Dictionary
		 * dictionary = (Dictionary) i.next(); if
		 * ("COUNTRY".equals(dictionary.getType())) { // 国家排序
		 * COUNTRYS.add(dictionary); }else
		 * if("CURRENCY".equals(dictionary.getType())) { // 国家排序
		 * COUNTRYS.add(dictionary); }else { OTHERS.add(dictionary); } }
		 * System.out.println("OTHERS:" + OTHERS.size());
		 * System.out.println("COUNTRYS:" + COUNTRYS.size());
		 * Collections.sort(COUNTRYS, new java.util.Comparator() { public int
		 * compare(Object o1, Object o2) { Dictionary dic1 = (Dictionary) o1;
		 * Dictionary dic2 = (Dictionary) o2; return
		 * dic1.getValueStandardLetter().compareTo(dic2.getValueStandardLetter()); }
		 * }); Collections.sort(CURRENCY, new java.util.Comparator() { public
		 * int compare(Object o1, Object o2) { Dictionary dic1 = (Dictionary)
		 * o1; Dictionary dic2 = (Dictionary) o2; return
		 * dic1.getValueStandardLetter().compareTo(dic2.getValueStandardLetter()); }
		 * }); OTHERS.addAll(COUNTRYS); OTHERS.addAll(CURRENCY);
		 * System.out.println("排序后dictionarys:" + OTHERS.size()); dictionarys =
		 * OTHERS; //排序后的 for (Iterator i = dictionarys.iterator();
		 * i.hasNext();) { Dictionary dictionary = (Dictionary) i.next(); if
		 * (dictionary.getTableId() != null) { tableId =
		 * dictionary.getTableId().split(","); for (int j = 0; j <
		 * tableId.length; j++) { tableId[j] = tableId[j].trim(); tableMap =
		 * (HashMap) dictionaryMap.get(tableId[j]); if (tableMap == null) {
		 * tableMap = new HashMap(); dictionaryMap.put(tableId[j], tableMap); }
		 * tableMap.put(dictionary.getType() + "_" +
		 * dictionary.getValueStandardNum(), dictionary.getName()); tableMap2 =
		 * (HashMap) dictionaryMap2.get(tableId[j]); if (tableMap2 == null) {
		 * tableMap2 = new HashMap(); dictionaryMap2.put(tableId[j], tableMap2); }
		 * tableTypeList = (ArrayList) tableMap2.get(dictionary.getType()); if
		 * (tableTypeList == null) { tableTypeList = new ArrayList();
		 * tableMap2.put(dictionary.getType(), tableTypeList); }
		 * tableTypeList.add(dictionary); } } } return new HashMap[] {
		 * dictionaryMap, dictionaryMap2 };
		 */
		return AppContextHolder.initDictionaryMap();
	}

	/**
	 * 将字典信息以key:tableId,value:(key:type_valueStandardNum,value:name)
	 * 的形式重新包装到MAP中返回
	 * 
	 * @return
	 */
	public Map[] refreshDictionaryMap() {
		return AppContextHolder.refreshDictionaryMap();
	}

	/**
	 * <p>
	 * 方法名称: initConfigParameters|描述: 初始化数据库中config参数配置信息map
	 * </p>
	 * 
	 * @return Map
	 */
	public Map initConfigParameters() {
		Map map = new HashMap();
		try {
			List list = this.find("findConfigParameters", new HashMap());
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ConfigParameter config = (ConfigParameter) list.get(i);
					map.put(config.getConfigName(), config.getConfigValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}

	public Map initConfigMts() {
		Map map = new HashMap();
		try {
			List list = this.find("findConfigMts", new HashMap());
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Mts config = (Mts) list.get(i);
					map.put(config.getRptTitle(), config);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}

	public void updateConfig(String configName, String value) {
		Map param = new HashMap();
		param.put("configName", configName);
		param.put("configValue", value);
		this.update("updateTConfigParameter", param);
	}

	public void updateConfigMts(Mts mtsVO) {
		Map param = new HashMap();
		param.put("rptTitle", mtsVO.getRptTitle());
		List t = this.find("findConfigMts", param);
		param.clear();
		param.put("mts", mtsVO);
		if (t.size() > 0)
			this.update("updateConfigMts", param);
		else
			this.save("insertConfigMts", param);
	}

	public List findVRoleUser(RoleUser roleUser, String searchUser) {
		Map map = new HashMap();
		map.put("roleUser", roleUser);
		map.put("searchUser", searchUser);
		return this.find("findVRoleUser", map);
	}

	public List findRelaTablesByRoleId(String roleId) {
		Map map = new HashMap();
		map.put("roleId", roleId);
		return this.find("findRelaTables", map);
	}

	public void deleteRelaTables(String objId, String tableId, String objType) {
		Map map = new HashMap();
		map.put("objId", objId);
		map.put("tableId", tableId);
		map.put("objType", objType);
		this.delete("deleteRelaTables", map);
	}

	public void insertRelaTables(String insertColumns, String insertValues) {
		Map map = new HashMap();
		map.put("insertColumns", insertColumns);
		map.put("insertValues", insertValues);
		this.save("insertRelaTables", map);
	}

	
	public BillClass getBillClass(String billType) {
		Map paraMap = new HashMap();
		paraMap.put("billType", billType);
		List list = this.find("getBillType", paraMap);
		BillClass billClass = new BillClass();
		billClass = (BillClass) list.get(0);
		return billClass;
	}

	public List getBillClassList() {
		// TODO Auto-generated method stub
		return this.find("getBillTypeList", null);
	}


}

class CrossModel {

	char start;
	char end;
}
