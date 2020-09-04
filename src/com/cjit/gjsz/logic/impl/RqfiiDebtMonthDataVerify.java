package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.logic.model.VerifyModel;

public class RqfiiDebtMonthDataVerify extends AgencyDataVerify {
	
	public RqfiiDebtMonthDataVerify(){
	}

	public RqfiiDebtMonthDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_checkPositionNum=new String[]{"SAVING_BALANCE","STOCK_COST","STOCK_VALUE","STOCK_FUND_COST",
			"STOCK_FUND_VALUE","REGULAR_PROFIT_FUND_COST","REGULAR_PROFIT_FUND_VALUE","OTHER_FUND_COST","OTHER_FUND_VALUE",
			"SPIF_COST","SPIF_VALUE","SHARE_WARRANT_COST","SHARE_WARRANT_VALUE","EXCHANGE_GOVERNMENT_LOAN_COST","EXCHANGE_GOVERNMENT_LOAN_VALUE",
			"EXCHANGE_LOCAL_DEBT_COST","EXCHANGE_LOCAL_DEBT_VALUE","EXCHANGE_COMPANY_DEBT_COST","EXCHANGE_COMPANY_DEBT_VALUE",
			"EXCHANGE_EX_DEBT_COST","EXCHANGE_EX_DEBT_VALUE","OTHER_EXCHANGE_DEBT_COST","OTHER_EXCHANGE_DEBT_VALUE","BANK_GOVERNMENT_DEBT_COST",
			"BANK_GOVERNMENT_DEBT_VALUE","BANK_FINANCIAL_DEBT_COST","BANK_FINANCIAL_DEBT_VALUE","BANK_CENTRE_BILL_COST","BANK_CENTRE_BILL_VALUE",
			"BANK_SHORT_FINANCING_COST","BANK_SHORT_FINANCING_VALUE","BANK_INTERIM_BILL_COST","BANK_INTERIM_BILL_VALUE","BANK_COMPANY_LOAN_COST",
			"BANK_COMPANY_LOAN_VALUE","BANK_LOCAL_LOAN_COST","BANK_LOCAL_LOAN_VALUE","BANK_FUND_SUPPORT_COST","BANK_FUND_SUPPORT_VALUE",
			"OTHER_BANK_LOAN_COST","OTHER_BANK_LOAN_VALUE","CLEAR_AMOUNT_INCOME","STOCK_PROFIT_AMOUNT_INCOME","PROFIT_AMOUNT_INCOME",
			"OTHER_AMOUNT_INCOME","CLEAR_AMOUNT_EXPEND","DEPOSIT_AMOUNT_EXPEND","MANAGER_AMOUNT_EXPEND","TAX_AMOUNT_EXPEND",
			"OTHER_AMOUNT_EXPEND","INCOME_VELOCITY","EXPEND_VELOCITY"};
	
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
				//投资市值合计=银行存款余额+股票市值+股票类基金市值+固定收益类市值+其他基金市值+股指期货市值+权证市值+交易所市场国债市值+交易所市场地方债市值+交易所市场公司债（企业债）市值+交易所市场可转债市值+其他交易所市场证券市值+银行间市场国债市值+银行间市场金融债市值+银行间市场央票市值+银行间市场短期融资券市值+银行间市场中期票据市值+银行间市场企业债市值+银行间市场地方政府债市值+银行间市场资产支持证券市值+其他银行间市场证券市值 
				double invest_value=getDoubleSum(data, new String []{"SAVING_BALANCE","STOCK_VALUE","STOCK_FUND_VALUE",
						"REGULAR_PROFIT_FUND_VALUE","OTHER_FUND_VALUE","SPIF_VALUE","SHARE_WARRANT_VALUE","EXCHANGE_GOVERNMENT_LOAN_VALUE",
						"EXCHANGE_LOCAL_DEBT_VALUE","EXCHANGE_COMPANY_DEBT_VALUE","EXCHANGE_EX_DEBT_VALUE","OTHER_EXCHANGE_DEBT_VALUE",
						"BANK_GOVERNMENT_DEBT_VALUE","BANK_FINANCIAL_DEBT_VALUE","BANK_CENTRE_BILL_VALUE","BANK_SHORT_FINANCING_VALUE",
						"BANK_INTERIM_BILL_VALUE","BANK_COMPANY_LOAN_VALUE","BANK_LOCAL_LOAN_VALUE","BANK_FUND_SUPPORT_VALUE","OTHER_BANK_LOAN_VALUE"});
				if(getDouble(data, "INVESTMENT_VALUE_TOTAL")!=invest_value)
				{
					map.put("INVESTMENT_VALUE_TOTAL", "投资市值合计=银行存款余额+股票市值+股票类基金市值+固定收益类市值+其他基金市值+股指期货市值+权证市值+交易所市场国债市值+交易所市场地方债市值+交易所市场公司债（企业债）市值+交易所市场可转债市值+其他交易所市场证券市值+银行间市场国债市值+银行间市场金融债市值+银行间市场央票市值+银行间市场短期融资券市值+银行间市场中期票据市值+银行间市场企业债市值+银行间市场地方政府债市值+银行间市场资产支持证券市值+其他银行间市场证券市值 。");
				}
				//资产市值合计=银行存款余额+股票市值+股票类基金市值+固定收益类市值+其他基金市值+股指期货市值+权证市值+交易所市场国债市值+交易所市场地方债市值+交易所市场公司债（企业债）市值+交易所市场可转债市值+其他交易所市场证券市值+银行间市场国债市值+银行间市场金融债市值+银行间市场央票市值+银行间市场短期融资券市值+银行间市场中期票据市值+银行间市场企业债市值+银行间市场地方政府债市值+银行间市场资产支持证券市值+其他银行间市场证券市值+应收清算款金额+应收股利金额+应收利息金额+其他应收款金额
				if(getDouble(data, "ASSETS_TOTAL")!=invest_value+getDoubleSum(data, new String []{"CLEAR_AMOUNT_INCOME","STOCK_PROFIT_AMOUNT_INCOME","PROFIT_AMOUNT_INCOME","OTHER_AMOUNT_INCOME"}))
				{
					map.put("ASSETS_TOTAL", "资产市值合计=银行存款余额+股票市值+股票类基金市值+固定收益类市值+其他基金市值+股指期货市值+权证市值+交易所市场国债市值+交易所市场地方债市值+交易所市场公司债（企业债）市值+交易所市场可转债市值+其他交易所市场证券市值+银行间市场国债市值+银行间市场金融债市值+银行间市场央票市值+银行间市场短期融资券市值+银行间市场中期票据市值+银行间市场企业债市值+银行间市场地方政府债市值+银行间市场资产支持证券市值+其他银行间市场证券市值+应收清算款金额+应收股利金额+应收利息金额+其他应收款金额。");
				}
				//负债合计=应付清算款金额+应付托管费金额+应付管理费金额+应纳税款金额+其他应付款金额。
				if(getDouble(data, "DEBT_TOTAL")!=getDoubleSum(data, new String []{"CLEAR_AMOUNT_EXPEND","DEPOSIT_AMOUNT_EXPEND","MANAGER_AMOUNT_EXPEND","TAX_AMOUNT_EXPEND","OTHER_AMOUNT_EXPEND"}))
				{
					map.put("DEBT_TOTAL", "负债合计=应付清算款金额+应付托管费金额+应付管理费金额+应纳税款金额+其他应付款金额。");
				}
				//净资产=资产合计-负债合计。
				if(getDouble(data, "CLEAN_ASSETS")!=getDouble(data, "ASSETS_TOTAL")-getDouble(data, "DEBT_TOTAL"))
				{
					map.put("CLEAN_ASSETS", "净资产=资产市值合计-负债合计。");
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
