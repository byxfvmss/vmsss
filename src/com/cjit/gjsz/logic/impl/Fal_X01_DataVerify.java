package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_X01Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_X01_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_X01_DataVerify() {
	}

	public Fal_X01_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_X01Entity x01 = (Fal_X01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, x01.getActiontype())) {
					String value = getKey(x01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(x01.getActiontype())) {
					if (StringUtil.isEmpty(x01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(x01.getActiontype())) {
					if (!isNull(x01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (x01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// X0101 项目代码 (见银行进出口贸易融资项目指标代码表)
				if (!verifyDictionaryValue(TRADE_FINANCE_PROJ_INDI, x01
						.getX0101())) {
					String value = getKey(x01.getX0101(),
							TRADE_FINANCE_PROJ_INDI);
					map.put("X0101", "[项目代码] [" + value + "] 无效 ");
				}
				// X0102 金额
				if (x01.getX0102() == null
						|| x01.getX0102().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("X0102", "[金额] 不能为空且应当大于等于0 ");
				} else {
					if (!ACTIONTYPE_D.equalsIgnoreCase(x01.getActiontype())) {
						String errorMas = this.checkX01(x01);
						if (errorMas != null) {
							map.put("X0102", "[金额] 必须满足 " + errorMas);
						}
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
