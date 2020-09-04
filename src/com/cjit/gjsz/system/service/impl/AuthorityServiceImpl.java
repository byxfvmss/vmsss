/**
 * Authority Service
 */
package com.cjit.gjsz.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.Authority;
import com.cjit.gjsz.system.service.AuthorityService;

/**
 * @author huboA
 */
public class AuthorityServiceImpl extends GenericServiceImpl implements
		AuthorityService{

	public List findAuthority(Authority authority){
		Map map = new HashMap();
		map.put("authority", authority);
		return this.find("findAuthority", map);
	}

	public List findAuthorityByParentId(Authority authority){
		Map map = new HashMap();
		map.put("authority", authority);
		return this.find("findAuthorityByParentId", map);
	}

	public boolean checkAuthority(Authority authority){
		Map map = new HashMap();
		map.put("authority", authority);
		List authoritys = this.find("checkAuthority", map);
		if(CollectionUtil.isNotEmpty(authoritys)){
			Authority tmp = (Authority) authoritys.get(0);
			if(authority.getId() != tmp.getId()){
				return false;
			}
		}
		return true;
	}

	public void deleteAuthority(Authority authority){
		throw new UnsupportedOperationException("还未实现");
	}

	public void deleteAuthority(List ids){
		if(CollectionUtil.isNotEmpty(ids)){
			this
					.delete("deleteAuthoritys", CollectionUtil
							.getNumberSQLIds(ids));
		}
	}

	public List findAuthority(Authority authority, PaginationList paginationList){
		Authority tmp = new Authority();
		BeanUtils.copyProperties(authority, tmp);
		Map map = new HashMap();
		if(StringUtil.isNotEmpty(tmp.getName())){
			tmp.setName("%" + tmp.getName() + "%");
		}
		map.put("authority", tmp);
		return find("findAuthority", map, paginationList);
	}

	public Authority getAuthority(Authority authority){
		Map map = new HashMap();
		map.put("authority", authority);
		List list = this.find("getAuthority", map);
		if(CollectionUtil.isNotEmpty(list)){
			return (Authority) list.get(0);
		}else{
			return null;
		}
	}

	public void saveAuthority(Authority authority){
		Map map = new HashMap();
		map.put("authority", authority);
		if(authority.getId() > 0){
			this.update("updateAuthority", map);
		}else{
			this.save("saveAuthority", map);
		}
	}

	// public List findAuthorityByUser(User user){
	// Map map = new HashMap();
	// map.put("user", user);
	// return this.find("findAuthorityByUser", map);
	// }
	// public List findAuthorityByUser(User user, Object obj){
	// Map map = new HashMap();
	// map.put("user", user);
	// return this.find("findNameAuthorityByUser", map);
	// }
	public List find(String query, Map parameters){
		return super.find(query, parameters);
	}
}
