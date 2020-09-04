package com.cjit.vms.trans.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;

public interface GoodsConfigService {
	public List selectlistGoodsConfig(Map map ,PaginationList paginationList) ;
	public void updateGoodsConfig(Map map);
	public void deleteGoodsConfig(Map map);
	public void insertGoodsConfig(Map map);
	public int selectlistGoodsConfigSeq();
}
