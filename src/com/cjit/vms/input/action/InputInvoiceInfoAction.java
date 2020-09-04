package com.cjit.vms.input.action;

import java.io.IOException;
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
import jxl.write.biff.RowsExceededException;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.input.service.InputInvoiceInfoService;
import com.cjit.vms.input.service.InputTransService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.util.DataUtil;

/**
 * 进项税-抵扣预警Action类
 * 
 */
public class InputInvoiceInfoAction extends DataDealAction{
	private InputTransService inputTransService;


	public String inputInvoiceInfoList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			InputInvoiceNew info= new InputInvoiceNew();
			info.setIndustryType(request.getParameter("industryType"));
			System.out.println("info==="+info.getIndustryType());
			info.setBillDate(request.getParameter("billDate"));
			info.setTaxNo(request.getParameter("vendorTaxno"));
			info.setDataStatus(request.getParameter("datastatus"));
			info.setBillCode(request.getParameter("billCode"));
			info.setBillNo(request.getParameter("billNo"));
			info.setBillType("0");
			info.setDeducBeginDate(request.getParameter("billStartDate"));
			info.setDeducEndDate(request.getParameter("billEndDate"));
			info.setLstAuthInstId(lstAuthInstId);
			info.setUserId(currentUser.getId());
			inputInvoiceInfoService.getInputInvoiceInfoList(info, paginationList);
			
			
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");			
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDataStatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				if("1".equals(tag.getValue()) || "2".equals(tag.getValue()) || "4".equals(tag.getValue()) || "6".equals(tag.getValue()) || "8".equals(tag.getValue())){
					mapDataStatus.put(tag.getValue(), tag.getText());
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InputInvoiceInfoAction-inputInvoiceInfoList", e);
		}
		return ERROR;
	}
	
	
	/**
	 * @Action
	 * 
	 * 查看数据
	 * 
	 * @return
	 */
	public String inputInvoiceInfoViewData(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			String billId = request.getParameter("billId");
			inputInvoiceInfo= inputInvoiceInfoService.getInputInvoiceInfoDetail(billId);
			itemList = inputInvoiceInfoService.getInputInvoiceItemList(billId,null);
			transList = inputInvoiceInfoService.getInputInvoiceTransList(billId,null);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InputInvoiceInfoAction-inputInvoiceInfoViewData", e);
		}
		return ERROR;
	}
	/**
	 * @Action
	 * 
	 * 查看票据
	 * 
	 * @return
	 */
	public String inputInvoiceInfoViewImg(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			String billId = request.getParameter("billId");
			inputInvoiceInfo= inputInvoiceInfoService.getInputInvoiceInfoDetail(billId);
			String contextPath = request.getContextPath(); 
			if(null!=inputInvoiceInfo && null!=inputInvoiceInfo.getUrlBillImage() && !"".equals(inputInvoiceInfo.getUrlBillImage())){
				urlBillImage = contextPath+inputInvoiceInfo.getUrlBillImage();
			}else{
				urlBillImage = "";
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InputInvoiceInfoAction-inputInvoiceInfoViewImg", e);
		}
		return ERROR;
	}
	/**
	 * @Action
	 * 
	 * 抵扣预警帐票excel出力
	 * 
	 * @return
	 */
	public void inputInvoiceInfoExcel() throws Exception{
		try{
			StringBuffer fileName = new StringBuffer("进项税管理-抵扣预警");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();

			WritableWorkbook wb = Workbook.createWorkbook(os);
			// sheet1的作成
			writeToExcel1(os, wb);
			wb.write();
			wb.close();
			os.flush();
			os.close();
		}catch (Exception e){
			log.error(e);
			throw e;
		}
	}
	/**
	 * @Action
	 * 
	 * sheet1的作成
	 * 
	 * @return
	 */
	private void writeToExcel1(OutputStream os, WritableWorkbook wb) throws IOException,
			RowsExceededException, WriteException, Exception {

		WritableSheet ws = null;
		ws = wb.createSheet("抵扣预警", 0);
		/*WritableCellFormat wc = new WritableCellFormat();
        // 设置单元格的背景颜色
        wc.setBackground(jxl.format.Colour.YELLOW);*/
        JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header0 = new Label(0, 0, "序号", JXLTool.getHeaderC(excelInfo));
		Label header1 = new Label(1, 0, "发票代码", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(2, 0, "发票号码", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(3, 0, "开票日期", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(4, 0, "所属机构", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(5, 0, "金额", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(6, 0, "税额", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(7, 0, "发票类型", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(8, 0, "供应商名称", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(9, 0, "供应商纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header10 = new Label(10, 0, "发票状态", JXLTool.getHeaderC(excelInfo));
		Label header11 = new Label(11, 0, "认证剩余时间", JXLTool.getHeaderC(excelInfo));
		ws.addCell(header0);
		ws.setColumnView(0, 7);
		ws.addCell(header1);
		ws.setColumnView(1, 20);
		ws.addCell(header2);
		ws.setColumnView(2, 18);
		ws.addCell(header3);
		ws.setColumnView(3, 15);
		ws.addCell(header4);
		ws.setColumnView(4, 15);
		ws.addCell(header5);
		ws.setColumnView(5, 10);
		ws.addCell(header6);
		ws.setColumnView(6, 15);
		ws.addCell(header7);
		ws.setColumnView(7, 15);
		ws.addCell(header8);
		ws.setColumnView(8, 15);
		ws.addCell(header9);
		ws.setColumnView(9, 22);
		ws.addCell(header10);
		ws.setColumnView(10, 15);
		ws.addCell(header11);
		ws.setColumnView(11, 18);
		
		List lstAuthInstId=new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		InputInvoiceInfo info= new InputInvoiceInfo();
		info.setInstcode(request.getParameter("instCode"));
		info.setBillDate(request.getParameter("billDate"));
		info.setVendorTaxno(request.getParameter("vendorTaxno"));
		info.setDatastatus(request.getParameter("datastatus"));
		info.setBillCode(request.getParameter("billCode"));
		info.setFapiaoType("0");
		info.setBillStartDate(request.getParameter("billStartDate"));
		info.setBillEndDate(request.getParameter("billEndDate"));
		info.setLstAuthInstId(lstAuthInstId);
		// 一览数据检索
//		List outList = inputInvoiceInfoService.getInputInvoiceInfoList(info, null);
		List outList = inputInvoiceInfoService.getInputInvoiceInfoListNew(info, null);
		mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
		int count = 1;
		for (int i = 0; i < outList.size(); i++) {
			InputInvoiceInfo outInfo = (InputInvoiceInfo) outList.get(i);
			int column = count++;
			setWritableSheet1(ws, outInfo, mapVatType, column);
		}
	}
	/**
	 * @Action
	 * 
	 * 销项附加税详细列表数据
	 * 
	 * @return
	 */
	private void setWritableSheet1(WritableSheet ws, InputInvoiceInfo info,Map mapVatType, int column) throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column), JXLTool.getContentFormat());
		// 发票代码
		Label cell2 = new Label(1, column, info.getBillCode(), JXLTool.getContentFormat());
		// 发票号码
		Label cell3 = new Label(2, column, info.getBillNo(), JXLTool.getContentFormat());
		// 开票日期
		Label cell4 = new Label(3, column, info.getBillDate(), JXLTool.getContentFormat());
		// 所属机构
		Label cell5 = new Label(4, column, info.getInstName(), JXLTool.getContentFormat());
		// 金额
		Label cell6 = new Label(5, column, info.getAmtSum()+"", JXLTool.getContentFormat());
		// 税额
		Label cell7 = new Label(6, column, info.getTaxAmtSum()+"", JXLTool.getContentFormat());
		// 发票类型
		Label cell8 = new Label(7, column, mapVatType.get(info.getFapiaoType())+"", JXLTool.getContentFormat());
		// 供应商名称
		Label cell9 = new Label(8, column, info.getVendorName(), JXLTool.getContentFormat());
		// 供应商纳税人识别号
		Label cell10 = new Label(9, column, info.getVendorTaxno(), JXLTool.getContentFormat());
		// 发票状态
		Label cell11 = new Label(10, column, DataUtil.getDataStatusCHNew(info.getDatastatus(), "INPUT_INVOICE"), JXLTool.getContentFormat());
		// 认证剩余时间
		Label cell12 = new Label(11, column, info.getVerifyDataRemain(), JXLTool.getContentFormat());

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
	
	
	//页面值传递变量声明
	private String instCode;//机构
	private String instName;//机构
	private String billDate;//开票日期
	private String vendorTaxno;//销方纳税人识别号
	private String datastatus;//状态
	private String billCode;//发票代码
	private String fapiaoType;// 发票类型
	private String billStartDate;//认证开始时间
	private String billEndDate;//认证结束时间
	private Map mapVatType;//发票种类map
	private Map mapDataStatus;//发票状态map
	
	
	private List industryTypeList;
	/*service 声明*/
	private InputInvoiceInfoService inputInvoiceInfoService;
	private InputInvoiceInfo inputInvoiceInfo;// 对应的主体
	private List itemList; 
	private List transList; 
	private String urlBillImage;//票据图片地址

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
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

	public InputInvoiceInfoService getInputInvoiceInfoService() {
		return inputInvoiceInfoService;
	}

	public void setInputInvoiceInfoService(
			InputInvoiceInfoService inputInvoiceInfoService) {
		this.inputInvoiceInfoService = inputInvoiceInfoService;
	}

	public Map getMapVatType() {
		return mapVatType;
	}

	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}

	public String getVendorTaxno() {
		return vendorTaxno;
	}

	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public void setVendorTaxno(String vendorTaxno) {
		this.vendorTaxno = vendorTaxno;
	}

	public Map getMapDataStatus() {
		return mapDataStatus;
	}

	public void setMapDataStatus(Map mapDataStatus) {
		this.mapDataStatus = mapDataStatus;
	}

	public String getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}
	public InputInvoiceInfo getInputInvoiceInfo() {
		return inputInvoiceInfo;
	}
	public void setInputInvoiceInfo(InputInvoiceInfo inputInvoiceInfo) {
		this.inputInvoiceInfo = inputInvoiceInfo;
	}
	public List getItemList() {
		return itemList;
	}
	public void setItemList(List itemList) {
		this.itemList = itemList;
	}
	public List getTransList() {
		return transList;
	}
	public void setTransList(List transList) {
		this.transList = transList;
	}
	public String getUrlBillImage() {
		return urlBillImage;
	}
	public void setUrlBillImage(String urlBillImage) {
		this.urlBillImage = urlBillImage;
	}

	//add by sinosoft daimeng 2016-05-25
	public String inputInvoiceInfoNewList(){
		// 获取行业类型
		industryTypeList = inputInvoiceInfoService.findIndustryTypeList();
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			InputInvoiceInfo info= new InputInvoiceInfo();
			info.setInstcode(request.getParameter("instCode"));
			info.setBillDate(request.getParameter("billDate"));
			info.setVendorTaxno(request.getParameter("vendorTaxno"));
			info.setDatastatus(request.getParameter("datastatus"));
			info.setBillCode(request.getParameter("billCode"));
			info.setBillNo(request.getParameter("billNo"));
			info.setFapiaoType("0");
			info.setBillStartDate(request.getParameter("billStartDate"));
			info.setBillEndDate(request.getParameter("billEndDate"));
			info.setLstAuthInstId(lstAuthInstId);
			info.setUserId(currentUser.getId());
			inputInvoiceInfoService.getInputInvoiceInfoListNew(info, paginationList);
			
			
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");			
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusListNew();
			mapDataStatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
//				if("1".equals(tag.getValue()) || "2".equals(tag.getValue()) || "4".equals(tag.getValue()) || "6".equals(tag.getValue()) || "8".equals(tag.getValue())){
					mapDataStatus.put(tag.getValue(), tag.getText());
//				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InputInvoiceInfoAction-inputInvoiceInfoList", e);
		}
		return ERROR;
	}
	
	public List getIndustryTypeList()
	{
		return industryTypeList;
	}

	public void setIndustryTypeList(List industryTypeList)
	{
		this.industryTypeList = industryTypeList;
	}

	
}
