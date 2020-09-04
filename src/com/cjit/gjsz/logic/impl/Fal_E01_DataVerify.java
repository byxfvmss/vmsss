package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_E01Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_E01_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_E01_DataVerify() {
	}

	public Fal_E01_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_E01Entity e01 = (Fal_E01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, e01.getActiontype())) {
					String value = getKey(e01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(e01.getActiontype())) {
					if (StringUtil.isEmpty(e01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(e01.getActiontype())) {
					if (!isNull(e01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (e01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// E0101 s,1,1,1 [项目代码]不能为空且需在字典表中有定义 TRANSACTION_CODE
				if (!verifyDictionaryValue(TRANSACTION_CODE, e01.getE0101())) {
					String value = getKey(e01.getE0101(), TRANSACTION_CODE);
					map.put("E0101", "[项目代码] [" + value + "] 无效 ");
				}
				// E0102 s,1,3,3 [国别代码]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, e01.getE0102())) {
					String value = getKey(e01.getE0102(), COUNTRY);
					map.put("E0102", "[国别代码] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(e01.getE0102())
						|| "N/A".equals(e01.getE0102())) {
					map.put("E0102", "[国别代码] 不能选择CHN或N/A ");
				}
				// E0103 n,1,24,2 [金额]不能为空
				if (e01.getE0103() == null) {
					map.put("E0103", "[金额] 不能为空 ");
				} else if (e01.getE0103().compareTo(new BigDecimal(0)) < 0) {
					// 问答四-1.1表内核查规则-6
					if (StringUtil.isEmpty(e01.getRemark())) {
						map.put("REMARK", "[备注] 金额小于零时本字段不能为空 ");
					}
				} else {
					if (!ACTIONTYPE_D.equalsIgnoreCase(e01.getActiontype())) {
						String errorMsg = this.checkE01(e01);
						if (errorMsg != null) {
							if (errorMsg.indexOf("REMARK") > 0) {
								map.put("REMARK", "[备注] 当"
										+ errorMsg.replaceAll("REMARK", "")
										+ "时，本字段不能为空");
							} else {
								map.put("E0103", "[金额] 在国别相同的情况下，必须满足 "
										+ errorMsg);
							}
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
