package com.cjit.vms.trans.service.createBill.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.createBill.BillGoodsInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.ConstructBillService;
import com.cjit.vms.trans.service.createBill.CreateBillService;
import com.cjit.vms.trans.service.createBill.billContex.BillContext;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;
import com.cjit.vms.trans.util.DataUtil;

public class CreateBillServiceManualImpl extends CreateBillServicelAbs {

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
			saveContextAsMerge(billContext);
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
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		List list = find("findTransCreateBill", map);
		if (list.size() > 0) {
			return (TransInfo) list.get(0);
		}
		return null;

	}

	@Override
	public List findTransList(TransInfo transInfo, PaginationList paginationList) {
		return null;
	}
	
	@Override
	public List findTransMergeFlagList() {
		return null;
	}
	
	@Override
	public List findTransList(TransInfo transInfo, PaginationList paginationList, String strFlagType, String strMergeSql) {
		return null;
	}

	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	@Override
	public void setBillStaticInfoMerge(BillInfo billInfo) {
		
		String billStatus = DataUtil.BILL_STATUS_3;// 状态-编辑待提交
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_2;// 是否手工录入2-人工审核
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
		String instCode = billInfo.getInstcode();
		
		System.out.println("instcode=============="+instCode);
		String billStatus = DataUtil.BILL_STATUS_3;// 状态-编辑待提交
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_2;// 是否手工录入2-人工审核
		String isSueType = DataUtil.ISSUE_TYPE_1; // 发票开具类型1-单笔
		String reviewer = findReviewer(instCode);
		billInfo.setReviewer(reviewer);
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
		String billStatus = DataUtil.BILL_STATUS_3;// 状态-编辑待提交
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_2;// 是否手工录入2-人工审核
		String isSueType = DataUtil.ISSUE_TYPE_3; // 发票开具类型1-单笔
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
			save("updateTransAmtAndStatusManual", map);
		}
	}
	public String findReviewer(String instId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("instId", instId);
		List<String> list = this.find("getPayee", map);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return "";
	}

}
