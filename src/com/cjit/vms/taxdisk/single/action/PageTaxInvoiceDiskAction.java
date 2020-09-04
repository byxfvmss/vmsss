package com.cjit.vms.taxdisk.single.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;
import cjit.crms.util.json.JsonUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.model.parseXml.MonitorInfoQueryReturnXml;
import com.cjit.vms.taxdisk.single.model.parseXml.TaxDiskInfoQueryReturnXml;
import com.cjit.vms.taxdisk.single.model.parseXml.TaxItemInfoQueryReturnXml;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;
import com.cjit.vms.taxdisk.single.service.PageTaxInvoiceDiskAssitService;
import com.cjit.vms.taxdisk.single.service.util.Message;
import com.cjit.vms.taxdisk.single.util.DiskParseBusiUtil;
/**
 * @author tom  同步稅控盘
 *
 */
public class PageTaxInvoiceDiskAction extends BaseDiskAction {
	private String taxParam;//税控参数
	private List lstAuthInstId;
	private Map mapVatType;
	private String flag;//提交按钮区分
	private PaperInvoiceListInfo paperListInfo = new PaperInvoiceListInfo();
	private ParamConfigVmssService paramConfigVmssService;
	private VmsCommonService vmsCommonService;
	private PageTaxInvoiceService pageTaxInvoiceService;
	private BillIssueService billIssueService;
	private PageTaxInvoiceDiskAssitService pageTaxInvoiceDiskService;
	public String listPageTaxInvoiceDisk(){
		
		if (!sessionInit(true)) {
			request.getSession().setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			if (StringUtil.isNotEmpty(flag)) {
				
				paginationList.setCurrentPage(1);
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			paperListInfo.setLstAuthInstId(lstAuthInstId);
			paperListInfo.setUserID(this.getCurrentUser().getId());
			// 一览数据检索
			List paperInfoList = pageTaxInvoiceService.findPageTaxInvoice(paperListInfo, paginationList);
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");
			this.request.getSession().setAttribute("paperInfoList", paperInfoList);
			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.getSession().setAttribute("currMonth", currMonth);
			if(taxParam.equals(TaxSelvetUtil.tax_Server_ch)){
				return "tax";
			}else{
				return "disk";
			}
			} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}
	
	public void saveTaxDiskInfoForTwo(){
		Message mess=new Message();
		String StringXml=request.getParameter("StringXmml");
		try{
		TaxDiskInfoQueryReturnXml tax=new TaxDiskInfoQueryReturnXml(StringXml);
		pageTaxInvoiceDiskService.updateTaxDiskInfo(tax);
		mess.setReturnMsg(Message.tax_disk_info_save_success);
		mess.setReturnCode(Message.success);
		}catch (Exception e) {
			mess.setReturnMsg(Message.tax_disk_info_save_success);
			mess.setReturnCode(Message.error);
		}
	}
	public void createMonDataQueryXml() throws Exception{
		String taxDiskNo=request.getParameter("taxDiskNo");
		String fapiaoType0=request.getParameter("fapiaoType0");
		String fapiaoType1=request.getParameter("fapiaoType1");
		Message messsge=new Message();
		try {
			String StringXml0=pageTaxInvoiceDiskService.createMonDataQuery(taxDiskNo, getFapiaoTypeCh(fapiaoType0));
			String StringXml1=pageTaxInvoiceDiskService.createMonDataQuery(taxDiskNo, getFapiaoTypeCh(fapiaoType1));
			messsge.setStringXml(StringXml0);	
			messsge.setStringXmlTwo(StringXml1);
			messsge.setReturnCode(Message.success);
			printWriterResult(JsonUtil.toJsonString(messsge));
		} catch (Exception e) {
				messsge.setReturnMsg(Message.system_exception_Xml_error);
				messsge.setReturnCode(Message.error);
				printWriterResult(JsonUtil.toJsonString(messsge));

		}
		
		
		
	}
	/**
	 * @throws Exception 保存监控信息表
	 */
	public void saveTaxDiskMonDataInfo() throws Exception{
		String StringXml0=request.getParameter("StringXml0");
		String StringXml1=request.getParameter("StringXml1");
		String diskNo=request.getParameter("diskNo");
		String instCode=this.getCurrentUser().getOrgId();
		Message message=new Message();
		List list=new ArrayList();
		try {
			list.add(DiskParseBusiUtil.getTaxDiskMonitorInfo(new MonitorInfoQueryReturnXml(StringXml0),instCode,diskNo));
			list.add(DiskParseBusiUtil.getTaxDiskMonitorInfo(new MonitorInfoQueryReturnXml(StringXml1),instCode,diskNo));
		} catch (Exception e) {
			message.setReturnCode(Message.error);
			message.setReturnMsg(Message.parse_disk_mon_info_erroe);
			printWriterResult(JsonUtil.toJsonString(message));
		}
		try {
			pageTaxInvoiceDiskService.saveTaxDiskMonInfoQuery(list);
			message.setReturnMsg(Message.tax_disk_mon_info_save_success);
			message.setReturnCode(Message.success);
		} catch (Exception e) {
			message.setReturnCode(Message.error);
			message.setReturnMsg(Message.tax_disk_mon_info_save_erroe);
			printWriterResult(JsonUtil.toJsonString(message));
		}
		printWriterResult(JsonUtil.toJsonString(message));

	}
	/**
	 * @throws Exception 创建税种稅税目信息xml
	 */
	public void createTaxDiskItemXml() throws Exception{
		String diskNo=request.getParameter("diskNo");
		String fapiaoType0=request.getParameter("fapiaoType0");
		String fapiaoType1=request.getParameter("fapiaoType1");
		Message message=new Message();
		try {
				message.setStringXml(pageTaxInvoiceDiskService.CreateTaxItemXml(diskNo, fapiaoType0));
				message.setStringXmlTwo(pageTaxInvoiceDiskService.CreateTaxItemXml(diskNo, fapiaoType1));
				message.setReturnCode(Message.success);
		} catch (Exception e) {
			message.setReturnCode(Message.error);
			message.setReturnMsg(Message.system_exception_tax_Xml_error);
			printWriterResult(JsonUtil.toJsonString(message));
		}
		printWriterResult(JsonUtil.toJsonString(message));
	}
	/**
	 *  保存税种税目信息
	 * @throws Exception 
	 */
	public void saveTaxItemInfoDisk() throws Exception{
		String Stringxml0=request.getParameter("Stringxml0");
		String Stringxml1=request.getParameter("Stringxml1");
		String taxNo=request.getParameter("taxNo");
		List list=new ArrayList();
		Message message=new Message();
		try {
			list.addAll(DiskParseBusiUtil.getVmsTaxInfo(new TaxItemInfoQueryReturnXml(Stringxml0),taxNo));;
			list.addAll(DiskParseBusiUtil.getVmsTaxInfo(new TaxItemInfoQueryReturnXml(Stringxml1),taxNo));;
		} catch (Exception e) {
			message.setReturnCode(Message.error);
			message.setReturnMsg(Message.parse_tax_Item_info_query_erroe);
			printWriterResult(JsonUtil.toJsonString(message));
		}
		//保存税种税目
		try {
			pageTaxInvoiceDiskService.saveAndUpdateTaxInfo(list);
			message.setReturnCode(Message.success);
			message.setReturnMsg(Message.tax_Item_save_success);
			printWriterResult(JsonUtil.toJsonString(message));
		} catch (Exception e) {
			
		}
	}
	/**
	 *  创建购票信息查询xml
	 */
	public void createBuyBillInfoXml(){
		String diskNo=request.getParameter("diskNo");
		String fapiaoType0=request.getParameter("fapiaoType0");
		String fapiaoType1=request.getParameter("fapiaoType1");
		Message message=new Message();
		try {
			message.setStringXml(pageTaxInvoiceDiskService.CreateBuyBillInfoQuery(fapiaoType0, fapiaoType0));
			message.setStringXmlTwo(pageTaxInvoiceDiskService.CreateBuyBillInfoQuery(diskNo, fapiaoType1));
			message.setReturnCode(Message.success);
		} catch (Exception e) {
			message.setReturnCode(Message.error);
			message.setReturnMsg(Message.system_exception_buy_bill_Xml_error);
		}
		
		
	}
	/**
	 *  保存 电子库存 及库存信息
	 */
	public void saveOrUpdateAutoInvoiceDisk(){
		String Stringxml0=request.getParameter("Stringxml0");
		String Stringxml1=request.getParameter("Stringxml1");
		
	}
	public String getTaxParam() {
		return taxParam;
	}

	public void setTaxParam(String taxParam) {
		this.taxParam = taxParam;
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	public Map getMapVatType() {
		return mapVatType;
	}

	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public PaperInvoiceListInfo getPaperListInfo() {
		return paperListInfo;
	}

	public void setPaperListInfo(PaperInvoiceListInfo paperListInfo) {
		this.paperListInfo = paperListInfo;
	}

	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}

	public void setParamConfigVmssService(
			ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}

	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
	}

	public PageTaxInvoiceService getPageTaxInvoiceService() {
		return pageTaxInvoiceService;
	}

	public void setPageTaxInvoiceService(PageTaxInvoiceService pageTaxInvoiceService) {
		this.pageTaxInvoiceService = pageTaxInvoiceService;
	}

	public BillIssueService getBillIssueService() {
		return billIssueService;
	}

	public void setBillIssueService(BillIssueService billIssueService) {
		this.billIssueService = billIssueService;
	}

	public PageTaxInvoiceDiskAssitService getPageTaxInvoiceDiskService() {
		return pageTaxInvoiceDiskService;
	}

	public void setPageTaxInvoiceDiskService(
			PageTaxInvoiceDiskAssitService pageTaxInvoiceDiskService) {
		this.pageTaxInvoiceDiskService = pageTaxInvoiceDiskService;
	}
	

}
