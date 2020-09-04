package com.cjit.vms.taxdisk.single.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cjit.crms.util.json.JsonUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.trans.action.BillAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.service.BillInfoService;
import com.cjit.vms.trans.service.storage.PaperInvoiceService;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.service.BillPrintDiskService;
import com.cjit.vms.taxdisk.single.service.util.Message;
 

public class BillPrintDiskAction extends BillAction{

	private static final long serialVersionUID = 1L;
	
	//注入service
	private ParamConfigVmssService paramConfigVmssService;
	private BillInfoService billInfoService;
	private BillPrintDiskService billPrintDiskService;
	
	public String  listBillPrintDisk(){
		if (!sessionInit(true)) {
			request.getSession().setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				this.billInfo.setBillBeginDate(null);
				this.billInfo.setBillEndDate(null);
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().setAttribute("curPage",
						new Integer(1));
				this.request.getSession().setAttribute("pageSize",
						new Integer(20));
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				this.request.getSession().removeAttribute("customerName");
				this.request.getSession().removeAttribute("customerTaxno");
				this.request.getSession().removeAttribute("isHandiwork");
				this.request.getSession().removeAttribute("issueType");
				this.request.getSession().removeAttribute("fapiaoType");
				this.request.getSession().removeAttribute("dataStatus");
				fromFlag = null;
			} else if ("view".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				// 申请开票日期(开始)
				if (!String
						.valueOf(
								this.request.getSession().getAttribute(
										"billBeginDate")).isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("billBeginDate")))) {
					this.billInfo.setBillBeginDate(String.valueOf(this.request
							.getSession().getAttribute("billBeginDate")));
				}
				// 申请开票日期(结束)
				if (!String.valueOf(
						this.request.getSession().getAttribute("billEndDate"))
						.isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("billEndDate")))) {
					this.billInfo.setBillEndDate(String.valueOf(this.request
							.getSession().getAttribute("billEndDate")));
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
				// 发票状态
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("dataStatus")))) {
					this.billInfo.setDataStatus(String.valueOf(this.request
							.getSession().getAttribute("dataStatus")));
				}
				fromFlag = null;
			} else {
				billInfo = new BillInfo();
				this.billInfo.setBillBeginDate(this.request
						.getParameter("billInfo.billBeginDate"));
				this.billInfo.setBillEndDate(this.request
						.getParameter("billInfo.billEndDate"));
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

			try {
				printLimitValue = Integer.valueOf(
						paramConfigVmssService.findvaluebyName("单次打印限值（张）"))
						.intValue();
				taxParam = paramConfigVmssService.findvaluebyName("税控参数");

			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			// billInfo.setBillBeginDate(this.getBillBeginDate());
			// billInfo.setBillEndDate(this.getBillEndDate());
			// 默认按照专用发票查询
			if (billInfo.getFapiaoType() == null
					|| billInfo.getFapiaoType().equals("0")) {
				billInfo.setFapiaoType("0");
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billInfo.setLstAuthInstId(lstAuthInstId);
			billInfo.setUserId(currentUser.getId());
			billInfoList = billInfoService.findBillInfoListNew(billInfo,
					currentUser.getId(), paginationList);

			this.request.getSession().setAttribute("billInfoList", billInfoList);
			if (this.billInfo.getBillBeginDate() != null) {
				this.request.getSession().setAttribute("billBeginDate",
						this.billInfo.getBillBeginDate());
			}
			if (this.billInfo.getBillEndDate() != null) {
				this.request.getSession().setAttribute("billEndDate",
						this.billInfo.getBillEndDate());
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
			if (this.billInfo.getDataStatus() != null) {
				this.request.getSession().setAttribute("dataStatus",
						this.billInfo.getDataStatus());
			}

			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.getSession().setAttribute("currMonth", currMonth);
			this.request.getSession().setAttribute("curPage",
					String.valueOf(paginationList.getCurrentPage()));
			this.request.getSession().setAttribute("pageSize",
					String.valueOf(paginationList.getPageSize()));

			// czl
			int zbillInfo = billInfoService.findz("G", "VMS_BILL_INFO");
			int pbillInfo = billInfoService.findp("G", "VMS_BILL_INFO");
			this.request.getSession().setAttribute("zbillInfo", Integer.toString(zbillInfo));
			this.request.getSession().setAttribute("pbillInfo", Integer.toString(pbillInfo));
			if (taxParam.equals(TaxSelvetUtil.tax_Server_ch)) {
				return "tax";
			} else {
				return "disk";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillPrint", e);
		}
		return ERROR;
	}
	
	/**
	 *  创建发票打印的xml
	 * @throws Exception 
	 */
	public void createBillPrintDiskXml() throws Exception{
		String diskNo =request.getParameter("diskNo");
		String billId =request.getParameter("billId");
		String messages=billPrintDiskService.createBillPrintXml(diskNo, billId);
		printWriterResult(messages);
	
	}
	/**
	 *  更改打印结果
	 * @throws Exception
	 */
/*	public void updateBillDiskPrintResult() throws Exception{
		String StringXml=request.getParameter("StringXml");
		String billId = request.getParameter("billId");
		String messages=billPrintDiskService.updateBillPrintResult(StringXml, billId);
		printWriterResult(messages);
			
	}*/
	
	public PaperInvoiceService getPaperInvoiceService() {
		return paperInvoiceService;
	}

	public void setPaperInvoiceService(PaperInvoiceService paperInvoiceService) {
		this.paperInvoiceService = paperInvoiceService;
	}

	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}

	public void setParamConfigVmssService(
			ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}

	public BillInfoService getBillInfoService() {
		return billInfoService;
	}

	public void setBillInfoService(BillInfoService billInfoService) {
		this.billInfoService = billInfoService;
	}

	public BillPrintDiskService getBillPrintDiskService() {
		return billPrintDiskService;
	}

	public void setBillPrintDiskService(BillPrintDiskService billPrintDiskService) {
		this.billPrintDiskService = billPrintDiskService;
	}
	protected void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}
	
}
