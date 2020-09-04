package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_G02Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_G02_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_G02_DataVerify() {
	}

	public Fal_G02_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_G02Entity g02 = (Fal_G02Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, g02.getActiontype())) {
					String value = getKey(g02.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(g02.getActiontype())) {
					if (StringUtil.isEmpty(g02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(g02.getActiontype())) {
					if (!isNull(g02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (g02.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// G0201 s,1,1,1 [银行卡清算渠道]不能为空且需在字典表中有定义 BANK_CARD_CLEAR_CHANNEL
				if (!verifyDictionaryValue(BANK_CARD_CLEAR_CHANNEL, g02
						.getG0201())) {
					String value = getKey(g02.getG0201(),
							BANK_CARD_CLEAR_CHANNEL);
					map.put("G0201", "[银行卡清算渠道] [" + value + "] 无效 ");
				}
				// G0202 s,1,1,1 [交易类型]不能为空且需在字典表中有定义 BANK_CARD_EXPENSE_TYPE
				if (!verifyDictionaryValue(BANK_CARD_EXPENSE_TYPE, g02
						.getG0202())) {
					String value = getKey(g02.getG0202(),
							BANK_CARD_EXPENSE_TYPE);
					map.put("G0202", "[交易类型] [" + value + "] 无效 ");
				}
				// G0203 s,1,3,3 [发卡行所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, g02.getG0203())) {
					String value = getKey(g02.getG0203(), COUNTRY);
					map.put("G0203", "[发卡行所属国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(g02.getG0203())
						|| "N/A".equals(g02.getG0203())) {
					map.put("G0203", "[发卡行所属国家/地区] 不能选择CHN或N/A ");
				}
				// G0204 s,1,3,3 [交易原币]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, g02.getG0204())) {
					String value = getKey(g02.getG0204(), CURRENCY);
					map.put("G0204", "[交易原币] [" + value + "] 无效 ");
				}
				// G0205 n,1,24,2 [交易金额]不能为空
				if (g02.getG0205() == null) {
					map.put("G0205", "[交易金额] 不能为空 ");
				} else if (g02.getG0205().compareTo(new BigDecimal(0)) < 0
						&& StringUtil.isEmpty(g02.getRemark())) {
					map.put("G0205", "[交易金额] G0205（交易金额）为负数时备注（REMARK）字段必填 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
