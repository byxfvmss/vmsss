package com.cjit.gjsz.system.action;

import java.util.Iterator;
import java.util.List;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.RelaTables;
import com.cjit.gjsz.system.model.RoleUser;

public class RelaTablesAction extends BaseListAction {

	private static final long serialVersionUID = 1L;
	private SystemCache systemCache;
	private UserInterfaceConfigService userInterfaceConfigService;
	private List roleList;
	private List relaTableList;
	private RoleUser roleUser = new RoleUser();
	private String roleId;
	private String message = "";
	private String isSave;
	protected String[] selectTableIds;

	public String search() {
		if ("1".equals(isSave)) {
			message = "保存成功！";
		}
		this.request.setAttribute("message", this.message);
		try {
			RoleUser roleUser = new RoleUser();
			roleUser.setEnabled("1");
			roleUser.setSystemId(Constants.SYSTEM_ID);
			roleList = userInterfaceConfigService.findVRoleUser(roleUser, null);
			this.request.setAttribute("roleList", roleList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("RelaTablesAction-search", e);
		}
		return ERROR;
	}

	public String edit() {
		if ("1".equals(isSave)) {
			message = "保存成功！";
		}
		RoleUser roleUser = new RoleUser();
		roleUser.setRoleId(roleId);
		roleUser.setEnabled("1");
		roleUser.setSystemId(Constants.SYSTEM_ID);
		roleList = userInterfaceConfigService.findVRoleUser(roleUser, null);
		if (roleList != null && roleList.size() == 1) {
			this.roleUser = (RoleUser) roleList.get(0);
			if (this.roleUser != null
					&& StringUtil.isNotEmpty(this.roleUser.getRoleId())) {
				relaTableList = userInterfaceConfigService
						.findRelaTablesByRoleId(this.roleUser.getRoleId());
				if (CollectionUtil.isNotEmpty(relaTableList)) {
					boolean allIsRela = true;
					for (Iterator i = relaTableList.iterator(); i.hasNext();) {
						RelaTables rt = (RelaTables) i.next();
						if (!"1".equals(rt.getIsRela())) {
							allIsRela = false;
							break;
						}
					}
					if (allIsRela) {
						this.request.setAttribute("allIsRela", "true");
					}
				}
			}
		}
		this.request.setAttribute("roleUser", roleUser);
		this.request.setAttribute("relaTableList", relaTableList);
		this.request.setAttribute("message", this.message);
		return SUCCESS;
	}

	public String save() {
		try {
			if (StringUtil.isNotEmpty(this.roleId)) {
				userInterfaceConfigService.deleteRelaTables(this.roleId, null,
						"R");
				if (this.selectTableIds != null
						&& this.selectTableIds.length > 0) {
					StringBuffer sbInsertColumns = new StringBuffer()
							.append("objId,tableId,fileType,objType");
					StringBuffer sbInsertValues = null;
					for (int i = 0; i < this.selectTableIds.length; i++) {
						String tableId = null;
						String fileType = null;
						if (this.selectTableIds[i].length() > 0
								&& this.selectTableIds[i].indexOf("#") > 0) {
							tableId = this.selectTableIds[i].split("#")[0];
							fileType = this.selectTableIds[i].split("#")[1];
						}
						sbInsertValues = new StringBuffer();
						sbInsertValues.append("'").append(this.roleId).append(
								"','").append(tableId).append("','").append(
								fileType).append("','R'");
						userInterfaceConfigService.insertRelaTables(
								sbInsertColumns.toString(), sbInsertValues
										.toString());
					}
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("RelaTablesAction-save", e);
		}
		return ERROR;
	}

	public SystemCache getSystemCache() {
		return systemCache;
	}

	public void setSystemCache(SystemCache systemCache) {
		this.systemCache = systemCache;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public List getRoleList() {
		return roleList;
	}

	public void setRoleList(List roleList) {
		this.roleList = roleList;
	}

	public List getRelaTableList() {
		return relaTableList;
	}

	public void setRelaTableList(List relaTableList) {
		this.relaTableList = relaTableList;
	}

	public RoleUser getRoleUser() {
		return roleUser;
	}

	public void setRoleUser(RoleUser roleUser) {
		this.roleUser = roleUser;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getIsSave() {
		return isSave;
	}

	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}

	public String[] getSelectTableIds() {
		return selectTableIds;
	}

	public void setSelectTableIds(String[] selectTableIds) {
		this.selectTableIds = selectTableIds;
	}
}
