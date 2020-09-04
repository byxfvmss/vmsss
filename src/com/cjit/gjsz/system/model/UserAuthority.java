/**
 * 权限PO
 */
package com.cjit.gjsz.system.model;

import java.io.Serializable;

/**
 * @author huboA
 */
public class UserAuthority implements Serializable{

	private static final long serialVersionUID = -1195032677719224364L;
	private int id;
	private String userId;
	private int authorityId;

	public int getId(){
		return id;
	}

	public String getUserId(){
		return userId;
	}

	public int getAuthorityId(){
		return authorityId;
	}

	public void setId(int id){
		this.id = id;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public void setAuthorityId(int authorityId){
		this.authorityId = authorityId;
	}
}
