package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_I01Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_I01_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_I01_DataVerify() {
	}

	public Fal_I01_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_I01Entity i01 = (Fal_I01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, i01.getActiontype())) {
					String value = getKey(i01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(i01.getActiontype())) {
					if (StringUtil.isEmpty(i01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(i01.getActiontype())) {
					if (!isNull(i01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (i01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// I0101 保险类别
				if (!verifyDictionaryValue(INSURANCE_TYPE, i01.getI0101())) {
					String value = getKey(i01.getI0101(), INSURANCE_TYPE);
					map.put("I0101", "[保险类别] [" + value + "] 无效 ");
				}
				// I0102 保单持有人所属国家/地区(见国家和地区代码表的3 位字母代码)
				if (!verifyDictionaryValue(COUNTRY, i01.getI0102())) {
					String value = getKey(i01.getI0102(), COUNTRY);
					map.put("I0102", "[保单持有人所属国家/地] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(i01.getI0102())
						|| "N/A".equals(i01.getI0102())) {
					map.put("I0102", "[保单持有人所属国家/地区] 不能选择CHN或N/A ");
				}
				// I0103 保单持有人所属部门(见投资者（被投资者）部门代码表)
				if (!verifyDictionaryValue(INVESTORINST, i01.getI0103())) {
					String value = getKey(i01.getI0103(), INVESTORINST);
					map.put("I0103", "[保单持有人所属部门] [" + value + "] 无效 ");
				}
				// I0104 保单持有人与本机构的关系(见对方与本机构/被代理居民机构/委托人的关系代码表)
				if (!verifyDictionaryValue(OPPOSITERELA, i01.getI0104())) {
					String value = getKey(i01.getI0104(), OPPOSITERELA);
					map.put("I0104", "[保单持有人与本机构的关系] [" + value + "] 无效 ");
				}
				// I0105 填表币种(见币种代码表)
				if (!verifyDictionaryValue(CURRENCY, i01.getI0105())) {
					String value = getKey(i01.getI0105(), CURRENCY);
					map.put("I0105", "[填表币种] [" + value + "] 无效 ");
				}
				// I0106 本月已赚毛保费总额
				if (i01.getI0106() == null) {
					map.put("I0106", "[本月已赚毛保费总额] 不能为空 ");
				}
				// I0107 本月归属于非居民保单持有人的收益（补充保费） 必须≥0
				if (i01.getI0107() == null
						|| i01.getI0107().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0107", "[本月归属于非居民保单持有人的收益（补充保费）] 应当大于等于0 ");
				}
				// I0108 本月应付索赔/福利总额 必须≥0
				if (i01.getI0108() == null
						|| i01.getI0108().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0108", "[本月应付索赔/福利总] 应当大于等于0 ");
				}
				// I0109 上月末保单责任准备金余额 必须≥0
				if (i01.getI0109() == null
						|| i01.getI0109().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0109", "[上月末保单责任准备金余额] 应当大于等于0 ");
				}
				// I0110 本月末保单责任准备金余额 必须≥0
				if (i01.getI0110() == null
						|| i01.getI0110().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0110", "[本月末保单责任准备金余额] 应当大于等于0 ");
				}
				// I0111 备注
				if (StringUtil.isEmpty(i01.getI0111())) {
					// 问答四-1.1表内核查规则-6
					if ((i01.getI0106() != null && i01.getI0106().compareTo(
							new BigDecimal(0.00)) < 0)
							|| (i01.getI0107() != null && i01.getI0107()
									.compareTo(new BigDecimal(0.00)) < 0)
							|| (i01.getI0108() != null && i01.getI0108()
									.compareTo(new BigDecimal(0.00)) < 0)
							|| (i01.getI0109() != null && i01.getI0109()
									.compareTo(new BigDecimal(0.00)) < 0)
							|| (i01.getI0110() != null && i01.getI0110()
									.compareTo(new BigDecimal(0.00)) < 0)) {
						map
								.put("I0111",
										"I0106-I0110各项中任何一项为负数，备注（I0111）字段必填 ");
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
