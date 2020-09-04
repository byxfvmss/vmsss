package com.cjit.vms.input.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cjit.crms.util.ExcelUtil;
import cjit.crms.util.StringUtil;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.input.service.InputInvoiceService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.input.model.InformationBills;
import com.cjit.vms.input.model.InformationInput;
import com.cjit.vms.input.model.InputInvoice;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceItem;
import com.cjit.vms.input.model.InputRedReceiptApplyInfo;
import com.cjit.vms.trans.model.SpecialTicket;
import com.cjit.vms.trans.util.DataUtil;

public class InputInvoiceAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private List inputVatInfoList;

	public List getInputInvoiceList() {
		return inputInvoiceList;
	}

	public void setInputInvoiceList(List inputInvoiceList) {
		this.inputInvoiceList = inputInvoiceList;
	}

	public InputInvoiceService getInputInvoiceService() {
		return inputInvoiceService;
	}

	public void setInputInvoiceService(InputInvoiceService inputInvoiceService) {
		this.inputInvoiceService = inputInvoiceService;
	}

	private List fapiaoTypeList;// 发票种类信息
	private InputInvoiceService inputInvoiceService;
	private String[] selectInVatIds;
	private List itemCodeList; // 记帐科目种类
	private List inputItemList;
	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	private InputInvoice inputInvoice = new InputInvoice();
	// 进项税列表
	private List inputInvoiceList;
	// 开票起止日期
	private String billBeginDate;
	private String billEndDate;
	// 记账起止日期
	private String bookingBeginDate;
	private String bookingEndDate;
	// 记帐科目
	private String bookingItem;
	// 供应商名称
	private String vendorName;
	// 认证结果
	private String authenticationFlag;
	// 认证结果信息
	private List authenticationFlagList;
	// 勾选结果集
	private String[] selectInputInvoiceIds;
	// 票据类型
	private String faPiaoType;
	// 交易类型
	private List bussTypeList;
	// 表头列表
	private List headList;
	// 预警时间
	private BigDecimal deductedAlart;
	private List transTypeList;
	private String vendorTaxNo;
	private InformationInput informationInput=new InformationInput();
	// 综合查询票据实体类
	private InformationBills informationBills=new InformationBills();
	// 综合查询票据对应的商品明细类
	private InputInvoiceItem inputInvoiceItem;
	
	
 	public InputInvoiceItem getInputInvoiceItem() {
		return inputInvoiceItem;
	}

	public void setInputInvoiceItem(InputInvoiceItem inputInvoiceItem) {
		this.inputInvoiceItem = inputInvoiceItem;
	}



	// sea start
	private String message;
	private InputRedReceiptApplyInfo applyInfo=new InputRedReceiptApplyInfo();
	private List invoiceInfoList;
	private SpecialTicket specialTicket;
	private List billItemList;
	private Map mapVatType;//发票种类map
	private Map mapDataStatus;//发票状态map
	private List billsList; //票据信息
	private String urlBillImage;//票据图片地址
	
	// 发票状态列表
	private List billDataStatusList = new ArrayList();
	private List instCodeList = new ArrayList();
	// end
	
 	public InformationInput getInformationInput() {
		return informationInput;
	}

	public void setInformationInput(InformationInput informationInput) {
		this.informationInput = informationInput;
	}
	

	public InformationBills getInformationBills() {
		return informationBills;
	}

	public void setInformationBills(InformationBills informationBills) {
		this.informationBills = informationBills;
	}

	/**
	 * 菜单点击进入进项税管理--数据处理界面
	 * 
	 * @return String
	 */
	public String listInputInvoice() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		// 获取发票种类
		fapiaoTypeList = this.userInterfaceConfigService.getDictionarys(
				"BILL_TYPE", "");
		// 获取记帐科目
//		itemCodeList = this.userInterfaceConfigService.getDictionarys(
//				"ITEM_CODE", "");
		// 获取交易品种
//		bussTypeList = inputInvoiceService.findBussTypeList();
		// 获取表头列表(导入)
		headList = userInterfaceConfigService.getDictionarys(
				"INPUT_INVOICE_IMP", "");
		// 获取预警时间
		deductedAlart = inputInvoiceService.findDeductedAlart();
		// String id = createBusinessId("IN");
		try {
			// User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				inputInvoice = new InputInvoice();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.setBookingBeginDate(null);
				this.setBookingEndDate(null);
				this.setVendorName(null);
				this.setBookingItem(null);
				this.setFaPiaoType(null);
				this.setAuthenticationFlag(null);
				// this.request.getSession().removeAttribute("bookingBeginDate");
				// this.request.getSession().removeAttribute("bookingEndDate");
				fromFlag = null;
			}else if("edit".equals(fromFlag)){
				String s = this.authenticationFlag;
				System.out.println(s);
			}
			InputInvoice inputInvoice = new InputInvoice();
			inputInvoice.setBillBeginDate(billBeginDate);
			inputInvoice.setBillEndDate(billEndDate);
//			inputInvoice.setVendorName(vendorName);
//			inputInvoice.setFaPiaoType(faPiaoType);
			inputInvoice.setBillCode(request.getParameter("billCode"));
			inputInvoice.setBillNo(request.getParameter("billNo"));
			inputInvoice.setTaxNo(request.getParameter("taxNo"));
			if(deductedAlart!=null&&!"".equals(deductedAlart)){
				inputInvoice.setDeductedAlart(deductedAlart);
			}
			inputInvoiceList = inputInvoiceService.findInputInvoiceList(
					inputInvoice, paginationList);
			// 处理发票种类
			for (int i = 0; i < inputInvoiceList.size(); i++) {
				inputInvoice = (InputInvoice) inputInvoiceList.get(i);
				for (int j = 0; j < fapiaoTypeList.size(); j++) {
					Dictionary item = (Dictionary) fapiaoTypeList.get(j);
					String itemCode = item.getValueStandardLetter();
					String itemName = item.getName();
					if (itemCode.equals(inputInvoice.getFaPiaoType())) {
						inputInvoice.setFaPiaoType(itemName);
					}
				}
			}
			this.request.setAttribute("billCode", request.getParameter("billCode"));
			this.request.setAttribute("billNo", request.getParameter("billNo"));
			this.request.setAttribute("taxNo", request.getParameter("taxNo"));
			this.request.setAttribute("fapiaoTypeList", fapiaoTypeList);
			this.request.setAttribute("inputInvoiceList", inputInvoiceList);
			this.request.setAttribute("authenticationFlagList",
					authenticationFlagList);
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0110", "进项税管理", "", "数据处理", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0110", "进项税管理", "", "数据处理", "0");
			log.error("InputVatInfoAction-listInputVat", e);
		}
		return ERROR;
	}
	
	/**
	 * 进项税明细查看
	 * 
	 * @return
	 */
	public String inputInvoiceDetail() {
		// String id = (String) this.request.getAttribute("id");
		String id = request.getParameter("billId");
		inputInvoice = inputInvoiceService.findInputInvoiceDetail(id);
		// 获取记帐科目
//		itemCodeList = this.userInterfaceConfigService.getDictionarys(
//				"ITEM_CODE", "");
//		for (int i = 0; i < itemCodeList.size(); i++) {
//			Dictionary item = (Dictionary) itemCodeList.get(i);
//			String itemCode = item.getValueStandardLetter();
//			String itemName = item.getName();
//			if (itemCode.equals(inputInvoice.getBookingItem())) {
//				inputInvoice.setBookingItemView(itemName);
//			}
//		}
		// 获取交易类型
//		for (int i = 0; i < bussTypeList.size(); i++) {
//			InputInvoice buss = (InputInvoice) bussTypeList.get(i);
//			String bussType = buss.getBussType();
//			String businessCname = buss.getBusinessCname();
//			if (null != bussType && bussType.equals(inputInvoice)) {
//				inputInvoice.setBusinessCname(businessCname);
//			}
//		}
		return SUCCESS;
	}
	
	/**
	 * 进项税综合查询-交易菜单
	 * @return
	 */
	public String listInputCompare(){
		List lstAuthInstId=new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		this.setAuthInstList(lstAuthInstId);
		//informationInput.setBillNo(informationInput.getBillNo());
		informationInput.setTransBeginDate(request.getParameter("billBeginDate"));
		informationInput.setTransEndDate(request.getParameter("billEndDate"));
		informationInput.setVendorName(request.getParameter("vendorName"));
		informationInput.setVendorTaxNo(request.getParameter("vendorTaxNo"));	
		informationInput.setDealNo(request.getParameter("dealNo"));
		informationInput.setBankCode(request.getParameter("bankCode"));	
		informationInput.setBankName(request.getParameter("bankName"));	
		
		informationInput.setLstAuthInstId(lstAuthInstId);
		
		List list = inputInvoiceService.findInputInformation(informationInput, paginationList);
		inputVatInfoList = list;
		this.request.setAttribute("paginationList", paginationList);
		return SUCCESS;
	}
	
	/**
	 * 进项税综合查询-票据菜单
	 * @return
	 */
	public String listInputBillsQuery(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		if ("menu".equalsIgnoreCase(fromFlag)) {
			fromFlag = null;
		}
		List lstAuthInstId=new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		this.setAuthInstList(lstAuthInstId);
		//InformationBills informationBills= new informationBillsrmationBills();
		informationBills.setBillStartDate(request.getParameter("billStartDate_s"));
		informationBills.setBillEndDate(request.getParameter("billEndDate_s"));
		informationBills.setVendorName(request.getParameter("vendorName_s"));
		informationBills.setDataStatus(request.getParameter("dataStatus_s"));
		informationBills.setInstCode(request.getParameter("instCode_s"));
		informationBills.setInstName(request.getParameter("instName_s"));
		informationBills.setBillCode(request.getParameter("billCode_s"));
		informationBills.setVendorTaxNo(request.getParameter("vendorTaxNo_s"));
		informationBills.setBillNo(request.getParameter("billNo_s"));
		informationBills.setFaPiaoType(request.getParameter("faPiaoType_s"));
		informationBills.setLstAuthInstId(lstAuthInstId);
		inputInvoiceService.findInputBillsQuery(informationBills, paginationList);
		
		mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
		List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
		mapDataStatus=new HashMap();
		for(int i=0;i<lstDataStatus.size();i++){
			SelectTag tag=(SelectTag) lstDataStatus.get(i);
			mapDataStatus.put(tag.getValue(), tag.getText());
		}
		return SUCCESS;
	}
	
	
	/**
	 * 保存进项税明细
	 * 
	 * @return
	 */
	public String saveInputInvoice() {
		String id = request.getParameter("billId");
		String bookingDate = request.getParameter("bookingDate");
		String billType = request.getParameter("billType");
		BigDecimal taxRate = null;
		if (null == request.getParameter("taxRate")
				|| "".equals(request.getParameter("taxRate"))) {
		} else {
			taxRate = new BigDecimal(request.getParameter("taxRate"));
		}
		inputInvoiceService.saveInputInvoiceInfo(inputInvoice);
		return SUCCESS;
	}
	
	/**
	 * 该页面显示同一笔交易下存在的票据信息。
	 * 如果为单笔开票，则一笔交易对应一笔票据													
	 * 如果为合并开票，则显示一笔交易对应的票据，票据为票据的完整信息													
	 * 如果为拆分开票，则显示该笔票据对应的多张票据明细													
	 * @return
	 */
	public String inputInformationDetail(){
		String id = request.getParameter("dealNo");
		//List billItemsList = new ArrayList();
		// 查询一笔交易信息
		informationInput=inputInvoiceService.findInputInformationById(id);
		// 交易对应的票据 
		billsList = inputInvoiceService.findInputBillsByTrans(id);
		mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
		// 将查询结果放入页面中
		//request.setAttribute("billsList", billsList);
		//request.setAttribute("billItemsList", billItemsList);
		return SUCCESS;
	}
	/**
	 * 查看票据										
	 * @return
	 */
	public String inputInformationViewImg(){
		String id = request.getParameter("dealNo");
		InputInvoiceInfo inputInvoiceInfo = new InputInvoiceInfo();
		inputInvoiceInfo = inputInvoiceService.findInputInformationViewImg(id);
		String contextPath = request.getContextPath(); 
		if(null!=inputInvoiceInfo && null!=inputInvoiceInfo.getUrlBillImage() && !"".equals(inputInvoiceInfo.getUrlBillImage())){
			urlBillImage = contextPath+inputInvoiceInfo.getUrlBillImage();
		}else{
			urlBillImage = "";
		}
		return SUCCESS;
	}
	
	public String inputBillsQueryDetail(){
		String billCode = request.getParameter("billCode");
		informationBills=inputInvoiceService.findInputBillsById(billCode);
		return SUCCESS;
	}
	
	/**
	 * 进税项明细编辑
	 * 
	 * @return
	 */
	public String editInputInvoice() {
		String id = request.getParameter("billId");
		inputInvoice = inputInvoiceService.findInputInvoiceDetail(id);
//		for (int i = 0; i < itemCodeList.size(); i++) {
//			Dictionary item = (Dictionary) itemCodeList.get(i);
//			String itemCode = item.getValueStandardLetter();
//			String itemName = item.getName();
//			if (itemCode.equals(inputInvoice.getBookingItem())) {
//				inputInvoice.setBookingItemView(itemName);
//			}
//		}
		return SUCCESS;
	}
	/**
	 * 
	 * 票据详单
	 * @return
	 */
	public String  listInputBillItem(){
		String id=request.getParameter("id");
		inputItemList=inputInvoiceService.findInputItemList(id);
		this.request.setAttribute("inputItemList", inputItemList);
		return SUCCESS;
	}
	/**
	 * 导入进项税数据文件
	 * 
	 * @return
	 */
	public String importInputInvoice() {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("theFile");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				doImportFile(files[0]);
				request.setAttribute("message", "上传文件完成!");
				files = null;
				return SUCCESS;
			} catch (Exception e) {
				log.error(e);
				request.setAttribute("message", "上传文件失败:" + e.getMessage());
				return ERROR;
			}
		} else {
			request.setAttribute("message", "上传文件失败!");
			return ERROR;
		}
	}
	private List doImportFile(File file) throws Exception {
		List errorList = new ArrayList();
		try {
			Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
			if (hs != null) {
				String[][] sheet = (String[][]) hs.get("0");
				// 获取表头列表
				String[] heads = sheet[0];
				// 创建数据List对象
				List dataList = new ArrayList();
				// 获取每行
				for (int i = 1; i < sheet.length; i++) {
					Map map = new HashMap();
					String[] row = sheet[i];
					// 获取单元格
					for (int j = 0; j < heads.length; j++) {
						String columnName = heads[j];
						for (int k = 0; k < headList.size(); k++) {
							Dictionary head = (Dictionary) headList.get(k);
							if (null != columnName
									&& columnName.equals(head
											.getValueStandardLetter())) {
								columnName = head.getName();
							}
						}
						String value = row[j];
						if (null != columnName && null != value) {
							map.put(columnName, value);
							// 创建流水号
							map.put("id", this.createBusinessId("IN"));
						}
					}
					dataList.add(map);
				}
				inputInvoiceService.importInputInvoice(dataList);
			}
		} catch (Exception e) {
			// 出现异常
			e.printStackTrace();
		}
		return errorList;
	}

	public List getInputVatInfoList() {
		return inputVatInfoList;
	}

	public void setInputVatInfoList(List inputVatInfoList) {
		this.inputVatInfoList = inputVatInfoList;
	}

	public List getFapiaoTypeList() {
		return fapiaoTypeList;
	}

	public void setFapiaoTypeList(List fapiaoTypeList) {
		this.fapiaoTypeList = fapiaoTypeList;
	}

	public List getAuthenticationFlagList() {
		return authenticationFlagList;
	}

	public void setAuthenticationFlagList(List authenticationFlagList) {
		this.authenticationFlagList = authenticationFlagList;
	}

	// public InputVatInfo getInputVatInfo() {
	// return inputVatInfo;
	// }

	// public void setInputVatInfo(InputVatInfo inputVatInfo) {
	// this.inputVatInfo = inputVatInfo;
	// }

	public String[] getSelectInVatIds() {
		return selectInVatIds;
	}

	public void setSelectInVatIds(String[] selectInVatIds) {
		this.selectInVatIds = selectInVatIds;
	}

	/**
	 * @return 综合查询导出
	 * @throws Exception
	 */
	public String  exportInformationInput() throws Exception{
		inputVatInfoList=inputInvoiceService.findInputInformation(informationInput, paginationList);
		StringBuffer fileName = new StringBuffer("进项综合查询信息列表-交易");
		fileName.append(".xls");
		String name = name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcelInput(os, inputVatInfoList);
		os.flush();
		os.close();
		
		return SUCCESS;
	}
	
	
	public void writeToExcelInput(OutputStream os, List content) throws IOException,RowsExceededException, WriteException{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("进项综合查询信息列表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "交易时间", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "交易编号", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "交易金额", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "税额", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "交易机构", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "供应商编号", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "供应商名称", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "供应商纳税人识别号", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "发票代码", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "发票号码", JXLTool.getHeader());
		
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
		
		for(int j = 0; j < 12; j++){
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			InformationInput informationInput = (InformationInput)content.get(c);
			int column = count++;
			setWritableSheetInput(ws, informationInput, column);
		}
		wb.write();
		wb.close();
	}
	private void setWritableSheetInput(WritableSheet ws, InformationInput informationInput, int column)throws WriteException{
		int i = 0;
		
		Label cell1 = new Label(i++, column, column+"", JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, informationInput.getTransDate(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, informationInput.getDealNo(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, informationInput.getAmtCny()==null?"":informationInput.getAmtCny().toString(),JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, informationInput.getTaxAmtCny()==null?"":informationInput.getTaxAmtCny().toString(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, informationInput.getBankCode(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, informationInput.getVendorId(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, informationInput.getVendorName(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, informationInput.getVendorTaxNo(), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, informationInput.getBillCode(), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, informationInput.getBillNo(), JXLTool.getContentFormat());
		
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
	}


	/**
	 * @return 综合查询导出-票据
	 * @throws Exception
	 */
	public String  exportInformationBills() throws Exception{
		inputVatInfoList=inputInvoiceService.findInputBillsQuery(informationBills, paginationList);
		StringBuffer fileName = new StringBuffer("进项综合查询信息列表-票据");
		fileName.append(".xls");
		String name = name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcelBills(os, inputVatInfoList);
		os.flush();
		os.close();
		
		return SUCCESS;
	}
	
	
	public void writeToExcelBills(OutputStream os, List content) throws IOException,RowsExceededException, WriteException{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("进项综合查询信息列表-票据", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "发票代码", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "发票号码", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "开票日期", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "所属机构", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "金额", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "税额", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "发票种类", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "供应商名称", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "供应商纳税人识别号", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "票据状态", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "认证时间", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "扫描时间", JXLTool.getHeader());
		
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
		
		for(int j = 0; j < 14; j++){
			ws.setColumnView(j, 18);
		}
		int count = 1;
		mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
		for(int c = 0; c < content.size(); c++){
			InformationBills informationBills = (InformationBills)content.get(c);
			int column = count++;
			setWritableSheetBills(ws, mapVatType, informationBills, column);
		}
		wb.write();
		wb.close();
	}
	private void setWritableSheetBills(WritableSheet ws, Map mapVatType, InformationBills informationBills, int column)throws WriteException{
		int i = 0;
		
		Label cell1 = new Label(i++, column, column+"", JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, informationBills.getBillCode(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, informationBills.getBillNo(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, informationBills.getBillDate(), JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, informationBills.getInstName(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, informationBills.getAmtSum()==null?"":informationBills.getAmtSum().toString(),JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, informationBills.getTaxAmtSum()==null?"":informationBills.getTaxAmtSum().toString(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, mapVatType.get(informationBills.getFaPiaoType())+"", JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, informationBills.getVendorName(), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, informationBills.getVendorTaxNo(), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, DataUtil.getDataStatusCH(informationBills.getDataStatus(), "INPUT_INVOICE"), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, informationBills.getIdentifyDate(), JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, informationBills.getScanDate(), JXLTool.getContentFormat());
		
		
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


	public String getBillBeginDate() {
		return billBeginDate;
	}

	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getBookingItem() {
		return bookingItem;
	}

	public void setBookingItem(String bookingItem) {
		this.bookingItem = bookingItem;
	}

	public InputInvoice getInputInvoice() {
		return inputInvoice;
	}

	public void setInputInvoice(InputInvoice inputInvoice) {
		this.inputInvoice = inputInvoice;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getAuthenticationFlag() {
		return authenticationFlag;
	}

	public void setAuthenticationFlag(String authenticationFlag) {
		this.authenticationFlag = authenticationFlag;
	}

	public String[] getSelectInputInvoiceIds() {
		return selectInputInvoiceIds;
	}

	public void setSelectInputInvoiceIds(String[] selectInputInvoiceIds) {
		this.selectInputInvoiceIds = selectInputInvoiceIds;
	}

	public String getBookingBeginDate() {
		return bookingBeginDate;
	}

	public void setBookingBeginDate(String bookingBeginDate) {
		this.bookingBeginDate = bookingBeginDate;
	}

	public String getBookingEndDate() {
		return bookingEndDate;
	}

	public void setBookingEndDate(String bookingEndDate) {
		this.bookingEndDate = bookingEndDate;
	}

	public List getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(List itemCodeList) {
		this.itemCodeList = itemCodeList;
	}

	public List getBussTypeList() {
		return bussTypeList;
	}

	public void setBussTypeList(List bussTypeList) {
		this.bussTypeList = bussTypeList;
	}

	public List getHeadList() {
		return headList;
	}

	public void setHeadList(List headList) {
		this.headList = headList;
	}

	public BigDecimal getDeductedAlart() {
		return deductedAlart;
	}

	public void setDeductedAlart(BigDecimal deductedAlart) {
		this.deductedAlart = deductedAlart;
	}

	public List getTransTypeList() {
		return transTypeList;
	}

	public void setTransTypeList(List transTypeList) {
		this.transTypeList = transTypeList;
	}

	public String getFaPiaoType() {
		return faPiaoType;
	}

	public void setFaPiaoType(String faPiaoType) {
		this.faPiaoType = faPiaoType;
	}


	public List getInputItemList() {
		return inputItemList;
	}

	public void setInputItemList(List inputItemList) {
		this.inputItemList = inputItemList;
	}
	// 徐海洋 start
	public String listInputRedReceiptApply() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		String type = request.getParameter("type");
		if (null != type && type.equals("back")) {
			applyInfo = null;
		}
		// 发票状态列表
		billDataStatusList = DataUtil.getInputInvoiceDataStatusListPart();
		try {
			if (null == request.getParameter("msg")
					|| !request.getParameter("msg").equals("message")) {
				this.request.getSession().removeAttribute("message");
				this.message = "";
			}
			if ("menu".equalsIgnoreCase(fromFlag)) {
				applyInfo = new InputRedReceiptApplyInfo();
				//this.request.getSession().r(applyInfo);
				fromFlag = null;
			}
			System.out.print(this.message);
			User currentUser = this.getCurrentUser();
			Map map = new HashMap();
			map.put("searchCondition", " t.DATASTATUS in (15，16，10，11) ");
			map.put("applyInfo", applyInfo);
			invoiceInfoList = inputInvoiceService.findInputInvoiceInfoList(map,
					paginationList);
			instCodeList = getAuthInstList();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲申请", "销项税管理", "查询可供进行红冲申请的票据信息列表", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲", "销项税管理", "查询可供进行红冲申请的票据信息列表", "0");
			log.error("BillInfoAction-listBillTrack", e);
		}
		specialTicket = null;
		return ERROR;
	}

	public String inputRedReceiptSpecialApply() {
		String billId = request.getParameter("billId");
		specialTicket = null;
		Map map = new HashMap();
		map.put("billId", billId);
		List list = inputInvoiceService.redReceiptDetail(map);
		if (null != list && list.size() > 0) {
			applyInfo = (InputRedReceiptApplyInfo) list.get(0);
		}
		map.put("searchCondition", "t.BILL_NO ='" + applyInfo.getBillNo()
				+ "' and t.BILL_CODE = '" + applyInfo.getBillCode()+"'");
		List ticketList = billInfoService.findSpecialTicketById(map);
		if (null != ticketList && ticketList.size() > 0) {
			specialTicket = (SpecialTicket) ticketList.get(0);
		}
		return SUCCESS;
	}

	public String viewSpecialApply() {
		String billId = request.getParameter("billId");
		specialTicket = null;
		Map map = new HashMap();
		map.put("billId", billId);
		List list = inputInvoiceService.redReceiptDetail(map);
		if (null != list && list.size() > 0) {
			applyInfo = (InputRedReceiptApplyInfo) list.get(0);
		} else {
			return ERROR;
		}
		map.put("searchCondition", "t.BILL_NO ='" + applyInfo.getBillNo()
				+ "' and t.BILL_CODE = '" + applyInfo.getBillCode()+"'");
		List ticketList = billInfoService.findSpecialTicketById(map);
		if (null != ticketList && ticketList.size() > 0) {
			specialTicket = (SpecialTicket) ticketList.get(0);
		} else {
			this.message = "还未填写申请单！";
			applyInfo = null;
			return "cannotView";
		}
		return SUCCESS;
	}

	public String viewSpecialReceipt() {
		String billId = request.getParameter("billId");
		specialTicket = null;
		Map map = new HashMap();
		map.put("billId", billId);
		List list = inputInvoiceService.redReceiptDetail(map);
		if (null != list && list.size() > 0) {
			applyInfo = (InputRedReceiptApplyInfo) list.get(0);
		} else {
			return ERROR;
		}
		map.put("searchCondition", "t.BILL_NO ='" + applyInfo.getBillNo()
				+ "' and t.BILL_CODE = '" + applyInfo.getBillCode()+"'");
		List ticketList = billInfoService.findSpecialTicketById(map);
		if (null != ticketList && ticketList.size() > 0) {
			specialTicket = (SpecialTicket) ticketList.get(0);
		} else {
			this.message = "还未填写申请单！";
		}
		return SUCCESS;
	}

	// 专票保存
	public String inputRedReceiptSpecialSave() {
		if(null==specialTicket.getLevel1Option()){
			this.message = "请填写原因，保存失败!";
			applyInfo = null;
			return SUCCESS;
		}
		specialTicket.setBuySellInd("0");
		specialTicket.setFapiaoType("0");
		if (specialTicket.getLevel1Option().equals("0")) {
			specialTicket.setLevel2Option("");
		}
		Map map = new HashMap();
		map.put("searchCondition", "t.BILL_NO ='" + specialTicket.getBillNo()
				+ "' and t.BILL_CODE = '" + specialTicket.getBillCode()+"'");
		List ticketList = billInfoService.findSpecialTicketById(map);
		if (null != ticketList && ticketList.size() > 0) {
			SpecialTicket temp = (SpecialTicket) ticketList.get(0);
			temp.setLevel1Option(specialTicket.getLevel1Option());
			temp.setLevel2Option(specialTicket.getLevel2Option());
			billInfoService.updateSpecialTicket(temp);
		} else {
			billInfoService.saveSpecialTicket(specialTicket);
		}
		String type = request.getParameter("type");
		this.message = "保存成功!";
		if (null != type && type.equals("commit")) {
			map.put("billId", specialTicket.getBillId());
			List list = inputInvoiceService.findInvoiceInfoList(map);
			if (null != list && list.size() > 0) {
				InputInvoiceInfo inputInvoiceInfo = (InputInvoiceInfo) list
						.get(0);
				/*String temp = inputInvoiceInfo.getDatastatus();
				inputInvoiceInfo.setOperateStatus(temp);*/
				inputInvoiceInfo.setDatastatus("12");
				inputInvoiceInfo.setCancelReason(null);
				inputInvoiceService.updateInputInvoiceInfo(inputInvoiceInfo);
				this.message = "红冲申请成功!";
			}
		}
		applyInfo = null;
		return SUCCESS;
	}

	public String commitRedReceipt() {
		String billId = request.getParameter("billId");
		Map map = new HashMap();
		map.put("billId", billId);
		List list = inputInvoiceService.findInvoiceInfoList(map);
		if (null != list && list.size() > 0) {
			InputInvoiceInfo inputInvoiceInfo = (InputInvoiceInfo) list.get(0);
			map.remove("billId");
			map.put("searchCondition",
					"t.BILL_NO ='" + inputInvoiceInfo.getBillNo()
							+ "' and t.BILL_CODE = '"
							+ inputInvoiceInfo.getBillCode()+"'");
			List ticketList = billInfoService.findSpecialTicketById(map);
			if (null != ticketList && ticketList.size() > 0) {
				String temp = inputInvoiceInfo.getDatastatus();
				inputInvoiceInfo.setOperateStatus(temp);
				inputInvoiceInfo.setDatastatus("12");
				inputInvoiceService.updateInputInvoiceInfo(inputInvoiceInfo);
				this.message = "红冲申请成功!";
				applyInfo = null;
			} else {
				this.message = "请先填写申请表!";
			}
		} else {
			return ERROR;
		}
		applyInfo = null;
		return SUCCESS;
	}

	// 红冲审核
	public String listInputRedReceiptApprove() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		instCodeList = getAuthInstList();
		try {
			if (null == request.getParameter("msg")
					|| !request.getParameter("msg").equals("message")) {
				this.request.getSession().removeAttribute("message");
				this.message = "";
			}
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				this.paginationList.setCurrentPage(1);
				this.applyInfo = new InputRedReceiptApplyInfo();
				fromFlag = null;
			}
			Map map = new HashMap();
			map.put("searchCondition", " t.DATASTATUS =12 ");
			map.put("applyInfo", applyInfo);
			invoiceInfoList = inputInvoiceService.findInputInvoiceInfoList(map,
					paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String approveSpecialticket() {
		String billId = request.getParameter("billId");
		Map map = new HashMap();
		map.put("billId", billId);
		List list = inputInvoiceService.redReceiptDetail(map);
		if (null != list && list.size() > 0) {
			applyInfo = (InputRedReceiptApplyInfo) list.get(0);
		}
		map.put("searchCondition", "t.BILL_NO ='" + applyInfo.getBillNo()
				+ "' and t.BILL_CODE = '" + applyInfo.getBillCode()+"'");
		List ticketList = billInfoService.findSpecialTicketById(map);
		if (null != ticketList && ticketList.size() > 0) {
			specialTicket = (SpecialTicket) ticketList.get(0);
		}
		return SUCCESS;
	}

	public String inputRedReceiptApprove() throws Exception {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		String billId = request.getParameter("billId");
		String result = request.getParameter("result");
		Map map = new HashMap();
		InputInvoiceInfo inputInvoiceInfo;
		if (null != result && result.equals("13")) {
			inputInvoiceInfo = new InputInvoiceInfo();
			map.put("billId", billId);
			List list = inputInvoiceService.findInvoiceInfoList(map);
			if (null != list && list.size() > 0) {
				inputInvoiceInfo = (InputInvoiceInfo) list.get(0);
			} else {
				request.setAttribute("message", "数据错误");
				return ERROR;
			}
			// inputInvoiceInfo.setCancelAuditor(getCurrentUser().getId());
			inputInvoiceInfo.setDatastatus(result);
			inputInvoiceService.updateInputInvoiceInfo(inputInvoiceInfo);
			map.remove("billId");
			this.message = "审核成功";
			this.request.setAttribute("message", this.message);
		} else {
			String cancelReason = request.getParameter("cancelReason");
			cancelReason = URLDecoder.decode(cancelReason, "utf-8");
			cancelReason = URLDecoder.decode(cancelReason, "utf-8");
			inputInvoiceInfo = new InputInvoiceInfo();
			map.put("billId", billId);
			List list = inputInvoiceService.findInvoiceInfoList(map);
			if (null != list && list.size() > 0) {
				inputInvoiceInfo = (InputInvoiceInfo) list.get(0);
			} else {
				request.setAttribute("message", "数据错误");
				return ERROR;
			}
			billInfoService.deleteApplyInfo(inputInvoiceInfo.getBillNo());
			// inputInvoiceInfo.setCancelInitiator("");
			String temp = inputInvoiceInfo.getOperateStatus();
			int c  = temp.length();
			inputInvoiceInfo.setDatastatus(temp.substring(c-2));
			inputInvoiceInfo.setCancelReason(cancelReason);
			//inputInvoiceInfo.setOperateStatus("");
			inputInvoiceService.updateInputInvoiceInfo(inputInvoiceInfo);
			
			this.message = "成功拒绝申请！";
			printWriterResult("success");
			this.request.setAttribute("message", this.message);
		}
		applyInfo = null;
		return SUCCESS;
	}

	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();

	}
	
	public String listInputRedReceipt() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		instCodeList = getAuthInstList();
		String type = request.getParameter("type");
		if (null != type && type.equals("back")) {
			applyInfo = null;
		}
		try {
			if (null == request.getParameter("msg")
					|| !request.getParameter("msg").equals("message")) {
				this.request.getSession().removeAttribute("message");
				this.message = "";
			}
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				this.paginationList.setCurrentPage(1);
				this.applyInfo = new InputRedReceiptApplyInfo();
				fromFlag = null;
			}
			Map map = new HashMap();
			map.put("searchCondition", " t.DATASTATUS =13 ");
			map.put("applyInfo", applyInfo);
			invoiceInfoList = inputInvoiceService.findInputInvoiceInfoList(map,
					paginationList);

			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲", "销项税管理", "查询可供进行红冲申请的票据信息列表", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "红冲", "销项税管理", "查询可供进行红冲申请的票据信息列表", "0");
			log.error("BillInfoAction-listBillTrack", e);
		}
		return ERROR;
	}

	// 红冲
	public String inputRedReceipt() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		String billId = request.getParameter("billId");
		String result = request.getParameter("result");
		Map map = new HashMap();
		InputInvoiceInfo inputInvoiceInfo;
		if (result.equals("14")) {
			inputInvoiceInfo = new InputInvoiceInfo();
			map.put("billId", billId);
			List list = inputInvoiceService.findInvoiceInfoList(map);
			if (null != list && list.size() > 0) {
				inputInvoiceInfo = (InputInvoiceInfo) list.get(0);
			} else {
				request.setAttribute("message", "数据错误");
				return ERROR;
			}
			inputInvoiceInfo.setDatastatus(result);
			inputInvoiceService.updateInputInvoiceInfo(inputInvoiceInfo);
			map.remove("billId");
			this.message = "发票红冲成功";
			this.request.setAttribute("message", this.message);
		} else {
			inputInvoiceInfo = new InputInvoiceInfo();
			map.put("billId", billId);
			List list = inputInvoiceService.findInvoiceInfoList(map);
			if (null != list && list.size() > 0) {
				inputInvoiceInfo = (InputInvoiceInfo) list.get(0);
			} else {
				request.setAttribute("message", "数据错误");
				return ERROR;
			}
			String temp = inputInvoiceInfo.getOperateStatus();
			/*
			inputInvoiceInfo.setOperateStatus("");*/
			int c = temp.length();
			inputInvoiceInfo.setDatastatus(temp.substring(c-2));
			inputInvoiceService.updateInputInvoiceInfo(inputInvoiceInfo);
			this.message = "成功撤销！";
			this.request.setAttribute("message", this.message);
		}
		return SUCCESS;
	}

	// item
	public String listInputItem() {
		try {
			String billId = request.getParameter("billId");
			if (StringUtil.isNotEmpty(billId)) {
				Map map = new HashMap();
				map.put("billId", billId);
				billItemList = inputInvoiceService.findItemInfo(map);
				this.request.setAttribute("billItemList", billItemList);
				this.request.setAttribute("billId", billId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillItem", e);
			return ERROR;
		}
		return SUCCESS;
	}

	public String listInputTransItem() {
		try {
			String billId = request.getParameter("billId");
			if (StringUtil.isNotEmpty(billId)) {
				Map map = new HashMap();
				map.put("billId", billId);
				List list = inputInvoiceService.findInvoiceInfoList(map);
				if (null != list && list.size() > 0) {
					map.remove("billId");
					InputInvoiceInfo temp = (InputInvoiceInfo) list.get(0);
//					String searchCondition = "BILL_CODE = '"
//							+ temp.getBillCode() + "' and BILL_NO = '"
//							+ temp.getBillNo()+"'";
					String searchCondition = "iii.BILL_ID = '" + temp.getBillId()+"'";
					map.put("searchCondition", searchCondition);
					List lstAuthInstId = new ArrayList();
					this.getAuthInstList(lstAuthInstId);
					List lstTmp=new ArrayList();
					for(int i=0;i<lstAuthInstId.size();i++){
						Organization org=(Organization)lstAuthInstId.get(i);
						lstTmp.add(org.getId());
					}
					map.put("auth_inst_ids", lstTmp);
					List transList = inputInvoiceService
							.listInputTransItem(map);
					this.request.setAttribute("transList", transList);
					this.request.setAttribute("billId", billId);
				} else {
					return ERROR;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillItem", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public void exportExcel() throws Exception{
		User currentUser = this.getCurrentUser();
		Map map = new HashMap();
		map.put("searchCondition", " t.DATASTATUS in (15，16，10，11) ");
		map.put("applyInfo", applyInfo);
		invoiceInfoList = inputInvoiceService.findInputInvoiceInfoList(map,
				paginationList);
		try {
			OutputStream os = response.getOutputStream();
				StringBuffer fileName = new StringBuffer("红冲发票信息表");
				fileName.append(".xls");
				String name = "attachment;filename="
						+ URLEncoder.encode(fileName.toString(), "UTF-8")
								.toString();
				response.setHeader("Content-type", "application/vnd.ms-excel");
				response.setHeader("Content-Disposition", name);
				toExcel(os, invoiceInfoList);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}
	
	public void toExcel(OutputStream os, List content)
			throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);

		WritableSheet ws = null;
		ws = wb.createSheet("红冲审核发票信息表", 0);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "发票代码", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "发票号码", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "开票日期", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "所属机构", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "金额", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "税额", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "发票种类", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "供应商名称", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "供应商纳税人识别号", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "发票状态", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "转出金额", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "认证日期", JXLTool.getHeader());
		

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
		for (int i = 0; i < 14; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			InputRedReceiptApplyInfo bill = (InputRedReceiptApplyInfo) content.get(i);
			int column = count++;
			Map map = new HashMap();
			map.put("Id", Integer.valueOf(i + 1));
			
			map.put("applyBillDate", bill.getBillCode());
			map.put("billDate", bill.getBillNo());
			map.put("customerName", bill.getBillDate());
			map.put("customerTaxNo", bill.getInstcode());
			map.put("billCode", bill.getAmtSum());
			map.put("billNo", bill.getTaxAmtSum());
			map.put("drawer", bill.getFapiaoType());
			map.put("taxDiskNo", bill.getVendorName());
			map.put("machineNo", bill.getVendorTaxno());
			map.put("amtSum", bill.getDatastatus());
			map.put("taxAmtSum", bill.getVatOutAmt());
			map.put("taxRate", bill.getIdentifyDate());

			setWritableSheetForRedReceiptApprove(ws, map, column);
		}
		wb.write();
		wb.close();
	}
	private void setWritableSheetForRedReceiptApprove(WritableSheet ws, Map o,
			int column) throws WriteException {

		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);

		Label cell1 = new Label(0, column, o.get("Id").toString(),
				tempCellFormat);

		// applyBillDate
		Label cell2 = new Label(1, column, o.get("applyBillDate") == null
				|| o.get("applyBillDate").equals("") ? "" : o.get(
				"applyBillDate").toString(), tempCellFormat);
		// billDate
		Label cell3 = new Label(2, column, o.get("billDate") == null
				|| o.get("billDate").equals("") ? "" : o.get("billDate")
				.toString(), tempCellFormat);
		// customerName
		Label cell4 = new Label(3, column, o.get("customerName") == null
				|| o.get("customerName").equals("") ? "" : o
				.get("customerName").toString(), tempCellFormat);
		// customerTaxNo
		Label cell5 = new Label(4, column, o.get("customerTaxNo") == null
				|| o.get("customerTaxNo").equals("") ? "" : o.get(
				"customerTaxNo").toString(), tempCellFormat);
		Label cell6 = new Label(5, column, o.get("billCode") == null
				|| o.get("billCode").equals("") ? "" : o.get("billCode")
				.toString(), tempCellFormat);
		Label cell7 = new Label(6, column,
				o.get("billNo") == null || o.get("billNo").equals("") ? "" : o
						.get("billNo").toString(), tempCellFormat);

		String fapiaoType = "";
		if (o.get("drawer") == null || o.get("drawer").equals("")) {
			fapiaoType = "";
		} else {
			if (o.get("drawer").equals("1")) {
				fapiaoType = "普通发票";
			} else {
				fapiaoType = "专用发票";
			}
		}
		Label cell8 = new Label(7, column, fapiaoType, tempCellFormat);
		
		
		Label cell9 = new Label(8, column, o.get("taxDiskNo") == null
				|| o.get("taxDiskNo").equals("") ? "" : o.get("taxDiskNo")
				.toString(), tempCellFormat);
		Label cell10 = new Label(9, column, o.get("machineNo") == null
				|| o.get("machineNo").equals("") ? "" : o.get("machineNo")
				.toString(), tempCellFormat);

		// amtSum
		
		String datastatus = "";
		if (o.get("amtSum") == null || o.get("amtSum").equals("")) {
			datastatus = "";
		} else {
			if (o.get("amtSum").equals("3")) {
				datastatus = "首次认证通过";
			} else if(o.get("amtSum").equals("5")){
				datastatus = "再次认证通过";
			}else if(o.get("amtSum").equals("7")){
				datastatus = "税务局当场认证通过";
			}
			else if(o.get("amtSum").equals("10")){
				datastatus = "已抵扣";
			}
			else if(o.get("amtSum").equals("11")){
				datastatus = "不可抵扣";
			}else{
				datastatus = "";
			}
		}
		Label cell11 = new Label(10, column, datastatus, tempCellFormat);
		
		// taxAmtSum
		Label cell12 = new Label(11, column, o.get("taxAmtSum") == null
				|| o.get("taxAmtSum").equals("") ? "" : o.get("taxAmtSum")
				.toString(), tempCellFormat);
		// taxAmtSum
		Label cell13 = new Label(12, column, o.get("taxRate") == null
				|| o.get("taxRate").equals("") ? "" : o.get("taxRate")
				.toString(), tempCellFormat);
		
		
		

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
	
	public WritableCellFormat getBodyCellStyle() {

		/*
		 * WritableFont.createFont("宋体")：设置字体为宋体 10：设置字体大小
		 * WritableFont.NO_BOLD:设置字体非加粗（BOLD：加粗 NO_BOLD：不加粗） false：设置非斜体
		 * UnderlineStyle.NO_UNDERLINE：没有下划线
		 */
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10,
				WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);

		WritableCellFormat bodyFormat = new WritableCellFormat(font);
		try {
			// 设置单元格背景色：表体为白色
			bodyFormat.setBackground(Colour.WHITE);
			// 设置表头表格边框样式
			// 整个表格线为细线、黑色
			bodyFormat
					.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

		} catch (WriteException e) {
			System.out.println("表体单元格样式设置失败！");
		}
		return bodyFormat;
	}
	
	public void exportWord() throws UnsupportedEncodingException, IOException {
		
		List itemList = new ArrayList();
		BillItemInfo item = new BillItemInfo();
		/*
		item.setGoodsName("sss");
		item.setGoodsUnit("1");
	  	item.setGoodsName("111");
    	item.setGoodsNo(new BigDecimal("1"));
    	item.setGoodsPrice(new BigDecimal("1"));
    	item.setAmt(new BigDecimal("1"));
    	item.setTaxRate(new BigDecimal("1"));
    	item.setTaxAmt(new BigDecimal("1"));
    	item.setAmt(new BigDecimal("1"));
    	item.setTaxAmt(new BigDecimal("1"));
    	itemList.add(item);
		Map map = new HashMap(); 
		map.put("tkDate", "777");
		map.put("cancelName", "777");
		map.put("cancelTaxno", "777");
		map.put("customerName", "77");
		map.put("customerTaxno", "777");
		map.put("customerBillCode", "777");
		map.put("customerBillNo", "777");
		map.put("cancelBillCode", "777");
		map.put("cancelBillNo", "777777");
		map.put("dealNo", "7777");
		map.put("buySellInd", "0");
		map.put("level1Option", "1");
		map.put("level2Option", "1");
		map.put("billItemList", itemList);
		*/
		String billId = request.getParameter("billId");
		Map map = new HashMap();
		map.put("billId", billId);
		itemList = inputInvoiceService.findItemInfo(map);
		
		List list = inputInvoiceService.redReceiptDetail(map);
		if (null != list && list.size() > 0) {
			applyInfo = (InputRedReceiptApplyInfo) list.get(0);
		}
		map.put("searchCondition", "t.BILL_NO ='" + applyInfo.getBillNo()
				+ "' and t.BILL_CODE = '" + applyInfo.getBillCode()+"'");
		List ticketList = billInfoService.findSpecialTicketById(map);
		if (null != ticketList && ticketList.size() > 0) {
			specialTicket = (SpecialTicket) ticketList.get(0);
		}
		List billList = new ArrayList();
		BillItemInfo bill;
		for(int i=0;i<itemList.size();i++){
			bill = new BillItemInfo();
			InputInvoiceItem temp = (InputInvoiceItem)itemList.get(i);
			bill.setGoodsName(temp.getGoodsName());
			bill.setGoodsUnit(temp.getGoodsUnit());
			bill.setGoodsNo(new BigDecimal(temp.getGoodsNo()));
			bill.setGoodsPrice(new BigDecimal(temp.getGoodsPrice()));
			bill.setAmt(new BigDecimal(temp.getAmt()));
			bill.setTaxRate(new BigDecimal(temp.getTaxRate()));
	    	bill.setTaxAmt(temp.getTaxAmt());
	    	billList.add(bill);
		}
		map.put("tkDate", applyInfo.getBillDate());
		map.put("cancelName", applyInfo.getVendorName());
		map.put("cancelTaxno", applyInfo.getVendorTaxno());
		map.put("customerName", applyInfo.getName());
		map.put("customerTaxno", applyInfo.getTaxno());
		map.put("customerBillCode", applyInfo.getBillCode());
		map.put("customerBillNo", applyInfo.getBillNo());
		map.put("cancelBillCode", "");
		map.put("cancelBillNo", "");
		map.put("dealNo", "");
		map.put("buySellInd", specialTicket.getBuySellInd());
		map.put("level1Option", specialTicket.getLevel1Option());
		map.put("level2Option", specialTicket.getLevel2Option());
		map.put("billItemList", billList);
		String fileName = vmsCommonService.createWord(map);
		String name = "attachment;filename="
			+ URLEncoder.encode(fileName.toString(), "UTF-8")
					.toString();
		/*response.setHeader("Content-type","application/vnd.ms-word;charset=UTF-8");
		response.setHeader("Content-Disposition", name);
		response.setCharacterEncoding("UTF-8");*/
		
		  if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0){
		   fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");//firefox浏览器
		  }else {
		       if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0){
		        fileName = URLEncoder.encode(fileName, "UTF-8");//IE浏览器
		       }
		  }  
		  response.setContentType("text/plain");
		  response.setHeader("Location",fileName);
		  response.reset();
		  response.setHeader("Cache-Control", "max-age=0" );
		  response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		 BufferedInputStream bis = null;
		  BufferedOutputStream bos = null;
		  OutputStream fos = null;
		  InputStream fis = null;
		  String classPath = this.getClass().getClassLoader().getResource("").getPath();
		  String rootPath = classPath.replaceAll("WEB-INF/classes/","");
		  String tplPath = rootPath+"/template/bill";
		  String filePath = tplPath+"/outWord/"+fileName;
		  fis = new FileInputStream(filePath);
		     bis = new BufferedInputStream(fis);
		     fos = response.getOutputStream();
		     bos = new BufferedOutputStream(fos);
		int bytesRead = 0;
		     byte[] buffer = new byte[5 * 1024];
		     while ((bytesRead = bis.read(buffer)) != -1) {
		         bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
		     }
		     bos.close();
		     bis.close();
		     fos.close();
		     fis.close();
	}
	public void exportXML(){
			StringBuffer sbMsg = new StringBuffer();
			String curDate = DateUtils.serverCurrentDate();
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String instCode = currentUser.getOrgId();
			try {
				// billType 区分是哪种类型的发票
				// 增值税专用发票 billType=1 增值税普通发票=4
				String billType = (String) request.getParameter("billType");
				String tableId = "";
				String subTableId = "";
				// 用工厂类创建一个document实例
				Document doc = DocumentHelper.createDocument();
				
						// 将document中的内容写入文件中
				Element test = doc.addElement("test");
				test.addText("test");
				int bufferSize = 65000;
				doc.setXMLEncoding("GBK");
				byte[] bytes = doc.asXML().getBytes("GBK");
				ByteArrayInputStream inputstream = new ByteArrayInputStream(
						bytes);
				byte abyte0[] = new byte[bufferSize];

				response.setContentType("application/octet-stream");
				response.setContentLength((int) bytes.length);
				// 增值税专用发票 billType=1 增值税普通发票=4 货物运输业增值税专用发票=5 机动车销售统一发票=6
				response.setHeader("Content-Disposition",
						"attachment;filename=" + curDate + instCode
								+ "spec.xml");
				int sum = 0;
				int k = 0;
				OutputStream output = response.getOutputStream();
				while ((k = inputstream.read(abyte0, 0, bufferSize)) > -1) {
					output.write(abyte0, 0, k);
					sum += k;
				}
				inputstream.close();
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
	}
	
	public String inputApplyListToCancelReason() {
		try {
			String billId = request.getParameter("billId");
			inputInvoice = inputInvoiceService.findInputInvoiceDetail(billId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}
	
	public String inputApproveListToCancelReason() {
		request.setAttribute("billId", request.getParameter("billId"));
		request.setAttribute("result", request.getParameter("result"));
		return SUCCESS;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public InputRedReceiptApplyInfo getApplyInfo() {
		return applyInfo;
	}

	public void setApplyInfo(InputRedReceiptApplyInfo applyInfo) {
		this.applyInfo = applyInfo;
	}

	public List getInvoiceInfoList() {
		return invoiceInfoList;
	}

	public void setInvoiceInfoList(List invoiceInfoList) {
		this.invoiceInfoList = invoiceInfoList;
	}

	public SpecialTicket getSpecialTicket() {
		return specialTicket;
	}

	public void setSpecialTicket(SpecialTicket specialTicket) {
		this.specialTicket = specialTicket;
	}

	public List getBillItemList() {
		return billItemList;
	}

	public void setBillItemList(List billItemList) {
		this.billItemList = billItemList;
	}

	public List getBillDataStatusList() {
		return billDataStatusList;
	}

	public void setBillDataStatusList(List billDataStatusList) {
		this.billDataStatusList = billDataStatusList;
	}

	public Map getMapVatType() {
		return mapVatType;
	}

	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}
	
	public Map getMapDataStatus() {
		return mapDataStatus;
	}

	public void setMapDataStatus(Map mapDataStatus) {
		this.mapDataStatus = mapDataStatus;
	}
	
	public List getBillsList() {
		return billsList;
	}

	public void setBillsList(List billsList) {
		this.billsList = billsList;
	}

	public String getUrlBillImage() {
		return urlBillImage;
	}

	public void setUrlBillImage(String urlBillImage) {
		this.urlBillImage = urlBillImage;
	}

	public List getInstCodeList() {
		return instCodeList;
	}

	public void setInstCodeList(List instCodeList) {
		this.instCodeList = instCodeList;
	}

	
	// 徐海洋end
	//页面变量传递参数
	// 开票起止日期
	private String billBeginDate_s;
	private String billEndDate_s;
	// 供应商名称
	private String vendorName_s;
	// 发票状态
	private String dataStatus_s;
	// 所属机构
	private String instCode_s;
	// 发票代码
	private String billCode_s;
	// 票据类型
	private String faPiaoType_s;
	// 认证日期
	private String identifyDateBegin_s;
	private String identifyDateEnd_s;

	public String getBillBeginDate_s() {
		return billBeginDate_s;
	}

	public void setBillBeginDate_s(String billBeginDate_s) {
		this.billBeginDate_s = billBeginDate_s;
	}

	public String getBillEndDate_s() {
		return billEndDate_s;
	}

	public void setBillEndDate_s(String billEndDate_s) {
		this.billEndDate_s = billEndDate_s;
	}

	public String getVendorName_s() {
		return vendorName_s;
	}

	public void setVendorName_s(String vendorName_s) {
		this.vendorName_s = vendorName_s;
	}

	public String getDataStatus_s() {
		return dataStatus_s;
	}

	public void setDataStatus_s(String dataStatus_s) {
		this.dataStatus_s = dataStatus_s;
	}

	public String getInstCode_s() {
		return instCode_s;
	}

	public void setInstCode_s(String instCode_s) {
		this.instCode_s = instCode_s;
	}

	public String getBillCode_s() {
		return billCode_s;
	}

	public void setBillCode_s(String billCode_s) {
		this.billCode_s = billCode_s;
	}

	public String getFaPiaoType_s() {
		return faPiaoType_s;
	}

	public void setFaPiaoType_s(String faPiaoType_s) {
		this.faPiaoType_s = faPiaoType_s;
	}

	public String getIdentifyDateBegin_s() {
		return identifyDateBegin_s;
	}

	public void setIdentifyDateBegin_s(String identifyDateBegin_s) {
		this.identifyDateBegin_s = identifyDateBegin_s;
	}

	public String getIdentifyDateEnd_s() {
		return identifyDateEnd_s;
	}

	public void setIdentifyDateEnd_s(String identifyDateEnd_s) {
		this.identifyDateEnd_s = identifyDateEnd_s;
	}

	public String getVendorTaxNo() {
		return vendorTaxNo;
	}

	public void setVendorTaxNo(String vendorTaxNo) {
		this.vendorTaxNo = vendorTaxNo;
	}

}
