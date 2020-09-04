package com.cjit.vms.taxdisk.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.service.BillInterfaceService;
import com.cjit.vms.taxdisk.single.model.BillCancel;
import com.cjit.vms.taxdisk.single.model.BillPrint;
import com.cjit.vms.taxdisk.single.model.BuyBillInfoQuery;
import com.cjit.vms.taxdisk.single.model.ImportRegCodeInfo;
import com.cjit.vms.taxdisk.single.model.MonitorInfoQuery;
import com.cjit.vms.taxdisk.single.model.TaxDiskInfoQuery;
import com.cjit.vms.taxdisk.single.model.TaxItemInfoQuery;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.parseXml.BillCancelReturnXMl;
import com.cjit.vms.taxdisk.single.model.parseXml.BillIssueReturnXML;
import com.cjit.vms.taxdisk.single.model.parseXml.BillPrintReturnXML;
import com.cjit.vms.taxdisk.single.model.parseXml.ImportRegCodeInfoReturnXml;
import com.cjit.vms.taxdisk.single.model.parseXml.MonitorInfoQueryReturnXml;
import com.cjit.vms.taxdisk.single.model.parseXml.TaxDiskInfoQueryReturnXml;
import com.cjit.vms.taxdisk.single.model.parseXml.TaxItemInfoQueryReturnXml;
import com.cjit.vms.taxdisk.single.service.BillCancelDiskAssitService;
import com.cjit.vms.taxdisk.single.service.BillIssueDiskAssitService;
import com.cjit.vms.taxdisk.single.service.BillPrintDiskAssistService;
import com.cjit.vms.taxdisk.single.service.PageTaxInvoiceDiskAssitService;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import com.cjit.vms.taxdisk.single.util.DiskParseBusiUtil;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.taxdisk.tools.Message;
import com.cjit.vms.trans.model.UBaseInst;

public class BWDiskBillInterfaceServiceImpl extends GenericServiceImpl implements BillInterfaceService {

	public static final String INTERFACE_TYPE = "bw_disk";
	
	private BillIssueDiskAssitService billIssueDiskAssitService;
	private TaxDiskInfoQueryService taxDiskInfoQueryService;
	private BillPrintDiskAssistService billPrintDiskAssistService;
	private BillCancelDiskAssitService billCancelDiskAssitService;
	private PageTaxInvoiceDiskAssitService pageTaxInvoiceDiskAssitService;
	
	
	@Override
	public AjaxReturn createBillissue(Map params) throws Exception {
		
	AjaxReturn message=null;
		//BuyBillInfoQueryReturnXML bill=new BuyBillInfoQueryReturnXML((String)params.get("StringXml"));
		 //String fapiaoType=bill.getFapiaoType()=="004"?"0":"1";
		//String billCode=billIssueDiskAssitService.findBillDiskByNoAndTypeForBlankCancel(bill.getCurBillCode(), bill.getCurBillNo(), fapiaoType);
		TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo((String)params.get("diskNo"));
		if(disk==null){
			message=new AjaxReturn(false,Message.no_disk_info);
			return message;
		}
		/*
		if(billCode.isEmpty()){*/
		 message=	billIssueDiskAssitService.createBillIssueXml(disk, (String)params.get("billId"),(String)params.get("MachineNo"));
		
		/*}else{
			billIssueDiskAssitService.createBillBalanlCancel(disk, bill.getCurBillCode(), bill.getCurBillNo(), fapiaoType, (String)params.get("userId"));
		}*/
		return message;
		
	}

	@Override
	public AjaxReturn updateBillIssueResult(Map params) throws Exception {
		AjaxReturn message=null;
		BillIssueReturnXML bill=null;
		try {
			 bill=new BillIssueReturnXML((String)params.get("StringXml"));
			
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_bill_Issue_info_query_erroe);
			return message;
		}
		String falg=bill.getId();
		if (falg.equals(TaxDiskUtil.id_bill_issue)) {
					message=billIssueDiskAssitService.updateBillIssueResult(bill.getBillCode(),bill.getBillNo(), 
					(String)params.get("billId"), (String)params.get("diskNo"),(String)params.get("MachineNo"),
					(String)params.get("userId"), bill.getIssueDate(),bill.getReturnmsg(),bill.getReturnCode().equals(TaxDiskUtil.return_success)||bill.getReturnCode().equals(TaxDiskUtil.return_reg_info_success));
		} else {
			message=new AjaxReturn(true);
		}

		return message;
	}

	@Override
	public AjaxReturn createBillPrint(Map params) throws Exception {
		AjaxReturn message=null;
		String StringXml=null;
		try {
			TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo((String)params.get("diskNo"));
			BillInfo  billInfo  =billIssueDiskAssitService.findBillInfoById((String)params.get("billId"));
			BillPrint billPrint =
			new BillPrint(billInfo, disk, TaxDiskUtil.print_Way_1);
			StringXml = billPrint.createBillPrintXml();
			message=new AjaxReturn(true);
			Map map=new HashMap();
			map.put("StringXml", StringXml);
			message.setAttributes(map);
		} catch (Exception e) {
			message=new AjaxReturn(false,Message.system_exception_bill_print_Xml_error);
			return message;
		}
		return message;
	}

	@Override
	public AjaxReturn updateBillPrintResult(Map params) throws Exception {
		AjaxReturn message=null;
		BillPrintReturnXML billPrint=null;
		try {
			billPrint = new BillPrintReturnXML((String)params.get("StringXml"));
			
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_print_Xml_error);
			
			return message;
		}
		return billPrintDiskAssistService.
		updateBillPrintResult((String)params.get("billId"), billPrint.getReturnMsg(),
				billPrint.getReturnCode().equals(TaxDiskUtil.return_TaxDisk_info_success)||billPrint.getReturnCode().equals(TaxDiskUtil.return_success));
		
		
	}

	@Override
	public AjaxReturn createBillCancel(Map params) throws Exception {
		AjaxReturn message=null;
		
		try {
			BillInfo bill =billIssueDiskAssitService.findBillInfoById((String)params.get("billId"));;
			TaxDiskInfo disk = taxDiskInfoQueryService.findTaxDiskInfo((String)params.get("diskNo"));
			BillCancel billCancel =new BillCancel(bill, disk,(String)params.get("userId"), TaxDiskUtil.cancel_Type_1);
			message=new AjaxReturn(true);
			String StringXml=billCancel.createBillCancelXMl();
			Map map=new HashMap();
			map.put("StringXml", StringXml);
			message.setAttributes(map);
			
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_bill_cancel_Xml_error);
			return message;
		}
			return message;
	}

	@Override
	public AjaxReturn updateBillCancelResult(Map params) throws Exception {
		AjaxReturn message=null;
		BillCancelReturnXMl billcancel=null;
		try {
			 billcancel=new BillCancelReturnXMl((String)params.get("StringXml"));	
		} catch (Exception e) {
			message=new AjaxReturn(false,Message.parse_bill_cancel_Xml_error);
			return message;
		}
		return billCancelDiskAssitService.updateBillCancelResult((String)params.get("billId"),billcancel.getReturnmsg(),
				billcancel.getReturnCode().equals(TaxDiskUtil.return_TaxDisk_info_success)||billcancel.getReturnCode().equals(TaxDiskUtil.return_success) );
	}

	@Override
	public AjaxReturn createTaxItemInfo(Map params) throws Exception {
		AjaxReturn message=null; 
		try {
			TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo((String)params.get("diskNo"));
			TaxItemInfoQuery taxItem=new TaxItemInfoQuery(disk, (String)params.get("fapiaoType0"));
			TaxItemInfoQuery taxItem1=new TaxItemInfoQuery(disk, (String)params.get("fapiaoType1"));
			String StringXml= taxItem.createTaxItemInfoQueryXml();
			String StringXmlTwo= taxItem1.createTaxItemInfoQueryXml();
			Map map=new HashMap();
			map.put("StringXml", StringXml);
			map.put("StringXmlTwo", StringXmlTwo);
			message=new AjaxReturn(true,Message.tax_Item_save_success);
			message.setAttributes(map);
		} catch (Exception e) {
			message=new AjaxReturn(true, Message.system_exception_tax_Xml_error);
			return message;
		}
		
		return message;
	}

	@Override
	public AjaxReturn saveTaxItemInfo(Map params) throws Exception {
		AjaxReturn message=null;
		List list=new ArrayList();
		try {
			list.addAll(DiskParseBusiUtil.getVmsTaxInfo(new TaxItemInfoQueryReturnXml((String)params.get("Stringxml0")),(String)params.get("taxNo")));;
			list.addAll(DiskParseBusiUtil.getVmsTaxInfo(new TaxItemInfoQueryReturnXml((String)params.get("Stringxml1")),(String)params.get("taxNo")));;
		} catch (Exception e) {
			message=new AjaxReturn(false,Message.parse_tax_Item_info_query_erroe);
			return message;
		}
		return pageTaxInvoiceDiskAssitService.saveAndUpdateTaxInfo(list);
	}

	@Override
	public AjaxReturn createTaxMonitor(Map params) throws Exception {
		AjaxReturn message=null;
		try {
			TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo((String)params.get("diskNo"));
			MonitorInfoQuery mon=new MonitorInfoQuery(disk, (String)params.get("fapiaoType0"));
			MonitorInfoQuery mon1=new MonitorInfoQuery(disk, (String)params.get("fapiaoType1"));
			String StringXml=mon.createMonitorInfoQueryXml();
			String StringXmlTwo=mon1.createMonitorInfoQueryXml();
			message=new AjaxReturn(true);
			Map map=new HashMap();
			map.put("StringXml", StringXml);
			map.put("StringXmlTwo", StringXmlTwo);
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_buy_bill_Xml_error);
			return message;
		}
		return message;
	}

	@Override
	public AjaxReturn saveTaxMonitor(Map params) throws Exception {
		AjaxReturn message=null;
		List list=new ArrayList();
		try {
			list.add(DiskParseBusiUtil.getTaxDiskMonitorInfo(new MonitorInfoQueryReturnXml((String)params.get("StringXml0")),(String)params.get("instCode"),(String)params.get("diskNo")));
			list.add(DiskParseBusiUtil.getTaxDiskMonitorInfo(new MonitorInfoQueryReturnXml((String)params.get("StringXml1")),(String)params.get("instCode"),(String)params.get("diskNo")));
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_disk_mon_info_erroe);
			return message;
		}
		
		return pageTaxInvoiceDiskAssitService.saveTaxDiskMonInfoQuery(list);
	}

	@Override
	public AjaxReturn createStockInfo(Map params) throws Exception {
		AjaxReturn message=null;
		try {
			TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo((String)params.get("diskNo"));
			BuyBillInfoQuery buyBill=new BuyBillInfoQuery(disk, getFapiaoTypeInteger((String)params.get("fapiaoType0")));
			Map map=new HashMap();
			if(StringUtil.isNotEmpty((String)params.get("fapiaoType1"))){
				BuyBillInfoQuery buyBill1=new BuyBillInfoQuery(disk, getFapiaoTypeInteger((String)params.get("fapiaoType1")));
				String StringXmlTwo=buyBill1.createBuyBillInfoQueryXml();
				map.put("StringXmlTwo", StringXmlTwo);
			}
			String StringXml=buyBill.createBuyBillInfoQueryXml();
			message=new AjaxReturn(true);
			map.put("StringXml", StringXml);
			message.setAttributes(map);
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_buy_bill_Xml_error);
		}
		return message;
	}

	@Override
	public AjaxReturn saveStockInfo(Map params) throws Exception {
		AjaxReturn message=null;
		
		return message;
	}

	@Override
	public AjaxReturn createStockIssue(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn saveStockIssue(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn createStockRecover(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn saveStockRecover(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn createTaxInfo(Map params) throws Exception {
		AjaxReturn message=null;
		
		try {
			Map map=new HashMap();
			 List list=	find("findTaxDiskapwd",map);
			 String pwd="";
			 if(list.size()==1){
				 pwd=(String) list.get(0);
			 }
			 TaxDiskInfoQuery taxInfoQuery=new TaxDiskInfoQuery();
			 taxInfoQuery.setId(TaxDiskUtil.id_tax_disk_information_query);
			 taxInfoQuery.setComment(TaxDiskUtil.comment_tax_disk_information_query);
			 taxInfoQuery.setApplyTypeCode(TaxDiskUtil.Application_type_code_1);
			 taxInfoQuery.setTaxDiskPwd(pwd);
			 String StringXml=taxInfoQuery.createTaxDiskInfoQueryXml();
			 message=new AjaxReturn(true);
			 Map mappa=new HashMap();
			 mappa.put("StringXml", StringXml);
			 System.out.println(StringXml);
			 message.setAttributes(mappa);
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_disk_Xml_error);
			return message;
		}
	     
		return message;
	}

	@Override
	public AjaxReturn checkTaxInfo(Map params) throws Exception {
		AjaxReturn message=null;
		String diskNo=null;
		String MachineNo=null;
		try {
			UBaseInst inst = taxDiskInfoQueryService.findTaxDiskInfoByinstID((String)params.get("instCode"));
			if (inst.getTaxperNumber().isEmpty()) {
				message=new AjaxReturn(false, Message.blank_inst_tax_no);
				return message;
			}
			TaxDiskInfoQueryReturnXml taxDiskInfoQRXml = new TaxDiskInfoQueryReturnXml(
					(String)params.get("StringXml"));
			if (taxDiskInfoQRXml.getReturnCode().equals(TaxDiskUtil.return_TaxDisk_info_success)||taxDiskInfoQRXml.getReturnCode().equals(TaxDiskUtil.return_success)) {
				diskNo=taxDiskInfoQRXml.getTaxDiskNo();
				MachineNo=taxDiskInfoQRXml.getIssueNo();
				if(!inst.getTaxperName().equals(taxDiskInfoQRXml.getName())){
					message=new AjaxReturn(false, Message.inst_name_Not_name);
					
					return	message; 
				}
				if (!taxDiskInfoQRXml.getTaxNo().equals(inst.getTaxperNumber())) {
					message=new AjaxReturn(false, Message.tax_no_and_inst_tax_no_not);
					
					return	message;
				}
			}else{
				message=new AjaxReturn(false, taxDiskInfoQRXml.getReturnMsg());
				
				return	message;
			}
			
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception);
			
			return	message; 
		
		}
		message=new AjaxReturn(true);
		Map map=new HashMap();
		map.put("diskNo", diskNo);
		map.put("MachineNo", MachineNo);
		message.setAttributes(map);
		return	message;
	}

	@Override
	public AjaxReturn saveTaxInfo(Map params) throws Exception {
		AjaxReturn message=null;
		try{
			TaxDiskInfoQueryReturnXml tax=new TaxDiskInfoQueryReturnXml((String)params.get("StringXml"));
			pageTaxInvoiceDiskAssitService.updateTaxDiskInfo(tax);
			message=new AjaxReturn(true, Message.tax_disk_info_save_success);
			}catch (Exception e) {
			message=new AjaxReturn(false, Message.tax_disk_info_save_success);
			}
		return message;
	}
	public String getFapiaoTypeInteger(String fapiaoType){
		fapiaoType=fapiaoType.equals("0")?"004":"007";
		return fapiaoType;
		 
	}
	
	public String getFapiaoType(String fapiaoType){
		fapiaoType=fapiaoType.equals("004")?"0":"1";
		return fapiaoType;
	}

	@Override
	public AjaxReturn createRegistInfo(Map params) throws Exception {
		AjaxReturn message=null;
		String reg=taxDiskInfoQueryService.findTaxDiskRegInfo((String)params.get("diskNo"));
		if(StringUtil.IsEmptyStr(reg)){
			message=new AjaxReturn(false, Message.no_reg_info);
			return message;
		}
		String StringXml=new ImportRegCodeInfo(reg).createImportRegCodeInfoXml();
		Map map=new HashMap();
		map.put("StringXml", StringXml);
		message=new AjaxReturn(true);
		message.setAttributes(map);
		return message;
	}

	@Override
	public AjaxReturn checkRegistInfo(Map params) throws Exception {
		AjaxReturn message=null;
		ImportRegCodeInfoReturnXml xml=new ImportRegCodeInfoReturnXml((String)params.get("StringXml"));
		if(!xml.getReturnCode().equals(TaxDiskUtil.return_reg_info_success)&&!xml.getReturnCode().equals(TaxDiskUtil.return_success)){
			message=new AjaxReturn(false,xml.getReturnMsg());
			return message;
		}
		message=new AjaxReturn(true);
		return message;
	}

	public void setBillIssueDiskAssitService(
			BillIssueDiskAssitService billIssueDiskAssitService) {
		this.billIssueDiskAssitService = billIssueDiskAssitService;
	}

	public void setTaxDiskInfoQueryService(
			TaxDiskInfoQueryService taxDiskInfoQueryService) {
		this.taxDiskInfoQueryService = taxDiskInfoQueryService;
	}

	public void setBillPrintDiskAssistService(
			BillPrintDiskAssistService billPrintDiskAssistService) {
		this.billPrintDiskAssistService = billPrintDiskAssistService;
	}

	public void setBillCancelDiskAssitService(
			BillCancelDiskAssitService billCancelDiskAssitService) {
		this.billCancelDiskAssitService = billCancelDiskAssitService;
	}

	public void setPageTaxInvoiceDiskAssitService(
			PageTaxInvoiceDiskAssitService pageTaxInvoiceDiskAssitService) {
		this.pageTaxInvoiceDiskAssitService = pageTaxInvoiceDiskAssitService;
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

	
}
