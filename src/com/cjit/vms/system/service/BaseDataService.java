package com.cjit.vms.system.service;

import java.util.List;

import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.Business;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;

/**
 * 基础信息操作类 可供交易、票据处理时，查询基础参数信息（避免action中引入过多service）
 * 
 * @author Larry
 */
public interface BaseDataService {

	/**
	 * 查询交易种类列表
	 * 
	 * @param business
	 * @return List
	 */
	//public List findBusinessList(Business business);

	/**
	 * 查询交易种类信息
	 * 
	 * @param business
	 * @return Business
	 */
	//public Business findBusiness(Business business);

	/**
	 * 查询税目信息表列表(vms_tax_info)
	 * 
	 * @param vmsTaxInfo
	 * @return List
	 */
	public List findVmsTaxInfoList(VmsTaxInfo vmsTaxInfo);

	/**
	 * 查询税目信息(vms_tax_info)
	 * 
	 * @param vmsTaxInfo
	 * @return VmsTaxInfo
	 */
	public VmsTaxInfo findVmsTaxInfo(VmsTaxInfo vmsTaxInfo);

	/**
	 * 查询机构信息(t_org)
	 * 
	 * @param org
	 * @return Organization
	 */
	public Organization getOrganization(Organization org);
	
	/**
	 * 查询发票商品列表(vms_goods_info)
	 * @param goodsInfo
	 * @return List
	 */
	public List findGoodsInfoList(GoodsInfo goodsInfo);
	
	/**
	 * 查询发票商品(vms_goods_info)
	 * @param goodsInfo
	 * @return GoodsInfo
	 */
	public GoodsInfo findGoodsInfo(GoodsInfo goodsInfo);
}
