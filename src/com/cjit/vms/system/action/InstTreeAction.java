package com.cjit.vms.system.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.constant.Constants;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.vms.trans.action.DataDealAction;

public class InstTreeAction extends DataDealAction {

	OrganizationService organizationService;

	/**
	 * <p>
	 * 方法名称: loadInstXml|描述: 异步获取部门
	 * </p>
	 */
	public void loadInstXml() {
		// 按机构名称查询机构
		String bankName = request.getParameter("bankName");
		try {
			if (StringUtils.isNotEmpty(bankName)) {
				bankName = new String(bankName.getBytes("iso8859-1"),"utf-8");
			}
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		Map map = new HashMap();
		map.put("name", bankName);
		
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		List authInstList = organizationService.findUserOrganization(currentUser, map);

		List resList = new ArrayList();

		for (int i = 0; i < authInstList.size(); i++) {
			Organization inst = (Organization) authInstList.get(i);
			Map itemMap = new HashMap();
			itemMap.put("id", inst.getId());
			itemMap.put("name", inst.getName());
			if (null == inst.getParentId() || "".equals(inst.getParentId())) {
				itemMap.put("pId", "0");
			} else {
				itemMap.put("pId", inst.getParentId());
			}
			resList.add(itemMap);
		}

		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(
					JSONArray.fromObject(resList).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/***
	 * 加载当前用户所有机构的基本信息
	 */
	public void loadInstFullInfoXml(){
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		Map map = new HashMap();
		List authInstList = organizationService.findUserOrganization(currentUser, map);
		
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(
					JSONArray.fromObject(authInstList).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	// 税项监控-增值税对账-完整性对账
	public void loadInstXmlWiethCheckAccount() {
		// 按机构名称查询机构
		String bankName = request.getParameter("bankName");
		try {
			if (StringUtils.isNotEmpty(bankName)) {
				bankName = new String(bankName.getBytes("iso8859-1"),"utf-8");
			}
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		Map map = new HashMap();
		map.put("name", bankName);
		
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		List authInstList = organizationService.findUserOrganization(currentUser, map);
		
		List resList = new ArrayList();
		String instId = "";
		for (int i = 0; i < authInstList.size(); i++) {
			Organization inst = (Organization) authInstList.get(i);
			Map itemMap = new HashMap();
			itemMap.put("id", inst.getId());
			itemMap.put("name", inst.getName());
			if (null == inst.getParentId() || "".equals(inst.getParentId())) {
				instId = inst.getId();
				itemMap.put("pId", "0");
			} else {
				if(inst.getParentId().equals(instId)){
					itemMap.put("pId", inst.getParentId());
				}else{
					itemMap.put("pId", "");
				}
			}
			if(itemMap.get("pId")!=null&&!itemMap.get("pId").equals("")){
				resList.add(itemMap);
			}
		}
		
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(
					JSONArray.fromObject(resList).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public OrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
