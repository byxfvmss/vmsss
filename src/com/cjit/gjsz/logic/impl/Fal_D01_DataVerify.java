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
import com.cjit.gjsz.logic.model.Fal_D01Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_D01_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_D01_DataVerify() {
	}

	public Fal_D01_DataVerify(List dictionarys, List verifylList,
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
				Fal_D01Entity d01 = (Fal_D01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, d01.getActiontype())) {
					String value = getKey(d01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(d01.getActiontype())) {
					if (StringUtil.isEmpty(d01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(d01.getActiontype())) {
					if (!isNull(d01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (d01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// D0101 业务类别
				if (!verifyDictionaryValue(BUSITYPE, d01.getD0101())) {
					String value = getKey(d01.getD0101(), BUSITYPE);
					map.put("D0101", "[业务类别] [" + value + "] 无效 ");
				} else if ("0".equals(d01.getD0101())) {
					// 问答四-1.1表内核查规则-48
					if (!"2".equals(d01.getD0103())) {
						map.put("D0103",
								"当D0101业务类别=0时，则D0103=2（中央银行），D0104=4,D0105=1");
					}
					if (!"4".equals(d01.getD0104())) {
						map.put("D0104",
								"当D0101业务类别=0时，则D0103=2（中央银行），D0104=4,D0105=1");
					}
					if (!"1".equals(d01.getD0105())) {
						map.put("D0105",
								"当D0101业务类别=0时，则D0103=2（中央银行），D0104=4,D0105=1");
					}
					// 问答四-1.1表内核查规则-49
					if (CURRENCY_EUR.equals(d01.getD0106())
							&& !COUNTRY_DEU.equals(d01.getD0102())) {
						map
								.put("D0102",
										"当D0101业务类别=0且D0106原始币种为EUR（欧元），则D0102对方国家/地区必须=DEU（德国）");
					}
				}
				// D0102 对方国家/地区(见国家和地区代码的3 位字母代码)
				if (!verifyDictionaryValue(COUNTRY, d01.getD0102())) {
					String value = getKey(d01.getD0102(), COUNTRY);
					map.put("D0102", "[对方国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(d01.getD0102())
						|| "N/A".equals(d01.getD0102())) {
					map.put("D0102", "[对方国家/地区] 不能选择CHN或N/A ");
				}
				// D0103 对方部门(见投资者（被投资者）部门代码表)
				if (!verifyDictionaryValue(INVESTORINST, d01.getD0103())) {
					String value = getKey(d01.getD0103(), INVESTORINST);
					map.put("D0103", "[对方部门] [" + value + "] 无效 ");
				}
				// D0104 对方与本机构的关系(见对方与本机构/被代理居民机构/委托人的关系代码表)
				if (!verifyDictionaryValue(OPPOSITERELA, d01.getD0104())) {
					String value = getKey(d01.getD0104(), OPPOSITERELA);
					map.put("D0104", "[对方与本机构的关系] [" + value + "] 无效 ");
				}
				// D0105 原始期限
				if (!verifyDictionaryValue(ORIDEADLINE, d01.getD0105())) {
					String value = getKey(d01.getD0105(), ORIDEADLINE);
					map.put("D0105", "[原始期限] [" + value + "] 无效 ");
				}
				// D0106 原始币种(见币种代码表。
				if (!verifyDictionaryValue(CURRENCY, d01.getD0106())) {
					String value = getKey(d01.getD0106(), CURRENCY);
					map.put("D0106", "[原始币种] [" + value + "] 无效 ");
				}
				// D0107 [上月末本金余额]不能为空且大于等于0
				if (d01.getD0107() == null
						|| d01.getD0107().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0107", "[上月末本金余额] 不能为空且应当大于等于0 ");
				}
				// D0108 [上月末应收利息余额]不能为空
				if (d01.getD0108() == null) {
					map.put("D0108", "[上月末应收利息余额] 不能为空 ");
				}
				// D0109 [本月末本金余额]不能为空且大于等于0
				if (d01.getD0109() == null
						|| d01.getD0109().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0109", "[本月末本金余额] 不能为空且应当大于等于0 ");
				}
				// D0110 [本月末本金余额：其中剩余期限在一年及以下]不能为空且大于等于0
				if (d01.getD0110() == null
						|| d01.getD0110().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0110", "[本月末本金余额：其中剩余期限在一年及以下] 不能为空且应当大于等于0 ");
				} else {
					if ("1".equals(d01.getD0105())
							&& d01.getD0110().compareTo(d01.getD0109()) != 0) {
						map.put("D0110", "[本月末本金余额：其中剩余期限在一年及以下] 应等于本月末本金余额 ");
					} else if ("2".equals(d01.getD0105())
							&& d01.getD0110().compareTo(d01.getD0109()) > 0) {
						map
								.put("D0110",
										"[本月末本金余额：其中剩余期限在一年及以下] 应小于等于本月末本金余额 ");
					}
				}
				// D0111 [本月末应收利息余额]不能为空
				if (d01.getD0111() == null) {
					map.put("D0111", "[本月末应收利息余额] 不能为空 ");
				}
				// D0112 [本月非交易变动]不能为空
				if (d01.getD0112() == null) {
					map.put("D0112", "[本月非交易变动] 不能为空 ");
				}
				// D0113 [本月净发生额]不能为空
				if (d01.getD0113() == null) {
					map.put("D0113", "[本月净发生额] 不能为空 ");
				} else {
					// D0113+D0112=(D0109+D0111)-(D0107+D0108)
					BigDecimal d0113 = d01.getD0109().add(d01.getD0111())
							.subtract(d01.getD0107()).subtract(d01.getD0108())
							.subtract(d01.getD0112());
					if (d01.getD0113().compareTo(d0113) != 0) {
						map.put("D0113",
								"[本月净发生额] 应满足 D0113+D0112=(D0109+D0111)-(D0107+D0108) 理论值为 "
										+ d0113.toString());
					}
				}
				// D0114 [本月利息收入]不能为空且大于等于0
				if (d01.getD0114() == null
						|| d01.getD0114().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0114", "[本月利息收入] 不能为空且应当大于等于0 ");
				}
				// REMARK
				if (StringUtil.isEmpty(d01.getRemark())) {
					// 问答四-1.1表内核查规则-6
					if (d01.getD0107() != null
							&& d01.getD0107().compareTo(new BigDecimal(0)) < 0) {
						map.put("REMARK", "当D0107为负数时，备注字段必填");
					} else if (d01.getD0109() != null
							&& d01.getD0109().compareTo(new BigDecimal(0)) < 0) {
						map.put("REMARK", "当D0109为负数时，备注字段必填");
					} else if (d01.getD0110() != null
							&& d01.getD0110().compareTo(new BigDecimal(0)) < 0) {
						map.put("REMARK", "当D0110为负数时，备注字段必填");
					} else if (d01.getD0114() != null
							&& d01.getD0114().compareTo(new BigDecimal(0)) < 0) {
						map.put("REMARK", "当D0114为负数时，备注字段必填");
					} else if (d01.getD0109() != null
							&& d01.getD0110() != null
							&& d01.getD0109().abs().compareTo(
									d01.getD0110().abs()) < 0) {
						map.put("REMARK", "当|D0109|<|D0110|时，备注字段必填");
					}
				}
				// 问答四-1.2跨表和跨期核查规则-30
				String preBuocMonth = DateUtils.getPreMonth(d01.getBuocmonth(),
						"yyyyMM");
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(preBuocMonth)
						.append("' and INSTCODE = '").append(d01.getInstcode())
						.append("' and D0106 = '").append(d01.getD0106())
						.append("' and DATASTATUS in (").append(
								DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> 'D' ");
				SearchModel searchModel = new SearchModel();
				searchModel.setTableId("T_FAL_D01");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				List d01List = service.search(searchModel);
				if (CollectionUtils.isNotEmpty(d01List)) {
					for (Iterator it = d01List.iterator(); it.hasNext();) {
						Fal_D01Entity pre = (Fal_D01Entity) it.next();
						if (pre != null
								&& pre.getD0101().equals(d01.getD0101())
								&& pre.getD0102().equals(d01.getD0102())
								&& pre.getD0103().equals(d01.getD0103())) {
							// 上一个报告期的“本月末本金余额”
							BigDecimal preD0109 = pre.getD0109() == null ? new BigDecimal(
									0)
									: pre.getD0109();
							// 上一个报告期的“本月末应收利息余额”
							BigDecimal preD0111 = pre.getD0111() == null ? new BigDecimal(
									0)
									: pre.getD0111();
							// 本报告期的“上月末本金余额”
							BigDecimal d0107 = d01.getD0107() == null ? new BigDecimal(
									0)
									: d01.getD0107();
							// 本报告期的“上月末应付利息余额”
							BigDecimal d0108 = d01.getD0108() == null ? new BigDecimal(
									0)
									: d01.getD0108();
							// 比较 上一个报告期的“本月末本金余额” 与 本报告期的“上月末本金余额”
							if (preD0109.compareTo(d0107) != 0) {
								map.put("D0107", "[上月末本金余额] 与上一个报告期的“本月末本金余额”"
										+ preD0109.toString() + "不一致 ");
							}
							// 比较 上一个报告期的“本月末应收利息余额” 与 本报告期的“上月末应付利息余额”
							if (preD0111.compareTo(d0108) != 0) {
								map.put("D0108",
										"[上月末应付利息余额] 与上一个报告期的“本月末应收利息余额”"
												+ preD0111.toString() + "不一致 ");
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
