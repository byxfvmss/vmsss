package com.cjit.vms.taxdisk.tools;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;

public class InterfaceType implements Serializable {

	public static String interfaceType;
	public static String objectParam;

	private TaxDiskInfoQueryService taxDiskInfoQueryService;
	
	public void init() {
		Map map = taxDiskInfoQueryService.findSysParam("TAXPARAMETERS");
		String interfaceType = "";
		String objectParam = "";
		List<TaxDict> objList = taxDiskInfoQueryService.findCodeDictionaryList("TAXPARAMETERS", map.get("SELECTED_VALUE").toString());
		for (TaxDict taxDict : objList) {
			interfaceType = taxDict.getCode_name();
			objectParam = taxDict.getCode_type_desc();
			if (StringUtils.isEmpty(objectParam)) {
				objectParam = "";
			}
		}
		InterfaceType.interfaceType = interfaceType;
		InterfaceType.objectParam = objectParam;
	}


	public void setTaxDiskInfoQueryService(TaxDiskInfoQueryService taxDiskInfoQueryService) {
		this.taxDiskInfoQueryService = taxDiskInfoQueryService;
	}

}
