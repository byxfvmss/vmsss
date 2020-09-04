package com.cjit.vms.input.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.InputVatInfo;
import com.cjit.vms.input.service.InputVatInfoService;

public class InputVatInfoServiceImpl extends GenericServiceImpl implements
		InputVatInfoService {

	public List findInputVatInfoList(InputVatInfo inputVatInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("inputVatInfo", inputVatInfo);
		return find("findInputVatInfo", map, paginationList);
	}

	public List findInputVatInfoList(InputVatInfo inputVatInfo) {
		Map map = new HashMap();
		map.put("inputVatInfo", inputVatInfo);
		return find("findInputVatInfo", map);
	}

	public InputVatInfo findInputVatInfo(String inVatId) {
		InputVatInfo inputVatInfo = new InputVatInfo();
		inputVatInfo.setInVatId(inVatId);
		List list = this.findInputVatInfoList(inputVatInfo);
		if (list != null && list.size() == 1) {
			return (InputVatInfo) list.get(0);
		} else {
			return null;
		}
	}

	public void saveInputVatInfo(InputVatInfo inputVatInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("inputVat", inputVatInfo);
		if (isUpdate) {
			this.save("updateInputVatInfo", param);
		} else {
			this.save("saveInputVatInfo", param);
		}
	}

	public void deleteInputVatInfo(String inVatId, String billCode,
			String billNo) {
		Map param = new HashMap();
		if (StringUtil.isNotEmpty(inVatId)) {
			param.put("inVatId", inVatId);
			this.delete("deleteInputVatInfo", param);
		} else if (StringUtil.isNotEmpty(billCode)
				&& StringUtil.isNotEmpty(billCode)) {
			param.put("billCode", billCode);
			param.put("billNo", billCode);
			this.delete("deleteInputVatInfo", param);
		}
	}
	
	public void deleteInputTransInfo(String importDate, String dataStatus){
		Map param = new HashMap();
		param.put("importDate", importDate);
		param.put("dataStatus", dataStatus);
		this.delete("deleteInputTransInfo", param);
	}

}
