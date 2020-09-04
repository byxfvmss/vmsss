/**
 * Authority Service
 */
package com.cjit.gjsz.system.service;

import java.util.List;

import com.cjit.common.service.GenericService;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Authority;

/**
 * @author huboA
 */
public interface AuthorityService extends GenericService{

	/**
	 * 查找功能权限
	 * @param user
	 * @return
	 */
	public List findAuthority(Authority authority);

	/**
	 * 根据父Id获取功能权限
	 * @param authority
	 * @return
	 */
	public List findAuthorityByParentId(Authority authority);

	/**
	 * 查找功能权限
	 * @param user
	 * @return
	 */
	// public List findAuthorityByUser(User user);
	/**
	 * 查找功能权限
	 * @param user
	 * @return
	 */
	// public List findAuthorityByUser(User user, Object obj);
	/**
	 * 新建功能权限
	 */
	public void saveAuthority(Authority authority);

	/**
	 * 验证功能权限
	 * @param authority
	 * @return
	 */
	public boolean checkAuthority(Authority authority);

	/**
	 * 删除功能权限
	 * @param authority
	 */
	public void deleteAuthority(Authority authority);

	/**
	 * 删除功能权限
	 * @param authority
	 */
	public void deleteAuthority(List ids);

	/**
	 * 查找功能权限
	 * @param authority
	 * @return
	 */
	public List findAuthority(Authority authority, PaginationList paginationList);

	/**
	 * 取得单个功能权限
	 * @param authority
	 * @return
	 */
	public Authority getAuthority(Authority authority);
}
