package com.cjit.vms.trans.service.config;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.config.GoodsInfo;
import com.cjit.vms.trans.model.config.GoodsTrans;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;


/**
 * 交易类型服务类
 * @author Dylan
 *
 */
public interface TransTypeService {
	/***
	 * 
	 * @param transTypeInfo
	 * @param paginationList
	 * @return
	 */
	public List selectTransType(TransTypeInfo transTypeInfo, PaginationList paginationList);
	/***
	 * 
	 * @param transTypeInfo
	 * @param itemCodeIsEmpty //不为空时 只查询itemcode为空的
	 * @param paginationList
	 * @return
	 */
	public List selectTransType(TransTypeInfo transTypeInfo,boolean itemCodeIsEmpty, PaginationList paginationList);
	
	/**
	 * 获取交易类型列表
	 * @param transTypeInfo
	 * @param paginationList
	 * @return
	 */
	public List listTransType(VerificationInfo transTypeInfo, PaginationList paginationList);
	
	/**
	 * 获取未关联的商品列表
	 * @param searchCondition
	 * @return
	 */
	public List listLeftGoods(VerificationInfo searchCondition);
	
	/**
	 * 获取已关联的商品列表
	 * @param searchCondition
	 * @return
	 */
	public List listRightGoods(VerificationInfo searchCondition);

	/***
	 * 更新交易关联科目
	 * @param transTypeIds
	 * @return
	 */
	public List updateTransTypeItemCode(String transTypeIds[],String itemCode);
	
	/***
	 * 移除itemCode
	 * @param itemCode
	 * @return
	 */
	public List removeItemCodeByItemCode(String itemCode);
	
	public List selectGoodsTransType(VerificationInfo transTypeInfo,GoodsInfo goodsInfo,boolean goodIdIsEmpty);
	
	public void deleteVmsGoodsTrans(GoodsTrans goodsTrans);
	
	public void insertVmsGoodsTrans(GoodsTrans goodsTrans);
	
	public void insertTransType(VerificationInfo transType);
	
	public void updateTransType(VerificationInfo transType);
	
	public void removeTransType(VerificationInfo transType);
	
	/***
	 * List<VerificationInfo>
	 * @param transTypeList
	 */
	public void removeTransTypeList(List transTypeList);
	
	public List findTaxRateBytransType(VerificationInfo transType);
}
