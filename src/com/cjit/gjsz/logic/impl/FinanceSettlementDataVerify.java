package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.FinanceSettlement;
import com.cjit.gjsz.logic.model.VerifyModel;

public class FinanceSettlementDataVerify extends FinanceDataVerify implements
		DataVerify{

	private VerifyConfig verifyConfig;

	public FinanceSettlementDataVerify(){
	}

	public FinanceSettlementDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				FinanceSettlement financeSettlement = (FinanceSettlement) verifylList
						.get(i);
				if(!verifyActiontype(financeSettlement.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(financeSettlement.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(financeSettlement.getActiontype(),
						financeSettlement.getRptno())){
					map.put("RPTNO", "当 [申报号码] 为空时, [操作类型] 必需为 [新建]");
				}
				if(!verifyAReasion(financeSettlement.getActiontype(),
						financeSettlement.getActiondesc())){
					map.put("ACTIONDESC", "当 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				if(!verifyReasion(financeSettlement.getActiontype(),
						financeSettlement.getActiondesc())){
					map.put("ACTIONDESC", "当 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyConfig.verifyRegno(financeSettlement.getRegno(),
						financeSettlement.getTxcode(), null, "income")){
					map
							.put(
									"REGNO",
									"当 [外汇局批件号/备案表号/业务编号] 资本项目项下交易（涉外收支交易编码以“5”、“6”、“7”、“8”和部分“9”开头，“外汇局批件号/备案表号/业务编号”不能为空.\n");
				}
				if(!verifyTxcode(financeSettlement.getTxcode(), BOP_INCOME)){
					String type = getKey(financeSettlement.getTxcode(),
							BOP_INCOME);
					map.put("TXCODE", "[交易编码] [" + type
							+ "] 无效.必须在国际收支交易编码表中存在 \n");
				}
				if(!verifyUseType(financeSettlement.getUsetype(),
						financeSettlement.getTxcode(), SETTLEMENT_USERTYPE)){
					map.put("USETYPE", "[结汇用途] 资本项目项下必填且必须在结汇用途表里存在。\n");
				}
				if(!verifyUseDetail(financeSettlement.getUsedetail(),
						financeSettlement.getUsetype())){
					map
							.put("USEDETAIL",
									"[结汇详细用途] 如果结汇用途选择“005支付其他服务费用”、“006支付税款”或“099其他”，则应填列详细用途。\n");
				}
				if(!verifyCrtuser(financeSettlement.getCrtuser())){
					map.put("CRTUSER", "[填报人] 不能为空。\n");
				}
				if(!verifyInptelc(financeSettlement.getInptelc())){
					map.put("INPTELC", "[填报人电话]不能为空。 \n");
				}
				if(!verifyRptdate(financeSettlement.getRptdate())){
					map
							.put("RPTDATE",
									" 按申报主体真实 [申报日期] 填写，不能为空,必须小于等于当前日期。 \n");
				}
			}
		}
		return verifyModel;
	}

	/**
	 * 结汇用途 校验
	 * @param usetype 结汇用途
	 * @param dicCodeType 结汇用途对应字典类型
	 * @return 校验通过true 校验未通过false
	 */
	private boolean verifyUseType(String usetype, String txCode,
			String dicCodeType){
		if(StringUtil.isEmpty(usetype)){
			if(!verifyConfig.verifyRegno(usetype, txCode, null, "income")){
				return false;
			}
			return true;
		}else{
			return findKey(dictionarys, dicCodeType, usetype);
		}
	}

	public void setVerifyConfig(VerifyConfig vc){
		verifyConfig = vc;
	}
}
