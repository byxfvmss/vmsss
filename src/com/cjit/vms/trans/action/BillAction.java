package com.cjit.vms.trans.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.SpecialTicket;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.BillTrackService;
import com.cjit.vms.trans.service.TaxDiskInfoService;

public class BillAction extends DataDealAction {
	protected static final long serialVersionUID = 1L;
	protected List billInfoList;
	protected BillInfo billInfo = new BillInfo();
	protected BillInfo bill = new BillInfo();
	protected BillItemInfo billItem = new BillItemInfo();
	protected TransInfo transInfo = new TransInfo();
	protected String[] selectBillIds;
	protected String submitFlag;
	protected String message;
	protected String flag;
	protected String filePath;
	protected BigDecimal amtSumBegin;// 合计金额
	protected BigDecimal amtSumEnd;// 合计金额
	protected String billCode;
	protected String billNo;
	protected String billId;
	protected Customer customer = new Customer();
	protected String reasionInfo;//ys
	protected BillTrackService billTrackService;//ys
	protected List transInfoList;//ys
	protected List customerList = new ArrayList();
	protected List amtList = new ArrayList();
	protected ParamConfigVmssService paramConfigVmssService;
	protected BillIssueService billIssueService;
	// 交易类型列表
	protected List businessList = new ArrayList();
	protected List authInstList = new ArrayList();
	protected String instCode;
	protected TaxDiskInfoService taxDiskInfoService;
	// 生成XML的一些开头标签
	protected static final String IN = "IN";
	protected static final String Kp_ELEMENT = "Kp";
	protected static final String Fpxx_ELEMENT = "Fpxx";
	protected static final String Zsl_ELEMENT = "Zsl";
	protected static final String Fpsj_ELEMENT = "Fpsj";
	protected static final String Fp_ELEMENT = "Fp";

	// xhy start
	protected RedReceiptApplyInfo applyInfo;
	protected String billBeginDate;
	protected String billEndDate;
	protected String transBeginDate;
	protected String transEndDate;
	protected SpecialTicket specialTicket;
	protected int printLimitValue;//单次打印限值
	
	protected String customerId; // 客户号
	protected String faPiaoType; // 发票类型
	protected Map mapVatType; // 发票类型List
	protected Map mapGoodsList; // 商品名称List
	protected Map mapTaxList; // 是否含税List
	protected String RESULT_MESSAGE;
	protected String updFlg; // 新增修改flag。                  0：新增，1：修改
	protected String taxId; // 税目ID
	protected String taxRate; // 税率
	protected String taxParam;//税控参数
	protected String goodsParams;
	
	public String getTaxParam() {
		return taxParam;
	}

	public void setTaxParam(String taxParam) {
		this.taxParam = taxParam;
	}

	public String getGoodsParams() {
		return goodsParams;
	}

	public void setGoodsParams(String goodsParams) {
		this.goodsParams = goodsParams;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public List getAuthInstList() {
		return authInstList;
	}

	public TaxDiskInfoService getTaxDiskInfoService() {
		return taxDiskInfoService;
	}

	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}

	public void setTaxDiskInfoService(TaxDiskInfoService taxDiskInfoService) {
		this.taxDiskInfoService = taxDiskInfoService;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	
	public List getBillInfoList() {
		return billInfoList;
	}

	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public BillItemInfo getBillItem() {
		return billItem;
	}

	public void setBillItem(BillItemInfo billItem) {
		this.billItem = billItem;
	}

	public String getSubmitFlag() {
		return submitFlag;
	}

	public void setSubmitFlag(String submitFlag) {
		this.submitFlag = submitFlag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String[] getSelectBillIds() {
		return selectBillIds;
	}

	public void setSelectBillIds(String[] selectBillIds) {
		this.selectBillIds = selectBillIds;
	}

	public String getReasionInfo() {
		return reasionInfo;
	}

	public void setReasionInfo(String reasionInfo) {
		this.reasionInfo = reasionInfo;
	}

	public List getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List businessList) {
		this.businessList = businessList;
	}

	public BigDecimal getAmtSumBegin() {
		return amtSumBegin;
	}

	public void setAmtSumBegin(BigDecimal amtSumBegin) {
		this.amtSumBegin = amtSumBegin;
	}

	public BigDecimal getAmtSumEnd() {
		return amtSumEnd;
	}

	public void setAmtSumEnd(BigDecimal amtSumEnd) {
		this.amtSumEnd = amtSumEnd;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public RedReceiptApplyInfo getApplyInfo() {
		return applyInfo;
	}

	public void setApplyInfo(RedReceiptApplyInfo applyInfo) {
		this.applyInfo = applyInfo;
	}

	public String getBillBeginDate() {
		return billBeginDate;
	}

	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getTransBeginDate() {
		return transBeginDate;
	}

	public void setTransBeginDate(String transBeginDate) {
		this.transBeginDate = transBeginDate;
	}

	public String getTransEndDate() {
		return transEndDate;
	}

	public void setTransEndDate(String transEndDate) {
		this.transEndDate = transEndDate;
	}

	public SpecialTicket getSpecialTicket() {
		return specialTicket;
	}

	public void setSpecialTicket(SpecialTicket specialTicket) {
		this.specialTicket = specialTicket;
	}

	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public List getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List customerList) {
		this.customerList = customerList;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
