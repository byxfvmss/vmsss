package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.SpecialTicket;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.service.BillInfoService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.SqlUtil;

public class BillInfoServiceImpl extends GenericServiceImpl implements
		BillInfoService {

	// public List findBillInfoList(BillInfo billInfo, String userID,
	// PaginationList paginationList) {
	// Map map = new HashMap();
	// map.put("billInfo", billInfo);
	// if (billInfo != null) {
	// StringBuffer searchCondition = new StringBuffer();
	// if (DataUtil.MODIFY_BILL.equalsIgnoreCase(billInfo.getSearchFlag())) {
	// // 可编辑票据查询
	// searchCondition.append(" DATASTATUS = '").append(
	// DataUtil.BILL_STATUS_1).append("' ");
	// } else if (DataUtil.TRACK_BILL.equalsIgnoreCase(billInfo
	// .getSearchFlag())) {
	// // 废票/红冲 操作查询已完成的票据
	// /*searchCondition
	// .append(" t.CANCELFLAG is null and ((t.DATASTATUS = '")
	// .append(DataUtil.BILL_STATUS_99)
	// .append(
	// "' and  not exists (select 1 from vms_bill_info b where b.CANCELFLAG = '")
	// .append(DataUtil.HONGCHONG)
	// .append(
	// "' and b.ORI_BILL_CODE = t.BILL_CODE and b.ORI_BILL_NO = t.BILL_NO and b.DataStatus in ('")
	// .append(DataUtil.BILL_STATUS_1).append("','").append(
	// DataUtil.BILL_STATUS_2).append(
	// "'))) or t.DATASTATUS in ('").append(
	// DataUtil.BILL_STATUS_5).append("','").append(
	// DataUtil.BILL_STATUS_6).append("','").append(
	// DataUtil.BILL_STATUS_7).append("','").append(
	// DataUtil.BILL_STATUS_8).append("','").append(
	// DataUtil.BILL_STATUS_9).append("'))");*/
	// } else if (DataUtil.AUDIT_BILL.equalsIgnoreCase(billInfo
	// .getSearchFlag())) {
	// // 审核 操作查询((状态为提交待审核且开票人非当前用户) or
	// // (状态为已完成、撤销标志为废票或红冲且撤销发起人非当前用户))的票据
	// /*searchCondition.append(" ((DATASTATUS = '").append(
	// DataUtil.BILL_STATUS_2).append("' and DRAWER <> '")
	// .append(userID).append("') or (DATASTATUS in ('")
	// .append(DataUtil.BILL_STATUS_5).append("','").append(
	// DataUtil.BILL_STATUS_6).append("','").append(
	// DataUtil.BILL_STATUS_7).append("','").append(
	// DataUtil.BILL_STATUS_8).append("','").append(
	// DataUtil.BILL_STATUS_9).append(
	// "') and CANCELFLAG = '").append(
	// DataUtil.FEIPIAO).append(
	// "' and CANCEL_INITIATOR <> '").append(userID)
	// .append("')) ");*/
	// } else if ("print".equalsIgnoreCase(billInfo.getSearchFlag())) {
	// // 打印 查询可供打印的票据
	// searchCondition.append(" DATASTATUS in (").append(
	// DataUtil.BILL_STATUS_3).append(", ").append(
	// DataUtil.BILL_STATUS_4).append(") ");
	// } else if ("ems".equalsIgnoreCase(billInfo.getSearchFlag())) {
	// searchCondition.append(
	// " exists (select 1 from VMS_CUSTOMER_TB c where c.CUSTOMER_TAXNO= t.CUSTOMER_TAXNO) ");
	// }
	// map.put("searchCondition", searchCondition.toString());
	// }
	// return find("findBillInfo", map, paginationList);
	// }

	public List findBillInfoList(BillInfo billInfo, String userID,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		return find("findBillInfo", map, paginationList);
	}

	public List findBillInfoListNew(BillInfo billInfo, String userID,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("billInfo", billInfo);
		return find("findBillInfoNew", map, paginationList);
	}

	public List findBillInfoList(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		return find("findBillInfo", map);
	}

	public BillInfo findBillInfo(String billId) {
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		List list = this.findBillInfoList(billInfo);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}

	public List findBillItemInfoList(BillItemInfo billItemInfo) {
		Map map = new HashMap();
		map.put("billItem", billItemInfo);
		return find("findBillItemInfo", map);
	}

	public BillItemInfo findBillItemInfo(String billItemId) {
		BillItemInfo billItem = new BillItemInfo();
		billItem.setBillItemId(billItemId);
		List list = this.findBillItemInfoList(billItem);
		if (list != null && list.size() == 1) {
			return (BillItemInfo) list.get(0);
		} else {
			return null;
		}
	}

	public void saveBillInfo(BillInfo billInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("billInfo", billInfo);
		if (isUpdate) {
			this.save("updateBill", param);
		} else {
			this.save("saveBill", param);
		}
	}


	public void saveBillItemInfo(BillItemInfo billItemInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("billItemInfo", billItemInfo);
		if (isUpdate) {
			this.save("updateBillItem", param);
		} else {
			this.save("saveBillItem", param);
		}
	}






	
	public void savePreBillItemInfo(BillItemInfo billItemInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("billItemInfo", billItemInfo);
		this.save("saveBillItem", param);
	}

	public void updatePreBillItemInfo(BillItemInfo billItemInfo,
			boolean isUpdate) {
		Map param = new HashMap();
		param.put("billItemInfo", billItemInfo);
		this.update("updatePreBillItemInfo", param);
	}

	public void deleteBillInfo(String billId) {
		Map param = new HashMap();
		if (billId.contains(",")) {
			param.put("billId", billId.split(","));
			this.delete("deleteBillInfos", param);
		} else {
			param.put("billId", billId);
			this.delete("deleteBillInfo", param);
		}
	}

	public void deleteBillItemInfo(String billId, String billItemId) {
		Map param = new HashMap();
		param.put("billId", billId);
		param.put("billItemId", billItemId);
		this.delete("deleteBillItemInfo", param);
	}

	public List findtransId(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		return find("getTransIds", map);
	}

	public void setBillStatus(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		this.update("setBillStatus", map);
	}

	public List findBillSubDatas(String billId, String subTableId) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("subTableId", subTableId);
		return find("select_Xml_SubDatas_ByType", map);
	}

	public List findBillInfoList(BillInfo billInfo, String userID,
			String[] billIds) {

		Map map = new HashMap();
		map.put("billInfo", billInfo);
		if (billInfo != null) {
			StringBuffer searchCondition = new StringBuffer();
			if (DataUtil.MODIFY_BILL.equalsIgnoreCase(billInfo.getSearchFlag())) {
				// 可编辑票据查询
				searchCondition.append(" DATASTATUS = '")
						.append(DataUtil.BILL_STATUS_1).append("' ");
			} else if (DataUtil.TRACK_BILL.equalsIgnoreCase(billInfo
					.getSearchFlag())) {
				// 废票/红冲 操作查询已完成的票据
				searchCondition
						.append(" t.CANCELFLAG is null and ((t.DATASTATUS = '")
						.append(DataUtil.BILL_STATUS_99)
						.append("' and  not exists (select 1 from vms_bill_info b where b.CANCELFLAG = '")
						.append(DataUtil.HONGCHONG)
						.append("' and b.ORI_BILL_CODE = t.BILL_CODE and b.ORI_BILL_NO = t.BILL_NO and b.DataStatus in ('")
						.append(DataUtil.BILL_STATUS_1).append("','")
						.append(DataUtil.BILL_STATUS_2)
						.append("'))) or t.DATASTATUS in ('")
						.append(DataUtil.BILL_STATUS_5).append("','")
						.append(DataUtil.BILL_STATUS_6).append("','")
						.append(DataUtil.BILL_STATUS_7).append("','")
						.append(DataUtil.BILL_STATUS_8).append("','")
						.append(DataUtil.BILL_STATUS_9).append("'))");
			} else if (DataUtil.AUDIT_BILL.equalsIgnoreCase(billInfo
					.getSearchFlag())) {
				// 审核 操作查询((状态为提交待审核且开票人非当前用户) or
				// (状态为已完成、撤销标志为废票或红冲且撤销发起人非当前用户))的票据
				searchCondition.append(" ((DATASTATUS = '")
						.append(DataUtil.BILL_STATUS_2)
						.append("' and DRAWER <> '").append(userID)
						.append("') or (DATASTATUS in ('")
						.append(DataUtil.BILL_STATUS_5).append("','")
						.append(DataUtil.BILL_STATUS_6).append("','")
						.append(DataUtil.BILL_STATUS_7).append("','")
						.append(DataUtil.BILL_STATUS_8).append("','")
						.append(DataUtil.BILL_STATUS_9)
						.append("') and CANCELFLAG = '")
						.append(DataUtil.FEIPIAO)
						.append("' and CANCEL_INITIATOR <> '").append(userID)
						.append("')) ");
			} else if ("print".equalsIgnoreCase(billInfo.getSearchFlag())) {
				// 打印 查询可供打印的票据
				searchCondition.append(" DATASTATUS in (")
						.append(DataUtil.BILL_STATUS_3).append(", ")
						.append(DataUtil.BILL_STATUS_4).append(") ");
			}
			if (billIds != null && billIds.length > 0) {
				map.put("billIds", billIds);
			}
			map.put("searchCondition", searchCondition.toString());
		}
		return find("findBillInfo", map);

	}

	public List findTaxpayerTypeDatas(String txpayerType, String tableId) {
		Map map = new HashMap();
		map.put("txpayerType", txpayerType);
		map.put("tableId", tableId);
		return find("findTaxpayerType", map);
	}

	public List findTaxpayerTypeDatas2(String txpayerType, String tableId) {
		Map map = new HashMap();
		map.put("txpayerType", txpayerType);
		map.put("tableId", tableId);
		return find("findTaxpayerType2", map);
	}

	/*
	 * BillItemInfo billItem = new BillItemInfo();
	 * billItem.setBillItemId(billItemId); List list =
	 * this.findBillItemInfoList(billItem); if (list != null && list.size() ==
	 * 1) { return (BillItemInfo) list.get(0); } else { return null; }
	 */
	public BillInfo findTransInfoList(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		// TransInfo transInfo=new TransInfo();
		List list = find("findtransInfo1", map);
		BillInfo billInfo = new BillInfo();
		if (list != null && list.size() == 1) {
			billInfo = (BillInfo) list.get(0);
		}
		return billInfo;

	}

	public List findBillInfoForEmsList(BillInfo billInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("billInfo", billInfo);
		return find("findBillInfoForEms", map, paginationList);
	}

	public BillInfo findBillInfoForEms(BillInfo billInfo) {
		List list = this.findBillInfoForEmsList(billInfo);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}

	public List findBillInfoForEmsList(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		StringBuffer searchCondition = new StringBuffer();
		searchCondition.append(" rownum=1 ");
		map.put("searchCondition", searchCondition.toString());
		return find("findBillInfoForEms", map);
	}

	// czl
	public int findz(String type, String tableId) {
		Map map = new HashMap();
		map.put("type", type);
		map.put("tableId", tableId);
		List list = find("findz", map);
		if (list.get(0).toString().equals("0")) {
			return 0;
		} else {
			return 1;
		}

	}

	// czl
	public int findp(String type, String tableId) {
		Map map = new HashMap();
		map.put("type", type);
		map.put("tableId", tableId);
		List list = find("findp", map);
		if (list.get(0).toString().equals("0")) {
			return 0;
		} else {
			return 1;
		}
	}

	public void updateBillInfo(Map map) {
		this.update("updateBillInfo", map);
	}

	/**
	 * author 徐海洋 红冲
	 */
	public List findRedReceiptList(String sqlId, RedReceiptApplyInfo applyInfo,
			String userID, PaginationList paginationList) {
		Map map = new HashMap();
		map.put("applyInfo", applyInfo);
		StringBuffer searchCondition = new StringBuffer();
		/**
		 * 发标红冲画面 点击红冲时的用户不能是本人功能去除  20160310
		 */
		/*if (sqlId.equals("findRedReceiptApprove1")) {
			searchCondition
					.append(" (t.CANCEL_INITIATOR is null  or   t.CANCEL_INITIATOR<>'")
					.append(userID).append("')");
		}*/
		if (sqlId.equals("findRedReceipt")) {
		}

		map.put("applyInfo", applyInfo);
		map.put("searchCondition", searchCondition.toString());
		return find(sqlId, map, paginationList);
	}

	public BillInfo findBillInfo1(String billId) {
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		List list = find("findBillInfo1", map);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}

	public void saveBillInfo1(BillInfo billInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("billInfo", billInfo);
		if (isUpdate) {
			this.save("updateBill1", param);
		} else {
			this.save("saveBill", param);
		}
	}

	public void deleteApplyInfo(String billNo) {
		Map param = new HashMap();
		param.put("billNo", billNo);
		this.delete("deleteApplyInfo", param);
	}

	public RedReceiptApplyInfo findByBillId(String billId) {
		Map map = new HashMap();
		RedReceiptApplyInfo temp = new RedReceiptApplyInfo();
		temp.setBillId(billId);
		map.put("applyInfo", temp);
		List list = find("findSpecialRedReceipt", map);
		RedReceiptApplyInfo rrai = null;
		if (list.size() > 0) {
			rrai = (RedReceiptApplyInfo) list.get(0);
		}
		return rrai;
	}

	public RedReceiptApplyInfo findListByBillId(String billId, Map map) {

		RedReceiptApplyInfo temp = new RedReceiptApplyInfo();
		temp.setBillId(billId);
		map.put("applyInfo", temp);
		List list = find("findRedReceiptList", map);
		RedReceiptApplyInfo rrai = null;
		if (list.size() > 0) {
			rrai = (RedReceiptApplyInfo) list.get(0);
		}
		return rrai;
	}

	// public List findRedReceiptTrans(String billId,PaginationList
	// paginationList){
	// Map map = new HashMap();
	// map.put("billId", billId);
	// return find("redReceiptTransList", map, paginationList);
	// }

	public List findReleaseTrans(Map map) {
		return find("redReceiptTransList", map);
	}

	public List findBillItemInfoList1(BillItemInfo billItemInfo) {
		Map map = new HashMap();
		map.put("billItem", billItemInfo);
		return find("findBillItemInfo1", map);
	}

	public void saveSpecialTicket(SpecialTicket specialTicket) {
		Map param = new HashMap();
		param.put("specialTicket", specialTicket);
		this.save("saveSpecialTicket", param);

	}

	public List inputBillTrans(String billCode, String billNo) {
		Map map = new HashMap();
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		return find("inputBillTrans", map);
	}

	public List findSpecialTicketById(Map map) {
		return find("findSpecialTicketById", map);
	}

	public void updateSpecialTicket(SpecialTicket specialTicket) {
		Map map = new HashMap();
		map.put("specialTicket", specialTicket);
		this.update("updateSpecialTicket", map);
	}

	// 结束

	public List billsToExcelNew(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		return find("findBillInfoExcel", map);
	}

	/**
	 * 进入发票编辑列表页 ys
	 * 
	 * @param billInfo
	 * @param userID
	 * @param paginationList
	 * @return
	 */
	public List selectBillInfoList(BillInfo billInfo, String userID,
			PaginationList paginationList, boolean flag) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		// 票据审核画面case追加 at lee start
		if (DataUtil.AUDIT_BILL.equalsIgnoreCase(billInfo.getSearchFlag())) {
			billInfo.setDataStatus("2");
			billInfo.setDrawer(userID);
		}
		// 票据审核画面case追加 at lee end
		map.put("billInfo", billInfo);
		if (flag == true) {
			return find("selectBillInfoList", map, paginationList);
		} else {
			return find("selectBillInfoOpenList", map, paginationList);
		}
	}

	public BillInfo findBillById(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		List list = this.find("findBillById", map);
		BillInfo billInfo = new BillInfo();
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}

	public void UpdateRemarkAndPayee(String billId, String remark, String payee) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("remark", remark);
		map.put("payee", payee);
		this.update("UpdateRemarkAndPayee", map);
	}

	public BillInfo findBillInfoByIDFaPiaoType(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		List list = find("findBillInfoByIDFaPiaoType", map);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				billInfo = (BillInfo) list.get(0);
			}
		}
		return billInfo;
	}

	public List findEmsInfoForExport(BillInfo billInfo) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);

		map.put("billInfo", billInfo);
		return find("findBillInfoForEms", map);
	}

	// 票据审核画面case追加 at lee start
	/**
	 * 票据审核画面 状态更新
	 * 
	 * @param billId
	 * @param dataStatus
	 * 
	 * @author lee
	 */
	public void updateBillInfoDataStatus(String billId, String dataStatus) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("dataStatus", dataStatus);
		this.update("updateBillCancelStatus", map);
	}

	public void updateBillInfoDataStatus(String billId, String dataStatus,
			String cancelReason) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("dataStatus", dataStatus);
		map.put("cancelReason", cancelReason);
		this.update("updateBillCancelStatus1", map);
	}

	// 票据审核画面case追加 at lee end

	/**
	 * 票据审核画面 审核通过
	 * 
	 * @param billId
	 * @param dataStatus
	 * @param reviewer
	 * 
	 * @author leixu
	 */
	public void updateBillInfoApprovedStatus(String billId, String dataStatus,
			String reviewer) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("dataStatus", dataStatus);
		map.put("reviewer", reviewer);
		this.update("updateBillApprovedStatus", map);
	}

	public List findBillInfoByIDFaPiaoType(String[] billId, String fapiaoType) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("fapiaoType", fapiaoType);
		return find("findBillInfoByIDFaPiaoType", map);
	}

	public void updateBillInfoByBillNo(Map map) {
		this.update("updateBillInfoByBillNo", map);
	}

	public List findBillInfosNew(String[] selectIds) {
		Map map = new HashMap();
		map.put("selectIds", selectIds);
		return find("findBillInfosNew", map);
	}

	public void updateBillByBillId(String billIds, String dataStatus) {
		if (billIds.contains(",")) {
			Map map = new HashMap();
			map.put("billId", billIds.split(","));
			map.put("dataStatus", dataStatus);
			this.update("updateBillInfoById", map);
		} else {
			updateBillInfoDataStatus(billIds, dataStatus);
			// 打印修改 分发表-1 当前领用表-1 库存详情表+1 删除当前表领用信息
			Map map = new HashMap();
			map.put("billId", billIds);
			/***
			 * 插入发票快递基础信息
			 */
			save("insertBillExpress", map);

			List<BillInfo> list = find("findBillInfoforPrintById", map);
			map.put("billCode", list.get(0).getBillCode());
			map.put("billNo", list.get(0).getBillNo());
			map.put("num", 1);
			update("updateInvoiceDistrInReturn", map);
			update("updateInvoiceReCurInReturn", map);
			// update("updateStockDetialNumInReturn", map);
			List listNum = find("findInvoiceCurSurNumforCancel", map);
			// System.out.println("-------------"+listNum.get(0));
			if (listNum != null) {
				int num = (Integer) listNum.get(0);
				if (num == 0) {
					delete("deleteInvoiceCurInCancel", map);
				}
			}
		}
		// TODO Auto-generated method stub

	}

	public void updateBillByBillIdsup(String billIds, String dataStatus) {
		if (billIds.contains(",")) {
			Map map = new HashMap();
			map.put("billId", billIds.split(","));
			map.put("dataStatus", dataStatus);
			this.update("updateBillInfoById", map);
		} else {
			updateBillInfoDataStatus(billIds, dataStatus);
			// 打印修改 分发表-1 当前领用表-1 库存详情表+1 删除当前表领用信息

		}
		// TODO Auto-generated method stub

	}

	public List findBillInfoCheckList(BillInfo info,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("applyDate", info.getApplyDate());
		map.put("startDate", info.getStartDate());
		map.put("endDate", info.getEndDate());
		map.put("customerName", info.getCustomerName());
		map.put("customerId", info.getCustomerId());
		map.put("fapiaoType", info.getFapiaoType());
		map.put("dataStatus", info.getDataStatus());
		if ("1".equals(info.getGjType())) {
			map.put("notInTrans", "1");
			map.put("inTrans", "");
		} else {
			map.put("notInTrans", "");
			map.put("inTrans", "0");
		}
		List instIds = info.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		// map.put("auth_inst_ids", SqlUtil.list2String(lstTmp, ","));
		map.put("auth_inst_ids", lstTmp);
		if (paginationList == null) {
			return this.find("findBillInfoCheckList", map);
		} else {
			return this.find("findBillInfoCheckList", map, paginationList);
		}
	}

	public BillInfo getBillInfoDetail(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		return (BillInfo) this.findForObject("findBillInfoDetail", map);
	}

	public List getBillItemInfoList(String billId, PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billId", billId);
		return this.find("getBillItemInfoList", map);
	}

	public List getTransInfoCheckYlist(String billId,
			PaginationList paginationList) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("billId", billId);
		return this.find("getTransInfoCheckYlist", map);
	}

	public List getTransInfoCheckNlist(Map m, PaginationList paginationList) {
		return this.find("getTransInfoCheckNlist", m, paginationList);
	}

	public int saveBillInfoCheck(Map confirmMap, Map cancelMap, String billId,
			Map newData, Map oldIdDataChange) {
		Iterator confirmIt = confirmMap.keySet().iterator();
		Map map = new HashMap();
		map.put("billId", billId);
		Map insertMap = new HashMap();
		while (confirmIt.hasNext()) {
			String transId = (String) confirmIt.next().toString();
			map.put("transId", transId);
			TransInfo transInfo = (TransInfo) this.findForObject(
					"findTransInfoDetail", map);
			insertMap.put("transId", transId);
			insertMap.put("billId", billId);
			insertMap.put("amt", transInfo.getAmt());
			insertMap.put("taxAmt", transInfo.getTaxAmt());
			insertMap.put("income", transInfo.getIncome());
			insertMap.put("balance", newData.get(transId));
			System.out.println(newData.get(transId));
			int insert_result = this.save("saveBillInfoCheck", insertMap);// 保存钩稽金额
			// 更新未钩稽金额
			this.update("upadteBillInfoCheck", insertMap);

			// if(insert_result<1){
			// return -1;
			// }else{
			// String oldDataStatus = transInfo.getDataStatus();
			// if("1".equals(oldDataStatus) || "2".equals(oldDataStatus) ||
			// "3".equals(oldDataStatus) || "7".equals(oldDataStatus)){
			// map.put("dataStatus", "2");
			// }else if("5".equals(oldDataStatus) || "9".equals(oldDataStatus)){
			// map.put("dataStatus", "3");
			// }else if("8".equals(oldDataStatus) || "10".equals(oldDataStatus)
			// || "11".equals(oldDataStatus) || "12".equals(oldDataStatus)){
			// map.put("dataStatus", "99");
			// }
			// this.update("updateTransInfoDataStatus", map);
			// }
		}
		Iterator cancelIt = cancelMap.keySet().iterator();
		while (cancelIt.hasNext()) {
			Map cancelmap = new HashMap();
			cancelmap.put("billId", billId);
			String transId = (String) cancelIt.next().toString();
			if (oldIdDataChange.get(transId) == null) {
				cancelmap.put("transId", transId);
				TransInfo transInfo = (TransInfo) this.findForObject(
						"findOdData", cancelmap);
				cancelmap.put("balance", transInfo.getBalance());
				this.update("updateBillInfoCheckadd", cancelmap);
				this.delete("deleteBillInfoCheck", cancelmap);
			} else {
				cancelmap.put("transId", transId);
				cancelmap.put("balance", oldIdDataChange.get(transId));
				this.update("updateBillInfoCheckadd1", cancelmap);
				this.update("updateTransBill", cancelmap);
			}

			// cancelmap.put("dataStatus", "1");
			// this.update("updateTransInfoDataStatus", cancelmap);
		}
		return 0;
	}

	public TransInfo findTransInById(String transId) {
		TransInfo transInfo = new TransInfo();
		transInfo.setTransId(transId);
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		List list = this.find("findTransInfoMainQuery", map);

		if (list != null && list.size() == 1) {
			return (TransInfo) list.get(0);
		}
		return null;
	}

	public List selectBillInfoList(BillInfo billInfo, String userID) {
		Map map = new HashMap();
		// 票据审核画面case追加 at lee start
		if (DataUtil.AUDIT_BILL.equalsIgnoreCase(billInfo.getSearchFlag())) {
			billInfo.setDataStatus("2");
			billInfo.setDrawer(userID);
		}
		// 票据审核画面case追加 at lee end
		map.put("billInfo", billInfo);
		return find("selectBillInfoList", map);
	}

	public List selBillAmtByBillId(BillInfo bill) {
		Map map = new HashMap();
		map.put("billInfo", bill);
		return find("selBillAmtByBillId", map);
	}

	public void updateBillRemarkById(String remark, String billId) {
		Map map = new HashMap();
		map.put("remark", remark);
		map.put("billId", billId);

		update("updateBillRemarkById", map);
	}

	public List selectBillInfoListAudit(BillInfo billInfo, String userID,
			PaginationList paginationList, boolean flag) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		// 票据审核画面case追加 at lee start
		if (DataUtil.AUDIT_BILL.equalsIgnoreCase(billInfo.getSearchFlag())) {
			billInfo.setDataStatus("2");
			billInfo.setDrawer(userID);
		}
		// 票据审核画面case追加 at lee end
		map.put("billInfo", billInfo);
		if (flag == true) {
			return find("selectBillInfoListAudit", map, paginationList);
		} else {
			return find("selectBillInfoOpenList", map, paginationList);
		}
	}

	@Override
	public void updatebillCancelStatus(String billCode, String billNo) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		update("updatebillCancelStatus", map);

	}
	
	@Override
	public void updateBillStatus(String billId, String billCode, String billNo,
			String dataStatus, String cancelReason) {
		// TODO Auto-generated method stub
		
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		map.put("dataStatus", dataStatus);
		map.put("cancelReason", cancelReason);
		update("updateBillStatus", map);
	}
}
