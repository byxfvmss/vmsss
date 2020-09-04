package com.cjit.vms.trans.action;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.ExcelUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.vms.trans.model.ExchangeRate;
import com.cjit.vms.trans.service.ExchangeRateService;
import com.cjit.vms.trans.util.DataUtil;

public class ExchangeRateAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private ExchangeRateService exchangeRateService;
	private ExchangeRate exchangeRate;
	private List exchangeRateList;
	private List currencyList;
	private String[] selectExchangeRateIds;
	private String message;
	
	/**
	 * 汇率列表
	 */
	public String listExchangeRate() {
		if ("menu".equalsIgnoreCase(fromFlag)) {
			exchangeRate = new ExchangeRate();
			fromFlag = null;
		}
		try {
			exchangeRateList = exchangeRateService.findExchangeRateList(exchangeRate, paginationList);
			
			currencyList = exchangeRateService.findAllCurrency();
			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("currencyList", currencyList);
			this.request.setAttribute("exchangeRateList", exchangeRateList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ExchangeRateAction-listExchangeRate", e);
		}
		return ERROR;
	}
	
	/**
	 * 跳转新增/修改页面
	 */
	public String toEditExchangeRate() {
		String method = request.getParameter("method");
		try {
			if (method != null && "edit".equals(method)) {
				int exchangeRateId = Integer.parseInt(request.getParameter("exchangeRateId"));
				exchangeRate = exchangeRateService.findExchangeRateById(exchangeRateId);
			}
			this.request.setAttribute("method", method);
			currencyList = exchangeRateService.findAllCurrency();
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ExchangeRateAction-toEditExchangeRate", e);
		}
		return ERROR;
	}
	
	/**
	 * 新增/修改页面
	 * @throws Exception 
	 */
	public void editExchangeRate() throws Exception {
		String result = "";
		String method = request.getParameter("method");
		String exchangeRateId = request.getParameter("exchangeRateId");
		ExchangeRate exchangeRate1 = new ExchangeRate();
		exchangeRate1.setDataDt(request.getParameter("dataDt"));
		exchangeRate1.setBasicCcy(request.getParameter("basicCcy"));
		exchangeRate1.setCcyDate(request.getParameter("ccyDate"));
		exchangeRate1.setForwardCcy(request.getParameter("forwardCcy"));
		exchangeRate1.setConvertTyp(request.getParameter("convertTyp"));
		if (exchangeRateId != null && !"".equals(exchangeRateId)) {
			exchangeRate1.setExchangeRateId(Integer.parseInt(exchangeRateId));
		} else {
			exchangeRate1.setExchangeRateId(0);
		}
		currencyList = exchangeRateService.findAllCurrency();
		String ccyRate = request.getParameter("ccyRate");
		try {
			String rate = NumberUtils.format(new BigDecimal(ccyRate),"",2);
			exchangeRate1.setCcyRateStr(rate);
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			printOutResult("wrongRate");
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(sdf.parse(request.getParameter("ccyDate")).after(new Date())) {
			printOutResult("afterNow");
			return;
		}

		List list = exchangeRateService.findExchangeRate(exchangeRate1, method);
		if(list != null && 0 < list.size()) {
			printOutResult("repeatRate");
			return;
		}

		if(request.getParameter("basicCcy").equals(request.getParameter("forwardCcy")) && Double.parseDouble(ccyRate) != 1) {
			printOutResult("repeatCcy");
			return;
		}
		try {
			if (method != null && "add".equals(method)) {
				exchangeRateService.insertExchangeRate(exchangeRate1);
				result = "addSuccess";
			} else {
				exchangeRate1.setExchangeRateId(Integer.parseInt(exchangeRateId));
				exchangeRateService.updateExchangeRate(exchangeRate1);
				result = "editSuccess";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ExchangeRateAction-editExchangeRate", e);
			if (method != null && "add".equals(method)) {
				result = "addError";
			} else {
				result = "editError";
			}
		}
		
		printOutResult(result);
	}
	
	private void printOutResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}
	
	/**
	 * 删除汇率
	 */
	public String deleteExchangeRate() {
		try {
			exchangeRateService.deleteExchangeRate(selectExchangeRateIds);
			exchangeRate = new ExchangeRate();
			exchangeRate.setExchangeRateId(0);
			this.setResultMessages("汇率删除成功。");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ExchangeRateAction-toEditExchangeRate", e);
			this.setResultMessages("汇率删除失败。");
		}
		return ERROR;
	}
	
	/**
	 * 导出汇率
	 */
	public void exportExchangeRate() {
		try {
			exchangeRateList = exchangeRateService.findExchangeRate(exchangeRate, "export");
			currencyList = exchangeRateService.findAllCurrency();
			
			StringBuffer fileName = new StringBuffer("汇率信息列表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, exchangeRateList);
			os.flush();
			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ExchangeRateAction-toEditExchangeRate", e);
		}
	}
	
	private void writeToExcel(OutputStream os, List content) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("汇率信息", 0);
		Label header1 = new Label(i++, 0, "数据日期", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "基准币种", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "汇率日期", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "折算币种", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "折算类型", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "汇率", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
		for(int j = 0; j < 6; j++){
			ws.setColumnView(j, 18);
		}
		Map map = getCurrencyMap();
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			ExchangeRate exchangeRate1 = (ExchangeRate)content.get(c);
			int column = count++;
			setWritableSheet(ws, exchangeRate1, column, map);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, ExchangeRate exchangeRate1,
			int column, Map map) throws Exception {
		int i = 0;
		DateTime cell1 = new DateTime(i++, column, DateUtils.stringToDate(exchangeRate1.getDataDt(),"yyyy-MM-dd"), JXLTool.getContentFormatDateFormat());
		Label cell2 = new Label(i++, column, map.get(exchangeRate1.getBasicCcy()) + "",JXLTool.getContentFormat());
		DateTime cell3 = new DateTime(i++, column, DateUtils.stringToDate(exchangeRate1.getCcyDate(),"yyyy-MM-dd"),JXLTool.getContentFormatDateFormat());
		Label cell4 = new Label(i++, column, map.get(exchangeRate1.getForwardCcy()) + "", JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, DataUtil.getConvertTypCH(exchangeRate1.getConvertTyp()), JXLTool.getContentFormat());
		String ccyRate = NumberUtils.format(exchangeRate1.getCcyRate(),"",2);
		Number cell6 = new Number(i++, column, Double.parseDouble(ccyRate), JXLTool.getContentFormatNumberFloat());
		
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
	}

	private Map getCurrencyMap() {
		currencyList = exchangeRateService.findAllCurrency();
		Map map = new HashMap();
		for (int i=0; i<currencyList.size(); i++) {
			SelectTag st = (SelectTag) currencyList.get(i);
			map.put(st.getValue(), st.getText());
		}
		return map;
	}

	/**
	 * 导入汇率
	 */
	public String importExchangeRate() {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("attachmentExchangeRate");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				doImportFile(files[0]);
				files = null;
					exchangeRate = new ExchangeRate();
					
				setResultMessages(this.getMessage());
				return SUCCESS;
			} catch (Exception e) {
				log.error(e);
				this.setMessage("导入文件失败:" + e.getMessage());
				setResultMessages(message);
				return ERROR;
			}
		} else {
			this.setMessage("上传文件失败!");
			setResultMessages(message);
			return ERROR;
		}
	}
	 public static String dayAddition(int num) throws Exception{  
		         SimpleDateFormat timeformat = new SimpleDateFormat("dd/MM/yyyy");  
		         java.util.Date date = timeformat.parse("01/01/1900");  
		        Calendar a = Calendar.getInstance();  
		        a.setTime(date);  
		         a.add(Calendar.DATE, (num-2));  
		         return timeformat.format(a.getTime());  
		     }  

	private String doImportFile(File file) throws Exception {
		Workbook book = Workbook.getWorkbook(file);
		Sheet sheet1 = book.getSheet(0);
		DateCell cell0;
		DateCell cell2;
		Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
		if (hs != null) {
			// 获取excel第一个sheet页
			String[][] sheet = (String[][]) hs.get("0");
			// 从第二行开始获取每行数据
			StringBuffer repeatExchangeRate = new StringBuffer("");
			StringBuffer nullDataDt = new StringBuffer("");
			StringBuffer nullBasicCcy = new StringBuffer("");
			StringBuffer nullCcyDate = new StringBuffer("");
			StringBuffer nullForwardCcy = new StringBuffer("");
			StringBuffer nullConvertTyp = new StringBuffer("");
			StringBuffer numberFormateCcyRate = new StringBuffer("");
			for (int i = 1; i < sheet.length; i++) {
				String[] row = sheet[i];
				if (null == row[0] || "".equals(row[0])) {
					nullDataDt.append(i + ",");
				}
				if (null == row[1] || "".equals(row[1])) {
					nullBasicCcy.append(i + ",");
				}
				if (null == row[2] || "".equals(row[2])) {
					nullCcyDate.append(i + ",");
				}
				if (null == row[3] || "".equals(row[3])) {
					nullForwardCcy.append(i + ",");
				}
				if (null == row[4] || "".equals(row[4])) {
					nullConvertTyp.append(i + ",");
				}
				if (!"".equals(nullDataDt) && !"".equals(nullBasicCcy) && !"".equals(nullCcyDate) 
						&& !"".equals(nullForwardCcy) && !"".equals(nullConvertTyp)) {
					//转换单元格自定义日期格式为string类型
					cell0=(DateCell) sheet1.getCell(0, i);
					Date date = cell0.getDate();
					SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd");
					String Datedt = ds.format(date);
					
					exchangeRate = new ExchangeRate();
					exchangeRate.setDataDt(Datedt);
					exchangeRate.setBasicCcy(row[1].substring(0, 3));
					
					cell2=(DateCell) sheet1.getCell(2, i);
					Date date2 = cell2.getDate();
					String Datedt2 = ds.format(date2); 
					exchangeRate.setCcyDate(Datedt2);
					exchangeRate.setForwardCcy(row[3].substring(0, 3));
					exchangeRate.setConvertTyp(row[4].substring(0, 1));
					List list = exchangeRateService.findExchangeRate(exchangeRate, "add");
					if (0 == list.size()) {
						BigDecimal ccyRate = null;
						try {
							ccyRate = new BigDecimal(row[5]);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							numberFormateCcyRate.append(i + ",");
						}
						exchangeRate.setCcyRate(ccyRate);
						exchangeRateService.insertExchangeRate(exchangeRate);
					} else {
						repeatExchangeRate.append(i + ",");
					}
				}
			}
			message = "";
			if(0 == repeatExchangeRate.length() && 0 == nullDataDt.length() && 0 == nullBasicCcy.length() && 0 == nullCcyDate.length() 
					&& 0 == nullForwardCcy.length() && 0 == nullConvertTyp.length() && 0 == numberFormateCcyRate.length()){
				message = "导入成功.";
			} else if(0 < repeatExchangeRate.length()){
				message += "第" + (repeatExchangeRate.deleteCharAt(repeatExchangeRate.length()-1)).toString() + "行汇率已存在，不可重复添加。\\n";
			} else if(0 == nullDataDt.length()){
				message += "第" + (nullDataDt.deleteCharAt(nullDataDt.length()-1)).toString() + "行数据日期为空，未导入。\\n";
			} else if(0 == nullBasicCcy.length()){
				message += "第" + (nullBasicCcy.deleteCharAt(nullBasicCcy.length()-1)).toString() + "行数据基准币种为空，未导入。\\n";
			} else if(0 == nullCcyDate.length()){
				message += "第" + (nullCcyDate.deleteCharAt(nullCcyDate.length()-1)).toString() + "行数据汇率日期为空，未导入。\n";
			} else if(0 == nullForwardCcy.length()){
				message += "第" + (nullForwardCcy.deleteCharAt(nullForwardCcy.length()-1)).toString() + "行数据折算币种为空，未导入。\\n";
			} else if(0 == nullConvertTyp.length()){
				message += "第" + (nullConvertTyp.deleteCharAt(nullConvertTyp.length()-1)).toString() + "行数据折算类型为空，未导入。\\n";
			} else if(0 == numberFormateCcyRate.length()){
				message += "第" + (numberFormateCcyRate.deleteCharAt(numberFormateCcyRate.length()-1)).toString() + "行数据不是数字，未导入。\\n";
			}
		} else {
			this.setMessage("导入文件为空，请选择导入文件。");
		}
		return message;
	}

	public ExchangeRateService getExchangeRateService() {
		return exchangeRateService;
	}
	public void setExchangeRateService(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}
	public ExchangeRate getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(ExchangeRate exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public List getExchangeRateList() {
		return exchangeRateList;
	}
	public void setExchangeRateList(List exchangeRateList) {
		this.exchangeRateList = exchangeRateList;
	}

	public List getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List currencyList) {
		this.currencyList = currencyList;
	}

	public String[] getSelectExchangeRateIds() {
		return selectExchangeRateIds;
	}

	public void setSelectExchangeRateIds(String[] selectExchangeRateIds) {
		this.selectExchangeRateIds = selectExchangeRateIds;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
