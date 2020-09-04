package com.cjit.vms.trans.action;

import java.util.List;

import com.cjit.vms.trans.service.BillTrackService;

public class BillInfoPrintViewTransAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private String flag;
	private String billId;
	private List transList;
	private BillTrackService billTrackService;//ys

	/**
	 * 发票编辑页面查看交易
	 * 
	 * @return
	 */
	public String viewTransFromPrint() {
		try {
			if ("first".equalsIgnoreCase(fromFlag)) {
				paginationList.setCurrentPage(1);
				fromFlag = null;
			}
			transList = billTrackService.findTransByBillId(billId, paginationList);
			request.setAttribute("transList", transList);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoPrintViewTransAction-viewTransFromPrint", e);
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

	public List getTransList() {
		return transList;
	}

	public void setTransList(List transList) {
		this.transList = transList;
	}

	public BillTrackService getBillTrackService() {
		return billTrackService;
	}

	public void setBillTrackService(BillTrackService billTrackService) {
		this.billTrackService = billTrackService;
	}
}
