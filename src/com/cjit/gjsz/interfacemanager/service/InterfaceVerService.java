package com.cjit.gjsz.interfacemanager.service;

import com.cjit.common.service.GenericService;

public interface InterfaceVerService extends GenericService{

	/**
	 * 国际收支网上申报系统与银行业务系统数据接口规范（1.2版）升级
	 * @return boolean
	 */
	public boolean upgradeInterfaceVer12();

	/**
	 * 国际收支网上申报系统与银行业务系统数据接口规范（1.2版）降级
	 * @return boolean
	 */
	public boolean degradeInterfaceVer12();
}
