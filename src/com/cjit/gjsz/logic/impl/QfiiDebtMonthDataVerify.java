package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QfiiDebtMonthDataVerify extends AgencyDataVerify {
	
	public QfiiDebtMonthDataVerify(){
	}

	public QfiiDebtMonthDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}
	private final String[] t_checkPositionNum=new String[]{"SAVING_BALANCE","STOCK_COST","STOCK_VALUE","GOVERNMENT_LOAN_COST","GOVERNMENT_LOAN_VALUE","EXCHANGE_DEBT_COST","EXCHANGE_DEBT_VALUE","COMPANY_DEBT_COST","COMPANY_DEBT_VALUE","CLOSED_FUND_COST","CLOSED_FUND_VALUE","OPEN_FUND_COST","OPEN_FUND_VALUE","SHARE_WARRANT_COST","SHARE_WARRANT_VALUE","SPIF_COST","SPIF_VALUE","OTHER_COST","OTHER_VALUE","CLEAR_AMOUNT_INCOME","STOCK_PROFIT_AMOUNT_INCOME","PROFIT_AMOUNT_INCOME","OTHER_AMOUNT_INCOME","CLEAR_AMOUNT_EXPEND","DEPOSIT_AMOUNT_EXPEND","MANAGER_AMOUNT_EXPEND","TAX_AMOUNT_EXPEND","OTHER_AMOUNT_EXPEND","INCOME_VELOCITY","EXPEND_VELOCITY"};
	
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
				
				//投资市值合计=银行存款余额+股票市值+国债市值+可转债市值+公司债市值+封闭式基金市值+开放式基金市值+权证市值+股指期货市值+其他投资市值。
				if(getDouble(data, "INVESTMENT_VALUE_TOTAL")!=getDoubleSum(data,new String[]{"SAVING_BALANCE","STOCK_VALUE","GOVERNMENT_LOAN_VALUE","EXCHANGE_DEBT_VALUE","COMPANY_DEBT_VALUE","CLOSED_FUND_VALUE","OPEN_FUND_VALUE","SHARE_WARRANT_VALUE","SPIF_VALUE","OTHER_VALUE"}))
				{
					map.put("INVESTMENT_VALUE_TOTAL", "投资市值合计=银行存款余额+股票市值+国债市值+可转债市值+公司债市值+封闭式基金市值+开放式基金市值+权证市值+股指期货市值+其他投资市值。");
				}
				//资产合计=银行存款余额+股票市值+国债市值+可转债市值+公司债市值+封闭式基金市值+开放式基金市值+权证市值+股指期货市值+其他投资市值+应收清算款+应收股利+应收利息+其他应收款。
				if(getDouble(data, "ASSETS_TOTAL")!=getDoubleSum(data,new String[]{ "SAVING_BALANCE", "STOCK_VALUE", "GOVERNMENT_LOAN_VALUE"
						, "EXCHANGE_DEBT_VALUE", "COMPANY_DEBT_VALUE", "CLOSED_FUND_VALUE", "OPEN_FUND_VALUE", "SHARE_WARRANT_VALUE", "SPIF_VALUE","OTHER_VALUE"
						, "CLEAR_AMOUNT_INCOME", "STOCK_PROFIT_AMOUNT_INCOME", "PROFIT_AMOUNT_INCOME", "OTHER_AMOUNT_INCOME"}))
				{
					map.put("ASSETS_TOTAL", "资产合计=银行存款余额+股票市值+国债市值+可转债市值+公司债市值+封闭式基金市值+开放式基金市值+权证市值+股指期货市值+其他投资市值+应收清算款+应收股利+应收利息+其他应收款。");
				}
				
				//负债合计=应付清算款+应付托管费+应付管理费+应纳税款+其他应付款。
				if(getDouble(data, "DEBT_TOTAL")!=getDoubleSum(data,new String[]{"CLEAR_AMOUNT_EXPEND","DEPOSIT_AMOUNT_EXPEND","MANAGER_AMOUNT_EXPEND","TAX_AMOUNT_EXPEND","OTHER_AMOUNT_EXPEND"}))
				{
					map.put("DEBT_TOTAL", "负债合计=应付清算款+应付托管费+应付管理费+应纳税款+其他应付款。");
				}
				//净资产=资产合计-负债合计。
				if(getDouble(data, "CLEAN_ASSETS")!=getDouble(data,"ASSETS_TOTAL")-getDouble(data,"DEBT_TOTAL"))
				{
					map.put("CLEAN_ASSETS", "净资产=资产合计-负债合计。");
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
