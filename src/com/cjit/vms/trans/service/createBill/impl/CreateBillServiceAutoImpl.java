package com.cjit.vms.trans.service.createBill.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.billContex.BillContext;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;
import com.cjit.vms.trans.util.DataUtil;

public class CreateBillServiceAutoImpl extends CreateBillServicelAbs {

	@Override
	public  List<CheckResult> constructBillAndSaveAsMerge(List transInfoList,
			User currentUser) {
		// 构建票据
		BillsTaxNoContext billsTaxNoContext = constructBillService
				.constructBill(transInfoList);
		// 保存票据
		List<BillContext> billContexts = billsTaxNoContext.getAllBillContext();
		for (int i = 0; i < billContexts.size(); i++) {
			BillContext billContext = billContexts.get(i);
			try {
				saveContextAsMerge(billContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		return null;
	}

	@Override
	public  List<CheckResult> constructBillAndSaveAsSingle(List transInfoList,
			User currentUser) {
		// 构建票据
		BillsTaxNoContext billsTaxNoContext = constructBillService
				.constructBill(transInfoList);
		// 保存票据
		List<BillContext> billContexts = billsTaxNoContext.getAllBillContext();
		for (int i = 0; i < billContexts.size(); i++) {
			BillContext billContext = billContexts.get(i);
			saveContextAsSingle(billContext);
		}
		return null;
	}
	
	
	@Override
	public  List<CheckResult> constructBillAndSaveAsSplit(List transInfoList,
			User currentUser) {
		// 构建票据
		BillsTaxNoContext billsTaxNoContext = constructBillService
				.constructBill(transInfoList);
		// 保存票据
		List<BillContext> billContexts = billsTaxNoContext.getAllBillContext();
		for (int i = 0; i < billContexts.size(); i++) {
			BillContext billContext = billContexts.get(i);
			saveContextAsSplit(billContext);
		}
		return null;
	}

	@Override
	public TransInfo findTransInfo(TransInfo transInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findTransList(TransInfo transInfo, PaginationList paginationList) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		if (null != paginationList) {
			return find("findTransCreateBillAuto", map, paginationList);
		}
		return find("findTransCreateBillAuto", map);
	}
	
	public List findTransList(TransInfo transInfo, PaginationList paginationList,String strFlagType,String strMergeSql) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		map.put("MergeFlagType",strFlagType);
		map.put("MergeFlag",strMergeSql);
		if (null != paginationList) {
			return find("findTransCreateBillAuto", map, paginationList);
		}
		return find("findTransCreateBillAuto", map);
	}
	
	public List findTransMergeFlagList() {
		Map map = new HashMap();
		return find("findTransMergeFlagList", map);
	}
	
	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	@Override
	public void setBillStaticInfoMerge(BillInfo billInfo) {
		String billStatus = DataUtil.BILL_STATUS_3;// 状态-审核通过
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_1;// 是否手工录入1 自动开票
		String isSueType = DataUtil.ISSUE_TYPE_2; // 发票开具类型1-单笔
		billInfo.setDatastatus(billStatus);// 状态-待提交
		billInfo.setIsHandiwork(isHandiwork);// 是否手工录入2-人工审核
		billInfo.setIssueType(isSueType); // 发票开具类型1-单笔
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		billInfo.setApplyDate(sf.format(new Date()));
	}
	
	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	public void setBillStaticInfoSingle(BillInfo billInfo) {
		String billStatus = DataUtil.BILL_STATUS_3;// 状态-审核通过
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_1;// 是否手工录入1 自动开票
		String isSueType = DataUtil.ISSUE_TYPE_1; // 发票开具类型1-单笔
		billInfo.setDatastatus(billStatus);// 状态-待提交
		billInfo.setIsHandiwork(isHandiwork);// 是否手工录入2-人工审核
		billInfo.setIssueType(isSueType); // 发票开具类型1-单笔
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		billInfo.setApplyDate(sf.format(new Date()));
	}

	
	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	public void setBillStaticInfoSplit(BillInfo billInfo) {
		String billStatus = DataUtil.BILL_STATUS_3;// 状态-审核通过
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_1;// 是否手工录入1 自动开票
		String isSueType = DataUtil.ISSUE_TYPE_3; // 发票开具类型3-拆分
		billInfo.setDatastatus(billStatus);// 状态-待提交
		billInfo.setIsHandiwork(isHandiwork);// 是否手工录入2-人工审核
		billInfo.setIssueType(isSueType); // 发票开具类型1-单笔
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		billInfo.setApplyDate(sf.format(new Date()));
	}
	@Override
	public void updateTransAmtAndStatus(List<BillTransInfo> billTrans) {
		for (int i = 0; i < billTrans.size(); i++) {
			BillTransInfo billTransInfo = billTrans.get(i);
			Map map = new HashMap();
			map.put("billTrans", billTransInfo);
			save("updateTransAmtAndStatusAuto", map);
		}
	}
	
	

}
