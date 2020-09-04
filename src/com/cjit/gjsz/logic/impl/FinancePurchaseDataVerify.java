package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.FinancePurchase;
import com.cjit.gjsz.logic.model.VerifyModel;

public class FinancePurchaseDataVerify extends FinanceDataVerify implements
		DataVerify{

	private VerifyConfig verifyConfig;

	public FinancePurchaseDataVerify(){
	}

	public FinancePurchaseDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				FinancePurchase financePurchase = (FinancePurchase) verifylList
						.get(i);
				if(!verifyActiontype(financePurchase.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(financePurchase.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(financePurchase.getActiontype(),
						financePurchase.getRptno())){
					map.put("RPTNO", "当 [申报号码] 为空时, [操作类型] 必需为 [新建]");
				}
				if(!verifyAReasion(financePurchase.getActiontype(),
						financePurchase.getActiondesc())){
					map.put("ACTIONDESC", "当 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				if(!verifyReasion(financePurchase.getActiontype(),
						financePurchase.getActiondesc())){
					map.put("ACTIONDESC", "当 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyConfig.verifyRegno(financePurchase.getRegno(),
						financePurchase.getTxcode(), null, "payout")){
					map
							.put(
									"REGNO",
									"当 [外汇局批件号/备案表号/业务编号] 资本项目项下交易（涉外收支交易编码以“5”、“6”、“7”、“8”和部分“9”开头，“外汇局批件号/备案表号/业务编号”不能为空.\n");
				}
				if(!verifyTxcode(financePurchase.getTxcode(), BOP_PAYOUT)){
					String type = getKey(financePurchase.getTxcode(),
							BOP_PAYOUT);
					map.put("TXCODE", "[交易编码] [" + type
							+ "] 无效.必须在国际收支交易编码表中存在 \n");
				}
				if(!verifyCrtuser(financePurchase.getCrtuser())){
					map.put("CRTUSER", "[填报人] 不能为空。\n");
				}
				if(!verifyInptelc(financePurchase.getInptelc())){
					map.put("INPTELC", "[填报人电话]不能为空。 \n");
				}
				if(!verifyRptdate(financePurchase.getRptdate())){
					map
							.put("RPTDATE",
									" 按申报主体真实 [申报日期] 填写，不能为空,必须小于等于当前日期。 \n");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		verifyConfig = vc;
	}
}
