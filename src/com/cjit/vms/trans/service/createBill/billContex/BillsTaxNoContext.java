package com.cjit.vms.trans.service.createBill.billContex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.vms.trans.model.createBill.TransInfo;

public class BillsTaxNoContext {

	Map<String, BillsCustomerContext> billsCustomerContextMap = new HashMap<String, BillsCustomerContext>();

	public BillsTaxNoContext() {
		// TODO Auto-generated constructor stub
	}

	/***
	 * 根据税号 客户号 发票类型取得 票据List
	 * 
	 * @param transInfo
	 * @return
	 */
	public List<BillContext> getBillContextList(TransInfo transInfo) {
		// 税号下所有票据结构
		String taxNo = transInfo.getTaxNo();
		
		String customerId = transInfo.getCustomerId();
		
		String fapiaoType = transInfo.getFapiaoType();
		List<BillContext> billContextList = getBillContextList(taxNo, customerId, fapiaoType);

		return billContextList;

	}

	/***
	 * 根据税号 客户号 发票类型取得 票据List
	 * 
	 * @param taxNo
	 * @param customerId
	 * @param fapiaoType
	 * @return
	 */
	public List<BillContext> getBillContextList(String taxNo,
			String customerId, String fapiaoType) {

		BillsCustomerContext billsCustomerContext = getBillsCustomerContext(taxNo);

		BillsContext billsContext = billsCustomerContext
				.getBillsContext(customerId);

		List<BillContext> billContextList = billsContext
				.getBillListByFapiaoType(fapiaoType);

		return billContextList;
	}

	/***
	 * 取得指定税号下所有客户下下文
	 * 
	 * @param taxNo
	 * @return
	 */
	public BillsCustomerContext getBillsCustomerContext(String taxNo) {
		BillsCustomerContext billsCustomerContext = billsCustomerContextMap
				.get(taxNo);
		if (null == billsCustomerContext) {
			billsCustomerContext = new BillsCustomerContext();
			billsCustomerContextMap.put(taxNo, billsCustomerContext);
		}
		return billsCustomerContext;
	}

	/***
	 * 追加票据billContext
	 * 
	 * @param billContext
	 */
	public void addBillContext(BillContext billContext) {

		String taxNo = billContext.getBillInfo().getTaxno();
		String customerId = billContext.getBillInfo().getCustomerId();
		String fapiaoType = billContext.getBillInfo().getFapiaoType();
		getBillContextList(taxNo, customerId, fapiaoType).add(billContext);

	}

	/***
	 * 取得所有税号
	 * @return
	 */
	public String[] getTaxNos() {
		if (null != billsCustomerContextMap) {
			return billsCustomerContextMap.keySet().toArray(new String[0]);
		}
		return new String[0];
	}

	/***
	 * 取得指定税号下所有客户号
	 * @param taxNo
	 * @return
	 */
	public String[] getCustomerIds(String taxNo) {
		BillsCustomerContext billsCustomerContext = getBillsCustomerContext(taxNo);
		if (null != billsCustomerContext) {
			return billsCustomerContext.getCustomerIds();
		}
		return new String[0];
	}

	/***
	 * 取得指定客户所有票据（专票加普票）
	 * @param taxNo
	 * @param customerId
	 * @return
	 */
	public List<BillContext> getCustomerBillContext(String taxNo,
			String customerId) {
		BillsCustomerContext  cCon = getBillsCustomerContext(taxNo);
		BillsContext billsContext =  cCon.getBillsContext(customerId);
		 return billsContext.getAllBillList();
	}
	
	/***
	 * 取得所有票据
	 * @return
	 */
	public List<BillContext> getAllBillContext() {
		List<BillContext> bills = new ArrayList<BillContext>();
		String[] taxNos = getTaxNos();
		for (int i = 0; i < taxNos.length; i++) {
			String taxNo = taxNos[i];
			String[] customerIds = getCustomerIds(taxNo);
			for (int j = 0; j < customerIds.length; j++) {
				String customerId = customerIds[j];
				bills.addAll(getCustomerBillContext(taxNo, customerId));
			}
		}
		return bills;
	}
	

	/***
	 * 取得指定税号下的所有票据
	 * @return
	 */
	public List<BillContext> getTaxNoBillContext(String taxNo) {
			List<BillContext> bills = new ArrayList<BillContext>();
			String[] customerIds = getCustomerIds(taxNo);
			for (int j = 0; j < customerIds.length; j++) {
				String customerId = customerIds[j];
				bills.addAll(getCustomerBillContext(taxNo, customerId));
			}
		return bills;
	}
	
	
	
}
