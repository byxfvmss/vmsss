package com.cjit.vms.aisino.action.redReceipt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
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

import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cjit.common.util.JXLTool;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.aisino.service.HxServiceFactory;
import com.cjit.vms.aisino.util.XmlFunc;
import com.cjit.vms.filem.util.ThreadLock;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.RedReceiptTransInfo;
import com.cjit.vms.trans.model.SpecialTicket;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.vms.trans.service.BillTrackService;
import com.cjit.vms.trans.service.redRecipt.RedReceiptApplyInfoService;
import com.cjit.vms.trans.util.DataUtil;

public class RedReceiptApplyInfoAisinoAction extends DataDealAction {

	private static final long serialVersionUID = 1L;

	private RedReceiptApplyInfo redReceiptApplyInfo = new RedReceiptApplyInfo();
	private BillInfo billInfo = new BillInfo();
	private BillTrackService billTrackService;
	private RedReceiptApplyInfoService redReceiptApplyInfoService;

	private String filePath;
	private String flag = "";
	private List billInfoList;
	private List transInfoList;
	private String message;
	private SpecialTicket specialTicket;
	private String listFlg;
	private String billId;

	private String[] selectBillIds;

	private ParamConfigVmssService paramConfigVmssService;
	private String taxParam;// 税控参数

	private String exportWordFileName;
	private InputStream wordFileStream;

	/**
	 * @return 红冲开具列表
	 */
	public String billRedIssueList() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		// 状态：红冲审核通过
		redReceiptApplyInfo.setDatastatus(DataUtil.BILL_STATUS_22);
		/*if (StringUtil.isEmpty(flag)) {
			redReceiptApplyInfo.setFapiaoType("0");
		}*/
		if (StringUtil.isEmpty(redReceiptApplyInfo.getIsHandiwork())) {
			redReceiptApplyInfo.setIsHandiwork("1");
		}
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		redReceiptApplyInfo.setLstAuthInstId(lstAuthInstId);
		redReceiptApplyInfoService.findRedReceiptList(redReceiptApplyInfo,
				paginationList);
		taxParam = paramConfigVmssService.findvaluebyName("税控参数");
		if (taxParam.equals(TaxSelvetUtil.tax_Server_ch)) {
			return "tax";
		} else {
			return "disk";
		}
	}

	/**
	 * @return 红冲处理 发票打印列表
	 */
	public String billRedPrintList() {
		redReceiptApplyInfo.setDatastatus(DataUtil.BILL_STATUS_5);
		if (StringUtil.isEmpty(flag)) {
			redReceiptApplyInfo.setFapiaoType("0");
		}
		if (StringUtil.isEmpty(redReceiptApplyInfo.getIsHandiwork())) {
			redReceiptApplyInfo.setIsHandiwork("1");
		}
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		redReceiptApplyInfo.setLstAuthInstId(lstAuthInstId);
		redReceiptApplyInfoService.findRedReceiptList(redReceiptApplyInfo,
				paginationList);
		return SUCCESS;
	}

	public void redReceiptBillPriintToExcel() throws IOException,
			RowsExceededException, WriteException {
		redReceiptApplyInfo.setDatastatus(DataUtil.BILL_STATUS_5);
		List resultsList = redReceiptApplyInfoService
				.findRedReceiptList(redReceiptApplyInfo);
		StringBuffer fileName = new StringBuffer("红冲打印");
		fileName.append(".xls");
		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, resultsList);
		os.flush();
		os.close();
	}

	/**
	 * 查看交易
	 */
	public String viewPrintTrans() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = new BillInfo();
			billInfo.setBillId(billId);
			redReceiptApplyInfoService
					.findTransByBillId(billId, paginationList);
			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0016", "发票开具", "销项税管理", "查看交易", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0016", "发票开具", "销项税管理", "查看交易", "0");
			log.error("BillIssueAction-cancelBill", e);
		}
		return ERROR;
	}

	/**
	 * @return 查看交易-红票开具
	 */
	public String viewIssueTrans() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = new BillInfo();
			billInfo.setBillId(billId);
			redReceiptApplyInfoService
					.findTransByBillId(billId, paginationList);
			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0016", "发票开具", "销项税管理", "查看交易", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0016", "发票开具", "销项税管理", "查看交易", "0");
			log.error("BillIssueAction-cancelBill", e);
		}
		return ERROR;
	}

	public String listRedReceipt() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			if (null == request.getParameter("msg")
					|| !request.getParameter("msg").equals("message")) {
				this.request.getSession().removeAttribute("message");
				this.message = "";
			}
			User currentUser = this.getCurrentUser();
			if (null != redReceiptApplyInfo
					&& redReceiptApplyInfo.getBillId() != null) {
				redReceiptApplyInfo = null;
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			redReceiptApplyInfo.setLstAuthInstId(lstAuthInstId);
			billInfoList = redReceiptApplyInfoService.findRedReceiptList(
					"redReceiptList", redReceiptApplyInfo, currentUser.getId(),
					paginationList);

			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲", "销项税管理", "查询可供进行红冲申请的票据信息列表", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲", "销项税管理", "查询可供进行红冲申请的票据信息列表", "0");
			log.error("BillInfoAction-listBillTrack", e);
		}
		return ERROR;
	}

	public void redReceiptBillToExcel() throws Exception {
		String type = request.getParameter("type");
		User currentUser = this.getCurrentUser();
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		redReceiptApplyInfo.setLstAuthInstId(lstAuthInstId);
		if (null != type && type.equals("redReceiptExcel")) {
			billInfoList = redReceiptApplyInfoService.findRedReceiptList(
					"redReceiptList", redReceiptApplyInfo, currentUser.getId(),
					null);
		} else if (null != type && type.equals("redReceiptApplyExcel")) {
			billInfoList = redReceiptApplyInfoService.findRedReceiptList(
					"findRedReceipt", redReceiptApplyInfo, currentUser.getId(),
					null);
		} else {
			billInfoList = redReceiptApplyInfoService.findRedReceiptList(
					"findRedReceiptApprove", redReceiptApplyInfo,
					currentUser.getId(), null);
		}
		List list = new ArrayList();
		try {
			BillInfo bill;
			for (int i = 0; i < billInfoList.size(); i++) {
				bill = new BillInfo();
				bill = billInfoService
						.findBillInfo1(((RedReceiptApplyInfo) billInfoList
								.get(i)).getBillId());
				ck: if (bill != null) {
					for (int k = 0; k < list.size(); k++) {
						if (((BillInfo) list.get(k)).getBillId().equals(
								bill.getBillId())) {
							break ck;
						}
					}
					list.add(bill);
				}
			}
			OutputStream os = response.getOutputStream();
			if (null != type && type.equals("redReceiptExcel")) {
				StringBuffer fileName = new StringBuffer("红冲发票信息表");
				fileName.append(".xls");
				String name = "attachment;filename="
						+ URLEncoder.encode(fileName.toString(), "UTF-8")
								.toString();
				response.setHeader("Content-type", "application/vnd.ms-excel");
				response.setHeader("Content-Disposition", name);
				redReceiptToExcel(os, list);
			} else if (null != type && type.equals("redReceiptApplyExcel")) {
				StringBuffer fileName = new StringBuffer("红冲申请信息表");
				fileName.append(".xls");
				String name = "attachment;filename="
						+ URLEncoder.encode(fileName.toString(), "UTF-8")
								.toString();
				response.setHeader("Content-type", "application/vnd.ms-excel");
				response.setHeader("Content-Disposition", name);
				redReceiptApproveToExcel(os, list);
			} else {
				StringBuffer fileName = new StringBuffer("红冲审核发票信息表");
				fileName.append(".xls");
				String name = "attachment;filename="
						+ URLEncoder.encode(fileName.toString(), "UTF-8")
								.toString();
				response.setHeader("Content-type", "application/vnd.ms-excel");
				response.setHeader("Content-Disposition", name);
				redReceiptApproveToExcel(os, list);
			}
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public void redReceiptApproveToExcel(OutputStream os, List content)
			throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);

		WritableSheet ws = null;
		ws = wb.createSheet("红冲审核发票信息表", 0);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "申请开票日期", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "开票日期", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "客户纳税人税号", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "发票代码", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "发票号码", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "开票人", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "税控盘号", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "开票机号", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "合计金额", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "合计税额", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "价税合计", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "是否手工录入", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "开具类型", JXLTool.getHeader());
		Label header16 = new Label(15, 0, "发票类型", JXLTool.getHeader());
		Label header17 = new Label(16, 0, "	状态", JXLTool.getHeader());

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
		// ws.addCell(header18);
		for (int i = 0; i < 18; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			BillInfo billInfo = (BillInfo) content.get(i);
			int column = count++;
			Map map = new HashMap();
			map.put("Id", Integer.valueOf(i + 1));
			map.put("applyBillDate", billInfo.getApplyDate());
			map.put("billDate", billInfo.getBillDate());
			map.put("customerName", billInfo.getCustomerName());
			map.put("customerTaxNo", billInfo.getCustomerTaxno());
			map.put("billCode", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("drawer", billInfo.getDrawer());
			map.put("taxDiskNo", billInfo.getTaxDiskNo());
			map.put("machineNo", billInfo.getMachineNo());
			map.put("amtSum", billInfo.getAmtSum());
			map.put("taxAmtSum", billInfo.getTaxAmtSum());
			map.put("sumAmt", billInfo.getSumAmt());
			map.put("isHandiwork", billInfo.getIsHandiwork());
			map.put("issueType", billInfo.getIssueType());
			map.put("fapiaoType", billInfo.getFapiaoType());
			map.put("dataStatus", billInfo.getDataStatus());

			setWritableSheetForRedReceiptApprove(ws, map, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheetForRedReceiptApprove(WritableSheet ws, Map o,
			int column) throws WriteException {

		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);

		Label cell1 = new Label(0, column, o.get("Id").toString(),
				tempCellFormat);

		// applyBillDate
		Label cell2 = new Label(1, column, o.get("applyBillDate") == null
				|| o.get("applyBillDate").equals("") ? "" : o.get(
				"applyBillDate").toString(), tempCellFormat);
		// billDate
		Label cell3 = new Label(2, column, o.get("billDate") == null
				|| o.get("billDate").equals("") ? "" : o.get("billDate")
				.toString(), tempCellFormat);
		// customerName
		Label cell4 = new Label(3, column, o.get("customerName") == null
				|| o.get("customerName").equals("") ? "" : o
				.get("customerName").toString(), tempCellFormat);
		// customerTaxNo
		Label cell5 = new Label(4, column, o.get("customerTaxNo") == null
				|| o.get("customerTaxNo").equals("") ? "" : o.get(
				"customerTaxNo").toString(), tempCellFormat);
		Label cell6 = new Label(5, column, o.get("billCode") == null
				|| o.get("billCode").equals("") ? "" : o.get("billCode")
				.toString(), tempCellFormat);
		Label cell7 = new Label(6, column,
				o.get("billNo") == null || o.get("billNo").equals("") ? "" : o
						.get("billNo").toString(), tempCellFormat);

		Label cell8 = new Label(7, column,
				o.get("drawer") == null || o.get("drawer").equals("") ? "" : o
						.get("drawer").toString(), tempCellFormat);
		Label cell9 = new Label(8, column, o.get("taxDiskNo") == null
				|| o.get("taxDiskNo").equals("") ? "" : o.get("taxDiskNo")
				.toString(), tempCellFormat);
		Label cell10 = new Label(9, column, o.get("machineNo") == null
				|| o.get("machineNo").equals("") ? "" : o.get("machineNo")
				.toString(), tempCellFormat);

		// amtSum
		Label cell11 = new Label(10, column,
				o.get("amtSum") == null || o.get("amtSum").equals("") ? "" : o
						.get("amtSum").toString(), tempCellFormat);
		// taxAmtSum
		Label cell12 = new Label(11, column, o.get("taxAmtSum") == null
				|| o.get("taxAmtSum").equals("") ? "" : o.get("taxAmtSum")
				.toString(), tempCellFormat);
		// taxAmtSum
		/*
		 * Label cell13 = new Label(12, column, o.get("taxRate") == null ||
		 * o.get("taxRate").equals("") ? "" : o.get("taxRate") .toString(),
		 * tempCellFormat);
		 */
		// sumAmt
		Label cell13 = new Label(12, column,
				o.get("sumAmt") == null || o.get("sumAmt").equals("") ? "" : o
						.get("sumAmt").toString(), tempCellFormat);

		String isHandiwork = "";
		if (o.get("isHandiwork") == null || o.get("isHandiwork").equals("")) {
			isHandiwork = "";
		} else {
			if (o.get("isHandiwork").equals("1")) {
				isHandiwork = "自动开票";
			} else if (o.get("isHandiwork").equals("2")) {
				isHandiwork = "人工审核";
			} else if (o.get("isHandiwork").equals("3")) {
				isHandiwork = "人工开票";
			} else {
				isHandiwork = "";
			}
		}
		Label cell14 = new Label(13, column, isHandiwork, tempCellFormat);

		String issueType = "";
		if (o.get("issueType") == null || o.get("issueType").equals("")) {
			issueType = "";
		} else {
			if (o.get("issueType").equals("1")) {
				issueType = "单笔";
			} else if (o.get("issueType").equals("2")) {
				issueType = "合并";
			} else if (o.get("issueType").equals("3")) {
				issueType = "拆分";
			} else {
				issueType = "";
			}
		}
		Label cell15 = new Label(14, column, issueType, tempCellFormat);

		// fapiaoType
		String fapiaoType = "";
		if (o.get("fapiaoType") == null || o.get("fapiaoType").equals("")) {
			fapiaoType = "";
		} else {
			if (o.get("fapiaoType").equals("1")) {
				fapiaoType = "专用发票";
			} else {
				fapiaoType = "普通发票";
			}
		}
		Label cell16 = new Label(15, column, fapiaoType, tempCellFormat);
		// dataStatus
		String dataStatus = "";
		if (o.get("dataStatus") == null || o.get("dataStatus").equals("")) {
			dataStatus = "";
		} else {
			if (o.get("dataStatus").equals("16")) {
				dataStatus = "申请待审核";
			} else if (o.get("dataStatus").equals("8")) {
				dataStatus = "已打印";
			} else if (o.get("dataStatus").equals("12")) {
				dataStatus = "已抄报";
			} else if (o.get("dataStatus").equals("17")) {
				dataStatus = "红冲已审核";
			} else if (o.get("dataStatus").equals("18")) {
				dataStatus = "已红冲";
			} else if (o.get("dataStatus").equals("19")) {
				dataStatus = "已回收";
			}
		}
		Label cell17 = new Label(16, column, dataStatus, tempCellFormat);

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
		// ws.addCell(cell18);
	}

	// 红冲导出
	public void redReceiptToExcel(OutputStream os, List content)
			throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);

		WritableSheet ws = null;
		ws = wb.createSheet("红冲发票信息表", 0);

		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "申请开票日期", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "开票日期", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "客户纳税人税号", JXLTool.getHeader());

		Label header6 = new Label(5, 0, "红字通知单号", JXLTool.getHeader());

		Label header7 = new Label(6, 0, "发票代码", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "发票号码", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "开票人", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "税控盘号", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "开票机号", JXLTool.getHeader());

		Label header12 = new Label(11, 0, "原始发票代码", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "原始发票号码", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "原始合计金额", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "原始合计税额", JXLTool.getHeader());
		Label header16 = new Label(15, 0, "原始税率", JXLTool.getHeader());
		Label header17 = new Label(16, 0, "原始价税合计", JXLTool.getHeader());

		Label header18 = new Label(17, 0, "合计金额", JXLTool.getHeader());
		Label header19 = new Label(18, 0, "合计税额", JXLTool.getHeader());
		// Label header20 = new Label(19, 0, "税率", JXLTool.getHeader());
		Label header20 = new Label(19, 0, "价税合计", JXLTool.getHeader());
		Label header21 = new Label(20, 0, "是否手工录入", JXLTool.getHeader());
		Label header22 = new Label(21, 0, "开具类型", JXLTool.getHeader());
		Label header23 = new Label(22, 0, "发票类型", JXLTool.getHeader());
		Label header24 = new Label(23, 0, "	状态", JXLTool.getHeader());

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
		ws.addCell(header19);
		ws.addCell(header20);
		ws.addCell(header21);
		ws.addCell(header22);
		ws.addCell(header23);
		ws.addCell(header24);
		// ws.addCell(header25);
		for (int i = 0; i < 26; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			BillInfo billInfo = (BillInfo) content.get(i);
			int column = count++;
			Map map = new HashMap();
			map.put("Id", Integer.valueOf(i + 1));
			map.put("applyBillDate", "");
			map.put("billDate", billInfo.getBillDate());
			map.put("customerName", billInfo.getCustomerName());
			map.put("customerTaxNo", billInfo.getCustomerTaxno());

			map.put("noticeNe", billInfo.getNoticeNo());

			map.put("billCode", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("drawer", billInfo.getDrawer());
			map.put("taxDiskNo", billInfo.getTaxDiskNo());
			map.put("machineNo", billInfo.getMachineNo());

			map.put("oriBillCode", billInfo.getOriBillCode());
			map.put("oriBillNo", billInfo.getOriBillNo());
			map.put("oriAmtSum", "");
			map.put("oritaxAmtSum", "");
			map.put("oriTaxRate", "");
			map.put("oriSumAmt", "");

			map.put("amtSum", billInfo.getAmtSum());
			map.put("taxAmtSum", billInfo.getTaxAmtSum());
			// map.put("taxRate", "");
			map.put("sumAmt", billInfo.getSumAmt());
			map.put("isHandiwork", billInfo.getIsHandiwork());
			map.put("issueType", billInfo.getIssueType());
			map.put("fapiaoType", billInfo.getFapiaoType());
			map.put("dataStatus", billInfo.getDataStatus());

			setWritableSheetForRedReceipt(ws, map, column);
		}
		wb.write();
		wb.close();
	}

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

	// 红冲导出
	private void setWritableSheetForRedReceipt(WritableSheet ws, Map o,
			int column) throws WriteException {

		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);

		Label cell1 = new Label(0, column, o.get("Id").toString(),
				tempCellFormat);

		// applyBillDate
		Label cell2 = new Label(1, column, o.get("applyBillDate") == null
				|| o.get("applyBillDate").equals("") ? "" : o.get(
				"applyBillDate").toString(), tempCellFormat);
		// billDate
		Label cell3 = new Label(2, column, o.get("billDate") == null
				|| o.get("billDate").equals("") ? "" : o.get("billDate")
				.toString(), tempCellFormat);
		// customerName
		Label cell4 = new Label(3, column, o.get("customerName") == null
				|| o.get("customerName").equals("") ? "" : o
				.get("customerName").toString(), tempCellFormat);
		// customerTaxNo
		Label cell5 = new Label(4, column, o.get("customerTaxNo") == null
				|| o.get("customerTaxNo").equals("") ? "" : o.get(
				"customerTaxNo").toString(), tempCellFormat);

		Label cell6 = new Label(5, column, o.get("noticeNe") == null
				|| o.get("noticeNe").equals("") ? "" : o.get("noticeNe")
				.toString(), tempCellFormat);

		Label cell7 = new Label(6, column, o.get("billCode") == null
				|| o.get("billCode").equals("") ? "" : o.get("billCode")
				.toString(), tempCellFormat);
		Label cell8 = new Label(7, column,
				o.get("billNo") == null || o.get("billNo").equals("") ? "" : o
						.get("billNo").toString(), tempCellFormat);

		Label cell9 = new Label(8, column,
				o.get("drawer") == null || o.get("drawer").equals("") ? "" : o
						.get("drawer").toString(), tempCellFormat);
		Label cell10 = new Label(9, column, o.get("taxDiskNo") == null
				|| o.get("taxDiskNo").equals("") ? "" : o.get("taxDiskNo")
				.toString(), tempCellFormat);
		Label cell11 = new Label(10, column, o.get("machineNo") == null
				|| o.get("machineNo").equals("") ? "" : o.get("machineNo")
				.toString(), tempCellFormat);

		Label cell12 = new Label(11, column, o.get("oriBillCode") == null
				|| o.get("oriBillCode").equals("") ? "" : o.get("oriBillCode")
				.toString(), tempCellFormat);
		Label cell13 = new Label(12, column, o.get("oriBillNo") == null
				|| o.get("oriBillNo").equals("") ? "" : o.get("oriBillNo")
				.toString(), tempCellFormat);
		Label cell14 = new Label(13, column, o.get("oriBillAmtSum") == null
				|| o.get("oriBillAmtSum").equals("") ? "" : o.get(
				"oriBillAmtSum").toString(), tempCellFormat);
		Label cell15 = new Label(14, column, o.get("oritaxAmtSum") == null
				|| o.get("oritaxAmtSum").equals("") ? "" : o
				.get("oritaxAmtSum").toString(), tempCellFormat);
		Label cell16 = new Label(15, column, o.get("oriTaxRate") == null
				|| o.get("oriTaxRate").equals("") ? "" : o.get("oriTaxRate")
				.toString(), tempCellFormat);
		Label cell17 = new Label(16, column, o.get("oriSumAmt") == null
				|| o.get("oriSumAmt").equals("") ? "" : o.get("oriSumAmt")
				.toString(), tempCellFormat);

		// amtSum
		Label cell18 = new Label(17, column,
				o.get("amtSum") == null || o.get("amtSum").equals("") ? "" : o
						.get("amtSum").toString(), tempCellFormat);
		// taxAmtSum
		Label cell19 = new Label(18, column, o.get("taxAmtSum") == null
				|| o.get("taxAmtSum").equals("") ? "" : o.get("taxAmtSum")
				.toString(), tempCellFormat);
		// taxAmtSum
		/*
		 * Label cell20 = new Label(19, column, o.get("taxRate") == null ||
		 * o.get("taxRate").equals("") ? "" : o.get("taxRate") .toString(),
		 * tempCellFormat);
		 */
		// sumAmt
		Label cell20 = new Label(19, column,
				o.get("sumAmt") == null || o.get("sumAmt").equals("") ? "" : o
						.get("sumAmt").toString(), tempCellFormat);

		String isHandiwork = "";
		if (o.get("isHandiwork") == null || o.get("isHandiwork").equals("")) {
			isHandiwork = "";
		} else {
			if (o.get("isHandiwork").equals("1")) {
				isHandiwork = "自动开票";
			} else if (o.get("isHandiwork").equals("2")) {
				isHandiwork = "人工审核";
			} else if (o.get("isHandiwork").equals("3")) {
				isHandiwork = "人工开票";
			} else {
				isHandiwork = "";
			}
		}
		Label cell21 = new Label(20, column, isHandiwork, tempCellFormat);

		String issueType = "";
		if (o.get("issueType") == null || o.get("issueType").equals("")) {
			isHandiwork = "";
		} else {
			if (o.get("issueType").equals("1")) {
				issueType = "单笔";
			} else if (o.get("issueType").equals("1")) {
				issueType = "合并";
			} else if (o.get("issueType").equals("1")) {
				issueType = "拆分";
			} else {
				issueType = "";
			}
		}
		Label cell22 = new Label(21, column, issueType, tempCellFormat);

		// fapiaoType
		String fapiaoType = "";
		if (o.get("fapiaoType") == null || o.get("fapiaoType").equals("")) {
			fapiaoType = "";
		} else {
			if (o.get("fapiaoType").equals("0")) {
				fapiaoType = "专用发票";
			} else {
				fapiaoType = "普通发票";
			}
		}
		Label cell23 = new Label(22, column, fapiaoType, tempCellFormat);
		// dataStatus
		String dataStatus = "";
		if (o.get("dataStatus") == null || o.get("dataStatus").equals("")) {
			dataStatus = "";
		} else {
			if (o.get("dataStatus").equals("17")) {
				dataStatus = "红冲已审核";
			}
		}
		Label cell24 = new Label(23, column, dataStatus, tempCellFormat);

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
		ws.addCell(cell19);
		ws.addCell(cell20);
		ws.addCell(cell21);
		ws.addCell(cell22);
		ws.addCell(cell23);
		ws.addCell(cell24);
		// ws.addCell(cell25);
	}

	public void exportToWord() throws UnsupportedEncodingException, IOException {
		List itemList = new ArrayList();
		BillItemInfo item = new BillItemInfo();

		String billId = request.getParameter("billId");
		Map map = new HashMap();
		item.setBillId(billId);
		itemList = billInfoService.findBillItemInfoList1(item);
		User currentUser = this.getCurrentUser();
		RedReceiptApplyInfo rrai = new RedReceiptApplyInfo();
		rrai.setBillId(billId);
		List list = billInfoService.findRedReceiptList(
				"findRedReceiptApprove1", rrai, currentUser.getId(),
				paginationList);
		redReceiptApplyInfo = (RedReceiptApplyInfo) list.get(0);
		map.put("searchCondition",
				"t.BILL_NO ='" + redReceiptApplyInfo.getOriBillNo()
						+ "' and t.BILL_CODE = '"
						+ redReceiptApplyInfo.getOriBillCode() + "'");
		List ticketList = billInfoService.findSpecialTicketById(map);
		if (null != ticketList && ticketList.size() > 0) {
			specialTicket = (SpecialTicket) ticketList.get(0);
		}

		map.put("tkDate", redReceiptApplyInfo.getBillDate());
		map.put("cancelName", redReceiptApplyInfo.getCustomerName());
		map.put("cancelTaxno", redReceiptApplyInfo.getCustomerTaxno());
		map.put("customerName", redReceiptApplyInfo.getName());
		map.put("customerTaxno", redReceiptApplyInfo.getTaxno());
		map.put("customerBillCode", redReceiptApplyInfo.getBillCode());
		map.put("customerBillNo", redReceiptApplyInfo.getBillNo());
		map.put("cancelBillCode", "");
		map.put("cancelBillNo", "");
		map.put("dealNo", "");
		map.put("buySellInd", specialTicket.getBuySellInd());
		map.put("level1Option", specialTicket.getLevel1Option());
		map.put("level2Option", specialTicket.getLevel2Option());
		map.put("billItemList", itemList);
		String fileName = vmsCommonService.createWord(map);

		// String classPath = this.getClass().getClassLoader().getResource("//")
		// .getPath();
		// String rootPath = classPath.replaceAll("WEB-INF/classes/", "");
		// String tplPath = rootPath + "template/bill";
		// String filePath = tplPath + "/outWord/" + fileName;
		// filePath = URLDecoder.decode(filePath, "utf-8");
		// this.setExportWordFileName(filePath);
		//
		//
		// return SUCCESS;

		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();

		response.setHeader("Content-type",
				"application/vnd.ms-word;charset=UTF-8");
		response.setHeader("Content-Disposition", name);
		response.setCharacterEncoding("UTF-8");

		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");// firefox浏览器
		} else {
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				fileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
			}
		}
		response.setContentType("text/plain");
		response.setHeader("Location", fileName);
		response.reset();
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;
		String classPath = this.getClass().getClassLoader().getResource("")
				.getPath();
		String rootPath = classPath.replaceAll("WEB-INF/classes/", "");
		String tplPath = rootPath + "/template/bill";
		String filePath = tplPath + "/outWord/" + fileName;
		fis = new FileInputStream(filePath);
		bis = new BufferedInputStream(fis);
		fos = response.getOutputStream();
		bos = new BufferedOutputStream(fos);
		int bytesRead = 0;
		byte[] buffer = new byte[5 * 1024];
		while ((bytesRead = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
		}
		System.out.println("*************************************" + fileName);
		bos.close();
		bis.close();
		fos.close();
		fis.close();
	}

	// xhy end

	public String cancelRedReceipt() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		String billId = request.getParameter("billId");
		BillInfo bill = redReceiptApplyInfoService.findBillInfo1(billId);

		
		if (null != bill) {
			String billCode = bill.getOriBillCode();
			String billNo = bill.getOriBillNo();
			if (StringUtil.isNotEmpty(billCode) && StringUtil.isNotEmpty(billNo)) {
				// bill.setDataStatus(DataUtil.BILL_STATUS_20);
				// billInfoService.saveBillInfo1(bill, true);
				billInfoService.updateBillStatus(billId, null, null,
						DataUtil.BILL_STATUS_20, null);
				billInfoService.updateBillStatus(null, billCode, billNo,
						DataUtil.BILL_STATUS_16, null);
				setResultMessages("撤销成功");
			} else {
				setResultMessages("撤销失败！未找到原始票据！");
			}
		}else{
			setResultMessages("撤销失败！票据已不存在！");
		}
		

		return SUCCESS;
	}

	/**
	 * 查看票样
	 */
	public String viewIssueSample() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = billTrackService.findBillInfoById(billId);

			BillItemInfo billItem = new BillItemInfo();
			billItem.setBillId(billId);
			List billItemList = billTrackService.findBillItemByBillId(billId);
			if (billItemList.size() > 9) {
				this.setRESULT_MESSAGE("发票最多包含9条数据。");
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
			// map.put("billPasswd",billInfo);
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

			// String imgName = vmsCommonService.createRedMark(map);
			String imgName = vmsCommonService.createMark(map);
			Map retMap = vmsCommonService.findCodeDictionary("BILL_IMG_PATH");

			filePath = retMap.get("BILL_IMG_PATH") + imgName;

			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0015", "发票红冲", "销项税管理", "查看票样", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0015", "发票红冲", "销项税管理", "查看票样", "0");
			// log.error("BillTrackAction-viewSample", e);
		}
		return ERROR;
	}

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("红冲打印列表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "申请开票日期", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "开票日期", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "发票代码", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "发票号码", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "开票人", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "税控盘号", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "开票机号", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "合计金额", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "合计税额", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "税率", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "价税合计", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "是否手工录入", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "开具类型", JXLTool.getHeader());
		Label header17 = new Label(i++, 0, "发票类型", JXLTool.getHeader());
		Label header18 = new Label(i++, 0, "状态", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header7);
		ws.addCell(header5);
		ws.addCell(header6);
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

		for (int j = 0; j < 18; j++) {
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			RedReceiptApplyInfo redReceiptApplyInfo = (RedReceiptApplyInfo) content
					.get(c);
			int column = count++;
			setWritableSheet(ws, redReceiptApplyInfo, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws,
			RedReceiptApplyInfo redReceiptApplyInfo, int column)
			throws WriteException {
		int i = 0;
		Label cell1 = new Label(i++, column,
				redReceiptApplyInfo.getApplyDate(), JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, redReceiptApplyInfo.getBillDate(),
				JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column,
				redReceiptApplyInfo.getCustomerName(),
				JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column,
				redReceiptApplyInfo.getCustomerTaxno(),
				JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, Integer.toString(column),
				JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, redReceiptApplyInfo.getBillCode(),
				JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, redReceiptApplyInfo.getBillNo(),
				JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, redReceiptApplyInfo.getDrawer(),
				JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column,
				redReceiptApplyInfo.getTaxDiskNo(), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column,
				redReceiptApplyInfo.getMachineNo(), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, redReceiptApplyInfo.getAmtSum()
				.toString(), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, redReceiptApplyInfo
				.getTaxAmtSum().toString(), JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, redReceiptApplyInfo
				.getTaxAmtSum().toString(), JXLTool.getContentFormat());
		Label cell14 = new Label(i++, column, redReceiptApplyInfo.getSumAmt()
				.toString(), JXLTool.getContentFormat());
		Label cell15 = new Label(
				i++,
				column,
				DataUtil.getIsHandiworkCH(redReceiptApplyInfo.getIsHandiwork()),
				JXLTool.getContentFormat());
		Label cell16 = new Label(i++, column,
				DataUtil.getIssueTypeCH(redReceiptApplyInfo.getIssueType()),
				JXLTool.getContentFormat());
		Label cell17 = new Label(i++, column,
				DataUtil.getFapiaoTypeCH(redReceiptApplyInfo.getFapiaoType()),
				JXLTool.getContentFormat());
		Label cell18 = new Label(i++, column, DataUtil.getDataStatusCH(
				redReceiptApplyInfo.getDatastatus(), "BILL"),
				JXLTool.getContentFormat());

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

	/**
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 *             导出红票开具
	 */
	public void redReceiptBillIssueToExcel() throws IOException,
			RowsExceededException, WriteException {
		redReceiptApplyInfo.setDatastatus(DataUtil.BILL_STATUS_3);
		List resultsList = redReceiptApplyInfoService
				.findRedReceiptList(redReceiptApplyInfo);
		StringBuffer fileName = new StringBuffer("红票开具列表");
		fileName.append(".xls");
		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeIssueToExcel(os, resultsList);
		os.flush();
		os.close();
	}

	public void writeIssueToExcel(OutputStream os, List content)
			throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("红票开具列表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "申请开票日期", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "开票日期", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "发票代码", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "发票号码", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "开票人", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "税控盘号", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "开票机号", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "合计金额", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "合计税额", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "价税合计", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "是否手工录入", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "开具类型", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "发票类型", JXLTool.getHeader());
		Label header17 = new Label(i++, 0, "状态", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header7);
		ws.addCell(header5);
		ws.addCell(header6);
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

		for (int j = 0; j < 17; j++) {
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			RedReceiptApplyInfo redReceiptApplyInfo = (RedReceiptApplyInfo) content
					.get(c);
			int column = count++;
			setWritIssueableSheet(ws, redReceiptApplyInfo, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritIssueableSheet(WritableSheet ws,
			RedReceiptApplyInfo redReceiptApplyInfo, int column)
			throws WriteException {
		int i = 0;
		Label cell1 = new Label(i++, column,
				redReceiptApplyInfo.getApplyDate(), JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, redReceiptApplyInfo.getBillDate(),
				JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column,
				redReceiptApplyInfo.getCustomerName(),
				JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column,
				redReceiptApplyInfo.getCustomerTaxno(),
				JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, Integer.toString(column),
				JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, redReceiptApplyInfo.getBillCode(),
				JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, redReceiptApplyInfo.getBillNo(),
				JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, redReceiptApplyInfo.getDrawer(),
				JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column,
				redReceiptApplyInfo.getTaxDiskNo(), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column,
				redReceiptApplyInfo.getMachineNo(), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, redReceiptApplyInfo.getAmtSum()
				.toString(), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, redReceiptApplyInfo
				.getTaxAmtSum().toString(), JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, redReceiptApplyInfo.getSumAmt()
				.toString(), JXLTool.getContentFormat());
		Label cell14 = new Label(
				i++,
				column,
				DataUtil.getIsHandiworkCH(redReceiptApplyInfo.getIsHandiwork()),
				JXLTool.getContentFormat());
		Label cell15 = new Label(i++, column,
				DataUtil.getIssueTypeCH(redReceiptApplyInfo.getIssueType()),
				JXLTool.getContentFormat());
		Label cell16 = new Label(i++, column,
				DataUtil.getFapiaoTypeCH(redReceiptApplyInfo.getFapiaoType()),
				JXLTool.getContentFormat());
		Label cell17 = new Label(i++, column, DataUtil.getDataStatusCH(
				redReceiptApplyInfo.getDatastatus(), "BILL"),
				JXLTool.getContentFormat());

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
	}

	/**
	 * 红票开具，查询注册码
	 * 
	 * @throws Exception
	 */

	public void getRegisteredInfo() throws Exception {
		String result = "";
		String lock = getInstLock();
		if (lock != null) {
			result = lock;
			printWriterResult(result);
			return;
		}
		String taxDiskNo = request.getParameter("taxDiskNo");
		String fapiaoType = request.getParameter("fapiaoType");

		// 查询空白发票作废数量
		Long invalidInvoiceNum = redReceiptApplyInfoService
				.findInvalidInvoiceCount("200", fapiaoType);
		this.request.setAttribute("invalidInvoiceNum", invalidInvoiceNum);

		// 获取注册码
		String registeredInfo = redReceiptApplyInfoService
				.findRegisteredInfo(taxDiskNo);
		if (registeredInfo == null) {
			result = "registeredInfoError";
			printWriterResult(result);
			return;
		}

		// 查询税控盘口令和证书口令
		TaxDiskInfo taxDiskInfo = redReceiptApplyInfoService
				.findTaxDiskInfoByTaxDiskNo(taxDiskNo);
		if (taxDiskInfo == null) {
			result = "taxPwdError";
			printWriterResult(result);
			return;
		}

		result = registeredInfo + "|" + taxDiskInfo.getTaxDiskPsw() + "|"
				+ taxDiskInfo.getTaxCertPsw() + "|" + fapiaoType;
		System.out.println("查询发票数量：" + result);
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
	 * 红票开具，拼写开具信息
	 * 
	 * @throws Exception
	 */
	public void issueRedBill() throws Exception {
		StringBuffer sb = new StringBuffer();
		String result = "";
		String preStr = request.getParameter("preStr");
		String billId = request.getParameter("billId");

		sb.append(preStr); // 注册码 + | + 发票类型

		// 获取作废的空白发票 ----需发票类型
		List invalidInvoiceList = redReceiptApplyInfoService
				.findInvalidPaperInvoice("200", "发票类型");

		sb.append("|" + (invalidInvoiceList.size() + 1)); // 发票数量

		// 空白作废发票信息
		for (int i = 0; i < invalidInvoiceList.size(); i++) {
			PaperInvoiceUseDetail invoice = (PaperInvoiceUseDetail) invalidInvoiceList
					.get(i);
			sb.append("|0^"); // 操作类型、开票类型
			sb.append("^" + invoice.getPaperInvoiceId()); // 发票代码
			sb.append("^"); // 购货单位名称
			sb.append("^"); // 购货单位纳税人识别号
			sb.append("^"); // 购货单位地址电话
			sb.append("^"); // 购货单位开户行及账号
			sb.append("^"); // 销货单位名称
			sb.append("^"); // 销货单位纳税人识别号
			sb.append("^"); // 销货单位地址电话
			sb.append("^"); // 销货单位开户行及账号
			sb.append("^"); // 合计金额
			sb.append("^"); // 合计税额
			sb.append("^"); // 收款人
			sb.append("^"); // 复核
			sb.append("^"); // 开票人
			sb.append("^" + invoice.getInvoiceCode()); // 原发票代码
			sb.append("^" + invoice.getInvoiceNo()); // 原发票号码
			sb.append("^"); // 通知单编号
			sb.append("^"); // 商品数量
			sb.append("^"); // 货物名称
			sb.append("~"); // 规格
			sb.append("~"); // 单位
			sb.append("~"); // 数量
			sb.append("~"); // 单价
			sb.append("~"); // 金额
			sb.append("~"); // 税率
			sb.append("~"); // 税额
		}

		// 查询需要红冲开具的发票
		BillInfo bill = redReceiptApplyInfoService.findBillInfoById(billId);
		sb.append("|1^1"); // 操作类型、开票类型
		sb.append("^" + bill.getBillId()); // 发票ID
		sb.append("^" + bill.getCustomerName()); // 购货单位名称
		sb.append("^" + bill.getCustomerTaxno()); // 购货单位纳税人识别号
		sb.append("^" + bill.getAddressandphone()); // 购货单位地址电话
		sb.append("^" + bill.getBankandaccount()); // 购货单位开户行及账号
		sb.append("^" + bill.getName()); // 销货单位名称
		sb.append("^" + bill.getTaxno()); // 销货单位纳税人识别号
		sb.append("^" + bill.getCustomerAddressandphone()); // 销货单位地址电话
		sb.append("^" + bill.getCustomerBankandaccount()); // 销货单位开户行及账号
		sb.append("^" + bill.getAmtSum()); // 合计金额
		sb.append("^" + bill.getTaxAmtSum()); // 合计税额
		sb.append("^" + bill.getPayee()); // 收款人
		sb.append("^" + bill.getReviewerName()); // 复核
		sb.append("^" + this.getCurrentUser().getName()); // 开票人
		sb.append("^" + bill.getOriBillCode()); // 原发票代码
		sb.append("^" + bill.getOriBillNo()); // 原发票号码
		sb.append("^" + bill.getNoticeNo()); // 通知单编号
		// 红冲开具发票商品信息
		List billItemList = redReceiptApplyInfoService
				.findBillItemByBillId(billId);
		if (billItemList == null || billItemList.size() == 0) {
			result = "billItemError";
			printWriterResult(result);
			return;
		} else if (billItemList.size() > 9) {
			result = "billItemNum";
			printWriterResult(result);
			return;
		}
		sb.append("^" + billItemList.size()); // 商品数量
		for (int j = 0; j < billItemList.size(); j++) {
			BillItemInfo item = (BillItemInfo) billItemList.get(j);
			sb.append("^" + item.getGoodsName()); // 货物名称
			sb.append("~" + item.getSpecandmodel()); // 规格
			sb.append("~" + item.getGoodsUnit()); // 单位
			sb.append("~" + item.getGoodsNo()); // 数量
			sb.append("~" + item.getGoodsPrice()); // 单价
			sb.append("~" + item.getAmt()); // 金额
			sb.append("~" + item.getTaxRate()); // 税率
			sb.append("~" + item.getTaxAmt()); // 税额
			sb.append("~" + "0"); // 折扣item.getDiscountRate()
		}
		result = sb.toString();
		System.out.println("红冲开具：" + result);
		printWriterResult(result);
	}

	// 获取锁
	private String getInstLock() {
		String instCode = this.getCurrentUser().getOrgId();
		boolean lock = ThreadLock.getLockState(instCode);
		if (lock) {
			return "lock";
		}
		return null;
	}

	/**
	 * 更新红票开具结果
	 * 
	 * @throws Exception
	 */
	public void updateIssueRedBillResult() throws Exception {
		String issueRes = request.getParameter("issueRes");
		String[] result = issueRes.split("\\|");
		for (int i = 3; i < result.length; i++) { // 测试有OK!,从3开始
			String[] bills = result[i].split("\\^");
			// 区分开具、空白作废发票
			BillInfo bill = new BillInfo();
			bill = redReceiptApplyInfoService.findBillInfoById(bills[2]);

			// 空白作废发票更改状态,继续循环
			if (bill == null) {
				if ("0".equals(bills[0])) {
					PaperInvoiceUseDetail invalidInvoice = new PaperInvoiceUseDetail();
					invalidInvoice.setPaperInvoiceId(bills[2]);
					invalidInvoice.setInvoiceCode(bills[3]);
					invalidInvoice.setInvoiceNo(bills[4]);
					invalidInvoice.setInvoiceStatus("");
					redReceiptApplyInfoService
							.updatePaperInvoiceStatus(invalidInvoice);
				}
				continue;
			}
			// 开具成功，更改发票信息及交易信息，开具失败，更改状态
			String dataStatus = bill.getDataStatus();
			String issueType = bill.getIssueType();
			BillInfo issueBill = new BillInfo();
			issueBill.setBillId(bills[2]);
			if ("0".equals(bills[0])) {
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 设置日期格式
				issueBill.setDataStatus(DataUtil.BILL_STATUS_5);
				issueBill.setTaxDiskNo(bills[1]);
				issueBill.setBillCode(bills[3]);
				issueBill.setBillNo(bills[4]);
				issueBill.setBillDate(bills[5] + " " + df.format(new Date()));
				issueBill.setMachineNo(bills[6]);
				issueBill.setDrawer(this.getCurrentUser().getId());
				redReceiptApplyInfoService.updatebillInfoIssueResult(issueBill);
				// 更改交易状态
				if ("3".equals(issueType)) {
					// 票据为拆分而来，更改交易状态
				} else {
					redReceiptApplyInfoService.updateTransInfoStatus("99",
							bills[2]);
				}
			} else {
				if (!"7".equals(dataStatus)) {
					bill.setDataStatus(DataUtil.BILL_STATUS_7);
					redReceiptApplyInfoService.updateBillInfoStatus(issueBill);
				}
			}
		}

		// 释放锁
		releaseInstLock();
		printWriterResult("红冲开具成功！");
	}

	/**
	 * 拼接打印发票需要传进税控盘中的参数
	 * 
	 * @throws IOException
	 */
	public String showRedLock() throws IOException {
		String result = "";

		try {
			result = getInstLock();

			// 将结果写出
			this.response.setContentType("application/json;charset=UTF-8");
			this.response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		releaseInstLock();
		return null;
	}
	
	
	/**
	 * 打印红冲发票
	 * @return
	 */
	public String printAisinoRedInvoice(){
		try {
			String billIds = (String) request.getParameter("billIds");
			billInfo = billInfoService.findBillById(billIds);
			String sendStr = getAisinoRedstring(billInfo);
			System.out.println(sendStr);
			
			this.response.setContentType("application/json;charset=UTF-8");
			try{
				String result = HxServiceFactory.createHxInvoiceService().printInvoice(sendStr.toString());
				if(isSuccess(result)){
					billInfoService.updateBillByBillId(billIds, "8");
					releaseInstLock();
					// 将结果写出
					this.response.getWriter().print("success");
				}
			}catch(Exception e){
				this.response.getWriter().print(e.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("打印红冲发票出错!");
		} finally {
	
		}
		return null;
	}
	
	/**
	 * 根据红冲发票信息，获取发票打印信息
	 * @param billInfo 发票信息
	 * @return
	 */
	private String getAisinoRedstring(BillInfo billInfo){
		String ip = "";
		String port = "";
		int count = 1;
		
		StringBuffer sendStr = new StringBuffer();
		sendStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sendStr.append("<service>");
		sendStr.append("	<sid>2</sid>");//请求类型：发票打印
		sendStr.append("	<ip></ip>");//客户端IP地址
		sendStr.append("	<port></port>");//客户端端口号
		
		sendStr.append("	<data count=").append(count).append(">");
		for(int i=0;i<count;i++){
			sendStr.append("	<record>");
			sendStr.append("		<FPZL>").append(billInfo.getBillId()).append("</FPZL>");
			sendStr.append("		<FPHM>").append(billInfo.getBillNo()).append("</FPHM>");
			sendStr.append("		<FPDM>").append(billInfo.getBillCode()).append("</FPDM>");
			sendStr.append("	</record>");
		}
		sendStr.append("	</data>");
		sendStr.append("	</data>");
		sendStr.append("</service>");
		return sendStr.toString();
	}
	
	/**
	 * 根据发票打印结果信息，判断是否打印成功
	 * 不成功则直接抛出异常
	 * @param result
	 * @return
	 */
	private boolean isSuccess(String result)throws Exception{
		Document doc = XmlFunc.getDocument(result);
		Element root = doc.getDocumentElement();
		String errorCode = XmlFunc.getNodeValue(root, "RETCODE", "-2");
		if("0".equals(errorCode)){
			return true;
		}else if("-1".equals(errorCode)){
			throw new RuntimeException("控制台异常信息");
		}else if("-2".equals(errorCode)){
			throw new RuntimeException("连接控制台异常！");
		}else if("-3".equals(errorCode)){
			throw new RuntimeException("控制台ip或端口号为空！");
		}
		return false;
	}
	/**
	 * 发票打印返回的XML格式
	 * RETCODE  
	 * -1	控制台异常信息
	 * -2	连接控制台异常！
	 * -3	控制台ip或端口号为空！
	 * 0	操作成功
	<?xml version="1.0" encoding="UTF-8"?>
	<service>
		<err count="1">
			<refp>
				<RETCODE></RETCODE>
				<RETMSG></RETMSG>
				<FPZL></FPZL>
				<FPHM></FPHM>
				<FPDM></FPDM>
			</refp>
			<refp>...</refp>
		</err>
	</service>
	 */
	
	/**
	 <?xml version="1.0" encoding="UTF-8"?>
	 <service>
		<sid>2</sid>
		<ip></ip>
		<port></port>
		<data count="2">
			<record>
				<FPZL></FPZL>
				<FPHM></FPHM>
				<FPDM></FPDM>
			</record>
			<record>...</record>
		</data>
	 </service>
	 */
	/**
	 * 生成符合航信接口的发票打印的XML字符串
	 * @return
	 */
	public String showAisinoRedstring(){
		try {
			String billIds = (String) request.getParameter("billIds");
			billInfo = billInfoService.findBillById(billIds);
			String ip = "";
			String port = "";
			int count = 1;
			
			StringBuffer sendStr = new StringBuffer();
			sendStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sendStr.append("<service>");
			sendStr.append("	<sid>2</sid>");//请求类型：发票打印
			sendStr.append("	<ip></ip>");//客户端IP地址
			sendStr.append("	<port></port>");//客户端端口号
			
			sendStr.append("	<data count=").append(count).append(">");
			for(int i=0;i<count;i++){
				sendStr.append("	<record>");
				sendStr.append("		<FPZL>").append(billInfo.getBillId()).append("</FPZL>");
				sendStr.append("		<FPHM>").append(billInfo.getBillNo()).append("</FPHM>");
				sendStr.append("		<FPDM>").append(billInfo.getBillCode()).append("</FPDM>");
				sendStr.append("	</record>");
			}
			sendStr.append("	</data>");
			sendStr.append("	</data>");
			sendStr.append("</service>");
			System.out.println(sendStr.toString());
			// 将结果写出
			this.response.setContentType("application/json;charset=UTF-8");
			this.response.getWriter().print(sendStr.toString());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("ajax-拼接传给税控盘的字符串出错");
		} finally {

		}
		return null;
	}

	/**
	 * @return 红冲打印字符处理
	 */
	public String showOCXRedstring() {
		try {
			String billIds = (String) request.getParameter("billIds");
			StringBuffer sbInner = new StringBuffer();
			StringBuffer billPrintId = new StringBuffer();
			billInfo = billInfoService.findBillById(billIds);

			// ：注册码|发票类型(0:专,1:普)|发票数量|发票ID^发票代码^发票号码| 红色部分为循环域
			sbInner.append(billInfo.getBillId()).append("^")
					.append(billInfo.getBillCode()).append("^")
					.append(billInfo.getBillNo());
			billPrintId.append(billInfo.getBillId());

			String result = sbInner.toString() + "+" + billPrintId;
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

	public String[] getSelectBillIds() {
		return selectBillIds;
	}

	public void setSelectBillIds(String[] selectBillIds) {
		this.selectBillIds = selectBillIds;
	}

	public String getExportWordFileName() {
		return exportWordFileName;
	}

	public void setExportWordFileName(String exportWordFileName) {
		this.exportWordFileName = exportWordFileName;
	}

	public InputStream getWordFileStream() {
		InputStream is = ServletActionContext
				.getServletContext()
				.getResourceAsStream(
						"C:/GreenSoft/apache-tomcat-6.0.44/webapps/vmss/template/bill/outWord/1457608953319.doc");
		return is;
	}

	public void setWordFileStream(InputStream wordFileStream) {
		this.wordFileStream = wordFileStream;
	}

	/**
	 * @return 【红冲申请】页面数据查询
	 */
	public String listRedReceiptApply() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			if (null == request.getParameter("msg")
					|| !request.getParameter("msg").equals("message")) {
				this.request.getSession().removeAttribute("message");
				this.message = "";
			}
			if (null != request.getParameter("back")
					&& request.getParameter("back").equals("back")) {
				redReceiptApplyInfo = new RedReceiptApplyInfo();
			}

			String strFromViewFlg = request.getParameter("fromViewFlg");
			if ("first".equalsIgnoreCase(strFromViewFlg)) {
				paginationList.setCurrentPage(1);
				paginationList.setPageSize(20);
			}
			User currentUser = this.getCurrentUser();
			if (null != redReceiptApplyInfo
					&& redReceiptApplyInfo.getBillId() != null) {
				redReceiptApplyInfo = null;
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			/*redReceiptApplyInfo = new RedReceiptApplyInfo();*/
			redReceiptApplyInfo.setLstAuthInstId(lstAuthInstId);
			billInfoList = redReceiptApplyInfoService.findRedReceiptApplyList(
					"findRedReceipt", redReceiptApplyInfo, currentUser.getId(),
					paginationList);

			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲申请", "销项税管理", "查询可供进行红冲申请的票据信息列表", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲", "销项税管理", "查询可供进行红冲申请的票据信息列表", "0");
			log.error("BillInfoAction-listBillTrack", e);
		}
		return ERROR;
	}

	/*
	 * // 【交易发票关联】页面数据查询 public String billInfoAndTransList() { if
	 * (!sessionInit(true)) { request.setAttribute("message", "用户失效"); return
	 * ERROR; } String billId = request.getParameter("billId"); flag =
	 * request.getParameter("ticket"); redReceiptApplyInfo.setBillId(billId);
	 * List lstAuthInstId = new ArrayList();
	 * this.getAuthInstList(lstAuthInstId);
	 * redReceiptApplyInfo.setLstAuthInstId(lstAuthInstId); billInfoList =
	 * redReceiptApplyInfoService
	 * .findRedReceiptApplyList("findRedReceipt",redReceiptApplyInfo, "",
	 * paginationList); redReceiptApplyInfo=null;
	 * redReceiptApplyInfo=(RedReceiptApplyInfo) billInfoList.get(0); //
	 * redReceiptApplyInfo = redReceiptApplyInfoService.findByBillId(billId);
	 * String type = request.getParameter("type"); paginationList=new
	 * PaginationList(); if ("first".equalsIgnoreCase(fromFlag)) {
	 * paginationList.setCurrentPage(1); paginationList.setPageSize(20);
	 * fromFlag = null; } if (null != type) { if (type.equals("all")) {
	 * request.setAttribute("type", "all");
	 * redReceiptApplyInfoService.findRedReceiptTrans(billId, paginationList); }
	 * else if (type.equals("part")) { request.setAttribute("type", "part");
	 * String strIds = request.getParameter("ids"); List billInfoList =
	 * redReceiptApplyInfoService.findRedReceiptTrans(billId,
	 * paginationList,strIds);
	 * 
	 * paginationList.setRecordList(billInfoList);
	 * paginationList.setRecordCount(paginationList.getRecordCount()); } else if
	 * (type.equals("del")) { List billInfoList =
	 * paginationList.getRecordList(); String[] ids =
	 * request.getParameter("ids").split(","); RedReceiptTransInfo rrti; for
	 * (int i = 0; i < ids.length; i++) { if (null != ids[i] &&
	 * !ids[i].equals("")) { for (int j = 0; j < billInfoList.size(); j++) {
	 * rrti = (RedReceiptTransInfo) billInfoList.get(j); if
	 * (rrti.getTransId().equals(ids[i])) { billInfoList.remove(i - 1); } } } }
	 * paginationList.setRecordList(billInfoList);
	 * paginationList.setRecordCount(billInfoList.size()); } else {
	 * paginationList.setRecordCount(0); paginationList.setRecordList(null); } }
	 * else { paginationList.setRecordCount(0);
	 * paginationList.setRecordList(null); } return SUCCESS; }
	 */

	// 【交易发票关联】页面数据查询
	public String billInfoAndTransList() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}

		String billId = request.getParameter("billId");
		flag = request.getParameter("ticket");
		redReceiptApplyInfo.setBillId(billId);
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		redReceiptApplyInfo.setLstAuthInstId(lstAuthInstId);
		billInfoList = redReceiptApplyInfoService.findRedReceiptApplyList(
				"findRedReceipt", redReceiptApplyInfo, "", paginationList);
		redReceiptApplyInfo = null;
		redReceiptApplyInfo = (RedReceiptApplyInfo) billInfoList.get(0);
		// redReceiptApplyInfo =
		// redReceiptApplyInfoService.findByBillId(billId);
		String type = request.getParameter("type");

		fromFlag = null;

		request.setAttribute("type", "all");
		redReceiptApplyInfoService.findRedReceiptTrans(billId, paginationList);

		return SUCCESS;
	}

	/**
	 * 【红冲申请】页面[查看交易]
	 * 
	 * @return
	 */
	public String RedReceiptApplyToTrans() {
		try {
			billInfo.setBillId((String) this.request.getParameter("billId"));
			// this.request.getSession().setAttribute("curPage",
			// Integer.valueOf(this.getCurPage()));

			listFlg = (String) request.getParameter("listFlg");

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-billEditToTrans", e);
		}
		return ERROR;
	}

	/**
	 * 【红冲审核】页面[查看交易]
	 * 
	 * @return
	 */
	public String RedReceiptApplyToTrans1() {
		try {
			billInfo.setBillId((String) this.request.getParameter("billId"));
			// this.request.getSession().setAttribute("curPage",
			// Integer.valueOf(this.getCurPage()));

			listFlg = (String) request.getParameter("listFlg");

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-billEditToTrans", e);
		}
		return ERROR;
	}

	public String listBillTransInfo() {
		try {
			if ("first".equalsIgnoreCase(fromFlag)) {
				paginationList.setCurrentPage(1);
				paginationList.setPageSize(20);
				fromFlag = null;
			}
			String billId = (String) this.request.getParameter("billId");
			request.setAttribute("billId", billId);

			// String strListFlg=(String)request.getParameter("listFlg");
			// request.setAttribute("listFlg", strListFlg);
			//
			transInfoList = billTrackService.findTransByBillId(billId,
					paginationList);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoDetailAction-listBillTransInfo", e);
		}
		return ERROR;
	}

	// // 【发票红冲】页面数据查询
	// public String redReceiptTransList() {
	// String billId = request.getParameter("billId");
	// // if (null != paginationList.getRecordList()) {
	// // List tempList = paginationList.getRecordList();
	// // redReceiptApplyInfoService.findRedReceiptTrans(billId,
	// paginationList);
	// // List list = paginationList.getRecordList();
	// // RedReceiptTransInfo all;
	// // RedReceiptTransInfo part;
	// // for (int i = 0; i < list.size(); i++) {
	// // for (int j = 0; j < tempList.size(); j++) {
	// // all = (RedReceiptTransInfo) list.get(i);
	// // part = (RedReceiptTransInfo) tempList.get(j);
	// // if (all.getBillId().equals(part.getBillId())) {
	// // list.remove(i);
	// // }
	// // }
	// // }
	// // paginationList.setRecordList(list);
	// // paginationList.setRecordCount(list.size());
	// // } else {
	// // redReceiptApplyInfoService.findRedReceiptTrans(billId,
	// paginationList);
	// // }
	//
	// redReceiptApplyInfoService.findRedReceiptTrans(billId, paginationList);
	//
	// return SUCCESS;
	// }

	public String viewTransList() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = new BillInfo();
			billInfo.setBillId(billId);
			redReceiptApplyInfoService
					.findTransByBillId(billId, paginationList);
			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0016", "发票开具", "销项税管理", "查看交易", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0016", "发票开具", "销项税管理", "查看交易", "0");
			log.error("BillIssueAction-cancelBill", e);
		}
		return ERROR;
	}

	// 【红冲审核】页面数据查询
	public String listRedReceiptApprove() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			if (null == request.getParameter("msg")
					|| !request.getParameter("msg").equals("message")) {
				this.request.getSession().removeAttribute("message");
				this.message = "";
			}
			User currentUser = this.getCurrentUser();
			if (null != redReceiptApplyInfo
					&& redReceiptApplyInfo.getBillId() != null) {
				redReceiptApplyInfo = null;
			}
			String strFromViewFlg = request.getParameter("fromViewFlg");
			if ("first".equalsIgnoreCase(strFromViewFlg)) {
				paginationList.setCurrentPage(1);
				paginationList.setPageSize(20);
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			redReceiptApplyInfo.setLstAuthInstId(lstAuthInstId);
			billInfoList = redReceiptApplyInfoService.findRedReceiptList(
					"findRedReceiptApprove", redReceiptApplyInfo,
					currentUser.getId(), paginationList);

			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲申请", "销项税管理", "查询可供进行红冲申请的票据信息列表", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲", "销项税管理", "查询可供进行红冲申请的票据信息列表", "0");
			log.error("BillInfoAction-listBillTrack", e);
		}
		return ERROR;
	}

	// 【红冲审核】页面[审核通过],[审核拒绝]
	public String redReceiptApprove() throws Exception {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		String billId = request.getParameter("billId");
		String[] ids = billId.split(",");
		String result = request.getParameter("result");
		BillInfo bill;
		if (result.equals("17")) {
			for (int i = 0; i < ids.length; i++) {
				bill = new BillInfo();
				if (!ids[i].equals("") && ids[i] != null) {
					bill = redReceiptApplyInfoService.findBillInfo1(ids[i]);
					if (bill == null) {
						request.setAttribute("message", "数据错误");
						return ERROR;
					}

					bill.setCancelAuditor(getCurrentUser().getId());
					// 原bill状态：[17：红冲已审核]
					bill.setDataStatus(DataUtil.BILL_STATUS_17);
					redReceiptApplyInfoService.saveBillInfo1(bill, true);
					// 红冲bill状态：[21：红冲审核已通过]
					bill.setDataStatus(DataUtil.BILL_STATUS_21);
					redReceiptApplyInfoService.updateRedBill(bill);
				}
			}
			this.message = "审核成功";
			this.request.setAttribute("message", this.message);
		} else {
			String cancelReason = request.getParameter("cancelReason");
			cancelReason = URLDecoder.decode(cancelReason, "utf-8");
			cancelReason = URLDecoder.decode(cancelReason, "utf-8");
			for (int i = 0; i < ids.length; i++) {
				bill = new BillInfo();
				if (!ids[i].equals("") && ids[i] != null) {
					bill = redReceiptApplyInfoService.findBillInfo1(ids[i]);
					if (bill == null) {
						request.setAttribute("message", "数据错误");
						return ERROR;
					}
					Map map = new HashMap();
					map.put("searchCondition", "t.DATASTATUS in (16)");
					RedReceiptApplyInfo rrai = redReceiptApplyInfoService
							.findListByBillId(ids[i], map);
					if (null != rrai && rrai.getFapiaoType().equals("0")) {
						redReceiptApplyInfoService.deleteApplyInfo(bill
								.getBillNo());
					}
					bill.setCancelInitiator("");
					bill.setDataStatus(bill.getOperateStatus());
					bill.setOperateStatus("");
					bill.setCancelReason(cancelReason);
					bill.setBalance(bill.getSumAmt());
					redReceiptApplyInfoService.saveBillInfo1(bill, true);
					// 删除billInfo
					Map dataMap = new HashMap();
					dataMap.put("oriBillCode", bill.getBillCode());
					dataMap.put("oriBillNo", bill.getBillNo());
					List list = redReceiptApplyInfoService
							.findReleaseTrans(dataMap);
					//删除VMS_HC_APPLY_INFO
					redReceiptApplyInfoService.deleteApplyInfo(bill.getBillCode(), bill.getBillNo());
					BillItemInfo bii;
					BillItemInfo tempItem = new BillItemInfo();
					RedReceiptTransInfo rrti = (RedReceiptTransInfo) list
							.get(0);
					redReceiptApplyInfoService.deleteBillInfo(rrti.getBillId());
					tempItem.setBillId(rrti.getBillId());
					List itemList = redReceiptApplyInfoService
							.findBillItemInfoList(tempItem);
					for (int j = 0; j < itemList.size(); j++) {
						bii = (BillItemInfo) itemList.get(j);
						redReceiptApplyInfoService.deleteBillItemInfo(
								rrti.getBillId(), bii.getBillItemId());
					}
					for (int k = 0; k < list.size(); k++) {
						rrti = (RedReceiptTransInfo) list.get(k);
						transInfoService.deleteTransBill(rrti.getTransId(),
								rrti.getBillId());
					}
				}
			}
			this.message = "成功拒绝申请！";
			this.setResultMessages(message);
			printWriterResult("sucess");
		}

		return SUCCESS;
	}

	public String toRedReceiptRefuse() {
		request.setAttribute("billId", request.getParameter("billId"));
		request.setAttribute("result", request.getParameter("result"));
		return SUCCESS;
	}

	// 【交易发票关联】页面[红冲申请]
	public String redReceiptApply() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		// String billId = request.getParameter("billId");
		BillInfo cancelBill = redReceiptApplyInfoService.findBillInfo1(billId);
		if (cancelBill == null) {
			request.setAttribute("message", "数据错误");
			return ERROR;
		}

		// String type = request.getParameter("type");
		/*
		 * if (type.equals("all")) { request.setAttribute("type", "all");
		 * redReceiptApplyInfoService.findRedReceiptTrans(billId,
		 * paginationList); } else if (type.equals("part")) {
		 * request.setAttribute("type", "part"); String strIds =
		 * request.getParameter("ids"); List billInfoList =
		 * redReceiptApplyInfoService.findRedReceiptTrans( billId,
		 * paginationList, strIds);
		 * 
		 * paginationList.setRecordList(billInfoList);
		 * paginationList.setRecordCount(paginationList.getRecordCount()); }
		 */

		request.setAttribute("type", "part");
		// String strIds = request.getParameter("ids");

		List list = redReceiptApplyInfoService.findRedReceiptTransByIds(billId,
				null, selectBillIds);
		// 校验是否存在冲账交易 存在时 使用冲账金额开票
		for (int i = 0; i < list.size(); i++) {
			RedReceiptTransInfo r = (RedReceiptTransInfo) list.get(i);
			if (StringUtil.isNotEmpty(r.getReverseTransId())) {
				r.setAmtCny(r.getReverseAmtCny());
				r.setIncomeCny(r.getReverseIncomeCny());
				r.setTaxAmtCny(r.getReverseTaxAmtCny());
			}
		}
		// 根据选中的transBill,逐条生成新transBill,合并新billItem及新bill
		this.transBillMergeOrSplit(list);

		// 获取所有红冲后的新bill
		List redBill = redReceiptApplyInfoService
				.findRedBillByOriBillId(billId);
		BillInfo redBillInfo;
		// 合计金额净额
		BigDecimal redAmtSum = new BigDecimal("0");// 净价
		BigDecimal redSumAmt = new BigDecimal("0");// 价税合计

		for (int i = 0; i < redBill.size(); i++) {
			redBillInfo = (BillInfo) redBill.get(i);
			redAmtSum = redAmtSum.add(redBillInfo.getAmtSum());
			redSumAmt = redSumAmt.add(redBillInfo.getSumAmt());
		}
		// 获取原bill
		// System.out.println("*********************************" + billId);
		BillInfo bill = redReceiptApplyInfoService.findBillInfo1(billId);
		// 设定Balance:原balance + 红冲掉的amtSum(后者已为负值)

		bill.setBalance(bill.getBalance() == null ? bill.getSumAmt().add(
				redSumAmt) : bill.getBalance().add(redSumAmt));
		bill.setOperateStatus(bill.getDataStatus());
		// 原billinfo状态：[16：红冲待审核]
		bill.setDataStatus(DataUtil.BILL_STATUS_16);
		bill.setCancelInitiator(getCurrentUser().getId());
		bill.setCancelReason(null);
		// 更新原billInfo
		redReceiptApplyInfoService.saveBillInfo1(bill, true);
		// 保存申请
		if (cancelBill.getFapiaoType().equals("0") && null != specialTicket) {
			specialTicket.setBillNo(bill.getBillNo());
			specialTicket.setBuySellInd("1");
			specialTicket.setFapiaoType("0");
			// specialTicket.setLevel1Option("0");
			specialTicket.setBillCode(bill.getBillCode());
			redReceiptApplyInfoService.saveSpecialTicket(specialTicket);

		}

		this.message = "红冲成功";
		this.setResultMessages(message);
		redReceiptApplyInfo = null;
		return SUCCESS;
	}

	/*
	 * 将选中的transBillList依据以下原则合并成新billItem,bill
	 * 原billItemId相同，且合计税额误差小于峰值可合并为新billItem 8个以下新billItem可合并为一个新bill
	 */
	public String transBillMergeOrSplit(List selTransList) {
		// 初始值为首个transBill的transId
		String transIds = "";
		// 定义临时变量（用于临时储存循环过程中各transBill）
		RedReceiptTransInfo indexTransBill;
		// 获取首个transBill
		RedReceiptTransInfo firstTransBill = (RedReceiptTransInfo) selTransList
				.get(0);
		// 获取首个transBill：BillItemId
		String billItemId = firstTransBill.getBillItemId();
		// 获取首个transBill：VMS_TRANS_BILL的金额_人民币
		BigDecimal amtCny = firstTransBill.getAmtCny();
		// 获取首个transBill：VMS_TRANS_BILL的税额_人民币
		BigDecimal taxAmtCny = firstTransBill.getTaxAmtCny();
		// 获取首个transBill：VMS_BILL_ITEM_INFO的税率
		BigDecimal taxRate = firstTransBill.getTaxRate();

		// // transBill：BillItemId
		// String billItemId = "";
		// // transBill：VMS_TRANS_BILL的金额_人民币
		// BigDecimal amtCny = new BigDecimal(0.0);
		// // transBill：VMS_TRANS_BILL的税额_人民币
		// BigDecimal taxAmtCny = new BigDecimal(0.0);
		// // transBill：VMS_BILL_ITEM_INFO的税率
		// BigDecimal taxRate = firstTransBill.getTaxRate();
		// 获取原billId
		String strOldBillId = firstTransBill.getBillId();

		// 要生成同一个billItem的transIds
		// 初始值为首个transBill的transId
		transIds = firstTransBill.getTransId();
		// transBillList
		List transBillList = new ArrayList();
		transBillList.add(firstTransBill);
		// 实际税额
		BigDecimal taxAmt = new BigDecimal(0.0);
		// billItemNum
		int billItemNum = 1;

		// 生成新billId
		String strBillId = createBillId("B");// 票据ID
		// // 生成新billItemId
		String strBillItemId = createBusinessId("BI");

		// 循环处理选中transBill
		for (int i = 0; i < selTransList.size(); i++) {

			// 获取临时transBill
			indexTransBill = (RedReceiptTransInfo) selTransList.get(i);
			// 当前transBill的billItemId与前者不同，则可生成新billItem
			boolean isSameItemId = billItemId.equalsIgnoreCase(indexTransBill
					.getBillItemId());
			boolean isFirst = i == 0 ? true : false;
			boolean isLast = selTransList.size() - i == 1 ? true : false;

			// 如果只选了一条交易信息直接插入
			if (isFirst) {
				if (isLast) {
					// 保存新数据
					this.createRedBill(strOldBillId, strBillId, strBillItemId,
							transBillList);
					continue;
				} else {
					continue;
				}

			}

			if (!isSameItemId) {
				// // billItemNum达到峰值
				// if (billItemNum == 8) {
				// // 生成新bill
				// strBillId = createBillId("B");// 票据ID
				// billItemNum = 1;
				// } else {
				// // billItemNum累加1
				// billItemNum++;
				// }

				// 保存新数据
				this.createRedBill(strOldBillId, strBillId, strBillItemId,
						transBillList);

				// 将当前transBill的billItemId作为新的对比对象
				billItemId = indexTransBill.getBillItemId();
				// 记录当前transBill的transId
				transIds = indexTransBill.getTransId();

				transBillList = new ArrayList();
				transBillList.add(indexTransBill);
				// 记录当前税率
				taxRate = indexTransBill.getTaxRate();
				// 各金额清空
				amtCny = indexTransBill.getAmtCny();
				taxAmtCny = indexTransBill.getTaxAmtCny();

				// 生成新billItemId
				strBillItemId = createBusinessId("BI");

				if (isLast) {
					this.createRedBill(strOldBillId, strBillId, strBillItemId,
							transBillList);
				}
			}
			// 相同，则进行税额误差check,大于误差峰值，则生成新billItem;否则继续循环
			else {
				billItemId = indexTransBill.getBillItemId();
				// 金额_人民币累加
				amtCny = amtCny.add(indexTransBill.getAmtCny());
				// 税额_人民币累加
				taxAmtCny = taxAmtCny.add(indexTransBill.getTaxAmtCny());
				// 实际税额算出：= 金额_人民币 * 税率
				taxAmt = DataUtil.calculateTaxAmt(amtCny, taxRate, "base");

				// 以上两税额之差
				BigDecimal diff = (taxAmt.subtract(taxAmtCny)).abs();
				if (diff.compareTo(DataUtil.different) >= 0) {

					// billItemNum达到峰值
					// if (billItemNum == 8) {
					// // 生成新bill
					// strBillId = createBillId("B");// 票据ID
					// billItemNum = 1;
					// } else {
					// // billItemNum累加1
					// billItemNum++;
					// }
					// 生成新billItemId

					// 保存新数据
					this.createRedBill(strOldBillId, strBillId, strBillItemId,
							transBillList);

					// 将当前transBill的billItemId作为新的对比对象
					billItemId = indexTransBill.getBillItemId();
					// 记录当前transBill的transId
					transIds = indexTransBill.getTransId();
					transBillList = new ArrayList();
					transBillList.add(indexTransBill);
					// 记录当前税率
					taxRate = indexTransBill.getTaxRate();
					// 各金额清空
					amtCny = indexTransBill.getAmtCny();
					taxAmtCny = indexTransBill.getTaxAmtCny();

					strBillItemId = createBusinessId("BI");
					if (isLast) {
						// 保存新数据
						this.createRedBill(strOldBillId, strBillId,
								strBillItemId, transBillList);
					}
				} else {
					// transIds追加
					transIds += indexTransBill.getTransId() + ",";
					transBillList.add(indexTransBill);

					if (isLast) {
						// 保存新数据
						this.createRedBill(strOldBillId, strBillId,
								strBillItemId, transBillList);
					}

				}
			}
		}
		return SUCCESS;
	}

	/*
	 * public void mergeTransBillAndCreateRedBill(List selTransList){
	 * 
	 * RedReceiptTransInfo transBillA = null; // 实际税额 BigDecimal taxAmt = new
	 * BigDecimal(0.0); //税率 BigDecimal taxRate = new BigDecimal(0.0); //
	 * 生成新billId String strBillId = createBillId("B");// 票据ID
	 * 
	 * List billList = new ArrayList<RedReceiptTransInfo>(); //循环原标交易 合并商品 for
	 * (int i = 0; i < selTransList.size(); i++) { if (i==0) { //使用第一条数据初始化
	 * transBillA = (RedReceiptTransInfo) selTransList.get(0);
	 * taxAmt.add(transBillA.getTaxAmtCny());
	 * taxRate.add(transBillA.getTaxRate()); billList.add(transBillA); continue;
	 * } RedReceiptTransInfo transBillB = (RedReceiptTransInfo)
	 * selTransList.get(i);
	 * 
	 * String billItemIdA = transBillA.getBillItemId(); String billItemIdB =
	 * transBillB.getBillItemId(); if
	 * (!billItemIdA.equalsIgnoreCase(billItemIdB)) { String strBillItemId =
	 * createBusinessId("BI"); this.createRedBill(transBillA.getBillId(),
	 * strBillId, strBillItemId, billList); }else {
	 * 
	 * if (diff.compareTo(DataUtil.different) >= 0) {
	 * 
	 * } }
	 * 
	 * 
	 * }
	 * 
	 * }
	 */
	/*
	 * 根据拆分或合并后的transBill，生成新transBill,billItem 有必要时生成新bill
	 */
	private void createRedBill(String strOldBillId, String newBillId,
			String newBillItemId, List transBillList) {
		// transBill逐个取出，各金额置负值，保存;相应billItem中各金额算出
		RedReceiptTransInfo transBill = (RedReceiptTransInfo) transBillList
				.get(0);
		// 获取原billId
		String oldBillId = transBill.getBillId();
		// 获取原billItemId
		String oldBillItemId = transBill.getBillItemId();
		// 金额_人民币 合计
		BigDecimal amtCnySum = new BigDecimal(0.0);
		// 税额_人民币 合计
		BigDecimal taxAmtCnySum = new BigDecimal(0.0);

		for (int i = 0; i < transBillList.size(); i++) {
			transBill = (RedReceiptTransInfo) transBillList.get(i);
			// 金额_人民币 合计算出
			amtCnySum = amtCnySum.add(transBill.getAmtCny());
			// 税额_人民币 合计算出
			taxAmtCnySum = taxAmtCnySum.add(transBill.getTaxAmtCny());
			// 插入交易票据对应信息
			transInfoService.saveTransBill(transBill.getTransId(), newBillId,
					newBillItemId,
					new BigDecimal(0).subtract(transBill.getAmtCny()),
					new BigDecimal(0).subtract(transBill.getTaxAmtCny()),
					new BigDecimal(0).subtract(transBill.getIncomeCny()));
		}

		// 新billItem,保存
		// 读取原billItem信息
		BillItemInfo billItem = new BillItemInfo();
		billItem.setBillId(oldBillId);
		billItem.setBillItemId(oldBillItemId);
		billItem = (BillItemInfo) (redReceiptApplyInfoService
				.findBillItemInfoList(billItem)).get(0);
		// 更换金额字段值
		billItem.setBillId(newBillId);// 可为新采番billId,也可为原billId
		billItem.setBillItemId(newBillItemId);// 为新采番billItemId
		billItem.setAmt(new BigDecimal(0).subtract(amtCnySum));
		billItem.setTaxAmt(new BigDecimal(0).subtract(taxAmtCnySum));
		billItem.setGoodsNo(new BigDecimal(0).subtract(billItem.getGoodsNo()));
		billItem.setGoodsPrice(amtCnySum);
		billItem.setTaxFlag("N");
		// 保存新生成billItem
		redReceiptApplyInfoService.saveBillItemInfo(billItem, false);

		// 为新生成bill时，需保存bill
		if (!strOldBillId.equalsIgnoreCase(newBillId)) {
			// 获取新billId相关所有billItem,算出合计金额,合计税额
			BillItemInfo newBillItem = new BillItemInfo();
			newBillItem.setBillId(newBillId);
			List billItemList = redReceiptApplyInfoService
					.findBillItemInfoList(newBillItem);
			// 合计金额
			BigDecimal amtSum = new BigDecimal(0.0);
			// 合计税额
			BigDecimal taxAmtSum = new BigDecimal(0.0);

			BigDecimal sumAmt = new BigDecimal("0");
			for (int i = 0; i < billItemList.size(); i++) {
				newBillItem = (BillItemInfo) billItemList.get(i);
				amtSum = amtSum.add(newBillItem.getAmt());
				taxAmtSum = taxAmtSum.add(newBillItem.getTaxAmt());
				sumAmt = sumAmt.add(newBillItem.getSumAmt());
			}

			// 获取原billInfo
			BillInfo billInfo = (BillInfo) (billInfoService
					.findBillInfo(oldBillId));
			// 重新设定部分字段值
			billInfo.setBillId(newBillId);
			billInfo.setAmtSum(amtSum);
			billInfo.setBalance(sumAmt);
			billInfo.setTaxAmtSum(taxAmtSum);
			billInfo.setSumAmt(sumAmt);
			billInfo.setDataStatus(DataUtil.BILL_STATUS_20);// 红冲待审核
			billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_2);// 人工审核
			billInfo.setIssueType("2");// 合并
			billInfo.setOriBillCode(billInfo.getBillCode());
			billInfo.setOriBillNo(billInfo.getBillNo());
			billInfo.setBillCode("");
			billInfo.setBillNo("");
			billInfo.setBillDate("");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			billInfo.setApplyDate(sf.format(new Date()));
			// 保存bill
			billInfoService.saveBillInfo(billInfo, false);
		}
	}

	public String updateRedPrintResult() {
		String resultOCX = (String) request.getParameter("resultOCX");
		billInfoService.updateBillByBillId(resultOCX, "8");
		releaseInstLock();
		String result = "success";
		// 将结果写出
		this.response.setContentType("application/json;charset=UTF-8");
		try {
			this.response.getWriter().print(result);
		} catch (IOException e) {
			result = "error";
			e.printStackTrace();
		}
		return null;
	}

	private void releaseInstLock() {
		String instCode = this.getCurrentUser().getOrgId();
		ThreadLock.releaseLock(instCode);
	}

	public String RedReceiptApplyToCancelReason() {
		try {
			String billId = request.getParameter("billId");

			billInfo = redReceiptApplyInfoService.findBillInfo1(billId);
			request.setAttribute("billInfo", billInfo);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ERROR;
	}

	public RedReceiptApplyInfo getRedReceiptApplyInfo() {
		return redReceiptApplyInfo;
	}

	public RedReceiptApplyInfoService getRedReceiptApplyInfoService() {
		return redReceiptApplyInfoService;
	}

	public void setRedReceiptApplyInfo(RedReceiptApplyInfo redReceiptApplyInfo) {
		this.redReceiptApplyInfo = redReceiptApplyInfo;
	}

	public void setRedReceiptApplyInfoService(
			RedReceiptApplyInfoService redReceiptApplyInfoService) {
		this.redReceiptApplyInfoService = redReceiptApplyInfoService;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public String getListFlg() {
		return listFlg;
	}

	public void setListFlg(String listFlg) {
		this.listFlg = listFlg;
	}

	public List getBillInfoList() {
		return billInfoList;
	}

	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SpecialTicket getSpecialTicket() {
		return specialTicket;
	}

	public void setSpecialTicket(SpecialTicket specialTicket) {
		this.specialTicket = specialTicket;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
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
}
