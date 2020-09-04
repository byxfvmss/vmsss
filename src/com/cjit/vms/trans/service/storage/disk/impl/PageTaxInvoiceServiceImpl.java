package com.cjit.vms.trans.service.storage.disk.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.TaxDiskMonitorInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceStockDetail;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;

public class PageTaxInvoiceServiceImpl extends GenericServiceImpl implements PageTaxInvoiceService {

	public List findPageTaxInvoice(PaperInvoiceListInfo paperInvoiceListInfo,PaginationList paginationList ) {
		Map map=new HashMap(); 
		map.put("paperInvoiceListInfo", paperInvoiceListInfo);
		map.put("userID", paperInvoiceListInfo.getUserID());
		List instIds=paperInvoiceListInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		List list=find("findPaperInvoiceInfoByTaxNo",map);
		if(paginationList==null){
			return list;
		}
		if(list.size()==0){
			return list;
		}else{
			list=find("findPaperInvoiceInfoByTaxNo",map,paginationList);
		}
		 return list;
	}

	public void addOrUpdateTaxDiskInfo(TaxDiskInfo taxDiskInfo) {
		TaxDiskInfo taxDiskInfo1= findTaxDiskInfoByTaxNoAndDiskNo(taxDiskInfo.getTaxDiskNo());
		Map map=new HashMap();
		map.put("taxDiskInfo", taxDiskInfo); 
		if(taxDiskInfo1==null){
			save("saveTaxInfobyTax",map);
		}else{
			update("updateTaxInfobyTax", map);
		}
	}

	public void delTaxDiskInfoByTaxNoAndDiskNo(String taxDiskNo,
			String taxpayerNo) {
		// TODO Auto-generated method stub
		
	}

	public TaxDiskInfo findTaxDiskInfoByTaxNoAndDiskNo(String taxDiskNo) {
		Map map=new HashMap();
		map.put("taxDiskNo", taxDiskNo);
		List list=find("findTaxDiskInfoByTaxNoAndDiskNo",map);
		TaxDiskInfo taxDiskInfo=null;
		if(list.size()!=0 && list.size()==1) {
			taxDiskInfo=new TaxDiskInfo();
			 taxDiskInfo	=(TaxDiskInfo)list.get(0);
		}
		return taxDiskInfo;
	}

	

	public String getInstIdbyTaxNo(String taxNo) {
		Map map =new HashMap();
		map.put("taxNo", taxNo);
		List list=find("getInstIdbyTaxNo", map);
		String instId="";
		if(list.size()> 0 && list.size()==1){
			instId=(String)list.get(0);
		}
		return instId;
	}

	

	
	
	public void savePaperInvoiceStockDetail(
			PaperInvoiceStockDetail paperInvoiceStockDetail) {
		Map map=new HashMap();
		map.put("paperInvoiceStockDetail", paperInvoiceStockDetail);
		save("savePaperInvoiceOnTax", map);
	}

	public void deleteVmsTaxInfoById(VmsTaxInfo vmsTaxInfo) {
		Map map=new HashMap();
		map.put("vmsTaxInfo", vmsTaxInfo);
		delete("deleteVmsTaxInfoById", map);
	}

	public VmsTaxInfo findVmsTaxInfoById(String taxId,String taxNo,String fapiaoType) {
		Map map=new HashMap();
		map.put("taxId", taxId);
		map.put("taxNo", taxNo);
		map.put("fapiaoType", fapiaoType);
		List list=find("findVmsTaxInfoById", map);
		VmsTaxInfo vmsTaxInfo=null;
		if(list.size()>0 && list.size()==1){
			vmsTaxInfo=(VmsTaxInfo) list.get(0);
		}
		return vmsTaxInfo;
	}

	public void saveVmsTaxInfo(VmsTaxInfo vmsTaxInfo) {
		if(StringUtil.isNotEmpty(vmsTaxInfo.getTaxId())){
			VmsTaxInfo	 vmsTaxInfo1=findVmsTaxInfoById(vmsTaxInfo.getTaxId(),vmsTaxInfo.getTaxno(),vmsTaxInfo.getFapiaoType());
			if(vmsTaxInfo1!=null){
				deleteVmsTaxInfoById(vmsTaxInfo);
			}
		}
		Map map=new HashMap();
		map.put("vmsTaxInfo", vmsTaxInfo);
		save("saveVmsTaxInfo", map);
	}

	public void deletePaperAutoInvoice(String invoiceType, String taxDiskNo,
			String taxpayerNo, String invoiceCode, String invoiceBeginNo) {
			Map map=new HashMap();
			map.put("invoiceType", invoiceType);
			map.put("taxDiskNo", taxDiskNo);
			map.put("taxpayerNo", taxpayerNo);
			map.put("invoiceCode", invoiceCode);
			map.put("invoiceBeginNo", invoiceBeginNo);
			delete("deletePaperAutoInvoice", map);
	}

	public void savePaperAutoInvoice(PaperAutoInvoice paperAutoInvoice) {
		deletePaperAutoInvoice(paperAutoInvoice.getInvoiceType(),paperAutoInvoice.getTaxDiskNo(),paperAutoInvoice.getTaxpayerNo()
				,paperAutoInvoice.getInvoiceCode(),paperAutoInvoice.getInvoiceBeginNo());
		Map map=new HashMap();
		map.put("paperAutoInvoice", paperAutoInvoice);
		save("savePaperAutoInvoice", map);
	}

	public List findPaperAutoInvoiceDetial(String invoiceType,
			String taxDiskNo, String taxpayerNo, String invoiceCode,
			String invoiceBeginNo,PaginationList paginationList) {
				Map map=new HashMap();
				map.put("invoiceType", invoiceType);
				map.put("taxDiskNo", taxDiskNo);
				map.put("taxpayerNo", taxpayerNo);
				map.put("invoiceCode", invoiceCode);
				map.put("invoiceBeginNo", invoiceBeginNo);
		return find("findPaperAutoInvoiceDetial", map, paginationList);
	}

	public void addOrupdateMonTaxDiskInfoByDiskinfo(TaxDiskMonitorInfo taxDiskMonitorInfo) {
		Map map =new HashMap();
		map.put("taxDiskMonitorInfo", taxDiskMonitorInfo);
		if(StringUtil.isNotEmpty(findMonTaxDiskinfosByDiskNo(taxDiskMonitorInfo))){
			update("updateMonTaxDiskInfo", map);
		}else{
			save("saveMonTaxDiskinfo", map);
		}
		
		
	}

	public String findMonTaxDiskinfosByDiskNo(TaxDiskMonitorInfo taxDiskMonitorInfo) {
		Map map=new HashMap();
		map.put("taxDiskMonitorInfo", taxDiskMonitorInfo);
		List list=find("findMontaxDiskInfobyDiskNoandType", map);
		String diskNo="";
		if(list.size()>0 && list.size()==1){
			diskNo=(String)list.get(0);
		}
		return diskNo;
	}


	@Override
		public void updateInvoiceRedNum(String invoiceBeginNo, String invoiceEndNo,
				String currentInvoiceCode) {
			Map<String,String> map=new HashMap<String, String>();
			map.put("invoiceBeginNo",invoiceBeginNo );
			map.put("invoiceEndNo",invoiceEndNo );
			map.put("currentInvoiceCode",currentInvoiceCode );
			update("updateInvoiceRedNum", map);
			
		}

		@Override
		public void updateIssueCancleNum(String invoiceBeginNo,
				String invoiceEndNo, String currentInvoiceCode) {
			Map<String,String> map=new HashMap<String, String>();
			map.put("invoiceBeginNo",invoiceBeginNo );
			map.put("invoiceEndNo",invoiceEndNo );
			map.put("currentInvoiceCode",currentInvoiceCode );
			update("updateIssueCancleNum", map);

			
		}

		@Override
		public void updateinvoiceEmptyNum(String invoiceBeginNo,
				String invoiceEndNo, String currentInvoiceCode) {
			Map<String,String> map=new HashMap<String, String>();
			map.put("invoiceBeginNo",invoiceBeginNo );
			map.put("invoiceEndNo",invoiceEndNo );
			map.put("currentInvoiceCode",currentInvoiceCode );
			update("updateinvoiceEmptyNum", map);

			
		}

		@Override
		public void updateissueinvoiceNum(String invoiceBeginNo,
				String invoiceEndNo, String currentInvoiceCode,String currentBillNo) {
			Map<String,String> map=new HashMap<String, String>();
			map.put("invoiceBeginNo",invoiceBeginNo );
			map.put("invoiceEndNo",invoiceEndNo );
			map.put("currentInvoiceCode",currentInvoiceCode);
			map.put("currentBillNo", currentBillNo);
			update("updateissueinvoiceNum", map);

			
		}

		@Override
		public void updatepaperAutoInvoicebybusId(PaperAutoInvoice paperAutoInvoice) {
			Map<String,PaperAutoInvoice> map=new HashMap<String, PaperAutoInvoice>();
			map.put("paperAutoInvoice",paperAutoInvoice);
		
			update("updatepaperAutoInvoicebybusId", map);

		}

		@Override
		public List<PaperAutoInvoice> findpaperAutoInvoicebyBusId(
				String invoiceNo, List<String> authInstIds, String invoiceCode) {
			Map map=new HashMap();
			map.put("invoiceNo", invoiceNo);
			map.put("authInstIds", authInstIds);
			map.put("invoiceCode", invoiceCode);
			
			List<PaperAutoInvoice> list=find("findpaperBankAutoInvoicebyBusId", map);
			if(list.size()>0&&list.size()==1){
				return list;
			}
			
			return null;
		}

		@Override
		public void updateinvoiceEmptyCurrentNo(String invoiceBeginNo,
				String invoiceEndNo, String currentInvoiceCode, String currentBillNo) {
			Map map=new HashMap();
			map.put("invoiceBeginNo", invoiceBeginNo);
			map.put("invoiceEndNo", invoiceEndNo);
			map.put("currentInvoiceCode", currentInvoiceCode);
			map.put("currentBillNo", currentBillNo);
			
		}
		@Override
		public void updateDistributeAfterIssue(String billCode,
				String invoiceBeginNo,String invoiceEndNo, String billNo) {

			Map map=new HashMap();
			map.put("billCode", billCode);
			map.put("invoiceBeginNo", invoiceBeginNo);
			map.put("billNo", billNo);
			map.put("invoiceEndNo", invoiceEndNo);
			update("updateDistributeAfterIssue", map);
		}

		@Override
		public void updateStorckdetialAfterIssue(String billNo, String billBeginNo,
				String biilCode) {
			Map map=new HashMap();
			map.put("billNo", billNo);
			map.put("billBeginNo", billBeginNo);
			map.put("biilCode", biilCode);
			update("updateStorckdetialAfterIssue", map);
			 
			
		}
		@Override
		public void copyAutoInvoiceToStockDetail() {
			Map map=new HashMap();
			this.save("copyAutoInvoiceToStockDetail", map);
		}

		@Override
		public void copyAutoInvoiceTostock() {
			Map map=new HashMap();
			this.save("copyAutoInvoiceTostock", map);
			
		}

		@Override
		public void updateSynStockDetial() {
			Map map=new HashMap();
			this.update("updateSynStockDetial", map);
			
		}

}
