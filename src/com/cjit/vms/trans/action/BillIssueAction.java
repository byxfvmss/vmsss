package com.cjit.vms.trans.action;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.DateUtil;

import com.alibaba.fastjson.JSON;
import com.cjit.common.util.UUID;
import com.cjit.vms.trans.model.*;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.util.DataFileParser;
import com.cjit.vms.trans.util.DataFileParserUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.filem.util.ThreadLock;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.JXLTool;

import org.apache.struts2.ServletActionContext;

import com.cjit.vms.metlife.model.TtmPrcnoMatch;

import javax.servlet.ServletContext;

public class BillIssueAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private String message;
	private BillIssueService billIssueService;
	private BillInfo billInfo;
	private List billInfoList;
	private List transInfoList;
	private String[] selectBillIds;
	private String filePath;
	private PageTaxInvoiceService pageTaxInvoiceService;
	private VmsCommonService vmsCommonService;
	private Map chanNelList;
	private Map billFreqlList;
	private Map feeTypList;
	private Map dsouRceList;
	private String cherNum;//保单号
	private String repNum;//旧保单号
	private String ttmpRcno;//投保单号
	private String feeTyp;//费用类型
	private String billFreq;//交费频率
	private String polYear;//保单年度
	private String hissDte;//承保日期
	private String dsouRce;//数据来源
	private String chanNel;//渠道
	private String premTerm;//期数
	private String hissBeginDte;//开始
	private String hissEndDte;//结束
	private File attachment;
	private String attachmentFileName;
	private Date createBeginTime;
	private Date createEndTime;
	private String beginDate;
	private String endDate;

	private String billId = "";
	private List matchInfoList;
	private List<TtmPrcnoMatch> findMatchInfoOpenList;
	
	/**
	 * 开具发票列表
	 */
	public String listIssueBill() {
		try {
			if ("menu".equals(fromFlag)) {
				billInfo = new BillInfo();
				this.billInfo.setApplyBeginDate(null);
				this.billInfo.setApplyEndDate(null);
				this.request.getSession().setAttribute("curPage", new Integer(1));
				this.request.getSession().setAttribute("pageSize", new Integer(20));
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				this.request.getSession().removeAttribute("customerName");
				this.request.getSession().removeAttribute("customerTaxno");
				this.request.getSession().removeAttribute("isHandiwork");
				this.request.getSession().removeAttribute("issueType");
				this.request.getSession().removeAttribute("fapiaoType");
				this.request.getSession().removeAttribute("dataStatus");
				billInfo.setFapiaoType("1");
				fromFlag = null;
			} else if ("view".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				// 申请开票日期(开始)
				if (!String.valueOf(this.request.getSession().getAttribute("applyBeginDate")).isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("applyBeginDate")))) {
					this.billInfo.setApplyBeginDate(String.valueOf(this.request
							.getSession().getAttribute("applyBeginDate")));
				}
				// 申请开票日期(结束)
				if (!String.valueOf(this.request.getSession().getAttribute("applyEndDate")).isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("applyEndDate")))) {
					this.billInfo.setApplyEndDate(String.valueOf(this.request
							.getSession().getAttribute("applyEndDate")));
				}
				// 客户名称
				if (!"".equals(this.request.getSession().getAttribute(
						"customerName"))) {
					this.billInfo.setCustomerName(String.valueOf(this.request
							.getSession().getAttribute("customerName")));
				}
				// 客户纳税人识别号
				if (!"".equals(this.request.getSession().getAttribute(
						"customerTaxno"))) {
					this.billInfo.setCustomerTaxno(String.valueOf(this.request
							.getSession().getAttribute("customerTaxno")));
				}
				// 是否手工录入
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("isHandiwork")))) {
					this.billInfo.setIsHandiwork(String.valueOf(this.request
							.getSession().getAttribute("isHandiwork")));
				}
				// 开具类型
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("issueType")))) {
					this.billInfo.setIssueType(String.valueOf(this.request
							.getSession().getAttribute("issueType")));
				}
				// 发票类型
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("fapiaoType")))) {
					this.billInfo.setFapiaoType(String.valueOf(this.request
							.getSession().getAttribute("fapiaoType")));
				}
				// 状态
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("dataStatus")))) {
					this.billInfo.setDataStatus(String.valueOf(this.request
							.getSession().getAttribute("dataStatus")));
				}
				// 当前页
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("curPage")))) {
					paginationList.setCurrentPage(Integer.valueOf((String.valueOf(this.request
							.getSession().getAttribute("curPage")))).intValue());
				}
				fromFlag = null;
			} else {
				billInfo = new BillInfo();
				this.billInfo.setApplyBeginDate(this.request
						.getParameter("billInfo.applyBeginDate"));
				this.billInfo.setApplyEndDate(this.request
						.getParameter("billInfo.applyEndDate"));
				this.billInfo.setCustomerName(this.request
						.getParameter("billInfo.customerName"));
				this.billInfo.setCustomerTaxno(this.request
						.getParameter("billInfo.customerTaxno"));
				this.billInfo.setIsHandiwork(this.request
						.getParameter("billInfo.isHandiwork"));
				this.billInfo.setIssueType(this.request
						.getParameter("billInfo.issueType"));
				this.billInfo.setFapiaoType(this.request
						.getParameter("billInfo.fapiaoType"));
				this.billInfo.setDataStatus(this.request
						.getParameter("billInfo.dataStatus"));
			}
			User currentUser = this.getCurrentUser();
			billInfo.setUserId(currentUser.getId());
			
			if (StringUtil.isEmpty(billInfo.getDataStatus())) {
				billInfo.setDataStatus("3,4,7");
			}

			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
			feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
			dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");

			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billInfo.setLstAuthInstId(lstAuthInstId);
			billInfo.setSumAmtBegin(new BigDecimal(0));
			billInfoList = billIssueService.findBillInfoList(billInfo, paginationList);
			this.request.setAttribute("paginationList", paginationList);
			
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");
			//查询空白发票作废数量
			String instId=this.getCurrentUser().getOrgId();
			String currMonth = DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_YYYYMM);
			Long invalidInvoiceNum = billIssueService.findInvalidInvoiceCount("2", billInfo.getFapiaoType(),instId);
			this.request.setAttribute("invalidInvoiceNum", invalidInvoiceNum);
			this.request.setAttribute("currMonth", currMonth);
			
			if (this.billInfo.getApplyBeginDate() != null) {
				this.request.getSession().setAttribute("applyBeginDate",
						this.billInfo.getApplyBeginDate());
			}
			if (this.billInfo.getApplyEndDate() != null) {
				this.request.getSession().setAttribute("applyEndDate",
						this.billInfo.getApplyEndDate());
			}
			if (this.billInfo.getCustomerName() != null) {
				this.request.getSession().setAttribute("customerName",
						this.billInfo.getCustomerName());
			}
			if (this.billInfo.getCustomerTaxno() != null) {
				this.request.getSession().setAttribute("customerTaxno",
						this.billInfo.getCustomerTaxno());
			}
			if (this.billInfo.getIsHandiwork() != null) {
				this.request.getSession().setAttribute("isHandiwork",
						this.billInfo.getIsHandiwork());
			}
			if (this.billInfo.getIssueType() != null) {
				this.request.getSession().setAttribute("issueType",
						this.billInfo.getIssueType());
			}
			if (this.billInfo.getFapiaoType() != null) {
				this.request.getSession().setAttribute("fapiaoType",
						this.billInfo.getFapiaoType());
			}
			this.request.getSession().setAttribute("curPage", String.valueOf(paginationList.getCurrentPage()));
			this.request.getSession().setAttribute("pageSize", String.valueOf(paginationList.getPageSize()));
			
			logManagerService.writeLog(request, currentUser,
					"0016", "发票开具", "销项税管理", "查询可供进行开具处理的票据信息列表", "1");
				return "tax";
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0016", "发票开具", "销项税管理", "查询可供进行开具处理的票据信息列表", "0");
			log.error("BillIssueAction-listIssueBill", e);
		}
		return ERROR;
	}
	
	/**
	 * 查询开具发票数量
	 * @throws Exception 
	 */
	public void getInvoiceNum() throws Exception {
		String result = "";
		String lock = getInstLock();
		if (lock != null) {
			result = lock;
			printWriterResult(result);
			return;
		}
		String taxDiskNo = request.getParameter("taxDiskNo");
		String fapiaoType = request.getParameter("fapiaoType");	
		System.out.println(taxDiskNo);
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
		
		result = registeredInfo + "|" + taxDiskInfo.getTaxDiskPsw() + "|" + taxDiskInfo.getTaxCertPsw() + "|" + fapiaoType;
		
		System.out.println("查询发票数量：" + result);
		printWriterResult(result);
	}
	
	/**
	 * 获取开具信息
	 * @throws Exception 
	 */
	public void issueBill() throws Exception {
		String result = "";
		StringBuffer sb = new StringBuffer();
		String preStr = request.getParameter("preStr");
		String billIds = request.getParameter("billIds");
		String fapiaoType=request.getParameter("fapiaoType");
		
		sb.append(preStr);  //注册码 + | + 发票类型
		
		//获取作废的空白发票 ----需发票类型
		List invalidInvoiceList = billIssueService.findInvalidPaperInvoice("2", fapiaoType);
		
		//获取开具发票id
		selectBillIds = billIds.split(",");
		sb.append("|" + (invalidInvoiceList.size() + selectBillIds.length));
		for (int i=0; i<invalidInvoiceList.size(); i++) {
			PaperInvoiceUseDetail invoice = (PaperInvoiceUseDetail) invalidInvoiceList.get(i);
			sb.append("|0^");  //操作类型、开票类型
			sb.append("^" + invoice.getPaperInvoiceId());        //发票代码
			sb.append("^");                     //购货单位名称
			sb.append("^");                     //购货单位纳税人识别号
			sb.append("^");                     //购货单位地址电话
			sb.append("^");                     //购货单位开户行及账号
			sb.append("^");                     //销货单位名称
			sb.append("^");                     //销货单位纳税人识别号
			sb.append("^");                     //销货单位地址电话
			sb.append("^");                     //销货单位开户行及账号
			sb.append("^");                     //合计金额
			sb.append("^");                     //合计税额
			sb.append("^");                     //收款人
			sb.append("^");                     //复核
			sb.append("^"+this.getCurrentUser().getName());                     //开票人
			sb.append("^" + invoice.getInvoiceCode());   //原发票代码
			sb.append("^" + invoice.getInvoiceNo());     //原发票号码
			sb.append("^");    	 	//通知单编号
			sb.append("^");        //商品数量
			sb.append("^");        //备注
			sb.append("^");        //货物名称
			sb.append("~");        //规格 
			sb.append("~");        //单位
			sb.append("~");        //数量
			sb.append("~");        //单价
			sb.append("~");        //金额
			sb.append("~");        //税率
			sb.append("~");        //税额
		}

		//获取所选发票信息
		for (int i=0; i<selectBillIds.length; i++) {
			BillInfo bill = billIssueService.findBillInfoById(selectBillIds[i]);
			sb.append("|1^0");  //操作类型、开票类型
			sb.append("^" + bill.getBillId());                   //发票ID
			sb.append("^" + bill.getCustomerName());             //购货单位名称
			sb.append("^" + bill.getCustomerTaxno());  			 //购货单位纳税人识别号
			sb.append("^" + bill.getCustomerAddressandphone());		//购货单位地址电话
			sb.append("^" + bill.getCustomerBankandaccount());  	//购货单位开户行及账号
			sb.append("^" + bill.getName());    					//销货单位名称
			sb.append("^" + bill.getTaxno());                    //购货单位纳税人识别号//销货单位纳税人识别号
			System.out.println(bill.getTaxno());
			sb.append("^" + bill.getAddressandphone());          //销货单位地址电话
			sb.append("^" + bill.getBankandaccount());  		//销货单位开户行及账号
			sb.append("^" + bill.getAmtSum());                   //合计金额
			sb.append("^" + bill.getTaxAmtSum());                //合计税额
			if (bill.getPayee() != null) {
				sb.append("^" + bill.getPayee());                    //收款人
			} else {
				sb.append("^" + " "); 
			}
			sb.append("^" + bill.getReviewerName());             //复核
			sb.append("^" + this.getCurrentUser().getName());    //开票人
			sb.append("^" + bill.getOriBillCode());   //原发票代码
			sb.append("^" + bill.getOriBillNo());     //原发票号码	
			sb.append("^" + bill.getNoticeNo());     //通制单编号	
			System.out.println(bill.getNoticeNo());
			//发票商品信息
			List billItemList = billIssueService.findBillItemByBillId(selectBillIds[i]);
			if (billItemList == null || billItemList.size() == 0) {
				result = "billItemError";
				printWriterResult(result);
				return;
			} else if (billItemList.size() > 9) {
				result = "billItemNum";
				printWriterResult(result);
				return;
			}
			sb.append("^" + billItemList.size());
			//商品数量
			if(bill.getRemark().contains("\r\n")){
				bill.setRemark(bill.getRemark().replace("\r\n", "\\r\\n"));
			}
			sb.append("^" + bill.getRemark());            //商品数量
			for (int j=0; j<billItemList.size(); j++) {
				BillItemInfo item = (BillItemInfo)billItemList.get(j);
				sb.append("^" + item.getGoodsName());        //货物名称
				sb.append("~" + item.getSpecandmodel());     //规格
				sb.append("~" + item.getGoodsUnit());        //单位
				sb.append("~" + item.getGoodsNo());          //数量
				sb.append("~" + item.getGoodsPrice());       //单价
				sb.append("~" + item.getAmt());              //金额
				sb.append("~" + item.getTaxRate());          //税率
				sb.append("~" + item.getTaxAmt());           //税额
				//sb.append("~" + "0");           //折扣item.getDiscountRate()
			}
		}
		result = sb.toString();
		System.out.println("开票：" + result);
		printWriterResult(result);
	}
	
	/**
	 * 更新开具结果
	 * 开具/开具所有的数据统一写入VMS_BILL_INFO表中（发票号码、发票代码、开票日期、开票人、税控盘号、开票机号）
     * 根据税控端信息更新交易状态：5-已开具→交易状态更新为“99-开票完成”   7-开具失败→交易状态不更改
	 * @throws Exception 
	 */
	
	//成功笔数|失败笔数|操作结果（1：成功，0：失败）^税控盘号^发票ID^发票代码^发票号码^开票日期^开票机号^返回信息（”成功”或者具体错误）^|
//	0|1|0^499000135568^B2015101800000000211^		  ^        ^           ^0^输入纳税人识别号有误|
//  1|0|1^499000102521^B2016011200000001232^3100124130^00082296^2016-01-25^14|成功^|

	public void updateIssueResult() throws Exception {
		String issueRes = request.getParameter("issueRes");
		System.out.println(issueRes);
		String string="";
		String[] result = issueRes.split("\\|");
		//1|0|1^499000103961^B2016011100000001200^3100124130^00081408^2016-01-11^5|成功^|
		//0|1|0^499000103961^B2016011200000001232^^^^5^证书口令错误 CftCode:381063C0 CftError:你还有0次重试机会(0x381063C0)  DiskErr:|

		User user = this.getCurrentUser();
		this.message = (String) this.request.getAttribute("message");
		for (int i=2; i<result.length; i++) {      //测试有OK!,从3开始
			String[] bills = result[i].split("\\^");
			//区分开具、空白作废发票
			BillInfo bill = new BillInfo();
			bill = billIssueService.findBillInfoById(bills[2]);
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			//空白作废发票更改状态,继续循环
			if (bill == null) {
				if ("1".equals(bills[0])) {
					PaperInvoiceUseDetail invalidInvoice = new PaperInvoiceUseDetail();
					invalidInvoice.setInvoiceCode(bills[3]);
					invalidInvoice.setInvoiceNo(bills[4]);
					invalidInvoice.setInvoiceStatus("");
					billIssueService.updatePaperInvoiceStatus(invalidInvoice);
					List listbill=pageTaxInvoiceService.findpaperAutoInvoicebyBusId(bills[4], lstAuthInstId, bills[3]);
					PaperAutoInvoice pa=(PaperAutoInvoice)listbill.get(0);
					//数据回传
					bills[4]=Integer.toString(Integer.parseInt(bills[4])+1);
					pageTaxInvoiceService.updateinvoiceEmptyCurrentNo(pa.getInvoiceBeginNo(), pa.getInvoiceBeginNo(), bills[3],bills[4]);
					pageTaxInvoiceService.updateDistributeAfterIssue(bills[3], pa.getInvoiceBeginNo(),pa.getInvoiceEndNo(),bills[4]);
					pageTaxInvoiceService.updateStorckdetialAfterIssue(bills[4], pa.getInvoiceBeginNo(), bills[3]);

				}
				continue;
			}
			//开具成功，更改发票信息及交易信息，开具失败，更改状态
			String dataStatus = bill.getDataStatus();
			String issueType = bill.getIssueType();
			BillInfo issueBill = new BillInfo();
			issueBill.setBillId(bills[2]);
			if ("1".equals(bills[0])){
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
				issueBill.setDataStatus("5");
				issueBill.setTaxDiskNo(bills[1]);
				issueBill.setBillCode(bills[3]);
				issueBill.setBillNo(bills[4]);
				issueBill.setBillDate(bills[5] + " " + df.format(new Date()));
				issueBill.setMachineNo(bills[6]);
				issueBill.setDrawer(this.getCurrentUser().getId());
				
				billIssueService.updatebillInfoIssueResult(issueBill);
				bills[4]=Integer.toString(Integer.parseInt(bills[4])+1);
				
				List listbill=pageTaxInvoiceService.findpaperAutoInvoicebyBusId(bills[4], lstAuthInstId, bills[3]);
				PaperAutoInvoice pa=(PaperAutoInvoice)listbill.get(0);
				//数据回传
				bills[4]=Integer.toString(Integer.parseInt(bills[4])+1);
				pageTaxInvoiceService.updateissueinvoiceNum(pa.getInvoiceBeginNo(), pa.getInvoiceEndNo(), bills[3],bills[4]);
				//加1
				pageTaxInvoiceService.updateDistributeAfterIssue(bills[3], pa.getInvoiceBeginNo(),pa.getInvoiceEndNo(),bills[4]);
				pageTaxInvoiceService.updateStorckdetialAfterIssue(bills[4], pa.getInvoiceBeginNo(), bills[3]);
				
				//交易表中的数据
				List transList = billIssueService.findTransByBillId(bills[2]);
				
				//更改交易状态
				if ("3".equals(issueType)) {
					//票据为拆分而来，当交易对应的所有票据状态为已开具更改交易状态
					issueBill = new BillInfo();
					issueBill.setBillId(bills[2]);
					
					if (transList != null && transList.size() == 1){
						TransInfo trans = (TransInfo) transList.get(0);
						if (trans.getBalance().compareTo(new BigDecimal(0)) == 0){
							issueBill = new BillInfo();
							issueBill.setTransId(trans.getTransId());
							List billFromOneTransList = billIssueService.findBillInfoList(issueBill);
							boolean flag = true;
							for (int j=0; j<billFromOneTransList.size(); j++) {
								BillInfo bill1 = (BillInfo) billFromOneTransList.get(j);
								if (!"5".equals(bill1.getDataStatus())) {
									flag = false;
									break;
								}
							}
							if (flag) {
								billIssueService.updateTransInfoStatus("5", bills[2]);
							}
						}
					}
					
				}else {
					billIssueService.updateTransInfoStatus("5", bills[2]);
					//printWriterResult("开具成功!");
				}
				
			} else {
				if (!"7".equals(dataStatus)) {
					issueBill.setDataStatus("7");
					billIssueService.updateBillInfoStatus(issueBill);
				}
				//	printWriterResult("开具失败！");
				string=string+bills[2]+"开具失败!原因是"+bills[7]+"|";
			} 
		}
		string=string.length()==0?"开具成功":string;
		printWriterResult(string);
		//释放锁
		
	}
	
	/**
	 * 请求核心接口
	 * @throws Exception
	 */
	public void invoiceIssueAccessCore() throws Exception {
		System.out.println("进入BillIssueAction--invoiceIssueAccessCore");
		AjaxReturn coreMessage =null;
		String errorMessage = "";
		try{
			if (!sessionInit(true)) {
				errorMessage = "用户失效";			
			}else{
				//获取发票id
				String billId = request.getParameter("billId");
				//获取用户user
				User user = this.getCurrentUser();
				List lstAuthInstId = new ArrayList();
				this.getAuthInstList(lstAuthInstId);
				
				//根据发票id,获取发票信息
				billInfo=billIssueService.findBillInfoById(billId);
				//交易表中的数据
				List transList = billIssueService.findTransInfo(billId);
				String returnStr = billIssueService.invoiceIssueAccessCore(logManagerService, request, user, transList, billInfo,log);
				if(returnStr != null && "1".equals(returnStr.split("\\|")[0])){
					//接口调用失败
					errorMessage = returnStr.split("\\|")[1];
					coreMessage = new AjaxReturn(false, errorMessage);
				}else if(returnStr != null && "0".equals(returnStr.split("\\|")[0])){
					//接口调用成功
					coreMessage = new AjaxReturn(true);
					System.out.println("接口调用成功");

				}else{
					coreMessage = new AjaxReturn(false,"与核心交互返回信息为空");
					System.out.println("与核心交互返回信息为空");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		returnResult(coreMessage);
	}
	
	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
	}
	
	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
		releaseInstLock();
	}

	/**
	 * 开具所有
	 */
	public String issueAllBill() {
		
		return SUCCESS;
	}
	
	/**
	 * 导出
	 * @throws Exception 
	 */
	public void exportIssueBill() throws Exception {
		try {
			User currentUser = this.getCurrentUser();
			billInfo.setUserId(currentUser.getId());
			billInfo.setFapiaoType(null);if (StringUtil.isEmpty(billInfo.getDataStatus())) {
				billInfo.setDataStatus("3,4,7");
			}
			billInfo.setSumAmtBegin(new BigDecimal(0));
			billInfoList = billIssueService.findBillInfoList(billInfo);
			
			StringBuffer fileName = new StringBuffer("发票信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, billInfoList);
			os.flush();
			os.close();
			
			logManagerService.writeLog(request, this.getCurrentUser(), "0016", "发票开具", "销项税管理", "导出发票信息", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "0016", "发票开具", "销项税管理", "导出发票信息", "0");
			log.error("BillIssueAction-exportIssueBill", e);
		}
	}
	
	private void writeToExcel(OutputStream os, List content) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("发票信息", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "投保单号", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "保单号", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "旧保单号", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "申请开票日期", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "客户名称", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "合计金额", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "合计税额", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "价税合计", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "是否手工录入", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "开具类型", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "发票类型", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "状态", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "渠道", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "费用类型", JXLTool.getHeader());
		Label header17 = new Label(i++, 0, "承保日期", JXLTool.getHeader());
		Label header18 = new Label(i++, 0, "缴费频率", JXLTool.getHeader());
		Label header19 = new Label(i++, 0, "数据来源", JXLTool.getHeader());

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
		ws.addCell(header19);
		for(int j = 0; j < 19; j++){
			ws.setColumnView(j, 19);
		}
		int count = 1;
		for(int c = 0; c < content.size(); c++){
			BillInfo bill = (BillInfo)content.get(c);
			int column = count++;
			setWritableSheet(ws, bill, column);
		}
		wb.write();
		wb.close();
		
	}


	private void setWritableSheet(WritableSheet ws, BillInfo bill, int column) throws Exception {
		int i = 0;
		Label cell1 = new Label(i++, column, column + "", JXLTool.getContentFormat());
		//投保单号
		Label cell2 = new Label(i++, column, billInfo.getTtmpRcno(), JXLTool.getContentFormat());
		//保单号
		Label cell3 = new Label(i++, column, billInfo.getCherNum(), JXLTool.getContentFormat());
		//旧保单号
		Label cell4 = new Label(i++, column, billInfo.getRepNum(), JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, bill.getApplyDate(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, bill.getCustomerName(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, bill.getCustomerTaxno(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, NumberUtils.format(bill.getAmtSum(), "", 2), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, NumberUtils.format(bill.getTaxAmtSum(), "", 2), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, NumberUtils.format(bill.getSumAmt(), "", 2), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, DataUtil.getIsHandiworkCH(bill.getIsHandiwork()), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, DataUtil.getIssueTypeCH(bill.getIssueType()), JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, DataUtil.getFapiaoTypeCH(bill.getFapiaoType()), JXLTool.getContentFormat());
		Label cell14 = new Label(i++, column, DataUtil.getDataStatusCH(bill.getDataStatus(), "BILL"), JXLTool.getContentFormat());
		//渠道
		Label cell15 = new Label(i++, column, billInfo.getChanNel(), JXLTool.getContentFormat());
		//费用类型
		Label cell16 = new Label(i++, column, billInfo.getFeeTyp(), JXLTool.getContentFormat());
		//承保日期
		Label cell17 = new Label(i++, column, billInfo.getHissDte(), JXLTool.getContentFormat());
		//缴费频率
		Label cell18 = new Label(i++, column, billInfo.getBillFreq(), JXLTool.getContentFormat());
		//数据来源
		Label cell19 = new Label(i++, column, billInfo.getDsouRce(), JXLTool.getContentFormat());
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

	/**
	 * 撤销
	 */
	public String revokeBill() {
		try {
			for (int i=0; i<selectBillIds.length; i++) {
				String billId = selectBillIds[i].trim();
				BillInfo bill = new BillInfo();
				bill.setBillId(billId);
				bill.setDataStatus("2");
				billIssueService.updateBillInfoStatus(bill);
			}

			User currentUser = this.getCurrentUser();
			this.session.put("currentUser", currentUser);
			logManagerService.writeLog(request, this.getCurrentUser(), "0016", "发票开具", "销项税管理", "撤销选中的发票", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "0016", "发票开具", "销项税管理", "撤销选中的发票", "0");
			log.error("BillIssueAction-revokeBill", e);
		}
		return ERROR;
	}
	
	/**
	 * 查看交易
	 */
	public String viewTrans() {
		try {
			billInfo = new BillInfo();
			billInfo.setBillId((String) this.request.getParameter("billId"));
			this.request.getSession().setAttribute("curPage", Integer.valueOf(this.getCurPage()));

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillIssueAction-viewTrans", e);
		}
		return ERROR;
	}
	
	/**
	 * 查看票样
	 */
	public String viewSample() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = billIssueService.findBillInfoById(billId);
			
			BillItemInfo billItem = new BillItemInfo();
			billItem.setBillId(billId);
			List billItemList = billIssueService.findBillItemByBillId(billId);
			if (billItemList.size() > 9) {
				this.setRESULT_MESSAGE("发票最多包含9条数据。");
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

			logManagerService.writeLog(request, this.getCurrentUser(), "0016", "发票开具", "销项税管理", "查看票样", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "0016", "发票开具", "销项税管理", "查看票样", "0");
			log.error("BillTrackAction-viewSample", e);
		}
		return ERROR;
	}
	public void checkTaxDiskNo(){
		String taxDiskNo=request.getParameter("taxDiskNo");
		this.getCurrentUser().getOrgId();
	}
	
	//select * from U_BASE_INST i where exists (select 1 from VMS_BILL_INFO b where b.instcode = i.inst_id and b.bill_id in ('B201509071923265621902133','B201509071847524811965471','B2015092100000000055'))
	private String getInstLock() {
		String instCode = this.getCurrentUser().getOrgId();
		boolean lock = ThreadLock.getLockState(instCode);
		if (lock) {
			return "lock";
		}
		return null;
	}

	private void releaseInstLock() {
		String instCode = this.getCurrentUser().getOrgId();
		ThreadLock.releaseLock(instCode);
	}

	/**
	 * 导入开票文件并开票
	 *
	 * @return
	 * @throws ParseException
	 */
	public String inmputMatchInfoList() throws ParseException {
		try {
			String flag = request.getParameter("flag");
			if (flag != null && "true".equals(flag)) {
				return "form";
			}
			ServletContext sc = ServletActionContext.getServletContext();
			if (attachment != null) {
				//若文件已存在，删除原文件
				String dir = sc.getRealPath("/WEB-INF");
				System.out.println("存放路径是：" + dir);
				File saveFile = new File(new File(dir), attachmentFileName);
				if (saveFile.exists()) {
					saveFile.delete();
					saveFile = new File(new File(dir), attachmentFileName);
				}
				attachment.renameTo(saveFile);

				DataFileParser dataFileParserUtil = DataFileParserUtil.createDataFileParser(saveFile);
				if (dataFileParserUtil == null) {
					setResultMessages("请选择后缀是【xls】的Excel文件导入.");
					return ERROR;
				}
				if (!dataFileParserUtil.hasNextLine()) {
					setResultMessages("所上传为空文件，请检查文件.");
					return ERROR;
				}
				// 行数据对象
				List temp = new ArrayList();
				// 临时表信息对象
				matchInfoList = new ArrayList();
				String uuId = UUID.randomUUID().toString();
				while (dataFileParserUtil.hasNextLine()) {
					temp = dataFileParserUtil.next();
					TtmPrcnoMatch match = new TtmPrcnoMatch();
					match.setTtmPrcno(temp.get(0) == null || "".equals(temp.get(0)) ? "" : temp.get(0).toString());
					match.setChanNelName(temp.get(1) == null || "".equals(temp.get(1)) ? "" : temp.get(1).toString());
					match.setBraNch(temp.get(2) == null || "".equals(temp.get(2)) ? "" : temp.get(2).toString());
					match.setVtmipId(uuId);
					match.setTtmPrcnoMatchId(UUID.randomUUID().toString());
					matchInfoList.add(match);
				}
				billIssueService.saveMatchInfoList(matchInfoList);
				//查询出已经匹配的票据信息
				List findMatchInfoList = billIssueService.findMatchInfoList(matchInfoList, false);

				//开票数据
				findMatchInfoOpenList = billIssueService.findMatchInfoList(matchInfoList, true);
				for (int i = 0; i < findMatchInfoOpenList.size(); i++) {
					TtmPrcnoMatch m = findMatchInfoOpenList.get(i);
					if (i == (findMatchInfoOpenList.size() - 1)) {
						billId += m.getBillId();
					} else {
						billId += m.getBillId() + ",";
					}
				}
				if (findMatchInfoList != null && findMatchInfoList.size() > 0) {
					TtmPrcnoMatch match = new TtmPrcnoMatch();
					match.setMatchFlag(DataUtil.MATCH_FLAG_1);
					match.setVtmipId(uuId);
					billIssueService.updateMatchInfoList(findMatchInfoList);
				}

			}else{
				this.setRESULT_MESSAGE("文件不可为空!");
				return ERROR;
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}


	public void exportMatchInfoList() throws Exception {
		try {
			TtmPrcnoMatch match = new TtmPrcnoMatch();
			match.setMatchFlag(DataUtil.MATCH_FLAG_2);
			if(beginDate != null && !"".equals(beginDate)){
				match.setCreateBeginTime(DateUtil.parseStringToDate(beginDate));
			}
			if(endDate != null && !"".equals(endDate)){
				match.setCreateEndTime(DateUtil.parseStringToDate(endDate));
			}
			List matchInfoList = billIssueService.findAllMatchInfoList(match);
			StringBuffer fileName = new StringBuffer("未匹配表单号信息表");
			fileName.append(".xls");
			String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			matchInfoListToExcel(os, matchInfoList);
			os.flush();
			os.close();
//            return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
//            return ERROR;
		}
	}


	private void matchInfoListToExcel(OutputStream os, List content) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("未匹配表单号信息", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "投保单号", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "项目名称", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "分公司", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "导入时间", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "匹配标识", JXLTool.getHeader());

		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);

		for (int j = 0; j < 11; j++) {
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			TtmPrcnoMatch match = (TtmPrcnoMatch) content.get(c);
			int column = count++;
			setWritableSheetMatchInfoList(ws, match, column);
		}
		wb.write();
		wb.close();

	}

	private void setWritableSheetMatchInfoList(WritableSheet ws, TtmPrcnoMatch match, int column) throws Exception {
		int i = 0;
		Label cell1 = new Label(i++, column, column + "", JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, match.getTtmPrcno(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, match.getChanNelName(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, match.getBraNch(), JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, DateUtil.parseDateToString(match.getCreateTime(), DateUtil.FORMAT_SECOND), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, DataUtil.MATCH_FLAG_1_CH, JXLTool.getContentFormat());

		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);

	}


	private ParamConfigVmssService paramConfigVmssService;
	private String taxParam;//税控参数
	public BillIssueService getBillIssueService() {
		return billIssueService;
	}

	public void setBillIssueService(BillIssueService billIssueService) {
		this.billIssueService = billIssueService;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}
	
	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}
	
	public List getBillInfoList() {
		return billInfoList;
	}
	
	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public String[] getSelectBillIds() {
		return selectBillIds;
	}

	public void setSelectBillIds(String[] selectBillIds) {
		this.selectBillIds = selectBillIds;
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

	@Override
	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	@Override
	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
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

	public String getCherNum() {
		return cherNum;
	}

	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}

	public String getRepNum() {
		return repNum;
	}

	public void setRepNum(String repNum) {
		this.repNum = repNum;
	}

	public String getTtmpRcno() {
		return ttmpRcno;
	}

	public void setTtmpRcno(String ttmpRcno) {
		this.ttmpRcno = ttmpRcno;
	}

	public String getFeeTyp() {
		return feeTyp;
	}

	public void setFeeTyp(String feeTyp) {
		this.feeTyp = feeTyp;
	}

	public String getBillFreq() {
		return billFreq;
	}

	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
	}

	public String getPolYear() {
		return polYear;
	}

	public void setPolYear(String polYear) {
		this.polYear = polYear;
	}

	public String getHissDte() {
		return hissDte;
	}

	public void setHissDte(String hissDte) {
		this.hissDte = hissDte;
	}

	public String getDsouRce() {
		return dsouRce;
	}

	public void setDsouRce(String dsouRce) {
		this.dsouRce = dsouRce;
	}

	public String getChanNel() {
		return chanNel;
	}

	public void setChanNel(String chanNel) {
		this.chanNel = chanNel;
	}

	public String getPremTerm() {
		return premTerm;
	}

	public void setPremTerm(String premTerm) {
		this.premTerm = premTerm;
	}

	public String getHissBeginDte() {
		return hissBeginDte;
	}

	public void setHissBeginDte(String hissBeginDte) {
		this.hissBeginDte = hissBeginDte;
	}

	public String getHissEndDte() {
		return hissEndDte;
	}

	public void setHissEndDte(String hissEndDte) {
		this.hissEndDte = hissEndDte;
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

	public Date getCreateBeginTime() {
		return createBeginTime;
	}

	public void setCreateBeginTime(Date createBeginTime) {
		this.createBeginTime = createBeginTime;
	}

	public Date getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public List getMatchInfoList() {
		return matchInfoList;
	}

	public void setMatchInfoList(List matchInfoList) {
		this.matchInfoList = matchInfoList;
	}

	public List<TtmPrcnoMatch> getFindMatchInfoOpenList() {
		return findMatchInfoOpenList;
	}

	public void setFindMatchInfoOpenList(List<TtmPrcnoMatch> findMatchInfoOpenList) {
		this.findMatchInfoOpenList = findMatchInfoOpenList;
	}
}
