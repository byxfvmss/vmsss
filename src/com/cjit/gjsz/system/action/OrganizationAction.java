/**
 * OrganizationAction
 */
package com.cjit.gjsz.system.action;

import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.service.OrganizationService;

/**
 * @author huboA
 */
public class OrganizationAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1582213395156394080L;
	private OrganizationService organizationService;
	private Organization organization = new Organization();

	public String list(){
		try{
			organizationService.findOrganization(organization, paginationList);
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
			if(organizationService.checkOrganization(organization)){
				organizationService.saveOrganization(organization);
				return SUCCESS;
			}else{
				organization.setError("yes");
				this.addActionMessage("[行内机构编码] 或 [报送编码] 已存在，请重新输入");
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
			organizationService.deleteOrganization(ids);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String view(){
		try{
			organization = organizationService.getOrganization(organization);
			if(organization == null){
				this.addActionError("访问的机构不存在。");
				return ERROR;
			}else{
				organization.setNews("no");
			}
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			this.addActionError("错误信息为：" + e.getMessage());
		}
		return ERROR;
	}

	public void setOrganizationService(OrganizationService organizationService){
		this.organizationService = organizationService;
	}

	public Organization getOrganization(){
		return organization;
	}

	public void setOrganization(Organization organization){
		this.organization = organization;
	}
}
