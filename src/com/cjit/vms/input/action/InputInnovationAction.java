package com.cjit.vms.input.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.json.JSONObject;

import com.cjit.common.util.JXLTool;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputTrans;
import com.cjit.vms.input.service.InputInnovationService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.util.DataUtil;

public class InputInnovationAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private InputInnovationService  inputInnovationService;
	private InputInvoiceInfo  inputInvoiceInfo=new InputInvoiceInfo();
	private InputTrans  inputTrans=new InputTrans();
	private List inputInvoiceInfoList;
	private List inputTransList;
	private String billId;
	private String flag="";
	private String filePath="";
	private List lstAuthInstId;
	private List billItemList;
	private BillItemInfo billItem = new BillItemInfo();
	private Map mapVatType;//发票种类map
	private Map mapDataStatus;//发票状态map
	private Map mapIssueType;//开具类型map
	private Map mapTexType;//含税标志map
	/**
	 * @return 显示进项税列表
	 */
	public String listInnovation(){
		User currentUser = this.getCurrentUser();
		inputInvoiceInfo.setUserId(currentUser.getId());
		lstAuthInstId=new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		inputInvoiceInfo.setLstAuthInstId(lstAuthInstId);
		inputInvoiceInfoList=inputInnovationService.findInnovationList(inputInvoiceInfo, paginationList);
		return SUCCESS;
	}
	
	
	public void exportInnovation() throws IOException, JXLException{
		inputInvoiceInfo.setUserId(this.getCurrentUser().getId());
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		inputInvoiceInfo.setLstAuthInstId(lstAuthInstId);
		inputInvoiceInfoList=inputInnovationService.findInnovationList(inputInvoiceInfo);
		StringBuffer fileName = new StringBuffer("进项税勾稽列表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, inputInvoiceInfoList);
		os.flush();
		os.close();

	}
	/**
	 * @return 一钩机的交易列表
	 */
	public String listInputTransByVen(){
		billId=request.getParameter("billId");
		String vendorId=request.getParameter("vendorId");
		inputTransList=inputInnovationService.findInputInvoiceList(billId,vendorId);
		return SUCCESS;
	}
	/**
	 * @return 未钩稽列表
	 */
	public String listInputnoTransByVen(){
		billId=request.getParameter("billId");
		String vendorId=request.getParameter("vendorId");
		String dealNo=request.getParameter("dealNo");
		String bankCode=request.getParameter("bankCode");
		inputTransList=inputInnovationService.findInputNOInvoiceList(billId,vendorId,dealNo,bankCode,paginationList);
		return SUCCESS;
	}
	
	/**
	 * @return 详情页面
	 */
	public String InnovationDetail(){
		 billId=request.getParameter("billId");
		  flag=request.getParameter("flag");
		  List lstAuthInstId = new ArrayList();
		  this.getAuthInstList(lstAuthInstId);
		if(StringUtil.isNotEmpty(billId)){
			inputInvoiceInfo=new InputInvoiceInfo();
			InputInvoiceInfo inputInvoiceInfos=new InputInvoiceInfo();
			inputInvoiceInfos.setBillId(billId);
			inputInvoiceInfos.setLstAuthInstId(lstAuthInstId);
			inputInvoiceInfo=inputInnovationService.findInputInvoiceInfo(inputInvoiceInfo);
		}
		request.setAttribute("billId", billId);
		request.setAttribute("flag", flag);
		return  SUCCESS;
	}	
		/**
		 * @return 票据编辑
		 */
		public String editInnovation(){
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			String billId = request.getParameter("billId");
			if(StringUtil.isNotEmpty(billId)){
				inputInvoiceInfo=new InputInvoiceInfo();
				InputInvoiceInfo inputInvoiceInfos=new InputInvoiceInfo();
				inputInvoiceInfos.setBillId(billId);
				inputInvoiceInfos.setLstAuthInstId(lstAuthInstId);
				inputInvoiceInfo=inputInnovationService.findInputInvoiceInfo(inputInvoiceInfos);
			}
			setMapVatType(this.vmsCommonService.findCodeDictionary("VAT_TYPE"));
			setMapTexType(this.vmsCommonService.findCodeDictionary("TAX_FLAG"));
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDataStatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				mapDataStatus.put(tag.getValue(), tag.getText());
			}
			BillItemInfo xxx=new BillItemInfo(billId, null);
			billItemList = inputInnovationService.findBillItemInfoList(xxx);
			String vendorId=request.getParameter("vendorId");
			inputTransList=inputInnovationService.findInputInvoiceList(billId,vendorId);
			request.setAttribute("billId", billId);
			request.setAttribute("flag", flag);
			Map mapTransId = new HashMap();
			for(int i=0;i<inputTransList.size();i++){
				InputTrans transInfo = (InputTrans) inputTransList.get(i);
				mapTransId.put(transInfo.getDealNo(), transInfo.getDealNo());
			}
			JSONObject dealNoJson = JSONObject.fromObject(mapTransId);
			request.setAttribute("dealNoJson", dealNoJson);
			return  SUCCESS;
		}
		
	public String inputBillInfoCheckSave(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billId = request.getParameter("billId");
			String vendId=request.getParameter("vendId");
			String dealNodStr = request.getParameter("dealNodStr");
			String json = request.getParameter("odldealNoJson");
			Map oldIdData = new HashMap();
			Map newIdData = new HashMap();
			JSONObject jsonObject = JSONObject.fromObject(json); 
			Iterator it = jsonObject.keys();  
		    // 遍历jsonObject数据，添加到Map对象  初始数据  
		    while (it.hasNext())  
		    {  
		    	String key = String.valueOf(it.next());  
		    	String value = (String) jsonObject.get(key);  
		    	oldIdData.put(key, value);  
		    }
		    String[] dealNoArray = dealNodStr.split(",");
		    for (int i = 0; i < dealNoArray.length; i++) {
		    	if(!"".equals(dealNoArray[i])){
		            if(null!=oldIdData.get(dealNoArray[i])){
		            	//删除未变动   得到已取消钩稽的map
		            	oldIdData.remove(dealNoArray[i]);
		            }else{
		            	//增加钩稽的map
		            	newIdData.put(dealNoArray[i], dealNoArray[i]);//新增勾稽
		            }
		    	}
	        }
		    saveInputBillInfoCheck(oldIdData,newIdData,billId,vendId);
		    
		}catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoCheckAction-billInfoCheckSave", e);
		}
		return SUCCESS;
	}
	
	public void saveInputBillInfoCheck(Map oldIdData,Map newIdData,String billId,String vendId){
		Iterator confirmIt = newIdData.keySet().iterator(); 
	    while (confirmIt.hasNext()) {
	    	String dealNo=(String)confirmIt.next().toString();
			if(StringUtil.isNotEmpty(billId) && StringUtil.isNotEmpty(dealNo)){
				
				inputTrans=new InputTrans();
				inputTrans=inputInnovationService.findInputTransById(billId, dealNo);
			if(StringUtil.isEmpty(inputTrans.getBillId())){
				inputInnovationService.insertInputInvoice(billId, dealNo);
				inputInnovationService.updateInputTrans(billId, "1");
			}
			}
	    }
	    Iterator cancelIt = oldIdData.keySet().iterator(); 
	    while (cancelIt.hasNext()) {
	    	String dealNo=(String)cancelIt.next().toString();
	    	if(StringUtil.isNotEmpty(billId) && StringUtil.isNotEmpty(dealNo)){
				inputInnovationService.deleteInputInvoice(billId, dealNo);
				List list=inputInnovationService.findInputInvoiceList(billId, vendId);
				if(list.size()==0){
				inputInnovationService.updateInputTrans(billId, "2");
				}
			}
	    }
		
		
	}
	/**
	 * @return 撤销钩稽
	 */
	public void  deleteInnovation(){
		String  billId=request.getParameter("billId");
		String dealNo=request.getParameter("dealNo");
		String vendId=request.getParameter("vendId");
		if(StringUtil.isNotEmpty(billId) && StringUtil.isNotEmpty(dealNo)){
			inputInnovationService.deleteInputInvoice(billId, dealNo);
			List list=inputInnovationService.findInputInvoiceList(billId, vendId);
			if(list.size()==0){
			inputInnovationService.updateInputTrans(billId, "2");
			}
		}
		
		
	}
	/**
	 * 确认钩稽
	 */
	public void insertInnovation(){
		String  billId=request.getParameter("billId");
		String dealNo=request.getParameter("dealNo");
		if(StringUtil.isNotEmpty(billId) && StringUtil.isNotEmpty(dealNo)){
			
			inputTrans=new InputTrans();
			inputTrans=inputInnovationService.findInputTransById(billId, dealNo);
		if(StringUtil.isEmpty(inputTrans.getBillId())){
			inputInnovationService.insertInputInvoice(billId, dealNo);
			inputInnovationService.updateInputTrans(billId, "1");
		}
		}
	}
	/**
	 * @return 查看票样
	 */
	public String inputBill(){
		 filePath=request.getParameter("filePath");
		request.setAttribute("filePath", filePath);
		return SUCCESS;
	}
	/*public void exportInnovation() throws IOException, RowsExceededException, WriteException{
		inputInvoiceInfoList=inputInnovationService.findInnovationList(inputInvoiceInfo);
		StringBuffer fileName = new StringBuffer("进项税勾稽列表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, inputInvoiceInfoList);
		os.flush();
		os.close();

	}*/
	
	public List setwriteWidth(List list) throws JXLException{
		 List rowlist=null;
		 List sheetList=new ArrayList();
		 InputInvoiceInfo inputInvoiceInfo=null;
		for(int i=0;i<list.size();i++){
			rowlist=new ArrayList();
			inputInvoiceInfo=(InputInvoiceInfo) list.get(i);
			rowlist.add(Integer.toString(i));
			rowlist.add(inputInvoiceInfo.getBillCode());
			rowlist.add(inputInvoiceInfo.getBillNo());
			rowlist.add(inputInvoiceInfo.getBillDate());
			rowlist.add(inputInvoiceInfo.getInstName());
			rowlist.add(inputInvoiceInfo.getTaxAmtSum().toString());
			rowlist.add(inputInvoiceInfo.getFapiaoType());
			rowlist.add(inputInvoiceInfo.getVendorName());
			rowlist.add(inputInvoiceInfo.getVendorTaxno());
			rowlist.add(inputInvoiceInfo.getDatastatusName());
			rowlist.add(inputInvoiceInfo.getVatOutAmt().toString());
			rowlist.add(inputInvoiceInfo.getVerifyData().toString());
			sheetList.add(rowlist);
		}
		return sheetList;
	}
	public void writeToExcel(OutputStream os, List content) throws IOException,JXLException{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("进项税勾稽列表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "发票代码", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "发票号码", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "开票日期", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "金额所属机构", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "税额", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "发票类型", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "供应商名称", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "供应商纳税人识别号", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "发票状态", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "转出金额", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "认证日期", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header7);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header8);
		ws.addCell(header9);
		ws.addCell(header10);
		ws.addCell(header11);
		ws.addCell(header12);
//		for(int j = 0; j < 12; j++){
//			ws.setColumnView(j, 12);
//		}
		JXLTool.setAutoWidth(ws, setwriteWidth(content));
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			InputInvoiceInfo inputInvoiceInfo = (InputInvoiceInfo)content.get(c);
			int column = count++;
			setWritableSheet(ws, inputInvoiceInfo, column);
		}

		wb.write();
		wb.close();
	}
	private void setWritableSheet(WritableSheet ws,InputInvoiceInfo inputInvoiceInfo, int column)throws WriteException{
		int i = 0;
		Label cell1 = new Label(i++, column, Integer.toString(column),JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, inputInvoiceInfo.getBillCode(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, inputInvoiceInfo.getBillNo(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, inputInvoiceInfo.getBillDate(), JXLTool.getContentFormatDateFormat());
		Label cell5 = new Label(i++, column, inputInvoiceInfo.getInstName(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, inputInvoiceInfo.getTaxAmtSum().toString(), JXLTool.getContentFormatNumberFloat());
		Label cell7 = new Label(i++, column, DataUtil.getFapiaoTypeName(inputInvoiceInfo.getFapiaoType()), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, inputInvoiceInfo.getVendorName(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, inputInvoiceInfo.getVendorTaxno(), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, inputInvoiceInfo.getDatastatusName(), JXLTool.getContentFormat());
		Label cell11= new Label(i++, column, inputInvoiceInfo.getVatOutAmt().toString(), JXLTool.getContentFormatFloat());
		Label cell12= new Label(i++, column, inputInvoiceInfo.getVerifyData().toString(), JXLTool.getContentFormatDateFormat());
		
		
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
	
	public InputInnovationService getInputInnovationService() {
		return inputInnovationService;
	}
	public InputInvoiceInfo getInputInvoiceInfo() {
		return inputInvoiceInfo;
	}
	public void setInputInnovationService(
			InputInnovationService inputInnovationService) {
		this.inputInnovationService = inputInnovationService;
	}
	public void setInputInvoiceInfo(InputInvoiceInfo inputInvoiceInfo) {
		this.inputInvoiceInfo = inputInvoiceInfo;
	}



	public List getInputInvoiceInfoList() {
		return inputInvoiceInfoList;
	}



	public void setInputInvoiceInfoList(List inputInvoiceInfoList) {
		this.inputInvoiceInfoList = inputInvoiceInfoList;
	}

	public List getInputTransList() {
		return inputTransList;
	}

	public void setInputTransList(List inputTransList) {
		this.inputTransList = inputTransList;
	}

	public InputTrans getInputTrans() {
		return inputTrans;
	}

	public void setInputTrans(InputTrans inputTrans) {
		this.inputTrans = inputTrans;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public List getLstAuthInstId() {
		return lstAuthInstId;
	}


	public BillItemInfo getBillItem() {
		return billItem;
	}


	public void setBillItem(BillItemInfo billItem) {
		this.billItem = billItem;
	}


	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}


	public List getBillItemList() {
		return billItemList;
	}


	public void setBillItemList(List billItemList) {
		this.billItemList = billItemList;
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


	public Map getMapIssueType() {
		return mapIssueType;
	}


	public void setMapIssueType(Map mapIssueType) {
		this.mapIssueType = mapIssueType;
	}


	public Map getMapTexType() {
		return mapTexType;
	}


	public void setMapTexType(Map mapTexType) {
		this.mapTexType = mapTexType;
	}
	
	
	

}
