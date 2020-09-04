package com.cjit.vms.trans.action.invoiceInquerity;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.invoiceInquerity.InvoiceInquerityListInfo;
import com.cjit.vms.trans.model.invoiceInquerity.TransactionAmount;
import com.cjit.vms.trans.service.invoiceInquerity.InvoiceInquerityService;
/**
 * 
 * @ClassName: InvoiceInquerityAction
 * @date:2019-12-12
 * @Description:
 */
public class InvoiceInquerityAction extends DataDealAction {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private static final String PATTREN = "yyyy-MM-dd";
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(PATTREN);

	/**
	 * 1.查询发票申请记录
	 * 
	 * @return
	 */
	public String invoiceInquerityList() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			User currentUser = this.getCurrentUser();
			InvoiceInquerityListInfo invoiceInquerityListInfo = new InvoiceInquerityListInfo();
			invoiceInquerityListInfo.setCreateUserId(currentUser.getId());
			if (getApplyTime() != null) {
				invoiceInquerityListInfo.setApplyTime(DateUtils.toString(getApplyTime(), DateUtils.ORA_DATES_FORMAT));
			} else {
				invoiceInquerityListInfo.setApplyTime(null);
			}
			invoiceInquerityListInfo.setProductType(getProductType());

			List paperInfoList = invoiceInquerityService.getInvoiceInquerityListInfo(invoiceInquerityListInfo,
					paginationList);
			mapVatType = this.vmsCommonService.findCodeDictionary("PRODUCT_TYPE");
			this.request.setAttribute("paperInfoList", paperInfoList);
			this.request.setAttribute("mapVatType", paperInfoList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPageInvoice", e);
		}
		return ERROR;
	}

	/**
	 * 2.新增发票申请单信息，获取应纳税服务名称
	 * 
	 * @return
	 */
	public String addInvoiceInquerity() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		mapVatType = this.vmsCommonService.findCodeDictionary("PRODUCT_TYPE");
		this.setOperType("add");
		return SUCCESS;
	}

	/**
	 * 3. 编辑申请单信息
	 * 
	 * @return
	 */
	public String editInvoiceInquerity() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		mapVatType = this.vmsCommonService.findCodeDictionary("PRODUCT_TYPE");
		this.setOperType("edit");
		return SUCCESS;
	}

	/**
	 * 4.保存申请单信息
	 * 
	 * @return
	 */
	public String saveInvoiceInquerity() {
		PrintWriter out = null;
		String formStatus = null;
		String operType = request.getParameter("operType");
		String message="";
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		if ("edit".equals(operType)) {
			invoiceInquerityInfo.setApplicationFormId(this.getApplicationFormId());
		}
		User currentUser = this.getCurrentUser();
		try {
			String applyTime = DateUtils.toString(this.getApplyTime(), DateUtils.DATE_FORMAT_YYYYMM);
			// 1. 获取数仓数据，校验申请日期的金额是否正确
			List<TransactionAmount> transactionAmount = invoiceInquerityService.getAmountByChannelAndMonth(this.getProductType(), applyTime);
			if (transactionAmount.size() == 0) {
				this.setResultMessages("没有" + applyTime + "的交易数据");
				formStatus = "0";
			}else {
				TransactionAmount amount =transactionAmount.get(0);
				if (amount.getTransactionAmount().equals(this.getAmount())) {
					this.setResultMessages(applyTime + "的交易数据总金额与发票申请表一致");
					formStatus = "1";
				} else {
					this.setResultMessages(applyTime + "的交易数据总金额与发票申请表不一致，请线下核对");
					formStatus = "0";
				}
			}
			
			// 2. 获取校验结果,保存校验结果
			invoiceInquerityInfo.setApplyTime(DateUtils.toString(getApplyTime(), DateUtils.ORA_DATES_FORMAT));
			invoiceInquerityInfo.setProductType(getProductType());
			invoiceInquerityInfo.setAmount(getAmount());
			invoiceInquerityInfo.setTaxRate(getTaxRate());
			invoiceInquerityInfo.setAmountTax(getAmountTax());
			invoiceInquerityInfo.setTotalPriceandTax(getTotalPriceandTax());
			invoiceInquerityInfo.setFormStatus(formStatus);
			invoiceInquerityInfo.setCreateUserId(currentUser.getId());
			invoiceInquerityService.saveInvoiceInquerityInfo(invoiceInquerityInfo);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	// 删除
	public void deleteInvoiceInquerity() {
		PrintWriter out = null;
		try {
			String applicationFormId = request.getParameter("applicationFormId");
			invoiceInquerityService.deleteInvoiceInquerity(applicationFormId);
			out = response.getWriter();
			out.print("Y");
			out.close();
		} catch (Exception e) {
			out.print("N");
			out.close();
		    
			e.printStackTrace();
		}
	}

	protected InvoiceInquerityService invoiceInquerityService;

	public InvoiceInquerityListInfo invoiceInquerityInfo = new InvoiceInquerityListInfo();// 发票申请单
	// 页面：条件筛选
	private Date applyTime; // 申请日期
	private String applyName;// 申请人
	private String invoiceType;// 发票类型
	private Map mapVatType;
	private String applicationFormId;// 申请表id
	private String productType; // 应税服务名称
	private String customerNo; // 纳税人识别号
	private String customerCname; // 纳税人识别号
	private BigDecimal amount = new BigDecimal("0"); // 金额
	private BigDecimal amountTax = new BigDecimal("0"); // 税额
	private BigDecimal totalPriceandTax = new BigDecimal("0"); // 价税合计
	private BigDecimal taxRate = new BigDecimal("0"); // 税率
	private String formStatus; // 申请表单状态0是校验失败1是校验成功
	private String monthly; // 月度
	private String taxpayerNo; // 纳税人识别号
	private String createTime; // 创建时间
	private String createUserId; // 操作人ID
	/** 操作类型 add,edit */
	private String operType;
	private String RESULT_MESSAGE;

	public void setResultMessages(String resultMessages) {

		try {

			this.RESULT_MESSAGE = java.net.URLEncoder.encode(resultMessages, "UTF-8");
			request.setAttribute("RESULT_MESSAGE", RESULT_MESSAGE);
			request.setAttribute("resultMessages", resultMessages);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

	}

	public String getRESULT_MESSAGE() {
		return RESULT_MESSAGE;
	}

	public void setRESULT_MESSAGE(String rESULT_MESSAGE) {
		RESULT_MESSAGE = rESULT_MESSAGE;
	}

	public String getApplicationFormId() {
		return applicationFormId;
	}

	public void setApplicationFormId(String applicationFormId) {
		this.applicationFormId = applicationFormId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerCname() {
		return customerCname;
	}

	public void setCustomerCname(String customerCname) {
		this.customerCname = customerCname;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountTax() {
		return amountTax;
	}

	public void setAmountTax(BigDecimal amountTax) {
		this.amountTax = amountTax;
	}

	public BigDecimal getTotalPriceandTax() {
		return totalPriceandTax;
	}

	public void setTotalPriceandTax(BigDecimal totalPriceandTax) {
		this.totalPriceandTax = totalPriceandTax;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}

	public String getMonthly() {
		return monthly;
	}

	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}

	public String getTaxpayerNo() {
		return taxpayerNo;
	}

	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public Map getMapVatType() {
		return mapVatType;
	}

	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public InvoiceInquerityListInfo getInvoiceInquerityInfo() {
		return invoiceInquerityInfo;
	}

	public InvoiceInquerityService getInvoiceInquerityService() {
		return invoiceInquerityService;
	}

	public void setInvoiceInquerityService(InvoiceInquerityService invoiceInquerityService) {
		this.invoiceInquerityService = invoiceInquerityService;
	}

	public void setInvoiceInquerityInfo(InvoiceInquerityListInfo invoiceInquerityInfo) {
		this.invoiceInquerityInfo = invoiceInquerityInfo;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

}
