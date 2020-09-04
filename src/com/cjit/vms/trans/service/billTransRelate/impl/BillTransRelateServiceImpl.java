package com.cjit.vms.trans.service.billTransRelate.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.TransBillInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.billTransRelate.BillTransRelateService;
import com.cjit.vms.trans.service.createBill.CreateBillService;

public class BillTransRelateServiceImpl extends GenericServiceImpl implements
		BillTransRelateService {

	CreateBillService createBillService;

	@Override
	public String saveBillTransRelate(String billId, String[] relateAmt,
			String[] relateTransId, String[] relateTaxRate,
			String[] relateItemIds) throws Exception {
		// 删除transbill 并回写交易信息
		deleteBillTransAndUpdateTrans(billId, null, null);

		// 插入钩稽信息并回写交易信息
		for (int i = 0; i < relateTransId.length; i++) {
			
			String transId = relateTransId[i];
			String billItemId = relateItemIds[i];
			BigDecimal amtCny = new BigDecimal(relateAmt[i]);
			BigDecimal taxRate = new BigDecimal(relateTaxRate[i]);

			BigDecimal taxAmtCny = amtCny
					.divide(BigDecimal.ONE.add(taxRate), 2).multiply(taxRate);

			BigDecimal incomeCny = amtCny.subtract(taxAmtCny);

			
			TransInfo transInfo = new TransInfo();
			transInfo.setTransId(relateTransId[i]);
			// 取得最新交易信息
			transInfo = createBillService.findTransInfo(transInfo);
			if (null == transInfo) {
				throw new Exception("交易信息已不存在" + relateTransId[i]);
			}
			if(transInfo.getBalance().compareTo(amtCny)<0){
				throw new Exception("交易数据已被更新" + relateTransId[i]);
			}
			
			if(transInfo.getBalance().compareTo(amtCny)==0){
				taxAmtCny = transInfo.getTaxCnyBalance();
				incomeCny = amtCny.subtract(taxAmtCny);
			}
			
			
			
			TransBillInfo transBillInfo = new TransBillInfo();
			transBillInfo.setBillId(billId);
			transBillInfo.setTransId(transId);
			transBillInfo.setBillItemId(billItemId);
			transBillInfo.setAmtCny(amtCny);
			transBillInfo.setIncomeCny(incomeCny);
			transBillInfo.setTaxAmtCny(taxAmtCny);
			transBillInfo.setBalance(BigDecimal.ZERO);

			saveBillTrans(transBillInfo);
			updateTransAmtAndStatusGet(transBillInfo);

		}
		// 更新状态为钩稽完成
		updateBillRelateStatus(billId, "2");

		return null;
	}

	
	public void saveBillTrans(TransBillInfo billTrans) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billTrans", billTrans);
		save("saveBillTransByBillTransRelate", map);
	}

	public void deleteBillTrans(String billId, String transId, String billItemId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billId", billId);
		map.put("transId", transId);
		map.put("billItemId", billItemId);
		delete("deleteTransBillByBillTransRelate", map);

	};

	@Override
	public boolean deleteBillTransAndUpdateTrans(String billId, String transId,
			String billItemId) {

		List billTransList = selectTransBillInfo(billId, transId, billItemId);
		if (null != billTransList && billTransList.size() > 0) {
			updateBillRelateStatus(billId, "1");
			for (int i = 0; i < billTransList.size(); i++) {
				TransBillInfo transBillInfo = (TransBillInfo) billTransList
						.get(i);
				updateTransAmtAndStatusReturn(transBillInfo);
				deleteBillTrans(transBillInfo.getBillId(),
						transBillInfo.getTransId(),
						transBillInfo.getBillItemId());
			}
		}

		List billTransListEndDelete = selectTransBillInfo(billId, null, null);

		if (billTransListEndDelete.size() == 0) {
			updateBillRelateStatus(billId, "0");
		}

		return true;
	}

	@Override
	public List selectBillTransReateList(BillInfo billInfo, List lstAuthInstId,
			String applyDateStart, String applyDateEnd, String relateStatus,
			PaginationList paginationList) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 限制只查询手动开票
		billInfo.setIsHandiwork("3");
		map.put("billInfo", billInfo);
		map.put("lstAuthInstId", lstAuthInstId);
		map.put("applyDateStart", applyDateStart);
		map.put("applyDateEnd", applyDateEnd);
		map.put("relateStatus", relateStatus);
		if (paginationList == null) {
			return this.find("selectBillTransReateList", map);
		} else {
			return this.find("selectBillTransReateList", map, paginationList);
		}
	}

	@Override
	public List selectTransReatedList(BillInfo billInfo,
			PaginationList paginationList) {
		Map<String, Object> map = new HashMap<String, Object>();
		billInfo.setIsHandiwork("3");
		map.put("billInfo", billInfo);
		if (paginationList == null) {
			return this.find("selectBillTransReatedList", map);
		} else {
			return this.find("selectBillTransReatedList", map, paginationList);
		}
	}

	@Override
	public List selectTransNotReateList(BillInfo billInfo,
			String relateGoodsId, String relateTransId, List lstAuthInstId,
			PaginationList paginationList) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billInfo", billInfo);
		map.put("lstAuthInstId", lstAuthInstId);
		map.put("goodsId", relateGoodsId);
		map.put("transId", relateTransId);
		if (paginationList == null) {
			return this.find("selectBillTransNotReateList", map);
		} else {
			return this
					.find("selectBillTransNotReateList", map, paginationList);
		}

	}

	@Override
	public List selectTransBillInfo(String billId, String transId,
			String billItemId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billId", billId);
		map.put("transId", transId);
		map.put("billItemId", billItemId);
		return find("selectTransBillByBillId", map);

	}

	@Override
	public void updateTransAmtAndStatusGet(TransBillInfo transBillInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billTrans", transBillInfo);
		update("updateTransAmtAndStatusGet", map);
	}

	@Override
	public void updateTransAmtAndStatusReturn(TransBillInfo transBillInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billTrans", transBillInfo);
		update("updateTransAmtAndStatusReturn", map);
	}

	public List selectBillItemInfoList(String billId,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billId", billId);
		if (null != paginationList) {
			return this.find("selectBillItemInfo", map, paginationList);
		}
		return this.find("selectBillItemInfo", map);
	}

	@Override
	public void updateBillRelateStatus(String billId, String relateStatus) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("relateStatus", relateStatus);
		update("updateBillRelateStatus", map);
	}


	public CreateBillService getCreateBillService() {
		return createBillService;
	}


	public void setCreateBillService(CreateBillService createBillService) {
		this.createBillService = createBillService;
	}
}
