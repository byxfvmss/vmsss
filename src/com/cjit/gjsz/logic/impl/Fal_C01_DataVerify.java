package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_C01Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_C01_DataVerify extends SelfDataVerify implements DataVerify {

	public Fal_C01_DataVerify() {
	}

	public Fal_C01_DataVerify(List dictionarys, List verifylList,
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
				Fal_C01Entity c01 = (Fal_C01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, c01.getActiontype())) {
					String value = getKey(c01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(c01.getActiontype())) {
					if (StringUtil.isEmpty(c01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(c01.getActiontype())) {
					if (!isNull(c01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (c01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// C0101 s,1,1,1 [填报机构身份]不能为空且需在字典表中有定义 INST_IDENTITY
				if (!verifyDictionaryValue(INST_IDENTITY, c01.getC0101(),
						"T_FAL_C01")) {
					String value = getKey(c01.getC0101(), INST_IDENTITY);
					map.put("C0101", "[填报机构身份] [" + value + "] 无效 ");
				}
				// C0102 s,0,3,3 [居民被代理人/委托人所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if ("1".equals(c01.getC0101())
						&& StringUtil.isNotEmpty(c01.getC0102())) {
					map.put("C0102", "[居民被代理人/委托人所属国家/地区] 填报机构身份选择1时，此处应为空 ");
				} else if (("2".equals(c01.getC0101()) || "3".equals(c01
						.getC0101()))
						&& !COUNTRY_CHN.equals(c01.getC0102())) {
					map.put("C0102",
							"[居民被代理人/委托人所属国家/地区] 填报机构身份选择2或3时，此处必须为CHN中国 ");
				}
				// C0103 s,0,1,1 [居民被代理人/委托人所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if ("1".equals(c01.getC0101())
						&& StringUtil.isNotEmpty(c01.getC0103())) {
					map.put("C0103", "[居民被代理人/委托人所属部门] 填报机构身份选择1时，此处应为空 ");
				} else if (("2".equals(c01.getC0101()) || "3".equals(c01
						.getC0101()))
						&& !verifyDictionaryValue(INVESTORINST, c01.getC0103())) {
					String value = getKey(c01.getC0103(), INVESTORINST);
					map.put("C0103", "[居民被代理人/委托人所属部门] [" + value + "] 无效 ");
				}
				// C0104 s,1,1,1 [合约类别]不能为空且需在字典表中有定义 DERIVATIVECONTRACTCLASS
				if (!verifyDictionaryValue(DERIVATIVECONTRACTCLASS, c01
						.getC0104())) {
					String value = getKey(c01.getC0104(),
							DERIVATIVECONTRACTCLASS);
					map.put("C0104", "[合约类别] [" + value + "] 无效 ");
				}
				// C0105 s,1,1,1 [金融风险类别]不能为空且需在字典表中有定义 DERIVATIVERISKCLASS
				if (!verifyDictionaryValue(DERIVATIVERISKCLASS, c01.getC0105())) {
					String value = getKey(c01.getC0105(), DERIVATIVERISKCLASS);
					map.put("C0105", "[金融风险类别] [" + value + "] 无效 ");
				}
				// C0106 s,1,3,3 [非居民交易对手所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, c01.getC0106())) {
					String value = getKey(c01.getC0106(), COUNTRY);
					map.put("C0106", "[非居民交易对手所属国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(c01.getC0106())
						|| "N/A".equals(c01.getC0106())) {
					map.put("C0106", "[非居民交易对手所属国家/地区] 不能选择CHN或N/A ");
				}
				// C0107 s,1,1,1 [非居民交易对手所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, c01.getC0107())) {
					String value = getKey(c01.getC0107(), INVESTORINST);
					map.put("C0107", "[非居民交易对手所属部门] [" + value + "] 无效 ");
				}
				// C0108 s,1,1,1 [非居民交易对手与本机构/居民机构的关系]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(OPPOSITERELA, c01.getC0108())) {
					String value = getKey(c01.getC0108(), OPPOSITERELA);
					map.put("C0108", "[非居民交易对手与本机构/居民机构的关系] [" + value
							+ "] 无效 ");
				}
				// C0109 s,1,3,3 [结算的原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, c01.getC0109())) {
					String value = getKey(c01.getC0109(), CURRENCY);
					map.put("C0109", "[结算的原始币种] [" + value + "] 无效 ");
				}
				// C0110 n,1,24,2 [上月末头寸市值]不能为空
				if (c01.getC0110() == null) {
					map.put("C0110", "[上月末头寸市值] 不能为空 ");
				}
				// C0111 n,1,24,2 [本月（现金）结算付款额]不能为空，必须≥0
				if (c01.getC0111() == null
						|| c01.getC0111().compareTo(new BigDecimal(0)) < 0) {
					map.put("C0111", "[本月（现金）结算付款额] 不能为空且应当大于等于0 ");
				}
				// C0112 n,1,24,2 [本月（现金）结算收款额]不能为空，必须≥0
				if (c01.getC0112() == null
						|| c01.getC0112().compareTo(new BigDecimal(0)) < 0) {
					map.put("C0112", "[本月（现金）结算收款额] 不能为空且应当大于等于0 ");
				}
				// C0113 n,1,24,2 [本月非交易变动]不能为空
				if (c01.getC0113() == null) {
					map.put("C0113", "[本月非交易变动] 不能为空 ");
				} else {
					// C0113=C0116–C0110–(C0111–C0112)
					if (c01.getC0116() != null && c01.getC0110() != null
							&& c01.getC0111() != null && c01.getC0112() != null) {
						BigDecimal d = c01.getC0116().subtract(c01.getC0110())
								.subtract(c01.getC0111()).add(c01.getC0112());
						if (c01.getC0113().compareTo(d) != 0) {
							map
									.put("C0113",
											"[本月非交易变动] 应当满足C0113=C0116–C0110–(C0111–C0112) ");
						}
					}
					// C0113=C0114+C0115
					if (c01.getC0114() != null && c01.getC0115() != null) {
						BigDecimal d = c01.getC0114().add(c01.getC0115());
						if (c01.getC0113().compareTo(d) != 0) {
							map
									.put("C0113",
											"[本月非交易变动] 应当满足C0113=C0114+C0115 ");
						}
					}
				}
				// C0114 n,1,24,2 [本月非交易变动：其中注销、调整及重新分类至其他报表统计的金额]不能为空
				if (c01.getC0114() == null) {
					map.put("C0114", "[本月非交易变动：其中注销、调整及重新分类至其他报表统计的金额] 不能为空 ");
				}
				// C0115 n,1,24,2 [本月非交易变动：其中价值重估因素]不能为空
				if (c01.getC0115() == null) {
					map.put("C0115", "[本月非交易变动：其中价值重估因素] 不能为空 ");
				}
				// C0116 n,1,24,2 [本月末头寸市值]不能为空
				if (c01.getC0116() == null) {
					map.put("C0116", "[本月末头寸市值] 不能为空 ");
				}
				// C0117 s,1,3,3 [名义本金币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, c01.getC0117())) {
					String value = getKey(c01.getC0117(), CURRENCY);
					map.put("C0117", "[名义本金币种] [" + value + "] 无效 ");
				}
				// C0118 n,1,24,2 [本月末名义本金金额]不能为空，必须≥0
				if (c01.getC0118() == null
						|| c01.getC0118().compareTo(new BigDecimal(0)) < 0) {
					map.put("C0118", "[本月末名义本金金额] 不能为空且应当大于等于0 ");
				}
				// 问答四-1.2跨表和跨期核查规则-29
				String preBuocMonth = DateUtils.getPreMonth(c01.getBuocmonth(),
						"yyyyMM");
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(preBuocMonth)
						.append("' and INSTCODE = '").append(c01.getInstcode())
						.append("' and C0109 = '").append(c01.getC0109())
						.append("' and DATASTATUS in (").append(
								DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> 'D' ");
				SearchModel searchModel = new SearchModel();
				searchModel.setTableId("T_FAL_C01");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				List c01List = service.search(searchModel);
				if (CollectionUtils.isNotEmpty(c01List)) {
					for (Iterator it = c01List.iterator(); it.hasNext();) {
						Fal_C01Entity pre = (Fal_C01Entity) it.next();
						if (pre != null
								&& pre.getC0101().equals(c01.getC0101())
								&& pre.getC0104().equals(c01.getC0104())) {
							// 上一个报告期的“本月末头寸市值”
							BigDecimal preC0116 = pre.getC0116() == null ? new BigDecimal(
									0)
									: pre.getC0116();
							// 本报告期的“上月末头寸市值”
							BigDecimal c0110 = c01.getC0110() == null ? new BigDecimal(
									0)
									: c01.getC0110();
							// 比较 上一个报告期的“本月末头寸市值” 与 本报告期的“上月末头寸市值”
							if (preC0116.compareTo(c0110) != 0) {
								map.put("C0110", "[上月末头寸市值] 与上一个报告期的“本月末头寸市值”"
										+ preC0116.toString() + "不一致 ");
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
