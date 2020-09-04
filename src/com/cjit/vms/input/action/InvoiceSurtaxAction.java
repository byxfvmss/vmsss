package com.cjit.vms.input.action;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
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

import org.apache.commons.lang.StringUtils;

import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.service.InvoiceSurtaxService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.util.DataUtil;

/**
 * 票据管理：进项税转出Action类
 * 
 * @author jobell
 */ 
public class InvoiceSurtaxAction extends DataDealAction{

	/**
	 * 票据管理:进项税转出查询页面
	 * 
	 * @return
	 */
	public String listInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			this.getAuthInstList(lstAuthInstId);
			
			InputInvoiceInfo info= new InputInvoiceInfo();
			info.setBillDate(StringUtils.substringBeforeLast(billDate, "."));
			info.setVendorName(vendorName);
			info.setDatastatus(datastatus);
			info.setInstcode(instId);
			info.setBillCode(billCode);
			info.setBillNo(billNo);
			info.setFapiaoType(fapiaoType);
			if(null != vatOutAmt && !"".equals(vatOutAmt)){
				info.setVatOutAmt(BigDecimal.valueOf(Double.parseDouble(vatOutAmt)));
			}
			info.setLstAuthInstId(lstAuthInstId);
			List paperInfoList=invoiceSurtaxService.findInvoiceInSurtaxList(info, paginationList);
			mapVatType=vmsCommonService.findCodeDictionary("VAT_TYPE");
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				mapDatastatus.put(tag.getValue(), tag.getText());
			}
			
			this.request.setAttribute("paperInfoList", paperInfoList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}
	
	/**
	 * 票据管理:进项税转出编辑
	 * 
	 * @return
	 * @author jobell
	 */
	public String editInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		inputInvoiceInfo=invoiceSurtaxService.findVmsInputInvoiceInfoByBillId(bill_id);
		lstInputInvoiceItem=invoiceSurtaxService.findVmsInputInvoiceItemsByBillId(bill_id);
		return SUCCESS;
	}
	
	public String uptSaveInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		invoiceSurtaxService.updateVmsInputInvoiceInfoVatOut(vatOutAmt, vatOutProportion, remark,bill_id);
		return SUCCESS;
	}

	/**
	 * @Action
	 * 
	 * 票据管理，进项税转出帐票excel出力
	 * 
	 * @author jobell
	 * @return
	 */
	public void invoiceInSurtaxExcel() throws Exception{
		try{
			
			
			InputInvoiceInfo info= new InputInvoiceInfo();
			info.setBillDate(StringUtils.substringBeforeLast(billDate, "."));
			info.setVendorName(vendorName);
			info.setDatastatus(datastatus);
			info.setBillCode(billCode);
			info.setFapiaoType(fapiaoType);
			this.getAuthInstList(lstAuthInstId);
			info.setLstAuthInstId(lstAuthInstId);
			List lstInputInvoiceInfo=invoiceSurtaxService.findInvoiceInSurtaxList(info, paginationList);
//			if(null==lstInputInvoiceInfo||lstInputInvoiceInfo.size()==0){
//				return;
//			}
			StringBuffer fileName = new StringBuffer("进项税转出");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();

			
			WritableWorkbook wb = Workbook.createWorkbook(os);
			writeToExcel1(os,lstInputInvoiceInfo, wb);
//			// 发票库存统计sheet1的作成
//			writeToExcel1(os, wb);
//			// 发票使用情况sheet2的作成
//			writeToExcel2(os, wb);
//			// 发票领用与归还统计sheet3的作成
//			writeToExcel3(os, wb);
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
	 * 销项附加税sheet1的作成
	 * 
	 * @return
	 */
	private void writeToExcel1(OutputStream os, List lstInputInvoiceInfo,WritableWorkbook wb) throws IOException,
			RowsExceededException, WriteException, Exception {

		WritableSheet ws = null;
		ws = wb.createSheet("进项附加税", 0);
		/*WritableCellFormat wc = new WritableCellFormat();
        // 设置单元格的背景颜色
        wc.setBackground(jxl.format.Colour.YELLOW);*/
        JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header0 = new Label(0, 0, "发票代码", JXLTool.getHeaderC(excelInfo));
		Label header1 = new Label(1, 0, "发票号码", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(2, 0, "开票日期", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(3, 0, "金额", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(4, 0, "税额", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(5, 0, "发票种类", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(6, 0, "供应商名称", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(7, 0, "供应商纳税人识别号", JXLTool.getHeaderC(excelInfo));
//		Label header8 = new Label(8, 0, "业务凭证编号", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(8, 0, "认证结果", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(9, 0, "转出金额", JXLTool.getHeaderC(excelInfo));
		Label header10 = new Label(10, 0, "认证日期", JXLTool.getHeaderC(excelInfo));
		ws.addCell(header0);
		ws.setColumnView(0, 7);
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
		ws.setColumnView(8, 15);
		ws.addCell(header9);
		ws.setColumnView(9, 15);
		ws.addCell(header10);
		ws.setColumnView(10, 15);
//		ws.addCell(header11);
//		ws.setColumnView(11, 15);
		mapVatType=vmsCommonService.findCodeDictionary("VAT_TYPE");
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
		Label cell1 = new Label(0, column, info.getBillCode() == null ? "" : info.getBillCode(), JXLTool.getContentFormat());
		Label cell2 = new Label(1, column, info.getBillNo() == null ? "" : info.getBillNo(), JXLTool.getContentFormat());
		Label cell3 = new Label(2, column, info.getBillDate() == null ? "" : info.getBillDate(), JXLTool.getContentFormat());
		Label cell4 = new Label(3, column, info.getAmtSum() == null ? "" : info.getAmtSum().toString(), JXLTool.getContentFormat());
		Label cell5 = new Label(4, column, info.getTaxAmtSum() == null ? "" : info.getTaxAmtSum().toString(), JXLTool.getContentFormat());
		Label cell6 = new Label(5, column, info.getFapiaoType() == null ? "" : (String)mapVatType.get(info.getFapiaoType()), JXLTool.getContentFormat());
		Label cell7 = new Label(6, column, info.getVendorName() == null ? "" : info.getVendorName(), JXLTool.getContentFormat());
		Label cell8 = new Label(7, column, info.getVendorTaxno() == null ? "" : info.getVendorTaxno(), JXLTool.getContentFormat());
		Label cell9 = new Label(8, column, info.getDatastatusName() == null ? "" : info.getDatastatusName(), JXLTool.getContentFormat());
		Label cell10 = new Label(9, column, info.getVatOutAmt() == null ? "" : info.getVatOutAmt().toString(), JXLTool.getContentFormat());
		Label cell11 = new Label(10, column, info.getVerifyData() == null ? "" : info.getVerifyData(), JXLTool.getContentFormat());

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
	 * @Action
	 * 
	 * 票据管理，进项税转出 转出提交处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String rollOutSubmitInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			invoiceSurtaxService.updateVmsInputInvoiceInfoDatastatus(billId, "16");
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
	 * 票据管理，进项税转出 批量转出处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String batchRollOutInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			invoiceSurtaxService.updateVmsInputInvoiceInfoForBatchRollOut(billId); 
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
	 * 票据管理，进项税转出 撤回数据处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String batchRollBackInvoiceInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			invoiceSurtaxService.updateVmsInputInvoiceInfoForRollBack(billId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}
	
	private String billDate;//开票日期
	private String vendorName;//供应商名称
	private String datastatus;//认证结果
	private String billCode;//发票代码
	private String fapiaoType;//票据种类
	private String vatOutAmt;
	private String vatOutProportion;
	private String remark;
	private String billNo;
	
	private String bill_id;//票据ID
	private String[] billId;
	private Map mapVatType;
	private Map mapDatastatus;//发票状态下拉列表Map[查询，展示]
	
	private InputInvoiceInfo inputInvoiceInfo;
	private List lstInputInvoiceItem;
	private List lstAuthInstId = new ArrayList();//所属机构下拉列表[查询]
	private String instId;
	private String instName;
	
	
	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String bill_id) {
		this.bill_id = bill_id;
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

	public String getVatOutAmt() {
		return vatOutAmt;
	}

	public void setVatOutAmt(String vatOutAmt) {
		this.vatOutAmt = vatOutAmt;
	}

	public String getVatOutProportion() {
		return vatOutProportion;
	}

	public void setVatOutProportion(String vatOutProportion) {
		this.vatOutProportion = vatOutProportion;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String[] getBillId() {
		return billId;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public void setBillId(String[] billId) {
		this.billId = billId;
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

	/*service 声明*/
	private InvoiceSurtaxService invoiceSurtaxService;

	public InvoiceSurtaxService getInvoiceSurtaxService() {
		return invoiceSurtaxService;
	}

	public void setInvoiceSurtaxService(InvoiceSurtaxService invoiceSurtaxService) {
		this.invoiceSurtaxService = invoiceSurtaxService;
	}
	
}
