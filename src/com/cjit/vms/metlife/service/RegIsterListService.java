package com.cjit.vms.metlife.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.SpecialRegIster;

public interface RegIsterListService {
	
	public List getRegIsterList(SpecialRegIster ister, PaginationList paginationList,boolean flag);
	
	public void saveRegIsterList(SpecialRegIster ister,boolean falg);
	
	public void delRegIsterList(SpecialRegIster ister);
	
	
}
