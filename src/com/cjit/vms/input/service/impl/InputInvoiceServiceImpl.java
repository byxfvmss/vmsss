package com.cjit.vms.input.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.input.model.InformationBills;
import com.cjit.vms.input.model.InputInvoice;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputVatInfo;
import com.cjit.vms.input.service.InputInvoiceService;
import com.cjit.vms.input.model.InformationInput;

public class InputInvoiceServiceImpl extends GenericServiceImpl implements
		InputInvoiceService {

	public List findBussTypeList() {
		Map map = new HashMap();
		List list = find("bussTypeList", map);
		return list;
	}

	public List findInputInvoiceList(InputInvoice inputInvoice,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("inputInvoice", inputInvoice);
		return find("listInputInvoice", map, paginationList);
	}

	public InputInvoice findInputInvoiceDetail(String id) {
		Map map = new HashMap();
		map.put("billId", id);
		List list = find("inputInvoiceDetail", map);
		InputInvoice inputInvoice = new InputInvoice();
		if (list.size() > 0) {
			inputInvoice = (InputInvoice) list.get(0);
		}

		return inputInvoice;
	}

	public List findInputInvoiceList(InputVatInfo inputVatInfo) {
		Map map = new HashMap();
		map.put("inputVatInfo", inputVatInfo);
		return find("findInputVatInfo", map);
	}

	public InputVatInfo findInputInvoiceInfo(String inVatId) {
		InputVatInfo inputVatInfo = new InputVatInfo();
		inputVatInfo.setInVatId(inVatId);
		List list = this.findInputInvoiceList(inputVatInfo);
		if (list != null && list.size() == 1) {
			return (InputVatInfo) list.get(0);
		} else {
			return null;
		}
	}

	public void saveInputInvoiceInfo(InputInvoice inputInvoice) {
		Map param = new HashMap();
		param.put("inputInvoice", inputInvoice);
		this.save("updateInputInvoiceInfo", param);
	}

	public void deleteInputInvoiceInfo(String inVatId, String billCode,
			String billNo) {
		Map param = new HashMap();
		if (StringUtil.isNotEmpty(inVatId)) {
			param.put("inVatId", inVatId);
			this.delete("deleteInputVatInfo", param);
		} else if (StringUtil.isNotEmpty(billCode)
				&& StringUtil.isNotEmpty(billCode)) {
			param.put("billCode", billCode);
			param.put("billNo", billCode);
			this.delete("deleteInputVatInfo", param);
		}
	}

	public void importInputInvoice(List dataList) {
		Map nullMap = new HashMap();
		// 清空临时表 -临时表VMS_INPUT_INVOICE_DATA
		this.delete("deleteInputInvoiceTemp", nullMap);
		// 将票据文件插入临时表
		for (int i = 0; i < dataList.size(); i++) {
			Map paraMap = (Map) dataList.get(i);
			this.save("importInputInvoice", paraMap);
		}
		// 查询主表
		List inputInvoiceList = find("listInputInvoice", nullMap);
		// 和主表对照票据状态
//		for (int i = 0; i < inputInvoiceList.size(); i++) {
//			InputInvoice inputInvoiceMain = (InputInvoice) inputInvoiceList
//					.get(i);
//			for (int j = 0; j < dataList.size(); j++) {
//				Map inputInvoiceTemp = (Map) dataList.get(j);
//				String mainKey = inputInvoiceMain.getBillCode()
//						+ inputInvoiceMain.getBillNo();
//				String tempKey = (String) inputInvoiceTemp.get("billCode")
//						+ (String) inputInvoiceTemp.get("billNo");
//				if (mainKey.equals(tempKey)) {
//					// 获取上一次认证状态
//					//String oldStatus = inputInvoiceMain.getAuthenticationFlag();
//					if (null == inputInvoiceTemp.get("authenticationFlag")) {
//						//inputInvoiceMain.setAuthenticationFlag("0");
//					} else {
//						// 获取本次认证状态
//						String newStatus = (String) inputInvoiceTemp
//								.get("authenticationFlag");
//						// 本次未通过
//						if (newStatus.equals("N")) {
//							//inputInvoiceMain.setAuthenticationFlag("3");
//							// 认证通过
//						} else if (newStatus.equals("Y")) {
//							// 本次认证通过
////							if (oldStatus.equals("3")) {
////								inputInvoiceMain.setAuthenticationFlag("2");
////							} else {
////								inputInvoiceMain.setAuthenticationFlag("1");
////							}
//						}
//					}
//					// 更新主表状态
//					Map map = new HashMap();
//					map.put("inputInvoice", inputInvoiceMain);
//					this.save("updateAuthenticationFlag", map);
//				}
//			}
//		}
		// 插入新数据
		this.save("addInputInvoice", nullMap);
		// 更新已有数据
		for (int i = 0; i < dataList.size(); i++) {
			Map map = (Map) dataList.get(i);
			this.save("updateSelectedInvoice", map);
		}
	}

	public BigDecimal findDeductedAlart(){
		List list = this.find("findDeductedAlart",null);
		if(null!=list||!list.isEmpty()){
			BigDecimal deductedAlart = 
				((InputInvoice) list.get(0)).getDeductedAlart();
			return  deductedAlart;
		}else{
			return null;
		}
	}

	public List findInputInformation(InformationInput informationInput,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("informationInput", informationInput);
		List instIds=informationInput.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids",lstTmp); 
		find("InputInformation",map);
		return find("InputInformation",map,paginationList);
	}
	
	/**
	 * 进项税综合查询-票据菜单
	 */
	public List findInputBillsQuery(InformationBills informationBills,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("informationBills", informationBills);
		List instIds=informationBills.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
//		map.put("auth_inst_ids", SqlUtil.list2String(lstTmp, ",")); 
		map.put("auth_inst_ids", lstTmp); 
		return find("InputBillsQuery",map,paginationList);
	}

	public InformationInput findInputInformationById(String dealNo) {
		Map map=new HashMap();
		map.put("dealNo", dealNo);
		List list=find("InformationInputById",map);
		InformationInput informationInput=new InformationInput();
		if(list.size()>0){
			informationInput=(InformationInput) list.get(0);
		}
		// TODO Auto-generated method stub
		return informationInput;
	}
	
	public InformationBills findInputBillsById(String billCode) {
		Map map=new HashMap();
		map.put("billCode", billCode);
		List list=find("BillsQueryById",map);
		InformationBills informationBills=new InformationBills();
		if(list.size()>0){
			informationBills=(InformationBills) list.get(0);
		}
		return informationBills;
	}
	
	public List findInputBillsByTrans(String billCode,String billNo) {
		Map map=new HashMap();
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		List list=find("BillsQueryByTrans",map);
		return list;
	}
	
	public List findInputBillsByTrans(String id) {
		Map map=new HashMap();
		map.put("transId", id);
		List list=find("BillsQueryByTrans",map);
		return list;
	}
	
	public List findInputInvoiceItemByBill(String billId) {
		Map map=new HashMap();
		map.put("billId", billId);
		List list=find("BillsQueryByTrans",map);
		return list;
	}

	public List findInputItemList(String id) {
		Map map=new HashMap();
		map.put("id", id);
		if(StringUtil.isNotEmpty(id)){
		List list=find("listInputItem",map);
		
		return list;
		}
		return null;
	}
	//xhy
	public List findInputInvoiceInfoList(Map map,
			PaginationList paginationList){
				return find("findInputInvoiceInfoList", map, paginationList);
			}
	public List redReceiptDetail(Map map){
		return find("redReceiptDetail", map);
	}
	public List findItemInfo(Map map){
		return find("findInputItemInfo", map);
	}
	public List findInvoiceInfoList(Map map){
		return find("findInvoiceInfoList", map);
	}
	public void updateInputInvoiceInfo(InputInvoiceInfo inputInvoiceInfo){
		Map param = new HashMap();
		param.put("inputInvoiceInfo", inputInvoiceInfo);
		this.save("updateInputInvoiceStatus", param);
	}
	public List listInputTransItem(Map map){
		return find("findInputTransList", map);
	}

	public InputInvoiceInfo findInputInformationViewImg(String dealNo) {
		Map map = new HashMap();
		map.put("dealNo", dealNo);
		return (InputInvoiceInfo) this.findForObject("findInputInvoiceInfoBillImg", map);
	}
}
