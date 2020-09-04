package com.cjit.vms.trans.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.trans.model.EmsInfo;
import com.cjit.vms.trans.service.EmsInfoService;

public class EmsInfoServiceImpl extends GenericServiceImpl implements EmsInfoService {

	public EmsInfo findEmsInfo(String billNo) {
		EmsInfo emsInfo = new EmsInfo();
		emsInfo.setBillNo(billNo);
		List list = this.findEmsInfoList(emsInfo);
//		EmsInfo eInfo = (EmsInfo)list.get(0);
//		for (int i = 0; i < emsStatusList.size(); i++) {
//			String key = ((Dictionary) emsStatusList.get(i))
//					.getValueStandardLetter();
//			String value = ((Dictionary) emsStatusList.get(i)).getName();
//			if (null != eInfo.getStatus()
//					&& key.equals(eInfo.getStatus())) {
//				eInfo.setStatus(value);
//			}
//		}
		if (list != null && list.size() == 1) {
			return (EmsInfo)list.get(0);
		} else {
			return null;
		}
	}

	public List findEmsInfoList(EmsInfo emsInfo){
		Map map = new HashMap();
		map.put("emsInfo", emsInfo);
		return find("findEmsInfo",map);
	}

	public void saveEmsInfo(EmsInfo emsInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("emsInfo", emsInfo);
		if (isUpdate) {
			this.save("updateEms", param);
		} else {
			this.save("saveEms", param);
		}
	}

	public void deleteEmsInfo(String billNo) {
		Map map = new HashMap();
		map.put("billNo", billNo);
		this.delete("deleteEms", map);
	}

	public void updateEmsStatus(String billNo) {
		Map map = new HashMap();
		map.put("billNo", billNo);
		map.put("emsStatus", "3");
		this.delete("updateEmsStatus", map);
	}
}
