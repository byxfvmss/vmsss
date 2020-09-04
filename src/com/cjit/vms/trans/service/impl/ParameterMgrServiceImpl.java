package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.trans.service.ParameterMgrService;

public class ParameterMgrServiceImpl  extends GenericServiceImpl implements ParameterMgrService {

	/**
	 * 税目管理 列表画面初期化/检索用情报检索
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getListTaxItemInfo(VmsTaxInfo info, PaginationList paginationList) {
		Map map = new HashMap();
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		// 税目ID
		map.put("taxId", info.getTaxId());
		// 纳税人识别号
		map.put("taxno", info.getTaxno());
		// 发票类型
		map.put("fapiaoType", info.getFapiaoType());
		// 税率
		map.put("taxRate", info.getTaxRate());
		// 用户ID
		map.put("user_id", info.getUser_id());
		
		if (paginationList == null){
			return find("getListTaxItemInfo", map);
		}
		return find("getListTaxItemInfo", map, paginationList);
	}
	
	/**
	 * 税目 登录
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void saveTaxItemInfo(VmsTaxInfo info){
		Map map = new HashMap();
		
		// 税目ID
		map.put("taxId", info.getTaxId());
		// 纳税人识别号
		map.put("taxno", info.getTaxno());
		// 发票类型
		map.put("fapiaoType", info.getFapiaoType());
		// 税率
		map.put("taxRate", info.getTaxRate());
		
		update("saveTaxItemInfo", map);
	}
	
	/**
	 * 税目 修改
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void updTaxItemInfo(VmsTaxInfo info){
		Map map = new HashMap();
		
		// 税目ID
		map.put("taxId", info.getTaxId());
		// 纳税人识别号
		map.put("taxno", info.getTaxno());
		// 发票类型
		map.put("fapiaoType", info.getFapiaoType());
		// 税率
		map.put("taxRate", info.getTaxRate());
		
		update("updTaxItemInfo", map);
	}
	
	/**
	 * 税目 删除
	 * 
	 * @param taxId
	 * 
	 * @author lee
	 */
	public void delTaxItemInfo(String taxId){
		Map map = new HashMap();
		// 税目ID
		map.put("taxId", taxId);
		this.delete("delTaxItemInfo", map);
	}
	
	
	
	public List getListGoodsBusi(GoodsInfo info, PaginationList paginationList) {
		Map map = new HashMap();

		// 纳税人识别号
		map.put("taxNo", info.getTaxNo());
		// 发票商品编号
		map.put("goodsNo", info.getGoodsNo());
		List list=new ArrayList();
		if (paginationList == null){
			list= find("getListGoodsBusi",map);
		}else{
			list= find("getListGoodsBusi", map, paginationList);
		}
		return list;
	}
	public List getListBusiness(GoodsInfo info){
		Map map = new HashMap();

		// 纳税人识别号
		map.put("taxNo", info.getTaxNo());
		// 发票商品编号
		map.put("goodsNo", info.getGoodsNo());
		
		return find("getListGoodsBusi",map);
	}
	/**
	 * 商品信息  列表画面初期化/检索用情报检索
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getListGoodsInfo(GoodsInfo info, PaginationList paginationList) {
		Map map = new HashMap();

		// 纳税人识别号
		map.put("taxNo", info.getTaxNo());
		// 发票商品编号
		map.put("goodsNo", info.getGoodsNo());
		// 商品名称
		map.put("goodsName", info.getGoodsName());
				
		if (paginationList == null){
			return find("getListGoodsInfo", map);
		}
		return find("getListGoodsInfo", map, paginationList);
	}
	
	/**
	 * 商品 登录
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void saveGoodsInfo(GoodsInfo info){
		Map map = new HashMap();

		// 纳税人识别号
		map.put("taxNo", info.getTaxNo());
		// 发票商品编号
		map.put("goodsNo", info.getGoodsNo());
		// 商品名称
		map.put("goodsName", info.getGoodsName());
		
		// 发票商品表 登录
		update("saveGoodsInfo", map);
	}
	
	/**
	 * 商品明细 登录
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void saveGoodsItemInfo(GoodsInfo info){
		Map map = new HashMap();
		// 发票商品编号
		map.put("goodsNo", info.getGoodsNo());
		// 纳税人识别号
		map.put("taxNo", info.getTaxNo());
		
		// 发票商品明细表 登录
		update("saveGoodsItemInfo", map);
	}
	
	
	
	public void delGoodsInfoItem(GoodsInfo info) {
		Map map = new HashMap();
		// 纳税人识别号
		map.put("taxNo", info.getTaxNo());
		// 发票商品编号
		map.put("goodsNo", info.getGoodsNo());
//		// 交易名称
//		map.put("transType", info.getTransType());
		
		// 发票商品明细表 
		delete("delGoodsItemInfo", map);
	}

	/**
	 * 商品 删除
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void delGoodsInfo(GoodsInfo info){
		Map map = new HashMap();
		// 纳税人识别号
		map.put("taxNo", info.getTaxNo());
		// 发票商品编号
		map.put("goodsNo", info.getGoodsNo());
		// 商品名称
		map.put("goodsName", info.getGoodsName());
//		// 交易名称
//		map.put("transType", info.getTransType());
		
		// 发票商品表 登录
		delete("delGoodsInfo", map);
		// 发票商品明细表 登录
		delete("delGoodsItemInfo", map);
	}

	/**
	 * 商品信息  商品存在性check
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getGoodsInfoPK(GoodsInfo info) {
		Map map = new HashMap();

		// 纳税人识别号
		map.put("taxNo", info.getTaxNo());
		// 发票商品编号
		map.put("goodsNo", info.getGoodsNo());
		// 商品名称
		map.put("goodsName", info.getGoodsName());
		
		return find("getGoodsInfoPK", map);
	}

	/**
	 * 商品信息  商品明细存在性check
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getGoodsItemInfoPK(GoodsInfo info) {
		Map map = new HashMap();

		// 纳税人识别号
		map.put("taxNo", info.getTaxNo());
		// 发票商品编号
		map.put("goodsNo", info.getGoodsNo());
		
		return find("getGoodsItemInfoPK", map);
	}

	public List getListGoodstoBusi(String taxNo, String goodsNo,String transName,String  transType,
			PaginationList paginationList) {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		map.put("taxNo", taxNo);
		map.put("goodsNo", goodsNo);
		map.put("transName", transName);
		map.put("transType", transType);
		return find("getListGoodstoBusi", map, paginationList);
	}

	public List getGoodLines(GoodsInfo info) {
		Map map=new HashMap();
		map.put("taxNo", info.getTaxNo());
		return find("getGoodLines",map);
	}
	public List findTaxItemInfoRepeatList(VmsTaxInfo info){
		Map map=new HashMap();
		map.put("taxId", info.getTaxId());
		map.put("taxno", info.getTaxno());
		map.put("fapiaoType", info.getFapiaoType());
		map.put("taxRate", info.getTaxRate());
		return find("findTaxItemInfoRepeatList",map);
	}

	@Override
	public List getGoodInfoToExcel(GoodsInfo info) {
		Map paraMap = new HashMap();
		paraMap.put("goodsName", info.getGoodsName());
		paraMap.put("goodsNo",info.getGoodsNo());
		paraMap.put("taxNo",info.getTaxNo());
		List list = find("getGoodInfoToExcel", paraMap);
		return list;
	}

	
}
