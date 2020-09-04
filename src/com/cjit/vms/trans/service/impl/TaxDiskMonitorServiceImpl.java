package com.cjit.vms.trans.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.TaxDiskMonitorInfo;
import com.cjit.vms.trans.service.TaxDiskMonitorService;

public class TaxDiskMonitorServiceImpl extends GenericServiceImpl implements TaxDiskMonitorService {

	public List findTaxDiskMonitorListInfo(TaxDiskMonitorInfo info,
			PaginationList paginationList) {
		Map params = new HashMap();
		params.put("inst_id", info.getInstId());
		params.put("taxpayer_no", info.getTaxPerNumber());
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		params.put("auth_inst_ids", lstTmp);
		return this.find("findTaxDiskMonitorListInfo", params, paginationList); 
	}

	public void saveTaxDiskMonitor(TaxDiskMonitorInfo info) {
		if(null != info.getBillLimitAmtNS()){
			info.setBillLimitAmtNS(BigDecimal.valueOf(Double.parseDouble("-"+info.getBillLimitAmtNS())));
		}
		if(null != info.getnBilDayS()){
			info.setnBilDayS("-"+info.getnBilDayS());
		}
		if(null != info.getOffLineAmtNS()){
			info.setOffLineAmtNS(BigDecimal.valueOf(Double.parseDouble("-"+info.getOffLineAmtNS())));
		}
		Map params = new HashMap();
		params.put("info", info);
		this.save("saveVmsDiskMonInfo", params);
	}

	public void updateTaxDiskMonitor(TaxDiskMonitorInfo info) {
		if(null != info.getBillLimitAmtNS()){
			info.setBillLimitAmtNS(BigDecimal.valueOf(Double.parseDouble("-"+info.getBillLimitAmtNS())));
		}
		if(null != info.getnBilDayS()){
			info.setnBilDayS("-"+info.getnBilDayS());
		}
		if(null != info.getOffLineAmtNS()){
			info.setOffLineAmtNS(BigDecimal.valueOf(Double.parseDouble("-"+info.getOffLineAmtNS())));
		}
		Map params = new HashMap();
		params.put("info", info);
		this.update("updateVmsDiskMonInfo", params);
	}

	public void deleteTaxDiskMonitor(String[] tax_disk_nos) {
		if(null!=tax_disk_nos){
			for(int i=0;i<tax_disk_nos.length;i++){
				String ids=tax_disk_nos[i];
				String[] arr=StringUtils.split(ids, "_");
				Map params = new HashMap();
				params.put("tax_disk_no", arr[0]);
				params.put("fapiao_type", arr[1]);
				this.delete("deleteVmsDiskMonInfo", params);
			}
		}
	}

	public TaxDiskMonitorInfo findTaxDiskMonitorItemInfo(String taxDiskNo,
			String fapiaoType) {
		Map params = new HashMap();
		params.put("tax_disk_no", taxDiskNo);
		params.put("fapiao_type", fapiaoType);
		TaxDiskMonitorInfo taxDiskMonitorInfo = (TaxDiskMonitorInfo) this.findForObject("findTaxDiskMonitorItemInfo", params);
		
		if(null != taxDiskMonitorInfo){
			if(null != taxDiskMonitorInfo.getBillLimitAmtNS()){
				taxDiskMonitorInfo.setBillLimitAmtNS(taxDiskMonitorInfo.getBillLimitAmtNS().abs());
			}
			if(null != taxDiskMonitorInfo.getnBilDayS() && !"".equals(taxDiskMonitorInfo.getnBilDayS())){
				taxDiskMonitorInfo.setnBilDayS(String.valueOf(java.lang.Math.abs(Integer.parseInt(taxDiskMonitorInfo.getnBilDayS()))));
			}
			if(null != taxDiskMonitorInfo.getOffLineAmtNS()){
				taxDiskMonitorInfo.setOffLineAmtNS(taxDiskMonitorInfo.getOffLineAmtNS().abs());
			}
		}
		return taxDiskMonitorInfo;
	}
	
	public BigDecimal getMinBillLimitAmtFromTaxDisk(){
		BigDecimal minAmt = null;
		Object obj = this.findForObject("getMinBillLimitAmtFromTaxDisk", null);
		if(obj != null){
			minAmt = new BigDecimal(obj.toString());
		}
		return minAmt;
	}
	
	public List getInstInfoList(InstInfo info,PaginationList paginationList){
			
			Map map = new HashMap();
			map.put("inasId", info.getInstId());
			map.put("user_id", info.getUserId());
			map.put("instName", info.getInstName());
			map.put("taxNo", info.getTanNo());
			List instIds=info.getLstAuthInstIds();
			List lstTmp=new ArrayList();
			for(int i=0;i<instIds.size();i++){
				Organization org=(Organization)instIds.get(i);
				lstTmp.add(org.getId());
			}
			map.put("auth_inst_ids", lstTmp);
			return this.find("getInstInfoList", map);
		}
	
	public List getInstInfoTaxNoList(InstInfo info,PaginationList paginationList){
		Map map = new HashMap();
		map.put("instId", info.getInstId());
		map.put("user_id", info.getUserId());
		List instIds=info.getLstAuthInstIds();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		return this.find("getInstInfoTaxNoList", map);
	}
}
