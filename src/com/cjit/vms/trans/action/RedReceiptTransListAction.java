package com.cjit.vms.trans.action;

import com.cjit.vms.trans.service.redRecipt.RedReceiptApplyInfoService;

public class RedReceiptTransListAction  extends DataDealAction {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private  RedReceiptApplyInfoService redReceiptApplyInfoService;
	private String flag="";
	
	// 【发票红冲】页面数据查询
	public String redReceiptTransList() {
		String billId = request.getParameter("billId");
		
		flag = request.getParameter("ticket");
		
		try {
			if ("first".equalsIgnoreCase(fromFlag)) {
				paginationList.setCurrentPage(1);
				paginationList.setPageSize(20);
				fromFlag = null;
			}
			redReceiptApplyInfoService.findRedReceiptTrans(billId, paginationList);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("RedReceiptTransListAction-redReceiptTransList", e);
		}
		return ERROR;
	}
	
	public RedReceiptApplyInfoService getRedReceiptApplyInfoService() {
		return redReceiptApplyInfoService;
	}

	public void setRedReceiptApplyInfoService(
			RedReceiptApplyInfoService redReceiptApplyInfoService) {
		this.redReceiptApplyInfoService = redReceiptApplyInfoService;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
