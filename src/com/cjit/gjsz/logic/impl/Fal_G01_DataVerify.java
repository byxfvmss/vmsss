package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_G01Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_G01_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_G01_DataVerify() {
	}

	public Fal_G01_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_G01Entity g01 = (Fal_G01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, g01.getActiontype())) {
					String value = getKey(g01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(g01.getActiontype())) {
					if (StringUtil.isEmpty(g01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(g01.getActiontype())) {
					if (!isNull(g01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (g01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// G0101CODE s,0,4 [发卡机构代码]不能超过4位字符
				// G0101 s,0,256 [发卡机构名称]不能超过256位字符
				// G0102 s,1,1,1 [银行卡清算渠道]不能为空且需在字典表中有定义 BANK_CARD_CLEAR_CHANNEL
				if (!verifyDictionaryValue(BANK_CARD_CLEAR_CHANNEL, g01
						.getG0102())) {
					String value = getKey(g01.getG0102(),
							BANK_CARD_CLEAR_CHANNEL);
					map.put("G0102", "[银行卡清算渠道] [" + value + "] 无效 ");
				}
				// G0103 s,1,1,1 [持卡人是否为中国居民]不能为空且需在字典表中有定义 ISCHINESE
				if (!verifyDictionaryValue(ISCHINESE, g01.getG0103())) {
					String value = getKey(g01.getG0103(), ISCHINESE);
					map.put("G0103", "[持卡人是否为中国居民] [" + value + "] 无效 ");
				}
				// G0104 s,1,1,1 [交易类型]不能为空且需在字典表中有定义 BANK_CARD_EXPENSE_TYPE
				if (!verifyDictionaryValue(BANK_CARD_EXPENSE_TYPE, g01
						.getG0104())) {
					String value = getKey(g01.getG0104(),
							BANK_CARD_EXPENSE_TYPE);
					map.put("G0104", "[交易类型] [" + value + "] 无效 ");
				}
				// G0105 s,1,3,3 [交易所在地国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, g01.getG0105())) {
					String value = getKey(g01.getG0105(), COUNTRY);
					map.put("G0105", "[交易所在地国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(g01.getG0105())
						|| "N/A".equals(g01.getG0105())) {
					map.put("G0105", "[交易所在地国家/地区] 不能选择CHN或N/A ");
				}
				// G0106 s,1,3,3 [交易原币]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, g01.getG0106())) {
					String value = getKey(g01.getG0106(), CURRENCY);
					map.put("G0106", "[交易原币] [" + value + "] 无效 ");
				}
				// G0107 n,1,24,2 [交易金额]不能为空
				if (g01.getG0107() == null) {
					map.put("G0107", "[交易金额] 不能为空 ");
				} else if (g01.getG0107().compareTo(new BigDecimal(0)) < 0
						&& StringUtil.isEmpty(g01.getRemark())) {
					map.put("REMARK", "G0107（交易金额）为负数时备注（REMARK）字段必填 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
