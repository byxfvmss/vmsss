package com.cjit.vms.trans.service.impl.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.AutoInvoice;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.service.config.ItemRateService;

public class ItemRateServiceImpl extends GenericServiceImpl implements
		ItemRateService {
	@Override
	public void insertItemRate(VerificationInfo verificationInfo) {
		Map parameters = new HashMap();
		parameters.put("itemRate", verificationInfo);
		save("insertItemRate", parameters);
	}

	@Override
	public void removeItemRate(VerificationInfo verificationInfo) {
		Map parameters = new HashMap();
		parameters.put("itemRate", verificationInfo);
		delete("removeItemRate", parameters);
	}

	@Override
	public List selectItemRate(VerificationInfo verificationInfo,
			PaginationList paginationList, boolean itemCodeIsEmpty) {

		Map parameters = new HashMap();
		parameters.put("itemRate", verificationInfo);
		if (!itemCodeIsEmpty) {
			parameters.put("itemCodeIsEmpty", itemCodeIsEmpty);
		}
		if (null == paginationList) {
			return find("selectItemRate", parameters);
		}
		return find("selectItemRate", parameters, paginationList);
	}

	@Override
	public List selectItemRateBase(VerificationInfo verificationInfo) {
		Map parameters = new HashMap();
		parameters.put("itemRate", verificationInfo);
		return find("selectItemRateBase", parameters);
	}

	
	//参数设置
	@Override
	public List alertInvoice(String instId,PaginationList paginationList) {//专票总数
		Map map = new HashMap();
		map.put("instId", instId);
		if (null!=paginationList) {
			return find("InvoiceSum",map,paginationList);
		}
		return find("InvoiceSum",map);
	}
	//参数编辑页面
	@Override
	public List editInvoice(String instId,String type,PaginationList paginationList) {//专票总数
		Map map = new HashMap();
		map.put("instId", instId);
		map.put("type", type);
		if (null!=paginationList) {
			return find("InvoiceSum1",map,paginationList);
			//update(statements, map)
		}
		return find("InvoiceSum1",map);
	}
	
	
	//修改参数
	public void updateParam(String instId,String type,String alertNum,String invoicePercent){
		Map map = new HashMap();
		map.put("instId", instId);
		map.put("type", type);
		map.put("alertNum", alertNum);
		map.put("invoicePercent", invoicePercent);
		update("updateBillAlertParam", map);
	}
	
	
}
