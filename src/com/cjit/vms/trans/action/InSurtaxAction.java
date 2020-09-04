package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.InSurtaxListInfo;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.VmsSurtaxInfo;
import com.cjit.vms.trans.service.InSurtaxService;

/**
 * 进项附加税Action类
 * 
 * @author jobell
 */
public class InSurtaxAction extends DataDealAction{

	/**
	 * 进项附加税查询页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String listInSurtax(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		 
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			if(applyPeriod == null){
				String perMonth=DateUtils.getPreMonth();
				this.applyPeriod=StringUtils.substring(perMonth, 0, 4)+"-"+StringUtils.substring(perMonth, 4);
			}
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			InSurtaxListInfo info= new InSurtaxListInfo();
			info.setLstAuthInstId(lstAuthInstId);
			info.setTaxPerNumber(taxPerNumber);
			info.setSurtaxType(surtaxType);
			info.setSurtaxRate(surtaxRate);
			info.setApplyPeriod(applyPeriod);
			info.setTaxPerName(taxperName);
			inSurtaxService.findInSurtaxList(info, paginationList);
			mapSurtaxAmtType=this.vmsCommonService.findCodeDictionary("SURTAX_AMT_TYPE");
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
	 * 进项附加税一览中，帐票excel出力
	 * 
	 * @author jobell
	 * @return
	 */
	public void inSurtaxExcel() throws Exception{
		try{
			StringBuffer fileName = new StringBuffer("进项附加税统计");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();

//			if(StringUtils.isEmpty(applyPeriod)){
//				String perMonth=DateUtils.getPreMonth();
//				this.applyPeriod=StringUtils.substring(perMonth, 0, 4)+"-"+StringUtils.substring(perMonth, 4);
//			}
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			InSurtaxListInfo info= new InSurtaxListInfo();
			info.setLstAuthInstId(lstAuthInstId);
			info.setTaxPerNumber(taxPerNumber);
			info.setSurtaxType(surtaxType);
			info.setSurtaxRate(surtaxRate);
			info.setApplyPeriod(applyPeriod);
			List lstInSurtaxInfoList=inSurtaxService.findInSurtaxList(info, null);
			
			WritableWorkbook wb = Workbook.createWorkbook(os);
			writeToExcel1(os,lstInSurtaxInfoList, wb);
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
	 * 附加税税种维护 列表画面初期化/检索
	 * 
	 * @return
	 * @author lee
	 */
	public String listSurtaxType(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		VmsSurtaxInfo info = new VmsSurtaxInfo();
		// 纳税人识别号
		info.setTaxpayerId(getTaxPerNumber());
		// 附加税类型
		info.setSurtaxType(getSurtaxType());
		// 附加税名称
		info.setSurtaxName(getSurtaxName());
		// 附加税税率
		info.setSurtaxRate(getSurtaxRate());
		// 附加税起始日期
		info.setSurtaxStrDt(getSurtaxStrDt());
		// 附加税终止日期
		info.setSurtaxEndDt(getSurtaxEndDt());
		// 纳税人名称
		info.setTaxperName(getTaxperName());
		//用户ID
		info.setUser_id(this.getCurrentUser().getId());
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		info.setLstAuthInstId(lstAuthInstId);
		// 一览数据检索
		List paperInfoList = inSurtaxService.getListSurtaxTypeInfo(info, paginationList);
		this.request.setAttribute("paperInfoList", paperInfoList);
		
		return SUCCESS;
	}

	
	
	/**
	 * 附加税税种维护 新增/修改画面初期化
	 * 
	 * @return
	 * @author lee
	 */
	public String surtaxTypeAddOrUpdInit(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try{
			List InstId=new ArrayList();
			this.getAuthInstList(InstId);
			this.setAuthInstList(InstId);
//			this.setOperType("add");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InSurtaxAction-surtaxTypeAddInit", e);
			return ERROR;
		}
		
		// 修改的情况
		if ("1".equals(getUpdFlg())){
			VmsSurtaxInfo info = new VmsSurtaxInfo();
			// 纳税人识别号
			info.setTaxpayerId(getTaxPerNumber());
			// 附加税类型
			info.setSurtaxType(getSurtaxType());
			
			// 一览数据检索
			List paperInfoList = inSurtaxService.getVmsSurtaxInfo(info);
			if (paperInfoList != null && paperInfoList.size() > 0){
				VmsSurtaxInfo vm = (VmsSurtaxInfo)paperInfoList.get(0);
				// 纳税人名称
				setTaxperName(vm.getTaxperName());
				// 附加税税率
				setSurtaxRate(vm.getSurtaxRate());
				// 附加税起始日期
				setSurtaxStrDt(vm.getSurtaxStrDt());
				// 附加税终止日期
				setSurtaxEndDt(vm.getSurtaxEndDt());
			}
		}
		
		
		
		return SUCCESS;
	}
	
	/**
	 * 附加税税种维护 新增/修改画面 保存/更新
	 * @return
	 * @author lee
	 */
	public String addOrUpdSurtaxTypeInfo(){
		VmsSurtaxInfo info = new VmsSurtaxInfo();
		// 纳税人识别号
		info.setTaxpayerId(getTaxPerNumber());
		// 附加税类型
		info.setSurtaxType(getSurtaxType());
		
		// 1-城市建设维护税
		// 2-教育费附加税
		// 3-地方教育费附加税
		// 4-其他地方附加税
		if ("1".equals(getSurtaxType())){	
			// 附加税名称
			info.setSurtaxName("城市建设维护税");
		}else if ("2".equals(getSurtaxType())){	
			// 附加税名称
			info.setSurtaxName("教育费附加税");
		}else if ("3".equals(getSurtaxType())){	
			// 附加税名称
			info.setSurtaxName("地方教育费附加税");
		}else if ("4".equals(getSurtaxType())){	
			// 附加税名称
			info.setSurtaxName("其他地方附加税");
		}
	
		// 附加税税率
		info.setSurtaxRate(getSurtaxRate());
		// 附加税起始日期
		info.setSurtaxStrDt(getSurtaxStrDt());
		// 附加税终止日期
		info.setSurtaxEndDt(getSurtaxEndDt());
		
		inSurtaxService.addOrUpdSurtaxTypeInfo(getUpdFlg(), info);
		
		return SUCCESS;
	}
	
	/**
	 * @Action
	 * 
	 * 附加税税种维护 新增/修改画面  存在性check
	 * 
	 * @author lee
	 * @return
	 */
	public void chkSurtaxTypeInfo()throws IOException{
		int cnt = 0;
		VmsSurtaxInfo info = new VmsSurtaxInfo();
		// 纳税人识别号
		info.setTaxpayerId(getTaxPerNumber());
		// 附加税类型
		info.setSurtaxType(getSurtaxType());

		// 纸质发票使用明细件数的取得
		List lst = inSurtaxService.getVmsSurtaxInfo(info);
		if (lst != null && lst.size() > 0){
			cnt = lst.size();
		}
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(String.valueOf(cnt));
		out.close();
	}
	
	/**
	 * 附加税税种维护 列表画面 删除
	 * 
	 * @return
	 * @author lee
	 */
	public String delSurtaxTypeInfo(){
		if (this.selectedIds != null && !"".equals(this.selectedIds)){
			String[] ids = this.selectedIds.split(",");
			for (int i = 0; i <= ids.length - 1; i ++){
				String[] ms = ids[i].split(":");
				inSurtaxService.delSurtaxTypeInfo(ms[0], ms[1]);
			}
		}
		return SUCCESS;
	}
	
	
	//页面值传递变量声明
	private String taxPerNumber;//纳税人识别号
	private String taxperName; // 纳税人名称
	private String surtaxType;//附加税税种
	private String surtaxRate;//附加税税率
	private String surtaxName; // 附加税名称
	private String surtaxStrDt; // 附加税起始日期
	private String surtaxEndDt; // 附加税终止日期
	private String applyPeriod;//申报周期
	private String updFlg; // 新增修改flag。                  0：新增，1：修改
	private String selectedIds;
	private Map mapSurtaxAmtType;
	public String getTaxPerNumber() {
		return taxPerNumber;
	}
	public void setTaxPerNumber(String taxPerNumber) {
		this.taxPerNumber = taxPerNumber;
	}
	public String getTaxperName() {
		return taxperName;
	}
	public void setTaxperName(String taxperName) {
		this.taxperName = taxperName;
	}
	public String getSurtaxType() {
		return surtaxType;
	}
	public void setSurtaxType(String surtaxType) {
		this.surtaxType = surtaxType;
	}
	public String getSurtaxRate() {
		return surtaxRate;
	}
	public void setSurtaxRate(String surtaxRate) {
		this.surtaxRate = surtaxRate;
	}
	public String getApplyPeriod() {
		return applyPeriod;
	}
	public void setApplyPeriod(String applyPeriod) {
		this.applyPeriod = applyPeriod;
	}
	public String getUpdFlg() {
		return updFlg;
	}
	public void setUpdFlg(String updFlg) {
		this.updFlg = updFlg;
	}
	public String getSurtaxName() {
		return surtaxName;
	}

	public void setSurtaxName(String surtaxName) {
		this.surtaxName = surtaxName;
	}

	public String getSurtaxStrDt() {
		return surtaxStrDt;
	}

	public void setSurtaxStrDt(String surtaxStrDt) {
		this.surtaxStrDt = surtaxStrDt;
	}

	public String getSurtaxEndDt() {
		return surtaxEndDt;
	}

	public void setSurtaxEndDt(String surtaxEndDt) {
		this.surtaxEndDt = surtaxEndDt;
	}
	public String getSelectedIds() {
		return selectedIds;
	}
	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}
	public Map getMapSurtaxAmtType() {
		return mapSurtaxAmtType;
	}

	public void setMapSurtaxAmtType(Map mapSurtaxAmtType) {
		this.mapSurtaxAmtType = mapSurtaxAmtType;
	}


	/*service 声明*/
	private InSurtaxService inSurtaxService;
	public InSurtaxService getInSurtaxService() {
		return inSurtaxService;
	}
	public void setInSurtaxService(InSurtaxService inSurtaxService) {
		this.inSurtaxService = inSurtaxService;
	}
	
	
	/**
	 * @Action
	 * 
	 * 销项附加税sheet1的作成
	 * 
	 * @return
	 */
	private void writeToExcel1(OutputStream os, List lstInSurtaxInfoList,WritableWorkbook wb) throws IOException,
			RowsExceededException, WriteException, Exception {

		WritableSheet ws = null;
		ws = wb.createSheet("进项附加税", 0);
		/*WritableCellFormat wc = new WritableCellFormat();
        // 设置单元格的背景颜色
        wc.setBackground(jxl.format.Colour.YELLOW);*/
        JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header0 = new Label(0, 0, "序号", JXLTool.getHeaderC(excelInfo));
		Label header1 = new Label(1, 0, "纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(2, 0, "纳税人名称", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(3, 0, "附加税类型", JXLTool.getHeaderC(excelInfo));
//		Label header4 = new Label(4, 0, "附加税名称", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(4, 0, "附加税税率", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(5, 0, "进项税", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(6, 0, "汇总附加税", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(7, 0, "明细附加税", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(8, 0, "附加税差异", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(9, 0, "机构名称", JXLTool.getHeaderC(excelInfo));
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
//		ws.addCell(header10);
//		ws.setColumnView(10, 10);
//		List lstAuthInstId=new ArrayList();
//		this.getAuthInstList(lstAuthInstId);
//		OutSurtaxListInfo info= new OutSurtaxListInfo();
//		info.setLstAuthInstId(lstAuthInstId);
//		String applyPeriod = request.getParameter("applyPeriod");
//		if(StringUtils.isEmpty(applyPeriod)){
//			String perMonth=DateUtils.getPreMonth();
//			this.applyPeriod=StringUtils.substring(perMonth, 0, 4)+"-"+StringUtils.substring(perMonth, 4);
//		}
//		
//		info.setTaxPerName(request.getParameter("taxperName"));
//		info.setSurtaxType(request.getParameter("surtaxType"));
//		info.setSurtaxRate(request.getParameter("surtaxRate"));
//		info.setApplyPeriod(request.getParameter("applyPeriod"));
		// 一览数据检索
		//List outSurtaxList = outSurtaxService.findOutSurtaxList(info, null);
		
//		int count = 1;
		for(int i=0;i<lstInSurtaxInfoList.size();i++){
//		for (int i = 0; i < 10; i++) {
//			InSurtaxListInfo inInfo = new InSurtaxListInfo();
			InSurtaxListInfo inInfo=(InSurtaxListInfo) lstInSurtaxInfoList.get(i);
//			inInfo.setTaxPerNumber(i+"");
//			inInfo.setTaxPerName(i+"");
//			inInfo.setSurtaxType("1");
//			inInfo.setSurtaxName(i+"");
//			inInfo.setSurtaxRate(i+"");
//			inInfo.setTaxAmtCny(i+"");
//			inInfo.setGatherSurtax(i+"");
//			inInfo.setSurtax1AmtCny(i+"");
//			inInfo.setSurtax2AmtCny(i+"");
//			inInfo.setSurtax3AmtCny(i+"");
//			inInfo.setSurtax4AmtCny(i+"");
//			inInfo.setDiffSurtax(i+"");
//			inInfo.setInstCode(i+"");
//			int column = count++;
			setWritableSheet1(ws, inInfo, i+1);
		}
	}
	/**
	 * @Action
	 * 
	 * 销项附加税详细列表数据
	 * 
	 * @return
	 */
	private void setWritableSheet1(WritableSheet ws, InSurtaxListInfo info, int column) throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column), JXLTool.getContentFormat());
		// 纳税人识别号
		Label cell2 = new Label(1, column, info.getTaxPerNumber(), JXLTool.getContentFormat());
		// 纳税人名称
		Label cell3 = new Label(2, column, info.getTaxPerName(), JXLTool.getContentFormat());
		// 附加税类型
		Label cell4 = new Label(3, column, info.getSurtaxName(), JXLTool.getContentFormat());
//		// 附加税名称
//		Label cell5 = new Label(4, column, info.getSurtaxName(), JXLTool.getContentFormat());
		// 附加税税率
		Label cell5 = new Label(4, column, info.getSurtaxRate(), JXLTool.getContentFormat());
		// 销项税
		Label cell6 = new Label(5, column, info.getTaxAmtCny(), JXLTool.getContentFormat());
		// 汇总附加税
		Label cell7 = new Label(6, column, info.getGatherSurtax(), JXLTool.getContentFormat());
		// 明细附加税
		Label cell8 = new Label(7, column, info.getSurtaxAmt(), JXLTool.getContentFormat());
//		Label cell9 = null;
//		if(info.getSurtaxType()=="1"){
//			cell9 = new Label(8, column, info.getSurtax1AmtCny(), JXLTool.getContentFormat());
//		}else if(info.getSurtaxType()=="2"){
//			cell9 = new Label(8, column, info.getSurtax2AmtCny(), JXLTool.getContentFormat());
//		}else if(info.getSurtaxType()=="3"){
//			cell9 = new Label(8, column, info.getSurtax3AmtCny(), JXLTool.getContentFormat());
//		}else if(info.getSurtaxType()=="4"){
//			cell9 = new Label(8, column, info.getSurtax4AmtCny(), JXLTool.getContentFormat());
//		}
		// 附加税差异
		Label cell9 = new Label(8, column, info.getDiffSurtax(), JXLTool.getContentFormat());
		// 机构号
		Label cell10 = new Label(9, column, info.getInstName(), JXLTool.getContentFormat());

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
	}
}
