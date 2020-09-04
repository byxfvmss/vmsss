package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_Sub_BENEFICIARY;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_SUB_BENEFICIARY_INFO]受益人信息表校验类
 */
public class Self_Sub_BeneficiaryVerify extends SelfDataVerify implements
		DataVerify{

	public Self_Sub_BeneficiaryVerify(){
	}

	public Self_Sub_BeneficiaryVerify(List dictionarys, List verifylList){
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
				Self_Sub_BENEFICIARY sub = (Self_Sub_BENEFICIARY) verifylList
						.get(i);
				if(isNull(sub.getBename()) && isNull(sub.getBenamen())){
					map.put("BENAME", "[受益人中英文名称]至少填写一个 ");
					map.put("BENAMEN", "[受益人中英文名称]至少填写一个 ");
				}
				if(StringUtil.isEmpty(sub.getBentype())){
					map.put("BENTYPE", "[受益人类型]不能为空且必须在字典表中存在 ");
				}else if(!verifyDictionaryValue(MAINBODYTYPE, sub.getBentype())){
					String value = getKey(sub.getBentype(), MAINBODYTYPE);
					map.put("BENTYPE", "[受益人类型] [" + value + "] 无效.\n");
				}else{
					// 境外(20130909修改为限制在境外银行类金融机构)
					if(verifyOverseaFinanceOrg(sub.getBentype(), true)){
						// 如果受益人为境外金融机构，应填写8位或11位的SWIFT CODE；
						if(isNull(sub.getBencode())
								|| (sub.getBencode().length() != 8 && sub
										.getBencode().length() != 11)){
							map.put("BENCODE", "[受益人代码]应填写8位或11位的SWIFT CODE ");
						}
					}
					// 境内
					else if(sub.getBentype().startsWith("10")
							&& isNull(sub.getBencode())){
						// 如果受益人为境内个人，应填写受益人个人身份证件号码。
						if("10130000".equals(sub.getBentype())){
							map.put("BENCODE", "[受益人代码]应填写受益人个人身份证件号码 ");
						}
						// 如果受益人为境内机构，应填写国家质量监督检验检疫总局向受益人颁发的组织机构代码证上的组织机构代码；
						else{
							map
									.put("BENCODE",
											"[受益人代码]应填写国家质量监督检验检疫总局向受益人颁发的组织机构代码证上的组织机构代码 ");
						}
					}
				}
				if(StringUtil.isEmpty(sub.getBencountrycode())){
					map.put("BENCOUNTRYCODE", "[受益人国别/地区]不能为空且必须在字典表中存在 ");
				}else if(!verifyDictionaryValue(COUNTRY, sub
						.getBencountrycode())){
					String value = getKey(sub.getBencountrycode(), COUNTRY);
					map.put("BENCOUNTRYCODE", "[受益人国别/地区] [" + value
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
