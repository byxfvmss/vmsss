package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QfiiChangesDataVerify extends AgencyDataVerify {
	
	public QfiiChangesDataVerify(){
	}

	public QfiiChangesDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}
	private final String[] t_checkPositionNum=new String[]{"CAPITAL_INCOME","CAPITAL_INCOME_DOLLARS","ACCUMULATE_CAPITAL_INCOME","ACCUMULATE_CAPITAL_INCOME_DOL","INTEREST_INCOME","INTEREST_INCOME_DOLLARS","ACCUMULATE_INTEREST_INCOME","ACCUMULATE_INTEREST_INCOME_DOL","RMB_SPE_PURCHASE_AMOUNT","RMB_SPE_PURCHASE_AMOUNT_DOL","ACC_RMB_SPE_PURCHASE_AMOUNT","ACC_RMB_SPE_PURC_AMOUNT_DOL","INCOME_TOTAL","INCOME_TOTAL_DOLLARS","ACCUMULATE_INCOME_TOTAL","ACCUMULATE_INCOME_TOTAL_DOL","SELL_EXCHANGE_SPECIAL_AMOUNT","SELL_EXCHANGE_SPE_AMOUNT_DOL","ACC_SELL_EXCHANGE_SPE_AMO","ACC_SELL_EXCHANGE_SPE_AMO_DOL","EXPEND_CAPITAL_AMOUNT","EXPEND_CAPITAL_AMOUNT_DOL","ACC_EXPEND_CAPITAL_AMOUNT","ACC_EXPEND_CAPITAL_AMOUNT_DOL","EXPEND_PROFIT_AMOUNT","EXPEND_PROFIT_AMOUNT_DOL","ACC_EXPEND_PROFIT_AMOUNT","ACC_EXPEND_PROFIT_AMOUNT_DOL","PAY_AMOUNT","PAY_AMOUNT_DOL","ACCUMULATE_PAY_AMOUNT","ACCUMULATE_PAY_AMOUNT_DOL","INCOME_AMOUNT","INCOME_AMOUNT_DOL","ACC_INCOME_AMOUNT","ACC_INCOME_AMOUNT_DOL"};
	
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
				
				//本月收入合计=本月汇入本金金额+本月利息收入金额+本月人民币特殊账户购汇划入金额。
				if(getDouble(data, "INCOME_TOTAL")!=getDouble(data, "CAPITAL_INCOME")+getDouble(data, "INTEREST_INCOME")+getDouble(data, "RMB_SPE_PURCHASE_AMOUNT"))
				{
					map.put("INCOME_TOTAL", "本月收入合计=本月汇入本金金额+本月利息收入金额+本月人民币特殊账户购汇划入金额。");
				}
				//本月收入合计折美元=本月汇入本金金额折美元+本月利息收入折美元+本月人民币特殊账户购汇划入金额折美元。
				if(getDouble(data, "INCOME_TOTAL_DOLLARS")!=getDouble(data, "CAPITAL_INCOME_DOLLARS")+getDouble(data, "INTEREST_INCOME_DOLLARS")+getDouble(data, "RMB_SPE_PURCHASE_AMOUNT_DOL"))
				{
					map.put("INCOME_TOTAL_DOLLARS", "本月收入合计折美元=本月汇入本金金额折美元+本月利息收入折美元+本月人民币特殊账户购汇划入金额折美元。");
				}
				//累计收入合计=累计汇入本金金额+累计利息收入金额+累计人民币特殊账户购汇划入金额。
				if(getDouble(data, "ACCUMULATE_INCOME_TOTAL")!=getDouble(data, "ACCUMULATE_CAPITAL_INCOME")+getDouble(data, "ACCUMULATE_INTEREST_INCOME")+getDouble(data, "ACC_RMB_SPE_PURCHASE_AMOUNT"))
				{
					map.put("ACCUMULATE_INCOME_TOTAL", "累计收入合计=累计汇入本金金额+累计利息收入金额+累计人民币特殊账户购汇划入金额。");
				}
				//累计收入合计折美元=累计汇入本金金额折美元+累计利息收入金额折美元+累计人民币特殊账户购汇划入金额折美元。
				if(getDouble(data, "ACCUMULATE_INCOME_TOTAL_DOL")!=getDouble(data, "ACCUMULATE_CAPITAL_INCOME_DOL")+getDouble(data, "ACCUMULATE_INTEREST_INCOME_DOL")+getDouble(data, "ACC_RMB_SPE_PURC_AMOUNT_DOL"))
				{
					map.put("ACCUMULATE_INCOME_TOTAL_DOL", "累计收入合计=累计汇入本金金额+累计利息收入金额+累计人民币特殊账户购汇划入金额。");
				}

				//本月支出合计=本月结汇划入人民币特殊账户金额+本月汇出本金金额+本月汇出收益金额
				if(getDouble(data, "PAY_AMOUNT")!=getDouble(data, "SELL_EXCHANGE_SPECIAL_AMOUNT")+getDouble(data, "EXPEND_CAPITAL_AMOUNT")+getDouble(data, "EXPEND_PROFIT_AMOUNT"))
				{
					map.put("PAY_AMOUNT", "本月支出合计=本月结汇划入人民币特殊账户金额+本月汇出本金金额+本月汇出收益金额。");
				}
				//本月支出合计折美元=本月结汇划入人民币特殊账户金额折美元+本月汇出本金金额折美元+本月汇出收益金额折美元
				if(getDouble(data, "PAY_AMOUNT_DOL")!=getDouble(data, "SELL_EXCHANGE_SPE_AMOUNT_DOL")+getDouble(data, "EXPEND_CAPITAL_AMOUNT_DOL")+getDouble(data, "EXPEND_PROFIT_AMOUNT_DOL"))
				{
					map.put("PAY_AMOUNT_DOL", "本月支出合计折美元=本月结汇划入人民币特殊账户金额折美元+本月汇出本金金额折美元+本月汇出收益金额折美元。");
				}
				//累计支出合计=累计结汇划入人民币特殊账户金额+累计汇出本金金额+累计汇出收益金额
				if(getDouble(data, "ACCUMULATE_PAY_AMOUNT")!=getDouble(data, "ACC_SELL_EXCHANGE_SPE_AMO")+getDouble(data, "ACC_EXPEND_CAPITAL_AMOUNT")+getDouble(data, "ACC_EXPEND_PROFIT_AMOUNT"))
				{
					map.put("ACCUMULATE_PAY_AMOUNT", "累计支出合计=累计结汇划入人民币特殊账户金额+累计汇出本金金额+累计汇出收益金额。");
				}
				//累计支出合计折美元=累计结汇划入人民币特殊账户金额折美元+累计汇出本金金额折美元+累计汇出收益金额折美元
				if(getDouble(data, "ACCUMULATE_PAY_AMOUNT_DOL")!=getDouble(data, "ACC_SELL_EXCHANGE_SPE_AMO_DOL")+getDouble(data, "ACC_EXPEND_CAPITAL_AMOUNT_DOL")+getDouble(data, "ACC_EXPEND_PROFIT_AMOUNT_DOL"))
				{
					map.put("ACCUMULATE_PAY_AMOUNT_DOL", "累计支出合计=累计结汇划入人民币特殊账户金额+累计汇出本金金额+累计汇出收益金额。");
				}

				//本月净汇入金额与本月净汇入金额折美元为一组，同时为空或不为空。
				if(!isAllEmpty(data, new String[]{"INCOME_AMOUNT","INCOME_AMOUNT_DOL"}) && !isAllNotEmpty(data,  new String[]{"INCOME_AMOUNT","INCOME_AMOUNT_DOL"}))
				{
					map.put("INCOME_AMOUNT", "本月净汇入金额与本月净汇入金额折美元为一组，同时为空或不为空。");
					map.put("INCOME_AMOUNT_DOL", "本月净汇入金额与本月净汇入金额折美元为一组，同时为空或不为空。");
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
