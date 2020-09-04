package com.cjit.vms.metlife.service.Impl;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:单证管理 metlife
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.metlife.service.DocumentManageService;
import com.cjit.vms.metlife.model.DocumentManageInfo;
import com.cjit.vms.trans.model.InstInfo;



public class DocumentManageServiceImpl extends GenericServiceImpl implements DocumentManageService{
	public List findDocumentManagelist(DocumentManageInfo documentManageInfo, PaginationList paginationList){
		Map map=new HashMap();
		map.put("documentManageInfo", documentManageInfo);
		return find("createKeyCodeList", map, paginationList);
	}
	
	public List getInstInfoList(DocumentManageInfo documentManageInfo,PaginationList paginationList){
		
		Map map = new HashMap();
		List instIds=documentManageInfo.getLstAuthInstIds();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("inasId", documentManageInfo.getInstId());
		map.put("user_id", documentManageInfo.getUserId());
		map.put("instName", documentManageInfo.getInstName());
		map.put("taxNo", documentManageInfo.getTanNo());
		
		return this.find("getInstInfoList", map);
	}

	public List findByRuleId(String ruleId){
	Map map = new HashMap();
	map.put("ruleId",ruleId);
	return this.find("findByRuleId", map);
	}

	public void updateKeyCode(DocumentManageInfo documentManageInfo) {
		Map map=new HashMap();
		map.put("documentManageInfo", documentManageInfo);
		this.update("updateKeyCode", map);
	}

public List checkCode(DocumentManageInfo documentManageInfo) {
	Map map=new HashMap();
	map.put("documentManageInfo", documentManageInfo);
	
	return this.find("checkCode", map);
}

public int saveCode(DocumentManageInfo documentManageInfo) {
	Map map=new HashMap();
	map.put("documentManageInfo", documentManageInfo);
	 return this.save("saveKeyCode", map);
}

public void deleteKeyCode(String ruleid) {
	Map param = new HashMap();
	if(ruleid.contains(",")){
		param.put("ruleid", ruleid.split(","));
		this.delete("deleteKeyCodes", param);
	}else{
		param.put("ruleid", ruleid);
		this.delete("deleteKeyCode", param);
	}
	
}

public void saveCodeNum(DocumentManageInfo documentManageInfo,
		String[] curnumarray) {
	try{
	Map map=new HashMap();
	map.put("documentManageInfo", documentManageInfo);
	map.put("curnumarray", curnumarray);
	this.save("saveInsureCode1", map);
//	for(int i=0;i<curnumarray.length;i++){
//		documentManageInfo.setCurNum(curnumarray[i]);
//		map.put("documentManageInfo", documentManageInfo);
//		this.save("saveInsureCode", map);
//	}	
	}catch(Exception e){
		e.fillInStackTrace();
	
	}
}

public void updatecurNum(DocumentManageInfo documentManageInfo) {
	Map map=new HashMap();
	map.put("documentManageInfo",documentManageInfo);
	this.update("updatecurNum", map);
}
@Override
public List findManageInsureCodeList(DocumentManageInfo documentManageInfo,
		PaginationList paginationList) {
	Map map=new HashMap();
	map.put("documentManageInfo", documentManageInfo);
	return find("findManageInsureCodeList", map, paginationList);

}

public void updateKeyCode(String numId,String sta,String name,String vdName) {
	Map param = new HashMap();
	param.put("sta", sta);
	param.put("name", name);
	param.put("vdName", vdName);
	if(numId.contains(",")){
		param.put("numId", numId.split(","));
		this.update("updateStatusList", param);
	}else{
		param.put("numId", numId);
		this.update("updateStatu", param);
	}
	
}
@Override
public List finDocRecInfo(DocumentManageInfo documentManageInfo,PaginationList paginationList) {
	Map map=new HashMap();
	map.put("documentManageInfo", documentManageInfo);
	return find("finDocRecInfo", map, paginationList);
}

public void insertTransTo(String numId, String orderNum) {
	Map map=new HashMap();
	map.put("numId", numId);
	map.put("orderNum", orderNum);
	this.save("insertTransTo", map);
}
@Override
public void updateTransTo(String sta, String numId, String orderNum,
		String vdName){
	Map map=new HashMap();
	map.put("numId", numId);
	map.put("orderNum", orderNum);
	map.put("sta", sta);
	map.put("vdName", vdName);
	this.update("updateTranTo", map);
}
@Override
public List findBaseuser() {
	Map map=new HashMap();
	return find("findBaseUser", map);
}
@Override
public void distributeTo(DocumentManageInfo documentManageInfo) {
	Map map=new HashMap();
	map.put("documentManageInfo", documentManageInfo);
	String numId=documentManageInfo.getNum();
	if(numId.contains(",")){
		map.put("numId", numId.split(","));
		this.update("distributeTolist", map);
	}else{
		map.put("numId", numId);
		this.update("distributeTo", map);
	}
}
@Override
public void updateCancel(String sta, String orderNum, String numId) {
	Map map=new HashMap();
	map.put("sta", sta);
	map.put("orderNum", orderNum);
	map.put("numId", numId);
	this.update("updatecancal", map);
}
@Override
public List findDocMapInfo(DocumentManageInfo documentManageInfo,PaginationList paginationList) {
	Map map=new HashMap();
	map.put("documentManageInfo", documentManageInfo);
	return find("findDocMapInfo",map,paginationList);
}
@Override
public void saveCodeNum1(List documentManageInfoList) {
	this.insertBatch("saveInsureCode2", documentManageInfoList);
}




}
