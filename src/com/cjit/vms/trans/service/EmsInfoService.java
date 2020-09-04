package com.cjit.vms.trans.service;

import com.cjit.vms.trans.model.EmsInfo;

public interface EmsInfoService {
//	public EmsInfo findEmsInfo(String billNo, List emsStatusList);
	public EmsInfo findEmsInfo(String billNo);
	public void saveEmsInfo(EmsInfo emsInfo, boolean isUpdate);
	public void deleteEmsInfo(String billNo);
	public void updateEmsStatus(String billNo);
}
