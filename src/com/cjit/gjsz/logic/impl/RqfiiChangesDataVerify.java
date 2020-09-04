package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.logic.model.VerifyModel;

public class RqfiiChangesDataVerify extends AgencyDataVerify {
	
	public RqfiiChangesDataVerify(){
	}

	public RqfiiChangesDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_checkPositionNum=new String[]{"INVEST_CAPITAL_INCOME_AMOUNT","ACC_INVEST_CAPITAL_IN_AMOUNT","SELL_SECURITY_AMOUNT","ACC_SELL_SECURITY_AMOUNT","EXCHANGE_SELL_SECURITY_AMOUNT","ACC_EX_SELL_SECURITY_AMOUNT","BANK_SELL_SECURITY_AMOUNT","ACC_BANK_SELL_SECURITY_AMOUNT","DIVIDEND_INCOME","ACC_DIVIDEND_INCOME","INTEREST_INCOME","ACC_INTEREST_INCOME","OTHER_INCOME","ACCUMULATE_OTHER_INCOME","INCOME_TOTAL","ACCUMULATE_INCOME_TOTAL","EXPEND_CAPITAL_AMOUNT","ACC_EXPEND_CAPITAL_AMOUNT","BUY_EXPEND_CAPITAL_AMOUNT","ACC_BUY_EXPEND_CAPITAL_AMOUNT","PAY_SECURITY_AMOUNT","ACC_PAY_SECURITY_AMOUNT","EXCHANGE_PAY_SECURITY_AMOUNT","ACC_EX_PAY_SECURITY_AMOUNT","BANK_PAY_SECURITY_AMOUNT","ACC_BANK_PAY_SECURITY_AMOUNT","DEPOSIT_AMOUNT","ACCUMULATE_DEPOSIT_AMOUNT","MANAGE_AMOUNT","ACCUMULATE_MANAGE_AMOUNT","OTHER_PAY_AMOUNT","ACCUMULATE_OTHER_PAY_AMOUNT","PAY_AMOUNT","ACCUMULATE_PAY_AMOUNT"};
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
				//本月交易所市场卖出证券所得价款额=本月卖出证券所得价款额.+本月银行间市场卖出证券所得价款额。
				if(getDouble(data, "EXCHANGE_SELL_SECURITY_AMOUNT")!=getDoubleSum(data, new String []{"SELL_SECURITY_AMOUNT","BANK_SELL_SECURITY_AMOUNT"}))
				{
					map.put("EXCHANGE_SELL_SECURITY_AMOUNT", "本月交易所市场卖出证券所得价款额=本月卖出证券所得价款额+本月银行间市场卖出证券所得价款额。");
				}
				//累计交易所市场卖出证券所得价款额=累计卖出证券所得价款额+累计银行间市场卖出证券所得价款额。
				if(getDouble(data, "ACC_EX_SELL_SECURITY_AMOUNT")!=getDoubleSum(data, new String []{"ACC_SELL_SECURITY_AMOUNT","ACC_BANK_SELL_SECURITY_AMOUNT"}))
				{
					map.put("ACC_EX_SELL_SECURITY_AMOUNT", "累计交易所市场卖出证券所得价款额=累计卖出证券所得价款额+累计银行间市场卖出证券所得价款额。");
				}
				
				//本月买入证券支付价款额=本月交易所市场买入证券支付价款额+本月银行间市场买入证券支付价款额
				if(getDouble(data, "PAY_SECURITY_AMOUNT")!=getDoubleSum(data, new String []{"EXCHANGE_PAY_SECURITY_AMOUNT","BANK_PAY_SECURITY_AMOUNT"}))
				{
					map.put("PAY_SECURITY_AMOUNT", "本月买入证券支付价款额=本月交易所市场买入证券支付价款额+本月银行间市场买入证券支付价款额。");
				}
				//累计买入证券支付价款额=累计交易所市场买入证券支付价款额+累计银行间市场买入证券支付价款额
				if(getDouble(data, "ACC_PAY_SECURITY_AMOUNT")!=getDoubleSum(data, new String []{"ACC_EX_PAY_SECURITY_AMOUNT","ACC_BANK_PAY_SECURITY_AMOUNT"}))
				{
					map.put("ACC_PAY_SECURITY_AMOUNT", "累计买入证券支付价款额=累计交易所市场买入证券支付价款额+累计银行间市场买入证券支付价款额。");
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
