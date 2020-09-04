package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_B05Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_B05_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_B05_DataVerify() {
	}

	public Fal_B05_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_B05Entity b05 = (Fal_B05Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, b05.getActiontype())) {
					String value = getKey(b05.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(b05.getActiontype())) {
					if (StringUtil.isEmpty(b05.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(b05.getActiontype())) {
					if (!isNull(b05.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (b05.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// EXDEBTCODE s,1,28,28 [外债编号]不能为空，填写外债唯一性编码
				if (b05.getExdebtcode() == null) {
					map.put("EXDEBTCODE", "[外债编号] 不能为空，填写外债唯一性编码 ");
				}
				// B0501 s,1,12 [证券代码]不能为空且不能超过12位
				// B0502 s,1,3,3 [发行地]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, b05.getB0502())) {
					String value = getKey(b05.getB0502(), COUNTRY);
					map.put("B0502", "[发行地] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(b05.getB0502())) {
					map.put("B0502", "[发行地] 不能为CHN中国 ");
				}
				// B0503 s,1,1,1 [原始期限]不能为空且需在字典表中有定义 ORIDEADLINE
				if (!verifyDictionaryValue(ORIDEADLINE, b05.getB0503())) {
					String value = getKey(b05.getB0503(), ORIDEADLINE);
					map.put("B0503", "[原始期限] [" + value + "] 无效 ");
				}
				// B0504 s,1,255 [投资者名称]不能为空且不能超过255位字符
				// B0505 s,1,3,3 [投资者所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, b05.getB0505())) {
					String value = getKey(b05.getB0505(), COUNTRY);
					map.put("B0505", "[投资者所属国家/地区] [" + value + "] 无效 ");
				}
				// B0506 s,1,1,1 [投资者所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, b05.getB0506())) {
					String value = getKey(b05.getB0506(), INVESTORINST);
					map.put("B0506", "[投资者所属部门] [" + value + "] 无效 ");
				}
				// B0507 s,1,1,1 [投资者与本机构的关系]不能为空且需在字典表中有定义 OPPOSITERELA
				if (!verifyDictionaryValue(OPPOSITERELA, b05.getB0507())) {
					String value = getKey(b05.getB0507(), OPPOSITERELA);
					map.put("B0507", "[投资者与本机构的关系] [" + value + "] 无效 ");
				}
				// B0508 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, b05.getB0508())) {
					String value = getKey(b05.getB0508(), CURRENCY);
					map.put("B0508", "[原始币种] [" + value + "] 无效 ");
				}
				// B0509 n,1,24,2 [上月末市值]不能为空，必须≥0
				if (b05.getB0509() == null
						|| b05.getB0509().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0509", "[上月末市值] 不能为空且应当大于等于0 ");
				}
				// B0510 n,1,24,2 [本月发行金额]不能为空，必须≥0
				if (b05.getB0510() == null
						|| b05.getB0510().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0510", "[本月发行金额] 不能为空且应当大于等于0 ");
				}
				// B0511 n,1,24,2 [本月赎回金额]不能为空，必须≥0
				if (b05.getB0511() == null
						|| b05.getB0511().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0511", "[本月赎回金额] 不能为空且应当大于等于0 ");
				}
				// B0512 n,1,24,2 [本月非交易变动]不能为空
				if (b05.getB0512() == null) {
					map.put("B0512", "[本月非交易变动] 不能为空 ");
				} else {
					// B0512=B0515-B0509-（B0510-B0511）
					if (b05.getB0515() != null && b05.getB0509() != null
							&& b05.getB0510() != null && b05.getB0511() != null) {
						BigDecimal d = b05.getB0515().subtract(b05.getB0509())
								.subtract(b05.getB0510()).add(b05.getB0511());
						if (d.compareTo(b05.getB0512()) != 0) {
							map
									.put("B0512",
											"[本月非交易变动] 应满足B0512=B0515-B0509-（B0510-B0511） ");
						}
					}
					// B0512=B0513+B0514
					if (b05.getB0513() != null && b05.getB0514() != null) {
						BigDecimal d = b05.getB0513().add(b05.getB0514());
						if (d.compareTo(b05.getB0512()) != 0) {
							map.put("B0512", "[本月非交易变动] 应满足B0512=B0513+B0514 ");
						}
					}
				}
				// B0513 n,1,24,2 [本月非交易变动：其中重新分类等非买卖因素引起的变动]不能为空
				if (b05.getB0513() == null) {
					map.put("B0513", "[本月非交易变动：其中重新分类等非买卖因素引起的变动] 不能为空 ");
				}
				// B0514 n,1,24,2 [本月非交易变动：价值重估因素]不能为空
				if (b05.getB0514() == null) {
					map.put("B0514", "[本月非交易变动：价值重估因素] 不能为空 ");
				}
				// B0515 n,1,24,2 [本月末市值]不能为空，必须≥0
				if (b05.getB0515() == null
						|| b05.getB0515().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0515", "[本月末市值] 不能为空且应当大于等于0 ");
				}
				// B0516 n,1,24,2 [本月末市值：其中剩余期限在一年及以下]不能为空，必须≥0
				if (b05.getB0516() == null
						|| b05.getB0516().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0516", "[本月末市值：其中剩余期限在一年及以下] 不能为空且应当大于等于0 ");
				} else if ("1".equals(b05.getB0503())
						&& b05.getB0516().compareTo(b05.getB0515()) != 0) {
					map.put("B0516", "[本月末市值：其中剩余期限在一年及以下] 应等于B0515本月末市值 ");
				} else if ("2".equals(b05.getB0503())
						&& b05.getB0516().compareTo(b05.getB0515()) > 0) {
					map.put("B0516", "[本月末市值：其中剩余期限在一年及以下] 应小于等于B0515本月末市值 ");
				}
				// B0517 n,1,24,2 [本月利息支出]不能为空，必须≥0
				if (b05.getB0517() == null
						|| b05.getB0517().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0517", "[本月利息支出] 不能为空且应当大于等于0 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
