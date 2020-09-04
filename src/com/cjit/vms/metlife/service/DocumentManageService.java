package com.cjit.vms.metlife.service;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:单证管理 metlife
*/
import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.DocumentManageInfo;
import com.cjit.vms.trans.model.InstInfo;

public interface DocumentManageService {


	public List getInstInfoList(DocumentManageInfo documentManageInfo,PaginationList paginationList);
	List findDocumentManagelist(DocumentManageInfo documentManageInfo, PaginationList paginationList);
	public List findByRuleId(String ruleId);
	public void updateKeyCode(DocumentManageInfo documentManageInfo);
	public List checkCode(DocumentManageInfo documentManageInfo);
	public int saveCode(DocumentManageInfo documentManageInfo);
	public void deleteKeyCode(String ruleid);
	public void saveCodeNum(DocumentManageInfo documentManageInfo, String[] curnumarray);
	public void updatecurNum(DocumentManageInfo documentManageInfo);
	public List findManageInsureCodeList(DocumentManageInfo documentManageInfo,
			PaginationList paginationList);
	public void updateKeyCode(String numId, String sta, String usernam, String vdName);
	public List finDocRecInfo(DocumentManageInfo documentManageInfo, PaginationList paginationList);
	public void insertTransTo(String string, String string2);
	public void updateTransTo(String sta, String string, String string2,
			String vdName);
	public List findBaseuser();
	public void distributeTo(DocumentManageInfo documentManageInfo);
	public void updateCancel(String sta, String string, String string2);
	public List findDocMapInfo(DocumentManageInfo documentManageInfo,PaginationList paginationList);
	public void saveCodeNum1(List documentManageInfoList);

}
