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
import com.cjit.gjsz.logic.model.Fal_A01Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_A01_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_A01_DataVerify() {
	}

	public Fal_A01_DataVerify(List dictionarys, List verifylList,
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
				Fal_A01Entity a01 = (Fal_A01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, a01.getActiontype())) {
					String value = getKey(a01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(a01.getActiontype())) {
					if (StringUtil.isEmpty(a01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(a01.getActiontype())) {
					if (!isNull(a01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (a01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// T_FAL_A01_1 对外直接投资（资产负债表及市值）
				if ("T_FAL_A01_1".equalsIgnoreCase(a01.getTableId())) {
					// A0103 境外被投资机构所属国家/地区
					if (!verifyDictionaryValue(COUNTRY, a01.getA0103())) {
						String value = getKey(a01.getA0103(), COUNTRY);
						map
								.put("A0103", "[境外被投资机构所属国家/地区] [" + value
										+ "] 无效 ");
					} else if (COUNTRY_CHN.equalsIgnoreCase(a01.getA0103())) {
						// 问答四-取消
						// map.put("A0103", "[境外被投资机构所属国家/地区] 不能选择CHN ");
					}
					// A0104 境外被投资机构所属行业
					if (!verifyDictionaryValue(INVESTOR_INDUSTRY, a01
							.getA0104())) {
						String value = getKey(a01.getA0104(), INVESTOR_INDUSTRY);
						map.put("A0104", "[境外被投资机构所属行业] [" + value + "] 无效 ");
					} else if ("8".equals(a01.getA0104())
							|| "9".equals(a01.getA0104())
							|| "10".equals(a01.getA0104())) {
						// 问答四-1.1表内核查规则-29
						map
								.put("A0104",
										"[境外被投资机构所属行业] A0104≠8（政府）、9（中央银行/货币当局）或10(国际组织)");
					}
					// A0105 境外被投资机构与本机构的关系
					if (!verifyDictionaryValue(INVESTORRELA, a01.getA0105())) {
						String value = getKey(a01.getA0105(), INVESTORRELA);
						map
								.put("A0105", "[境外被投资机构与本机构的关系] [" + value
										+ "] 无效 ");
					} else {
						// 根据A0108 判断：
						// 如果10≤A0108≤50，则A0105=1 或3
						// 如果50<A0108≤100，则A0105=1 或2
						// if (a01.getA0108() != null) {
						// if (a01.getA0108().compareTo(new BigDecimal(10)) >= 0
						// && a01.getA0108().compareTo(
						// new BigDecimal(50)) <= 0
						// && !"1".equals(a01.getA0105())
						// && !"3".equals(a01.getA0105())) {
						// map
						// .put("A0105",
						// "[境外被投资机构与本机构的关系] 当10≤A0108≤50时，此处应选择1或3 ");
						// } else if (a01.getA0108().compareTo(
						// new BigDecimal(50)) > 0
						// && a01.getA0108().compareTo(
						// new BigDecimal(100)) <= 0
						// && !"1".equals(a01.getA0105())
						// && !"2".equals(a01.getA0105())) {
						// map
						// .put("A0105",
						// "[境外被投资机构与本机构的关系] 当50<A0108≤100时，此处应选择1或2 ");
						// }
						// }
					}
					// A0107 最终控制方所属国家/地区
					if (!"N/A".equals(a01.getA0106())) {
						// A0107必填
						if (StringUtil.isEmpty(a01.getA0107())) {
							map.put("A0107",
									"[最终控制方所属国家/地区] 当[最终控制方]不为'N/A'时，此项必填 ");
						} else if (!verifyDictionaryValue(COUNTRY, a01
								.getA0107())) {
							String value = getKey(a01.getA0107(), COUNTRY);
							map.put("A0107", "[最终控制方所属国家/地区] [" + value
									+ "] 无效 ");
						}
					} else {
						// A0107为空
						if (StringUtil.isNotEmpty(a01.getA0107())) {
							map.put("A0107",
									"[最终控制方所属国家/地区] 当[最终控制方全称]为'N/A'时，此项为空 ");
						}
					}
					// A0108 期末（本机构）表决权比例（%）
					if (a01.getA0108() == null
							|| a01.getA0108().compareTo(new BigDecimal(0.00)) < 0
							|| a01.getA0108().compareTo(new BigDecimal(100.00)) > 0) {
						map.put("A0108",
								"[期末（本机构）表决权比例（%）] 不能为空且应当大于等于0并小于等于100 ");
					} else if (a01.getA0108().compareTo(new BigDecimal(10.00)) < 0) {
						// 问答四-1.1表内核查规则-30
						if (a01.getA0113() != null
								&& a01.getA0113().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0113",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0114() != null
								&& a01.getA0114().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0114",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0115() != null
								&& a01.getA0115().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0115",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0116() != null
								&& a01.getA0116().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0116",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0117() != null
								&& a01.getA0117().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0117",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0118() != null
								&& a01.getA0118().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0118",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0119() != null
								&& a01.getA0119().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0119",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0120() != null
								&& a01.getA0120().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0120",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0121() != null
								&& a01.getA0121().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0121",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0122() != null
								&& a01.getA0122().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0122",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0123() != null
								&& a01.getA0123().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0123",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0124() != null
								&& a01.getA0124().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0124",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0125() != null
								&& a01.getA0125().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0125",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0126() != null
								&& a01.getA0126().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0126",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0127() != null
								&& a01.getA0127().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0127",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						if (a01.getA0128() != null
								&& a01.getA0128().compareTo(
										new BigDecimal(0.00)) != 0) {
							map
									.put("A0128",
											"当0（%）≤A0108＜10（%），A0113至A0128均为0");
						}
						StringBuffer sbSearchCondition = new StringBuffer();
						sbSearchCondition.append(" BUOCMONTH = '").append(
								a01.getBuocmonth())
								.append("' and INSTCODE = '").append(
										a01.getInstcode()).append(
										"' and datastatus <> 0 ");
						SearchModel searchModel = new SearchModel();
						searchModel.setTableId("T_FAL_A01_2");
						searchModel.setSearchCondition(sbSearchCondition
								.toString());
						long count = service.getCount(searchModel);
						if (count == 0) {
							map.put("A0108",
									"当0（%）≤A0108＜10（%），A01-2表必填，但当前并未录入A01-2表");
						}
					} else if (a01.getA0108().compareTo(new BigDecimal(10.00)) >= 0) {
						// 问答四-1.1表内核查规则-31修改
						// 当A0108≥10（%）时：
						// A0105=1或2时，50<A0108≤100
						// A0105=3时，10≤A0108≤50
						if ("1".equals(a01.getA0105())
								|| "2".equals(a01.getA0105())) {
							if (a01.getA0108().compareTo(new BigDecimal(50.00)) <= 0
									|| a01.getA0108().compareTo(
											new BigDecimal(100.00)) > 0) {
								map
										.put("A0108",
												"[期末（本机构）表决权比例（%）] 若A0108≥10（%）时，需满足A0105=1或2时，50<A0108≤100 ");
							}
						} else if ("3".equals(a01.getA0105())) {
							if (a01.getA0108().compareTo(new BigDecimal(10.00)) < 0
									|| a01.getA0108().compareTo(
											new BigDecimal(50.00)) > 0) {
								map
										.put("A0108",
												"[期末（本机构）表决权比例（%）] 若A0108≥10（%）时，需满足A0105=3时，10≤A0108≤50 ");
							}
						}
					}
					// A0109 期末（本机构）持股比例（%）
					if (a01.getA0109() == null
							|| a01.getA0109().compareTo(new BigDecimal(0.00)) < 0
							|| (a01.getA0109().compareTo(new BigDecimal(0.00)) == 0
									&& a01.getA0108() != null && a01.getA0108()
									.compareTo(new BigDecimal(10.00)) >= 0)
							|| a01.getA0109().compareTo(new BigDecimal(100.00)) > 0) {
						map
								.put("A0109",
										"[期末（本机构）持股比例（%）] 不能为空且应当大于0并小于等于100 ");
					}
					// A0110 本机构是否通过SPV或壳机构持有该境外被投资机构
					if (!verifyDictionaryValue(YESORNO, a01.getA0110())) {
						String value = getKey(a01.getA0110(), YESORNO);
						map.put("A0110", "[本机构是否通过SPV或壳机构持有该境外被投资机构] [" + value
								+ "] 无效 ");
					}
					if (YESORNO_1.equals(a01.getA0110())) {
						// A0111 该SPV或壳机构所属国家/地区
						if (StringUtil.isEmpty(a01.getA0111())) {
							map.put("A0111", "[该SPV或壳机构所属国家/地区] 不能为空 ");
						} else if (COUNTRY_CHN.equals(a01.getA0111())
								|| "N/A".equals(a01.getA0111())) {
							map.put("A0111", "[该SPV或壳机构所属国家/地区] 不能选择CHN或N/A ");
						} else if (!verifyDictionaryValue(COUNTRY, a01
								.getA0111())) {
							String value = getKey(a01.getA0111(), COUNTRY);
							map.put("A0111", "[该SPV或壳机构所属国家/地区] [" + value
									+ "] 无效 ");
						}
						// A0103 境外被投资机构所属国家/地区
						if ("N/A".equals(a01.getA0103())) {
							map.put("A0103", "[境外被投资机构所属国家/地区] 不能选择N/A ");
						} else if (COUNTRY_CHN.equals(a01.getA0103())) {
							if (StringUtil.isEmpty(a01.getRemark())) {
								map.put("REMARK",
										" A0110选择1-是，且A0103选择CHN时，备注字段必填 ");
							}
						}
					} else if (YESORNO_0.equals(a01.getA0110())) {
						if (COUNTRY_CHN.equals(a01.getA0103())
								|| "N/A".equals(a01.getA0103())) {
							map.put("A0111", "[境外被投资机构所属国家/地区] 不能选择CHN或N/A ");
						}
					}
					// A0112 期末会计记账币种（原币）
					if (!verifyDictionaryValue(CURRENCY, a01.getA0112())) {
						String value = getKey(a01.getA0112(), CURRENCY);
						map.put("A0112", "[期末会计记账币种（原币）] [" + value + "] 无效 ");
					}
					// A0113 期末资产
					if (a01.getA0113() == null
							|| a01.getA0113().compareTo(new BigDecimal("0")) < 0) {
						map.put("A0113", "[期末资产] 不能为空且应当大于等于0 ");
					}
					// A0114 期末负债
					if (a01.getA0114() == null
							|| a01.getA0114().compareTo(new BigDecimal("0")) < 0) {
						map.put("A0114", "[期末负债] 不能为空且应当大于等于0 ");
					}
					// A0115 期末负债中本机构拨付的营运资金
					if (a01.getA0115() == null
							|| a01.getA0115().compareTo(new BigDecimal("0")) < 0) {
						map.put("A0115", "[期末负债中本机构拨付的营运资金] 不能为空且应当大于等于0 ");
					}
					// A0116 期末归属于被投资机构全体股东的权益（总额）
					if (a01.getA0116() == null) {
						map.put("A0116", "[期末归属于被投资机构全体股东的权益（总额）] 不能为空 ");
					} else {
						// A0113-A0114=A0116+A0122
						BigDecimal a0116_1 = a01.getA0113().subtract(
								a01.getA0114()).subtract(a01.getA0122());
						// A0116=A0117+A0118+A0119+A0120+A0121
						BigDecimal a0116_2 = a01.getA0117().add(a01.getA0118())
								.add(a01.getA0119()).add(a01.getA0120()).add(
										a01.getA0121());
						if (a01.getA0116().compareTo(a0116_1) != 0) {
							// A0113-A0114=A0116+A0122
							String errorMsg = "[期末归属于被投资机构全体股东的权益（总额）] 应满足 A0113-A0114=A0116+A0122";
							if (a0116_1.compareTo(a0116_2) == 0) {
								errorMsg += " 理论值为 " + a0116_1.toString();
							}
							map.put("A0116", errorMsg);
						} else if (a01.getA0116().compareTo(a0116_2) != 0) {
							// A0116=A0117+A0118+A0119+A0120+A0121
							String errorMsg = "[期末归属于被投资机构全体股东的权益（总额）] 应满足 A0116=A0117+A0118+A0119+A0120+A0121";
							if (a0116_1.compareTo(a0116_2) == 0) {
								errorMsg += " 理论值为 " + a0116_2.toString();
							}
							map.put("A0116", errorMsg);
						}
					}
					// A0125 本期宣告分配（本机构）利润
					if (a01.getA0125() == null
							|| a01.getA0125().compareTo(new BigDecimal("0")) < 0) {
						map.put("A0125", "[本期宣告分配（本机构）利润] 不能为空且应当大于等于0 ");
					}
					if (a01.getBuocmonth().endsWith("12")
							&& a01.getA0108().compareTo(new BigDecimal("50")) > 0) {
						// A0126 对所在国缴纳的税金总额
						if (a01.getA0126() == null) {
							map.put("A0126", "[对所在国缴纳的税金总额] 不能为空 ");
						}
						// A0127 年末从业人数(人)
						if (a01.getA0127() == null
								|| a01.getA0127()
										.compareTo(new BigDecimal("0")) < 0) {
							map.put("A0127", "[年末从业人数(人)] 不能为空且应当大于等于0 ");
						}
						// A0128 年末从业人数中的中方雇员数
						if (a01.getA0128() == null
								|| a01.getA0128()
										.compareTo(new BigDecimal("0")) < 0
								|| a01.getA0128().compareTo(a01.getA0127()) > 0) {
							map
									.put("A0128",
											"[年末从业人数中的中方雇员数] 不能为空且应当大于等于0并小于等于年末从业人数(人) ");
						}
					}
					// REMARK
					if (StringUtil.isEmpty(a01.getRemark())) {
						// 问答四-1.1表内核查规则-6
						if (COUNTRY_CHN.equalsIgnoreCase(a01.getA0103())) {
							map.put("REMARK",
									"当[A0103境外被投资机构所属国家/地区]选择CHN时，备注字段必填 ");
						}
						if (a01.getA0108() != null
								&& a01.getA0108().compareTo(
										new BigDecimal(10.00)) < 0) {
							map.put("REMARK", "当0(%)<=A0108<10(%)时，备注字段必填 ");
						}
						if (a01.getA0114() != null && a01.getA0115() != null
								&& a01.getA0114().compareTo(a01.getA0115()) < 0) {
							map.put("REMARK", "当A0114<A0115时，备注字段必填 ");
						}
						if (a01.getA0126() != null
								&& a01.getA0126().compareTo(
										new BigDecimal(0.00)) < 0) {
							map.put("REMARK", "当A0126<0时，备注字段必填 ");
						}
					}
					// STOCKINFO 股票信息
					List stockList = service.getFalChildren(
							"T_FAL_A01_1_STOCKINFO", a01.getBusinessid());
					List list = new ArrayList();
					if (CollectionUtil.isNotEmpty(stockList)) {
						if (stockList.size() > 5) {
							map.put("T_FAL_A01_1_STOCKINFO",
									"[股票信息] 接口方式下，最多填写5种不同记账币种的数据 ");
						}
						for (int j = 0; j < stockList.size(); j++) {
							Fal_A01Entity a01stock = (Fal_A01Entity) stockList
									.get(j);
							Map mapSub = new HashMap();
							if ("N/A".equalsIgnoreCase(a01stock.getA0129())) {
								// A0130 本机构持股（份额）数量
								if (a01stock.getA0130() == null
										|| a01stock.getA0130().compareTo(
												new BigDecimal("0")) != 0) {
									mapSub
											.put("A0130",
													"[本机构持股（份额）数量] 当可流通股票（份额）的记账币种（原币）为N/A时，此项当为0 ");
								}
								// A0131 每股（每份）市价
								if (a01stock.getA0131() == null
										|| a01stock.getA0131().compareTo(
												new BigDecimal("0")) != 0) {
									mapSub
											.put("A0131",
													"[每股（每份）市价量] 当可流通股票（份额）的记账币种（原币）为N/A时，此项当为0 ");
								}
							} else {
								// A0129 可流通股票（份额）的记账币种（原币）
								if (!verifyDictionaryValue(CURRENCY, a01stock
										.getA0129())) {
									String value = getKey(a01.getA0129(),
											CURRENCY);
									mapSub.put("A0129",
											"[可流通股票（份额）的记账币种（原币）] [" + value
													+ "] 无效 ");
								}
								// A0130 本机构持股（份额）数量
								if (a01stock.getA0130() == null
										|| a01stock.getA0130().compareTo(
												new BigDecimal("0")) <= 0) {
									mapSub.put("A0130",
											"[本机构持股（份额）数量] 不能为空且应当大于0 ");
								}
								// A0131 每股（每份）市价
								if (a01stock.getA0131() == null
										|| a01stock.getA0131().compareTo(
												new BigDecimal("0")) <= 0) {
									mapSub.put("A0131",
											"[每股（每份）市价量] 不能为空且应当大于0");
								}
								// 问答四-1.1表内核查规则-33
								if (a01stock.getA0130() != null
										&& a01stock.getA0131() != null
										&& a01stock.getA0131().compareTo(
												a01stock.getA0131()) == 0) {
									mapSub
											.put("A0130",
													"[本机构持股（份额）数量] A0129非N/A时，A0130、A0131不能同时填写为相同数值 ");
									mapSub
											.put("A0131",
													"[每股（每份）市价量] A0129非N/A时，A0130、A0131不能同时填写为相同数值 ");
								}
							}
							if (mapSub != null && !mapSub.isEmpty()) {
								mapSub.put(SUBID, a01stock.getSubid());
								mapSub.put(INNERTABLEID,
										"T_FAL_A01_1_STOCKINFO");
								list.add(mapSub);
							}
						}
						if (CollectionUtil.isNotEmpty(list)) {
							verifyModel.setChildren(list);
						}
					} else {
						map.put("T_FAL_A01_1_STOCKINFO", "[股票信息] 不能为空 ");
					}
					// 问答四-1.1表内核查规则-27
					StringBuffer sbSearchCondition = new StringBuffer();
					sbSearchCondition.append(" BUOCMONTH = '").append(
							a01.getBuocmonth()).append("' and INSTCODE = '")
							.append(a01.getInstcode()).append(
									"' and BUSINESSID <> '").append(
									a01.getBusinessid()).append(
									"' and A0101 = '").append(a01.getA0101())
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
					searchModel.setTableId("T_FAL_A01_1");
					searchModel
							.setSearchCondition(sbSearchCondition.toString());
					long count = service.getCount(searchModel);
					if (count > 0) {
						map.put("A0101", "同一报告期，一个被投资机构应只有一条（合并）资产负债表 ");
					}
				}
				// T_FAL_A01_2 对外直接投资（流量）
				else if ("T_FAL_A01_2".equalsIgnoreCase(a01.getTableId())) {
					// A0101 境外被投资机构代码
					// A0132 投资日期
					if (StringUtil.isEmpty(a01.getA0132())
							|| !a01.getA0132().startsWith(a01.getBuocmonth())) {
						map.put("A0132", "[投资日期] 不能为空且必须是报告期对应月内的日期 ");
					}
					// A0133 投资币种（原币）
					if (!verifyDictionaryValue(CURRENCY, a01.getA0133())) {
						String value = getKey(a01.getA0133(), CURRENCY);
						map.put("A0133", "[投资币种（原币）] [" + value + "] 无效 ");
					}
					// A0134 投资金额增减（+或-）
					if (a01.getA0134() == null) {
						map.put("A0134", "[投资金额增减（+或-）] 不能为空 ");
					}
					// A0135 本机构持有表决权比例增减(%)
					if (a01.getA0135() == null
							|| a01.getA0135()
									.compareTo(new BigDecimal(-100.00)) < 0
							|| a01.getA0135().compareTo(new BigDecimal(100.00)) > 0) {
						map.put("A0135",
								"[本机构持有表决权比例增减(%)] 不能为空且应当大于等于-100并小于等于100 ");
					}
					// A0136 是否兼并、收购
					if (!verifyDictionaryValue(YESORNO, a01.getA0136())) {
						String value = getKey(a01.getA0136(), YESORNO);
						map.put("A0136", "[是否兼并、收购] [" + value + "] 无效 ");
					}
					// A0137 出资方式
					if (!verifyDictionaryValue(WAYSOFINVESTMENT, a01.getA0137())) {
						String value = getKey(a01.getA0137(), WAYSOFINVESTMENT);
						map.put("A0137", "[出资方式] [" + value + "] 无效 ");
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
