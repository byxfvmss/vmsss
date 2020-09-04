package com.cjit.vms.trans.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cjit.vms.trans.service.VmsCommonService;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.ExcelUtil;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.EmsInfo;
import com.cjit.vms.trans.util.CheckUtil;
import com.cjit.vms.trans.util.DataUtil;


public class BillEmsMessageAction extends DataDealAction{

	private static final long serialVersionUID = 1L;
	private String message;
	private BillInfo billInfo = new BillInfo();
	private List billInfoList;
	private String flag;
	private EmsInfo emsInfo = new EmsInfo();
	private String submitFlag;
	private String[] selectBillIds;
	private String emsId;//emsId为0时新增，为1时修改
	private CustomerService customerService;
	private Map chanNelList;
	private Map billFreqlList;
	private Map feeTypList;
	private Map dsouRceList;
	private VmsCommonService vmsCommonService;
	/**
	 * 进入EMS快递信息界面
	 * 
	 * @return String
	 */
	public String getBillEmsMessage(){
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}

		chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
		feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
		dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");

		try {
			// 获取EMS状态种类
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			billInfo.setBillBeginDate(this.getBillBeginDate());
			billInfo.setBillEndDate(this.getBillEndDate());
			billInfo.setUserId(currentUser.getId());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billInfo.setLstAuthInstId(lstAuthInstId);
			billInfoList = billInfoService.findBillInfoForEmsList(billInfo, paginationList);
			this.request.setAttribute("billInfoList", billInfoList);
			this.request.getSession().setAttribute("billBeginDate",
					billInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate",
					billInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(),
					DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);
			setResultMessages("aa");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillEmsMessageAction-getBillEmsMessage", e);
		}
		return ERROR;
	}
	
	/**
	 * 进入EMS快递信息编辑界面
	 * 
	 * @return String
	 */
	public String editbillEmsMessage() {
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			String billId = request.getParameter("billId");
			BillInfo billInfos = new BillInfo();
			billInfos.setBillId(billId);
			billInfos.setLstAuthInstId(lstAuthInstId);
			billInfo = billInfoService.findBillInfoForEms(billInfos);
			String billNo = request.getParameter("billNo");
//			emsInfo = emsInfoService.findEmsInfo(billNo, emsStatusList);
			emsInfo = emsInfoService.findEmsInfo(billNo);
			if(emsInfo == null){
				emsId = "0";
			}else{
				emsId = "1";
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillEmsMessageAction-editbillEmsMessage", e);
		}
		return ERROR;
	}
	
	/**
	 * 进入EMS快递信息添加/修改界面
	 * 
	 * @return String
	 */
	public String addbillEmsMessage() {
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		try {
			emsId = "0";
			List billInfoList = new ArrayList();
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				// 循环查询选中票据信息
				for (int i = 0; i < this.selectBillIds.length; i++) {
					BillInfo billInfo = new BillInfo();
					BillInfo billInfos = new BillInfo();
					billInfo.setLstAuthInstId(lstAuthInstId);
					billInfo.setBillId(selectBillIds[i]);
					billInfos = billInfoService.findBillInfoForEms(billInfo);
					billInfoList.add(billInfos);
				}
			}
			this.request.setAttribute("billInfoList", billInfoList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillEmsMessageAction-addbillEmsMessage", e);
		}
		return ERROR;
	}
	
	/**
	 * 查看EMS快递信息界面
	 * 
	 * @return String
	 */
	public String viewbillEmsMessage(){
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			String billId = request.getParameter("billId");
			BillInfo in = new BillInfo();
			in.setBillId(billId);
			in.setLstAuthInstId(lstAuthInstId);
			billInfo = billInfoService.findBillInfoForEms(in);
			BillItemInfo billItem = new BillItemInfo();
			billItem.setBillId(billId);
			List itemList = billInfoService.findBillItemInfoList(billItem);
			List billItemList = new ArrayList();
			if (CollectionUtil.isNotEmpty(itemList)) {
				// 过滤掉打折行信息
				for (Iterator t = itemList.iterator(); t.hasNext();) {
					BillItemInfo item = (BillItemInfo) t.next();
					if ("0".equals(item.getRowNature())) {
						// 正常行
						billItemList.add(item);
					} else if ("1".equals(item.getRowNature())) {
						// 打折行
						continue;
					} else if ("2".equals(item.getRowNature())) {
						// 被打折行
						for (Iterator t1 = itemList.iterator(); t1.hasNext();) {
							BillItemInfo item1 = (BillItemInfo) t1.next();
							// 计算打折后金额和税金
							if ("1".equals(item1.getRowNature())
									&& item.getBillItemId().equals(
											item1.getDisItemId())) {
								item.setDiscountRate(item1.getDiscountRate());
								item.setAmt(item.getAmt().subtract(
										item1.getAmt()));
								item.setTaxAmt(item.getTaxAmt().subtract(
										item1.getTaxAmt()));
								billItemList.add(item);
								break;
							} else {
								continue;
							}
						}
					}
				}
			}
			this.request.setAttribute("billItemList", billItemList);
			
			//获取EMS快递信息
			String billNo = request.getParameter("billNo");
//			emsInfo = emsInfoService.findEmsInfo(billNo, emsStatusList);
			emsInfo = emsInfoService.findEmsInfo(billNo);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillEmsMessageAction-viewbillEmsMessage", e);
		}
		return ERROR;
	}
	
	/**
	 * 保存EMS快递信息
	 * 
	 * @return String
	 */
	public String saveEmsMessage(){
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);

			flag = request.getParameter("flag");
			if (emsId.equals("0")) {
				
				if (flag.equals("add")) {
					String[] billIds = request.getParameter("billIds").split(",");
					// 循环查询选中票据信息
					String result="";
					Set<String> set=new HashSet<String>();
					for (int i = 0; i < billIds.length; i++) {
						BillInfo billInfos = new BillInfo();
						billInfos.setLstAuthInstId(lstAuthInstId);
						billInfos.setBillId(billIds[i].trim());
						billInfo = billInfoService.findBillInfoForEms(billInfos);
						emsInfo.setBillDate(billInfo.getBillDate());
						emsInfo.setBillCode(billInfo.getBillCode());
						emsInfo.setBillNo(billInfo.getBillNo());
						emsInfo.setCustomerName(billInfo.getCustomerName());
						emsInfo.setCustomerTaxno(billInfo.getCustomerTaxno());
						emsInfoService.saveEmsInfo(emsInfo, false);
						set=updateCustomer(billInfo,set);
					}
					result=CheckUtil.checkCId(set, "此纳税认识别号在客户信息表中");
					if(result.length()>0){
						setResultMessages(result);
					}
				}
				if(flag.equals("edit")){
					emsInfoService.saveEmsInfo(emsInfo, false);
					updateCustomer(emsInfo);
				}
			}else{
				emsInfoService.saveEmsInfo(emsInfo, true);
				updateCustomer(emsInfo);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillEmsMessageAction-saveEmsMessage", e);
		}
		return ERROR;
	}//emsInfo.addressee  emsInfo.addresseePhone emsInfo.addresseeZipcode emsInfo.customerTaxno
	public Set<String> updateCustomer(BillInfo billInfo,Set<String>set){
		Customer customer=customerService.findCustomerByTaxNo(billInfo.getCustomerTaxno());
		if(StringUtil.isEmpty(customer.getCustomerTaxno())){
			set.add(customer.getCustomerTaxno());
			//setResultMessages("此纳税识别号客户信息表中不存在");
		}else{
			customer.setLinkName(billInfo.getAddressee());
			customer.setLinkAddress(billInfo.getAddresseeAddress());
			customer.setLinkPhone(billInfo.getAddressandphone());
			customer.setCustomerZipCode(billInfo.getAddresseeZipcode());
			customer.setCustomerTaxno(billInfo.getCustomerTaxno());
			customerService.updateCutomerbyTaxNo(customer);
			
		}
		return set;
	}
	public void updateCustomer(EmsInfo emsInfo){
		Customer customer=customerService.findCustomerByTaxNo(emsInfo.getCustomerTaxno());
		if(StringUtil.isEmpty(customer.getCustomerTaxno())){
			setResultMessages("纳税识别号"+emsInfo.getCustomerTaxno()+"客户信息表中不存在");
		}else{
			customer.setLinkName(emsInfo.getAddressee());
			customer.setLinkAddress(emsInfo.getAddresseeAddress());
			customer.setLinkPhone(emsInfo.getAddresseePhone());
			customer.setCustomerZipCode(emsInfo.getAddresseeZipcode());
			customer.setCustomerTaxno(emsInfo.getCustomerTaxno());
			customerService.updateCutomerbyTaxNo(customer);
			
		}
		
	}
	
	public String deleteEmsMessage(){
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				// 循环查询选中票据信息
				for (int i = 0; i < this.selectBillIds.length; i++) {
					BillInfo in = new BillInfo();
					in.setBillId(selectBillIds[i]);
					in.setLstAuthInstId(lstAuthInstId);
					billInfo = billInfoService.findBillInfoForEms(in);
					emsInfoService.deleteEmsInfo(billInfo.getBillNo());
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillEmsMessageAction-saveEmsMessage", e);
			return ERROR;
		}
	}
	
	public String editEmsStatus(){
		if (!sessionInit(true)) {
			this.setResultMessages("用户失效");
			return ERROR;
		}
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);

			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				// 循环查询选中票据信息
				for (int i = 0; i < this.selectBillIds.length; i++) {
					BillInfo in = new BillInfo();
					in.setLstAuthInstId(lstAuthInstId);
					in.setBillId(selectBillIds[i]);
					billInfo = billInfoService.findBillInfoForEms(in);
					emsInfoService.updateEmsStatus(billInfo.getBillNo());
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillEmsMessageAction-editEmsStatus", e);
			return ERROR;
		}
	}
	
	/**
	 * 导出Excel
	 * 
	 * @return String
	 */
	public void exportEmsMsgtoExcel() throws Exception {
		try {
			User currentUser = this.getCurrentUser();
			billInfo.setBillBeginDate(this.getBillBeginDate());
			billInfo.setBillEndDate(this.getBillEndDate());
			billInfo.setUserId(currentUser.getId());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billInfo.setLstAuthInstId(lstAuthInstId);
			billInfoList = billInfoService.findEmsInfoForExport(billInfo);

			this.request.getSession().setAttribute("billBeginDate",
					billInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate",
					billInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(),
					DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);
			
			
			StringBuffer fileName = new StringBuffer("EMS快递信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, billInfoList);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	
	/**
	 * 导入Excel
	 * 
	 * @return String
	 */
	public String importEmsMsgtoExcel() throws Exception {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("attachmentCustomer");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				String result = doImportFile(files[0]);
				files = null;
				billInfo = new BillInfo();
				return result;
			} catch (Exception e) {
				log.error(e);
				this.setResultMessages("导入文件失败:" + e.getMessage());
				return ERROR;
			}
		} else {
			this.setResultMessages("请选择上传文件!");
			return ERROR;
		}
	}
	
	/**
	 * <p>
	 * 方法名称: setResultMessages|描述:设置session中存储的处理信息结果
	 * </p>
	 * 
	 * @param resultMessages
	 *            处理信息结果
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public void setResultMessages(String resultMessages){
		
		log.info(request.getHeader("user-agent"));
		log.info(request.getHeader(request.getLocale().toString()));
		resultMessages = chr2Unicode(resultMessages);
		log.info(resultMessages);
		try {
			this.message=java.net.URLEncoder.encode(resultMessages,"utf-8");
			request.setAttribute("message", this.message);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		};
	}
	
	/**
	 * 中文转unicode字符(英文环境)
	 * 
	 * @param str
	 * @return
	 */
	public String chr2Unicode(String str) {
        String[] a={"","000","00","0",""};
		String result = "";
		if (StringUtils.isNotEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {
				int chr = (char) str.charAt(i);
				String s=Integer.toHexString(chr);
				result += "\\u" +a[s.length()]+s;
			}
		}
		return result;
	}
	private String doImportFile(File file) throws Exception {
		Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
		if (hs != null) {
			// 获取excel第一个sheet页
			String[][] sheet = (String[][]) hs.get("0");
			// 从第二行开始获取每行数据
			StringBuffer taxNoSB = new StringBuffer();
			StringBuffer nullRow = new StringBuffer("");
			String taxNo = "";
			for (int i = 1; i < sheet.length; i++) {
				String[] row = sheet[i];
				taxNo="";
				taxNo = null==row[1] ? "" : row[1];
				if(!"".equals(taxNo)){
					EmsInfo emsInfo=new EmsInfo();
					emsInfo.setBillDate(null==row[1]?"":row[1]);
					emsInfo.setBillCode(null==row[2]?"":row[2]);
					emsInfo.setBillNo(null==row[3]?"":row[3]);
					emsInfo.setCustomerName(null==row[4]?"":row[4]);
					emsInfo.setAddressee(null==row[5]?"":row[5]);
					emsInfo.setAddresseePhone(null==row[6]?"":row[6]);
					emsInfo.setCustomerEMail(null==row[7]?"":row[7]);
					emsInfo.setAddresseeAddress(null==row[8]?"":row[8]);
					emsInfo.setAddresseeAddressdetail(null==row[9]?"":row[9]);
					emsInfo.setFedexExpress(null==row[10]?"":row[10]);
					emsInfo.setEmsNo(null==row[11]?"":row[11]);
					emsInfo.setEmsStatus(null==row[12]?"":getEmsStatus(row[12]));
					emsInfo.setSender(null==row[13]?"":row[13]);
					if(null==emsInfoService.findEmsInfo(emsInfo.getBillNo())){
						//billInfoService.saveBillInfo(billInfo, false);
						emsInfoService.saveEmsInfo(emsInfo, false);
					}else{
						//billInfoService.saveBillInfo(billInfo, true);
						emsInfoService.saveEmsInfo(emsInfo, true);
					}
				}
			}
			if(0 == taxNoSB.length() && 0 == nullRow.length()){
				this.setResultMessages("导入成功.");
				return SUCCESS;
			}else if(0 < taxNoSB.length() && 0 == nullRow.length()){
				this.setResultMessages( "导入文件失败:" + taxNoSB.toString() );
				return ERROR;
			}else if(0 == taxNoSB.length() && 0 < nullRow.length()){
				this.setResultMessages( "导入文件失败:" + taxNoSB.toString() );
				return ERROR;
			}else{
				this.setResultMessages( "导入文件失败:" + taxNoSB.toString() );
				return ERROR;
			}
		} else {
			this.setResultMessages("导入文件失败.");
			return ERROR;
		}
	}
	public void writeToExcel(OutputStream os, List content) throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		
		WritableSheet ws = null;
		ws = wb.createSheet("EMS快递信息表", 0);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "开票日期", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "发票代码", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "发票号码", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "收件人", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "收件人电话", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "收件人邮箱", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "收件地址", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "详细收件地址", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "快递公司", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "快递单号", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "快递状态", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "寄件人", JXLTool.getHeader());
		
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
		for (int i = 0; i < 14; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			BillInfo o = (BillInfo) content.get(i);
			int column = count++;
			Map map = new HashMap();
			map.put("Id", Integer.valueOf(i+1));
			map.put("billDate",  o.getBillDate());
			map.put("billCode", o.getBillCode());
			map.put("billNo", o.getBillNo());
			map.put("customerName", o.getCustomerName());
			map.put("addressee", o.getAddressee());
			map.put("addresseePhone", o.getAddresseePhone());
			map.put("customerEMail", o.getCustomerEMail());
			map.put("addresseeAddress", o.getAddresseeAddress());
			map.put("addresseeAddressdetail", o.getAddresseeAddressdetail());
			map.put("fedexExpress", o.getFedexExpress());
			map.put("emsNo", o.getEmsNo());
			map.put("emsStatus", DataUtil.getEmsStatusCH(o.getEmsStatus()));
			map.put("sender", o.getSender());
			
			setWritableSheet(ws, map, column);
		}
		wb.write();
		wb.close();
	}
	
	private void setWritableSheet(WritableSheet ws, Map o, int column) throws WriteException {
		
		WritableCellFormat tempCellFormat = getBodyCellStyle();
		tempCellFormat.setAlignment(Alignment.RIGHT);
		//序号
		Label cell1 = new Label(0, column, o.get("Id").toString(), tempCellFormat);
		
		//开票日期billDate
		Label cell2 = new Label(1, column, o.get("billDate") == null
				|| o.get("billDate").equals("") ? "" : o.get("billDate")
				.toString(), tempCellFormat);
		
		//发票代码billCode
		Label cell3 = new Label(2, column, o.get("billCode") == null
				|| o.get("billCode").equals("") ? "" : o.get("billCode")
				.toString(), tempCellFormat);
		
		//发票号码billNo
		Label cell4 = new Label(3, column, o.get("billNo") == null
				|| o.get("billNo").equals("") ? "" : o.get("billNo")
				.toString(), tempCellFormat);
		
		//客户纳税人名称customerName
		Label cell5 = new Label(4, column, o.get("customerName") == null
				|| o.get("customerName").equals("") ? "" : o.get(
				"customerName").toString(), tempCellFormat);
		
		//收件人addressee
		Label cell6 = new Label(5, column, o.get("addressee") == null
				|| o.get("addressee").equals("") ? "" : o.get("addressee")
				.toString(), tempCellFormat);
		
		//收件人电话addresseePhone
		Label cell7 = new Label(6, column, o.get("addresseePhone") == null
				|| o.get("addresseePhone").equals("") ? "" : o.get("addresseePhone")
				.toString(), tempCellFormat);
		
		//收件人邮箱customerEMail
		Label cell8 = new Label(7, column, o.get("customerEMail") == null
				|| o.get("customerEMail").equals("") ? "" : o.get("customerEMail")
				.toString(), tempCellFormat);
		
		//收件地址addresseeAddress
		Label cell9 = new Label(8, column,
				o.get("addresseeAddress") == null || o.get("addresseeAddress").equals("") ? "" : o
						.get("addresseeAddress").toString(), tempCellFormat);
		//详细收件地址addresseeAddressdetail
		Label cell10 = new Label(9, column,
				o.get("addresseeAddressdetail") == null || o.get("addresseeAddressdetail").equals("") ? "" : o
						.get("addresseeAddressdetail").toString(), tempCellFormat);
		
		//快递公司fedexExpress
		Label cell11 = new Label(10, column,
				o.get("fedexExpress") == null || o.get("fedexExpress").equals("") ? "" : o
						.get("fedexExpress").toString(), tempCellFormat);
		
		//快递单号emsNo
		Label cell12 = new Label(11, column, o.get("emsNo") == null
				|| o.get("emsNo").equals("") ? "" : o.get("emsNo")
				.toString(), tempCellFormat);
		
		//快递状态emsStatus
//		String emsStatus = "";
//		if(o.get("emsStatus") == null || o.get("emsStatus").equals("")){
//			emsStatus = "";
//		}else{
//			if(o.get("emsStatus").equals("1")){
//				emsStatus = "打印已快递";
//			}else if(o.get("emsStatus").equals("2")){
//				emsStatus = "打印未快递";
//			}else if(o.get("emsStatus").equals("3")){
//				emsStatus = "已签收";
//			}else{
//				emsStatus = "打印未快递";
//			}
//		}
		Label cell13 = new Label(12, column, o.get("emsStatus") + "", tempCellFormat);
		
		//寄件人sender
		Label cell14 = new Label(13, column, o.get("sender") == null
				|| o.get("sender").equals("") ? "" : o.get("sender")
				.toString(), tempCellFormat);
		
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
	}
	
	private String getEmsStatus(String emsStatusName)
	{
		if(emsStatusName.equals("打印已快递"))
		{
			return "1";
		}
		else if(emsStatusName.equals("打印未快递"))
		{
			return "2";
		} else if(emsStatusName.equals("已签收"))
		{
			return "3";
		}
		else return "";
	}
	 /** 
     * 表头单元格样式的设定 
     */  
    public WritableCellFormat getBodyCellStyle(){
          
        /* 
         * WritableFont.createFont("宋体")：设置字体为宋体 
         * 10：设置字体大小 
         * WritableFont.NO_BOLD:设置字体非加粗（BOLD：加粗     NO_BOLD：不加粗） 
         * false：设置非斜体 
         * UnderlineStyle.NO_UNDERLINE：没有下划线 
         */  
        WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                                             10,   
                                             WritableFont.NO_BOLD,   
                                             false,  
                                             UnderlineStyle.NO_UNDERLINE);  
          
        WritableCellFormat bodyFormat = new WritableCellFormat(font);  
        try {  
            //设置单元格背景色：表体为白色  
            bodyFormat.setBackground(Colour.WHITE);  
            //设置表头表格边框样式  
            //整个表格线为细线、黑色  
            bodyFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);  
              
        } catch (WriteException e) {  
            System.out.println("表体单元格样式设置失败！");  
        }  
        return bodyFormat;  
    }
	
	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public EmsInfo getEmsInfo() {
		return emsInfo;
	}

	public void setEmsInfo(EmsInfo emsInfo) {
		this.emsInfo = emsInfo;
	}

	public String getSubmitFlag() {
		return submitFlag;
	}

	public void setSubmitFlag(String submitFlag) {
		this.submitFlag = submitFlag;
	}

	public String[] getSelectBillIds() {
		return selectBillIds;
	}

	public void setSelectBillIds(String[] selectBillIds) {
		this.selectBillIds = selectBillIds;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getEmsId() {
		return emsId;
	}

	public void setEmsId(String emsId) {
		this.emsId = emsId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List getBillInfoList() {
		return billInfoList;
	}

	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
	}

	public Map getChanNelList() {
		return chanNelList;
	}

	public void setChanNelList(Map chanNelList) {
		this.chanNelList = chanNelList;
	}

	public Map getBillFreqlList() {
		return billFreqlList;
	}

	public void setBillFreqlList(Map billFreqlList) {
		this.billFreqlList = billFreqlList;
	}

	public Map getFeeTypList() {
		return feeTypList;
	}

	public void setFeeTypList(Map feeTypList) {
		this.feeTypList = feeTypList;
	}

	public Map getDsouRceList() {
		return dsouRceList;
	}

	public void setDsouRceList(Map dsouRceList) {
		this.dsouRceList = dsouRceList;
	}

	@Override
	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	@Override
	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
	}
}
