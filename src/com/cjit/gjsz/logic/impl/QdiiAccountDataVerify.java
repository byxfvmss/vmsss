package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QdiiAccountDataVerify extends AgencyDataVerify {
	
	public QdiiAccountDataVerify(){
	}

	public QdiiAccountDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
	
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Map data=(Map)verifylList.get(i);
				//境内托管行代码金融机构标识码
				String strDistrictCode = (String)data.get("CUSTODIAN_BANK_ID");
				if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode.substring(0, 6))){
					map.put("CUSTODIAN_BANK_ID", "[境内托管行代码]应为金融机构标识码,前6位数字地区标识码有误 ");
				}
				//境外托管人中文名称和英文名称至少填写一个。
				String v_name=(String)data.get("TRUSTEE_NAME");
				String v_ename=(String)data.get("TRUSTEE_ENNAME");
				if(StringUtil.IsEmptyStr(v_name) && StringUtil.IsEmptyStr(v_ename))
				{
					map.put("TRUSTEE_NAME", "[境外托管人中文名称]和[境外托管人英文名称]至少填写一个。");
					map.put("TRUSTEE_ENNAME", "[境外托管人中文名称]和[境外托管人英文名称]至少填写一个。");
				}
				
				//非必填项，有SWIFT码的必填，有特殊机构赋码必填，其他的不填。
			}
		}
		return verifyModel;
	}
}
