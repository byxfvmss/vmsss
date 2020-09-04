package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_B03Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_B03_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_B03_DataVerify() {
	}

	public Fal_B03_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_B03Entity b03 = (Fal_B03Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, b03.getActiontype())) {
					String value = getKey(b03.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(b03.getActiontype())) {
					if (StringUtil.isEmpty(b03.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(b03.getActiontype())) {
					if (!isNull(b03.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (b03.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// B0301 s,1,3,3 [投资者所属国家/地区（必须为中国）]不能为空且需在字典表中有定义 COUNTRY
				if (!COUNTRY_CHN.equals(b03.getB0301())) {
					map.put("B0301", "[投资者所属国家/地区（必须为中国）] 必须为中国 ");
				}
				// B0302 s,1,1,1 [投资者所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, b03.getB0302())) {
					String value = getKey(b03.getB0302(), INVESTORINST);
					map.put("B0302", "[投资者所属部门] [" + value + "] 无效 ");
				}
				// B0303 s,1,1,1 [所投资非居民发行产品类型]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVEST_NON_RESIDENT_ISSUE_PROD, b03
						.getB0303())) {
					String value = getKey(b03.getB0303(),
							INVEST_NON_RESIDENT_ISSUE_PROD);
					map.put("B0303", "[所投资非居民发行产品类型] [" + value + "] 无效 ");
				}
				// B0304 s,1,255 [所投资产品代码（逐支报送使用）]不能为空且不能超过255位字符
				// B0305 s,0,255 [非居民发行主体名称（逐支报送使用）]不能超过255位字符
				if ("N/A".equals(b03.getB0304())
						&& StringUtil.isNotEmpty(b03.getB0305())) {
					map.put("B0305", "[非居民发行主体名称（逐支报送使用）] 应为空 ");
				}
				// B0306 s,1,3,3 [非居民发行主体所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, b03.getB0306())) {
					String value = getKey(b03.getB0306(), COUNTRY);
					map.put("B0306", "[非居民发行主体所属国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(b03.getB0306())) {
					map.put("B0306", "[非居民发行主体所属国家/地区] 不能是CHN ");
				}
				// B0307 s,1,1,1 [非居民发行主体所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, b03.getB0307())) {
					String value = getKey(b03.getB0307(), INVESTORINST);
					map.put("B0307", "[非居民发行主体所属部门] [" + value + "] 无效 ");
				}
				// B0308 s,0,1 [债务证券原始期限]需在字典表中有定义 ORIDEADLINE
				if ("1".equals(b03.getB0303()) || "2".equals(b03.getB0303())) {
					if (StringUtil.isNotEmpty(b03.getB0308())) {
						map.put("B0308", "[债务证券原始期限] 当B0303=1或2时，本字段应为空 ");
					}
				} else if ("3".equals(b03.getB0303())) {
					if (StringUtil.isEmpty(b03.getB0308())) {
						map.put("B0308", "[债务证券原始期限] 当B0303=3时，本字段不能为空 ");
					} else if (!verifyDictionaryValue(ORIDEADLINE, b03
							.getB0308())) {
						String value = getKey(b03.getB0308(), ORIDEADLINE);
						map.put("B0308", "[债务证券原始期限] [" + value + "] 无效 ");
					}
				}
				// B0309 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, b03.getB0309())) {
					String value = getKey(b03.getB0309(), CURRENCY);
					map.put("B0309", "[原始币种] [" + value + "] 无效 ");
				}
				// B0310 n,1,24,2 [上月末市值]不能为空，必须≥0
				if (b03.getB0310() == null
						|| b03.getB0310().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0310", "[上月末市值] 不能为空且应当大于等于0 ");
				}
				// B0311 n,1,24,2 [本月买入（申购）金额]不能为空，必须≥0
				if (b03.getB0311() == null
						|| b03.getB0311().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0311", "[本月买入（申购）金额] 不能为空且应当大于等于0 ");
				}
				// B0312 n,1,24,2 [本月卖出（赎回）金额]不能为空，必须≥0
				if (b03.getB0312() == null
						|| b03.getB0312().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0312", "[本月卖出（赎回）金额] 不能为空且应当大于等于0 ");
				}
				// B0313 n,1,24,2 [本月非交易变动]不能为空
				if (b03.getB0313() == null) {
					map.put("B0313", "[本月非交易变动] 不能为空 ");
				} else {
					// B0313=B0316-B0310-（B0311-B0312）
					if (b03.getB0316() != null && b03.getB0310() != null
							&& b03.getB0311() != null && b03.getB0312() != null) {
						BigDecimal d = b03.getB0316().subtract(b03.getB0310())
								.subtract(b03.getB0311()).add(b03.getB0312());
						if (d.compareTo(b03.getB0313()) != 0) {
							map
									.put("B0313",
											"[本月非交易变动] 应满足B0313=B0316-B0310-（B0311-B0312）  ");
						}
					}
					// B0313=B0314+B0315
					if (b03.getB0314() != null && b03.getB0315() != null) {
						BigDecimal d = b03.getB0314().add(b03.getB0315());
						if (d.compareTo(b03.getB0313()) != 0) {
							map
									.put("B0313",
											"[本月非交易变动] 应满足B0313==B0314+B0315 ");
						}
					}
				}
				// B0314 n,1,24,2 [本月非交易变动：其中债权注销金额]不能为空
				if (b03.getB0314() == null) {
					map.put("B0314", "[本月非交易变动：其中债权注销金额] 不能为空 ");
				}
				// B0315 n,1,24,2 [本月非交易变动：其中价值重估因素]不能为空
				if (b03.getB0315() == null) {
					map.put("B0315", "[本月非交易变动：其中价值重估因素] 不能为空 ");
				}
				// B0316 n,1,24,2 [本月末市值]不能为空，必须≥0
				if (b03.getB0316() == null
						|| b03.getB0316().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0316", "[本月末市值] 不能为空且应当大于等于0 ");
				}
				// B0317 n,1,24,2 [本月末市值：其中剩余期限在一年及以下]不能为空
				if (b03.getB0317() == null) {
					map.put("B0317", "[本月末市值：其中剩余期限在一年及以下] 不能为空 ");
				} else {
					if ("1".equals(b03.getB0303())
							|| "2".equals(b03.getB0303())) {
						if (b03.getB0317().compareTo(new BigDecimal(0)) != 0) {
							map.put("B0317",
									"[本月末市值：其中剩余期限在一年及以下] 当B0303=1或2时，本项应为0 ");
						}
					} else if ("3".equals(b03.getB0303())) {
						if (b03.getB0317().compareTo(new BigDecimal(0)) < 0) {
							map
									.put("B0317",
											"[本月末市值：其中剩余期限在一年及以下] 当B0303=3时，本项应当大于等于0 ");
						} else if ("1".equals(b03.getB0308())
								&& b03.getB0317().compareTo(b03.getB0316()) != 0) {
							map
									.put("B0317",
											"[本月末市值：其中剩余期限在一年及以下] 当B0303=3且B0308=1时，本项应等于B0316本月末市值 ");
						} else if ("2".equals(b03.getB0308())
								&& b03.getB0317().compareTo(b03.getB0316()) > 0) {
							map
									.put("B0317",
											"[本月末市值：其中剩余期限在一年及以下] 当B0303=3且B0308=2时，本项应小于等于B0316本月末市值 ");
						}
					}
				}
				// B0318 n,1,24,2 [本月投资者红利或利息收入]不能为空，必须≥0
				if (b03.getB0318() == null
						|| b03.getB0318().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0318", "[本月投资者红利或利息收入] 不能为空且应当大于等于0 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
