package com.cjit.gjsz.filem.action;

import java.util.List;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.filem.model.t_org_config;
import com.cjit.gjsz.filem.service.OrgConfigeService;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;

public class OrgConfigeAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrgConfigeService orgconfigeservice;
	private LogManagerService logManagerService;
	private SystemCache systemCache;
	private List org;
	private List org1;
	private t_org_config t_org = new t_org_config();
	// JSP页面的参数
	private String orgName = "";
	private String orgId = "";
	private String rptNo = "";
	private String rptTitle = "";
	private String message = "";
	private String isSave;

	public String getIsSave(){
		return isSave;
	}

	public void setIsSave(String isSave){
		this.isSave = isSave;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String search(){
		try{
			User user = this.getCurrentUser();
			String userid = user.getId();
			org = orgconfigeservice.findAll(orgName, orgId, userid,
					paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String view(){
		if ("1".equals(isSave)) {
			message = "保存成功！";
		}
		this.request.setAttribute("message", this.message);
		try{
			orgName = "";
			orgId = "";
			User user = this.getCurrentUser();
			String userId = user.getId();
			org = orgconfigeservice.findAll(orgName, orgId, userId,
					paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String save(){
		if(orgName != null
				&& (orgName.indexOf("\"") > 0 || orgName.indexOf("'") > 0)){
			message = "保存失败！机构名称不可含有双引号或单引号。";
		}
		if(rptNo != null && rptNo.length() == 12){
			String strDistrictCode = rptNo.substring(0, 6);
			List list = orgconfigeservice.findCodeDictionaryList("DISTRICTCO",
					strDistrictCode);
			if(list == null || list.size() <= 0){
				message = "保存失败！机构申报号前六位数字地区标识码有误。";
			}
			if(!StringUtil.isNumberOrLatter(rptNo)){
				message = "保存失败！机构申报号不可含中文、特殊字符、空格";
			}
		}else{
			message = "保存失败！机构申报号位数有误。";
		}
		if(rptTitle != null && rptTitle.length() == 12){
			String strDistrictCode = rptTitle.substring(0, 6);
			List list = orgconfigeservice.findCodeDictionaryList("DISTRICTCO",
					strDistrictCode);
			if(list == null || list.size() <= 0){
				message = "保存失败！主报告行号前六位数字地区标识码有误。";
			}
			if(!StringUtil.isNumberOrLatter(rptTitle)){
				message = "保存失败！主报告行号不可含中文、特殊字符、空格";
			}
		}else{
			message = "保存失败！主报告行号位数有误。";
		}
		if(message != null && message.startsWith("保存失败")){
			this.request.setAttribute("message", message);
			t_org = orgconfigeservice.findAll(orgId);
			t_org.setRptNo(rptNo);
			t_org.setRptTitle(rptTitle);
			orgName = "";
			orgId = "";
			return "rptnoerror";
		}
		orgconfigeservice.save(orgId, orgName, rptNo, rptTitle);
		User user = this.getCurrentUser();
		logManagerService.writeLog(request, user, "0006.0007", "系统管理.机构对照管理",
				"保存", "保存申报号信息", "1");
		//add by panshaobo 每次保存之后，重新缓存mts配置
		getSystemCache().initConfigMts();
		// orgName = "";
		// //orgId = "";
		return SUCCESS;
	}

	public String edit(){
		if("1".equals(isSave)){
			message = "保存成功！";
			this.request.setAttribute("message", message);
		}
		t_org = orgconfigeservice.findAll(orgId);
		orgName = "";
		orgId = "";
		return SUCCESS;
	}

	public OrgConfigeService getOrgconfigeservice(){
		return orgconfigeservice;
	}

	public void setOrgconfigeservice(OrgConfigeService orgconfigeservice){
		this.orgconfigeservice = orgconfigeservice;
	}

	public List getOrg(){
		return org;
	}

	public void setOrg(List org){
		this.org = org;
	}

	public String getOrgName(){
		return orgName;
	}

	public void setOrgName(String orgName){
		this.orgName = orgName;
	}

	public String getOrgId(){
		return orgId;
	}

	public void setOrgId(String orgId){
		this.orgId = orgId;
	}

	public List getOrg1(){
		return org1;
	}

	public void setOrg1(List org1){
		this.org1 = org1;
	}

	public t_org_config getT_org(){
		return t_org;
	}

	public void setT_org(t_org_config t_org){
		this.t_org = t_org;
	}

	public String getRptNo(){
		return rptNo;
	}

	public void setRptNo(String rptNo){
		this.rptNo = rptNo;
	}

	public void setLogManagerService(LogManagerService logManagerService){
		this.logManagerService = logManagerService;
	}

	public String getRptTitle(){
		return rptTitle;
	}

	public void setRptTitle(String rptTitle){
		this.rptTitle = rptTitle;
	}

	public SystemCache getSystemCache() {
		return systemCache;
	}

	public void setSystemCache(SystemCache systemCache) {
		this.systemCache = systemCache;
	}
}
