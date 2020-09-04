package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QdiiInvestDataVerify extends AgencyDataVerify {

	public QdiiInvestDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_checkPositionNum=new String[]{"BANK_SAVINGS_BALANCE","CURRENCY_TOOLS_COST","CURRENCY_TOOLS_VALUE",
			"DEBENTURE_COST","DEBENTURE_VALUE","COMPANY_STOCK_COST","COMPANY_STOCK_VALUE","FUND_COST","FUND_VALUE","DERIVATIVE_COST",
			"DERIVATIVE_VALUE","OTHER_INVEST_COST","OTHER_INVEST_VALUE","EXPEND_INVEST_AMOUNT","INVEST_AMOUNT_INCOME","STOCK_PROFIT_AMOUNT_INCOME",
			"PROFIT_AMOUNT_INCOME","OTHER_AMOUNT_INCOME","ASSETS_TOTAL","INVEST_AMOUNT_EXPEND","DEPOSIT_AMOUNT_EXPEND","BROKERAGE_AMOUNT_EXPEND",
			"MANAGER_AMOUNT_EXPEND","TAX_AMOUNT_EXPEND","OTHER_AMOUNT_EXPEND","DEBT_TOTAL","DEPOSIT_RMB_SAVING_BALANCE"};
	
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
				//投资市值合计=银行存款余额+货币市场工具市值+债券市值+公司股票市值+基金市值+衍生产品市值+其他投资市值。
				if(getDouble(data, "INVEST_VALUE_TOTAL")!=getDoubleSum(data,new String[]{"BANK_SAVINGS_BALANCE","CURRENCY_TOOLS_VALUE","DEBENTURE_VALUE","COMPANY_STOCK_VALUE","FUND_VALUE","DERIVATIVE_VALUE","OTHER_INVEST_VALUE"}))
				{
					map.put("INVEST_VALUE_TOTAL", "投资市值合计=银行存款余额+货币市场工具市值+债券市值+公司股票市值+基金市值+衍生产品市值+其他投资市值");
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
