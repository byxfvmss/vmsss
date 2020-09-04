package com.cjit.gjsz.filem.action;

import java.util.ArrayList;
import java.util.List;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.action.DataDealAction;
import com.cjit.gjsz.filem.model.CustomerConfigEntity;
import com.cjit.gjsz.filem.service.CustConfigService;
import com.cjit.gjsz.logic.impl.BaseDataVerify;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;

//组织机构代码名称对照表
public class CustomerConfigAction extends DataDealAction{

	private static final long serialVersionUID = 1L;
	private CustConfigService custConfigService;
	private LogManagerService logManagerService;
	// JSP页面的参数
	private String custId = "";
	private String custCode = "";
	private String custName = "";
	private String instCode = "";
	private String userName = "";
	private String message = "";
	private String isSave;
	private String[] custIds = null;
	private String[] custCodes = null;
	//
	private List custlist;
	private CustomerConfigEntity custConfig;

	// 查询列表
	public String list(){
		try{
			User user = this.getCurrentUser();
			if(authInstList == null)
				authInstList = new ArrayList();
			authInstList.addAll(user.getOrgs());
			if(authInstList.size() > 0){
				if(instCode == null || "".equals(instCode)){
					Organization org = (Organization) authInstList.get(0);
					if(org != null){
						instCode = org.getId();
					}
				}
				custlist = custConfigService.findAll(custId, custCode,
						custName, instCode, paginationList);
			}
			this.request.getSession().setAttribute("custId", custId);
			this.request.getSession().setAttribute("custCode", custCode);
			this.request.getSession().setAttribute("custName", custName);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			this.request.setAttribute("paginationList", this.paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	// 返回列表
	public String view(){
		try{
			User user = this.getCurrentUser();
			if(authInstList == null)
				authInstList = new ArrayList();
			authInstList.addAll(user.getOrgs());
			if(authInstList.size() > 0){
				if(instCode == null || "".equals(instCode)){
					Organization org = (Organization) authInstList.get(0);
					if(org != null){
						instCode = org.getId();
					}
				}
			}
			custId = (String) this.request.getSession().getAttribute("custId");
			custCode = (String) this.request.getSession().getAttribute(
					"custCode");
			custName = (String) this.request.getSession().getAttribute(
					"custName");
			custlist = custConfigService.findAll(custId, custCode, custName,
					instCode, paginationList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			this.request.setAttribute("paginationList", this.paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	// 查询实体
	public String edit(){
		if("success".equals(isSave)){
			message = "保存成功！";
		}else if("error".equals(isSave)){
			message = "保存失败！组织机构代码不符合规范！";
		}else if("repeat".equals(isSave)){
			message = "保存失败！组织机构代码已配置！";
			custCode = null;
		}
		this.request.setAttribute("message", message);
		if(custCode != null && !"".equals(custCode)){
			custConfig = custConfigService.findCustomerConfigByCustCode(
					instCode, custCode);
		}
		if(custConfig == null){
			custConfig = new CustomerConfigEntity();
			if(instCode != null && !"".equals(instCode)){
				custConfig.setInstCode(instCode);
			}
			if(StringUtil.isNotEmpty(custId)){
				custConfig.setCustId(custId);
			}
			if(StringUtil.isNotEmpty(custName)){
				custConfig.setCustName(custName);
			}
		}
		this.request.setAttribute("custConfig", custConfig);
		return SUCCESS;
	}

	// 保存
	public String save(){
		User user = this.getCurrentUser();
		if(BaseDataVerify.verifyCustcode(custCode)){
			if(StringUtil.isEmpty(userName)
					&& custConfigService
							.judgeRepeatCustCode(instCode, custCode)){
				logManagerService.writeLog(request, user, "0006.0008",
						"系统管理.ETL特殊机构对照", "保存", "保存ETL特殊机构对照", "0");
				try{
					custName = java.net.URLEncoder.encode(custName, "UTF-8");
				}catch (Exception e){
					e.printStackTrace();
				}
				this.isSave = "repeat";
			}else{
				String userName = user.getName();
				custConfig = new CustomerConfigEntity();
				custConfig.setCustId(custId);
				custConfig.setCustCode(custCode);
				custConfig.setCustName(custName);
				custConfig.setInstCode(instCode);
				custConfig.setUserName(userName);
				custConfig.setModifyTime(DateUtils.serverCurrentTimeStamp());
				custConfigService.save(custConfig);
				logManagerService.writeLog(request, user, "0006.0008",
						"系统管理.ETL特殊机构对照", "保存", "保存ETL特殊机构对照", "1");
				this.isSave = "success";
			}
		}else{
			logManagerService.writeLog(request, user, "0006.0008",
					"系统管理.ETL特殊机构对照", "保存", "保存ETL特殊机构对照", "0");
			this.isSave = "error";
		}
		this.request.setAttribute("isSave", this.isSave);
		this.request.setAttribute("instCode", this.instCode);
		return SUCCESS;
	}

	// 删除
	public String delete(){
		User user = this.getCurrentUser();
		CustomerConfigEntity custConfigTemp = null;
		StringBuffer sbLogDesc = new StringBuffer();
		try{
			if(custCodes != null && custCodes.length > 0){
				for(int i = 0; i < custCodes.length; i++){
					custConfigTemp = custConfigService
							.findCustomerConfigByCustCode(instCode,
									custCodes[i]);
					if(custConfigTemp != null){
						custConfigService.delete(instCode, custCodes[i]);
						sbLogDesc.append("删除ETL特殊机构对照 CustomerID：").append(
								custIds[i]).append("，组织机构代码：").append(
								custConfigTemp.getCustCode())
								.append("，组织机构名称：").append(
										custConfigTemp.getCustName());
						logManagerService.writeLog(request, user, "0006.0008",
								"系统管理.ETL特殊机构对照", "删除", sbLogDesc.toString(),
								"1");
					}
				}
			}
			return SUCCESS;
		}catch (Exception e){
			if(custConfigTemp != null){
				sbLogDesc.append("删除ETL特殊机构对照 CustomerID：").append(
						custConfigTemp.getCustId()).append("，组织机构代码：").append(
						custConfigTemp.getCustCode()).append("，组织机构名称：")
						.append(custConfigTemp.getCustName());
				logManagerService.writeLog(request, user, "0006.0008",
						"系统管理.ETL特殊机构对照", "删除", sbLogDesc.toString(), "0");
			}
			e.printStackTrace();
			return ERROR;
		}
	}

	public CustConfigService getCustConfigService(){
		return custConfigService;
	}

	public void setCustConfigService(CustConfigService custConfigService){
		this.custConfigService = custConfigService;
	}

	public LogManagerService getLogManagerService(){
		return logManagerService;
	}

	public void setLogManagerService(LogManagerService logManagerService){
		this.logManagerService = logManagerService;
	}

	public String getCustId(){
		return custId;
	}

	public void setCustId(String custId){
		this.custId = custId;
	}

	public String getCustCode(){
		return custCode;
	}

	public void setCustCode(String custCode){
		this.custCode = custCode;
	}

	public String getCustName(){
		return custName;
	}

	public void setCustName(String custName){
		this.custName = custName;
	}

	public String getInstCode(){
		return instCode;
	}

	public void setInstCode(String instCode){
		this.instCode = instCode;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getIsSave(){
		return isSave;
	}

	public void setIsSave(String isSave){
		this.isSave = isSave;
	}

	public List getCustlist(){
		return custlist;
	}

	public void setCustlist(List custlist){
		this.custlist = custlist;
	}

	public String[] getCustIds(){
		return custIds;
	}

	public void setCustIds(String[] custIds){
		this.custIds = custIds;
	}

	public String[] getCustCodes(){
		return custCodes;
	}

	public void setCustCodes(String[] custCodes){
		this.custCodes = custCodes;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}
}
