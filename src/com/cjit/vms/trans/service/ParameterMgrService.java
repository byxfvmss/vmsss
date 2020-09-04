package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;

public interface ParameterMgrService {
	
	/**
	 * 税目管理 列表画面初期化/检索用情报检索
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getListTaxItemInfo(VmsTaxInfo info, PaginationList paginationList);
	
	/**
	 * 商品信息  列表画面初期化/检索用情报检索
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getListGoodsInfo(GoodsInfo info, PaginationList paginationList);
	 public List getListBusiness(GoodsInfo info);
	
	/**
	 * 商品信息  查看交易种类列表
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lyman
	 */
	public List getListGoodsBusi(GoodsInfo info, PaginationList paginationList);
	
	/**
	 * 商品信息  查看未交易种类列表
	 * 
	 * @param info
	 * @param paginationList    

	 * @return
	 * @author lyman 
	 */
	public List getListGoodstoBusi(String taxNo,String goodsNo,String transName, String transType, PaginationList paginationList);
	
	/**
	 * 税目 登录
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void saveTaxItemInfo(VmsTaxInfo info);
	
	/**
	 * 税目 修改
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void updTaxItemInfo(VmsTaxInfo info);
	
	/**
	 * 税目 删除
	 * 
	 * @param taxId
	 * 
	 * @author lee
	 */
	public void delTaxItemInfo(String taxId);
	
	/**
	 * 商品 登录
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void saveGoodsInfo(GoodsInfo info);
	
	/**
	 * 商品明细 登录
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void saveGoodsItemInfo(GoodsInfo info);
	
	/**
	 * 商品 删除
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void delGoodsInfo(GoodsInfo info);
	
	/**
	 * 商品 删除
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void delGoodsInfoItem(GoodsInfo info);
	
	/**
	 * 商品信息  商品存在性check
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getGoodsInfoPK(GoodsInfo info);
	
	/**
	 * 商品信息  商品明细存在性check
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getGoodsItemInfoPK(GoodsInfo info);
	
	/**
	 * 商品信息校验纳税人识别号下的交易类型是否重复
	 * @param info
	 * @return
	 */
	public List getGoodLines(GoodsInfo info);

	/**
	 * 商品管理 判断新增,修改时重复数据不可录入
	 */
	public List findTaxItemInfoRepeatList(VmsTaxInfo info);
	
	public List getGoodInfoToExcel(GoodsInfo info);
}
