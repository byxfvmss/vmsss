package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;
import cjit.crms.util.date.DateUtil;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QfiiAccountDataVerify extends AgencyDataVerify {

	public QfiiAccountDataVerify(){
	}

	public QfiiAccountDataVerify(List dictionarys, List verifylList,
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
				
				//托管人代码金融机构标识码
				String strDistrictCode = (String)data.get("CUSTODIAN_ID");
				if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode.substring(0, 6))){
					map.put("CUSTODIAN_ID", "[托管人代码]应为金融机构标识码,前6位数字地区标识码有误 ");
				}
				
				//必填项，格式YYYYMMDD，，开户日期和关户日期至少填写一个。
				String strOpenDate=(String)data.get("OPEN_DATE");
				String strCloseDate=(String)data.get("CLOSE_DATA");
				if(StringUtil.IsEmptyStr(strOpenDate) && StringUtil.IsEmptyStr(strCloseDate))
				{
					map.put("OPEN_DATE", "[开户日期]和[关户日期]至少填写一个");
					map.put("CLOSE_DATA", "[开户日期]和[关户日期]至少填写一个");
				}
				else
				{
					//大于等于当前日期
					if(!StringUtil.IsEmptyStr(strOpenDate) && DateUtil.before(strOpenDate, DateUtil.getNowCrmsDateStr()))
					{
						map.put("OPEN_DATE", "[开始时间]应大于等于当前日期");
					}
					
					//非必填项，格式YYYYMMDD，大于等于开户日期。
					if(!StringUtil.IsEmptyStr(strOpenDate) && !StringUtil.IsEmptyStr(strCloseDate) && DateUtil.before(strCloseDate , strOpenDate ))
					{
						map.put("CLOSE_DATA", "[关户时间]应大于等于开户日期");
					}
				}
			}
		}
		return verifyModel;
	}

}
