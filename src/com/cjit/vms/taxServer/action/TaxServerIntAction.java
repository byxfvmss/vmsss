package com.cjit.vms.taxServer.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.cjit.vms.taxServer.model.BillCancel;
import com.cjit.vms.taxServer.model.CurrentBill;
import com.cjit.vms.taxServer.model.ParamSet;
import com.cjit.vms.taxServer.model.parseXMl.BaseReturnXml;
import com.cjit.vms.taxServer.model.parseXMl.BillCancelReturnCancel;
import com.cjit.vms.taxServer.model.parseXMl.CurrentReturnXml;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.vms.trans.service.BillInfoService;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;

public class TaxServerIntAction extends BaseTaxSelverAction {

	private static final long serialVersionUID = 1L;
	private BillInfo billInfo;
	private BillIssueService billIssueService;
	private List billInfoList;
	private List transInfoList;
	private String[] selectBillIds;
	private String filePath;
	private  ParamSet paramSet=new ParamSet();
	private CurrentBill currentBill=new CurrentBill();
	private PageTaxInvoiceService pageTaxInvoiceService;
	private BillInfoService billInfoService;



	/**
	 * @throws Exception
	 *             构建参数设置模型
	 */
	public void getParamSet() throws Exception {
		ParamSet paramSet=new ParamSet();
		paramSet.setId(TaxSelvetUtil.id_param);
		paramSet.setApplyTypeCode(TaxSelvetUtil.apply_type_ch);
		paramSet.setComment(TaxSelvetUtil.comment_paramSet);
		paramSet.setSelverIpAddress(TaxSelvetUtil.selvet_Ip_address);
		paramSet.setServletport(TaxSelvetUtil.servlet_port);
		paramSet.setTaxKeyPwd(TaxSelvetUtil.Tax_key_pwd);
		paramSet.setAqm(TaxSelvetUtil.Tax_aqm);
		String paramXml=paramSet.createParamSetXml();
		printWriterResult(paramXml);

	}
	/**
	 * 解析参数设置
	 * @throws Exception 
	 */
	public void getParamResult() throws Exception{
		String param=request.getParameter("param");
		BaseReturnXml baseReturnXml=paramSet.ParserParamSetToMap(param);
		String result="";
		if(baseReturnXml.getReturncode().equals("-3")){
			result=baseReturnXml.getReturnmsg();
		}else if(baseReturnXml.getReturncode().equals("0")){
			result="success";
		}else{
			result=baseReturnXml.getReturnmsg();
		}
		paramSet.outPramaXmlFile(param); //生成xml 文件
		printWriterResult(result);
	
		
	}
	
	
	/**
	 * @throws Exception
	 *             构建查询当前票号模型
	 */

	/*
	 * <?xml version=\"1.0\" encoding=\"gbk\"?>\r\n <business id=\"10004\"
	 * comment=\"查询当前未开票号\">\r\n <body yylxdm=\"1\">\r\n
	 * <kpzdbs>7020</kpzdbs>\r\n <fplxdm>004</fplxdm>\r\n </body>\r\n</business>
	 */
	public void CreateCurrentBillXML() throws Exception {
		String fapiaoType=request.getParameter("fapiaoType");
		currentBill=new CurrentBill();
		currentBill.setApplyTypeCode(TaxSelvetUtil.apply_type_ch);
		currentBill.setBillTerminalFlag(TaxSelvetUtil.bill_TerminalFlag_ch);
		currentBill.setComment(TaxSelvetUtil.comment_current_bill);
		currentBill.setFapiaoTypeCode(getfapiaoType(fapiaoType));
		currentBill.setId(TaxSelvetUtil.id_current_bill);
		String result=currentBill.createCurrentBillXml();
		printWriterResult(result);

	}
	/**
	 * 解析当前票号
	 * @throws Exception 
	 */
	public void parseCurrentBillXml() throws Exception{
		String param =request.getParameter("param");
		CurrentReturnXml currentXml=currentBill.ParserCurrentBillXml(param);
		String result="";
		if(currentXml.getReturncode().equals("0")){
			result=currentXml.getBillCode()+","+currentXml.getBillNo();
		}else if(currentXml.getReturncode().equals("99")){
			result="nobill";
		}
		currentBill.outCurrentBillXmlFile(param);
		printWriterResult(result);
	}
	/**
	 * @throws Exception  发票空白作废
	 */
	public void billCancelSelver() throws Exception{
		String fapiaoType=request.getParameter("fapiaoType");
		billCancelSelverUtil("", "", "", TaxSelvetUtil.bill_enmpty_cancel_Typech, fapiaoType);
	}
	/**
	 * @throws Exception  发票已开具作废
	 */
	public void billNomeCancelSelver() throws Exception{
		String billId=request.getParameter("billId");
		BillInfo bill=billInfoService.findBillInfo1(billId);
		billCancelSelverUtil(bill.getAmtSum().toString(),bill.getBillCode(),bill.getBillNo(), TaxSelvetUtil.bill_cancel_Typech, bill.getFapiaoType());
	}
	public void billCancelSelverUtil(String amtSum,String billcode,String billNo,String billCancelType,String fapiaoType)
		throws Exception{
		BillCancel  billCancel=new BillCancel();
		billCancel.setAmtSum(amtSum);
		billCancel.setApplyTypeCode(TaxSelvetUtil.apply_type_ch);
		billCancel.setBillCancelType(billCancelType);
		billCancel.setBillCode(billcode);
		billCancel.setBillNo(billNo);
		billCancel.setBillTerminalFlag(TaxSelvetUtil.bill_TerminalFlag_ch);
		billCancel.setCancelPeople(this.getCurrentUser().getName());
		billCancel.setComment(TaxSelvetUtil.comment_bill_Cancel);
		billCancel.setFapiaoTypeCode(getfapiaoType(fapiaoType));
		billCancel.setId(TaxSelvetUtil.id_Cancel);
		String result=billCancel.createBillCancelXml(billCancel.getBillCancelType());
		printWriterResult(result);
	}
	/**
	 * 发票空白作废解析
	 * @throws Exception getBillCancelResult
	 *//*
	public void getBillCancelResult() throws Exception{
		String param =request.getParameter("param");
		BillCancel  billCancel=new BillCancel();
		BillCancelReturnCancel bill=billCancel.ParserBillCancelXml(param);
		String result="";
		if(bill.getReturncode().equals("0")){
			result=bill.getBillCode()+","+bill.getBillNo()+","+bill.getBillCancelDate();
		}else if(bill.getReturncode().equals("99")){
			result="errorNo";
		}
		billCancel.outBillCancelXmlFile(param, "0");
		printWriterResult(result);
	}*/
	/**
	 * 发票作废解析
	 * @throws Exception getBillCancelResult
	 */
	public void parseBillCancelResult() throws Exception{
		String param =request.getParameter("param");
		String billId =request.getParameter("billId");
		BillCancel  billCancel=new BillCancel();
		BillCancelReturnCancel bill=billCancel.ParserBillCancelXml(param);
		String result="";
		if(bill.getReturncode().equals("0")){
			Map map = new HashMap();
			map.put("billId", billId);
			map.put("datastatus", "15");
			billCancelService.updateBillCanceldataStatus(map);
			result="success";
		}else if(bill.getReturncode().equals("99")){
			result="errorNo";
		}
		billCancel.outBillCancelXmlFile(param, "0");
		printWriterResult(result);
	}
	
	/**
	 * @throws Exception 空白做费解析
	 */
	public void getBillBankCancelResult() throws Exception{
		String param =request.getParameter("param");
		BillCancel  billCancel=new BillCancel();
		BillCancelReturnCancel bill=billCancel.ParserBillCancelXml(param);
		String result="";
		if(bill.getReturncode().equals("0")){
			result=bill.getBillCode()+","+bill.getBillNo()+","+bill.getBillCancelDate();
			PaperInvoiceUseDetail invalidInvoice = new PaperInvoiceUseDetail();
			invalidInvoice.setInvoiceCode(billCancel.getBillCode());
			invalidInvoice.setInvoiceNo(billCancel.getBillNo());
			invalidInvoice.setInvoiceStatus("");
			billIssueService.updatePaperInvoiceStatus(invalidInvoice);
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			
			List<PaperAutoInvoice> list=pageTaxInvoiceService.findpaperAutoInvoicebyBusId(billCancel.getBillNo(), lstAuthInstId, billCancel.getBillCode());
			PaperAutoInvoice pa=list.get(0);
			billCancel.setBillNo(Integer.toString(Integer.parseInt(billCancel.getBillNo())+1));
			pageTaxInvoiceService.updateinvoiceEmptyCurrentNo(pa.getInvoiceBeginNo(), pa.getInvoiceBeginNo(), billCancel.getBillCode(),billCancel.getBillNo());
			pageTaxInvoiceService.updateDistributeAfterIssue(billCancel.getBillCode(), pa.getInvoiceBeginNo(),pa.getInvoiceEndNo(),billCancel.getBillNo());
			pageTaxInvoiceService.updateStorckdetialAfterIssue(billCancel.getBillNo(), pa.getInvoiceBeginNo(), billCancel.getBillCode());
		}else if(bill.getReturncode().equals("99")){
			result="errorNo";
		}
		billCancel.outBillCancelXmlFile(param, "0");
		printWriterResult(result);
	}
	

	

	/**
	 * @throws Exception
	 *             解析数据模型
	 *//*
	public void updateIssueSevlerResult() throws Exception {
		String issueRes = request.getParameter("issueRes");
		String billId = request.getParameter("billId");
		// 空白作废
		//billCancel();
		StringReader read = new StringReader(issueRes);
		InputSource source = new InputSource(read);//利用字节流创建新的输入源
		SAXBuilder sb = new SAXBuilder();

		Document doc = sb.build(source);
		Element root1 = doc.getRootElement();
		System.out.println(root1.getName());// 输出根元素的名称（测试）
		Element body = root1.getChild("body");
		System.out.println(body.getName());
		String returncode = body.getChildText("returncode");
		String returnmsg = body.getChildText("returnmsg");
		String result = "";
		if (returncode.endsWith("0")) {
			String billCode = body.getChildText("fpdm");
			String billNo = body.getChildText("fphm");
			billInfo.setBillCode(billCode);
			billInfo.setBillNo(billNo);
			billInfo.setBillId(billId);
			billIssueService.updatebillInfoIssueResult(billInfo);
			billIssueService.updateTransInfoStatus("99", billId);
			result = "开具成功";
		} else {
			result = returnmsg;
		}

		System.out.println(issueRes);
		printWriterResult(result);
	}
	*//**
	 * 发 票空白作废 构造数据模型
	 * @throws Exception 
	 *//*
	public void billCancelSelver() throws Exception{
		String fapiaoType=request.getParameter("fapiaoType");
		Element root = new Element("business").setAttribute("id", "10009")
		.setAttribute("comment", "发票作废");
		Document Doc = new Document(root);//
		Element elements = new Element("body").setAttribute("yylxdm", "1");//
		setElementText(elements,"kpzdbs","7020");
		setElementText(elements,"fplxdm",getfapiaoType(fapiaoType));
		setElementText(elements,"zflx","0");
		setElementText(elements,"fpdm","");
		setElementText(elements,"fphm","");
		setElementText(elements,"zfr",this.getCurrentUser().getName());
		root.addContent(elements);
		getDocumentRoot(Doc,path_ch,"BillemptyCancel.xml");
		
	}
\
	
	 
	
	*//**
	 * 当前票号的比对  成功则执行发票空白开具
	 * @throws Exception 
	 */
	public void CompareCurrentBill() throws Exception{
		String data=request.getParameter("result");
		String []datas=data.split(",");
		String invoiceCode =datas[0];
		String invoiceNo =datas[1];
		String invoiceStatus="2";
		String result="end";
		PaperInvoiceUseDetail paperInvoiceUseDetail=billIssueService.getPaperInvoiceUseDetail(invoiceStatus, invoiceCode, invoiceNo);
		if(paperInvoiceUseDetail!=null){
			result="success";
		}
		printWriterResult(result);
	}

	
	
	
	
	public BillInfo getBillInfo() {
		return billInfo;
	}

	public BillIssueService getBillIssueService() {
		return billIssueService;
	}

	public List getBillInfoList() {
		return billInfoList;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public String[] getSelectBillIds() {
		return selectBillIds;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public void setBillIssueService(BillIssueService billIssueService) {
		this.billIssueService = billIssueService;
	}

	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public void setSelectBillIds(String[] selectBillIds) {
		this.selectBillIds = selectBillIds;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public PageTaxInvoiceService getPageTaxInvoiceService() {
		return pageTaxInvoiceService;
	}
	public void setPageTaxInvoiceService(PageTaxInvoiceService pageTaxInvoiceService) {
		this.pageTaxInvoiceService = pageTaxInvoiceService;
	}
	public BillInfoService getBillInfoService() {
		return billInfoService;
	}
	public void setBillInfoService(BillInfoService billInfoService) {
		this.billInfoService = billInfoService;
	}
	

	
}
