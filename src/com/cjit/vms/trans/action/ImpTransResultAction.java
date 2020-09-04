package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cjit.common.util.JXLTool;
import com.cjit.vms.trans.model.ImpTransResultInfo;
import com.cjit.vms.trans.service.ImpTransResultService;

public class ImpTransResultAction extends DataDealAction {

	private String impTime;
	private ImpTransResultService impTransResultService;

	public String searchList() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		impTransResultService.getImpResultListInfo(impTime, paginationList);
		return SUCCESS;
	}
	/**
	 * @param list
	 * @return
	 * @throws JXLException
	 */
	public List setwriteWidth(List list) throws JXLException{
	 List rowlist=null;
	 List sheetList=new ArrayList();
	 ImpTransResultInfo line=null;
	for(int i=0;i<list.size();i++){
		rowlist=new ArrayList();
		line=(ImpTransResultInfo) list.get(i);
		rowlist.add(line.getTerminal());
		rowlist.add(line.getTransId());
		rowlist.add(line.getTransactionDate());
		rowlist.add(line.getCustomerId()+" ");
		rowlist.add(String.valueOf(line.getAmtCny()));
		rowlist.add(String.valueOf(line.getTaxAmtCny()));
		rowlist.add(String.valueOf(line.getIncomeCny()));
		rowlist.add(line.getVatRateCode());
		rowlist.add(String.valueOf(line.getTaxRate()));
		rowlist.add(line.getProductIeType());
		rowlist.add(line.getIeItem());
		
		sheetList.add(rowlist);
	}
	return sheetList;
}
	/*Label cell2 = new Label(0, column, ,
			JXLTool.getContentFormat());
	Label cell3 = new Label(1, column, ,
			JXLTool.getContentFormat());
	Label cell5 = new Label(2, column, ,
			JXLTool.getContentFormat());
	Label cell4 = new Label(3, column, ,
			JXLTool.getContentFormat());
	Label cell6 = new Label(4, column, ),
			JXLTool.getContentFormat());
	Label cell7 = new Label(5, column, ),
			JXLTool.getContentFormat());
	Label cell8 = new Label(6, column, ),
			JXLTool.getContentFormat());
	Label cell9 = new Label(7, column, ,
			JXLTool.getContentFormat());
	Label cell10 = new Label(8, column, ),
			JXLTool.getContentFormat());
	Label cell11 = new Label(9, column, ,
			JXLTool.getContentFormat());
	Label cell12 = new Label(10, column, ,
			JXLTool.getContentFormat());*/

	public void expImpTransResult() throws Exception {
		try {
			StringBuffer fileName = new StringBuffer("价税分离数据信息表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	/**
	 * @Action
	 * 
	 *         税目管理 导出
	 * 
	 * @author lee
	 * @return
	 */
	private void writeToExcel(OutputStream os) throws IOException,
			RowsExceededException, WriteException, Exception {

		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("价税分离数据信息表", 0);
		//WorkStation ID	Deal No. 	Transaction Date	Merchant ID / Customer ID	Total Revenue Amount in local currency	VAT Amount in local currency	Revenue Amount in local currency	VAT Rate Code	VAT Rate	Product I/E Type	I/E Item
		//机构				交易ID																										_人民币						_人民币													

		// Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		// Label header2 = new Label(1, 0, "导入日期", JXLTool.getHeader());
		Label header3 = new Label(0, 0, "机构", JXLTool.getHeader());
		Label header4 = new Label(1, 0, "交易ID", JXLTool.getHeader());
		Label header6 = new Label(2, 0, "交易时间", JXLTool.getHeader());
		Label header5 = new Label(3, 0, "客户ID",
				JXLTool.getHeader());
		Label header7 = new Label(4, 0,
				"金额", JXLTool.getHeader());
		Label header8 = new Label(5, 0, "税额",
				JXLTool.getHeader());
		Label header9 = new Label(6, 0, "收入",
				JXLTool.getHeader());
		Label header10 = new Label(7, 0, "税目", JXLTool.getHeader());
		Label header11 = new Label(8, 0, "税率	", JXLTool.getHeader());
		Label header12 = new Label(9, 0, "交易种类TYPE",
				JXLTool.getHeader());
		Label header13 = new Label(10, 0, "交易种类ITEM", JXLTool.getHeader());

		ws.setColumnView(0, 10);
		ws.setColumnView(1, 40);
		ws.setColumnView(2, 10);
		ws.setColumnView(3, 30);
		ws.setColumnView(4, 10);
		ws.setColumnView(5, 10);
		ws.setColumnView(6, 10);
		ws.setColumnView(7, 10);
		ws.setColumnView(8, 10);
		ws.setColumnView(9, 10);
		ws.setColumnView(10, 10);

		// ws.addCell(header1);
		// ws.addCell(header2);
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

		List dataList = impTransResultService.getImpResultListInfo(impTime,
				null);
		JXLTool.setAutoWidth(ws, setwriteWidth(dataList));

		int count = 1;
		for (int i = 0; i < dataList.size(); i++) {
			ImpTransResultInfo line = (ImpTransResultInfo) dataList.get(i);
			// if (info.getInvoiceCode() != null &&
			// !"".equals(info.getInvoiceCode())){
			int column = count++;
			setWritableSheet(ws, line, column);
			// }
		}
		wb.write();
		wb.close();
		// ws.setColumnView(i + 1, width);
	}

	/**
	 * @Action
	 * 
	 *         税目管理 导出
	 * 
	 * @author lee
	 * @return
	 */
	private void setWritableSheet(WritableSheet ws, ImpTransResultInfo line,
			int column) throws WriteException {

		// Label cell0 = new Label(0, column, String.valueOf(line.getRowno()),
		// JXLTool.getContentFormat());
		// Label cell1 = new Label(1, column, line.getImptime(),
		// JXLTool.getContentFormat());
		Label cell2 = new Label(0, column, line.getTerminal(),
				JXLTool.getContentFormat());
		Label cell3 = new Label(1, column, line.getTransId(),
				JXLTool.getContentFormat());
		Label cell5 = new Label(2, column, line.getTransactionDate(),
				JXLTool.getContentFormat());
		Label cell4 = new Label(3, column, line.getCustomerId(),
				JXLTool.getContentFormat());
		Label cell6 = new Label(4, column, String.valueOf(line.getAmtCny()),
				JXLTool.getContentFormat());
		Label cell7 = new Label(5, column, String.valueOf(line.getTaxAmtCny()),
				JXLTool.getContentFormat());
		Label cell8 = new Label(6, column, String.valueOf(line.getIncomeCny()),
				JXLTool.getContentFormat());
		Label cell9 = new Label(7, column, line.getVatRateCode(),
				JXLTool.getContentFormat());
		Label cell10 = new Label(8, column, String.valueOf(line.getTaxRate()),
				JXLTool.getContentFormat());
		Label cell11 = new Label(9, column, line.getProductIeType(),
				JXLTool.getContentFormat());
		Label cell12 = new Label(10, column, line.getIeItem(),
				JXLTool.getContentFormat());

		// ws.addCell(cell0);
		// ws.addCell(cell1);
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

	public ImpTransResultService getImpTransResultService() {

		return impTransResultService;
	}

	public void setImpTransResultService(
			ImpTransResultService impTransResultService) {
		this.impTransResultService = impTransResultService;
	}

	public String getImpTime() {
		return impTime;
	}

	public void setImpTime(String impTime) {
		this.impTime = impTime;
	}

}
