package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QfiiChangesSpecialDataVerify extends AgencyDataVerify {
	
	public QfiiChangesSpecialDataVerify(){
	}

	public QfiiChangesSpecialDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_checkPositionNum=new String[]{"FOREIGN_INCOME","ACCUMULATE_FOREIGN_INCOME","SELL_SECURITY_AMOUNT","ACCUMULATE_SELL_SECURIT_AMOUNT","DIVIDEND_INCOME","ACC_DIVIDEND_INCOME","INTEREST_INCOME","ACCUMULATE_INTEREST_INCOME","OTHER_INCOME","ACCUMULATE_OTHER_INCOME","INCOME_TOTAL","ACCUMULATE_INCOME_TOTAL","EXCHANGE_AMOUNT","ACCUMULATE_EXCHANGE_AMOUNT","PAY_SECURITY_AMOUNT","ACCUMULATE_PAY_SECURITY_AMOUNT","DEPOSIT_AMOUNT","ACCUMULATE_DEPOSIT_AMOUNT","MANAGE_AMOUNT","ACCUMULATE_MANAGE_AMOUNT","OTHER_PAY_AMOUNT","ACCUMULATE_OTHER_PAY_AMOUNT","PAY_AMOUNT","ACCUMULATE_PAY_AMOUNT"};
	
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
				//人民币校验
				String curr=(String)data.get("CURRENCE_CODE");
				if(!"CNY".equals(curr)) map.put("CURRENCE_CODE", "[币种]只能选择CNY-人民币");
				
				//本月收入合计=本月外汇账户结汇划入金额+本月卖出证券所得价款金额+本月股利收入金额+本月利息收入金额+本月其他收入金额。
				if(getDouble(data, "INCOME_TOTAL")!=getDouble(data, "FOREIGN_INCOME")+getDouble(data, "SELL_SECURITY_AMOUNT")+
						getDouble(data, "DIVIDEND_INCOME")+getDouble(data, "INTEREST_INCOME")+getDouble(data, "OTHER_INCOME"))
				{
					map.put("INCOME_TOTAL", "本月收入合计=本月外汇账户结汇划入金额+本月卖出证券所得价款金额+本月股利收入金额+本月利息收入金额+本月其他收入金额。");
				}
				
				//累计收入合计=累计外汇账户结汇划入金额+累计卖出证券所得价款金额+累计股利收入金额+累计利息收入金额+累计其他收入金额。
				if(getDouble(data, "ACCUMULATE_INCOME_TOTAL")!=getDouble(data, "ACCUMULATE_FOREIGN_INCOME")+getDouble(data, "ACCUMULATE_SELL_SECURIT_AMOUNT")+
						getDouble(data, "ACC_DIVIDEND_INCOME")+getDouble(data, "ACCUMULATE_INTEREST_INCOME")+getDouble(data, "ACCUMULATE_OTHER_INCOME"))
				{
					map.put("ACCUMULATE_INCOME_TOTAL", "累计收入合计=累计外汇账户结汇划入金额+累计卖出证券所得价款金额+累计股利收入金额+累计利息收入金额+累计其他收入金额。");
				}
				
				//本月支出合计=本月购汇划入外汇账户金额+本月买入证券支付价款金额+本月托管费支出金额+本月管理费支出金额+本月其他支出金额。
				if(getDouble(data, "PAY_AMOUNT")!=getDouble(data, "EXCHANGE_AMOUNT")+getDouble(data, "PAY_SECURITY_AMOUNT")+
						getDouble(data, "DEPOSIT_AMOUNT")+getDouble(data, "MANAGE_AMOUNT")+getDouble(data, "OTHER_PAY_AMOUNT"))
				{
					map.put("PAY_AMOUNT", "本月支出合计=本月购汇划入外汇账户金额+本月买入证券支付价款金额+本月托管费支出金额+本月管理费支出金额+本月其他支出金额。");
				}
				
				//累计支出合计=累计购汇划入外汇账户金额+累计买入证券支付价款金额+累计托管费支出金额+累计管理费支出金额+累计其他支出金额。
				if(getDouble(data, "ACCUMULATE_PAY_AMOUNT")!=getDouble(data, "ACCUMULATE_EXCHANGE_AMOUNT")+getDouble(data, "ACCUMULATE_PAY_SECURITY_AMOUNT")+
						getDouble(data, "ACCUMULATE_DEPOSIT_AMOUNT")+getDouble(data, "ACCUMULATE_MANAGE_AMOUNT")+getDouble(data, "ACCUMULATE_OTHER_PAY_AMOUNT"))
				{
					map.put("ACCUMULATE_PAY_AMOUNT", "累计支出合计=累计购汇划入外汇账户金额+累计买入证券支付价款金额+累计托管费支出金额+累计管理费支出金额+累计其他支出金额。");
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
