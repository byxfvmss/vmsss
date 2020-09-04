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
import com.cjit.gjsz.logic.model.Fal_H01Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_H01_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_H01_DataVerify() {
	}

	public Fal_H01_DataVerify(List dictionarys, List verifylList,
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
				Fal_H01Entity h01 = (Fal_H01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, h01.getActiontype())) {
					String value = getKey(h01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(h01.getActiontype())) {
					if (StringUtil.isEmpty(h01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(h01.getActiontype())) {
					if (!isNull(h01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (h01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// H0101CODE s,1,9 [非居民委托人代码]不能为空，填写特殊机构代码
				// H0101 s,1,255 [非居民委托人名称]
				// H0102 s,1,3,3 [非居民委托人所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, h01.getH0102())) {
					String value = getKey(h01.getH0102(), COUNTRY);
					map.put("H0102", "[非居民委托人所属国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(h01.getH0102())
						|| "N/A".equals(h01.getH0102())) {
					map.put("H0102", "[非居民委托人所属国家/地区] 不能选择CHN或N/A ");
				}
				// H0103 s,1,1,1 [非居民委托人所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, h01.getH0103())) {
					String value = getKey(h01.getH0103(), INVESTORINST);
					map.put("H0103", "[非居民委托人所属部门] [" + value + "] 无效 ");
				}
				// H0104CODE s,1,20 [业务编号]不能为空，填写外汇局签发的QFII/RQFII 产品编号
				// H0104 s,1,255 [非居民委托人产品名称]不能为空且不能超过255位字符
				// H0105 s,1,2,2 [投资工具类型]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVESTTOOLTYPE, h01.getH0105())) {
					String value = getKey(h01.getH0105(), INVESTTOOLTYPE);
					map.put("H0105", "[投资工具类型] [" + value + "] 无效 ");
				}
				// INVTYPE s,1,3,3 [投资品种类型]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(QFII_RQFII_INVTYPE, h01.getInvtype())) {
					String value = getKey(h01.getInvtype(), QFII_RQFII_INVTYPE);
					map.put("INVTYPE", "[投资品种类型] [" + value + "] 无效 ");
				}
				// H0106 s,0,1 如H0105=15，则为必填项，见金融衍生产品的合约类别代码表。如H0105≠15，则此字段为空。
				if ("15".equals(h01.getH0105())) {
					// [投资工具类型]选择15-金融衍生产品
					if (StringUtil.isEmpty(h01.getH0106())) {
						map.put("H0106",
								"[金融衍生产品的合约类别] 当投资工具类型选择15-金融衍生产品时，此项不能为空 ");
					} else if (!verifyDictionaryValue(DERIVATIVECONTRACTCLASS,
							h01.getH0106())) {
						String value = getKey(h01.getH0106(),
								DERIVATIVECONTRACTCLASS);
						map.put("H0106", "[金融衍生产品的合约类别] [" + value + "] 无效 ");
					}
				} else {
					// [投资工具类型]非15-金融衍生产品
					if (StringUtil.isNotEmpty(h01.getH0106())) {
						map.put("H0106",
								"[金融衍生产品的合约类别] 当投资工具类型不是15-金融衍生产品时，此项应为空 ");
					}
				}
				// H0107 s,0,1 如H0105=15，则为必填项，见金融衍生产品的风险类别代码表。如H0105≠15，则此字段为空
				if ("15".equals(h01.getH0105())) {
					// [投资工具类型]选择15-金融衍生产品
					if (StringUtil.isEmpty(h01.getH0107())) {
						map.put("H0107",
								"[金融衍生产品的风险类别] 当投资工具类型选择15-金融衍生产品时，此项不能为空 ");
					} else if (!verifyDictionaryValue(DERIVATIVERISKCLASS, h01
							.getH0107())) {
						String value = getKey(h01.getH0107(),
								DERIVATIVERISKCLASS);
						map.put("H0107", "[金融衍生产品的风险类别] [" + value + "] 无效 ");
					}
				} else {
					// [投资工具类型]非15-金融衍生产品
					if (StringUtil.isNotEmpty(h01.getH0107())) {
						map.put("H0107",
								"[金融衍生产品的风险类别] 当投资工具类型不是15-金融衍生产品时，此项应为空 ");
					}
				}
				// H0108 s,1,255 [投资工具代码（逐支报送使用）]
				if ("17".equals(h01.getH0105()) || "18".equals(h01.getH0105())
						|| "19".equals(h01.getH0105())) {
					if (!"N/A".equals(h01.getH0108())) {
						map
								.put("H0108",
										"[投资工具代码（逐支报送使用）] 当投资工具类型所选择值为17、18或19时，此项应填写“N/A” ");
					}
				}
				// H0109 s,0,255 [投资工具发行人名称（逐支报送使用）]
				if (!"N/A".equals(h01.getH0108())) {
					// 如H0108≠N/A，则填写实际名称
					if (StringUtil.isEmpty(h01.getH0109())) {
						map
								.put("H0109",
										"[投资工具发行人名称（逐支报送使用）] 当投资工具代码（逐支报送使用）不为N/A时，此项需填写实际名称 ");
					}
				} else {
					// 如H0108=N/A，本字段为空
					if (StringUtil.isNotEmpty(h01.getH0109())) {
						map
								.put("H0109",
										"[投资工具发行人名称（逐支报送使用）] 当投资工具代码（逐支报送使用）为N/A时，此项应为空 ");
					}
				}
				// H0110 s,1,1,1 [投资工具发行人（对手方）所属部门]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVESTORINST, h01.getH0110())) {
					String value = getKey(h01.getH0110(), INVESTORINST);
					map.put("H0110", "[投资工具发行人（对手方）所属部门] [" + value + "] 无效 ");
				}
				// H0111 s,1,1,1 [非居民委托人与境内发行人（对手方）的关系]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(OPPOSITERELA, h01.getH0111())) {
					String value = getKey(h01.getH0111(), OPPOSITERELA);
					map.put("H0111", "[非居民委托人与境内发行人（对手方）的关系] [" + value
							+ "] 无效 ");
				}
				// H0112 s,0,1 [投资工具的原始期限]
				// 当H0105=11、12、13、14 或15 时，该项为空；
				// 当H0105=16、17、18 或19 时，该项必须2选1
				if ("11".equals(h01.getH0105()) || "12".equals(h01.getH0105())
						|| "13".equals(h01.getH0105())
						|| "14".equals(h01.getH0105())
						|| "15".equals(h01.getH0105())) {
					if (StringUtil.isNotEmpty(h01.getH0112())) {
						map
								.put("H0112",
										"[投资工具的原始期限] 当投资工具类型所选择值为11、12、13、14或15时，此项应为空 ");
					}
				} else if ("16".equals(h01.getH0105())
						|| "17".equals(h01.getH0105())
						|| "18".equals(h01.getH0105())
						|| "19".equals(h01.getH0105())) {
					if (StringUtil.isEmpty(h01.getH0112())) {
						map.put("H0112",
								"[投资工具的原始期限] 当投资工具类型所选择值为16、17、18或19时，此项不能为空 ");
					} else if (!verifyDictionaryValue(ORIDEADLINE, h01
							.getH0112())) {
						String value = getKey(h01.getH0112(), ORIDEADLINE);
						map.put("H0112", "[投资工具的原始期限] [" + value + "] 无效 ");
					}
				}
				// H0113 s,1,3,3 [原始币种]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(CURRENCY, h01.getH0113())) {
					String value = getKey(h01.getH0113(), CURRENCY);
					map.put("H0113", "[原始币种] [" + value + "] 无效 ");
				}
				// H0114 n,1,24,2 [上月末市值]不能为空
				if (h01.getH0114() == null) {
					map.put("H0114", "[上月末市值] 不能为空 ");
				} else if (!"15".equals(h01.getH0105())
						&& h01.getH0114().compareTo(new BigDecimal(0)) < 0) {
					// 问答四-1.1表内核查规则-61 取消
					// map.put("H0114", "[上月末市值] 当[投资工具类型]≠15(金融衍生产品)时，必须大于0 ");
				}
				// H0115 n,1,24,2 [本月买入/申购/(现金)结算付款额]不能为空，必须≥0
				if (h01.getH0115() == null
						|| h01.getH0115().compareTo(new BigDecimal(0)) < 0) {
					map.put("H0115", "[本月买入/申购/(现金)结算付款额] 不能为空且应当大于等于0 ");
				}
				// H0116 n,1,24,2 [本月卖出/赎回/(现金)结算收款额]不能为空，必须≥0
				if (h01.getH0116() == null
						|| h01.getH0116().compareTo(new BigDecimal(0)) < 0) {
					map.put("H0116", "[本月卖出/赎回/(现金)结算收款额] 不能为空且应当大于等于0 ");
				}
				// H0117 n,1,24,2 [本月非交易变动]不能为空
				if (h01.getH0117() == null) {
					map.put("H0117", "[本月非交易变动] 不能为空 ");
				} else {
					// H0117=H0120-H0114-（H0115-H0116）
					if (h01.getH0117().compareTo(
							h01.getH0120().subtract(h01.getH0114()).subtract(
									h01.getH0115()).add(h01.getH0116())) != 0) {
						map
								.put("H0117",
										"[本月非交易变动] 应满足H0117=H0120-H0114-(H0115-H0116) ");
					}
					// H0117=H0118+H0119
					if (h01.getH0117().compareTo(
							h01.getH0118().add(h01.getH0119())) != 0) {
						map.put("H0117", "[本月非交易变动] 应满足H0117=H0118+H0119 ");
					}
				}
				// H0118 n,1,24,2 [本月非交易变动：其中注销或重新分类至其他项目统计的金额]不能为空
				if (h01.getH0118() == null) {
					map.put("H0118", "[本月非交易变动：其中注销或重新分类至其他项目统计的金额] 不能为空 ");
				}
				// H0119 n,1,24,2 [本月非交易变动：其中价值重估因素]不能为空
				if (h01.getH0119() == null) {
					map.put("H0119", "[本月非交易变动：其中价值重估因素] 不能为空 ");
				}
				// H0120 n,1,24,2 [本月末市值]不能为空
				if (h01.getH0120() == null) {
					map.put("H0120", "[本月末市值] 不能为空 ");
				} else if (!"15".equals(h01.getH0105())
						&& h01.getH0120().compareTo(new BigDecimal(0)) < 0) {
					// 问答四-1.1表内核查规则-62 取消
					// map.put("H0120", "[本月末市值] 当[投资工具类型]≠15时，本字段必须大于等于0 ");
				}
				// H0121 n,1,24,2 [本月末市值：其中剩余期限在一年及以下]
				if (h01.getH0121() == null) {
					map.put("H0121", "[本月末市值：其中剩余期限在一年及以下] 不能为空 ");
				} else {
					// 问答四-1.1表内核查规则-63
					if ("16".equals(h01.getH0105())
							|| "17".equals(h01.getH0105())
							|| "18".equals(h01.getH0105())
							|| "19".equals(h01.getH0105())) {
						// 当H0105=16、17、18或19时，且H0112=1时，H0120=H0121
						if ("1".equals(h01.getH0112())
								&& h01.getH0120() != null
								&& h01.getH0120().compareTo(h01.getH0121()) != 0) {
							map
									.put("H0121",
											"当H0105=16、17、18或19时，且H0112=1时，H0120=H0121");
						} else if ("2".equals(h01.getH0112())
								&& h01.getH0120().compareTo(h01.getH0121()) < 0) {
							// 问答四-1.1表内核查规则-63 取消
							// 当H0112=2时，H0120≥H0121
							// map.put("H0121", "[本月末市值：其中剩余期限在一年及以下]
							// 当[投资工具的原始期限]在一年以上时，应小于等于本月末市值");
						}
					} else if ("11".equals(h01.getH0105())
							|| "12".equals(h01.getH0105())
							|| "13".equals(h01.getH0105())
							|| "14".equals(h01.getH0105())
							|| "15".equals(h01.getH0105())) {
						// 当H0105=11、12、13、14或15时，该项为0
						if (h01.getH0121().compareTo(new BigDecimal(0)) != 0) {
							map
									.put("H0121",
											"[本月末市值：其中剩余期限在一年及以下] 当[投资工具类型]=11、12、13、14或15时，应当等于0 ");
						}
					}
				}
				// H0122 n,1,24,2 [本月红利或利息收入]不能为空，必须≥0
				if (h01.getH0122() == null
						|| h01.getH0122().compareTo(new BigDecimal(0)) < 0) {
					map.put("H0122", "[本月红利或利息收入] 不能为空且应当大于等于0 ");
				}
				// H0123 n,1,24,2 [本月未实现收益（仅适用于货币市场基金份额）]不能为空
				if (h01.getH0123() == null
						|| h01.getH0123().compareTo(new BigDecimal(0)) < 0) {
					map.put("H0123", "[本月未实现收益（仅适用于货币市场基金份额）] 不能为空且应当大于等于0 ");
				} else if (!"13".equals(h01.getH0105())
						&& h01.getH0123().compareTo(new BigDecimal(0)) != 0) {
					map.put("H0123",
							"[本月未实现收益（仅适用于货币市场基金份额）] 当[投资工具类型]≠13时，本字段为0");
				}
				// H0124 s,1,3,3 [金融衍生产品的名义本金币种]不能为空且需在字典表中有定义
				if ("15".equals(h01.getH0105())) {
					if (!verifyDictionaryValue(CURRENCY, h01.getH0124())) {
						String value = getKey(h01.getH0124(), CURRENCY);
						map.put("H0124", "[金融衍生产品的名义本金币种] [" + value + "] 无效 ");
					}
				} else if (!"15".equals(h01.getH0105())
						&& !"N/A".equals(h01.getH0124())) {
					map
							.put("H0124",
									"[金融衍生产品的名义本金币种] 当[投资工具类型]≠15时，本字段填“N/A” ");
				}
				// H0125 n,1,24,2 [金融衍生产品的本月末名义本金金额]不能为空，必须≥0
				if (h01.getH0125() == null
						|| h01.getH0125().compareTo(new BigDecimal(0)) < 0) {
					map.put("H0125", "[金融衍生产品的本月末名义本金金额] 不能为空且应当大于等于0 ");
				} else {
					if ("15".equals(h01.getH0105())
							&& h01.getH0125().compareTo(new BigDecimal(0)) < 0) {
						// 当H0105=15 时，必须≥0
						map.put("H0125",
								"[金融衍生产品的本月末名义本金金额] 当[投资工具类型]=15时，本字段必须大于等于0 ");
					} else if (!"15".equals(h01.getH0105())
							&& h01.getH0125().compareTo(new BigDecimal(0)) != 0) {
						// 当H0105≠15 时，本字段为0
						map.put("H0125",
								"[金融衍生产品的本月末名义本金金额] 当[投资工具类型]≠15时，本字段为0 ");
					}
				}
				// REMARK
				if (StringUtil.isEmpty(h01.getRemark())) {
					// 问答四-1.1表内核查规则-6
					if (!"15".equals(h01.getH0105())) {
						// 当H0105≠15时，H0114、H0120、H0121和H0122中任一项为负数，备注（REMARK）字段必填
						if ((h01.getH0114() != null && h01.getH0114()
								.compareTo(new BigDecimal(0)) < 0)
								|| (h01.getH0120() != null && h01.getH0120()
										.compareTo(new BigDecimal(0)) < 0)
								|| (h01.getH0121() != null && h01.getH0121()
										.compareTo(new BigDecimal(0)) < 0)
								|| (h01.getH0122() != null && h01.getH0122()
										.compareTo(new BigDecimal(0)) < 0)) {
							map
									.put("REMARK",
											"当H0105≠15时，H0114、H0120、H0121和H0122中任一项为负数，备注（REMARK）字段必填");
						}
					}
				}
				// 问答四-1.2跨表和跨期核查规则-29
				String preBuocMonth = DateUtils.getPreMonth(h01.getBuocmonth(),
						"yyyyMM");
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(preBuocMonth)
						.append("' and INSTCODE = '").append(h01.getInstcode())
						.append("' and H0113 = '").append(h01.getH0113())
						.append("' and DATASTATUS in (").append(
								DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> 'D' ");
				SearchModel searchModel = new SearchModel();
				searchModel.setTableId("T_FAL_H01");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				List h01List = service.search(searchModel);
				if (CollectionUtils.isNotEmpty(h01List)) {
					for (Iterator it = h01List.iterator(); it.hasNext();) {
						Fal_H01Entity pre = (Fal_H01Entity) it.next();
						if (pre != null
								&& pre.getH0101code()
										.equals(h01.getH0101code())) {
							// 上一个报告期的“本月末市值”
							BigDecimal preH0120 = pre.getH0120() == null ? new BigDecimal(
									0)
									: pre.getH0120();
							// 本报告期的“上月末市值”
							BigDecimal h0114 = h01.getH0114() == null ? new BigDecimal(
									0)
									: h01.getH0114();
							// 比较 上一个报告期的“本月末市值” 与 本报告期的“上月末市值”
							if (preH0120.compareTo(h0114) != 0) {
								map.put("H0114", "[上月末市值] 与上一个报告期的“本月末市值”"
										+ preH0120.toString() + "不一致 ");
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
