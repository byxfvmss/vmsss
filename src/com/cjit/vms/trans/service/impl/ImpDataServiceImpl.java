package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.VmsTransInfo;
import com.cjit.vms.trans.service.ImpDataService;

public class ImpDataServiceImpl extends GenericServiceImpl implements
		ImpDataService {

	public List getImpDataListInfo(VmsTransInfo vmsTransInfo,
			PaginationList paginationList) {
		Map map = new HashMap();

		List authInstIds = new ArrayList();
		List authInsts = vmsTransInfo.getAuthInsts();
		for (int i = 0; i < authInsts.size(); i++) {
			Organization org = (Organization) authInsts.get(i);
			authInstIds.add(org.getId());
		}
		map.put("auth_inst_ids", authInstIds);

		String startTime = vmsTransInfo.getStartTime();
		String endTime = vmsTransInfo.getEndTime();
		map.put("impInst", vmsTransInfo.getImpInst());
		map.put("status", vmsTransInfo.getStatus());
		map.put("statusList", vmsTransInfo.getStatusList());
		map.put("userId", vmsTransInfo.getUserId());
		if (paginationList == null) {
			return find("findImpDataListInfo", map);
		}
		return find("findImpDataListInfo", map, paginationList);
	}

	public List findTransImpDataInfo(VmsTransInfo vmsTransInfo,
			PaginationList paginationList) {
		Map map = new HashMap();

		map.put("impBatchId", vmsTransInfo.getBatchId());
		map.put("impStatus", vmsTransInfo.getImpStatus());
		map.put("impStatusList", vmsTransInfo.getImpStatusList());
		map.put("customerId", vmsTransInfo.getCustomerId());
		map.put("transId", vmsTransInfo.getTransId());

		String startTime = vmsTransInfo.getStartTime();
		String endTime = vmsTransInfo.getEndTime();
		if (StringUtils.isNotEmpty(startTime)) {
			map.put("startTime", startTime.trim() + " 00:00:00");
		}
		if (StringUtils.isNotEmpty(endTime)) {
			map.put("endTime", endTime.trim() + " 23:59:59");
		}

		map.put("transAmtStart", vmsTransInfo.getTransAmtStart());
		map.put("transAmtEnd", vmsTransInfo.getTransAmtEnd());
		map.put("transType", vmsTransInfo.getTransType());
		map.put("fapiaoType", vmsTransInfo.getFapiaoType());
		map.put("transFapiaoType", vmsTransInfo.getTransFapiaoType());
		map.put("dStatus", vmsTransInfo.getdStatus());
		map.put("status", vmsTransInfo.getStatus());
		if (paginationList == null) {
			return find("findTransImpDataInfo", map);
		}
		return find("findTransImpDataInfo", map, paginationList);
	}

	public void saveVmsTransInfoImpDataInfo(List dataList) {
		insertBatch("saveVmsTransInfoImpDataInfo", dataList);
	}

	public void saveVmsTransBatchInfo(VmsTransInfo vmsTransInfo) {
		Map map = new HashMap();
		map.put("vmsTransInfo", vmsTransInfo);
		map.put("status", "0");
		map.put("batchId", vmsTransInfo.getBatchId());
		map.put("impTime", vmsTransInfo.getImpTime());
		map.put("impInst", vmsTransInfo.getImpInst());
		map.put("impUser", vmsTransInfo.getImpUser());
		update("saveVmsTransBatchInfo", map);
	}

	public void saveVmsTransInfo(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		update("saveVmsTransInfo", map);
	}

	public void updateVmsTransInfoImpDataStatus(VmsTransInfo vmsTransInfo) {
		Map map = new HashMap();
		map.put("impId", vmsTransInfo.getImpId());
		map.put("impStatus", vmsTransInfo.getImpStatus());
		map.put("fapiaoType", vmsTransInfo.getFapiaoType());
		map.put("isReverse", vmsTransInfo.getIsReverse());
		map.put("message", vmsTransInfo.getMessage());
		map.put("D_STATUS", vmsTransInfo.getdStatus());
		update("updateVmsTransInfoImpDataStatus", map);
	}

	public void updateVmsTransBatchStatus(String status, String batchId) {
		Map map = new HashMap();
		map.put("batchId", batchId);
		map.put("status", status);
		update("updateVmsTransBatchStatus", map);
	}


	/**
	 * 删除导入信息
	 */
	@Override
	public void deleteTransInfoImpData(String batchID) {
		Map map = new HashMap();
		/**
		 * 截取获得batchID
		 * 
		 */
		// 新的list
		String[] subBatchID = batchID.split(",");
		/*
		 * for (int i = 0; i < subBatchID.length; i++) { subBatchID[i] =
		 * subBatchID[i].substring(0, subBatchID[i].length() - 2); }
		 */

		map.put("batchID", subBatchID);
		delete("deleteTransInfoImpData", map);
		delete("deleteTransInfoImpDatas", map);
		// deleteBatch(hql, data) data=list
	}

	/**
	 * 根据批次ID查看详细数据
	 */
	@Override
	public List<VmsTransInfo> findVmsTransInfoBybatchID(
			VmsTransInfo vmsTransInfo, PaginationList paginationList) {

		Map param = new HashMap();
		param.put("batchID", vmsTransInfo.getBatchId());
		if (paginationList == null) {
			return find("findVmsTransInfoBybatchID", param);
		}
		return find("findVmsTransInfoBybatchID", param, paginationList);
	}

	/**
	 * 将查询添加至transInfo添加数据
	 */
	@Override
	public int savetransInfoByBatchStateAndImpBatchState(String batchID) {

		try {
			Map param = new HashMap();
			param.put("batchID", batchID);
			this.save("savetransInfoByState", param);
			return 1;

		} catch (Exception e) {

			e.printStackTrace();

			return -1;
		}
	}

	/**
	 * 将查询添加成功的数据删除
	 */

	public int deleteInVmsTransInfo(String batchID) {
		try {
			Map map = new HashMap();
			// 截取获得batchID 新的list
			String[] subBatchID = batchID.split(",");

			map.put("batchID", subBatchID);
			delete("deleteTransInfoImpDataByIDStatus", map);

			/**
			 * 判断信息表的数据是否为空
			 */
			List infoNum = find("findVmsTransInfoBybatchIDCount", map);
			int num = Integer.parseInt((String) infoNum.get(0));
			if (num == 0) {
				delete("deleteTransInfoImpDataByIDStatuss", map);
			}
			return 1;
		} catch (NumberFormatException e) {
			/* e.printStackTrace(); */
			return -1;
		}

	}

	/**
	 * 修改审核拒绝状态
	 */
	@Override
	public void updateStateBybatchID(List list) {
		this.updateBatch("updateStateBybatchID", list);
	}

	/**
	 * 检验修改状态
	 */
	@Override
	public void updateStateByCheckID(String batchID) {

		Map param = new HashMap();
		param.put("batchID", batchID);
		update("updateTransBatchByCheckID", param);
		update("updateStateByCheckID", param);

	}

	@Override
	public List findTransInfo(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		return find("findTransInfo",map);
	}

	@Override
	public List findImpDataDetailByBatchId(VmsTransInfo vmsTransInfo) {
		Map map = new HashMap();
		map.put("vmsTransInfo", vmsTransInfo);
		return this.find("findImpDataDetailByBatchId",map);
	}

	@Override
	public void savaCheckResult(List dataList) {
		updateBatch("updateCheckStatus", dataList);
	}

	@Override
	public void updateBatchStatus(List batchList) {
		updateBatch("updateBatchStatus", batchList);
	}

	@Override
	public void saveTransInfo(List list) {
		insertBatch("saveTransInfo", list);
	}

	@Override
	public void deleteDataAfterTransInfo(List list) {
		deleteBatch("deleteDataAfterTransInfo", list);
	}

	@Override
	public void deleteBatchAterTransInfo(List list) {
		deleteBatch("deleteBatchAterTransInfo",list);
	}

	@Override
	public void updateBatchAfterTrans(List list) {
		updateBatch("updateBatchAfterTrans", list);
	}

	@Override
	public void updateTransDataByIdWidthMessage(List list) {
		updateBatch("updateTransDataByIdWidthMessage",list);
	}
}
