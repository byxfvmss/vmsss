/**
 * AuthorityAction
 */
package com.cjit.gjsz.system.action;

import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.system.model.Authority;
import com.cjit.gjsz.system.service.AuthorityService;

/**
 * @author huboA
 */
public class AuthorityAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8277217121709502797L;
	private AuthorityService authorityService;
	private Authority authority = new Authority();

	public String list(){
		try{
			authorityService.findAuthority(authority, paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String create(){
		try{
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String save(){
		try{
			if(authorityService.checkAuthority(authority)){
				authorityService.saveAuthority(authority);
				return SUCCESS;
			}else{
				this.addActionMessage("该功能权限已存在，请重新输入");
				create();
				return INPUT;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String delete(){
		try{
			authorityService.deleteAuthority(ids);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String view(){
		try{
			authority = authorityService.getAuthority(authority);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public void setAuthorityService(AuthorityService authorityService){
		this.authorityService = authorityService;
	}

	public Authority getAuthority(){
		return authority;
	}

	public void setAuthority(Authority authority){
		this.authority = authority;
	}
}
