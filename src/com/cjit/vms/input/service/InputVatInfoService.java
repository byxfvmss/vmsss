package com.cjit.vms.input.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.InputVatInfo;

public interface InputVatInfoService {

	/* 进项税票据相关 */
	public List findInputVatInfoList(InputVatInfo inputVatInfo,
			PaginationList paginationList);

	public List findInputVatInfoList(InputVatInfo inputVatInfo);

	public InputVatInfo findInputVatInfo(String inVatId);

	public void saveInputVatInfo(InputVatInfo inputVatInfo, boolean isUpdate);

	public void deleteInputVatInfo(String inVatId, String billCode,
			String billNo);
	
	/* 进项税交易相关 */
	public void deleteInputTransInfo(String importDate, String dataStatus);
}
