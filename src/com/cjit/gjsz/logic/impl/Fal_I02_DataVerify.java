package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_I02Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_I02_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_I02_DataVerify() {
	}

	public Fal_I02_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_I02Entity i02 = (Fal_I02Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, i02.getActiontype())) {
					String value = getKey(i02.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(i02.getActiontype())) {
					if (StringUtil.isEmpty(i02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(i02.getActiontype())) {
					if (!isNull(i02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (i02.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// I0201 保险类别
				if (!verifyDictionaryValue(INSURANCE_TYPE, i02.getI0201())) {
					String value = getKey(i02.getI0201(), INSURANCE_TYPE);
					map.put("I0201", "[保险类别] [" + value + "] 无效 ");
				}
				// I0202 再保险分出人所属国家/地区(见国家和地区代码表的3 位字母代码)
				if (!verifyDictionaryValue(COUNTRY, i02.getI0202())) {
					String value = getKey(i02.getI0202(), COUNTRY);
					map.put("I0202", "[再保险分出人所属国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(i02.getI0202())
						|| "N/A".equals(i02.getI0202())) {
					map.put("I0202", "[再保险分出人所属国家/地区] 不能选择CHN或N/A ");
				}
				// I0203 再保险分出人所属部门(见投资者（被投资者）部门代码表)
				if (!verifyDictionaryValue(INVESTORINST, i02.getI0203())) {
					String value = getKey(i02.getI0203(), INVESTORINST);
					map.put("I0203", "[再保险分出人所属部门] [" + value + "] 无效 ");
				} else if (!"4".equals(i02.getI0203())) {
					// 问答四-1.1表内核查规则-68 “I0203再保险分出人所属部门”=4（非银行金融机构）
					map.put("I0203", "[再保险分出人所属部门] 应选择4-非银行金融机构 ");
				}
				// I0204 再保险分出人与本机构的关系(见对方与本机构/被代理居民机构/委托人的关系代码表)
				if (!verifyDictionaryValue(OPPOSITERELA, i02.getI0204())) {
					String value = getKey(i02.getI0204(), OPPOSITERELA);
					map.put("I0204", "[再保险分出人与本机构的关系] [" + value + "] 无效 ");
				}
				// I0205 填表币种(见币种代码表)
				if (!verifyDictionaryValue(CURRENCY, i02.getI0205())) {
					String value = getKey(i02.getI0205(), CURRENCY);
					map.put("I0205", "[填表币种] [" + value + "] 无效 ");
				}
				// I0206 本月分入业务已赚分保费收入
				if (i02.getI0206() == null) {
					map.put("I0206", "[本月分入业务已赚分保费收入] 不能为空 ");
				}
				// I0207 本月归属于非居民保单持有人的收益（补充保费） 必须≥0。
				if (i02.getI0207() == null
						|| i02.getI0207().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0207", "[本月归属于非居民保单持有人的收益（补充保费）] 应当大于等于0 ");
				}
				// I0208 本月应付分保费用 必须≥0。
				if (i02.getI0208() == null
						|| i02.getI0208().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0208", "[本月应付分保费用] 应当大于等于0 ");
				}
				// I0209 本月应付分保赔款 必须≥0。
				if (i02.getI0209() == null
						|| i02.getI0209().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0209", "[本月应付分保赔款] 应当大于等于0 ");
				}
				// I0210 上月末分保责任准备金余额 必须≥0。
				if (i02.getI0210() == null
						|| i02.getI0210().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0210", "[上月末分保责任准备金余额] 应当大于等于0 ");
				}
				// I0211 本月末分保责任准备金余额 必须≥0。
				if (i02.getI0211() == null
						|| i02.getI0211().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0211", "[本月末分保责任准备金余额] 应当大于等于0 ");
				}
				// I0212 备注
				if (StringUtil.isEmpty(i02.getI0212())) {
					// 问答四-1.1表内核查规则-6
					if ((i02.getI0208() != null && i02.getI0208().compareTo(
							new BigDecimal(0.00)) < 0)
							|| (i02.getI0209() != null && i02.getI0209()
									.compareTo(new BigDecimal(0.00)) < 0)
							|| (i02.getI0210() != null && i02.getI0210()
									.compareTo(new BigDecimal(0.00)) < 0)
							|| (i02.getI0211() != null && i02.getI0211()
									.compareTo(new BigDecimal(0.00)) < 0)) {
						map
								.put("I0212",
										"I0208-I0211各项中任何一项为负数，备注（I0212）字段必填 ");
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
