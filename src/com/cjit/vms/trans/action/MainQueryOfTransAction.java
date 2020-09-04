package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.JXLException;
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
import com.cjit.vms.system.model.Business;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.util.DataUtil;


public class MainQueryOfTransAction extends DataDealAction {

	private TransInfo transInfo=new TransInfo();
	private String[] selectTransIds;
	private List transInfoList = new ArrayList();
	private Map chanNelList;

	/** businessList 交易种类列表 */
	private List businessList;
	// 发票类型 : 1\增值税专用发票 2\海关进口增值税专用缴款书 3\代扣代缴税收通用缴款书 4\运输费用结算单据
	private List billList;

	private String transId;
	String income = "";
	String taxAmount = "";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private List transDataStatusList=new ArrayList();
	/**
	 * <p>
	 * 方法名称: listTrans|描述: 查询业务列表
	 * </p>
	 * 
//	 * @return 业务列表
	 * 
	 */
	public String mainQuery() {
		User user = this.getCurrentUser();
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			if ("menu".equalsIgnoreCase(fromFlag)) {
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.paginationList.setCurrentPage(1);
				this.request.getSession().removeAttribute("billEndDate");
				this.transInfo = new TransInfo();
				fromFlag = null;
			}
			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			// 交易日期 开始 结束
			transInfo.setTransBeginDate(this.transInfo.getTransBeginDate());
			transInfo.setTransEndDate(this.transInfo.getTransEndDate());
			if(StringUtil.IsEmptyStr(this.transInfo.getAmttwo())){
				transInfo.setAmt(new BigDecimal("0"));
			}else{
			transInfo.setAmt(new BigDecimal(this.transInfo.getAmttwo()));
			}
			if(StringUtil.IsEmptyStr(this.transInfo.getBalancetwo())){
				transInfo.setBalance(new BigDecimal("0"));
			}else{
				transInfo.setBalance(new BigDecimal(this.transInfo.getBalancetwo()));
			}
			// 交易类别
			transInfo.setTransTypeName(this.transInfo.getTransTypeName());
			// 状态（未开票、开票编辑锁定中、开票中、开票完成）
			transInfo.setDataStatus(this.transInfo.getDataStatus());
			transInfo.setFapiaoType(this.transInfo.getFapiaoType());
			
			// 客户名称
			transInfo.setCustomerName(this.transInfo.getCustomerName());
			transInfo.setCustomerTaxPayerType(this.transInfo.getCustomerTaxPayerType());
			transInfo.setTransFlag(this.transInfo.getTransFlag());
			// 交易机构
			//transInfo.setBankCode(this.transInfo.getBankCode());
			// 客户类别
//			transInfo.setTaxpayerType(this.transInfo.getTaxpayerType());
			// 发票代码
//			transInfo.setBillCode(this.transInfo.getBillCode());
			// 发票类型
			transDataStatusList=DataUtil.getTransDataStatusListForPageListMainTrans();
//			businessList = this.businessService
//					.findBusinessList(new Business());
			if (StringUtil.isNotEmpty(this.instCode)) {
				transInfo.setInstCode(this.instCode);
			}
			if (user != null) {
				transInfo.setUserId(user.getId());
			}
			/*transDataStatusList = this.userInterfaceConfigService.getDictionarys(						
					BILL_TYPE, "");	
*/			
			transInfo.setLstAuthInstId(lstAuthInstId);
			transInfoList = transInfoService.findTransInfoMainQuery(transInfo,
					paginationList);
			
			this.request.setAttribute("paginationList", paginationList);
//			this.request.setAttribute("businessList", businessList);
			this.request.setAttribute("transInfoList", transInfoList);
			this.request.setAttribute("transDataStatusList", transDataStatusList);
			
			this.request.getSession().setAttribute("transBeginDate",
					transInfo.getTransBeginDate());
			this.request.getSession().setAttribute("transEndDate",
					transInfo.getTransEndDate());

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return ERROR;
	}

	/**
	 * <p>
	 * 方法名称: viewTrans|描述: 获取交易视图信息
	 * </p>
	 */
	public String viewTransmainQuery() {
		try {
			// 交易日期 开始 结束
			transInfo.setTransBeginDate(this.getTransBeginDate());
			transInfo.setTransEndDate(this.getTransEndDate());
			// 客户名称
			transInfo.setCustomerName(this.transInfo.getCustomerName());
			// 开票日期
			transInfo.setBillBeginDate(this.getBillBeginDate());
			transInfo.setBillEndDate(this.getBillEndDate());
			// 交易类别
			transInfo.setTransTypeName(this.transInfo.getTransTypeName());
			// 状态（未开票、开票编辑锁定中、开票中、开票完成）
			transInfo.setDataStatus(this.transInfo.getDataStatus());
			// 交易机构
			String branchCode = request.getParameter("branchCode");
			// 客户类别
			String taxpayerType = request.getParameter("taxpayerType");
			// 发票代码
			String billCode = request.getParameter("billCode");
			// 发票类型
			String billType = request.getParameter("billType");
			if (this.selectTransIds != null && this.selectTransIds.length > 0) {
				// 循环查询选中交易信息
				for (int i = 0; i < this.selectTransIds.length; i++) {
//					businessList = this.businessService
//							.findBusinessList(new Business());
					TransInfo transInfo = transInfoService
							.findTransInfoMainQuery(this.selectTransIds[i]);
					if (StringUtil.isNotEmpty(this.instCode)) {
						transInfo.setInstCode(this.instCode);
					}
				}
			}

//			this.request.setAttribute("businessList", businessList);
			this.request.setAttribute("transInfo", transInfo);
			this.request.getSession().setAttribute("transBeginDate",
					transInfo.getTransBeginDate());
			this.request.getSession().setAttribute("transEndDate",
					transInfo.getTransEndDate());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	public void transMainQueryToExcel() throws Exception {
		User user = this.getCurrentUser();
		try {
			if(transInfo == null){
				transInfo = new TransInfo();
			}
			List instList = new ArrayList();
			getAuthInstList(instList);
			transInfo.setLstAuthInstId(instList);
			//transInfo=new TransInfo();
			// 交易日期 开始 结束
			transInfo.setTransBeginDate(this.getTransBeginDate());
			transInfo.setTransEndDate(this.getTransEndDate());
			// 客户名称
			transInfo.setCustomerName(this.transInfo.getCustomerName());
			// 开票日期
			transInfo.setBillBeginDate(this.getBillBeginDate());
			transInfo.setBillEndDate(this.getBillEndDate());
			// 交易类别
			transInfo.setTransTypeName(this.transInfo.getTransTypeName());
			// 状态（未开票、开票编辑锁定中、开票中、开票完成）
			transInfo.setDataStatus(this.transInfo.getDataStatus());
			// 交易机构
			String branchCode = request.getParameter("branchCode");
			// 客户类别
			String taxpayerType = request.getParameter("taxpayerType");
			// 发票代码
			String billCode = request.getParameter("billCode");
			// 发票类型
			String billType = request.getParameter("billType");
//			businessList = this.businessService
//					.findBusinessList(new Business());
			if (StringUtil.isNotEmpty(this.instCode)) {
				transInfo.setInstCode(this.instCode);
			}
			if (user != null) {
				transInfo.setUserId(user.getId());
			}

			transInfoList = transInfoService.findTransInfoMainQuery(transInfo);
						this.request.setAttribute("transInfoList", transInfoList);
			this.request.getSession().setAttribute("transBeginDate",
					transInfo.getTransBeginDate());
			this.request.getSession().setAttribute("transEndDate",
					transInfo.getTransEndDate());

			StringBuffer fileName = new StringBuffer("交易查询信息列表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(),"UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, transInfoList);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public List setwriteWidth(List list) throws JXLException{
		 List rowlist=null;
		 List sheetList=new ArrayList();
		 transInfo=null;
		for(int i=0;i<list.size();i++){
			rowlist=new ArrayList();
			transInfo=(TransInfo)list.get(i);
			rowlist.add(Integer.toString(i+1));
			rowlist.add(transInfo.getTransDate());
			rowlist.add(transInfo.getCustomerName());
			rowlist.add(transInfo.getCustomerTaxno());
			rowlist.add(transInfo.getCustomerAccount());
			rowlist.add(DataUtil.getTaxpayerTypeCH(transInfo.getCustomerTaxPayerType()));
			rowlist.add(transInfo.getTransType());
			rowlist.add(DataUtil.getTransFlag(transInfo.getTransFlag()));
			rowlist.add(DataUtil.getFapiaoFlagCH(transInfo.getTransFapiaoFlag()));
			rowlist.add(DataUtil.getVatRateCodeCH(transInfo.getVatRateCode()));
			rowlist.add(DataUtil.getYOrNCH(transInfo.getIsReverse()));
			rowlist.add(DataUtil.getFapiaoTypeCH(transInfo.getFapiaoType()));
			rowlist.add(NumberUtils.format(transInfo.getAmt(),"",2));
			rowlist.add(DataUtil.getTaxFlagCH(transInfo.getTaxFlag()) );
			rowlist.add(NumberUtils.format(transInfo.getTaxRate(),"",4));
			rowlist.add(NumberUtils.format(transInfo.getTaxAmt(),"",2));
			rowlist.add(NumberUtils.format(transInfo.getIncome(),"",2));
			rowlist.add(NumberUtils.format(transInfo.getTaxAmt().add(transInfo.getIncome()),"",2));
			rowlist.add(NumberUtils.format(transInfo.getBalance(),"",2));
			rowlist.add(DataUtil.getDataStatusCH(transInfo.getDataStatus(), "TRANS"));
			
			sheetList.add(rowlist);
		}
		return sheetList;
	}




	public void writeToExcel(OutputStream os, List content) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("交易查询信息列表", 0);
		
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "交易时间", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "投保单号", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "保单号", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "旧保单号", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "客户名称", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "客户账号", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "纳税人类型", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "交易类型", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "交易标志", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "是否打票", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "增值税类型", JXLTool.getHeader());
		Label header14= new Label(13, 0, "是否冲账	", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "发票类型", JXLTool.getHeader());
		Label header16 = new Label(15, 0, "交易金额", JXLTool.getHeader());
		Label header17 = new Label(16, 0, "是否含税", JXLTool.getHeader());
		Label header18 = new Label(17, 0, "税率", JXLTool.getHeader());
		Label header19 = new Label(18, 0, "税额", JXLTool.getHeader());
		Label header20 = new Label(19, 0, "收入", JXLTool.getHeader());
		Label header21 = new Label(20, 0, "价税合计", JXLTool.getHeader());
		Label header22= new Label(21, 0, "未开票金额	", JXLTool.getHeader());
		Label header23 = new Label(22, 0, "状态", JXLTool.getHeader());
		Label header24 = new Label(23, 0, "保单年度", JXLTool.getHeader());
		Label header25 = new Label(24, 0, "期数", JXLTool.getHeader());
		Label header26 = new Label(25, 0, "承保日期", JXLTool.getHeader());
		Label header27 = new Label(26, 0, "缴费频率", JXLTool.getHeader());

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
		ws.addCell(header22);
		ws.addCell(header23);
		ws.addCell(header24);
		ws.addCell(header25);
		ws.addCell(header26);
		ws.addCell(header27);
	
		JXLTool.setAutoWidth(ws, setwriteWidth(content));
		int count = 1;

		for(int j = 0; j < 27; j++){
			ws.setColumnView(j, 27);
		}

		for (int i = 0; i < content.size(); i++) {
			TransInfo o = (TransInfo) content.get(i);
			int column = count++;
			if(count>65535){
				break;
			}
			setWritableSheet(ws, o, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, TransInfo transInfo, int column)throws WriteException{
		int i = 0;
		Label cell1 = new Label(i++, column,Integer.toString(column),JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, transInfo.getTransDate(), JXLTool.getContentFormatDateFormat());
		Label cell3 = new Label(i++, column, transInfo.getTtmpRcno(), JXLTool.getContentFormatDateFormat());
		Label cell4 = new Label(i++, column, transInfo.getCherNum(), JXLTool.getContentFormatDateFormat());
		Label cell5 = new Label(i++, column, transInfo.getRepNum(), JXLTool.getContentFormatDateFormat());
		Label cell6 = new Label(i++, column, transInfo.getCustomerName(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, transInfo.getCustomerTaxno(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, transInfo.getCustomerAccount(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, DataUtil.getTaxpayerTypeCH(transInfo.getCustomerTaxPayerType()), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, transInfo.getTransType(), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, DataUtil.getTransFlag(transInfo.getTransFlag()), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, DataUtil.getFapiaoFlagCH(transInfo.getTransFapiaoFlag()), JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, DataUtil.getVatRateCodeCH(transInfo.getVatRateCode()), JXLTool.getContentFormat());
		Label cell14= new Label(i++, column, DataUtil.getYOrNCH(transInfo.getIsReverse()), JXLTool.getContentFormat());
		Label cell15= new Label(i++, column, DataUtil.getFapiaoTypeCH(transInfo.getFapiaoType()), JXLTool.getContentFormat());
		Label cell16= new Label(i++, column, NumberUtils.format(transInfo.getAmt(),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell17= new Label(i++, column, DataUtil.getTaxFlagCH(transInfo.getTaxFlag()) , JXLTool.getContentFormat());
		Label cell18= new Label(i++, column, NumberUtils.format(transInfo.getTaxRate(),"",4), JXLTool.getContentFormatNumberFloat());
		Label cell19= new Label(i++, column, NumberUtils.format(transInfo.getTaxAmt(),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell20= new Label(i++, column, NumberUtils.format(transInfo.getIncome(),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell21= new Label(i++, column, NumberUtils.format(transInfo.getTaxAmt().add(transInfo.getIncome()),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell22= new Label(i++, column, NumberUtils.format(transInfo.getBalance(),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell23= new Label(i++, column, DataUtil.getDataStatusCH(transInfo.getDataStatus(), "TRANS"), JXLTool.getContentFormat());
		Label cell24= new Label(i++, column, transInfo.getPolYear() == null ? "" : transInfo.getPolYear().toString(), JXLTool.getContentFormat());
		Label cell25= new Label(i++, column, transInfo.getPremTerm() == null ? "" : transInfo.getPremTerm().toString(), JXLTool.getContentFormat());
		Label cell26= new Label(i++, column, transInfo.getHissDte(), JXLTool.getContentFormat());
		Label cell27= new Label(i++, column, transInfo.getBillFreqCh(), JXLTool.getContentFormat());




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
		ws.addCell(cell19);
		ws.addCell(cell20);
		ws.addCell(cell21);
		ws.addCell(cell22);
		ws.addCell(cell23);
		ws.addCell(cell24);
		ws.addCell(cell25);
		ws.addCell(cell26);
		ws.addCell(cell27);
		
	}
	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
		
	}

	/**
	 * 导出是否收回发票差异报表
	 * @throws Exception
	 */
	public void transMainQueryWhetherToRecoverToExcel() throws Exception {
		User user = this.getCurrentUser();
		try {
			//transInfo=new TransInfo();
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			transInfo.setLstAuthInstId(lstAuthInstId);
			// 交易日期 开始 结束
			transInfo.setTransBeginDate(this.getTransBeginDate());
			transInfo.setTransEndDate(this.getTransEndDate());
			// 客户名称
			transInfo.setCustomerName(this.transInfo.getCustomerName());
			// 开票日期
			transInfo.setBillBeginDate(this.getBillBeginDate());
			transInfo.setBillEndDate(this.getBillEndDate());
			// 交易类别
			transInfo.setTransTypeName(this.transInfo.getTransTypeName());
			// 状态（未开票、开票编辑锁定中、开票中、开票完成）
			transInfo.setDataStatus(this.transInfo.getDataStatus());
			// 交易机构
			String branchCode = request.getParameter("branchCode");
			// 客户类别
			String taxpayerType = request.getParameter("taxpayerType");
			// 发票代码
			String billCode = request.getParameter("billCode");
			// 发票类型
			String billType = request.getParameter("billType");
//			businessList = this.businessService
//					.findBusinessList(new Business());
			if (StringUtil.isNotEmpty(this.instCode)) {
				transInfo.setInstCode(this.instCode);
			}
			if (user != null) {
				transInfo.setUserId(user.getId());
			}

			transInfo.setType(request.getParameter("type"));
			transInfoList = transInfoService.findTransInfoMainQuery(transInfo);
			this.request.setAttribute("transInfoList", transInfoList);
			this.request.getSession().setAttribute("transBeginDate",
					transInfo.getTransBeginDate());
			this.request.getSession().setAttribute("transEndDate",
					transInfo.getTransEndDate());

			StringBuffer fileName = new StringBuffer("是否收回发票差异报表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(),"UTF-8")
					.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeWhetherToRecoverToExcel(os, transInfoList);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	/**
	 * 导出是否收回发票差异报表
	 * @param os
	 * @param content
	 * @throws Exception
	 */
	public void writeWhetherToRecoverToExcel(OutputStream os, List content) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("是否收回发票差异报表", 0);

		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "保单号", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "险种名称", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "交易开始日期", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "交易终止日期", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "交易时间", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "客户名称", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "纳税人类型", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "交易类型", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "交易金额", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "是否含税", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "税率", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "税额", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "收入", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "价税合计", JXLTool.getHeader());
		Label header16 = new Label(15, 0, "未开票金额", JXLTool.getHeader());
		Label header17 = new Label(16, 0, "发票类型", JXLTool.getHeader());
		Label header18 = new Label(17, 0, "是否打票", JXLTool.getHeader());
		Label header19 = new Label(18, 0, "交易标志", JXLTool.getHeader());
		Label header20 = new Label(19, 0, "状态", JXLTool.getHeader());
		Label header21 = new Label(20, 0, "发票代码", JXLTool.getHeader());
		Label header22 = new Label(21, 0, "发票号码", JXLTool.getHeader());
		Label header23 = new Label(22, 0, "是否收回发票", JXLTool.getHeader());
		Label header24 = new Label(23, 0, "是否作废&红冲", JXLTool.getHeader());


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
		ws.addCell(header22);
		ws.addCell(header23);
		ws.addCell(header24);

		JXLTool.setAutoWidth(ws, setwriteWidth(content));
		int count = 1;

		if(content.size()>0){
			for (int i = 0; i < content.size(); i++) {
				TransInfo o = (TransInfo) content.get(i);
				int column = count++;
				if(count>65535){

					break;
				}
				setWritableWhetherToRecoverSheet(ws, o, column);
			}
		}
		wb.write();
		wb.close();
	}

	private void setWritableWhetherToRecoverSheet(WritableSheet ws, TransInfo transInfo, int column)throws WriteException{
		int i = 0;
		Label cell1 = new Label(i++, column,Integer.toString(column),JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, transInfo.getCherNum(), JXLTool.getContentFormatDateFormat());
		Label cell3 = new Label(i++, column, transInfo.getTransTypeName(), JXLTool.getContentFormatDateFormat());
		Label cell4 = new Label(i++, column, transInfo.getTransBeginDate(), JXLTool.getContentFormatDateFormat());
		Label cell5 = new Label(i++, column, transInfo.getTransEndDate(), JXLTool.getContentFormatDateFormat());
		Label cell6 = new Label(i++, column, transInfo.getTransDate(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, transInfo.getCustomerName(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, DataUtil.getTaxpayerTypeCH(transInfo.getCustomerTaxPayerType()), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, transInfo.getTransType(), JXLTool.getContentFormat());
		Label cell10= new Label(i++, column, NumberUtils.format(transInfo.getAmt(),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell11= new Label(i++, column, DataUtil.getTaxFlagCH(transInfo.getTaxFlag()) , JXLTool.getContentFormat());
		Label cell12= new Label(i++, column, NumberUtils.format(transInfo.getTaxRate(),"",4), JXLTool.getContentFormatNumberFloat());
		Label cell13= new Label(i++, column, NumberUtils.format(transInfo.getTaxAmt(),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell14= new Label(i++, column, NumberUtils.format(transInfo.getIncome(),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell15= new Label(i++, column, NumberUtils.format(transInfo.getTaxAmt().add(transInfo.getIncome()),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell16= new Label(i++, column, NumberUtils.format(transInfo.getBalance(),"",2), JXLTool.getContentFormatNumberFloat());
		Label cell17= new Label(i++, column, DataUtil.getFapiaoTypeCH(transInfo.getFapiaoType()), JXLTool.getContentFormat());
		Label cell18 = new Label(i++, column, DataUtil.getFapiaoFlagCH(transInfo.getTransFapiaoFlag()), JXLTool.getContentFormat());
		Label cell19 = new Label(i++, column, DataUtil.getTransFlag(transInfo.getTransFlag()), JXLTool.getContentFormat());
		Label cell20= new Label(i++, column, DataUtil.getDataStatusCH(transInfo.getDataStatus(), "TRANS"), JXLTool.getContentFormat());
		Label cell21 = new Label(i++, column, transInfo.getBillCode(), JXLTool.getContentFormat());
		Label cell22 = new Label(i++, column, transInfo.getBillNo(), JXLTool.getContentFormat());
		Label cell23= new Label(i++, column, "", JXLTool.getContentFormat());
		Label cell24= new Label(i++, column, DataUtil.getDataSrarus(transInfo.getBillDatastatus()), JXLTool.getContentFormat());




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
		ws.addCell(cell19);
		ws.addCell(cell20);
		ws.addCell(cell21);
		ws.addCell(cell22);
		ws.addCell(cell23);
		ws.addCell(cell24);

	}


	/*private void setWritableSheet(WritableSheet ws, Map o, int column)
			throws WriteException {
		Label cell1 = new Label(0, column, o.get("transDate") == null
				|| o.get("transDate").equals("") ? "" : o.get("transDate")
				.toString(), JXLTool.getContentFormat());
		// transType
		Label cell2 = new Label(1, column, o.get("businessCName") == null
				|| o.get("businessCName").equals("") ? "" : o.get(
				"businessCName").toString(), JXLTool.getContentFormat());
		// amtCny
		Label cell3 = new Label(2, column,
				o.get("amtCny") == null || o.get("amtCny").equals("") ? "" : o
						.get("amtCny").toString(), JXLTool.getContentFormat());
		// taxRate
		Label cell4 = new Label(3, column, o.get("taxRate") == null
				|| o.get("taxRate").equals("") ? "" : o.get("taxRate")
				.toString(), JXLTool.getContentFormat());
		// taxAmt
		Label cell5 = new Label(4, column,
				o.get("taxAmt") == null || o.get("taxAmt").equals("") ? "" : o
						.get("taxAmt").toString(), JXLTool.getContentFormat());
		// sumAmt
		Label cell6 = new Label(5, column,
				o.get("sumAmt") == null || o.get("sumAmt").equals("") ? "" : o
						.get("sumAmt").toString(), JXLTool.getContentFormat());

		// branchCode
		Label cell7 = new Label(6, column, o.get("branchCode") == null
				|| o.get("branchCode").equals("") ? "" : o.get("branchCode")
				.toString(), JXLTool.getContentFormat());
		// departCode
		Label cell8 = new Label(7, column, o.get("departCode") == null
				|| o.get("departCode").equals("") ? "" : o.get("departCode")
				.toString(), JXLTool.getContentFormat());
		// customerCode
		Label cell9 = new Label(8, column, o.get("customerCode") == null
				|| o.get("customerCode").equals("") ? "" : o
				.get("customerCode").toString(), JXLTool.getContentFormat());
		// customerCName
		Label cell10 = new Label(9, column, o.get("customerCName") == null
				|| o.get("customerCName").equals("") ? "" : o.get(
				"customerCName").toString(), JXLTool.getContentFormat());
		// customerTaxNo
		Label cell11 = new Label(10, column, o.get("customerTaxNo") == null
				|| o.get("customerTaxNo").equals("") ? "" : o.get(
				"customerTaxNo").toString(), JXLTool.getContentFormat());
		// taxpayerType
		Label cell12 = new Label(11, column, o.get("taxpayerType") == null
				|| o.get("taxpayerType").equals("") ? "" : o
				.get("taxpayerType").toString(), JXLTool.getContentFormat());
		// customerOffice
		Label cell13 = new Label(12, column, o.get("customerOffice") == null
				|| o.get("customerOffice").equals("") ? "" : o.get(
				"customerOffice").toString(), JXLTool.getContentFormat());
		// billCode
		Label cell14 = new Label(13, column, o.get("billCode") == null
				|| o.get("billCode").equals("") ? "" : o.get("billCode")
				.toString(), JXLTool.getContentFormat());
		// billNo
		Label cell15 = new Label(14, column,
				o.get("billNo") == null || o.get("billNo").equals("") ? "" : o
						.get("billNo").toString(), JXLTool.getContentFormat());
		// 发票类型 不知道是什么 暂时这么写
		Label cell16 = new Label(15, column, o.get("billType") == null
				|| o.get("billType").equals("") ? "" : o.get("billType")
				.toString(), JXLTool.getContentFormat());
		// dataStatus
		Label cell17 = new Label(16, column, o.get("dataStatus") == null
				|| o.get("dataStatus").equals("") ? "" : o.get("dataStatus")
				.toString(), JXLTool.getContentFormat());
		// billDate
		Label cell18 = new Label(17, column, o.get("billDate") == null
				|| o.get("billDate").equals("") ? "" : o.get("billDate")
				.toString(), JXLTool.getContentFormat());
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
	}*/

	public TransInfo getTransInfo() {
		return transInfo;
	}

	public void setTransInfo(TransInfo transInfo) {
		this.transInfo = transInfo;
	}

//	public List getBusinessList() {
//		return businessList;
//	}
//
//	public void setBusinessList(List businessList) {
//		this.businessList = businessList;
//	}

	public List getTransDataStatusList() {
		return transDataStatusList;
	}

	public void setTransDataStatusList(List transDataStatusList) {
		this.transDataStatusList = transDataStatusList;
	}

	public String[] getSelectTransIds() {
		return selectTransIds;
	}

	public void setSelectTransIds(String[] selectTransIds) {
		this.selectTransIds = selectTransIds;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public Map getChanNelList() {
		return chanNelList;
	}

	public void setChanNelList(Map chanNelList) {
		this.chanNelList = chanNelList;
	}

	public List getBillList() {
		return billList;
	}

	public void setBillList(List billList) {
		this.billList = billList;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public List getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List businessList) {
		this.businessList = businessList;
	}
}
