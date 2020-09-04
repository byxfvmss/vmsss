package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.Business;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.system.service.BaseDataService;
import com.cjit.vms.trans.model.VmsTaxInfo;

public class BaseDataServiceImpl extends GenericServiceImpl implements
		BaseDataService {

/*	public List findBusinessList(Business business) {
		Map map = new HashMap();
		map.put("business", business);
		return find("findBusiness", map);
	}

	public Business findBusiness(Business business) {
		List list = this.findBusinessList(business);
		if (list != null && list.size() == 1) {
			return (Business) list.get(0);
		} else {
			return null;
		}
	}*/

	public List findVmsTaxInfoList(VmsTaxInfo vmsTaxInfo) {
		Map map = new HashMap();
		map.put("taxId", vmsTaxInfo.getTaxId());
		map.put("taxno", vmsTaxInfo.getTaxno());
		map.put("fapiaoType", vmsTaxInfo.getFapiaoType());
		map.put("taxRate", vmsTaxInfo.getTaxRate());
		map.put("user_id", vmsTaxInfo.getUser_id());
		return find("getListTaxItemInfo", map);
	}

	public VmsTaxInfo findVmsTaxInfo(VmsTaxInfo vmsTaxInfo) {
		List list = this.findVmsTaxInfoList(vmsTaxInfo);
		if (list != null && list.size() == 1) {
			return (VmsTaxInfo) list.get(0);
		} else {
			return null;
		}
	}

	public Organization getOrganization(Organization org) {
		Map map = new HashMap();
		map.put("organization", org);
		List list = this.find("getOrganization", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (Organization) list.get(0);
		} else {
			return null;
		}
	}

	public List findGoodsInfoList(GoodsInfo goodsInfo) {
		Map map = new HashMap();
		map.put("goods", goodsInfo);
		return find("findGoodsList", map);
	}

	public GoodsInfo findGoodsInfo(GoodsInfo goodsInfo) {
		Map map = new HashMap();
		map.put("goods", goodsInfo);
		List list = find("findGoods", map);
		if (list != null && list.size() == 1) {
			return (GoodsInfo) list.get(0);
		} else {
			return null;
		}
	}
}
