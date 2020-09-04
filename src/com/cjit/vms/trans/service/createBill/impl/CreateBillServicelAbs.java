package com.cjit.vms.trans.service.createBill.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public abstract class CreateBillServicelAbs extends GenericServiceImpl
		implements CreateBillService {

	ConstructBillService constructBillService;

	@Override
	public BillsTaxNoContext constructBill(List transInfoList, User currentUser) {
		BillsTaxNoContext billsTaxNoContext = constructBillService
				.constructBill(transInfoList);
		return billsTaxNoContext;
	}

	@Override
	public abstract  List<CheckResult> constructBillAndSaveAsMerge(List transInfoList,
			User currentUser);

	@Override
	public abstract  List<CheckResult> constructBillAndSaveAsSingle(
			List transInfoList, User currentUser);

	@Override
	public abstract  List<CheckResult> constructBillAndSaveAsSplit(List transInfoList,
			User currentUser);

	@Override
	public List findGoodsInfo(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		return find("findGoodsInfo", map);
	}

	@Override
	public List findTaxInfoList(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		find("findTaxInfo", map);

		return find("findTaxInfo", map);
	}

	@Override
	public abstract TransInfo findTransInfo(TransInfo transInfo);

	@Override
	public abstract List findTransList(TransInfo transInfo,
			PaginationList paginationList);

	public ConstructBillService getConstructBillService() {
		return constructBillService;
	}

	public void saveBillGoodsInfo(BillGoodsInfo billGoodsInfo) {
		Map map = new HashMap();
		map.put("billGoodsInfo", billGoodsInfo);
		save("saveBillGoodsInfo", map);
	}

	public void saveBillGoodsList(List<BillGoodsInfo> billGoodsInfoList) {
		List<BillGoodsInfo> billGoodsInfoList2 = new ArrayList<BillGoodsInfo>();
		int flag = 0;
		billGoodsInfoList2.add(billGoodsInfoList.get(0));
		for (int i = 1; i < billGoodsInfoList.size(); i++) {
			flag = 0;
			for (int j = 0; j < billGoodsInfoList2.size(); j++) {
				if (billGoodsInfoList.get(i).getGoodsName().equals(billGoodsInfoList2.get(j).getGoodsName())){
					billGoodsInfoList2.get(j).myAdd(billGoodsInfoList.get(i));
					flag = 1;
					break;
				}
			}
			if (flag == 0){
				billGoodsInfoList2.add(billGoodsInfoList.get(i));
			}
		}
		for (int i = 0; i < billGoodsInfoList2.size(); i++) {
			BillGoodsInfo billGoodsInfo = billGoodsInfoList2.get(i);
			saveBillGoodsInfo(billGoodsInfo);
			//saveBillTransList(billGoodsInfo.getTransInfoList());
		}
		for(int i=0; i<billGoodsInfoList.size();i++){
			BillGoodsInfo billGoodsInfo = billGoodsInfoList.get(i);
			saveBillTransList(billGoodsInfo.getTransInfoList());
			updateTransAmtAndStatus(billGoodsInfo.getTransInfoList());
		}
	}

	public void saveBillInfo(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		save("saveBillInfo", map);
	}

	public CheckResult saveBillsTaxNoContext(BillsTaxNoContext billsTaxNoContext) {
		List<BillContext> billContexts = billsTaxNoContext.getAllBillContext();
		for (int i = 0; i < billContexts.size(); i++) {
			BillContext billContext = billContexts.get(i);
			saveBillInfo(billContext.getBillInfo());
			saveBillGoodsList(billContext.getGoodsInfoList());

		}
		return null;
	}

	public void saveBillTrans(BillTransInfo billTrans) {
		Map map = new HashMap();
		map.put("billTrans", billTrans);
		save("saveBillTrans", map);
	}

	public void saveBillTransList(List<BillTransInfo> billTrans) {
		for (int i = 0; i < billTrans.size(); i++) {
			BillTransInfo billTransInfo = billTrans.get(i);
			saveBillTrans(billTransInfo);
		}
	}

	public CheckResult saveContextAsMerge(BillContext billContext) {
		// 设置其他信息
		BillInfo billInfo = billContext.getBillInfo();
		setBillStaticInfoMerge(billInfo);
		// 保存
		saveBillInfo(billInfo);
		saveBillGoodsList(billContext.getGoodsInfoList());
		return null;
	}

	public CheckResult saveContextAsSingle(BillContext billContext) {

		// 设置其他信息
		BillInfo billInfo = billContext.getBillInfo();
		billInfo.setPayee("付款人");
		billInfo.setDrawer("开票人");
//		billInfo.setReviewer("复核人");
		setBillStaticInfoSingle(billInfo);
		// 保存
		saveBillInfo(billInfo);
		saveBillGoodsList(billContext.getGoodsInfoList());

		return null;
	}

	public CheckResult saveContextAsSplit(BillContext billContext) {

		// 设置其他信息
		BillInfo billInfo = billContext.getBillInfo();
		setBillStaticInfoSplit(billInfo);
		// 保存
		saveBillInfo(billInfo);
		saveBillGoodsList(billContext.getGoodsInfoList());

		return null;
	}

	@Override
	public String saveLog(BillsTaxNoContext billsTaxNoContext, User currentUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveTransProcessing(List transList) {
		this.insertBatch("saveTransProcessing", transList);

	}

	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	public abstract void setBillStaticInfoMerge(BillInfo billInfo);

	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	public abstract void setBillStaticInfoSingle(BillInfo billInfo);

	/***
	 * 设置票据基本信息
	 * 
	 * @param billInfo
	 */
	public abstract void setBillStaticInfoSplit(BillInfo billInfo);

	public void setConstructBillService(
			ConstructBillService constructBillService) {
		this.constructBillService = constructBillService;
	}

	public abstract void updateTransAmtAndStatus(List<BillTransInfo> billTrans);

}
