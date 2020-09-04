package com.cjit.vms.trans.service.invoiceInquerity.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.invoiceInquerity.InvoiceInquerityListInfo;
import com.cjit.vms.trans.model.invoiceInquerity.TransactionAmount;
import com.cjit.vms.trans.service.invoiceInquerity.InvoiceInquerityService;

/**
 * 2.0v
 * 
 * @author moran
 *
 */
public class InvoiceInquerityServiceImpl  extends GenericServiceImpl implements InvoiceInquerityService {

	@Override
	public List getInvoiceInquerityListInfo(InvoiceInquerityListInfo invoiceInquerityInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("invoiceInquerityListInfo", invoiceInquerityInfo);
		if (paginationList == null){
			return find("getInvoiceInquerityListInfo", map);
		}
		return findLargeData("getInvoiceInquerityListInfo", map, paginationList);
		
	}

	@Override
	public void saveInvoiceInquerityInfo(InvoiceInquerityListInfo saveInvoiceInquerityInfo) {
		Map map = new HashMap();	
		map.put("InquerityInfo", saveInvoiceInquerityInfo);
		save("saveInvoiceInquerityInfo", map);

	}

	@Override
	public void deleteInvoiceInquerity(String applicationFormId) {
		Map param = new HashMap();
		param.put("applicationFormId", applicationFormId.split(","));
		this.delete("deleteInvoiceInquerityInfo", param);
		
	}


	@Override
	public List<TransactionAmount> getAmountByChannelAndMonth(String productType, String applyTime) {
    Map<String,String>channelAndMonth=new HashMap<String, String>();
    channelAndMonth.put("productType", productType);
    channelAndMonth.put("applyTime", applyTime);
    return this.find("getAmountByChannelAndMonth", channelAndMonth);
	}

	 
}
