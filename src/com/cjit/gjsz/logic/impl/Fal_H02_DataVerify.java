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
import com.cjit.gjsz.logic.model.Fal_H02Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_H02_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_H02_DataVerify() {
	}

	public Fal_H02_DataVerify(List dictionarys, List verifylList,
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
				Fal_H02Entity h02 = (Fal_H02Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, h02.getActiontype())) {
					String value = getKey(h02.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(h02.getActiontype())) {
					if (StringUtil.isEmpty(h02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(h02.getActiontype())) {
					if (!isNull(h02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (h02.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}

				// H0201CODE s,1,12 [居民委托人代码]不能为空，填写特殊机构代码
				// H0201 s,1,255 [非居民委托人名称]不能为空且不能超过255位字符
				// H0202 s,1,1,1 [居民委托人所属部门]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVESTORINST, h02.getH0202())) {
					String value = getKey(h02.getH0202(), INVESTORINST);
					map.put("H0202", "[居民委托人所属部门] [" + value + "] 无效 ");
				}
				// H0203CODE s,1,255 [居民委托人产品代码]不能为空且不能超过255位字符
				// H0203 s,1,255 [居民委托人产品名称]不能为空且不能超过255位字符
				// H0204 s,1,2,2 [投资工具类型]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVESTTOOLTYPE, h02.getH0204())) {
					String value = getKey(h02.getH0204(), INVESTTOOLTYPE);
					map.put("H0204", "[投资工具类型] [" + value + "] 无效 ");
				}
				// INVTYPE s,1,3,3 [投资品种类型]不能为空且需在字典表中有定义
				if ("11".equals(h02.getH0204()) || "12".equals(h02.getH0204())
						|| "13".equals(h02.getH0204())
						|| "14".equals(h02.getH0204())
						|| "15".equals(h02.getH0204())
						|| "16".equals(h02.getH0204())) {
					// “投资工具类型”填写11-16 时，“投资品种类型”填写“N/A”；
					if (!"N/A".equals(h02.getInvtype())) {
						map.put("INVTYPE",
								"[投资品种类型] 当“投资工具类型”填写11-16 时，“投资品种类型”填写“N/A” ");
					}
				} else {
					if (!verifyDictionaryValue(QDII_INVTYPE, h02.getInvtype())
							&& !"N/A".equals(h02.getInvtype())) {
						String value = getKey(h02.getInvtype(), QDII_INVTYPE);
						map.put("INVTYPE", "[投资品种类型] [" + value + "] 无效 ");
					} else if ("17".equals(h02.getH0204())) {
						// “投资工具类型”填写17 时，“投资品种类型”应填写“T11”或“T12”；
						if (!"T11".equals(h02.getInvtype())
								&& !"T12".equals(h02.getInvtype())) {
							map
									.put("INVTYPE",
											"[投资品种类型] 当“投资工具类型”填写17 时，“投资品种类型”应填写“T11”或“T12” ");
						}
					} else if ("18".equals(h02.getH0204())) {
						// “投资工具类型”填写18
						// 时，“投资品种类型”应在以下值中选择其一填写：“T21”、“T22”、“T23”、“T24”、“T25”；
						if (!"T21".equals(h02.getInvtype())
								&& !"T22".equals(h02.getInvtype())
								&& !"T23".equals(h02.getInvtype())
								&& !"T24".equals(h02.getInvtype())
								&& !"T25".equals(h02.getInvtype())) {
							map
									.put("INVTYPE",
											"[投资品种类型] 当“投资工具类型”填写18 时，“投资品种类型”应在以下值中选择其一填写：“T21”、“T22”、“T23”、“T24”、“T25” ");
						}
					} else if ("19".equals(h02.getH0204())) {
						// “投资工具类型”填写19
						// 时，“投资品种类型”应在以下值中选择其一填写：“T31”、“T32”、“T33”、“T34”、“T35”、“T36”。
						if (!"T31".equals(h02.getInvtype())
								&& !"T32".equals(h02.getInvtype())
								&& !"T33".equals(h02.getInvtype())
								&& !"T34".equals(h02.getInvtype())
								&& !"T35".equals(h02.getInvtype())
								&& !"T36".equals(h02.getInvtype())) {
							map
									.put(
											"INVTYPE",
											"[投资品种类型] 当“投资工具类型”填写19 时，“投资品种类型”应在以下值中选择其一填写：“T31”、“T32”、“T33”、“T34”、“T35”、“T36” ");
						}
					}
				}
				// H0205 s,1,1,1 [金融衍生产品的合约类别]不能为空且需在字典表中有定义
				if ("15".equals(h02.getH0204())) {
					if (h02.getH0205() == null) {
						map.put("H0205", "[金融衍生产品的合约类别] 不能为空 ");
					} else if (!verifyDictionaryValue(DERIVATIVECONTRACTCLASS,
							h02.getH0205())) {
						String value = getKey(h02.getH0205(),
								DERIVATIVECONTRACTCLASS);
						map.put("H0205", "[金融衍生产品的合约类别] [" + value + "] 无效 ");
					}
				} else {
					if (StringUtil.isNotEmpty(h02.getH0205())) {
						map.put("H0205", "[金融衍生产品的合约类别] 当[投资工具类型]=15时，本字段当为空 ");
					}
					// 问答四-1.1表内核查规则-6
					// 当H0204≠15时，H0215、H0221、H0222和H0223中任一项为负数，备注（REMARK）字段必填
					if (StringUtil.isEmpty(h02.getRemark())) {
						if (h02.getH0215() != null
								&& h02.getH0215().compareTo(new BigDecimal(0)) < 0) {
							map.put("REMARK",
									"当H0204≠15，且H0215为负数时，备注（REMARK）字段必填 ");
						}
						if (h02.getH0221() != null
								&& h02.getH0221().compareTo(new BigDecimal(0)) < 0) {
							map.put("REMARK",
									"当H0204≠15，且H0221为负数时，备注（REMARK）字段必填 ");
						}
						if (h02.getH0222() != null
								&& h02.getH0222().compareTo(new BigDecimal(0)) < 0) {
							map.put("REMARK",
									"当H0204≠15，且H0222为负数时，备注（REMARK）字段必填 ");
						}
						if (h02.getH0223() != null
								&& h02.getH0223().compareTo(new BigDecimal(0)) < 0) {
							map.put("REMARK",
									"当H0204≠15，且H0223为负数时，备注（REMARK）字段必填 ");
						}
					}
				}
				// H0206 s,1,1,1 [金融衍生产品的风险类别]不能为空且需在字典表中有定义
				if ("15".equals(h02.getH0204())) {
					if (StringUtil.isEmpty(h02.getH0206())) {
						map.put("H0206", "[金融衍生产品的风险类别] 不能为空 ");
					} else if (!verifyDictionaryValue(DERIVATIVERISKCLASS, h02
							.getH0206())) {
						String value = getKey(h02.getH0206(),
								DERIVATIVERISKCLASS);
						map.put("H0206", "[金融衍生产品的风险类别] [" + value + "] 无效 ");
					}
				} else {
					if (StringUtil.isNotEmpty(h02.getH0206())) {
						map.put("H0206", "[金融衍生产品的风险类别] 当[投资工具类型]=15时，本字段当为空 ");
					}
				}
				// H0207 s,1,255 [投资工具代码（逐支报送使用）]
				if ("17".equals(h02.getH0204()) || "18".equals(h02.getH0204())
						|| "19".equals(h02.getH0204())) {
					if (!"N/A".equalsIgnoreCase(h02.getH0207())) {
						map
								.put("H0207",
										"[投资工具代码（逐支报送使用）] 当[投资工具类型]=17、18或19时，本字段当为'N/A' ");
					}
				}
				// H0208 s,1,255 [投资工具发行人名称（逐支报送使用）]
				if ("N/A".equalsIgnoreCase(h02.getH0207())
						&& StringUtil.isNotEmpty(h02.getH0208())) {
					map
							.put("H0208",
									"[投资工具发行人名称（逐支报送使用）] 当[投资工具代码（逐支报送使用）]为'N/A'时，本字段当为空 ");
				}
				// H0209 s,1,3,3 [投资工具发行市场]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(COUNTRY, h02.getH0209())) {
					String value = getKey(h02.getH0209(), COUNTRY);
					map.put("H0209", "[投资工具发行市场] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(h02.getH0209())
						|| "N/A".equals(h02.getH0209())) {
					map.put("H0209", "[投资工具发行市场] 不能选择CHN或N/A ");
				}
				// H0210 s,1,3,3 [投资工具发行人所属国家/地区]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(COUNTRY, h02.getH0210())) {
					String value = getKey(h02.getH0210(), COUNTRY);
					map.put("H0210", "[投资工具发行人所属国家/地区] [" + value + "] 无效 ");
				}
				// H0211 s,1,1,1 [投资工具发行人所属部门]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(INVESTORINST, h02.getH0211())) {
					String value = getKey(h02.getH0211(), INVESTORINST);
					map.put("H0211", "[投资工具发行人所属部门] [" + value + "] 无效 ");
				}
				// H0212 s,1,1,1 [投资工具发行人与居民委托人的关系]不能为空且需在字典表中有定义
				if (!verifyDictionaryValue(OPPOSITERELA, h02.getH0212())) {
					String value = getKey(h02.getH0212(), OPPOSITERELA);
					map.put("H0212", "[投资工具发行人与居民委托人的关系] [" + value + "] 无效 ");
				}
				// H0213 s,1,1,1 [投资工具的原始期限]不能为空且需在字典表中有定义
				if ("11".equals(h02.getH0204()) || "12".equals(h02.getH0204())
						|| "13".equals(h02.getH0204())
						|| "14".equals(h02.getH0204())
						|| "15".equals(h02.getH0204())) {
					if (StringUtil.isNotEmpty(h02.getH0213())) {
						map.put("H0213",
								"[投资工具的原始期限] 当[投资工具类型]=11、12、13、14或15时，此项应为空 ");
					}
				} else if ("16".equals(h02.getH0204())
						|| "17".equals(h02.getH0204())
						|| "18".equals(h02.getH0204())
						|| "19".equals(h02.getH0204())) {
					if (StringUtil.isEmpty(h02.getH0213())) {
						map.put("H0213", "[投资工具的原始期限] 不能为空 ");
					} else if (!verifyDictionaryValue(ORIDEADLINE, h02
							.getH0213())) {
						String value = getKey(h02.getH0213(), ORIDEADLINE);
						map.put("H0213", "[投资工具的原始期限] [" + value + "] 无效 ");
					}
				}
				// H0214 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, h02.getH0214())) {
					String value = getKey(h02.getH0214(), CURRENCY);
					map.put("H0214", "[原始币种] [" + value + "] 无效 ");
				}
				// H0215 n,1,24,2 [上月末市值]不能为空
				if (h02.getH0215() == null) {
					map.put("H0215", "[上月末市值] 不能为空 ");
				} else {
					// 问答四-1.1表内核查规则-65 取消
					// if ("15".equals(h02.getH0204())
					// && h02.getH0215().compareTo(new BigDecimal(0)) < 0) {
					// map.put("H0215", "[上月末市值] 当[投资工具类型]=15时，此项应大于等于0 ");
					// }
				}
				// H0216 n,1,24,2 [本月买入/申购/(现金)结算付款额]不能为空，必须≥0
				if (h02.getH0216() == null
						|| h02.getH0216().compareTo(new BigDecimal(0)) < 0) {
					map.put("H0216", "[本月买入/申购/(现金)结算付款额] 不能为空且应当大于等于0 ");
				}
				// H0217 n,1,24,2 [本月卖出/赎回/(现金)结算收款额]不能为空，必须≥0
				if (h02.getH0217() == null
						|| h02.getH0217().compareTo(new BigDecimal(0)) < 0) {
					map.put("H0217", "[本月卖出/赎回/(现金)结算收款额] 不能为空且应当大于等于0 ");
				}
				// H0218 n,1,24,2 [本月非交易变动]不能为空
				if (h02.getH0218() == null) {
					map.put("H0218", "[本月非交易变动] 不能为空 ");
				} else {
					// H0218=H0221-H0215-(H0216-H0217)
					BigDecimal d1 = h02.getH0221().subtract(h02.getH0215())
							.subtract(h02.getH0216()).add(h02.getH0217());
					if (h02.getH0218().compareTo(d1) != 0) {
						map
								.put("H0218",
										"[本月非交易变动] 应满足 H0218=H0221-H0215-(H0216-H0217) ");
					}
					// H0218=H0219+H0220
					BigDecimal d2 = h02.getH0219().add(h02.getH0220());
					if (h02.getH0218().compareTo(d2) != 0) {
						map.put("H0218", "[本月非交易变动] 应满足 H0218=H0219+H0220 ");
					}
				}
				// H0219 n,1,24,2 [本月非交易变动：其中注销或重新分类至其他项目统计的金额]不能为空
				if (h02.getH0219() == null) {
					map.put("H0219", "[本月非交易变动：其中注销或重新分类至其他项目统计的金额] 不能为空 ");
				}
				// H0220 n,1,24,2 [本月非交易变动：其中价值重估因素]不能为空
				if (h02.getH0220() == null) {
					map.put("H0220", "[本月非交易变动：其中价值重估因素] 不能为空 ");
				}
				// H0221 n,1,24,2 [本月末市值]不能为空
				if (h02.getH0221() == null) {
					map.put("H0221", "[本月末市值] 不能为空 ");
				} else {
					// 问答四-1.1表内核查规则-66 取消
					// if (!"15".equals(h02.getH0204())
					// && h02.getH0221().compareTo(new BigDecimal(0)) < 0) {
					// map.put("H0221",
					// "[本月末市值] 当[投资工具类型]≠15（金融衍生产品）时，本字段应当大于等于0 ");
					// }
				}
				// H0222 n,1,24,2 [本月末市值：其中剩余期限在一年及以下]
				if (h02.getH0222() == null) {
					map.put("H0222", "[本月末市值：其中剩余期限在一年及以下] 不能为空 ");
				} else {
					// 问答四-1.1表内核查规则-67
					if ("16".equals(h02.getH0204())
							|| "17".equals(h02.getH0204())
							|| "18".equals(h02.getH0204())
							|| "19".equals(h02.getH0204())) {
						if ("1".equals(h02.getH0213())
								&& h02.getH0221() != null
								&& h02.getH0222().compareTo(h02.getH0221()) != 0) {
							map.put("H0222",
									"[本月末市值：其中剩余期限在一年及以下] 应当等于[本月末市值] ");
						} else if ("2".equals(h02.getH0213())
								&& h02.getH0222().compareTo(h02.getH0221()) > 0) {
							// 问答四-1.1表内核查规则-67 取消
							// map.put("H0222", "[本月末市值：其中剩余期限在一年及以下]
							// 应当小于等于[本月末市值] ");
						}
					} else if ("11".equals(h02.getH0204())
							|| "12".equals(h02.getH0204())
							|| "13".equals(h02.getH0204())
							|| "14".equals(h02.getH0204())
							|| "15".equals(h02.getH0204())) {
						if (h02.getH0222().compareTo(new BigDecimal(0)) != 0) {
							map.put("H0222", "[本月末市值：其中剩余期限在一年及以下] 应当等于0 ");
						}
					}
				}
				// H0223 n,1,24,2 [本月红利或利息收入]不能为空，必须≥0
				if (h02.getH0223() == null
						|| h02.getH0223().compareTo(new BigDecimal(0)) < 0) {
					map.put("H0223", "[本月红利或利息收入] 不能为空且应当大于等于0 ");
				}
				// H0224 s,1,3,3 [金融衍生产品的名义本金币种]不能为空且需在字典表中有定义 CURRENCY
				if ("15".equals(h02.getH0204())) {
					if (StringUtil.isEmpty(h02.getH0224())) {
						map.put("H0224", "[金融衍生产品的名义本金币种] 不能为空 ");
					} else if (!verifyDictionaryValue(CURRENCY, h02.getH0224())
							&& !"N/A".equals(h02.getH0224())) {
						String value = getKey(h02.getH0224(), CURRENCY);
						map.put("H0224", "[金融衍生产品的名义本金币种] [" + value + "] 无效 ");
					}
				} else {
					if (!"N/A".equals(h02.getH0224())) {
						map
								.put("H0224",
										"[金融衍生产品的名义本金币种] 当H0204≠15 时，此字段为N/A ");
					}
				}
				// H0225 n,1,24,2 [金融衍生产品的本月末名义本金金额]不能为空，必须≥0
				if (h02.getH0225() == null) {
					map.put("H0225", "[金融衍生产品的本月末名义本金金额] 不能为空 ");
				} else {
					if ("15".equals(h02.getH0204())
							&& h02.getH0225().compareTo(new BigDecimal(0)) < 0) {
						map.put("H0225", "[金融衍生产品的本月末名义本金金额] 不能为空且应当大于等于0 ");
					} else if (!"15".equals(h02.getH0204())
							&& h02.getH0225().compareTo(new BigDecimal(0)) != 0) {
						map.put("H0225", "[金融衍生产品的本月末名义本金金额] 应当等于0 ");
					}
				}
				// 问答四-1.2跨表和跨期核查规则-29
				String preBuocMonth = DateUtils.getPreMonth(h02.getBuocmonth(),
						"yyyyMM");
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(preBuocMonth)
						.append("' and INSTCODE = '").append(h02.getInstcode())
						.append("' and H0214 = '").append(h02.getH0214())
						.append("' and DATASTATUS in (").append(
								DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> 'D' ");
				SearchModel searchModel = new SearchModel();
				searchModel.setTableId("T_FAL_H02");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				List h02List = service.search(searchModel);
				if (CollectionUtils.isNotEmpty(h02List)) {
					for (Iterator it = h02List.iterator(); it.hasNext();) {
						Fal_H02Entity pre = (Fal_H02Entity) it.next();
						if (pre != null
								&& pre.getH0201code()
										.equals(h02.getH0201code())
								&& pre.getH0203code()
										.equals(h02.getH0203code())) {
							// 上一个报告期的“本月末市值”
							BigDecimal preH0221 = pre.getH0221() == null ? new BigDecimal(
									0)
									: pre.getH0221();
							// 本报告期的“上月末市值”
							BigDecimal h0215 = h02.getH0215() == null ? new BigDecimal(
									0)
									: h02.getH0215();
							// 比较 上一个报告期的“本月末市值” 与 本报告期的“上月末市值”
							if (preH0221.compareTo(h0215) != 0) {
								map.put("H0215", "[上月末市值] 与上一个报告期的“本月末市值”"
										+ preH0221.toString() + "不一致 ");
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
