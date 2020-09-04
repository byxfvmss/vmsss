package com.cjit.gjsz.filem.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.filem.model.t_org_config;

public interface OrgConfigeService{

	public List findAll(String orgName, String orgId, String userid,
			PaginationList paginationList);

	public List findOrgConfigs(String orgId, String rptNo);

	public List findOrgConfigList(String rptTitle);

	public t_org_config findAll(String orgId);

	public void save(String orgId, String rptNo, String rptTitle);

	public void save(String orgId, String orgName, String rptNo, String rptTitle);

	public List findCodeDictionaryList(String codeType,
			String codeValueStandardNum);

	public List findRptTitleList();

	public List findRptTitles(String userId, String instCode);
}