package com.cjit.vms.trans.service.impl.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.config.GoodsInfo;
import com.cjit.vms.trans.model.config.GoodsTrans;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.service.config.TransTypeService;

/**
 * 交易类型服务类
 * 
 * @author Dylan
 *
 */
public class TransTypeServiceImpl extends GenericServiceImpl implements
		TransTypeService {

	/***
	 * 
	 * @param transTypeInfo
	 * @param paginationList
	 * @return
	 */
	public List selectTransType(TransTypeInfo transTypeInfo,
			PaginationList paginationList) {
		Map parameters = new HashMap();
		parameters.put("transTypeInfo", transTypeInfo);
		if (null != paginationList) {
			return find("selectVmsTransType", parameters, paginationList);
		}
		return find("selectVmsTransType", parameters);
	}

	/***
	 * 
	 * @param transTypeInfo
	 * @param itemCodeIsEmpty
	 *            //不为空时 只查询itemcode为空的
	 * @param paginationList
	 * @return
	 */
	@Override
	public List selectTransType(TransTypeInfo transTypeInfo,
			boolean itemCodeIsEmpty, PaginationList paginationList) {
		Map parameters = new HashMap();

		parameters.put("transTypeInfo", transTypeInfo);

		// 科目不为空
		if (itemCodeIsEmpty) {
			parameters.put("itemCodeIsEmpty", itemCodeIsEmpty);
		}

		if (null != paginationList) {
			return find("selectVmsTransType", parameters, paginationList);
		}
		return find("selectVmsTransType", parameters);
	}

	/***
	 * 更新交易关联科目
	 * 
	 * @param transTypeIds
	 * @return
	 */
	@Override
	public List updateTransTypeItemCode(String transTypeIds[], String itemCode) {
		Map map = new HashMap();
		map.put("transTypeIds", transTypeIds);
		map.put("itemCode", itemCode);
		update("updateVmsTransTypeItemCode", map);
		return null;
	}

	@Override
	public List listTransType(VerificationInfo transTypeInfo,
			PaginationList paginationList) {
		// TODO Auto-generated method stub
		Map paraMap = new HashMap();
		paraMap.put("verification", transTypeInfo);
		if (null == paginationList) {
			return find("findTransTypeList", paraMap);
		}
		return find("findTransTypeList", paraMap, paginationList);
	}

	@Override
	public List removeItemCodeByItemCode(String itemCode) {
		Map paraMap = new HashMap();
		paraMap.put("itemCode", itemCode);
		update("updateVmsTransTypeItemCodeToEmpity", paraMap);
		return null;
	}

	@Override
	public List selectGoodsTransType(VerificationInfo transTypeInfo,
			GoodsInfo goodsInfo, boolean goodIdIsEmpty) {

		Map paraMap = new HashMap();
		paraMap.put("transTypeInfo", transTypeInfo);
		paraMap.put("goodsInfo", goodsInfo);
		if (goodIdIsEmpty) {
			paraMap.put("goodIdIsEmpty", goodIdIsEmpty);
		}
		return find("selectGoodsTransType", paraMap);
	}

	@Override
	public void deleteVmsGoodsTrans(GoodsTrans goodsTrans) {
		Map paraMap = new HashMap();
		paraMap.put("goodsTrans", goodsTrans);
		delete("deleteVmsGoodsTrans", paraMap);
	}

	@Override
	public void insertVmsGoodsTrans(GoodsTrans goodsTrans) {
		Map paraMap = new HashMap();
		paraMap.put("goodsTrans", goodsTrans);
		save("insertVmsGoodsTrans", paraMap);
	}

	@Override
	public List listLeftGoods(VerificationInfo searchCondition) {
		Map paraMap = new HashMap();
		paraMap.put("searchCondition", searchCondition);
		List list = this.find("listLeftGoods", paraMap);
		return list;
	}

	@Override
	public List listRightGoods(VerificationInfo searchCondition) {
		Map paraMap = new HashMap();
		paraMap.put("searchCondition", searchCondition);
		List list = this.find("listRightGoods", paraMap);
		return list;
	}

	@Override
	public void insertTransType(VerificationInfo transType) {
		Map paraMap = new HashMap();
		paraMap.put("transType", transType);
		save("insertTransType", paraMap);
	}

	@Override
	public void updateTransType(VerificationInfo transType) {
		Map paraMap = new HashMap();
		paraMap.put("transType", transType);
		update("updateTransType", paraMap);
	}

	@Override
	public void removeTransType(VerificationInfo transType) {
		Map paraMap = new HashMap();
		paraMap.put("transType", transType);
		delete("deleteTransType", paraMap);
	}

	@Override
	public void removeTransTypeList(List transTypeList) {
		
	}

	@Override
	public List findTaxRateBytransType(VerificationInfo transType) {
		Map paraMap = new HashMap();
		paraMap.put("transType", transType);
		return this.find("findTaxRateBytransType", paraMap);
	}
}
