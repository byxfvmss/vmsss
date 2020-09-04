package com.cjit.vms.trans.action.seo;

import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.TransInfo;

/**
 * 综合查询（销项税管理）
 * 
 * @author Tinna
 * 
 */
public class SEOAction extends DataDealAction {

	private static final long serialVersionUID = 1L;

	/**
	 * 交易查询 进入页面
	 */
	public String businessQuery() {

		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		seoTransInfoService.findSEOInfo();
		return SUCCESS;
	}
}
