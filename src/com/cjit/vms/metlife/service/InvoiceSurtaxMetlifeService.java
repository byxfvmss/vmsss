package com.cjit.vms.metlife.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.trans.model.InputInvoiceInfo;

/** 
 *  createTime:2016.3
 * 	author:沈磊
 *	content:会计分录  metlife
*/
public interface InvoiceSurtaxMetlifeService {

	List findInvoiceInSurtaxList(InputInvoiceInfo info,
			PaginationList paginationList);

	List findListInvoice(InputInvoiceInfo info);

	void updateInvoiceInfo(List infolist);

	List findTansferOutRatio(InputInvoiceInfo info,
			PaginationList paginationList);

	List updatetransferOutRatio();

	void insertInvoceInfo(List listbillinfo);

	void updatetransInfo(List listbillinfo);

	void insertcommission(List listbillinfo);

	List findFinanceMonth(InputInvoiceInfo inputInvoiceInfo,
			PaginationList paginationList);

	void insertFinanceMonth(List<Map<String, String>> dataList);

	void cancelFinanceMonth(List cancelFinanceMonth);

	void updatetransferOutRatio1(List<InputInvoiceInfo> transferOutRatio);
	
	//李松加
	/**
	 * 为三表新增
	 * @param idMap
	 */
	/**
	 * 李松加     2016/5/23
	 * @param billNoAndBillCode
	 * @return
	 */
	public InputInvoiceNew findInputTransByIdCodeNoAndNo(String billNoAndBillCode);
	
	
	public void saveInputInvoice(Map<String, List> map) ;
	
	void updatetransInfoNew(Map idMap);
	
	List findInvoiceInSurtaxListNew(InputInvoiceNew info,
			PaginationList paginationList);

}
