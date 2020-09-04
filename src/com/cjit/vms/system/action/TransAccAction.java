package com.cjit.vms.system.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cjit.common.util.JXLTool;
import com.cjit.vms.system.model.TransAccInfo;
import com.cjit.vms.system.service.AccTitleService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.service.ExchangeRateService;

public class TransAccAction extends DataDealAction {
	private static final long serialVersionUID = 1L;
	private AccTitleService accTitleService;
	private List transAccList = new ArrayList();
	private TransAccInfo transAccInfo = new TransAccInfo();
	private List currencyList = new ArrayList();
	private ExchangeRateService exchangeRateService;

	public String listTransAcc() {
		currencyList = exchangeRateService.findAllCurrency();
		transAccList = accTitleService.findTransAccList(transAccInfo,
				paginationList);
		return SUCCESS;
	}
	
	public void exportTransAcc() throws Exception {
		
		List resultList = accTitleService.findTransAccList(transAccInfo);

		StringBuffer fileName = new StringBuffer("交易分录");
		fileName.append(".xls");
		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
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
		Label header3 = new Label(i++, 0, "交易种类名称", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "冲账标识", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "币种", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "借贷标识", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "科目编码", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "科目名称", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "金额", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header7);
		ws.addCell(header8);
		ws.addCell(header9);

		for (int j = 0; j < 9; j++) {
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
		Label cell3 = new Label(i++, column, transAccInfo.getBusinessCName(),JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, "Y".equals(transAccInfo.getIsReverse())?"是":"否",JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, transAccInfo.getCurrencyName(),JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, "D".equals(transAccInfo.getCdFlag())?"借方":"贷方",JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, transAccInfo.getAccTitleCode(),JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, transAccInfo.getAccTitleName(),JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, transAccInfo.getAmt().toString(),JXLTool.getContentFormat());
		
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
		ws.addCell(cell8);
		ws.addCell(cell9);
	}

	public TransAccInfo getTransAccInfo() {
		return transAccInfo;
	}

	public void setTransAccInfo(TransAccInfo transAccInfo) {
		this.transAccInfo = transAccInfo;
	}

	public List getTransAccList() {
		return transAccList;
	}

	public void setTransAccList(List transAccList) {
		this.transAccList = transAccList;
	}

	public AccTitleService getAccTitleService() {
		return accTitleService;
	}

	public void setAccTitleService(AccTitleService accTitleService) {
		this.accTitleService = accTitleService;
	}

	public List getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List currencyList) {
		this.currencyList = currencyList;
	}

	public ExchangeRateService getExchangeRateService() {
		return exchangeRateService;
	}

	public void setExchangeRateService(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

}
