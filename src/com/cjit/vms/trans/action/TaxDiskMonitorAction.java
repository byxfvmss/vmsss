package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONObject;

import com.cjit.common.util.JXLTool;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.TaxDiskMonitorInfo;
import com.cjit.vms.trans.service.TaxDiskMonitorService;


/**
 * 参数配置_税控盘监控信息管理Action类
 *
 * @author jobell
 */
public class TaxDiskMonitorAction extends DataDealAction{

	/**
	 * @Action 税控盘监控信息管理 查询页面
	 * 
	 * @author jobell
	 * @return
	 */
	
	public String listTaxDiskMonitor(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			
			InstInfo in = new InstInfo();
			in.setInstId(this.getInstId());
			in.setUserId(this.getCurrentUser().getId());
			in.setTanNo(this.getTaxPerNumber());
			in.setLstAuthInstIds(lstAuthInstId);
			List insts = taxDiskMonitorService.getInstInfoList(in, paginationList);
			this.setAuthInstList(insts);
			
			List instTaxno = taxDiskMonitorService.getInstInfoTaxNoList(in,paginationList);
			this.setTaxperLists(instTaxno);
			
			TaxDiskMonitorInfo info = new TaxDiskMonitorInfo();
			info.setInstId(instId);;
			info.setLstAuthInstId(lstAuthInstId);
			info.setTaxPerNumber(taxPerNumber);
			taxDiskMonitorService.findTaxDiskMonitorListInfo(info, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskMonitorAction-listTaxDiskMonitor", e);
		}
		return ERROR;
	}
	
	public void findInfoTaxNo()
            throws ServletException, IOException {
		
		try {
			
			InstInfo in = new InstInfo();
			in.setInstId(request.getParameter("authInst"));
			in.setUserId(this.getCurrentUser().getId());
			List instTaxno = taxDiskMonitorService.getInstInfoTaxNoList(in,paginationList);
			String result = "";
			for(int i=0;i<instTaxno.size();i++){
				InstInfo inst = (InstInfo) instTaxno.get(i);
				if("".equals(result)){
					result += inst.getTanNo();
				}else{
					result += ","+inst.getTanNo();
				}
			}
			response.setHeader("Content-Type","text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Action 税控盘监控信息管理 添加初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public String initAddTaxDiskMonitor(){
		
		mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
		return SUCCESS; 
	}
	
	/**
	 * @Action 税控盘监控信息管理 添加保存处理
	 * 
	 * @author jobell
	 * @return
	 */
	public  String saveTaxDiskMonitor(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			taxDiskMonitorService.saveTaxDiskMonitor(taxDiskMonitorInfo);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskMonitorAction-listTaxDiskMonitor", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 税控盘监控信息管理 编辑初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public String editTaxDiskMonitor(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			taxDiskMonitorInfo=taxDiskMonitorService.findTaxDiskMonitorItemInfo(taxDiskNo, fapiaoType);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskMonitorAction-listTaxDiskMonitor", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 添加验证数据存在
	 * 
	 * @author jobell
	 * @return
	 */
	public void checkTaxDiskMonitor(){
		JSONObject jsonObject=new JSONObject();
		try{
			TaxDiskMonitorInfo info=taxDiskMonitorService.findTaxDiskMonitorItemInfo(taxDiskMonitorInfo.getTaxDiskNo(), taxDiskMonitorInfo.getFapiaoType());
			if(info!=null){
				jsonObject.put("msg","ng");
			}else{
				jsonObject.put("msg","ok");	
			}
		}catch(Exception e){
			jsonObject.put("msg","ng");
		}
		
		try {
			response.getWriter().write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * @Action 税控盘监控信息管理 编辑更新处理
	 * 
	 * @author jobell
	 * @return
	 */
	public  String updateTaxDiskMonitor(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			taxDiskMonitorService.updateTaxDiskMonitor(taxDiskMonitorInfo);
			System.out.println(taxDiskMonitorInfo.getTaxDiskNo()+"-"+taxDiskMonitorInfo.getFapiaoType());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskMonitorAction-listTaxDiskMonitor", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 税控盘监控信息管理 删除处理
	 * 
	 * @author jobell
	 * @return
	 */
	public  String deleteTaxDiskMonitor(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			taxDiskMonitorService.deleteTaxDiskMonitor(tax_no_type_id);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskMonitorAction-listTaxDiskMonitor", e);
		}
		return ERROR;
	}
	
	/*传递参数*/
	private String instId;
	private String taxPerNumber;
	private List lstAuthInstId = new ArrayList();
	private String tax_no_type_id[];
	private TaxDiskMonitorInfo taxDiskMonitorInfo;
	private Map mapVatType;
	private String  taxDiskNo;//税控盘号
	private String  fapiaoType;//发票类型
	private List authInstList;
	
	public List getAuthInstList() {
		return authInstList;
	}

	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}

	public String getTaxPerNumber() {
		return taxPerNumber;
	}
	public void setTaxPerNumber(String taxPerNumber) {
		this.taxPerNumber = taxPerNumber;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String[] getTax_no_type_id() {
		return tax_no_type_id;
	}
	public void setTax_no_type_id(String[] tax_no_type_id) {
		this.tax_no_type_id = tax_no_type_id;
	}
	public TaxDiskMonitorInfo getTaxDiskMonitorInfo() {
		return taxDiskMonitorInfo;
	}
	public void setTaxDiskMonitorInfo(TaxDiskMonitorInfo taxDiskMonitorInfo) {
		this.taxDiskMonitorInfo = taxDiskMonitorInfo;
	}
	public Map getMapVatType() {
		return mapVatType;
	}
	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	/*Service 注入声明*/
	private TaxDiskMonitorService taxDiskMonitorService;
	public TaxDiskMonitorService getTaxDiskMonitorService() {
		return taxDiskMonitorService;
	}
	public void setTaxDiskMonitorService(TaxDiskMonitorService taxDiskMonitorService) {
		this.taxDiskMonitorService = taxDiskMonitorService;
	}

	
	/**
	 * 税控盘监控信息列表画面  导出
	 * 
	 * @return
	 * @author lee
	 */
	public void expTaxDiskMonitor() throws Exception{
		try{
			StringBuffer fileName = new StringBuffer("税控盘监控信息");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os);
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
	 * 税控盘监控信息列表画面   导出
	 * 
	 * @author lee
	 * @return
	 */
	private void writeToExcel(OutputStream os) throws IOException,
			RowsExceededException, WriteException, Exception {

		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("税控盘监控信息", 0);
		JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(1, 0, "税控盘号", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(2, 0, "发票类型", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(3, 0, "开票截止日期", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(4, 0, "数据报送起始日期", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(5, 0, "数据报送终止日期", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(6, 0, "单张发票开票金额限额(元)", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(7, 0, "正数发票累计金额限额(元)", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(8, 0, "负数发票累计金额限额(元)", JXLTool.getHeaderC(excelInfo));
		Label header10 = new Label(9, 0, "负数发票标志", JXLTool.getHeaderC(excelInfo));
		Label header11 = new Label(10, 0, "负数发票天数", JXLTool.getHeaderC(excelInfo));
		Label header12 = new Label(11, 0, "最新报税日期", JXLTool.getHeaderC(excelInfo));
		Label header13 = new Label(12, 0, "剩余容量", JXLTool.getHeaderC(excelInfo));
		Label header14 = new Label(13, 0, "上传截止日期", JXLTool.getHeaderC(excelInfo));
		Label header15 = new Label(14, 0, "限定功能标识", JXLTool.getHeaderC(excelInfo));
		Label header16 = new Label(15, 0, "离线开票时长", JXLTool.getHeaderC(excelInfo));
		Label header17 = new Label(16, 0, "离线开票张数", JXLTool.getHeaderC(excelInfo));
		Label header18 = new Label(17, 0, "离线正数累计金额", JXLTool.getHeaderC(excelInfo));
		Label header19 = new Label(18, 0, "离线负数累计金额", JXLTool.getHeaderC(excelInfo));
		Label header20 = new Label(19, 0, "离线扩展信息", JXLTool.getHeaderC(excelInfo));
		
		ws.addCell(header1);
		ws.setColumnView(1, 20);
		ws.addCell(header2);
		ws.setColumnView(2, 15);
		ws.addCell(header3);
		ws.setColumnView(3, 15);
		ws.addCell(header4);
		ws.setColumnView(4, 15);
		ws.addCell(header5);
		ws.setColumnView(5, 20);
		ws.addCell(header6);
		ws.setColumnView(6, 20);
		ws.addCell(header7);
		ws.setColumnView(7, 20);
		ws.addCell(header8);
		ws.setColumnView(8, 20);
		ws.addCell(header9);
		ws.setColumnView(9, 20);
		ws.addCell(header10);
		ws.setColumnView(10, 20);
		ws.addCell(header11);
		ws.setColumnView(11, 20);
		ws.addCell(header12);
		ws.setColumnView(12, 20);
		ws.addCell(header13);
		ws.setColumnView(13, 20);
		ws.addCell(header14);
		ws.setColumnView(14, 20);
		ws.addCell(header15);
		ws.setColumnView(15, 20);
		ws.addCell(header16);
		ws.setColumnView(16, 20);
		ws.addCell(header17);
		ws.setColumnView(17, 20);
		ws.addCell(header18);
		ws.setColumnView(18, 20);
		ws.addCell(header19);
		ws.setColumnView(19, 20);
		ws.addCell(header20);
		ws.setColumnView(20, 20);
		
		this.getAuthInstList(lstAuthInstId);
		TaxDiskMonitorInfo info = new TaxDiskMonitorInfo();
		info.setInstId(instId);;
		info.setTaxPerNumber(taxPerNumber);
		info.setLstAuthInstId(lstAuthInstId);
		List taxDiskMonitorList = taxDiskMonitorService.findTaxDiskMonitorListInfo(info, paginationList);
		
		int count = 1;
		for (int i = 0; i < taxDiskMonitorList.size(); i++) {
			TaxDiskMonitorInfo info1 = (TaxDiskMonitorInfo) taxDiskMonitorList.get(i);
				int column = count++;
				setWritableSheet(ws, info1, column);
		}
		wb.write();
		wb.close();
	}
	/**
	 * @Action
	 * 
	 * 税控盘监控信息列表画面   导出
	 * 
	 * @author lee
	 * @return
	 */
	private void setWritableSheet(WritableSheet ws, TaxDiskMonitorInfo info, int column) throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column), JXLTool.getContentFormat());
		// 税控盘号
		Label cell2 = new Label(1, column, info.getTaxDiskNo(),JXLTool.getContentFormat());
		String invoiceType = "";
		if ("0".equals(info.getFapiaoType())){
			invoiceType = "增值税专用发票";
		} else if ("1".equals(info.getFapiaoType())){
			invoiceType = "增值税普通发票";
		}
		Label cell3 = new Label(2, column, invoiceType,JXLTool.getContentFormat());
		
		//开票截止日期
		Label cell4 = new Label(3, column, info.getBillEndDateS(),JXLTool.getContentFormat());
		//数据报送起始日期
		Label cell5 = new Label(4, column, info.getDataRepStrDateS(),JXLTool.getContentFormat());
		//数据报送终止日期
		Label cell6 = new Label(5, column, info.getDataRepEndDateS(),JXLTool.getContentFormat());
		//单张发票开票金额限额(元)
		Label cell7 = new Label(6, column, info.getBillLimitAmtS()+"",JXLTool.getContentFormat());
		//正数发票累计金额限额(元)
		Label cell8 = new Label(7, column, info.getBillLimitAmtPS()+"",JXLTool.getContentFormat());
		//负数发票累计金额限额(元)
		Label cell9 = new Label(8, column, info.getBillLimitAmtNS()+"",JXLTool.getContentFormat());
		//负数发票标志
		String nBillFlgS = "";
		if ("1".equals(info.getnBillFlgS())){
			nBillFlgS = "在盘内";
		} else if ("2".equals(info.getnBillFlgS())){
			nBillFlgS = "不在盘内";
		}
		//负数发票天数
		Label cell10 = new Label(9, column, nBillFlgS,JXLTool.getContentFormat());
		//负数发票天数
		Label cell11 = new Label(10, column, info.getnBilDayS(),JXLTool.getContentFormat());
		//最新报税日期
		Label cell12 = new Label(11, column, info.getNewReportDateS(),JXLTool.getContentFormat());
		//剩余容量
		Label cell13 = new Label(12, column, info.getResidualCapacityS()+"",JXLTool.getContentFormat());
		//上传截止日期
		Label cell14 = new Label(13, column, info.getUploadDeadlineS(),JXLTool.getContentFormat());
		//限定功能标识
		Label cell15 = new Label(14, column, info.getLimitFunctionS(),JXLTool.getContentFormat());
		//离线开票时长
		Label cell16 = new Label(15, column, info.getOffLineDayS()+"",JXLTool.getContentFormat());
		//离线开票张数
		Label cell17 = new Label(16, column, info.getOffLineBillS()+"",JXLTool.getContentFormat());
		//离线正数累计金额
		Label cell18 = new Label(17, column, info.getOffLineAmtPS()+"",JXLTool.getContentFormat());
		//离线负数累计金额
		Label cell19 = new Label(18, column, info.getOffLineAmtNS()+"",JXLTool.getContentFormat());
		//离线扩展信息
		Label cell20 = new Label(19, column, info.getOffLineOtsS(),JXLTool.getContentFormat());
		
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
}
