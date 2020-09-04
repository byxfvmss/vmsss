package com.cjit.vms.trans.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cjit.common.service.GenericService;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.UserColComments;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.TransBillInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.TransInfoYS;
import com.cjit.vms.trans.model.trans.ConnCust;

public interface TransInfoService extends GenericService {

	// ====================YS ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	/**
	 * 查询交易信息列表(含翻页)YS
	 * 
	 * @param transInfo
	 * @param paginationList
	 * @return List
	 */
	public List selectTransInfoList(TransInfoYS transInfoYS,
			PaginationList paginationList);

	/**
	 * 查询交易信息列表
	 * 
	 * @param transInfo
	 * @param paginationList
	 * @return List
	 */
	public List selectTransInfoList(TransInfoYS transInfoYS);

	/**
	 * 根据transBusId查询交易信息
	 * 
	 * @param transBusId
	 * @return TransInfo
	 */
	public TransInfoYS selectTransInfo(String transBusId);

	/**
	 * 根据transId查询交易信息
	 * 
	 * @param transId
	 * @return TransInfo
	 */
	public TransInfoYS selectTransInfoById(String transId);

	/**
	 * 更新交易信息
	 * 
	 * @param transInfo
	 */
	public void updateTransInfoYS(TransInfoYS transInfoYS);

	// ====================YS ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	/**
	 * 查询交易信息列表(含翻页)
	 * 
	 * @param transInfo
	 * @param paginationList
	 * @return List
	 */
	public List findTransInfoList(TransInfo transInfo,
			PaginationList paginationList);

	/**
	 * 查询交易信息列表
	 * 
	 * @param transInfo
	 * @param paginationList
	 * @return List
	 */
	public List findTransInfoList(TransInfo transInfo);

	/**
	 * 根据ID查询交易信息
	 * 
	 * @param transId
	 * @return TransInfo
	 */
	public TransInfo findTransInfo(BillInfo billId);

	/**
	 * 更新交易信息
	 * 
	 * @param transInfo
	 */
	public void updateTransInfo(TransInfo transInfo);
	
	/**
	 * 更新交易信息状态及余额
	 * 
	 * @param transInfo
	 */
	public void updateTransInfoForCancel(TransInfo transInfo);

	/**
	 * 自动开票更新状态
	 */
	public void updateTransInfoForAuto(String dataStatus, String billId);

	/**
	 * 更新交易信息状态
	 * 
	 * @param transId
	 * @param datastatus
	 */
	public void updateTransDatastatus(String transId, String datastatus);

	/**
	 * 插入交易与票据对应表
	 * 
	 * @param transId
	 * @param billId
	 * @param billItemId
	 * @param transAmt
	 */
	public void saveTransBill(String transId, String billId, String billItemId,
			BigDecimal amt, BigDecimal taxAmt, BigDecimal income);

	/**
	 * 插入交易与票据对应表
	 */
	public void saveTransBillForAuto(List transBillInfoList);

	/**
	 * 修改交易与票据对应表状态
	 * 
	 * @param transId
	 * @param billId
	 * @param billItemId
	 * @param billStatus
	 */
	public void updateTransBillStatus(String transId, String billId,
			String billItemId, String billStatus);

	/**
	 * 查询交易与票据对应表个数
	 * 
	 * @param transId
	 * @param billId
	 * @param billItemId
	 * @param searchFlag
	 * @return long
	 */
	public long findTransBillCount(String transId, String billId,
			String billItemId, String searchFlag);

	/**
	 * 删除交易与票据对应信息
	 * 
	 * @param transId
	 * @param billId
	 */
	public void deleteTransBill(String transId, String billId);

	/**
	 * 查询临时交易信息列表
	 * 
	 * @param transInfo
	 * @return List
	 */
	public List findTransInfoTempList(TransInfo transInfo);

	/**
	 * 删除销项税交易信息
	 * 
	 * @param transInfo
	 */
	public void deleteTransInfo(TransInfo transInfo);

	/**
	 * 删除未开票的数据
	 * @param trans_id
	 */
	public void deleteTransData(String trans_id);
	/**
	 * 获取票据ID序列
	 * 
	 * @return
	 */
	public String getBillIdSequence();

	public List findTransInfoMainQuery(TransInfo transInfo,
			PaginationList paginationList);

	public List findTransInfoMainQuery(TransInfo transInfo);

	public TransInfo findTransInfoMainQuery(String transId);

	public String findGoodsNameByTransType(String transType);

	public Map findSysParam(String itemKey);
	
	public Map findIncomeById(String transId);
	
	public void updateTransStatusAndBalance(String transId, BigDecimal balance, String dataStatus);
	
	public List findTransInfoForBillCancelList(TransInfo transInfo);
	
	public String checkTranSql(String sql);
	
	public List findUserColComments(UserColComments userColComments);
	
	public List selectTransToTrans(TransInfoYS transInfoYS);
	//public List findCustomerByCustomerId(String customerID,PaginationList paginationList);
	public List findCustomerByCustomerId(String customerID,String orgCustomerName,PaginationList paginationList);
	public List isFindSubCustomerByCustomerId(String customerID);
	/**
	 * 查询所有子公司信息
	 * @param customerID
	 * @param paginationList
	 * @return
	 */
	public List findSubCustomerByCustomerId(String customerID,PaginationList paginationList);
	public void updateConnectCustomer(ConnCust connCust,int flag);

	// 交易信息税额误差表
	public BigDecimal selectTransTaxAmtErr(String transId);
	public BigDecimal selectTransTaxAmt(String transId);
	public TransBillInfo selectTransBillInfo(String billId, String transId);
	public void updateTransTaxAmtErr(String transId, BigDecimal taxErr);
	public void insertTransTaxAmtErr(String transId, BigDecimal taxErr);
	// 交易信息税额误差表
	public List findTransTypeList(Map map);
	
	//从交易表中查询客户ID
	public List findTransCustomerList(TransInfo transInfo);
	//从票据表中查询客户ID
//	public List findTransCustomerList(BillInfo billInfo);
	/**
	 * Abel:Metlife Begin
	 * @param transInfo
	 * @param flag
	 * @return
	 */
	public List checkingTransByCherNum(TransInfo transInfo,boolean flag);
	
	public List findTransInfoListByTransId(String[] selectTransId);
	
	public void saveSaleAccountDetailsD(String transId,String acCountCodeD);
	
	public void saveSaleAccountDetailsC(String transId,String acCountCodeC);
	
	/**
	 * Abel:Metlife End
	 */

	/**
	 * TianAnlife Begin
	 */

	/**
	 * 查询交易信息列表 add yangqm
	 * 
	 * @param map
	 * @return List
	 */
	public long findCheckApplyTransInfoCount(Map map);
	
	public String applyInvoice(HttpServletRequest request,User user,String businessType,String businessCode,String source);
	
	public String applyInvoice(HttpServletRequest request,User user,String businessCode);

	/**
	 * TianAnlife End
	 */
}