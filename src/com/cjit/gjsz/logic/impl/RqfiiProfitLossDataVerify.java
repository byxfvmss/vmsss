package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.model.VerifyModel;

public class RqfiiProfitLossDataVerify extends AgencyDataVerify {
	
	public RqfiiProfitLossDataVerify(){
	}

	public RqfiiProfitLossDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_checkPositionNum=new String[]{"STOCK_PROFIT_AMOUNT_INCOME","PROFIT_AMOUNT_INCOME","ACCEPT_VALUE_PROFIT_LOSS","INVEST_PROFIT_LOSS_AMOUNT","OTHER_AMOUNT_INCOME","INCOME_TOTAL","DEPOSIT_AMOUNT","MANAGER_AMOUNT","TAX_AMOUNT","PROFIT_EXPEND_AMOUNT","OTHER_AMOUNT","EXPEND_TOTAL"};
	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
	
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Map data=(Map)verifylList.get(i);
				// >0
				map.putAll(checkPositiveNumber(data, t_checkPositionNum));
				//托管人代码金融机构标识码
				String strDistrictCode = (String)data.get("CUSTODIAN_ID");
				if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode.substring(0, 6))){
					map.put("CUSTODIAN_ID", "[托管人代码]应为金融机构标识码,前6位数字地区标识码有误 ");
				}
			}
		}
		return verifyModel;
	}
}
