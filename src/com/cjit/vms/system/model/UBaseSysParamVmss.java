package com.cjit.vms.system.model;

/**
 * <p>
 * 版权所有:CHINAJESIT
 * </p>
 * 
 * @描述: [UBaseSysParamDO]请在此简要描述类的功能
 */
public class UBaseSysParamVmss {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 参数id */
	private Integer paramId;
	/** 子系统id */
	private String systemId;
	/** 参数项中文 */
	private String itemCname;
	/** 参数项值 */
	private String selectedValue;// paramId
	/** 参数英文KEY */
	private String itemKey;
	/** 排列次序 */
	private Integer orderBy;
	/**
	 * xml导出path*/
	private String xmlPath;
	
	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	/**
	 * @return paramId
	 */
	public Integer getParamId() {
		return paramId;
	}

	/**
	 * @param paramId
	 *            要设置的 paramId
	 */
	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}

	/**
	 * @return systemId
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * @param systemId
	 *            要设置的 systemId
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	/**
	 * @return itemCname
	 */
	public String getItemCname() {
		return itemCname;
	}

	/**
	 * @param itemCname
	 *            要设置的 itemCname
	 */
	public void setItemCname(String itemCname) {
		this.itemCname = itemCname;
	}

	/**
	 * @return selectedValue
	 */
	public String getSelectedValue() {
		return selectedValue;
	}

	/**
	 * @param selectedValue
	 *            要设置的 selectedValue
	 */
	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
}
