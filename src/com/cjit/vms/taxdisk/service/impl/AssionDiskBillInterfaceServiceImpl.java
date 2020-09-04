package com.cjit.vms.taxdisk.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.aisino.single.model.GoldFacilityReq;
import com.cjit.vms.taxdisk.aisino.single.model.InvoiceIssueReq;
import com.cjit.vms.taxdisk.aisino.single.model.InvoicePrintReq;
import com.cjit.vms.taxdisk.aisino.single.model.InvoiceVoidReq;
import com.cjit.vms.taxdisk.aisino.single.model.parseJson.GoldFacilityReturnJSON;
import com.cjit.vms.taxdisk.aisino.single.model.parseJson.InvoiceIssueResp;
import com.cjit.vms.taxdisk.aisino.single.model.parseJson.InvoicePrintResp;
import com.cjit.vms.taxdisk.aisino.single.model.parseJson.InvoiceVoidResp;
import com.cjit.vms.taxdisk.aisino.single.util.JsonUtil;
import com.cjit.vms.taxdisk.service.BillInterfaceService;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.service.BillCancelDiskAssitService;
import com.cjit.vms.taxdisk.single.service.BillIssueDiskAssitService;
import com.cjit.vms.taxdisk.single.service.BillPrintDiskAssistService;
import com.cjit.vms.taxdisk.single.service.PageTaxInvoiceDiskAssitService;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.taxdisk.tools.Message;
import com.cjit.vms.trans.model.UBaseInst;

public class AssionDiskBillInterfaceServiceImpl extends GenericServiceImpl implements BillInterfaceService {

	public static final String INTERFACE_TYPE = "assion_disk";
	
	private BillIssueDiskAssitService billIssueDiskAssitService;
	private TaxDiskInfoQueryService taxDiskInfoQueryService;
	private BillPrintDiskAssistService billPrintDiskAssistService;
	private BillCancelDiskAssitService billCancelDiskAssitService;
	private PageTaxInvoiceDiskAssitService pageTaxInvoiceDiskAssitService;

	@Override
	public AjaxReturn createBillissue(Map params) throws Exception {
		AjaxReturn message=null;
		try {
			BillInfo billInfo=billIssueDiskAssitService.findBillInfoById((String)params.get("billId"));
			InvoiceIssueReq invoiceIssueReq = new InvoiceIssueReq(billInfo, null);
			message=new AjaxReturn(true); 
			Map map=new HashMap();
			map.put("json",JsonUtil.toJson(invoiceIssueReq));
			message.setAttributes(map);
		} catch (Exception e) {
			message=new AjaxReturn(false,Message.no_disk_info);
			return message;
		}
		return message;
	}

	@Override
	public AjaxReturn updateBillIssueResult(Map params) throws Exception {
		AjaxReturn message=null;
		InvoiceIssueResp bill = null;
		try {
			bill = JsonUtil.toBean((String)params.get("json"), InvoiceIssueResp.class);
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_bill_Issue_info_query_erroe);
			return message;
		}
		String falg=bill.getHisInfoTypeCode();
		if (falg.equals(TaxDiskUtil.id_bill_issue)) {
					message=billIssueDiskAssitService.updateBillIssueResult(bill.getInfoTypeCode(),bill.getInfoNumber(), 
					(String)params.get("billId"), (String)params.get("diskNo"),(String)params.get("MachineNo"),
					(String)params.get("userId"), bill.getInfoDate(),bill.getRetmsg(),bill.getRetcode().equals(TaxDiskUtil.return_TaxDisk_info_success));
		} else {
			message=new AjaxReturn(true);
		}

		return message;
	}
	@Override
	public AjaxReturn createBillPrint(Map params) throws Exception {
		AjaxReturn message=null;
		try {
			BillInfo  billInfo  =billIssueDiskAssitService.findBillInfoById((String)params.get("billId"));
			InvoicePrintReq invoicePrintReq = new InvoicePrintReq(billInfo, null);
			message=new AjaxReturn(true);
			Map map=new HashMap();
			map.put("json", JsonUtil.toJson(invoicePrintReq));
			message.setAttributes(map);
		} catch (Exception e) {
			message=new AjaxReturn(false,Message.system_exception_bill_print_Json_error);
			return message;
		}
		return message;
	}

	@Override
	public AjaxReturn updateBillPrintResult(Map params) throws Exception {
		AjaxReturn message=null;
		InvoicePrintResp billPrint = null;
		try {
			billPrint = JsonUtil.toBean((String)params.get("json"), InvoicePrintResp.class);
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_print_Xml_error);
			return message;
		}
		return billPrintDiskAssistService.
		updateBillPrintResult((String)params.get("billId"),  billPrint.getRetmsg(),billPrint.getRetcode().equals(billPrint.getRetcode()));
	}

	@Override
	public AjaxReturn createBillCancel(Map params) throws Exception {
		AjaxReturn message=null;
		try {
			BillInfo billInfo =billIssueDiskAssitService.findBillInfoById((String)params.get("billId"));;
			InvoiceVoidReq invoiceVoidReq = new InvoiceVoidReq(billInfo);
			message=new AjaxReturn(true);
			Map map=new HashMap();
			map.put("json", JsonUtil.toJson(invoiceVoidReq));
			message.setAttributes(map);
			
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_bill_cancel_Json_error);
			return message;
		}
		return message;
	}

	@Override
	public AjaxReturn updateBillCancelResult(Map params) throws Exception {
		AjaxReturn message=null;
		InvoiceVoidResp billcancel=null;
		try {
			 billcancel=JsonUtil.toBean((String)params.get("json"), InvoiceVoidResp.class);
		} catch (Exception e) {
			message=new AjaxReturn(false,Message.parse_bill_cancel_Xml_error);
			return message;
		}
		return billCancelDiskAssitService.updateBillCancelResult((String)params.get("billId"),billcancel.getRetmsg(),billcancel.getRetcode().equals(TaxDiskUtil.return_TaxDisk_info_success) );
	}

	@Override
	public AjaxReturn createTaxItemInfo(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn saveTaxItemInfo(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn createTaxMonitor(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn saveTaxMonitor(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn createStockInfo(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn saveStockInfo(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn createStockIssue(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn saveStockIssue(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn createStockRecover(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn saveStockRecover(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn createTaxInfo(Map params) throws Exception {
		AjaxReturn message=null;
		Map map = new HashMap();
		List list=	find("findTaxDiskapwd",map);
		String pwd="";
		if(list.size()==1){
			pwd=(String) list.get(0);
		}else{
			message=new AjaxReturn(false, Message.pwd_no);
			return message;
		}
		GoldFacilityReq goldFacility = new GoldFacilityReq(TaxDiskUtil.id_tax_disk_information_query,pwd);
		map.put("json", JsonUtil.toJson(goldFacility));
		message=new AjaxReturn(true);
		message.setAttributes(map);
		return message;
	}

	@Override
	public AjaxReturn checkTaxInfo(Map params) throws Exception {
		AjaxReturn message=null;
		String MachineNo=null;
		try {
			UBaseInst inst = taxDiskInfoQueryService.findTaxDiskInfoByinstID((String)params.get("instCode"));
			if (inst.getTaxperNumber().isEmpty()) {
				message=new AjaxReturn(false, Message.blank_inst_tax_no);
				return message;
			}
			GoldFacilityReturnJSON goldFacilityReturnJSON = JsonUtil.toBean((String)params.get("json"), GoldFacilityReturnJSON.class);
			if (goldFacilityReturnJSON.getRetcode().equals(TaxDiskUtil.return_TaxDisk_info_success)) {
				MachineNo=goldFacilityReturnJSON.getMachineNo();
				if(!inst.getTaxperName().equals(goldFacilityReturnJSON.getCorpName())){
					message=new AjaxReturn(false, Message.inst_name_Not_name);
					
					return	message; 
				}
			}else{
				message=new AjaxReturn(false, goldFacilityReturnJSON.getRetmsg());
				return	message;
			}
			
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception);
			return	message; 
		}
		message=new AjaxReturn(true);
		Map map=new HashMap();
		map.put("MachineNo", MachineNo);
		message.setAttributes(map);
		return	message;
	}

	@Override
	public AjaxReturn saveTaxInfo(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * base64 加密字符串
	 * 加密后会将加密串种的 + 号替换为 _
	 */
	private String encodeBase64(String value, String charsetName) throws Exception {
		if (StringUtils.isEmpty(value)) {
			value = "";
		}
		value = new String(Base64.encodeBase64(value.getBytes(charsetName)), charsetName);
		value = value.replaceAll("\\+", "\\_");
		return value;
	}

	/*
	 * base64 解密字符串
	 */
	private String decodeBase64(String value, String charsetName) throws Exception {
		if (StringUtils.isEmpty(value)) {
			value = "";
		}
		value = new String(Base64.decodeBase64(value.getBytes(charsetName)), charsetName);
		return value;
	}

	@Override
	public AjaxReturn createRegistInfo(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn checkRegistInfo(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn checkCurBillNoInfo(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AjaxReturn createCurBillNoInfo(Map params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBillIssueDiskAssitService(BillIssueDiskAssitService billIssueDiskAssitService) {
		this.billIssueDiskAssitService = billIssueDiskAssitService;
	}

	public void setTaxDiskInfoQueryService(TaxDiskInfoQueryService taxDiskInfoQueryService) {
		this.taxDiskInfoQueryService = taxDiskInfoQueryService;
	}

	public void setBillPrintDiskAssistService(BillPrintDiskAssistService billPrintDiskAssistService) {
		this.billPrintDiskAssistService = billPrintDiskAssistService;
	}

	public void setBillCancelDiskAssitService(BillCancelDiskAssitService billCancelDiskAssitService) {
		this.billCancelDiskAssitService = billCancelDiskAssitService;
	}

	public void setPageTaxInvoiceDiskAssitService(PageTaxInvoiceDiskAssitService pageTaxInvoiceDiskAssitService) {
		this.pageTaxInvoiceDiskAssitService = pageTaxInvoiceDiskAssitService;
	}

	

	
	
	

}
