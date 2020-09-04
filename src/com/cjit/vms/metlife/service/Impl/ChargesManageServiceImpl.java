package com.cjit.vms.metlife.service.Impl;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:收费凭证  metlife
*/
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.service.ChargesManageService;
import com.cjit.vms.metlife.model.ChargesVoucherInfo;


public class ChargesManageServiceImpl extends GenericServiceImpl implements ChargesManageService{

	@Override
	//收费凭证初始化&查询
	public List findChargesVoucher(ChargesVoucherInfo chargesVoucherInfo,
			PaginationList paginationList) {
		Map map=new HashMap();
		map.put("chargesVoucherInfo", chargesVoucherInfo);
		return find("findChargesVoucher", map, paginationList);
	}
	
	

}
