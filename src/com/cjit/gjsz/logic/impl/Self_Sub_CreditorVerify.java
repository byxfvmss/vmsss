package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_A_EXDEBT;
import com.cjit.gjsz.logic.model.Self_Sub_CREDITOR;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_SUB_CREDITOR_INFO]债权人信息表校验类
 */
public class Self_Sub_CreditorVerify extends SelfDataVerify implements
		DataVerify{

	private Self_A_EXDEBT exdebt;

	public Self_Sub_CreditorVerify(){
	}

	public Self_Sub_CreditorVerify(List dictionarys, List verifylList,
			Self_A_EXDEBT exdebt){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
		this.exdebt = exdebt;
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
				Self_Sub_CREDITOR sub = (Self_Sub_CREDITOR) verifylList.get(i);
				// 债权人代码
				// 如果债权人为银行，应填写8位或11位的SWIFTCODE；
				// 如果债权人类型为“政府”、或“中央银行”时，或者当债务类型为“货币市场工具”或“债券和票据”或“非居民个人存款”时，应在外汇局编制的部分债权人代码表中选择债权人代码；
				// 否则为空。
				// 如果债权人为“非银行金融机构”或“国际金融组织”，应优先填写SWIFT CODE。
				if(!isNull(sub.getCreditortype())){
					if("20001401".equals(sub.getCreditortype())
							|| "20001402".equals(sub.getCreditortype())
							|| "20001403".equals(sub.getCreditortype())){
						if(isNull(sub.getCreditorcode())
								|| (sub.getCreditorcode().length() != 8 && sub
										.getCreditorcode().length() != 11)){
							map.put("CREDITORCODE",
									"[债权人代码] 债权人为银行，应填写8位或11位的SWIFT CODE ");
						}
					}else if("20001100".equals(sub.getCreditortype())
							|| "20001300".equals(sub.getCreditortype())
							|| "1201".equals(exdebt.getDebtype())
							|| "1202".equals(exdebt.getDebtype())
							|| "1304".equals(exdebt.getDebtype())){
						if(isNull(sub.getCreditorcode())){
							map
									.put(
											"CREDITORCODE",
											"[债权人代码] 债权人类型为“政府”、或“中央银行”时，或者当债务类型为“货币市场工具”或“债券和票据”或“非居民个人存款”时，应在外汇局编制的部分债权人代码表中选择债权人代码 ");
						}
					}else if("20001501".equals(sub.getCreditortype())
							|| "20001502".equals(sub.getCreditortype())
							|| "20001200".equals(sub.getCreditortype())){
						if(isNull(sub.getCreditorcode())){
							map
									.put("CREDITORCODE",
											"[债权人代码] 债权人为“非银行金融机构”或“国际金融组织”，应优先填写SWIFT CODE ");
						}
					}else{
						if(!isNull(sub.getCreditorcode())){
							map.put("CREDITORCODE", "[债权人代码] 应为空 ");
						}
					}
				}
				if(isNull(sub.getCreditorname())
						&& isNull(sub.getCreditornamen())){
					map.put("CREDITORNAME", "[中文名称] 中文名称和英文名称至少填写一个 ");
				}
				if(isNull(sub.getCreditorca())
						|| sub.getCreditorca()
								.compareTo(new BigDecimal("0.00")) < 0){
					map.put("CREDITORCA", "[债权人签约金额] 不能为空且大于等于0 ");
				}
				if(isNull(sub.getCreditortype())){
					map.put("CREDITORTYPE", "[类型代码] 不能为空 ");
				}else if(!verifyDictionaryValue(MAINBODYTYPE, sub
						.getCreditortype())){
					String value = getKey(sub.getCreditortype(), MAINBODYTYPE);
					map.put("CREDITORTYPE", "[类型代码] [" + value + "] 无效.\n");
				}
				if(isNull(sub.getCrehqcode())){
					map.put("CREHQCODE", "[总部所在国家（地区）代码] 不能为空 ");
				}else if(!verifyDictionaryValue(COUNTRY, sub.getCrehqcode())){
					String value = getKey(sub.getCrehqcode(), COUNTRY);
					map
							.put("CREHQCODE", "[总部所在国家（地区）代码] [" + value
									+ "] 无效.\n");
				}
				if(isNull(sub.getOpercode())){
					map.put("OPERCODE", "[债权人经营地所在国家（地区）代码] 不能为空 ");
				}else if(!verifyDictionaryValue(COUNTRY, sub.getOpercode())){
					String value = getKey(sub.getOpercode(), COUNTRY);
					map.put("OPERCODE", "[债权人经营地所在国家（地区）代码] [" + value
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
