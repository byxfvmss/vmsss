package com.cjit.vms.monitor.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import cjit.crms.util.StringUtil;

import com.cjit.common.util.JXLTool;
import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Monitor;
import com.cjit.vms.system.model.MonitorInput;
import com.cjit.vms.system.service.MonitorService;
import com.cjit.vms.trans.action.DataDealAction;


/**
 * @author tom
 *
 */
/**
 * @author tom
 *
 */
/**
 * @author tom
 *
 */
public class StatisticsDataAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private MonitorService monitorService;
	private Monitor monitor=new Monitor();
	private MonitorInput monitorInput=new MonitorInput();
	private List monList;
	private List lstAuthInstId;
	public String listMontior(){
		String parma=(String)request.getParameter("parma");
		monitorInput = initMonitorInput();//instCode transType vendorTaxno
		if(parma==null){
			parma="instCode";
		}
		User currentUser = this.getCurrentUser();
		monitorInput.setUserId(currentUser.getId());
		lstAuthInstId=new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		monitorInput.setLstAuthInstId(lstAuthInstId);
		monList=monitorService.findInputInvoiceMonitorList(monitorInput,paginationList,parma);
		this.request.setAttribute("monList", monList);
		this.request.setAttribute("paginationList", paginationList);

		this.request.setAttribute("parma", parma);
		
	    return SUCCESS;
	   
	}
	/**
	 * @return 销项监控
	 */
	public String listBuyMontior(){
		String parma=(String)request.getParameter("parma");
		if ("menu".equalsIgnoreCase(fromFlag)) {
		monitor = initMonitor();
		}
		if(parma==null){
			parma="instId";
		}
		User currentUser = this.getCurrentUser();
		monitor.setUserId(currentUser.getId());
		lstAuthInstId=new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		monitor.setLstAuthInstId(lstAuthInstId);
		monList=monitorService.findBuyMonitorList(monitor,paginationList,parma);
		paginationList.setRecordList(monList);
		paginationList.setRecordCount(monList.size());
		this.request.setAttribute("paginationList", paginationList);
		this.request.setAttribute("parma", parma);
	
		
		
	    return SUCCESS;
	}
	public String listMonitorTax(){
		    monitorInput = initPayTax();//instCode transType vendorTaxno
		    User currentUser = this.getCurrentUser();
		    monitorInput.setUserId(currentUser.getId());
		    List lstAuthInstId = new ArrayList();
		    this.getAuthInstList(lstAuthInstId);
		    monitorInput.setLstAuthInstId(lstAuthInstId);
			monList=monitorService.findMonitoTaxrList(monitorInput,paginationList);
			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("monList", monList);
		    return SUCCESS;	
		    }
	/**
	 * jinxiang
	 * 进项监控初始化
	 */
	public MonitorInput initMonitorInput(){
		
		if(StringUtil.IsEmptyStr(monitorInput.getFlag())){
			if(StringUtil.IsEmptyStr(monitorInput.getStartDate())){
				monitorInput.setStartDate(getFirstDay());
			}
			if(StringUtil.IsEmptyStr(monitorInput.getEndDate())){
				monitorInput.setEndDate(getLastDay());
			}
		}
	
		return monitorInput;
	}
	
	/**
	 * @return 应缴税金初始化
	 */
	public MonitorInput initPayTax(){
	
		if(StringUtil.IsEmptyStr(monitorInput.getFlag())){
			if(StringUtil.IsEmptyStr(monitorInput.getStartDate())){
				monitorInput.setStartDate(getFirstDay());
			}
			if(StringUtil.IsEmptyStr(monitorInput.getEndDate())){
				monitorInput.setEndDate(getLastDay());
			}
			if(StringUtil.IsEmptyStr(monitorInput.getInstCode())){
				monitorInput.setInstCode(getInstCode());
			}
		}
		return monitorInput;
	}
	
	/**
	 * 监控初始化
	 */
	public Monitor initMonitor(){
		
		
		if(StringUtil.IsEmptyStr(monitor.getFlag())){
			if(StringUtil.IsEmptyStr(monitor.getBeginDate())){
				monitor.setBeginDate(getFirstDay());
			}
			if(StringUtil.IsEmptyStr(monitor.getEndDate())){
				monitor.setEndDate(getLastDay());
			}
		}
	
		return monitor;
	}
	
	/**
	 * @return 销向导出
	 * @throws Exception
	 */
	public void  exportMonitor() throws Exception{
		String parma=(String)request.getParameter("parma");
		 monitor = initMonitor();
		if(parma==null){
			parma="instId";
		}
		try{
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			User currentUser = this.getCurrentUser();
			monitor.setLstAuthInstId(lstAuthInstId);
			monitor.setUserId(currentUser.getId());
			monList=monitorService.findBuyMonitorList(monitor,paginationList,parma);
		
		StringBuffer fileName = new StringBuffer("销向监控信息列表");
		fileName.append(".xls");
		String  name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, monList);
		os.flush();
		os.close();
		}catch(Exception e){
			log.error(e);
			throw e;
		}
		
		
	}
	
		public void writeToExcel(OutputStream os, List content) throws IOException,RowsExceededException, WriteException{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		/*mergeCells(a,b,c,d) 单元格合并函数
		a 单元格的列号
		b 单元格的行号
		c 从单元格[a,b]起，向下合并的列数
		d 从单元格[a,b]起，向下合并的行数*/
		int t=3;
		ws = wb.createSheet("销向监控列表", 0);
		Label header1 = new Label(i++,0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "机构", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "客户名称", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "专用发票", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "专用发票", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "普通发票", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "普通发票", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "未开票金额", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "未开票金额", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "合计", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "合计", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "合计", JXLTool.getHeader());
		Label header13 = new Label(t++, 1, "金额", JXLTool.getHeader());
		Label header14 = new Label(t++, 1, "应纳税额", JXLTool.getHeader());
		
		Label header15 = new Label(t++, 1, "金额", JXLTool.getHeader());
		Label header16 = new Label(t++, 1, "应纳税额", JXLTool.getHeader());
		Label header17 = new Label(t++, 1, "金额", JXLTool.getHeader());
		Label header18 = new Label(t++, 1, "应纳税额", JXLTool.getHeader());
		Label header19 = new Label(t++, 1, "金额", JXLTool.getHeader());
		Label header20 = new Label(t++, 1, "应纳税额", JXLTool.getHeader());
		Label header21 = new Label(t++, 1, "价税合计", JXLTool.getHeader());
		
		
		
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
		
		//ws.addCell(header11);
		ws.mergeCells(0, 0, 0, 1);
		ws.mergeCells(1, 0, 0, 1);
		ws.mergeCells(2, 0, 0, 1);
		ws.mergeCells(3, 0,	4, 0);
		ws.mergeCells(5, 0, 6, 0);
		ws.mergeCells(7, 0, 8, 0);
		ws.mergeCells(9, 0, 11, 0);
		//ws.mergeCells(3, 1, 0, 0);
	
		for(int j = 0; j < 12; j++){
			ws.setColumnView(j, 12);
		}
		int count = 2;
		for(int c = 0; c < content.size(); c++){
			Monitor monitor = (Monitor)content.get(c);
			int column = count++;
			setWritableSheet(ws, monitor, column);
		}
		wb.write();
		wb.close();
	}
	
	
	private void setWritableSheet(WritableSheet ws, Monitor monitor, int column)throws WriteException{
		int i = 0;
		Label cell1 = new Label(i++, column, monitor.getNumber(), JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, monitor.getInstName(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, monitor.getCustomerName(),JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, NumberUtils.format(monitor.getzAmtSum(),"",2),JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, NumberUtils.format(monitor.getzTaxAmtSum(),"",2), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, NumberUtils.format(monitor.getpAmtSum(),"",2), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, NumberUtils.format(monitor.getpTaxAmtSum(),"",2), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, NumberUtils.format(monitor.getBalance(),"",2), JXLTool.getContentFormat());
		Label cell9= new Label(i++, column, NumberUtils.format(monitor.getBalanceTax(),"",2), JXLTool.getContentFormat());
		Label cell10= new Label(i++, column, NumberUtils.format(monitor.getAmtSum(),"",2), JXLTool.getContentFormat());
		Label cell11= new Label(i++, column, NumberUtils.format(monitor.getTaxAmtSum(),"",2), JXLTool.getContentFormat());
		Label cell12= new Label(i++, column, NumberUtils.format(monitor.getSumAmt(),"",2), JXLTool.getContentFormat());
		
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
		
		
	}

	
	/**
	 * @return 应交税金导出
	 * @throws Exception
	 */
	public String  payTaxExport() throws Exception{
		  monitorInput = initPayTax();//instCode transType vendorTaxno
		  User currentUser = this.getCurrentUser();
		    monitorInput.setUserId(currentUser.getId());
		    List lstAuthInstId = new ArrayList();
		    this.getAuthInstList(lstAuthInstId);
		    monitorInput.setLstAuthInstId(lstAuthInstId);
		  monList=monitorService.findMonitoTaxrList(monitorInput,paginationList);
		StringBuffer fileName = new StringBuffer("应交税金信息列表");
		fileName.append(".xls");
	
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcelTax(os, monList);
		os.flush();
		os.close();
		return SUCCESS;
	}
	
	
	public void writeToExcelTax(OutputStream os, List content) throws IOException,RowsExceededException, WriteException{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("应交税金信息列表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "机构", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "销向税额", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "进项税额", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "进项转出", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "应缴税金", JXLTool.getHeader());
		
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
	
		
		for(int j = 0; j < 15; j++){
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			MonitorInput monitor = (MonitorInput)content.get(c);
			int column = count++;
			setWritableSheetTax(ws, monitor, column);
		}
		wb.write();
		wb.close();
	}
	
	private void setWritableSheetTax(WritableSheet ws, MonitorInput monitor, int column)throws WriteException{
		int i = 0;
		Label cell1 = new Label(i++, column, column+"", JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, monitor.getInstCode(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, monitor.getOutTaxAmtSum().toString(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, monitor.getInputTaxAmtSum().toString(),JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, monitor.getBillVatOutAmtSum().toString(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, monitor.getOutTaxAmtSum().subtract(monitor.getInputTaxAmtSum()).add(monitor.getBillVatOutAmtSum()).toString(), JXLTool.getContentFormat());
		
		
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
	
		
	}
	/*
	public void  exportMonitor() throws Exception{
		String parma=(String)request.getParameter("parma");
		 monitor = initMonitor();
		if(parma==null){
			parma="instId";
		}
		try{
			monList=monitorService.findBuyMonitorList(monitor,paginationList,parma);
		
		StringBuffer fileName = new StringBuffer("销向监控信息列表");
		fileName.append(".xls");
		String  name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcelInput(os, monList);
		os.flush();
		os.close();
		}catch(Exception e){
			log.error(e);
			throw e;
		}
		
		
	}*/
	/**
	 * @return 进项导出
	 * @throws Exception
	 */
	public void  exportMonitorInput() throws Exception{
		String parma=(String)request.getParameter("parma");
		monitorInput = initMonitorInput();
		if(parma==null){
			parma="instCode";
		}
		try{
			User currentUser = this.getCurrentUser();
			monitorInput.setUserId(currentUser.getId());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			monitorInput.setLstAuthInstId(lstAuthInstId);
			monList=monitorService.findMonitorList(monitorInput,paginationList,parma);
		StringBuffer fileName = new StringBuffer("进项监控信息列表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcelInput(os, monList);
		os.flush();
		os.close();
		}
		catch(Exception e){
			log.error(e);
			throw e;
		}
		
	}
	
	
	public void writeToExcelInput(OutputStream os, List content) throws IOException,RowsExceededException, WriteException{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("进项监控信息列表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "机构", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "供应商", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "交易类型", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "已收票交易总额", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "已收票交易金额", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "已收票交易税额", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "发票总额", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "发票金额", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "发票税额", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "可抵扣金额", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "已抵扣金额", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "转出金额", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "留抵金额", JXLTool.getHeader());
		
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
		
		for(int j = 0; j < 15; j++){
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			MonitorInput monitor = (MonitorInput)content.get(c);
			int column = count++;
			setWritableSheetInput(ws, monitor, column);
		}
		wb.write();
		wb.close();
		
	}
	private void setWritableSheetInput(WritableSheet ws, MonitorInput monitor, int column)throws WriteException{
		int i = 0;
		Label cell1 = new Label(i++, column, monitor.getNumber(), JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, monitor.getInstName(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, monitor.getVendorName(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, monitor.getTransName(),JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, monitor.getAmtCny().toString(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, (monitor.getAmtCny().subtract(monitor.getTaxAmtCny())).toString(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, monitor.getTaxAmtCny().toString(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, monitor.getAmtSum().toString(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, (monitor.getAmtSum().subtract(monitor.getTaxAmtSum())).toString(), JXLTool.getContentFormat());
		Label cell10= new Label(i++, column, monitor.getTaxAmtSum().toString(), JXLTool.getContentFormat());
		Label cell11= new Label(i++, column, "", JXLTool.getContentFormat());
		Label cell12= new Label(i++, column, "", JXLTool.getContentFormat());
		Label cell13= new Label(i++, column, monitor.getVatOutAmt().toString(), JXLTool.getContentFormat());
		Label cell14= new Label(i++, column, "", JXLTool.getContentFormat());
		
		
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
	}

	
	public MonitorService getMonitorService() {
	    return monitorService;
	}
	public void setMonitorService(MonitorService monitorService) {
	    this.monitorService = monitorService;
	}
	public Monitor getMonitor() {
	    return monitor;
	}
	public void setMonitor(Monitor monitor) {
	    this.monitor = monitor;
	}


	/**
	 * @return the monList
	 */
	public List getMonList() {
		return monList;
	}


	/**
	 * @param monList the monList to set
	 */
	public void setMonList(List monList) {
		this.monList = monList;
	}
    /**
     * 当月第一天
     * @return
     */
    private static String getFirstDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first);
        return str.toString();

    }
    
    /**
     * 当月最后一天
     * @return
     */
    private static String getLastDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        StringBuffer str = new StringBuffer().append(s);
        return str.toString();

    }


	/**
	 * @return the monitorInput
	 */
	public MonitorInput getMonitorInput() {
		return monitorInput;
	}
	/**
	 * @param monitorInput the monitorInput to set
	 */
	public void setMonitorInput(MonitorInput monitorInput) {
		this.monitorInput = monitorInput;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	
	
}
