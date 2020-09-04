package com.cjit.vms.trans.action.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cjit.common.util.JXLTool;
import com.cjit.vms.trans.action.DataDealAction;

import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.storage.InvoiceStockDetail;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.service.storage.BillCancelNoneService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;

public class BillCancelNoneAction extends DataDealAction {
	

	//空白发票作废页面检索初始化,查询
	public String SelectNoneAction(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		this.message = (String) this.request.getAttribute("message");
		InstInfo in = new InstInfo();
		in.setInstId(this.getInstId());
		in.setUserId(this.getCurrentUser().getId());
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		in.setLstAuthInstIds(lstAuthInstId);
		List insts = billCancelNoneService.getInstInfoList(in, paginationList);
		this.setAuthInstList(insts);		
		stockDetail = new InvoiceStockDetail();		
		stockDetail.setUserId(this.getCurrentUser().getId());
		stockDetail.setInstId(this.getInstId());
		stockDetail.setInvoiceType(this.getInvoiceType());
		stockDetail.setStartTime(this.getStartTime());
		stockDetail.setEndTime(this.getEndTime());
		stockDetail.setLstAuthInstId(lstAuthInstId);
		stockDetail.setInvoiceNo(this.invoiceNo);
		stockDetail.setInvoiceStatus(this.invoiceStatus);
		billCancelNoneService.getInvoiceStockDetailList(stockDetail, paginationList);
		return SUCCESS;
	}
	
	//添加作废
	public String addBillNoneCancel(){
		return SUCCESS;
	}
	
	
	//发票代码,发票号码 联动 发票类型
	public void selectLianDong(){
		String result = "";
		try {
			stockDetail = new InvoiceStockDetail();
			stockDetail.setInvoiceCode(invoiceCode);
			stockDetail.setInvoiceNo(invoiceNo);
			stockDetail.setUserId(this.getCurrentUser().getId());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			//stockDetail.setLstAuthInstIdlstAuthInstId);
			//查询库存发票类型。2019
			result=billCancelNoneService.checkBillCodeAndBillNo(invoiceCode, invoiceNo, lstAuthInstId);
		
			response.setHeader("Content-Type","text/html; charset=utf-8");
			PrintWriter out;
			out = response.getWriter();
				out.print(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(".action : -"+e);
		}
	}
	//作废
	public void updateBillNoneCancel() throws IOException{
		try {
			String result = "";
			stockDetail = new InvoiceStockDetail();
			stockDetail.setInvoiceCode(request.getParameter("invoiceCode"));
			stockDetail.setInvoiceNo(request.getParameter("invoiceNo"));
			stockDetail.setInvalidReason(request.getParameter("invalidReason"));
			stockDetail.setInvoiceType(request.getParameter("invoiceType"));
			stockDetail.setOpertType(request.getParameter("opertType"));
			//stockDetail.setInvoiceStatus("2");
			stockDetail.setInstId(this.getCurrentUser().getOrgId());
			//stockDetail.setUserId(this.getCurrentUser().getId());
			//2019
			billCancelNoneService.updateInvioceStatus(stockDetail);
			
			response.setHeader("Content-Type","text/html; charset=utf-8");
			PrintWriter out;
			out = response.getWriter();
			out.print(result);
		} catch (IOException e) {
			response.setHeader("Content-Type","text/html; charset=utf-8");
			PrintWriter out;
			out = response.getWriter();
			out.print("2");
			e.printStackTrace();
			log.error(e);
		}
	}
	
	
	//撤销
	public void revokeBillNoneCancel() throws IOException{
		String result = "1";
		try {
			stockDetail = new InvoiceStockDetail();
			String invoIceId = request.getParameter("invoiceId");	
			//更新发票信息和库存发票状态
			billCancelNoneService.undoBillInfoStatus(invoIceId);				
			response.setHeader("Content-Type","text/html; charset=utf-8");
			PrintWriter out;
			out = response.getWriter();
			out.print(result);
		} catch (Exception e) {
			result = "0";
			e.printStackTrace();
			response.setHeader("Content-Type","text/html; charset=utf-8");
			PrintWriter out;
			out = response.getWriter();
			out.print(result);
			log.error(e);
		}
	}
	
	//导出
	public void cancelNoneBillToExcel(){
		try {
			stockDetail = new InvoiceStockDetail();
			stockDetail.setUserName(this.getUserName());
			stockDetail.setInvoiceType(this.getInfoType());
			stockDetail.setStartTime(this.getStartTime());
			stockDetail.setEndTime(this.getEndTime());
			stockDetail.setInvoiceNo(this.getInvoiceNo());
			stockDetail.setInvoiceCode(this.getInvoiceCode());
			stockDetail.setInvoiceStatus(this.getInvoiceStatus());
			stockDetail.setReceiveInvoiceTime(this.getReceiveInvoiceTime());
			stockDetail.setUserId(this.getCurrentUser().getId());
			billCanceNonelInfoList = billCancelNoneService.getInvoiceStockDetailList(stockDetail, paginationList);
			StringBuffer fileName = new StringBuffer("空白发票作废信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os,billCanceNonelInfoList);
			os.flush();
			os.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeToExcel(OutputStream os, List content) throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = wb.createSheet("作废发票信息表", 0);
		JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(1, 0, "领购日期", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(2, 0, "领购人员", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(3, 0, "发票类型", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(4, 0, "发票代码", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(5, 0, "发票号码", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(6, 0, "状态", JXLTool.getHeaderC(excelInfo));
		
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header7);
		for (int i = 0; i < 7; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			InvoiceStockDetail o = (InvoiceStockDetail) content.get(i);
			int column = count++;
			setWritableSheet(ws, o, column,i);
		}
		wb.write();
		wb.close();
	}
	
	public void setWritableSheet(WritableSheet ws,InvoiceStockDetail detail,int column,int i){
		try {
			Label header1 = new Label(0, column, i+1+"", JXLTool.getContentFormat());
			Label header2 = new Label(1, column, detail.getReceiveInvoiceTime(), JXLTool.getContentFormat());
			Label header3 = new Label(2, column, detail.getUserName(), JXLTool.getContentFormat());
			Label header4 = new Label(3, column, detail.getInvoiceType(), JXLTool.getContentFormat());
			Label header6 = new Label(4, column, detail.getInvoiceCode(), JXLTool.getContentFormat());
			Label header7 = new Label(5, column, detail.getInvoiceNo(), JXLTool.getContentFormat());
			Label header5 = new Label(6, column, "已作废", JXLTool.getContentFormat());
			ws.addCell(header1);
			ws.addCell(header2);
			ws.addCell(header3);
			ws.addCell(header4);
			ws.addCell(header5);
			ws.addCell(header6);
			ws.addCell(header7);
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
	//查看作废原因
	public String addBillNoneInvoiceReason(){
		String invoiceId = request.getParameter("invoiceId");
		stockDetail = new InvoiceStockDetail();
		stockDetail.setInvoiceId(invoiceId);
		stockDetail.setInvoiceCode(this.getInvoiceCode());
		stockDetail.setInvoiceNo(this.getInvoiceNo());
		note = billCancelNoneService.billNoneInvoiceReason(stockDetail);
		return SUCCESS;
	}
	public PageTaxInvoiceService getPageTaxInvoiceService() {
		return pageTaxInvoiceService;
	}
	public void setPageTaxInvoiceService(PageTaxInvoiceService pageTaxInvoiceService) {
		this.pageTaxInvoiceService = pageTaxInvoiceService;
	}
	private static final long serialVersionUID = 1L;
	private String message;
	private InvoiceStockDetail stockDetail;
	private BillCancelNoneService billCancelNoneService;
	private List billCanceNonelInfoList;
	private String instId;//机构ID
	private String instName;//机构名
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String invoiceType;//发票类型 0:专票 1:普票
	private String invoiceNo;//发票号码
	private String invalidReason;	//作废原因
	private String invoiceCode;//发票代码
	private String userName; //用户名称
	private String receiveInvoiceTime;//领购时间
	private String invoiceStatus;//状态
	private String note;
	private List lstAuthInstId;
	private PageTaxInvoiceService pageTaxInvoiceService;

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getReceiveInvoiceTime() {
		return receiveInvoiceTime;
	}
	public void setReceiveInvoiceTime(String receiveInvoiceTime) {
		this.receiveInvoiceTime = receiveInvoiceTime;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public String getInvalidReason() {
		return invalidReason;
	}
	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public BillCancelNoneService getBillCancelNoneService() {
		return billCancelNoneService;
	}
	public void setBillCancelNoneService(BillCancelNoneService billCancelNoneService) {
		this.billCancelNoneService = billCancelNoneService;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public InvoiceStockDetail getStockDetail() {
		return stockDetail;
	}
	public void setStockDetail(InvoiceStockDetail stockDetail) {
		this.stockDetail = stockDetail;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public List getBillCanceNonelInfoList() {
		return billCanceNonelInfoList;
	}
	public void setBillCanceNonelInfoList(List billCanceNonelInfoList) {
		this.billCanceNonelInfoList = billCanceNonelInfoList;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
