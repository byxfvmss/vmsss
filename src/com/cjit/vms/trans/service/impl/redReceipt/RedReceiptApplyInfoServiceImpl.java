package com.cjit.vms.trans.service.impl.redReceipt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.DiskRegInfo;
import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.model.SpecialTicket;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.vms.trans.service.redRecipt.RedReceiptApplyInfoService;

public class RedReceiptApplyInfoServiceImpl extends GenericServiceImpl
		implements RedReceiptApplyInfoService {

	public List findRedReceiptList(RedReceiptApplyInfo redReceiptApplyInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = redReceiptApplyInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		String dataStatus = redReceiptApplyInfo.getDatastatus();
		if (dataStatus != null && "3,7".equals(dataStatus)) {
			redReceiptApplyInfo.setDatastatus(null);
			map.put("issueRedStatuses", dataStatus.split(","));
		}
		map.put("redReceiptApplyInfo", redReceiptApplyInfo);
		return find("findBillInfoList", map, paginationList);
	}

	public List findRedReceiptList(RedReceiptApplyInfo redReceiptApplyInfo) {
		Map map = new HashMap();
		String dataStatus = redReceiptApplyInfo.getDatastatus();
		if (dataStatus != null && "3,7".equals(dataStatus)) {
			redReceiptApplyInfo.setDatastatus(null);
			map.put("issueRedStatuses", dataStatus.split(","));
		}
		map.put("redReceiptApplyInfo", redReceiptApplyInfo);
		return find("findBillInfoList", map);
	}

	public List findTransByBillId(String billId, PaginationList paginationList) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);

		return find("findTransByBillId", map, paginationList);
	}

	public BillInfo findBillInfoById(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		List list = find("findBillTrack", map);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		}
		return null;
	}

	public List findBillItemByBillId(String billId) {
		Map map = new HashMap();
		BillItemInfo billItem = new BillItemInfo();
		billItem.setBillId(billId);
		map.put("billItem", billItem);

		return find("findBillItemInfo", map);
	}

	public List findRedBillByOriBillId(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		return find("findRedBillByOriBillId", map);
	}

	public Long findInvalidInvoiceCount(String dataStatus, String fapiaoType) {
		Map map = new HashMap();
		PaperInvoiceUseDetail paperInvoiceUseDetail = new PaperInvoiceUseDetail();
		paperInvoiceUseDetail.setInvoiceStatus(dataStatus);
		// 发票类型
		map.put("paperInvoiceUseDetail", paperInvoiceUseDetail);

		return getRowCount("findInvalidPaperInvoiceCount", map);
	}

	public List findInvalidPaperInvoice(String dataStatus, String fapiaoType) {
		Map map = new HashMap();
		PaperInvoiceUseDetail paperInvoiceUseDetail = new PaperInvoiceUseDetail();
		paperInvoiceUseDetail.setInvoiceStatus(dataStatus);
		// 发票类型
		map.put("paperInvoiceUseDetail", paperInvoiceUseDetail);
		return find("findInvalidPaperInvoice", map);
	}

	public String findRegisteredInfo(String taxDiskNo) {
		Map map = new HashMap();
		DiskRegInfo diskRegInfo = new DiskRegInfo();
		diskRegInfo.setTaxDiskNo(taxDiskNo);
		map.put("diskRegInfo", diskRegInfo);
		List list = find("findRegisteredInfo", map);
		if (list != null && list.size() == 1) {
			diskRegInfo = (DiskRegInfo) list.get(0);
			return diskRegInfo.getRegisteredInfo();
		}
		return null;
	}

	public void updateBillInfoStatus(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billId", billInfo.getBillId());
		map.put("dataStatus", billInfo.getDataStatus());

		update("updateBillDataStatus", map);
	}

	public void updatePaperInvoiceStatus(PaperInvoiceUseDetail invalidInvoice) {
		Map map = new HashMap();
		map.put("paperInvoiceUseDetail", invalidInvoice);
		update("updatePaperInvoiceStatus", map);
	}

	public void updateTransInfoStatus(String dataStatus, String billId) {
		Map map = new HashMap();
		map.put("dataStatus", dataStatus);
		map.put("billId", billId);

		update("updateTransInfoStatus", map);
	}

	public void updatebillInfoIssueResult(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		update("updatebillInfoIssueResult", map);
	}

	public TaxDiskInfo findTaxDiskInfoByTaxDiskNo(String taxDiskNo) {
		Map map = new HashMap();
		TaxDiskInfo taxDiskInfo = new TaxDiskInfo();
		taxDiskInfo.setTaxDiskNo(taxDiskNo);
		map.put("taxDiskInfo", taxDiskInfo);
		List list = find("findTaxDiskInfoByTaxDiskNo", map);
		if (list != null && list.size() == 1) {
			return (TaxDiskInfo) list.get(0);
		}
		return null;
	}

	/**
	 * author 徐海洋 【红冲申请】页面数据查询
	 */
	public List findRedReceiptApplyList(String sqlId,
			RedReceiptApplyInfo applyInfo, String userID,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("applyInfo", applyInfo);
		List instIds = applyInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		StringBuffer searchCondition = new StringBuffer();
		if (sqlId.equals("findRedReceiptApprove")) {
			searchCondition
					.append(" (t.CANCEL_INITIATOR is null  or   t.CANCEL_INITIATOR<>'")
					.append(userID).append("')");
		}
		if (sqlId.equals("findRedReceipt")) {
		}

		map.put("applyInfo", applyInfo);
		map.put("searchCondition", searchCondition.toString());
		return find(sqlId, map, paginationList);
	}

	/**
	 * 【交易发票关联】页面数据查询
	 */
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

	/**
	 * 【发票红冲】页面数据查询
	 */
	public List findRedReceiptTrans(String billId, PaginationList paginationList) {
		Map map = new HashMap();
		map.put("billId", billId);
		return find("redReceiptTransList", map, paginationList);
	}

	public List findRedReceiptTrans(String billId,
			PaginationList paginationList, String ids) {
		Map map = new HashMap();
		map.put("billId", billId);
		if (ids.charAt(0) == '[') {
			ids = StringUtils.remove(ids, "[");
			ids = StringUtils.remove(ids, "]");
		}
		String strIds = ids.replaceAll(",", "','").substring(2).concat("'");
		map.put("transId", strIds);
		return find("redReceiptTransList", map, paginationList);
	}

	public List findRedReceiptTransByIds(String billId,
			PaginationList paginationList, String[] ids) {
		Map map = new HashMap();
		map.put("billId", billId);
		String strIds = "";
		for (int i = 0; i < ids.length; i++) {
			strIds += "'" + ids[i] + "'";
		}
		strIds = strIds.replace("''", "','");
		map.put("transId", strIds);
		
		if (null==paginationList) {
			return find("redReceiptTransList", map);
		}
		return find("redReceiptTransList", map, paginationList);
	}

	/**
	 * 【红冲审核】页面[审核通过]
	 */
	public void saveBillInfo1(BillInfo billInfo, boolean isUpdate) {
		Map param = new HashMap();
		param.put("billInfo", billInfo);
		if (isUpdate) {
			this.save("updateBill1", param);
		} else {
			this.save("saveBill", param);
		}
	}

	public void updateRedBill(BillInfo billInfo) {
		Map param = new HashMap();
		param.put("billInfo", billInfo);
		this.save("updateRedBill", param);
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

	public List findReleaseTrans(Map map) {
		return find("redReceiptTransList", map);
	}

	public List findBillItemInfoList(BillItemInfo billItemInfo) {
		Map map = new HashMap();
		map.put("billItem", billItemInfo);
		return find("findBillItemInfo", map);
	}

	public List findBillItemInfoList1(BillItemInfo billItemInfo) {
		Map map = new HashMap();
		map.put("billItem", billItemInfo);
		return find("findBillItemInfo1", map);
	}

	/**
	 * author 徐海洋 红冲
	 */
	public List findRedReceiptList(String sqlId, RedReceiptApplyInfo applyInfo,
			String userID, PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = applyInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("applyInfo", applyInfo);
		StringBuffer searchCondition = new StringBuffer();
		if (sqlId.equals("findRedReceiptApprove")) {
			/*
			 * searchCondition
			 * .append(" (t.CANCEL_INITIATOR is null  or   t.CANCEL_INITIATOR<>'"
			 * ) .append(userID).append("')");
			 */
		}
		if (sqlId.equals("findRedReceipt")) {

		}

		map.put("applyInfo", applyInfo);
		map.put("searchCondition", searchCondition.toString());
		if (paginationList == null) {
			return find(sqlId, map);
		} else {
			return find(sqlId, map, paginationList);
		}

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

	public void deleteBillItemInfo(String billId, String billItemId) {
		Map param = new HashMap();
		param.put("billId", billId);
		param.put("billItemId", billItemId);
		this.delete("deleteBillItemInfo", param);
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

	public void saveSpecialTicket(SpecialTicket specialTicket) {
		Map param = new HashMap();
		param.put("specialTicket", specialTicket);
		this.save("saveSpecialTicket", param);

	}

	public void deleteApplyInfo(String billCode ,String billNo) {
		Map param = new HashMap();
		param.put("billNo", billNo);
		param.put("billCode", billCode);
		this.delete("deleteApplyInfo", param);
	}
	public void deleteApplyInfo(String billNo) {
		Map param = new HashMap();
		param.put("billNo", billNo);
		param.put("billNo", billNo);
		this.delete("deleteApplyInfo", param);
	}

	public void deleteBillInfo(String billId) {
		Map param = new HashMap();
		param.put("billId", billId);
		this.delete("deleteBillInfo", param);
	}

}
