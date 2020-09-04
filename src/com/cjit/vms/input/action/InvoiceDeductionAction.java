package com.cjit.vms.input.action;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.input.model.InputTransInfo;
import com.cjit.vms.input.service.InputTransService;
import com.cjit.vms.input.service.InvoiceDeductionService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.util.DataUtil;

/**
 * 进项税-抵扣处理Action类
 *
 * @author jobell
 */
public class InvoiceDeductionAction extends DataDealAction {
	
	InputInvoiceNew inputInvoiceNew = new InputInvoiceNew();
	private InputTransService inputTransService;
	private List industryTypeList;

	/**
	 * @Action 进项税-抵扣处理 查询页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String listInvoiceDeduction(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			this.getAuthInstList(lstAuthInstId);
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			mapDatastatus=new HashMap();
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			for(int i=lstDataStatus.size()-1;i>=0;i--){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				if(tag.getValue().equals("16") || tag.getValue().equals("11") || tag.getValue().equals("10")){
					mapDatastatus.put(tag.getValue(), tag.getText());
				}
			}
//			if(null == getDatastatus() || "".equals(getDatastatus())){
//				setDatastatus("16");
//			}
			InputInvoiceInfo info = new InputInvoiceInfo();
			info.setBillDate(billDate);
			info.setVendorName(vendorName);
			info.setBillCode(billCode);
			info.setFapiaoType(fapiaoType);
			info.setLstAuthInstId(lstAuthInstId);
			info.setBillNo(billNo);
			info.setInstcode(instId);
			info.setDatastatus(dataStatus);
			invoiceDeductionService.findInvoiceDeductionList(info,paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceDeductionAction-listInvoiceDeduction", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 进项税-抵扣处理 撤回数据处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String rollbackInvoiceDeduction(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			invoiceDeductionService.saveRollbackInvoiceDeduction(billId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceDeductionAction-rollbackInvoiceDeduction", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 进项税-抵扣处理 导出EXCEL
	 * 
	 * @author jobell
	 * @return
	 */
	public String exportInvoiceDeduction(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			this.getAuthInstList(lstAuthInstId);
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			mapDatastatus=new HashMap();
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				mapDatastatus.put(tag.getValue(), tag.getText());
			}
			InputInvoiceNew info = new InputInvoiceNew();
			info.setBillDate(billDate);
			info.setVendorName(vendorName);
			info.setBillCode(billCode);
			info.setBillType(fapiaoType);
			info.setIndustryType(industryType);
			info.setDataStatus(datastatus);
			List lstInputInvoiceInfo=invoiceDeductionService.findInvoiceDeductionListNew(info,null);
			
			StringBuffer fileName = new StringBuffer("进项附发票抵扣处理");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();

			WritableWorkbook wb = Workbook.createWorkbook(os);
			writeToExcel(os,lstInputInvoiceInfo, wb);
			wb.write();
			wb.close();
			os.flush();
			os.close();
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceDeductionAction-exportInvoiceDeduction", e);
		}
		return ERROR;
	}
	
	private void writeToExcel(OutputStream os, List lstInputInvoiceInfo,
			WritableWorkbook wb) throws WriteException {

		WritableSheet ws = null;
		ws = wb.createSheet("进项附发票抵扣处理", 0);
		/*WritableCellFormat wc = new WritableCellFormat();
        // 设置单元格的背景颜色
        wc.setBackground(jxl.format.Colour.YELLOW);*/
        JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header0 = new Label(0, 0, "发票代码", JXLTool.getHeaderC(excelInfo));
		Label header1 = new Label(1, 0, "发票号码", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(2, 0, "开票日期", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(3, 0, "所属机构", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(4, 0, "金额", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(5, 0, "税额", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(6, 0, "发票种类", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(7, 0, "供应商名称", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(8, 0, "供应商纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(9, 0, "发票状态", JXLTool.getHeaderC(excelInfo));
//		Label header10 = new Label(9, 0, "认证日期", JXLTool.getHeaderC(excelInfo));
//		Label header11 = new Label(9, 0, "扫描时间", JXLTool.getHeaderC(excelInfo));
		ws.addCell(header0);
		ws.setColumnView(0, 15);
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
		ws.setColumnView(8, 25);
		ws.addCell(header9);
		ws.setColumnView(9, 15);
//		ws.addCell(header10);
//		ws.setColumnView(10, 15);
//		ws.addCell(header11);
//		ws.setColumnView(11, 15);
		if(null != lstInputInvoiceInfo && lstInputInvoiceInfo.size()>0){
			for(int i=0;i<lstInputInvoiceInfo.size();i++){
				InputInvoiceInfo inInfo=(InputInvoiceInfo) lstInputInvoiceInfo.get(i);
				setWritableSheet1(ws, inInfo, i+1);
			}		
		}
	}
	
	/**
	 * @Action
	 * 
	 * 销项附加税详细列表数据
	 * 
	 * @return
	 */
	private void setWritableSheet1(WritableSheet ws, InputInvoiceInfo info, int column) throws WriteException {
		// 发票代码
		Label cell1 = new Label(0, column, info.getBillCode(), JXLTool.getContentFormat());
		// 发票号码
		Label cell2 = new Label(1, column, info.getBillNo(), JXLTool.getContentFormat());
		// 开票日期
		Label cell3 = new Label(2, column, info.getBillDate(), JXLTool.getContentFormat());
		// 所属机构
		Label cell4 = new Label(3, column, info.getInstName(), JXLTool.getContentFormat());
		// 金额
		Label cell5 = new Label(4, column, info.getAmtSum().toString(), JXLTool.getContentFormat());
		// 税额
		Label cell6 = new Label(5, column, info.getTaxAmtSum().toString(), JXLTool.getContentFormat());
		// 发票种类
		Label cell7 = new Label(6, column, DataUtil.getFapiaoTypeCH(info.getFapiaoType()), JXLTool.getContentFormat());
		// 供应商名称
		Label cell8 = new Label(7, column, info.getVendorName(), JXLTool.getContentFormat());
		// 供应商纳税人识别号
		Label cell9 = new Label(8, column, info.getVendorTaxno(), JXLTool.getContentFormat());
		// 发票状态
		Label cell10 = new Label(9, column, DataUtil.getDataStatusCHNew(info.getDatastatus(), "INPUT_INVOICE"), JXLTool.getContentFormat());
//		//认证日期
//		Label cell11 = new Label(9, column, info.getIdentifyDate(), JXLTool.getContentFormat());
//		//扫描时间
//		Label cell12 = new Label(9, column, info.getScanDate(), JXLTool.getContentFormat());

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
//		ws.addCell(cell11);
//		ws.addCell(cell12);
	}
	
	/**
	 * @Action 进项税-抵扣处理 编辑初始化页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String editInvoiceDeduction(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			inputInvoiceInfo=invoiceDeductionService.findInvoiceDeductionByBillId(o_bill_id);
			String bill_code=inputInvoiceInfo.getBillCode();
			String bill_no=inputInvoiceInfo.getBillNo();
			
			inputTransInfo=invoiceDeductionService.findInvoiceDeductionTransInfoByBillCodeAndBillNo(bill_code,bill_no);
			lstInputInvoiceItem=invoiceDeductionService.findInvoiceDeductionItemsByBillId(o_bill_id);
			
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				if(Integer.parseInt(tag.getValue())==10 ||Integer.parseInt(tag.getValue())==11){
					mapDatastatus.put(tag.getValue(), tag.getText());
				}
				continue; 
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceDeductionAction-editInvoiceDeduction", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 进项税-抵扣处理 编辑保存处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String updateInvoiceDeduction(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			invoiceDeductionService.updateVmsInputInvoiceInfoForDeduction(inputInvoiceInfo,o_bill_id);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceDeductionAction-updateInvoiceDeduction", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 进项税-抵扣处理 查看数据
	 * 
	 * @author jobell
	 * @return
	 */
	public String viewInvoiceDeduction(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			inputInvoiceInfo=invoiceDeductionService.findInvoiceDeductionByBillId(o_bill_id);
			String bill_code=inputInvoiceInfo.getBillCode();
			String bill_no=inputInvoiceInfo.getBillNo();
			inputTransInfo=invoiceDeductionService.findInvoiceDeductionTransInfoByBillCodeAndBillNo(bill_code,bill_no);
			lstInputInvoiceItem=invoiceDeductionService.findInvoiceDeductionItemsByBillId(o_bill_id);
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				mapDatastatus.put(tag.getValue(), tag.getText());
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceDeductionAction-viewInvoiceDeduction", e);
		}
		return ERROR;
	}
	
	//页面传递参数定义
	private String billDate;//开票日期[查询]
	private String vendorName;//供应商名称[查询]
	private String datastatus;//数据状态[查询]
	private String billCode;//发票代码[查询]
	private String fapiaoType;//发票种类[查询]
	private String[] billId;//
	private String o_bill_id;//发票号码[查看，编辑]
	private String billNo;
	private String instId;
	private String instName;
	private String billNoAndCode ;
	private String industryType;
	private String dataStatus;
	
	
	private InputInvoiceInfo inputInvoiceInfo;//[编辑，查看]
	private List lstInputInvoiceItem;//[编辑，查看]
	private InputTransInfo inputTransInfo;
	private String currentPage;//当前页数[编辑，查看]
	
	private Map mapVatType;//发票种类下拉列表Map[查询，展示]
	private Map mapDatastatus;//发票状态下拉列表Map[查询，展示]
	private List lstAuthInstId = new ArrayList();//所属机构下拉列表[查询]
	
	
	/**
	 * @Action 进项税-抵扣处理 查询页面
	 * 
	 * @author dm
	 * @return
	 */
	public String listInvoiceDeductionNew(){
		// 获取行业类型
		industryTypeList = invoiceDeductionService.findIndustryTypeList();
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			this.getAuthInstList(lstAuthInstId);
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			mapDatastatus=new HashMap();
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusListNew();
			for(int i=lstDataStatus.size()-1;i>=0;i--){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
//				if(tag.getValue().equals("16") || tag.getValue().equals("11") || tag.getValue().equals("10")){
					mapDatastatus.put(tag.getValue(), tag.getText());
//				}
			}
//			if(null == getDatastatus() || "".equals(getDatastatus())){
//				setDatastatus("1");
//			}
			InputInvoiceNew info = new InputInvoiceNew();
			info.setBillDate(billDate);
			info.setVendorName(vendorName);
			info.setBillCode(billCode);
//			info.setFapiaoType(fapiaoType);
			info.setLstAuthInstId(lstAuthInstId);
			info.setBillNo(billNo);
			info.setIndustryType(industryType);
			info.setDataStatus(dataStatus);
			invoiceDeductionService.findInvoiceDeductionListNew(info,paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceDeductionAction-listInvoiceDeduction", e);
		}
		return ERROR;
	}
	
	
	
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getDatastatus() {
		return datastatus;
	}
	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String[] getBillId() {
		return billId;
	}
	public void setBillId(String[] billId) {
		this.billId = billId;
	}
	public String getO_bill_id() {
		return o_bill_id;
	}
	public void setO_bill_id(String o_bill_id) {
		this.o_bill_id = o_bill_id;
	}
	public Map getMapVatType() {
		return mapVatType;
	}
	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}
	public Map getMapDatastatus() {
		return mapDatastatus;
	}
	public void setMapDatastatus(Map mapDatastatus) {
		this.mapDatastatus = mapDatastatus;
	}
	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public InputInvoiceInfo getInputInvoiceInfo() {
		return inputInvoiceInfo;
	}
	public void setInputInvoiceInfo(InputInvoiceInfo inputInvoiceInfo) {
		this.inputInvoiceInfo = inputInvoiceInfo;
	}
	public List getLstInputInvoiceItem() {
		return lstInputInvoiceItem;
	}
	public void setLstInputInvoiceItem(List lstInputInvoiceItem) {
		this.lstInputInvoiceItem = lstInputInvoiceItem;
	}
	public InputTransInfo getInputTransInfo() {
		return inputTransInfo;
	}
	public void setInputTransInfo(InputTransInfo inputTransInfo) {
		this.inputTransInfo = inputTransInfo;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	/*Service 注入*/
	private InvoiceDeductionService invoiceDeductionService;
	public InvoiceDeductionService getInvoiceDeductionService() {
		return invoiceDeductionService;
	}
	public void setInvoiceDeductionService(
			InvoiceDeductionService invoiceDeductionService) {
		this.invoiceDeductionService = invoiceDeductionService;
	}
	
	/**
	 * @Action 进项税-抵扣处理 
	 * 
	 * @author daimeng
	 * @return
	 */
	
	private String selectedIds ;
	
	public String InvoiceDeductionNew(){
		// 获取行业类型
		industryTypeList = invoiceDeductionService.findIndustryTypeList();
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		String Ids[] = selectedIds.split(",");
		List idList = new ArrayList<String>();
		List insertList = new ArrayList<InputInvoiceNew>();
		for(String id :Ids){
			InputInvoiceNew inputInvoiceNew = invoiceDeductionService.findInputTransByIdCodeNoAndNo(id);
			insertList.add(inputInvoiceNew);
			idList.add(id);
		}
		Map idMap = new HashMap<String, List<String>>();
		idMap.put("idList", idList);
		try {
			invoiceDeductionService.updateInvoiceInfoForDeductionNew(idMap);
			
			//往表中插入数据的时候先查询出数据
			idMap.put("insertList",insertList);
			invoiceDeductionService.saveInputInvoice(idMap);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceDeductionAction-updateInvoiceDeduction", e);
		}
		return ERROR;
	}

	public String getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}
	
	public String invoiceDeductionListNew(){
		 inputInvoiceNew = invoiceDeductionService.findInputTransByIdCodeNoAndNo(billNoAndCode);
		return SUCCESS;
	}

	public InputInvoiceNew getInputInvoiceNew() {
		return inputInvoiceNew;
	}

	public void setInputInvoiceNew(InputInvoiceNew inputInvoiceNew) {
		this.inputInvoiceNew = inputInvoiceNew;
	}

	public String getBillNoAndCode() {
		return billNoAndCode;
	}

	public void setBillNoAndCode(String billNoAndCode) {
		this.billNoAndCode = billNoAndCode;
	}
	
	
	public List getIndustryTypeList()
	{
		return industryTypeList;
	}

	public void setIndustryTypeList(List industryTypeList)
	{
		this.industryTypeList = industryTypeList;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	
	
	
}
