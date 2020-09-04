package com.cjit.vms.trans.action.disk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;
import com.cjit.vms.trans.action.DataDealAction;


/**
 * @author tom  同步稅控盘
 *
 */
public class PageTaxInvoiceDiskAction extends DataDealAction {
	private String taxParam;//税控参数
	private List lstAuthInstId;
	private Map mapVatType;
	private String flag;//提交按钮区分
	private PaperInvoiceListInfo paperListInfo = new PaperInvoiceListInfo();
	private ParamConfigVmssService paramConfigVmssService;
	private VmsCommonService vmsCommonService;
	private PageTaxInvoiceService pageTaxInvoiceService;
	private BillIssueService billIssueService;

	public String listPageTaxInvoiceDisk(){
		
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
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
			this.request.setAttribute("paperInfoList", paperInfoList);
			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.setAttribute("currMonth", currMonth);
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
	

}
