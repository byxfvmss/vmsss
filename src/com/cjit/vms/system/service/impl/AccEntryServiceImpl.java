package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.AccEntry;
import com.cjit.vms.system.service.AccEntryService;

public class AccEntryServiceImpl extends GenericServiceImpl implements
		AccEntryService {

	public List findAccEntryList(AccEntry accEntry,
			PaginationList paginationList) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("accEntry", accEntry);
		if (null == paginationList) {
			return find("findAccEntryList", map);
		}
		return find("findAccEntryList", map, paginationList);
	}

	public void insertAccEntry(AccEntry accEntry) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		accEntry.setCdFlag("D");
		accEntry.setIsReverse("N");
		accEntry.setAccTitleCode(accEntry.getAccTitD());
		accEntry.setTransNumTyp(accEntry.getTransNumTypD());
		map.put("accEntry", accEntry);

		save("insertAccEntry", map);
		accEntry.setIsReverse("Y");
		accEntry.setCdFlag("C");
		save("insertAccEntry", map);

		map.clear();
		accEntry.setCdFlag("C");
		accEntry.setIsReverse("N");
		accEntry.setAccTitleCode(accEntry.getAccTitC1());
		accEntry.setTransNumTyp(accEntry.getTransNumTypC1());
		map.put("accEntry", accEntry);

		save("insertAccEntry", map);
		accEntry.setIsReverse("Y");
		accEntry.setCdFlag("D");
		save("insertAccEntry", map);

		map.clear();
		accEntry.setCdFlag("C");
		accEntry.setIsReverse("N");
		accEntry.setAccTitleCode(accEntry.getAccTitC2());
		accEntry.setTransNumTyp(accEntry.getTransNumTypC2());
		map.put("accEntry", accEntry);

		save("insertAccEntry", map);
		accEntry.setIsReverse("Y");
		accEntry.setCdFlag("D");
		save("insertAccEntry", map);
	}

	public void insertAccEntryImp(AccEntry accEntry) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("accEntry", accEntry);
		save("insertAccEntry", map);
	}

	public void updateAccEntry(AccEntry accEntry) {
		deleteAccEntry(accEntry.getGl_code(), accEntry.getCurrency());
		insertAccEntry(accEntry);
	}

	public void deleteAccEntry(String gl_code, String currency) {
		Map map = new HashMap();
		map.put("gl_code", gl_code);
		map.put("currency", currency);
		delete("deleteAccEntry", map);

	}

	public List findAccEntryGroupList(AccEntry accEntry,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("accEntry", accEntry);
		if (null == paginationList) {
			return find("findAccEntryGroupList", map);
		}
		return find("findAccEntryGroupList", map, paginationList);
	}

}
