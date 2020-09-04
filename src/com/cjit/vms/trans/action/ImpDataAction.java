package com.cjit.vms.trans.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.cjit.gjsz.system.service.OrganizationService;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.struts2.ServletActionContext;

import cjit.crms.util.ExcelIOUtil;
import cjit.crms.util.ExcelUtil;

import com.cjit.common.util.JXLTool;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.VmsTransInfo;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.service.ImpDataService;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.trans.service.config.TransTypeService;
import com.cjit.vms.trans.util.DataFileParser;
import com.cjit.vms.trans.util.DataFileParserUtil;

public class ImpDataAction extends DataDealAction {
	private String flag;
	private String batchId;
	private TransInfoService transInfoService;
	private CustomerService customerService;
	private ImpDataService impDataService;
	private TransTypeService transTypeService;
	private VmsTransInfo impTransCondition = new VmsTransInfo();// 查询条件
	private File attachment;
	private String attachmentFileName;
	private String batchIds;
	private VmsTransInfo detailInfo = new VmsTransInfo();
	private String impBatchId;
	private String status;
	private String messages;
	private OrganizationService organizationService;
	/**
	 * 列数据处理
	 * @param rowMap
	 * @return
	 */
	public void dealRowMap(Map rowMap){
		//是否手工开票
		String transFapiaoFlag = (String) rowMap.get("transFapiaoFlag");
		if(checkNull(transFapiaoFlag)){
			rowMap.put("transFapiaoFlag", "M");
		}
		//是否冲账
		String isReverse = (String) rowMap.get("isReverse");
		if(checkNull(isReverse)){
			rowMap.put("isReverse", "N");
		}
		String taxFlag = (String) rowMap.get("taxFlag");
		//含税交易
		if(null==taxFlag||"".equals(taxFlag)||"Y".equals(taxFlag)){
			rowMap.put("taxFlag", "Y");
			//补充税率(交易认定)
			String transTypeId = (String) rowMap.get("transType");//交易ID
			String instcode = (String) rowMap.get("instcode");//交易机构
			Organization org = new Organization();
			org.setId(instcode);
			//获取税号
			org = organizationService.getOrganization(org);
			String taxNo= "";
			if(org != null){
				taxNo = org.getTaxperNumber();
			}
			VerificationInfo transType = new VerificationInfo();
			transType.setTransTypeId(transTypeId);
			transType.setTaxNo(taxNo);
			List list = transTypeService.findTaxRateBytransType(transType);
			if(list.size()>0){
				transType = (VerificationInfo)list.get(0);
				String taxRate = transType.getTaxRate();
				rowMap.put("taxRate", taxRate);
				//价税分离
				String transAmt = (String) rowMap.get("transAmt");//价税合计
				BigDecimal transAmtBd = new BigDecimal(transAmt);
				BigDecimal taxRateBd = new BigDecimal(taxRate);
				BigDecimal taxAmtF = transAmtBd.multiply(taxRateBd);
				BigDecimal taxAmtS = taxAmtF.divide((BigDecimal.ONE.add(taxRateBd)),10,BigDecimal.ROUND_HALF_DOWN);
				BigDecimal taxAmtBd = taxAmtF.divide((BigDecimal.ONE.add(taxRateBd)),2,BigDecimal.ROUND_HALF_DOWN);
				BigDecimal shortAndOverBd = taxAmtS.subtract(taxAmtBd);
				BigDecimal incomeBd = transAmtBd.subtract(taxAmtBd);
				String taxAmt = taxAmtBd.toString();//税额
				String shortAndOver = shortAndOverBd.toString();//尾差
				String income = incomeBd.toString();
				rowMap.put("taxAmt", taxAmt);
				rowMap.put("shortAndOver", shortAndOver);
				rowMap.put("income", income);//收入
			}
			//不含税交易
		}else if("N".equals(taxFlag)){
			//补充税率(交易认定)
			String transTypeId = (String) rowMap.get("transType");//交易ID
			String instcode = (String) rowMap.get("instcode");//交易机构
			Organization org = new Organization();
			org.setId(instcode);
			//获取税号
			String taxNo = organizationService.getOrganization(org).getTaxperNumber();
			VerificationInfo transType = new VerificationInfo();
			transType.setTransTypeId(transTypeId);
			transType.setTaxNo(taxNo);
			List list = transTypeService.findTaxRateBytransType(transType);
			if(list.size()>0){
				transType = (VerificationInfo)list.get(0);
				String taxRate = transType.getTaxRate();
				rowMap.put("taxRate", taxRate);
				//价税分离
				String income = (String) rowMap.get("transAmt");//收入
				BigDecimal incomeBd = new BigDecimal(income);
				BigDecimal taxRateBd = new BigDecimal(taxRate);
				BigDecimal taxAmtBd = incomeBd.multiply(taxRateBd);
				BigDecimal shortAndOverBd = BigDecimal.ZERO;
				BigDecimal transAmtBd = incomeBd.add(taxAmtBd);
				String taxAmt = taxAmtBd.toString();//税额
				String shortAndOver = shortAndOverBd.toString();//尾差
				String transAmt = transAmtBd.toString();//价税合计
				rowMap.put("taxAmt", taxAmt);
				rowMap.put("shortAndOver", shortAndOver);
				rowMap.put("income", income);
				rowMap.put("transAmt", transAmt);
			}
		}
	}

	/**
	 * 获取数据列表（导入）页面
	 */
	public String listImpdata() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		List authInsts = new ArrayList();
		this.getAuthInstList(authInsts);
		List statusList = new ArrayList();
		String[] a = { "0", "1", "2", "6" };// 可察看状态表
		statusList.addAll(Arrays.asList(a));
		impTransCondition.setAuthInsts(authInsts);
		impTransCondition.setStatusList(statusList);
		impDataService.getImpDataListInfo(impTransCondition, paginationList);
		return SUCCESS;
	}

	/**
	 * 上传数据
	 */
	public String uploadData(){
		try {
			ExcelIOUtil excelIOUtil = new ExcelIOUtil();
			Map map = excelIOUtil.checkFile(attachment, attachmentFileName);
			boolean flag = (Boolean) map.get("flag");
			File file = (File) map.get("file");
			if (flag) {
				List<Dictionary> headList = userInterfaceConfigService.getDictionarys1("TRANS_INFO_IMP", "", "");
				List<Map<String,String>> dataList = excelIOUtil.doImportFile(file, headList);
				for(int i = 0 ; i < dataList.size(); i++){
					Map rowMap = new HashMap();
					rowMap = dataList.get(i);
					dealRowMap(rowMap);
				}
				//获取批次主健
				String batchId = dataList.get(0).get("impBatchId");
				//获取当前用户
				String impUser = this.getCurrentUser().getUsername();
				//获取当前机构
				String impInst = this.getCurrentUser().getOrgId();
				//获取时间
				String impTime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
				VmsTransInfo batchInfo = new VmsTransInfo();
				batchInfo.setBatchId(batchId);
				batchInfo.setImpUser(impUser);
				batchInfo.setImpInst(impInst);
				batchInfo.setImpTime(impTime);
				batchInfo.setStatus("0");//未校验
				impDataService.saveVmsTransBatchInfo(batchInfo);
				impDataService.saveVmsTransInfoImpDataInfo(dataList);
				return SUCCESS;
			} else {
				this.setResultMessages((String) map.get("resultMessages"));
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			setResultMessages("数据导入异常，请确保模板正确");
			return ERROR;
		}
	}

	/**
	 * 校验数据(修改状态)
	 */
	public String checkData() {
		String[] a = batchIds.split(",");
		List msgList = new ArrayList();
		for(int i = 0; i < a.length; i++){
			String batchId = a[i];
			VmsTransInfo info = new VmsTransInfo();
			info.setBatchId(batchId);
			List impDataList = impDataService.findTransImpDataInfo(info,null);
			//获取明细
			for(int j = 0; j < impDataList.size(); j ++){
				StringBuilder str = new StringBuilder();
				VmsTransInfo resultInfo = (VmsTransInfo) impDataList.get(j);
				String impId = resultInfo.getImpId();
				String transId = resultInfo.getTransId();
				//transId重复性校验
				//表内重复性校验
				VmsTransInfo vmsTransInfo = new VmsTransInfo();
				//获取交易信息
				vmsTransInfo.setTransId(transId);
				List innerMatchList = impDataService.findTransImpDataInfo(vmsTransInfo,null);
				if(innerMatchList.size()> 1){
					str.append("数据重复!,");
					resultInfo.setdStatus("1");
				}else{
					resultInfo.setdStatus("0");
				}
				//表外重复性校验
				TransInfo transInfo = new TransInfo();
				transInfo.setTransId(transId);
				if("0".equals(resultInfo.getdStatus())){
					List outerMatchList = impDataService.findTransInfo(transInfo);
					if(outerMatchList.size()>0){
						resultInfo.setdStatus("1");
					}
				}
				//相关性及非空校验
				vmsTransInfo.setImpId(impId);
				List resultList = impDataService.findImpDataDetailByBatchId(vmsTransInfo);
				VmsTransInfo checkResult = (VmsTransInfo) resultList.get(0);
				//客户号
				String customerId = checkResult.getCustomerId();
				if(checkNull(customerId)){
					str.append("客户号未找到,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}else{
					resultInfo.setdStatus(resultInfo.getdStatus()+"0");
				}
				//交易日期
				String transDate = checkResult.getTransDate();
				String[] dataSplit = transDate.split("-");
				if(null != transDate && !"".equals(transDate) && dataSplit.length == 3 &&
						dataSplit[1].length() == dataSplit[2].length() && transDate.length() == 10){
					resultInfo.setdStatus(resultInfo.getdStatus()+"0");
				}else{
					str.append("交易日期格式不正确或为空,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}
				//交易类型
				String transType = checkResult.getTransType();
				if(checkNull(transType)){
					str.append("交易类型为空或未找到,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}else{
					resultInfo.setdStatus(resultInfo.getdStatus()+"0");
				}
				//交易金额
				String transAmt = checkResult.getTransAmt();
				if(checkNull(transAmt)){
					str.append("交易金额为空,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}else{
					resultInfo.setdStatus(resultInfo.getdStatus()+"0");
				}
				//是否含税
				String taxFlag = checkResult.getTaxFlag();
				if(checkNull(taxFlag)){
					str.append("是否含税为空,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}else if("Y".equals(taxFlag)||"N".equals(taxFlag)){
					resultInfo.setdStatus(resultInfo.getdStatus()+"0");
				}else{
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}
				//税率
				String taxRate = checkResult.getTaxRate();
				if(checkNull(taxRate)){
					str.append("税率为空,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}else{
					resultInfo.setdStatus(resultInfo.getdStatus()+"0");
				}
				//税额
				String taxAmt = checkResult.getTaxAmt();
				if(checkNull(taxAmt)){
					str.append("税额为空,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}else{
					resultInfo.setdStatus(resultInfo.getdStatus()+"0");
				}
				//交易机构
				String bankCode = checkResult.getBankCode();
				if(checkNull(bankCode)){
					str.append("交易机构为空或未找到此机构,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}else{
					resultInfo.setdStatus(resultInfo.getdStatus()+"0");
				}
				//发票类型
				String fapiaoType = checkResult.getFapiaoType();
				if(checkNull(fapiaoType)){
					str.append("发票类型为空,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"1");
				}else{
					resultInfo.setdStatus(resultInfo.getdStatus()+"0");
				}
				//是否冲账+对应冲账流水
				String isReverse = checkResult.getIsReverse();
				String reverseTransId = checkResult.getReverseTransId();
				if(!checkNull(reverseTransId)&&"Y".equals(isReverse)){
					resultInfo.setdStatus(resultInfo.getdStatus()+"00");
				}else if("N".equals(isReverse)&&checkNull(reverseTransId)){
					resultInfo.setdStatus(resultInfo.getdStatus()+"00");
				}else{
					str.append("是否冲账为空,对应冲账流水为空,");
					resultInfo.setdStatus(resultInfo.getdStatus()+"11");
				}
				VmsTransInfo vi = new VmsTransInfo();
				String sTr = str.toString();
				if(resultInfo.getdStatus().substring(0,1) == "1" || "1".equals(resultInfo.getdStatus().substring(0,1))){
					sTr = "数据重复,,";
				}
				vi.setMessage(sTr != null && !"".equals(sTr)  ? sTr.substring(0,sTr.length()-1) : "");
				vi.setImpId(resultInfo.getImpId());
				msgList.add(vi);
			}


			impDataService.updateTransDataByIdWidthMessage(msgList);
			impDataService.savaCheckResult(impDataList);
			//更新校验状态
			for(int j = 0; j < impDataList.size(); j ++){
				VmsTransInfo resultInfo = (VmsTransInfo) impDataList.get(j);
				String checkStatus = resultInfo.getdStatus();
				if(checkStatus.contains("1")){
					impDataService.updateVmsTransBatchStatus("1", info.getBatchId());
				}else{
					impDataService.updateVmsTransBatchStatus("2", info.getBatchId());
				}
			}
		}
		return SUCCESS;
	}

	/**
	 * 删除导入数据
	 *
	 * @return
	 */
	public String removeImpdata() {
		try {
			String batchID = request.getParameter("batchID");

			if (batchID == null) {
				this.setRESULT_MESSAGE("删除失败！");
				return ERROR;
			}
			impDataService.deleteTransInfoImpData(batchID);
			this.setRESULT_MESSAGE("删除成功!");
			return SUCCESS;
		} catch (Exception e) {
			this.setRESULT_MESSAGE("系统异常!");
			return ERROR;
		}
	}

	/**
	 * 查询数据详细信息
	 */
	public String showDataDetail() {
		detailInfo.setBatchId(impBatchId);
		detailInfo.setStatus(status);
		List takeList = impDataService.findTransImpDataInfo(detailInfo, paginationList);
		return SUCCESS;
	}

	/**
	 * 列表画面 导出
	 */
	public void impdataToExcel() throws Exception {
		try {
			String name = "attachment;filename="
					+ URLEncoder.encode("数据导入.xls", "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}


	/**
	 * 提交审核
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String submitTransInfoByState() throws UnsupportedEncodingException {
		try {
			String[] batchIds = this.batchIds.split(",");
			List batchList = new ArrayList();
			Map map = new HashMap();
			for(int i = 0; i < batchIds.length; i++){
				map.put("batchId", batchIds[i]);
				batchList.add(map);
			}
			impDataService.updateBatchStatus(batchList);
			return SUCCESS;
		} catch (Exception e) {
			this.setRESULT_MESSAGE("系统异常!");
			return ERROR;
		}
	}

	/**
	 * 获取数据列表（审核） 页面
	 */
	public String listImpdataAuditPage() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		List statusList = new ArrayList();
		statusList.add("3");
		List authInsts = new ArrayList();
		this.getAuthInstList(authInsts);
		// 审核通过）页面数据
		impTransCondition.setAuthInsts(authInsts);
		impTransCondition.setStatusList(statusList);
		impDataService.getImpDataListInfo(impTransCondition, paginationList);
		return SUCCESS;
	}
	/**
	 * 审核拒绝
	 */
	public String auditDataNo() {

		String[] batchIds = this.batchIds.split(",");
		Map map = new HashMap();
		List list = new ArrayList();
		for(int i = 0; i < batchIds.length; i++){
			map.put("batchId", batchIds[i]);
			list.add(map);
		}
		impDataService.updateStateBybatchID(list);
		return SUCCESS;
	}

	/**
	 * 数据审核方法
	 *
	 * @return
	 */
	public String auditData() {
		String[] batchIds = this.batchIds.split(",");
		List list = new ArrayList();
		Map map = new HashMap();
		for(int i= 0 ; i < batchIds.length; i ++){
			map.put("batchId", batchIds[i]);
			list.add(map);
		}
		impDataService.saveTransInfo(list);
		impDataService.deleteDataAfterTransInfo(list);
		impDataService.deleteBatchAterTransInfo(list);
		impDataService.updateBatchAfterTrans(list);
		return SUCCESS;
	}

	/**
	 * 创建excel
	 * @param os
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws Exception
	 */
	private void writeToExcel(OutputStream os) throws IOException,
			RowsExceededException, WriteException, Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("销项数据导入", 0);

		Label header1 = new Label(0, 0, "业务流水号", JXLTool.getHeader());
		ws.addCell(header1);
		ws.setColumnView(0, 25);
		Label header2 = new Label(1, 0, "客户号", JXLTool.getHeader());
		ws.addCell(header2);
		ws.setColumnView(1, 25);
		Label header3 = new Label(2, 0, "交易时间", JXLTool.getHeader());
		ws.addCell(header3);
		ws.setColumnView(2, 25);
		Label header4 = new Label(3, 0, "交易类型", JXLTool.getHeader());
		ws.addCell(header4);
		ws.setColumnView(3, 25);
		Label header5 = new Label(4, 0, "交易金额", JXLTool.getHeader());
		ws.addCell(header5);
		ws.setColumnView(4, 25);
		Label header6 = new Label(5, 0, "是否含税", JXLTool.getHeader());
		ws.addCell(header6);
		ws.setColumnView(5, 25);
		Label header7 = new Label(6, 0, "税率", JXLTool.getHeader());
		ws.addCell(header7);
		ws.setColumnView(6, 25);
		Label header8 = new Label(7, 0, "交易发生机构", JXLTool.getHeader());
		ws.addCell(header8);
		ws.setColumnView(7, 25);
		Label header9 = new Label(8, 0, "发票类型", JXLTool.getHeader());
		ws.addCell(header9);
		ws.setColumnView(8, 25);
		Label header10 = new Label(9, 0, "是否冲账", JXLTool.getHeader());
		ws.addCell(header10);
		ws.setColumnView(9, 25);
		// 是否打票
		Label header11 = new Label(10, 0, "是否打票", JXLTool.getHeader());
		ws.addCell(header11);
		ws.setColumnView(10, 25);

		Label header12 = new Label(11, 0, "原始业务流水号", JXLTool.getHeader());
		ws.addCell(header12);
		ws.setColumnView(11, 25);

		Label header13 = new Label(12, 0, "备注", JXLTool.getHeader());
		ws.addCell(header13);
		ws.setColumnView(11, 25);

		VmsTransInfo vmsTransInfo = new VmsTransInfo();
		vmsTransInfo.setBatchId(impBatchId);
		vmsTransInfo.setStatus("1");
		List impDatas = impDataService.findTransImpDataInfo(vmsTransInfo, null);
		int count = 1;
		for (int i = 0; i < impDatas.size(); i++) {
			int column = count++;
			setWritableSheet(ws, (VmsTransInfo) impDatas.get(i), column);
		}
		wb.write();
		wb.close();
	}

	/**
	 * 创建excel
	 * @param ws
	 * @param vmsTransInfo
	 * @param column
	 * @throws WriteException
	 */
	private void setWritableSheet(WritableSheet ws, VmsTransInfo vmsTransInfo,
								  int column) throws WriteException {
		Label cell1 = new Label(0, column, vmsTransInfo.getTransId(),
				JXLTool.getContentFormat());
		ws.addCell(cell1);
		Label cell2 = new Label(1, column, vmsTransInfo.getCustomerId(),
				JXLTool.getContentFormat());
		ws.addCell(cell2);
		Label cell3 = new Label(2, column, vmsTransInfo.getTransDate(),
				JXLTool.getContentFormat());
		ws.addCell(cell3);
		Label cell4 = new Label(3, column, vmsTransInfo.getTransType(),
				JXLTool.getContentFormat());
		ws.addCell(cell4);
		Label cell5 = new Label(4, column, vmsTransInfo.getTransAmt(),
				JXLTool.getContentFormat());
		ws.addCell(cell5);
		Label cell6 = new Label(5, column, vmsTransInfo.getTaxFlag(),
				JXLTool.getContentFormat());
		ws.addCell(cell6);
		String taxRate = vmsTransInfo.getTaxRate();
		if (taxRate.startsWith(".")) {
			taxRate = "0" + taxRate;
		}
		Label cell7 = new Label(6, column, taxRate,
				JXLTool.getContentFormat());
		ws.addCell(cell7);
		Label cell8 = new Label(7, column, vmsTransInfo.getBankCode(),
				JXLTool.getContentFormat());
		ws.addCell(cell8);
		Label cell9 = new Label(8, column, vmsTransInfo.getFapiaoType(),
				JXLTool.getContentFormat());
		ws.addCell(cell9);
		Label cell10 = new Label(9, column, vmsTransInfo.getIsReverse(),
				JXLTool.getContentFormat());
		ws.addCell(cell10);
		Label cell11 = new Label(10, column, vmsTransInfo.getTransFapiaoType(),
				JXLTool.getContentFormat());
		ws.addCell(cell11);
		Label cell12 = new Label(11, column, vmsTransInfo.getReverseTransId(),
				JXLTool.getContentFormat());
		ws.addCell(cell12);
		Label cell13 = new Label(12, column, vmsTransInfo.getRemark(),
				JXLTool.getContentFormat());
		ws.addCell(cell13);
	}

	/**
	 * 非空校验
	 * @param source
	 * @return
	 */
	public boolean checkNull(String source){
		if(null == source || "".equals(source)){
			return true;
		}else{
			return false;
		}
	}
	public TransInfoService getTransInfoService() {
		return transInfoService;
	}

	public void setTransInfoService(TransInfoService transInfoService) {
		this.transInfoService = transInfoService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public ImpDataService getImpDataService() {
		return impDataService;
	}

	public void setImpDataService(ImpDataService impDataService) {
		this.impDataService = impDataService;
	}

	public TransTypeService getTransTypeService() {
		return transTypeService;
	}

	public void setTransTypeService(TransTypeService transTypeService) {
		this.transTypeService = transTypeService;
	}

	public VmsTransInfo getImpTransCondition() {
		return impTransCondition;
	}

	public void setImpTransCondition(VmsTransInfo impTransCondition) {
		this.impTransCondition = impTransCondition;
	}

	public File getAttachment() {
		return attachment;
	}

	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}
	public String getBatchIds() {
		return batchIds;
	}
	public void setBatchIds(String batchIds) {
		this.batchIds = batchIds;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public VmsTransInfo getDetailInfo() {
		return detailInfo;
	}
	public void setDetailInfo(VmsTransInfo detailInfo) {
		this.detailInfo = detailInfo;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getImpBatchId() {
		return impBatchId;
	}
	public void setImpBatchId(String impBatchId) {
		this.impBatchId = impBatchId;
	}

	@Override
	public OrganizationService getOrganizationService() {
		return organizationService;
	}

	@Override
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
