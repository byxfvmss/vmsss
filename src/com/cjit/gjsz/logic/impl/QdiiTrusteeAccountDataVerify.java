package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QdiiTrusteeAccountDataVerify extends AgencyDataVerify {
	
	public QdiiTrusteeAccountDataVerify(){
	}

	public QdiiTrusteeAccountDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_checkPositionNum=new String[]{"ACCOUNT_BALANCE","ACCOUNT_BALANCE_DOLLAR","BUY_EXCHANGE_AMOUNT",
			"BUY_EXCHANGE_AMOUNT_DOLLAR","ACC_BUY_EXCHANGE_AMOUNT","ACC_BUY_EXCHANGE_AMOUNT_DOLLAR","TRUSTEE_ACCOUNT_IN",
			"TRUSTEE_ACCOUNT_IN_DOL","ACC_TRUSTEE_ACCOUNT_IN","ACC_TRUSTEE_ACCOUNT_IN_DOL","ASKFOR_BUY_INCOME","ASKFOR_BUY_IN_DOL",
			"ACC_ASKFOR_BUY_IN","ACC_ASKFOR_BUY_IN_DOL","PROFIT_AMOUNT_INCOME","PROFIT_AMOUNT_INCOME_DOL","ACC_PROFIT_AMOUNT_INCOME",
			"ACC_PROFIT_AMOUNT_INCOME_DOL","OTHER_AMOUNT_INCOME","OTHER_AMOUNT_INCOME_DOL","ACC_OTHER_AMOUNT_INCOME","ACC_OTHER_AMOUNT_INCOME_DOL",
			"INCOME_TOTAL","INCOME_TOTAL_DOL","ACC_INCOME_TOTAL","ACC_INCOME_TOTAL_DOL","SELL_EXCHANGE_AMOUNT","SELL_EXCHANGE_AMOUNT_DOL",
			"ACC_SELL_EXCHANGE_AMOUNT","ACC_SELL_EXCHANGE_AMOUNT_DOL","TO_OVER_TRUSTEE_ACCOUNT","TO_OVER_TRUSTEE_ACCOUNT_DOL",
			"ACC_TO_OVER_TRUSTEE_ACCOUNT","ACC_TO_OVER_TRUSTEE_DOL","EXPEND_REDEMPTION","EXPEND_REDEMPTION_DOL","ACC_EXPEND_REDEMPTION",
			"ACC_EXPEND_REDEMPTION_DOL","SHARE_BONUS_AMOUNT","SHARE_BONUS_AMOUNT_DOL","ACC_SHARE_BONUS_AMOUNT","ACC_SHARE_BONUS_AMOUNT_DOL",
			"DEPOSIT_AMOUNT_EXPEND","DEPOSIT_AMOUNT_EXPEND_DOL","ACC_DEPOSIT_AMOUNT_EXPEND","ACC_DEPOSIT_AMOUNT_EXPEND_DOL","MANAGER_AMOUNT_EXPEND",
			"MANAGER_AMOUNT_EXPEND_DOL","ACC_MANAGER_AMOUNT_EXPEND","ACC_MANAGER_AMOUNT_EXPEND_DOL","HANDLING_CHARGE_EXPEND","HANDLING_CHARGE_EXPEND_DOL",
			"ACC_HANDLING_CHARGE_EXPEND","ACC_HANDLING_CHARGE_EXPEND_DOL","OTHER_AMOUNT_EXPEND","OTHER_AMOUNT_EXPEND_DOL","ACC_OTHER_AMOUNT_EXPEND",
			"ACC_OTHER_AMOUNT_EXPEND_DOL","EXPEND_TOTAL","EXPEND_TOTAL_DOL","ACC_EXPEND_TOTAL","ACC_EXPEND_TOTAL_DOL"};
	
	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
	
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Map data=(Map)verifylList.get(i);
				// >0
				map.putAll(checkPositiveNumber(data, t_checkPositionNum));
				//境内托管行代码金融机构标识码
				String strDistrictCode = (String)data.get("CUSTODIAN_BANK_ID");
				if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode.substring(0, 6))){
					map.put("CUSTODIAN_BANK_ID", "[境内托管行代码]应为金融机构标识码,前6位数字地区标识码有误 ");
				}
				//校验报告期时间格式
				if(!DateUtils.isValidDateStrict((String)data.get("REPORT_PERIOD"), "yyyyMM")){
					map.put("REPORT_PERIOD", "[报告期] 格式不正确，应为YYYYMM ");
				}
			}
		}
		return verifyModel;
	}
}
