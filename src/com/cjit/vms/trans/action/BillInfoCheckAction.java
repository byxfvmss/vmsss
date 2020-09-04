package com.cjit.vms.trans.action;

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
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.service.BillInfoService;
import com.cjit.vms.trans.util.DataUtil;

/**
 * 销项税勾稽Action类
 * 
 */
public class BillInfoCheckAction extends DataDealAction{

	public String billInfoCheckList(){
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
			BillInfo info= new BillInfo();
			info.setApplyDate(request.getParameter("applyDate"));
			info.setEndDate(request.getParameter("endDate"));
			info.setStartDate(request.getParameter("startDate"));
			info.setCustomerName(request.getParameter("customerName"));
			info.setCustomerId(request.getParameter("customerId"));
			info.setFapiaoType(request.getParameter("fapiaoType"));
			info.setDataStatus(request.getParameter("dataStatus"));
			info.setLstAuthInstId(lstAuthInstId);
			if(null == request.getParameter("gjType")){
				info.setGjType("1");
			}else{
				info.setGjType(request.getParameter("gjType"));
			}
			billInfoService.findBillInfoCheckList(info, paginationList);
			setMapVatType(this.vmsCommonService.findCodeDictionary("VAT_TYPE"));
			setMapIssueType(this.vmsCommonService.findCodeDictionary("Issue_type"));
			List lstDataStatus=DataUtil.getAllBillDataStatusList();
			mapDataStatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				mapDataStatus.put(tag.getValue(), tag.getText());
			}
			this.request.setAttribute("gjType", info.getGjType());
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoCheckAction-billInfoCheckList", e);
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
	public String billInfoViewData(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			String billId = request.getParameter("billId");
			billInfo= billInfoService.getBillInfoDetail(billId);
			setMapVatType(this.vmsCommonService.findCodeDictionary("VAT_TYPE"));
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDataStatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				mapDataStatus.put(tag.getValue(), tag.getText());
			}
			itemList= billInfoService.getBillItemInfoList(billId, null);
			checkYList= billInfoService.getTransInfoCheckYlist(billId, null);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoCheckAction-billInfoViewData", e);
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
	public String billInfoViewImg(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			String billId = request.getParameter("billId");
			billInfo= billInfoService.getBillInfoDetail(billId);
			itemList= billInfoService.getBillItemInfoList(billId, null);
			if (itemList.size() > 9) {
				this.setRESULT_MESSAGE("发票最多包含9条数据！");
				return ERROR;
			}
			
			Map map = new HashMap();
			map.put("vatType", billInfo.getFapiaoType());
			map.put("billCode", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("billDate", billInfo.getBillDate());
			map.put("customerName", billInfo.getName());
			map.put("customerTaxno", billInfo.getTaxno());
			map.put("customerAddressandphone", billInfo.getAddressandphone());
			map.put("customerBankandaccount", billInfo.getBankandaccount());
//			map.put("billPasswd",billInfo);
			map.put("billItemList", itemList);
			map.put("cancelName", billInfo.getCustomerName());
			map.put("cancelTaxno", billInfo.getCustomerTaxno());
			map.put("cancelAddressandphone", billInfo.getCustomerAddressandphone());
			map.put("cancelBankandaccount", billInfo.getCustomerBankandaccount());
			map.put("payeeName", billInfo.getPayee());
			map.put("reviewerName", billInfo.getReviewerName());
			map.put("drawerName", billInfo.getDrawerName());
			map.put("remark", billInfo.getRemark());
			
			String imgName = vmsCommonService.createMark(map);
			Map retMap = vmsCommonService.findCodeDictionary("BILL_IMG_PATH");
			
			imgUrl = request.getContextPath()+retMap.get("BILL_IMG_PATH") + imgName;
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoCheckAction-billInfoViewImg", e);
		}
		return ERROR;
	}
	/**
	 * @Action
	 * 
	 * 数据勾稽操作
	 * 
	 * @return
	 */
	public String billInfoCheckExe(){
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
			billInfo= billInfoService.getBillInfoDetail(billId);
			setMapVatType(this.vmsCommonService.findCodeDictionary("VAT_TYPE"));
			setMapTexType(this.vmsCommonService.findCodeDictionary("TAX_FLAG"));
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDataStatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				mapDataStatus.put(tag.getValue(), tag.getText());
			}
			itemList= billInfoService.getBillItemInfoList(billId, null);
			checkYList= billInfoService.getTransInfoCheckYlist(billId, null);
			Map mapTransId = new HashMap();
			for(int i=0;i<checkYList.size();i++){
				TransInfo transInfo = (TransInfo) checkYList.get(i);
				mapTransId.put(transInfo.getTransId(), transInfo.getTransId());
			}
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			JSONObject transIdJson = JSONObject.fromObject(mapTransId);
			request.setAttribute("transIdJson", transIdJson);
			
			//Map m = new HashMap();
			//m.put("billId", billId);
			//checkNList = billInfoService.getTransInfoCheckNlist(m, null);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoCheckAction-billInfoCheckExe", e);
		}
		return ERROR;
	}
	/**
	 * @Action
	 * 
	 * 数据勾稽保存操作
	 * 
	 * @return
	 */
	public String billInfoCheckSave(){
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
			String transIdStr = request.getParameter("transIdStr");//需要勾稽的transId
			String transAmt=request.getParameter("transAmt");//钩稽编辑后的金额
			String json = request.getParameter("transIdJson");
			String transAmtOrg=request.getParameter("transAmtOrg");//钩稽编辑前的金额
			Map oldIdData = new HashMap();
			Map newIdData = new HashMap();
			Map oldIdDataChange = new HashMap();//已经钩稽的金额再次改动
			Map newData=new HashMap();
			JSONObject jsonObject = JSONObject.fromObject(json);  
		    Iterator it = jsonObject.keys();  
		    // 遍历jsonObject数据，添加到Map对象  
		    while (it.hasNext())  
		    {  
		    	String key = String.valueOf(it.next());  
		    	String value = (String) jsonObject.get(key);  
		    	oldIdData.put(key, value);  
		    }
		    String[] transIdArray = transIdStr.split(",");
		    String[] transAmtArray=transAmt.split(",");
		    String[] transAmtOrgArray=transAmtOrg.split(",");
		    for (int i = 0; i < transIdArray.length; i++) {
		    	if(!"".equals(transIdArray[i])){
		            if(null!=oldIdData.get(transIdArray[i])){
		            	//跟新数据
		            	
		            	if(transAmtArray[i]==transAmtOrgArray[i]){
		            		oldIdData.remove(transIdArray[i]);//原来存在 没有改动不需要处理的
		            	}
		            	//保存已改动数据
		            	Double cha=	Double.parseDouble(transAmtOrgArray[i])-Double.parseDouble(transAmtArray[i]);
		            	oldIdDataChange.put(transIdArray[i],cha );
		            }else{
		            	newIdData.put(transIdArray[i], transIdArray[i]);//新增勾稽
		            	newData.put(transIdArray[i],transAmtArray[i]);
		            }
		    	}
	        }
		    //撤销勾稽的
//		    Iterator entries = oldIdData.keySet().iterator(); 
//		    while (entries.hasNext()) {  
//		    	String key=(String)entries.next().toString();
//		        System.out.println("----" + key ); 
//		    }  
			int result = billInfoService.saveBillInfoCheck(newIdData,oldIdData,billId,newData,oldIdDataChange);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoCheckAction-billInfoCheckSave", e);
		}
		return ERROR;
	}
	/**
	 * @Action
	 * 
	 * 未勾稽数据查询
	 * 
	 * @return
	 */
	public String transInfoCheckNQuery(){
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
			setMapTexType(this.vmsCommonService.findCodeDictionary("TAX_FLAG"));
			setMapVatType(this.vmsCommonService.findCodeDictionary("VAT_TYPE"));
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDataStatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				mapDataStatus.put(tag.getValue(), tag.getText());
			}
			billId = request.getParameter("billId");
			transBusId = request.getParameter("transBusId");
			customerId = request.getParameter("customerId");
			bankCode = request.getParameter("bankCode");
			String transIdNotIn = request.getParameter("transIdNotIn");
			Map m = new HashMap();
			m.put("billId", billId);
			m.put("transBusId", transBusId);
			m.put("customerId", customerId);
			m.put("bankCode", bankCode);
			if(null!=transIdNotIn && !"".equals(transIdNotIn)){
				transIdNotIn = transIdNotIn.substring(0,transIdNotIn.length()-1);
				m.put("transIdNotIn", transIdNotIn);
			}
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			checkNList = billInfoService.getTransInfoCheckNlist(m, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoCheckAction-transInfoCheckNQuery", e);
		}
		return ERROR;
	}
	public List setwriteWidth(List list) throws JXLException{
	 List rowlist=null;
	 List sheetList=new ArrayList();
	 BillInfo info=null;
	for(int i=0;i<list.size();i++){
		rowlist=new ArrayList();
		info=(BillInfo) list.get(i);
		rowlist.add(String.valueOf(i));
		rowlist.add(info.getApplyDate());
		rowlist.add(info.getBillDate());
		rowlist.add(info.getCustomerName());
		rowlist.add(info.getCustomerTaxno());
		rowlist.add(info.getAmtSum()+"");
		rowlist.add(info.getTaxAmtSum()+"");
		rowlist.add(info.getSumAmt()+"");
		rowlist.add(mapIssueType.get(info.getIssueType())+"");
		rowlist.add(mapVatType.get(info.getFapiaoType())+"");
		rowlist.add(DataUtil.getDataStatusCH(info.getDataStatus(), "INPUT_INVOICE"));
		
		sheetList.add(rowlist);
	}
	return sheetList;
}/*
	Label cell1 = new Label(0, column, , JXLTool.getContentFormat());
	// 申请开票日期
	Label cell2 = new Label(1, column, , JXLTool.getContentFormat());
	// 开票日期
	Label cell3 = new Label(2, column, , JXLTool.getContentFormat());
	// 客户名称
	Label cell4 = new Label(3, column, , JXLTool.getContentFormat());
	// 客户纳税人识别号
	Label cell5 = new Label(4, column, , JXLTool.getContentFormat());
	// 合计金额
	Label cell6 = new Label(5, column, , JXLTool.getContentFormat());
	// 合计税额
	Label cell7 = new Label(6, column, , JXLTool.getContentFormat());
	// 价税合计
	Label cell8 = new Label(7, column, , JXLTool.getContentFormat());
	// 开具类型
	Label cell9 = new Label(8, column, , JXLTool.getContentFormat());
	// 发票类型
	Label cell10 = new Label(9, column, , JXLTool.getContentFormat());
	// 发票状态
	Label cell11 = new Label(10, column, , JXLTool.getContentFormat());*/
	/**
	 * @Action
	 * 
	 * 帐票excel出力
	 * 
	 * @return
	 */
	public void billInfoCheckExportExcel() throws Exception{
		try{
			StringBuffer fileName = new StringBuffer("销项税勾稽");
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
		ws = wb.createSheet("销项税勾稽", 0);
		/*WritableCellFormat wc = new WritableCellFormat();
        // 设置单元格的背景颜色
        wc.setBackground(jxl.format.Colour.YELLOW);*/
        JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header0 = new Label(0, 0, "序号", JXLTool.getHeaderC(excelInfo));
		Label header1 = new Label(1, 0, "申请开票日期", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(2, 0, "开票日期", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(3, 0, "客户名称", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(4, 0, "客户纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(5, 0, "合计金额", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(6, 0, "合计税额", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(7, 0, "价税合计", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(8, 0, "开具类型", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(9, 0, "发票类型", JXLTool.getHeaderC(excelInfo));
		Label header10 = new Label(10, 0, "状态", JXLTool.getHeaderC(excelInfo));
		ws.addCell(header0);
		ws.setColumnView(0, 7);
		ws.addCell(header1);
		ws.setColumnView(1, 18);
		ws.addCell(header2);
		ws.setColumnView(2, 20);
		ws.addCell(header3);
		ws.setColumnView(3, 20);
		ws.addCell(header4);
		ws.setColumnView(4, 20);
		ws.addCell(header5);
		ws.setColumnView(5, 12);
		ws.addCell(header6);
		ws.setColumnView(6, 12);
		ws.addCell(header7);
		ws.setColumnView(7, 12);
		ws.addCell(header8);
		ws.setColumnView(8, 12);
		ws.addCell(header9);
		ws.setColumnView(9, 18);
		ws.addCell(header10);
		ws.setColumnView(10, 20);
		
		List lstAuthInstId=new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		BillInfo info= new BillInfo();
		info.setEndDate(request.getParameter("endDate"));
		info.setStartDate(request.getParameter("startDate"));
		info.setCustomerName(request.getParameter("customerName"));
		info.setFapiaoType(request.getParameter("fapiaoType"));
		info.setDataStatus(request.getParameter("dataStatus"));
		info.setLstAuthInstId(lstAuthInstId);
		if(null == request.getParameter("gjType")){
			info.setGjType("1");
		}else{
			info.setGjType(request.getParameter("gjType"));
		}
		// 一览数据检索
		List outList = billInfoService.findBillInfoCheckList(info, null);
		mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
		mapIssueType = this.vmsCommonService.findCodeDictionary("Issue_type");
		int count = 1;
		JXLTool.setAutoWidth(ws, setwriteWidth(outList));

		for (int i = 0; i < outList.size(); i++) {
			BillInfo outInfo = (BillInfo) outList.get(i);
			int column = count++;
			setWritableSheet1(ws, outInfo, mapVatType, mapIssueType, column);
		}
	}
	/**
	 * @Action
	 * 
	 * 销项附加税详细列表数据
	 * 
	 * @return
	 */
	private void setWritableSheet1(WritableSheet ws, BillInfo info,Map mapVatType, Map mapIssueType, int column) throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column), JXLTool.getContentFormat());
		// 申请开票日期
		Label cell2 = new Label(1, column, info.getApplyDate(), JXLTool.getContentFormat());
		// 开票日期
		Label cell3 = new Label(2, column, info.getBillDate(), JXLTool.getContentFormat());
		// 客户名称
		Label cell4 = new Label(3, column, info.getCustomerName(), JXLTool.getContentFormat());
		// 客户纳税人识别号
		Label cell5 = new Label(4, column, info.getCustomerTaxno(), JXLTool.getContentFormat());
		// 合计金额
		Label cell6 = new Label(5, column, info.getAmtSum()+"", JXLTool.getContentFormat());
		// 合计税额
		Label cell7 = new Label(6, column, info.getTaxAmtSum()+"", JXLTool.getContentFormat());
		// 价税合计
		Label cell8 = new Label(7, column, info.getSumAmt()+"", JXLTool.getContentFormat());
		// 开具类型
		Label cell9 = new Label(8, column, mapIssueType.get(info.getIssueType())+"", JXLTool.getContentFormat());
		// 发票类型
		Label cell10 = new Label(9, column, mapVatType.get(info.getFapiaoType())+"", JXLTool.getContentFormat());
		// 发票状态
		Label cell11 = new Label(10, column, DataUtil.getDataStatusCH(info.getDataStatus(), "INPUT_INVOICE"), JXLTool.getContentFormat());
		
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
	
	
	//页面值传递变量声明
	private String applyDate;//申请开票日期
	private String customerName;//客户名称
	private String dataStatus;//状态
	private String fapiaoType;// 发票类型
	private Map mapVatType;//发票种类map
	private Map mapDataStatus;//发票状态map
	private Map mapIssueType;//开具类型map
	private Map mapTexType;//含税标志map
	private String gjType;//勾稽状态
	private String imgUrl;
	
	//发票勾稽页面查询条件
	private String billId;
	private String transBusId;//交易业务编号
	private String customerId;//客户ID
	private String bankCode;//交易发生机构


	/*service 声明*/
	private BillInfoService billInfoService;
	private BillInfo billInfo;// 对应的主体
	private List itemList;
	private List checkYList;//已勾稽list
	private List checkNList;//未勾稽list
	private String endDate;
	private String startDate;
	
	public BillInfoService getBillInfoService() {
		return billInfoService;
	}
	public void setBillInfoService(BillInfoService billInfoService) {
		this.billInfoService = billInfoService;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
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
	public BillInfo getBillInfo() {
		return billInfo;
	}
	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}
	public Map getMapIssueType() {
		return mapIssueType;
	}
	public void setMapIssueType(Map mapIssueType) {
		this.mapIssueType = mapIssueType;
	}
	public List getItemList() {
		return itemList;
	}
	public void setItemList(List itemList) {
		this.itemList = itemList;
	}
	public List getCheckYList() {
		return checkYList;
	}
	public void setCheckYList(List checkYList) {
		this.checkYList = checkYList;
	}
	public String getGjType() {
		return gjType;
	}
	public void setGjType(String gjType) {
		this.gjType = gjType;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public List getCheckNList() {
		return checkNList;
	}
	public void setCheckNList(List checkNList) {
		this.checkNList = checkNList;
	}

	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getTransBusId() {
		return transBusId;
	}
	public void setTransBusId(String transBusId) {
		this.transBusId = transBusId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public Map getMapTexType() {
		return mapTexType;
	}
	public void setMapTexType(Map mapTexType) {
		this.mapTexType = mapTexType;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
}
