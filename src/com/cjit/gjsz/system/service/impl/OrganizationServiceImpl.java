/**
 * Organization Service
 */
package com.cjit.gjsz.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.gjsz.system.service.UserService;

/**
 * @author huboA
 */
public class OrganizationServiceImpl extends GenericServiceImpl implements
		OrganizationService{

	private UserService userService;

	public String findRptNo(String customId){
		Map map = new HashMap();
		map.put("customid", customId);
		List l = this.find("getRptNoByCustomId", map);
		if(CollectionUtils.isNotEmpty(l)){
			return (String) l.get(0);
		}
		return "null";
	}

	public List findOrganization(Organization org){
		Map map = new HashMap();
		map.put("organization", org);
		return this.find("findOrganization", map);
	}

	public boolean checkOrganization(Organization org){
		Map map = new HashMap();
		map.put("organization", org);
		List organizations = null;
		if(StringUtil.equalsIgnoreCase(org.getNews(), "yes")){
			organizations = this.find("checkOrganization", map);
			if(CollectionUtil.isNotEmpty(organizations)){
				return false;
			}
			organizations = this.find("checkOrganizationCustomId", map);
			if(CollectionUtil.isNotEmpty(organizations)){
				return false;
			}
			return true;
		}else{
			organizations = this.find("checkOrganizationCustomId", map);
			if(CollectionUtil.isNotEmpty(organizations)){
				Organization tmp = (Organization) organizations.get(0);
				if(StringUtil.equalsIgnoreCase(tmp.getId(), org.getId())){
					return true;
				}
				return false;
			}
			return true;
		}
	}

	public void deleteOrganization(Organization org){
		throw new UnsupportedOperationException("还未实现");
	}

	public void deleteOrganization(List ids){
		List userIds = new ArrayList();
		// 删除机构下的用户
		if(CollectionUtil.isNotEmpty(ids)){
			for(int i = 0; i < ids.size(); i++){
				String tmpId = (String) ids.get(i);
				if((tmpId != null) && !(tmpId.equals(""))){
					List usrList = userService.findUsersByOrgId(tmpId);
					if(CollectionUtil.isNotEmpty(usrList)){
						for(int j = 0; j < usrList.size(); j++){
							User tmpUsr = new User();
							userIds.add(tmpUsr.getId());
						}
						userService.deleteUser(userIds);
					}
				}
			}
		}
		this.delete("deleteOrganizationsUsers", CollectionUtil
				.getStringSQLIds(ids));
		this.delete("deleteOrganizations", CollectionUtil.getStringSQLIds(ids));
	}

	public List findOrganization(Organization org, PaginationList paginationList){
		Organization tmp = new Organization();
		BeanUtils.copyProperties(org, tmp);
		Map map = new HashMap();
		if(StringUtil.isNotEmpty(tmp.getName())){
			tmp.setName("%" + tmp.getName() + "%");
		}
		map.put("organization", tmp);
		return find("findOrganization", map, paginationList);
	}

	public Organization getOrganization(Organization org){
		Map map = new HashMap();
		map.put("organization", org);
		List list = this.find("getOrganization", map);
		if(CollectionUtil.isNotEmpty(list)){
			return (Organization) list.get(0);
		}else{
			return null;
		}
	}

	public String saveOrganization(Organization org){
		Map map = new HashMap();
		map.put("organization", org);
		Organization tmp = getOrganization(org);
		if(StringUtil.isNotEmpty(org.getId()) && tmp != null){
			this.update("updateOrganization", map);
		}else{
			this.save("saveOrganization", map);
		}
		return org.getId();
	}

	public List findOrganizationByUser(User user){
		Map map = new HashMap();
		map.put("user", user);
		return this.find("findOrganizationByUser", map);
	}

	public List findOrganizationByRights(User user){
		Map map = new HashMap();
		map.put("user", user);
		return this.find("findOrganizationByRights", map);
	}

	public List findOrganizationByUser(User user, Object obj){
		Map map = new HashMap();
		map.put("user", user);
		return this.find("findNameOrganizationByUser", map);
	}

	public String findRptTitle(String customId){
		Map map = new HashMap();
		map.put("customid", customId);
		List l = this.find("getRptTitleByCustomId", map);
		if(CollectionUtils.isNotEmpty(l)){
			return (String) l.get(0);
		}
		return "null";
	}

	public UserService getUserService(){
		return userService;
	}

	public void setUserService(UserService userService){
		this.userService = userService;
	}

	// 按机构号去找机构名称
	public String getOrgNameByInstCode(String instCode){
		Map map = new HashMap();
		map.put("instcode", instCode);
		List l = this.find("getOrgNameByInstcode", map);
		if(CollectionUtils.isNotEmpty(l))
			return (String) l.get(0);
		return null;
	}

	public String findInstCode(String rptNo){
		Map map = new HashMap();
		map.put("rptNo", rptNo);
		List l = this.find("getInstCodeByRptNo", map);
		if(CollectionUtils.isNotEmpty(l)){
			return (String) l.get(0);
		}
		return null;
	}

	public List findOrgIdsWithSameRptTitle(String instCode){
		Map map = new HashMap();
		map.put("instCode", instCode);
		return this.find("findOrgIdsWithSameRptTitle", map);
	}

	@Override
	public List findUserOrganization(User user, Map obj) {
		Map map = new HashMap();
		map.put("user", user);
		map.put("par", obj);
		return this.find("findUserOrganization", map);
		
	}

	@Override
	public Organization findTaxNoByInstCode(String instCode) {
		Map map = new HashMap();
		map.put("instCode", instCode);
		List list = this.find("findTaxNoByInstCode", map);
		Organization org = (Organization) list.get(0);
		return org;
	}
	
	
}
