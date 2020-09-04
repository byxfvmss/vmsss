package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.model.SearchModel;

public class SearchServiceImpl extends GenericServiceImpl implements
		SearchService {

	private UserInterfaceConfigService userInterfaceConfigService;

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public String getKey(String dataKey) {
		List dictionarys = userInterfaceConfigService
				.getAllDictionarysByCache();
		if (CollectionUtil.isNotEmpty(dictionarys)) {
			for (int i = 0; i < dictionarys.size(); i++) {
				Dictionary dictionary = (Dictionary) dictionarys.get(i);
				if (StringUtil.equalsIgnoreCase(dictionary
						.getValueStandardNum(), dataKey)) { // 如果找到币种
					return dictionary.getName();
				}
			}
			return dataKey;
		}
		throw new RuntimeException("字典表不能为空");
	}

	public String getBackupNum(String dataKey) {
		List dictionarys = userInterfaceConfigService
				.getAllDictionarysByCache();
		if (CollectionUtil.isNotEmpty(dictionarys)) {
			for (int i = 0; i < dictionarys.size(); i++) {
				Dictionary dictionary = (Dictionary) dictionarys.get(i);
				if (StringUtil.equalsIgnoreCase(dictionary
						.getValueStandardNum(), dataKey)) { // 如果找到币种
					return dictionary.getBackupNum();
				}
			}
			return dataKey;
		}
		throw new RuntimeException("字典表不能为空");
	}

	public List search(SearchModel searchModel, PaginationList paginationList) {
		Map map = new HashMap();
		// map.put("authority", searchModel);
		return find((String) SearchModel.SEARCH_MAP.get(searchModel
				.getTableId()), map, paginationList);
	}

	public List search(SearchModel searchModel) {
		Map map = new HashMap();
		map.put("searchModel", searchModel);
		String sql = (String) SearchModel.SEARCH_MAP.get(searchModel
				.getTableId());
		if (sql != null)
			return find(sql, map);
		else
			return null;
	}

	public long getCount(SearchModel searchModel) {
		Map map = new HashMap();
		map.put("searchModel", searchModel);
		Long size = (Long) getRowCount((String) SearchModel.SEARCH_MAP
				.get(searchModel.getTableId())
				+ "Count", map);
		if (size != null) {
			return size.longValue();
		}
		return 0;
	}

	public List getChildren(String tableId, String businessid) {
		Map map = new HashMap();
		map.put("businessids", "'" + businessid + "'");
		return find((String) SearchModel.SEARCH_MAP.get(tableId), map);
	}

	public List getCfaChildren(String tableId, String businessId) {
		Map map = new HashMap();
		String str = (String) SearchModel.SEARCH_MAP.get(tableId);
		if (businessId == null) {
			return null;
		}
		map.put("businessId", businessId);
		return find(str, map);
	}

	public List getFalChildren(String tableId, String businessId) {
		Map map = new HashMap();
		String str = (String) SearchModel.SEARCH_MAP.get(tableId);
		if (businessId == null) {
			return null;
		}
		map.put("businessId", businessId);
		return find(str, map);
	}

	public Object getDataVerifyModel(String tableId, String businessid) {
		Map map = new HashMap();
		map.put("ids", "'" + businessid + "'");
		String str = (String) SearchModel.SEARCH_MAP.get(tableId);
		List list = find(str, map);
		if (CollectionUtil.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	public Object getDataVerifyModel(String tableId, String rptNo,
			String businessNo) {
		Map map = new HashMap();
		map.put("rptNo", rptNo);
		map.put("businessNo", businessNo);
		String str = (String) SearchModel.SEARCH_MAP.get(tableId);
		List list = find(str, map);
		if (CollectionUtil.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	public long getDataVerifyModelSize(String tableId, String businessid) {
		Map map = new HashMap();
		map.put("businessids", "'" + businessid + "'");
		Long size = (Long) getRowCount((String) SearchModel.SEARCH_MAP
				.get(tableId)
				+ "Count", map);
		if (size != null) {
			return size.longValue();
		}
		return 0;
	}

	public long getVerifySize(String sql) {
		Map map = new HashMap();
		map.put("value", sql);
		Long size = (Long) getRowCount("getKeysCount", map);
		if (size != null) {
			size.longValue();
		}
		return 0;
	}

	public List getCompanyInfos(String businessId, String custCode,
			String instCode) {
		Map map = new HashMap();
		map.put("businessId", businessId);
		map.put("custCode", custCode);
		map.put("instCode", instCode);
		return find("getCompanyInfosByCustCode", map);
	}

	public BigDecimal getFalSumBigDecimal(SearchModel searchModel) {
		Map map = new HashMap();
		map.put("searchModel", searchModel);
		if (StringUtil.isNotEmpty(searchModel.getTableId())
				&& StringUtil.isNotEmpty(searchModel.getSumColumn()))
			return (BigDecimal) findForObject("getFalSumBigDecimal", map);
		else
			return null;
	}
}
