package com.cjit.vms.taxServer.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cjit.crms.util.date.DateUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.vms.taxServer.model.BillIssue;
import com.cjit.vms.taxServer.model.Product;
import com.cjit.vms.taxServer.model.parseXMl.BillIssueReturnXml;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.service.BillInfoService;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.redRecipt.RedReceiptApplyInfoService;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;
public class BillIssueSevletAction extends BaseTaxSelverAction{
	private static final long serialVersionUID = 1L;
	private BillIssueService billIssueService;
	private BillIssue billIssue=new BillIssue();
	private BillInfo billInfo=new BillInfo();
	/**//**
	 * @throws Exception
	 *             开具数据模型的构造 CreatebillIssueSelverXml
	 */
	public void CreatebillIssueSelverXml() throws Exception {
		String billIds = request.getParameter("billIds");
		String fapiaoType = getfapiaoType(request.getParameter("fapiaoType"));
		String IssueType=request.getParameter("IssueType");
		
		BillInfo bill = billIssueService.findBillInfoById(billIds);
		billIssue.setAmtsum(bill.getAmtSum().toString());
		billIssue.setApplyTypeCode(TaxSelvetUtil.apply_type_ch);
		billIssue.setBillTerminalFlag(TaxSelvetUtil.bill_TerminalFlag_ch);
		billIssue.setComment(TaxSelvetUtil.comment_bill_issue);
		billIssue.setCustomerAddressphone(bill.getCustomerAddressandphone());
		billIssue.setCustomerBankaccount(bill.getCustomerBankandaccount());
		billIssue.setCustomerName(bill.getCustomerName());
		billIssue.setCustomerTaxno(bill.getCustomerTaxno());
		billIssue.setDetailedFalg(TaxSelvetUtil.Detailed_Falg_0);
		billIssue.setDrawer(this.getCurrentUser().getName());
		billIssue.setFapiaoRequestNo(TaxSelvetUtil.Fapiao_Request_No_);
		billIssue.setFapiaoTypeCode(fapiaoType);
		billIssue.setId(TaxSelvetUtil.id_Issue);
		billIssue.setInformationtablenumber(TaxSelvetUtil.information_table_number_);
		billIssue.setIssueBillTppe(IssueType);
		billIssue.setOriginalinvoicecode(bill.getOriBillCode());
		billIssue.setOriginalinvoiceno(bill.getOriBillNo());
		billIssue.setPayee(bill.getPayee());
		billIssue.setRemark(bill.getRemark());
		billIssue.setReviewer(bill.getReviewerName());
		billIssue.setSaleAddressPhone(bill.getAddressandphone());
		billIssue.setSaleBankAccount(bill.getBankandaccount());
		billIssue.setSaleName(bill.getName());
		billIssue.setSaleTaxno(bill.getTaxno());
		billIssue.setSignatureparam(TaxSelvetUtil.Signature_param_);
		billIssue.setSpecialFalg(TaxSelvetUtil.Special_Falg_00);
		billIssue.setSumamt(bill.getSumAmt().toString());
		billIssue.setTaxamtsum(bill.getTaxAmtSum().toString());
		List billItemList = billIssueService.findBillItemByBillId(billIds);
		List list=new ArrayList();
		Product product=null;
		for(int i=0;i<billItemList.size();i++){
			product=new Product();
			BillItemInfo item = (BillItemInfo) billItemList.get(i);
			product.setAmt(item.getAmt().toString());
			product.setBillroProperty(TaxSelvetUtil.Bill_row_Property_0);
			product.setPrice(item.getGoodsPrice().toString());
			product.setProductName(item.getGoodsName());
			product.setProductNumber(item.getGoodsNo().toString());
			product.setProductTax(TaxSelvetUtil.Product_Tax_);
			product.setRate(item.getTaxRate().toString());
			product.setSpecification(item.getSpecandmodel());
			product.setTaxamt(item.getTaxAmt().toString());
			product.setTaxlogo(TaxSelvetUtil.Tax_logo_0);
			product.setUnit(item.getGoodsUnit());
			list.add(product);
		}
		billIssue.setListProduct(list);
		String result=billIssue.createBillIssueXml();
		printWriterResult(result);
	}
	/*SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
	issueBill.setDataStatus("5");
	issueBill.setTaxDiskNo(bills[1]);
	issueBill.setBillCode(bills[3]);
	issueBill.setBillNo(bills[4]);
	issueBill.setBillDate(bills[5] + " " + df.format(new Date()));
	issueBill.setMachineNo(bills[6]);
	issueBill.setDrawer(this.getCurrentUser().getId());*/
	/*billIssueService.updatebillInfoIssueResult(issueBill);*/
	public void praseBillIssueReturnXml() throws Exception{
		String issueRes = request.getParameter("issueRes");
		String fapiaoType = request.getParameter("fapiaoType").equals("0")?"004":"007";
		String id=request.getParameter("billId");
		BillIssue bill=new BillIssue();
		bill.setFapiaoTypeCode(fapiaoType);
		BillIssueReturnXml returnXml=bill.ParserBillIssueXml(issueRes);
		bill.outBillIssueXmlFile(issueRes);
		String result="";
		BillInfo bil=billIssueService.findBillInfoById(id);
	if (returnXml.getReturncode().equals("0")) {
			String issueType = bil.getIssueType();
			billInfo.setBillCode(returnXml.getBillCode());
			billInfo.setBillNo(returnXml.getBillNo());
			billInfo.setBillDate(DateUtils.toString(DateUtils.stringToDate(returnXml.getBillIssueDate(),"yyyyMMddHHmmss"),"yyyy-MM-dd HH:mm:ss"));
			billInfo.setBillId(id);
			billInfo.setDataStatus("5");
			billIssueService.updatebillInfoIssueResult(billInfo);
			//数据回传
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			String billNo=Integer.parseInt(returnXml.getBillNo())+1+"";
			List listbill=pageTaxInvoiceService.findpaperAutoInvoicebyBusId(returnXml.getBillNo(), lstAuthInstId,returnXml.getBillCode());
			PaperAutoInvoice pa=(PaperAutoInvoice)listbill.get(0);
			pageTaxInvoiceService.updateissueinvoiceNum(pa.getInvoiceBeginNo(), pa.getInvoiceEndNo(), returnXml.getBillCode(),billNo);
			//加1
			pageTaxInvoiceService.updateDistributeAfterIssue(returnXml.getBillCode(), pa.getInvoiceBeginNo(),pa.getInvoiceEndNo(),billNo);
			pageTaxInvoiceService.updateStorckdetialAfterIssue(billNo, pa.getInvoiceBeginNo(), returnXml.getBillCode());
			//为拆分而来
			if("3".equals(issueType)){
				
				List transList = billIssueService.findTransByBillId(id);
				if (transList != null && transList.size() == 1){
					TransInfo trans = (TransInfo) transList.get(0);
					if (trans.getBalance().compareTo(new BigDecimal(0)) == 0){
						bil = new BillInfo();
						bil.setTransId(trans.getTransId());
						List billFromOneTransList = billIssueService.findBillInfoList(bil);
						boolean flag = true;
						for (int j=0; j<billFromOneTransList.size(); j++) {
							BillInfo bill1 = (BillInfo) billFromOneTransList.get(j);
							if (!"5".equals(bill1.getDataStatus())) {
								flag = false;
								break;
							}
						}
						if (flag) {
							billIssueService.updateTransInfoStatus("99", id);
						}
					}
				}
			}else{
			billIssueService.updateTransInfoStatus("99", id);
			}
			result = "success";
		} else {
			result = returnXml.getReturnmsg();
			bil.setDataStatus("7");
			billIssueService.updateBillInfoStatus(bil);
		}

		System.out.println(issueRes);
		printWriterResult(result);
	}
	public void praseRedBillIssueReturnXml() throws Exception{
		String issueRes = request.getParameter("issueRes");
		String fapiaoType = request.getParameter("fapiaoType").equals("0")?"004":"007";
		String id=request.getParameter("billId");
		BillIssue bill=new BillIssue();
		bill.setFapiaoTypeCode(fapiaoType);
		BillIssueReturnXml returnXml=bill.ParserBillIssueXml(issueRes);
		bill.outBillIssueXmlFile(issueRes);
		String result="";
		BillInfo bil=billIssueService.findBillInfoById(id);
		if (returnXml.getReturncode().equals("0")) {
			String issueType = bil.getIssueType();
			billInfo.setBillCode(returnXml.getBillCode());
			billInfo.setBillNo(returnXml.getBillNo());
			billInfo.setBillDate(DateUtils.toString(DateUtils.stringToDate(returnXml.getBillIssueDate(),"yyyyMMddHHmmss"),"yyyy-MM-dd HH:mm:ss"));
			billInfo.setBillId(id);
			billInfo.setDataStatus("5");
			//billIssueService.updatebillInfoIssueResult(billInfo);
			redReceiptApplyInfoService.updatebillInfoIssueResult(billInfo);
			//为拆分而来
			if("3".equals(issueType)){
				
				/*List transList = billIssueService.findTransByBillId(id);
				if (transList != null && transList.size() == 1){
					TransInfo trans = (TransInfo) transList.get(0);
					if (trans.getBalance().compareTo(new BigDecimal(0)) == 0){
						bil = new BillInfo();
						bil.setTransId(trans.getTransId());
						List billFromOneTransList = billIssueService.findBillInfoList(bil);
						boolean flag = true;
						for (int j=0; j<billFromOneTransList.size(); j++) {
							BillInfo bill1 = (BillInfo) billFromOneTransList.get(j);
							if (!"5".equals(bill1.getDataStatus())) {
								flag = false;
								break;
							}
						}
						if (flag) {
							billIssueService.updateTransInfoStatus("99", id);
						}
					}
				}*/
				
			}else{
				//billIssueService.updateTransInfoStatus("99", id);
				redReceiptApplyInfoService.updateTransInfoStatus("99", id);
			}
			result = "success";
		} else {
			result = returnXml.getReturnmsg();
			bil.setDataStatus("7");
			billIssueService.updateBillInfoStatus(bil);
		}
		
		System.out.println(issueRes);
		printWriterResult(result);
	}
	
	
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
	
	private RedReceiptApplyInfoService redReceiptApplyInfoService;
	public RedReceiptApplyInfoService getRedReceiptApplyInfoService() {
		return redReceiptApplyInfoService;
	}
	public void setRedReceiptApplyInfoService(
			RedReceiptApplyInfoService redReceiptApplyInfoService) {
		this.redReceiptApplyInfoService = redReceiptApplyInfoService;
	}
	private PageTaxInvoiceService pageTaxInvoiceService;
	public PageTaxInvoiceService getPageTaxInvoiceService() {
		return pageTaxInvoiceService;
	}
	public void setPageTaxInvoiceService(PageTaxInvoiceService pageTaxInvoiceService) {
		this.pageTaxInvoiceService = pageTaxInvoiceService;
	}
	

}
