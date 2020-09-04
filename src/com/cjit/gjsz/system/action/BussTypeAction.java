package com.cjit.gjsz.system.action;

import java.io.IOException;
import java.util.List;

import cjit.crms.util.StringUtil;

import com.cjit.gjsz.datadeal.action.DataDealAction;
import com.cjit.gjsz.system.model.BussType;
import com.cjit.gjsz.system.model.User;

/**
 * @作者: lihaiboA
 * @日期: Sep 30, 2013 10:39:30 AM
 * @描述: [BussTypeAction]业务类型管理控制类
 */
public class BussTypeAction extends DataDealAction{

	private static final long serialVersionUID = 1L;
	private String bussTypeCode = "";
	private String bussTypeName = "";
	private List bussType;
	private BussType bt = new BussType();
	private String message = "";
	private String isSave;
	private String isCreate;

	public String search(){
		try{
			User user = this.getCurrentUser();
			String userid = user.getId();
			bussType = dataDealService.findBussTypeList(bussTypeCode,
					bussTypeName);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String create(){
		bussTypeCode = "";
		bussTypeName = "";
		bt = new BussType();
		this.request.setAttribute("bt", bt);
		this.isCreate = "yes";
		this.request.setAttribute("isCreate", isCreate);
		return SUCCESS;
	}

	public String edit(){
		if("1".equals(isSave)){
			message = "保存成功！";
			this.request.setAttribute("message", message);
		}
		List list = dataDealService.findBussTypeList(bussTypeCode, null);
		if(list != null){
			bt = (BussType) list.get(0);
			this.request.setAttribute("bt", bt);
		}
		return SUCCESS;
	}

	public String save(){
		User user = this.getCurrentUser();
		List list = dataDealService
				.findBussTypeList(bt.getBussTypeCode(), null);
		if(list != null && list.size() > 0){
			message = "保存失败！业务类型编号已存在。";
			this.bussTypeCode = bt.getBussTypeCode();
			logManagerService.writeLog(request, user, "0030.0090",
					"系统管理.业务类型管理", "保存", "保存业务类型信息", "0");
			this.isCreate = "yes";
			this.request.setAttribute("isCreate", isCreate);
			return ERROR;
		}
		if(bt.getBussTypeName() != null
				&& (bt.getBussTypeName().indexOf("\"") > 0 || bt
						.getBussTypeName().indexOf("'") > 0)){
			message = "保存失败！业务类型名称不可含有双引号或单引号。";
			this.bussTypeCode = bt.getBussTypeCode();
			logManagerService.writeLog(request, user, "0030.0090",
					"系统管理.业务类型管理", "保存", "保存业务类型信息", "0");
			this.isCreate = "yes";
			this.request.setAttribute("isCreate", isCreate);
			return ERROR;
		}
		dataDealService
				.saveBussType(bt.getBussTypeCode(), bt.getBussTypeName());
		this.bussTypeCode = bt.getBussTypeCode();
		logManagerService.writeLog(request, user, "0030.0090", "系统管理.业务类型管理",
				"保存", "保存业务类型信息", "1");
		return SUCCESS;
	}

	public String update(){
		User user = this.getCurrentUser();
		if(bt.getBussTypeName() != null
				&& (bt.getBussTypeName().indexOf("\"") > 0 || bt
						.getBussTypeName().indexOf("'") > 0)){
			message = "保存失败！业务类型名称不可含有双引号或单引号。";
			this.bussTypeCode = bt.getBussTypeCode();
			logManagerService.writeLog(request, user, "0030.0090",
					"系统管理.业务类型管理", "保存", "保存业务类型信息", "0");
			return SUCCESS;
		}
		dataDealService.updateBussType(bt.getBussTypeCode(), bt
				.getBussTypeName(), null);
		this.bussTypeCode = bt.getBussTypeCode();
		logManagerService.writeLog(request, user, "0030.0090", "系统管理.业务类型管理",
				"保存", "保存业务类型信息", "1");
		return SUCCESS;
	}

	public String changeBussTypeEnabled(){
		try{
			String bussTypeCode = request.getParameter("bussTypeCode");
			String checked = request.getParameter("checked");
			String isEnabled = "";
			if(StringUtil.isNotEmpty(bussTypeCode)){
				List list = dataDealService
						.findBussTypeList(bussTypeCode, null);
				if(list != null){
					BussType bt = (BussType) list.get(0);
					if("true".equalsIgnoreCase(checked)){
						isEnabled = "1";
					}else{
						isEnabled = "0";
					}
					dataDealService.updateBussType(bt.getBussTypeCode(), bt
							.getBussTypeName(), isEnabled);
				}
			}
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(isEnabled);
			this.response.getWriter().close();
		}catch (IOException ex){
			ex.printStackTrace();
		}
		return null;
	}

	public String getBussTypeCode(){
		return bussTypeCode;
	}

	public void setBussTypeCode(String bussTypeCode){
		this.bussTypeCode = bussTypeCode;
	}

	public String getBussTypeName(){
		return bussTypeName;
	}

	public void setBussTypeName(String bussTypeName){
		this.bussTypeName = bussTypeName;
	}

	public List getBussType(){
		return bussType;
	}

	public void setBussType(List bussType){
		this.bussType = bussType;
	}

	public BussType getBt(){
		return bt;
	}

	public void setBt(BussType bt){
		this.bt = bt;
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

	public String getIsCreate(){
		return isCreate;
	}

	public void setIsCreate(String isCreate){
		this.isCreate = isCreate;
	}
}
