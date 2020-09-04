package com.cjit.vms.trans.service.createBill.billContex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillsCustomerContext {
	
	Map<String,BillsContext> billsContextMap = new HashMap<String,BillsContext>();

	public BillsContext getBillsContext(String customerId){
		
		BillsContext billsContext = billsContextMap.get(customerId);
		
		if (null==billsContext) {
			billsContext = new BillsContext();
			billsContextMap.put(customerId, billsContext);
			
		}
		return billsContext;
	}
	
	public String[] getCustomerIds(){
		
		if (null!=billsContextMap) {
			return billsContextMap.keySet().toArray(new String[0]);
		}
		return new String[0];
	}

}
