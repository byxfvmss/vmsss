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
import com.cjit.gjsz.logic.model.Fal_D03Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_D03_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_D03_DataVerify() {
	}

	public Fal_D03_DataVerify(List dictionarys, List verifylList,
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
				Fal_D03Entity d03 = (Fal_D03Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, d03.getActiontype())) {
					String value = getKey(d03.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(d03.getActiontype())) {
					if (StringUtil.isEmpty(d03.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(d03.getActiontype())) {
					if (!isNull(d03.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (d03.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// D0303 s,1,3,3 [对方国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, d03.getD0303())) {
					String value = getKey(d03.getD0303(), COUNTRY);
					map.put("D0303", "[对方国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(d03.getD0303())
						|| "N/A".equals(d03.getD0303())) {
					map.put("D0303", "[对方国家/地区] 不能选择CHN或N/A ");
				}
				// D0304 s,1,1,1 [对方部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, d03.getD0304())) {
					String value = getKey(d03.getD0304(), INVESTORINST);
					map.put("D0304", "[对方部门] [" + value + "] 无效 ");
				} else {
					// 问答四-1.1表内核查规则-
					if ("1".equals(d03.getD0304())
							|| "2".equals(d03.getD0304())) {
						map.put("D0304", "[对方部门] 不能选择1或2 ");

					} else if ("3".equals(d03.getD0304())
							|| "4".equals(d03.getD0304())
							|| "5".equals(d03.getD0304())) {
						if (d03.getD0311() != null
								&& d03.getD0311().compareTo(new BigDecimal(0)) < 0
								&& d03.getD0311().compareTo(new BigDecimal(10)) >= 0) {
							map
									.put("D0311",
											"[对方部门]选择3、4或5时，[本月末本机构持表决权比例（%）]应当满足0≤D0311＜10 ");
						}

					} else if ("6".equals(d03.getD0304())) {
						if (d03.getD0311() != null
								&& d03.getD0311().compareTo(new BigDecimal(0)) < 0
								&& d03.getD0311()
										.compareTo(new BigDecimal(100)) > 0) {
							map
									.put("D0311",
											"[对方部门]选择6时，[本月末本机构持表决权比例（%）]应当满足0≤D0311≤100 ");
						}
					}
				}
				// D0305 s,1,1,1 [对方与本机构的关系]不能为空且需在字典表中有定义 OPPOSITERELA
				if (!verifyDictionaryValue(OPPOSITERELA, d03.getD0305())) {
					String value = getKey(d03.getD0305(), OPPOSITERELA);
					map.put("D0305", "[对方与本机构的关系] [" + value + "] 无效 ");
				} else if (!"4".equals(d03.getD0305())) {
					map.put("D0305", "[对方与本机构的关系] 此字段必须选择4 ");
				}
				// D0306 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, d03.getD0306())) {
					String value = getKey(d03.getD0306(), CURRENCY);
					map.put("D0306", "[原始币种] [" + value + "] 无效 ");
				}
				// D0307 n,1,24,2 [上月末余额]不能为空，必须≥0
				if (d03.getD0307() == null
						|| d03.getD0307().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0307", "[上月末余额] 不能为空且应当大于等于0 ");
				}
				// D0308 n,1,24,2 [本月末余额]不能为空，必须≥0
				if (d03.getD0308() == null
						|| d03.getD0308().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0308", "[本月末余额] 不能为空且应当大于等于0 ");
				}
				// D0309 n,1,24,2 [本月非交易变动]不能为空
				if (d03.getD0309() == null) {
					map.put("D0309", "[本月非交易变动] 不能为空 ");
				} else {
					// 必须满足：D0309+D0310=D0308-D0307
					BigDecimal d0309 = d03.getD0308().subtract(d03.getD0307())
							.subtract(d03.getD0310());
					if (d03.getD0309().compareTo(d0309) != 0) {
						map.put("D0309",
								"[本月非交易变动] 必须满足：D0309+D0310=D0308-D0307 理论值为 "
										+ d0309.toString());
					}
				}
				// D0310 n,1,24,2 [本月净发生额]不能为空
				if (d03.getD0310() == null) {
					map.put("D0310", "[本月净发生额] 不能为空 ");
				}
				// D0311 n,1,24,2 [本月末本机构持表决权比例（%）]不能为空，必须≥0
				if (d03.getD0311() == null
						|| d03.getD0311().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0311", "[本月末本机构持表决权比例（%）] 不能为空且应当大于等于0 ");
				} else {
					if ("3".equals(d03.getD0304())
							|| "4".equals(d03.getD0304())
							|| "5".equals(d03.getD0304())) {
						// 当D0304=3 或4 或5 时，0≤D0311<10，精确到小数点后两位。
						if (d03.getD0311().compareTo(new BigDecimal(0.0)) < 0
								|| d03.getD0311().compareTo(
										new BigDecimal(10.00)) >= 0) {
							map
									.put("D0311",
											"[本月末本机构持表决权比例（%）] 当对方部门选择3、4、5时，应满足0≤D0311<10 ");
						}
					} else if ("1".equals(d03.getD0304())
							|| "2".equals(d03.getD0304())
							|| "6".equals(d03.getD0304())) {
						// 当D0304=1 或2 或6 时，0≤D0311≤100，精确到小数点后两位。
						if (d03.getD0311().compareTo(new BigDecimal(0.0)) < 0
								|| d03.getD0311().compareTo(
										new BigDecimal(100.00)) > 0) {
							map
									.put("D0311",
											"[本月末本机构持表决权比例（%）] 当对方部门选择1、2、6时，应满足0≤D0311≤100 ");
						}
					}
				}
				// D0312 n,1,24,2 [本月本机构的红利/股息/利润收入]不能为空，必须≥0
				if (d03.getD0312() == null
						|| d03.getD0312().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0312", "[本月本机构的红利/股息/利润收入] 不能为空且应当大于等于0 ");
				}
				// 问答四-1.2跨表和跨期核查规则-29
				String preBuocMonth = DateUtils.getPreMonth(d03.getBuocmonth(),
						"yyyyMM");
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(preBuocMonth)
						.append("' and INSTCODE = '").append(d03.getInstcode())
						.append("' and D0306 = '").append(d03.getD0306())
						.append("' and DATASTATUS in (").append(
								DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> 'D' ");
				SearchModel searchModel = new SearchModel();
				searchModel.setTableId("T_FAL_D03");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				List d03List = service.search(searchModel);
				if (CollectionUtils.isNotEmpty(d03List)) {
					for (Iterator it = d03List.iterator(); it.hasNext();) {
						Fal_D03Entity pre = (Fal_D03Entity) it.next();
						if (pre != null
								&& pre.getD0302().equals(d03.getD0302())) {
							// 上一个报告期的“本月末余额”
							BigDecimal preD0308 = pre.getD0308() == null ? new BigDecimal(
									0)
									: pre.getD0308();
							// 本报告期的“上月末余额”
							BigDecimal d0307 = d03.getD0307() == null ? new BigDecimal(
									0)
									: d03.getD0307();
							// 比较 上一个报告期的“本月末余额” 与 本报告期的“上月末余额”
							if (preD0308.compareTo(d0307) != 0) {
								map.put("D0307", "[上月末余额] 与上一个报告期的“本月末余额”"
										+ preD0308.toString() + "不一致 ");
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
