package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QfiiRemitDataVerify extends AgencyDataVerify {
	
	public QfiiRemitDataVerify(){
	}

	public QfiiRemitDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_income=new String[]{"INCOME_CURR_CODE","INCOME_AMOUNT","INCOME_AMOUNT_DOLLARS"};
	private final String[] t_sell=new String[]{"SELL_EXCHANGE_CURR_CODE","SELL_EXCHANGE_AMOUNT","SELL_EXCHANGE_AMOUNT_DOLLARS","SELL_EXCHANGE_AMOUNT_RMB"};
	private final String[] t_buy=new String[]{"BUY_EXCHANGE_CURR_CODE","BUY_EXCHANGE_AMOUNT","BUY_EXCHANGE_AMOUNT_DOLLARS","BUY_EXCHANGE_AMOUNT_RMB"};
	private final String[] t_expend_capital=new String[]{"EXPEND_CAPITAL_CURR_CODE","EXPEND_CAPITAL_AMOUNT","EXPEND_CAPITAL_AMOUNT_DOLLARS"};
	private final String[] t_expend_profit=new String[]{"EXPEND_PROFIT_CURR_CODE","EXPEND_PROFIT_AMOUNT","EXPEND_PROFIT_DOLLARS"};
	private final String[] t_checkPositionNum=new String[]{"INCOME_AMOUNT","INCOME_AMOUNT_DOLLARS","","SELL_EXCHANGE_AMOUNT","SELL_EXCHANGE_AMOUNT_DOLLARS","SELL_EXCHANGE_AMOUNT_RMB","BUY_EXCHANGE_AMOUNT_RMB","","BUY_EXCHANGE_AMOUNT","BUY_EXCHANGE_AMOUNT_DOLLARS","","EXPEND_CAPITAL_AMOUNT","EXPEND_CAPITAL_AMOUNT_DOLLARS","","EXPEND_PROFIT_AMOUNT","EXPEND_PROFIT_DOLLARS"};
	
	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++)
			{
				Map data=(Map)verifylList.get(i);
				
				//托管人代码金融机构标识码
				String strDistrictCode = (String)data.get("CUSTODIAN_ID");
				if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode.substring(0, 6))){
					map.put("CUSTODIAN_ID", "[托管人代码]应为金融机构标识码,前6位数字地区标识码有误 ");
				}
				
				//汇入币种、汇入金额、汇入金额折美元为一组，同时为空或者不为空。汇入、结汇、购汇、汇出本金、汇出受益等五组内容中至少有一组不为空。
				if(!isAllEmpty(data,t_income) && !isAllNotEmpty(data, t_income))
				{
					putInfoTo(map,t_income, "汇入币种、汇入金额、汇入金额折美元为一组，同时为空或者不为空!");
				}
				if(!isAllEmpty(data,t_sell) && !isAllNotEmpty(data, t_sell))
				{
					putInfoTo(map,t_sell, "结汇币种、结汇金额、结汇金额折美元、结汇所得人民币金额为一组，同时为空或者不为空!");
				}
				if(!isAllEmpty(data,t_buy) && !isAllNotEmpty(data, t_buy))
				{
					putInfoTo(map,t_buy,"购汇币种、购汇金额、购汇金额折美元、购汇所得人民币金额为一组，同时为空或者不为空!");
				}
				if(!isAllEmpty(data,t_expend_capital) && !isAllNotEmpty(data, t_expend_capital))
				{
					putInfoTo(map,t_expend_capital, "汇出本金币种、汇出本金金额、汇出本金金额折美元为一组，同时为空或者不为空!");
				}
				if(!isAllEmpty(data,t_expend_profit) && !isAllNotEmpty(data, t_expend_profit))
				{
					putInfoTo(map,t_expend_profit, "汇出受益币种、汇出受益金额、汇出受益金额折美元为一组，同时为空或者不为空!");
				}
				if(isAllEmpty(data,t_income) && isAllEmpty(data,t_sell) 
						&& isAllEmpty(data,t_buy) && isAllEmpty(data,t_expend_capital) &&isAllEmpty(data,t_expend_profit))
				{
					map.put(t_income[1], "汇入、结汇、购汇、汇出本金、汇出受益等五组内容中至少有一组不为空!");
					map.put(t_buy[1], "汇入、结汇、购汇、汇出本金、汇出受益等五组内容中至少有一组不为空!");
					map.put(t_sell[1], "汇入、结汇、购汇、汇出本金、汇出受益等五组内容中至少有一组不为空!");
					map.put(t_expend_capital[1], "汇入、结汇、购汇、汇出本金、汇出受益等五组内容中至少有一组不为空!");
					map.put(t_expend_profit[1], "汇入、结汇、购汇、汇出本金、汇出受益等五组内容中至少有一组不为空!");
				}
				//校验大于0
				map.putAll(checkPositiveNumber(data, t_checkPositionNum));
				
				
			}
		}
		return verifyModel;
	}

	private void putInfoTo(Map map,String[] type, String info) {
		for(int i=0;i<type.length;i++){
			map.put(type[i], info);
		}
	}
}
