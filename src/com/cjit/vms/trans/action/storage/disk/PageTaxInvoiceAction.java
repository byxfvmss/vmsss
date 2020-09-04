package com.cjit.vms.trans.action.storage.disk;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.PaperInvoiceStock;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.TaxDiskMonitorInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceStockDetail;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;
import com.cjit.vms.trans.util.DataUtil;

public class PageTaxInvoiceAction  extends DataDealAction {
	
	private Map mapVatType;
	private String flag;//提交按钮区分
	private String instId;
	private String receiveInvoiceTime; // 领购日期	
	private String receiveInvoiceEndTime; // 领购日期
	private String receiveUserId; // 领购人员
	private String invoiceType; // 发票类型
	private String max_distrubute_limit;
	private String taxDiskNo; 
	private String taxpayerNo;
	private String invoiceCode;
	private String  invoiceBeginNo;
	private static final String PATTREN = "yyyy-MM-dd";
	private PaperInvoiceListInfo paperListInfo = new PaperInvoiceListInfo();
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(PATTREN);
	private PageTaxInvoiceService pageTaxInvoiceService;
	private BillIssueService billIssueService;
	private ParamConfigVmssService paramConfigVmssService;
	private String taxParam;//税控参数
	private List lstAuthInstId;
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	} 
	public String listPageTaxInvoice(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			if (StringUtil.isNotEmpty(flag)) {
				
				paginationList.setCurrentPage(1);
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			paperListInfo.setLstAuthInstId(lstAuthInstId);
			paperListInfo.setUserID(this.getCurrentUser().getId());
			// 一览数据检索
			List paperInfoList = pageTaxInvoiceService.findPageTaxInvoice(paperListInfo, paginationList);
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");

			this.request.setAttribute("paperInfoList", paperInfoList);
			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.setAttribute("currMonth", currMonth);
			if(taxParam.equals(TaxSelvetUtil.tax_Server_ch)){
				return "tax";
			}else{
				return "disk";
			}
			} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}
	public void deletePaperInvoice() throws Exception{
		String ids=request.getParameter("billIds");
		String[] billIds=ids.split(",");
		if(billIds.length==0){
			printWriterResult("exception");
			return;
		}
		for(int i=1;i<billIds.length;i+=2){
			if(billIds[i]!="0"){
				printWriterResult("error");
				return;
			}
		}
		for(int i=0;i<billIds.length;i+=2){
			
		}
		printWriterResult("success");
	}
	public void deleteInvoice(){
		
	}
	//获取税控盘基本信息
	public void getDiskInfo() throws Exception{
		String taxDiskNo = request.getParameter("taxDiskNo");
		String result="";
		//获取注册码
		String registeredInfo = billIssueService.findRegisteredInfo(taxDiskNo);
		if (registeredInfo == null) {
			result = "registeredInfoError";
			printWriterResult(result);
			return;
		}
		//查询税控盘口令和证书口令
		TaxDiskInfo taxDiskInfo = billIssueService.findTaxDiskInfoByTaxDiskNo(taxDiskNo);
		if (taxDiskInfo == null) {
			result = "taxPwdError";
			printWriterResult(result);
			return;
		}
		result = registeredInfo + "|" + taxDiskInfo.getTaxDiskPsw() + "|" + taxDiskInfo.getTaxCertPsw();
		
		System.out.println("查询发票数量：" + result);
		printWriterResult(result);
	}
	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}
	
	/**
	 * @throws Exception保存  vms_tax_disk_info
	 */
	public void getTaxDiskInfo() throws Exception{
		String DataQuery=request.getParameter("DataQuery");
		String[] infos=DataQuery.split("\\|");
		String result="";
		if(infos[0].equals("0")){
			result=infos[1];
			
		}else{
			
			TaxDiskInfo taxDiskInfo=getdataDiskInfo(infos);
			pageTaxInvoiceService.addOrUpdateTaxDiskInfo(taxDiskInfo);
			result="success";
		}
		//数据处理
		printWriterResult(result);
	}
	// 税控盘信息处理
	// 1					|成功						|499000135701|500102010003760|升级版测试用户3760|150010200|重庆市涪陵区国家税务局|004007|20151216112521|20151102000000|1001150128|0|00|0000010000000000||
	//返回结果（0失败1成功）|返回信息（”成功”或者具体错误）|税控盘编号|纳税人识别号		|纳税人名称			|税务机关代码|税务机关名称|发票类型代码|当前时钟|启用时间|版本号|开票机号|企业类型|保留信息|其它扩展信息
	//		0					1							2			3			4			5				6			7	8			9		10	11		12			13		14
	public TaxDiskInfo getdataDiskInfo(String infos[]){
		TaxDiskInfo taxDiskInfo=new TaxDiskInfo();
		
		taxDiskInfo.setTaxDiskNo(infos[2]);
		taxDiskInfo.setTaxpayerNo(infos[3]);
		taxDiskInfo.setTaxpayerNam(infos[4]);
		taxDiskInfo.setTaxBureauNum(infos[5]);
		taxDiskInfo.setTaxBureauNam(infos[6]);
		taxDiskInfo.setDiskBillType(infos[7]);
		taxDiskInfo.setTaxDiskDate(infos[8]);
		taxDiskInfo.setEnableDt(infos[9]);
		taxDiskInfo.setTaxDiskVersion(infos[10]);
		taxDiskInfo.setBillMachineNo(infos[11]);
		taxDiskInfo.setDiskCustType(infos[12]);
		taxDiskInfo.setRetainInfo(infos[13]);
		return taxDiskInfo;
	}
	
	
public void saveTaxDiskStockInvoice() throws Exception{
	//499000135701|0|1|成功|004|1110098079|73760011|490|1110098079^73760001^73760500^500^490^20151102^cs|
	//			|	|返回结果（0失败1成功）|返回信息（”成功”或者具体错误）|发票类型|当前发票代码|当前发票号码|总剩余份数|发票代码^发票起始号码^发票终止号码^发票领购份数^剩余份数^领购日期^领购人员^|
		String data0=request.getParameter("buyInvoiceMsg0");//专票数据
		String data1=request.getParameter("buyInvoiceMsg1");//普票数据
		String infors[]=data0.split("\\|");
		String result="";
		if(infors[2].equals("0")){
			result=infors[3];
		}else{
		System.out.println(data0);
		String taxNo=request.getParameter("taxNo");	//纳税人识别号
		String dateTime=request.getParameter("dateTime");//当前时钟
		String maxamt0=request.getParameter("maxamt0");
		String maxamt1=request.getParameter("maxamt1");
		List list=getPaperInvoiceStock(data0, taxNo, dateTime); //专票列表
		List list1=getPaperInvoiceStock(data1, taxNo, dateTime);//普票列表
		list1.addAll(list);
		for(int i=0;i<list1.size();i++){
			PaperAutoInvoice paperAutoInvoice=(PaperAutoInvoice) list1.get(i);
			List listr=pageTaxInvoiceService.findPaperAutoInvoiceDetial(paperAutoInvoice.getInvoiceType(),
					paperAutoInvoice.getTaxDiskNo(), paperAutoInvoice.getTaxpayerNo(), 
					paperAutoInvoice.getInvoiceCode(), paperAutoInvoice.getInvoiceBeginNo(),  paginationList);
			if(listr.size()==1){
				PaperAutoInvoice pa=(PaperAutoInvoice)listr.get(0);
				paperAutoInvoice.setAutoInvoiceId(pa.getAutoInvoiceId());
				pageTaxInvoiceService.updatepaperAutoInvoicebybusId(paperAutoInvoice);
				//回写库存信息
				pageTaxInvoiceService.copyAutoInvoiceTostock();
				pageTaxInvoiceService.copyAutoInvoiceToStockDetail();
				pageTaxInvoiceService.updateSynStockDetial();
				// 回写分发表
				//pageTaxInvoiceService.copyAutoInvoiceToDispense();
				//pageTaxInvoiceService.updateSynInvoiceDispense();
				
				
			}else{
				pageTaxInvoiceService.savePaperAutoInvoice(paperAutoInvoice);
				//回写纸质库存信息
				pageTaxInvoiceService.copyAutoInvoiceTostock();
				pageTaxInvoiceService.copyAutoInvoiceToStockDetail();
				pageTaxInvoiceService.updateSynStockDetial();
				//回写分发表
				//pageTaxInvoiceService.copyAutoInvoiceToDispense();
				//pageTaxInvoiceService.updateSynInvoiceDispense();
			}
		}
		result="success";
		}
		printWriterResult(result);
	}
	
	
	//dan maxMoney 单张发票开票金额限额 未处理
	public List getPaperInvoiceStock(String data,String taxNo,String dateTime){
		PaperAutoInvoice paperAutoInvoice=null;
		List list=null;
		//String instId=pageTaxInvoiceService.getInstIdbyTaxNo(taxNo);
		String instId=this.getCurrentUser().getOrgId();
		if(StringUtil.isNotEmpty(data)){
			String[] infos=data.split("\\|");
			//499000135701, 0, 004, 1110098079, 73760010, 491, 1110098079^73760001^73760500^500^491^20151102^cs]
			//499000135701|0|1|成功|004|1110098079|73760011|490|1110098079^73760001^73760500^500^490^20151102^cs|

			//		0		
			//paperInvoiceStock.setInvoiceType(infos[0]);
			//paperInvoiceStock.s 机构id
			//税控盘号|发片类型|发票类型|当前发票代码|当前发票号码|总剩余份数|发票代码^发票起始号码^发票终止号码^发票领购份数^剩余份数^领购日期^领购人员^|
			// 	0		1			2		3			4			5			6		7			8			9			10		11		12
			//499000135701|0|1|成功|004|1110098079|73760011|490|1110098079^73760001^73760500^500^490^20151102^cs|
			//	0			1 2	3	4		5		6		7		8		
			/*	PAPER_INVOICE_STOCK_ID	VARCHAR2(20)	N			库存ID
			INVOICE_CODE	VARCHAR2(10)	N			发票代码
			INVOICE_BEGIN_NO	VARCHAR2(8)	N			发票起始号码
			INVOICE_END_NO	VARCHAR2(8)	N			发票终止号码
			INVOICE_NUM	FLOAT(9)	N			总张数
			USERD_NUM	FLOAT(9)	N	0		已用张数
			HAS_DISTRIBUTE_NUM	FLOAT(9)	N	0		已分发张数*/
			list=new ArrayList();
		for(int i=8;i<infos.length;i++)	{
			String datas=infos[i];
			paperAutoInvoice=new PaperAutoInvoice();
			paperAutoInvoice.setAutoInvoiceId(createBusinessId("P"));
			paperAutoInvoice.setTaxpayerNo(taxNo);
			paperAutoInvoice.setInstId(instId);
			paperAutoInvoice.setTaxDiskNo(infos[0]);
			paperAutoInvoice.setInvoiceType(infos[1]);
			paperAutoInvoice.setCurrentInvoiceCode(infos[5]);
			paperAutoInvoice.setCurrentInvoiceNo(infos[6]);
			paperAutoInvoice.setInvoiceNum(infos[7]);
		//发票代码^发票起始号码^发票终止号码^发票领购份数^剩余份数^领购日期^领购人员^|
		// 0		1				2			3			4		5		6
			if(StringUtil.isNotEmpty(datas)){
				String[] fapiaos=datas.split("\\^");
				paperAutoInvoice.setReceiveInvoiceTime(fapiaos[5]);
				paperAutoInvoice.setInvoiceCode(fapiaos[0]);
				paperAutoInvoice.setInvoiceBeginNo(fapiaos[1]);
				paperAutoInvoice.setInvoiceEndNo(fapiaos[2]);
				paperAutoInvoice.setInvoiceNum(fapiaos[3]);
				paperAutoInvoice.setSurplusNum(fapiaos[4]);
				paperAutoInvoice.setUserId(fapiaos[6]);
			}
			list.add(paperAutoInvoice);
			
			}
			
		}
		return list;
	}
	/**
	 * 税目信息保存
	 * @throws Exception  vms_tax_info
	 */
	public void saveTaxInfo() throws Exception{
	String	data0=request.getParameter("taxesQuery0");
	String	data1=request.getParameter("taxesQuery1");
	System.out.println(data0);
	//：taXno|00|发票类型|税种税目索引号^税种税目代码^税率^税种名称^税目名称^|
	/*public class VmsTaxInfo {
		private String taxId; // 税目ID
		private String taxno; // 纳税人识别号
		private String fapiaoType; // 发票类型
		private String taxRate; // 税率
*/		//500102010003760|0|1|成功|004|8^030103^0.0300^增值税^增值税|9^040104^0.0400^增值税^增值税|10^060106^0.0600^增值税^增值税|11^110111^0.1100^增值税^增值税|12^130113^0.1300^增值税^增值税|13^170117^0.1700^增值税^增值税|
	String result="";
	String infors[]=data0.split("\\|");
	if(infors[2].equals("0")){
		result=infors[3];
	}else{
	List list=getVmsTaxInfoList(data0);
	List list1=getVmsTaxInfoList(data1);
	list1.addAll(list);
	if(list1!=null){
		for(int i=0;i<list1.size();i++){
			VmsTaxInfo vmsTaxInfo=(VmsTaxInfo) list1.get(i);
			pageTaxInvoiceService.saveVmsTaxInfo(vmsTaxInfo);
		}
	}
		result="success";
	}
	printWriterResult(result);
	}
	//*/		//500102010003760|0|1|成功|004|8^030103^0.0300^增值税^增值税|9^040104^0.0400^增值税^增值税|10^060106^0.0600^增值税^增值税|11^110111^0.1100^增值税^增值税|12^130113^0.1300^增值税^增值税|13^170117^0.1700^增值税^增值税|

	public List getVmsTaxInfoList(String data){
		List list=null;
		VmsTaxInfo vmsTaxInfo=null;
		if(StringUtil.isNotEmpty(data)){
			list=new ArrayList();
			String[] infos=data.split("\\|");
			for(int i=5;i<infos.length;i++){
				vmsTaxInfo=new VmsTaxInfo();
				String[] datas=infos[i].split("\\^");
				vmsTaxInfo.setTaxno(infos[0]);
				vmsTaxInfo.setFapiaoType(infos[1]);
				vmsTaxInfo.setTaxId(datas[0]);
				vmsTaxInfo.setTaxRate(datas[2]);
				list.add(vmsTaxInfo);
			}
		}
		return list;
	}
	public void extortTaxInvoice() throws IOException, JXLException
	{
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return;
		}
		
			mapVatType=this.vmsCommonService.findCodeDictionary("VAT_TYPE");
			User currentUser = this.getCurrentUser();
			paperListInfo.setUserID(currentUser.getId());
			// 一览数据检索
			List paperInfoList = pageTaxInvoiceService.findPageTaxInvoice(paperListInfo, null);

			StringBuffer fileName = new StringBuffer("发票信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, paperInfoList);
			os.flush();
			os.close();
	
	}
	public List setwriteWidth(List list) throws JXLException{
		 List rowlist=null;
		 List sheetList=new ArrayList();
		 PaperInvoiceListInfo paperInvoiceListInfo=null;
		for(int i=0;i<list.size();i++){
			rowlist=new ArrayList();
			paperInvoiceListInfo=(PaperInvoiceListInfo) list.get(i);
			rowlist.add(Integer.toString(i+1));
			rowlist.add(paperInvoiceListInfo.getTaxpayerNo());
			rowlist.add(DateUtils.toString(paperInvoiceListInfo.getReceiveInvoiceTime(),"yyyy-MM-dd"));
			rowlist.add(paperInvoiceListInfo.getReceiveUserName());
			rowlist.add(DataUtil.getFapiaoTypeCH(paperInvoiceListInfo.getInvoiceType()));
			rowlist.add(paperInvoiceListInfo.getInvoiceNum().toString());
			rowlist.add(paperInvoiceListInfo.getUserdNum().toString());
			rowlist.add(paperInvoiceListInfo.getUnUserdNum().toString());
			rowlist.add(paperInvoiceListInfo.getUserRatioNum().toString());
			
			sheetList.add(rowlist);
		}
		return sheetList;
	}
	//序号	纳税人识别号	领购日期	领购人员	发票类型	总张数	已用张数	未用张数	已用百分比(%)

	public void writeToExcel(OutputStream os, List content) throws IOException,JXLException{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("发票信息表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "纳税人识别号", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "领购日期", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "领购人员", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "发票类型", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "总张数", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "已用张数", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "未用张数	", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "已用百分比(%)", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header7);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header8);
		ws.addCell(header9);
		JXLTool.setAutoWidth(ws, setwriteWidth(content));

		
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			PaperInvoiceListInfo paperInvoiceListInfo = (PaperInvoiceListInfo)content.get(c);
			int column = count++;
			setWritableSheet(ws, paperInvoiceListInfo, column);
		}
		wb.write();
		wb.close();
	}
	private void setWritableSheet(WritableSheet ws, PaperInvoiceListInfo paperInvoiceListInfo, int column)throws WriteException{
		int i = 0;
		
		Label cell1 = new Label(i++, column, Integer.toString(column),JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, paperInvoiceListInfo.getTaxpayerNo(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, DateUtils.toString(paperInvoiceListInfo.getReceiveInvoiceTime(),"yyyy-MM-dd"), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, paperInvoiceListInfo.getReceiveUserName(), JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, DataUtil.getFapiaoTypeCH(paperInvoiceListInfo.getInvoiceType()), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, paperInvoiceListInfo.getInvoiceNum().toString(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, paperInvoiceListInfo.getUserdNum().toString(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, paperInvoiceListInfo.getUnUserdNum().toString(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, paperInvoiceListInfo.getUserRatioNum().toString(), JXLTool.getContentFormat());
		
		
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
		ws.addCell(cell8);
		ws.addCell(cell9);
	
	}
	public void saveMonTaxDiskInfo() throws Exception{
		//data:{mondata0:mondata0,mondata1:mondata1,diskNo:diskNo},
//c)	输出：返回结果（0失败1成功）|返回信息（”成功”或者具体错误）|开票截止时间|数据报送起始日期|数据报送终止日期|
//				0					1 									2		3				4				
		//单张发票开票金额限额|正数发票累计金额限|负数发票累计金额限|负数发票标志|开具负数发票限定天数|最新报税日期
		//	5					6					7					8			9				10			
		//|剩余容量|上传截止日期（0~31）|离线限定功能标识|离线开票限定时长|离线开票限定张数|离线开票限定正数累计金额|离线开票限定负数累计金额|离线开票扩展信息
		
		//11		12					13				14					15				16					17						18				
		//1|成功|20151215|20151102|20151130|1000000|10000000000|10000000000|0|32767|20151101|90216448|1|05|999|0|100000000|0|
		//0	 1		2		3		4			5		6			7		6	9		10		11	  12 13 14 15 16	  17
		String result="";
		String diskNo=request.getParameter("diskNo");
		String mondata0=request.getParameter("mondata0");
		String mondata1=request.getParameter("mondata1");
		System.out.println(mondata0);
		String infors[]=mondata0.split("\\|");
		if(infors[0].equals("0")){
			result=infors[1];
		}else{
		TaxDiskMonitorInfo TaxDiskMonitorInfo0=getMonlist(mondata0,"0",diskNo);
		TaxDiskMonitorInfo TaxDiskMonitorInfo1=getMonlist(mondata1,"1",diskNo);
		pageTaxInvoiceService.addOrupdateMonTaxDiskInfoByDiskinfo(TaxDiskMonitorInfo0);
		pageTaxInvoiceService.addOrupdateMonTaxDiskInfoByDiskinfo(TaxDiskMonitorInfo1);
		result="success";
		}
		printWriterResult(result);
	}
	public TaxDiskMonitorInfo getMonlist(String mondata,String fapiaoType,String diskNo){
		TaxDiskMonitorInfo taxDiskMonitorInfo=new TaxDiskMonitorInfo();
		String infors[]=mondata.split("\\|");
		System.out.println(infors.length);
		taxDiskMonitorInfo.setBillEndDateS(infors[2]);
		taxDiskMonitorInfo.setBillLimitAmtNS(new BigDecimal(infors[7]));
		taxDiskMonitorInfo.setBillLimitAmtPS(new BigDecimal(infors[6]));
		taxDiskMonitorInfo.setBillLimitAmtS(new BigDecimal(infors[5]));
		taxDiskMonitorInfo.setDataRepEndDateS(infors[4]);
		taxDiskMonitorInfo.setDataRepStrDateS(infors[3]);
		taxDiskMonitorInfo.setFapiaoType(fapiaoType);
		taxDiskMonitorInfo.setInstId(this.getCurrentUser().getOrgId());
		taxDiskMonitorInfo.setLimitFunctionS(infors[13]);
		taxDiskMonitorInfo.setnBilDayS(infors[9]);
		taxDiskMonitorInfo.setnBillFlgS(infors[8]);
		taxDiskMonitorInfo.setNewReportDateS(infors[10]);
		taxDiskMonitorInfo.setOffLineAmtNS(new BigDecimal(infors[17]));
		taxDiskMonitorInfo.setOffLineAmtPS(new BigDecimal(infors[16]));
		taxDiskMonitorInfo.setOffLineBillS(new BigDecimal(infors[15]));
		taxDiskMonitorInfo.setOffLineDayS(new BigDecimal(infors[14]));
		taxDiskMonitorInfo.setOffLineOtsS("");
		taxDiskMonitorInfo.setResidualCapacityS(new BigDecimal(infors[11]));
		taxDiskMonitorInfo.setTaxDiskNo(diskNo);
		taxDiskMonitorInfo.setUploadDeadlineS(infors[12]);
		return taxDiskMonitorInfo;
	}
	public String viewpaperAutoInvoiceDetial(){
		//String invoiceType,String taxDiskNo, String taxpayerNo,String invoiceCode,String  invoiceBeginNo
		paginationList.setCurrentPage(1);
		pageTaxInvoiceService.findPaperAutoInvoiceDetial(invoiceType, taxDiskNo, taxpayerNo, invoiceCode, invoiceBeginNo, paginationList);
		return SUCCESS;
	}
	public Map getMapVatType() {
		return mapVatType;
	}
	public String getFlag() {
		return flag;
	}
	public String getInstId() {
		return instId;
	}
	public String getReceiveInvoiceTime() {
		return receiveInvoiceTime;
	}
	public String getReceiveInvoiceEndTime() {
		return receiveInvoiceEndTime;
	}
	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public void setReceiveInvoiceTime(String receiveInvoiceTime) {
		this.receiveInvoiceTime = receiveInvoiceTime;
	}
	public void setReceiveInvoiceEndTime(String receiveInvoiceEndTime) {
		this.receiveInvoiceEndTime = receiveInvoiceEndTime;
	}
	public String getReceiveUserId() {
		return receiveUserId;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public String getMax_distrubute_limit() {
		return max_distrubute_limit;
	}
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public void setMax_distrubute_limit(String maxDistrubuteLimit) {
		max_distrubute_limit = maxDistrubuteLimit;
	}
	
	public PaperInvoiceListInfo getPaperListInfo() {
		return paperListInfo;
	}
	public void setPaperListInfo(PaperInvoiceListInfo paperListInfo) {
		this.paperListInfo = paperListInfo;
	}
	public PageTaxInvoiceService getPageTaxInvoiceService() {
		return pageTaxInvoiceService;
	}
	public void setPageTaxInvoiceService(PageTaxInvoiceService pageTaxInvoiceService) {
		this.pageTaxInvoiceService = pageTaxInvoiceService;
	}
	public BillIssueService getBillIssueService() {
		return billIssueService;
	}
	public void setBillIssueService(BillIssueService billIssueService) {
		this.billIssueService = billIssueService;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public String getTaxpayerNo() {
		return taxpayerNo;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public String getInvoiceBeginNo() {
		return invoiceBeginNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public void setInvoiceBeginNo(String invoiceBeginNo) {
		this.invoiceBeginNo = invoiceBeginNo;
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
	

}
