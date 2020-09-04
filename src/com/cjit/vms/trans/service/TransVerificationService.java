package com.cjit.vms.trans.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;

public interface TransVerificationService {

	public List selectTransVerification(Map parameters,PaginationList paginationList);
	public void insertTransVerification(Map parameters);
	public void updateTransVerification(Map parameters);
	public void deleteTransVerification(Map parameters);
	public void updateTransVerificationGoodsNo(Map parameters);
}
