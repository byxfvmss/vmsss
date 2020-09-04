package com.cjit.vms.trans.action.invoice;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import cjit.crms.util.StringUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.billPreOpen.BillTransBus;
import com.cjit.vms.trans.model.createBill.BillGoodsInfo;
import com.cjit.vms.trans.model.createBill.BillPreInfo;
import com.cjit.vms.trans.service.BillTrackService;
import com.cjit.vms.trans.service.billPreOpen.BillPreOpenService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.JXLTool;
/**
 * 
 * @ClassName: InvoiceAction
 * @date:2019-12-09
 * @Description:
 */
public class InvoiceAction extends DataDealAction {
	private static final long serialVersionUID = 1L;
	private BillPreOpenService billPreOpenService;
	private BillTrackService billTrackService;
	private String customerId;
	private String customerTaxno;
	private BillItemInfo billItems=new BillItemInfo();
	private BillPreInfo billInfo = new BillPreInfo();
	private BillInfo invoiceInfo = new BillInfo();
	private BillPreInfo billCondiction = new BillPreInfo();
	private Map fapiaoTypeMap = new HashMap();
	private List authInstList = new ArrayList();
	private String goodsListJson;
	private List catchGoodsList=new ArrayList();

	private List billInfoList = new ArrayList();
	private List invoiceInfoList = new ArrayList();
	private List goodsLists=new ArrayList();
	private List transBusIdList=new ArrayList();
	//交易流水号列表
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
	 * 发票信息
	 */
	public String invoiceInfo() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			User currentUser = this.getCurrentUser();
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			this.billCondiction.setLstAuthInstId(lstAuthInstId);
			this.billCondiction.setType("invoice");
			billInfoList = billPreOpenService.selectBillInfoList(
					this.billCondiction, paginationList);
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
	public String invoiceForm() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		init();
		return SUCCESS;
	}

	public void deleteInvoiceInfoList() {
		PrintWriter out = null;
		try {
			String billId = request.getParameter("billId");
			billPreOpenService.updatePaperInvoiceState(billId);
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
	 */
	public String getInvoiceCustomerInfo() {
		init();
		if (this.getCustomerTaxno() == null || "".equals(this.getCustomerTaxno())) {
			setResultMessages("请输入客户纳税识别号");
			this.setBillInfo(new BillPreInfo());
			return SUCCESS;
		}
		Customer customer = billPreOpenService.findCustomer(this.getCustomerTaxno());
		if (null == customer) {
			setResultMessages("客户信息不存在，请重新输入");
			this.setBillInfo(new BillPreInfo());
			return SUCCESS;
		}
		// 构建预开票据信息
		billPreOpenService.constructBillInfo("customer", customer, billInfo);
		billInfo.setDrawer(this.getCurrentUser().getName());
		return SUCCESS;
	}

	/**
	 *
	 * @date:2019年11月15日
	 * @return
	 * @Description:保存票据信息
	 */
	public String saveInvoice() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			init();
			saveBillInit();
			//2019 标记库存
			
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0002.0003", "票据预开", "保存提交", "对票据ID为("
							+ billInfo.getBillId() + ")的票据进行" + "保存处理", "1");
			setResultMessages("保存成功，请新增或者编辑明细。");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-saveBill", e);
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0002.0003", "票据预开", "保存提交", "对票据ID为("
							+ billInfo.getBillId() + ")的票据进行" + "保存处理", "0");
			setResultMessages("保存失败，请检查数据正确性。");
			return SUCCESS;
		}
	}

	/**
	 * 票据预开-->新增-->保存商品信息
	 * 
	 * @return String
	 */
	public String saveInvoiceGoodsList() {
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
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0002.0003", "票据预开", "保存提交", "对票据ID为("
							+ billInfo.getBillId() + ")的票据进行" + "保存处理", "0");
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
    * 
    * @date:2019年11月15日
    * @Description: 预开保存时，补充票据信息，并执行保存
    */	 
	public void saveBillInit() {
		billInfo.setDrawer(this.getCurrentUser().getName());
		billInfo.setInstcode(this.getInstCode());
		billInfo.setCustomerTaxno(this.getCustomerTaxno());
		// 申请日期
		billInfo.setApplyDate(DateUtils.toString(new Date(),
				DateUtils.ORA_DATES_FORMAT));
		// 票据ID
		billInfo.setBillId(billPreOpenService.createBillId("B"));
		// 是否手工录入
		billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_3);
		// 设置开据类型为合并
		billInfo.setIssueType(DataUtil.ISSUE_TYPE_2);
		// 状态 保存时为5 已开具
		billInfo.setDatastatus(DataUtil.BILL_STATUS_5);
		//2019-add：开票日期、发票代码、发票号	
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
			if (StringUtil.isEmpty(trnasBusIds[i])
					|| null != trnasBusIdsMap.get(trnasBusIds[i])) {
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
	 * @author dev4
	 * @return,
	 */
	public String editInvoice(){
		billInfo=(BillPreInfo) billPreOpenService.findBillInfoByBillId(billInfo.getBillId());
		transBusIdList=billPreOpenService.selectBillTransBus(billInfo.getBillId());
		goodsLists = this.billPreOpenService.findBillItemInfo(billInfo.getBillId());
		init();
		return SUCCESS;
	}
	
	/**
	 * @title 全部提交保存票据信息
	 * @description TODO
	 * @author dev4
	 * @return
	 */
	public String editAllInvoice(){
		billPreOpenService.updatePaperInvoiceState(billInfo.getBillId());
		billPreOpenService.deleteBillPreOpen(billInfo.getBillId());
		billPreOpenService.delBillItemById(billInfo.getBillId());
		billPreOpenService.deleteBillTransBus(billInfo.getBillId());
		saveBillInit();
		saveInvoiceGoodsList();
		return SUCCESS;
	}

	/**
	 * @title 提交票据信息
	 * @description TODO
	 * @author dev4
	 */
	public void commitBillInfo(){
		PrintWriter out = null;
		try {
			out=this.response.getWriter();
			String billId = request.getParameter("billId");
			billPreOpenService.commitBillInfo(billId);
			out.print("Y");
		} catch (Exception e) {
			out.print("N");
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出
	 * @throws Exception 
	 */
	public void invoiceListOpenToExcel() throws Exception {
		if(billCondiction.getBillDate()!=null&&!"".equals(billCondiction.getBillDate())) {
			invoiceInfo.setBillDate(billCondiction.getBillDate());
		}
		if(billCondiction.getBillCode()!=null&&!"".equals(billCondiction.getBillCode())) {
			invoiceInfo.setBillCode(billCondiction.getBillCode());
		}
		if(billCondiction.getBillNo()!=null&&billCondiction.getBillNo()!="") {
			invoiceInfo.setBillNo(billCondiction.getBillNo());
		}
		if(billCondiction.getCustomerName()!=null&&billCondiction.getCustomerName()!="") {
			invoiceInfo.setCustomerName(billCondiction.getCustomerName());
		}
		if(billCondiction.getCustomerTaxno()!=null&&billCondiction.getCustomerTaxno()!="") {
			invoiceInfo.setCustomerTaxno(billCondiction.getCustomerTaxno());
		}
		if(billCondiction.getFapiaoType()!=null&&billCondiction.getFapiaoType()!="") {
			invoiceInfo.setFapiaoType(billCondiction.getFapiaoType());
		}	
		String []invoiceStatuses={"5","15","18"};
		invoiceInfo.setInvoiceStatuses(invoiceStatuses);
		invoiceInfoList = billTrackService.findInvoiceInfoList(invoiceInfo);
		
		StringBuffer fileName = new StringBuffer("发票信息列表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, invoiceInfoList);
		os.flush();
		os.close();
	}
	private void writeToExcel(OutputStream os, List content) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("发票查询信息", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "开票日期", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "发票代码", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "发票号码", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "客户地址电话", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "客户银行账号", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "合计金额", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "合计税额", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "价税合计", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "应税服务名称", JXLTool.getHeader());
		Label header13 = new Label(i++, 0, "规格型号", JXLTool.getHeader());
		Label header14 = new Label(i++, 0, "单位", JXLTool.getHeader());
		Label header15 = new Label(i++, 0, "单价", JXLTool.getHeader());
		Label header16 = new Label(i++, 0, "数量", JXLTool.getHeader());
		Label header17 = new Label(i++, 0, "金额", JXLTool.getHeader());
		Label header18 = new Label(i++, 0, "税率", JXLTool.getHeader());
		Label header19 = new Label(i++, 0, "税额", JXLTool.getHeader());
		Label header20 = new Label(i++, 0, "开票人", JXLTool.getHeader());
		Label header21 = new Label(i++, 0, "复核人", JXLTool.getHeader());
		Label header22 = new Label(i++, 0, "收款人", JXLTool.getHeader());
		Label header23 = new Label(i++, 0, "发票类型", JXLTool.getHeader());
		Label header24 = new Label(i++, 0, "状态", JXLTool.getHeader());
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
		ws.addCell(header20);
		ws.addCell(header21);
		ws.addCell(header22);
		ws.addCell(header23);
		ws.addCell(header24);
		for(int j = 0; j < 24; j++){
			ws.setColumnView(j, 18);
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
		Label cell2 = new Label(i++, column, bill.getBillDate(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, bill.getBillCode(),JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, bill.getBillNo(), JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, bill.getCustomerName(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, bill.getCustomerTaxno(), JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column, bill.getCustomerAddressandphone(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, bill.getCustomerBankandaccount(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, NumberUtils.format(bill.getAmtSum(),"",2), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, NumberUtils.format(bill.getTaxAmtSum(),"",2), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, NumberUtils.format(bill.getSumAmt(),"",2), JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, bill.getGoodsName(), JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, bill.getSpecandmodel(), JXLTool.getContentFormat());
		Label cell14 = new Label(i++, column, bill.getGoodsUnit(), JXLTool.getContentFormat());
		Label cell15= new Label(i++, column, bill.getGoodsPrice(), JXLTool.getContentFormat());
		Label cell16 = new Label(i++, column, bill.getGoodsNo(), JXLTool.getContentFormat());
		Label cell17 = new Label(i++, column, bill.getAmt(), JXLTool.getContentFormat());
		Label cell55 = new Label(i++, column, NumberUtils.format(bill.getTaxRate(),"",2), JXLTool.getContentFormat());
		Label cell18 = new Label(i++, column, bill.getTaxAmt(), JXLTool.getContentFormat());
		Label cell20 = new Label(i++, column, bill.getDrawerName(), JXLTool.getContentFormat());
		Label cell21 = new Label(i++, column, bill.getReviewerName(), JXLTool.getContentFormat());
		Label cell22 = new Label(i++, column, bill.getPayee(), JXLTool.getContentFormat());
    	Label cell23 = new Label(i++, column, DataUtil.getFapiaoTypeCH(bill.getFapiaoType()), JXLTool.getContentFormat());
		Label cell24 = new Label(i++, column, DataUtil.getDataStatusCH(bill.getDataStatus(), "BILL"), JXLTool.getContentFormat());
		
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
		ws.addCell(cell55);
		ws.addCell(cell20);
		ws.addCell(cell21);
		ws.addCell(cell22);
		ws.addCell(cell23);
		ws.addCell(cell24);
		
	}
	
	public String getCustomerTaxno() {
		return customerTaxno;
	}

	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
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


	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
	}

	public BillInfo getInvoiceInfo() {
		return invoiceInfo;
	}

	public void setInvoiceInfo(BillInfo invoiceInfo) {
		this.invoiceInfo = invoiceInfo;
	}

	public List getInvoiceInfoList() {
		return invoiceInfoList;
	}

	public void setInvoiceInfoList(List invoiceInfoList) {
		this.invoiceInfoList = invoiceInfoList;
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
}
