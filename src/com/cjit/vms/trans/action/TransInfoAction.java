package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import cjit.crms.util.StringUtil;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.JXLTool;
import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.customer.model.SubCustomer;
import com.cjit.vms.system.model.Business;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.TransInfoYS;
import com.cjit.vms.trans.model.trans.ConnCust;
import com.cjit.vms.trans.util.DataUtil;

public class TransInfoAction extends TransAction {
	/* ======================ys start================= */
	// 数据来源列表
	protected List kbcDataSouceList = new ArrayList();

	/**
	 * 菜单进入，查询冲账交易信息列表（查询冲账标识字段为Y的记录）
	 * 
	 * @return String
	 */
	public String transReverseList() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				paginationList.setCurrentPage(1);
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			} else if ("list".equalsIgnoreCase(fromFlag)) {
				this.transInfoYS.setCustomerName(null);
				this.request.getSession().removeAttribute(
						"transInfoYS.customerName");
				this.transInfoYS.setTransType(null);
				this.request.getSession().removeAttribute(
						"transInfoYS.transType");
				this.transInfoYS.setAmtMin(null);
				this.request.getSession().removeAttribute("transInfoYS.amtMin");
				this.transInfoYS.setAmtMax(null);
				this.request.getSession().removeAttribute("transInfoYS.amtMax");
				fromFlag = null;
				this.transInfoYS = null;
			} else {
				String message = (String) this.request.getAttribute("message");
				if ("success".equalsIgnoreCase(message)) {
					this.message = "冲账成功！";
				}
			}
			TransInfoYS transInfoYS = new TransInfoYS();
			transInfoYS.setTransEndDate(this.getTransEndDate());
			transInfoYS.setTransBeginDate(this.getTransBeginDate());
			transInfoYS.setCustomerName(this.getCustomerName());
			// transInfoYS.setCustomerTaxno(this.transInfoYS.getCustomerTaxno());
			transInfoYS.setIsReverse("Y");
			transInfoYS.setDataStatus("1");
			if (this.transInfoYS != null) {
				// 客户名称
				if (StringUtil.isNotEmpty(this.transInfoYS.getCustomerName())) {
					transInfoYS.setCustomerName(this.transInfoYS
							.getCustomerName());
				}
				// 纳税人类型
				transInfoYS.setTaxpayerType(this.transInfoYS.getTaxpayerType());
				// 交易类型
				transInfoYS.setBusinessCname(this.transInfoYS
						.getBusinessCname());
				// 交易金额
				String amtMax = this.request
						.getParameter("transInfoYS.amtMaxStr");
				String amtMin = this.request
						.getParameter("transInfoYS.amtMinStr");
				transInfoYS.setAmtMinStr(amtMin);
				transInfoYS.setAmtMaxStr(amtMax);
				// 未开票金额
				String balanceMin = this.request
						.getParameter("transInfoYS.balanceMinStr");
				String balanceMax = this.request
						.getParameter("transInfoYS.balanceMaxStr");
				transInfoYS.setBalanceMinStr(balanceMin);
				transInfoYS.setBalanceMaxStr(balanceMax);
			}
			transInfoList = transInfoService.selectTransInfoList(transInfoYS,
					paginationList);
			transDataKindList = DataUtil.getYesOrNoListForPageListTrans();
			// 纳税人类型下拉列表
			custTaxPayerTypeList = this.createSelectList(
					DataUtil.TAXPAYER_TYPE, null, true);
			// 查询kbc数据来源
			kbcDataSouceList = DataUtil.getKbcDataSouceList();
			this.request.setAttribute("custTaxPayerTypeList",
					custTaxPayerTypeList);
			this.request.setAttribute("transInfoList", transInfoList);
			this.request.setAttribute("transDataKindList", transDataKindList);
			this.request.getSession().setAttribute("transBeginDate",
					transInfoYS.getTransBeginDate());
			this.request.getSession().setAttribute("transEndDate",
					transInfoYS.getTransEndDate());
			this.request.setAttribute("kbcDataSouceList", kbcDataSouceList);
			// this.transInfoYS = null;
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "0");
			log.error("TransInfoAction-listTransReverse", e);
		}
		return ERROR;
	}

	/**
	 * 交易冲账列表页点击详情按钮
	 * 
	 * @return
	 */
	public String transEdit() {
		String temp = (String) request.getParameter("transId");
		TransInfoYS transInfoYS = new TransInfoYS();
		transInfoYS.setTransId(temp);
		transEditList = transInfoService.selectTransInfoList(transInfoYS);
		this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
		return SUCCESS;
	}

	/**
	 * 进入冲账列表
	 * 
	 * @return String
	 */
	public String transToReverse1() {
		try {

			// String reverseTransId = (String)
			// request.getParameter("reverseTransId");
			// 获取已冲账 交易ID
			transId = (String) request.getParameter("transId");
			if ("list2".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			if ("list".equalsIgnoreCase(fromFlag)) {
				this.transInfoYS = null;
			}
			List normalTransList = new ArrayList();// 准备冲账的交易集合
			// 设定查询条件：交易ID
			TransInfoYS transInfoYS1 = new TransInfoYS();
			transInfoYS1.setTransId(transId);
			// 查询kbc数据来源
			kbcDataSouceList = DataUtil.getKbcDataSouceList();
			// 查询已冲账交易
			transEditList = transInfoService.selectTransInfoList(transInfoYS1);
			// 获取唯一已冲账交易
			transInfoYS1 = (TransInfoYS) transEditList.get(0);

			// 设定相同客户未冲账交易查询条件：已冲账交易的交易时间，客户号
			TransInfoYS tempTrans = new TransInfoYS();
			// 冲账类型：未冲账
			tempTrans.setIsReverse("N");
			tempTrans.setTransDate(transInfoYS1.getTransDate());
			// tempTrans.setTransType(transInfoYS1.getTransType());
			tempTrans.setCustomerId(transInfoYS1.getCustomerId());

			// 设定页面传入查询条件
			if (transInfoYS != null) {
				if (StringUtil.isNotEmpty(this.transInfoYS.getCustomerName())) {
					tempTrans.setCustomerName(this.transInfoYS
							.getCustomerName());
				}
				String amtMax = this.request
						.getParameter("transInfoYS.amtMaxStr");
				String amtMin = this.request
						.getParameter("transInfoYS.amtMinStr");
				tempTrans.setTransBeginDate(this.transInfoYS
						.getTransBeginDate());
				tempTrans.setTransEndDate(this.transInfoYS.getTransEndDate());
				tempTrans.setAmtMinStr(amtMin);
				tempTrans.setAmtMaxStr(amtMax);
				tempTrans.setTransType(this.transInfoYS.getTransType());
				tempTrans.setBusinessCname(this.transInfoYS.getBusinessCname());
				tempTrans.setCustomerId(this.transInfoYS.getCustomerId());
			}
			// 查询相同客户的冲账交易记录
			normalTransList = transInfoService.selectTransToTrans(tempTrans);
			this.request.setAttribute("normalTransList", normalTransList);
			this.request.setAttribute("transInfoYS", transInfoYS);
			this.request.setAttribute("kbcDataSouceList", kbcDataSouceList);
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TransInfoAction-transToReverse", e);
		}
		return ERROR;
	}

	/**
	 * 执行交易冲账功能
	 * 
	 * @return String
	 */
	public String transToReverse2() {
		String transSelectedId = (String) request
				.getParameter("transSelectedId");
		TransInfoYS transInfo1 = transInfoService
				.selectTransInfoById(transSelectedId);
		transInfo1.setDataStatus("5");
		transInfoService.updateTransInfoYS(transInfo1);
		String reverseTransId = transInfo1.getTransBusId();
		String transId = (String) request.getParameter("transId");
		TransInfoYS transInfo2 = transInfoService.selectTransInfoById(transId);
		transInfo2.setDataStatus("6");
		transInfo2.setReverseTransId(reverseTransId);
		transInfoService.updateTransInfoYS(transInfo2);
		return SUCCESS;
	}

	/**
	 * 导出
	 */
	public void transInfoQueryToExcel() throws Exception {
		try {

			transInfoYS.setIsReverse("Y");
			transInfoYS.setDataStatus("1");
			if (this.transInfoYS != null) {
				// 客户名称
				if (StringUtil.isNotEmpty(this.transInfoYS.getCustomerName())) {
					transInfoYS.setCustomerName(this.transInfoYS
							.getCustomerName());
				}
				// 纳税人类型
				transInfoYS.setTaxpayerType(this.transInfoYS.getTaxpayerType());
				// 交易种类
				transInfoYS.setTransTypeName(this.transInfoYS
						.getTransTypeName());
				// 交易金额
				String amtMax = this.request
						.getParameter("transInfoYS.amtMaxStr");
				String amtMin = this.request
						.getParameter("transInfoYS.amtMinStr");
				transInfoYS.setAmtMinStr(amtMin);
				transInfoYS.setAmtMaxStr(amtMax);
				// 未开票金额
				String balanceMin = this.request
						.getParameter("transInfoYS.balanceMinStr");
				String balanceMax = this.request
						.getParameter("transInfoYS.balanceMaxStr");
				transInfoYS.setBalanceMinStr(balanceMin);
				transInfoYS.setBalanceMaxStr(balanceMax);
			}
			transInfoList = transInfoService.selectTransInfoList(transInfoYS,
					paginationList);

			StringBuffer fileName = new StringBuffer("交易冲账结果表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, transInfoList);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("交易冲账结果表", 0);

		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "交易时间", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "客户名称", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "客户账号", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "纳税人类型", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "交易类型", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "交易标志", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "是否打票", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "增值税种类", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "是否冲账", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "发票类型", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "交易金额", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "是否含税", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "税率", JXLTool.getHeader());
		Label header16 = new Label(15, 0, "税额", JXLTool.getHeader());
		Label header17 = new Label(16, 0, "收入", JXLTool.getHeader());
		Label header18 = new Label(17, 0, "价税合计", JXLTool.getHeader());
		Label header19 = new Label(18, 0, "未开票金额", JXLTool.getHeader());
		Label header20 = new Label(19, 0, "状态", JXLTool.getHeader());

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
			TransInfoYS o = (TransInfoYS) content.get(i);
			int column = count++;

			setWritableSheet(ws, o, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, TransInfoYS transInfoYS,
			int column) throws WriteException {
		int i = 0;
		// 序号
		Label cell1 = new Label(i++, column, Integer.toString(column), JXLTool
				.getContentFormat());
		// 交易时间
		Label cell2 = new Label(i++, column, transInfoYS.getTransDate(),
				JXLTool.getContentFormat());
		// 客户名称
		Label cell3 = new Label(i++, column, transInfoYS.getCustomerName(),
				JXLTool.getContentFormat());
		// 客户纳税人识别号customerTaxno
		Label cell4 = new Label(i++, column, transInfoYS.getCustomerTaxno(),
				JXLTool.getContentFormat());
		// 客户账号customerAccount
		Label cell5 = new Label(i++, column, transInfoYS.getCustomerAccount(),
				JXLTool.getContentFormat());
		// 纳税人类型
		Label cell6 = new Label(i++, column, DataUtil
				.getTaxpayerTypeCH(transInfoYS.getTaxpayerType()), JXLTool
				.getContentFormat());
		// 交易类型
		Label cell7 = new Label(i++, column, transInfoYS.getBusinessCname(),
				JXLTool.getContentFormat());
		// 交易标志
		Label cell8 = new Label(i++, column, DataUtil.getTaxFlagCH(transInfoYS
				.getTaxFlag()), JXLTool.getContentFormat());
		// 是否打票
		Label cell9 = new Label(i++, column, DataUtil
				.getFapiaoFlagCH(transInfoYS.getTransFapiaoFlag()), JXLTool
				.getContentFormat());
		// 增值税种类
		Label cell10 = new Label(i++, column, DataUtil
				.getVatRateCodeCH(transInfoYS.getVatRateCode()), JXLTool
				.getContentFormat());
		// 是否冲账
		Label cell11 = new Label(i++, column, DataUtil
				.getIsReverseCH(transInfoYS.getIsReverse()), JXLTool
				.getContentFormat());
		// 发票类型
		Label cell12 = new Label(i++, column, DataUtil
				.getFapiaoTypeCH(transInfoYS.getFapiaoType()), JXLTool
				.getContentFormat());
		// 交易金额
		Label cell13 = new Label(i++, column, transInfoYS.getAmtCny()
				.toString(), JXLTool.getContentFormat());
		// 是否含税
		Label cell14 = new Label(i++, column, DataUtil.getTaxFlagCH(transInfoYS
				.getTaxFlag()), JXLTool.getContentFormat());
		// 税率
		Label cell15 = new Label(i++, column, NumberUtils.format(transInfoYS
				.getTaxRate(), "", 4), JXLTool.getContentFormat());
		// 税额
		Label cell16 = new Label(i++, column, NumberUtils.format(transInfoYS
				.getTaxAmtCny(), "", 2), JXLTool.getContentFormat());
		// 收入
		Label cell17 = new Label(i++, column, NumberUtils.format(transInfoYS
				.getInComeCny(), "", 2), JXLTool.getContentFormat());
		// 价税合计
		Label cell18 = new Label(i++, column, NumberUtils.format((transInfoYS
				.getInComeCny().add(transInfoYS.getTaxAmtCny())), "", 2),
				JXLTool.getContentFormat());
		// 未开票金额
		Label cell19 = new Label(i++, column, transInfoYS.getBalance()
				.toString(), JXLTool.getContentFormat());
		// 状态
		Label cell20 = new Label(i++, column, DataUtil.getDataStatusCH(
				transInfoYS.getDataStatus(), "TRANS"), JXLTool
				.getContentFormat());

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

	/* ======================ys end================= */


	/**
	 * 展示交易对应所有开票明细
	 * 
	 * @return String
	 */
	public String transDetail() {
		this.setMessage(null);
		String transId = request.getParameter("transId");
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			// 查询交易信息
			BillInfo info = new BillInfo();
			// info.setBillId(transId);
			info.setTransId(transId);
			info.setLstAuthInstId(lstAuthInstId);
//			// 获取相关联的客户ID
//			TransInfo transInfo = new TransInfo();
//			transInfo.setTransId(transId);
//			List list = transInfoService.findTransCustomerList(transInfo);
//			// 去重复
//			Map map = new HashMap();
//			for (int j = 0; j < list.size(); j++) {
//				String customerId = ((Customer) list.get(j)).getCustomerID();
//				map.put(customerId, customerId);
//			}
//			Object transCustomerList[] = map.keySet().toArray();
//			// transInfo.setTransCustomerList(transCustomerList);
//			info.setCustomerList(transCustomerList);
			TransInfo trans = this.transInfoService.findTransInfo(info);
			if (trans != null) {
				// 按开票日期查询该笔交易所有开票记录
				List billList = this.billInfoService
						.findBillInfoList(new BillInfo(null, null, null, null,
								null, transId));
				for (Iterator t = billList.iterator(); t.hasNext();) {
					BillInfo bill = (BillInfo) t.next();
					List billItemList = this.billInfoService
							.findBillItemInfoList(new BillItemInfo(bill
									.getBillId(), null));
					bill.setBillItemList(billItemList);
				}
				trans.setBillList(billList);
				if (trans.getRemark() == null) {
					trans.setRemark("");
				}
			}
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			this.request.setAttribute("transInfo", trans);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TransInfoAction-transDetail", e);
		}
		return ERROR;
	}

	/**
	 * 菜单进入，查询冲账交易信息列表（查询冲账标识字段为Y的记录）
	 * 
	 * @return String
	 */
	public String listTransReverse() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			} else {
				String message = (String) this.request.getAttribute("message");
				if ("success".equalsIgnoreCase(message)) {
					this.message = "冲账成功！";
				}
			}
			TransInfo transInfo = new TransInfo();
			// transInfo.setTransBeginDate(this.getTransBeginDate());
			// transInfo.setTransEndDate(this.getTransEndDate());
			transInfo.setIsReverse("Y");
			if (this.transInfo != null) {
				if (StringUtil.isNotEmpty(this.transInfo.getCustomerId())) {
					transInfo.setCustomerId(this.transInfo.getCustomerId());
				}
				// if (StringUtil.isNotEmpty(this.transInfo
				// .getCustomerOfficeCode())) {
				// transInfo.setCustomerOfficeCode(this.transInfo
				// .getCustomerOfficeCode());
				// }
			}
			if (StringUtil.isNotEmpty(this.instCode)) {
				transInfo.setInstCode(this.instCode);
			}
			//businessList = this.baseDataService.findBusinessList(new Business());
			// 查询kbc数据来源
			kbcDataSouceList = DataUtil.getKbcDataSouceList();
//			// 获取相关联的客户ID
//			List list = transInfoService.findTransCustomerList(transInfo);
//			// 去重复
//			Map map = new HashMap();
//			for (int j = 0; j < list.size(); j++) {
//				String customerId = ((Customer) list.get(j)).getCustomerID();
//				map.put(customerId, customerId);
//			}
//			Object transCustomerList[] = map.keySet().toArray();
//			transInfo.setTransCustomerList(transCustomerList);
			transInfoList = transInfoService.findTransInfoList(transInfo,
					paginationList);
			transDataKindList = DataUtil.getYesOrNoListForPageListTrans();
			this.request.setAttribute("businessList", businessList);
			this.request.setAttribute("transInfoList", transInfoList);
			this.request.setAttribute("transDataKindList", transDataKindList);
			this.request.setAttribute("kbcDataSouceList", kbcDataSouceList);
			// this.request.getSession().setAttribute("transBeginDate",
			// transInfo.getTransBeginDate());
			// this.request.getSession().setAttribute("transEndDate",
			// transInfo.getTransEndDate());
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "1");
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "0");
			log.error("TransInfoAction-listTransReverse", e);
		}
		return ERROR;
	}

	/**
	 * 执行交易冲账功能
	 * 
	 * @return String
	 */
	public String transToReverse() {
		// String transIds = "";
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			String reverseTransId = (String) request
					.getParameter("reverseTransId");
			String oper = (String) request.getParameter("oper");
			List normalTransList = new ArrayList();// 准备冲账的交易集合
			// 冲账交易记录
			BillInfo info = new BillInfo();
			info.setTransId(reverseTransId);
			info.setLstAuthInstId(lstAuthInstId);
			TransInfo transInfo = transInfoService.findTransInfo(info);
			if (this.selectTransIds != null && this.selectTransIds.length > 0
					&& "reverse".equals(oper)) {
				// 已选择要进行冲账的正常交易记录
				for (int i = 0; i < this.selectTransIds.length; i++) {
					BillInfo infos = new BillInfo();
					infos.setTransId(this.selectTransIds[i]);
					infos.setLstAuthInstId(lstAuthInstId);
//					// 获取相关联的客户ID
//					List list = transInfoService
//							.findTransCustomerList(transInfo);
//					// 去重复
//					Map map = new HashMap();
//					for (int j = 0; j < list.size(); j++) {
//						String customerId = ((Customer) list.get(j))
//								.getCustomerID();
//						map.put(customerId, customerId);
//					}
//					Object transCustomerList[] = map.keySet().toArray();
//					// transInfo.setTransCustomerList(transCustomerList);
//					infos.setCustomerList(transCustomerList);
					TransInfo tempTrans = transInfoService.findTransInfo(infos);
					if (this.reverseEntry(tempTrans, reverseTransId)) {
						// 被执行冲账的交易记录ID
						// transIds += tempTrans.getTransId() + ",";
					}
				}
			} else if (transInfo != null) {
				// 进入冲账选择界面，依冲账记录客户编号、客户部门查询在冲账记录交易日期之前的可进行冲账的记录
				TransInfo tempTrans = new TransInfo();
				tempTrans.setCustomerId(transInfo.getCustomerId());
				// tempTrans.setCustomerOfficeCode(transInfo
				// .getCustomerOfficeCode());
				// tempTrans.setTransEndDate(transInfo.getTransDate());
				tempTrans.setIsReverse("N");
				tempTrans.setLstAuthInstId(lstAuthInstId);
//				// 获取相关联的客户ID
//				List list = transInfoService.findTransCustomerList(transInfo);
//				// 去重复
//				Map map = new HashMap();
//				for (int j = 0; j < list.size(); j++) {
//					String customerId = ((Customer) list.get(j))
//							.getCustomerID();
//					map.put(customerId, customerId);
//				}
//				Object transCustomerList[] = map.keySet().toArray();
//				// transInfo.setTransCustomerList(transCustomerList);
//				tempTrans.setTransCustomerList(transCustomerList);
				// 查询相同客户的冲账交易记录
				normalTransList = transInfoService.findTransInfoList(tempTrans);
				if (CollectionUtil.isEmpty(normalTransList)) {
					this.message = "相同客户无可供冲账的记录！";
					return ERROR;
				}
				this.request.setAttribute("normalTransList", normalTransList);
				this.request.setAttribute("transInfo", transInfo);
			}
			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0001.0010", "查询开票", "交易冲账", "将交易流水号为("
			// + transIds.substring(0, transIds.length() - 1)
			// + ")的交易合并开票", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			// logManagerService.writeLog(request, this.getCurrentUser(),
			// "0001.0010", "查询开票", "交易冲账", "将交易流水号为("
			// + transIds.substring(0, transIds.length() - 1)
			// + ")的交易合并开票", "0");
			log.error("TransInfoAction-transToReverse", e);
		}
		return ERROR;
	}

	/**
	 * 对选中交易执行冲账操作
	 * 
	 * @param trandInfo
	 *            交易信息
	 * @param reverseTransId
	 *            冲账交易ID
	 */
	private boolean reverseEntry(TransInfo transInfo, String reverseTransId) {
		User currentUser = this.getCurrentUser();
		try {
			// 查询对应销项税信息
			BillInfo bi = new BillInfo();
			bi.setTransId(transInfo.getTransId());
			List billList = this.billInfoService.findBillInfoList(bi);
			if (billList != null && billList.size() > 0) {
				// 循环处理当前交易相关票据
				for (Iterator b = billList.iterator(); b.hasNext();) {
					BillInfo bill = (BillInfo) b.next();
					// 依票据状态不同，进行相应处理
					if (DataUtil.BILL_STATUS_1.equals(bill.getDataStatus())
							|| DataUtil.BILL_STATUS_2.equals(bill
									.getDataStatus())
							|| DataUtil.BILL_STATUS_3.equals(bill
									.getDataStatus())
							|| DataUtil.BILL_STATUS_4.equals(bill
									.getDataStatus())) {
						// 编辑待提交 or 提交待审核 or 审核通过 or 无需审核
						// 复位状态和未开票金额 并删除票据
						this.restoreOtherTransForWriteOff(bill.getBillId(),
								transInfo.getTransId());
						// 鉴于DataUtil.BILL_STATUS_0属性被删除，故此功能处直接删除对应票据
						// bill.setDataStatus(DataUtil.BILL_STATUS_0);
						// bill.setDescription("所含交易发生冲账 票据做删除处理");
						// this.billInfoService.saveBillInfo(bill, true);
						this.billInfoService.deleteBillItemInfo(bill
								.getBillId(), null);
						this.billInfoService.deleteBillInfo(bill.getBillId());
					} else if (DataUtil.BILL_STATUS_5.equals(bill
							.getDataStatus())
							|| DataUtil.BILL_STATUS_6.equals(bill
									.getDataStatus())
							|| DataUtil.BILL_STATUS_7.equals(bill
									.getDataStatus())
							|| DataUtil.BILL_STATUS_8.equals(bill
									.getDataStatus())) {
						// 已点击打印但未完成
						// 票据需进行废票处理
						// bill.setCancelFlag(DataUtil.FEIPIAO);
						// 记录废票发起人，以便交叉审核
						bill.setCancelInitiator(currentUser.getId());
						bill.setRemark("交易冲账");
						billInfoService.saveBillInfo(bill, true);
						// 复位状态和未开票金额 并删除票据
						this.restoreOtherTransForWriteOff(bill.getBillId(),
								transInfo.getTransId());
					} else if (DataUtil.BILL_STATUS_9.equals(bill
							.getDataStatus())
							|| DataUtil.BILL_STATUS_99.equals(bill
									.getDataStatus())) {
						// 已进行抄报
						// 票据需进行红冲处理 但因不知其通知单编号，只生成编辑待提交状态的红冲票据
						// 查询对应票据明细信息
						List billItemList = this.billInfoService
								.findBillItemInfoList(new BillItemInfo(bill
										.getBillId(), null));
						// 生成编辑待提交状态的红冲票据
						this.writeOffBill(bill.getBillId(), null, "交易冲账",
								billItemList, null, null,
								DataUtil.BILL_STATUS_1);
						// 复位状态和未开票金额 并删除票据
						this.restoreOtherTransForWriteOff(bill.getBillId(),
								transInfo.getTransId());
					}
				}
			}
			// 记录冲账交易ID，修改交易状态
			transInfo.setReverseTransId(reverseTransId);
			this.transInfoService.updateTransInfo(transInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TransInfoAction-reverseEntry", e);
			return false;
		}
	}

	/**
	 * 交易红冲时查询交易所属票据对应的其余交易信息，复位这些交易的状态和未开票金额
	 * 
	 * @param billId
	 *            被红冲交易对应的某笔票据ID
	 * @param transId
	 *            该笔被红冲的交易ID
	 */
	private void restoreOtherTransForWriteOff(String billId, String transId) {
		TransInfo trans = new TransInfo();
		// trans.setSearchFlag(DataUtil.DELETE_BILL);
		// trans.setBillId(billId);
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		trans.setLstAuthInstId(lstAuthInstId);
//		// 获取相关联的客户ID
//		List list = transInfoService.findTransCustomerList(trans);
//		// 去重复
//		Map map = new HashMap();
//		for (int j = 0; j < list.size(); j++) {
//			String customerId = ((Customer) list.get(j)).getCustomerID();
//			map.put(customerId, customerId);
//		}
//		Object transCustomerList[] = map.keySet().toArray();
//		// transInfo.setTransCustomerList(transCustomerList);
//		trans.setTransCustomerList(transCustomerList);
		List transList = this.transInfoService.findTransInfoList(trans);
		for (Iterator t = transList.iterator(); t.hasNext();) {
			trans = (TransInfo) t.next();
			if (!trans.getTransId().equals(transId)) {
				// BigDecimal balance = trans.getBalance();
				// if (balance != null && trans.getTransBillAmt() != null) {
				// if (balance.compareTo(new BigDecimal(0)) == 0) {
				// trans.setDataStatus(DataUtil.TRANS_STATUS_1);
				// }
				// trans.setBalance(balance.add(trans.getTransBillAmt()));
				// this.transInfoService.updateTransInfo(trans);
				// }
			}
		}
	}

	public String connectCustomer() {
		int showFlag = -1;
		try{
			String flag = request.getParameter("flag");
			if(!"search".equals(flag)){
				String customerType = request.getParameter("orgCustomerType");
			
				String customerId = request.getParameter("orgCustomerId");
			//客户号为空时，取原始客户号
				String customerCode = request.getParameter("customerCode")==null?"":request.getParameter("customerCode");
			
				customerCode = !"".equals(customerCode)?customerCode:customerId;
				String transIds = request.getParameter("transIds");
			
				connCust.setTransIds(transIds);
				connCust.setOrgCustomerId(customerId);
				connCust.getOrgCustomerId();
				connCust.setCustomerCode(customerCode);
				//paginationList.setCurrentPage(1);
				
				request.setAttribute("orgCustomerType", customerType);
				request.setAttribute("customerCode", customerCode);
				request.setAttribute("customerId", customerId);
				request.setAttribute("transIds", transIds);
				showFlag = 0;
				transInfoService.findCustomerByCustomerId(connCust.getCustomerCode(), connCust.getCustomerName(), paginationList);
				return ERROR;
			
			}else{
				//直接取客户信息，不需要区分客户类型
				showFlag = 0;
				transInfoService.findCustomerByCustomerId(connCust.getCustomerCode(), connCust.getCustomerName(), paginationList);
				return ERROR;
//				if(customerType.equals("C")){
//					//判断sub_customer中是否存在分公司
//					List customer_list = transInfoService.isFindSubCustomerByCustomerId(customerId);
//					
//					//判断客户id是否存在主表中
//					if(customer_list != null && !customer_list.isEmpty()){
//						showFlag = 1;
//						transInfoService.findSubCustomerByCustomerId(connCust.getOrgCustomerId(), paginationList);
//						return SUCCESS;
//					}else{
//						showFlag = 0;
//						List list = transInfoService.findCustomerByCustomerId(connCust.getCustomerCode(), paginationList);
//						System.out.println("长度===="+list.size());
//						return ERROR;
//					}
//				}else if(customerType.equals("I")){
//					showFlag = 0;
//					transInfoService.findCustomerByCustomerId(connCust.getCustomerCode(), paginationList);
//					return ERROR;
//				}
			}
		}catch(Exception e){
			System.out.println("关联客户失败！！！");
			e.printStackTrace();
		}
		if(showFlag == 1){
			return SUCCESS;
		}else{
			return ERROR;
		}
	}

	public String updateConnectCustomer() {
		String operUser = this.getCurrentUser().getUsername();
		// var newUrl =
		// "<%=webapp%>/connectCustomer.action?customerId="+customerId+"&transId="+transId;
//		String transIds = this.getTransIds();
//		String orgCustomerId = request.getParameter("orgCustomerId");
//		String currCustomerId = request.getParameter("customerId");
		
		ConnCust connCust = this.connCust;
		connCust.setOperUser(operUser);
		transInfoService.updateConnectCustomer(connCust,0);

		return SUCCESS;
	}
	
	public String updateConnectSubCustomer() {
		String operUser = this.getCurrentUser().getUsername();
		ConnCust connCust = this.connCust;
		connCust.setOperUser(operUser);
		transInfoService.updateConnectCustomer(connCust,1);

		return SUCCESS;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List getKbcDataSouceList() {
		return kbcDataSouceList;
	}

	public void setKbcDataSouceList(List kbcDataSouceList) {
		this.kbcDataSouceList = kbcDataSouceList;
	}
	
}
