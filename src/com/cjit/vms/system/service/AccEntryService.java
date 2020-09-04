package com.cjit.vms.system.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.AccEntry;

public interface AccEntryService {

	public List findAccEntryList(AccEntry accEntry,PaginationList paginationList);
	
	public List findAccEntryGroupList(AccEntry accEntry,PaginationList paginationList);
	
	public void insertAccEntry(AccEntry accEntry);
	
	public void insertAccEntryImp(AccEntry accEntry);
	
	public void updateAccEntry(AccEntry accEntry);
	
	public void deleteAccEntry(String gl_code,String currency);
}
