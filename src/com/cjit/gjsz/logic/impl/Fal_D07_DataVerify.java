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
import com.cjit.gjsz.logic.model.Fal_D07Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_D07_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_D07_DataVerify() {
	}

	public Fal_D07_DataVerify(List dictionarys, List verifylList,
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
				Fal_D07Entity d07 = (Fal_D07Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, d07.getActiontype())) {
					String value = getKey(d07.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(d07.getActiontype())) {
					if (StringUtil.isEmpty(d07.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(d07.getActiontype())) {
					if (!isNull(d07.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (d07.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// D0703 s,1,3,3 [对方国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, d07.getD0703())) {
					String value = getKey(d07.getD0703(), COUNTRY);
					map.put("D0703", "[对方国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(d07.getD0703())
						|| "N/A".equals(d07.getD0703())) {
					map.put("D0703", "[对方国家/地区] 不能选择CHN或N/A ");
				}
				// D0704 s,1,1,1 [对方部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, d07.getD0704())) {
					String value = getKey(d07.getD0704(), INVESTORINST);
					map.put("D0704", "[对方部门] [" + value + "] 无效 ");
				}
				// D0705 s,1,1,1 [对方与本机构的关系]不能为空且需在字典表中有定义 OPPOSITERELA
				if (!verifyDictionaryValue(OPPOSITERELA, d07.getD0705())) {
					String value = getKey(d07.getD0705(), OPPOSITERELA);
					map.put("D0705", "[对方与本机构的关系] [" + value + "] 无效 ");
				} else if ("1".equals(d07.getD0705())
						|| "3".equals(d07.getD0705())) {
					map.put("D0705", "[对方与本机构的关系] 不可选择1或3 ");
				}
				// D0706 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, d07.getD0706())) {
					String value = getKey(d07.getD0706(), CURRENCY);
					map.put("D0706", "[原始币种] [" + value + "] 无效 ");
				}
				// D0707 n,1,24,2 [上月末余额]不能为空，必须≥0
				if (d07.getD0707() == null
						|| d07.getD0707().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0707", "[上月末余额] 不能为空且应当大于等于0 ");
				}
				// D0708 n,1,24,2 [本月末余额]不能为空，必须≥0
				if (d07.getD0708() == null
						|| d07.getD0708().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0708", "[本月末余额] 不能为空且应当大于等于0 ");
				}
				// D0709 n,1,24,2 [本月非交易变动]不能为空
				if (d07.getD0709() == null) {
					map.put("D0709", "[本月非交易变动] 不能为空 ");
				}
				// D0710 n,1,24,2 [本月净发生额]不能为空
				if (d07.getD0710() == null) {
					map.put("D0710", "[本月净发生额] 不能为空 ");
				} else {
					// D0709+D0710=D0708-D0707
					if (d07.getD0709() != null && d07.getD0708() != null
							&& d07.getD0707() != null) {
						BigDecimal d0710 = d07.getD0708().subtract(
								d07.getD0707()).subtract(d07.getD0709());
						if (d07.getD0710().compareTo(d0710) != 0) {
							map.put("D0710",
									"[本月净发生额] 应满足D0709+D0710=D0708-D0707 理论值为 "
											+ d0710.toString());
						}
					}
				}
				// D0711 n,1,24,2 [本月末外方表决权比例（%）]不能为空，必须≥0
				if (d07.getD0711() == null
						|| d07.getD0711().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0711", "[本月末外方表决权比例（%）] 不能为空且应当大于等于0 ");
				}
				// D0712 n,1,24,2 [本月对外方的股息/红利/利润支出]不能为空，必须≥0
				if (d07.getD0712() == null
						|| d07.getD0712().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0712", "[本月对外方的股息/红利/利润支出] 不能为空且应当大于等于0 ");
				}
				// 问答四-1.2跨表和跨期核查规则-29
				String preBuocMonth = DateUtils.getPreMonth(d07.getBuocmonth(),
						"yyyyMM");
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(preBuocMonth)
						.append("' and INSTCODE = '").append(d07.getInstcode())
						.append("' and D0706 = '").append(d07.getD0706())
						.append("' and DATASTATUS in (").append(
								DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> 'D' ");
				SearchModel searchModel = new SearchModel();
				searchModel.setTableId("T_FAL_D07");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				List d07List = service.search(searchModel);
				if (CollectionUtils.isNotEmpty(d07List)) {
					for (Iterator it = d07List.iterator(); it.hasNext();) {
						Fal_D07Entity pre = (Fal_D07Entity) it.next();
						if (pre != null
								&& pre.getD0702().equals(d07.getD0702())) {
							// 上一个报告期的“本月末余额”
							BigDecimal preD0708 = pre.getD0708() == null ? new BigDecimal(
									0)
									: pre.getD0708();
							// 本报告期的“上月末余额”
							BigDecimal d0707 = d07.getD0707() == null ? new BigDecimal(
									0)
									: d07.getD0707();
							// 比较 上一个报告期的“本月末余额” 与 本报告期的“上月末余额”
							if (preD0708.compareTo(d0707) != 0) {
								map.put("D0707", "[上月末余额] 与上一个报告期的“本月末余额”"
										+ preD0708.toString() + "不一致 ");
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
