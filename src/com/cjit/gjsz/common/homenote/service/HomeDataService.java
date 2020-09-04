/**
 * 
 */
package com.cjit.gjsz.common.homenote.service;

import java.sql.SQLException;
import java.util.List;

import com.cjit.common.service.GenericService;
import com.cjit.gjsz.common.homenote.model.PubHomeCell;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.TransInfo;

/**
 * @author sunzhan
 */
public interface HomeDataService extends GenericService {

	/**
	 * <p>
	 * 查询PUB_HOME_DIC表中所定义需在FMSS首页统计的信息项
	 * </p>
	 * 
	 * @param dicValue
	 * @return List
	 */
	public List getHomeDic(String dicValue);

	/**
	 * <p>
	 * 查询银行自身业务待处理信息笔数
	 * </p>
	 * 
	 * @param searchCondition
	 * @param userId
	 * @param interfaceVer
	 *            接口版本
	 * @return Long
	 */
	public Long sumTableCount(String searchCondition, String userId,
			String interfaceVer);

	/**
	 * <p>
	 * 查询待处理反馈信息笔数
	 * </p>
	 * 
	 * @param searchCondition
	 * @param userId
	 * @param interfaceVer
	 * @return Long
	 * @throws SQLException
	 */
	public Long sumFeedbackCount(String searchCondition, String userId,
			String interfaceVer) throws SQLException;

	/**
	 * <p>
	 * 查询需要处理的任务中列表明细信息
	 * </p>
	 * 
	 * @param tablId
	 * @param dicTypeName
	 * @param userId
	 * @param top
	 * @param interfaceVer
	 * @return List
	 */
	public List searchHomeCell(String tableId, String dicTypeName,
			String userId, String top, String interfaceVer);

	/**
	 * <p>
	 * 查询待处理FAL报表信息笔数
	 * </p>
	 * 
	 * @param userId
	 * @param searchCondition
	 * @return List
	 * @throws SQLException
	 */
	public List getFalDatasCnt(String userId, String searchCondition)
			throws SQLException;

	public List getHomeCell(String userId, String date, String top);

	public void insertHomeCell(PubHomeCell pubHomeCell);

	// -----vms-----

	public List getTransCount(TransInfo transInfo, List orgIdList);

	public List getBillCount(BillInfo billInfo, List orgIdList);

	public List getIuputInvoiceCount(List orgIdList);

	public List getInvoicePaperAlert(List orgIdList);
}
