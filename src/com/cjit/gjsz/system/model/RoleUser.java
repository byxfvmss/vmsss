package com.cjit.gjsz.system.model;

import java.io.Serializable;

/**
 * @author lihaiboA
 */
public class RoleUser implements Serializable{

	private static final long serialVersionUID = -1L;
	private String roleId;
	private String roleName;
	private String enabled;
	private String systemId;
	private String userId;

	public String getRoleId(){
		return roleId;
	}

	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	public String getRoleName(){
		return roleName;
	}

	public void setRoleName(String roleName){
		this.roleName = roleName;
	}

	public String getEnabled(){
		return enabled;
	}

	public void setEnabled(String enabled){
		this.enabled = enabled;
	}

	public String getSystemId(){
		return systemId;
	}

	public void setSystemId(String systemId){
		this.systemId = systemId;
	}

	public String getUserId(){
		return userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}
}