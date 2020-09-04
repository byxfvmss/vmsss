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
import com.cjit.gjsz.logic.model.Fal_D04Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_D04_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_D04_DataVerify() {
	}

	public Fal_D04_DataVerify(List dictionarys, List verifylList,
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
				Fal_D04Entity d04 = (Fal_D04Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, d04.getActiontype())) {
					String value = getKey(d04.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(d04.getActiontype())) {
					if (StringUtil.isEmpty(d04.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(d04.getActiontype())) {
					if (!isNull(d04.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (d04.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// D0401 s,1,3,3 [对方国家/地区]不能为空且需在字典表中有定义 COUNTRY
				if (!verifyDictionaryValue(COUNTRY, d04.getD0401())) {
					String value = getKey(d04.getD0401(), COUNTRY);
					map.put("D0401", "[对方国家/地区] [" + value + "] 无效 ");
				} else if (COUNTRY_CHN.equals(d04.getD0401())
						|| "N/A".equals(d04.getD0401())) {
					map.put("D0401", "[对方国家/地区] 不能选择CHN或N/A ");
				}
				// D0402 s,1,1,1 [对方部门]不能为空且需在字典表中有定义 INVESTORINST
				if (!verifyDictionaryValue(INVESTORINST, d04.getD0402())) {
					String value = getKey(d04.getD0402(), INVESTORINST);
					map.put("D0402", "[对方部门] [" + value + "] 无效 ");
				}
				// D0403 s,1,1,1 [对方与本机构的关系]不能为空且需在字典表中有定义 OPPOSITERELA
				if (!verifyDictionaryValue(OPPOSITERELA, d04.getD0403())) {
					String value = getKey(d04.getD0403(), OPPOSITERELA);
					map.put("D0403", "[对方与本机构的关系] [" + value + "] 无效 ");
				}
				// D0404 s,1,1,1 [原始期限]不能为空且需在字典表中有定义 ORIDEADLINE
				if (!verifyDictionaryValue(ORIDEADLINE, d04.getD0404())) {
					String value = getKey(d04.getD0404(), ORIDEADLINE);
					map.put("D0404", "[原始期限] [" + value + "] 无效 ");
				}
				// D0405 s,1,3,3 [原始币种]不能为空且需在字典表中有定义 CURRENCY
				if (!verifyDictionaryValue(CURRENCY, d04.getD0405())) {
					String value = getKey(d04.getD0405(), CURRENCY);
					map.put("D0405", "[原始币种] [" + value + "] 无效 ");
				}
				// D0406 n,1,24,2 [上月末余额]不能为空
				if (d04.getD0406() == null) {
					map.put("D0406", "[上月末余额] 不能为空 ");
				}
				// D0407 n,1,24,2 [本月末余额]不能为空
				if (d04.getD0407() == null) {
					map.put("D0407", "[本月末余额] 不能为空 ");
				}
				// D0408 n,1,24,2 [本月末余额：其中剩余期限在一年及以下]不能为空
				if (d04.getD0408() == null) {
					map.put("D0408", "[本月末余额：其中剩余期限在一年及以下] 不能为空 ");
				} else {
					// 当D0404=1 时，D0407=D0408；
					// 当D0404=2 时，|D0407|≥|D0408|。问答四-1.1表内核查规则-修改为绝对值进行比较
					if ("1".equals(d04.getD0404())) {
						if (d04.getD0408().compareTo(d04.getD0407()) != 0) {
							map
									.put("D0408",
											"[本月末余额：其中剩余期限在一年及以下] 当原始期限选择1时，应满足D0407=D0408 ");
						}
					} else if ("2".equals(d04.getD0404())) {
						// 问答四-1.1表内核查规则-修改为绝对值进行比较
						if (d04.getD0408().abs()
								.compareTo(d04.getD0407().abs()) > 0) {
							map
									.put("D0408",
											"[本月末余额：其中剩余期限在一年及以下] 当原始期限选择2时，应满足|D0407|≥|D0408| ");
						}
					}
				}
				// D0409 n,1,24,2 [本月非交易变动]不能为空
				if (d04.getD0409() == null) {
					map.put("D0409", "[本月非交易变动] 不能为空 ");
				}
				// D0410 n,1,24,2 [本月净发生额]不能为空
				if (d04.getD0410() == null) {
					map.put("D0410", "[本月净发生额] 不能为空 ");
				} else {
					// D0410+D0409=D0407-D0406
					BigDecimal d0410 = d04.getD0407().subtract(d04.getD0406())
							.subtract(d04.getD0409());
					if (d04.getD0410().compareTo(d0410) != 0) {
						map.put("D0410",
								"[本月净发生额] 应满足D0410+D0409=D0407-D0406 理论值为 "
										+ d0410.toString());
					}
				}
				// REMARK
				if (StringUtil.isEmpty(d04.getRemark())) {
					// 问答四-1.1表内核查规则-6
					if (d04.getD0406() != null
							&& d04.getD0406().compareTo(new BigDecimal(0)) < 0) {
						map.put("REMARK", "当D0406为负数时，备注字段必填");
					} else if (d04.getD0407() != null
							&& d04.getD0407().compareTo(new BigDecimal(0)) < 0) {
						map.put("REMARK", "当D0407为负数时，备注字段必填");
					} else if (d04.getD0408() != null
							&& d04.getD0408().compareTo(new BigDecimal(0)) < 0) {
						map.put("REMARK", "当D0408为负数时，备注字段必填");
					} else if (d04.getD0407() != null
							&& d04.getD0408() != null
							&& d04.getD0407().abs().compareTo(
									d04.getD0408().abs()) < 0) {
						map.put("REMARK", "当|D0407|<|D0408|时，备注字段必填");
					}
				}
				// 问答四-1.2跨表和跨期核查规则-30
				String preBuocMonth = DateUtils.getPreMonth(d04.getBuocmonth(),
						"yyyyMM");
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(preBuocMonth)
						.append("' and INSTCODE = '").append(d04.getInstcode())
						.append("' and D0405 = '").append(d04.getD0405())
						.append("' and DATASTATUS in (").append(
								DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> 'D' ");
				SearchModel searchModel = new SearchModel();
				searchModel.setTableId("T_FAL_D04");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				List d04List = service.search(searchModel);
				if (CollectionUtils.isNotEmpty(d04List)) {
					for (Iterator it = d04List.iterator(); it.hasNext();) {
						Fal_D04Entity pre = (Fal_D04Entity) it.next();
						if (pre != null
								&& pre.getD0401().equals(d04.getD0401())
								&& pre.getD0402().equals(d04.getD0402())) {
							// 上一个报告期的“本月金余额”
							BigDecimal preD0407 = pre.getD0407() == null ? new BigDecimal(
									0)
									: pre.getD0407();
							// 本报告期的“上月末余额”
							BigDecimal d0406 = d04.getD0406() == null ? new BigDecimal(
									0)
									: d04.getD0406();
							// 比较 上一个报告期的“本月末余额” 与 本报告期的“上月末余额”
							if (preD0407.compareTo(d0406) != 0) {
								map.put("D0406", "[上月末余额] 与上一个报告期的“本月末余额”"
										+ preD0407.toString() + "不一致 ");
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
