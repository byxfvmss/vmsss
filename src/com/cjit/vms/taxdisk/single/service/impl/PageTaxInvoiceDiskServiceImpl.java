package com.cjit.vms.taxdisk.single.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.trans.model.TaxDiskMonitorInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.taxdisk.single.model.BuyBillInfoQuery;
import com.cjit.vms.taxdisk.single.model.MonitorInfoQuery;
import com.cjit.vms.taxdisk.single.model.TaxItemInfoQuery;
import com.cjit.vms.taxdisk.single.model.parseXml.TaxDiskInfoQueryReturnXml;
import com.cjit.vms.taxdisk.single.service.PageTaxInvoiceDiskAssitService;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import com.cjit.vms.taxdisk.tools.Message;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;
import com.cjit.vms.taxdisk.tools.AjaxReturn;

public class PageTaxInvoiceDiskServiceImpl extends GenericServiceImpl  implements PageTaxInvoiceDiskAssitService {
	private TaxDiskInfoQueryService taxDiskInfoQueryService;
	@Override
	public String createMonDataQuery(String diskNo,
			String fapiaoType) throws Exception {
		TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo(diskNo);
		MonitorInfoQuery mon=new MonitorInfoQuery(disk.getTaxpayerNo()
				, disk.getTaxDiskNo(),disk.getTaxDiskPsw(),disk.getTaxCertPsw(), fapiaoType);
		mon.setApplyTypeCode(TaxDiskUtil.Application_type_code_1);
		mon.setComment(TaxDiskUtil.comment_Mon_data_query);
		mon.setId(TaxDiskUtil.id_Mon_data_query);
		
		return mon.createMonitorInfoQueryXml();
	}
	@Override
	public void updateTaxDiskInfo(TaxDiskInfoQueryReturnXml disk) {
		Map map=new HashMap();
		map.put("disk", disk);
		update("updateTaxDiskInfoForTwo", map);
		
	}
	public TaxDiskInfoQueryService getTaxDiskInfoQueryService() {
		return taxDiskInfoQueryService;
	}
	public void setTaxDiskInfoQueryService(
			TaxDiskInfoQueryService taxDiskInfoQueryService) {
		this.taxDiskInfoQueryService = taxDiskInfoQueryService;
	}
	
	public void addOrupdateMonTaxDiskInfoByDiskinfo(TaxDiskMonitorInfo taxDiskMonitorInfo) {
		Map map =new HashMap();
		map.put("taxDiskMonitorInfo", taxDiskMonitorInfo);
		if(StringUtil.isNotEmpty(findMonTaxDiskinfosByDiskNo(taxDiskMonitorInfo))){
			update("updateMonTaxDiskInfoInDisk", map);
		}else{
			save("saveMonTaxDiskinfoInDisk", map);
		}
		
		
	}

	public String findMonTaxDiskinfosByDiskNo(TaxDiskMonitorInfo taxDiskMonitorInfo) {
		Map map=new HashMap();
		map.put("taxDiskMonitorInfo", taxDiskMonitorInfo);
		List list=find("findMontaxDiskInfobyDiskNoandTypeInDisk", map);
		String diskNo="";
		if(list.size()>0 && list.size()==1){
			diskNo=(String)list.get(0);
		}
		return diskNo;
	}
	@Override
	public AjaxReturn saveTaxDiskMonInfoQuery(List list) {
		AjaxReturn message=null;
		try {
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					TaxDiskMonitorInfo taxDiskMon=(TaxDiskMonitorInfo) list.get(i);
					addOrupdateMonTaxDiskInfoByDiskinfo(taxDiskMon);
				}
				}
			message=new AjaxReturn(true, Message.tax_disk_mon_info_save_success);
			
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.tax_disk_mon_info_save_erroe);
			return message;
		}
		
		return message;
	}
/*	<nsrsbh>纳税人识别号</nsrsbh>
	<skpbh>税控盘编号</skpbh>
	<skpkl>税控盘口令</skpkl>
	<keypwd>税务数字证书密码</keypwd>
	<fplxdm>发票类型代码</fplxdm>
*/
	@Override
	public String CreateTaxItemXml(String diskNo, String fapiaoType) throws Exception {
		TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo(diskNo);
		TaxItemInfoQuery taxItem=new TaxItemInfoQuery(disk.getTaxpayerNo(), disk.getTaxDiskNo(), disk.getTaxDiskPsw(), disk.getTaxCertPsw(), fapiaoType);
		taxItem.setApplyTypeCode(TaxDiskUtil.Application_type_code_1);
		taxItem.setComment(TaxDiskUtil.comment_Tax_taxable_items_information_query);
		taxItem.setId(TaxDiskUtil.id_Tax_taxable_items_information_query);
		return taxItem.createTaxItemInfoQueryXml();
	}
	@Override
	public String findTaxIdByIdAndTaxNo(VmsTaxInfo taxInfo) {
		Map map =new HashMap();
		map.put("taxInfo", taxInfo);
		List list=find("findTaxIdByIdAndTaxNo", map);
		
		if(list.size()==1){
			return (String)list.get(0);
		}
		return null;
	}
	@Override
	public AjaxReturn saveAndUpdateTaxInfo(List list) {
		AjaxReturn message=null;
		try {
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					VmsTaxInfo	taxInfo=(VmsTaxInfo) list.get(i);
					Map map=new HashMap();
					map.put("taxInfo", taxInfo);
					if(findTaxIdByIdAndTaxNo(taxInfo).isEmpty()){
						save("saveTaxItemDisk", map);
					}else{
						update("updateTaxItemInfoById", map);
					}
					
				}
				}
			message=new AjaxReturn(true, Message.tax_Item_save_success);
		} catch (Exception e) {
			message=new AjaxReturn(false, Message.save_tax_Item_save_error);
			return message;
		}
		return message;

		}
	@Override
	public String CreateBuyBillInfoQuery(String diskNo,String fapiaoType) throws Exception {
		TaxDiskInfo disk=taxDiskInfoQueryService.findTaxDiskInfo(diskNo);
		BuyBillInfoQuery buyBill=new BuyBillInfoQuery(disk.getTaxpayerNo(), disk.getTaxDiskNo(),disk.getTaxDiskPsw(), disk.getTaxCertPsw(), fapiaoType);
		buyBill.setApplyTypeCode(TaxDiskUtil.Application_type_code_1);
		buyBill.setComment(TaxDiskUtil.comment_buy_ticket_information_query);
		buyBill.setId(TaxDiskUtil.id_buy_ticket_information_query);
		return buyBill.createBuyBillInfoQueryXml();
	}
	
}
