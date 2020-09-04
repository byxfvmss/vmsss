package com.cjit.gjsz.userimport.service;

import java.util.List;

import com.cjit.common.service.GenericService;

public interface UserImportService extends GenericService{

	public List getBatsByOrgId(String orgId);

	public List getFilesByOrgId(String orgId);
}
