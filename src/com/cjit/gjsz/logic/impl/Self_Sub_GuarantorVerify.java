package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_Sub_GUARANTOR;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_SUB_GUARANTOR_INFO]被担保人信息表校验类
 */
public class Self_Sub_GuarantorVerify extends SelfDataVerify implements
		DataVerify{

	public Self_Sub_GuarantorVerify(){
	}

	public Self_Sub_GuarantorVerify(List dictionarys, List verifylList){
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
				Self_Sub_GUARANTOR sub = (Self_Sub_GUARANTOR) verifylList
						.get(i);
				if(isNull(sub.getGuedname()) && isNull(sub.getGuednamen())){
					map.put("GUEDNAME", "[被担保人中文名称] 被担保人中英文名称至少填写一个 ");
					map.put("GUEDNAMEN", "[被担保人英文名称] 被担保人中英文名称至少填写一个 ");
				}
				if(StringUtil.isEmpty(sub.getGuedtype())){
					map.put("GUEDTYPE", "[被担保人类型] 不能为空且必须在字典表中存在 ");
				}else if(!verifyDictionaryValue(MAINBODYTYPE, sub.getGuedtype())){
					String value = getKey(sub.getGuedtype(), MAINBODYTYPE);
					map.put("GUEDTYPE", "[被担保人类型] [" + value + "] 无效.\n");
				}else{
					// 境外(20130909修改为限制在境外银行类金融机构)
					if(verifyOverseaFinanceOrg(sub.getGuedtype(), true)){
						// 如果被担保人为境外金融机构，应填写8位或11位的SWIFT CODE；
						if(isNull(sub.getGuedcode())
								|| (sub.getGuedcode().length() != 8 && sub
										.getGuedcode().length() != 11)){
							map
									.put("GUEDCODE",
											"[被担保人代码]应填写8位或11位的SWIFT CODE ");
						}
					}
					// 境内
					else if(sub.getGuedtype().startsWith("10")
							&& isNull(sub.getGuedcode())){
						// 如果被担保人为境内个人，应填写被担保人个人身份证件号码。
						if("10130000".equals(sub.getGuedtype())){
							map.put("GUEDCODE", "[被担保人代码]应填写被担保人个人身份证件号码 ");
						}
						// 如果被担保人为境内机构，应填写国家质量监督检验检疫总局向被担保人颁发的组织机构代码证上的组织机构代码；
						else{
							map
									.put("GUEDCODE",
											"[被担保人代码]应填写国家质量监督检验检疫总局向被担保人颁发的组织机构代码证上的组织机构代码 ");
						}
					}
				}
				if(StringUtil.isNotEmpty(sub.getGuedcouncode())
						&& !verifyDictionaryValue(COUNTRY, sub
								.getGuedcouncode())){
					String value = getKey(sub.getGuedcouncode(), COUNTRY);
					map.put("GUEDCOUNCODE", "[被担保人国别/地区代码] [" + value
							+ "] 无效.\n");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
