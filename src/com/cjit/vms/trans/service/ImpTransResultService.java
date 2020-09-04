package com.cjit.vms.trans.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.VmsTransInfo;


public interface ImpTransResultService {
	/**
	 * 数据导入价税查询    列表画面初期化/检索用情报检索
	 * @param impTime
	 * @param paginationList
	 * @return
	 */
	public List getImpResultListInfo(String impTime, PaginationList paginationList);
	

	
	/**
	 * 数据导入价税导出    列表画面初期化/检索用情报检索
	 * @param impTime
	 * @param paginationList
	 * @return
	 */
	public List expImpResultListInfo(String impTime, PaginationList paginationList);
	
	
}
