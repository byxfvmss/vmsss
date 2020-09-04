package com.cjit.vms.trans.service.billPreOpen.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.model.GoodsInfo;

import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.createBill.BillGoodsInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillPreInfo;
import com.cjit.vms.trans.service.billPreOpen.BillPreOpenService;

public class BillPreOpenServiceImpl extends GenericServiceImpl implements BillPreOpenService {
	@Override
	public List selectBillInfoList(BillPreInfo billPreOpen, PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = billPreOpen.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("billPreOpen", billPreOpen);
		// 添加判断区分票据预开和发票信息
		if (billPreOpen.getType().isEmpty()) {
			return find("selectbillPreOpenList", map, paginationList);
		}
		return findLargeData("selectInvoiceList", map, paginationList);
	}

	@Override
	public void deleteBillPreOpen(String billId) {
		Map param = new HashMap();
		param.put("billId", billId.split(","));
		this.delete("deleteBillPreOpens", param);
	}

	public void delBillItemById(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("billId", billId.split(","));
		this.delete("delBillItemByIds", map);
	}

	@Override
	public void deleteBillTransBus(String billId) {
		Map param = new HashMap();
		param.put("billId", billId.split(","));
		delete("deleteBillTransBus", param);
	}

	public Customer findCustomer(String id) {
		Customer customer = new Customer();
		customer.setCustomerTaxno(id);
		Map map = new HashMap();
		map.put("customer", customer);
		List list = this.find("findFirstCustomer", map);
		if (null != list && list.size() == 1) {
			customer = (Customer) list.get(0);
			return customer;
		} else {
			return null;
		}
	}

	public List getInstInfoList(InstInfo info) {

		Map map = new HashMap();
		List instIds = info.getLstAuthInstIds();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("inasId", info.getInstId());
		map.put("user_id", info.getUserId());
		map.put("instName", info.getInstName());
		map.put("taxNo", info.getTanNo());
		map.put("taxFlag", info.getTaxFlag());
		return this.find("getInstInfoListForPreOpen", map);
	}

	public Map findCodeDictionary(String codeType) {
		Map retMap = new HashMap();
		Map param = new HashMap();
		param.put("codeType", codeType);
		List listCD = this.find("findCodeDictionaryListForPre", param);
		if (listCD != null && listCD.size() > 0) {
			for (int i = 0; i < listCD.size(); i++) {
				CodeDictionary cd = (CodeDictionary) listCD.get(i);
				retMap.put(cd.getCodeValueStandardNum(), cd.getCodeName().equals("null") ? "" : cd.getCodeName());
			}
		}
		return retMap;
	}

	public String createBillId(String tabFlag) {
		String temp = DateUtils.serverCurrentDetailDate();
		String sequence = this.getBillIdSequence();
		return tabFlag + temp + sequence;
	}

	public String getBillIdSequence() {
		Map para = new HashMap();
		List list = this.find("getBillIdSequenceForPre", para);
		String sequence = (String) list.get(0);
		return sequence;
	}

	public void savePreBillInfo(BillInfo billInfo, boolean isUpdate) {
		Map param = new HashMap();
		Map map = new HashMap();
		param.put("billInfo", billInfo);
		// 如果存在billId,则删除相关记录
		// 发票代码
		map.put("invoiceCode", billInfo.getBillCode());
		// 发票号码
		map.put("invoiceNo", billInfo.getBillNo());
		// 标记发票状态默认正常使用1
		map.put("invoiceStatus", "1");
		if (null == billInfo.getBillId() || "".equals(billInfo.getBillId())) {
			this.delete("delBillItemById", param);
			this.delete("deleteBillPreOpen", param);			
		}
		this.update("updateInvalidPaperInvoiceUseDetail", map);
		this.save("saveBillForPre", param);
	}

	public List findGoodsInfoList(GoodsInfo goodsInfo) {
		Map map = new HashMap();
		map.put("goods", goodsInfo);
		return find("findGoodsListForPre", map);
	}

	@Override
	public void constructBillInfo(String inputObject, Object obj, BillInfo billInfo) {
		if ("customer".equals(inputObject)) {
			Customer customer = (Customer) obj;
			// 客户纳税人名称
			billInfo.setCustomerName(customer.getCustomerCName());
			// 客户纳税人识别号
			billInfo.setCustomerTaxno(customer.getCustomerTaxno());
			// 2019
			billInfo.setCustomerId(customer.getCustomerID());
			// 客户地址电话
			billInfo.setCustomerAddressandphone(customer.getCustomerAddress() + " " + customer.getCustomerPhone());
			// 客户银行账号
			billInfo.setCustomerBankandaccount(customer.getCustomerCBank() + " " + customer.getCustomerAccount());
			// 发票类型
			billInfo.setFapiaoType(customer.getFapiaoType());
		}
	}

	@Override
	public void saveGoodsInfoList(List goodsInfoList) {
		String billId = ((BillGoodsInfo) goodsInfoList.get(0)).getBillId();
		deleteBillTransBus(billId);
		this.deleteBatch("delBillItemById", goodsInfoList);
		this.insertBatch("saveBillItemInfoList", goodsInfoList);
		// 更新票据金额
		BigDecimal income = new BigDecimal("0");
		BigDecimal taxAmt = new BigDecimal("0");
		for (int i = 0; i < goodsInfoList.size(); i++) {
			BillGoodsInfo goodsInfo = (BillGoodsInfo) goodsInfoList.get(i);

			income = income.add(goodsInfo.getAmt().subtract(goodsInfo.getTaxAmt()));
			taxAmt = taxAmt.add(goodsInfo.getTaxAmt());
		}
		BigDecimal amtSum = income.add(taxAmt);
		BillPreInfo billInfo = new BillPreInfo();
		billInfo.setBillId(billId);
		billInfo.setSumAmt(amtSum);// 价税合计
		billInfo.setAmtSum(income);// 收入
		billInfo.setTaxAmtSum(taxAmt);// 税额
		Map param = new HashMap();
		param.put("billInfo", billInfo);
		this.update("updatePreBillAmt", param);

	}

	@Override
	public void saveBillTransBus(List billTransBus) {
		if (null != billTransBus && billTransBus.size() > 0) {
			for (int i = 0; i < billTransBus.size(); i++) {
				Map map = new HashMap();
				map.put("billTransBus", billTransBus.get(i));
				save("insertBillTransBus", map);
			}
		}
	}

//	@Override
//	public void updateBillPreOpenByBillId(String billIds, String dataStatus) {
//		Map map = new HashMap();
//		map.put("billId", billIds.split(","));
//		map.put("dataStatus", dataStatus);
//		this.update("updateBillPreOpenDataStatusByIds", map);
//		//
//
//	}
//
	@Override
	public List billPreOpenListToExcl(BillPreInfo billPreOpen, String userID) {
		Map map = new HashMap();
		List instIds = billPreOpen.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); i++) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		billPreOpen.setDatastatus("1");
		map.put("auth_inst_ids", lstTmp);
		map.put("billInfo", billPreOpen);
		return find("billPreOpenListToExcl", map);
	}

//
//
//
//	public List selBillAmtByBillId(BillPreInfo bill) {
//		Map map = new HashMap();
//		map.put("billInfo", bill);
//		return find("selBillAmtByBillIdForPre", map);
//
//	}
//
//
//
//	public Organization getOrganization(Organization org) {
//		Map map = new HashMap();
//		map.put("organization", org);
//		List list = this.find("getOrganizationForPre", map);
//		if (CollectionUtil.isNotEmpty(list)) {
//			return (Organization) list.get(0);
//		} else {
//			return null;
//		}
//	}
//
//
//	public void saveOrUpPreBillItemInfoList(List billItemInfoList,
//			boolean isUpdate) {
//		Map param = new HashMap();
//		param.put("billItemList", billItemInfoList);
//		if (isUpdate) {
//			this.updateRptDataBatch("upBillItemInfoList", billItemInfoList);
//		} else {
//			this.insertBatch("saveBillItemInfoList", billItemInfoList);
//		}
//	}
//
//	public List findBillItemInfoList(BillItemInfo billItemInfo) {
//		Map map = new HashMap();
//		map.put("billItem", billItemInfo);
//		return find("findBillItemInfoForPre", map);
//	}
//
//
//
//	public List findVmsTaxInfoList(VmsTaxInfo vmsTaxInfo) {
//		Map map = new HashMap();
//		map.put("taxId", vmsTaxInfo.getTaxId());
//		map.put("taxno", vmsTaxInfo.getTaxno());
//		map.put("fapiaoType", vmsTaxInfo.getFapiaoType());
//		map.put("taxRate", vmsTaxInfo.getTaxRate());
//		map.put("user_id", vmsTaxInfo.getUser_id());
//		return find("getListTaxItemInfoForPre", map);
//	}
//
//
//
//	public BillPreInfo findBillByIdForPre(String billId) {
//		Map map = new HashMap();
//		map.put("billId", billId);
//		List list = this.find("findBillByIdForPre", map);
//		BillPreInfo billInfo = new BillPreInfo();
//		if (list != null && list.size() == 1) {
//			return (BillPreInfo) list.get(0);
//		} else {
//			return null;
//		}
//	}
//
//	@Override
//	public GoodsInfo findGoodsForPre(String goodsId) {
//		Map map = new HashMap();
//		map.put("goodsId", goodsId);
//		List list = find("findGoodsForPre", map);
//		GoodsInfo goods = null;
//		if (list.size() > 0) {
//			goods = (GoodsInfo) list.get(0);
//		}
//		return goods;
//	}
//
//
////	@Override
////	public List selectBillTransBus(BillTransBus billTransBus,PaginationList paginationList) {
////		Map map = new HashMap();
////		map.put("billTransBus", billTransBus);
////		if (null!=paginationList) {
////			return find("selectBillTransBus", map, paginationList);
////		}
////		
////		return find("selectBillTransBus",map);
////	}
	@Override
	public Object findBillInfoByBillId(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		List list = find("findBillInfoByBillId", map);
		if (list != null && list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List selectBillTransBus(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		List list = find("selectBillTransBus", map);
		if (list != null && list.size() != 0) {
			return list;
		} else {
			return null;
		}
	}

	@Override
	public List<BillItemInfo> findBillItemInfo(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		List<BillItemInfo> list = find("findBillItemInfoForEdit", map);
		if (list != null && list.size() != 0) {
			return list;
		} else {
			return null;
		}
	}

	@Override
	public void updateBillItemInfo(BillItemInfo billItemInfo, List goodsInfoList) {
		delBillItemById(billItemInfo.getBillId());
		this.insertBatch("saveBillItemInfoList", goodsInfoList);
	}

	@Override
	public void commitBillInfo(String billId) {
		Map param = new HashMap();
		param.put("billId", billId.split(","));

		Map map = new HashMap();
		map.put("billId", billId);

		List list = find("findReviewerNew", map);
		param.put("reviewer", list.get(0));

		List list007 = find("findFapiaoType", map);
		if (list007.get(0).equals("1")) {
			update("commitZhuanpiao", param);
		} else {
			update("commitBillInfo", param);
		}
	}

	@Override
	public void updatePaperInvoiceState(String billId) {
		String billIds[] = billId.split(",");
		Map map = new HashMap();
		map.put("billIds", billIds);
		List<Map<String, String>> billInfoList = new ArrayList<Map<String, String>>();
	     billInfoList = this.find("findBillInfoByIds", map);
		if (billInfoList.size() != 0) {
			for (Map<String, String> billInfo : billInfoList) {
				Map invoicemap = new HashMap();
				invoicemap.put("invoiceCode", billInfo.get("BILLCODE"));
				invoicemap.put("invoiceNo", billInfo.get("BILLNO"));
				invoicemap.put("invoiceStatus", "0");
				update("updateInvalidPaperInvoiceUseDetail", invoicemap);
			}

		}

	}


}
