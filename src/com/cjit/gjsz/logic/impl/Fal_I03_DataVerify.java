package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_I03Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_I03_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_I03_DataVerify() {
	}

	public Fal_I03_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_I03Entity i03 = (Fal_I03Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, i03.getActiontype())) {
					String value = getKey(i03.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(i03.getActiontype())) {
					if (StringUtil.isEmpty(i03.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(i03.getActiontype())) {
					if (!isNull(i03.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (i03.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// I0301 保险类别
				if (!verifyDictionaryValue(INSURANCE_TYPE, i03.getI0301())) {
					String value = getKey(i03.getI0301(), INSURANCE_TYPE);
					map.put("I0301", "[保险类别] [" + value + "] 无效 ");
				}
				// I0302 再保险接受人所属国家/地区(见国家和地区代码表的3 位字母代码)
				if (!verifyDictionaryValue(COUNTRY, i03.getI0302())) {
					String value = getKey(i03.getI0302(), COUNTRY);
					map.put("I0302", "[再保险接受人所属国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(i03.getI0302())
						|| "N/A".equals(i03.getI0302())) {
					map.put("I0302", "[再保险接受人所属国家/地区] 不能选择CHN或N/A ");
				}
				// I0303 再保险接受人所属部门(见投资者（被投资者）部门代码表)
				if (!verifyDictionaryValue(INVESTORINST, i03.getI0303())) {
					String value = getKey(i03.getI0303(), INVESTORINST);
					map.put("I0303", "[再保险接受人所属部门] [" + value + "] 无效 ");
				} else if (!"4".equals(i03.getI0303())) {
					// 问答四-1.1表内核查规则-69 “I0303 再保险接受人所属部门”=4（非银行金融机构）
					map.put("I0303", "[再保险接受人所属部门] 应选择4-非银行金融机构 ");
				}
				// I0304 再保险接受人与本机构的关系(见对方与本机构/被代理居民机构/委托人的关系代码表)
				if (!verifyDictionaryValue(OPPOSITERELA, i03.getI0304())) {
					String value = getKey(i03.getI0304(), OPPOSITERELA);
					map.put("I0304", "[再保险接受人与本机构的关系] [" + value + "] 无效 ");
				}
				// I0305 填表币种(见币种代码表)
				if (!verifyDictionaryValue(CURRENCY, i03.getI0305())) {
					String value = getKey(i03.getI0305(), CURRENCY);
					map.put("I0305", "[填表币种] [" + value + "] 无效 ");
				}
				// I0306 本月分出业务保费支出 必须≥0。
				if (i03.getI0306() == null
						|| i03.getI0306().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0306", "[本月分出业务保费支出] 应当大于等于0 ");
				}
				// I0307 本月摊回分保费用收入 必须≥0。
				if (i03.getI0307() == null
						|| i03.getI0307().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0307", "[本月摊回分保费用收入] 应当大于等于0 ");
				}
				// I0308 本月摊回赔付成本收入 必须≥0。
				if (i03.getI0308() == null
						|| i03.getI0308().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0308", "[本月摊回赔付成本收入] 应当大于等于0 ");
				}
				// I0309 上月末应收分保责任准备金余额 必须≥0。
				if (i03.getI0309() == null
						|| i03.getI0309().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0309", "[上月末应收分保责任准备金余额] 应当大于等于0 ");
				}
				// I0310 本月末应收分保责任准备金余额 必须≥0。
				if (i03.getI0310() == null
						|| i03.getI0310().compareTo(new BigDecimal(0.00)) < 0) {
					map.put("I0310", "[本月末应收分保责任准备金余额] 应当大于等于0 ");
				}
				// I0311 备注
				if (StringUtil.isEmpty(i03.getI0311())) {
					// 问答四-1.1表内核查规则-6
					if ((i03.getI0306() != null && i03.getI0306().compareTo(
							new BigDecimal(0.00)) < 0)
							|| (i03.getI0307() != null && i03.getI0307()
									.compareTo(new BigDecimal(0.00)) < 0)
							|| (i03.getI0308() != null && i03.getI0308()
									.compareTo(new BigDecimal(0.00)) < 0)
							|| (i03.getI0309() != null && i03.getI0309()
									.compareTo(new BigDecimal(0.00)) < 0)
							|| (i03.getI0310() != null && i03.getI0310()
									.compareTo(new BigDecimal(0.00)) < 0)) {
						map
								.put("I0311",
										"I0306-I0310各项中任何一项为负数，备注（I0311）字段必填 ");
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
