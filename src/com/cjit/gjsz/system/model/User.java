/**
 * 机构PO
 */
package com.cjit.gjsz.system.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author huboA
 */
public class User implements Serializable {

	private static final long serialVersionUID = -1195032677719224364L;
	private String id;
	private String name;
	private String password;
	private String oldPassword;
	private String username;
	private String description;
	private String orgId;
	private String orgName;
	private String orgFullName;
	private String customId;

	public User() {
	}

	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	private String authoritys;
	private int changePassword;
	private List ids; // 功能权限
	private List orgIds; // 保存,修改时机构权限
	private List orgs; // 验证权限时
	private boolean hasModify = false;
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List getOrgs() {
		return orgs;
	}

	public void setOrgs(List orgs) {
		this.orgs = orgs;
	}

	public String getPassword() {
		return password;
	}

	public String getOrgId() {		
		return orgId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAuthoritys() {
		return authoritys;
	}

	public void setAuthoritys(String authoritys) {
		this.authoritys = authoritys;
	}

	public List getIds() {
		return ids;
	}

	public void setIds(List ids) {
		this.ids = ids;
	}

	public String getUsername() {
		return username;
	}

	public String getDescription() {
		return description;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List orgIds) {
		this.orgIds = orgIds;
	}

	public int getChangePassword() {
		return changePassword;
	}

	public void setChangePassword(int changePassword) {
		this.changePassword = changePassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public boolean isHasModify() {
		return hasModify;
	}

	public void setHasModify(boolean hasModify) {
		this.hasModify = hasModify;
	}

	public String getOrgFullName() {
		return orgFullName;
	}

	public void setOrgFullName(String orgFullName) {
		this.orgFullName = orgFullName;
	}
}
