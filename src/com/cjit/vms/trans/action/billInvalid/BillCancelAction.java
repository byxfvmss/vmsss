package com.cjit.vms.trans.action.billInvalid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

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

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import cjit.crms.util.DateUtil;
import cjit.crms.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.common.util.XmlUtil;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.JxlExcelInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.billInvalid.BillCancelInfo;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.service.BillTrackService;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.ws.jdkClient.VatServiceClient;
import com.cjit.ws.utils.Axis2Client;

public class BillCancelAction extends DataDealAction {
	private static final long serialVersionUID = 1L;
	private String message;
	private BillCancelInfo billCancelInfo = new BillCancelInfo();
	private BillInfo billInfo = new BillInfo();
	private List billCancelInfoList;
	private String flag;
	private String submitFlag;
	private String[] selectBillIds;
	private TransInfoService transInfoService;
	private BillTrackService billTrackService;
	private String filePath;
	private ParamConfigVmssService paramConfigVmssService;
	private PageTaxInvoiceService pageTaxInvoiceService;
	private Map chanNelList;
	private Map billFreqlList;
	private Map feeTypList;
	private Map dsouRceList;
	private VmsCommonService vmsCommonService;
	private String taxParam;//税控参数
	public String billCancelApply(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
		feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
		dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");

		try {

			this.message = (String) this.request.getAttribute("message");
			Map map = transInfoService.findSysParam("OBSOLTETTIME");
			billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString());
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billCancelInfo = new BillCancelInfo();
				billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString());
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			if (StringUtil.isNotEmpty(flag)) {
				billInfo.setSearchFlag(flag);
			}
			billCancelInfo.setBillBeginDate(this.getBillBeginDate());
			billCancelInfo.setBillEndDate(this.getBillEndDate());
			billCancelInfo.setUserId(currentUser.getId());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			billCancelInfoList = billCancelService.findBillCancelInfoList(billCancelInfo,
					currentUser.getId(), paginationList);
			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("billInfoList", billCancelInfoList);
			this.request.getSession().setAttribute("billBeginDate",
					billCancelInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate",
					billCancelInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(),
					DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-billCancelApply", e);
		}
		return ERROR;
	}
	
	public String updateBillCancelStatus(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			String dataStatus = request.getParameter("dataStatus");
			Map map = transInfoService.findSysParam("OBSOLTETTIME");
			billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString());
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				for (int i = 0; i < this.selectBillIds.length; i++) {
					Map params = new HashMap();
					params.put("billId", selectBillIds[i]);
					params.put("dataStatus", dataStatus);
					User currentUser = this.getCurrentUser();
					if (currentUser != null) {
						// 记录废票发起人，以便交叉审核
						params.put("cancelInitiator", currentUser.getId());
					}
					// billCancelInfo = billCancelService.findBillCancelInfo(selectBillIds[i]);
					billCancelService.updateBillCancelOperateStatus(params);//保存交易原始状态
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateBillCancelStatus", e);
			return ERROR;
		}
	}
	
	public String updateBillCancelAuditingStatus(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			String dataStatus = request.getParameter("dataStatus");
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				for (int i = 0; i < this.selectBillIds.length; i++) {
					Map params = new HashMap();
					params.put("billId", selectBillIds[i]);
					params.put("dataStatus", dataStatus);
					params.put("cancelReason", "");
					User currentUser = this.getCurrentUser();
					if (currentUser != null) {
						// 记录废票发起人，以便交叉审核
						params.put("cancelInitiator", currentUser.getId());
					}
					// 改变交易状态dataStatus
					billCancelService.updateBillCanceldataStatus(params);
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateBillCancelAuditingStatus", e);
			return ERROR;
		}
	}
	
	public String billCancelToRefuse() {
		String[] billIds = request.getParameter("billId").split(",");
		if (billIds != null && billIds.length > 0) {
			String billId = "";
			for (int i=0; i<billIds.length; i++) {
				billId = billId.equals("") ? billIds[i] : billId + "," + billIds[i];
			}
			request.setAttribute("billId", billId);
			return SUCCESS;
		}
		return ERROR;
	}
	/**
	 * 审核拒绝将状态改变为作废前的原始状态
	 * @return
	 * @throws Exception 
	 */
	public void updateDataStatusToOriginalStatus() throws Exception{
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
		}
		try {
			String[] billIds = request.getParameter("billId").split(",");
			String cancelReason = request.getParameter("cancelReason");
			cancelReason = URLDecoder.decode(cancelReason, "utf-8");
			cancelReason = URLDecoder.decode(cancelReason, "utf-8");
			
			if (billIds != null && billIds.length > 0) {
				for (int i = 0; i < billIds.length; i++) {
					// billCancelInfo = billCancelService.findBillCancelInfo(billIds[i]);
					// billCancelInfo.setDataStatus(billCancelInfo.getOperateStatus());

					Map params = new HashMap();
					params.put("billId", billIds[i]);
					params.put("cancelReason", cancelReason);
					User currentUser = this.getCurrentUser();
					if (currentUser != null) {
						// 记录废票发起人，以便交叉审核
						params.put("cancelInitiator", currentUser.getId());
					}
					billCancelService.updateBillCanceldataStatus(params);//将原始交易状态赋值dataStatus
				}
			}
			printWriterResult("success");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateDataStatusToOriginalStatus", e);
			printWriterResult("error");
		}
	}
	
	/**
	 * 撤销交易后将状态改变为作废前的原始状态
	 * @return
	 */
	public String revokeBill(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			User currentUser = this.getCurrentUser();
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				for (int i = 0; i < this.selectBillIds.length; i++) {
					BillCancelInfo billCancelInfo = billCancelService.findBillCancelInfo(selectBillIds[i]);
					// 如果不是当天的废票，需要【作废已审核】撤销后，将状态更新为【作废待审核】
					// 否则的话，直接改变为作废前的原始状态。
					Map params = new HashMap();
					params.put("billId", selectBillIds[i]);
					params.put("cancelReason", "");
					if (currentUser != null) {
						// 记录废票发起人，以便交叉审核
						params.put("cancelInitiator", currentUser.getId());
					}
					if (!DateUtil.parseDateToString(new Date()).equals(billCancelInfo.getBillDate().substring(0, 10))) {
						params.put("dataStatus", DataUtil.BILL_STATUS_13);
						billCancelService.updateBillCanceldataStatus(params);
					} else {
						billCancelService.updateBillCanceldataStatus(params);
					}
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-revokeBill", e);
			return ERROR;
		}
	}
	
	public void billCancelDispose() throws Exception{
		String result = "";
		StringBuffer sb = new StringBuffer();
		String taxDiskNo = request.getParameter("taxDiskNo");
		String billId = request.getParameter("billId");
		
		//获取注册码
		String registeredInfo = billCancelService.getRegisteredInfo(taxDiskNo);
		if (registeredInfo == null) {
			result = "registeredInfoError";
			printWriterResult(result);
			return;
		}
		sb.append(registeredInfo);
		
		//查询税控盘口令和证书口令
		TaxDiskInfo taxDiskInfo = billCancelService.getTaxDiskInfo(taxDiskNo);
		if (taxDiskInfo == null) {
			result = "taxPwdError";
			printWriterResult(result);
			return;
		}
		sb.append("|" + taxDiskInfo.getTaxDiskPsw() + "|" + taxDiskInfo.getTaxCertPsw());
		
		sb.append("|" + billId);
		
		//查询需要开具的发票
		BillInfo bill = billInfoService.findBillById(billId);
		sb.append("|" + bill.getFapiaoType());  //发票类型
		sb.append("|1");                        //作废类型
		sb.append("|" + bill.getBillCode());    //发票代码
		sb.append("|" + bill.getBillNo());      //发票号码
		sb.append("|" + this.getCurrentUser().getName());  //作废人
		sb.append("|" + bill.getAmtSum());                 //发票金额
		
		result = sb.toString();	
		//System.out.println(result);
		printWriterResult(result);
	}
	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		//out.print(result);
		out.write(result);
		out.close();
	}

	public void updateCancelBillResult(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return;
		}
		try {
			this.message = (String) this.request.getAttribute("message");
			String cancelRes = request.getParameter("cancelRes");
			System.out.println(cancelRes+"===");
			String[] results = cancelRes.split("\\|");
			// 0|已开具发票作废，当前发票已作废|B2016011100000001200|
			System.out.println("319");
			if ("1".equals(results[0])) {
				Map params = new HashMap();
				params.put("billId", results[2]);
				params.put("dataStatus", DataUtil.BILL_STATUS_15);
				params.put("cancelReason", "");
				billCancelService.updateBillCanceldataStatus(params);//改变交易状态dataStatus
				
				List lstAuthInstId = new ArrayList();
				this.getAuthInstList(lstAuthInstId);
				//数据回传
				billInfo=billInfoService.findBillById(results[2]);
				List list=pageTaxInvoiceService.findpaperAutoInvoicebyBusId(billInfo.getBillNo(),lstAuthInstId, billInfo.getBillCode());
				if(list!=null){
					PaperAutoInvoice pa=(PaperAutoInvoice)list.get(0);
					pageTaxInvoiceService.updateIssueCancleNum(pa.getInvoiceBeginNo(), pa.getInvoiceEndNo(), billInfo.getBillCode());
				}
				// 查询对应交易信息，修改交易状态为开票中
				TransInfo transInfo = new TransInfo();
				transInfo.setBillId(results[2]);
				
				transInfo.setLstAuthInstId(lstAuthInstId);
				List transList = transInfoService.findTransInfoForBillCancelList(transInfo);
				for (Iterator t = transList.iterator(); t.hasNext();) {
					TransInfo trans = (TransInfo) t.next();
					
					trans.setBalance(trans.getBalance().add(trans.getTbincomeCny()));
					// 在vms_trans_info表中，修改状态为未开票，并且将作废发票中的金额增加到未开票金额中
					transInfoService.updateTransStatusAndBalance(trans.getTransId(), trans.getBalance(), DataUtil.TRANS_STATUS_1);
				}
				
			}
			printWriterResult(results[1]);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateCancelBillResult", e);
		}
		
	}
	
	/**与核心交互*/
	public void cancelBillAccessCore() throws Exception{
		AjaxReturn coreMessage =null;
		String errorMessage = "";
		try {
			String billId = request.getParameter("billId");
			if (!sessionInit(true)) {
				errorMessage = "用户失效";			
			}
		
			billInfo=billInfoService.findBillById(billId);
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			TransInfo transInfo = new TransInfo();
			transInfo.setBillId(billId);		
			transInfo.setLstAuthInstId(lstAuthInstId);
			List transList = transInfoService.findTransInfoForBillCancelList(transInfo);   //遍历trans，如果发票是合并的，因此会又多个交易，或者如果是拆分的，则多个发票对应一个交易
			//与核心交互1-拼接报文
			InputStream is = this.getClass().getResourceAsStream("/config/config.properties");
			Properties prop = new Properties();
			prop.load(is);
			
			String interfaceParam = "";
			if("LSP".equals(billInfo.getDsouRce())){	//个险
				interfaceParam = (String) prop.get("LSP.interface");
			}else if("LIS".equals(billInfo.getDsouRce())){	//团险
				interfaceParam = (String) prop.get("LIS.interface.fapiaoInfo");
			}else {
				errorMessage = "数据来源获取失败";
			}			
			String[] interfaceParams = interfaceParam.split("\\|");
			
			if(transList.size()==0){
				errorMessage = "没有查到交易信息";
			}
			if("".equals(errorMessage)){//校验完成没有错误则生成报文
				
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				DecimalFormat decimalFormat = new DecimalFormat("000");
				String serialno = dateFormat.format(new Date())+ decimalFormat.format(new Random().nextInt(1000));
				
				Document document = DocumentHelper.createDocument();
				document.setXMLEncoding("UTF-8");
				Element requestElement = document.addElement("REQUEST");
				Element headDoc = requestElement.addElement("HEAD");
				headDoc.addElement("REQUESTTYPE").addText("0003");	//返回发票信息
				headDoc.addElement("REQSERIALNO").addText(serialno);
				headDoc.addElement("FLOWINTIME").addText(DateUtils.getCurrentDateTime());
				
				Element dataDoc = requestElement.addElement("DATA");
				Element billListDoc = dataDoc.addElement("BILLLIST");
				if(transList.size()==1){
					for (Iterator it = transList.iterator(); it.hasNext();) {
						TransInfo trans = (TransInfo) it.next();
						Element billDoc = billListDoc.addElement("BILL");
						String transId = trans.getTransId().substring(0,trans.getTransId().lastIndexOf(trans.getTransType()));
						billDoc.addElement("BUSINESS_ID").addText(transId);	//交易编号
						billDoc.addElement("INS_COD").addText(trans.getTransType());	//险种信息
						billDoc.addElement("BILL_CODE").addText(billInfo.getBillCode());	//发票代码
						billDoc.addElement("BILL_NO").addText(billInfo.getBillNo());	//发票号码
						billDoc.addElement("FAPIAO_TYPE").addText(billInfo.getFapiaoType());	//发票类型
						billDoc.addElement("AMT_CNY").addText("-"+billInfo.getSumAmt());	//金额_人民币
						billDoc.addElement("TAX_AMT_CNY").addText("-"+billInfo.getTaxAmtSum());	//税额_人民币
						billDoc.addElement("INCOME_CNY").addText("-"+billInfo.getAmtSum());	//收入_人民币
					}						
					
				}else{
					for (Iterator it = transList.iterator(); it.hasNext();) {
						TransInfo trans = (TransInfo) it.next();
						Element billDoc = billListDoc.addElement("BILL");
						String transId = trans.getTransId().substring(0,trans.getTransId().lastIndexOf(trans.getTransType()));
						billDoc.addElement("BUSINESS_ID").addText(transId);	//交易编号
						billDoc.addElement("INS_COD").addText(trans.getTransType());	//险种信息
						billDoc.addElement("BILL_CODE").addText(billInfo.getBillCode());	//发票代码
						billDoc.addElement("BILL_NO").addText(billInfo.getBillNo());	//发票号码
						billDoc.addElement("FAPIAO_TYPE").addText(billInfo.getFapiaoType());	//发票类型
						billDoc.addElement("AMT_CNY").addText("-"+trans.getAmt());	//金额_人民币
						billDoc.addElement("TAX_AMT_CNY").addText("-"+trans.getTaxAmt());	//税额_人民币
						billDoc.addElement("INCOME_CNY").addText("-"+trans.getIncome());	//收入_人民币
					}
				}
				
				String requestXml = document.asXML();
				System.out.println(requestXml);
				
				if(XmlUtil.validateXML(requestXml)){
					logManagerService.writeLog(this.request, this.getCurrentUser(),
							serialno, "发票作废申请接口", "请求报文", requestXml, "1");
					log.info("发票作废请求核心接口"+requestXml);
					//与核心交互2-与核心交互					
					
					String responseXml = "";
					if("LSP".equals(billInfo.getDsouRce())){	//个险
						//个险专属的方法，里面有些如命名空间或者调用方法个险若有修改，则需检查生成的客户端所有java类都需要修改
						/*VatServicePortType_VatServicePort_Client client = new VatServicePortType_VatServicePort_Client();
						responseXml = client.invoke(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);*/
						//个险专属的方法，里面有些如命名空间或者调用方法个险若有修改，则需检查生成的客户端所有java类都需要修改
						VatServiceClient vatServiceClient = new VatServiceClient();
						responseXml = vatServiceClient.invoke(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);
					}else if("LIS".equals(billInfo.getDsouRce())){	//团险
						Axis2Client axis2Client = new Axis2Client();
						responseXml = axis2Client.invokeRPCClient(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);
					}		
					
					//本地测试用
//					File file = new File("E:\\aaa.xml");
//			        FileReader reader = new FileReader(file);// 获取该文件的输入流  
//			        char[] bb = new char[1024];// 用来保存每次读取到的字符  
//			        String str = "";// 用来将每次读取到的字符拼接，当然使用StringBuffer类更好  
//			        int n;// 每次读取到的字符长度  
//			        while ((n = reader.read(bb)) != -1) {  
//			            str += new String(bb, 0, n);  
//			        }  
//			        reader.close();// 关闭输入流，释放连接  
//					String responseXml = str;
										
					logManagerService.writeLog(this.request, this.getCurrentUser(),
							serialno, "发票作废申请接口", "响应报文", responseXml, "1");
					log.info("发票作废响应核心接口"+responseXml);
					//与核心交互3-解析返回报文
					Document rootDocument = DocumentHelper.parseText(responseXml);
					Node responseNode = rootDocument.selectSingleNode("RESPONSE");
					//首先判断是否存在错误信息
					Node resultNode = responseNode.selectSingleNode("RESULT");
					String resultType = resultNode.selectSingleNode("RESULTTYPE").getStringValue();
					if(!"0".equals(resultType)){
						//失败的错误信息
						errorMessage = resultNode.selectSingleNode("ERRORINFO").getStringValue();			
						coreMessage = new AjaxReturn(false, errorMessage);
					}else{
						//成功
						coreMessage = new AjaxReturn(true);
					}
				}else{
					errorMessage = "请求核心接口报文格式不正确，报文生成失败";
					coreMessage = new AjaxReturn(false, errorMessage);
				}
			}else{
				coreMessage = new AjaxReturn(false, errorMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-updateCancelBillResult", e);
			coreMessage = new AjaxReturn(false, "系统内部异常");
		}
		returnResult(coreMessage);
	}
	
	
	
	public String listBillCancelAuditing(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}

		chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
		feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
		dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");

		try {
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billCancelInfo = new BillCancelInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
//				this.request.getSession().removeAttribute("billCancelInfo.customerName");
				fromFlag = null;
			}
			if (StringUtil.isNotEmpty(flag)) {
				billInfo.setSearchFlag(flag);
			}
			billCancelInfo.setBillBeginDate(this.getBillBeginDate());
			billCancelInfo.setBillEndDate(this.getBillEndDate());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			billCancelInfoList = billCancelService.findBillCancelAuditingList(billCancelInfo,
					currentUser.getId(), paginationList);
			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("billInfoList", billCancelInfoList);
			this.request.getSession().setAttribute("billBeginDate",
					billCancelInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate",
					billCancelInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(),
					DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-listBillCancelAuditing", e);
		}
		return ERROR;
	}
	
	public String listBillCancel(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}

		chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
		feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
		dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");

		try {
			User currentUser = this.getCurrentUser();
			Map map = transInfoService.findSysParam("OBSOLTETTIME");
			billCancelInfo.setCancelTime(map.get("SELECTED_VALUE").toString());
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billCancelInfo = new BillCancelInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
//			if (StringUtil.isNotEmpty(flag)) {
//				billInfo.setSearchFlag(flag);
//			}
			billCancelInfo.setBillBeginDate(this.getBillBeginDate());
			billCancelInfo.setBillEndDate(this.getBillEndDate());//写到这里了
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			billCancelInfoList = billCancelService.findBillCancelList(billCancelInfo,
					currentUser.getId(), paginationList);
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");
			this.request.setAttribute("paginationList", paginationList);
			System.out.println("flag-----"+billCancelInfoList.toString());
			this.request.setAttribute("billInfoList", billCancelInfoList);
			this.request.getSession().setAttribute("billBeginDate",
					billCancelInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate",
					billCancelInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(),
					DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);
				return "disk";
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-listBillCancel", e);
		}
		return ERROR;
	}
	
	/**
	 * 发票作废页面查看交易
	 */
	public String seeTransWithBill() {
		try {
			String reqSource = request.getParameter("reqSource");
			String billId = (String) this.request.getParameter("billId");
			billCancelInfoList = billTrackService.findTransByBillId(billId, paginationList);
			this.request.setAttribute("reqSource", reqSource);
			this.request.setAttribute("paginationList", paginationList);
			this.request.setAttribute("billInfoList", billCancelInfoList);
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-seeTransWithBill", e);
		}
		return ERROR;
	}
	
	/**
	 * 发票作废页面查看票样
	 */
	public String viewImgFromBill() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = billTrackService.findBillInfoById(billId);
			
			List billItemList = billTrackService.findBillItemByBillId(billId);
			if (billItemList.size() > 9) {
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
			map.put("billItemList", billItemList);
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
			
			filePath = retMap.get("BILL_IMG_PATH") + imgName;
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-viewImgFromBill", e);
		}
		return ERROR;
	}
	
	/**
	 * 查看发票作废信息明细界面
	 * 
	 * @return String
	 */
	public String viewbillCancelInfo(){
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try{
			String reqSource = request.getParameter("reqSource");
			String billId = request.getParameter("billId");
			billCancelInfo = billCancelService.findBillCancelInfo(billId);
			this.request.setAttribute("billCancelInfo", billCancelInfo);
			this.request.setAttribute("reqSource", reqSource);
			return SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			log.error("BillCancelAction-viewbillCancelInfo", e);
		}
		return ERROR;
	}
	
	/**
	 * 导出Excel
	 * 
	 * @return String
	 */
	public void cancelBillToExcel() throws Exception {
		try {
			String reqExportSource = request.getParameter("reqExportSource");
			// 开票日期 开始 结束
			billCancelInfo.setBillBeginDate(this.billCancelInfo.getBillBeginDate());
			billCancelInfo.setBillEndDate(this.billCancelInfo.getBillEndDate());
			// 客户纳税人名称
			billCancelInfo.setCustomerName(this.billCancelInfo.getCustomerName());
			// 发票代码
			billCancelInfo.setBillCode(this.billCancelInfo.getBillCode());
			// 发票号码
			billCancelInfo.setBillNo(this.billCancelInfo.getBillNo());
			// 发票类型
			billCancelInfo.setFapiaoType(this.billCancelInfo.getFapiaoType());
			// 状态（已开具、已打印、打印失败、已收回、13-作废待审核、14-作废已审核、15-已作废）
			if(!reqExportSource.equals("billCancelAuditing"))
			{
				if(this.billCancelInfo.getDataStatus()!=null){
					billCancelInfo.setDataStatus(this.billCancelInfo.getDataStatus());
					reqExportSource = "";
				}
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billCancelInfo.setLstAuthInstId(lstAuthInstId);
			User currentUser = this.getCurrentUser();
			billCancelInfoList = billCancelService.findBillCancelInfoQuery(billCancelInfo, currentUser.getId(),reqExportSource);
			this.request.getSession().setAttribute("transBeginDate",
					billInfo.getBillBeginDate());
			this.request.getSession().setAttribute("transEndDate",
					billInfo.getBillEndDate());

			StringBuffer fileName = new StringBuffer("作废发票信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, billCancelInfoList);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}
	
	public void writeToExcel(OutputStream os, List content) throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = wb.createSheet("作废发票信息表", 0);
		JxlExcelInfo excelInfo = new JxlExcelInfo();
        excelInfo.setBgColor(Colour.YELLOW2);
        excelInfo.setBorderColor(Colour.BLACK);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeaderC(excelInfo));
		Label header2 = new Label(1, 0, "申请开票日期", JXLTool.getHeaderC(excelInfo));
		Label header3 = new Label(2, 0, "开票日期", JXLTool.getHeaderC(excelInfo));
		Label header4 = new Label(3, 0, "客户纳税人名称", JXLTool.getHeaderC(excelInfo));
		Label header5 = new Label(4, 0, "客户纳税人识别号", JXLTool.getHeaderC(excelInfo));
		Label header6 = new Label(5, 0, "发票代码", JXLTool.getHeaderC(excelInfo));
		Label header7 = new Label(6, 0, "发票号码", JXLTool.getHeaderC(excelInfo));
		Label header8 = new Label(7, 0, "开票人", JXLTool.getHeaderC(excelInfo));
		Label header9 = new Label(8, 0, "税控盘号", JXLTool.getHeaderC(excelInfo));
		Label header10 = new Label(9, 0, "开票机号", JXLTool.getHeaderC(excelInfo));
		Label header11 = new Label(10, 0, "合计金额", JXLTool.getHeaderC(excelInfo));
		Label header12 = new Label(11, 0, "合计税额", JXLTool.getHeaderC(excelInfo));
		Label header13 = new Label(12, 0, "税率", JXLTool.getHeaderC(excelInfo));
		Label header14 = new Label(13, 0, "价税合计", JXLTool.getHeaderC(excelInfo));
		Label header15 = new Label(14, 0, "是否手工录入", JXLTool.getHeaderC(excelInfo));
		Label header16 = new Label(15, 0, "开具类型", JXLTool.getHeaderC(excelInfo));
		Label header17 = new Label(16, 0, "发票类型", JXLTool.getHeaderC(excelInfo));
		Label header18 = new Label(17, 0, "状态", JXLTool.getHeaderC(excelInfo));
		
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
		ws.addCell(header17);
		ws.addCell(header18);
		for (int i = 0; i < 18; i++) {
			ws.setColumnView(i, 20);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			BillCancelInfo o = (BillCancelInfo) content.get(i);
			int column = count++;
			Map map = new HashMap();
			map.put("Id", Integer.valueOf(i+1));
			map.put("applyDate",  o.getApplyDate());
			map.put("billDate",  o.getBillDate());
			map.put("customerName", o.getCustomerName());
			map.put("customerTaxNo", o.getCustomerTaxno());
			map.put("billCode", o.getBillCode());
			map.put("billNo", o.getBillNo());
			map.put("drawer", o.getDrawer());
			map.put("taxDiskNo", o.getTaxDiskNo());
			map.put("machineNo", o.getMachineNo());
			map.put("amtSum", o.getAmtSum());
			map.put("taxAmtSum", o.getTaxAmtSum());
			map.put("taxRate", o.getTaxRate());
			map.put("sumAmt", o.getSumAmt());
			map.put("isHandiwork", o.getIsHandiwork());
			map.put("issueType", o.getIssueType());
			map.put("fapiaoType", o.getFapiaoType());
			map.put("dataStatus", o.getDataStatus());
			
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
		
		//申请开票日期applyDate
		Label cell2 = new Label(1, column, o.get("applyDate") == null
				|| o.get("applyDate").equals("") ? "" : o.get("applyDate")
				.toString(), tempCellFormat);
		
		//开票日期billDate
		Label cell3 = new Label(2, column, o.get("billDate") == null
				|| o.get("billDate").equals("") ? "" : o.get("billDate")
				.toString(), tempCellFormat);
		
		//客户纳税人名称customerName
		Label cell4 = new Label(3, column, o.get("customerName") == null
				|| o.get("customerName").equals("") ? "" : o.get(
				"customerName").toString(), tempCellFormat);
		//客户纳税人识别号customerTaxNo
		Label cell5 = new Label(4, column,
				o.get("customerTaxNo") == null || o.get("customerTaxNo").equals("") ? "" : o
						.get("customerTaxNo").toString(), tempCellFormat);
		
		//发票代码billCode
		Label cell6 = new Label(5, column, o.get("billCode") == null
				|| o.get("billCode").equals("") ? "" : o.get("billCode")
				.toString(), tempCellFormat);
		
		//发票号码billNo
		Label cell7 = new Label(6, column, o.get("billNo") == null
				|| o.get("billNo").equals("") ? "" : o.get("billNo")
				.toString(), tempCellFormat);
		
		//开票人drawer
		Label cell8 = new Label(7, column, o.get("drawer") == null
				|| o.get("drawer").equals("") ? "" : o.get("drawer")
				.toString(), tempCellFormat);
		
		//税控盘号taxDiskNo
		Label cell9 = new Label(8, column, o.get("taxDiskNo") == null
				|| o.get("taxDiskNo").equals("") ? "" : o.get("taxDiskNo")
				.toString(), tempCellFormat);
		
		//开票机号machineNo
		Label cell10 = new Label(9, column, o.get("machineNo") == null
				|| o.get("machineNo").equals("") ? "" : o.get("machineNo")
				.toString(), tempCellFormat);
		
		//合计金额amtSum
		Label cell11 = new Label(10, column,
				o.get("amtSum") == null || o.get("amtSum").equals("") ? "" : o
						.get("amtSum").toString(), tempCellFormat);
		//合计税额taxAmtSum
		Label cell12 = new Label(11, column,
				o.get("taxAmtSum") == null || o.get("taxAmtSum").equals("") ? "" : o
						.get("taxAmtSum").toString(), tempCellFormat);
		
		//税率taxRate
		Label cell13 = new Label(12, column,
				o.get("taxRate") == null || o.get("taxRate").equals("") ? "" : o
						.get("taxRate").toString(), tempCellFormat);
		
		//价税合计sumAmt
		Label cell14 = new Label(13, column, o.get("sumAmt") == null
				|| o.get("sumAmt").equals("") ? "" : o.get("sumAmt")
				.toString(), tempCellFormat);
		
		//是否手工录入isHandiwork
		String isHandiwork = "";
		if(o.get("isHandiwork") == null || o.get("isHandiwork").equals("")){
			isHandiwork = "";
		}else{
			if(o.get("isHandiwork").equals("1")){
				isHandiwork = "自动开票";
			}else if(o.get("isHandiwork").equals("2")){
				isHandiwork = "人工审核";
			}else{
				isHandiwork = "人工开票";
			}
		}
		Label cell15 = new Label(14, column, isHandiwork, tempCellFormat);
		
		//开具类型issueType
		String issueType = "";
		if(o.get("issueType") == null || o.get("issueType").equals("")){
			issueType = "";
		}else{
			if(o.get("issueType").equals("1")){
				issueType = "单笔";
			}else if(o.get("issueType").equals("2")){
				issueType = "合并";
			}else{
				issueType = "拆分";
			}
		}
		Label cell16 = new Label(15, column, issueType, tempCellFormat);
		
		//发票类型fapiaoType
		String fapiaoType = "";
		if(o.get("fapiaoType") == null || o.get("fapiaoType").equals("")){
			fapiaoType = "";
		}else{
			if(o.get("fapiaoType").equals("0")){
				fapiaoType = "增值税专用发票";
			}else{
				fapiaoType = "增值税普通发票";
			}
		}
		Label cell17 = new Label(16, column, fapiaoType, tempCellFormat);
		
		//状态dataStatus
		String dataStatus = "";
		if(o.get("dataStatus") == null || o.get("dataStatus").equals("")){
			dataStatus = "";
		}else{
			if(o.get("dataStatus").equals("5")){
				dataStatus = "已开具";
			}else if(o.get("dataStatus").equals("8")){
				dataStatus = "已打印";
			}else if(o.get("dataStatus").equals("9")){
				dataStatus = "打印失败";
			}else if(o.get("dataStatus").equals("19")){
				dataStatus = "已收回";
			}else if(o.get("dataStatus").equals("13")){
				dataStatus = "作废待审核";
			}else if(o.get("dataStatus").equals("14")){
				dataStatus = "作废已审核";
			}else if(o.get("dataStatus").equals("15")){
				dataStatus = "已作废";
			}else{
				dataStatus = "";
			}
		}
		Label cell18 = new Label(17, column, dataStatus, tempCellFormat);
		
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
	}
	
	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
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
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BillCancelInfo getBillCancelInfo() {
		return billCancelInfo;
	}

	public void setBillCancelInfo(BillCancelInfo billCancelInfo) {
		this.billCancelInfo = billCancelInfo;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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

	public TransInfoService getTransInfoService() {
		return transInfoService;
	}

	public void setTransInfoService(TransInfoService transInfoService) {
		this.transInfoService = transInfoService;
	}

	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}

	public void setParamConfigVmssService(
			ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}

	public String getTaxParam() {
		return taxParam;
	}

	public void setTaxParam(String taxParam) {
		this.taxParam = taxParam;
	}

	public PageTaxInvoiceService getPageTaxInvoiceService() {
		return pageTaxInvoiceService;
	}

	public void setPageTaxInvoiceService(PageTaxInvoiceService pageTaxInvoiceService) {
		this.pageTaxInvoiceService = pageTaxInvoiceService;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public List getBillCancelInfoList() {
		return billCancelInfoList;
	}

	public void setBillCancelInfoList(List billCancelInfoList) {
		this.billCancelInfoList = billCancelInfoList;
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
