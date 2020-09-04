package com.cjit.vms.trans.action;

import java.util.List;

import com.cjit.vms.trans.service.BillTrackService;

public class BillInfoPrintDetailAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private String flag;
	private String billId;
	private List transInfoList;
	private BillTrackService billTrackService;//ys

	/**
	 * 发票编辑页面查看交易
	 * 
	 * @return
	 */
	public String listBillTransPrint() {
		try {
			if ("first".equalsIgnoreCase(fromFlag)) {
				paginationList.setCurrentPage(1);
				fromFlag = null;
			}
			transInfoList = billTrackService.findTransByBillId(billId, paginationList);
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			this.request.setAttribute("transInfoList", this.transInfoList);
			this.request.setAttribute("paginationList", this.paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoPrintDetailAction-viewTransFromBill", e);
		}
		return ERROR;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
	}
}
