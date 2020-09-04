package com.cjit.vms.trans.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.IntegrityCheckAccount;
import com.cjit.vms.trans.service.IntegrityCheckAccountService;

public class IntegrityCheckAccountServiceImpl extends GenericServiceImpl implements IntegrityCheckAccountService{

	private IntegrityCheckAccountService integrityCheckAccountService;
	public IntegrityCheckAccountService getIntegrityCheckAccountService() {
		return integrityCheckAccountService;
	}
	
	public void setIntegrityCheckAccountService(
			IntegrityCheckAccountService integrityCheckAccountService) {
		this.integrityCheckAccountService = integrityCheckAccountService;
	}
	
	/**
	 * 完整性对账 页面初始化数据
	 */
	public List getCheckAccountList(IntegrityCheckAccount account,PaginationList paginationList) {
		Map map = new HashMap();
		map.put("instCode", account.getInstCode());
		if(paginationList != null){
			return this.find("getCheckAccountList", map, paginationList);
		}else{
			return this.find("getCheckAccountList", map);
		}
	}

	/**
	 * 完整性对账 商品明细页面初始化数据
	 */
	@Override
	public List getCheckGoodsInfoList(String instCode,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("instCode", instCode);
		
		if(paginationList != null){
			return this.find("getCheckGoodsInfoList", map, paginationList);
		}else{
			return this.find("getCheckGoodsInfoList", map);
		}
	}

	@Override
	public List getCheckTransInfoList(IntegrityCheckAccount account,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("instCode", account.getInstCode());
		map.put("goodsNo", account.getGoodsNo());
		if(paginationList != null){
			return this.find("getCheckTransInfoList", map, paginationList);
		}else{
			return this.find("getCheckTransInfoList", map);
		}
	}
	
	
}
