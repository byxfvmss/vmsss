package com.cjit.vms.system.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.struts2.ServletActionContext;

import com.cjit.common.util.JXLTool;
import com.cjit.vms.system.model.AccEntry;
import com.cjit.vms.system.model.AccTitle;
import com.cjit.vms.system.model.BusinessInfo;
import com.cjit.vms.system.service.AccEntryService;
import com.cjit.vms.system.service.AccTitleService;
import com.cjit.vms.system.service.BusinessInfoService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.service.ExchangeRateService;
import com.cjit.vms.trans.util.DataFileParser;
import com.cjit.vms.trans.util.DataFileParserUtil;

public class AccEntryAction extends DataDealAction {

	private File attachment;
	private String attachmentFileName;
	private String attachmentFileType;

	private AccEntryService accEntryService;
	private AccTitleService accTitleService;
	private BusinessInfoService businessInfoService;
	private ExchangeRateService exchangeRateService;
	private AccEntry accEntry;
	// private AccEntry accEntrySearch;
	// private AccEntry accEntryEdit;
	private String gl_code;
	private String handleCurrency;

	private String[] selectAccTitleCode1;
	private List accTitList;
	private List businessTypeList;
	private List currencyList;

	public String listAccEntry() {
		AccEntry accEntrySearch = new AccEntry();
		accEntrySearch.setGl_code(gl_code);
		accEntrySearch.setCurrency(handleCurrency);

		accEntryService.findAccEntryList(accEntrySearch, paginationList);
		return SUCCESS;
	}

	public String listAccEntryGroup() {
		currencyList = exchangeRateService.findAllCurrency();
		accEntryService.findAccEntryGroupList(accEntry, paginationList);
		return SUCCESS;
	}

	public String initAccEntry() {
		accTitList = accTitleService.findAccTitleList(null, null);
		businessTypeList = businessInfoService.findBusinessInfoList(null, null);
		currencyList = exchangeRateService.findAllCurrency();

		if (null != gl_code && !"".equals(gl_code)) {
			accEntry = new AccEntry();
			// 指定code跳转到编辑画面
			AccEntry accEntrySearch = new AccEntry();
			accEntrySearch.setGl_code(gl_code);
			accEntrySearch.setCurrency(handleCurrency);
			// 根据code查询现有数据
			List list = accEntryService.findAccEntryList(accEntrySearch, null);

			if (null != list) {
				accEntry.setGl_code(gl_code);
				accEntry.setCurrency(handleCurrency);
				for (int i = 0; i < list.size(); i++) {
					// 组合actionForm
					accEntrySearch = (AccEntry) list.get(i);
					accEntry.setAccTitB(accEntrySearch
									.getAccTitleCode());
					accEntry.setTransNumTypB(accEntrySearch
									.getTransNumTyp());
					if ("N".equals(accEntrySearch.getIsReverse())) {
						if ("D".equals(accEntrySearch.getCdFlag())) {
							accEntry.setAccTitD(accEntrySearch
									.getAccTitleCode());
							accEntry.setTransNumTypD(accEntrySearch
									.getTransNumTyp());
						} else {
							if (null == accEntry.getAccTitC1()) {
								accEntry.setAccTitC1(accEntrySearch
										.getAccTitleCode());
								accEntry.setTransNumTypC1(accEntrySearch
										.getTransNumTyp());
							} else {
								accEntry.setAccTitC2(accEntrySearch
										.getAccTitleCode());
								accEntry.setTransNumTypC2(accEntrySearch
										.getTransNumTyp());
							}

						}

					}
				}
			}
			return "edit";
		} else {
			return "create";
		}

	}

	public String saveAccEntry() {

		AccEntry accEntryPar = new AccEntry();
		accEntryPar.setGl_code(accEntry.getAccTitB());
		accEntry.setGl_code(accEntry.getAccTitB());
		accEntryPar.setCurrency(accEntry.getCurrency());
		List list = accEntryService.findAccEntryGroupList(accEntryPar, null);
		if (null!=list&&list.size()>0) {
			accTitList = accTitleService.findAccTitleList(null, null);
			businessTypeList = businessInfoService.findBusinessInfoList(null, null);
			currencyList = exchangeRateService.findAllCurrency();
			setRESULT_MESSAGE("数据库已存在");
			return ERROR;
		}else{
			accEntryService.insertAccEntry(accEntry);
			setResultMessages("新增成功");
			return SUCCESS;
		}
		
	}

	public String updateAccEntry() {
		accEntryService.updateAccEntry(accEntry);
		setResultMessages("修改成功");
		return SUCCESS;
	}

	public String deleteAccEntry() {

		if (null != selectAccTitleCode1 && selectAccTitleCode1.length > 0) {
			for (int i = 0; i < selectAccTitleCode1.length; i++) {
				String[] pars = selectAccTitleCode1[i].split("_");
				accEntry.setGl_code(pars[0]);
				accEntry.setCurrency(pars[1]);
				accEntryService.deleteAccEntry(pars[0], pars[1]);
			}
		}
		setResultMessages("删除成功");
		return SUCCESS;
	}

	public String impAccEntry() {
		ServletContext sc = ServletActionContext.getServletContext();
		if (attachment != null) {
			// 若文件已存在，删除原文件
			int i = 0;
			try {
				String dir = sc.getRealPath("/WEB-INF");
				File saveFile = new File(new File(dir), attachmentFileName);
				if (saveFile.exists()) {
					saveFile.delete();
					saveFile = new File(new File(dir), attachmentFileName);
				}

				attachment.renameTo(saveFile);

				DataFileParser dataFileParserUtil = DataFileParserUtil
						.createDataFileParser(saveFile);

				if (dataFileParserUtil == null) {
					setResultMessages("请选择[.xls]后缀的文件.");
					return ERROR;
				}

				List temp = new ArrayList();
				
				String gl_code = "";
				String isReverse = "";
				String currency = "";
				String cdFlag = "";
				String accTitleCode = "";
				String transNumTyp = "";
				String messege = "上传成功";
				Map messegeMap = new LinkedHashMap();
				while (dataFileParserUtil.hasNextLine()) {
					temp = dataFileParserUtil.next();
					gl_code = null == temp.get(1) ? "" : temp.get(1)
							.toString();
					isReverse = null == temp.get(4) ? "" : temp.get(4)
							.toString();
					currency = null == temp.get(6) ? "" : temp.get(6).toString();
					cdFlag = null == temp.get(7) ? "" : temp.get(7).toString();
					accTitleCode = null == temp.get(2) ? "" : temp.get(2)
							.toString();
					transNumTyp = null == temp.get(8) ? "" : temp.get(8).toString();

					if (!"".equals(gl_code) &&  !"".equals(currency)) {
						AccEntry accEntryimp = new AccEntry();
						accEntryimp.setGl_code(gl_code);
						if(isReverse.equals("是")){
							isReverse="Y";
						}else{
							isReverse="N";
						}
						accEntryimp.setIsReverse(isReverse);
						accEntryimp.setCurrency(currency);
						if(cdFlag.equals("借方")){
							cdFlag="D";
						}else{
							cdFlag="C";
						}
						accEntryimp.setCdFlag(cdFlag);
						accEntryimp.setAccTitleCode(accTitleCode);
						accEntryimp.setTransNumTyp(transNumTyp);
						// 商品信息的取得
						List lsInfo = accEntryService.findAccEntryList(accEntryimp, null);
						if (lsInfo != null && lsInfo.size()==6) {
							messegeMap.put(gl_code+currency, "\\n 交易类型："+gl_code+" 币种："+currency +"  数据库已存在");
						} else {
							accEntryService.insertAccEntryImp(accEntryimp);
							
						}

					} else {

					}
					i++;
				}
				String [] keys = (String[]) messegeMap.keySet().toArray(new String[0]);
				for (int j = 0; j < keys.length; j++) {
					messege = messege+messegeMap.get(keys[j]);
				}
				setResultMessages(messege);
				return SUCCESS;
			} catch (Exception e) {
				setResultMessages("上传发生异常，Line："+i);
				return ERROR;
			}
			
		} else {
			setResultMessages("请选择上传文件.");
			return ERROR;
		}

	}

	public void exportAccEntry() throws Exception {

		List resultList = accEntryService.findAccEntryList(accEntry, null);

		StringBuffer fileName = new StringBuffer("分录参数设置");
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

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;

		// <th style="text-align: center">序号</th>
		// <!-- <th style="text-align: center">分录ID</th> -->
		// <th style="text-align: center">交易种类</th>
		// <th style="text-align: center">交易种类名称</th>
		// <th style="text-align: center">是否冲账</th>
		// <th style="text-align: center">币种</th>
		// <th style="text-align: center">借贷标识</th>
		// <th style="text-align: center">科目编号</th>
		// <th style="text-align: center">科目名称</th>
		// <th style="text-align: center">取值类型</th>
		ws = wb.createSheet("分录参数设置", 0);

		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "总科目编号", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "科目编号", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "科目名称", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "是否冲账", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "币种", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "币种简写", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "借贷标识", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "取值类型", JXLTool.getHeader());
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
			AccEntry accEntry = (AccEntry) content.get(c);
			int column = count++;
			setWritableSheet(ws, accEntry, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, AccEntry accEntry,
			int column) throws WriteException {
		int i = 0;

		Label cell1 = new Label(i++, column, String.valueOf(column),
				JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, accEntry.getGl_code(),
				JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, accEntry.getAccTitleCode(),
				JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, accEntry.getAccTitleName(),
				JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, accEntry.getIsReverseName(),
				JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, accEntry.getCurrencyName(),
				JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, accEntry.getCurrency(),
				JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, accEntry.getCdFlagName(),
				JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, accEntry.getTransNumTyp(),
				JXLTool.getContentFormat());
		

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

	public AccEntryService getAccEntryService() {
		return accEntryService;
	}

	public void setAccEntryService(AccEntryService accEntryService) {
		this.accEntryService = accEntryService;
	}

	public AccTitleService getAccTitleService() {
		return accTitleService;
	}

	public void setAccTitleService(AccTitleService accTitleService) {
		this.accTitleService = accTitleService;
	}

	public BusinessInfoService getBusinessInfoService() {
		return businessInfoService;
	}

	public void setBusinessInfoService(BusinessInfoService businessInfoService) {
		this.businessInfoService = businessInfoService;
	}

	public ExchangeRateService getExchangeRateService() {
		return exchangeRateService;
	}

	public void setExchangeRateService(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	public AccEntry getAccEntry() {
		return accEntry;
	}

	public void setAccEntry(AccEntry accEntry) {
		this.accEntry = accEntry;
	}

	public List getAccTitList() {
		return accTitList;
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

	public String getAttachmentFileType() {
		return attachmentFileType;
	}

	public void setAttachmentFileType(String attachmentFileType) {
		this.attachmentFileType = attachmentFileType;
	}

	public void setAccTitList(List accTitList) {
		this.accTitList = accTitList;
	}

	public List getBusinessTypeList() {
		return businessTypeList;
	}

	public void setBusinessTypeList(List businessTypeList) {
		this.businessTypeList = businessTypeList;
	}

	public List getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List currencyList) {
		this.currencyList = currencyList;
	}

	public String getGl_code() {
		return gl_code;
	}

	public void setGl_code(String gl_code) {
		this.gl_code = gl_code;
	}

	public String getHandleCurrency() {
		return handleCurrency;
	}

	public void setHandleCurrency(String handleCurrency) {
		this.handleCurrency = handleCurrency;
	}

	public String[] getSelectAccTitleCode1() {
		return selectAccTitleCode1;
	}

	public void setSelectAccTitleCode1(String[] selectAccTitleCode1) {
		this.selectAccTitleCode1 = selectAccTitleCode1;
	}

}
