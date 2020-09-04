package com.cjit.vms.metlife.service.Impl;
/** 
 *  createTime:2016.3
 * 	author:沈磊
 *	content:会计分录  metlife
*/
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.metlife.service.InvoiceSurtaxMetlifeService;
import com.cjit.vms.trans.model.InputInvoiceInfo;

public class InvoiceSurtaxMetlifeServiceImpl extends GenericServiceImpl implements InvoiceSurtaxMetlifeService{

	@Override
	public List findInvoiceInSurtaxList(InputInvoiceInfo inputInvoiceInfo,
			PaginationList paginationList) {
		Map map=new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		return find("findInvoiceSurtaxMetlife", map, paginationList);
	}

	@Override
	public List findListInvoice(InputInvoiceInfo inputInvoiceInfo) {
		Map map=new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		return find("findListInvoice", map);
	}

	@Override
	public void updateInvoiceInfo(List infolist) {
		this.updateBatch("updateInvoiceInfo", infolist);
		
	}

	@Override
	public List findTansferOutRatio(InputInvoiceInfo inputInvoiceInfo,
			PaginationList paginationList) {
		Map map=new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		return find("findTansferOutRatio", map, paginationList);
	}

	@Override
	public List updatetransferOutRatio() {
		//this.updateBatch("updatetransferOutRatio", transferOutRatioList);
		Map map=new HashMap();
		return this.find("updatetransferOutRatio", map);
	}

	
	
	@Override
	public void updatetransferOutRatio1(List<InputInvoiceInfo> transferOutRatio) {
		// TODO Auto-generated method stub
		this.updateBatch("updatetransferOutRatio1", transferOutRatio);
	}

	@Override
	public void insertInvoceInfo(List listbillinfo) {
		try{
			
		this.insertBatch("insertInvoceInfoBatch", listbillinfo);
		this.insertBatch("insertInvoceInfoBatch1",listbillinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updatetransInfo(List listbillinfo) {
		this.updateBatch("updatetransInfostatusbatch", listbillinfo);
		
	}

	@Override
	public void insertcommission(List listbillinfo) {

			this.insertBatch("insertInvoiceCommission", listbillinfo);
			this.insertBatch("insertInvoiceCommission1", listbillinfo);
			this.insertBatch("insertInvoiceCommission2", listbillinfo);
			this.updateBatch("updateIncome", listbillinfo);

		
	}

	@Override
	public List findFinanceMonth(InputInvoiceInfo inputInvoiceInfo,
			PaginationList paginationList) {
		Map map=new HashMap();
		map.put("inputInvoiceInfo", inputInvoiceInfo);
		return find("findFinanceMonth", map, paginationList);
	}

	@Override
	public void insertFinanceMonth(List<Map<String, String>> dataList) {
			this.insertBatch("insertFinanceMonth", dataList);
	}

	@Override
	public void cancelFinanceMonth(List cancelFinanceMonth) {
		this.deleteBatch("cancelFinanceMonth", cancelFinanceMonth);
		
	}

	/**
	 * 李松2016/5/23加
	 */
	public InputInvoiceNew findInputTransByIdCodeNoAndNo(String billNoAndBillCode){
		Map map = new HashMap();
		map.put("no_code", billNoAndBillCode);
		List list = find("findByBillNoBillCode", map);
		if (list != null && list.size() > 0){
			InputInvoiceNew inputInvoiceNew = (InputInvoiceNew) list.get(0);
			return inputInvoiceNew;
		}
		return null;
	}
	
	/**
	 * 李松加
	 */
	@Override
	public void saveInputInvoice(Map<String, List> map) {
		List<InputInvoiceNew> list = map.get("insertList");
		Map inputInvoiceMap = new HashMap<String, String>();
		for(InputInvoiceNew inputInvoiceNew:list){
			inputInvoiceMap.put("porpuse_code",inputInvoiceNew.getPorpuseCode());
			inputInvoiceMap.put("inst_id",inputInvoiceNew.getInstId());
			inputInvoiceMap.put("inst_name", inputInvoiceNew.getInstName());
			inputInvoiceMap.put("bill_code", inputInvoiceNew.getBillCode());
			inputInvoiceMap.put("bill_no", inputInvoiceNew.getBillNo());
			inputInvoiceMap.put("amt", inputInvoiceNew.getAmt());
			inputInvoiceMap.put("tax_rate", inputInvoiceNew.getTaxRate());
			inputInvoiceMap.put("tax_amt", inputInvoiceNew.getTaxAmt());
			inputInvoiceMap.put("amt_tax_sum", inputInvoiceNew.getAmtTaxSum());
			inputInvoiceMap.put("data_status", inputInvoiceNew.getDataStatus());
			inputInvoiceMap.put("direction_id", inputInvoiceNew.getDirectionId());
			inputInvoiceMap.put("transfer_amt", inputInvoiceNew.getTransferAmt());
			inputInvoiceMap.put("deduc_amt", inputInvoiceNew.getDeducAmt());
			this.save("saveInputInvoiceNew", inputInvoiceMap);
		}

	}
	
	@Override
	public void updatetransInfoNew(Map idMap) {
		this.update("updatetransInfoNew", idMap);
		this.update("updatetransInfoTemp", idMap);
		
	}

	@Override
	public List findInvoiceInSurtaxListNew(InputInvoiceNew info,
			PaginationList paginationList) {
		Map map=new HashMap();
		map.put("inputInvoiceNew", info);
		return find("findInvoiceSurtaxMetlifeNew", map, paginationList);
	}


}
