package com.cjit.vms.input.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.ExcelUtil;
import cjit.crms.util.StringUtil;

import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceItem;
import com.cjit.vms.input.model.InputTrans;
import com.cjit.vms.input.model.InputTransInfo;
import com.cjit.vms.input.service.InputInvoiceService;
import com.cjit.vms.input.service.InputTransService;
import com.cjit.vms.input.service.InvoiceScanAuthService;
import com.cjit.vms.input.service.VendorInfoService;
import com.cjit.vms.system.model.LogEmp;
import com.cjit.vms.system.service.LogEmpService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.UBaseInst;
import com.cjit.vms.trans.util.CheckUtil;
import com.cjit.vms.trans.util.DataUtil;

/**
 * 进项税-发票扫描认证Action类
 *
 * @author jobell
 */
public class InvoiceScanAuthAction extends DataDealAction {
	

	/**
	 * @Action 进项税-发票扫描认证 查询页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String listInvoiceScanAuth(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			this.getAuthInstList(lstAuthInstId);
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			InputInvoiceInfo info = new InputInvoiceInfo();
			info.setBillDate(billDate);
			info.setVendorName(customerName);
			info.setDatastatus(datastatus);
			info.setInstcode(instId);
			info.setBillCode(billCode);
			info.setFapiaoType(fapiaoType); 
			info.setIdentifyDate(identifyDate);
			info.setLstAuthInstId(lstAuthInstId);
			info.setBillNo(billNo);
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				if(Integer.parseInt(tag.getValue())>9){
					break;
				}
				mapDatastatus.put(tag.getValue(), tag.getText());
			}
			invoiceScanAuthService.findListInvoiceScanAuth(info,paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-listInvoiceScanAuth", e);
		}
		return ERROR;
	}

	/**
	 * @Action 进项税-发票扫描认证 认证提交处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String authSubmit(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			invoiceScanAuthService.updateVmsInputInvoiceInfoForAuthSubmit("15", billId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-authSubmit", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 进项税-发票扫描认证 导出处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String exportInvoiceScanAuth(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			this.getAuthInstList(lstAuthInstId);
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			InputInvoiceInfo info = new InputInvoiceInfo();
			//modify by wang 20151029 start----------------------------
//			info.setBillDate(billDate);
//			info.setName(customerName);
//			info.setDatastatus(datastatus);
//			info.setInstcode(instId);
//			info.setBillCode(billCode);
//			info.setFapiaoType(fapiaoType); 
//			info.setIdentifyDate(identifyDate);
//			info.setLstAuthInstId(lstAuthInstId);
			info.setBillDate(billDate);
			info.setVendorName(customerName);
			info.setDatastatus(datastatus);
			info.setInstcode(instId);
			info.setBillCode(billCode);
			info.setFapiaoType(fapiaoType); 
			info.setIdentifyDate(identifyDate);
			info.setLstAuthInstId(lstAuthInstId);
			info.setBillNo(billNo);
			//modify by wang 20151029 end----------------------------
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				if(Integer.parseInt(tag.getValue())>9){
					break;
				}
				mapDatastatus.put(tag.getValue(), tag.getText());
			}
			List lstInputInvoiceInfo=invoiceScanAuthService.findListInvoiceScanAuth(info,null);
			if(lstInputInvoiceInfo.size()==0){
				return null;
			}
			
			StringBuffer fileName = new StringBuffer("进项附发票扫描认证");
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
			log.error("InvoiceScanAuthAction-exportInvoiceScanAuth", e);
		}
		return ERROR;
	}
	
	
	private void writeToExcel(OutputStream os, List lstInputInvoiceInfo,
			WritableWorkbook wb) throws WriteException {

		WritableSheet ws = null;
		ws = wb.createSheet("进项附发票扫描认证", 0);
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
		Label header10 = new Label(10, 0, "认证日期", JXLTool.getHeaderC(excelInfo));
		Label header11 = new Label(11, 0, "扫描时间", JXLTool.getHeaderC(excelInfo));
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
		ws.addCell(header10);
		ws.setColumnView(10, 15);
		ws.addCell(header11);
		ws.setColumnView(11, 15);
		for(int i=0;i<lstInputInvoiceInfo.size();i++){
			InputInvoiceInfo inInfo=(InputInvoiceInfo) lstInputInvoiceInfo.get(i);
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
		Label cell10 = new Label(9, column, DataUtil.getDataStatusCH(info.getDatastatus(), "INPUT_INVOICE"), JXLTool.getContentFormat());
		//认证日期
		Label cell11 = new Label(10, column, info.getIdentifyDate(), JXLTool.getContentFormat());
		//扫描时间
		Label cell12 = new Label(11, column, info.getScanDate(), JXLTool.getContentFormat());

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
	
	/**
	 * @Action 进项税-发票扫描认证 导入处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String importInvoiceScanAuth(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
			File[] files = mRequest.getFiles("theFile");
			if (files != null && files.length > 0) {
				try {
					doImportFile(files[0]);
					return SUCCESS;
				} catch (Exception e) {
					log.error(e);
					this.setResultMessages("上传文件失败:" + e.getMessage());
					e.printStackTrace();
					return ERROR;
				}
			} else {
				this.setResultMessages("上传文件失败!");
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-importInvoiceScanAuth", e);
		}
		return ERROR;
	}
	
	private List<Map<String, String>> doImportFile(File file) throws Exception {
		ArrayList<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
		
		List<Dictionary> headList= userInterfaceConfigService.getDictionarys(
				"INPUT_INVOICE_IMP", "");
		
		Hashtable<?, ?> hs = ExcelUtil.parseExcel(null, file, 1);
		String result="";
		Map<String, String> mapBusi=new HashMap<String,String>();
		String resultBusi="";
	/*	发票代码 	发票号码	开票日期	购方纳税人识别号	销方纳税人识别号	合计金额	合计税额	认证结果
		BILL_CODE	BILL_NO	BILL_DATE	TAXNO	VENDOR_TAXNO	AMT_SUM	TAX_AMT_SUM	datastatus*/

		if (hs != null) {
			String[][] sheet = (String[][]) hs.get("0");
			// 获取表头列表
			String[] heads = sheet[0];
			// 创建数据List对象
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			Map<String,Boolean> mapBillId=new HashMap<String, Boolean>();
			Set<String> set=new HashSet<String>();
			// 获取每行
			System.out.println("sheet.length"+sheet.length);
			String resultBillcode="";
			String batchNo=com.cjit.common.util.StringUtil.getBatchNo();
			String startDate=com.cjit.common.util.StringUtil.getCurrentDate();

			for (int i = 1; i < sheet.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				Map<String, Boolean> mapCheck=new HashMap<String, Boolean>();
				Map<String, Boolean> mapbillCode=new HashMap<String, Boolean>();
				String[] row = sheet[i];
				//System.out.println(heads[i]);
				// 获取单元格
				map=CheckUtil.CreatMap(heads, headList, row);
				String id=map.get("billCode")+","+map.get("billNo");
				set=CheckUtil.checkId(mapBillId, id, set);
				mapBillId.put(id, true);
				UBaseInst inst = invoiceScanAuthService.findUbaseInstByTaxNo(map.get("taxNo"));
				map.put("instcode", inst.getInstId()==null?"":inst.getInstId());
				map.put("balance", map.get("amtSum"));
				map.put("datastatus", "1");
					{//业务验证
						
					if(CheckUtil.checkDate(map.get("billDate"))&&CheckUtil.checkNotNull(map)){
						
						mapBusi=checkEmpInvoice(map, mapBusi, i, sheet.length);
						
						resultBusi=mapBusi.get("resultBusi");
					}
					}
					{ 
						List<String> checkList=new ArrayList<String>();
						checkList.add(map.get("billCode"));
						checkList.add(map.get("billNo"));
						checkList.add(map.get("billDate"));
						checkList.add(map.get("taxNo"));
						checkList.add(map.get("name"));
						checkList.add(map.get("addressAndPhone"));
						checkList.add(map.get("bankAndAccount"));
						checkList.add(map.get("amtSum"));
						checkList.add(map.get("taxAmtSum"));
						checkList.add(map.get("faPiaoType"));
						checkList.add(map.get("vendorName"));
						checkList.add(map.get("vendorTaxNo"));
						checkList.add(map.get("vendorAddressAndPhone"));
						checkList.add(map.get("vendorBankAndAccount"));
						checkList.add(map.get("vendorBankAndAccount"));
					mapCheck.put("checkDate", CheckUtil.checkDate(map.get("billDate").toString()));
					mapCheck.put("checkNull",CheckUtil.checkNotNull(checkList));
					result=CheckUtil.checkData(mapCheck, i, result, sheet.length);
					}
				// 创建流水号
				map.put("billId", this.createBusinessId("IN"));
				dataList.add(map);
			}
			resultBillcode=CheckUtil.checkId(set,"文件中发票代码|发票号码");
			result+=resultBusi+resultBillcode;
			if(result.length()>0){
				throw new Exception(result);
			}
			/*if(resultBusi.length()>0){
				throw new Exception(resultBusi);
			}*/
			else{
				if(dataList.size()>0){
					for(int i=0;i<dataList.size();i++){
						Map<String,String> map=dataList.get(i);
						map.put("batchNo", batchNo);
						map.put("conformFlg", "2");
						invoiceScanAuthService.saveInputinvoiceData(map);
					}
				}
			}
			mapBusi.put("batchNo", batchNo);
			List<String> list=invoiceScanAuthService.findinputInvoiceCompareinvoiceData(mapBusi);
			result="";
			if(list.size()>0){
				result=CheckUtil.checkSysId(list,"系统中发票代码|发票号码");
			}
			if(result.length()>0){
				throw new Exception(result);
			}else{
				for(int i=0;i<dataList.size();i++){
					Map<String,String> map=dataList.get(i);
					List<InputTrans> transList=inputTransService.findbillcodeInInputInvoice(map);
					invoiceScanAuthService.saveInputinvoiceInfo(map);
					if(transList.size()>0){
						for(int j=0;i<transList.size();j++){
						map.put("dealNo", transList.get(j).getBillNo());
						map.put("conformFlg", "1");
						inputTransService.saveTransInvoice(map);
						invoiceScanAuthService.updateInputInvoiceYconformFlg(map);
						}
					}
					
				}
				{	
					LogEmp log=new LogEmp();
					log.setSuccessNo(Integer.toString(dataList.size()));
					log.setBatchNo(batchNo);
					log.setEndDate(StringUtil.getCurrentDate());
					log.setFailedNo(Integer.toString(0));
					log.setId(com.cjit.common.util.StringUtil.getUUID());
					log.setFailedLog("");
					log.setStartDate(startDate);
					log.setSys("客户信息管理");
					logEmpService.saveLogEmp(log);
				}
				setResultMessages("导入成功");
			}
			//this.invoiceScanAuthService.saveVmsInputInvoiceInfoImport(dataList);
		}
		return errorList;
	}
	// 验证业务数据
	public Map<String, String> checkEmpInvoice(Map<String, String> map,Map<String,String> mapBusi,int i,int length){
		String resultBusi=StringUtil.IsEmptyStr(mapBusi.get("resultBusi"))?"":mapBusi.get("resultBusi");//业务验证字符 串
		String vendorMessage=StringUtil.IsEmptyStr(mapBusi.get("vendorMessage"))?"":mapBusi.get("vendorMessage");//供应商信息验证
		String invoiceCertified =StringUtil.IsEmptyStr(mapBusi.get("invoiceCertified"))?"":mapBusi.get("vendorCertified");// 发票 是否审核验证
		String instTaxNo =StringUtil.IsEmptyStr(mapBusi.get("instTaxNo"))?"":mapBusi.get("instTaxNo");// 发票 是否审核验证
		List<?> list=vendorInfoService.findVendorByTaxNo(map.get("vendorTaxNo").toString());
		if(map.get("instcode").equals("")){
			instTaxNo+=i+",";
		}
		if(list.size()==0){
			vendorMessage+=Integer.toString(i)+",";
		}
			String billCode = (String) map.get("billCode");
			String billNo = (String) map.get("billNo");
			InputInvoiceInfo oldObj=(InputInvoiceInfo) invoiceScanAuthService.findInvoiceScanAuthByBillCodeAndBillNo(billCode, billNo);
			System.out.println(billCode);
			if (oldObj!=null&&("3".equals(oldObj.getDatastatus()) || "5".equals(oldObj.getDatastatus()) || "7".equals(oldObj.getDatastatus()))) {
				invoiceCertified+=Integer.toString(i)+",";
			}
		vendorMessage=i+1==length&&vendorMessage.length()>0?"第"+vendorMessage.substring(0, vendorMessage.length()-1)+"行供应商纳税人识别号在供应商信息不存在":vendorMessage;
		invoiceCertified=i+1==length&&invoiceCertified.length()>0?"第"+invoiceCertified.substring(0, invoiceCertified.length()-1)+"行发票号码 发票代码 待审核中":invoiceCertified;
		instTaxNo=i+1==length&&instTaxNo.length()>0?"第"+instTaxNo.substring(0, instTaxNo.length()-1)+"行纳税人识别号对应的机构不存在":instTaxNo;
		resultBusi=i+1==length?vendorMessage+invoiceCertified+instTaxNo:resultBusi;
		mapBusi.put("vendorMessage",vendorMessage);
		mapBusi.put("invoiceCertified",invoiceCertified );
		mapBusi.put("resultBusi",resultBusi);
		mapBusi.put("instTaxNo",instTaxNo);
		return mapBusi;
	}
	/**
	 * @Action 进项税-发票扫描认证 编辑页面初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public String editInvoiceScanAuth(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			//发票主信息
			inputInvoiceInfo = invoiceScanAuthService.findInvoiceScanAuthByBillId(o_bill_id);
			String bill_code=inputInvoiceInfo.getBillCode();
			String bill_no=inputInvoiceInfo.getBillNo();
			// 交易信息获取
			inputTransInfo=invoiceScanAuthService.findInvoiceScanAuthTransInfoByBillId(bill_code,bill_no);
			//发票商品信息
			lstInputInvoiceItem=invoiceScanAuthService.findInvoiceScanAuthItemsByBillId(o_bill_id);
			if(null != lstInputInvoiceItem && lstInputInvoiceItem.size()>0 && lstInputInvoiceItem.size() < 8){
				for(int i = 0; i<lstInputInvoiceItem.size(); i++){
					InputInvoiceItem inputInvoiceItem = (InputInvoiceItem)lstInputInvoiceItem.get(i);
					if(null != inputInvoiceItem.getGoodsNo() && !"".equals(inputInvoiceItem.getGoodsNo())){
						if(inputInvoiceItem.getGoodsNo().startsWith(".")){
							inputInvoiceItem.setGoodsNo("0"+inputInvoiceItem.getGoodsNo());
						}
					}
					if(null != inputInvoiceItem.getGoodsPrice() && !"".equals(inputInvoiceItem.getGoodsPrice())){
						if(inputInvoiceItem.getGoodsPrice().startsWith(".")){
							inputInvoiceItem.setGoodsPrice("0"+inputInvoiceItem.getGoodsPrice());
						}
					}
					if(null != inputInvoiceItem.getAmt() && !"".equals(inputInvoiceItem.getAmt())){
						if(inputInvoiceItem.getAmt().startsWith(".")){
							inputInvoiceItem.setAmt("0"+inputInvoiceItem.getAmt());
						}
					}
					if(null != inputInvoiceItem.getTaxAmt() && !"".equals(inputInvoiceItem.getTaxAmt())){
						if(inputInvoiceItem.getTaxAmt().toString().startsWith(".")){
							inputInvoiceItem.setTaxAmt(BigDecimal.valueOf(Double.parseDouble("0"+inputInvoiceItem.getTaxAmt())));
						}
					}
					if(null != inputInvoiceItem.getTaxRate() && !"".equals(inputInvoiceItem.getTaxRate())){
						if(inputInvoiceItem.getTaxRate().startsWith(".")){
							inputInvoiceItem.setTaxRate("0"+inputInvoiceItem.getTaxRate());
						}
					}
				}
				for(int i = lstInputInvoiceItem.size(); i<8; i++){
					lstInputInvoiceItem.add(new InputInvoiceItem());
				}
			} else if(null == lstInputInvoiceItem || lstInputInvoiceItem.size() == 0){
				for(int i = 0; i< 8; i++){
					lstInputInvoiceItem.add(new InputInvoiceItem());
				}
			}
			
			String datastatus=inputInvoiceInfo.getDatastatus();
			if("3".equals(datastatus)||"5".equals(datastatus)||"7".equals(datastatus)){
				authPassFlag="1";
			}else{
				authPassFlag="0";
			}
			
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				if(Integer.parseInt(tag.getValue())>9){
					break;
				}
				mapDatastatus.put(tag.getValue(), tag.getText());
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-editInvoiceScanAuth", e);
		}
		return ERROR;
	}
	/**
	 * 进项税-发票扫描认证     商品信息编辑保存处理
	 */
	public void updateInvoiceItemInfo(){
		try {
			InputInvoiceItem item=new InputInvoiceItem();
			item.setGoodsName(request.getParameter("goodsName"));  
			item.setSpecandmodel(request.getParameter("specandmodel"));  
			item.setGoodsNo(request.getParameter("goodsNo"));  
			item.setGoodsPrice(request.getParameter("goodsPrice"));  
			item.setAmt(request.getParameter("amt"));  
			item.setTaxRate(request.getParameter("taxRate"));  
			item.setTaxAmt(new BigDecimal(request.getParameter("taxAmt")));  
			item.setBillId(request.getParameter("billId"));
			
			String billItemId = request.getParameter("billItemId");
			if(null != billItemId && !"".equals(billItemId)){
				item.setBillItemId(billItemId);
				invoiceScanAuthService.updateVmsInputInvoiceItem(item);
			} else {
				billItemId = invoiceScanAuthService.findSequenceBillItemId();
				item.setBillItemId(billItemId);
				invoiceScanAuthService.insertVmsInputInvoiceItem(item);
			}
			
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(billItemId);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("loadTransTypeInfo : ", ex);
		}
	}
	/**
	 * 进项税-发票扫描认证     商品信息删除处理
	 */
	public void deleteInvoiceItemInfo(){
		try {
			InputInvoiceItem item=new InputInvoiceItem(); 
			item.setBillId(request.getParameter("billId"));
			String billItemId = request.getParameter("billItemId");
			item.setBillItemId(billItemId);
			invoiceScanAuthService.deleteVmsInputInvoiceItem(item);
			
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(billItemId);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("loadTransTypeInfo : ", ex);
		}
	}
	/**
	 * @Action 进项税-发票扫描认证 编辑保存处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String updateInvoiceScanAuth(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
//			List lstGoodAdded=new ArrayList();
//			if(StringUtils.isNotEmpty(addGoodNum)){
//				int add_good_size=Integer.parseInt(addGoodNum);
//				for(int i=0;i<add_good_size;i++){
//					InputInvoiceItem item=new InputInvoiceItem();
//					item.setGoodsName(request.getParameter("goodsName"+i));  
//					item.setSpecandmodel(request.getParameter("specandmodel"+i));  
//					item.setGoodsNo(request.getParameter("goodsNo"+i));  
//					item.setGoodsPrice(request.getParameter("goodsPrice"+i));  
//					item.setAmt(request.getParameter("amt"+i));  
//					item.setTaxRate(request.getParameter("taxRate"+i));  
//					item.setTaxAmt(new BigDecimal(request.getParameter("taxAmt"+i)));  
//					item.setBillId(o_bill_id);
//					lstGoodAdded.add(item);
//				}
//			}
//			invoiceScanAuthService.updateVmsInputInvoiceInfoForScanAuth(inputInvoiceInfo,lstGoodAdded,o_bill_id);
			
			invoiceScanAuthService.updateVmsInputInvoiceInfoForScanAuth(inputInvoiceInfo,o_bill_id);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-updateInvoiceScanAuth", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 进项税-发票扫描认证 查看详情页面初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public String viewInvoiceScanAuth(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			List lstDataStatus=DataUtil.getInputInvoiceDataStatusList();
			mapDatastatus=new HashMap();
			for(int i=0;i<lstDataStatus.size();i++){
				SelectTag tag=(SelectTag) lstDataStatus.get(i);
				if(Integer.parseInt(tag.getValue())>9){
					break;
				}
				mapDatastatus.put(tag.getValue(), tag.getText());
			}
			
			inputInvoiceInfo = invoiceScanAuthService.findInvoiceScanAuthByBillId(o_bill_id);
			String bill_code=inputInvoiceInfo.getBillCode();
			String bill_no=inputInvoiceInfo.getBillNo();
			inputTransInfo=invoiceScanAuthService.findInvoiceScanAuthTransInfoByBillId(bill_code,bill_no);
			lstInputInvoiceItem=invoiceScanAuthService.findInvoiceScanAuthItemsByBillId(o_bill_id);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-viewInvoiceScanAuth", e);
		}
		return ERROR;
	}
	public String viewInvoicePic(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceScanAuthAction-viewInvoicePic", e);
		}
		return ERROR;
	}
	
	//页面传递参数定义
	private String billDate;//开票日期[查询]
	private String customerName;//供应商名称[查询]
	private String datastatus;//发票状态[查询]
	private String instId;//所属机构[查询]
	private String instName;
	private String billCode;//发票代码[查询]
	private String billNo;//发票号码[查询]
	private String fapiaoType;//发票种类[查询]
	private String identifyDate;//认证日期[查询]
	
	private String[] billId;//认证提交
	private String o_bill_id;//发票号码[查看，编辑]
	
	private InputInvoiceInfo inputInvoiceInfo;//[编辑，查看]
	private List lstInputInvoiceItem;//[编辑，查看]
	private InputTransInfo inputTransInfo;
	private String currentPage;//当前页数[编辑，查看]
	
	private Map mapVatType;//发票种类下拉列表Map[查询，展示]
	private Map mapDatastatus;//发票状态下拉列表Map[查询，展示]
	private List lstAuthInstId = new ArrayList();//所属机构下拉列表[查询]
	private String addGoodNum;
	
	private String authPassFlag;//认证是否通过 3,5,7为通过

	public String getAddGoodNum() {
		return addGoodNum;
	}

	public void setAddGoodNum(String addGoodNum) {
		this.addGoodNum = addGoodNum;
	}

	public String getAuthPassFlag() {
		return authPassFlag;
	}

	public void setAuthPassFlag(String authPassFlag) {
		this.authPassFlag = authPassFlag;
	}

	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public Map getMapVatType() {
		return mapVatType;
	}
	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getDatastatus() {
		return datastatus;
	}
	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
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
	public String getIdentifyDate() {
		return identifyDate;
	}
	public void setIdentifyDate(String identifyDate) {
		this.identifyDate = identifyDate;
	}
	public Map getMapDatastatus() {
		return mapDatastatus;
	}
	public void setMapDatastatus(Map mapDatastatus) {
		this.mapDatastatus = mapDatastatus;
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
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/*service 注入*/
	private InvoiceScanAuthService invoiceScanAuthService;
	private InputInvoiceService inputInvoiceService;
	private VendorInfoService vendorInfoService;
	private InputTransService inputTransService;

	public InvoiceScanAuthService getInvoiceScanAuthService() {
		return invoiceScanAuthService;
	}
	public void setInvoiceScanAuthService(
			InvoiceScanAuthService invoiceScanAuthService) {
		this.invoiceScanAuthService = invoiceScanAuthService;
	}

	public InputInvoiceService getInputInvoiceService() {
		return inputInvoiceService;
	}

	public void setInputInvoiceService(InputInvoiceService inputInvoiceService) {
		this.inputInvoiceService = inputInvoiceService;
	}

	public VendorInfoService getVendorInfoService() {
		return vendorInfoService;
	}

	public void setVendorInfoService(VendorInfoService vendorInfoService) {
		this.vendorInfoService = vendorInfoService;
	}

	public InputTransService getInputTransService() {
		return inputTransService;
	}

	public void setInputTransService(InputTransService inputTransService) {
		this.inputTransService = inputTransService;
	}
	private LogEmpService logEmpService;

	public LogEmpService getLogEmpService() {
		return logEmpService;
	}

	public void setLogEmpService(LogEmpService logEmpService) {
		this.logEmpService = logEmpService;
	}
	
	
}
