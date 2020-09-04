package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cjit.vms.trans.service.VmsCommonService;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import cjit.crms.util.PropertyUtils;
import cjit.crms.util.StringUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.TransBillInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.JXLTool;

public class BillModifyAction extends BillAction {
	
	private BillInfo billCondition = new BillInfo();
	private VmsCommonService vmsCommonService;
	private Map chanNelList;
	private Map billFreqlList;
	private Map feeTypList;
	private Map dsouRceList;
	/**
	 * 进入发票编辑列表界面
	 * 
	 * @return String
	 */
	public String listBillModify() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			User currentUser = this.getCurrentUser();
			if("menu".equals(fromFlag)){
				billCondition = new BillInfo();
				fromFlag = null;
				this.paginationList.setCurrentPage(0);
				this.paginationList.setPageSize(20);
			}
			this.billCondition.setDataStatus("1");
			// 票据审核画面case追加 at lee start
			// if (StringUtil.isNotEmpty(flag) &&
			// DataUtil.AUDIT_BILL.equalsIgnoreCase(flag)) {
			// this.billInfo.setSearchFlag(flag);
			// }
			// 票据审核画面case追加 at lee end

			chanNelList = vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			billFreqlList = vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
			feeTypList = vmsCommonService.findCodeDictionary("CHARGES_TYPE");
			dsouRceList = vmsCommonService.findCodeDictionary("DATA_SOURCE");

			billCondition.setSearchFlag("modify");
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCondition.setLstAuthInstId(lstAuthInstId);
			billCondition.setUserId(currentUser.getId());
			billInfoList = billInfoService.selectBillInfoList(this.billCondition,
					currentUser.getId(), paginationList, true);

			this.request.setAttribute("billInfoList", billInfoList);
			this.request.setAttribute("paginationList", paginationList);

			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			this.request.setAttribute("currMonth", currMonth);
			this.request.getSession().setAttribute("curPage",
					String.valueOf(paginationList.getCurrentPage()));
			this.request.getSession().setAttribute("pageSize",
					String.valueOf(paginationList.getPageSize()));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillModify", e);
		}
		return ERROR;
	}

	/**
	 * 进入票据编辑界面
	 * 
	 * @return String
	 */
	public String editBill() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			billCondition.getAddressandphone();
			String billId = request.getParameter("billId");
			billInfo = billInfoService.findBillById(billId);
			Organization org = new Organization();
			org.setId(billInfo.getInstCode());
			org = (Organization) (this.organizationService
					.findOrganization(org).get(0));
			billInfo.setBankName(org.getName());
			bill = new BillInfo();
			bill.setBillId(billId);
			List ls = billInfoService.selBillAmtByBillId(bill);
			BillInfo info = (BillInfo) ls.get(0);
			billInfo.setAmtSum(info.getAmtSum());
			billInfo.setSumAmt(info.getSumAmt()); 
			billInfo.setTaxAmtSum(info.getTaxAmtSum());
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			this.fromFlag = "";
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-editBill", e);
		}
		return ERROR;
	}

	/**
	 * 查看退回原因
	 * 
	 * @return
	 */
	public String viewBackReason() {
		try {
			String billId = request.getParameter("billId");
			billInfo = billInfoService.findBillById(billId);
			this.fromFlag = "";
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-viewBackReason", e);
		}
		return ERROR;
	}

	/**
	 * 撤销仅编辑但未提交的票据
	 * 
	 * @return String
	 */
	public String revokeBill() {
		String billId = "";
		try {
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				// 循环查询选中票据信息
				for (int i = 0; i < this.selectBillIds.length; i++) {
					billId = selectBillIds[i];
					BillInfo bill = this.billInfoService.findBillById(billId);
					if (bill == null) {
						// 票据已被物理删除
						return SUCCESS;
					} else if (!DataUtil.BILL_STATUS_1.equals(bill
							.getDataStatus())) {
						// 票据已被提交
						return SUCCESS;
					}
					List lstAuthInstId = new ArrayList();
					this.getAuthInstList(lstAuthInstId);

					// 查询对应交易信息
					TransInfo transSelect = new TransInfo();
					transSelect.setSearchFlag(DataUtil.DELETE_BILL);
					transSelect.setBillId(billId);
					transSelect.setLstAuthInstId(lstAuthInstId);
					List transInfoList = this.transInfoService
							.findTransInfoList(transSelect);
					for (Iterator t = transInfoList.iterator(); t.hasNext();) {
						TransInfo trans = (TransInfo) t.next();
						// 更新交易中未开票金额，状态为未开票 &&
						// DataUtil.TRANS_STATUS_2.equals(trans.getDataStatus())
						if (trans != null && trans.getTransBillAmt() != null) {
							BigDecimal newBalance = new BigDecimal("0");
							BigDecimal newTaxAmtBalance  = new BigDecimal("0");
							if (trans.getBalance() != null) {
								newBalance = trans.getBalance();
							}
							if (trans.getTaxCnyBalance() != null) {
								newTaxAmtBalance = trans.getTaxCnyBalance();
							}

							TransBillInfo transBillInfo = this.transInfoService
									.selectTransBillInfo(billId, trans
											.getTransId());
							newBalance = newBalance.add(transBillInfo.getAmtCny());
							newTaxAmtBalance = newTaxAmtBalance.add(transBillInfo.getTaxAmtCny());
							// 税额计算
							// 抽取交易信息税额误差表
							if (transInfoService.selectTransTaxAmtErr(trans
									.getTransId()) != null
									&& trans.getBalance().compareTo(
											new BigDecimal(0.0)) != 0) {
								BigDecimal tax = DataUtil.calculateTaxAmt(
										transBillInfo.getIncomeCny(), trans
												.getTaxRate(), "base");
								BigDecimal taxErr = transBillInfo
										.getTaxAmtCny().subtract(tax).divide(
												(new BigDecimal(0.01)), 1,
												BigDecimal.ROUND_HALF_UP);
								taxErr = transInfoService.selectTransTaxAmtErr(
										trans.getTransId()).subtract(taxErr);
								// 如果误差过大则修改税额
								if (taxErr.compareTo(new BigDecimal(1)) >= 0) {
									taxErr = taxErr.subtract(new BigDecimal(1));
								} else if (taxErr.compareTo(new BigDecimal(-1)) <= 0) {
									taxErr = taxErr
											.subtract(new BigDecimal(-1));
								}
								// 重新更新交易信息税额误差表
								transInfoService.updateTransTaxAmtErr(trans
										.getTransId(), taxErr);
							}

							trans.setBalance(newBalance);
							trans.setTaxCnyBalance(newTaxAmtBalance);
							trans.setDataStatus(DataUtil.TRANS_STATUS_1);
							this.transInfoService
									.updateTransInfoForCancel(trans);

						}
					}
					// 删除交易票据对应关系
					this.transInfoService.deleteTransBill(null, bill
							.getBillId());
					// 修改该笔票据状态为0-删除作废(与废票、红冲票据的区别在于此类票据撤销标志为空)
					// bill.setDataStatus(DataUtil.BILL_STATUS_0);
					// this.billInfoService.saveBillInfo(bill, true);
					// 鉴于DataUtil.BILL_STATUS_0属性被删除，故此功能处直接删除对应票据
					this.billInfoService.deleteBillItemInfo(bill.getBillId(),
							null);
					this.billInfoService.deleteBillInfo(bill.getBillId());
					logManagerService.writeLog(request, this.getCurrentUser(),
							"0001.0010", "查询开票", "合并开票", "申请对票据ID为(" + billId
									+ ")的票据进行撤销处理", "1");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0010", "查询开票", "合并开票", "申请对票据ID为(" + billId
							+ ")的票据进行撤销处理", "0");
			log.error("BillInfoAction-revokeBill", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 批量提交票据
	 * 
	 * @return String
	 */
	public String submitBills() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return SUCCESS;
		}
		String submitBillId = "";// 执行提交的票据ID
		try {
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				// 循环查询选中票据信息
				for (int i = 0; i < this.selectBillIds.length; i++) {
					submitBillId = selectBillIds[i];
					BillInfo bill = billInfoService.findBillById(submitBillId);
					if (bill != null
							&& DataUtil.BILL_STATUS_1.equals(bill
									.getDataStatus())) {
						bill.setDataStatus(DataUtil.BILL_STATUS_2);
						User currentUser = this.getCurrentUser();
						if (currentUser != null) {
							bill.setDrawer(currentUser.getName());// 开票人
							bill.setDrawerName(currentUser.getName());// 开票人
						}
						billInfo.setCancelReason(null);
						billInfoService.saveBillInfo(bill, true);
						logManagerService.writeLog(request, this
								.getCurrentUser(), "0001.0030", "票据编辑", "批量提交",
								"对票据ID为(" + submitBillId + ")的票据进行批量提交处理", "1");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-saveBill", e);
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0030", "票据编辑", "批量提交", "对票据ID为(" + submitBillId
							+ ")的票据进行批量提交处理", "0");
		}
		return SUCCESS;
	}
	
	/**
	 * 保存/提交 票据
	 * 
	 * @return String
	 */
	public String saveBill() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			BillInfo temp = null;
			if (billInfo == null || StringUtil.IsEmptyStr(billInfo.getBillId())) {
				// 手工添加票据
				temp = new BillInfo();
				temp.setDataStatus(DataUtil.BILL_STATUS_1);
				if (billInfo == null) {
					billInfo = new BillInfo();
				}
				billInfo.setDataStatus(DataUtil.BILL_STATUS_1);
				billInfo.setIsHandiwork("1");
			} else {
				temp = billInfoService.findBillById(billInfo.getBillId());
			}
			flag = request.getParameter("flag");
			if (temp != null
					&& DataUtil.BILL_STATUS_1.equals(temp.getDataStatus())) {
				User currentUser = this.getCurrentUser();
				if (currentUser != null) {
					billInfo.setDrawer(currentUser.getId());// 开票人
					billInfo.setDrawerName(currentUser.getName());
					if (StringUtil.IsEmptyStr(billInfo.getInstCode())) {
						billInfo.setInstCode(currentUser.getOrgId());
					}
				}
				// 当操作为提交票据时
				if ("S".equals(submitFlag)) {
					billInfo.setDataStatus(DataUtil.BILL_STATUS_2);
				}
				if (StringUtil.IsEmptyStr(billInfo.getBillId())) {
					billInfo.setBillId(createBillId("B"));
					billInfoService.saveBillInfo(billInfo, false);
				} else {
					billInfoService.saveBillInfo(billInfo, true);
				}
			} else {
				PropertyUtils.copyProperties(billInfo, temp);
			}
			BillItemInfo billItem = new BillItemInfo();
			billItem.setBillId(billInfo.getBillId());
			List billItemList = billInfoService.findBillItemInfoList(billItem);
			this.request.setAttribute("billItemList", billItemList);
			if ("create".equalsIgnoreCase(flag)) {
				logManagerService.writeLog(request, this.getCurrentUser(),
						"0001.0050", "票据添加", "保存提交", "对票据ID为("
								+ billInfo.getBillId()
								+ ")的票据进行"
								+ ("S".equalsIgnoreCase(submitFlag) ? "提交处理"
										: "保存处理"), "1");
				billInfo = new BillInfo();
				return SUCCESS;
			} else {
				logManagerService.writeLog(request, this.getCurrentUser(),
						"0001.0030", "票据编辑", "保存提交", "对票据ID为("
								+ billInfo.getBillId()
								+ ")的票据进行"
								+ ("S".equalsIgnoreCase(submitFlag) ? "提交处理"
										: "保存处理"), "1");
				billInfo = new BillInfo();
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-saveBill", e);
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0030", "票据编辑", "保存提交", "对票据ID为("
							+ billInfo.getBillId()
							+ ")的票据进行"
							+ ("1".equalsIgnoreCase(submitFlag) ? "提交处理"
									: "保存处理"), "0");
			return ERROR;
		}
	}

	/**
	 * 导出
	 */
	public void billModifyQueryToExcel() throws Exception {
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
