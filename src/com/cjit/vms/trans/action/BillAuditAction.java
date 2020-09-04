package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cjit.vms.trans.service.VmsCommonService;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.JXLTool;

public class BillAuditAction extends BillAction{
	
	private BillInfo billCondition = new BillInfo();
	private VmsCommonService vmsCommonService;
	private Map chanNelList;
	private Map billFreqlList;
	private Map feeTypList;
	private Map dsouRceList;
	/**
	 * 进入可供审核票据列表界面
	 * 
	 * @return String
	 */
	public String listBillAudit() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			this.setResultMessages("");
			User currentUser = this.getCurrentUser();
			if("menu".equals(fromFlag)){
				billCondition = new BillInfo();
				fromFlag = null;
				this.paginationList.setCurrentPage(0);
				this.paginationList.setPageSize(20);
			}
			// 票据审核画面case追加 at lee start
			// if (StringUtil.isNotEmpty(flag) &&
			// DataUtil.AUDIT_BILL.equalsIgnoreCase(flag)) {
			// this.billInfo.setSearchFlag(flag);
			// }
			// 票据审核画面case追加 at lee end

			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
			feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
			dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");

			billCondition.setDataStatus("2");
			billCondition.setSearchFlag("audit");
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCondition.setLstAuthInstId(lstAuthInstId);
			billCondition.setUserId(currentUser.getId());
			billInfoList = billInfoService.selectBillInfoListAudit(this.billCondition,
					currentUser.getId(), paginationList, true);

			this.request.setAttribute("billInfoList", billInfoList);
			this.request.setAttribute("paginationList", paginationList);

			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.setAttribute("currMonth", currMonth);
			this.request.getSession().setAttribute("curPage",
					String.valueOf(paginationList.getCurrentPage()));
			this.request.getSession().setAttribute("pageSize",
					String.valueOf(paginationList.getPageSize()));
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillModify", e);
		}
		return ERROR;
	}

	/**
	 * 审核票据信息
	 * 
	 * @return String
	 */
	public String auditBill() {
		try {
			String[] billIds = request.getParameter("billId").split(",");
			if (billIds != null && billIds.length > 0) {
				User currentUser = this.getCurrentUser();
				if ("P".equals(submitFlag)) {
					// 审核通过
					// 循环查询选中票据信息
					for (int i = 0; i < billIds.length; i++) {
						billInfoService.updateBillInfoApprovedStatus(
								billIds[i], DataUtil.BILL_STATUS_3, currentUser.getName()
										);
					}
					this.setResultMessages("审核通过！");
				} else if ("R".equals(submitFlag)) {
					// 审核拒绝
					// 循环查询选中票据信息
					String cancelReason = request.getParameter("cancelReason");
					for (int i = 0; i < billIds.length; i++) {
						billInfoService.updateBillInfoDataStatus(billIds[i],
								DataUtil.BILL_STATUS_1, cancelReason);
					}
					this.setResultMessages("审核拒绝！");
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-auditBill", e);
			this.setResultMessages("审核失败！");
			return ERROR;
		}
	}
	
	/**
	 * 填写退回原因页面
	 * @return
	 */
	public String billModifyAuditingToCancelReason() {
		request.setAttribute("billId", request.getParameter("billId"));
		request.setAttribute("submitFlag", request.getParameter("submitFlag"));
		return SUCCESS;
	}
	
	/**
	 * 导出
	 */
	public void billAuditQueryToExcel() throws Exception {
		try {
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			if (StringUtil.isNotEmpty(flag)) {
				billInfo.setSearchFlag(flag);
			}
			if (currentUser != null) {
				billInfo.setUserId(currentUser.getId());
			}

			if (this.billInfo != null) {
				billInfo.setBillBeginDate(this.getBillBeginDate());
				billInfo.setBillEndDate(this.getBillEndDate());
				// 客户名称
				if (StringUtil.isNotEmpty(this.billInfo.getCustomerName())) {
					billInfo.setCustomerName(this.billInfo.getCustomerName());
				}
				// 客户纳税人识别号
				if (StringUtil.isNotEmpty(this.billInfo.getCustomerTaxno())) {
					billInfo.setCustomerTaxno(this.billInfo.getCustomerTaxno());
				}
				billInfo.setIsHandiwork(this.billInfo.getIsHandiwork());
				billInfo.setIssueType(this.billInfo.getIssueType());
				billInfo.setFapiaoType(this.billInfo.getFapiaoType());
			}

			billInfo.setDataStatus("1");
			billInfoList = billInfoService.selectBillInfoList(billInfo,
					currentUser.getId());
			StringBuffer fileName = null;
			if (DataUtil.AUDIT_BILL.equals(flag)) {
				fileName = new StringBuffer("票据审核结果表");
			} else {
				fileName = new StringBuffer("票据编辑结果表");
			}
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcelYS(os, billInfoList, flag);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public void writeToExcelYS(OutputStream os, List content, String flag)
			throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		if (DataUtil.AUDIT_BILL.equals(flag)) {
			ws = wb.createSheet("票据审核结果表", 0);
		} else {
			ws = wb.createSheet("交易冲账结果表", 0);
		}

		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "投保单号", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "保单号", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "旧保单号", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "申请开票日期", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "合计金额", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "合计税额", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "税率", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "价税合计", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "是否手工录入", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "开具类型", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "发票类型", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "状态", JXLTool.getHeader());
		Label header16 = new Label(15, 0, "渠道", JXLTool.getHeader());
		Label header17 = new Label(16, 0, "费用类型", JXLTool.getHeader());
		Label header18 = new Label(17, 0, "承保日期", JXLTool.getHeader());
		Label header19 = new Label(18, 0, "缴费频率", JXLTool.getHeader());
		Label header20 = new Label(19, 0, "数据来源", JXLTool.getHeader());

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

		for (int i = 0; i < 20; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;

		for (int i = 0; i < content.size(); i++) {
			BillInfo o = (BillInfo) content.get(i);
			int column = count++;

			setWritableSheet(ws, o, column);
		}
		wb.write();
		wb.close();
	}
	
	private void setWritableSheet(WritableSheet ws, BillInfo billInfo, int column)throws WriteException{
		int i = 0;
		//序号
		Label cell1 = new Label(i++, column,Integer.toString(column),JXLTool.getContentFormat());
		//投保单号
		Label cell2 = new Label(i++, column,billInfo.getTtmpRcno(),JXLTool.getContentFormat());
		//保单号
		Label cell3 = new Label(i++, column,billInfo.getCherNum(),JXLTool.getContentFormat());
		//旧保单号
		Label cell4 = new Label(i++, column,billInfo.getRepNum(),JXLTool.getContentFormat());
		//申请开票日期
		Label cell5 = new Label(i++, column, billInfo.getApplyDate(), JXLTool.getContentFormat());
		//客户纳税人名称
		Label cell6 = new Label(i++, column, billInfo.getCustomerName(), JXLTool.getContentFormat());
		//客户纳税人识别号customerTaxno
		Label cell7 = new Label(i++, column, billInfo.getCustomerTaxno(), JXLTool.getContentFormat());
		//合计金额
		Label cell8 = new Label(i++, column,NumberUtils.format(billInfo.getAmtSum(),"",2), JXLTool.getContentFormat());
		//合计税额
		Label cell9 = new Label(i++, column,NumberUtils.format(billInfo.getTaxAmtSum(),"",2) , JXLTool.getContentFormat());
		//税率
		Label cell10 = new Label(i++, column,NumberUtils.format(billInfo.getTaxRate(),"",2), JXLTool.getContentFormat());
		//价税合计
		Label cell11 = new Label(i++, column,NumberUtils.format(billInfo.getSumAmt(),"",2), JXLTool.getContentFormat());
		//是否手工录入
		Label cell12 = new Label(i++, column, DataUtil.getIsHandiworkCH(billInfo.getIsHandiwork()), JXLTool.getContentFormat());
		//开具类型getIssueTypeCH
		Label cell13= new Label(i++, column,DataUtil.getIssueTypeCH((billInfo.getIssueType())), JXLTool.getContentFormat());
		//发票类型
		Label cell14= new Label(i++, column,DataUtil.getFapiaoTypeCH(billInfo.getFapiaoType()), JXLTool.getContentFormat());
		//状态
		Label cell15= new Label(i++, column,DataUtil.getDataStatusCH(billInfo.getDataStatus(),"BILL"), JXLTool.getContentFormat());
		//渠道
		Label cell16= new Label(i++, column,billInfo.getChanNelCh(), JXLTool.getContentFormat());
		//费用类型
		Label cell17= new Label(i++, column,billInfo.getFeeTypCh(), JXLTool.getContentFormat());
		//承保日期
		Label cell18= new Label(i++, column,billInfo.getHissDte(), JXLTool.getContentFormat());
		//缴费频率
		Label cell19= new Label(i++, column,billInfo.getBillFreqCh(), JXLTool.getContentFormat());
		//数据来源
		Label cell20= new Label(i++, column,billInfo.getDsouRceCh(), JXLTool.getContentFormat());

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
	
	}

	public BillInfo getBillCondition() {
		return billCondition;
	}

	public void setBillCondition(BillInfo billCondition) {
		this.billCondition = billCondition;
	}

	@Override
	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	@Override
	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
	}

	public Map getChanNelList() {
		return chanNelList;
	}

	public void setChanNelList(Map chanNelList) {
		this.chanNelList = chanNelList;
	}

	public Map getBillFreqlList() {
		return billFreqlList;
	}

	public void setBillFreqlList(Map billFreqlList) {
		this.billFreqlList = billFreqlList;
	}

	public Map getFeeTypList() {
		return feeTypList;
	}

	public void setFeeTypList(Map feeTypList) {
		this.feeTypList = feeTypList;
	}

	public Map getDsouRceList() {
		return dsouRceList;
	}

	public void setDsouRceList(Map dsouRceList) {
		this.dsouRceList = dsouRceList;
	}
}
