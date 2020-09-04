package com.cjit.vms.metlife.action;

/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:进项销项会计分录 metlife
*/
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdom.output.XMLOutputter;

import cjit.crms.util.ExcelUtil;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.metlife.model.AccountingEntriesInfo;
import com.cjit.vms.metlife.model.ChargesVoucherInfo;
import com.cjit.vms.metlife.service.AccountingEntriesService;
import com.cjit.vms.system.model.LogEmp;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillItemInfo;

import com.cjit.vms.trans.model.InstInfo;

import com.cjit.vms.trans.service.TaxDiskInfoService;
import com.cjit.vms.trans.util.CheckUtil;
import com.cjit.vms.trans.util.DataFileParser;
import com.cjit.vms.trans.util.DataFileParserUtil;
import com.cjit.vms.trans.util.JXLTool;

public class AccountingEntriesAction extends DataDealAction{
		private AccountingEntriesService accountingEntriesService;
		private TaxDiskInfoService taxDiskInfoService;
		private UserInterfaceConfigService userInterfaceConfigService;
		private AccountingEntriesInfo accountingEntriesInfo=new AccountingEntriesInfo();
		private File attachment;
		private String attachmentFileName;
		private String attachmentContentType;
		private List authInstList = new ArrayList();
		protected Map chanNelList;
		protected Map flglist;
		
		//销项
		public String accountingEntriesInfo(){
			try{
			if("menu".equalsIgnoreCase(fromFlag)){
				fromFlag=null;
				accountingEntriesInfo=new AccountingEntriesInfo();
				accountingEntriesInfo.setVsadFlg("2");
				accountingEntriesInfo.setLa10Branch(this.getCurrentUser().getCustomId().toString());
			}
			String type="";
					type+=request.getParameter("type");
			if(type.equalsIgnoreCase("1")&&type!=null){
				accountingEntriesInfo.setAccountingPeriod(request.getParameter("accountingPeriod"));
				accountingEntriesInfo.setLa5Plan(request.getParameter("la5Plan"));
				accountingEntriesInfo.setLa10Branch(request.getParameter("la10Branch"));
				accountingEntriesInfo.setVsadFlg("2");
				accountingEntriesInfo.setDc("C");
			}
			InstInfo in = new InstInfo();
			in.setUserId(this.getCurrentUser().getId());
			List lstAuthInstId = new ArrayList(); 
			this.getAuthInstList(lstAuthInstId);
			in.setLstAuthInstIds(lstAuthInstId);
			authInstList = taxDiskInfoService.getInstInfoList(in);
		    chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		    flglist = this.vmsCommonService.findCodeDictionary("VMS_SALE_ACCOUNT_DETAILS");
			accountingEntriesService.findAccountingEntriesInfoSale(accountingEntriesInfo,paginationList);
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}
				return SUCCESS;
		}
		//备案生成
//		弹窗
		public String openwindow(){
			try{
			if("menu".equalsIgnoreCase(fromFlag)){
				fromFlag=null;
				accountingEntriesInfo=new AccountingEntriesInfo();
				accountingEntriesInfo.setLa10Branch(this.getCurrentUser().getCustomId().toString());
			}
			InstInfo in = new InstInfo();
			in.setUserId(this.getCurrentUser().getId());
			List lstAuthInstId = new ArrayList(); 
			this.getAuthInstList(lstAuthInstId);
			in.setLstAuthInstIds(lstAuthInstId);
			authInstList = taxDiskInfoService.getInstInfoList(in);
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}
				return SUCCESS;
		}
		public String createAccountingEntriesInfo(){
			request.setAttribute("mana", "1");
			try{
				List list=accountingEntriesService.findAccountingEntriesInfoSale1(accountingEntriesInfo);
				if(0==list.size()){
					request.setAttribute("mana", "0");
					this.setResultMessages("生成失败");
					return ERROR;
				}
				accountingEntriesService.findsaleAccountingEntriesInfo(accountingEntriesInfo);
			}catch(Exception e){
				this.setResultMessages("生成失败");
				request.setAttribute("mana", "0");
				e.printStackTrace();
				return ERROR;
			}
				
			request.setAttribute("AccountingPeriod", accountingEntriesInfo.getAccountingPeriod());
			request.setAttribute("la5Plan", accountingEntriesInfo.getLa5Plan());
			request.setAttribute("la10Branch", accountingEntriesInfo.getLa10Branch());
				this.setResultMessages("生成成功");
				return SUCCESS;
		}
		
		public String toExcelAccountingEntriesInfo(){
			try{
				List list=accountingEntriesService.findtoExcelAccountingEntriesInfo(accountingEntriesInfo);
				if(0==list.size()){
					this.setResultMessages("没有数据");
					return ERROR;
				}
				StringBuffer fileName = null;
				fileName = new StringBuffer("待备案产品分录");
				fileName.append(".xls");
				String name = "attachment;filename="
						+ URLEncoder.encode(fileName.toString(), "UTF-8")
								.toString();
				response.setHeader("Content-type", "application/vnd.ms-excel");
				response.setHeader("Content-Disposition", name);
				OutputStream os = response.getOutputStream();
				writeSunToExcel2(os, list);
				os.flush();
				os.close();
				
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}

			return SUCCESS;
		}
		public void writeSunToExcel2(OutputStream os, List list) throws Exception{
			WritableWorkbook wb = Workbook.createWorkbook(os);
			WritableSheet ws = null;
			ws = wb.createSheet("待备案产品分录", 0);
			Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
			Label header2 = new Label(1, 0, "AccountCode", JXLTool.getHeader());
			Label header3 = new Label(2, 0, "AccountingPeriod", JXLTool.getHeader());
			Label header4 = new Label(3, 0, "AnalysisCode1", JXLTool.getHeader());
			Label header5 = new Label(4, 0, "AnalysisCode2", JXLTool.getHeader());
			Label header6 = new Label(5, 0, "AnalysisCode3", JXLTool.getHeader());
			Label header7 = new Label(6, 0, "AnalysisCode5", JXLTool.getHeader());
			Label header8 = new Label(7, 0, "AnalysisCode6", JXLTool.getHeader());
			Label header9 = new Label(8, 0, "AnalysisCode7", JXLTool.getHeader());
			Label header10 = new Label(9, 0, "AnalysisCode10", JXLTool.getHeader());
			Label header11 = new Label(10, 0, "BaseAmount", JXLTool.getHeader());
			Label header12 = new Label(11, 0, "CurrencyCode", JXLTool.getHeader());
			Label header13 = new Label(12, 0, "DebitCredit", JXLTool.getHeader());
			Label header14 = new Label(13, 0, "Description", JXLTool.getHeader());
			Label header15= new Label(14, 0, "JournalSource", JXLTool.getHeader());
			Label header16 = new Label(15, 0, "TransactionAmount", JXLTool.getHeader());
			Label header17 = new Label(16, 0, "TransactionDate", JXLTool.getHeader());
			Label header18= new Label(17, 0, "TransactionReference", JXLTool.getHeader());

			

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
			for (int i = 1; i < 23; i++) {
				ws.setColumnView(i, 17);
			}
			int count = 1;

			for (int i = 0; i < list.size(); i++) {
				AccountingEntriesInfo accountingEntriesInfo = (AccountingEntriesInfo) list.get(i);
				int column = count++;

				setWritableSheetSun1(ws, accountingEntriesInfo, column);
			}
			wb.write();
			wb.close();
		}
		public void setWritableSheetSun1(WritableSheet ws, AccountingEntriesInfo accountingEntriesInfo, int column) throws Exception{
			int i = 0;
			Label cell1 = new Label(i++, column,Integer.toString(column),JXLTool.getContentFormat());
			
			Label cell3 = new Label(i++, column,accountingEntriesInfo.getAccountCode(),JXLTool.getContentFormat());
			
			Label cell2 = new Label(i++, column,accountingEntriesInfo.getAccountingPeriod(),JXLTool.getContentFormat());
			
			Label cell4 = new Label(i++, column,accountingEntriesInfo.getLa1Fund(),JXLTool.getContentFormat());
			
			Label cell5 = new Label(i++, column, accountingEntriesInfo.getLa2Channel(), JXLTool.getContentFormat());
			
			Label cell6 = new Label(i++, column,accountingEntriesInfo.getLa3Category(), JXLTool.getContentFormat());
			
			Label cell7 = new Label(i++, column,accountingEntriesInfo.getLa5Plan(), JXLTool.getContentFormat());

			Label cell8 = new Label(i++, column,accountingEntriesInfo.getLa6District(), JXLTool.getContentFormat());

			Label cell9 = new Label(i++, column,accountingEntriesInfo.getLa7Unit(), JXLTool.getContentFormat());

			Label cell10 = new Label(i++, column,accountingEntriesInfo.getLa10Branch(), JXLTool.getContentFormat());

			Label cell11 = new Label(i++, column,accountingEntriesInfo.getSumbaseAmount(), JXLTool.getContentFormat());

			Label cell12 = new Label(i++, column,accountingEntriesInfo.getCurrency(), JXLTool.getContentFormat());

			Label cell13 = new Label(i++, column,accountingEntriesInfo.getDc(), JXLTool.getContentFormat());

			Label cell14 = new Label(i++, column,accountingEntriesInfo.getTransactiondescription(), JXLTool.getContentFormat());

			Label cell15 = new Label(i++, column,accountingEntriesInfo.getJournalSource(), JXLTool.getContentFormat());

			Label cell16 = new Label(i++, column,accountingEntriesInfo.getSumtransactionAmount().toPlainString(), JXLTool.getContentFormat());

			Label cell17 = new Label(i++, column,accountingEntriesInfo.getTransactionDate(), JXLTool.getContentFormat());

			Label cell18 = new Label(i++, column,accountingEntriesInfo.getTransactionReference(), JXLTool.getContentFormat());
			
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
		//进项会计分录初始化&查询
		public String incomeAccountingEntries(){
			try{	
				if("menu".equalsIgnoreCase(fromFlag)){
					fromFlag=null;
					accountingEntriesInfo=new AccountingEntriesInfo();
				}
				InstInfo in = new InstInfo();
				in.setUserId(this.getCurrentUser().getId());
				List lstAuthInstId = new ArrayList(); 
				this.getAuthInstList(lstAuthInstId);
				in.setLstAuthInstIds(lstAuthInstId);
				authInstList = taxDiskInfoService.getInstInfoList(in);
				chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			    flglist = this.vmsCommonService.findCodeDictionary("VMS_SALE_ACCOUNT_DETAILS");
				accountingEntriesService.findAccountingEntriesInfo(accountingEntriesInfo,paginationList);
			}catch(Exception e){
				e.printStackTrace();				
				return ERROR;
			}
				return SUCCESS;
			
		}
		//进项导入
		public String importExcelIncomeAccountingEntries(){
			MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
			File[] files = mRequest.getFiles("theFile");
			if (files != null && files.length > 0) {
				try {
					if (!sessionInit(false))
						throw new Exception("初始化缓存数据失败!");
					doImportFile(files[0]);
					//this.setResultMessages("上传文件完成!");
					files = null;
					return SUCCESS;
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
					this.setResultMessages("上传文件失败:" + e.getMessage());
					return ERROR;
				}
			} else {
				this.setResultMessages("上传文件失败!");
				return ERROR;
			}
		}
		private String doImportFile(File file) throws Exception {
			try{
			List<Dictionary> headList = userInterfaceConfigService.getDictionarys1("VMS_INPUT_INACCDET", "", "");
			Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
			Map<String, String> mapBusi=new HashMap<String,String>();
			if (hs != null) {
				String[][] sheet = (String[][]) hs.get("0");
				// 获取表头列表
				String[] heads = sheet[0];
				List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
				String result="";
				String resultId="";
				Map<String,Boolean> m=new  HashMap<String, Boolean>();
				String startDate=StringUtil.getCurrentDate();
				for (int i = 1; i < sheet.length; i++) {
					String[] row = sheet[i];
					Map<String,String> map= new HashMap<String, String>();
					map=CheckUtil.CreatMap(heads, headList, row);
					System.out.println(map.get("layoutIdenitifier"));
					//result=checkformat(map, i, sheet, result);
					dataList.add(map);
					}	
				accountingEntriesService.insertInputInaccdet(dataList);
				accountingEntriesService.operationAccountingEntrise();
			}
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				this.setResultMessages("上传文件失败:" + e.getMessage());
				return ERROR;
			}
			return SUCCESS;
			
		}
		public String checkformat(Map<String,String> map,int i,String[][]sheet,String result){
			List<String> list=new ArrayList<String>();
			list.add(map.get("expenseDocNum"));
			list.add(map.get("la10Branch"));
			list.add(map.get("la2Channel"));
			Map<String, Boolean> mapCheck=new HashMap<String, Boolean>();
			//mapCheck.put("checkNull",CheckUtil.checkNotNull(list));
			mapCheck.put("checkDate", CheckUtil.checkDate(map.get("accountPeriodStrart")));
			//mapCheck.put("checkDate", CheckUtil.checkDate(map.get("transDate")));
			result=CheckUtil.checkData(mapCheck, i, result, sheet.length);
			return result;
			
		} 
		//sun 导出execel
		public String accountingSunToExecel(){
			try{
				List list=accountingEntriesService.findAccountingEntriesReports1(accountingEntriesInfo);
				StringBuffer fileName = null;
				fileName = new StringBuffer("sun文件");
				fileName.append(".xls");
				String name = "attachment;filename="
						+ URLEncoder.encode(fileName.toString(), "UTF-8")
								.toString();
				response.setHeader("Content-type", "application/vnd.ms-excel");
				response.setHeader("Content-Disposition", name);
				OutputStream os = response.getOutputStream();
				writeSunToExcel(os, list);
				os.flush();
				os.close();
				
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}
			return SUCCESS;
		}
		public void writeSunToExcel(OutputStream os, List list) throws Exception{
			WritableWorkbook wb = Workbook.createWorkbook(os);
			WritableSheet ws = null;
			ws = wb.createSheet("预算报表信息", 0);
			Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
			Label header2 = new Label(1, 0, "AccountCode", JXLTool.getHeader());
			Label header3 = new Label(2, 0, "AccountingPeriod", JXLTool.getHeader());
			Label header4 = new Label(3, 0, "AnalysisCode1", JXLTool.getHeader());
			Label header5 = new Label(4, 0, "AnalysisCode2", JXLTool.getHeader());
			Label header6 = new Label(5, 0, "AnalysisCode3", JXLTool.getHeader());
			Label header7 = new Label(6, 0, "AnalysisCode5", JXLTool.getHeader());
			Label header8 = new Label(7, 0, "AnalysisCode6", JXLTool.getHeader());
			Label header9 = new Label(8, 0, "AnalysisCode7", JXLTool.getHeader());
			Label header10 = new Label(9, 0, "AnalysisCode10", JXLTool.getHeader());
			Label header11 = new Label(10, 0, "BaseAmount", JXLTool.getHeader());
			Label header12 = new Label(11, 0, "CurrencyCode", JXLTool.getHeader());
			Label header13 = new Label(12, 0, "DebitCredit", JXLTool.getHeader());
			Label header14 = new Label(13, 0, "Description", JXLTool.getHeader());
			Label header15= new Label(14, 0, "JournalSource", JXLTool.getHeader());
			Label header16 = new Label(15, 0, "TransactionAmount", JXLTool.getHeader());
			Label header17 = new Label(16, 0, "TransactionDate", JXLTool.getHeader());
			Label header18= new Label(17, 0, "TransactionReference", JXLTool.getHeader());

			

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
			for (int i = 1; i < 23; i++) {
				ws.setColumnView(i, 17);
			}
			int count = 1;

			for (int i = 0; i < list.size(); i++) {
				AccountingEntriesInfo accountingEntriesInfo = (AccountingEntriesInfo) list.get(i);
				int column = count++;

				setWritableSheetSun(ws, accountingEntriesInfo, column);
			}
			wb.write();
			wb.close();
		}
		public void setWritableSheetSun(WritableSheet ws, AccountingEntriesInfo accountingEntriesInfo, int column) throws Exception{
			int i = 0;
			Label cell1 = new Label(i++, column,Integer.toString(column),JXLTool.getContentFormat());
			
			Label cell3 = new Label(i++, column,accountingEntriesInfo.getAccountCode(),JXLTool.getContentFormat());
			
			Label cell2 = new Label(i++, column,accountingEntriesInfo.getAccountingPeriod(),JXLTool.getContentFormat());
			
			Label cell4 = new Label(i++, column,accountingEntriesInfo.getLa1Fund(),JXLTool.getContentFormat());
			
			Label cell5 = new Label(i++, column, accountingEntriesInfo.getLa2Channel(), JXLTool.getContentFormat());
			
			Label cell6 = new Label(i++, column,accountingEntriesInfo.getLa3Category(), JXLTool.getContentFormat());
			
			Label cell7 = new Label(i++, column,accountingEntriesInfo.getLa5Plan(), JXLTool.getContentFormat());

			Label cell8 = new Label(i++, column,accountingEntriesInfo.getLa6District(), JXLTool.getContentFormat());

			Label cell9 = new Label(i++, column,accountingEntriesInfo.getLa7Unit(), JXLTool.getContentFormat());

			Label cell10 = new Label(i++, column,accountingEntriesInfo.getLa10Branch(), JXLTool.getContentFormat());

			Label cell11 = new Label(i++, column,accountingEntriesInfo.getBaseAmount().toPlainString(), JXLTool.getContentFormat());

			Label cell12 = new Label(i++, column,accountingEntriesInfo.getCurrency(), JXLTool.getContentFormat());

			Label cell13 = new Label(i++, column,accountingEntriesInfo.getDc(), JXLTool.getContentFormat());

			Label cell14 = new Label(i++, column,accountingEntriesInfo.getTransactiondescription(), JXLTool.getContentFormat());

			Label cell15 = new Label(i++, column,accountingEntriesInfo.getJournalSource(), JXLTool.getContentFormat());

			Label cell16 = new Label(i++, column,accountingEntriesInfo.getTransactionAmount().toPlainString(), JXLTool.getContentFormat());

			Label cell17 = new Label(i++, column,accountingEntriesInfo.getTransactionDate(), JXLTool.getContentFormat());

			Label cell18 = new Label(i++, column,accountingEntriesInfo.getTransactionReference(), JXLTool.getContentFormat());
			
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
		//进项sun文件

		//进项导出功能
		public String accountingToReport(){
		try{
			accountingEntriesInfo.setJournalSource("VMS");
			accountingEntriesInfo.setSubjectType("0");
			List list=accountingEntriesService.findAccountingEntriesReports(accountingEntriesInfo);
			StringBuffer fileName = null;
			fileName = new StringBuffer("预算报表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, list);
			os.flush();
			os.close();
			
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
			return SUCCESS; 
		}
		public void writeToExcel(OutputStream os, List list) throws Exception{
			WritableWorkbook wb = Workbook.createWorkbook(os);
			WritableSheet ws = null;
			ws = wb.createSheet("预算报表信息", 0);
			Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
			Label header2 = new Label(1, 0, "月份", JXLTool.getHeader());
			Label header3 = new Label(2, 0, "预算科目", JXLTool.getHeader());
			Label header4 = new Label(3, 0, "预算代码", JXLTool.getHeader());
			Label header5 = new Label(4, 0, "预算公司段", JXLTool.getHeader());
			Label header6 = new Label(5, 0, "预算成本中心", JXLTool.getHeader());
			Label header7 = new Label(6, 0, "营改增成本抵扣额", JXLTool.getHeader());
			
			ws.addCell(header1);
			ws.addCell(header2);
			ws.addCell(header3);
			ws.addCell(header4);
			ws.addCell(header5);
			ws.addCell(header6);
			ws.addCell(header7);
			for (int i = 1; i < 23; i++) {
				ws.setColumnView(i, 15);
			}
			int count = 1;

			for (int i = 0; i < list.size(); i++) {
				AccountingEntriesInfo accountingEntriesInfo = (AccountingEntriesInfo) list.get(i);
				int column = count++;

				setWritableSheet(ws, accountingEntriesInfo, column);
			}
			wb.write();
			wb.close();
		}
		//报表导出
		public void setWritableSheet(WritableSheet ws, AccountingEntriesInfo accountingEntriesInfo, int column) throws Exception{
			int i = 0;
			Label cell1 = new Label(i++, column,Integer.toString(column),JXLTool.getContentFormat());
			
			Label cell2 = new Label(i++, column,accountingEntriesInfo.getAccountingPeriod(),JXLTool.getContentFormat());
			
			Label cell3 = new Label(i++, column,accountingEntriesInfo.getBudgetSubjet(),JXLTool.getContentFormat());
			
			Label cell4 = new Label(i++, column,accountingEntriesInfo.getBudgetCode(),JXLTool.getContentFormat());
			
			Label cell5 = new Label(i++, column, accountingEntriesInfo.getBudgetCo(), JXLTool.getContentFormat());
			
			Label cell6 = new Label(i++, column,accountingEntriesInfo.getBudgetCostCenter(), JXLTool.getContentFormat());
			
			Label cell7 = new Label(i++, column,accountingEntriesInfo.getDeductionAmount().toPlainString(), JXLTool.getContentFormat());

			ws.addCell(cell1);
			ws.addCell(cell2);
			ws.addCell(cell3);
			ws.addCell(cell4);
			ws.addCell(cell5);
			ws.addCell(cell6);
			ws.addCell(cell7);
			
		}
		
		
		
		

		
		private String getSysDateYYYYMMDDHHMMSS(){
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置日期格式
			return df.format(new Date());
		}
		
		
		public AccountingEntriesService getAccountingEntriesService() {
			return accountingEntriesService;
		}
		public void setAccountingEntriesService(
				AccountingEntriesService accountingEntriesService) {
			this.accountingEntriesService = accountingEntriesService;
		}
		public TaxDiskInfoService getTaxDiskInfoService() {
			return taxDiskInfoService;
		}
		public void setTaxDiskInfoService(TaxDiskInfoService taxDiskInfoService) {
			this.taxDiskInfoService = taxDiskInfoService;
		}
		public AccountingEntriesInfo getAccountingEntriesInfo() {
			return accountingEntriesInfo;
		}
		public void setAccountingEntriesInfo(AccountingEntriesInfo accountingEntriesInfo) {
			this.accountingEntriesInfo = accountingEntriesInfo;
		}



		public File getAttachment() {
			return attachment;
		}



		public void setAttachment(File attachment) {
			this.attachment = attachment;
		}



		public String getAttachmentFileName() {
			return attachmentFileName;
		}



		public void setAttachmentFileName(String attachmentFileName) {
			this.attachmentFileName = attachmentFileName;
		}



		public String getAttachmentContentType() {
			return attachmentContentType;
		}



		public void setAttachmentContentType(String attachmentContentType) {
			this.attachmentContentType = attachmentContentType;
		}



		public List getAuthInstList() {
			return authInstList;
		}



		public void setAuthInstList(List authInstList) {
			this.authInstList = authInstList;
		}



		public Map getChanNelList() {
			return chanNelList;
		}



		public void setChanNelList(Map chanNelList) {
			this.chanNelList = chanNelList;
		}



		public Map getFlglist() {
			return flglist;
		}



		public void setFlglist(Map flglist) {
			this.flglist = flglist;
		}
		public UserInterfaceConfigService getUserInterfaceConfigService() {
			return userInterfaceConfigService;
		}
		public void setUserInterfaceConfigService(
				UserInterfaceConfigService userInterfaceConfigService) {
			this.userInterfaceConfigService = userInterfaceConfigService;
		}
		
		
		
}
