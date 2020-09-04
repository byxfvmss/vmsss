package com.cjit.vms.input.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.ExcelUtil;

import com.cjit.common.util.JXLTool;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.vms.input.model.VendorInfo;
import com.cjit.vms.input.service.VendorInfoService;
import com.cjit.vms.system.model.LogEmp;
import com.cjit.vms.system.service.LogEmpService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.util.CheckUtil;
import com.cjit.vms.trans.util.DataUtil;


public class VendorInfoAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private VendorInfoService vendorInfoService;
	private VendorInfo vendorInfo = new VendorInfo();
	private List vendorInfoList;
	private String message;
	private List dataOperationLabelList;
	private LogEmpService logEmpService;

	/**
	 * 数据操作  -新增
	 */
	private static final String dataOperation_label_ch1="1";
	/**
	 * 据操作标志-修改
	 */
	private static final String dataOperation_label_ch2="2";
	/**
	 * 据操作标志 删除
	 */
	private static final String dataOperation_label_ch3="3";
	/**
	 * 0-待审核
	 */
	private static final String data_audit_status_0="0";
	/**
	 *  1-审核通过
	 */
	private static final String data_audit_status_1="1";
	/**
	 * 2-审核不通过
	 */
	private static final String data_audit_status_2="2";
	/**
	 * 查询供应商列表
	 */
	public String listVendor() {
		message = "";
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				vendorInfo = new VendorInfo();
				fromFlag = null;
			}
			if (vendorInfo != null){
				vendorInfo.setVendorId(null);
			}
			vendorInfoList = vendorInfoService.findVendorInfoList(vendorInfo, paginationList);
			
			this.request.setAttribute("vendorInfoList", vendorInfoList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("VendorInfoAction-listVendor", e);
		}
		return ERROR;
	}
	
	/**
	 * 跳转新增供应商页面
	 */
	public String toNewVendor() {
		
		return SUCCESS;
	}
	
	/**
	 * 保存新增供应商
	 * @throws Exception 
	 */
	public void saveNewVendor() throws Exception {
		String vendorTaxNo = request.getParameter("vendorTaxNo");
		String result = "";
		
		try {
			Map map=CheckTaxNo(vendorTaxNo);
			if(map.get("check").toString().equals("true")){
				vendorInfo = new VendorInfo();
				vendorInfo.setVendorId(vendorTaxNo);
				vendorInfo.setVendorCName(request.getParameter("vendorCName"));
				vendorInfo.setVendorEName(request.getParameter("vendorEName"));
				vendorInfo.setVendorTaxNo(vendorTaxNo);
				vendorInfo.setVendorAccount(request.getParameter("vendorAccount"));
				vendorInfo.setVendorCBank(request.getParameter("vendorCBank"));
				vendorInfo.setVendorEBank(request.getParameter("vendorEBank"));
				vendorInfo.setVendorLinkman(request.getParameter("vendorLinkman"));
				vendorInfo.setVendorPhone(request.getParameter("vendorPhone"));
				vendorInfo.setVendorEmail(request.getParameter("vendorEmail"));
				vendorInfo.setVendorAddress(request.getParameter("vendorAddress"));
				vendorInfo.setTaxpayerType(request.getParameter("taxpayerType"));
				vendorInfo.setAddressee(request.getParameter("addressee"));
				vendorInfo.setAddresseePhone(request.getParameter("addresseePhone"));
				vendorInfo.setAddresseeZipcode(request.getParameter("addresseeZipcode"));
				vendorInfo.setAddresseeAddress(request.getParameter("addresseeAddress"));
				vendorInfo.setAddresseeAddressdetail(request.getParameter("addresseeAddressdetail"));
				vendorInfo.setDataAuditStatus(data_audit_status_0);
				vendorInfo.setDataOperationLabel(dataOperation_label_ch1);
				vendorInfoService.saveNewVendor(vendorInfo);
				result="1|新增供应商信息成功，等待后台审核";
			}else{
				result="0|"+map.get("message").toString();
			}
				
		} catch (Exception e) {
			result = "0|新增供应商失败";
			e.printStackTrace();
		}
		
		printWriterResult(result);
	}
	/**
	 * @param taxNo
	 * @return  原理是 纳税人识别号 不可能 同时出现在  供应商信息表 与供应商信息临时表中 
	 * 返回 错误相关信息
	 */
	public Map CheckTaxNo(String taxNo){
		List list= vendorInfoService.findVendorByTaxNo(taxNo);
		Map map=new HashMap();
		String message="";
		String check="";
		if(list.size()>0){
			message="纳税人识别号已存在，请重新输入";
			check="false";
			
		}
		List listvendorTemp=vendorInfoService.findvendorTaxnoTempByTaxNo(taxNo);
		if(listvendorTemp.size()>0){
			message="该纳税人识别号在待审核中 请确认";
			check="false";
		}if(taxNo.length()>18){
			message="纳税人识别号大于18位 请重新输入";
			check="false";

		}
		if(list.size()==0&&listvendorTemp.size()==0 && taxNo.length()<18){
			check="true";
		}
		map.put("check", check);
		map.put("message",message);
		return map;
	}
	
	/**
	 * 跳转新修改应商页面
	 */
	public String toEditVendor() {
		String vendorId = request.getParameter("vendorId");
		vendorInfo = vendorInfoService.findVendorById(vendorId);
		return SUCCESS;
	}
	
	/**
	 * 保存修改供应商
	 * @throws Exception 
	 */
	public void saveEditVendor() throws Exception {
		String result = "";
		Map map=CheckTaxNo(request.getParameter("vendorTaxNo"));
		String taxno=request.getParameter("vendorTaxNo1");
		if(map.get("check").toString().equals("true")||vendorInfo.getVendorTaxNo().equals(taxno)){
		try {
			vendorInfo = new VendorInfo();
			vendorInfo.setVendorId(request.getParameter("vendorId"));
			vendorInfo.setVendorCName(request.getParameter("vendorCName"));
			vendorInfo.setVendorEName(request.getParameter("vendorEName"));
			vendorInfo.setVendorCBank(request.getParameter("vendorCBank"));
			vendorInfo.setVendorEBank(request.getParameter("vendorEBank"));
			vendorInfo.setVendorTaxNo(request.getParameter("vendorTaxNo"));
			vendorInfo.setVendorAccount(request.getParameter("vendorAccount"));
			vendorInfo.setVendorLinkman(request.getParameter("vendorLinkman"));
			vendorInfo.setVendorPhone(request.getParameter("vendorPhone"));
			vendorInfo.setVendorEmail(request.getParameter("vendorEmail"));
			vendorInfo.setVendorAddress(request.getParameter("vendorAddress"));
			vendorInfo.setTaxpayerType(request.getParameter("taxpayerType"));
			vendorInfo.setAddressee(request.getParameter("addressee"));
			vendorInfo.setAddresseePhone(request.getParameter("addresseePhone"));
			vendorInfo.setAddresseeZipcode(request.getParameter("addresseeZipcode"));
			vendorInfo.setAddresseeAddress(request.getParameter("addresseeAddress"));
			vendorInfo.setAddresseeAddressdetail(request.getParameter("addresseeAddressdetail"));
			vendorInfo.setDataOperationLabel(dataOperation_label_ch2);
			vendorInfo.setDataAuditStatus(data_audit_status_0);
			vendorInfoService.saveNewVendor(vendorInfo);
			// vendorInfoService.updateVendorById(vendorInfo);
			
			result = "1|修改供应商信息提交成功，等待后台审核";
			//setResultMessages("");
		} catch (Exception e) {
			result = "0|系统错误修改失败";
			e.printStackTrace();
		}
		}else{
			//setResultMessages(map.get("message").toString());
			result="0|"+map.get("message").toString();
		}
		printWriterResult(result);
	
	}
	
	/**
	 *  查看供应商明细
	 */
	public String viewVendorDetail() {
		String vendorId = request.getParameter("vendorId");
		vendorInfo = vendorInfoService.findVendorById(vendorId);
		return SUCCESS;
	}
	
	/**
	 *  删除供应商
	 * @throws Exception 
	 */
	public void deleteVendor() throws Exception {
		String vendorIds = request.getParameter("vendorIds");
		String[] ids=vendorIds.split(",");
		String result="";
		String ida="";
		String idn="";
		for(int i=0;i<ids.length;i++){
			
			List vendorIdsList=vendorInfoService.findTransbyVendId(ids[i]);
			if(vendorIdsList.size()>0){
				ida+=ids[i]+",";
			}else{
				vendorInfo=new VendorInfo();
				vendorInfo.setVendorId(ids[i]);
				vendorInfo.setDataAuditStatus(data_audit_status_0);
				vendorInfo.setDataOperationLabel(dataOperation_label_ch3);
				vendorInfoService.copyVendorInfoToVendorInfoTemp(vendorInfo);
				idn+=ids[i]+",";
			}
		}
		if(StringUtil.isNotEmpty(ida)){
			result+="供应商id "+ida+"存在交易不能删除;";
		}
		if(StringUtil.isNotEmpty(idn)){
			result+="供应商id"+idn+"删除供应商信息请求成功，等待后台审核";
		}
		//vendorInfoService.deleteVendorById(vendorIds);
		printWriterResult(result);
	}
	
	/**
	 *  导出供应商
	 * @throws Exception 
	 */
	public void exportVendor() throws Exception {
		if (vendorInfo != null){
			vendorInfo.setVendorId(null);
		}
		vendorInfoList = vendorInfoService.findVendorInfoList(vendorInfo, paginationList);
		
		StringBuffer fileName = new StringBuffer("供应商信息表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, vendorInfoList);
		os.flush();
		os.close();
		
	}
	public List setwriteWidth(List list) throws JXLException{
	 List rowlist=null;
	 List sheetList=new ArrayList();
	 VendorInfo vendor=null;
	for(int i=0;i<list.size();i++){
		rowlist=new ArrayList();
		vendor=(VendorInfo) list.get(i);
		rowlist.add(vendor.getVendorCName());
		rowlist.add(vendor.getVendorEName());
		rowlist.add(vendor.getVendorTaxNo());
		rowlist.add(vendor.getVendorAccount());
		rowlist.add(vendor.getVendorCBank());
		rowlist.add(vendor.getVendorEBank());
		rowlist.add(vendor.getVendorPhone());
		rowlist.add(vendor.getVendorEmail());
		rowlist.add(vendor.getVendorAddress());
		rowlist.add(vendor.getVendorLinkman());
		rowlist.add(vendor.getTaxpayerType());
		rowlist.add(vendor.getAddressee());
		rowlist.add(vendor.getAddresseePhone());
		rowlist.add(vendor.getAddresseeAddress());
		rowlist.add(vendor.getAddresseeAddressdetail());
		rowlist.add(vendor.getAddresseeZipcode());
		
		sheetList.add(rowlist);
	}
	
	return sheetList;
}

	
	private void writeToExcel(OutputStream os, List content) throws IOException, JXLException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("供应商信息", 0);
		Label header1 = new Label(i++, 0, "供应商中文名", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "供应商英文名", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "供应商纳税人识别号", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "供应商账号", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "供应商开户行中文名", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "供应商开户英文名", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "供应商联系人", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "供应商电话", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "供应商邮箱地址", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "供应商地址", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "供应商类别", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "收件人", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "收件人电话", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "收件人邮编", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "收件地址", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "详细地址", JXLTool.getHeader());
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
		JXLTool.setAutoWidth(ws, setwriteWidth(content));

		int count = 1;
		for(int c = 0; c < content.size(); c++){
			VendorInfo vendor = (VendorInfo)content.get(c);
			int column = count++;
			setWritableSheet(ws, vendor, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, VendorInfo vendor,
			int column) throws WriteException {
		int i = 0;
		Label cell1 = new Label(i++, column, vendor.getVendorCName(), JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, vendor.getVendorEName(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, vendor.getVendorTaxNo(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, vendor.getVendorAccount(),JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, vendor.getVendorCBank(),JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, vendor.getVendorEBank(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, vendor.getVendorPhone(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, vendor.getVendorEmail(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, vendor.getVendorAddress(), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, vendor.getVendorLinkman(), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, DataUtil.getTaxpayerTypeCH(vendor.getTaxpayerType()), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, vendor.getAddressee(), JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, vendor.getAddresseePhone(), JXLTool.getContentFormat());
		Label cell14 = new Label(i++, column, vendor.getAddresseeAddress(), JXLTool.getContentFormat());
		Label cell15 = new Label(i++, column, vendor.getAddresseeAddressdetail(), JXLTool.getContentFormat());
		Label cell16 = new Label(i++, column, vendor.getAddresseeZipcode(), JXLTool.getContentFormat());
		
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
		
	}

	/**
	 *  导入供应商
	 */
	public String importVendor() {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("attachmentVendor");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				String result = doImportFile(files[0]);
				files = null;
				vendorInfo = new VendorInfo();
				return result;
			} catch (Exception e) {
				log.error(e);
				message = "上传文件失败:" + e.getMessage();
				return ERROR;
			}
		} else {
			message = "上传文件失败!";
			return ERROR;
		}
	}
	
	private String doImportFile(File file) throws Exception {
		List<Dictionary> headList= userInterfaceConfigService.getDictionarys(
				"VENDOR_INFO_IMP", "");
		String result="";
		Map<String, String> mapBusi=new HashMap<String,String>();
		Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
		if (hs != null) {
			// 获取excel第一个sheet页
			String[][] sheet = (String[][]) hs.get("0");
			// 从第二行开始获取每行数据
			String batchNo=StringUtil.getBatchNo();
			String startDate=StringUtil.getCurrentDate();
			String failedLog="";
			String resultBusi="";
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			Set<String> set=new HashSet<String>();
			Map<String,Boolean> maptaxNo=new HashMap<String, Boolean>();
			String resultTaxNo="";
			for (int i = 1; i < sheet.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				String[] row = sheet[i];
				String[] heads = sheet[0];

				// 值与列对应
				map=CheckUtil.CreatMap(heads, headList, row);
				result=checkformat(map, i, sheet, result);
				set=CheckUtil.checkId(maptaxNo,map.get("vendorTaxNo"), set);
				maptaxNo.put(map.get("vendorTaxNo"), true);
				
				
				dataList.add(map);
				}
			resultTaxNo=CheckUtil.checkId(set, "文件中纳税人识别号");
			result+=resultTaxNo;
			if(result.length()>0){
				message=result;
			}else{
				int j=0;
				for(int i=0;i<dataList.size();i++){
					j=mapBusi.get("j")==null?0:Integer.valueOf(mapBusi.get("j"));
					Map<String,String> map=dataList.get(i);
					map.put("vendorId",map.get("vendorTaxNo"));
					map.put("batchNo", batchNo);
					map.put("dataOperationLabel", dataOperation_label_ch1);
					vendorInfoService.saveVendorData(map);
					mapBusi=checkVendorBusi(map, mapBusi, sheet, i, j);
				}
				resultBusi=mapBusi.get("resultBusi");
				failedLog=mapBusi.get("failedLog");
			

			{	
				LogEmp log=new LogEmp();
				log.setSuccessNo(Integer.toString(j));
				log.setBatchNo(batchNo);
				log.setEndDate(StringUtil.getCurrentDate());
				log.setFailedNo(Integer.toString(sheet.length-j-1));
				log.setId(StringUtil.getUUID());
				log.setFailedLog(failedLog.toString().length()==0?"":"失败行"+failedLog.toString().substring(0,failedLog.toString().length()-1));
				log.setStartDate(startDate);
				log.setSys("供应商信息管理");
				logEmpService.saveLogEmp(log);
			}
			message=resultBusi;
			}
			return SUCCESS;
		} else {
			message = "导入文件为空，请选择导入文件。";
			return ERROR;
		}
	}
	
	/*1	VENDOR_ID	供应商ID	VARCHAR2(100)	N	PK		
	2	VENDOR_CNAME	供应商纳税人中文名称	VARCHAR2(100)	N			
	4	VENDOR_TAXNO	供应商纳税人识别号	VARCHAR2(25)	N			
	5	VENDOR_ACCOUNT	供应商账号	VARCHAR2(50)	N			
	6	VENDOR_CBANK	供应商开户银行中文名称	VARCHAR2(100)	N			


*/
	/**
	 * @param map
	 * @param i
	 * @param sheet
	 * @param result
	 * @return 验证格式
	 */
	private String checkformat(Map<String,String> map,int i,String[][]sheet,String result){
		List<String> list=new ArrayList<String>();
		list.add(map.get("vendorCName"));
		list.add(map.get("vendorTaxNo"));
		list.add(map.get("vendorAccount"));
		list.add(map.get("vendorCBank"));
		Map<String, Boolean> mapCheck=new HashMap<String, Boolean>();
		mapCheck.put("checkNull",CheckUtil.checkNotNull(list));
		result=CheckUtil.checkData(mapCheck, i, result, sheet.length);
		return result;
		
	}
	/* 
	验证不通过 
	vendorCName	vendorEName	vendorTaxNo	vendorAccount	vendorCBank	vendorEBank	vendorLinkman	vendorPhone	vendorEmail	vendorAddress	
	taxpayerType	addressee	addresseePhone	addresseeZipcode	addresseeAddress	addresseeAddressdetail
	*/
	public Map<String,String> checkVendorBusi(Map<String,String> map,Map<String,String> mapcheckBusi,String[][]sheet,int i,int j){
		String nullRow=StringUtil.isNotEmpty(mapcheckBusi.get("nullRow"))?mapcheckBusi.get("nullRow"):"";
		String failedLog=StringUtil.isNotEmpty(mapcheckBusi.get("failedLog"))?mapcheckBusi.get("failedLog"):"";
		String taxNoTempNum=StringUtil.isNotEmpty(mapcheckBusi.get("taxNoTempNum"))?mapcheckBusi.get("taxNoTempNum"):"";
		String taxNoNum=StringUtil.isNotEmpty(mapcheckBusi.get("taxNoNum"))?mapcheckBusi.get("taxNoNum"):"";
		String resultBusi=StringUtil.isNotEmpty(mapcheckBusi.get("resultBusi"))?mapcheckBusi.get("resultBusi"):"";
		String taxNo=StringUtil.isNotEmpty(map.get("vendorTaxNo"))?map.get("vendorTaxNo"):"";
		if(StringUtil.isEmpty(taxNo)&&map.get("taxpayerType").equals("G")){
			nullRow+=i+1+",";
			failedLog+=i+1+",";
			j++;
		}else if(StringUtil.isNotEmpty(taxNo)){
			List list= vendorInfoService.findVendorByTaxNo(taxNo);
			List listtemp=vendorInfoService.findvendorTaxnoTempByTaxNo(taxNo);
			if(taxNo.length()>18){
				taxNoNum+=i+1+",";
				failedLog+=i+1+",";
				j++;
			}
			if(listtemp.size()>0){
				taxNoTempNum+=i+1+",";
				failedLog+=i+1+",";
				j++;
				
			}else if(listtemp.size()==0&&list.size()>0){
				
				vendorInfoService.deleteVendorByTaxNo(taxNo);
				vendorInfoService.saveVendorInfo(map);
				
			}else{
				
				vendorInfoService.saveVendorInfo(map);
			}
		}else{
			nullRow+=i+1+",";
			failedLog+=i+1+",";
			j++;
		}
		nullRow=nullRow.length()>0&&i==sheet.length-2?"第"+nullRow.substring(0,nullRow.length()-1)+"行纳税人识别号为空；":nullRow;
		taxNoTempNum=taxNoTempNum.length()>0&&i==sheet.length-2?"第"+taxNoTempNum.substring(0,taxNoTempNum.length()-1)+"行纳税人识别号审核中；":taxNoTempNum;
		taxNoNum=taxNoNum.length()>0&&i==sheet.length-2?"第"+taxNoNum.substring(0,taxNoNum.length()-1)+"行纳税人识别号长度大于18位；":taxNoNum;
		failedLog=failedLog.length()>0&&i==sheet.length-2?"第"+failedLog.substring(0,failedLog.length()-1)+"行导入失败；":failedLog;
		resultBusi=nullRow+taxNoTempNum+taxNoNum;
		resultBusi=resultBusi.length()==0&&i==sheet.length-2?"导入成功!":resultBusi;
		mapcheckBusi.put("nullRow", nullRow);
		mapcheckBusi.put("taxNoTempNum", taxNoTempNum);
		mapcheckBusi.put("failedLog", failedLog);
		mapcheckBusi.put("resultBusi", resultBusi);
		mapcheckBusi.put("taxNoNum", taxNoNum);
		mapcheckBusi.put("j", Integer.toString(j));
		
		return mapcheckBusi;
	}
	
	
	/**
	 * @return 供应商审核界面
	 */
	public String listVendorAudit(){
		message="";
		if ("menu".equalsIgnoreCase(fromFlag)) {
			vendorInfo = new VendorInfo();
			fromFlag = null;
		}
		vendorInfoList = vendorInfoService.findVendorTempInfoList(vendorInfo, paginationList);
		dataOperationLabelList=DataUtil.getdataOperationLabelList();
		return SUCCESS;
	}
	public void auditVendorInfo() throws Exception{
		String auditsudits=request.getParameter("auditsudits");
		String vendorIdList=request.getParameter("deleteAarray");
		String addAarray=request.getParameter("addAarray");
		String updateAarray=request.getParameter("updateAarray");
		
		List vendorIdAuditList=getList(vendorIdList);
		List addList=getList(addAarray);
		List updateList=getList(updateAarray);
		String result="";
		if(auditsudits.equals("1")){
			if(vendorIdAuditList!=null){
				vendorInfoService.deleteVendorById(vendorIdAuditList);
			}
			if(addList!=null){
				vendorInfoService.copyVendorTempYoVendor(addList);
			}if(updateList!=null){
				vendorInfoService.updatevendorAfterAudit(updateList);
			}
			result="【审核通过】提交成功";
		}else{
			result="【审核不通过】提交成功";
		}
		if(vendorIdAuditList!=null){
			vendorInfoService.updateVendorTempAfterAudit(vendorIdAuditList, auditsudits);
			vendorInfoService.deleteVendorTemp(vendorIdAuditList);
		}
		if(addList!=null){
			vendorInfoService.updateVendorTempAfterAudit(addList, auditsudits);
			vendorInfoService.deleteVendorTemp(addList);
		}if(updateList!=null){
			
			
			vendorInfoService.updateVendorTempAfterAudit(updateList, auditsudits);
			vendorInfoService.deleteVendorTemp(updateList);
		}
		printWriterResult(result);
		
	
	}
	public List getList(String data){
		List list=null;
		if(StringUtil.isNotEmpty(data)){
		String[] infors=data.split(",");
		list=Arrays.asList(infors);
		}
		return list;
	}
	public String auditVendorInfoDetial(){
		String vendorId = request.getParameter("vendorId");
		vendorInfo = vendorInfoService.findvendorTempByVendorId(vendorId);
		return SUCCESS;
		
	}
	public VendorInfoService getVendorInfoService() {
		return vendorInfoService;
	}

	public void setVendorInfoService(VendorInfoService vendorInfoService) {
		this.vendorInfoService = vendorInfoService;
	}

	public VendorInfo getVendorInfo() {
		return vendorInfo;
	}

	public void setVendorInfo(VendorInfo vendorInfo) {
		this.vendorInfo = vendorInfo;
	}

	public List getVendorInfoList() {
		return vendorInfoList;
	}

	public void setVendorInfoList(List vendorInfoList) {
		this.vendorInfoList = vendorInfoList;
	}

	
	public List getDataOperationLabelList() {
		return dataOperationLabelList;
	}

	public void setDataOperationLabelList(List dataOperationLabelList) {
		this.dataOperationLabelList = dataOperationLabelList;
	}
	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LogEmpService getLogEmpService() {
		return logEmpService;
	}

	public void setLogEmpService(LogEmpService logEmpService) {
		this.logEmpService = logEmpService;
	}
	
	
}
