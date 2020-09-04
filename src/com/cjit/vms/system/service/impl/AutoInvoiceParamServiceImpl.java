package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.AutoInvoiceParam;
import com.cjit.vms.system.service.AutoInvoiceParamService;

public class AutoInvoiceParamServiceImpl extends GenericServiceImpl implements
		AutoInvoiceParamService {

	public List findAutoInvoiceParamList(AutoInvoiceParam aip, PaginationList paginationList) {
		Map map = new HashMap();
		map.put("aip", aip);
		return find("findAutoInvoiceParam", map,paginationList);
	}

	public AutoInvoiceParam findAutoInvoiceParam(AutoInvoiceParam aip) {
		Map map = new HashMap();
		map.put("aip", aip);
		List list = find("findAutoInvoiceParam", map);
		if (CollectionUtil.isNotEmpty(list)) {
			return (AutoInvoiceParam) list.get(0);
		}
		return null;
	}

	public void saveAutoInvoiceParam(AutoInvoiceParam aip, boolean isUpdate) {
		Map map = new HashMap();
		map.put("aip", aip);
		if (!isUpdate) {
			this.save("insertAutoInvoiceParam", map);
		} else {
			this.save("updateAutoInvoiceParam", map);
		}
	}

	public void deleteAutoInvoiceParam(String paramId) {
		Map map = new HashMap();
		map.put("paramId", paramId);
		this.delete("deleteAutoInvoiceParam", map);
	}
}
