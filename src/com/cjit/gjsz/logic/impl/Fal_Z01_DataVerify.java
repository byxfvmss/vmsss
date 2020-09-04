package com.cjit.gjsz.logic.impl;

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
import com.cjit.gjsz.logic.model.Fal_Z01Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_Z01_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_Z01_DataVerify() {
	}

	public Fal_Z01_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	// 金融业子类代码
	public static final List FINANCIAL_INDUSTRY = new ArrayList();
	static {
		FINANCIAL_INDUSTRY.add("6610");
		FINANCIAL_INDUSTRY.add("6620");
		FINANCIAL_INDUSTRY.add("6631");
		FINANCIAL_INDUSTRY.add("6632");
		FINANCIAL_INDUSTRY.add("6633");
		FINANCIAL_INDUSTRY.add("6639");
		FINANCIAL_INDUSTRY.add("6640");
		FINANCIAL_INDUSTRY.add("6711");
		FINANCIAL_INDUSTRY.add("6712");
		FINANCIAL_INDUSTRY.add("6713");
		FINANCIAL_INDUSTRY.add("6721");
		FINANCIAL_INDUSTRY.add("6729");
		FINANCIAL_INDUSTRY.add("6730");
		FINANCIAL_INDUSTRY.add("6740");
		FINANCIAL_INDUSTRY.add("6790");
		FINANCIAL_INDUSTRY.add("6811");
		FINANCIAL_INDUSTRY.add("6812");
		FINANCIAL_INDUSTRY.add("6820");
		FINANCIAL_INDUSTRY.add("6830");
		FINANCIAL_INDUSTRY.add("6840");
		FINANCIAL_INDUSTRY.add("6850");
		FINANCIAL_INDUSTRY.add("6860");
		FINANCIAL_INDUSTRY.add("6891");
		FINANCIAL_INDUSTRY.add("6899");
		FINANCIAL_INDUSTRY.add("6910");
		FINANCIAL_INDUSTRY.add("6920");
		FINANCIAL_INDUSTRY.add("6930");
		FINANCIAL_INDUSTRY.add("6940");
		FINANCIAL_INDUSTRY.add("6990");
	}

	public VerifyModel execute() {
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if (CollectionUtil.isNotEmpty(verifylList)) {
			service = (SearchService) SpringContextUtil
					.getBean("searchService");
			for (int i = 0; i < verifylList.size(); i++) {
				Fal_Z01Entity z01 = (Fal_Z01Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, z01.getActiontype())) {
					String value = getKey(z01.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(z01.getActiontype())) {
					if (StringUtil.isEmpty(z01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(z01.getActiontype())) {
					if (!isNull(z01.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (z01.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// Z0101 s,1,256 [填报单位名称]不能为空且不能超过256位字符
				if (StringUtil.isEmpty(z01.getZ0101())) {
					map.put("Z0101", "[填报单位名称] 不能为空 ");
				} else if (z01.getZ0101().equals(z01.getObjcode())) {
					// 问答四-1.1表内核查规则-8
					map.put("Z0101", "[填报单位名称] 与[OBJCODE填报机构代码]不得相同 ");
				}
				// Z0102 s,1,1 [证照类别]不能为空且需在字典表中有定义 LICENSETYPE
				if (!verifyDictionaryValue(LICENSETYPE, z01.getZ0102())) {
					String value = getKey(z01.getZ0102(), LICENSETYPE);
					map.put("Z0102", "[证照类别] [" + value + "] 无效 ");
				} else {
					// 问答四-1.1表内核查规则-11
					// 当“Z0102证照类别”=1或2时，“Z0107
					// 填报单位类型”≠4；当“Z0102证照类别”=3或4时，“Z0107 填报单位类型”=4
					if ("1".equals(z01.getZ0102())
							|| "2".equals(z01.getZ0102())) {
						if ("4".equals(z01.getZ0102())) {
							map.put("Z0107",
									"当[Z0102证照类别]=1或2时，[Z0107填报单位类型]≠4 ");
						}
					} else if ("3".equals(z01.getZ0102())
							|| "4".equals(z01.getZ0102())) {
						if (!"4".equals(z01.getZ0102())) {
							map.put("Z0107",
									"当[Z0102证照类别]=3或时，[Z0107填报单位类型]=4 ");
						}
					}
				}
				// Z0103 s,1,30 [证照号码]不能为空，如果Z0102=1 或2，为9位组织机构代码或特殊机构代码
				if (StringUtil.isEmpty(z01.getZ0103())) {
					map.put("Z0103", "[证照号码] 不能为空 ");
				} else if (("1".equals(z01.getZ0102()) || "2".equals(z01
						.getZ0102()))
						&& (z01.getZ0103().length() != 9 || !StringUtil
								.isNumberOrLatter(z01.getZ0103()))) {
					map.put("Z0103", "[证照号码] 当[证照类别]为1或2时，应为9位组织机构代码或特殊机构代码 ");
				} else if ("3".equals(z01.getZ0102())
						&& z01.getZ0103().length() != 15
						&& z01.getZ0103().length() != 18) {
					// 问答四-1.1表内核查规则-10 Z0102证照类别=3时，Z0103为15位或18位
					map.put("Z0103", "[证照号码] 当[证照类别]为3时，应为15位或18位 ");
				}
				// Z0104 s,1,4 [金融机构代码]不能为空
				if (StringUtil.isEmpty(z01.getZ0104())) {
					map.put("Z0104", "[金融机构代码] 不能为空 ");
				} else {
					if (FINANCIAL_INDUSTRY.contains(z01.getZ0105())
							&& z01.getZ0104().length() != 4) {
						map
								.put(
										"Z0104",
										"[金融机构代码] 当Z0105属于货币金融服务（1066）、资本市场服务（1067）、保险业（1068）或其他金融业（1069）下任一子类，则本字段填写国家外汇管理局赋予的4位代码 ");
					} else if (!FINANCIAL_INDUSTRY.contains(z01.getZ0105())
							&& !"N/A".equals(z01.getZ0104())) {
						map
								.put(
										"Z0104",
										"[金融机构代码] 当Z0105不属于货币金融服务（1066）、资本市场服务（1067）、保险业（1068）或其他金融业（1069）下任一子类，则本字段填写N/A");
					}
					if (StringUtil.isNotEmpty(z01.getObjcode())
							&& z01.getObjcode().length() == 12) {
						if ("N/A".equals(z01.getZ0104())) {
							map
									.put("Z0104",
											"填报机构使用12位金融机构标识码报送数据时，金融机构代码（Z0104）≠“N/A”");
						} else if (!z01.getObjcode().substring(6, 10).equals(
								z01.getZ0104())) {
							map
									.put("Z0104",
											"填报机构使用12位金融机构标识码报送数据时，该12位码的第7-第10位即该机构的4位金融机构代码");
						}
					}
				}
				// Z0105 s,1,4,4 [机构所属行业代码]不能为空且需在字典表中有定义 INDUSTRY
				if (!verifyDictionaryValue(INDUSTRY, z01.getZ0105())) {
					String value = getKey(z01.getZ0105(), INDUSTRY);
					map.put("Z0105", "[机构所属行业代码] [" + value + "] 无效 ");
				} else {
					if ("1066".equals(z01.getZ0105())
							|| "1067".equals(z01.getZ0105())
							|| "1068".equals(z01.getZ0105())
							|| "1069".equals(z01.getZ0105())) {
						map.put("Z0105", "[机构所属行业代码] 应选择最底层行业代码 ");
					}
					// 问答四-1.1表内核查规则-14
					if ("1990".equals(z01.getZ0105())
							|| "1991".equals(z01.getZ0105())
							|| "1992".equals(z01.getZ0105())
							|| "1993".equals(z01.getZ0105())
							|| "1994".equals(z01.getZ0105())
							|| "1995".equals(z01.getZ0105())
							|| "2099".equals(z01.getZ0105())
							|| "6610".equals(z01.getZ0105())
							|| "6640".equals(z01.getZ0105())
							|| "6730".equals(z01.getZ0105())
							|| "6860".equals(z01.getZ0105())) {
						map.put("Z0105",
								"[机构所属行业代码] 国有政策性银行和商业银行均应选择6620-货币银行服务 ");
					}
				}
				// Z0106 s,1,3,3 [经济类型]不能为空且需在字典表中有定义 ECONOMICTYPE
				if (!verifyDictionaryValue(ECONOMICTYPE, z01.getZ0106())) {
					String value = getKey(z01.getZ0106(), ECONOMICTYPE);
					map.put("Z0106", "[经济类型] [" + value + "] 无效 ");
				}
				// Z0107 s,1,1 [填报单位类型]不能为空且需在字典表中有定义 INSTTYPE
				if (!verifyDictionaryValue(INSTTYPE, z01.getZ0107())) {
					String value = getKey(z01.getZ0107(), INSTTYPE);
					map.put("Z0107", "[填报单位类型] [" + value + "] 无效 ");
				} else if ("2".equals(z01.getZ0107())) {
					// 问答四-1.1表内核查规则-15
					if (z01.getZ0106() != null
							&& !z01.getZ0106().startsWith("2")
							&& !z01.getZ0106().startsWith("3")) {
						map.put("Z0106",
								"当[填报单位类型]为2-分支机构时，[经济类型]应选择200或300下的子项 ");
					}
				}
				// Z0108 s,1,6 [所在地]不能为空且需在字典表中有定义 DISTRICTCO
				if (!verifyDictionaryValue(DISTRICTCO, z01.getZ0108())) {
					String value = getKey(z01.getZ0108(), DISTRICTCO);
					map.put("Z0108", "[所在地] [" + value + "] 无效 ");
				}
				// Z0109 s,1,256 [地址]不能超过256位字符
				// DPT s,1,256 [牵头部门]不能为空且不能超过256位字符
				if (StringUtil.isEmpty(z01.getDpt())) {
					map.put("DPT", "[牵头部门] 不能为空 ");
				}
				// CONTACT s,1,256 [牵头部门联系人]不能为空且不能超过256位字符
				if (StringUtil.isEmpty(z01.getContact())) {
					map.put("CONTACT", "[牵头部门联系人] 不能为空 ");
				}
				// TEL s,1,256 [牵头部门联系电话]不能为空且不能超过256位字符
				if (StringUtil.isEmpty(z01.getTel())) {
					map.put("TEL", "[牵头部门联系电话] 不能为空 ");
				} else {
					// 问答四-1.1表内核查规则-16
					String[] tels = z01.getTel().split("");
					boolean noNumber = true;
					for (int t = 0; t < tels.length; t++) {
						if (StringUtil.isNumLegal(tels[t])
								&& StringUtil.isNotEmpty(tels[t])) {
							noNumber = false;
							break;
						}
					}
					if (noNumber) {
						map.put("TEL", "TEL联系电话必须有数字 ");
					}
				}
				// 问答四-1.1表内核查规则-7 一家填报机构一个报告期应只有一条单位基本情况表
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(
						z01.getBuocmonth()).append("' and INSTCODE = '")
						.append(z01.getInstcode()).append(
								"' and BUSINESSID <> '").append(
								z01.getBusinessid()).append(
								"' and DATASTATUS in (").append(
								DataUtil.JYYTG_STATUS_NUM).append(",").append(
								DataUtil.YTJDSH_STATUS_NUM).append(",").append(
								DataUtil.SHWTG_STATUS_NUM).append(",").append(
								DataUtil.SHYTG_STATUS_NUM).append(",").append(
								DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> '").append(ACTIONTYPE_D)
						.append("' ");
				SearchModel searchModel = new SearchModel();
				searchModel.setTableId("T_FAL_Z01");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				long count = service.getCount(searchModel);
				if (count > 0) {
					map.put("OBJCODE", "一家填报机构一个报告期应只有一条单位基本情况表 ");
					map.put("Z0101", "一家填报机构一个报告期应只有一条单位基本情况表 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
