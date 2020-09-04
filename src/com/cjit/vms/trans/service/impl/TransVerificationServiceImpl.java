package com.cjit.vms.trans.service.impl;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.service.TransVerificationService;

public class TransVerificationServiceImpl extends GenericServiceImpl implements
		TransVerificationService {

	@Override
	public List selectTransVerification(Map parameters, PaginationList paginationList) {

		if (null != paginationList) {
			return find("findTransVerification", parameters, paginationList);
		}
		return find("findTransVerification", parameters);
	}

	@Override
	public void insertTransVerification(Map parameters) {
		save("insertTransVerification", parameters);

	}

	@Override
	public void updateTransVerification(Map parameters) {
		update("updateTransVerification", parameters);
	}

	@Override
	public void deleteTransVerification(Map parameters) {
		delete("deleteTransVerification", parameters);
		
	}

	@Override
	public void updateTransVerificationGoodsNo(Map parameters) {
		update("updateTransVerificationGoodsNo", parameters);
	}
	
	

}
