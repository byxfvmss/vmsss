package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.model.VerifyModel;

public class QfiiProfitLossDataVerify extends AgencyDataVerify{

	public QfiiProfitLossDataVerify(){
	}

	public QfiiProfitLossDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	private final String[] t_checkPositionNum = new String[] {
			"STOCK_PROFIT_AMOUNT_INCOME", "PROFIT_AMOUNT_INCOME",
			"OTHER_AMOUNT_INCOME", "INCOME_TOTAL", "DEPOSIT_AMOUNT",
			"MANAGER_AMOUNT", "TAX_AMOUNT", "OTHER_AMOUNT", "EXPEND_TOTAL"};

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Map data = (Map) verifylList.get(i);
				// >0
				map.putAll(checkPositiveNumber(data, t_checkPositionNum));
				// 托管人代码金融机构标识码
				String strDistrictCode = (String) data.get("CUSTODIAN_ID");
				if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode
						.substring(0, 6))){
					map.put("CUSTODIAN_ID", "[托管人代码]应为金融机构标识码,前6位数字地区标识码有误 ");
				}
				String value = (String) data.get("CURRENCE_CODE");
				if(!value.equals("CNY"))
					map.put("CURRENCE_CODE", "[币种]只能选择人民币");
				// 收入合计=股利收入金额+利息收入金额+其他收入金额。
				if(getDouble(data, "INCOME_TOTAL") != getDoubleSum(data,
						new String[] {"STOCK_PROFIT_AMOUNT_INCOME",
								"PROFIT_AMOUNT_INCOME", "OTHER_AMOUNT_INCOME"})){
					map.put("INCOME_TOTAL", "收入合计=股息收入金额+利息收入金额+其他收入金额。");
				}
				// 费用合计=托管费金额+管理费金额+税款金额+其他费用（税费）金额。
				if(getDouble(data, "EXPEND_TOTAL") != getDoubleSum(data,
						new String[] {"DEPOSIT_AMOUNT", "MANAGER_AMOUNT",
								"TAX_AMOUNT", "OTHER_AMOUNT"})){
					map
							.put("EXPEND_TOTAL",
									"费用合计=托管费金额+管理费金额+税款金额+其他费用（税费）金额。");
				}
			}
		}
		return verifyModel;
	}
}
