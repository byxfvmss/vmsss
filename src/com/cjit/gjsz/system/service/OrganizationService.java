/**
 * Organization Service
 */
package com.cjit.gjsz.system.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.GenericService;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;

/**
 * @author huboA
 */
public interface OrganizationService extends GenericService{

	/**
	 * 查找机构
	 * @param user
	 * @return
	 */
	public List findOrganization(Organization org);

	/**
	 * 按机构去查找申报号码
	 * @param user
	 * @return
	 */
	public String findRptNo(String customId);

	/**
	 * 新建机构
	 */
	public String saveOrganization(Organization org);

	/**
	 * 验证机构
	 * @param org
	 * @return
	 */
	public boolean checkOrganization(Organization org);

	/**
	 * 删除机构
	 * @param org
	 */
	public void deleteOrganization(Organization org);

	/**
	 * 删除机构
	 * @param org
	 */
	public void deleteOrganization(List ids);

	/**
	 * 查找机构
	 * @param org
	 * @return
	 */
	public List findOrganization(Organization org, PaginationList paginationList);

	/**
	 * 取得单个机构
	 * @param org
	 * @return
	 */
	public Organization getOrganization(Organization org);

	public List findOrganizationByUser(User user);
	
	public List findUserOrganization(User user,Map obj);

	public List findOrganizationByRights(User user);

	public List findOrganizationByUser(User user, Object obj);

	/**
	 * 按机构去查找主报告行号
	 * @param customId
	 * @return String
	 */
	public String findRptTitle(String customId);

	public String getOrgNameByInstCode(String instCode);

	public String findInstCode(String rptNo);

	/**
	 * 查询与指定机构拥有相同主报告行号的机构列表
	 * @param instCode
	 * @return List
	 */
	public List findOrgIdsWithSameRptTitle(String instCode);
	
	/**
	 * 根据机构号码获取税号
	 * @param instCode
	 * @return
	 */
	public Organization findTaxNoByInstCode(String instCode);
}
