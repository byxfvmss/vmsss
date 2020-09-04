package com.cjit.gjsz.filem.action;

import java.util.Arrays;
import java.util.List;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.filem.service.AutoDealRptService;
import com.cjit.gjsz.system.service.OrganizationService;

public class UpFileAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;
	protected String[] selectSendPacks; // 已生成报文下载页面所选中上传的文件名称
	private AutoDealRptService autoDealRptService;
	private OrganizationService organizationService;

	public String upFile(){
		boolean isSelectSend = false;
		// 手动选择可以上传的文件
		List sendList = null;
		if(selectSendPacks != null
				&& (selectSendPacks.length > 1 || (selectSendPacks.length == 1 && StringUtil
						.isNotEmpty(selectSendPacks[0])))){
			sendList = Arrays.asList(selectSendPacks);
			isSelectSend = true;
		}else{
			String autoUpFile = request.getParameter("autoUpFile");
			if("Y".equalsIgnoreCase(autoUpFile)){
				String sendPack = this.request.getParameter("sendPack");
				selectSendPacks = new String[] {sendPack};
				if(selectSendPacks != null && selectSendPacks.length >= 1){
					sendList = Arrays.asList(selectSendPacks);
				}
			}
		}
		// 执行方法
		String result = autoDealRptService.uploadReport(sendList, null, null);
		// 返回结果
		if(isSelectSend){
			this.request.setAttribute("result", result);
			return "downFile";
		}else{
			String getType = request.getParameter("genPageType");
			if(result.equals("lock"))
				return "successlock" + getType;
			else if(result.equals("error"))
				return ERROR + getType;
			else
				return SUCCESS + getType;
		}
	}

	public String[] getSelectSendPacks(){
		return selectSendPacks;
	}

	public void setSelectSendPacks(String[] selectSendPacks){
		this.selectSendPacks = selectSendPacks;
	}

	public AutoDealRptService getAutoDealRptService(){
		return autoDealRptService;
	}

	public void setAutoDealRptService(AutoDealRptService autoDealRptService){
		this.autoDealRptService = autoDealRptService;
	}

	public OrganizationService getOrganizationService(){
		return organizationService;
	}

	public void setOrganizationService(OrganizationService organizationService){
		this.organizationService = organizationService;
	}
}
