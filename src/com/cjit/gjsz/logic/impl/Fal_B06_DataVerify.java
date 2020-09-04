package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_B06Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_B06_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_B06_DataVerify() {
	}

	public Fal_B06_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_B06Entity b06 = (Fal_B06Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, b06.getActiontype())) {
					String value = getKey(b06.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(b06.getActiontype())) {
					if (StringUtil.isEmpty(b06.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(b06.getActiontype())) {
					if (!isNull(b06.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (b06.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// B0601TYPE s,1,1 [非居民投资者属性]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVESTOR_TYPE, b06.getB0601type())) {
					String value = getKey(b06.getB0601type(), INVESTOR_TYPE);
					map.put("B0601TYPE", "[非居民投资者属性] [" + value + "] 无效 ");
				}
				// B0601 s,1,255 [非居民投资者名称]不能为空且不能超过256位字符
				// B0602 s,1,3,3 [非居民投资者所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, b06.getB0602())) {
					String value = getKey(b06.getB0602(), COUNTRY);
					map.put("B0602", "[非居民投资者所属国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(b06.getB0602())) {
					map.put("B0602", "[非居民投资者所属国家/地区] 不能为CHN中国 ");
				}
				// B0603 s,1,1,1 [非居民投资者所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, b06.getB0603())) {
					String value = getKey(b06.getB0603(), INVESTORINST);
					map.put("B0603", "[非居民投资者所属部门] [" + value + "] 无效 ");
				}
				// B0604 s,1,1,1 [投资产品类型]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVEST_NON_RESIDENT_ISSUE_PROD, b06
						.getB0604())) {
					String value = getKey(b06.getB0604(),
							INVEST_NON_RESIDENT_ISSUE_PROD);
					map.put("B0604", "[投资产品类型] [" + value + "] 无效 ");
				}
				// B0605 s,1,255 [投资产品代码（逐支报送使用）]不能为空且不能超过256位字符
				// B0606 s,0,255 [发行主体名称（逐支报送使用）]不能超过256位字符
				if ("N/A".equals(b06.getB0605())
						&& StringUtil.isNotEmpty(b06.getB0606())) {
					map.put("B0606", "[发行主体名称（逐支报送使用）] 应为空 ");
				}
				// B0607 s,1,3,3 [发行主体所属国家/地区（必须为中国）]不能为空且需在字典表中有定义 COUNTRY
				if (!COUNTRY_CHN.equals(b06.getB0607())) {
					map.put("B0607", "[发行主体所属国家/地区（必须为中国）] 必须为CHN中国 ");
				}
				// B0608 s,1,1,1 [发行主体所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, b06.getB0608())) {
					String value = getKey(b06.getB0608(), INVESTORINST);
					map.put("B0608", "[发行主体所属部门] [" + value + "] 无效 ");
				}
				// B0609 s,0,1,1 [债务证券原始期限]不能为空且需在字典表中有定义 ORIDEADLINE
				if (("1".equals(b06.getB0604()) || "2".equals(b06.getB0604()))
						&& StringUtil.isNotEmpty(b06.getB0609())) {
					map.put("B0609", "[债务证券原始期限] 应为空 ");
				} else if ("3".equals(b06.getB0604())
						&& !verifyDictionaryValue(ORIDEADLINE, b06.getB0609())) {
					String value = getKey(b06.getB0609(), ORIDEADLINE);
					map.put("B0609", "[债务证券原始期限] [" + value + "] 无效 ");
				}
				// B0610 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, b06.getB0610())) {
					String value = getKey(b06.getB0610(), CURRENCY);
					map.put("B0610", "[原始币种] [" + value + "] 无效 ");
				}
				// B0611 n,1,24,2 [上月末市值]不能为空，必须≥0
				if (b06.getB0611() == null
						|| b06.getB0611().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0611", "[上月末市值] 不能为空且应当大于等于0 ");
				}
				// B0612 n,1,24,2 [本月买入金额]不能为空，必须≥0
				if (b06.getB0612() == null
						|| b06.getB0612().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0612", "[本月买入金额] 不能为空且应当大于等于0 ");
				}
				// B0613 n,1,24,2 [本月卖出金额]不能为空，必须≥0
				if (b06.getB0613() == null
						|| b06.getB0613().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0613", "[本月卖出金额] 不能为空且应当大于等于0 ");
				}
				// B0614 n,1,24,2 [本月非交易变动]不能为空
				if (b06.getB0614() == null) {
					map.put("B0614", "[本月非交易变动] 不能为空 ");
				} else {
					// B0614=B0617-B0611-（B0612-B0613）
					if (b06.getB0617() != null && b06.getB0611() != null
							&& b06.getB0612() != null && b06.getB0613() != null) {
						BigDecimal d = b06.getB0617().subtract(b06.getB0611())
								.subtract(b06.getB0612()).add(b06.getB0613());
						if (b06.getB0614().compareTo(d) != 0) {
							map
									.put("B0614",
											"[本月非交易变动] 应满足B0614=B0617-B0611-（B0612-B0613） ");
						}
					}
					// B0614=B0615+B0616
					if (b06.getB0615() != null && b06.getB0616() != null) {
						BigDecimal d = b06.getB0615().add(b06.getB0616());
						if (b06.getB0614().compareTo(d) != 0) {
							map.put("B0614", "[本月非交易变动] 应满足B0614=B0615+B0616 ");
						}
					}
				}
				// B0615 n,1,24,2 [本月非交易变动：其中债权注销金额]不能为空
				if (b06.getB0615() == null) {
					map.put("B0615", "[本月非交易变动：其中债权注销金额] 不能为空 ");
				}
				// B0616 n,1,24,2 [本月非交易变动：其中价值重估因素]不能为空
				if (b06.getB0616() == null) {
					map.put("B0616", "[本月非交易变动：其中价值重估因素] 不能为空 ");
				}
				// B0617 n,1,24,2 [本月末市值]不能为空，必须≥0
				if (b06.getB0617() == null
						|| b06.getB0617().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0617", "[本月末市值] 不能为空且应当大于等于0 ");
				}
				// B0618 n,1,24,2 [本月末市值：其中剩余期限在一年及以下]不能为空
				if (b06.getB0618() == null) {
					map.put("B0618", "[本月末市值：其中剩余期限在一年及以下] 不能为空 ");
				} else if (("1".equals(b06.getB0604()) || "2".equals(b06
						.getB0604()))
						&& b06.getB0618().compareTo(new BigDecimal(0)) != 0) {
					map.put("B0618", "[本月末市值：其中剩余期限在一年及以下] 应当等于0 ");
				} else if ("3".equals(b06.getB0604())) {
					if (b06.getB0618().compareTo(new BigDecimal(0)) < 0) {
						map.put("B0618", "[本月末市值：其中剩余期限在一年及以下] 应当大于等于0 ");
					}
					if ("1".equals(b06.getB0609())
							&& b06.getB0618().compareTo(b06.getB0617()) != 0) {
						map
								.put("B0618",
										"[本月末市值：其中剩余期限在一年及以下] 应当等于B0617本月末市值 ");
					} else if ("2".equals(b06.getB0609())
							&& b06.getB0618().compareTo(b06.getB0617()) > 0) {
						map.put("B0618",
								"[本月末市值：其中剩余期限在一年及以下] 应当小于等于B0617本月末市值 ");
					}
				}
				// B0619 n,1,24,2 [本月非居民投资者红利或利息收入]不能为空，必须≥0
				if (b06.getB0619() == null
						|| b06.getB0619().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0619", "[本月非居民投资者红利或利息收入] 不能为空且应当大于等于0 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
