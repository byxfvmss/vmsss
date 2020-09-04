package com.cjit.vms.taxdisk.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;

public interface TaxKeyInterfaceService {

	/**
	 *  查询税控钥匙列表
	 */
	public List getTaxKeyInfoList(VmsTaxKeyInfo taxKeyInfo,PaginationList paginationList) throws Exception;

	/**
	 *  增加税控钥匙
	 */
	public int saveTaxKeyInfo(String operType,VmsTaxKeyInfo taxKeyInfo) throws Exception;

	/**
	 *  修改税控钥匙
	 */
	public int updateTaxKey(VmsTaxKeyInfo taxKeyInfo) throws Exception;

	/**
	 *  删除税控钥匙
	 */
	public void deleteTaxKey(String[] selectTaxKeys) throws Exception;

	/**
	 *  取得税控钥匙详细信息
	 */
	public VmsTaxKeyInfo getTaxKeyInfoDetail(String taxKeyNo, String taxNo);
	

}
