package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_A02Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_A02_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_A02_DataVerify() {
	}

	public Fal_A02_DataVerify(List dictionarys, List verifylList,
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
				Fal_A02Entity a02 = (Fal_A02Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, a02.getActiontype())) {
					String value = getKey(a02.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(a02.getActiontype())) {
					if (StringUtil.isEmpty(a02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(a02.getActiontype())) {
					if (!isNull(a02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (a02.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// T_FAL_A02_1 外国来华直接投资（资产负债表）
				if ("T_FAL_A02_1".equalsIgnoreCase(a02.getTableId())) {
					// A0201 期末会计记账币种（原币）
					if (!verifyDictionaryValue(CURRENCY, a02.getA0201())) {
						String value = getKey(a02.getA0201(), CURRENCY);
						map.put("A0201", "[期末会计记账币种（原币）] [" + value + "] 无效 ");
					}
					// A0202 期末资产
					if (a02.getA0202() == null
							|| a02.getA0202().compareTo(new BigDecimal(0)) < 0) {
						map.put("A0202", "[期末资产] 不能为空且应当大于等于0 ");
					}
					// A0203 期末负债
					if (a02.getA0203() == null
							|| a02.getA0203().compareTo(new BigDecimal(0)) < 0) {
						map.put("A0203", "[期末负债] 不能为空且应当大于等于0 ");
					}
					// A0204 期末负债中境外母公司拨付的营运资金
					if (a02.getA0204() == null
							|| a02.getA0204().compareTo(new BigDecimal(0)) < 0) {
						map.put("A0204", "[期末负债中境外母公司拨付的营运资金] 不能为空且应当大于等于0 ");
					} else if (a02.getA0204().compareTo(a02.getA0203()) > 0) {
						map.put("A0204", "[期末负债中境外母公司拨付的营运资金] 应小于等于期末负债金额 ");
					}
					// A0205 期末归属于本机构全体股东的权益 (总额)
					if (a02.getA0205() == null) {
						map.put("A0205", "[期末归属于本机构全体股东的权益 (总额)] 不能为空 ");
					} else {
						// A0202-A0203=A0205+A0211
						BigDecimal a0205_1 = a02.getA0202().subtract(
								a02.getA0203()).subtract(a02.getA0211());
						// A0205=A0206+A0207+A0208+A0209+A0210
						BigDecimal a0205_2 = a02.getA0206().add(a02.getA0207())
								.add(a02.getA0208()).add(a02.getA0209()).add(
										a02.getA0210());
						if (a02.getA0205().compareTo(a0205_1) != 0) {
							// A0202-A0203=A0205+A0211
							String errorMsg = "[期末归属于本机构全体股东的权益 (总额)] 应满足 A0202-A0203=A0205+A0211 ";
							if (a0205_1.compareTo(a0205_2) == 0) {
								errorMsg += " 理论值为 " + a0205_1.toString();
							}
							map.put("A0205", errorMsg);
						} else if (a02.getA0205().compareTo(a0205_2) != 0) {
							// A0205=A0206+A0207+A0208+A0209+A0210
							String errorMsg = "[期末归属于本机构全体股东的权益 (总额)] 应满足 A0205=A0206+A0207+A0208+A0209+A0210 ";
							if (a0205_1.compareTo(a0205_2) == 0) {
								errorMsg += " 理论值为 " + a0205_2.toString();
							}
							map.put("A0205", errorMsg);
						}
					}
					// A0206 期末归属于本机构全体股东的权益中的实收资本
					if (a02.getA0206() == null
							|| a02.getA0206().compareTo(new BigDecimal(0)) < 0) {
						map.put("A0206",
								"[期末归属于本机构全体股东的权益中的实收资本] 不能为空且应当大于等于0 ");
					}
					// A0207 期末归属于本机构全体股东的权益中的资本公积
					if (a02.getA0207() == null) {
						map.put("A0207", "[期末归属于本机构全体股东的权益中的资本公积] 不能为空 ");
					}
					// A0208 期末归属于本机构全体股东的权益中的盈余公积
					if (a02.getA0208() == null) {
						map.put("A0208", "[期末归属于本机构全体股东的权益中的盈余公积] 不能为空 ");
					}
					// A0209 期末归属于本机构全体股东的权益中的未分配利润
					if (a02.getA0209() == null) {
						map.put("A0209", "[期末归属于本机构全体股东的权益中的未分配利润] 不能为空 ");
					}
					// A0210 期末归属于本机构全体股东的权益中的其他
					if (a02.getA0210() == null) {
						map.put("A0210", "[期末归属于本机构全体股东的权益中的其他] 不能为空 ");
					}
					// A0211 期末少数股东权益
					if (a02.getA0211() == null) {
						map.put("A0211", "[期末少数股东权益] 不能为空 ");
					}
					// A0212 本期利润总额
					if (a02.getA0212() == null) {
						map.put("A0212", "[本期利润总额] 不能为空 ");
					}
					// A0213 本期本机构全体股东应享净利润
					if (a02.getA0213() == null) {
						map.put("A0213", "[本期本机构全体股东应享净利润] 不能为空 ");
					}
					// A0214 本期本机构分配全体股东的利润
					if (a02.getA0214() == null) {
						map.put("A0214", "[本期本机构分配全体股东的利润] 不能为空 ");
					}
					// REMARK
					if (StringUtil.isEmpty(a02.getRemark())) {
						// 问答四-1.1表内核查规则-6
						if (a02.getA0213() == null && a02.getA0214() == null
								&& a02.getA0213().compareTo(a02.getA0214()) < 0) {
							map.put("REMARK", "当A0213<A0214时，备注字段必填 ");
						}
					}
					// 问答四-1.1表内核查规则-34
					StringBuffer sbSearchCondition = new StringBuffer();
					sbSearchCondition.append(" BUOCMONTH = '").append(
							a02.getBuocmonth()).append("' and INSTCODE = '")
							.append(a02.getInstcode()).append(
									"' and BUSINESSID <> '").append(
									a02.getBusinessid()).append(
									"' and datastatus in (").append(
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
					searchModel.setTableId("T_FAL_A02_1");
					searchModel
							.setSearchCondition(sbSearchCondition.toString());
					long countA21 = service.getCount(searchModel);
					if (countA21 > 0) {
						map.put("OBJCODE", "同一报告期，本机构（填报机构）（合并）资产负债表应只有一条");
					}

					// A0215 本机构在境内缴
					// 纳的税金总额
					// 数值型，22.2
					// 报告期为每年12 月，且本机构被境外投资者
					// 控股的，为必填项，必须≥0。其他报告期为
					// 非必填项。
					//					
					// A0216 年末从业人数
					// (人)
					// 长整数型
					// 报告期为每年12 月，且本机构被境外投资者
					// 控股的，为必填项，必须满足：A0216≥0。
					// 其他报告期为非必填项。
					//					
					// A0217 年末从业人数中
					// 的外方雇员数
					// （人）
					// 长整数型
					// 报告期为每年12 月，且本机构被境外投资者
					// 控股的，为必填项，必须满足：A0217≥0，
					// 且A0216≥A0217。其他报告期为非必填项。
				}
				// T_FAL_A02_2 境外直接投资者名录
				else if ("T_FAL_A02_2".equalsIgnoreCase(a02.getTableId())) {
					// A0220 境外投资者所属国家/地区
					if (!verifyDictionaryValue(COUNTRY, a02.getA0220())) {
						String value = getKey(a02.getA0220(), COUNTRY);
						map.put("A0220", "[境外投资者所属国家/地区] [" + value + "] 无效 ");
					} else if (COUNTRY_CHN.equalsIgnoreCase(a02.getA0220())
							|| "N/A".equalsIgnoreCase(a02.getA0220())) {
						map.put("A0220", "[境外投资者所属国家/地区] 不能选择CHN和N/A ");
					}
					// A0221 境外投资者所属行业
					if (!verifyDictionaryValue(INVESTOR_INDUSTRY, a02
							.getA0221())) {
						String value = getKey(a02.getA0221(), INVESTOR_INDUSTRY);
						map.put("A0221", "[境外投资者所属行业] [" + value + "] 无效 ");
					}
					// A0222 境外投资者与本机构的关系
					if (!verifyDictionaryValue(INVESTORRELA, a02.getA0222())) {
						String value = getKey(a02.getA0222(), INVESTORRELA);
						map.put("A0222", "[境外投资者与本机构的关系] [" + value + "] 无效 ");
					} else {
						// 问答四-1.1表内核查规则- 修改
						// if (a02.getA0225() != null) {
						// if (a02.getA0225().compareTo(new BigDecimal(10)) >= 0
						// && a02.getA0225().compareTo(
						// new BigDecimal(50)) <= 0) {
						// // 如果10≤A0225≤50，则A0222=1或3
						// if (!"1".equals(a02.getA0222())
						// && !"3".equals(a02.getA0222())) {
						// map
						// .put("A0222",
						// "[境外投资者与本机构的关系] 当10≤A0225≤50时，应选择1或3 ");
						// }
						// } else if (a02.getA0225().compareTo(
						// new BigDecimal(50)) > 0
						// && a02.getA0225().compareTo(
						// new BigDecimal(100)) <= 0) {
						// // 如果50<A0225≤100，则A0222=1或2
						// if (!"1".equals(a02.getA0222())
						// && !"2".equals(a02.getA0222())) {
						// map
						// .put("A0222",
						// "[境外投资者与本机构的关系] 当50<A0225≤100时，应选择1或2 ");
						// }
						// }
						// }
					}
					// A0224 最终控制方所属国家/地区
					if (!"N/A".equalsIgnoreCase(a02.getA0223())) {
						// A0224 必填
						if (StringUtil.isEmpty(a02.getA0224())) {
							map.put("A0224",
									"[最终控制方所属国家/地区] 当[最终控制方]不为'N/A'时，此项必填 ");
						} else if (!verifyDictionaryValue(COUNTRY, a02
								.getA0224())) {
							String value = getKey(a02.getA0224(), COUNTRY);
							map.put("A0224", "[最终控制方所属国家/地区] [" + value
									+ "] 无效 ");
						}
					} else {
						// A0224 为空
						if (StringUtil.isNotEmpty(a02.getA0224())) {
							map.put("A0224",
									"[最终控制方所属国家/地区] 当[最终控制方全称]为'N/A'时，此项为空 ");
						}
					}
					// A0225 期末(外方)表决权比例(%)
					if (a02.getA0225() == null
							|| a02.getA0225().compareTo(new BigDecimal(0.00)) < 0
							|| a02.getA0225().compareTo(new BigDecimal(100.00)) > 0) {
						map.put("A0225",
								"[期末（外方）表决权比例（%）] 不能为空且应当大于等于0并小于等于100 ");
					} else if (a02.getA0225().compareTo(new BigDecimal(10.00)) < 0) {
						// 问答四-1.1表内核查规则-6
						if (StringUtil.isEmpty(a02.getRemark())) {
							map.put("REMARK",
									"当0（%）≤A0225＜10（%）时，备注（REMARK）字段必填 ");
						}
						// 问答四-1.1表内核查规则-36 A02-1表和A02-3表必填
						StringBuffer sbSearchCondition = new StringBuffer();
						sbSearchCondition.append(" BUOCMONTH = '").append(
								a02.getBuocmonth())
								.append("' and INSTCODE = '").append(
										a02.getInstcode()).append(
										"' and datastatus <> 0 ");
						SearchModel searchModel = new SearchModel();
						searchModel.setTableId("T_FAL_A02_1");
						searchModel.setSearchCondition(sbSearchCondition
								.toString());
						long countA21 = service.getCount(searchModel);
						if (countA21 == 0) {
							map
									.put("A0225",
											"当0（%）≤A0225＜10（%）时，A02-1表和A02-3表必填，但当前并未录入A02-1表");
						}
						sbSearchCondition = new StringBuffer();
						sbSearchCondition.append(" BUOCMONTH = '").append(
								a02.getBuocmonth())
								.append("' and INSTCODE = '").append(
										a02.getInstcode()).append(
										"' and datastatus <> 0 ");
						searchModel = new SearchModel();
						searchModel.setTableId("T_FAL_A02_3");
						searchModel.setSearchCondition(sbSearchCondition
								.toString());
						long countA23 = service.getCount(searchModel);
						if (countA23 == 0) {
							map
									.put("A0225",
											"当0（%）≤A0225＜10（%）时，A02-1表和A02-3表必填，但当前并未录入A02-3表");
						}
					} else if (a02.getA0225().compareTo(new BigDecimal(10.00)) >= 0) {
						// 问答四-1.1表内核查规则-37
						// 当A0225≥10（%）时：
						// A0222=1或 2，50＜A0225≤100。
						// A0222=3时，10≤A0225≤50。
						if ("1".equals(a02.getA0222())
								|| "2".equals(a02.getA0222())) {
							if (a02.getA0225().compareTo(new BigDecimal(50.00)) <= 0) {
								map
										.put("A0225",
												"当A0222=1或2时，应满足50＜A0225≤100 ");
							}
						} else if ("3".equals(a02.getA0222())) {
							if (a02.getA0225().compareTo(new BigDecimal(50.00)) > 0) {
								map.put("A0225", "当A0222=3时，应满足10≤A0225≤50 ");
							}
						}
					}
					// A0226 期末(外方)持股比例(%)
					if (a02.getA0226() == null
							|| a02.getA0226().compareTo(new BigDecimal(0.00)) < 0
							|| (a02.getA0226().compareTo(new BigDecimal(0.00)) == 0
									&& a02.getA0225() != null && a02.getA0225()
									.compareTo(new BigDecimal(10.00)) >= 0)
							|| a02.getA0226().compareTo(new BigDecimal(100.00)) > 0) {
						map.put("A0226", "[期末(外方)持股比例(%)] 不能为空且应当大于0并小于等于100 ");
					}
					// STOCKINFO 股票信息
					List stockList = service.getCfaChildren(
							"T_FAL_A02_2_STOCKINFO", a02.getBusinessid());
					List list = new ArrayList();
					if (CollectionUtil.isNotEmpty(stockList)) {
						if (stockList.size() > 5) {
							map.put("T_FAL_A02_2_STOCKINFO",
									"[股票信息] 接口方式下，最多填写5种不同记账币种的数据 ");
						}
						for (int j = 0; j < stockList.size(); j++) {
							Fal_A02Entity a02stock = (Fal_A02Entity) stockList
									.get(j);
							Map mapSub = new HashMap();
							if ("N/A".equalsIgnoreCase(a02stock.getA0227())) {
								// A0228 持股（份额）数量
								if (a02stock.getA0228().compareTo(
										new BigDecimal("0")) != 0) {
									mapSub
											.put("A0228",
													"[持股（份额）数量] 当可流通股票（份额）的记账币种（原币）为N/A时，此项当为0 ");
								}
								// A0229 每股（每份）市价
								if (a02stock.getA0229().compareTo(
										new BigDecimal("0")) != 0) {
									mapSub
											.put("A0229",
													"[每股（每份）市价] 当可流通股票（份额）的记账币种（原币）为N/A时，此项当为0 ");
								}
							} else {
								// A0227 可流通股票（份额）的记账币种（原币）
								if (!verifyDictionaryValue(CURRENCY, a02stock
										.getA0227())) {
									String value = getKey(a02.getA0227(),
											CURRENCY);
									mapSub.put("A0227",
											"[可流通股票（份额）的记账币种（原币）] [" + value
													+ "] 无效 ");
								}
								// A0228 持股（份额）数量
								if (a02stock.getA0228() == null
										|| a02stock.getA0228().compareTo(
												new BigDecimal("0")) <= 0) {
									mapSub.put("A0228",
											"[持股（份额）数量] 不能为空且应当大于0 ");
								}
								// A0229 每股（每份）市价
								if (a02stock.getA0229() == null
										|| a02stock.getA0229().compareTo(
												new BigDecimal("0")) <= 0) {
									mapSub
											.put("A0229",
													"[每股（每份）市价] 不能为空且应当大于0");
								}
								// 问答四-1.1表内核查规则-38
								if (a02stock.getA0228() != null
										&& a02stock.getA0229() != null
										&& a02stock.getA0228().compareTo(
												a02stock.getA0229()) == 0) {
									mapSub
											.put("A0228",
													"A0227可流通股票（份额）的记账币种非N/A时，A0228持股（份额）数量、A0229每股（每份）市价不能同时填写为相同数值");
									mapSub
											.put("A0229",
													"A0227可流通股票（份额）的记账币种非N/A时，A0228持股（份额）数量、A0229每股（每份）市价不能同时填写为相同数值");

								}
							}
							if (mapSub != null && !mapSub.isEmpty()) {
								mapSub.put(SUBID, a02stock.getSubid());
								mapSub.put(INNERTABLEID,
										"T_FAL_A02_2_STOCKINFO");
								list.add(mapSub);
							}
						}
						if (CollectionUtil.isNotEmpty(list)) {
							verifyModel.setChildren(list);
						}
					} else {
						map.put("T_FAL_A02_2_STOCKINFO", "[股票信息] 不能为空 ");
					}
					// 问答四-1.1表内核查规则-35
					StringBuffer sbSearchCondition = new StringBuffer();
					sbSearchCondition.append(" BUOCMONTH = '").append(
							a02.getBuocmonth()).append("' and INSTCODE = '")
							.append(a02.getInstcode()).append(
									"' and BUSINESSID <> '").append(
									a02.getBusinessid()).append(
									"' and A0218 = '").append(a02.getA0218())
							.append("' and datastatus in (").append(
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
					searchModel.setTableId("T_FAL_A02_2");
					searchModel
							.setSearchCondition(sbSearchCondition.toString());
					long count = service.getCount(searchModel);
					if (count > 0) {
						map
								.put("A0218",
										"同一报告期，一个填报机构的一个境外投资者代码（A0218）在A02-2表应仅报送一条数据 ");
					}
				}
				// T_FAL_A02_3 外国来华直接投资（流量）
				else if ("T_FAL_A02_3".equalsIgnoreCase(a02.getTableId())) {
					// A0230 投资日期
					if (StringUtil.isEmpty(a02.getA0230())
							|| !a02.getA0230().startsWith(a02.getBuocmonth())) {
						map.put("A0230", "[投资日期] 不能为空且必须是报告期对应月内的日期 ");
					}
					// A0231 投资币种（ 原币）
					if (!verifyDictionaryValue(CURRENCY, a02.getA0231())) {
						String value = getKey(a02.getA0231(), CURRENCY);
						map.put("A0231", "[投资币种（ 原币）] [" + value + "] 无效 ");
					}
					// A0232 投资金额增减（+或-）
					if (a02.getA0232() == null) {
						map.put("A0232", "[投资金额增减（+或-）] 不能为空 ");
					}
					// A0233 外方持有表决权比例增减(%)
					if (a02.getA0233() == null
							|| a02.getA0233()
									.compareTo(new BigDecimal(-100.00)) < 0
							|| a02.getA0233().compareTo(new BigDecimal(100.00)) > 0) {
						map.put("A0233",
								"[外方持有表决权比例增减(%)] 不能为空且应当大于等于-100并小于等于100 ");
					}
					// A0136 是否兼并、收购
					if (!verifyDictionaryValue(YESORNO, a02.getA0234())) {
						String value = getKey(a02.getA0234(), YESORNO);
						map.put("A0234", "[是否兼并、收购] [" + value + "] 无效 ");
					}
					// A0137 出资方式
					if (!verifyDictionaryValue(WAYSOFINVESTMENT, a02.getA0235())) {
						String value = getKey(a02.getA0235(), WAYSOFINVESTMENT);
						map.put("A0235", "[出资方式] [" + value + "] 无效 ");
					}
					// 问答四-1.1表内核查规则-6
					if (StringUtil.isEmpty(a02.getRemark())
							&& a02.getA0232() != null && a02.getA0233() != null) {
						if (a02.getA0232().compareTo(new BigDecimal(0)) > 0
								&& a02.getA0233().compareTo(new BigDecimal(0)) < 0) {
							map
									.put("REMARK",
											"A0232投资金额＞0，而A233外方持有表决权比例增减＜0，备注（REMARK）字段必填");
						} else if (a02.getA0232().compareTo(new BigDecimal(0)) < 0
								&& a02.getA0233().compareTo(new BigDecimal(0)) > 0) {
							map
									.put("REMARK",
											"A0232投资金额＜0，而A233外方持有表决权比例增减＞0，备注（REMARK）字段必填");
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
