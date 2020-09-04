package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.service.TaxDiskPasswdService;

public class TaxDiskPasswdServiceImpl extends GenericServiceImpl  implements TaxDiskPasswdService {

	public List findTaxDiskPasswdInfoList(TaxDiskInfo info,
			PaginationList paginationList) {
		Map params = new HashMap();
		params.put("taxDiskNo", info.getTaxDiskNo());
		params.put("taxpayerNo", info.getTaxpayerNo());
		params.put("taxperName", info.getTaxpayerNam());
		params.put("user_id", info.getUser_id());
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		params.put("auth_inst_ids", lstTmp); 
		return this.find("findTaxDiskInfoList", params, paginationList); 
	}

	public void deleteTaxDiskPasswd(String[] checked_tax_disk_no) {
		if(null!=checked_tax_disk_no){
			for(int i=0;i<checked_tax_disk_no.length;i++){
				String ids=checked_tax_disk_no[i];
				String[] arr=StringUtils.split(ids, ",");
				Map params = new HashMap();
				params.put("taxDiskNo", arr[0]);
				params.put("taxpayerNo", arr[1]);
				this.delete("deleteTaxDiskInfo", params);
			}
		}
	}
	
	
	public void saveTaxDiskInfo(TaxDiskInfo info) {
		Map diskMap = new HashMap();
		diskMap.put("info", info); 
		this.save("saveTaxDiskInfo", diskMap);
	}
	
	public void updateTaxDiskInfo(TaxDiskInfo info) {
		Map diskMap = new HashMap();
		diskMap.put("info", info); 
		this.update("updateTaxDiskInfoForPasswd", diskMap);
	}

	
	public TaxDiskInfo findTaxDiskInfoDetail(String taxDiskNo,String taxpayerNo) {
		Map map = new HashMap();
		map.put("taxDiskNo", taxDiskNo);
		map.put("taxpayerNo", taxpayerNo);
		return (TaxDiskInfo) this.findForObject("findTaxDiskInfoDetail", map);
	}
	
	public Long CountTaxDiskInfo(String taxDiskNo, String taxpayerNo) {
		Map map = new HashMap();
		map.put("taxDiskNo", taxDiskNo);
		map.put("taxpayerNo", taxpayerNo);
		return this.getRowCount("countTaxDiskNo", map);
	}
	
	public List getInstInfoTaxNoList(InstInfo info,PaginationList paginationList){
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
		
		return this.find("getInstInfoTaxNoList", map);
	}

}
