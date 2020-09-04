package com.cjit.vms.trans.action.billPreOpen;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import cjit.crms.util.StringUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.billPreOpen.BillTransBus;
import com.cjit.vms.trans.model.createBill.BillGoodsInfo;
import com.cjit.vms.trans.model.createBill.BillPreInfo;
import com.cjit.vms.trans.service.billPreOpen.BillPreOpenService;
import com.cjit.vms.trans.util.DataUtil;
import org.apache.commons.lang.StringUtils;

public class BillPreOpenAction extends DataDealAction {
	private static final long serialVersionUID = 1L;
	private BillPreOpenService billPreOpenService;
	private String customerId;
	private BillItemInfo billItems = new BillItemInfo();
	private BillPreInfo billInfo = new BillPreInfo();
	private BillPreInfo billCondiction = new BillPreInfo();
	private Map fapiaoTypeMap = new HashMap();
	private List authInstList = new ArrayList();
	private String goodsListJson;
	private List catchGoodsList = new ArrayList();

	private List billInfoList = new ArrayList();
	private List goodsLists = new ArrayList();
	private List transBusIdList = new ArrayList();
	private String customerTaxno;

	// 交易流水号列表
	private String[] trnasBusIds;
	// 商品信息列
	private String[] specandmodel;
	private String[] goodsUnit;
	private String[] sumAmt;
	private String[] income;
	private String[] goodsNum;
	private String[] goodsPrice;
	private String[] taxRate;
	private String[] taxAmt;
	private String[] goodsName;
	private String[] goodsFullName;

	/**
	 * 票据预开页面
	 */
	public String billPreInvoice() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			User currentUser = this.getCurrentUser();
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			this.billCondiction.setLstAuthInstId(lstAuthInstId);
			this.billCondiction.setType("");
			billInfoList = billPreOpenService.selectBillInfoList(this.billCondiction, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillModify", e);
		}
		return ERROR;
	}

	/**
	 * 票据预开界面--新增
	 * 
	 * @return String
	 */
	public String billPreInvoiceForm() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		init();
		return SUCCESS;
	}

	/**
	 * 票据预开页面 -- 删除
	 */
	public void deletePreInvoiceList() {
		PrintWriter out = null;
		try {
			String billId = request.getParameter("billId");
			billPreOpenService.deleteBillPreOpen(billId);
			billPreOpenService.delBillItemById(billId);
			billPreOpenService.deleteBillTransBus(billId);			
			out = response.getWriter();
			out.print("Y");
		} catch (Exception e) {
			out.print("N");
			e.printStackTrace();
		}
	}

	/**
	 * 票据预开界面-->获取客户详细信息
	 * 
	 * @return String
	 * 2019
	 */
	public String getCustomerInfo() {
		init();
		if ((getCustomerTaxno() == null) || ("".equals(getCustomerTaxno()))) {
			setResultMessages("请输入客户纳税人识别号。");
			setBillInfo(new BillPreInfo());
			return "success";
		}
		Customer customer = billPreOpenService.findCustomer(getCustomerTaxno());
		if (customer == null) {
			setResultMessages("客户信息不存在，请重新输入");
			return "success";
		}

		setBillInfo(new BillPreInfo());
		billPreOpenService.constructBillInfo("customer", customer, this.billInfo);
		billInfo.setPayee(customer.getPayee());
		billInfo.setReviewer(customer.getReviewer());
		billInfo.setDrawer(getCurrentUser().getName());
		return "success";
	}

	/**
	 * 票据预开-->新增-->保存票据信息
	 * 
	 * @return String
	 */
	public String savePreBill() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			init();
			saveBillInit();
			logManagerService.writeLog(request, this.getCurrentUser(), "0002.0003", "票据预开", "保存提交",
					"对票据ID为(" + billInfo.getBillId() + ")的票据进行" + "保存处理", "1");
			setResultMessages("保存成功，请新增或者编辑明细。");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-saveBill", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "0002.0003", "票据预开", "保存提交",
					"对票据ID为(" + billInfo.getBillId() + ")的票据进行" + "保存处理", "0");
			setResultMessages("保存失败，请检查数据正确性。");
			return SUCCESS;
		}
	}

	/**
	 * 票据预开-->新增-->保存商品信息
	 * 
	 * @return String
	 */
	public String saveGoodsList() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			init();
			// 如果没有商品信息，则直接返回
			if (goodsName.length < 2) {
				return SUCCESS;
			}
			List goodsInfoList = constructGoodsInfo();
			billPreOpenService.saveGoodsInfoList(goodsInfoList);
			addTransBus();
			setResultMessages("保存成功!");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-saveBill", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "0002.0003", "票据预开", "保存提交",
					"对票据ID为(" + billInfo.getBillId() + ")的票据进行" + "保存处理", "0");
			setResultMessages("保存失败，请检查数据正确性。");
			return SUCCESS;
		}
	}

	/**
	 * 构建初始化信息
	 */
	public void init() {
		InstInfo in = new InstInfo();
		in.setUserId(this.getCurrentUser().getId());
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		in.setLstAuthInstIds(lstAuthInstId);
		in.setTaxFlag("true");// 筛除税号为空的机构
		authInstList = billPreOpenService.getInstInfoList(in);
		this.setCustomerId(this.getCustomerId());
		fapiaoTypeMap = this.billPreOpenService.findCodeDictionary("VAT_TYPE");
	}

	/**
	 * 预开保存时，补充票据信息，并执行保存
	 */
	public void saveBillInit() {
		billInfo.setDrawer(this.getCurrentUser().getName());
		billInfo.setInstcode(this.getInstCode());
		billInfo.setCustomerId(this.getCustomerId());
		billInfo.setCustomerTaxno(getCustomerTaxno());
		// 申请日期
		billInfo.setApplyDate(DateUtils.toString(new Date(), DateUtils.ORA_DATES_FORMAT));
		// 票据ID
		billInfo.setBillId(billPreOpenService.createBillId("B"));
		// 是否手工录入
		billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_3);
		// 设置开据类型为合并
		billInfo.setIssueType(DataUtil.ISSUE_TYPE_2);
		// 状态 保存时为1-编辑待提交，提交时为2-提交待审核
		billInfo.setDatastatus(DataUtil.BILL_STATUS_1);
		billInfo.getFapiaoType();
		billPreOpenService.savePreBillInfo(billInfo, false);
	}

	/**
	 * 保存票据信息同时获取可用的商品列表
	 */
	public void getGoodsList() {
		try {
			String taxno = billInfo.getTaxno(); // 纳税人识别号
			Map mapGoodsList = new HashMap();
			GoodsInfo goods = new GoodsInfo();
			goods.setTaxNo(taxno);
			User user = getCurrentUser();
			List goodsList = this.billPreOpenService.findGoodsInfoList(goods);
			goodsListJson = JSONArray.fromObject(goodsList).toString();
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(goodsListJson);
			this.response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getGoodsList : ", e);
		}
	}

	/**
	 * 构建商品信息
	 */
	public List constructGoodsInfo() {
		int size = goodsName.length;
		List goodsInfoList = new ArrayList();
		for (int i = 1; i < size; i++) {
			BillGoodsInfo goodsInfo = new BillGoodsInfo();
			goodsInfo.setBillItemId(createBusinessId("BI"));
			goodsInfo.setSpecandmodel(specandmodel[i]);
			goodsInfo.setGoodsUnit(goodsUnit[i]);
			goodsInfo.setAmt(new BigDecimal(sumAmt[i]));
			goodsInfo.setGoodsNo(goodsNum[i]);
			goodsInfo.setGoodsPrice(new BigDecimal(goodsPrice[i]));
			goodsInfo.setTaxRate(new BigDecimal(taxRate[i]));
			goodsInfo.setTaxAmt(new BigDecimal(taxAmt[i]));
			goodsInfo.setGoodsName(goodsName[i]);
			goodsInfo.setBillId(billInfo.getBillId());
			goodsInfo.setGoodsId(goodsFullName[i]);
			goodsInfoList.add(goodsInfo);
		}
		// 清空数据以满足单例模式返回
		specandmodel = null;
		goodsUnit = null;
		sumAmt = null;
		income = null;
		goodsNum = null;
		goodsPrice = null;
		taxRate = null;
		taxAmt = null;
		goodsName = null;
		return goodsInfoList;
	}

	/**
	 * 
	 * @return
	 */
	/***
	 * add by wang 票据交易流水号
	 */
	public void addTransBus() {
		BillTransBus billTransBus = new BillTransBus();
		billTransBus.setBillId(billInfo.getBillId());
		// billPreOpenService.deleteBillTransBus(billTransBus);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		List listBillBus = new ArrayList();
		Map trnasBusIdsMap = new HashMap();
		for (int i = 0; i < trnasBusIds.length; i++) {
			if (StringUtils.isEmpty(trnasBusIds[i]) || null != trnasBusIdsMap.get(trnasBusIds[i])) {
				continue;
			}
			trnasBusIdsMap.put(trnasBusIds[i], trnasBusIds[i]);
			billTransBus = new BillTransBus();
			billTransBus.setBillId(billInfo.getBillId());
			billTransBus.setTransBusId(trnasBusIds[i]);
			billTransBus.setUpdateUser(this.getCurrentUser().getId());
			billTransBus.setUpdateDatetime(sf.format(new Date()));
			listBillBus.add(billTransBus);
		}
		billPreOpenService.saveBillTransBus(listBillBus);
		trnasBusIds = (String[]) trnasBusIdsMap.keySet().toArray(new String[0]);
	}

	/**
	 * @title 初始化预开修改页面
	 * @description TODO
	 * @author dev4 @return,
	 */
	public String editOpenBill() {
		billInfo = (BillPreInfo) billPreOpenService.findBillInfoByBillId(billInfo.getBillId());
		transBusIdList = billPreOpenService.selectBillTransBus(billInfo.getBillId());
		goodsLists = this.billPreOpenService.findBillItemInfo(billInfo.getBillId());
		init();
		return SUCCESS;
	}

	/**
	 * @title 修改票据信息
	 * @description TODO
	 * @author dev4
	 * @return
	 */
	public String editBill() {
		billPreOpenService.deleteBillPreOpen(billInfo.getBillId());
		billPreOpenService.delBillItemById(billInfo.getBillId());
		billPreOpenService.deleteBillTransBus(billInfo.getBillId());
		saveBillInit();
		saveGoodsList();
		return SUCCESS;
	}

	/**
	 * @title 提交票据信息
	 * @description TODO
	 * @author dev4
	 */
	public void commitBillInfo() {
		PrintWriter out = null;
		try {
			out = this.response.getWriter();
			String billId = request.getParameter("billId");
			billPreOpenService.commitBillInfo(billId);
			out.print("Y");
		} catch (Exception e) {
			out.print("N");
			e.printStackTrace();
		}
	}

	public BillPreOpenService getBillPreOpenService() {
		return billPreOpenService;
	}

	public void setBillPreOpenService(BillPreOpenService billPreOpenService) {
		this.billPreOpenService = billPreOpenService;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public BillPreInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillPreInfo billInfo) {
		this.billInfo = billInfo;
	}

	public Map getFapiaoTypeMap() {
		return fapiaoTypeMap;
	}

	public void setFapiaoTypeMap(Map fapiaoTypeMap) {
		this.fapiaoTypeMap = fapiaoTypeMap;
	}

	public List getAuthInstList() {
		return authInstList;
	}

	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}

	public String getGoodsListJson() {
		return goodsListJson;
	}

	public void setGoodsListJson(String goodsListJson) {
		this.goodsListJson = goodsListJson;
	}

	public List getBillInfoList() {
		return billInfoList;
	}

	public void setBillInfoList(List billInfoList) {
		this.billInfoList = billInfoList;
	}

	public BillPreInfo getBillCondiction() {
		return billCondiction;
	}

	public void setBillCondiction(BillPreInfo billCondiction) {
		this.billCondiction = billCondiction;
	}

	public String[] getSpecandmodel() {
		return specandmodel;
	}

	public void setSpecandmodel(String[] specandmodel) {
		this.specandmodel = specandmodel;
	}

	public String[] getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String[] goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public String[] getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(String[] sumAmt) {
		this.sumAmt = sumAmt;
	}

	public String[] getIncome() {
		return income;
	}

	public void setIncome(String[] income) {
		this.income = income;
	}

	public String[] getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(String[] goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String[] getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String[] goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String[] getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String[] taxRate) {
		this.taxRate = taxRate;
	}

	public String[] getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(String[] taxAmt) {
		this.taxAmt = taxAmt;
	}

	public String[] getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String[] goodsName) {
		this.goodsName = goodsName;
	}

	public String[] getGoodsFullName() {
		return goodsFullName;
	}

	public void setGoodsFullName(String[] goodsFullName) {
		this.goodsFullName = goodsFullName;
	}

	public String[] getTrnasBusIds() {
		return trnasBusIds;
	}

	public void setTrnasBusIds(String[] trnasBusIds) {
		this.trnasBusIds = trnasBusIds;
	}

	public BillItemInfo getBillItems() {
		return billItems;
	}

	public void setBillItems(BillItemInfo billItems) {
		this.billItems = billItems;
	}

	public void setGoodsLists(List goodsLists) {
		this.goodsLists = goodsLists;
	}

	public List getGoodsLists() {
		return goodsLists;
	}

	public List getTransBusIdList() {
		return transBusIdList;
	}

	public void setTransBusIdList(List transBusIdList) {
		this.transBusIdList = transBusIdList;
	}

	public List getCatchGoodsList() {
		return catchGoodsList;
	}

	public void setCatchGoodsList(List catchGoodsList) {
		this.catchGoodsList = catchGoodsList;
	}

	/**
	 * @return the customerTaxno
	 */
	public String getCustomerTaxno() {
		return customerTaxno;
	}

	/**
	 * @param customerTaxno the customerTaxno to set
	 */
	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}
}
