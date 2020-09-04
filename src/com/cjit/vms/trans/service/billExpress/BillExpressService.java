package com.cjit.vms.trans.service.billExpress;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.billExpress.BillExpress;

public interface BillExpressService {
	public List selectBillExpress(BillExpress billExpress,List authInstIds ,PaginationList paginationList);
	/***
	 * 更新快递信息
	 * @param billExpress
	 * @return
	 */
	public void updateBillExpressInfo(BillExpress billExpress);
	
	public void updateBillExpressInfoList(List<BillExpress> billExpressList);
	
	
	public void updateBillExpressInfoFinish(List<BillExpress> billExpressList);
}
