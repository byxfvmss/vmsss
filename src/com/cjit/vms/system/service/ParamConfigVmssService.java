package com.cjit.vms.system.service;

import java.util.List;
import java.util.Map;

import com.cjit.vms.system.model.UBaseSysParamVmss;
import com.cjit.vms.taxdisk.single.model.BillCancel;

/**
 * <p>
 * 版权所有:(C)2003-2010
 * </p>
 * 
 * @作者: tom
 * @日期: 2009-6-24 上午10:03:08
 * @描述: [ThemeService]主题风格功能服务类
 */
public interface ParamConfigVmssService {

	public List findSystemId();

	public List findParamBytop(String systemId);

	public Map getParamsMap(List configs) throws Exception;

	public String findvaluebyName(String itemCname);

	public void saveParamConfig(List paramList);

	public void updateVmssParam(UBaseSysParamVmss param);

	/**
	 * 
	 * @param uBaseSysParamVmss
	 * @return List<UBaseSysParamVmss>
	 */
	public List findBaseVmssParamList(UBaseSysParamVmss uBaseSysParamVmss);

	/**
	 * @param itemKey
	 * @return uBaseSysParamVmss
	 */
	public UBaseSysParamVmss getUBaseSysParamVmssByKey(String itemKey);
	public UBaseSysParamVmss getUBaseSysParamVmssByName(String ParaName);
	public void updateUBaseSysParamVmssByParamId(String pValue,String paramId);
	
	//作废按钮
	public BillCancel getBillCancelByDiskNo(String DiskNo);
}
