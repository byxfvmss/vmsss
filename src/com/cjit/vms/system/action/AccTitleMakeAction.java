package com.cjit.vms.system.action;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tools.ant.taskdefs.Sleep;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cjit.common.util.JXLTool;
import com.cjit.vms.system.model.TransAccInfo;
import com.cjit.vms.system.service.AccTitleService;
import com.cjit.vms.system.service.DLTJobLogService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.DLTJobLog;

public class AccTitleMakeAction extends DataDealAction {
	private static final long serialVersionUID = 1L;
	
	private AccTitleService accTitleService ;
	private DLTJobLogService dLTJobLogService;
	
	private TransAccInfo transAccInfo = new TransAccInfo();
	
	public AccTitleService getAccTitleService() {
		return accTitleService;
	}

	public void setAccTitleService(AccTitleService accTitleService) {
		this.accTitleService = accTitleService;
	}
	
	public TransAccInfo getTransAccInfo() {
		return transAccInfo;
	}

	public void setTransAccInfo(TransAccInfo transAccInfo) {
		this.transAccInfo = transAccInfo;
	}

	public AccTitleMakeAction(){
		this.init();
	}
	
	private void init(){
		log.info("会计分录生成开始！");
		System.out.println("会计分录生成开始！");
	}
	
	public String execute() {
		//while（true）
		//查job状态及交易日期
		//
		SimpleDateFormat dsf = new SimpleDateFormat("yyyy-MM-dd");
		String runDay = dsf.format(new Date());
		String jobName = "SEPERATION_ON_PRICE";
		while (true) {
			//设置交易日期 = job数据日期
			List logList = dLTJobLogService.findDLTJobLog(runDay,jobName);
			if (logList.size()>0) {
				DLTJobLog jobLog =  (DLTJobLog) logList.get(0);
				String dataDate = jobLog.getDataDate();
				transAccInfo.setTransDate(dataDate);
				break;
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List list = accTitleService.findTransAccList(transAccInfo,null);
		for (int i = 0; i < list.size(); i++) {
			transAccInfo = (TransAccInfo)list.get(i);
			if("1".equals(transAccInfo.getTransNumTyp()))
				transAccInfo.setAmt(transAccInfo.getTotalAmt());
			else if("2".equals(transAccInfo.getTransNumTyp()))
				transAccInfo.setAmt(transAccInfo.getIncome());
			else if("3".equals(transAccInfo.getTransNumTyp()))
				transAccInfo.setAmt(transAccInfo.getTaxAmt());
			
			accTitleService.saveTransAcc(transAccInfo);
		}
		
//		try {
//			exportAccTitle(list);
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error(e);
//		}
		
		return SUCCESS;
	}
	
	public void exportAccTitle(List resultList) throws Exception {
//		List resultList = accTitleService.findAccTitleList(accTitle, null);

		StringBuffer fileName = new StringBuffer("交易分录");
		fileName.append(".xls");
//		String name = "attachment;filename="
//				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
//		response.setHeader("Content-type", "application/vnd.ms-excel");
//		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, resultList);
		os.flush();
		os.close();

	}

	private void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("交易分录", 0);
		Label header1 = new Label(i++, 0, "交易流水号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "交易种类编号", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "冲账标识", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "币种", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "借贷标识", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "科目编码", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "金额", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header7);

		for (int j = 0; j < 7; j++) {
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			TransAccInfo transAccInfo = (TransAccInfo) content.get(c);
			int column = count++;
			setWritableSheet(ws, transAccInfo, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, TransAccInfo transAccInfo,
			int column) throws WriteException {
		int i = 0;

		Label cell1 = new Label(i++, column, transAccInfo.getTransId(),JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, transAccInfo.getBusinessCode(),JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, transAccInfo.getIsReverse(),JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, transAccInfo.getCurrency(),JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, transAccInfo.getCdFlag(),JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, transAccInfo.getAccTitleCode(),JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, transAccInfo.getAmt().toString(),JXLTool.getContentFormat());
		
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
	}

	public DLTJobLogService getdLTJobLogService() {
		return dLTJobLogService;
	}

	public void setdLTJobLogService(DLTJobLogService dLTJobLogService) {
		this.dLTJobLogService = dLTJobLogService;
	}
}
