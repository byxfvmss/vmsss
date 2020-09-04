package com.cjit.vms.trans.service.createBill;

import java.math.BigDecimal;
import java.util.List;

import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.billContex.BillContext;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;

public interface ConstructBillService {

	/***
	 * 创建bill入口
	 * 
	 * @param transInfoList
	 * @return
	 */
	public BillsTaxNoContext constructBill(List transInfoList);

//	/**
//	 * 交易拆分
//	 * 
//	 * @param transInfo
//	 * @return
//	 */
//	public List<TransInfo> spilitTrans(TransInfo transInfo);
//
//	/***
//	 * 创建新的票据信息
//	 * @param billsTaxNoContext
//	 * @param transInfo
//	 * @return
//	 */
//	public BillContext createBillContext(BillsTaxNoContext billsTaxNoContext,
//			TransInfo transInfo);
//
//	/***
//	 * 是否超过发票最大限额
//	 * 
//	 * @param balance
//	 * @return
//	 */
//	// public boolean isGtThanBillMaxAmt(BigDecimal balance);
//
//	/***
//	 * 获取发票最大限额
//	 * 
//	 * @param taxNo
//	 * @param fapiaoType
//	 * @return
//	 */
//	public BigDecimal getBillMaxAmt(String taxNo, String fapiaoType);
//
//	public BillContext getBillContext(BillsTaxNoContext billsTaxNoContext,
//			TransInfo transInfo);

}
