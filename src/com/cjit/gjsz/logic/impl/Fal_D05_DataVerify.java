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
import com.cjit.gjsz.logic.model.Fal_D05Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_D05_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_D05_DataVerify() {
	}

	public Fal_D05_DataVerify(List dictionarys, List verifylList,
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
				Fal_D05Entity d05 = (Fal_D05Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, d05.getActiontype())) {
					String value = getKey(d05.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(d05.getActiontype())) {
					if (StringUtil.isEmpty(d05.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(d05.getActiontype())) {
					if (!isNull(d05.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (d05.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// T_FAL_D05_1 存款（含银行同业和联行存放）（负债）——境外机构存款
				if ("T_FAL_D05_1".equalsIgnoreCase(d05.getTableId())) {
					// EXDEBTCODE s,1,28,28 [外债编号]不能为空，填写外债唯一性编码
					// D0508 n,1,24,2 [上月末应付利息余额]不能为空，必须≥0
					if (d05.getD0508() == null
							|| d05.getD0508().compareTo(new BigDecimal(0)) < 0) {
						map.put("D0508", "[上月末应付利息余额] 不能为空且应当大于等于0 ");
					}
					// D0510 n,1,24,2 [本月末本金余额：其中剩余期限在一年及以下]不能为空，必须≥0
					if (d05.getD0510() == null
							|| d05.getD0510().compareTo(new BigDecimal(0)) < 0) {
						map
								.put("D0510",
										"[本月末本金余额：其中剩余期限在一年及以下] 不能为空且应当大于等于0 ");
					}
					// D0511 n,1,24,2 [本月末应付利息余额]不能为空，必须≥0
					if (d05.getD0511() == null
							|| d05.getD0511().compareTo(new BigDecimal(0)) < 0) {
						map.put("D0511", "[本月末应付利息余额] 不能为空且应当大于等于0 ");
					}
					// D0513 n,1,24,2 [本月净发生额]不能为空
					if (d05.getD0513() == null) {
						map.put("D0513", "[本月净发生额] 不能为空 ");
					}
					// D0514 n,1,24,2 [本月利息支出]不能为空
					if (d05.getD0514() == null) {
						map.put("D0514", "[本月利息支出] 不能为空 ");
					} else if (d05.getD0514().compareTo(new BigDecimal(0)) < 0
							&& StringUtil.isEmpty(d05.getRemark())) {
						// 问答四-1.1表内核查规则-6
						map.put("REMARK", "当D0514为负数，备注（REMARK）字段必填 ");
					}
					// 问答四-1.1表内核查规则-54 同一外债编号（EXDEBTCODE）下，每报告期应汇总报送最多一条数据
					StringBuffer sbSearchCondition = new StringBuffer();
					sbSearchCondition.append(" BUOCMONTH = '").append(
							d05.getBuocmonth()).append("' and INSTCODE = '")
							.append(d05.getInstcode()).append(
									"' and BUSINESSID <> '").append(
									d05.getBusinessid()).append(
									"' and EXDEBTCODE = '").append(
									d05.getExdebtcode()).append(
									"' and DATASTATUS in (").append(
									DataUtil.JYYTG_STATUS_NUM).append(",")
							.append(DataUtil.YTJDSH_STATUS_NUM).append(",")
							.append(DataUtil.SHWTG_STATUS_NUM).append(",")
							.append(DataUtil.SHYTG_STATUS_NUM).append(",")
							.append(DataUtil.YSC_STATUS_NUM).append(",")
							.append(DataUtil.YBS_STATUS_NUM).append(",")
							.append(DataUtil.LOCKED_STATUS_NUM).append(
									") and ACTIONTYPE <> '").append(
									ACTIONTYPE_D).append("' ");
					SearchModel searchModel = new SearchModel();
					searchModel.setTableId("T_FAL_D05_1");
					searchModel
							.setSearchCondition(sbSearchCondition.toString());
					long count = service.getCount(searchModel);
					if (count > 0) {
						map.put("EXDEBTCODE", "同一外债编号下，每报告期应汇总报送最多一条数据 ");
					}
					// 问答四-1.1表内核查规则-55
					if (d05.getD0508() != null
							&& d05.getD0508().compareTo(new BigDecimal(0)) == 0
							&& d05.getD0510() != null
							&& d05.getD0510().compareTo(new BigDecimal(0)) == 0
							&& d05.getD0511() != null
							&& d05.getD0511().compareTo(new BigDecimal(0)) == 0
							&& d05.getD0513() != null
							&& d05.getD0513().compareTo(new BigDecimal(0)) == 0
							&& d05.getD0514() != null
							&& d05.getD0514().compareTo(new BigDecimal(0)) == 0) {
						map
								.put("D0508",
										"同一外债编号下，D0508、D0510、D0511、D0513和D0514不能同时为0，且均应按照签约币种对应的金额填写 ");
						map
								.put("D0510",
										"同一外债编号下，D0508、D0510、D0511、D0513和D0514不能同时为0，且均应按照签约币种对应的金额填写 ");
						map
								.put("D0511",
										"同一外债编号下，D0508、D0510、D0511、D0513和D0514不能同时为0，且均应按照签约币种对应的金额填写 ");
						map
								.put("D0513",
										"同一外债编号下，D0508、D0510、D0511、D0513和D0514不能同时为0，且均应按照签约币种对应的金额填写 ");
						map
								.put("D0514",
										"同一外债编号下，D0508、D0510、D0511、D0513和D0514不能同时为0，且均应按照签约币种对应的金额填写 ");
					}
				}
				// T_FAL_D05_2 D05-2 表：存款（含银行同业和联行存放）（负债）——非居民个人存款
				else if ("T_FAL_D05_2".equalsIgnoreCase(d05.getTableId())) {
					// D0501 s,1,1,1 [业务类别]不能为空且需在字典表中有定义 06 BUSITYPE
					if (!verifyDictionaryValue(BUSITYPE, d05.getD0501())) {
						String value = getKey(d05.getD0501(), BUSITYPE);
						map.put("D0501", "[业务类别] [" + value + "] 无效 ");
					}
					// D0502 s,1,3,3 [对方国家/地区]不能为空且需在字典表中有定义 07 COUNTRY
					if (!verifyDictionaryValue(COUNTRY, d05.getD0502())) {
						String value = getKey(d05.getD0502(), COUNTRY);
						map.put("D0502", "[对方国家/地区] [" + value + "] 无效 ");
					} else if (COUNTRY_CHN.equals(d05.getD0502())
							|| "N/A".equals(d05.getD0502())) {
						map.put("D0502", "[对方国家/地区] 不能选择CHN或N/A ");
					}
					// D0503 s,1,1,1 [对方部门]不能为空且需在字典表中有定义 08 INVESTORINST
					if (!verifyDictionaryValue(INVESTORINST, d05.getD0503())) {
						String value = getKey(d05.getD0503(), INVESTORINST);
						map.put("D0503", "[对方部门] [" + value + "] 无效 ");
					} else if (!"5".equals(d05.getD0503())) {
						map.put("D0503", "[对方部门] 此项应为[5-其他企业和个人] ");
					}
					// D0504 s,1,1,1 [对方与本机构的关系]不能为空且需在字典表中有定义 09 OPPOSITERELA
					if (!verifyDictionaryValue(OPPOSITERELA, d05.getD0504())) {
						String value = getKey(d05.getD0504(), OPPOSITERELA);
						map.put("D0504", "[对方与本机构的关系] [" + value + "] 无效 ");
					} else if ("2".equals(d05.getD0504())
							|| "3".equals(d05.getD0504())) {
						// 问答四-1.1表内核查规则-56
						map
								.put("D0504",
										"[对方与本机构的关系] 不能选择“2—境外被投资企业”或“3—境外联属企业”，因为债权人是非居民个人 ");
					}
					// D0505 s,1,1,1 [原始期限]不能为空且需在字典表中有定义 10 ORIDEADLINE
					if (!verifyDictionaryValue(ORIDEADLINE, d05.getD0505())) {
						String value = getKey(d05.getD0505(), ORIDEADLINE);
						map.put("D0505", "[原始期限] [" + value + "] 无效 ");
					}
					// D0506 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 11 CURRENCY
					if (!verifyDictionaryValue(CURRENCY, d05.getD0506())) {
						String value = getKey(d05.getD0506(), CURRENCY);
						map.put("D0506", "[原始币种] [" + value + "] 无效 ");
					}
					// D0507 n,1,24,2 [上月末本金余额]不能为空，必须≥0 12
					if (d05.getD0507() == null
							|| d05.getD0507().compareTo(new BigDecimal(0)) < 0) {
						map.put("D0507", "[上月末本金余额] 不能为空且应当大于等于0 ");
					}
					// D0508 n,1,24,2 [上月末应付利息余额]不能为空，必须≥0 13
					if (d05.getD0508() == null
							|| d05.getD0508().compareTo(new BigDecimal(0)) < 0) {
						map.put("D0508", "[上月末应付利息余额] 不能为空且应当大于等于0 ");
					}
					// D0509 n,1,24,2 [本月末本金余额]不能为空，必须≥0 14
					if (d05.getD0509() == null
							|| d05.getD0509().compareTo(new BigDecimal(0)) < 0) {
						map.put("D0509", "[本月末本金余额] 不能为空且应当大于等于0 ");
					}
					// D0510 n,1,24,2 [本月末本金余额：其中剩余期限在一年及以下]不能为空，必须≥0 15
					if (d05.getD0510() == null
							|| d05.getD0510().compareTo(new BigDecimal(0)) < 0) {
						map
								.put("D0510",
										"[本月末本金余额：其中剩余期限在一年及以下] 不能为空且应当大于等于0 ");
					} else {
						if ("1".equals(d05.getD0505())) {
							// 当D0505=01 时，D0509=D0510
							if (d05.getD0509().compareTo(d05.getD0510()) != 0) {
								map
										.put("D0510",
												"[本月末本金余额：其中剩余期限在一年及以下] 当D0505=1时，应与D0509[本月末本金余额]相等");
							}
						} else if ("2".equals(d05.getD0505())) {
							// 当D0505=02 时，D0509≥D0510
							if (d05.getD0509().compareTo(d05.getD0510()) < 0) {
								map
										.put("D0510",
												"[本月末本金余额：其中剩余期限在一年及以下] 当D0505=2时，应小于等于D0509[本月末本金余额]");
							}
						}
					}
					// D0511 n,1,24,2 [本月末应付利息余额]不能为空，必须≥0 16
					if (d05.getD0511() == null
							|| d05.getD0511().compareTo(new BigDecimal(0)) < 0) {
						map.put("D0511", "[本月末应付利息余额] 不能为空且应当大于等于0 ");
					}
					// D0512 n,1,24,2 [本月非交易变动]不能为空 17
					if (d05.getD0512() == null) {
						map.put("D0512", "[本月非交易变动] 不能为空 ");
					}
					// D0513 n,1,24,2 [本月净发生额]不能为空 18
					if (d05.getD0513() == null) {
						map.put("D0513", "[本月净发生额] 不能为空 ");
					} else {
						// D0513+D0512=(D0509+D0511)-（D0507+D0508）
						if (d05.getD0507() != null && d05.getD0508() != null
								&& d05.getD0509() != null
								&& d05.getD0511() != null
								&& d05.getD0512() != null) {
							BigDecimal d0513 = d05.getD0509().add(
									d05.getD0511()).subtract(d05.getD0507())
									.subtract(d05.getD0508()).subtract(
											d05.getD0512());
							if (d05.getD0513().compareTo(d0513) != 0) {
								map.put("D0513",
										"[本月净发生额] 应满足D0513+D0512=(D0509+D0511)-（D0507+D0508） 理论值为 "
												+ d0513.toString());
							}
						}
					}
					// D0514 n,1,24,2 [本月利息支出]不能为空
					if (d05.getD0514() == null) {
						map.put("D0514", "[本月利息支出] 不能为空");
					} else if (d05.getD0514().compareTo(new BigDecimal(0)) < 0
							&& StringUtil.isEmpty(d05.getRemark())) {
						// 问答四-1.1表内核查规则-6
						map.put("REMARK", "当D0514为负数，备注（REMARK）字段必填 ");
					}
					// 问答四-1.2跨表和跨期核查规则-30
					String preBuocMonth = DateUtils.getPreMonth(d05
							.getBuocmonth(), "yyyyMM");
					StringBuffer sbSearchCondition = new StringBuffer();
					sbSearchCondition.append(" BUOCMONTH = '").append(
							preBuocMonth).append("' and INSTCODE = '").append(
							d05.getInstcode()).append("' and D0506 = '")
							.append(d05.getD0506()).append(
									"' and DATASTATUS in (").append(
									DataUtil.YSC_STATUS_NUM).append(",")
							.append(DataUtil.YBS_STATUS_NUM).append(",")
							.append(DataUtil.LOCKED_STATUS_NUM).append(
									") and ACTIONTYPE <> 'D' ");
					SearchModel searchModel = new SearchModel();
					searchModel.setTableId("T_FAL_D05_2");
					searchModel
							.setSearchCondition(sbSearchCondition.toString());
					List d05List = service.search(searchModel);
					if (CollectionUtils.isNotEmpty(d05List)) {
						for (Iterator it = d05List.iterator(); it.hasNext();) {
							Fal_D05Entity pre = (Fal_D05Entity) it.next();
							if (pre != null
									&& pre.getD0501().equals(d05.getD0501())
									&& pre.getD0502().equals(d05.getD0502())
									&& pre.getD0503().equals(d05.getD0503())) {
								// 上一个报告期的“本月末本金余额”
								BigDecimal preD0509 = pre.getD0509() == null ? new BigDecimal(
										0)
										: pre.getD0509();
								// 上一个报告期的“本月末应收利息余额”
								BigDecimal preD0511 = pre.getD0511() == null ? new BigDecimal(
										0)
										: pre.getD0511();
								// 本报告期的“上月末本金余额”
								BigDecimal d0507 = d05.getD0507() == null ? new BigDecimal(
										0)
										: d05.getD0507();
								// 本报告期的“上月末应付利息余额”
								BigDecimal d0508 = d05.getD0508() == null ? new BigDecimal(
										0)
										: d05.getD0508();
								// 比较 上一个报告期的“本月末本金余额” 与 本报告期的“上月末本金余额”
								if (preD0509.compareTo(d0507) != 0) {
									map.put("D0507",
											"[上月末本金余额] 与上一个报告期的“本月末本金余额”"
													+ preD0509.toString()
													+ "不一致 ");
								}
								// 比较 上一个报告期的“本月末应收利息余额” 与 本报告期的“上月末应付利息余额”
								if (preD0511.compareTo(d0508) != 0) {
									map.put("D0508",
											"[上月末应付利息余额] 与上一个报告期的“本月末应收利息余额”"
													+ preD0511.toString()
													+ "不一致 ");
								}
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
