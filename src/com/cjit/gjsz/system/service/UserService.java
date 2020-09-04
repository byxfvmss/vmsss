/**
 * Organization Service
 */
package com.cjit.gjsz.system.service;

import java.util.List;

import com.cjit.common.service.GenericService;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;

/**
 * @author huboA
 */
public interface UserService extends GenericService{

	/**
	 * 新建用户
	 */
	public String saveUser(User user);

	public void saveUserRights(User user);

	public void saveUseOrganiazatins(User user);

	/**
	 * 验证用户
	 * @param user
	 * @return
	 */
	public User checkUser(User user);

	/**
	 * 删除用户
	 * @param user
	 */
	public void deleteUser(User user);

	/**
	 * 删除用户
	 * @param user
	 */
	public void deleteUser(List ids);

	/**
	 * 查找用户是否已经注册 true: 不存在，允许添加 false: 存在，不允许添加
	 * @param user
	 * @return
	 */
	public boolean findUser(User user);

	/**
	 * 根据username取得用户
	 * @param user
	 * @return
	 */
	public User findUserByUsername(User user);

	/**
	 * 查找用户
	 * @param user
	 * @return
	 */
	public List findUserByOrg(User user, PaginationList paginationList);

	/**
	 * 取得单个用户
	 * @param user
	 * @return
	 */
	public User getUser(User user);

	/**
	 * 根据机构ID获取用户List
	 * @param organization
	 * @return
	 */
	public List findUsersByOrgId(String orgId);
}
