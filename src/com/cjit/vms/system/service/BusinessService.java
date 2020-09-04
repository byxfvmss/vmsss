package com.cjit.vms.system.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Business;

public interface BusinessService {

	/**
	 * 查询交易种类列表
	 * 
	 * @param business
	 * @return List
	 */
	public List findBusinessList(Business business,String userID, PaginationList paginationList);
	
	/**
	 * 查询交易认定列表
	 * 
	 * @param business
	 * @return List
	 */
	public List findBusinessMgtList(Business business,PaginationList paginationList);
	
	/**
	 * 查询交易种类列表
	 * 
	 * @param business
	 * @return List
	 */
	public List findBusinessList(Business business);
	public List findBusinessList(Business business,PaginationList paginationList);

	/**
	 * 查询交易种类信息
	 * 
	 * @param business
	 * @return Business
	 */
	public Business findBusiness(Business business);
	
	/**
	 * <p>
	 * 方法名称: loadInstAndUsrXmlEx|描述:异步获取部门列表
	 * </p>
	 * 
	 * @param instId
	 *            机构编号
	 * @param next
	 *            显示几级
	 * @return 机构列表生成的XML文件
	 */
	public String loadInstAndUsrXmlEx(String instId, int level, User user, String reInit);
	
	public String loadInstAndUsrXmlEx(String instId, User user, String reInit);
	
	public void saveInst(Business business);
	
	public void saveMgt(Business business);
	
	public void deleteMgt(String taxRateId);
	
	public Business getMgtById(String taxRateId);
	
	public void updateBusinessRela1(Business business);
	
	public void updateBusiness(Business business);
	
	public void updateBusinessMgt(Business business);
	
	public void updateBusinessIsUse(Business business);
	
	public Business getInstByInstId(String businessCode);
	
	public Business getBusinessListByCode(String businessCode);

	public List findBusiAndBusiSup(Business business, String id,
			PaginationList paginationList);
}
