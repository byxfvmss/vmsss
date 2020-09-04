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
import com.cjit.vms.trans.service.TaxDiskInfoService;
import com.cjit.vms.trans.util.SqlUtil;

public class TaxDiskInfoServiceImpl  extends GenericServiceImpl  implements TaxDiskInfoService {

	public List getTaxDiskInfoList(TaxDiskInfo info,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("instId", info.getInstId());
		map.put("taxpayerNo", info.getTaxpayerNo());
		map.put("user_id", info.getUser_id());
		return this.find("findTaxDiskInfoList", map,paginationList);
	}

	/**
	 * 取得税控盘基本信息（导出用）
	 * 
	 */
	public List getTaxDiskInfoList(TaxDiskInfo info){
	Map map = new HashMap();
	map.put("instId", info.getInstId());
	map.put("taxpayerNo", info.getTaxpayerNo());
	map.put("user_id", info.getUser_id());
	List instIds=info.getLstAuthInstId();
	List lstTmp=new ArrayList();
	for(int i=0;i<instIds.size();i++){
		Organization org=(Organization)instIds.get(i);
		lstTmp.add(org.getId());
	}
	map.put("auth_inst_ids", lstTmp); 
	return this.find("findTaxDiskInfoList", map);
	}

	
	
	public TaxDiskInfo getTaxDiskInfoDetail(String taxDiskNo,String taxpayerNo) {
		Map map = new HashMap();
		map.put("taxDiskNo", taxDiskNo);
		map.put("taxpayerNo", taxpayerNo);
		return (TaxDiskInfo) this.findForObject("findTaxDiskInfoDetail", map);
	}

	public int saveTaxDiskInfo(String operType, TaxDiskInfo info) {
		int result_flag = 0;
		Map diskMap = new HashMap();
		diskMap.put("info", info); 
		if(operType.equals("edit")){
			this.update("updateTaxDiskInfo", diskMap);
		}else{
			int add_result=this.save("saveTaxDiskInfo", diskMap);
			if(add_result<1){
				return -1;
			}
		}
		
		return result_flag;
	}

	public Long CountTaxDiskInfo(String taxDiskNo, String taxpayerNo) {
		Map map = new HashMap();
		map.put("taxDiskNo", taxDiskNo);
		map.put("taxpayerNo", taxpayerNo);
		return this.getRowCount("countTaxDiskNo", map);
	}

	public void deleteTaxDiskInfo(String[] selectTaxDisks) {
		if(null!=selectTaxDisks){
			for(int i=0;i<selectTaxDisks.length;i++){
				String ids=selectTaxDisks[i];
				String[] arr=StringUtils.split(ids, ",");
				Map params = new HashMap();
				params.put("taxDiskNo", arr[0]);
				params.put("taxpayerNo", arr[1]);
				this.delete("deleteTaxDiskInfo", params);
			}
		}
	}

	
	public List getInstInfoList(InstInfo info){
		
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
		map.put("taxFlag", info.getTaxFlag());
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
