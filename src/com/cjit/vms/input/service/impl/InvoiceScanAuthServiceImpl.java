package com.cjit.vms.input.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.input.model.InputTransInfo;
import com.cjit.vms.input.service.InvoiceScanAuthService;
import com.cjit.vms.input.model.InputInvoiceInfo;
import com.cjit.vms.input.model.InputInvoiceItem;
import com.cjit.vms.trans.model.UBaseInst;
import com.cjit.vms.input.model.VendorInfo;

public class InvoiceScanAuthServiceImpl extends GenericServiceImpl implements InvoiceScanAuthService {

	public List findListInvoiceScanAuth(InputInvoiceInfo info,
			PaginationList paginationList) {
		Map params = new HashMap();
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		params.put("auth_inst_ids", lstTmp); 
		params.put("bill_date", info.getBillDate());
		params.put("vendor_name", info.getVendorName());
		params.put("datastatus", info.getDatastatus());
		params.put("inst_id", info.getInstcode());
		params.put("bill_code", info.getBillCode());
		params.put("fapiao_type", info.getFapiaoType());
		params.put("identify_date", info.getIdentifyDate());
		params.put("bill_no", info.getBillNo());
		if(paginationList==null){
			return this.find("findListInvoiceScanAuth", params);
		}
		return this.find("findListInvoiceScanAuth", params, paginationList);
	}
	public String findSequenceBillItemId(){
		Map para = new HashMap();
		List list = this.find("getBillItemIdSequence", para);
		return (String) list.get(0);
	}
	public InputInvoiceInfo findInvoiceScanAuthByBillId(String billId) {
		Map params = new HashMap();
		params.put("bill_id", billId);
		return (InputInvoiceInfo) this.findForObject("findInvoiceScanAuthByBillId", params);
	}

	public List findInvoiceScanAuthItemsByBillId(String billId) {
		HashMap params=new HashMap();
		params.put("bill_id", billId);
		return  this.find("findVmsInputInvoiceItemsByBillId", params);
	}

	public InputTransInfo findInvoiceScanAuthTransInfoByBillId(String bill_code,String bill_no) {
		Map params = new HashMap();
		params.put("bill_code", bill_code);
		params.put("bill_no", bill_no);
		return (InputTransInfo) this.findForObject("findInvoiceScanAuthTransInfoByBillId", params);
	}

	public void updateVmsInputInvoiceInfoForScanAuth(
			InputInvoiceInfo inputInvoiceInfo, String o_bill_id) {
//		if(lstGoodAdded!=null&&lstGoodAdded.size()>0){
//			for(int i=0;i<lstGoodAdded.size();i++){
//				InputInvoiceItem item=(InputInvoiceItem) lstGoodAdded.get(i);
//				Map addMap = new HashMap();
//				addMap.put("info", item);
//				this.save("saveVmsInputInvoiceItem", addMap);
//			}
//		}
		Map params = new HashMap();
		inputInvoiceInfo.setBillId(o_bill_id);
		params.put("info", inputInvoiceInfo); 
		this.update("updateVmsInputInvoiceInfoForScanAuth", params);
	}
	/**
	 * 新增商品信息
	 * @param item
	 */
	public void insertVmsInputInvoiceItem(InputInvoiceItem item){
		Map addMap = new HashMap();
		addMap.put("info", item);
		this.save("insertVmsInputInvoiceItem", addMap);
	}
	/**
	 * 编辑更新商品信息 
	 * @param item 
	 */
	public void updateVmsInputInvoiceItem(InputInvoiceItem item){
		Map addMap = new HashMap();
		addMap.put("info", item);
		this.save("saveVmsInputInvoiceItemInfo", addMap);
	}
	/**
	 * 删除商品信息
	 * @param item
	 */
	public void deleteVmsInputInvoiceItem(InputInvoiceItem item){
		Map addMap = new HashMap();
		addMap.put("info", item);
		this.save("deleteVmsInputInvoiceItem", addMap);
	}
	
	
	public void updateVmsInputInvoiceInfoForAuthSubmit(
			String datastatus, String[] billIds) {
		for(int i=0;i<billIds.length;i++){
			Map params = new HashMap();
			params.put("bill_id", billIds[i]); 
			params.put("datastatus", datastatus); 
			params.put("operatestatus", datastatus); 
			this.update("updateVmsInputInvoiceInfoForAuthSubmit", params);
		}
	}

	
	public void saveVmsInputInvoiceInfoImport(List dataList){
		for(int i=0;i<dataList.size();i++){
			Map info = (Map) dataList.get(i);
			Map checkParams = new HashMap();
			checkParams.put("bill_code", info.get("billCode"));
			checkParams.put("bill_no", info.get("billNo"));
			InputInvoiceInfo oldObj=(InputInvoiceInfo) this.findForObject("findInvoiceScanAuthByBillCodeAndBillNo", checkParams);
			//导入数据整理
			if((null==info.get("conformFlg"))||(info.get("conformFlg").toString().isEmpty())){
				info.put("conformFlg","1");
			}
			if((null==info.get("balance"))||(info.get("balance").toString().isEmpty())){
				info.put("balance","0");
			}
			if((null==info.get("faPiaoType"))||(info.get("faPiaoType").toString().isEmpty())){
				info.put("faPiaoType","0");
			}
//			if((null==info.get("scanDate"))||(info.get("scanDate").toString().isEmpty())){
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//				info.put("scanDate",df.format(new Date()));
//			}
			InputInvoiceInfo inputInvoiceInfo = new InputInvoiceInfo();
			inputInvoiceInfo.setBillId((String)info.get("billId"));
			inputInvoiceInfo.setBillCode((String)info.get("billCode"));
			inputInvoiceInfo.setBillNo((String)info.get("billNo"));
			inputInvoiceInfo.setBillDate((String)info.get("billDate"));
			String taxNo = (String)info.get("vendorTaxNo");
			//String instCode = (String)info.get("instCode");
			inputInvoiceInfo.setTaxno(taxNo);
			Map instMap = new HashMap();
			/* 机构 跟纳税人识别号是一对一 已向大龙确认*/
			instMap.put("taxNo", taxNo);
			//instMap.put("instCode", instCode);
			
			UBaseInst inst = (UBaseInst)this.findForObject("findBaseInstByTaxNoAndInstCode", instMap); 
			if (null != inst) {
				if (StringUtils.isNotEmpty(inst.getTaxperName())){
					inputInvoiceInfo.setName(inst.getTaxperName());
				}
				if (StringUtils.isNotEmpty(inst.getTaxAddress()) || StringUtils.isNotEmpty(inst.getTaxTel())){
					inputInvoiceInfo.setAddressandphone(inst.getTaxAddress() + inst.getTaxTel());
				}
				if (StringUtils.isNotEmpty(inst.getTaxBank()) || StringUtils.isNotEmpty(inst.getAccount())){
					inputInvoiceInfo.setBankandaccount(inst.getTaxBank() + inst.getAccount());
				}
			}
			inputInvoiceInfo.setAmtSum(strToBigDecimal((String)info.get("amtSum")));
			inputInvoiceInfo.setTaxAmtSum(strToBigDecimal((String)info.get("taxAmtSum")));
			inputInvoiceInfo.setSumAmt(inputInvoiceInfo.getAmtSum().add(inputInvoiceInfo.getTaxAmtSum()));
			inputInvoiceInfo.setRemark((String)info.get("remark"));
			inputInvoiceInfo.setDrawer((String)info.get("drawer"));
			inputInvoiceInfo.setReviewer((String)info.get("reviewer"));
			inputInvoiceInfo.setPayee((String)info.get("payee"));
			String vendor_taxno=(String)info.get("vendorTaxNo");
			String vendorName="";
			String vendor_bankandaccount="";
			String vendor_addressandphone="";
			VendorInfo vendorInfo=findVendorInfo(vendor_taxno);
			if(null!=vendorInfo){
				if(StringUtils.isEmpty(vendorInfo.getVendorCName())){
					vendorName=vendorInfo.getVendorEName();
				}else{
					vendorName=vendorInfo.getVendorCName();
				}
				vendor_addressandphone=vendorInfo.getVendorAddress()+vendorInfo.getVendorPhone();
				if(StringUtils.isEmpty(vendorInfo.getVendorCBank())){
					vendor_bankandaccount=vendorInfo.getVendorEBank()+vendorInfo.getVendorAccount();
				}else{
					vendor_bankandaccount=vendorInfo.getVendorCBank()+vendorInfo.getVendorAccount();
				}
			}
			inputInvoiceInfo.setVendorName(vendorName);
			inputInvoiceInfo.setVendorTaxno(vendor_taxno);
			inputInvoiceInfo.setVendorAddressandphone(vendor_addressandphone);
			inputInvoiceInfo.setVendorBankandaccount(vendor_bankandaccount);
			inputInvoiceInfo.setInstcode((String)info.get("instCode"));
			inputInvoiceInfo.setNoticeNo((String)info.get("noticeNo"));
			inputInvoiceInfo.setDescription((String)info.get("description"));
			inputInvoiceInfo.setVatOutProportion((String)info.get("vatOutProportion"));
			inputInvoiceInfo.setVatOutAmt(strToBigDecimal((String)info.get("vatOutAmt")));
			inputInvoiceInfo.setConformFlg((String)info.get("conformFlg"));
			inputInvoiceInfo.setBalance((String)info.get("balance"));
			inputInvoiceInfo.setFapiaoType((String)info.get("faPiaoType"));
			inputInvoiceInfo.setScanOperator((String)info.get("scanOperator"));
			inputInvoiceInfo.setUrlBillImage((String)info.get("urlBillImage"));
			/*导入逻辑处理*/
			String curDatastatus=(String) info.get("dataStatus");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			String today = df.format(new Date());
			if ("0".equals(curDatastatus)){
				inputInvoiceInfo.setScanDate(today);
			} else if ("1".equals(curDatastatus)){
				inputInvoiceInfo.setIdentifyDate(today);
			}
			if(null==oldObj){//发票代码和发票号码不存在时，做插入处理
			//	String addDatastatus=getInvoiceInfoDatastatus(curDatastatus,oldObj,"add");
				//if(StringUtils.isNotEmpty(addDatastatus)){
					
					inputInvoiceInfo.setDatastatus((String) info.get("dataStatus"));
					inputInvoiceInfo.setOperateStatus((String) info.get("dataStatus"));
					Map addInfoMap = new HashMap();
					addInfoMap.put("info", inputInvoiceInfo);
					this.save("saveVmsInputInvoiceInfoForImport", addInfoMap);
				//}
			}else{
				
				//String updateDatastatus=getInvoiceInfoDatastatus(curDatastatus,oldObj,"update");
				//if(StringUtils.isNotEmpty(updateDatastatus)&& !"do_nothing".equals(updateDatastatus)){
					//do update
					inputInvoiceInfo.setDatastatus((String) info.get("dataStatus"));
					inputInvoiceInfo.setOperateStatus(oldObj.getOperateStatus());
					Map uptInfoMap = new HashMap();
					uptInfoMap.put("info", inputInvoiceInfo);
					this.update("updateVmsInputInvoiceInfoForImport", uptInfoMap);
				//}
			}
		}
	}
	
	private VendorInfo findVendorInfo(String vendor_taxno){
		Map params = new HashMap();
		params.put("vendor_taxno", vendor_taxno);
		return (VendorInfo) this.findForObject("findVendorInfoByVendorTaxno", params);
	}
	
	private BigDecimal strToBigDecimal(String data){
			BigDecimal bd = null;
			if(data!=null&&!"".equals(data)){
				bd = new BigDecimal(data);
			}else{
				return null;
			}
			return bd;
	}
	private String getInvoiceInfoDatastatus(String curDatastatus,InputInvoiceInfo oldObj,String opFlg){
		if("add".equals(opFlg)){
			if("0".equals(curDatastatus)){
				return "1";
			}
			if("1".equals(curDatastatus)){
				return "3";
			}
			if("2".equals(curDatastatus)){
				return "4";
			}
			return null;
		}
		if("update".equals(opFlg)){
			if("1".equals(curDatastatus)){
				if("1".equals(oldObj.getDatastatus())){
					return "3";
				}
				if("4".equals(oldObj.getDatastatus())){
					return "5";
				}
				if("6".equals(oldObj.getDatastatus())){
					return "5";
				}
				return null;
			}
			if("2".equals(curDatastatus)){
				if("1".equals(oldObj.getDatastatus())){
					return "4";
				}
				if("4".equals(oldObj.getDatastatus())){
					return "6";
				}
				if("6".equals(oldObj.getDatastatus())){
					return "6";
				}
				return null;
			}
			if("0".equals(curDatastatus)){
				Map dsMap = new HashMap(){{
					put("3","3");
					put("5","5");
					put("7","7");
				}};
				if(dsMap.containsKey(oldObj.getDatastatus())){
					return "do_nothing";
				}else{
					return "1";
				}
			}
		}
		return null;
	}
	public InputInvoiceInfo findInvoiceScanAuthByBillCodeAndBillNo(
			String billCode, String billNo) {
		Map checkParams = new HashMap();
		checkParams.put("bill_code", billCode);
		checkParams.put("bill_no", billNo);
		InputInvoiceInfo oldObj=
			(InputInvoiceInfo) this.findForObject("findInvoiceScanAuthByBillCodeAndBillNo", checkParams);
		return oldObj;
	}
	public UBaseInst findUbaseInstByTaxNo(String taxNo) {
		Map map=new HashMap();
		map.put("taxNo", taxNo);
	List list=	find("findBaseInstByTaxNoAndInstCode", map);
	UBaseInst uInst=new UBaseInst();
	if(list.size()>0){
		uInst=(UBaseInst)list.get(0);
	}
		return uInst;
	}
	@Override
	public void saveInputinvoiceData(Map<String, String> map) {

		this.save("saveInputinvoiceData", map);
	}
	@Override
	public List<String> findinputInvoiceCompareinvoiceData(
			Map<String, String> map) {

		return find("findinputInvoiceCompareinvoiceData", map);
	}
	@Override
	public void saveInputinvoiceInfo(Map<String, String> map) {
		this.save("saveInputinvoiceInfo", map);
	}
	@Override
	public void updateInputInvoiceYconformFlg(Map<String, String> map) {
		this.save("updateInputInvoiceYconformFlg", map);
	}
	
	
}
