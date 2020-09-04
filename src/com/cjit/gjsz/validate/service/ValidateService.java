package com.cjit.gjsz.validate.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;

public interface ValidateService {

	/**
	 * 存储数据（先删收插）
	 * 
	 * @param List
	 *            校验结果
	 * @param map
	 *            中主要对象有account外汇账户账号dealDate发生日期currCode币种代码branchCode主报告行
	 * @author gaole 2010-11-29
	 */
	public void saveValidateList(List list);

	public List findValidate(Map rptData, PaginationList paginationList);

	public List findValidate(Map rptData);

	public void deleteValidate(Map map);

}
