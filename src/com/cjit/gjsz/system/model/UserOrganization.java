/**
 * 权限PO
 */
package com.cjit.gjsz.system.model;

import java.io.Serializable;

/**
 * @author huboA
 */
public class UserOrganization implements Serializable{

	private static final long serialVersionUID = -1195032677719224364L;
	private int id;
	private String userId;
	private String organizationId;

	public int getId(){
		return id;
	}

	public String getUserId(){
		return userId;
	}

	public void setId(int id){
		this.id = id;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getOrganizationId(){
		return organizationId;
	}

	public void setOrganizationId(String organizationId){
		this.organizationId = organizationId;
	}
}
