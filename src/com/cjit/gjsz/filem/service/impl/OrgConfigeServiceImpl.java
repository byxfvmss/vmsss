package com.cjit.gjsz.filem.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.filem.model.t_org_config;
import com.cjit.gjsz.filem.service.OrgConfigeService;

public class OrgConfigeServiceImpl extends GenericServiceImpl implements
		OrgConfigeService{

	public List findAll(String orgName, String orgId, String userid,
			PaginationList paginationList){
		Map map = new HashMap();
		map.put("orgName", orgName);
		map.put("orgId", orgId);
		map.put("userid", userid);
		if(paginationList != null){
			return this.find("getorgConfige", map, paginationList);
		}else{
			return this.find("getorgConfige", map);
		}
	}

	public List findOrgConfigs(String orgId, String rptNo){
		Map map = new HashMap();
		map.put("orgId", orgId);
		map.put("rptNo", rptNo);
		return this.find("getOrgConfigs", map);
	}

	public t_org_config findAll(String org_Id){
		Map map = new HashMap();
		map.put("orgId", org_Id);
		List l = this.find("getorgConfigeById", map);
		t_org_config t = null;
		if(CollectionUtils.isNotEmpty(l))
			t = (t_org_config) l.get(0);
		return t;
	}

	public void save(String orgId, String rptNo, String rptTitle){
		Map map = new HashMap();
		map.put("orgId", orgId);
		map.put("rptNo", rptNo);
		map.put("rptTitle", rptTitle);
		List l = this.find("getorgConfigeByOrgId", map);
		if(CollectionUtils.isEmpty(l)){
			this.save("insertgetorgConfigeByOrgId", map);
		}else{
			this.save("updateorg", map);
		}
	}

	public List findCodeDictionaryList(String codeType,
			String codeValueStandardNum){
		Map param = new HashMap();
		param.put("codeType", codeType);
		param.put("codeValueStandardNum", codeValueStandardNum);
		return this.find("findCodeDictionaryList", param);
	}

	public List findOrgConfigList(String rptTitle){
		Map map = new HashMap();
		map.put("rptTitle", rptTitle);
		return this.find("findOrgConfig", map);
	}

	public void save(String orgId, String orgName, String rptNo, String rptTitle){
		Map map = new HashMap();
		map.put("orgId", orgId);
		map.put("orgName", orgName);
		map.put("rptNo", rptNo);
		map.put("rptTitle", rptTitle);
		List l = this.find("getorgConfigeByOrgId", map);
		if(CollectionUtils.isEmpty(l)){
			this.save("insertgetorgConfigeByOrgId", map);
		}else{
			this.save("updateorg", map);
		}
	}

	public List findRptTitleList(){
		return this.find("findRptTitleList", new HashMap());
	}

	public List findRptTitles(String userId, String instCode){
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("orgId", instCode);
		return find("findRptTitles", map);
	}
}
