package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_Sub_EXPLCURR;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_SUB_EXPLCURR_INFO]质押外汇金额信息表校验类
 */
public class Self_Sub_ExplcurrVerify extends SelfDataVerify implements
		DataVerify{

	public Self_Sub_ExplcurrVerify(){
	}

	public Self_Sub_ExplcurrVerify(List dictionarys, List verifylList){
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
				Self_Sub_EXPLCURR sub = (Self_Sub_EXPLCURR) verifylList.get(i);
				if(isNull(sub.getExplcurr())){
					map.put("EXPLCURR", "[质押外汇币种]不能为空且必须在字典表中存在 ");
				}else if(!verifyDictionaryValue(CURRENCY, sub.getExplcurr())){
					String value = getKey(sub.getExplcurr(), CURRENCY);
					map.put("EXPLCURR", "[质押外汇币种] [" + value + "] 无效 ");
				}
				if(isNull(sub.getExplamount())
						|| sub.getExplamount()
								.compareTo(new BigDecimal("0.00")) < 0){
					map.put("EXPLAMOUNT", "[质押外汇金额]不能为空且大于等于0 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
