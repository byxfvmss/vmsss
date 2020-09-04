package com.cjit.vms.system.action;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.model.LogEmp;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.system.service.LogEmpService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.util.CheckUtil;
import com.cjit.vms.trans.util.DataUtil;



public class CustomerAction  extends DataDealAction{
	private static final long serialVersionUID = 1L;
	
	private Customer customer=new Customer();
	private CustomerService customerService;
	private String[] customerIdList; // 选中客户编号列表
	
	private File attachmentCustomer;
	private String attachmentCustomerFileName;
	private String attachmentCustomerType;
	private List dataOperationLabelList;
	private List dataAuditstatusList;
	private LogEmpService logEmpService;
	/**
	 * 数据来源 手工
	 */
	private static final String dataSourch_ch1="1";
	/**
	 * 数据来源  系统
	 */
	private static final String dataSourch_ch2="2";
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
	private String customerCode;
	private String customerOffice;
	private String customerCName;
	private String taxpayerType;
	private String vatInvoice="-1";
	private String taxNo;
	private String customerNationality;
	private List countrys;
	private String customerFapiaoFlag; //是否打票
	private String customerType; //客户类型
	private List dataSourceList; //客户类型
	private String ghoClass;//gho 类
	private String datasSource;//数据来源
	private String dataOperationLabel;//数据来源
	private String dataAuditStatus;//数据来源
	private String   linkName;						//	LINK_NAME	VARCHAR2(30)	Y			联系人名称
	private String   linkPhone;						//	LINK_PHONE	VARCHAR2(15)	Y			联系人电话
	private String 	 linkAddress;
	private String  customerZipCode;
	private UserInterfaceConfigService userInterfaceConfigService;
	/**
	* <p>方法名称: listCustomer|描述: 查询客户列表</p>
	* @return 客户列表
	* 
	*/     
	public String listCustomer(){
		try{
			countrys = customerService.findCountry();
			String customerCode = request.getParameter("customerCode");
			String customerCName = request.getParameter("customerCName");
			String taxpayerType = request.getParameter("taxpayerType");
			String vatInvoice = request.getParameter("vatInvoice");
			String taxNo = request.getParameter("taxNo");
			String customerNationality = request.getParameter("customerNationality");
			//20160501 大写客户号查询失败
//			customer.setCustomerID(StringUtil.isEmpty(customerCode)?customerCode:customerCode.toLowerCase());
			customer.setCustomerID(customerCode);
			customer.setCustomerCName(customerCName);
			customer.setTaxPayerType(taxpayerType);
			customer.setVatInvoice(vatInvoice);
			customer.setCustomerTaxno(taxNo);
			customer.setCustomerType(request.getParameter("customerType"));
			customer.setCustomerFapiaoFlag(request.getParameter("customerFapiaoFlag"));
			customer.setCustomerNationality(customerNationality);
			customer.setDatasSource(request.getParameter("datasSource"));
			customer.setGhoClass(request.getParameter("ghoClass"));
			customerService.findCustomerList(customer,paginationList);
			dataSourceList=DataUtil.getdataSourceList();
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			log.error(e);
		}
		return ERROR;
	}
	
	/**
	* <p>方法名称: createCustomer|描述: 新增</p>
	* @return 
	*/
	public String createCustomer(){
		try{
			countrys = customerService.findCountry();
			dataSourceList=DataUtil.getdataSourceList();
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	
	/**
	* <p>方法名称: saveCustomer|描述: 保存新增</p>
	* @return       customerTaxno customerID  customerEMail taxPayerType
	*/
	public String saveCustomer(){
		String TaxNo = request.getParameter("customerTaxno");
		if(StringUtil.isNotEmpty(TaxNo)){
		customer= customerService.findCustomerByTaxNo(TaxNo);
		}
		try{
			if(StringUtil.isEmpty(customer.getCustomerTaxno())||(!request.getParameter("taxpayerType").equals("G")&&StringUtil.isEmpty(TaxNo))){
				String  result=customerService.findCustomerTempByTaxNo(TaxNo);
				if(StringUtil.isEmpty(result)){
					customer=new Customer();
					customer.setCustomerCName(request.getParameter("customerCName"));
					//System.out.println(request.getParameter("customerTaxno"));
					customer.setCustomerTaxno(request.getParameter("customerTaxno"));
					customer.setCustomerAccount(request.getParameter("customerAccount"));
					customer.setCustomerCBank(request.getParameter("customerCBank"));
					customer.setCustomerPhone(request.getParameter("customerPhone"));
					customer.setCustomerEMail(request.getParameter("customerEmail"));
					customer.setCustomerAddress(request.getParameter("customerAddress"));
					customer.setTaxPayerType(request.getParameter("taxpayerType"));
					customer.setFapiaoType(request.getParameter("fapiaoType"));
					customer.setCustomerType(request.getParameter("customerType"));
					customer.setCustomerFapiaoFlag(request.getParameter("customerFapiaoFlag"));
					customer.setCustomerID(request.getParameter("customerID"));
					customer.setCustomerNationality(request.getParameter("customerNationality"));
					customer.setGhoClass(request.getParameter("ghoClass"));
					customer.setDatasSource(dataSourch_ch1);
					customer.setDataOperationLabel("1");
					customer.setDataAuditStatus("0");
					customer.setLinkAddress(linkAddress);
					customer.setLinkName(linkName);
					customer.setLinkPhone(linkPhone);
					customer.setCustomerZipCode(customerZipCode);
					customerService.saveCustomer(customer);
				
				}else{
					setResultMessages("此纳税人识别号在待审核中;请先确认");
				}
				setResultMessages("新增客户信息提交成功，等待后台审核");
			}else{
				setResultMessages("客户识别号已存在！请重新输入");
			}
		}catch (Exception e) {
			setResultMessages("系统出错！保存失败!");
		}
		return SUCCESS;
			
	}
	
	/**
	* <p>方法名称: editCustomer|描述: 修改</p>
	* @return 
	*/
	public String editCustomer(){
		try{
			String customerId = request.getParameter("customerId");
			customer = customerService.findCustomer(customerId);
			countrys = customerService.findCountry();
			dataSourceList=DataUtil.getdataSourceList();
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	
	/**
	* <p>方法名称: saveEditCustomer|描述: 保存修改</p>
	* @return   private String  customerId;
  
	*/
	public String saveEditCustomer(){
		String texNo=request.getParameter("customerTaxno1");
		String texN=request.getParameter("customerTaxno");
		String taxpayerType=request.getParameter("taxpayerType");
		try{
			
			String result=customerService.findCustomerTempByTaxNo(texN);
			if(!StringUtil.isEmpty(texN)){
				customer=customerService.findCustomer("", texN, "", "", "");
			if(customer==null ||texNo.equals(texN)){
				if(StringUtil.isEmpty(result)){
					customer=new Customer();
					customer.setCustomerID(request.getParameter("customerId"));
					customer.setCustomerCName(request.getParameter("customerCName"));
					customer.setCustomerTaxno(request.getParameter("customerTaxno"));
					customer.setCustomerAccount(request.getParameter("customerAccount"));
					customer.setCustomerCBank(request.getParameter("customerCBank"));
					customer.setCustomerPhone(request.getParameter("customerPhone"));
					customer.setCustomerEMail(request.getParameter("customerEmail"));
					customer.setCustomerAddress(request.getParameter("customerAddress"));
					customer.setTaxPayerType(request.getParameter("taxpayerType"));
					customer.setFapiaoType(request.getParameter("fapiaoType"));
					customer.setCustomerType(request.getParameter("customerType"));
					customer.setDataOperationLabel(dataOperation_label_ch2);
					customer.setDataAuditStatus(data_audit_status_0);
					customer.setGhoClass(request.getParameter("ghoClass"));
					customer.setDatasSource(dataSourch_ch1);
					customer.setCustomerFapiaoFlag(request.getParameter("customerFapiaoFlag"));
					customer.setCustomerNationality(request.getParameter("customerNationality"));
					customer.setLinkAddress(linkAddress);
					customer.setLinkName(linkName);
					customer.setLinkPhone(linkPhone);
					customer.setCustomerZipCode(customerZipCode);
					customerService.saveCustomer(customer);
					setResultMessages("修改客户信息提交成功，等待后台审核");
				}else{
					setResultMessages("此纳税人识别号在审核中 请确认");
				}
			}else{
				setResultMessages("纳税识别号已存在！请重新书写");
			}
			}else{
				if(StringUtil.isEmpty(result)&& !taxpayerType.equals("G")){
					customer=new Customer();
					customer.setCustomerID(request.getParameter("customerId"));
					customer.setCustomerCName(request.getParameter("customerCName"));
					customer.setCustomerTaxno(request.getParameter("customerTaxno"));
					customer.setCustomerAccount(request.getParameter("customerAccount"));
					customer.setCustomerCBank(request.getParameter("customerCBank"));
					customer.setCustomerPhone(request.getParameter("customerPhone"));
					customer.setCustomerEMail(request.getParameter("customerEmail"));
					customer.setCustomerAddress(request.getParameter("customerAddress"));
					customer.setTaxPayerType(request.getParameter("taxpayerType"));
					customer.setFapiaoType(request.getParameter("fapiaoType"));
					customer.setCustomerType(request.getParameter("customerType"));
					customer.setDataOperationLabel(dataOperation_label_ch2);
					customer.setDataAuditStatus(data_audit_status_0);
					customer.setGhoClass(request.getParameter("ghoClass"));
					customer.setDatasSource(dataSourch_ch1);
					customer.setCustomerFapiaoFlag(request.getParameter("customerFapiaoFlag"));
					customer.setCustomerNationality(request.getParameter("customerNationality"));
					customer.setLinkAddress(linkAddress);
					customer.setLinkName(linkName);
					customer.setLinkPhone(linkPhone);
					customer.setCustomerZipCode(customerZipCode);
					customerService.saveCustomer(customer);
					setResultMessages("修改客户信息提交成功，等待后台审核");
					}else{
					setResultMessages("此纳税人识别号在审核中 请确认");
					}
			}
			}catch (Exception e) {
				setResultMessages("系统错误！修改失败");
			}
		return SUCCESS;
	}
	
	/**
	* <p>方法名称: viewCustomerDetail|描述: 查看客户信息明细</p>
	* @return 
	*/
	public String viewCustomerDetail(){
		String customerId = request.getParameter("customerId");
		customer = customerService.findCustomer(customerId);
		return SUCCESS;
	}
	
	
	/**
	* <p>方法名称: deleteCustomer|描述: 删除客户信息</p>
	* @return 
	*/
	public String deleteCustomer1(){
		List customers = new ArrayList();
		for(int i = 0; i < customerIdList.length; i++){
			Customer customer = new  Customer();
			customer.setCustomerID(customerIdList[i]);
			customers.add(customer);
		}
		if(customerIdList.length>0){
		customerService.deleteAll(customers);
		setResultMessages("删除成功");
		}
		return SUCCESS;
	}
	public void deleteCustomer() throws Exception{
		String ids="";
		List customers = new ArrayList();
		String cuatomerIds=request.getParameter("customerIdList");
		customerIdList=cuatomerIds.split(",");
		String idn="";
		String result="";
		for(int i=0;i<customerIdList.length;i++){
			List list=customerService.findTransByCustomerId(customerIdList[i]);
			if(list.size()>0){
				ids+=customerIdList[i]+",";
				
			}else{
				Customer customer = new  Customer();
				customer.setCustomerID(customerIdList[i]);
				customer.setDataOperationLabel("3");
				customer.setDataAuditStatus("0");
				customers.add(customer);
			}
			
		}
		if(ids!=""){
			 result="客户号"+ids+"存在交易不能删除请重新选择";
			//printWriterResult(result);
			
		}

			if(customers.size()>0){
				for(int i=0;i<customers.size();i++){
					customer=(Customer)customers.get(i);
				customerService.copyCustomerToCustommerTemp(customer);
				idn+=customer.getCustomerID()+",";
			}
				result+="客户号"+idn+"删除客户信息请求成功，等待后台审核";
		}
			printWriterResult(result);
	}
	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
		
	}

	/**
	* <p>方法名称: exportCustomer|描述: 导出客户信息</p>
	* @return 
	*/
	public void exportCustomer() throws Exception{
		countrys = customerService.findCountry();
		String customerCode = request.getParameter("customerCode");
		String customerCName = request.getParameter("customerCName");
		String taxpayerType = request.getParameter("taxpayerType");
		String vatInvoice = request.getParameter("vatInvoice");
		String taxNo = request.getParameter("taxNo");
		String customerNationality = request.getParameter("customerNationality");
		
		customer.setCustomerID(StringUtil.isEmpty(customerCode)?customerCode:customerCode.toLowerCase());
		customer.setCustomerCName(customerCName);
		customer.setTaxPayerType(taxpayerType);
		customer.setVatInvoice(vatInvoice);
		customer.setCustomerTaxno(taxNo);
		customer.setCustomerType(request.getParameter("customerType"));
		customer.setCustomerFapiaoFlag(request.getParameter("customerFapiaoFlag"));
		customer.setCustomerNationality(customerNationality);
		customer.setDatasSource(request.getParameter("datasSource"));
		customer.setGhoClass(request.getParameter("ghoClass"));
		List resultList=customerService.findCustomerList(customer);
		
		StringBuffer fileName = new StringBuffer("客户信息列表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, resultList);
		os.flush();
		os.close();
		
	}
	public List setwriteWidth(List list) throws JXLException{
		 List rowlist=null;
		 List sheetList=new ArrayList();
		 Customer customer=null;
		for(int i=0;i<list.size();i++){
			rowlist=new ArrayList();
			customer=(Customer) list.get(i);
			rowlist.add(customer.getCustomerCName());
			rowlist.add(customer.getCustomerTaxno());
			rowlist.add(customer.getCountrySName());
			rowlist.add(customer.getCustomerAddress());
			rowlist.add(customer.getCustomerPhone());
			rowlist.add(customer.getCustomerEMail());
			rowlist.add(customer.getCustomerCBank());
			rowlist.add(customer.getCustomerAccount());
			rowlist.add(getCustomerTypeName(customer.getCustomerType()));
			rowlist.add(getTaxpayerTypeName(customer.getTaxPayerType()));
			rowlist.add(getFapiaoTypeName(customer.getFapiaoType()));
			rowlist.add(getCustomerFapiaoFlagName(customer.getCustomerFapiaoFlag()));
/*			rowlist.add(customer.getGhoClass());
*/			rowlist.add(customer.getCustomerID());
			rowlist.add(DataUtil.getdataSourceCh(customer.getDatasSource()));
			rowlist.add(customer.getLinkName());
			rowlist.add(customer.getLinkAddress());
			rowlist.add(customer.getLinkPhone());
			rowlist.add(customer.getCustomerZipCode());
			sheetList.add(rowlist);
		}
		return sheetList;
	}
	
	public void writeToExcel(OutputStream os, List content) throws IOException,JXLException{
		WritableWorkbook wb = Workbook.createWorkbook(os);
	WritableSheet	 ws = null;
		int i = 0;
		ws = wb.createSheet("客户信息列表", 0);
		//客户名称	客户纳税人识别号	国籍	地址	电话	邮箱	开户银行	开户账号	客户类型	纳税人类型	发票类型	是否打票	客户编号

		Label header1 = new Label(i++, 0, "客户名称", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "国籍", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "地址", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "电话", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "邮箱", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "开户银行", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "开户账号", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "客户类型", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "纳税人类型", JXLTool.getHeader());
		Label header11= new Label(i++, 0, "发票类型", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "是否打票", JXLTool.getHeader());
/*		Label header13= new Label(i++, 0, "GHO类", JXLTool.getHeader());
*/		Label header14 = new Label(i++, 0, "客户编号", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "数据来源", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "联系人", JXLTool.getHeader());
		Label header17 = new Label(i++, 0, "联系人地址", JXLTool.getHeader());
		Label header18 = new Label(i++, 0, "联系人电话", JXLTool.getHeader());
		Label header19 = new Label(i++, 0, "客户邮编", JXLTool.getHeader());
		//		

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
/*		ws.addCell(header13);
*/		ws.addCell(header14);
		ws.addCell(header15);
		ws.addCell(header16);
		ws.addCell(header17);
		ws.addCell(header18);
		ws.addCell(header19);
		JXLTool.setAutoWidth(ws, setwriteWidth(content));
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			Customer customer = (Customer)content.get(c);
			int column = count++;
			if(count>65535){
				setResultMessages("导出数据超过excel最大承受量");
				break;
			}
			setWritableSheet(ws, customer, column);
		}
		wb.write();
		wb.close();
	}
	private void setWritableSheet(WritableSheet ws, Customer customer, int column)throws WriteException{
		int i = 0;
		
		Label cell1 = new Label(i++, column, customer.getCustomerCName(),JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, customer.getCustomerTaxno(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, customer.getCountrySName(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, customer.getCustomerAddress(), JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, customer.getCustomerPhone(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, customer.getCustomerEMail(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, customer.getCustomerCBank(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, customer.getCustomerAccount(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, getCustomerTypeName(customer.getCustomerType()), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, getTaxpayerTypeName(customer.getTaxPayerType()), JXLTool.getContentFormat());
		Label cell11= new Label(i++, column, getFapiaoTypeName(customer.getFapiaoType()), JXLTool.getContentFormat());
		Label cell12= new Label(i++, column,getCustomerFapiaoFlagName(customer.getCustomerFapiaoFlag()), JXLTool.getContentFormat());
/*		Label cell13= new Label(i++, column,customer.getGhoClass(), JXLTool.getContentFormat());
*/		Label cell14 = new Label(i++, column, customer.getCustomerID(), JXLTool.getContentFormat());
		Label cell15 = new Label(i++, column, DataUtil.getdataSourceCh(customer.getDatasSource()), JXLTool.getContentFormat());
		Label cell16 = new Label(i++, column, customer.getLinkName(), JXLTool.getContentFormat());
		Label cell17= new Label(i++, column, customer.getLinkAddress(), JXLTool.getContentFormat());
		Label cell18= new Label(i++, column, customer.getLinkPhone(), JXLTool.getContentFormat());
		Label cell19= new Label(i++, column, customer.getCustomerZipCode(), JXLTool.getContentFormat());

		
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
/*		ws.addCell(cell13);
*/		ws.addCell(cell14);
		ws.addCell(cell15);
		ws.addCell(cell16);
		ws.addCell(cell17);
		ws.addCell(cell18);
		ws.addCell(cell19);
	}
	

/*	*//**
	* <p>方法名称: importCustomer|描述: 导入客户信息</p>
	* @return 
	*/
	public String importCustomer(){
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("attachmentCustomer");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				String result= doImportFile(files[0]);
				files = null;
				return result;
			} catch (Exception e) {
				log.error(e);
				setResultMessages("上传文件失败" + e.getMessage());
				//response.sendRedirect(URLEncoder.encode(url,"GBK"));//
				return ERROR;
			}
		} else {
			setResultMessages("上传文件失败");		
			return ERROR;
		}
	}
	

		private String doImportFile(File file) throws Exception {
			List<Dictionary> headList= userInterfaceConfigService.getDictionarys(
					"CUSTOMER_INFO_IMP", "");
			Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
			String result="";
			Map<String, String> mapBusi=new HashMap<String,String>();
			String resultBusi="";
			if (hs != null) {
				String[][] sheet = (String[][]) hs.get("0");
				// 获取表头列表
				String[] heads = sheet[0];
				// 创建数据List对象
				List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
				String batchNo=StringUtil.getBatchNo();
				String startDate=StringUtil.getCurrentDate();
				String failedLog="";
				for (int i = 1; i < sheet.length; i++) {
					Map<String, String> map = new HashMap<String, String>();
					String[] row = sheet[i];
					// 值与列对应
					map=CheckUtil.CreatMap(heads, headList, row);
					result=checkformat(map, i, sheet, result);
					dataList.add(map);
				}
				if(result.length()>0){
					setResultMessages(result);
					return ERROR;
				}else{
					int j=0;
					for(int i=0;i<dataList.size();i++){
						j=mapBusi.get("j")==null?0:Integer.valueOf(mapBusi.get("j"));
						Map<String,String> map=dataList.get(i);
						map.put("customerID", StringUtil.getUUID());
						map.put("batchNo", batchNo);
						map.put("datasSource", dataSourch_ch1);
						map.put("dataOperationLabel", dataOperation_label_ch1);
						customerService.saveCustomerData(map);
						mapBusi=checkCustomerBusi(map, mapBusi, sheet, i, j);
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
						log.setSys("客户信息管理");
						logEmpService.saveLogEmp(log);
					}
					setResultMessages(resultBusi);
					return SUCCESS;
				}
				
					
			} else {
				setResultMessages("导入文件为空，请选择导入文件。");
				return ERROR;
			}
		}
		

		/**
		 * @param map
		 * @param i
		 * @param sheet
		 * @param result
		 * @return 验证格式
		 */
		private String checkformat(Map<String,String> map,int i,String[][]sheet,String result){
			List<String> list=new ArrayList<String>();
			list.add(map.get("customerCName"));
			list.add(map.get("customerType"));
			list.add(map.get("taxPayerType"));
			list.add(map.get("fapiaoType"));
			list.add(map.get("customerFapiaoFlag"));
			Map<String, Boolean> mapCheck=new HashMap<String, Boolean>();
			mapCheck.put("checkNull",CheckUtil.checkNotNull(list));
			result=CheckUtil.checkData(mapCheck, i, result, sheet.length);
			return result;
			
		}/* 
		验证不通过 
		*/
		public Map<String,String> checkCustomerBusi(Map<String,String> map,Map<String,String> mapcheckBusi,String[][]sheet,int i,int j){
			String nullRow=StringUtil.isNotEmpty(mapcheckBusi.get("nullRow"))?mapcheckBusi.get("nullRow"):"";
			String failedLog=StringUtil.isNotEmpty(mapcheckBusi.get("failedLog"))?mapcheckBusi.get("failedLog"):"";
			String taxNoTempNum=StringUtil.isNotEmpty(mapcheckBusi.get("taxNoTempNum"))?mapcheckBusi.get("taxNoTempNum"):"";
			String taxNoNum=StringUtil.isNotEmpty(mapcheckBusi.get("taxNoNum"))?mapcheckBusi.get("taxNoNum"):"";
			String resultBusi=StringUtil.isNotEmpty(mapcheckBusi.get("resultBusi"))?mapcheckBusi.get("resultBusi"):"";
			
			if(StringUtil.isEmpty(map.get("customerTaxno"))&&map.get("taxPayerType").equals("G")){
				nullRow+=i+1+",";
				failedLog+=i+1+",";
				j++;
			}else if(StringUtil.isNotEmpty(map.get("customerTaxno"))){
				customer= customerService.findCustomer("", map.get("customerTaxno"), "", "", "");
				String  result=customerService.findCustomerTempByTaxNo(map.get("customerTaxno"));
				if(StringUtil.isNotEmpty(result)){
				taxNoTempNum+=i+1+",";
				failedLog+=i+1+",";
				j++;
				}else if(null!= customer&&StringUtil.isEmpty(result)){
					customerService.deleteCustomerByTaxNo(map.get("customerTaxno"));
					customerService.saveCustomerInfo(map);
				
					
				}
				if(map.get("customerTaxno").length()>18||map.get("customerTaxno").length()<15){
					taxNoNum+=i+1+",";
					failedLog+=i+1+",";
					j++;
				}
				
			}else{
				customerService.saveCustomerInfo(map);

			}
			nullRow=nullRow.length()>0&&i==sheet.length-2?"第"+nullRow.substring(0,nullRow.length()-1)+"行纳税人识别号为空；":nullRow;
			taxNoTempNum=taxNoTempNum.length()>0&&i==sheet.length-2?"第"+taxNoTempNum.substring(0,taxNoTempNum.length()-1)+"行纳税人识别号审核中；":taxNoTempNum;
			taxNoNum=taxNoNum.length()>0&&i==sheet.length-2?"第"+taxNoNum.substring(0,taxNoNum.length()-1)+"行纳税人识别号格式有误":taxNoNum;
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
	 * @return 客户审核界面
	 */
	public String listCustomerAudit(){
		
		/*客户编号	1	客户名称	2	客户纳税人识别号	3	纳税人类型	4
		GHO类	5	数据来源	6	数据操作标志	7	数据审核状态	8
									*/
		String customerCode = request.getParameter("customerCode");
		String customerCName = request.getParameter("customerCName");
		String taxpayerType = request.getParameter("taxpayerType");
		String vatInvoice = request.getParameter("vatInvoice");
		String taxNo = request.getParameter("taxNo");
	
		customer.setCustomerID(customerCode);
		customer.setCustomerCName(customerCName);
		customer.setTaxPayerType(taxpayerType);
		customer.setVatInvoice(vatInvoice);
		customer.setCustomerTaxno(taxNo);
		customer.setDatasSource(request.getParameter("datasSource"));
		customer.setGhoClass(request.getParameter("ghoClass"));
		customer.setDataOperationLabel(request.getParameter("dataOperationLabel"));
		customer.setDataAuditStatus(request.getParameter("dataAuditStatus"));
		customerService.findCustomerAudit(customer,paginationList);
		dataOperationLabelList=DataUtil.getdataOperationLabelList();
		dataAuditstatusList=DataUtil.getdataAuditstatusList();
		dataSourceList=DataUtil.getdataSourceList();
		return SUCCESS;
	}
	/**
	 * @return 查看审核详情
	 */
	public String viewCustomerAuditDetail(){
		String customerId = request.getParameter("customerId");
		customer = customerService.findCustomerAuditById(customerId);
		return SUCCESS;
	}
	/**
	 * 客户审核alert(addAarray);
	alert(updateAarray);
	alert(deleteAarray);
	 */
	public String CustomerAudit(){
		String auditsudits=request.getParameter("auditsudits");
		String customerIdList=request.getParameter("deleteAarray");
		String addAarray=request.getParameter("addAarray");
		String updateAarray=request.getParameter("updateAarray");
		
		List customeIdAuditList=getList(customerIdList);
		List addList=getList(addAarray);
		List updateList=getList(updateAarray);
		String result="";
		if(auditsudits.equals("1")){
			if(customeIdAuditList!=null){
			customerService.deleteAll(customeIdAuditList);
			
			}
			if(addList!=null){
				customerService.copycustomerTempToCustomer(addList);

			}if(updateList!=null){
				
				customerService.updateCustomerAtferAudit(updateList);

			}
			result="【审核通过】提交成功";
		}else{
			result="【审核不通过】提交成功";
		}
		if(customeIdAuditList!=null){
			customerService.updatecustomerAuditstatus(customeIdAuditList, auditsudits);
			customerService.deleteCustomerTempAfterAudit(customeIdAuditList);
			}
			if(addList!=null){
				customerService.updatecustomerAuditstatus(addList, auditsudits);
				customerService.deleteCustomerTempAfterAudit(addList);
			}if(updateList!=null){
				

				customerService.updatecustomerAuditstatus(updateList, auditsudits);
				customerService.deleteCustomerTempAfterAudit(updateList);
			}
			setResultMessages(result);

		return SUCCESS;
	}
	public void CustomerListAudit() throws Exception{
		String auditsudits=request.getParameter("auditsudits");
		String customerIdList=request.getParameter("deleteAarray");
		String addAarray=request.getParameter("addAarray");
		String updateAarray=request.getParameter("updateAarray");
		
		List customeIdAuditList=getList(customerIdList);
		List addList=getList(addAarray);
		List updateList=getList(updateAarray);
		String result="";
		if(auditsudits.equals("1")){
			if(customeIdAuditList!=null){
				customerService.deleteAll(customeIdAuditList);
				
			}
			if(addList!=null){
				customerService.copycustomerTempToCustomer(addList);
				
			}if(updateList!=null){
				
				customerService.updateCustomerAtferAudit(updateList);
				
			}
			result="【审核通过】提交成功";
		}else{
			result="【审核不通过】提交成功";
		}
		if(customeIdAuditList!=null){
			customerService.updatecustomerAuditstatus(customeIdAuditList, auditsudits);
			customerService.deleteCustomerTempAfterAudit(customeIdAuditList);
		}
		if(addList!=null){
			customerService.updatecustomerAuditstatus(addList, auditsudits);
			customerService.deleteCustomerTempAfterAudit(addList);
		}if(updateList!=null){
			
			
			customerService.updatecustomerAuditstatus(updateList, auditsudits);
			customerService.deleteCustomerTempAfterAudit(updateList);
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
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public String[] getCustomerIdList() {
		return customerIdList;
	}

	public void setCustomerIdList(String[] customerIdList) {
		this.customerIdList = customerIdList;
	}
	
	public File getAttachmentCustomer() {
		return attachmentCustomer;
	}

	public void setAttachmentCustomer(File attachmentCustomer) {
		this.attachmentCustomer = attachmentCustomer;
	}

	public String getAttachmentCustomerFileName() {
		return attachmentCustomerFileName;
	}
	
	 /**
	 * @param filename
	 * @return  获取扩展名
	 */
	public static String getExtensionName(String filename) {  

	        if ((filename != null) && (filename.length() > 0)) {  

	            int dot = filename.lastIndexOf('.');  

	            if ((dot >-1) && (dot < (filename.length() - 1))) {  

	                return filename.substring(dot + 1);  

	            }  

	        }  

	        return filename;  

	    } 
	
	public void setAttachmentCustomerFileName(String attachmentCustomerFileName) {
		this.attachmentCustomerFileName = attachmentCustomerFileName;
	}

	public String getAttachmentCustomerType() {
		return attachmentCustomerType;
	}

	public void setAttachmentCustomerType(String attachmentCustomerType) {
		this.attachmentCustomerType = attachmentCustomerType;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerOffice() {
		return customerOffice;
	}

	public void setCustomerOffice(String customerOffice) {
		this.customerOffice = customerOffice;
	}

	public String getCustomerCName() {
		return customerCName;
	}

	public void setCustomerCName(String customerCName) {
		this.customerCName = customerCName;
	}

	public String getTaxpayerType() {
		return taxpayerType;
	}

	public void setTaxpayerType(String taxpayerType) {
		this.taxpayerType = taxpayerType;
	}

	public String getVatInvoice() {
		return vatInvoice;
	}

	public void setVatInvoice(String vatInvoice) {
		this.vatInvoice = vatInvoice;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	
	public List getCountrys() {
		return countrys;
	}

	public void setCountrys(List countrys) {
		this.countrys = countrys;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * get taxpayer type name by type
	 * */
	public String getTaxpayerTypeName(String TaxpayerType){
		if("".equals(TaxpayerType) || null == TaxpayerType){
			return "";
		}else if("G".equals(TaxpayerType)){
			return "一般纳税人";
		}else if("S".equals(TaxpayerType)){
			return "小规模纳税人";
		}else{
			return "其他";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getFapiaoTypeName(String getFapiaoType){
		if("".equals(getFapiaoType) || null == getFapiaoType){
			return "";
		}else if("0".equals(getFapiaoType)){
			return "增值税专用发票";
		}else if("1".equals(getFapiaoType)){
			return "增值税普通发票";
		}else{
			return "其他";
		}
	}//发票类型
	  //0-增值税专用发票
	  //1-增值税普通发票
	public String getFapiaoTypeCode(String getFapiaoType){
		if("".equals(getFapiaoType) || null == getFapiaoType){
			return "";
		}else if("增值税专用发票".equals(getFapiaoType)){
			return "0";
		}else if("增值税普通发票".equals(getFapiaoType)){
			return "1";
		}else{
			return "";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getCustomerTypeName(String CustomerType){
		if("".equals(CustomerType) || null == CustomerType){
			return "";
		}else if("I".equals(CustomerType)){
			return "私人客户";
  
		}else if("C".equals(CustomerType)){
			return "公司客户";
		}else{
			return "其他";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getCustomerFapiaoFlagName(String CustomerFapiaoFlag){
		if("".equals(CustomerFapiaoFlag) || null == CustomerFapiaoFlag){
			return "";
		}else if("A".equals(CustomerFapiaoFlag)){
			return "自动打印";
			
		}else if("M".equals(CustomerFapiaoFlag)){
			return "手动打印";
		}else if("N".equals(CustomerFapiaoFlag)){
			return "永不打印";
		}else{
			return "其他";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getTaxpayerTypeCode(String TaxpayerType){
		if("".equals(TaxpayerType) || null == TaxpayerType){
			return "";
		}else if("一般纳税人".equals(TaxpayerType)){
			return "G";
		}else if("小规模纳税人".equals(TaxpayerType)){
			return "S";
		}else if("个体模纳税人".equals(TaxpayerType)){
			return "I";
		}else if("其他".equals(TaxpayerType)){
			return "O";
		}else{
			return "";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getCustomerTypeCode(String CustomerType){
		if("".equals(CustomerType) || null == CustomerType){
			return "";
		}else if("私人客户".equals(CustomerType)){
			return "I";
			
		}else if("公司客户".equals(CustomerType)){
			return "C";
		}else{
			return "";
		}
	}
	/**
	 * get taxpayer type name by type
	 * */
	public String getCustomerFapiaoFlagCode(String CustomerFapiaoFlag){
		if("".equals(CustomerFapiaoFlag) || null == CustomerFapiaoFlag){
			return "";
		}else if("自动打印".equals(CustomerFapiaoFlag)){
			return "A";
			
		}else if("手动打印".equals(CustomerFapiaoFlag)){
			return "M";
		}else if("永不打印".equals(CustomerFapiaoFlag)){
			return "N";
		}else{
			return "";
		}
	}

	public String getCustomerNationality() {
		return customerNationality;
	}

	public void setCustomerNationality(String customerNationality) {
		this.customerNationality = customerNationality;
	}

	public String getCustomerFapiaoFlag() {
		return customerFapiaoFlag;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerFapiaoFlag(String customerFapiaoFlag) {
		this.customerFapiaoFlag = customerFapiaoFlag;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public List getDataSourceList() {
		return dataSourceList;
	}

	public void setDataSourceList(List dataSourceList) {
		this.dataSourceList = dataSourceList;
	}

	public String getGhoClass() {
		return ghoClass;
	}

	public void setGhoClass(String ghoClass) {
		this.ghoClass = ghoClass;
	}

	public String getDatasSource() {
		return datasSource;
	}

	public void setDatasSource(String datasSource) {
		this.datasSource = datasSource;
	}

	public List getDataOperationLabelList() {
		return dataOperationLabelList;
	}

	public void setDataOperationLabelList(List dataOperationLabelList) {
		this.dataOperationLabelList = dataOperationLabelList;
	}

	public List getDataAuditstatusList() {
		return dataAuditstatusList;
	}

	public void setDataAuditstatusList(List dataAuditstatusList) {
		this.dataAuditstatusList = dataAuditstatusList;
	}

	public String getDataOperationLabel() {
		return dataOperationLabel;
	}

	public void setDataOperationLabel(String dataOperationLabel) {
		this.dataOperationLabel = dataOperationLabel;
	}

	public String getDataAuditStatus() {
		return dataAuditStatus;
	}

	public void setDataAuditStatus(String dataAuditStatus) {
		this.dataAuditStatus = dataAuditStatus;
	}

	public LogEmpService getLogEmpService() {
		return logEmpService;
	}

	public void setLogEmpService(LogEmpService logEmpService) {
		this.logEmpService = logEmpService;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getCustomerZipCode() {
		return customerZipCode;
	}

	public void setCustomerZipCode(String customerZipCode) {
		this.customerZipCode = customerZipCode;
	}
	
	
}
