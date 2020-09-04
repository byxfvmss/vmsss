package com.cjit.vms.metlife.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.metlife.service.BillInfoMetlifeService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;

public class BillInfoMetlifeActiom extends DataDealAction {

	private BillInfo billInfo;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BillInfoMetlifeService billInfoMetlifeService;

	public void findTransInfoByBillInfo() {
		// String result = "";
		List list = new ArrayList();
		try {
			String[] billIds = request.getParameter("billId").split(",");
			for (int i = 0; i < billIds.length; i++) {
				list = billInfoMetlifeService
						.findTransInfoByBillInfo(billIds[i]);
				if (list.size() > 0) {
					break;
				}
			}
			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(list != null && list.size() > 0 ? list.size() : "");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		if (billInfo != null) {
			this.billInfo = new BillInfo();
		}
	}

	public BillInfoMetlifeService getBillInfoMetlifeService() {
		return billInfoMetlifeService;
	}

	public void setBillInfoMetlifeService(BillInfoMetlifeService billInfoMetlifeService){
			this.billInfoMetlifeService = billInfoMetlifeService;
	}

	
}
