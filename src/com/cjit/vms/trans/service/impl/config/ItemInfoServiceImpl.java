package com.cjit.vms.trans.service.impl.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.config.ItemInfo;
import com.cjit.vms.trans.service.config.ItemInfoService;

/**
 * 科目服务类
 * 
 * @author Dylan
 * 
 */
public class ItemInfoServiceImpl extends GenericServiceImpl implements
		ItemInfoService {

	@Override
	public List selectItemInfo(ItemInfo itemInfo, PaginationList paginationList) {
		Map parameters = new HashMap();
		parameters.put("itemInfo", itemInfo);
		if (null != paginationList) {
			return find("selectVmsTransVerification", parameters,
					paginationList);
		}
		return find("selectVmsTransVerification", parameters);
	}

	@Override
	public void insertItemInfo(ItemInfo itemInfo) {
		Map parameters = new HashMap();
		parameters.put("itemInfo", itemInfo);
		save("insertVmsTransVerification", parameters);
	}

	@Override
	public void updateItemInfo(ItemInfo itemInfo) {
		Map parameters = new HashMap();
		parameters.put("itemInfo", itemInfo);
		update("updateVmsTransVerification", parameters);
	}

	@Override
	public void deleteItemInfo(ItemInfo itemInfo) {
		Map parameters = new HashMap();
		parameters.put("itemInfo", itemInfo);
		update("deleteVmsTransVerification", parameters);
	}
}
