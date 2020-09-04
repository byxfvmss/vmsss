package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import cjit.crms.util.StringUtil;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.filem.util.ThreadLock;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillPrintHistoryInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.billInvalid.BillCancelInfo;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.BillPrintHistoryService;
import com.cjit.vms.trans.service.BillTrackService;
import com.cjit.vms.trans.util.DataUtil;

public class BillSupplementPrintAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private BillInfo billInfo = new BillInfo();
	private BillCancelInfo billCancelInfo = new BillCancelInfo();
	private String message;
	private String flag;
	private List billCancelInfoList;
	private BillTrackService billTrackService;
	private String filePath;
	private BillIssueService billIssueService;
	private List lsBillIsHandiWorklist = new ArrayList();
	private Map mapillIsHandiWorklist = null;
	private ParamConfigVmssService paramConfigVmssService;
	private String taxParam;// 税控参数

	// 发票补打信息
	private BillPrintHistoryInfo bphInfo;
	private BillPrintHistoryService bphService;

	public String listBillSupplementPrint() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			this.message = (String) this.request.getAttribute("message");
			Map map = transInfoService.findSysParam("OBSOLTETTIME");
			billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString());
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billCancelInfo = new BillCancelInfo();
				billCancelInfo.setCancelTime(map.get("SELECTED_VALUE")
						.toString());
				this.setBillBeginDate(DateUtils.getBeforeDay());
				this.setBillEndDate(DateUtils.getNowDay());
				this.billCancelInfo.setBillBeginDate(getBillBeginDate());
				this.billCancelInfo.setBillEndDate(getBillEndDate());
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				billCancelInfo.setFapiaoType("0");
				lsBillIsHandiWorklist = DataUtil.getBillIsHandiWorklist();
				mapillIsHandiWorklist = new HashMap();
				for (int i = 0; i < lsBillIsHandiWorklist.size(); i++) {
					SelectTag tag = (SelectTag) lsBillIsHandiWorklist.get(i);
					mapillIsHandiWorklist.put(tag.getValue(), tag.getText());
				}
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
				fromFlag = null;
			} else if ("view".equalsIgnoreCase(fromFlag)) {
				billCancelInfo = new BillCancelInfo();
				// 开票日期(开始)
				if (!String
						.valueOf(
								this.request.getSession().getAttribute(
										"billBeginDate")).isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("billBeginDate")))) {
					this.billCancelInfo.setBillBeginDate(String
							.valueOf(this.request.getSession().getAttribute(
									"billBeginDate")));
				}
				// 开票日期(结束)
				if (!String.valueOf(
						this.request.getSession().getAttribute("billEndDate"))
						.isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("billEndDate")))) {
					this.billCancelInfo.setBillEndDate(String
							.valueOf(this.request.getSession().getAttribute(
									"billEndDate")));
				}
				// 客户名称
				if (this.request.getSession().getAttribute("customerName") != null
						&& !"".equals(this.request.getSession().getAttribute(
								"customerName"))) {
					this.billCancelInfo.setCustomerName(String
							.valueOf(this.request.getSession().getAttribute(
									"customerName")));
				}
				// 客户号
				if (this.request.getSession().getAttribute("customerId") != null
						&& !"".equals(this.request.getSession().getAttribute(
								"customerId"))) {
					this.billCancelInfo.setCustomerId(String
							.valueOf(this.request.getSession().getAttribute(
									"customerId")));
				}
				// 是否手工录入
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("isHandiwork")))) {
					this.billCancelInfo.setIsHandiwork(String
							.valueOf(this.request.getSession().getAttribute(
									"isHandiwork")));
				}
				// 开具类型
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("issueType")))) {
					this.billCancelInfo.setIssueType(String
							.valueOf(this.request.getSession().getAttribute(
									"issueType")));
				}
				// 发票类型
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("fapiaoType")))) {
					this.billCancelInfo.setFapiaoType(String
							.valueOf(this.request.getSession().getAttribute(
									"fapiaoType")));
				}
				// 状态
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("dataStatus")))) {
					this.billCancelInfo.setDataStatus(String
							.valueOf(this.request.getSession().getAttribute(
									"dataStatus")));
				}
				fromFlag = null;
			} else {
				billCancelInfo = new BillCancelInfo();
				this.billCancelInfo.setBillBeginDate(this.getBillBeginDate());
				this.billCancelInfo.setBillEndDate(this.getBillEndDate());
				this.billCancelInfo.setCustomerName(this.request
						.getParameter("billCancelInfo.customerName"));
				this.billCancelInfo.setCustomerId(this.request
						.getParameter("billCancelInfo.customerId"));
				this.billCancelInfo.setCustomerTaxno(this.request
						.getParameter("billCancelInfo.customerTaxno"));
				this.billCancelInfo.setIsHandiwork(this.request
						.getParameter("billCancelInfo.isHandiwork"));
				this.billCancelInfo.setIssueType(this.request
						.getParameter("billCancelInfo.issueType"));
				this.billCancelInfo.setFapiaoType(this.request
						.getParameter("billCancelInfo.fapiaoType"));
				this.billCancelInfo.setDataStatus(this.request
						.getParameter("billCancelInfo.dataStatus"));
			}

			// 票据审核画面case追加 at lee start
			if (StringUtil.isNotEmpty(flag)
					&& DataUtil.AUDIT_BILL.equalsIgnoreCase(flag)) {
				this.billCancelInfo.setSearchFlag(flag);
			}
			// 票据审核画面case追加 at lee end

			billCancelInfo.setUserId(currentUser.getId());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			billCancelInfoList = billSupplementPrintService
					.findBillSupplementPrintInfoList(billCancelInfo,
							currentUser.getId(), paginationList);

			request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("billInfoList", billCancelInfoList);
			if (this.billCancelInfo.getBillBeginDate() != null) {
				this.request.getSession().setAttribute("billBeginDate",
						this.billCancelInfo.getBillBeginDate());
			}
			if (this.billCancelInfo.getBillEndDate() != null) {
				this.request.getSession().setAttribute("billEndDate",
						this.billCancelInfo.getBillEndDate());
			}
			if (this.billCancelInfo.getCustomerName() != null) {
				this.request.getSession().setAttribute("customerName",
						this.billCancelInfo.getCustomerName());
			}
			if (this.billCancelInfo.getCustomerId() != null) {
				this.request.getSession().setAttribute("customerId",
						this.billCancelInfo.getCustomerId());
			}
			if (this.billCancelInfo.getCustomerTaxno() != null) {
				this.request.getSession().setAttribute("customerTaxno",
						this.billCancelInfo.getCustomerTaxno());
			}
			if (this.billCancelInfo.getIsHandiwork() != null) {
				this.request.getSession().setAttribute("isHandiwork",
						this.billCancelInfo.getIsHandiwork());
			}
			if (this.billCancelInfo.getIssueType() != null) {
				this.request.getSession().setAttribute("issueType",
						this.billCancelInfo.getIssueType());
			}
			if (this.billCancelInfo.getFapiaoType() != null) {
				this.request.getSession().setAttribute("fapiaoType",
						this.billCancelInfo.getFapiaoType());
			}
			if (this.billCancelInfo.getDataStatus() != null) {
				this.request.getSession().setAttribute("dataStatus",
						this.billCancelInfo.getDataStatus());
			}
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");
			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.setAttribute("currMonth", currMonth);
			this.request.getSession().setAttribute("curPage",
					String.valueOf(paginationList.getCurrentPage()));
			this.request.getSession().setAttribute("pageSize",
					String.valueOf(paginationList.getPageSize()));
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
				return "tax";
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillSupplementPrintAction-listBillSupplementPrint", e);
		}
		return ERROR;
	}

	/**
	 * 发票补打页面查看交易
	 */
	public String seeTransForBillSupplement() {
		try {
			billCancelInfo.setBillId((String) this.request
					.getParameter("billId"));
			this.request.getSession().setAttribute("curPage",
					Integer.valueOf(this.getCurPage()));

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillSupplementPrintAction-seeTransWithBill", e);
		}
		return ERROR;
	}

	/**
	 * 发票补打页面查看票样
	 */
	public String viewImgFromBillSupplement() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = billTrackService.findBillInfoById(billId);

			List billItemList = billTrackService.findBillItemByBillId(billId);
			if (billItemList.size() > 9) {
				this.setRESULT_MESSAGE("发票最多包含9条数据！");
				return ERROR;
			}

			Map map = new HashMap();
			map.put("vatType", billInfo.getFapiaoType());
			map.put("billCode", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("billDate", billInfo.getBillDate());
			map.put("customerName", billInfo.getName());
			map.put("customerTaxno", billInfo.getTaxno());
			map.put("customerAddressandphone", billInfo.getAddressandphone());
			map.put("customerBankandaccount", billInfo.getBankandaccount());
			map.put("billItemList", billItemList);
			map.put("cancelName", billInfo.getCustomerName());
			map.put("cancelTaxno", billInfo.getCustomerTaxno());
			map.put("cancelAddressandphone",
					billInfo.getCustomerAddressandphone());
			map.put("cancelBankandaccount",
					billInfo.getCustomerBankandaccount());
			map.put("payeeName", billInfo.getPayee());
			map.put("reviewerName", billInfo.getReviewerName());
			map.put("drawerName", billInfo.getDrawerName());
			map.put("remark", billInfo.getRemark());

			String imgName = vmsCommonService.createMark(map);
			Map retMap = vmsCommonService.findCodeDictionary("BILL_IMG_PATH");

			filePath = retMap.get("BILL_IMG_PATH") + imgName;
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-viewImgFromBill", e);
		}
		return ERROR;
	}

	/**
	 * 发票补打信息记录查看
	 * 
	 * @return
	 */
	public String seeBillPrintHistory() {

		try {
			// String billID = "B2015112700000001037";
			String billID = (String) this.request.getParameter("billId");
			if (billID != null) {
				/*
				 * bphInfo=bphService.findBillPrintHistoryInfoByID(billID);
				 * this.fromFlag = ""; this.request.setAttribute("bphInfo",
				 * bphInfo);
				 */
				List<BillPrintHistoryInfo> bphList = bphService
						.findBillPrintHistoryListByID(billID);

				this.fromFlag = "";
				this.request.setAttribute("bphList", bphList);
				return SUCCESS;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return ERROR;
	}

	/**
	 * 导出Excel
	 * 
	 * @return String
	 */
	public void billSupplementPrintToExcel() throws Exception {
		try {
			// 开票日期 开始 结束
			billCancelInfo.setBillBeginDate(this.billCancelInfo
					.getBillBeginDate());
			billCancelInfo.setBillEndDate(this.billCancelInfo.getBillEndDate());
			// 客户纳税人名称
			billCancelInfo.setCustomerName(this.billCancelInfo
					.getCustomerName());
			// 是否手工录入
			billCancelInfo.setIsHandiwork(this.billCancelInfo.getIsHandiwork());
			// 开具类型
			billCancelInfo.setIssueType(this.billCancelInfo.getIssueType());
			// 发票类型
			billCancelInfo.setFapiaoType(this.billCancelInfo.getFapiaoType());
			// 状态（已打印）
			if (this.billCancelInfo.getDataStatus().equals("")) {
				billCancelInfo.setDataStatus(DataUtil.BILL_STATUS_8);
			} else {
				billCancelInfo.setDataStatus(this.billCancelInfo
						.getDataStatus());
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			billCancelInfoList = billSupplementPrintService
					.findBillSupplementPrintQuery(billCancelInfo);
			this.request.getSession().setAttribute("transBeginDate",
					billCancelInfo.getBillBeginDate());
			this.request.getSession().setAttribute("transEndDate",
					billCancelInfo.getBillEndDate());

			StringBuffer fileName = new StringBuffer("发票补打信息表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, billCancelInfoList);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);

		WritableSheet ws = null;
		ws = wb.createSheet("发票补打信息表", 0);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "申请开票日期", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "开票日期", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "发票代码", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "发票号码", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "开票人", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "税控盘号", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "开票机号", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "合计金额", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "合计税额", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "税率", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "价税合计", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "是否手工录入", JXLTool.getHeader());
		Label header16 = new Label(15, 0, "开具类型", JXLTool.getHeader());
		Label header17 = new Label(16, 0, "发票类型", JXLTool.getHeader());
		Label header18 = new Label(17, 0, "状态", JXLTool.getHeader());

		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header7);
		ws.addCell(header8);
		ws.addCell(header9);
		ws.addCell(header10);
		ws.addCell(header11);
		ws.addCell(header12);
		ws.addCell(header13);
		ws.addCell(header14);
		ws.addCell(header15);
		ws.addCell(header16);
		ws.addCell(header17);
		ws.addCell(header18);
		for (int i = 0; i < 18; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			BillCancelInfo o = (BillCancelInfo) content.get(i);
			int column = count++;
			Map map = new HashMap();
			map.put("Id", Integer.valueOf(i + 1));
			map.put("applyDate", o.getApplyDate());
			map.put("billDate", o.getBillDate());
			map.put("customerName", o.getCustomerName());
			map.put("customerTaxNo", o.getCustomerTaxno());
			map.put("billCode", o.getBillCode());
			map.put("billNo", o.getBillNo());
			map.put("drawer", o.getDrawer());
			map.put("taxDiskNo", o.getTaxDiskNo());
			map.put("machineNo", o.getMachineNo());
			map.put("amtSum", o.getAmtSum());
			map.put("taxAmtSum", o.getTaxAmtSum());
			map.put("taxRate", o.getTaxRate());
			map.put("sumAmt", o.getSumAmt());
			map.put("isHandiwork", o.getIsHandiwork());
			map.put("issueType", o.getIssueType());
			map.put("fapiaoType", o.getFapiaoType());
			map.put("dataStatus", o.getDataStatus());

			setWritableSheet(ws, map, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, Map o, int column)
			throws WriteException {

		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);
		// 序号
		Label cell1 = new Label(0, column, o.get("Id").toString(),
				tempCellFormat);

		// 申请开票日期applyDate
		Label cell2 = new Label(1, column, o.get("applyDate") == null
				|| o.get("applyDate").equals("") ? "" : o.get("applyDate")
				.toString(), tempCellFormat);

		// 开票日期billDate
		Label cell3 = new Label(2, column, o.get("billDate") == null
				|| o.get("billDate").equals("") ? "" : o.get("billDate")
				.toString(), tempCellFormat);

		// 客户纳税人名称customerName
		Label cell4 = new Label(3, column, o.get("customerName") == null
				|| o.get("customerName").equals("") ? "" : o
				.get("customerName").toString(), tempCellFormat);
		// 客户纳税人识别号customerTaxNo
		Label cell5 = new Label(4, column, o.get("customerTaxNo") == null
				|| o.get("customerTaxNo").equals("") ? "" : o.get(
				"customerTaxNo").toString(), tempCellFormat);

		// 发票代码billCode
		Label cell6 = new Label(5, column, o.get("billCode") == null
				|| o.get("billCode").equals("") ? "" : o.get("billCode")
				.toString(), tempCellFormat);

		// 发票号码billNo
		Label cell7 = new Label(6, column,
				o.get("billNo") == null || o.get("billNo").equals("") ? "" : o
						.get("billNo").toString(), tempCellFormat);

		// 开票人drawer
		Label cell8 = new Label(7, column,
				o.get("drawer") == null || o.get("drawer").equals("") ? "" : o
						.get("drawer").toString(), tempCellFormat);

		// 税控盘号taxDiskNo
		Label cell9 = new Label(8, column, o.get("taxDiskNo") == null
				|| o.get("taxDiskNo").equals("") ? "" : o.get("taxDiskNo")
				.toString(), tempCellFormat);

		// 开票机号machineNo
		Label cell10 = new Label(9, column, o.get("machineNo") == null
				|| o.get("machineNo").equals("") ? "" : o.get("machineNo")
				.toString(), tempCellFormat);

		// 合计金额amtSum
		Label cell11 = new Label(10, column,
				o.get("amtSum") == null || o.get("amtSum").equals("") ? "" : o
						.get("amtSum").toString(), tempCellFormat);
		// 合计税额taxAmtSum
		Label cell12 = new Label(11, column, o.get("taxAmtSum") == null
				|| o.get("taxAmtSum").equals("") ? "" : o.get("taxAmtSum")
				.toString(), tempCellFormat);

		// 税率taxRate
		Label cell13 = new Label(12, column, o.get("taxRate") == null
				|| o.get("taxRate").equals("") ? "" : o.get("taxRate")
				.toString(), tempCellFormat);

		// 价税合计sumAmt
		Label cell14 = new Label(13, column,
				o.get("sumAmt") == null || o.get("sumAmt").equals("") ? "" : o
						.get("sumAmt").toString(), tempCellFormat);

		// 是否手工录入isHandiwork
		String isHandiwork = "";
		if (o.get("isHandiwork") == null || o.get("isHandiwork").equals("")) {
			isHandiwork = "";
		} else {
			if (o.get("isHandiwork").equals("2")) {
				isHandiwork = "人工审核";
			} else {
				isHandiwork = "人工开票";
			}
		}
		Label cell15 = new Label(14, column, isHandiwork, tempCellFormat);

		// 开具类型issueType
		String issueType = "";
		if (o.get("issueType") == null || o.get("issueType").equals("")) {
			issueType = "";
		} else {
			if (o.get("issueType").equals("1")) {
				issueType = "单笔";
			} else if (o.get("issueType").equals("2")) {
				issueType = "合并";
			} else {
				issueType = "拆分";
			}
		}
		Label cell16 = new Label(15, column, issueType, tempCellFormat);

		// 发票类型fapiaoType
		String fapiaoType = "";
		if (o.get("fapiaoType") == null || o.get("fapiaoType").equals("")) {
			fapiaoType = "";
		} else {
			if (o.get("fapiaoType").equals("0")) {
				fapiaoType = "增值税专用发票";
			} else {
				fapiaoType = "增值税普通发票";
			}
		}
		Label cell17 = new Label(16, column, fapiaoType, tempCellFormat);

		// 状态dataStatus
		String dataStatus = "";
		if (o.get("dataStatus") == null || o.get("dataStatus").equals("")) {
			dataStatus = "";
		} else {
			if (o.get("dataStatus").equals("8")) {
				dataStatus = "已打印";
			} else {
				dataStatus = "";
			}
		}
		Label cell18 = new Label(17, column, dataStatus, tempCellFormat);

		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
		ws.addCell(cell8);
		ws.addCell(cell9);
		ws.addCell(cell10);
		ws.addCell(cell11);
		ws.addCell(cell12);
		ws.addCell(cell13);
		ws.addCell(cell14);
		ws.addCell(cell15);
		ws.addCell(cell16);
		ws.addCell(cell17);
		ws.addCell(cell18);
	}

	public void showSubLock() throws Exception {

		String result = "";
		String lock = getInstLock();
		if (lock != null) {
			result = lock;
			printWriterResult(result);
			return;
		}
		String taxDiskNo = request.getParameter("taxDiskNo");
		String fapiaoType = request.getParameter("fapiaoType");

		// 获取注册码
		String registeredInfo = billIssueService.findRegisteredInfo(taxDiskNo);
		if (registeredInfo == null) {
			result = "registeredInfoError";
			printWriterResult(result);
			return;
		}

		// 查询税控盘口令和证书口令
		TaxDiskInfo taxDiskInfo = billIssueService
				.findTaxDiskInfoByTaxDiskNo(taxDiskNo);
		if (taxDiskInfo == null) {
			result = "taxPwdError";
			printWriterResult(result);
			return;
		}

		result = registeredInfo + "|" + taxDiskInfo.getTaxDiskPsw() + "|"
				+ fapiaoType;
		printWriterResult(result);

	}

	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
		releaseInstLock();
	}

	/**
	 * @return lock 为锁定状态
	 */
	private String getInstLock() {
		String instCode = this.getCurrentUser().getOrgId();
		boolean lock = ThreadLock.getLockState(instCode);
		if (lock) {
			return "lock";
		}
		return null;
	}

	private void releaseInstLock() {
		String instCode = this.getCurrentUser().getOrgId();
		ThreadLock.releaseLock(instCode);
	}

	/**
	 * 后台根据前台页面选中的billId组装传进OCX的字符串 注册码|发票类型(0:专,1:普)|发票数量|发票ID^发票代码^发票号码|
	 * 发票ID^发票代码^发票号码为循环域
	 * 
	 * @return
	 */
	public String showOCXSubstring() {
		try {
			String billIds = (String) request.getParameter("billIds");
			String faPiaoType = (String) request.getParameter("faPiaoType");
			String[] ids = billIds.split(",");
			StringBuffer sbInner = new StringBuffer();
			List bills = billInfoService.findBillInfoByIDFaPiaoType(ids,
					faPiaoType);
			BillInfo billInfo = (BillInfo) bills.get(0);
			// ：注册码|发票类型(0:专,1:普)|发票数量|发票ID^发票代码^发票号码| 红色部分为循环域
			sbInner.append('0').append("^");
			sbInner.append(billInfo.getBillId()).append("^")
					.append(billInfo.getBillCode()).append("^")
					.append(billInfo.getBillNo());
			String result = sbInner.toString();
			// 将结果写出
			this.response.setContentType("application/json;charset=UTF-8");
			this.response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("ajax-拼接传给税控盘的字符串出错");
		} finally {

		}
		return null;
	}

	public void updateSubPrintResult() throws Exception {
		// resultOCX="1,1,2,0,3,1,4,0,5,1,6,0,7,1,8,0,9,1,10,0";
		// 1^B2015112700000001037^1110098079^73746052^成功|
		try {
			String resultOCX = (String) request.getParameter("resultOCX");
			String billID = (String) request.getParameter("billID");
			System.out.println(resultOCX);
			System.out.println(billID);

			String[] results = resultOCX.split("\\^");
			String print = "";
			if (results[0].equals("1")) {
				print = "打印成功";
				billInfoService.updateBillByBillIdsup(results[1], "8");
				updateTransInfo(results[1]);
				// billInfoService.updateBillByBillIdsup(results[1], "8");
				// updateTransInfo(results[1]);
				// 添加补打信息记录
				addBillPrintHistoryInfo(billID, "Y");
			} else {
				billInfoService.updateBillByBillIdsup(results[1], "9");
				addBillPrintHistoryInfo(billID, "N");
				print = results[4];
			}

			// 将结果写出
			printWriterResult(print);

		} catch (Exception e) {
			printWriterResult("系统错误");
		}
	}

	/**
	 * 添加补打信息记录
	 */
	public void addBillPrintHistoryInfo(String billID, String flag) {
		// 获取当前系统时间
		String date = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss")
				.format(new Date());
		// 获取当前用户
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		String printer = currentUser.getName();
		BillPrintHistoryInfo bph = new BillPrintHistoryInfo(billID, printer,
				date, flag);
		bphService.saveBillPrintHistoryInfo(bph);

	}

	/**
	 * 
	 * 表头单元格样式的设定
	 */
	public WritableCellFormat getBodyCellStyle() {

		/*
		 * WritableFont.createFont("宋体")：设置字体为宋体 10：设置字体大小
		 * WritableFont.NO_BOLD:设置字体非加粗（BOLD：加粗 NO_BOLD：不加粗） false：设置非斜体
		 * UnderlineStyle.NO_UNDERLINE：没有下划线
		 */
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10,
				WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);

		WritableCellFormat bodyFormat = new WritableCellFormat(font);
		try {
			// 设置单元格背景色：表体为白色
			bodyFormat.setBackground(Colour.WHITE);
			// 设置表头表格边框样式
			// 整个表格线为细线、黑色
			bodyFormat
					.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

		} catch (WriteException e) {
			System.out.println("表体单元格样式设置失败！");
		}
		return bodyFormat;
	}

	public BillCancelInfo getBillCancelInfo() {
		return billCancelInfo;
	}

	public void setBillCancelInfo(BillCancelInfo billCancelInfo) {
		this.billCancelInfo = billCancelInfo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
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

	public List getLsBillIsHandiWorklist() {
		return lsBillIsHandiWorklist;
	}

	public void setLsBillIsHandiWorklist(List lsBillIsHandiWorklist) {
		this.lsBillIsHandiWorklist = lsBillIsHandiWorklist;
	}

	public Map getMapillIsHandiWorklist() {
		return mapillIsHandiWorklist;
	}

	public void setMapillIsHandiWorklist(Map mapillIsHandiWorklist) {
		this.mapillIsHandiWorklist = mapillIsHandiWorklist;
	}

	public void updateTransInfo(String billId) {
		BillInfo bill = billIssueService.findBillInfoById(billId);
		if ("3".equals(bill.getIssueType())) {
			// 票据为拆分而来，当交易对应的所有票据状态为已开具更改交易状态
			BillInfo issueBill = new BillInfo();
			issueBill.setBillId(billId);
			List transList = billIssueService.findTransByBillId(billId);
			if (transList != null && transList.size() == 1) {
				TransInfo trans = (TransInfo) transList.get(0);
				if (trans.getBalance().compareTo(new BigDecimal(0)) == 0) {
					issueBill = new BillInfo();
					issueBill.setTransId(trans.getTransId());
					List billFromOneTransList = billIssueService
							.findBillInfoList(issueBill);
					boolean flag = true;
					for (int j = 0; j < billFromOneTransList.size(); j++) {
						BillInfo bill1 = (BillInfo) billFromOneTransList.get(j);
						if (!"8".equals(bill1.getDataStatus())) {
							flag = false;
							break;
						}
					}
					if (flag) {
						billIssueService.updateTransInfoStatus("99", billId);
					}
				}
			}

		} else {
			billIssueService.updateTransInfoStatus("99", billId);
			// printWriterResult("开具成功！");
		}
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

	public BillPrintHistoryService getBphService() {
		return bphService;
	}

	public void setBphService(BillPrintHistoryService bphService) {
		this.bphService = bphService;
	}

	public BillPrintHistoryInfo getBphInfo() {
		return bphInfo;
	}

	public void setBphInfo(BillPrintHistoryInfo bphInfo) {
		this.bphInfo = bphInfo;
	}

	/*
	 * private List<BillPrintHistoryInfo> bphList;
	 * 
	 * public List<BillPrintHistoryInfo> getBphList() { return bphList; }
	 * 
	 * public void setBphList(List<BillPrintHistoryInfo> bphList) { this.bphList
	 * = bphList; }
	 */

}
