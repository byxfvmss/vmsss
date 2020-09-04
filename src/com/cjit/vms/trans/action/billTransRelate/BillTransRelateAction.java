package com.cjit.vms.trans.action.billTransRelate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.service.BillInfoService;
import com.cjit.vms.trans.service.billTransRelate.BillTransRelateService;

public class BillTransRelateAction extends DataDealAction {
	private BillTransRelateService billTransRelateService;
	private BillInfo billInfoSearch = new BillInfo();
	private String relateStatus = "0";// 0未钩稽 //1已钩稽

	private String relateBillId;// 钩稽交易ID
	private BillInfo relateBillInfo;// 钩稽交易基本信息
	private List relateBillItemList;// 钩稽交易商品信息
	private List relatedBillTransList;// 已钩稽交易信息

	private String relateTransId;// 交易id
	private String relateItemId;// 票据商品id
	private String relateGoodsId;// 交易商品id

	private String applyDateStart;
	private String applyDateEnd;

	private String[] relateAmts;// 钩稽金额
	private String[] relateTransIds;// 钩稽交易
	private String[] relateItemIds;// 钩稽票据商品id
	private String[] relateTaxRates;// 钩稽交易税率
//	private String[] balance;// //钩稽交易未开票金额(钩稽金额如果等于balance，税额使用taxCnyBalance避免误差)
//	private String[] taxCnyBalance;// //钩稽交易税额(钩稽金额如果等于balance，使用此率额避免误差)

	/***
	 * 
	 * @return
	 */
	public String listBillTransRelate() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}

		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);

		billTransRelateService.selectBillTransReateList(billInfoSearch,
				lstAuthInstId, applyDateStart, applyDateEnd, relateStatus,
				paginationList);
		return SUCCESS;
	}

	public String billTransRelate() {
		if (!sessionInit(true)) {
			setResultMessages("用户失效");
			return ERROR;
		}

		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);

		// 跟据relateBillId查找票据基本信息
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(relateBillId);
		// 查询票据信息 （于一览画面使用同一方法）
		List dbList = billTransRelateService.selectBillTransReateList(billInfo,
				lstAuthInstId, null, null, null, null);
		if (dbList.size() > 0) {
			relateBillInfo = (BillInfo) dbList.get(0);
		} else {
			setResultMessages("选择的票据已不存在");
			return "listBillTransRelate";
		}
		// 跟据relateBillId查找票据商品信息
		relateBillItemList = billTransRelateService.selectBillItemInfoList(
				relateBillId, null);
		// 取得已钩稽的数据
		relatedBillTransList = billTransRelateService.selectTransReatedList(
				billInfo, null);

		return SUCCESS;
	}

	public String saveBillTransRelate() {
		if (!sessionInit(true)) {
			setResultMessages("用户失效");
			return ERROR;
		}
		try {
			billTransRelateService.saveBillTransRelate(relateBillId, relateAmts,
					relateTransIds, relateTaxRates, relateItemIds);

		} catch (Exception e) {
			setResultMessages(e.getMessage());
			return ERROR;
		}
		
		setResultMessages("钩稽成功");

		return SUCCESS;
	}

	public void deleteBillTransRelate() throws Exception {

		if (!sessionInit(true)) {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print("用户失效");
		}
		try {
			billTransRelateService.deleteBillTransAndUpdateTrans(relateBillId,
					relateTransId, relateItemId);
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print("取消钩稽成功");
		} catch (IOException e) {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print("取消钩稽异常");
		}
	}

	public String listBillTransNotRelate() {
		if (!sessionInit(true)) {
			setResultMessages("用户失效");
			return ERROR;
		}
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);

		billTransRelateService.selectTransNotReateList(billInfoSearch,
				relateGoodsId, relateTransId, lstAuthInstId, paginationList);
		return SUCCESS;
	}

	public BillTransRelateService getBillTransRelateService() {
		return billTransRelateService;
	}

	public void setBillTransRelateService(
			BillTransRelateService billTransRelateService) {
		this.billTransRelateService = billTransRelateService;
	}

	public BillInfo getBillInfoSearch() {
		return billInfoSearch;
	}

	public void setBillInfoSearch(BillInfo billInfoSearch) {
		this.billInfoSearch = billInfoSearch;
	}

	public String getApplyDateStart() {
		return applyDateStart;
	}

	public void setApplyDateStart(String applyDateStart) {
		this.applyDateStart = applyDateStart;
	}

	public String getApplyDateEnd() {
		return applyDateEnd;
	}

	public void setApplyDateEnd(String applyDateEnd) {
		this.applyDateEnd = applyDateEnd;
	}

	public String getRelateStatus() {
		return relateStatus;
	}

	public void setRelateStatus(String relateStatus) {
		this.relateStatus = relateStatus;
	}

	public String getRelateBillId() {
		return relateBillId;
	}

	public void setRelateBillId(String relateBillId) {
		this.relateBillId = relateBillId;
	}

	public BillInfo getRelateBillInfo() {
		return relateBillInfo;
	}

	public void setRelateBillInfo(BillInfo relateBillInfo) {
		this.relateBillInfo = relateBillInfo;
	}

	public List getRelateBillItemList() {
		return relateBillItemList;
	}

	public void setRelateBillItemList(List relateBillItemList) {
		this.relateBillItemList = relateBillItemList;
	}

	public List getRelatedBillTransList() {
		return relatedBillTransList;
	}

	public void setRelatedBillTransList(List relatedBillTransList) {
		this.relatedBillTransList = relatedBillTransList;
	}

	public String getRelateTransId() {
		return relateTransId;
	}

	public void setRelateTransId(String relateTransId) {
		this.relateTransId = relateTransId;
	}

	public String getRelateItemId() {
		return relateItemId;
	}

	public void setRelateItemId(String relateItemId) {
		this.relateItemId = relateItemId;
	}

	public String[] getRelateAmts() {
		return relateAmts;
	}

	public void setRelateAmts(String[] relateAmts) {
		this.relateAmts = relateAmts;
	}

	public String[] getRelateTransIds() {
		return relateTransIds;
	}

	public void setRelateTransIds(String[] relateTransIds) {
		this.relateTransIds = relateTransIds;
	}

	public String[] getRelateTaxRates() {
		return relateTaxRates;
	}

	public void setRelateTaxRates(String[] relateTaxRates) {
		this.relateTaxRates = relateTaxRates;
	}

	public String getRelateGoodsId() {
		return relateGoodsId;
	}

	public void setRelateGoodsId(String relateGoodsId) {
		this.relateGoodsId = relateGoodsId;
	}

	public String[] getRelateItemIds() {
		return relateItemIds;
	}

	public void setRelateItemIds(String[] relateItemIds) {
		this.relateItemIds = relateItemIds;
	}

	

}
