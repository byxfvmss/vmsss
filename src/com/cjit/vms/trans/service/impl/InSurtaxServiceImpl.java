package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.InSurtaxListInfo;
import com.cjit.vms.trans.model.VmsSurtaxInfo;
import com.cjit.vms.trans.service.InSurtaxService;

public class InSurtaxServiceImpl  extends GenericServiceImpl implements InSurtaxService {

	public List findInSurtaxList(InSurtaxListInfo info,
			PaginationList paginationList) {
//		info.setTaxPerNumber(taxPerNumber);
//		info.setSurtaxType(surtaxType);
//		info.setSurtaxRate(surtaxRate);
//		info.setApplyPeriod(applyPeriod);
		Map param = new HashMap();
		param.put("taxpernumber", info.getTaxPerNumber());
		param.put("surtaxtype", info.getSurtaxType());
		param.put("surtaxrate", info.getSurtaxRate());
		param.put("taxpername", info.getTaxPerName());
		param.put("applyperiod", StringUtils.replace(info.getApplyPeriod(),"-",""));
		if(paginationList==null){
			return this.find("findInSurtaxListInfo", param);
		}
		return this.find("findInSurtaxListInfo", param, paginationList);
	}
	
	
	/**
	 * 附加税税种维护 新增/修改画面 保存/更新
	 * 
	 * @param updFlg 新增修改flag。                  0：新增，1：修改
	 * @param info
	 * 
	 * @author lee
	 */
	public void addOrUpdSurtaxTypeInfo(String updFlg, VmsSurtaxInfo info){
		Map map = new HashMap();
		
		// 纳税人识别号
		map.put("taxpayerId", info.getTaxpayerId());
		// 附加税类型
		map.put("surtaxType", info.getSurtaxType());
		// 附加税名称
		map.put("surtaxName", info.getSurtaxName());
		// 附加税税率
		map.put("surtaxRate", info.getSurtaxRate());
		// 附加税起始日期
		map.put("surtaxStrDt", info.getSurtaxStrDt());
		// 附加税终止日期
		map.put("surtaxEndDt", info.getSurtaxEndDt());
		
		if ("0".equals(updFlg)){
			update("surtaxTypeAddInfo", map);
		}else if ("1".equals(updFlg)){
			update("surtaxTypeUpdInfo", map);
		}
	}
	
	/**
	 * 附加税税种维护 列表画面初期化/检索用情报检索
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getListSurtaxTypeInfo(VmsSurtaxInfo info, PaginationList paginationList) {
		Map map = new HashMap();
		List instIds=info.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		// 纳税人识别号
		map.put("taxpayerId", info.getTaxpayerId());
		// 纳税人名称
		map.put("taxperName", info.getTaxperName());
		// 附加税类型
		map.put("surtaxType", info.getSurtaxType());
		// 附加税名称
		map.put("surtaxName", info.getSurtaxName());
		// 附加税税率
		map.put("surtaxRate", info.getSurtaxRate());
		// 附加税起始日期
		map.put("surtaxStrDt", info.getSurtaxStrDt());
		// 附加税终止日期
		map.put("surtaxEndDt", info.getSurtaxEndDt());
		//用户ID
		map.put("user_id", info.getUser_id());
		
		if (paginationList == null){
			return find("getListSurtaxTypeInfo", map);
		}
		return find("getListSurtaxTypeInfo", map, paginationList);
	}
	
	/**
	 * 附加税税率维护表情报检索
	 * 
	 * @param info
	 * @return
	 * @author lee
	 */
	public List getVmsSurtaxInfo(VmsSurtaxInfo info) {
		Map map = new HashMap();
		// 纳税人识别号
		map.put("taxpayerId", info.getTaxpayerId());
		// 附加税类型
		map.put("surtaxType", info.getSurtaxType());
		
		return find("getVmsSurtaxInfo", map);
	}
	
	/**
	 * 附加税税种维护 列表画面 删除
	 * 
	 * @param taxpayerId
	 * @param surtaxType
	 * @return
	 * @author lee
	 */
	public void delSurtaxTypeInfo(String taxpayerId, String surtaxType) {
		Map paraMap = new HashMap();
		// 纳税人识别号
		paraMap.put("taxpayerId", taxpayerId);
		// 附加税类型
		paraMap.put("surtaxType", surtaxType);
		
		this.delete("delSurtaxTypeInfo", paraMap);
	}

}
