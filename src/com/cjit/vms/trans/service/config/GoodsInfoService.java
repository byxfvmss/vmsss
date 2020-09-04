package com.cjit.vms.trans.service.config;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.config.GoodsInfo;

/**
 * 商品信息服务类
 * @author Dylan
 *
 */
public interface GoodsInfoService {
	public List selectGoodsInfo(GoodsInfo goodsInfo,PaginationList paginationList);
	
	public void insertGoodsInfo(GoodsInfo goodsInfo);
	
	public void updateGoodsInfo(GoodsInfo goodsInfo);
	
	public void deleteGoodsInfo(GoodsInfo goodsInfo);
}
