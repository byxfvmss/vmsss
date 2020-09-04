/**
 * 
 */
package com.cjit.gjsz.datadeal.model;

import java.util.List;

import com.cjit.gjsz.datadeal.util.DataUtil;

/**
 * @author yulubin
 */
public class RptTableInfo {

	private String id;
	private String infoType;
	private String tableId;
	private String tableName;
	private String isShow;
	private String description;
	private List listDescription; // 使用连接的方式，描述未通过信息，并直接跳转
	private String descTitle; // 比description更详细的描述
	private String orderBys; // 业务表范围
	private String fileType;
	private String isEnabled; // 是否激活
	private String safeTableId;
	private String canInput; // 是否可录入
	private String notFileType; // 不包括的表单类型
	private String tableDesc;

	public RptTableInfo() {
	}

	public RptTableInfo(String infoType, String isShow, String isEnabled) {
		this.infoType = infoType;
		this.isShow = isShow;
		this.isEnabled = isEnabled;
	}

	public RptTableInfo(String tableId, String fileType) {
		this.tableId = tableId;
		this.fileType = fileType;
	}

	public RptTableInfo(String tableId) {
		this.tableId = tableId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescTitle() {
		return descTitle;
	}

	public void setDescTitle(String descTitle) {
		this.descTitle = descTitle;
	}

	public String getOrderBys() {
		return orderBys;
	}

	public void setOrderBys(String orderBys) {
		this.orderBys = orderBys;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public List getListDescription() {
		return listDescription;
	}

	public void setListDescription(List listDescription) {
		this.listDescription = listDescription;
	}

	public String getUniqueTable() {
		return new StringBuffer(this.tableId).append(
				DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL).append(this.fileType)
				.toString();
	}

	public String getSafeTableId() {
		return safeTableId;
	}

	public void setSafeTableId(String safeTableId) {
		this.safeTableId = safeTableId;
	}

	public String getNotFileType() {
		return notFileType;
	}

	public void setNotFileType(String notFileType) {
		this.notFileType = notFileType;
	}

	public String getCanInput() {
		return canInput;
	}

	public void setCanInput(String canInput) {
		this.canInput = canInput;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
}
