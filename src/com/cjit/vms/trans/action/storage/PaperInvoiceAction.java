package com.cjit.vms.trans.action.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import cjit.crms.util.StringUtil;
import cjit.crms.util.json.JsonUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.PaperInvoiceStock;
import com.cjit.vms.trans.model.SelectOption;
import com.cjit.vms.trans.model.storage.PaperInvoiceDistribute;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceRbHistory;
import com.cjit.vms.trans.model.storage.PaperInvoiceStockDetail;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.vms.trans.service.storage.impl.PaperInvoiceServiceImpl;
import com.cjit.vms.trans.util.SqlUtil;

/**
 * 纸质发票库存管理Action类
 * 
 * @author jobell
 */
public class PaperInvoiceAction extends DataDealAction {

	private static final long serialVersionUID = 1L;

	private static final String PATTREN = "yyyy-MM-dd";
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(PATTREN);

	/**
	 * @Action
	 * 
	 * 		纸质发票一览初始化页面
	 * 
	 * @author cylenve
	 * @return
	 */
	public String listPageInvoice() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			User currentUser = this.getCurrentUser();

			mapVatType = this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			PaperInvoiceListInfo paperListInfo = new PaperInvoiceListInfo();
			paperListInfo.setUserID(currentUser.getId());
			if (StringUtil.isNotEmpty(flag)) {
//				billInfo.setSearchFlag(flag);
			}
			// 机构ID
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			paperListInfo.setLstAuthInstId(lstAuthInstId);
			// 领购日期
			if (getReceiveInvoiceTime() != null && !"".equals(getReceiveInvoiceTime())) {
				paperListInfo.setReceiveInvoiceTime(DATE_FORMAT.parse(getReceiveInvoiceTime()));
			} else {
				paperListInfo.setReceiveInvoiceTime(null);
			}
			// 领购日期结束
			if (getReceiveInvoiceEndTime() != null && !"".equals(getReceiveInvoiceEndTime())) {
				paperListInfo.setReceiveInvoiceEndTime(DATE_FORMAT.parse(getReceiveInvoiceEndTime()));
			} else {
				paperListInfo.setReceiveInvoiceEndTime(null);
			}
			// 领购人员
			paperListInfo.setUserId(getReceiveUserId());
			// 发票类型
			paperListInfo.setInvoiceType(getInvoiceType());
			// 发票类型
			paperListInfo.setInvoiceCode(getInvoiceCode());
			// 一览数据检索
			List paperInfoList = paperInvoiceService.getPaperInvoiceListInfo(paperListInfo, paginationList);
			this.request.setAttribute("paperInfoList", paperInfoList);
			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.setAttribute("currMonth", currMonth);
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
	 * 		纸质发票一览中，帐票excel出力
	 * 
	 * @author cylenve
	 * @return
	 */
	public void createPageInvoiceExcel() throws Exception {
		try {
			StringBuffer fileName = new StringBuffer("发票使用情况统计");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();

			WritableWorkbook wb = Workbook.createWorkbook(os);
			// 发票库存统计sheet1的作成
			writeToExcel1(os, wb);
			// 发票使用情况sheet2的作成
			writeToExcel2(os, wb);
			// 发票领用与归还统计sheet3的作成
			writeToExcel3(os, wb);
			wb.write();
			wb.close();
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
	 * 		空白发票作废页面初始化
	 * 
	 * @author cylenve
	 * @return
	 */
	public String invalidBlankPaperInvoice() {
		return SUCCESS;
	}

	/**
	 * @Action
	 * 
	 * 		空白发票作废页面 发票的存在性check
	 * 
	 * @author cylenve
	 * @return
	 */
	public void chkPaperInvoiceUseDetailCnt() throws IOException {
		// 发票代码
		String invoiceCode = request.getParameter("invoiceCode");
		// 发票号码
		String invoiceNo = request.getParameter("invoiceNo");

		// 纸质发票使用明细件数的取得
		Long cnt = paperInvoiceService.getPaperInvoiceUseDetailCnt(invoiceCode, invoiceNo);

		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(String.valueOf(cnt));
		out.close();
	}

	/**
	 * @Action
	 * 
	 * 		空白发票作废页面 纸质发票使用明细表更新
	 * 
	 * @author cylenve
	 * @return
	 * @throws IOException
	 */
	public void updInvalidBlankPaperInvoice() throws IOException {
		// 发票代码
		String invoiceCode = request.getParameter("invoiceCode");
		// 发票号码
		String invoiceNo = request.getParameter("invoiceNo");
		// 作废原因
		String invalidReason = request.getParameter("invalidReason");

		String result = "";
		try {
			// 纸质发票使用明细件数的取得
			paperInvoiceService.updateInvalidPaperInvoiceUseDetail(invoiceCode, invoiceNo, invalidReason);
		} catch (Exception ex) {
			result = "ng";
		}

		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}

	/**
	 * @Action
	 * 
	 * 		纸质发票分发初始化页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String initDistrubute() {
		try {
//			List authInstList = new ArrayList();
//			getAuthInstList(authInstList);
//			for(int i=0;i<authInstList.size();i++){
//				Organization org=(Organization)authInstList.get(i);
//				lstInstOption.add(new SelectOption(org.getId()+"",org.getName()));
//			}

			ResourceBundle rb = ResourceBundle.getBundle("config.vmss");
			if (null == rb) {
				max_distrubute_limit = "1000";
			} else {
				max_distrubute_limit = rb.getString("max.distrubute.limit");
				if (StringUtils.isEmpty(max_distrubute_limit)) {
					max_distrubute_limit = "1000";
				}
			}

			String[] store_ids = StringUtils.split(paper_invoice_stock_ids, ",");
			lstInvoiceStoreDetail = paperInvoiceService.findPaperInvoiceStoreByStoreIds(store_ids);
		} catch (Exception e) {
			e.printStackTrace();
//			logManagerService.writeLog(request, this.getCurrentUser(),
//					"0001.0010", "查询开票", "分发纸质开票", "对纸质发票库存ID为(" + billId
//							+ ")的票据进行撤销处理", "0");
			log.error("PaperInvoiceAction-doDistrubute", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * @Action
	 * 
	 * 		纸质发票分发行为处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String doDistrubute() {
		List lstDistribute = new ArrayList();
		try {
//			List lstDistribute=new ArrayList();
			int iMax = Integer.parseInt(distribute_max_size);
			String currentTime = StringUtils.substringBefore(DateUtils.serverCurrentDateTime(), ".");
			for (int i = 1; i <= iMax; i++) {
				String tmp = request.getParameter("receive_inst_id_" + i);
				if (StringUtils.isEmpty(tmp)) {
					continue;
				}
				PaperInvoiceDistribute distribute = new PaperInvoiceDistribute();
				distribute.setPaperInvoiceStockId(request.getParameter("paper_invoice_stock_id_" + i));
				distribute.setReceiveInstId(request.getParameter("receive_inst_id_" + i));
				distribute.setReceiveUserId(request.getParameter("receive_user_id_" + i));
				distribute.setInvoiceCode(request.getParameter("invoice_code_" + i));
				// distribute.setInvoiceBeginNo(request.getParameter("invoice_begin_no"+i));
				// distribute.setInvoiceEndNo(request.getParameter("invoice_end_no"+i));
				distribute.setDistributeNum(request.getParameter("page_used_invoice_num_" + i));
				distribute.setCreateTime(currentTime);
				distribute.setCreateInstId(getCurrentUser().getOrgId());
				distribute.setCurrentbillNo(request.getParameter("page_current_num_" + i));
				distribute.setCreateUserId(getCurrentUser().getId());
				lstDistribute.add(distribute);
			}
			int result = paperInvoiceService.savePaperInvoiceDistribute(lstDistribute);
			if (result < 0) {
				throw new Exception("分发纸质发票失败");
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
//			logManagerService.writeLog(request, this.getCurrentUser(),
//					"0001.0010", "查询开票", "分发纸质开票", "对纸质发票库存ID为(" + billId
//							+ ")的票据进行撤销处理", "0");
			log.error("PaperInvoiceAction-doDistrubute", e);
//			return ERROR;
			try {
				Set tempSet = new HashSet();
				for (int i = 0; i < lstDistribute.size(); i++) {
					PaperInvoiceDistribute distribute = (PaperInvoiceDistribute) lstDistribute.get(i);
					String store_id = distribute.getPaperInvoiceStockId();
					tempSet.add(store_id);
				}
				String paper_invoice_stock_ids = "";
				Object[] arr = tempSet.toArray();
				paper_invoice_stock_ids = SqlUtil.arr2StringPlain(arr, ",");
				String path = request.getContextPath();
				String webapp = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ path;
				response.sendRedirect(webapp + "/initDistrubute.action?paper_invoice_stock_ids="
						+ paper_invoice_stock_ids + "&retry=true");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public String getInstList() {
		try {
			List authInstList = new ArrayList();
			getAuthInstList(authInstList);
			List lstTmp = new ArrayList();
//			for(int i=0;i<authInstList.size();i++){
//				Organization org=(Organization)authInstList.get(i);
//				lstTmp.add(new SelectOption(org.getId()+"",URLEncoder.encode(org.getName(), "utf8")));
//			}
			Organization org = new Organization();
			String instId = this.getCurrentUser().getOrgId();
			org.setId(instId);
			org = this.organizationService.getOrganization(org);
			lstTmp.add(new SelectOption(org.getId() + "", URLEncoder.encode(org.getName(), "utf8")));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("msg", "ok");
			jsonObject.put("lstOrg", lstTmp);

			response.getWriter().write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * 方法名称: addPaerInvoice|描述:纸质发票添加
	 * </p>
	 */
	public String addPaperInvoice() {
		try {
			mapVatType = this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			List InstId = new ArrayList();
			this.getAuthInstList(InstId);
			this.setAuthInstList(InstId);
			this.setOperType("add");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-addPaperInvoice", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: editPaerInvoice|描述:纸质发票编辑
	 * </p>
	 */
	public String editPaperInvoice() {
		try {
			mapVatType = this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			List InstId = new ArrayList();
			this.getAuthInstList(InstId);
			this.setAuthInstList(InstId);
			String invoiceStockId = request.getParameter("stockId");
			paperinvoicestock = paperInvoiceService.getPaperInvoiceStock(invoiceStockId);
			this.setInstId(paperinvoicestock.getInstId());
			List lstDetail = new ArrayList();
			lstDetail = paperInvoiceService.getPaperInvoiceStockDetail(invoiceStockId);
			paperinvoicestockdetail1 = (PaperInvoiceStockDetail) lstDetail.get(0);
			if (lstDetail.size() > 1) {
				paperinvoicestockdetail2 = (PaperInvoiceStockDetail) lstDetail.get(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-editPaperInvoice", e);
			return ERROR;
		}
		this.setOperType("edit");
		return SUCCESS;
	}

	/**
	 * 
	 * @author: bmf
	 * @date:2019年10月21日
	 * @return
	 * @Description:删除未使用的发票库存信息
	 */
	public String delectPaperInvoice() {
		try {
			String invoiceStockId = this.paper_invoice_stock_id;
			String invoiceNum = this.invoice_code;
			paperInvoiceService.deletePaperInvoiceStock(invoiceStockId, invoiceNum);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-delectPaperInvoice", e);
			return ERROR;
		}
		return SUCCESS;

	}

	/**
	 * <p>
	 * 方法名称: savePaperInvoice|描述:纸质发票入库
	 * </p>
	 */
	public String savePaperInvoice() {
		try {
			String operType = request.getParameter("operType");
			List listInvoiceStock = new ArrayList();
			String currentTime = StringUtils.substringBefore(DateUtils.serverCurrentDateTime(), ".");
			Date time = new Date();
			String receiveInvoiceTime = DATE_FORMAT.format(time);
			PaperInvoiceStock stock = new PaperInvoiceStock();
			if (operType.equals("edit")) {
				stock.setPaperInvoiceStockId(request.getParameter("paperInvoiceStockId"));// 库存ID
			}
			stock.setInstId(request.getParameter("instId"));// 机构ID
			stock.setReceiveInvoiceTime(StringUtils.substringBefore(DateUtils.serverCurrentDateTime(), "."));// 领取日期
			stock.setUserId(request.getParameter("userId"));// 领取人
			stock.setInvoiceType(request.getParameter("invoiceType"));// 发票类型
			stock.setMaxMoney(request.getParameter("maxMoney"));// 单张发票最高金额
			stock.setCreateTime(currentTime);// 创建时间
			stock.setDistributeFlag("0");
			stock.setCreateUserId(getCurrentUser().getId());
			stock.setCreateInstId(getCurrentUser().getOrgId());
			listInvoiceStock.add(stock);
			for (int i = 1; i <= 2; i++) {
				if (request.getParameter("invoiceCode" + i) != null
						&& !request.getParameter("invoiceCode" + i).equals("")) {
					String invoiceCodeParam = request.getParameter("invoiceCode" + i);
					String invoiceBeginNoParam = request.getParameter("invoiceBeginNo" + i);
					String invoiceEndNoParam = request.getParameter("invoiceEndNo" + i);
					PaperInvoiceStockDetail paperinvoicestockdetail = new PaperInvoiceStockDetail();
					paperinvoicestockdetail.setInvoiceCode(invoiceCodeParam);// 发票代码
					paperinvoicestockdetail.setInvoiceBeginNo(invoiceBeginNoParam);// 起始号码
					paperinvoicestockdetail.setInvoiceEndNo(invoiceEndNoParam);// 终止号码
					int num = Integer.parseInt(paperinvoicestockdetail.getInvoiceEndNo())
							- Integer.parseInt(paperinvoicestockdetail.getInvoiceBeginNo()) + 1;
					paperinvoicestockdetail.setInvoiceNum(num + "");
					paperinvoicestockdetail.setUserdNum("0");
					paperinvoicestockdetail.setHasDistributeNum("0");
					listInvoiceStock.add(paperinvoicestockdetail);
				}

			}

			int result = paperInvoiceService.savePaperInvoiceStock(operType, listInvoiceStock, receiveInvoiceTime);
		} catch (Exception e) {
			e.printStackTrace();
//			logManagerService.writeLog(request, this.getCurrentUser(),
//					"0001.0010", "查询开票", "分发纸质开票", "对纸质发票库存ID为(" + billId
//							+ ")的票据进行撤销处理", "0");
			log.error("PaperInvoiceAction-savePaperInvoice", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 2019 根据发票代码和发票号查询发票状态
	 */
	public void getStockPaperInvoiceStatus() {
		try {
			String invoiceCode = request.getParameter("invoiceCode");
			String invoiceNo = request.getParameter("invoiceNo");
			String result = paperInvoiceService.getStockPaperInvoiceStatus(invoiceCode, invoiceNo);
			response.setHeader("Content-Type", "text/html; charset=utf-8");
			PrintWriter out;
			out = response.getWriter();
			out.print(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("getStockPaperInvoiceStatus.action : -" + e);
		}
	}

	/**
	 * 
	 * @date:2019年12月10日
	 * @Description: 新增/编辑发票库存信息，判断是否已经存在相应的发票代码和发票号，
	 *               如果是编辑则需要与之前的做对比，如果没变不做提示，如已变更，则对差异的发票号进行判断
	 */
	public void CountPaperInvoiceCode()throws IOException {
	
		List<String> invoiceNoList = new ArrayList<String>();
		String invoiceCode = request.getParameter("invoiceCode");
		String beginNo = request.getParameter("beginNo");
		String endNo = request.getParameter("endNo");
		String result = "";
		String paperInvoiceStockId = request.getParameter("paperInvoiceStockId");
		if (paperInvoiceStockId != null && paperInvoiceStockId != "") {
			int beginNoNum = Integer.parseInt(request.getParameter("beginNo"));
			int endNoNum = Integer.parseInt(request.getParameter("endNo"));
			List<PaperInvoiceStockDetail> oldInvoice = paperInvoiceService.invoiceExistsByIdAndCode(invoiceCode,
					paperInvoiceStockId);
			int invoiceBeginNo = Integer.parseInt(oldInvoice.get(0).getInvoiceBeginNo());
			int invoiceEndNo = Integer.parseInt(oldInvoice.get(0).getInvoiceEndNo());
			if (beginNoNum >= invoiceBeginNo && endNoNum <= invoiceEndNo) {
				invoiceNoList = null;
			} else {
				List<String> list = new ArrayList<String>();
				for (int idx = beginNoNum; idx <= endNoNum; idx++) {
					list.add(idx + "");
				}
				List<String> detail = paperInvoiceService.iFInvoiceExists(oldInvoice.get(0).getInvoiceCode(),
						oldInvoice.get(0).getInvoiceBeginNo(), oldInvoice.get(0).getInvoiceEndNo());
				list.removeAll(detail);
				System.out.print(list);
				 
				for(String invocice:list) {
					PaperInvoiceUseDetail paperInvoiceUseDetail= paperInvoiceService.invoiceNoIfExists(invoiceCode,invocice);
					if(paperInvoiceUseDetail!=null) {
						 if(paperInvoiceUseDetail.getInvoiceCode()!=null) {
						    	invoiceNoList.add(paperInvoiceUseDetail.getInvoiceNo());
						    }	
					}
				}		
				System.out.print("-------------------------"+invoiceNoList);

			}

		} else {
			// 新增直接判断
			invoiceNoList = paperInvoiceService.iFInvoiceExists(invoiceCode, beginNo, endNo);
		}
			response.setHeader("Content-Type", "text/html; charset=utf-8");
			PrintWriter out;
			out = response.getWriter();	
			if(invoiceNoList==null) {
				result="Y";	
			}else if(invoiceNoList.size()==0) {
				result="Y";				
			}else {
				result="N";;
			}	
			out.print(result);
			out.close();
	}

	/**
	 * @Action
	 * 
	 * 		纸质发票一览中，发票库存统计sheet1的作成【发票库存统计】
	 * 
	 * @author cylenve
	 * @return
	 */
	private void writeToExcel1(OutputStream os, WritableWorkbook wb)
			throws IOException, RowsExceededException, WriteException, Exception {

		WritableSheet ws = null;
		ws = wb.createSheet("发票库存统计", 0);

		ws.mergeCells(0, 0, 11, 0);

		Label header0 = new Label(0, 0, "发票库存统计", JXLTool.getHeader());
		Label header1 = new Label(0, 1, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 1, "机构", JXLTool.getHeader());
		Label header3 = new Label(2, 1, "领购日期", JXLTool.getHeader());
		Label header4 = new Label(3, 1, "领购人员", JXLTool.getHeader());
		Label header5 = new Label(4, 1, "发票类型", JXLTool.getHeader());
		Label header6 = new Label(5, 1, "单张开票限额", JXLTool.getHeader());
		Label header7 = new Label(6, 1, "是否已分发", JXLTool.getHeader());
		Label header8 = new Label(7, 1, "已分发张数", JXLTool.getHeader());
		Label header9 = new Label(8, 1, "总张数", JXLTool.getHeader());
		Label header10 = new Label(9, 1, "已用张数", JXLTool.getHeader());
		Label header11 = new Label(10, 1, "未用张数", JXLTool.getHeader());
		Label header12 = new Label(11, 1, "已用百分比(%)", JXLTool.getHeader());
		ws.addCell(header0);
		ws.setColumnView(0, 7);
		ws.addCell(header1);
		ws.setColumnView(1, 20);
		ws.addCell(header2);
		ws.setColumnView(2, 14);
		ws.addCell(header3);
		ws.setColumnView(3, 12);
		ws.addCell(header4);
		ws.setColumnView(4, 18);
		ws.addCell(header5);
		ws.setColumnView(5, 20);
		ws.addCell(header6);
		ws.setColumnView(6, 14);
		ws.addCell(header7);
		ws.setColumnView(7, 15);
		ws.addCell(header8);
		ws.setColumnView(8, 12);
		ws.addCell(header9);
		ws.setColumnView(9, 12);
		ws.addCell(header10);
		ws.setColumnView(10, 14);
		ws.addCell(header11);
		ws.setColumnView(11, 20);
		ws.setColumnView(12, 20);
		ws.addCell(header12);
		PaperInvoiceListInfo paperListInfo = new PaperInvoiceListInfo();
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		paperListInfo.setLstAuthInstId(lstAuthInstId);
		// 机构ID
		if (getInstId() != null && !"".equals(getInstId())) {
			paperListInfo.setInstId("'" + getInstId() + "'");
		} else {
			List authInstList = new ArrayList();
			getAuthInstList(authInstList);
			String arrInstId = "";
			for (int i = 0; i <= authInstList.size() - 1; i++) {
				Organization user = (Organization) authInstList.get(i);
				arrInstId = arrInstId + "'" + user.getId() + "',";
			}
			if (arrInstId != null && !"".equals(arrInstId)) {
				arrInstId = arrInstId.substring(0, arrInstId.length() - 1);
				paperListInfo.setInstId(arrInstId);
			} else {
				paperListInfo.setInstId("''");
			}

		}
		// 领购日期
		if (getReceiveInvoiceTime() != null && !"".equals(getReceiveInvoiceTime())) {
			paperListInfo.setReceiveInvoiceTime(DATE_FORMAT.parse(getReceiveInvoiceTime()));
		} else {
			paperListInfo.setReceiveInvoiceTime(null);
		}
		// 领购日期结束
		if (getReceiveInvoiceEndTime() != null && !"".equals(getReceiveInvoiceEndTime())) {
			paperListInfo.setReceiveInvoiceEndTime(DATE_FORMAT.parse(getReceiveInvoiceEndTime()));
		} else {
			paperListInfo.setReceiveInvoiceEndTime(null);
		}
		// 领购人员
		paperListInfo.setUserId(getReceiveUserId());
		// 发票类型
		paperListInfo.setInvoiceType(getInvoiceType());
		// 一览数据检索
		List paperInfoList = paperInvoiceService.getPaperInvoiceListInfo(paperListInfo, null);
		Map mapVatType = this.vmsCommonService.findCodeDictionary("VAT_TYPE");

		int count = 2;
		for (int i = 0; i < paperInfoList.size(); i++) {
			PaperInvoiceListInfo info = (PaperInvoiceListInfo) paperInfoList.get(i);
//			if (info.getInvoiceCode() != null && !"".equals(info.getInvoiceCode())){
			int column = count++;
			setWritableSheet1(ws, info, column, mapVatType);
//			}
		}
//		wb.write();
//		wb.close();
		// ws.setColumnView(i + 1, width);
	}

	/**
	 * @Action
	 * 
	 * 		纸质发票一览中，发票库存统计sheet1的作成【发票库存统计】
	 * 
	 * @author cylenve
	 * @return
	 */
	private void setWritableSheet1(WritableSheet ws, PaperInvoiceListInfo info, int column, Map mapVatType)
			throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column - 1), JXLTool.getContentFormat());
		// 机构
		Label cell2 = new Label(1, column, info.getInstName(), JXLTool.getContentFormat());
		// 领购时日期
		Label cell3 = new Label(2, column, DATE_FORMAT.format(info.getReceiveInvoiceTime()),
				JXLTool.getContentFormat());
		// 领购人员
		Label cell4 = new Label(3, column, info.getUserId(), JXLTool.getContentFormat());
		// 发票类型
//		if ("1".equals(info.getInvoiceType())) {
//			invoiceType = "增值税专用发票";
//		} else if ("2".equals(info.getInvoiceType())) {
//			invoiceType = "增值税普通发票";
//		}
		String invoiceType = (String) mapVatType.get(info.getInvoiceType());
		Label cell5 = new Label(4, column, invoiceType, JXLTool.getContentFormat());
		// 单张开票限额
		Label cell6 = new Label(5, column, info.getMaxMoney(), JXLTool.getContentFormat());
		// 是否已分发
		String distributeFlag = "";
		if ("0".equals(info.getDistributeFlag())) {
			distributeFlag = "否";
		} else if ("1".equals(info.getDistributeFlag())) {
			distributeFlag = "是";
		} else if ("2".equals(info.getDistributeFlag())) {
			distributeFlag = "部分分发";
		}
		Label cell7 = new Label(6, column, distributeFlag, JXLTool.getContentFormat());
		// 已分发张数
		String distributeNum = "0";
		if (info.getDistributeNum() != null && !"".equals(info.getDistributeNum())) {
			distributeNum = info.getDistributeNum();
		}
		Label cell8 = new Label(7, column, distributeNum, JXLTool.getContentFormat());
		// 总张数
		Label cell9 = new Label(8, column, String.valueOf(info.getInvoiceNum()), JXLTool.getContentFormat());
		// 已用张数
		Label cell10 = new Label(9, column, String.valueOf(info.getUserdNum()), JXLTool.getContentFormat());
		// 未用张数
		Label cell11 = new Label(10, column, String.valueOf(info.getUnUserdNum()), JXLTool.getContentFormat());
		// 已用百分比(%)
		Label cell12 = new Label(11, column, String.valueOf(info.getUserRatioNum()), JXLTool.getContentFormat());

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
	}

	/**
	 * @Action
	 * 
	 * 		纸质发票一览中，发票使用情况sheet2的作成【发票使用情况】
	 * 
	 * @author cylenve
	 * @return
	 */
	private void writeToExcel2(OutputStream os, WritableWorkbook wb)
			throws IOException, RowsExceededException, WriteException, Exception {

		WritableSheet ws = null;
		ws = wb.createSheet("发票使用情况", 1);

		ws.mergeCells(0, 0, 12, 0);

		Label header0 = new Label(0, 0, "发票使用情况", JXLTool.getHeader());
		Label header1 = new Label(0, 1, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 1, "机构", JXLTool.getHeader());
		Label header3 = new Label(2, 1, "发票代码", JXLTool.getHeader());
		Label header4 = new Label(3, 1, "发票起始号码", JXLTool.getHeader());
		Label header5 = new Label(4, 1, "发票终止号码", JXLTool.getHeader());
		Label header6 = new Label(5, 1, "领用部门", JXLTool.getHeader());
		Label header7 = new Label(6, 1, "领用人", JXLTool.getHeader());
		Label header8 = new Label(7, 1, "总张数", JXLTool.getHeader());
		Label header9 = new Label(8, 1, "正常开票张数", JXLTool.getHeader());
		Label header10 = new Label(9, 1, "废票张数", JXLTool.getHeader());
		Label header11 = new Label(10, 1, "红冲张数", JXLTool.getHeader());
		Label header12 = new Label(11, 1, "被红冲张数", JXLTool.getHeader());
		Label header13 = new Label(12, 1, "未使用", JXLTool.getHeader());
		ws.addCell(header0);
		ws.setColumnView(0, 7);
		ws.addCell(header1);
		ws.setColumnView(1, 20);
		ws.addCell(header2);
		ws.setColumnView(2, 15);
		ws.addCell(header3);
		ws.setColumnView(3, 15);
		ws.addCell(header4);
		ws.setColumnView(4, 15);
		ws.addCell(header5);
		ws.setColumnView(5, 11);
		ws.addCell(header6);
		ws.setColumnView(6, 15);
		ws.addCell(header7);
		ws.setColumnView(7, 8);
		ws.addCell(header8);
		ws.setColumnView(8, 15);
		ws.addCell(header9);
		ws.setColumnView(9, 10);
		ws.addCell(header10);
		ws.setColumnView(10, 10);
		ws.addCell(header11);
		ws.setColumnView(11, 14);
		ws.addCell(header12);
		ws.setColumnView(12, 8);
		ws.addCell(header13);

		PaperInvoiceListInfo paperListInfo = new PaperInvoiceListInfo();
		// 机构ID
		if (getInstId() != null && !"".equals(getInstId())) {
			paperListInfo.setInstId("'" + getInstId() + "'");
		} else {
			List authInstList = new ArrayList();
			getAuthInstList(authInstList);
			String arrInstId = "";
			for (int i = 0; i <= authInstList.size() - 1; i++) {
				Organization user = (Organization) authInstList.get(i);
				arrInstId = arrInstId + "'" + user.getId() + "',";
			}
			if (arrInstId != null && !"".equals(arrInstId)) {
				arrInstId = arrInstId.substring(0, arrInstId.length() - 1);
				paperListInfo.setInstId(arrInstId);
			} else {
				paperListInfo.setInstId("''");
			}
		}
		// 领购日期
		if (getReceiveInvoiceTime() != null && !"".equals(getReceiveInvoiceTime())) {
			paperListInfo.setReceiveInvoiceTime(DATE_FORMAT.parse(getReceiveInvoiceTime()));
		} else {
			paperListInfo.setReceiveInvoiceTime(null);
		}
		// 领购日期结束
		if (getReceiveInvoiceEndTime() != null && !"".equals(getReceiveInvoiceEndTime())) {
			paperListInfo.setReceiveInvoiceEndTime(DATE_FORMAT.parse(getReceiveInvoiceEndTime()));
		} else {
			paperListInfo.setReceiveInvoiceEndTime(null);
		}
		// 领购人员
		paperListInfo.setUserId(getReceiveUserId());
		// 发票类型
		paperListInfo.setInvoiceType(getInvoiceType());
		// 一览数据检索
		List paperInfoList = paperInvoiceService.exportPaperInvoiceUserInfoSheet2(paperListInfo);

		int count = 2;
		for (int i = 0; i < paperInfoList.size(); i++) {
			PaperInvoiceUseDetail info = (PaperInvoiceUseDetail) paperInfoList.get(i);
			if (info.getInvoiceCode() != null && !"".equals(info.getInvoiceCode())) {
				int column = count++;
				setWritableSheet2(ws, info, column);
			}
		}
//		wb.write();
//		wb.close();
		// ws.setColumnView(i + 1, width);
	}

	/**
	 * @Action
	 * 
	 * 		纸质发票一览中，发票使用情况sheet2的作成【发票使用情况】
	 * 
	 * @author cylenve
	 * @return
	 */
	private void setWritableSheet2(WritableSheet ws, PaperInvoiceUseDetail info, int column) throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column - 1), JXLTool.getContentFormat());
		// 机构
		Label cell2 = new Label(1, column, info.getInstName(), JXLTool.getContentFormat());
		// 发票代码
		Label cell3 = new Label(2, column, info.getInvoiceCode(), JXLTool.getContentFormat());
		// 发票起始号码
		Label cell4 = new Label(3, column, info.getInvoiceBeginNo(), JXLTool.getContentFormat());
		// 发票终止号码
		Label cell5 = new Label(4, column, info.getInvoiceEndNo(), JXLTool.getContentFormat());
		// 领用部门
		Label cell6 = new Label(5, column, info.getReceiveInstId(), JXLTool.getContentFormat());
		// 领用人
		Label cell7 = new Label(6, column, info.getReceiveUserId(), JXLTool.getContentFormat());
		// 总张数
		Label cell8 = new Label(7, column, info.getDistributeNum(), JXLTool.getContentFormat());
		// 正常开票张数
		Label cell9 = new Label(8, column, info.getInvoiceStatus1(), JXLTool.getContentFormat());
		// 废票张数
		Label cell10 = new Label(9, column, info.getInvoiceStatus2(), JXLTool.getContentFormat());
		// 红冲张数
		Label cell11 = new Label(10, column, info.getInvoiceStatus3(), JXLTool.getContentFormat());
		// 被红冲张数
		Label cell12 = new Label(11, column, info.getInvoiceStatus4(), JXLTool.getContentFormat());
		// 未使用
		Label cell13 = new Label(12, column, info.getInvoiceStatus0(), JXLTool.getContentFormat());

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
	}

	/**
	 * @Action
	 * 
	 * 		纸质发票一览中，发票使用情况sheet3的作成【发票领用与归还统计】
	 * 
	 * @author cylenve
	 * @return
	 */
	private void writeToExcel3(OutputStream os, WritableWorkbook wb)
			throws IOException, RowsExceededException, WriteException, Exception {

		WritableSheet ws = null;
		ws = wb.createSheet("发票领用与归还统计", 2);

		ws.mergeCells(0, 0, 9, 0);

		Label header0 = new Label(0, 0, "发票领用与归还统计", JXLTool.getHeader());
		Label header1 = new Label(0, 1, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 1, "机构", JXLTool.getHeader());
		Label header3 = new Label(2, 1, "部门", JXLTool.getHeader());
		Label header4 = new Label(3, 1, "领/还时间", JXLTool.getHeader());
		Label header5 = new Label(4, 1, "领票人", JXLTool.getHeader());
		Label header6 = new Label(5, 1, "发票代码", JXLTool.getHeader());
		Label header7 = new Label(6, 1, "发票起始号码", JXLTool.getHeader());
		Label header8 = new Label(7, 1, "发票终止号码", JXLTool.getHeader());
		Label header9 = new Label(8, 1, "张数", JXLTool.getHeader());
		Label header10 = new Label(9, 1, "领用/归还", JXLTool.getHeader());
		ws.addCell(header0);
		ws.setColumnView(0, 7);
		ws.addCell(header1);
		ws.setColumnView(1, 20);
		ws.addCell(header2);
		ws.setColumnView(2, 15);
		ws.addCell(header3);
		ws.setColumnView(3, 20);
		ws.addCell(header4);
		ws.setColumnView(4, 15);
		ws.addCell(header5);
		ws.setColumnView(5, 12);
		ws.addCell(header6);
		ws.setColumnView(6, 16);
		ws.addCell(header7);
		ws.setColumnView(7, 16);
		ws.addCell(header8);
		ws.setColumnView(8, 8);
		ws.addCell(header9);
		ws.setColumnView(9, 15);
		ws.addCell(header10);

		PaperInvoiceListInfo paperListInfo = new PaperInvoiceListInfo();
		// 机构ID
		if (getInstId() != null && !"".equals(getInstId())) {
			paperListInfo.setInstId("'" + getInstId() + "'");
		} else {
			List authInstList = new ArrayList();
			getAuthInstList(authInstList);
			String arrInstId = "";
			for (int i = 0; i <= authInstList.size() - 1; i++) {
				Organization user = (Organization) authInstList.get(i);
				arrInstId = arrInstId + "'" + user.getId() + "',";
			}
			if (arrInstId != null && !"".equals(arrInstId)) {
				arrInstId = arrInstId.substring(0, arrInstId.length() - 1);
				paperListInfo.setInstId(arrInstId);
			} else {
				paperListInfo.setInstId("''");
			}
		}
		// 领购日期
		if (getReceiveInvoiceTime() != null && !"".equals(getReceiveInvoiceTime())) {
			paperListInfo.setReceiveInvoiceTime(DATE_FORMAT.parse(getReceiveInvoiceTime()));
		} else {
			paperListInfo.setReceiveInvoiceTime(null);
		}
		// 领购日期结束
		if (getReceiveInvoiceEndTime() != null && !"".equals(getReceiveInvoiceEndTime())) {
			paperListInfo.setReceiveInvoiceEndTime(DATE_FORMAT.parse(getReceiveInvoiceEndTime()));
		} else {
			paperListInfo.setReceiveInvoiceEndTime(null);
		}
		// 领购人员
		paperListInfo.setUserId(getReceiveUserId());
		// 发票类型
		paperListInfo.setInvoiceType(getInvoiceType());
		// 一览数据检索
		List paperInfoList = paperInvoiceService.exportPaperInvoiceUserInfoSheet3(paperListInfo);

		int count = 2;
		for (int i = 0; i < paperInfoList.size(); i++) {
			PaperInvoiceRbHistory info = (PaperInvoiceRbHistory) paperInfoList.get(i);
			if (info.getInvoiceCode() != null && !"".equals(info.getInvoiceCode())) {
				int column = count++;
				setWritableSheet3(ws, info, column);
			}
		}
//		wb.write();
//		wb.close();
		// ws.setColumnView(i + 1, width);
	}

	/**
	 * @Action
	 * 
	 * 		纸质发票一览中，发票使用情况sheet3的作成【发票领用与归还统计】
	 * 
	 * @author cylenve
	 * @return
	 */
	private void setWritableSheet3(WritableSheet ws, PaperInvoiceRbHistory info, int column) throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column - 1), JXLTool.getContentFormat());
		// 机构
		Label cell2 = new Label(1, column, info.getInstName(), JXLTool.getContentFormat());
		// 部门
		Label cell3 = new Label(2, column, info.getReceiveInstId(), JXLTool.getContentFormat());
		// 领/还时间
		Label cell4 = new Label(3, column, info.getCreateTime(), JXLTool.getContentFormat());
		// 领票人
		Label cell5 = new Label(4, column, info.getReceiveUserId(), JXLTool.getContentFormat());
		// 发票代码
		Label cell6 = new Label(5, column, info.getInvoiceCode(), JXLTool.getContentFormat());
		// 发票起始号码
		Label cell7 = new Label(6, column, info.getInvoiceBeginNo(), JXLTool.getContentFormat());
		// 发票终止号码
		Label cell8 = new Label(7, column, info.getInvoiceEndNo(), JXLTool.getContentFormat());
		// 张数
		Label cell9 = new Label(8, column, info.getInvoiceNum(), JXLTool.getContentFormat());
		// 领用/归还 operatorFlag;//操作标记 0：领用 1：退还
		String operatorFlag = "";
		if ("0".equals(info.getOperatorFlag())) {
			operatorFlag = "领用";
		} else if ("1".equals(info.getOperatorFlag())) {
			operatorFlag = "退还";
		}
		Label cell10 = new Label(9, column, operatorFlag, JXLTool.getContentFormat());

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

	// 页面属性
	private List lstInvoiceStoreDetail; // 用于分发纸质发票页面
	private List lstInvoiceDistribute;
	private String distribute_max_size;// 用于分发纸质发票页面,
	private String paper_invoice_stock_id;// 库存ID,用户库存一览页面到分发明细页面专递参数
	private String paper_invoice_stock_ids;// 用户库存一览页面到分发初始页面传递参数
	private String paper_invoice_distribute_id;
	private PaperInvoiceDistribute paperInvoiceDistribute;
	private PaperInvoiceRbHistory paperInvoiceRbHistory = new PaperInvoiceRbHistory();
	private String flag;// 提交按钮区分
	private List lstInstOption;
	private Map mapVatType;
	private String invoiceCode;
	private PaperInvoiceStock paperinvoicestock;// 编辑,新增时对应的发票主体
	private PaperInvoiceStockDetail paperinvoicestockdetail1;// 编辑,新增时对应的发票代码1实体
	private PaperInvoiceStockDetail paperinvoicestockdetail2;// 编辑,新增时对应的发票代码2实体

	private String instId;// 机构ID
	private String instName;
	private String receiveInvoiceTime; // 领购日期
	private String receiveInvoiceEndTime; // 领购日期
	private String receiveUserId; // 领购人员
	private String invoiceType; // 发票类型
	private String max_distrubute_limit;
	private PaperInvoiceDistribute invoiceDistribute;
	private String invoice_code;// 发票代码

	public String getInvoice_code() {
		return invoice_code;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}

	public String getMax_distrubute_limit() {
		return max_distrubute_limit;
	}

	public void setMax_distrubute_limit(String max_distrubute_limit) {
		this.max_distrubute_limit = max_distrubute_limit;
	}

	/** 操作类型 add,edit */
	private String operType;

	/** 发票领用退还翻页参数 */
	private String paperInvoiceDistributeId;

	public String getPaperInvoiceDistributeId() {
		return paperInvoiceDistributeId;
	}

	public void setPaperInvoiceDistributeId(String paperInvoiceDistributeId) {
		this.paperInvoiceDistributeId = paperInvoiceDistributeId;
	}

	/** 库存明细 */
	private PaperInvoiceStockDetail stockdetail;

	public PaperInvoiceStockDetail getPaperinvoicestockdetail1() {
		return paperinvoicestockdetail1;
	}

	public void setPaperinvoicestockdetail1(PaperInvoiceStockDetail paperinvoicestockdetail1) {
		this.paperinvoicestockdetail1 = paperinvoicestockdetail1;
	}

	public PaperInvoiceStockDetail getPaperinvoicestockdetail2() {
		return paperinvoicestockdetail2;
	}

	public void setPaperinvoicestockdetail2(PaperInvoiceStockDetail paperinvoicestockdetail2) {
		this.paperinvoicestockdetail2 = paperinvoicestockdetail2;
	}

	public PaperInvoiceStock getPaperinvoicestock() {
		return paperinvoicestock;
	}

	public void setPaperinvoicestock(PaperInvoiceStock paperinvoicestock) {
		this.paperinvoicestock = paperinvoicestock;
	}

	public List getLstInvoiceStoreDetail() {
		return lstInvoiceStoreDetail;
	}

	public void setLstInvoiceStoreDetail(List lstInvoiceStoreDetail) {
		this.lstInvoiceStoreDetail = lstInvoiceStoreDetail;
	}

	public String getDistribute_max_size() {
		return distribute_max_size;
	}

	public void setDistribute_max_size(String distribute_max_size) {
		this.distribute_max_size = distribute_max_size;
	}

	public String getPaper_invoice_stock_id() {
		return paper_invoice_stock_id;
	}

	public void setPaper_invoice_stock_id(String paper_invoice_stock_id) {
		this.paper_invoice_stock_id = paper_invoice_stock_id;
	}

	public List getLstInvoiceDistribute() {
		return lstInvoiceDistribute;
	}

	public void setLstInvoiceDistribute(List lstInvoiceDistribute) {
		this.lstInvoiceDistribute = lstInvoiceDistribute;
	}

	public String getPaper_invoice_distribute_id() {
		return paper_invoice_distribute_id;
	}

	public void setPaper_invoice_distribute_id(String paper_invoice_distribute_id) {
		this.paper_invoice_distribute_id = paper_invoice_distribute_id;
	}

	public String getPaper_invoice_stock_ids() {
		return paper_invoice_stock_ids;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public void setPaper_invoice_stock_ids(String paper_invoice_stock_ids) {
		this.paper_invoice_stock_ids = paper_invoice_stock_ids;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getReceiveInvoiceTime() {
		return receiveInvoiceTime;
	}

	public void setReceiveInvoiceTime(String receiveInvoiceTime) {
		this.receiveInvoiceTime = receiveInvoiceTime;
	}

	public String getReceiveInvoiceEndTime() {
		return receiveInvoiceEndTime;
	}

	public void setReceiveInvoiceEndTime(String receiveInvoiceEndTime) {
		this.receiveInvoiceEndTime = receiveInvoiceEndTime;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public PaperInvoiceDistribute getPaperInvoiceDistribute() {
		return paperInvoiceDistribute;
	}

	public void setPaperInvoiceDistribute(PaperInvoiceDistribute paperInvoiceDistribute) {
		this.paperInvoiceDistribute = paperInvoiceDistribute;
	}

	public PaperInvoiceRbHistory getPaperInvoiceRbHistory() {
		return paperInvoiceRbHistory;
	}

	public void setPaperInvoiceRbHistory(PaperInvoiceRbHistory paperInvoiceRbHistory) {
		this.paperInvoiceRbHistory = paperInvoiceRbHistory;
	}

	/**
	 * <p>
	 * 方法名称: getOperType|描述: 获取操作类型
	 * </p>
	 * 
	 * @return 操作类型
	 */
	public String getOperType() {
		return operType;
	}

	/**
	 * <p>
	 * 方法名称: setOperType|描述: 设置操作类型
	 * </p>
	 * 
	 * @param operType 操作类型
	 */
	public void setOperType(String operType) {
		this.operType = operType;
	}

	public List getLstInstOption() {
		return lstInstOption;
	}

	public void setLstInstOption(List lstInstOption) {
		this.lstInstOption = lstInstOption;
	}

	public Map getMapVatType() {
		return mapVatType;
	}

	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}

	public PaperInvoiceDistribute getInvoiceDistribute() {
		return invoiceDistribute;
	}

	public void setInvoiceDistribute(PaperInvoiceDistribute invoiceDistribute) {
		this.invoiceDistribute = invoiceDistribute;
	}

	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}

}
