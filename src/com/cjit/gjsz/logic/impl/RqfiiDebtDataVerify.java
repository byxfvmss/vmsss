package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.model.VerifyModel;

public class RqfiiDebtDataVerify extends AgencyDataVerify {
	
	public RqfiiDebtDataVerify(){
	}

	public RqfiiDebtDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}
	
	private final String[] t_checkPositionNum=new String[]{"SAVING_BALANCE","COMPANY_STOCK_AMOUNT","DEBT_AMOUNT","FUND_AMOUNT","FUND_SUPPORT_AMOUNT","SHARE_WARRANT_AMOUNT","OTHER_INVEST_AMOUNT","ADVANCE_STOCK","STOCK_AMOUNT_INCOME","ASKFOR_BUY_AMOUNT_INCOME","STOCK_PROFIT_AMOUNT_INCOME","PROFIT_AMOUNT_INCOME","SETTLE_RESERVE_AMOUNT","BUYING_BACK_ASSETS_AMOUNT","OTHER_AMOUNT_INCOME","ASSETS_TOTAL","MANAGER_AMOUNT_EXPEND","DEPOSIT_AMOUNT_EXPEND","TAX_AMOUNT_EXPEND","STOCK_AMOUNT_EXPEND","PROFIT_AMOUNT_EXPEND","SSETS_SOLD_REPURCHASE_AMOUNT","REDEMPTION_EXPEND","TRADE_EXPEND","OTHER_AMOUNT_EXPEND","DEBT_TOTAL"};
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
