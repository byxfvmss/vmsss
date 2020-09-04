package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_Sub_EXPLBALA;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_SUB_EXPLBALA_INFO]质押外汇余额信息表校验类
 */
public class Self_Sub_ExplbalaVerify extends SelfDataVerify implements
		DataVerify{

	public Self_Sub_ExplbalaVerify(){
	}

	public Self_Sub_ExplbalaVerify(List dictionarys, List verifylList){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
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
				Self_Sub_EXPLBALA sub = (Self_Sub_EXPLBALA) verifylList.get(i);
				// EXPLCURR 质押外汇币种 字符型，3 必填项，见币种代码表
				if(isNull(sub.getExplcurr())){
					map.put("EXPLCURR", "[质押外汇币种]不能为空且必须在字典表中存在 ");
				}else if(!verifyDictionaryValue(CURRENCY, sub.getExplcurr())){
					String value = getKey(sub.getExplcurr(), CURRENCY);
					map.put("EXPLCURR", "[质押外汇币种] [" + value + "] 无效 ");
				}
				// EXPLBALA 质押外汇余额 数值型，22.2 必填项，大于等于0。
				if(isNull(sub.getExplbala())
						|| sub.getExplbala().compareTo(new BigDecimal("0.00")) < 0){
					map.put("EXPLBALA", "[质押外汇余额]不能为空且大于等于0 ");
				}
				// EXPLPERAMOUNT 质押外汇履约金额 数值型，22.2 非必填项，大于等于0。
				if(!isNull(sub.getExplperamount())
						&& sub.getExplperamount().compareTo(
								new BigDecimal("0.00")) < 0){
					map.put("EXPLPERAMOUNT", "[质押外汇履约金额] 应大于等于0 ");
				}
				// PLCOSEAMOUNT 质押履约结汇金额 数值型，22.2 非必填项，大于等于0。
				if(!isNull(sub.getPlcoseamount())
						&& sub.getPlcoseamount().compareTo(
								new BigDecimal("0.00")) < 0){
					map.put("PLCOSEAMOUNT", "[质押履约结汇金额] 应大于等于0 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}