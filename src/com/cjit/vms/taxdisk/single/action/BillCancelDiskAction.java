package com.cjit.vms.taxdisk.single.action;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import cjit.crms.util.json.JsonUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.trans.model.billInvalid.BillCancelInfo;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.taxdisk.single.service.BillCancelDiskAssitService;
import com.cjit.vms.taxdisk.single.service.util.Message;

public class BillCancelDiskAction extends BaseDiskAction {
	private static final long serialVersionUID = 1L;
	
	//注入
	private BillCancelInfo	billCancelInfo=new BillCancelInfo();
	private ParamConfigVmssService paramConfigVmssService;
	private TransInfoService transInfoService;
	private BillCancelDiskAssitService billCancelDiskService;
	private List billCancelInfoList;
	private String taxParam;// 税控参数
	
	//发票作废(百旺)
	public String  listBillCancelDisk(){         
		if (!sessionInit(true)) {
			request.getSession().setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			User currentUser = this.getCurrentUser();
			Map map = transInfoService.findSysParam("OBSOLTETTIME");
			billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString());
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billCancelInfo = new BillCancelInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			// if (StringUtil.isNotEmpty(flag)) {
			// billInfo.setSearchFlag(flag);
			// }
			billCancelInfo.setBillBeginDate(this.getBillBeginDate());
			billCancelInfo.setBillEndDate(this.getBillEndDate());// 写到这里了
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			billCancelInfoList = billCancelService.findBillCancelList(
					billCancelInfo, currentUser.getId(), paginationList);
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");
			this.request.getSession().setAttribute("billInfoList", billCancelInfoList);
			this.request.getSession().setAttribute("billBeginDate",
					billCancelInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate",
					billCancelInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(),
					DateUtils.DATE_FORMAT_YYYYMM);
			this.request.getSession().setAttribute("currMonth", currMonth);
			if (taxParam.equals("2")) {
				return "tax";
			} else {
				return "disk";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-listBillCancel", e);
		}
		return ERROR;
	}


	//创建作废XMl   
	public void createBillCancelDiskXml() throws Exception{
		String billId = request.getParameter("billId");
		String diskNo=request.getParameter("diskNo");
		String currentUser=this.getCurrentUser().getId();
		Message message =new Message();
		try {
			//String StringXml=billCancelDiskService.createBillCancelXMl(billId, diskNo, currentUser);
			message.setReturnCode(Message.success);
			//message.setStringXml(StringXml);
		} catch (Exception e) {
			message.setReturnCode(Message.error);
			message.setReturnMsg(Message.system_exception_bill_cancel_Xml_error);
			}
	}
	
      
	//更改票据状态
	public void updateBillCancelResult() throws Exception{
		String StringXml=request.getParameter("StringXml");
		String billId = request.getParameter("billId");
		Message message =new Message();
		try {
		//	billCancelDiskService.updateBillCancelResult(StringXml,billId);
			message.setReturnCode(Message.success);
		} catch (Exception e) {
			message.setReturnCode(Message.error);
			message.setReturnMsg(Message.update_bill_cancel_result_error); 
			printWriterResult(JsonUtil.toJsonString(message));
		}
		printWriterResult(JsonUtil.toJsonString(message));
	}

	
	
	
	
	
	
	

	public BillCancelInfo getBillCancelInfo() {
		return billCancelInfo;
	}


	public void setBillCancelInfo(BillCancelInfo billCancelInfo) {
		this.billCancelInfo = billCancelInfo;
	}


	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}


	public void setParamConfigVmssService(
			ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}


	public String getTaxParam() {
		return taxParam;
	}


	public void setTaxParam(String taxParam) {
		this.taxParam = taxParam;
	}



	public List getBillCancelInfoList() {
		return billCancelInfoList;
	}



	public void setBillCancelInfoList(List billCancelInfoList) {
		this.billCancelInfoList = billCancelInfoList;
	}
 

	public TransInfoService getTransInfoService() {
		return transInfoService;
	}


 

	public void setTransInfoService(TransInfoService transInfoService) {
		this.transInfoService = transInfoService;
	}

	public BillCancelDiskAssitService getBillCancelDiskService() {
		return billCancelDiskService;
	}

	public void setBillCancelDiskService(BillCancelDiskAssitService billCancelDiskService) {
		this.billCancelDiskService = billCancelDiskService;
	}


	
	
	
	
}
