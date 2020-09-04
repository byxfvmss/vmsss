package com.cjit.vms.taxdisk.single.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.json.JsonUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.BillPrint;
import com.cjit.vms.taxdisk.single.model.parseXml.BillPrintReturnXML;
import com.cjit.vms.taxdisk.single.service.BillPrintDiskAssistService;
import com.cjit.vms.taxdisk.single.service.BillPrintDiskService;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import  com.cjit.vms.taxdisk.single.service.util.Message;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;

public class BillPrintDiskServiceImpl extends GenericServiceImpl implements BillPrintDiskService {
	
	private TaxDiskInfoQueryService  taxDiskInfoQueryService;
	private BillPrintDiskAssistService  billPrintDiskAssistService;
	
	@Override
	public String createBillPrintXml(String diskNo, String billId) throws Exception {
		TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo(diskNo);
		BillInfo  billInfo  =billPrintDiskAssistService.findBillInfo(billId);
		Message message = new Message();
		String StringXml=null;
		try {
			BillPrint billPrint = new BillPrint(disk.getTaxpayerNo(), disk.getTaxDiskNo(), disk.getTaxDiskPsw(),
					disk.getTaxCertPsw(), billInfo.getFapiaoType(), billInfo.getBillCode(), billInfo.getBillNo(),
					TaxDiskUtil.print_Type_0 ,TaxDiskUtil.print_Way_1);
			
			billPrint.setApplyTypeCode(TaxDiskUtil.Application_type_code_1);
			billPrint.setId(TaxDiskUtil.id_bill_print);
			billPrint.setComment(TaxDiskUtil.comment_bill_print);
			
			StringXml = billPrint.createBillPrintXml();
			message.setReturnCode(Message.success);
			message.setStringXml(StringXml);
		} catch (Exception e) {
			message.setReturnCode(Message.error);
			message.setReturnMsg(Message.system_exception_bill_print_Xml_error);
			return JsonUtil.toJsonString(message);
		}
		return JsonUtil.toJsonString(message);
	}

	@Override
	public AjaxReturn updateBillPrintResult(String StringXml, String billId) throws Exception {
		AjaxReturn message = null;
		BillPrintReturnXML billPrint=null;
		try {
			billPrint = new BillPrintReturnXML(StringXml);
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_print_Xml_error);
			return message;
		}
		return billPrintDiskAssistService.
		updateBillPrintResult(billId, billPrint.getReturnMsg(), billPrint.getReturnCode().equals(TaxDiskUtil.return_success));
	}

	
	public TaxDiskInfoQueryService getTaxDiskInfoQueryService() {
		return taxDiskInfoQueryService;
	}

	public void setTaxDiskInfoQueryService(
			TaxDiskInfoQueryService taxDiskInfoQueryService) {
		this.taxDiskInfoQueryService = taxDiskInfoQueryService;
	}

	public BillPrintDiskAssistService getBillPrintDiskAssistService() {
		return billPrintDiskAssistService;
	}

	public void setBillPrintDiskAssistService(
			BillPrintDiskAssistService billPrintDiskAssistService) {
		this.billPrintDiskAssistService = billPrintDiskAssistService;
	}
	
}
