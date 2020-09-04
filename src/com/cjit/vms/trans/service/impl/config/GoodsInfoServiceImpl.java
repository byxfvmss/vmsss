package com.cjit.vms.trans.service.impl.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.config.GoodsInfo;
import com.cjit.vms.trans.model.config.GoodsTrans;
import com.cjit.vms.trans.service.config.GoodsInfoService;
import com.sun.org.apache.bcel.internal.generic.IFNE;

/**
 * 商品信息服务类
 * 
 * @author Dylan
 *
 */
public class GoodsInfoServiceImpl extends GenericServiceImpl implements
		GoodsInfoService {
	@Override
	public List selectGoodsInfo(GoodsInfo goodsInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("goodsInfo", goodsInfo);
		if (null != paginationList) {
			return find("selectVmsGoodsInfo", map, paginationList);
		}
		return find("selectVmsGoodsInfo", map);
	}

	@Override
	public void insertGoodsInfo(GoodsInfo goodsInfo) {
		Map map = new HashMap();
		map.put("goodsInfo", goodsInfo);
		save("insertVmsGoodsInfo", map);
	}

	@Override
	public void updateGoodsInfo(GoodsInfo goodsInfo) {
		Map map = new HashMap();
		map.put("goodsInfo", goodsInfo);
		save("updateVmsGoodsInfo", map);
		
	}

	@Override
	public void deleteGoodsInfo(GoodsInfo goodsInfo) {
		Map map = new HashMap();
		map.put("goodsInfo", goodsInfo);
		save("deleteVmsGoodsInfo", map);
		//构建关联关系类，和商品一起删除
		Map goodsTransMap = new HashMap();
		GoodsTrans gootsTrans = new GoodsTrans();
		gootsTrans.setGoodsId(goodsInfo.getGoodsId());
		gootsTrans.setTaxNo(goodsInfo.getTaxNo());
		goodsTransMap.put("goodsTrans",gootsTrans);
		save("deleteVmsGoodsTrans", goodsTransMap);
	}
	
	
}
