package com.cjit.vms.taxdisk.single.action;

import java.io.PrintWriter;

import cjit.crms.util.json.JsonUtil;

import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import com.cjit.vms.taxdisk.single.service.util.Message;

public class BaseDiskAction extends DataDealAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TaxDiskInfoQueryService taxDiskInfoQueryService; 
	public void createQueryTaxDiskXml() throws Exception{
		String StringXml="";
		Message message=new Message();
		try{
		 StringXml= taxDiskInfoQueryService.createTaxDiskInfoQueryXml();
		 message.setStringXml(StringXml);
		 message.setReturnCode(Message.success);
		}catch (Exception e) {
			message.setReturnMsg(Message.system_exception_Xml_error);
			message.setReturnCode(Message.error);
			printWriterResult(JsonUtil.toJsonString(message));
		}
		printWriterResult(JsonUtil.toJsonString(message));
	}
	public void checkTaxDiskTaxNoandTaxNo() throws Exception{
		String result="";
		try {
			String instID=this.getCurrentUser().getOrgId();
			String StringXml=request.getParameter("StringXml");
			result=taxDiskInfoQueryService.checkTaxDiskTaxNoAndInstNo(StringXml, instID);
		} catch (Exception e) {
			
		}
		printWriterResult(result);
	}
	protected void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}
	public TaxDiskInfoQueryService getTaxDiskInfoQueryService() {
		return taxDiskInfoQueryService;
	}
	public void setTaxDiskInfoQueryService(
			TaxDiskInfoQueryService taxDiskInfoQueryService) {
		this.taxDiskInfoQueryService = taxDiskInfoQueryService;
	}
	public String getFapiaoTypeCh(String fapiaoType){
		fapiaoType=fapiaoType=="0"?"004":"007";
		return fapiaoType;
		 
	}
	
	public String getFapiaoType(String fapiaoType){
		fapiaoType=fapiaoType=="004"?"0":"1";
		return fapiaoType;
	}
	
}
