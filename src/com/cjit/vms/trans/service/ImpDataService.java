package com.cjit.vms.trans.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.VmsTransInfo;

public interface ImpDataService {

	/**
	 * 数据导入 列表画面初期化/检索用情报检索
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getImpDataListInfo(VmsTransInfo info,
			PaginationList paginationList);

	/**
	 * 导入数据明细表 检索用情报检索
	 * 
	 * @param batchId
	 * @return
	 * @author lee
	 */
	public List findTransImpDataInfo(VmsTransInfo info,
			PaginationList paginationList);

	/**
	 * VMS_Trans_Info_Imp_data 登录
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void saveVmsTransInfoImpDataInfo(List dataList);

	/**
	 * VMS_Trans_Batch 登录
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void saveVmsTransBatchInfo(VmsTransInfo info);

	/**
	 * VMS_TRANS_INFO 登录
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void saveVmsTransInfo(TransInfo info);

	/**
	 * VMS_TRANS_INFO_IMP_DATA的STATUS 更新
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void updateVmsTransInfoImpDataStatus(VmsTransInfo info);

	/**
	 * VMS_TRANS_BATCH的STATUS 更新
	 * 
	 * @param info
	 * 
	 * @author lee
	 */
	public void updateVmsTransBatchStatus(String status, String batchId);

	/**
	 * 根据batchID删除数据
	 * 
	 * @param batchID
	 */
	public void deleteTransInfoImpData(String batchID);

	public List findVmsTransInfoBybatchID(VmsTransInfo vmsTransInfo,
			PaginationList paginationList);

	/**
	 * 根据状态给TransInfo添加数据
	 * 
	 * @param state
	 * @return
	 */
	public int savetransInfoByBatchStateAndImpBatchState(String batchID);

	/**
	 * 将查询添加成功的数据删除
	 */
	public int deleteInVmsTransInfo(String batchID);

	/**
	 * 根据batchID查询审核拒绝的数据
	 */
	public void updateStateBybatchID(List list);

	/**
	 * 检验修改状态
	 */
	public void updateStateByCheckID(String batchID);
	
	/**
	 * 获取交易信息表中的交易
	 * @param transInfo
	 * @return
	 */
	public List findTransInfo(TransInfo transInfo);
	
	/**
	 * 根据批次号获取导入交易完整信息
	 * @param vmsTransInfo
	 * @return
	 */
	public List findImpDataDetailByBatchId(VmsTransInfo vmsTransInfo);
	
	/**
	 * 保存校验结果
	 * @param dataList
	 */
	public void savaCheckResult(List dataList);
	
	/**三
	 * 提交
	 * @param batchList
	 */
	public void updateBatchStatus(List batchList);
	
	/**
	 * 保存到transInfo表
	 * @param list
	 */
	public void saveTransInfo(List list);
	
	/**
	 * 删除已经插入transInfo表的数据
	 * @param list
	 */
	public void deleteDataAfterTransInfo(List list);
	
	/**
	 * 删除已经全部通过校验并通过审核的数据
	 * @param list
	 */
	public void deleteBatchAterTransInfo(List list);
	/**
	 * 修改批次状态
	 * @param list
	 */
	public void updateBatchAfterTrans(List list);

	/**
	 * 修改批次Message
	 * @param list
	 */
	public void updateTransDataByIdWidthMessage(List list);


}