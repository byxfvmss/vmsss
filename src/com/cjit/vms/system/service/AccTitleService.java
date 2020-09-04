package com.cjit.vms.system.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.AccTitle;
import com.cjit.vms.system.model.TransAccInfo;

public interface AccTitleService {

	public List findAccTitleList(AccTitle accTitle,PaginationList paginationList);
	
	public void saveAccTitle(AccTitle accTitle);
	
	/**
	 * @title deleteAccTitleByCode
	 * @description 通过code删除科目
	 * @author dev4
	 * @param accTitleId
	 */
	public void deleteAccTitleByCode(String accTitleCode);
	
	public void deleteAccTitle(String accTitleId);
	
	public void deleteAll(List list);
	
	public AccTitle getAccTitleById(String accTitleId);
	
	public AccTitle getAccTitleByCode(String accTitleCode);
	
	public void updateAccTitle(AccTitle accTitle);
	
	public List findTransAccList(TransAccInfo transAccInfo,PaginationList paginationList);
	
	public List findTransAccList(TransAccInfo transAccInfo);
	
	public void saveTransAcc(TransAccInfo transAccInfo);
	
	
}
