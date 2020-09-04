package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.model.VerifyModel;

public class BestirAccountClosedDataVerify extends AgencyDataVerify {
	
	public BestirAccountClosedDataVerify(){
	}

	public BestirAccountClosedDataVerify(List dictionarys, List verifylList,
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
				String strDistrictCode = (String)data.get("OPEN_BANK_ID");
				if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode.substring(0, 6))){
					map.put("OPEN_BANK_ID", "[开户银行代码]应为金融机构标识码,前6位数字地区标识码有误 ");
				}
				//11:结汇
				//12:境内划转
				//13:汇出境外
				String closed=(String)data.get("CLOSED_ASSETS_DEAL_TYPE");
				if(closed.equals("11"))
				{
					String t_bank=(String)data.get("SETTLEMENT_TO_ACCOUNT_BANK_ID");
					if(StringUtils.isEmpty(t_bank)){
						map.put("SETTLEMENT_TO_ACCOUNT_BANK_ID", "当关户资金处置类型为结汇时，该字段为必填项。");
					}
					else if(t_bank.length()!=12){
						map.put("SETTLEMENT_TO_ACCOUNT_BANK_ID", "[结汇所得人民币资金划往的账户开户行代码]应为12位金融机构标识码。");
					}
					else if(!verifyDictionaryValue(DISTRICTCO, t_bank.substring(0, 6))){
						map.put("SETTLEMENT_TO_ACCOUNT_BANK_ID", "[结汇所得人民币资金划往的账户开户行代码]应为金融机构标识码,前6位数字地区标识码有误 。");
					}
					if(StringUtils.isEmpty((String)data.get("SETTLEMENT_TO_ACCOUNT_NAME"))){
						map.put("SETTLEMENT_TO_ACCOUNT_NAME", "当关户资金处置类型为结汇时，该字段为必填项。");
					}
					if(StringUtils.isEmpty((String)data.get("SETTLEMENT_TO_ACCOUNT"))){
						map.put("SETTLEMENT_TO_ACCOUNT", "当关户资金处置类型为结汇时，该字段为必填项。");
					}
				}
				else if(closed.equals("12"))
				{
					String t_bank=(String)data.get("TO_ACCOUNT_BANK_ID");
					if(StringUtils.isEmpty(t_bank)){
						map.put("TO_ACCOUNT_BANK_ID", "当关户资金处置类型为境内划转时，该字段为必填项。");
					}
					else if(t_bank.length()!=12){
						map.put("TO_ACCOUNT_BANK_ID", "[划往境内外汇账户开户行代码]应为12位金融机构标识码。");
					}
					else if(!verifyDictionaryValue(DISTRICTCO, t_bank.substring(0, 6))){
						map.put("TO_ACCOUNT_BANK_ID", "[划往境内外汇账户开户行代码]应为金融机构标识码,前6位数字地区标识码有误 。");
					}
					if(StringUtils.isEmpty((String)data.get("TO_ACCOUNT_NAME"))){
						map.put("TO_ACCOUNT_NAME", "当关户资金处置类型为境内划转时，该字段为必填项。");
					}
					if(StringUtils.isEmpty((String)data.get("TO_ACCOUNT"))){
						map.put("TO_ACCOUNT", "当关户资金处置类型为境内划转时，该字段为必填项。");
					}
				}
				else if(closed.equals("13"))
				{
					String t_bank=(String)data.get("EXPEND_OVER_ACCOUNT_BANK_ID");
					if(StringUtils.isEmpty(t_bank)){
						map.put("EXPEND_OVER_ACCOUNT_BANK_ID", "当关户资金处置类型为境内划转时，该字段为必填项。");
					}
					else if(t_bank.length()!=11 && t_bank.length()!=8){
						map.put("EXPEND_OVER_ACCOUNT_BANK_ID", "[划往境内外汇账户开户行代码]应为该银行8位或11位的SWIFT CODE");
					}
					if(StringUtils.isEmpty((String)data.get("EXPEND_OVER_ACCOUNT_NAME"))){
						map.put("EXPEND_OVER_ACCOUNT_NAME", "当关户资金处置类型为境内划转时，该字段为必填项。");
					}
					if(StringUtils.isEmpty((String)data.get("EXPEND_OVER_ACCOUNT"))){
						map.put("EXPEND_OVER_ACCOUNT", "当关户资金处置类型为境内划转时，该字段为必填项。");
					}
				}
			}
		}
		return verifyModel;
	}
}
