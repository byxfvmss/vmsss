package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_B04Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_B04_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_B04_DataVerify() {
	}

	public Fal_B04_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_B04Entity b04 = (Fal_B04Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, b04.getActiontype())) {
					String value = getKey(b04.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(b04.getActiontype())) {
					if (StringUtil.isEmpty(b04.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(b04.getActiontype())) {
					if (!isNull(b04.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (b04.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// B0401 s,1,1,1 [工具类型]不能为空且需在字典表中有定义 INVESTTYPE
				if (!verifyDictionaryValue(INVESTTYPE, b04.getB0401())) {
					String value = getKey(b04.getB0401(), INVESTTYPE);
					map.put("B0401", "[工具类型] [" + value + "] 无效 ");
				}
				// B0402 s,1,12 [证券代码]不能为空
				// B0403 s,1,3,3 [发行地]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, b04.getB0403())) {
					String value = getKey(b04.getB0403(), COUNTRY);
					map.put("B0403", "[发行地] [" + value + "] 无效 ");
				} else if ("1".equals(b04.getB0401())
						&& COUNTRY_CHN.equals(b04.getB0403())) {
					map.put("B0403", "[发行地] 不应为CHN ");
				} else if ("2".equals(b04.getB0401())
						&& !COUNTRY_CHN.equals(b04.getB0403())) {
					map.put("B0403", "[发行地] 应为CHN ");
				}
				// B0404 s,1,256 [投资者名称]不能为空且不能超过256位字符
				// B0405 s,1,3,3 [投资者所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, b04.getB0405())) {
					String value = getKey(b04.getB0405(), COUNTRY);
					map.put("B0405", "[投资者所属国家/地区] [" + value + "] 无效 ");
				}
				// B0406 s,1,1,1 [投资者所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, b04.getB0406())) {
					String value = getKey(b04.getB0406(), INVESTORINST);
					map.put("B0406", "[投资者所属部门] [" + value + "] 无效 ");
				}
				// B0407 s,1,1,1 [投资者与本机构的关系]不能为空且需在字典表中有定义 OPPOSITERELA
				if (!verifyDictionaryValue(OPPOSITERELA, b04.getB0407())) {
					String value = getKey(b04.getB0407(), OPPOSITERELA);
					map.put("B0407", "[投资者与本机构的关系] [" + value + "] 无效 ");
				}
				// B0408 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, b04.getB0408())) {
					String value = getKey(b04.getB0408(), CURRENCY);
					map.put("B0408", "[原始币种] [" + value + "] 无效 ");
				}
				// B0409 n,1,24,2 [上月末市值]不能为空，必须≥0
				if (b04.getB0409() == null
						|| b04.getB0409().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0409", "[上月末市值] 不能为空且应当大于等于0 ");
				}
				// B0410 n,1,24,2 [本月发生金额]不能为空，必须≥0
				if (b04.getB0410() == null
						|| b04.getB0410().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0410", "[本月发生金额] 不能为空且应当大于等于0 ");
				}
				// B0411 n,1,24,2 [本月回购（赎回）金额]不能为空，必须≥0
				if (b04.getB0411() == null
						|| b04.getB0411().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0411", "[本月回购（赎回）金额] 不能为空且应当大于等于0 ");
				}
				// B0412 n,1,24,2 [本月非交易变动]不能为空
				if (b04.getB0412() == null) {
					map.put("B0412", "[本月非交易变动] 不能为空 ");
				} else {
					// B0412=B0415-B0409-（B0410-B0411）
					if (b04.getB0415() != null && b04.getB0409() != null
							&& b04.getB0410() != null && b04.getB0411() != null) {
						BigDecimal d = b04.getB0415().subtract(b04.getB0409())
								.subtract(b04.getB0410()).add(b04.getB0411());
						if (d.compareTo(b04.getB0412()) != 0) {
							map
									.put("B0412",
											"[本月非交易变动] 应满足B0412=B0415-B0409-（B0410-B0411） ");
						}
					}
					// B0412=B0413+B0414
					if (b04.getB0413() != null && b04.getB0414() != null) {
						BigDecimal d = b04.getB0413().add(b04.getB0414());
						if (d.compareTo(b04.getB0412()) != 0) {
							map.put("B0412", "[本月非交易变动] 应满足B0412=B0413+B0414 ");
						}
					}
				}
				// B0413 n,1,24,2 [本月非交易变动：其中调整及重新分类至其他报表统计的金额]不能为空
				if (b04.getB0413() == null) {
					map.put("B0413", "[本月非交易变动：其中调整及重新分类至其他报表统计的金额] 不能为空 ");
				}
				// B0414 n,1,24,2 [本月非交易变动：其中价值重估因素]不能为空
				if (b04.getB0414() == null) {
					map.put("B0414", "[本月非交易变动：其中价值重估因素] 不能为空 ");
				}
				// B0415 n,1,24,2 [本月末市值]不能为空，必须≥0
				if (b04.getB0415() == null
						|| b04.getB0415().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0415", "[本月末市值] 不能为空且应当大于等于0 ");
				}
				// B0416 n,1,24,2 [本月宣告分配投资者的股息/红利]不能为空，必须≥0
				if (b04.getB0416() == null
						|| b04.getB0416().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0416", "[本月宣告分配投资者的股息/红利] 不能为空且应当大于等于0 ");
				}
				// B0417 n,1,24,2 [本月末未实现收益（仅限发行产品为货币市场投资基金份额/单位）]不能为空，必须≥0
				if ("3".equals(b04.getB0401())
						&& b04.getB0417().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0417",
							"[本月末未实现收益（仅限发行产品为货币市场投资基金份额/单位）] 不能为空且应当大于等于0 ");
				} else if (!"3".equals(b04.getB0401())
						&& b04.getB0417().compareTo(new BigDecimal(0)) != 0) {
					map.put("B0417", "[本月末未实现收益（仅限发行产品为货币市场投资基金份额/单位）] 应当等于0 ");
				}
				// B0418 n,1,24,2 [本月末投资者持股（份额）数量]不能为空，必须≥0
				if (("1".equals(b04.getB0401()) || "3".equals(b04.getB0401()) || "4"
						.equals(b04.getB0401()))
						&& b04.getB0418().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0418", "[本月末投资者持股（份额）数量] 不能为空且应当大于等于0 ");
				} else if ("2".equals(b04.getB0401())
						&& b04.getB0418().compareTo(new BigDecimal(0)) != 0) {
					map.put("B0418", "[本月末投资者持股（份额）数量] 应当等于0 ");
				}
				// B0419 n,1,24,2 [本月末每股（每份）市价]不能为空，必须≥0
				if (("1".equals(b04.getB0401()) || "3".equals(b04.getB0401()) || "4"
						.equals(b04.getB0401()))
						&& b04.getB0419().compareTo(new BigDecimal(0)) < 0) {
					map.put("B0419", "[本月末每股（每份）市价] 不能为空且应当大于等于0 ");
				} else if ("2".equals(b04.getB0401())
						&& b04.getB0419().compareTo(new BigDecimal(0)) != 0) {
					map.put("B0419", "[本月末每股（每份）市价] 应当等于0 ");
				}
				// B0420 n,1,24,2 [本月末投资者持股（份额）比例]不能为空，必须>0且≤100
				if (b04.getB0420() == null
						|| b04.getB0420().compareTo(new BigDecimal(0)) <= 0
						|| b04.getB0420().compareTo(new BigDecimal(100)) > 0) {
					map.put("B0420", "[本月末投资者持股（份额）比例] 不能为空且应当大于0并小于等于100 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
