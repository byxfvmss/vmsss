package com.cjit.vms.taxdisk.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.service.BillInterfaceService;
import com.cjit.vms.taxdisk.servlet.model.BillCancel;
import com.cjit.vms.taxdisk.servlet.model.BillIssue;
import com.cjit.vms.taxdisk.servlet.model.BillPrint;
import com.cjit.vms.taxdisk.servlet.model.CurrentBillNo;
import com.cjit.vms.taxdisk.servlet.model.ParamSet;
import com.cjit.vms.taxdisk.servlet.model.Product;
import com.cjit.vms.taxdisk.servlet.model.TaxInformationQuery;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BaseReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BillCancelReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BillIssueReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BillPrintReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.CurrentBillNoReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.ParamSetReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.TaxKeyQueryReturnXml;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;
import com.cjit.vms.taxdisk.single.service.BillCancelDiskAssitService;
import com.cjit.vms.taxdisk.single.service.BillIssueDiskAssitService;
import com.cjit.vms.taxdisk.single.service.BillPrintDiskAssistService;
import com.cjit.vms.taxdisk.single.service.PageTaxInvoiceDiskAssitService;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.taxdisk.tools.Message;
import com.cjit.vms.trans.model.UBaseInst;
import org.apache.log4j.Logger;
public class BWServletBillInterfaceServiceImpl extends GenericServiceImpl implements BillInterfaceService {
	private BillIssueDiskAssitService billIssueDiskAssitService;
	private TaxDiskInfoQueryService taxDiskInfoQueryService;
	private BillPrintDiskAssistService billPrintDiskAssistService;
	private BillCancelDiskAssitService billCancelDiskAssitService;
	private PageTaxInvoiceDiskAssitService pageTaxInvoiceDiskAssitService;
	Logger log = Logger.getLogger(BWServletBillInterfaceServiceImpl.class);
	/**
	 * aa
	 */
	public static final String INTERFACE_TYPE = "bw_servlet";
	
	/* (non-Javadoc)
	 * @ 创建参数设置 信息
	 */
	@Override
	public AjaxReturn createRegistInfo(Map params) throws Exception {

		AjaxReturn message=null;
		try {
			
			Map map=new HashMap();
			 List list=	find("findTaxDiskapwd",map);
			 String pwd="";
			 if(list.size()==1){
				 pwd=(String) list.get(0);
				 VmsTaxKeyInfo taxKeyInfo=taxDiskInfoQueryService.findVmstaxKeyInfo((String)params.get("diskNo"));
					 
				String StringXml= new ParamSet(pwd).createParamSetXml(taxKeyInfo);
				System.out.println(StringXml);
				log.info("参数设置Xml"+StringXml);
				Map mapm=new HashMap();
				message=new AjaxReturn(true);
				mapm.put("StringXml", StringXml);
				message.setAttributes(mapm);
			 }else{
				 message=new AjaxReturn(false, Message.pwd_no);
				 return message;
			 }
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_bill_param_Xml_error);
		}
		return message;
	}
	
	
	/* (non-Javadoc)
	 * @验证参数设置
	 */
	@Override
	public AjaxReturn checkRegistInfo(Map params) throws Exception {
		AjaxReturn message=null;
		try {
			log.info("参数设置返回"+(String)params.get("StringXml"));
			ParamSetReturnXml parammXml=new ParamSetReturnXml((String)params.get("StringXml"));
			System.out.println((String)params.get("StringXml"));
			if(parammXml.getReturncode().equals(BaseReturnXml.return_success)){
				message=new AjaxReturn(true);
			}else{
				message=new AjaxReturn(false, parammXml.getReturnmsg());
			}
			 
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_bill_param_set_xml_error);
		}
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
			 }else{
				 message=new AjaxReturn(false, Message.pwd_no);
				 return message;
			 }
			 TaxInformationQuery taxInfo=new TaxInformationQuery(pwd);
			 String StringXml=taxInfo.createTaxKeyQueryXml();
			 System.out.println(StringXml);
			log.info("税控服务器查询Xml"+StringXml);
			 map.put("StringXml", StringXml);
			 message=new AjaxReturn(true);
			 message.setAttributes(map);
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_tax_key_Xml_error);
			return message;
			
		}
		return message;
	}

	@Override
	public AjaxReturn checkTaxInfo(Map params) throws Exception {
		AjaxReturn message=null;
		try {
			try {
				//UBaseInst inst = taxDiskInfoQueryService.findTaxDiskInfoByinstID((String)params.get("instCode"));
				//if (inst.getTaxperNumber().isEmpty()) {
				//	message=new AjaxReturn(false, Message.blank_inst_tax_no);
					//return message;
			///	}
				log.info("税控信息查询返回xml"+(String)params.get("StringXml"));
				TaxKeyQueryReturnXml taxDiskInfoQRXml = new TaxKeyQueryReturnXml(
						(String)params.get("StringXml"));
				System.out.println((String)params.get("StringXml"));
				
				if (taxDiskInfoQRXml.getReturncode().equals(BaseReturnXml.return_success)) {
					message=new AjaxReturn(true);
					Map map=new HashMap();
					map.put("diskNo", taxDiskInfoQRXml.getTaxNo());
					message.setAttributes(map);
					
					
					///if (!taxDiskInfoQRXml.getTaxNo().equals(inst.getTaxperNumber())) {
						//message=new AjaxReturn(false, Message.tax_no_and_inst_tax_no_not);
						//return	message;
					//}
				}else{
					message=new AjaxReturn(false, taxDiskInfoQRXml.getReturnmsg());
					
					return	message;
				}
				
			} catch (Exception e) {
				message=new AjaxReturn(false, Message.parse_bill_tax_key_xml_error);
				return	message; 
			}
			
		} catch (Exception e) {

		}
		
		return message;
	}
	@Override
	public AjaxReturn createCurBillNoInfo(Map params) throws Exception {
		AjaxReturn message=null;
		Map map=new HashMap();
		try {
			VmsTaxKeyInfo taxKeyInfo=taxDiskInfoQueryService.findVmstaxKeyInfo((String)params.get("diskNo"));

			
			CurrentBillNo bill=new CurrentBillNo((String)params.get("fapiaoType"),taxKeyInfo);
			String StringXml=bill.createCurrentBillXml();
			System.out.println(StringXml);
			log.info("查询当前发票号码Xml"+StringXml);
			map.put("StringXml", StringXml);
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.system_exception_bill_cur_bill_no_Xml_error);
			return message;
		}
		message=new AjaxReturn(true);
		message.setAttributes(map);
		return message;
	}
	@Override
	public AjaxReturn checkCurBillNoInfo(Map params) throws Exception {
		AjaxReturn message=null;

		try {
			log.info("当前发票号码返回xml"+(String)params.get("StringXml"));
			System.out.println((String)params.get("StringXml"));
			CurrentBillNoReturnXml bill=new CurrentBillNoReturnXml((String)params.get("StringXml"));
			if(bill.getReturncode().equals(BaseReturnXml.return_success)){
				message=new AjaxReturn(true);
			}else{
				message=new AjaxReturn(false, bill.getReturnmsg());
			}
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_bill_cancel_Xcur_bill_no_xml_error);
		}
		
	
		return message;
	}
	@Override
	public AjaxReturn createBillissue(Map params) throws Exception {
		AjaxReturn message=null;
		BillIssue bill=null;
		try {
			String billId=(String)params.get("billId");
			BillInfo billInfo=billIssueDiskAssitService.findBillInfoById(billId);
			List<BillItemInfo> list=billIssueDiskAssitService.findBillItemByBillIdDisk(billId);
			List<Product> goodsList=new ArrayList<Product>();
			if(list.size()>0){
				for (int i=0;i<list.size();i++){
					BillItemInfo billItem=list.get(i);
					Product goods=new Product(billItem);
					goodsList.add(goods);
				}
			}
			//税控钥匙新加
			 VmsTaxKeyInfo taxKeyInfo=taxDiskInfoQueryService.findVmstaxKeyInfo((String)params.get("diskNo"));
			
			bill=new BillIssue(billInfo,goodsList,TaxSelvetUtil.Issue_Bill_Type_0,"",taxKeyInfo);
			log.info("开具xml"+bill.createBillIssueXml());
			System.out.println(bill.createBillIssueXml());
					message=new AjaxReturn(true); 
					Map map=new HashMap();
					map.put("StringXml",bill.createBillIssueXml());
					message.setAttributes(map);
		} catch (Exception e) {
		}
		return message;
	}
	@Override
	public AjaxReturn updateBillIssueResult(Map params) throws Exception {
		AjaxReturn message=null;
		BillIssueReturnXml bill=null;
		try {
			System.out.println((String)params.get("StringXml"));
			log.info("开具xml返回"+(String)params.get("StringXml"));
			 bill=new BillIssueReturnXml((String)params.get("StringXml"));
			
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_bill_Issue_info_query_erroe);
			return message;
		}
		String falg=bill.getId();
		//
		if (falg.equals(TaxSelvetUtil.id_Issue)) {
					message=billIssueDiskAssitService.updateBillIssueResult(bill.getBillCode(),bill.getBillNo(), 
					(String)params.get("billId"), (String)params.get("diskNo"),(String)params.get("diskNo"),
					(String)params.get("userId"), bill.getBillIssueDate(),bill.getReturnmsg(),bill.getReturncode().equals(BaseReturnXml.return_success));
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
			
			BillInfo  billInfo  =billIssueDiskAssitService.findBillInfoById((String)params.get("billId"));
			VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo((String)params.get("diskNo"));
			BillPrint billPrint =
			new BillPrint(billInfo,taxKeyInfo);
			StringXml = billPrint.createPrintBillXml("发票打印");
			System.out.println(StringXml);
			log.info("发票打印xml"+StringXml);
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
		BillPrintReturnXml billPrint=null;
		try {
			System.out.println((String)params.get("StringXml"));
			log.info("打印信息返回"+(String)params.get("StringXml"));
			billPrint = new BillPrintReturnXml((String)params.get("StringXml"));
			
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.parse_print_Xml_error);
			
			return message;
		}
		return billPrintDiskAssistService.
		updateBillPrintResult((String)params.get("billId"),  billPrint.getReturnmsg(),billPrint.getReturncode().equals(BaseReturnXml.return_success));
	
	}

	@Override
	public AjaxReturn createBillCancel(Map params) throws Exception {
		AjaxReturn message=null;
		try {
			
			BillInfo bill =billIssueDiskAssitService.findBillInfoById((String)params.get("billId"));
			VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo((String)params.get("diskNo"));
			BillCancel billCancel =new BillCancel(bill, bill.getFapiaoType(), (String)params.get("userId"), TaxSelvetUtil.bill_cancel_Typech, taxKeyInfo);
			message=new AjaxReturn(true);
			String StringXml=billCancel.createBillCancelXml((String)params.get("flag"));
			System.out.println(StringXml);
			log.info("发票作废Xml"+StringXml);
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
		BillCancelReturnXml billcancel=null;
		try {
			System.out.println((String)params.get("StringXml"));
			log.info("发票作废返回"+(String)params.get("StringXml"));
			 billcancel=new BillCancelReturnXml((String)params.get("StringXml"));	
		} catch (Exception e) {
			message=new AjaxReturn(false,Message.parse_bill_cancel_Xml_error);
			return message;
		}
		return billCancelDiskAssitService.updateBillCancelResult((String)params.get("billId"),billcancel.getReturnmsg() ,billcancel.getReturncode().equals(BaseReturnXml.return_success));

	}
	


	@Override
	public AjaxReturn createStockInfo(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	/* (non-Javadoc)
	 * @ 税控信息基本查询
	 */
	@Override
	public AjaxReturn createStockIssue(Map params) throws Exception {
		AjaxReturn message=null;
		try {
			
		} catch (Exception e) {

		
		}
		return message;
	}

	@Override
	public AjaxReturn createStockRecover(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}


	@Override
	public AjaxReturn createTaxItemInfo(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn createTaxMonitor(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn saveStockInfo(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn saveStockIssue(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn saveStockRecover(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn saveTaxInfo(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn saveTaxItemInfo(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}

	@Override
	public AjaxReturn saveTaxMonitor(Map params) throws Exception {
		AjaxReturn message=null;
		return message;
	}








	public BillIssueDiskAssitService getBillIssueDiskAssitService() {
		return billIssueDiskAssitService;
	}

	public TaxDiskInfoQueryService getTaxDiskInfoQueryService() {
		return taxDiskInfoQueryService;
	}

	public BillPrintDiskAssistService getBillPrintDiskAssistService() {
		return billPrintDiskAssistService;
	}

	public BillCancelDiskAssitService getBillCancelDiskAssitService() {
		return billCancelDiskAssitService;
	}

	public PageTaxInvoiceDiskAssitService getPageTaxInvoiceDiskAssitService() {
		return pageTaxInvoiceDiskAssitService;
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



}
