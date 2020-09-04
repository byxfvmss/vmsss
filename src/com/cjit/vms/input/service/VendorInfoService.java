package com.cjit.vms.input.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.VendorInfo;

public interface VendorInfoService {

	public List findVendorInfoList(VendorInfo vendorInfo, PaginationList paginationList);

	public List findVendorByTaxNo(String taxNo);

	public void saveNewVendor(VendorInfo vendorInfo);

	public VendorInfo findVendorById(String vendorId);

	public void updateVendorById(VendorInfo vendorInfo);

	public void deleteVendorById(List vendorIds);
	
	/**
	 * @param vendorInfo
	 * @param paginationList
	 * @return 供应商信息审核 界面
	 */
	public List findVendorTempInfoList(VendorInfo vendorInfo, PaginationList paginationList);
	/**
	 * @param taxNo
	 * @return 依据纳税人识别号 找到对应的纳税人识别号信息是否存在
	 */
	public List findvendorTaxnoTempByTaxNo(String taxNo);
	
	/**
	 * @param vendorId
	 * @return 根据供应商 id 找到相应的供应商id信息
	 */
	public List findTransbyVendId(String vendorId);
	
	/**
	 * @param vendorInfo
	 *  将 表从供应商信息表中复制到供应商信息临时表中
	 */
	public void copyVendorInfoToVendorInfoTemp(VendorInfo vendorInfo);
	
	/**
	 * @param taxNo
	 * @return 根据纳税人识别号 
	 */
	public List findVendorInfoTempByTaxNo(String taxNo);
	
	/**
	 * @param vendorInfo 增加审核通过后
	 */
	public void copyVendorTempYoVendor(List list);
	/**
	 * @param vendorInfo 修改审核通过后
	 */
	public void updatevendorAfterAudit(List list);
	
	/**
	 * @param list 审核通过后删除临时表信息
	 */
	public void deleteVendorTemp(List list);
	
	/**
	 * @param list 审核通过后 修改临时表信息
	 */
	public void updateVendorTempAfterAudit(List list,String dataAuditStatus);
	/**
	 * @param vendorId
	 * @return 详情页面 列表
	 */
	public VendorInfo findvendorTempByVendorId(String vendorId);
	public void deleteVendorByTaxNo(String taxNo);
	public void saveVendorData(Map<String,String> map);
	public void saveVendorInfo(Map<String,String> map);
}
