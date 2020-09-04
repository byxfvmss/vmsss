package com.cjit.vms.trans.service.impl;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.service.GoodsConfigService;

public class GoodsConfigServiceImpl extends GenericServiceImpl implements
		GoodsConfigService {

	@Override
	public List selectlistGoodsConfig(Map map, PaginationList paginationList) {
		if (null != paginationList) {
			return find("findListGoodsConfig", map, paginationList);
		}
		return find("findListGoodsConfig", map);
	}

	@Override
	public void updateGoodsConfig(Map map) {
		update("updateGoodsConfig", map);
	}

	@Override
	public void deleteGoodsConfig(Map map) {
		delete("deleteGoodsConfig", map);
	}

	@Override
	public void insertGoodsConfig(Map map) {
		save("insertGoodsConfig", map);
	}
	
	@Override
	public int selectlistGoodsConfigSeq() {
		List list = find("selectGoodsConfigSeq", null);
		return (Integer) list.get(0);
	}

}
