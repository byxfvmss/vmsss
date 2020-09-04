package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_B_EXGUARAN;
import com.cjit.gjsz.logic.model.Self_Sub_GUPER;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_SUB_GUPER_INFO]履约信息表校验类
 */
public class Self_Sub_GuperVerify extends SelfDataVerify implements DataVerify{

	private Self_B_EXGUARAN contractExguaran;

	public Self_Sub_GuperVerify(){
	}

	public Self_Sub_GuperVerify(List dictionarys, List verifylList,
			Self_B_EXGUARAN contractExguaran){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
		this.contractExguaran = contractExguaran;
	}

	public void setDictionarys(List dictionarys){
		this.dictionarys = dictionarys;
	}

	public void setVerifylList(List verifylList){
		this.verifylList = verifylList;
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Self_Sub_GUPER sub = (Self_Sub_GUPER) verifylList.get(i);
				// 履约日期
				if(isNull(sub.getGuperdate())){
					map.put("GUPERDATE", "[履约日期] 不能为空 ");
				}else if(StringUtil.isNotEmpty(contractExguaran
						.getContractdate())){
					if(!verifyTwoDates(contractExguaran.getContractdate(), sub
							.getGuperdate())){
						map.put("GUPERDATE", "[履约日期] 不能早于签约日期 ["
								+ contractExguaran.getContractdate() + "] ");
					}
				}else{
					if(!verifyTwoDates(contractExguaran.getTradedate(), sub
							.getGuperdate())){
						map.put("GUPERDATE", "[履约日期] 不能早于签约信息交易日期 ["
								+ contractExguaran.getTradedate() + "] ");
					}
				}
				// 履约币种
				if(StringUtil.isEmpty(sub.getGupercurr())){
					map.put("GUPERCURR", "[履约币种] 不能为空且必须在字典表中存在 ");
				}else if(!verifyDictionaryValue(CURRENCY, sub.getGupercurr())){
					String value = getKey(sub.getGupercurr(), CURRENCY);
					map.put("GUPERCURR", "[履约币种] [" + value + "] 无效.\n");
				}
				// 履约金额
				if(isNull(sub.getGuperamount())){
					map.put("GUPERAMOUNT", "[履约金额] 不能为空 ");
				}else if(sub.getGuperamount().compareTo(new BigDecimal(0.0)) < 0){
					map.put("GUPERAMOUNT", "[履约金额] 应大于等于0 ");
				}else if(sub.getGuperamount().compareTo(
						this.contractExguaran.getGuaranamount()) > 0){
					map.put("GUPERAMOUNT", "[履约金额] 应小于等于保函金额[ "
							+ this.contractExguaran.getGuaranamount() + "] ");
				}
				// 购汇履约金额
				if(isNull(sub.getPguperamount())){
					map.put("PGUPERAMOUNT", "[购汇履约金额]不能为空 ");
				}else if(sub.getPguperamount().compareTo(new BigDecimal(0.0)) < 0){
					map.put("PGUPERAMOUNT", "[购汇履约金额]应大于等于0 ");
				}else if(sub.getPguperamount().compareTo(sub.getGuperamount()) > 0){
					map.put("PGUPERAMOUNT", "[购汇履约金额]应小于等于履约金额[ "
							+ sub.getGuperamount() + "] ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
