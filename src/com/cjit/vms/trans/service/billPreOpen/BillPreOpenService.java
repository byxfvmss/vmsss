package com.cjit.vms.trans.service.billPreOpen;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.createBill.BillPreInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;

public interface BillPreOpenService {
	/**
	 * @param billInfo
	 * @param userID
	 * @param paginationList
	 * @param flag
	 * @return  票据预开一览
	 */
	public List selectBillInfoList(BillPreInfo billPreOpen,
			PaginationList paginationList);
	/**
	 * 删除预开信息
	 * @param billId
	 */
	public void deleteBillPreOpen(String billId);
	public void delBillItemById(String billId);
	public void deleteBillTransBus(String billId);
	public Customer findCustomer(String id);
	public void constructBillInfo(String inputObject,Object obj, BillInfo billInfo);
	public List getInstInfoList(InstInfo info);
	public Map findCodeDictionary(String codeType);
	public String createBillId(String tabFlag);
	public void savePreBillInfo(BillInfo billInfo, boolean isUpdate);
	/**
	 * 查询发票商品列表(vms_goods_info)
	 * @param goodsInfo
	 * @return List
	 */
	public List findGoodsInfoList(GoodsInfo goodsInfo);
	
	/**
	 * 保存商品列表
	 * @param goodsInfoList
	 */
	public void saveGoodsInfoList(List goodsInfoList);
	/***
	 * 保存票据流水号
	 * @param billTransBus<BillTransBus>
	 */
	public void saveBillTransBus(List billTransBus);
	
	/**
	 * @title 查询交易流水号
	 * @description TODO
	 * @author dev4
	 * @param billId
	 */
	public List selectBillTransBus(String billId);
	
	public Object findBillInfoByBillId(String billId);
	
	public List<BillItemInfo> findBillItemInfo(String billId);
	
	public void updateBillItemInfo(BillItemInfo billItemInfo,List goodsInfoList);
	
	public void commitBillInfo(String billId);
//	/*
//	 * @param billId 删除预开信息
//	 */
//	public void deleteBillPreOpen(String billId);
//	/**
//	 * @param billIds
//	 * @param dataStatus 更改票据的状态
//	 */
//	public void updateBillPreOpenByBillId(String billIds,String dataStatus);
	public List billPreOpenListToExcl(BillPreInfo billPreOpen, String userID);
//	public List getInstInfoList(InstInfo info);
//	public Map findCodeDictionary(String codeType);
//	public List selBillAmtByBillId(BillPreInfo bill);
//	public Customer findCustomer(String id);
//	public Organization getOrganization(Organization org);
//	public void savePreBillInfo(BillInfo billInfo, boolean isUpdate);
//	public void delBillItemById(String billId);
//	public void saveOrUpPreBillItemInfoList(List billItemInfoList, boolean isUpdate);
//	public List findBillItemInfoList(BillItemInfo billItemInfo);
//	/**
//	 * 查询发票商品列表(vms_goods_info)
//	 * @param goodsInfo
//	 * @return List
//	 */
//	public List findGoodsInfoList(GoodsInfo goodsInfo);
//
//	/**
//	 * 查询税目信息表列表(vms_tax_info)
//	 * 
//	 * @param vmsTaxInfo
//	 * @return List
//	 */
//	public List findVmsTaxInfoList(VmsTaxInfo vmsTaxInfo);
//	public String getBillIdSequence();
//	public String createBillId(String tabFlag);
//	public BillPreInfo findBillByIdForPre(String billId);
//	public GoodsInfo findGoodsForPre(String goodsId);
//	
//	//public List selectBillTransBus(BillTransBus billTransBus,PaginationList paginationList);
//	
//	public void deleteBillTransBus(String billId);
//	
//	/**
//	 * 构建票据信息
//	 */
//	public void constructBillInfo(String inputObject,Object obj, BillInfo billInfo);
//	
	public void  updatePaperInvoiceState(String billId);
	
	
	
}
