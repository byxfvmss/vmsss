package com.cjit.vms.trans.action.disk;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cjit.crms.util.json.JsonUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.action.DataDealAction;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;
import com.cjit.vms.trans.service.taxDisk.TaxDiskInfoQueryService;
import com.cjit.vms.trans.service.taxDisk.util.Message;

public class BillIssueDiskAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private BillInfo billInfo;
	private List billInfoList;
	private List transInfoList;
	private String[] selectBillIds;
	private String filePath;
	private BillIssueService billIssueService;
	private PageTaxInvoiceService pageTaxInvoiceService;
	private ParamConfigVmssService paramConfigVmssService;
	private LogManagerService logManagerService;
	private VmsCommonService vmsCommonService;
	private TaxDiskInfoQueryService taxDiskInfoQueryService;
	private String taxParam;//税控参数
	
	public String listbillIssueDisk()
	{
		try {
			if ("menu".equals(fromFlag)) {
				billInfo = new BillInfo();
				this.billInfo.setApplyBeginDate(null);
				this.billInfo.setApplyEndDate(null);
				this.request.getSession().setAttribute("curPage", new Integer(1));
				this.request.getSession().setAttribute("pageSize", new Integer(20));
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				this.request.getSession().removeAttribute("customerName");
				this.request.getSession().removeAttribute("customerTaxno");
				this.request.getSession().removeAttribute("isHandiwork");
				this.request.getSession().removeAttribute("issueType");
				this.request.getSession().removeAttribute("fapiaoType");
				this.request.getSession().removeAttribute("dataStatus");
				billInfo.setFapiaoType("0");
				fromFlag = null;
			} else if ("view".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				// 申请开票日期(开始)
				if (!String.valueOf(this.request.getSession().getAttribute("applyBeginDate")).isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("applyBeginDate")))) {
					this.billInfo.setApplyBeginDate(String.valueOf(this.request
							.getSession().getAttribute("applyBeginDate")));
				}
				// 申请开票日期(结束)
				if (!String.valueOf(this.request.getSession().getAttribute("applyEndDate")).isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("applyEndDate")))) {
					this.billInfo.setApplyEndDate(String.valueOf(this.request
							.getSession().getAttribute("applyEndDate")));
				}
				// 客户名称
				if (!"".equals(this.request.getSession().getAttribute(
						"customerName"))) {
					this.billInfo.setCustomerName(String.valueOf(this.request
							.getSession().getAttribute("customerName")));
				}
				// 客户纳税人识别号
				if (!"".equals(this.request.getSession().getAttribute(
						"customerTaxno"))) {
					this.billInfo.setCustomerTaxno(String.valueOf(this.request
							.getSession().getAttribute("customerTaxno")));
				}
				// 是否手工录入
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("isHandiwork")))) {
					this.billInfo.setIsHandiwork(String.valueOf(this.request
							.getSession().getAttribute("isHandiwork")));
				}
				// 开具类型
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("issueType")))) {
					this.billInfo.setIssueType(String.valueOf(this.request
							.getSession().getAttribute("issueType")));
				}
				// 发票类型
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("fapiaoType")))) {
					this.billInfo.setFapiaoType(String.valueOf(this.request
							.getSession().getAttribute("fapiaoType")));
				}
				// 状态
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("dataStatus")))) {
					this.billInfo.setDataStatus(String.valueOf(this.request
							.getSession().getAttribute("dataStatus")));
				}
				// 当前页
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("curPage")))) {
					paginationList.setCurrentPage(Integer.valueOf((String.valueOf(this.request
							.getSession().getAttribute("curPage")))).intValue());
				}
				fromFlag = null;
			} else {
				billInfo = new BillInfo();
				this.billInfo.setApplyBeginDate(this.request
						.getParameter("billInfo.applyBeginDate"));
				this.billInfo.setApplyEndDate(this.request
						.getParameter("billInfo.applyEndDate"));
				this.billInfo.setCustomerName(this.request
						.getParameter("billInfo.customerName"));
				this.billInfo.setCustomerTaxno(this.request
						.getParameter("billInfo.customerTaxno"));
				this.billInfo.setIsHandiwork(this.request
						.getParameter("billInfo.isHandiwork"));
				this.billInfo.setIssueType(this.request
						.getParameter("billInfo.issueType"));
				this.billInfo.setFapiaoType(this.request
						.getParameter("billInfo.fapiaoType"));
				this.billInfo.setDataStatus(this.request
						.getParameter("billInfo.dataStatus"));
			}
			User currentUser = this.getCurrentUser();
			billInfo.setUserId(currentUser.getId());
			
			if (StringUtil.isEmpty(billInfo.getDataStatus())) {
				billInfo.setDataStatus("3,4,7");
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billInfo.setLstAuthInstId(lstAuthInstId);
			billInfo.setSumAmtBegin(new BigDecimal(0));
			billInfoList = billIssueService.findBillInfoList(billInfo, paginationList);
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");
			//查询空白发票作废数量
			String instId=this.getCurrentUser().getOrgId();
			String currMonth = DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_YYYYMM);
			Long invalidInvoiceNum = billIssueService.findInvalidInvoiceCount("2", billInfo.getFapiaoType(),instId);
			this.request.setAttribute("invalidInvoiceNum", invalidInvoiceNum);
			this.request.setAttribute("currMonth", currMonth);
			
			if (this.billInfo.getApplyBeginDate() != null) {
				this.request.getSession().setAttribute("applyBeginDate",
						this.billInfo.getApplyBeginDate());
			}
			if (this.billInfo.getApplyEndDate() != null) {
				this.request.getSession().setAttribute("applyEndDate",
						this.billInfo.getApplyEndDate());
			}
			if (this.billInfo.getCustomerName() != null) {
				this.request.getSession().setAttribute("customerName",
						this.billInfo.getCustomerName());
			}
			if (this.billInfo.getCustomerTaxno() != null) {
				this.request.getSession().setAttribute("customerTaxno",
						this.billInfo.getCustomerTaxno());
			}
			if (this.billInfo.getIsHandiwork() != null) {
				this.request.getSession().setAttribute("isHandiwork",
						this.billInfo.getIsHandiwork());
			}
			if (this.billInfo.getIssueType() != null) {
				this.request.getSession().setAttribute("issueType",
						this.billInfo.getIssueType());
			}
			if (this.billInfo.getFapiaoType() != null) {
				this.request.getSession().setAttribute("fapiaoType",
						this.billInfo.getFapiaoType());
			}
			this.request.getSession().setAttribute("curPage", String.valueOf(paginationList.getCurrentPage()));
			this.request.getSession().setAttribute("pageSize", String.valueOf(paginationList.getPageSize()));
			
			logManagerService.writeLog(request, currentUser,
					"0016", "发票开具", "销项税管理", "查询可供进行开具处理的票据信息列表", "1");
			if(taxParam.equals(TaxSelvetUtil.tax_Server_ch)){
				return "tax";
			}else{
				return "disk";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0016", "发票开具", "销项税管理", "查询可供进行开具处理的票据信息列表", "0");
			log.error("BillIssueAction-listIssueBill", e);
		}
		return ERROR;
	}

	public void createQueryTaxDiskXml() throws Exception{
		String StringXml="";
		Message message=new Message();
		try{
		 StringXml= taxDiskInfoQueryService.createTaxDiskInfoQueryXml();
		 message.setStringXml(StringXml);
		 message.setReturnCode(Message.success_ch);
		}catch (Exception e) {
			message.setReturnMsg(Message.system_exception);
			message.setReturnCode(Message.failure_ch);
			printWriterResult(JsonUtil.toJsonString(message));
		}
		printWriterResult(JsonUtil.toJsonString(message));
	}
	public void checkTaxDiskTaxNoandTaxNo(){
		String result="";
		try {
			String instID=this.getCurrentUser().getOrgId();
			String StringXml=request.getParameter("StringXml");
			result=taxDiskInfoQueryService.checkTaxDiskTaxNoAndInstNo(StringXml, instID);
		} catch (Exception e) {

		}
	}
	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public List getBillInfoList() {
		return billInfoList;
	}

	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
	}
	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public String[] getSelectBillIds() {
		return selectBillIds;
	}

	public void setSelectBillIds(String[] selectBillIds) {
		this.selectBillIds = selectBillIds;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public BillIssueService getBillIssueService() {
		return billIssueService;
	}

	public void setBillIssueService(BillIssueService billIssueService) {
		this.billIssueService = billIssueService;
	}

	public PageTaxInvoiceService getPageTaxInvoiceService() {
		return pageTaxInvoiceService;
	}

	public void setPageTaxInvoiceService(PageTaxInvoiceService pageTaxInvoiceService) {
		this.pageTaxInvoiceService = pageTaxInvoiceService;
	}

	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}

	public void setParamConfigVmssService(
			ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}

	public LogManagerService getLogManagerService() {
		return logManagerService;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
	}

	public String getTaxParam() {
		return taxParam;
	}

	public void setTaxParam(String taxParam) {
		this.taxParam = taxParam;
	}

	private void printWriterResult(String result) throws Exception {
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

	

}
