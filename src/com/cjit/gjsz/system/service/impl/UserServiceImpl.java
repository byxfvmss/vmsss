/**
 * Organization Service
 */
package com.cjit.gjsz.system.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.common.util.UUID;
import com.cjit.gjsz.system.model.Authority;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.model.UserAuthority;
import com.cjit.gjsz.system.model.UserOrganization;
import com.cjit.gjsz.system.service.UserService;

/**
 * @author huboA
 */
public class UserServiceImpl extends GenericServiceImpl implements UserService{

	private void saveUserAuthority(User user){
		if(CollectionUtil.isNotEmpty(user.getIds())){
			Map map = new HashMap();
			Set set = new HashSet();
			for(int i = 0; i < user.getIds().size(); i++){
				String id = (String) user.getIds().get(i);
				Authority authority = new Authority();
				authority.setId(Integer.valueOf(id).intValue());
				map.put("authority", authority);
				List parents = this.find("getAuthority", map);
				if(parents.size() > 0){
					Authority parAuth = (Authority) parents.get(0);
					set.add(new Integer(parAuth.getParentId()));
				}
				UserAuthority userAuthority = new UserAuthority();
				userAuthority.setUserId(user.getId());
				userAuthority.setAuthorityId(Integer.valueOf(id).intValue());
				map.put("userAuthority", userAuthority);
				this.save("saveUserAuthority", map);
			}
			// 保存父亲节点
			Iterator it = set.iterator();
			while(it.hasNext()){
				UserAuthority tmpuserAuthority = new UserAuthority();
				tmpuserAuthority.setUserId(user.getId());
				int parIds = ((Integer) it.next()).intValue();
				tmpuserAuthority.setAuthorityId(parIds);
				map.put("userAuthority", tmpuserAuthority);
				this.save("saveUserAuthority", map);
			}
		}
	}

	private void saveUserOrganiazatin(User user){
		Map map = new HashMap();
		if(user.getOrgIds() != null && user.getOrgIds().size() > 0){
			for(int i = 0; i < user.getOrgIds().size(); i++){
				String id = (String) user.getOrgIds().get(i);
				UserOrganization userOrganization = new UserOrganization();
				userOrganization.setUserId(user.getId());
				userOrganization.setOrganizationId(id);
				map.put("userOrganization", userOrganization);
				this.save("saveUserOrganizatin", map);
			}
		}else{
			this.delete("deleteUserOrganizatin", user);
		}
	}

	private void deleteUserAuthority(User user){
		this.delete("deleteUserAuthority", user);
	}

	private void deleteUserOrganiazatin(User user){
		this.delete("deleteUserOrganizatin", user);
	}

	public String saveUser(User user){
		Map map = new HashMap();
		map.put("user", user);
		if(StringUtil.isNotEmpty(user.getId())){
			this.update("updateUser", map); // 更新用户对象
			// deleteUserAuthority(user); // 删除用户现有功能权限
			// deleteUserOrganiazatin(user); // 删除用户现有机构权限
		}else{
			user.setId(UUID.randomUUID().toString());
			this.save("saveUser", map); // 创建用户对象
		}
		// saveUserAuthority(user); // 更新用户功能权限
		// saveUserOrganiazatin(user); // 更新用户机构权限
		return user.getId();
	}

	public void saveUserRights(User user){
		Map map = new HashMap();
		map.put("user", user);
		deleteUserAuthority(user); // 删除用户现有功能权限
		saveUserAuthority(user); // 更新用户功能权限
	}

	public void saveUseOrganiazatins(User user){
		Map map = new HashMap();
		map.put("user", user);
		deleteUserOrganiazatin(user); // 删除用户现有机构权限
		saveUserOrganiazatin(user); // 更新用户机构权限
	}

	public User checkUser(User user){
		Map map = new HashMap();
		map.put("user", user);
		List users = this.find("checkUserByOrg", map);
		if(CollectionUtil.isNotEmpty(users)){
			return (User) users.get(0);
		}else{
			return null;
		}
	}

	public boolean findUser(User user){
		Map map = new HashMap();
		map.put("user", user);
		List list = this.find("findUser", map);
		if(CollectionUtil.isNotEmpty(list)){
			User tmp = (User) list.get(0);
			if(!StringUtil.equalsIgnoreCase(user.getId(), tmp.getId())){
				return false;
			}
		}
		return true;
	}

	public User findUserByUsername(User user){
		Map map = new HashMap();
		map.put("user", user);
		List list = this.find("findUser", map);
		if(CollectionUtil.isNotEmpty(list)){
			return (User) list.get(0);
		}
		return null;
	}

	public List findUserByOrg(User user, PaginationList paginationList){
		User tmp = new User();
		BeanUtils.copyProperties(user, tmp);
		if(StringUtil.isNotEmpty(tmp.getUsername())){
			tmp.setUsername("%" + tmp.getUsername() + "%");
		}
		Map map = new HashMap();
		map.put("user", tmp);
		return this.find("findUserByOrg", map, paginationList);
	}

	public List findUsersByOrgId(String orgId){
		Map map = new HashMap();
		map.put("orgId", orgId);
		return this.find("findUserByOrgId", map);
	}

	public User getUser(User user){
		Map map = new HashMap();
		map.put("user", user);
		List list = this.find("getUser", map);
		if(CollectionUtil.isNotEmpty(list)){
			return (User) list.get(0);
		}else{
			return null;
		}
	}

	public void deleteUser(User user){
		this.delete("deleteUser", Long.valueOf(user.getId()));
	}

	public void deleteUser(List ids){
		if(CollectionUtil.isNotEmpty(ids)){
			this.delete("deleteUserOrganizatins", CollectionUtil
					.getStringSQLIds(ids));
			this.delete("deleteUserAuthoritys", CollectionUtil
					.getStringSQLIds(ids));
			this.delete("deleteUsers", CollectionUtil.getStringSQLIds(ids));
		}
	}
}
