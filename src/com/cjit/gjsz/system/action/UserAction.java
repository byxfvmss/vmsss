/**
 * UserAction
 */
package com.cjit.gjsz.system.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.system.model.Authority;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.AuthorityService;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.gjsz.system.service.UserService;

/**
 * @author huboA
 */
public class UserAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -325015966294787820L;
	private UserService userService;
	private OrganizationService organizationService;
	private AuthorityService authorityService;
	private List orgs;
	private Integer[] authoritys;
	private String[] organizations;
	private User user = new User();
	private String xmlString = "";
	private String id = "";
	private List allUserRight;
	private List menus = new ArrayList();

	// private String isAudit = "0";
	public List getAllUserRight(){
		return allUserRight;
	}

	public void setAllUserRight(List allUserRight){
		this.allUserRight = allUserRight;
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String list(){
		try{
			userService.findUserByOrg(user, paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String create(){
		try{
			orgs = organizationService.findOrganization(null);
			ids = authorityService.findAuthority(null);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String save(){
		try{
			if(userService.findUser(user)){
				userService.saveUser(user);
				return SUCCESS;
			}else{
				this.addActionMessage("该登录名已存在，请重新输入");
				create();
				return INPUT;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String saveUserRights(){
		try{
			userService.saveUserRights(user);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String saveUserOrganiazatins(){
		try{
			userService.saveUseOrganiazatins(user);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String delete(){
		try{
			userService.deleteUser(ids);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String modifyPassword(){
		try{
			User tmp = userService.getUser(user);
			if(tmp != null){
				if(StringUtil.equals(user.getOldPassword(), tmp.getPassword())){
					tmp.setPassword(user.getPassword());
					tmp.setChangePassword(1);
					user.setHasModify(true);
					userService.saveUser(tmp);
				}else{
					this.addActionMessage("原密码输入有误,请重新输入。");
					return INPUT;
				}
				return SUCCESS;
			}else{
				this.addActionMessage("该用户不存在。");
				return INPUT;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String changePassword(){
		if(StringUtil.equalsIgnoreCase(modifyPassword(), SUCCESS)){
			user = userService.getUser(user);
			return SUCCESS;
		}
		return INPUT;
	}

	public String view(){
		try{
			user = userService.getUser(user);
			if(user == null){
				this.addActionError("该用户不存在");
				return ERROR;
			}
			orgs = organizationService.findOrganization(null);
			ids = authorityService.findAuthority(null);
			// List authRights = authorityService.findAuthorityByUser(user);
			List orgRights = this.organizationService
					.findOrganizationByRights(user);
			// authoritys = (Integer[]) authRights.toArray(new Integer[0]);
			organizations = (String[]) orgRights.toArray(new String[0]);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			this.addActionError("错误信息为：" + e.getMessage());
		}
		return ERROR;
	}

	public String viewUserRights(){
		try{
			user = userService.getUser(user);
			if(user == null){
				this.addActionError("该用户不存在");
				return ERROR;
			}
			allUserRight = this.getAllUserRights();
			// List authRights = authorityService.findAuthorityByUser(user);
			// authoritys = (Integer[]) authRights.toArray(new Integer[0]);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			this.addActionError("错误信息为：" + e.getMessage());
		}
		return ERROR;
	}

	private List getAllUserRights(){
		List allRights = new ArrayList();
		Authority tmpAuthority = new Authority();
		tmpAuthority.setId(-1);
		List allFirstLevAuth = authorityService
				.findAuthorityByParentId(tmpAuthority);
		for(int i = 0; i < allFirstLevAuth.size(); i++){
			Authority tmp = (Authority) allFirstLevAuth.get(i);
			List childLevAuth = authorityService.findAuthorityByParentId(tmp);
			Map authorityMap = new HashMap();
			authorityMap.put(tmp.getName(), childLevAuth);
			allRights.add(authorityMap);
		}
		return allRights;
	}

	/**
	 * 异步获取部门和用户
	 */
	public void loadDeptAndUsrXml(){
		User user = this.getCurrentUser();
		StringBuffer sb = new StringBuffer();
		List userList = null;
		if(user != null){
			orgs = organizationService.findOrganization(null);
			if((id != null && !(id.equals("")))){
				// 取该机构下的所有用户
				userList = userService.findUsersByOrgId(id);
				if(userList != null){
					sb.append("<tree id='");
					sb.append(id);
					sb.append("'>");
					for(int j = 0; j < userList.size(); j++){
						User tmpUser = (User) userList.get(j);
						sb.append("<item text='");
						sb.append(tmpUser.getName());
						sb.append("' id='");
						sb.append(tmpUser.getId());
						sb.append("'>");
						sb.append("<userdata name='levelType'>2</userdata>");
						sb.append("</item>");
					}
					sb.append("</tree> ");
				}
			}else{
				sb.append("<?xml version='1.0' encoding='iso-8859-1'?>");
				sb.append("<tree id='0'>");
				for(int i = 0; i < orgs.size(); i++){
					Organization tmporg = (Organization) orgs.get(i);
					sb.append("<item text='");
					sb.append(tmporg.getName());
					sb.append("' id='");
					sb.append(tmporg.getId());
					sb.append("' child='1");
					sb.append("'>");
					sb.append("<userdata name='levelType'>1</userdata>");
					sb.append("</item>");
				}
				sb.append("</tree>");
			}
		}
		xmlString = sb.toString();
		System.out.println(xmlString);
		try{
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(xmlString);
			this.response.getWriter().close();
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}

	public String menu(){
		User user = this.getCurrentUser();
		List ids = user.getIds();
		if((ids != null) && (ids.size() > 0)){
			List topMenu = new ArrayList();
			List subMenu = new ArrayList();
			// 先处理第一级
			for(int i = 0; i < ids.size(); i++){
				Integer rightId = (Integer) (ids.get(i));
				// if(isAudit.equals("0") && rightId.intValue() == 3){
				// // TODO:Fix...
				// continue;
				// }
				Authority tmpAuthority = new Authority();
				tmpAuthority.setId(rightId.intValue());
				Authority loadAuthority = authorityService
						.getAuthority(tmpAuthority);
				if(loadAuthority.getParentId() == -1){
					topMenu.add(loadAuthority);
				}else{
					subMenu.add(loadAuthority);
				}
			}
			// 对应起来
			for(int j = 0; j < topMenu.size(); j++){
				List tmpList = new ArrayList();
				Authority topAuthority = (Authority) topMenu.get(j);
				for(int k = 0; k < subMenu.size(); k++){
					Authority subAuthority = (Authority) subMenu.get(k);
					if(subAuthority.getParentId() == topAuthority.getId()){
						tmpList.add(subAuthority);
					}
				}
				Map authorityMap = new HashMap();
				authorityMap.put(topAuthority.getName(), tmpList);
				menus.add(authorityMap);
			}
		}
		return SUCCESS;
	}

	public void setUserService(UserService userService){
		this.userService = userService;
	}

	public User getUser(){
		return user;
	}

	public void setUser(User user){
		this.user = user;
	}

	public void setOrganizationService(OrganizationService organizationService){
		this.organizationService = organizationService;
	}

	public List getOrgs(){
		return orgs;
	}

	public void setAuthorityService(AuthorityService authorityService){
		this.authorityService = authorityService;
	}

	public Integer[] getAuthoritys(){
		return authoritys;
	}

	public void setAuthoritys(Integer[] authoritys){
		this.authoritys = authoritys;
	}

	public String[] getOrganizations(){
		return organizations;
	}

	public void setOrganizations(String[] organizations){
		this.organizations = organizations;
	}

	public String getXmlString(){
		return xmlString;
	}

	public void setXmlString(String xmlString){
		this.xmlString = xmlString;
	}

	public List getMenus(){
		return menus;
	}

	public void setMenus(List menus){
		this.menus = menus;
	}

	public void setOrgs(List orgs){
		this.orgs = orgs;
	}
	// public String getIsAudit(){
	// return isAudit;
	// }
	// public void setIsAudit(String isAudit){
	// this.isAudit = isAudit;
	// }
}
