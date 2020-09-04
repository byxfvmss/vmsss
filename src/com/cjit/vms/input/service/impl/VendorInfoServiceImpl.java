package com.cjit.vms.input.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.input.model.VendorInfo;
import com.cjit.vms.input.service.VendorInfoService;

public class VendorInfoServiceImpl extends GenericServiceImpl implements
		VendorInfoService {

	public void deleteVendorById(List vendorIds) {
		Map map = new HashMap();
		
			map.put("vendorIds", vendorIds);
		
		
		delete("deleteVendorById", map);
	}

	public VendorInfo findVendorById(String vendorId) {
		Map map = new HashMap();
		VendorInfo vendorInfo = new VendorInfo();
		vendorInfo.setVendorId(vendorId);
		map.put("vendorInfo", vendorInfo);
		List list = find("findVendorInfo", map);
		if (list != null && list.size() == 1) {
			return (VendorInfo) list.get(0);
		} else {
			return null;
		}
	}

	public List findVendorByTaxNo(String taxNo) {
		Map map = new HashMap();
		map.put("taxNo", taxNo);
		return find("findVendorInfo", map);
	}

	public List findVendorInfoList(VendorInfo vendorInfo, PaginationList paginationList) {
		Map map = new HashMap();
		map.put("vendorInfo", vendorInfo);
		return find("findVendorInfo", map, paginationList);
	}

	public void saveNewVendor(VendorInfo vendorInfo) {
		Map map = new HashMap();
		map.put("vendorInfo", vendorInfo);
		save("insertVendorInfo", map);
		
	}

	public void updateVendorById(VendorInfo vendorInfo) {
		Map map = new HashMap();
		map.put("vendorInfo", vendorInfo);
		update("updateVendorInfo", map);
	}

	public List findVendorTempInfoList(VendorInfo vendorInfo,
			PaginationList paginationList) {
			Map map =new HashMap();
			map.put("vendorInfo", vendorInfo);
		return find("findVendorTempInfo", map,paginationList);
	}

	public List findvendorTaxnoTempByTaxNo(String taxNo) {
		Map map=new HashMap();
		map.put("taxNo", taxNo);
		return find("findvendorTaxnoTempByTaxNo", map);
	}

	public List findTransbyVendId(String vendorId) {
		Map map =new HashMap();
		map.put("vendorId", vendorId);
		return find("findTransbyVendId", map);
	}

	public void copyVendorInfoToVendorInfoTemp(VendorInfo vendorInfo) {
		Map map=new HashMap();
		map.put("vendorInfo", vendorInfo);
		save("copyVendorInfotoVendorInfoTemp", map);
	}

	public List findVendorInfoTempByTaxNo(String taxNo) {
		Map map=new HashMap();
		map.put("taxNo", taxNo);
		return find("findVendorInfoTempByTaxNo",map);
	}

	public void copyVendorTempYoVendor(List list) {
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				VendorInfo vendorInfo=new VendorInfo();
				vendorInfo.setVendorId(list.get(i).toString());
				Map map=new HashMap();
				map.put("vendorInfo", vendorInfo);
				save("copyVendorTempYoVendor", map);
			}
		}
	}

	public void updatevendorAfterAudit(List list) {
		
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				VendorInfo vendorInfo=new VendorInfo();
				vendorInfo.setVendorId(list.get(i).toString());
				Map map=new HashMap();
				map.put("vendorInfo", vendorInfo);
				update("updatevendorAfterAudit", map);
		}
		}
	}

	public void deleteVendorTemp(List list) {
		Map map=new HashMap();
		map.put("vendorIds", list);
		delete("deleteVendorTemp", map);
	}

	public void updateVendorTempAfterAudit(List list,String dataAuditStatus) {
		Map map =new HashMap();
		map.put("vendorIds", list);
		map.put("dataAuditStatus", dataAuditStatus);
		update("updateVendorTempAfterAudit", map);
	}

	public VendorInfo findvendorTempByVendorId(String vendorId) {
		Map map=new HashMap();
		map.put("vendorId", vendorId);
		List list=find("findvendorTempByVendorId", map);
		VendorInfo vendorInfo=new VendorInfo();
		if(list.size()>0){
			vendorInfo=(VendorInfo)list.get(0);
		}
		return vendorInfo;
	}

	@Override
	public void deleteVendorByTaxNo(String taxNo) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("taxNo", taxNo);
		delete("deleteVendorByTaxNo", map);
	}

	@Override
	public void saveVendorData(Map<String, String> map) {
		this.save("saveVendorData", map);
	}

	@Override
	public void saveVendorInfo(Map<String, String> map) {
		this.save("saveVendorInfo", map);
		
	}

}
