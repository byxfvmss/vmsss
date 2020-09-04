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
import com.cjit.gjsz.logic.model.Fal_F01Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_F01_DataVerify extends SelfDataVerify implements DataVerify {

	public Fal_F01_DataVerify() {
	}

	public Fal_F01_DataVerify(List dictionarys, List verifylList,
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
				Fal_F01Entity f01 = (Fal_F01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, f01.getActiontype())) {
					String value = getKey(f01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(f01.getActiontype())) {
					if (StringUtil.isEmpty(f01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(f01.getActiontype())) {
					if (!isNull(f01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (f01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// F0101 s,1,1,1 [相关业务类型]不能为空且需在字典表中有定义 RELEBUSSTYPE
				if (!verifyDictionaryValue(RELEBUSSTYPE, f01.getF0101())) {
					String value = getKey(f01.getF0101(), RELEBUSSTYPE);
					map.put("F0101", "[相关业务类型] [" + value + "] 无效 ");
				}
				// F0102 s,1,1,1 [是否附有银行承兑汇票]不能为空且需在字典表中有定义 YESORNO
				if (!verifyDictionaryValue(YESORNO, f01.getF0102())) {
					String value = getKey(f01.getF0102(), YESORNO);
					map.put("F0102", "[是否附有银行承兑汇票] [" + value + "] 无效 ");
				}
				// F0103 s,1,3,3 [境外付款人所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, f01.getF0103())) {
					String value = getKey(f01.getF0103(), COUNTRY);
					map.put("F0103", "[境外付款人所属国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(f01.getF0103())
						|| "N/A".equals(f01.getF0103())) {
					map.put("F0103", "[境外付款人所属国家/地区] 不能选择CHN或N/A ");
				}
				// F0104 s,1,1,1 [境外付款人所属部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, f01.getF0104())) {
					String value = getKey(f01.getF0104(), INVESTORINST);
					map.put("F0104", "[境外付款人所属部门] [" + value + "] 无效 ");
				}
				// F0105 s,1,1,1 [境外付款人与本机构的关系]不能为空且需在字典表中有定义 OPPOSITERELA
				if (!verifyDictionaryValue(OPPOSITERELA, f01.getF0105())) {
					String value = getKey(f01.getF0105(), OPPOSITERELA);
					map.put("F0105", "[境外付款人与本机构的关系] [" + value + "] 无效 ");
				}
				// F0106 s,1,1,1 [原始期限]不能为空且需在字典表中有定义 ORIDEADLINE
				if (!verifyDictionaryValue(ORIDEADLINE, f01.getF0106())) {
					String value = getKey(f01.getF0106(), ORIDEADLINE);
					map.put("F0106", "[原始期限] [" + value + "] 无效 ");
				}
				// F0107 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, f01.getF0107())) {
					String value = getKey(f01.getF0107(), CURRENCY);
					map.put("F0107", "[原始币种] [" + value + "] 无效 ");
				}
				// F0108 n,1,24,2 [上月末余额]不能为空，必须≥0
				if (f01.getF0108() == null
						|| f01.getF0108().compareTo(new BigDecimal(0)) < 0) {
					map.put("F0108", "[上月末余额] 不能为空且应当大于等于0 ");
				}
				// F0109 n,1,24,2 [本月末余额]不能为空，必须≥0
				if (f01.getF0109() == null
						|| f01.getF0109().compareTo(new BigDecimal(0)) < 0) {
					map.put("F0109", "[本月末余额] 不能为空且应当大于等于0 ");
				}
				// F0110 n,1,24,2 [本月末余额：其中剩余期限在一年及以下]不能为空，必须≥0
				if (f01.getF0110() == null
						|| f01.getF0110().compareTo(new BigDecimal(0)) < 0) {
					map.put("F0110", "[本月末余额：其中剩余期限在一年及以下] 不能为空且应当大于等于0 ");
				} else {
					if ("1".equals(f01.getF0106())) {
						// 当F0106=01 时，F0109=F0110
						if (f01.getF0109().compareTo(f01.getF0110()) != 0) {
							map
									.put("F0110",
											"[本月末余额：其中剩余期限在一年及以下] 当F0106=1时，应与F0109[本月末余额]相等");
						}
					} else if ("2".equals(f01.getF0106())) {
						// 当F0106=02 时，F0110≤F0109
						if (f01.getF0109().compareTo(f01.getF0110()) < 0) {
							map
									.put("F0110",
											"[本月末余额：其中剩余期限在一年及以下] 当F0106=2时，应小于等于F0109[本月末余额]");
						}
					}
				}
				// F0111 n,1,24,2 [本月非交易变动]不能为空
				if (f01.getF0111() == null) {
					map.put("F0111", "[本月非交易变动] 不能为空且应当大于等于0 ");
				}
				// F0112 n,1,24,2 [本月净发生额]不能为空
				if (f01.getF0112() == null) {
					map.put("F0112", "[本月净发生额] 不能为空且应当大于等于0 ");
				} else {
					// F0112+F0111=F0109-F0108
					BigDecimal d1 = f01.getF0112().add(f01.getF0111());
					BigDecimal d2 = f01.getF0109().subtract(f01.getF0108());
					if (d1.compareTo(d2) != 0) {
						map
								.put("F0112",
										"[本月净发生额] 应满足F0112+F0111=F0109-F0108 ");
					}
				}
				// 问答四-1.2跨表和跨期核查规则-29
				String preBuocMonth = DateUtils.getPreMonth(f01.getBuocmonth(),
						"yyyyMM");
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(preBuocMonth)
						.append("' and INSTCODE = '").append(f01.getInstcode())
						.append("' and F0107 = '").append(f01.getF0107())
						.append("' and DATASTATUS in (").append(
								DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> 'D' ");
				SearchModel searchModel = new SearchModel();
				searchModel.setTableId("T_FAL_F01");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				List f01List = service.search(searchModel);
				if (CollectionUtils.isNotEmpty(f01List)) {
					for (Iterator it = f01List.iterator(); it.hasNext();) {
						Fal_F01Entity pre = (Fal_F01Entity) it.next();
						if (pre != null
								&& pre.getF0101().equals(f01.getF0101())) {
							// 上一个报告期的“本月末余额”
							BigDecimal preF0109 = pre.getF0109() == null ? new BigDecimal(
									0)
									: pre.getF0109();
							// 本报告期的“上月末余额”
							BigDecimal f0108 = f01.getF0108() == null ? new BigDecimal(
									0)
									: f01.getF0108();
							// 比较 上一个报告期的“本月末余额” 与 本报告期的“上月末余额”
							if (preF0109.compareTo(f0108) != 0) {
								map.put("F0108", "[上月末余额] 与上一个报告期的“本月末余额”"
										+ preF0109.toString() + "不一致 ");
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
