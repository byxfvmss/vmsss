package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_D09Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_D09_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_D09_DataVerify() {
	}

	public Fal_D09_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_D09Entity d09 = (Fal_D09Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, d09.getActiontype())) {
					String value = getKey(d09.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(d09.getActiontype())) {
					if (StringUtil.isEmpty(d09.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(d09.getActiontype())) {
					if (!isNull(d09.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (d09.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// D0901 s,1,3,3 [资产类别]不能为空且需在字典表中有定义 ASSETS_TYPE
				if (!verifyDictionaryValue(ASSETS_TYPE, d09.getD0901())) {
					String value = getKey(d09.getD0901(), ASSETS_TYPE);
					map.put("D0901", "[资产类别] [" + value + "] 无效 ");
				}
				// D0902 s,1,3,3 [风险分类]不能为空且需在字典表中有定义 RISK_CLASS
				if (!verifyDictionaryValue(RISK_CLASS, d09.getD0902())) {
					String value = getKey(d09.getD0902(), RISK_CLASS);
					map.put("D0902", "[风险分类] [" + value + "] 无效 ");
				}
				// D0903 s,1,3,3 [币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, d09.getD0903())) {
					String value = getKey(d09.getD0903(), CURRENCY);
					map.put("D0903", "[币种] [" + value + "] 无效 ");
				}
				// D0904 n,1,24,2 [本月末减值准备余额]不能为空，必须≥0
				if (d09.getD0904() == null
						|| d09.getD0904().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0904", "[本月末减值准备余额] 不能为空且应当大于等于0 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
