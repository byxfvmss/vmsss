package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.AccTitle;
import com.cjit.vms.system.model.TransAccInfo;
import com.cjit.vms.system.service.AccTitleService;

public class AccTitleServiceImpl extends GenericServiceImpl implements
		AccTitleService {

	public List findAccTitleList(AccTitle accTitle,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("accTitle", accTitle);
		if (paginationList != null)
			return find("findAccTitleList", map, paginationList);
		else
			return find("findAccTitleList", map);
	}

	public void saveAccTitle(AccTitle accTitle) {
		Map map = new HashMap();
		map.put("accTitle", accTitle);
		save("saveAccTitle", map);
	}

	public void deleteAccTitle(String accTitleId) {
		AccTitle accTitle = new AccTitle();
		accTitle.setAccTitleId(accTitleId);
		Map map = new HashMap();
		map.put("accTitle", accTitle);
		delete("deleteAccTitle", map);
	}
	
	public void deleteAccTitleByCode(String accTitleCode) {
		AccTitle accTitle = new AccTitle();
		accTitle.setAccTitleCode(accTitleCode);
		Map map = new HashMap();
		map.put("accTitle", accTitle);
		delete("deleteAccTitleByCode", map);
	}

	public void deleteAll(List list) {
		// TODO Auto-generated method stub

	}

	public AccTitle getAccTitleById(String accTitleId) {

		AccTitle accTitle = new AccTitle();
		accTitle.setAccTitleId(accTitleId);
		Map map = new HashMap();
		map.put("accTitle", accTitle);
		List list = find("findAccTitleList", map);
		if (list != null && list.size() > 0)
			accTitle = (AccTitle) list.get(0);
		return accTitle;
	}
	
	public AccTitle getAccTitleByCode(String accTitleCode) {

		AccTitle accTitle = new AccTitle();
		accTitle.setAccTitleCode(accTitleCode);
		Map map = new HashMap();
		map.put("accTitle", accTitle);
		List list = find("findAccTitleList", map);
		if (list != null && list.size() > 0)
			accTitle = (AccTitle) list.get(0);
		return accTitle;
	}

	public void updateAccTitle(AccTitle accTitle) {
		Map map = new HashMap();
		map.put("accTitle", accTitle);
		update("updateAccTitle", map);
	}

	public List findTransAccList(TransAccInfo transAccInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("transAccInfo", transAccInfo);
		if (paginationList == null)
			return find("findTransAccList", map);
		else
			return find("getTransAccList", map, paginationList);
	}
	
	

	public List findTransAccList(TransAccInfo transAccInfo) {
		Map map = new HashMap();
		map.put("transAccInfo", transAccInfo);
		return find("getTransAccList", map);
	}

	public void saveTransAcc(TransAccInfo transAccInfo) {
		Map map = new HashMap();
		map.put("transAccInfo", transAccInfo);
		save("saveTransAcc", map);
	}

}
