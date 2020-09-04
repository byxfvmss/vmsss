package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.logic.model.VerifyModel;

public class RqfiiIncomeExpendDataVerify extends AgencyDataVerify {
	
	public RqfiiIncomeExpendDataVerify(){
	}

	public RqfiiIncomeExpendDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}
	private final String[] t_checkPositionNum=new String[]{"CAPITAL_INCOME","ACC_CAPITAL_INCOME","EXPEND_CAPITAL_AMOUNT","ACC_EXPEND_CAPITAL_AMOUNT","BUY_EXPEND_CAPITAL_AMOUNT","ACC_BUY_EXPEND_CAPITAL_AMOUNT","EXPEND_PROFIT_AMOUNT","ACC_EXPEND_PROFIT_AMOUNT","BUY_EXPEND_PROFIT_AMOUNT","ACC_BUY_EXPEND_PROFIT_AMOUNT","CLEAR_INCOME_AMOUNT","ACC_CLEAR_INCOME_AMOUNT"};
	
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
				//本月购汇汇出本金金额<本月汇出本金金额。
				if(getDouble(data, "BUY_EXPEND_CAPITAL_AMOUNT")>=getDouble(data, "EXPEND_CAPITAL_AMOUNT"))
				{
					map.put("BUY_EXPEND_CAPITAL_AMOUNT", "[本月购汇汇出本金金额]应小于[本月汇出本金金额]");
					map.put("EXPEND_CAPITAL_AMOUNT", "[本月购汇汇出本金金额]应小于[本月汇出本金金额]");
				}
				//累计购汇汇出本金金额<累计汇出本金金额。
				if(getDouble(data, "ACC_BUY_EXPEND_CAPITAL_AMOUNT")>=getDouble(data, "ACC_EXPEND_CAPITAL_AMOUNT"))
				{
					map.put("ACC_BUY_EXPEND_CAPITAL_AMOUNT", "[累计购汇汇出本金金额]应小于[累计汇出本金金额]");
					map.put("ACC_EXPEND_CAPITAL_AMOUNT", "[累计购汇汇出本金金额]应小于[累计汇出本金金额]");
				}
				//本月购汇汇出收益金额<本月汇出收益金额
				if(getDouble(data, "BUY_EXPEND_PROFIT_AMOUNT")>=getDouble(data, "EXPEND_PROFIT_AMOUNT"))
				{
					map.put("BUY_EXPEND_PROFIT_AMOUNT", "[本月购汇汇出收益金额]应小于[本月汇出收益金额]");
					map.put("EXPEND_PROFIT_AMOUNT", "[本月购汇汇出收益金额]应小于[本月汇出收益金额]");
				}
				//累计购汇汇出收益金额<累计汇出收益金额
				if(getDouble(data, "ACC_BUY_EXPEND_PROFIT_AMOUNT")>=getDouble(data, "ACC_EXPEND_PROFIT_AMOUNT"))
				{
					map.put("ACC_BUY_EXPEND_PROFIT_AMOUNT", "[累计购汇汇出收益金额]应小于[累计汇出收益金额]");
					map.put("ACC_EXPEND_PROFIT_AMOUNT", "[累计购汇汇出收益金额]应小于[累计汇出收益金额]");
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
