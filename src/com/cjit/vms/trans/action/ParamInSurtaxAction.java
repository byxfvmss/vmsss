package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import jxl.write.Number;
import org.apache.commons.lang.StringUtils;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.ParamInSurtaxListInfo;
import com.cjit.vms.trans.service.ParamInSurtaxService;

/**
 * 参数管理：进项税转出比例/金额管理Action类
 *
 * @author jobell
 */
public class ParamInSurtaxAction extends DataDealAction{
	
	/**
	 * @Action 进项税转出比例/金额 查询页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String listParamInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
//			if(StringUtils.isEmpty(dataDt)){
//				String perMonth=DateUtils.getPreMonth();
//				this.dataDt=StringUtils.substring(perMonth, 0, 4)+"-"+StringUtils.substring(perMonth, 4);
//			}
			
			this.getAuthInstList(lstAuthInstId);
			
			ParamInSurtaxListInfo info = new ParamInSurtaxListInfo();
			info.setLstAuthInstId(lstAuthInstId);
			info.setDataDt(StringUtils.replace(dataDt, "-",""));
			info.setInstId(instId);
			info.setTaxpayerId(taxPerNumber);
			info.setTaxpayerName(taxperName);
			
			paramInSurtaxService.findParamInSurtaxListInfo(info, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ParamInSurtaxAction-listParamInSurtax", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 进项税转出比例/金额 导出
	 * 
	 * @author jobell
	 * @return
	 */
	public void paramInsurtaxExcel() throws Exception{
		/*if(StringUtils.isEmpty(dataDt)){
			String perMonth=DateUtils.getPreMonth();
			this.dataDt=StringUtils.substring(perMonth, 0, 4)+"-"+StringUtils.substring(perMonth, 4);
			
		}*/
		
		
		this.getAuthInstList(lstAuthInstId);
		
		ParamInSurtaxListInfo info = new ParamInSurtaxListInfo();
		info.setLstAuthInstId(lstAuthInstId);
		info.setDataDt(StringUtils.replace(dataDt, "-",""));
		info.setInstId(instId);
		info.setTaxpayerId(taxPerNumber);
		info.setTaxpayerName(taxperName);
		
		List lstParamInSurtax=paramInSurtaxService.findParamInSurtaxListInfo(info, null);
		
		try{
			StringBuffer fileName = new StringBuffer("进项附加税统计");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			
			WritableWorkbook wb = Workbook.createWorkbook(os);
			writeToExcel1(os,lstParamInSurtax, wb);
			wb.write();
			wb.close();
			os.flush();
			os.close();
		}catch (Exception e){
			log.error(e);
			throw e;
		}
	}
	
	private void writeToExcel1(OutputStream os, List lstParamInSurtax,WritableWorkbook wb)  throws IOException,
	RowsExceededException, WriteException, Exception {
		WritableSheet ws = null;
		ws = wb.createSheet("进项税转出比例金额", 0);		
	    JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header0 = new Label(0, 0, "数据时间", JXLTool.getHeaderC(excelInfo));
		Label header1 = new Label(1, 0, "机构", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(2, 0, "纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(3, 0, "纳税人名称", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(4, 0, "免税收入", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(5, 0, "征税收入", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(6, 0, "转出比例", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(7, 0, "转出金额", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(8, 0, "标志", JXLTool.getHeaderC(excelInfo));
		ws.addCell(header0);
		ws.setColumnView(0, 18);
		ws.addCell(header1);
		ws.setColumnView(1, 20);
		ws.addCell(header2);
		ws.setColumnView(2, 18); 
		ws.addCell(header3);
		ws.setColumnView(3, 18);
		ws.addCell(header4);
		ws.setColumnView(4, 15);
		ws.addCell(header5);
		ws.setColumnView(5, 15);
		ws.addCell(header6);
		ws.setColumnView(6, 10);
		ws.addCell(header7);
		ws.setColumnView(7, 15);
		ws.addCell(header8);
		ws.setColumnView(8, 15);
		for(int i=0;i<lstParamInSurtax.size();i++){
			ParamInSurtaxListInfo paramInSurtaxInfo=(ParamInSurtaxListInfo)lstParamInSurtax.get(i);
			setWritableSheet1(ws, paramInSurtaxInfo, i+1);
		}
	}

	
	public static Date strToDateLong(String strDate) {
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		   ParsePosition pos = new ParsePosition(0);
		   Date strtodate = formatter.parse(strDate, pos);
		   return strtodate;
		}
	private void setWritableSheet1(WritableSheet ws,ParamInSurtaxListInfo info, int column) throws WriteException {
		// 数据时间
		Label cell1 = new Label(0, column, info.getDataDt(), JXLTool.getContentFormat());
		DateTime time1=new DateTime(0, column, strToDateLong(info.getDataDt().toString()), JXLTool.getContentFormatDateFormat());
		//机构
		Label cell2 = new Label(1, column, info.getInstName(), JXLTool.getContentFormat());
		// 纳税人识别号
		Label cell3 = new Label(2, column, info.getTaxpayerId(), JXLTool.getContentFormat());
		// 纳税人名称
		Label cell4 = new Label(3, column, info.getTaxpayerName(), JXLTool.getContentFormat());
		// 免税收入
		Label cell5 = new Label(4, column, info.getTaxfreeIncome().toString(), JXLTool.getContentFormat());
		Number number5 =new Number(4, column, Double.parseDouble(info.getTaxfreeIncome().toString()), JXLTool.getContentFormatNumberFloat());
		// 征税收入
		Label cell6 = new Label(5, column, info.getAssessableIncome().toString(), JXLTool.getContentFormat());
		Number number6 =new Number(5, column, Double.parseDouble(info.getAssessableIncome().toString()), JXLTool.getContentFormatNumberFloat());
		// 转出比例
		Label cell7 = new Label(6, column, info.getVatOutProportion().toString(), JXLTool.getContentFormat());
		Number number7 =new Number(6, column, Double.parseDouble(info.getVatOutProportion().toString()), JXLTool.getContentFormatNumberFloat());
		// 转出金额
		Label cell8 = new Label(7, column, info.getVatOutAmt().toString(), JXLTool.getContentFormat());
		Number number8 =new Number(7, column, Double.parseDouble(info.getVatOutAmt().toString()), JXLTool.getContentFormatNumberFloat());
		// 标志
		Label cell9 = new Label(8, column, info.getProportionFlgName(), JXLTool.getContentFormat()); 

		ws.addCell(time1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(number5);
		ws.addCell(number6);
		ws.addCell(number7);
		ws.addCell(number8);
		ws.addCell(cell9);
	}

	/**
	 * @Action 进项税转出比例/金额 编辑页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String editParamInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try{
			ParamInSurtaxListInfo info = new ParamInSurtaxListInfo();
			instId=request.getParameter("taxPerNumber");
			info.setInstId(instId);
			paramInSurtaxInfo=paramInSurtaxService.findParamInSurtaxItemInfo(info);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ParamInSurtaxAction-listParamInSurtax", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 进项税转出比例/金额 编辑页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String saveParamInSurtax(){
//		this.getAuthInstList(lstAuthInstId);
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try{
			ParamInSurtaxListInfo info = new ParamInSurtaxListInfo();
			info.setVatOutProportion(vatOutProportion);

			info.setTaxpayerId(taxPerNumber);
			paramInSurtaxService.saveParamInSurtaxInfo(info);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ParamInSurtaxAction-listParamInSurtax", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 进项税转出比例/金额 查看页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String viewParamInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try{
		
			ParamInSurtaxListInfo info = new ParamInSurtaxListInfo();
			instId=request.getParameter("taxPerNumber");

			info.setInstId(instId);
			paramInSurtaxInfo=paramInSurtaxService.findParamInSurtaxItemInfo(info);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ParamInSurtaxAction-listParamInSurtax", e);
		}
		return ERROR;
	}
	
	private List lstAuthInstId = new ArrayList();
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	private ParamInSurtaxListInfo paramInSurtaxInfo;
	private String dataDt;
	private String instId;
	private String taxPerNumber;
	private String taxperName;
	private BigDecimal vatOutProportion;
	
	public String getDataDt() {
		return dataDt;
	}

	public void setDataDt(String dataDt) {
		this.dataDt = dataDt;
	}

	public String getInstId() {
		return instId;
	}

	public ParamInSurtaxListInfo getParamInSurtaxInfo() {
		return paramInSurtaxInfo;
	}

	public void setParamInSurtaxInfo(ParamInSurtaxListInfo paramInSurtaxInfo) {
		this.paramInSurtaxInfo = paramInSurtaxInfo;
	}

	public void setInstId(String instId) {
		this.instId = instId;
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


	public BigDecimal getVatOutProportion() {
		return vatOutProportion;
	}

	public void setVatOutProportion(BigDecimal vatOutProportion) {
		this.vatOutProportion = vatOutProportion;
	}

	/**
	 * serveice 注入
	 */
	private ParamInSurtaxService paramInSurtaxService;

	public ParamInSurtaxService getParamInSurtaxService() {
		return paramInSurtaxService;
	}

	public void setParamInSurtaxService(ParamInSurtaxService paramInSurtaxService) {
		this.paramInSurtaxService = paramInSurtaxService;
	}
	
	
	
}
