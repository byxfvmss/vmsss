package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.OutSurtaxListInfo;
import com.cjit.vms.trans.service.OutSurtaxService;


/**
 * 销项附加税Action类
 * 
 */
public class OutSurtaxAction extends DataDealAction{

	public String outSurtaxList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			OutSurtaxListInfo info= new OutSurtaxListInfo();
			info.setLstAuthInstId(lstAuthInstId);
			String applyPeriod = request.getParameter("applyPeriod");
			if(applyPeriod == null){
				String perMonth=DateUtils.getPreMonth();
				this.applyPeriod=StringUtils.substring(perMonth, 0, 4)+"-"+StringUtils.substring(perMonth, 4);
			}else{
				this.applyPeriod=request.getParameter("applyPeriod");
			}
			info.setTaxPerNumber(request.getParameter("taxPerNumber"));
			info.setTaxPerName(request.getParameter("taxperName"));
			info.setSurtaxType(request.getParameter("surtaxType"));
			info.setSurtaxRate(request.getParameter("surtaxRate"));
			info.setApplyPeriod(this.applyPeriod);
			outSurtaxService.findOutSurtaxList(info, paginationList);
			mapSurtaxAmtType=vmsCommonService.findCodeDictionary("SURTAX_AMT_TYPE");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}
	/**
	 * @Action
	 * 
	 * 销项附加税一览中，帐票excel出力
	 * 
	 * @return
	 */
	public void outSurtaxExcel() throws Exception{
		try{
			StringBuffer fileName = new StringBuffer("销项附加税统计");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();

			WritableWorkbook wb = Workbook.createWorkbook(os);
			// 销项附加税sheet1的作成
			writeToExcel1(os, wb);
			wb.write();
			wb.close();
			os.flush();
			os.close();
		}catch (Exception e){
			log.error(e);
			throw e;
		}
	}
	/**
	 * @Action
	 * 
	 * 销项附加税sheet1的作成
	 * 
	 * @return
	 */
	private void writeToExcel1(OutputStream os, WritableWorkbook wb) throws IOException,
			RowsExceededException, WriteException, Exception {

		WritableSheet ws = null;
		ws = wb.createSheet("销项附加税", 0);
		/*WritableCellFormat wc = new WritableCellFormat();
        // 设置单元格的背景颜色
        wc.setBackground(jxl.format.Colour.YELLOW);*/
        JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header0 = new Label(0, 0, "序号", JXLTool.getHeaderC(excelInfo));
		Label header1 = new Label(1, 0, "纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(2, 0, "纳税人名称", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(3, 0, "附加税类型", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(4, 0, "附加税税率", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(5, 0, "销项税", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(6, 0, "汇总附加税", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(7, 0, "明细附加税", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(8, 0, "附加税差异", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(9, 0, "机构名称", JXLTool.getHeaderC(excelInfo));
		ws.addCell(header0);
		ws.setColumnView(0, 7);
		ws.addCell(header1);
		ws.setColumnView(1, 20);
		ws.addCell(header2);
		ws.setColumnView(2, 18);
		ws.addCell(header3);
		ws.setColumnView(3, 18);
		ws.addCell(header4);
		ws.setColumnView(4, 15);
		ws.addCell(header5);
		ws.setColumnView(5, 10);
		ws.addCell(header6);
		ws.setColumnView(6, 15);
		ws.addCell(header7);
		ws.setColumnView(7, 15);
		ws.addCell(header8);
		ws.setColumnView(8, 15);
		ws.addCell(header9);
		ws.setColumnView(9, 25);
		List lstAuthInstId=new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		OutSurtaxListInfo info= new OutSurtaxListInfo();
		info.setLstAuthInstId(lstAuthInstId);
		info.setTaxPerNumber(request.getParameter("taxPerNumber"));
		info.setTaxPerName(request.getParameter("taxperName"));
		info.setSurtaxType(request.getParameter("surtaxType"));
		info.setSurtaxRate(request.getParameter("surtaxRate"));
		info.setApplyPeriod(request.getParameter("applyPeriod"));
		// 一览数据检索
		List outSurtaxList = outSurtaxService.findOutSurtaxList(info, null);
		int count = 1;
		for (int i = 0; i < outSurtaxList.size(); i++) {
			OutSurtaxListInfo outInfo = (OutSurtaxListInfo) outSurtaxList.get(i);
			int column = count++;
			setWritableSheet1(ws, outInfo, column);
		}
	}
	/**
	 * @Action
	 * 
	 * 销项附加税详细列表数据
	 * 
	 * @return
	 */
	private void setWritableSheet1(WritableSheet ws, OutSurtaxListInfo info, int column) throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column), JXLTool.getContentFormat());
		// 纳税人识别号
		Label cell2 = new Label(1, column, info.getTaxPerNumber(), JXLTool.getContentFormat());
		// 纳税人名称
		Label cell3 = new Label(2, column, info.getTaxPerName(), JXLTool.getContentFormat());
		// 附加税类型
		Label cell4 = new Label(3, column, info.getSurtaxName(), JXLTool.getContentFormat());
		// 附加税税率
		Label cell5 = new Label(4, column, info.getSurtaxRate(), JXLTool.getContentFormat());
		// 销项税
		Label cell6 = new Label(5, column, info.getTaxAmtCny(), JXLTool.getContentFormat());
		// 汇总附加税
		Label cell7 = new Label(6, column, info.getGatherSurtax(), JXLTool.getContentFormat());
		// 明细附加税
		Label cell8 = null;
		if("1".equals(info.getSurtaxType())){
			cell8 = new Label(7, column, info.getSurtax1AmtCny(), JXLTool.getContentFormat());
		}else if("2".equals(info.getSurtaxType())){
			cell8 = new Label(7, column, info.getSurtax2AmtCny(), JXLTool.getContentFormat());
		}else if("3".equals(info.getSurtaxType())){
			cell8 = new Label(7, column, info.getSurtax3AmtCny(), JXLTool.getContentFormat());
		}else if("4".equals(info.getSurtaxType())){
			cell8 = new Label(7, column, info.getSurtax4AmtCny(), JXLTool.getContentFormat());
		}
		// 附加税差异
		Label cell9 = new Label(8, column, info.getDiffSurtax(), JXLTool.getContentFormat());
		// 机构号
		Label cell10 = new Label(9, column, info.getInstName(), JXLTool.getContentFormat());

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
	}
	//页面值传递变量声明
		private String taxPerNumber;//纳税人识别号
		private String taxperName; // 纳税人名称
		private String surtaxType;//附加税税种
		private Map mapSurtaxAmtType;//附加税税种列表
		private String surtaxRate;//附加税税率
		private String surtaxName; // 附加税名称
		private String applyPeriod;//申报周期
		

		public Map getMapSurtaxAmtType() {
			return mapSurtaxAmtType;
		}
		public void setMapSurtaxAmtType(Map mapSurtaxAmtType) {
			this.mapSurtaxAmtType = mapSurtaxAmtType;
		}
		public String getTaxPerNumber() {
			return taxPerNumber;
		}
		public void setTaxPerNumber(String taxPerNumber) {
			this.taxPerNumber = taxPerNumber;
		}
		public String getTaxperName() {
			return taxperName;
		}
		public void setTaxperName(String taxperName) {
			this.taxperName = taxperName;
		}
		public String getSurtaxType() {
			return surtaxType;
		}
		public void setSurtaxType(String surtaxType) {
			this.surtaxType = surtaxType;
		}
		public String getSurtaxRate() {
			return surtaxRate;
		}
		public void setSurtaxRate(String surtaxRate) {
			this.surtaxRate = surtaxRate;
		}
		public String getApplyPeriod() {
			return applyPeriod;
		}
		public void setApplyPeriod(String applyPeriod) {
			this.applyPeriod = applyPeriod;
		}
		public String getSurtaxName() {
			return surtaxName;
		}

		public void setSurtaxName(String surtaxName) {
			this.surtaxName = surtaxName;
		}

	/*service 声明*/
	private OutSurtaxService outSurtaxService;
	public OutSurtaxService getOutSurtaxService() {
		return outSurtaxService;
	}
	public void setOutSurtaxService(OutSurtaxService outSurtaxService) {
		this.outSurtaxService = outSurtaxService;
	}
}
