package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.logic.model.VerifyModel;

public class BestirChangesDataVerify extends AgencyDataVerify {
	
	public BestirChangesDataVerify(){
	}

	public BestirChangesDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_checkPositionNum=new String[]{"MONTH_BEGIN_BALANCE","INCOME_AMOUNT","BUY_INCOME_AMOUNT","PERSON_SAVING_ACCOUNT_INCOME","OVER_INVEST_PROFIT_BACK","EXPEND_AMOUNT","EXPEND_OVERSEAS_AMOUNT","SETTLEMENT_AMOUNT","TO_PERSON_ACCOUNT_AMOUNT","MONTH_END_BALANCE","OTHER_CLEAR_CHANGES_AMOUNT"};
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
				String strDistrictCode = (String)data.get("OPEN_BANK_ID");
				if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode.substring(0, 6))){
					map.put("OPEN_BANK_ID", "[开户银行代码]应为金融机构标识码,前6位数字地区标识码有误 ");
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
