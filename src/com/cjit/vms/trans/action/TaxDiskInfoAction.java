package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.service.DiskRegInfoService;
import com.cjit.vms.trans.service.TaxDiskInfoService;
import com.cjit.vms.trans.util.JXLTool;


/**
 * 税控盘基本信息管理Action类
 * 
 */
public class TaxDiskInfoAction extends DataDealAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List instTaxno=new ArrayList();
	private List authInstList;
	
	public List getAuthInstList() {
		return authInstList;
	}

	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}

	public String taxDiskInfoList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			InstInfo in = new InstInfo();
			in.setInstId(this.getInstId());
			in.setUserId(this.getCurrentUser().getId());
			in.setTanNo(this.getTaxpayerNo());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			in.setLstAuthInstIds(lstAuthInstId);
			List insts = taxDiskInfoService.getInstInfoList(in);
			this.setAuthInstList(insts);
			
			instTaxno = taxDiskInfoService.getInstInfoTaxNoList(in,paginationList);
			this.setTaxperLists(instTaxno);
			
			
			TaxDiskInfo info= new TaxDiskInfo();
			info.setLstAuthInstId(lstAuthInstId);
			info.setUser_id(this.getCurrentUser().getId());
			info.setTaxpayerNo(this.getTaxpayerNo());
			info.setInstId(this.getInstId());
			taxDiskInfoService.getTaxDiskInfoList(info, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskInfoAction-taxDiskInfoList", e);
		}
		return ERROR;
	}
	
	public void findInfoTaxNo()
            throws ServletException, IOException {
		
		try {
			
			
			
			InstInfo in = new InstInfo();
			in.setInstId(request.getParameter("authInst"));
			in.setUserId(this.getCurrentUser().getId());
			
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			in.setLstAuthInstIds(lstAuthInstId);
			instTaxno = taxDiskInfoService.getInstInfoTaxNoList(in,paginationList);
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
	 * <p>
	 * 方法名称: addTaxDiskInfo|描述:税控盘基本信息添加
	 * </p>
	 */
	public String addTaxDiskInfo() {
		try{
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			this.setOperType("add");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskInfoAction-addTaxDiskInfo", e);
			return ERROR;
		}
		return SUCCESS;
	}
	/**
	 * <p>
	 * 方法名称: editTaxDiskInfo|描述:税控盘基本信息编辑
	 * </p>
	 */
	public String editTaxDiskInfo() {
		try{
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			String taxDiskNo = request.getParameter("taxDiskNo");
			String taxpayerNo = request.getParameter("taxpayerNo");
			taxDiskInfo = taxDiskInfoService.getTaxDiskInfoDetail(taxDiskNo,taxpayerNo);
			this.setTaxpayerNo(taxDiskInfo.getTaxpayerNo());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskInfoAction-editTaxDiskInfo", e);
			return ERROR;
		}
		this.setOperType("edit");
		return SUCCESS;
	}
	/**
	 * <p>
	 * 方法名称: saveTaxDiskInfo|描述:税控盘基本信息入库
	 * </p>
	 */
	public String saveTaxDiskInfo() {
		try{
			String operType = request.getParameter("operType");
			TaxDiskInfo info=new TaxDiskInfo();
			info.setTaxDiskNo(request.getParameter("taxDiskNo"));
			info.setBillMachineNo(request.getParameter("billMachineNo"));
			info.setTaxDiskVersion(request.getParameter("taxDiskVersion"));
			info.setTaxDiskDate(request.getParameter("taxDiskDate"));
			info.setTaxpayerNo(request.getParameter("taxpayerNo"));
			info.setTaxpayerNam(request.getParameter("taxpayerNam"));
			info.setTaxDiskPsw(request.getParameter("taxDiskPsw"));
			info.setTaxCertPsw(request.getParameter("taxCertPsw"));
			info.setTaxBureauNum(request.getParameter("taxBureauNum"));
			info.setTaxBureauNam(request.getParameter("taxBureauNam"));
			info.setDiskBillType(request.getParameter("diskBillType"));
			info.setDiskCustType(request.getParameter("diskCustType"));
			info.setRetainInfo(request.getParameter("retainInfo"));
			info.setEnableDt(request.getParameter("enableDt"));
			
			int result=taxDiskInfoService.saveTaxDiskInfo(operType,info);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskInfoAction-saveTaxDiskInfo", e);
			return ERROR;
		}
		return SUCCESS;
	}
	/**
	 * <p>
	 * 方法名称: CountTaxDiskInfo|描述:检查税票盘相同的是否存在
	 * </p>
	 */
	public void CountTaxDiskInfo() {
		String taxDiskNo = request.getParameter("taxDiskNo");
		String taxpayerNo = request.getParameter("taxpayerNo");
		Long findNum = taxDiskInfoService.CountTaxDiskInfo(taxDiskNo,taxpayerNo);
		try {
			response.getWriter().write(findNum+"");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("TaxDiskInfoAction-CountTaxDiskInfo", e);
		} 
	}
	/**
	 * @Action 删除税控盘基本信息管理 删除
	 * 
	 */
	public  String deleteTaxDiskInfo(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			taxDiskInfoService.deleteTaxDiskInfo(selectTaxDisks);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskMonitorAction-listTaxDiskMonitor", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 导出税控盘注册信息
	 * 
	 */
	public  void exportTaxDiskInfo(){
		try {
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			TaxDiskInfo info= new TaxDiskInfo();
			info.setInstId(this.getInstId());
			info.setTaxpayerNo(this.getTaxpayerNo());
			info.setUser_id(this.getCurrentUser().getId());
			info.setLstAuthInstId(lstAuthInstId);
			List taxDiskInfoList=taxDiskInfoService.getTaxDiskInfoList(info);
			
			StringBuffer fileName = new StringBuffer("税控盘基本信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, taxDiskInfoList);
			os.flush();
			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("taxDiskRegInfoAction-exportTaxDiskInfo", e);
		}
	}
	/**
	 * @writeToExcel 写文件
	 * 
	 */
	private void writeToExcel(OutputStream os, List content) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("税控盘基本信息", 0);
		Label header0 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header1 = new Label(i++, 0, "税控盘号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "开票机号", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "税控盘版本号", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "税控盘时钟", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "纳税人识别号", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "纳税人名称", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "税务机关代码", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "税务机关名称", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "发票类型代码", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "企业类型", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "保留信息", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "启用时间", JXLTool.getHeader());
		ws.addCell(header0);
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
		for(int j = 0; j <= 12; j++){
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			TaxDiskInfo taxDiskInfo = (TaxDiskInfo)content.get(c);
			int column = count++;
			setWritableSheet(ws, taxDiskInfo, column);
		}
		wb.write();
		wb.close();
	}
	/**
	 * @setWritableSheet 文件格式定义
	 * 
	 */
	private void setWritableSheet(WritableSheet ws, TaxDiskInfo taxDiskInfo,
			int column) throws Exception {
		int i = 0;
		Label cell0 = new Label(i++, column, Integer.toString(column), JXLTool.getContentFormat());
		Label cell1 = new Label(i++, column, taxDiskInfo.getTaxDiskNo(), JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, taxDiskInfo.getBillMachineNo(),JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, taxDiskInfo.getTaxDiskVersion(),JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, taxDiskInfo.getTaxDiskDate(), JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, taxDiskInfo.getTaxpayerNo(),JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, taxDiskInfo.getTaxpayerNam(),JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, taxDiskInfo.getTaxBureauNum(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, taxDiskInfo.getTaxBureauNam(),JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, taxDiskInfo.getDiskBillType(),JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, taxDiskInfo.getDiskCustType(), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, taxDiskInfo.getRetainInfo(),JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, taxDiskInfo.getEnableDt(),JXLTool.getContentFormat());
		ws.addCell(cell0);
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
	private String instId;//机构
	private String taxpayerNo;//纳税人识别号
	/** 操作类型 add,edit*/
	private String operType;
	private String selectTaxDisks[];
	private TaxDiskInfo taxDiskInfo;// 编辑,新增时对应的主体
	/*service 声明*/
	private TaxDiskInfoService taxDiskInfoService;
	public TaxDiskInfoService getTaxDiskInfoService() {
		return taxDiskInfoService;
	}
	public void setTaxDiskInfoService(TaxDiskInfoService taxDiskInfoService) {
		this.taxDiskInfoService = taxDiskInfoService;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public TaxDiskInfo getTaxDiskInfo() {
		return taxDiskInfo;
	}

	public void setTaxDiskInfo(TaxDiskInfo taxDiskInfo) {
		this.taxDiskInfo = taxDiskInfo;
	}

	public String getTaxpayerNo() {
		return taxpayerNo;
	}

	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}

	public String[] getSelectTaxDisks() {
		return selectTaxDisks;
	}

	public void setSelectTaxDisks(String selectTaxDisks[]) {
		this.selectTaxDisks = selectTaxDisks;
	}

	public List getInstTaxno() {
		return instTaxno;
	}

	public void setInstTaxno(List instTaxno) {
		this.instTaxno = instTaxno;
	}
	
}
