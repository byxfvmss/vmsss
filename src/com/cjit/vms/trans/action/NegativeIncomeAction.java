package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.service.NegativeIncomeService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.JXLTool;

public class NegativeIncomeAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	
	private TransInfo transInfo;
	private List negativeIncomeList;
	private NegativeIncomeService negativeIncomeService;
	private List customerTaxnoList;
	
	/**
	 * 负数收入列表
	 */
	public String listNegativeIncome() {
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		
		try {
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				transInfo = new TransInfo();
			}
			transInfo.setUserId(currentUser.getId());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			transInfo.setLstAuthInstId(lstAuthInstId);
			negativeIncomeList = negativeIncomeService.findTransInfoForNegativeIncome(transInfo, paginationList);
			
			//权限机构
			this.getAuthInstList(lstAuthInstId);
			this.setAuthInstList(lstAuthInstId);
			
			//客户纳税人识别号
			List customerList = negativeIncomeService.findAllCustomer();
			customerTaxnoList = new ArrayList();
			for (int i=0; i<customerList.size(); i++){
				Customer customer = (Customer) customerList.get(i);
				SelectTag st = new SelectTag(customer.getCustomerTaxno(), customer.getCustomerTaxno());
				customerTaxnoList.add(st);
			}

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("NegativeIncomeAction-listNegativeIncome", e);
		}
		return ERROR;
	}
	
	/**
	 * 查看交易
	 */
	public String viewTransFromNegativeIncome() {
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		
		try {
			transInfo = new TransInfo();
			transInfo.setInstCode(request.getParameter("instCode"));
			transInfo.setCustomerId(request.getParameter("customerId"));
			transInfo.setBillingTime(request.getParameter("billingTime"));
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			transInfo.setLstAuthInstId(lstAuthInstId);
			negativeIncomeList = negativeIncomeService.findTransInfoForNegativeIncomeDetail(transInfo, paginationList);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("NegativeIncomeAction-viewTransFromNegativeIncome", e);
		}
		return ERROR;
	}
	
	/**
	 * 冲抵
	 */
	public String offsetTrans() {
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		
		try {
			transInfo = new TransInfo();
			transInfo.setInstCode(request.getParameter("instCode"));
			transInfo.setCustomerId(request.getParameter("customerId"));
			transInfo.setBillingTime(request.getParameter("billingTime"));
			//vms_trans_info状态改为9-已冲抵，收入改为0
			negativeIncomeService.offsetTrans(transInfo);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			transInfo.setTransBeginDate(sdf.format(new Date()));
			//vms_trans_nega_info新增数据
			negativeIncomeService.saveTransNegaInfo(transInfo);
			
			transInfo = null;
			this.setResultMessages("冲抵成功！");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			this.setResultMessages("冲抵失败：" + e.getMessage());
			log.error("NegativeIncomeAction-offsetTrans", e);
		}
		return ERROR;
	}
	
	/**
	 * 撤回
	 */
	public String cancelNegativeIncome() {
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		
		try {
			transInfo = new TransInfo();
			transInfo.setInstCode(request.getParameter("instCode"));
			transInfo.setCustomerId(request.getParameter("customerId"));
			transInfo.setBillingTime(request.getParameter("billingTime"));
			//vms_trans_info状态改为1-已开票，是否打票状态更改为M-手动打印
			negativeIncomeService.cancelNegativeIncome(transInfo);
			
			transInfo = null;
			this.setResultMessages("撤回成功！");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			this.setResultMessages("撤回失败：" + e.getMessage());
			log.error("NegativeIncomeAction-cancelNegativeIncome", e);
		}
		return ERROR;
	}
	
	/**
	 * 导出
	 */
	public void exportNegativeIncome() {
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return;
		}
		
		try {
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				transInfo = new TransInfo();
			}
			transInfo.setUserId(currentUser.getId());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			transInfo.setLstAuthInstId(lstAuthInstId);
			negativeIncomeList = negativeIncomeService.findTransInfoForNegativeIncomeExport(transInfo);
			
			StringBuffer fileName = new StringBuffer("负数收入交易信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, negativeIncomeList);
			os.flush();
			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("NegativeIncomeAction-exportNegativeIncome", e);
		}
	}
	
	private void writeToExcel(OutputStream os, List content) throws IOException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("负数收入交易信息", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "交易日期", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "客户类型", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "交易类型", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "交易金额", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "税率", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "税额", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "收入", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "冲抵成本ENTRY", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "VAT-output GL", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "GHO Class", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "WSID", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "交易账号", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "账单日期", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "状态", JXLTool.getHeader());
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
		for(int j = 0; j < 16; j++){
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			TransInfo trans = (TransInfo)content.get(c);
			int column = count++;
			setWritableSheet(ws, trans, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, TransInfo trans,
			int column) throws WriteException {
		int i = 0;
		Label cell1 = new Label(i++, column, column+"", JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, trans.getTransDate(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, trans.getCustomerName(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, DataUtil.getCustomerTypeCH(trans.getCustomerType()), JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, trans.getTransTypeName(),JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, NumberUtils.format(trans.getAmt(), "", 2),JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, NumberUtils.format(trans.getTaxRate(), "", 4), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, NumberUtils.format(trans.getTaxAmt(), "", 2), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, NumberUtils.format(trans.getIncome(), "", 2), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, NumberUtils.format(trans.getTaxAmt(), "", 2), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, NumberUtils.format(trans.getTaxAmt(), "", 2), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, trans.getGhoClass(), JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, trans.getOrigCapWorkstation(), JXLTool.getContentFormat());
		Label cell14 = new Label(i++, column, trans.getAssociateAccountNo(), JXLTool.getContentFormat());
		Label cell15 = new Label(i++, column, trans.getBillingTime(), JXLTool.getContentFormat());
		Label cell16 = new Label(i++, column, DataUtil.getDataStatusCH(trans.getDataStatus(), "TRANS"), JXLTool.getContentFormat());
		
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
		
	}

	/**
	 * 
	 */
	public void loadCustomerName() {
		try {
			String customerTaxno = request.getParameter("customerTaxno");
			Customer customer = negativeIncomeService.findCustomerByTaxno(customerTaxno);
			String customerName = "";
			if (customer != null) {
				customerName = customer.getCustomerCName();
			}
			this.response.setContentType("text/html; charset=UTF-8");
			log.info("loadCustomerName : " + customerName);
			this.response.getWriter().print(customerName);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("loadCustomerName : ", ex);
		}
	}

	public TransInfo getTransInfo() {
		return transInfo;
	}
	public void setTransInfo(TransInfo transInfo) {
		this.transInfo = transInfo;
	}
	public List getNegativeIncomeList() {
		return negativeIncomeList;
	}
	public void setNegativeIncomeList(List negativeIncomeList) {
		this.negativeIncomeList = negativeIncomeList;
	}
	public NegativeIncomeService getNegativeIncomeService() {
		return negativeIncomeService;
	}
	public void setNegativeIncomeService(NegativeIncomeService negativeIncomeService) {
		this.negativeIncomeService = negativeIncomeService;
	}

	public List getCustomerTaxnoList() {
		return customerTaxnoList;
	}

	public void setCustomerTaxnoList(List customerTaxnoList) {
		this.customerTaxnoList = customerTaxnoList;
	}
	
}
