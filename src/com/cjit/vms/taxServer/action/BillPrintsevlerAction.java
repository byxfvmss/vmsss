package com.cjit.vms.taxServer.action;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxServer.model.BillPrint;
import com.cjit.vms.taxServer.model.PrintMgrSet;
import com.cjit.vms.taxServer.model.parseXMl.BaseReturnXml;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.SpecialTicket;
import com.cjit.vms.trans.service.BillInfoService;

public class BillPrintsevlerAction extends BaseTaxSelverAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BillInfo billInfo = new BillInfo();
	private ParamConfigVmssService paramConfigVmssService;
	private BillInfoService billInfoService;
	
	private List businessList = new ArrayList();
	private List billInfoList;
	private RedReceiptApplyInfo applyInfo;
	private String billBeginDate;
	private String billEndDate;
	private String transBeginDate;
	private String transEndDate;
	private SpecialTicket specialTicket;
	private int printLimitValue;//单次打印限值
	
	private String customerId; // 客户号
	private String faPiaoType; // 发票类型
	private Map mapVatType; // 发票类型List
	private Map mapGoodsList; // 商品名称List
	private Map mapTaxList; // 是否含税List
	private String RESULT_MESSAGE;
	private String updFlg; // 新增修改flag。                  0：新增，1：修改
	private String taxId; // 税目ID
	private String taxRate; // 税率
	public  void createBillPrintXml() throws Exception{
		String billNo=request.getParameter("billNo");
		String fapiaoType=request.getParameter("fapiaoType").equals("0")?"004":"007";
		String billCode=request.getParameter("billCode");
		String fileName=request.getParameter("fileName");
		BillPrint billPrint=new BillPrint();
		billPrint.setApplyTypeCode(TaxSelvetUtil.apply_type_ch);
		billPrint.setBillCode(billCode);
		billPrint.setBillNo(billNo);
		billPrint.setBillTerminalFlag(TaxSelvetUtil.bill_TerminalFlag_ch);
		billPrint.setComment(TaxSelvetUtil.comment_bill_print);
		billPrint.setFapiaoTypeCode(fapiaoType);
		billPrint.setId(TaxSelvetUtil.id_print);
		billPrint.setPrintType(TaxSelvetUtil.Print_Type_0);
		billPrint.setPrintWay(TaxSelvetUtil.print_Way_1);
		String result=billPrint.createPrintBillXml(fileName);
		//System.out.println(request.getRequestURI());
		//System.out.println( request.getSession().getServletContext().getRealPath("/"));
		printWriterResult(result);
	}
	public void updatePrintServerResult() throws Exception{
		String billId=request.getParameter("billId");
		String Data=request.getParameter("data");
		String fileName=request.getParameter("fileName");
		BillPrint billprint=new BillPrint();
		String result="";
		BaseReturnXml baseReturn=billprint.ParsertBillPrintXml(Data);
		if(baseReturn.getReturncode().equals("0")){
			billInfoService.updateBillByBillId(billId, "8");
			result="success";
		}else{
			result=baseReturn.getReturnmsg();
		}
		billprint.outBillPrintXmlFile(Data, fileName);
		printWriterResult(result);
	}	
	
	/*public void getBillPrintSevlerData() throws Exception{
		String billNo=request.getParameter("billNo");
		String fapiaoType=request.getParameter("fapiaoType").equals("0")?"004":"007";
		String billCode=request.getParameter("billCode");
		Element root=new Element("business").setAttribute("id","20004").setAttribute("comment", "发票打印");
		Document Doc = new Document(root);//
		Element elements=new Element("body").setAttribute("yylxdm","1");//
		elements.addContent(new Element("kpzdbs").setText("7020"));
		elements.addContent(new Element("fplxdm").setText(fapiaoType));
		elements.addContent(new Element("fpdm").setText(billCode));
		elements.addContent(new Element("fphm").setText(billNo));
		elements.addContent(new Element("dylx").setText("0"));
		elements.addContent(new Element("dyfs").setText("1"));

		root.addContent(elements);
		getDocumentRoot(Doc, path_ch, "print.xml");
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
		String outString = xmlOut.outputString(Doc);
		Format format = Format.getPrettyFormat();  
		XMLOutputter XMLOut = new XMLOutputter(format); 
		
		XMLOut.output(Doc, new FileOutputStream("d:\\Taxselver\\print.xml")); 
		System.out.println(outString); 
		printWriterResult(outString);
	}
	public void updatePrintServerResult() throws Exception{
		String billId=request.getParameter("billId");
		String Data=request.getParameter("data");
		StringReader read = new StringReader(Data);
		InputSource source = new InputSource(read);
		SAXBuilder sb = new SAXBuilder();

		Document doc = sb.build(source);
		Element root1 = doc.getRootElement();
		System.out.println(root1.getName());// 输出根元素的名称（测试）
		Element body = root1.getChild("body");
		System.out.println(body.getName());
		String returncode = body.getChildText("returncode");
		String returnmsg = body.getChildText("returnmsg");
		if(returncode.equals("0")){
			billInfoService.updateBillByBillId(billId, "8");
		}
		printWriterResult(returnmsg);
	}	
	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}
	*//**
	 * 打印边距构造模型
	 * @throws Exception 
	 */
	public void getPrintMargin() throws Exception{
		String top=request.getParameter("top");
		String left=request.getParameter("left");
		String fapiaoType=request.getParameter("fapiaoType").equals("0")?"004":"007";
		PrintMgrSet print=new PrintMgrSet();
		print.setApplyTypeCode(TaxSelvetUtil.apply_type_ch);
		print.setComment(TaxSelvetUtil.comment_page_mgr);
		print.setFapiaoType(fapiaoType);
		print.setId(TaxSelvetUtil.id_print_mgr);
		print.setLeft(left);
		print.setTop(top);
		String result=print.createPrintMgrXML();
		printWriterResult(result);
	}
	/**
	 * @throws Exception 解析打印边距
	 *//*
	public void getPrintMarginResult() throws Exception{
		String param =request.getParameter("param");
		Element body=getDocument(param,path_out_ch,"printMgr.xml");
		String returnmsg= body.getChildText("returnmsg");
		String  returncode=body.getChildText("returncode");
		
		String result="";
		if(returncode.equals("0")){
			result="设置成功";
		}else{
			result=returnmsg;
		}
		printWriterResult(result);
	}
	*/
	public BillInfo getBillInfo() {
		return billInfo;
	}
	public int getPrintLimitValue() {
		return printLimitValue;
	}
	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}
	public void setPrintLimitValue(int printLimitValue) {
		this.printLimitValue = printLimitValue;
	}
	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}
	public List getBusinessList() {
		return businessList;
	}
	public List getBillInfoList() {
		return billInfoList;
	}
	public void setParamConfigVmssService(
			ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}
	public void setBusinessList(List businessList) {
		this.businessList = businessList;
	}
	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
	}
	public RedReceiptApplyInfo getApplyInfo() {
		return applyInfo;
	}
	public String getBillBeginDate() {
		return billBeginDate;
	}
	public String getBillEndDate() {
		return billEndDate;
	}
	public String getTransBeginDate() {
		return transBeginDate;
	}
	public String getTransEndDate() {
		return transEndDate;
	}
	public SpecialTicket getSpecialTicket() {
		return specialTicket;
	}
	public String getCustomerId() {
		return customerId;
	}
	public String getFaPiaoType() {
		return faPiaoType;
	}
	public Map getMapVatType() {
		return mapVatType;
	}
	public Map getMapGoodsList() {
		return mapGoodsList;
	}
	public Map getMapTaxList() {
		return mapTaxList;
	}
	public String getRESULT_MESSAGE() {
		return RESULT_MESSAGE;
	}
	public String getUpdFlg() {
		return updFlg;
	}
	public String getTaxId() {
		return taxId;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setApplyInfo(RedReceiptApplyInfo applyInfo) {
		this.applyInfo = applyInfo;
	}
	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}
	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}
	public void setTransBeginDate(String transBeginDate) {
		this.transBeginDate = transBeginDate;
	}
	public void setTransEndDate(String transEndDate) {
		this.transEndDate = transEndDate;
	}
	public void setSpecialTicket(SpecialTicket specialTicket) {
		this.specialTicket = specialTicket;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setFaPiaoType(String faPiaoType) {
		this.faPiaoType = faPiaoType;
	}
	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}
	public void setMapGoodsList(Map mapGoodsList) {
		this.mapGoodsList = mapGoodsList;
	}
	public void setMapTaxList(Map mapTaxList) {
		this.mapTaxList = mapTaxList;
	}
	public void setRESULT_MESSAGE(String rESULTMESSAGE) {
		RESULT_MESSAGE = rESULTMESSAGE;
	}
	public void setUpdFlg(String updFlg) {
		this.updFlg = updFlg;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public BillInfoService getBillInfoService() {
		return billInfoService;
	}
	public void setBillInfoService(BillInfoService billInfoService) {
		this.billInfoService = billInfoService;
	}
	
}
