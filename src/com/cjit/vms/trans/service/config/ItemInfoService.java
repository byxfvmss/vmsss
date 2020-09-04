package com.cjit.vms.trans.service.config;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.config.ItemInfo;

/**
 * 科目服务类
 * @author Dylan
 *
 */
public interface ItemInfoService {
	/***
	 * 查询科目
	 *  @param itemInfo
	 * @param paginationList
	 * @return
	 */
	public List selectItemInfo(ItemInfo itemInfo, PaginationList paginationList);
	
	/***
	 * 新建科目信息
	 * @param itemInfo
	 */
	public void insertItemInfo(ItemInfo itemInfo);
	
	/***
	 * 更新
	 * @param itemInfo
	 */
	public void updateItemInfo(ItemInfo itemInfo);
	
	/**
	 * 删除商品信息
	 * @param itemInfo
	 */
	public void deleteItemInfo(ItemInfo itemInfo);
}
