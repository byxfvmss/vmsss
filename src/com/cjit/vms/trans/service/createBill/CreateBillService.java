package com.cjit.vms.trans.service.createBill;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.createBill.BillGoodsInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;

public interface CreateBillService {
	public BillsTaxNoContext constructBill(List transInfoList, User currentUser);

	public List<CheckResult> constructBillAndSaveAsMerge(List transInfoList,
			User currentUser);

	public  List<CheckResult> constructBillAndSaveAsSingle(List transInfoList,
			User currentUser);

	public  List<CheckResult> constructBillAndSaveAsSplit(List transInfoList,
			User currentUser);

	public List findGoodsInfo(TransInfo transInfo);

	public List findTaxInfoList(TransInfo transInfo);

	public TransInfo findTransInfo(TransInfo transInfo);

	public List findTransList(TransInfo transInfo,PaginationList paginationList);

	public void saveBillGoodsInfo(BillGoodsInfo billGoodsInfo);

	public void saveBillInfo(BillInfo billInfo);

	public void saveBillTrans(BillTransInfo billTrans);

	public String saveLog(BillsTaxNoContext billsTaxNoContext, User currentUser);

	public void saveTransProcessing(List transList);
	
	public List findTransMergeFlagList();
	
	public List findTransList(TransInfo transInfo,PaginationList paginationList,String strFlagType,String strMergeSql);
}
