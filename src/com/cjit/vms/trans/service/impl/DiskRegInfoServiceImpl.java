package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.DiskRegInfo;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.service.DiskRegInfoService;
import com.cjit.vms.trans.util.SqlUtil;

public class DiskRegInfoServiceImpl  extends GenericServiceImpl  implements DiskRegInfoService {

	public List getTaxDiskInfoList(DiskRegInfo info,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("instId", info.getInstId());
		map.put("taxpayerNo", info.getTaxpayerNo());
		map.put("auth_inst_ids", info.getLstAuthInstId());
		return this.find("findDiskRegInfoList", map,paginationList);
	}

	public List getTaxDiskInfoList(DiskRegInfo info) {
		Map map = new HashMap();
		map.put("instId", info.getInstId());
		map.put("taxpayerNo", info.getTaxpayerNo());
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", SqlUtil.list2String(lstTmp, ",")); 
		return this.find("findDiskRegInfoList",map);
	}
	
	public int saveDiskRegInfo(String operType, DiskRegInfo info) {
		int result_flag = 0;
		Map diskMap = new HashMap();
		diskMap.put("info", info); 
		if(operType.equals("edit")){
			Long count = this.CountDiskRegInfo(info.getTaxDiskNo(), info.getTaxpayerNo());
			if(Integer.parseInt(count+"") > 0){
				this.update("updateDiskRegInfo", diskMap);
			} else {
				int add_result=this.save("saveDiskRegInfo", diskMap);
				if(add_result<1){
					return -1;
				}
			}
		}else{
			int add_result=this.save("saveDiskRegInfo", diskMap);
			if(add_result<1){
				return -1;
			}
		}
		
		return result_flag;
	}

	public Long CountDiskRegInfo(String taxDiskNo, String taxpayerNo) {
		Map map = new HashMap();
		map.put("taxDiskNo", taxDiskNo);
		map.put("taxpayerNo", taxpayerNo);
		return this.getRowCount("countDiskRegInfo", map);
	}

	public void deleteDiskRegInfo(String[] selectTaxDisks) {
		if(null!=selectTaxDisks){
			for(int i=0;i<selectTaxDisks.length;i++){
				String ids=selectTaxDisks[i];
				String[] arr=StringUtils.split(ids, ",");
				Map params = new HashMap();
				params.put("taxDiskNo", arr[0]);
				params.put("taxpayerNo", arr[1]);
				this.delete("deleteDiskRegInfo", params);
			}
		}
	}

	public List getDiskRegInfoDetail(String taxDiskNo, String taxpayerNo) {
		Map map = new HashMap();
		map.put("taxDiskNo", taxDiskNo);
		map.put("taxpayerNo", taxpayerNo);
		return this.find("findDiskRegInfoDetail", map);
	}

	public List getInstInfoList(InstInfo info,PaginationList paginationList){
		
		Map map = new HashMap();
		List instIds=info.getLstAuthInstIds();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("inasId", info.getInstId());
		map.put("user_id", info.getUserId());
		map.put("instName", info.getInstName());
		map.put("taxNo", info.getTanNo());
		
		return this.find("getInstInfoList", map);
	}
	
	public List getInstInfoTaxNoList(InstInfo info,PaginationList paginationList){
		Map map = new HashMap();
		List instIds=info.getLstAuthInstIds();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("instId", info.getInstId());
		map.put("user_id", info.getUserId());
		
		return this.find("getInstInfoTaxNoList", map);
	}
	
}
