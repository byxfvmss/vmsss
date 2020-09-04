package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QdiiRemitDataVerify extends AgencyDataVerify {
	
	public QdiiRemitDataVerify(){
	}

	public QdiiRemitDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_income=new String[]{"INCOME_CURR_CODE","INCOME_AMOUNT","INCOME_AMOUNT_DOLLARS"};
	private final String[] t_sell=new String[]{"SELL_EXCHANGE_AMOUNT_DOLLARS","SELL_EXCHANGE_AMOUNT_RMB"};
	private final String[] t_buy=new String[]{"BUY_EXCHANGE_AMOUNT_RMB","BUY_EXCHANGE_AMOUNT_DOLLARS"};
	private final String[] t_expend=new String[]{"EXPEND_CURR_CODE","EXPEND_AMOUNT","EXPEND_AMOUNT_DOLLARS"};
	private final String[] t_checkPositionNum=new String[]{"BUY_EXCHANGE_AMOUNT_RMB","BUY_EXCHANGE_AMOUNT_DOLLARS","EXPEND_AMOUNT","EXPEND_AMOUNT_DOLLARS","INCOME_AMOUNT","INCOME_AMOUNT_DOLLARS","SELL_EXCHANGE_AMOUNT_DOLLARS","SELL_EXCHANGE_AMOUNT_RMB"};
	
	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++)
			{
				Map data=(Map)verifylList.get(i);
				//境内托管行代码金融机构标识码
				String strDistrictCode = (String)data.get("CUSTODIAN_BANK_ID");
				if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode.substring(0, 6))){
					map.put("CUSTODIAN_BANK_ID", "[境内托管行代码]应为金融机构标识码,前6位数字地区标识码有误 ");
				}
				
				if(!isAllEmpty(data,t_income) && !isAllNotEmpty(data, t_income))
				{
					map.put(t_income[0], "汇入币种、汇入金额、汇入金额折美元为一组，同时为空或同时不为空");
					map.put(t_income[1], "汇入币种、汇入金额、汇入金额折美元为一组，同时为空或同时不为空!");
					map.put(t_income[2], "汇入币种、汇入金额、汇入金额折美元为一组，同时为空或同时不为空!");
				}
				if(!isAllEmpty(data,t_sell) && !isAllNotEmpty(data, t_sell))
				{
					map.put(t_sell[0], "结汇金额折美元、结汇所得人民币金额为一组，同时为空或同时不为空!");
					map.put(t_sell[1], "结汇金额折美元、结汇所得人民币金额为一组，同时为空或同时不为空!");
				}
				if(!isAllEmpty(data,t_buy) && !isAllNotEmpty(data, t_buy))
				{
					map.put(t_buy[0], "购汇人民币金额、购汇金额折美元为一组，同时为空或同时不为空!");
					map.put(t_buy[1], "购汇人民币金额、购汇金额折美元为一组，同时为空或同时不为空!");
				}
				if(!isAllEmpty(data,t_expend) && !isAllNotEmpty(data, t_expend))
				{
					map.put(t_expend[0], "汇出币种、汇出金额、汇出金额折美元为一组，同时为空或同时不为空!");
					map.put(t_expend[1], "汇出币种、汇出金额、汇出金额折美元为一组，同时为空或同时不为空!");
					map.put(t_expend[2], "汇出币种、汇出金额、汇出金额折美元为一组，同时为空或同时不为空!");
				}
				if(isAllEmpty(data,t_income) && isAllEmpty(data,t_sell) 
						&& isAllEmpty(data,t_buy) && isAllEmpty(data,t_expend))
				{
					map.put(t_income[0], "购汇、汇出、汇入、结汇至少填一组!");
					map.put(t_buy[0], "购汇、汇出、汇入、结汇至少填一组!");
					map.put(t_sell[0], "购汇、汇出、汇入、结汇至少填一组!");
					map.put(t_expend[0], "购汇、汇出、汇入、结汇至少填一组!");
				}
				
				//校验大于0
				map.putAll(checkPositiveNumber(data, t_checkPositionNum));
				
				
			}
		}
		return verifyModel;
	}
}
