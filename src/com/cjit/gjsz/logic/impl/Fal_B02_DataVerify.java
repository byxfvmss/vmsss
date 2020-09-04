package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_B02Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_B02_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_B02_DataVerify() {
	}

	public Fal_B02_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_B02Entity b02 = (Fal_B02Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, b02.getActiontype())) {
					String value = getKey(b02.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(b02.getActiontype())) {
					if (StringUtil.isEmpty(b02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(b02.getActiontype())) {
					if (!isNull(b02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (b02.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// B0201 s,1,1,1 [填报机构身份]不能为空且需在字典表中有定义 INST_IDENTITY
				if (!verifyDictionaryValue(INST_IDENTITY, b02.getB0201(),
						"T_FAL_B02")) {
					String value = getKey(b02.getB0201(), INST_IDENTITY);
					map.put("B0201", "[填报机构身份] [" + value + "] 无效 ");
				} else {
					if ("1".equals(b02.getB0201())) {
						// 当B0201选1-以自身名义投资（无境内托管人、代理人或管理人）
						if (StringUtil.isNotEmpty(b02.getB0202())) {
							map.put("B0202", "[被代理人/委托人所属国家/地区] 应为空 ");
						}
						if (StringUtil.isNotEmpty(b02.getB0203())) {
							map.put("B0203", "[被代理人/委托人所属部门] 应为空 ");
						}
					} else if ("2".equals(b02.getB0201())) {
						// 当B0201选2-是代理人或管理人，以客户或产品名义投资
						if (!COUNTRY_CHN.equals(b02.getB0202())) {
							map.put("B0202", "[被代理人/委托人所属国家/地区] 必须选CHN（中国） ");
						}
						if (!verifyDictionaryValue(INVESTORINST, b02.getB0203())) {
							// B0203 [被代理人/委托人所属部门]不能为空且需在字典表中有定义
							String value = getKey(b02.getB0203(), INVESTORINST);
							map.put("B0203", "[被代理人/委托人所属部门] [" + value
									+ "] 无效 ");
						}
					}
				}
				// B0204 s,1,3,3 [发行地]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, b02.getB0204())) {
					String value = getKey(b02.getB0204(), COUNTRY);
					map.put("B0204", "[发行地] [" + value + "] 无效 ");
				}
				// B0205 s,1,255 [证券代码（逐支报送使用）]不能超过255位字符
				// B0206 s,0,255 [证券发行主体名称（逐支报送使用）]不能超过255位字符
				if ("N/A".equals(b02.getB0205())
						&& StringUtil.isNotEmpty(b02.getB0206())) {
					map.put("B0206", "[证券发行主体名称（逐支报送使用）] 应为空 ");
				} else if (!"N/A".equals(b02.getB0205())
						&& StringUtil.isEmpty(b02.getB0206())) {
					map.put("B0206", "[证券发行主体名称（逐支报送使用）] 应填写实际名称 ");
				}
				// B0207 s,1,3,3 [发行主体所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, b02.getB0207())) {
					String value = getKey(b02.getB0207(), COUNTRY);
					map.put("B0207", "[发行主体所属国家/地区] [" + value + "] 无效 ");
				}
				// B0208 s,1,1,1 [发行主体所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, b02.getB0208())) {
					String value = getKey(b02.getB0208(), INVESTORINST);
					map.put("B0208", "[发行主体所属部门] [" + value + "] 无效 ");
				}
				// B0209 s,1,1,1 [发行主体与本机构的关系]不能为空且需在字典表中有定义 OPPOSITERELA
				if (!verifyDictionaryValue(OPPOSITERELA, b02.getB0209())) {
					String value = getKey(b02.getB0209(), OPPOSITERELA);
					map.put("B0209", "[发行主体与本机构的关系] [" + value + "] 无效 ");
				}
				// B0210 s,1,1,1 [原始期限]不能为空且需在字典表中有定义 ORIDEADLINE
				if (!verifyDictionaryValue(ORIDEADLINE, b02.getB0210())) {
					String value = getKey(b02.getB0210(), ORIDEADLINE);
					map.put("B0210", "[原始期限] [" + value + "] 无效 ");
				}
				// B0211 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, b02.getB0211())) {
					String value = getKey(b02.getB0211(), CURRENCY);
					map.put("B0211", "[原始币种] [" + value + "] 无效 ");
				}
				// B0212 n,1,24,2 [上月末市值]不能为空，必须≥0
				if (b02.getB0212() == null
						|| b02.getB0212().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0212", "[上月末市值] 不能为空且应当大于等于0 ");
				}
				// B0213 n,1,24,2 [本月买入金额]不能为空，必须≥0
				if (b02.getB0213() == null
						|| b02.getB0213().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0213", "[本月买入金额] 不能为空且应当大于等于0 ");
				}
				// B0214 n,1,24,2 [本月卖出金额]不能为空，必须≥0
				if (b02.getB0214() == null
						|| b02.getB0214().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0214", "[本月卖出金额] 不能为空且应当大于等于0 ");
				}
				// B0215 n,1,24,2 [本月非交易变动]不能为空
				if (b02.getB0215() == null) {
					map.put("B0215", "[本月非交易变动] 不能为空 ");
				} else {
					// B0215=B0218-B0212-（B0213-B0214）
					if (b02.getB0218() != null && b02.getB0212() != null
							&& b02.getB0213() != null && b02.getB0214() != null) {
						BigDecimal d1 = b02.getB0218().subtract(b02.getB0212())
								.subtract(b02.getB0213()).add(b02.getB0214());
						if (d1 != null && b02.getB0215().compareTo(d1) != 0) {
							map
									.put("B0215",
											"[本月非交易变动] 应满足B0215=B0218-B0212-（B0213-B0214）  ");
						}
					}
					// B0215=B0216+B0217
					if (b02.getB0216() != null && b02.getB0217() != null) {
						BigDecimal d2 = b02.getB0216().add(b02.getB0217());
						if (d2 != null && b02.getB0215().compareTo(d2) != 0) {
							map.put("B0215", "[本月非交易变动] 应满足B0215=B0216+B0217 ");
						}
					}
				}
				// B0216 n,1,24,2 [本月非交易变动：其中注销、调整或重新分类至其他报表统计的金额]不能为空
				if (b02.getB0216() == null) {
					map.put("B0216", "[本月非交易变动：其中注销、调整或重新分类至其他报表统计的金额] 不能为空 ");
				}
				// B0217 n,1,24,2 [本月非交易变动：其中价值重估因素]不能为空
				if (b02.getB0217() == null) {
					map.put("B0217", "[本月非交易变动：其中价值重估因素] 不能为空 ");
				}
				// B0218 n,1,24,2 [本月末市值]不能为空，必须≥0
				if (b02.getB0218() == null
						|| b02.getB0218().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0218", "[本月末市值] 不能为空且应当大于等于0 ");
				}
				// B0219 n,1,24,2 [本月末市值中的其中：剩余期限在一年及以下]不能为空，必须≥0
				if (b02.getB0219() == null
						|| b02.getB0219().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0219", "[本月末市值中的其中：剩余期限在一年及以下] 不能为空且应当大于等于0 ");
				} else {
					if ("1".equals(b02.getB0210())
							&& b02.getB0219().compareTo(b02.getB0218()) != 0) {
						map.put("B0219",
								"[本月末市值中的其中：剩余期限在一年及以下] 应等于B0218本月末市值 ");
					} else if ("2".equals(b02.getB0210())
							&& b02.getB0219().compareTo(b02.getB0218()) > 0) {
						map.put("B0219",
								"[本月末市值中的其中：剩余期限在一年及以下] 应小于等于B0218本月末市值 ");
					}
				}
				// B0220 n,1,24,2 [本月利息收入]不能为空，必须≥0
				if (b02.getB0220() == null
						|| b02.getB0220().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0220", "[本月利息收入] 不能为空且应当大于等于0 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
