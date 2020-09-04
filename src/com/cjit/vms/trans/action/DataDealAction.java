package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cjit.crms.util.StringUtil;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.vms.input.service.InputVatInfoService;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.system.service.BaseDataService;
import com.cjit.vms.system.service.BusinessService;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.system.service.SystemDataService;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.trans.service.BillInfoService;
import com.cjit.vms.trans.service.BillPrintHistoryService;
import com.cjit.vms.trans.service.BillSupplementPrintService;
import com.cjit.vms.trans.service.EmsInfoService;
import com.cjit.vms.trans.service.ImpDataService;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.service.billInvalid.BillCancelService;
import com.cjit.vms.trans.service.invoiceInquerity.InvoiceInquerityService;
import com.cjit.vms.trans.service.seo.SEOTransInfoService;
import com.cjit.vms.trans.service.storage.PaperInvoiceService;

public class DataDealAction extends BaseListAction {

	private static final long serialVersionUID = 1L;
	protected LogManagerService logManagerService;
	protected TransInfoService transInfoService;
	protected BillInfoService billInfoService;
	protected InputVatInfoService inputVatInfoService;
	protected BaseDataService baseDataService;
	protected BusinessService businessService;
	protected CustomerService customerService;
	protected OrganizationService organizationService;
	protected UserInterfaceConfigService userInterfaceConfigService;
	protected SystemDataService systemDataService;
	protected PaperInvoiceService paperInvoiceService;
	protected EmsInfoService emsInfoService;
	protected BillCancelService billCancelService;
	protected BillSupplementPrintService billSupplementPrintService;

	protected VmsCommonService vmsCommonService;
	protected ImpDataService impDataService;
	
	
	protected SEOTransInfoService seoTransInfoService;
	
	//发票补打信息
	private BillPrintHistoryService bphService;

	protected String fromFlag;
	protected String instCode;
	// 用于生成ID的属性
	private static int busFlag = 1;
	private static String timeStamp = "";
	// 受权机构列表
	private List authInstList = new ArrayList();
	// 纳税人识别号列表
	private List taxperList = new ArrayList();
	private List taxperLists = new ArrayList();
	private String taxperNumber; // 纳税人识别号，即税务登记号
	private String taxperName; // 纳税人名称
	// 交易起止日期
	private String transBeginDate;
	private String transEndDate;
	// 开票起止日期
	private String billBeginDate;
	private String billEndDate;
	// 票据价税合计金额范围
	private BigDecimal sumAmtBegin;
	private BigDecimal sumAmtEnd;
	// 甲方客户代码
	private String projectCode;
	private String RESULT_MESSAGE;

	public void setTaxperList(List taxperList) {
		this.taxperList = taxperList;
	}

	public List getTaxperLists() {
		return taxperLists;
	}

	public void setTaxperLists(List taxperLists) {
		this.taxperLists = taxperLists;
	}

	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}

	/**
	 * 生成业务主键（在插入数据时需要，由时间戳生成）
	 * 
	 * @return String
	 */
	protected String createBusinessId(String tabFlag) {
		if (tabFlag == null) {
			tabFlag = "";
		}
		String temp = DateUtils.serverCurrentTimeStamp();
		long random = Math.round(Math.random() * 110104 + 100000);
		if (!timeStamp.equals(temp)) {
			timeStamp = temp;
			busFlag = 1;
			return tabFlag + timeStamp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		} else {
			return tabFlag + temp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		}
	}

	protected String createBillId(String tabFlag) {
		String temp = DateUtils.serverCurrentDetailDate();
		String sequence = transInfoService.getBillIdSequence();
		return tabFlag + temp + sequence;
	}

	protected boolean sessionInit(boolean getBusinessIdFromSession) {
		// 受权机构列表
		try {
			getAuthInstList(authInstList);
		} catch (java.lang.NullPointerException npex) {
			return false;
		}
		if (CollectionUtil.isEmpty(authInstList)) {
			return false;
		}
		if (instCode == null) {
			instCode = (String) this
					.getFieldFromSession(ScopeConstants.CURRENT_INST_CODE);
			if (instCode == null || "".equals(instCode)) {
				instCode = ((Organization) authInstList.get(0)).getId();
			}
		}
		return true;
	}

	protected void getAuthInstList(List authInstList) {
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		if (currentUser != null) {
			if (authInstList == null) {
				authInstList = new ArrayList();
			}
			authInstList.clear();
			authInstList.addAll(currentUser.getOrgs());
		} else {
			throw new java.lang.NullPointerException();
		}
	}
	public String getUserTaxNumber(String orgId) {
		Organization org = new Organization();
		org.setId(orgId);
		org = this.baseDataService.getOrganization(org);
		if (null != org) {
			return org.getTaxperNumber();
		}
		return "";
	}

	/**
	 * 纳税人识别号列表的取得
	 * 
	 * @return
	 */
	public List getTaxperList() {
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		if (currentUser != null) {
			if (taxperList == null) {
				taxperList = new ArrayList();
			}
			taxperList.clear();

			List lst = currentUser.getOrgs();
			List taxperListNo = new ArrayList();
			List taxperNumberList = new ArrayList();
			if (lst != null && lst.size() > 0) {
				int k = 0;
				for (int i = 0; i <= lst.size() - 1; i++) {
					Organization user = (Organization) lst.get(i);
					if (user.getTaxperNumber() != null
							&& !"".equals(user.getTaxperNumber())
							&& !taxperNumberList.contains(user
									.getTaxperNumber())) {
						taxperNumberList.add(user.getTaxperNumber());
						taxperListNo.add(k, user);
						k++;
					}
				}
			}

			taxperList.addAll(taxperListNo);
		} else {
			taxperList = new ArrayList();
		}
		return taxperList;
	}

	/**
	 * 纳税人名称的取得
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void getAjaxTaxperName() throws IOException {
		String taxperName = null;
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		if (currentUser != null && getTaxperNumber() != null
				&& !"".equals(getTaxperNumber())) {
			List lst = currentUser.getOrgs();
			if (lst != null && lst.size() > 0) {
				for (int i = 0; i <= lst.size() - 1; i++) {
					Organization user = (Organization) lst.get(i);
					if (getTaxperNumber().equals(user.getTaxperNumber())) {
						taxperName = user.getTaxperName();
						break;
					}
				}
			}
		}

		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(taxperName);
		out.close();
	}

	/**
	 * 纳税人名称的取得
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void getAjaxTaxperNameList() throws IOException {
		Organization info = new Organization();
		List nameList = null;
		String layer = "";
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		if (currentUser != null && getTaxperNumber() != null) {
			List lst = currentUser.getOrgs();
			String number = "";
			if (lst != null && lst.size() > 0) {
				String arrInstId = "";
				for (int i = 0; i <= lst.size() - 1; i++) {
					Organization user = (Organization) lst.get(i);
					arrInstId = arrInstId + "'" + user.getId() + "',";
					if (user.getTaxperNumber() != null
							&& !"".equals(user.getTaxperNumber())) {
						number = number + "'" + user.getTaxperNumber() + "',";
					}
				}
				if (arrInstId != null && !"".equals(arrInstId)) {
					arrInstId = arrInstId.substring(0, arrInstId.length() - 1);
					info.setId(" in (" + arrInstId + ")");
				} else {
					info.setId(" in ('')");
				}
			}

			if (getTaxperNumber() != null && !"".equals(getTaxperNumber())) {
				info.setTaxperNumber(" in ('" + getTaxperNumber() + "')");
			} else {
				if (number != null && !"".equals(number)) {
					number = number.substring(0, number.length() - 1);
					info.setTaxperNumber(" in (" + number + ")");
				}
			}

			if (getTaxperName() != null && !"".equals(getTaxperName())) {
				info.setTaxperName(" like '%" + getTaxperName() + "%'");
			}
			nameList = vmsCommonService.getAjaxTaxperNameList(info);
		}

		if (nameList != null && nameList.size() > 0) {
			layer = "<table id='taxperNameId'>";
			for (int i = 0; i <= nameList.size() - 1; i++) {
				Organization user = (Organization) nameList.get(i);
				layer += "<tr class='line'><td class='std'>"
						+ user.getTaxperName() + "</td></tr>";
			}
			layer += "</table>";
		}

		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(layer);
		out.close();
	}

	/**
	 * 
	 * @param billId
	 *            原票据ID
	 * @param noticeNo
	 *            通知单编号
	 * @param remark
	 *            备注
	 * @param billItemList
	 *            票据明细信息
	 * @param cancelGoodsNos
	 *            票据明细红冲数量
	 * @param discountRates
	 *            票据折扣率
	 * @param billDataStatus
	 *            红冲票据状态
	 * @return boolean
	 */
	protected boolean writeOffBill(String billId, String noticeNo,
			String remark, List billItemList, String[] cancelGoodsNos,
			String[] discountRates, String billDataStatus) {
		try {
			if (cancelGoodsNos == null) {
				cancelGoodsNos = new String[billItemList.size()];
			}
			// 红冲票据ID
			String cancelBillId = createBillId("B");
			if (StringUtil.isNotEmpty(billId)
					&& StringUtil.isNotEmpty(noticeNo)
					&& CollectionUtil.isNotEmpty(billItemList)) {
				// 查询原票据信息
				BillInfo bill = this.billInfoService.findBillInfo(billId);
				String oriBillCode = bill.getBillCode();
				String oriBillNo = bill.getBillNo();
				String oriRemark = bill.getRemark();
				// 本次红冲涉及票据明细信息
				List cancelBillItemList = new ArrayList();
				// 红冲总金额、税额
				BigDecimal amtSum = new BigDecimal(0.00);
				BigDecimal taxAmtSum = new BigDecimal(0.00);
				// 查询票据明细信息
				for (int i = 0; i < billItemList.size(); i++) {
					BillItemInfo oriBillItem = (BillItemInfo) billItemList
							.get(i);
					if (oriBillItem != null
							&& (StringUtil.IsEmptyStr(cancelGoodsNos[i]) || new BigDecimal(
									cancelGoodsNos[i])
									.compareTo(new BigDecimal(0)) != 0)) {
						// 票据明细数量发生变化,当前票据在本次红冲范围内
						BillItemInfo billItem = new BillItemInfo();
						billItem.setBillId(cancelBillId);
						billItem.setBillItemId(createBusinessId("BI"));
						// billItem.setTransType(oriBillItem.getTransType());
						billItem.setRowNature(oriBillItem.getRowNature());
						billItem.setGoodsName(oriBillItem.getGoodsName());
						if (StringUtil.IsEmptyStr(cancelGoodsNos[i])) {
							// 按原明细数两红冲
							billItem.setGoodsNo(oriBillItem.getGoodsNo());
						} else {
							billItem.setGoodsNo(new BigDecimal(
									cancelGoodsNos[i]));
						}
						BigDecimal cancelAmt = oriBillItem
								.getAmt()
								.multiply(billItem.getGoodsNo())
								.divide(oriBillItem.getGoodsNo(), 2,
										BigDecimal.ROUND_HALF_UP);
						BigDecimal cancelTaxAmt = oriBillItem
								.getTaxAmt()
								.multiply(billItem.getGoodsNo())
								.divide(oriBillItem.getGoodsNo(), 2,
										BigDecimal.ROUND_HALF_UP);
						if (discountRates != null
								&& discountRates.length == billItemList.size()
								&& StringUtil.isNotEmpty(discountRates[i])
								&& !"1".equals(discountRates[i])) {
							cancelAmt = cancelAmt.multiply(new BigDecimal(
									discountRates[i]));
							cancelTaxAmt = cancelTaxAmt
									.multiply(new BigDecimal(discountRates[i]));
						}
						billItem.setAmt(cancelAmt);
						billItem.setTaxAmt(cancelTaxAmt);
						billItem.setTaxRate(oriBillItem.getTaxRate());
						billItem.setTaxFlag(oriBillItem.getTaxFlag());
						cancelBillItemList.add(billItem);
						amtSum = amtSum.add(cancelAmt);
						taxAmtSum = taxAmtSum.add(cancelTaxAmt);
					}
				}
				bill.setBillId(cancelBillId);// 赋值新红冲票据ID
				bill.setBillCode(null);
				bill.setBillNo(null);
				bill.setBillDate(null);
				bill.setAmtSum(amtSum);
				bill.setTaxAmtSum(taxAmtSum);
				bill.setSumAmt(amtSum.add(taxAmtSum));
				bill.setNoticeNo(noticeNo);
				bill.setOriBillCode(oriBillCode);
				bill.setOriBillNo(oriBillNo);
				// bill.setCancelFlag(DataUtil.HONGCHONG);
				bill.setDataStatus(billDataStatus);
				if (remark != null && !remark.equals(oriRemark)) {
					bill.setRemark(remark);
				}
				this.billInfoService.saveBillInfo(bill, false);
				if (CollectionUtil.isNotEmpty(cancelBillItemList)) {
					for (int i = 0; i < cancelBillItemList.size(); i++) {
						BillItemInfo billItem = (BillItemInfo) cancelBillItemList
								.get(i);
						this.billInfoService.saveBillItemInfo(billItem, false);
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataDealAction-writeOffBill", e);
		}
		return false;
	}

	/**
	 * 从字典表中构造下拉框数据集
	 * 
	 * @param codeType
	 *            字典类型
	 * @param codeSym
	 *            字典对应表单
	 * @param showKey
	 *            是否显示Key值，否则只显示value文字值
	 * @return List<SelectTag>
	 */
	protected List createSelectList(String codeType, String codeSym,
			boolean showKey) {
		try {
			List codeDicList = this.userInterfaceConfigService.getDictionarys(
					codeType, codeSym);
			if (CollectionUtil.isNotEmpty(codeDicList)) {
				List selectTagList = new ArrayList();
				for (Iterator t = codeDicList.iterator(); t.hasNext();) {
					Dictionary codeDic = (Dictionary) t.next();
					if (showKey) {
						SelectTag selectTag = new SelectTag(
								codeDic.getValueStandardLetter(),
								codeDic.getValueStandardLetter() + "-"
										+ codeDic.getName());
						selectTagList.add(selectTag);
					} else {
						SelectTag selectTag = new SelectTag(
								codeDic.getValueStandardLetter(),
								codeDic.getName());
						selectTagList.add(selectTag);
					}
				}
				return selectTagList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataDealAction-createSelectList", e);
		}
		return null;
	}

	/**
	 * 税目校验，验证税率存在与税目信息表中
	 * 
	 * @param transInfo
	 *            交易信息
	 * @param taxperNumber
	 *            纳税人识别号
	 * @param faPiaoType
	 *            发票类型
	 * @return String
	 */
	protected String checkTaxRate(TransInfo transInfo, String taxperNumber,
			String faPiaoType) {

		if (StringUtil.IsEmptyStr(taxperNumber)) {
			// 通过交易记录中bankcode获取对应纳税人识别号
			if (StringUtil.isNotEmpty(transInfo.getBankTaxperNumber())) {
				taxperNumber = transInfo.getBankTaxperNumber();
			} else {
				Organization org = new Organization();
				org.setId(transInfo.getInstCode());
				org = this.baseDataService.getOrganization(org);
				if (org != null && StringUtil.isNotEmpty(org.getTaxperNumber())) {
					taxperNumber = org.getTaxperNumber();
					transInfo.setBankTaxperNumber(taxperNumber);

				}
			}
		}
		if (StringUtil.isNotEmpty(taxperNumber)) {
			// 通过纳税人识别号、发票类型和税率在税目信息表中查询对应记录
			VmsTaxInfo taxInfo = new VmsTaxInfo();
			taxInfo.setUser_id(transInfo.getUserId());
			taxInfo.setTaxno(taxperNumber);
			taxInfo.setFapiaoType(faPiaoType);
			taxInfo.setTaxRate(transInfo.getTaxRate().toString());
			taxInfo = this.baseDataService.findVmsTaxInfo(taxInfo);
			if (taxInfo == null) {
				// 不存在符合条件的税率信息
				return null;
			} else {
				return taxInfo.getTaxId();
			}
		}
		return null;
	}

	/**
	 * 根据交易类型和纳税人识别号获取发票商品信息
	 * 
	 * @param transInfo
	 * @param taxperNumber
	 * @return GoodsInfo
	 */
	protected GoodsInfo findGoodsInfo(TransInfo transInfo, String taxperNumber) {
		GoodsInfo goods = null;
		Organization org = new Organization();
		org.setId(transInfo.getInstCode());
		org = this.baseDataService.getOrganization(org);

		if (StringUtil.IsEmptyStr(taxperNumber)) {
			// 通过交易记录中bankcode获取对应纳税人识别号
			if (StringUtil.isNotEmpty(transInfo.getBankTaxperNumber())) {
				taxperNumber = transInfo.getBankTaxperNumber();
			} else {

				if (org != null && StringUtil.isNotEmpty(org.getTaxperNumber())) {
					taxperNumber = org.getTaxperNumber();
					transInfo.setBankTaxperNumber(taxperNumber);
				}
			}
		}
		if (StringUtil.isNotEmpty(transInfo.getTransType())) {
			goods = new GoodsInfo(null, null, taxperNumber,
					transInfo.getTransType());
			List list = this.baseDataService.findGoodsInfoList(goods);
			if (list != null && list.size() == 1) {
				goods = (GoodsInfo) list.get(0);
			} else {
				return null;
			}
		}
		return goods;
	}

	public LogManagerService getLogManagerService() {
		return logManagerService;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	public TransInfoService getTransInfoService() {
		return transInfoService;
	}

	public void setTransInfoService(TransInfoService transInfoService) {
		this.transInfoService = transInfoService;
	}

	public BillInfoService getBillInfoService() {
		return billInfoService;
	}

	public void setBillInfoService(BillInfoService billInfoService) {
		this.billInfoService = billInfoService;
	}

	public InputVatInfoService getInputVatInfoService() {
		return inputVatInfoService;
	}

	public void setInputVatInfoService(InputVatInfoService inputVatInfoService) {
		this.inputVatInfoService = inputVatInfoService;
	}

	public BaseDataService getBaseDataService() {
		return baseDataService;
	}

	public void setBaseDataService(BaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public OrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public List getAuthInstList() {
		this.getAuthInstList(authInstList);
		return authInstList;
	}

	// public void setAuthInstList(List authInstList) {
	// this.authInstList = authInstList;
	// }

	public String getTransBeginDate() {
		if (transBeginDate == null) {
			// 从session中获取查询起止日期
			String sessionBeginDate = (String) this.request.getSession()
					.getAttribute("transBeginDate");
			if (sessionBeginDate != null && !"".equals(sessionBeginDate)) {
				transBeginDate = sessionBeginDate;
			}
		} else {
			request.getSession().setAttribute("transBeginDate", transBeginDate);
		}
		return transBeginDate;
	}

	public void setTransBeginDate(String transBeginDate) {
		this.transBeginDate = transBeginDate;
	}

	public String getTransEndDate() {
		if (transEndDate == null) {
			// 从session中获取查询起止日期
			String sessionEndDate = (String) this.request.getSession()
					.getAttribute("transEndDate");
			if (sessionEndDate != null && !"".equals(sessionEndDate)) {
				transEndDate = sessionEndDate;
			}
		} else {
			request.getSession().setAttribute("transEndDate", transEndDate);
		}
		return transEndDate;
	}

	public void setTransEndDate(String transEndDate) {
		this.transEndDate = transEndDate;
	}

	public String getBillBeginDate() {
		if (billBeginDate == null) {
			// 从session中获取查询起止日期
			String sessionBeginDate = (String) this.request.getSession()
					.getAttribute("billBeginDate");
			if (sessionBeginDate != null && !"".equals(sessionBeginDate)) {
				billBeginDate = sessionBeginDate;
			}
		} else {
			request.getSession().setAttribute("billBeginDate", billBeginDate);
		}
		return billBeginDate;
	}

	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public String getBillEndDate() {
		if (billEndDate == null) {
			// 从session中获取查询起止日期
			String sessionEndDate = (String) this.request.getSession()
					.getAttribute("billEndDate");
			if (sessionEndDate != null && !"".equals(sessionEndDate)) {
				billEndDate = sessionEndDate;
			}
		} else {
			request.getSession().setAttribute("billEndDate", billEndDate);
		}
		return billEndDate;
	}

	public void setResultMessages(String resultMessages) {

		try {

			this.RESULT_MESSAGE = java.net.URLEncoder.encode(resultMessages,
					"UTF-8");
			request.setAttribute("RESULT_MESSAGE", RESULT_MESSAGE);
			request.setAttribute("resultMessages", resultMessages);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getFromFlag() {
		return fromFlag;
	}

	public void setFromFlag(String fromFlag) {
		this.fromFlag = fromFlag;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public BigDecimal getSumAmtBegin() {
		return sumAmtBegin;
	}

	public void setSumAmtBegin(BigDecimal sumAmtBegin) {
		this.sumAmtBegin = sumAmtBegin;
	}

	public BigDecimal getSumAmtEnd() {
		return sumAmtEnd;
	}

	public void setSumAmtEnd(BigDecimal sumAmtEnd) {
		this.sumAmtEnd = sumAmtEnd;
	}

	public SystemDataService getSystemDataService() {
		return systemDataService;
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public PaperInvoiceService getPaperInvoiceService() {
		return paperInvoiceService;
	}

	public void setPaperInvoiceService(PaperInvoiceService paperInvoiceService) {
		this.paperInvoiceService = paperInvoiceService;
	}

	public EmsInfoService getEmsInfoService() {
		return emsInfoService;
	}

	public void setEmsInfoService(EmsInfoService emsInfoService) {
		this.emsInfoService = emsInfoService;
	}

	public BillCancelService getBillCancelService() {
		return billCancelService;
	}

	public void setBillCancelService(BillCancelService billCancelService) {
		this.billCancelService = billCancelService;
	}

	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
	}

	public String getTaxperNumber() {
		return taxperNumber;
	}

	public void setTaxperNumber(String taxperNumber) {
		this.taxperNumber = taxperNumber;
	}

	public String getTaxperName() {
		return taxperName;
	}

	public void setTaxperName(String taxperName) {
		this.taxperName = taxperName;
	}

	public String getRESULT_MESSAGE() {
		return RESULT_MESSAGE;
	}

	public void setRESULT_MESSAGE(String rESULTMESSAGE) {
		RESULT_MESSAGE = rESULTMESSAGE;
	}

	public BillSupplementPrintService getBillSupplementPrintService() {
		return billSupplementPrintService;
	}

	public void setBillSupplementPrintService(
			BillSupplementPrintService billSupplementPrintService) {
		this.billSupplementPrintService = billSupplementPrintService;
	}

	public ImpDataService getImpDataService() {
		return impDataService;
	}

	public void setImpDataService(ImpDataService impDataService) {
		this.impDataService = impDataService;
	}

	public SEOTransInfoService getSeoTransInfoService() {
		return seoTransInfoService;
	}

	public void setSeoTransInfoService(SEOTransInfoService seoTransInfoService) {
		this.seoTransInfoService = seoTransInfoService;
	}
	
	
	
}
