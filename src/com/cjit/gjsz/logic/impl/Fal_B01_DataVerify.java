package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_B01Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_B01_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_B01_DataVerify() {
	}

	public Fal_B01_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_B01Entity b01 = (Fal_B01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, b01.getActiontype())) {
					String value = getKey(b01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(b01.getActiontype())) {
					if (StringUtil.isEmpty(b01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(b01.getActiontype())) {
					if (!isNull(b01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (b01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// B0101 [填报机构身份]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INST_IDENTITY, b01.getB0101(),
						"T_FAL_B01")) {
					String value = getKey(b01.getB0101(), INST_IDENTITY);
					map.put("B0101", "[填报机构身份] [" + value + "] 无效 ");
				} else {
					if ("1".equals(b01.getB0101())) {
						// 当B0101选1-以自身名义投资（无境内托管人、代理人或管理人）
						if (StringUtil.isNotEmpty(b01.getB0102())) {
							map.put("B0102", "[被代理人/委托人所属国家/地区] 应为空 ");
						}
						if (StringUtil.isNotEmpty(b01.getB0103())) {
							map.put("B0103", "[被代理人/委托人所属部门] 应为空 ");
						}
					} else if ("2".equals(b01.getB0101())) {
						// 当B0101选2-是代理人或管理人，以客户或产品名义投资
						if (!COUNTRY_CHN.equals(b01.getB0102())) {
							map.put("B0102", "[被代理人/委托人所属国家/地区] 必须选CHN（中国） ");
						}
						if (!verifyDictionaryValue(INVESTORINST, b01.getB0103())) {
							// B0103 [被代理人/委托人所属部门]不能为空且需在字典表中有定义
							String value = getKey(b01.getB0103(), INVESTORINST);
							map.put("B0103", "[被代理人/委托人所属部门] [" + value
									+ "] 无效 ");
						}
					}
				}
				// B0104 [投资类型]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVESTTYPE, b01.getB0104())) {
					String value = getKey(b01.getB0104(), INVESTTYPE);
					map.put("B0104", "[投资类型] [" + value + "] 无效 ");
				}
				// B0105 [发行地]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(COUNTRY, b01.getB0105())) {
					String value = getKey(b01.getB0105(), COUNTRY);
					map.put("B0105", "[发行地] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(b01.getB0105())) {
					map.put("B0105", "[发行地] 不可选CHN（中国） ");
				}
				// B0106 [证券代码（逐支报送使用）]不能超过255位字符
				// B0107 [证券发行主体名称（逐支报送使用）]不能超过255位字符
				if ("N/A".equals(b01.getB0106())
						&& StringUtil.isNotEmpty(b01.getB0107())) {
					map.put("B0107", "[证券发行主体名称（逐支报送使用）] 应为空 ");
				} else if (!"N/A".equals(b01.getB0106())
						&& StringUtil.isEmpty(b01.getB0107())) {
					map.put("B0107", "[证券发行主体名称（逐支报送使用）] 应填写实际名称 ");
				}
				// B0108 [发行主体所属国家/地区]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(COUNTRY, b01.getB0108())) {
					String value = getKey(b01.getB0108(), COUNTRY);
					map.put("B0108", "[发行主体所属国家/地区] [" + value + "] 无效 ");
				}
				// B0109 [发行主体所属部门]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVESTORINST, b01.getB0109())) {
					String value = getKey(b01.getB0109(), INVESTORINST);
					map.put("B0109", "[发行主体所属部门] [" + value + "] 无效 ");
				}
				// B0110 [发行主体与本机构的关系]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(OPPOSITERELA, b01.getB0110())) {
					String value = getKey(b01.getB0110(), OPPOSITERELA);
					map.put("B0110", "[发行主体与本机构的关系] [" + value + "] 无效 ");
				}
				// B0111 [原始币种]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(CURRENCY, b01.getB0111())) {
					String value = getKey(b01.getB0111(), CURRENCY);
					map.put("B0111", "[原始币种] [" + value + "] 无效 ");
				}
				// B0112 [上月末市值]不能为空，必须≥0
				if (b01.getB0112() == null
						|| b01.getB0112().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0112", "[上月末市值] 不能为空且应当大于等于0 ");
				}
				// B0113 [本月买入金额]不能为空，必须≥0
				if (b01.getB0113() == null
						|| b01.getB0113().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0113", "[本月买入金额] 不能为空且应当大于等于0 ");
				}
				// B0114 [本月卖出金额]不能为空，必须≥0
				if (b01.getB0114() == null
						|| b01.getB0114().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0114", "[本月卖出金额] 不能为空且应当大于等于0 ");
				}
				// B0115 [本月非交易变动]不能为空
				if (b01.getB0115() == null) {
					map.put("B0115", "[本月非交易变动] 不能为空 ");
				} else {
					// B0115=B0118-B0112-（B0113-B0114）。
					if (b01.getB0118() != null && b01.getB0112() != null
							&& b01.getB0113() != null && b01.getB0114() != null) {
						BigDecimal d1 = b01.getB0118().subtract(b01.getB0112())
								.subtract(b01.getB0113()).add(b01.getB0114());
						if (d1 != null && b01.getB0115().compareTo(d1) != 0) {
							map
									.put("B0115",
											"[本月非交易变动] 应满足B0115=B0118-B0112-（B0113-B0114） ");
						}
					}
					// B0115=B0116+B0117。
					if (b01.getB0116() != null && b01.getB0117() != null) {
						BigDecimal d2 = b01.getB0116().add(b01.getB0117());
						if (d2 != null && b01.getB0115().compareTo(d2) != 0) {
							map.put("B0115", "[本月非交易变动] 应满足B0115=B0116+B0117 ");
						}
					}
				}
				// B0116 [本月非交易变动：其中注销、调整或重新分类至其他报表统计的金额]不能为空
				if (b01.getB0116() == null) {
					map.put("B0116", "[本月非交易变动：其中注销、调整或重新分类至其他报表统计的金额] 不能为空 ");
				}
				// B0117 [本月非交易变动：其中价值重估因素]不能为空
				if (b01.getB0117() == null) {
					map.put("B0117", "[本月非交易变动：其中价值重估因素] 不能为空 ");
				}
				// B0118 [本月末市值]不能为空，必须≥0
				if (b01.getB0118() == null
						|| b01.getB0118().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0118", "[本月末市值] 不能为空且应当大于等于0 ");
				}
				// B0119 [本月股息/红利收入]不能为空，必须≥0
				if (b01.getB0119() == null
						|| b01.getB0119().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0119", "[本月股息/红利收入] 不能为空且应当大于等于0 ");
				}
				// B0120 [本月末持股（份额）数量（逐支报送使用）]不能为空，必须≥0
				if (b01.getB0120() == null) {
					map.put("B0120", "[本月末持股（份额）数量（逐支报送使用）] 不能为空 ");
				} else {
					if (!"N/A".equals(b01.getB0106())
							&& b01.getB0120().compareTo(new BigDecimal(0)) < 0) {
						map.put("B0120", "[本月末持股（份额）数量（逐支报送使用）] 应当大于等于0 ");
					} else if ("N/A".equals(b01.getB0106())
							&& b01.getB0120().compareTo(new BigDecimal(0)) != 0) {
						map.put("B0120", "[本月末持股（份额）数量（逐支报送使用）] 应当等于0 ");
					}
				}
				// B0121 [本月末每股（每份）市价（逐支报送使用）]不能为空，必须≥0
				if (b01.getB0121() == null) {
					map.put("B0121", "[本月末每股（每份）市价（逐支报送使用）] 不能为空 ");
				} else {
					if (!"N/A".equals(b01.getB0106())
							&& b01.getB0121().compareTo(new BigDecimal(0)) < 0) {
						map.put("B0121", "[本月末每股（每份）市价（逐支报送使用）] 应当大于等于0 ");
					} else if ("N/A".equals(b01.getB0106())
							&& b01.getB0121().compareTo(new BigDecimal(0)) != 0) {
						map.put("B0121", "[本月末每股（每份）市价（逐支报送使用）] 应当等于0 ");
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
