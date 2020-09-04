package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_Sub_FOGUARANTOR;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_SUB_FOGUARANTOR_INFO]境外担保人信息表校验类
 */
public class Self_Sub_FoguarantorVerify extends SelfDataVerify implements
		DataVerify{

	public Self_Sub_FoguarantorVerify(){
	}

	public Self_Sub_FoguarantorVerify(List dictionarys, List verifylList){
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
				Self_Sub_FOGUARANTOR sub = (Self_Sub_FOGUARANTOR) verifylList
						.get(i);
				if(isNull(sub.getFoguname()) && isNull(sub.getFogunamen())){
					map.put("FOGUNAME", "[境外担保人中英文名称]至少填写一个 ");
					map.put("FOGUNAMEN", "[境外担保人中英文名称]至少填写一个 ");
				}
				if(isNull(sub.getFogurecode())){
					map.put("FOGURECODE", "[境外担保人注册地国家/地区代码]不能为空且必须在字典表中存在 ");
				}else if(!verifyDictionaryValue(COUNTRY, sub.getFogurecode())){
					String value = getKey(sub.getFogurecode(), COUNTRY);
					map.put("FOGURECODE", "[境外担保人注册地国家/地区代码] [" + value
							+ "] 无效. ");
				}
				if(isNull(sub.getGuaranteetype())){
					map.put("GUARANTEETYPE", "[担保方式]不能为空且必须在字典表中存在 ");
				}else if(!verifyDictionaryValue(GUARANTEETYPE, sub
						.getGuaranteetype())){
					String value = getKey(sub.getGuaranteetype(), GUARANTEETYPE);
					map.put("GUARANTEETYPE", "[担保方式] [" + value + "] 无效. ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
