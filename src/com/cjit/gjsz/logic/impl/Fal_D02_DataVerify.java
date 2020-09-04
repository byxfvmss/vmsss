package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_D02Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_D02_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_D02_DataVerify() {
	}

	public Fal_D02_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			service = (SearchService) SpringContextUtil
					.getBean("searchService");
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_D02Entity d02 = (Fal_D02Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, d02.getActiontype())) {
					String value = getKey(d02.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(d02.getActiontype())) {
					if (StringUtil.isEmpty(d02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(d02.getActiontype())) {
					if (!isNull(d02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (d02.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// D0201 [是否委托贷款]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(IS_ENTRUSTED_LOAN, d02.getD0201())) {
					String value = getKey(d02.getD0201(), IS_ENTRUSTED_LOAN);
					map.put("D0201", "[是否委托贷款] [" + value + "] 无效 ");
				}
				// D0202 [委托人所属部门]
				if ("1".equals(d02.getD0201())) {
					// 如果D0201=1，此字段为必填项，必须5 选1，且≠06，见投资者（被投资者）部门代码表
					if (StringUtil.isEmpty(d02.getD0202())) {
						map.put("D0202", "[委托人所属部门] 当是否委托贷款选择1时，此字段不能为空 ");
					} else if (!verifyDictionaryValue(INVESTORINST, d02
							.getD0202())) {
						String value = getKey(d02.getD0202(), INVESTORINST);
						map.put("D0202", "[委托人所属部门] [" + value + "] 无效 ");
					} else if ("6".equals(d02.getD0202())) {
						map.put("D0202", "[委托人所属部门] 不能选择6-国际组织 ");
					}
				} else if ("2".equals(d02.getD0201())) {
					// 如果D0201=2，此字段为空
					if (StringUtil.isNotEmpty(d02.getD0202())) {
						map.put("D0202", "[委托人所属部门] 当是否委托贷款选择2时，此字段应为空 ");
					}
				}
				// D0203 [对方国家/地区]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(COUNTRY, d02.getD0203())) {
					String value = getKey(d02.getD0203(), COUNTRY);
					map.put("D0203", "[对方国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(d02.getD0203())
						|| "N/A".equals(d02.getD0203())) {
					map.put("D0203", "[对方国家/地区] 不能选择CHN或N/A ");
				}
				// D0204 [对方部门]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVESTORINST, d02.getD0204())) {
					String value = getKey(d02.getD0204(), INVESTORINST);
					map.put("D0204", "[对方部门] [" + value + "] 无效 ");
				}
				// D0205 [对方与本机构/委托人的关系]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(OPPOSITERELA, d02.getD0205())) {
					String value = getKey(d02.getD0205(), OPPOSITERELA);
					map.put("D0205", "[对方与本机构/委托人的关系] [" + value + "] 无效 ");
				}
				// D0206 [原始期限]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(ORIDEADLINE, d02.getD0206())) {
					String value = getKey(d02.getD0206(), ORIDEADLINE);
					map.put("D0206", "[原始期限] [" + value + "] 无效 ");
				}
				// D0207 [原始币种]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(CURRENCY, d02.getD0207())) {
					String value = getKey(d02.getD0207(), CURRENCY);
					map.put("D0207", "[原始币种] [" + value + "] 无效 ");
				}
				// D0208 [上月末本金余额]不能为空且大于等于0
				if (d02.getD0208() == null
						|| d02.getD0208().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0208", "[上月末本金余额] 不能为空且应当大于等于0 ");
				}
				// D0209 [上月末应收利息余额]不能为空且大于等于0
				if (d02.getD0209() == null
						|| d02.getD0209().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0209", "[上月末应收利息余额] 不能为空且应当大于等于0 ");
				}
				// D0210 [本月末本金余额]不能为空且大于等于0
				if (d02.getD0210() == null
						|| d02.getD0210().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0210", "[本月末本金余额] 不能为空且应当大于等于0 ");
				}
				// D0211 [本月末本金余额：其中剩余期限在一年及以下]不能为空且大于等于0
				if (d02.getD0211() == null
						|| d02.getD0211().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0211", "[本月末本金余额：其中剩余期限在一年及以下] 不能为空且应当大于等于0 ");
				} else {
					if ("1".equals(d02.getD0206())
							&& d02.getD0210().compareTo(d02.getD0211()) != 0) {
						// 当D0206=1 时，D0210=D0211
						map.put("D0211", "[本月末本金余额：其中剩余期限在一年及以下] 应等于本月末本金余额 ");
					} else if ("2".equals(d02.getD0206())
							&& d02.getD0210().compareTo(d02.getD0211()) < 0) {
						// 当D0206=2 时，D0210≥D0211
						map.put("D0211", "[本月末本金余额：其中剩余期限在一年及以下] 应小于本月末本金余额 ");
					}
				}
				// D0212 [本月末应收利息余额]不能为空且大于等于0
				if (d02.getD0212() == null
						|| d02.getD0212().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0212", "[本月末应收利息余额] 不能为空且应当大于等于0 ");
				}
				// D0213 [本月非交易变动]不能为空
				if (d02.getD0213() == null) {
					map.put("D0213", "[本月非交易变动] 不能为空 ");
				}
				// D0214 [本月净发生额]不能为空
				if (d02.getD0214() == null) {
					map.put("D0214", "[本月净发生额] 不能为空 ");
				} else {
					// D0214+D0213=D0210+D0212-(D0208+D0209)
					BigDecimal d0214 = d02.getD0210().add(d02.getD0212())
							.subtract(d02.getD0208()).subtract(d02.getD0209())
							.subtract(d02.getD0213());
					if (d02.getD0214().compareTo(d0214) != 0) {
						map.put("D0214",
								"[本月净发生额] 应满足 D0214+D0213=D0210+D0212-(D0208+D0209) 理论值为 "
										+ d0214.toString());
					}
				}
				// D0215 [本月利息收入]不能为空
				if (d02.getD0215() == null) {
					map.put("D0215", "[本月利息收入] 不能为空 ");
				} else if (d02.getD0215().compareTo(new BigDecimal(0)) < 0) {
					// 问答四-1.1表内核查规则-6
					if (StringUtil.isEmpty(d02.getRemark())) {
						map.put("REMARK", "当D0215为负数时，备注字段必填 ");
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
