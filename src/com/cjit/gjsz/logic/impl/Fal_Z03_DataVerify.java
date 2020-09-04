package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Fal_Z03Entity;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_Z03_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_Z03_DataVerify() {
	}

	public Fal_Z03_DataVerify(List dictionarys, List verifylList,
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
				Fal_Z03Entity z03 = (Fal_Z03Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, z03.getActiontype())) {
					String value = getKey(z03.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(z03.getActiontype())) {
					if (StringUtil.isEmpty(z03.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(z03.getActiontype())) {
					if (!isNull(z03.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (z03.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// INVEST 投资信息
				List investList = service.getCfaChildren("T_FAL_Z03_INVEST",
						z03.getBusinessid());
				List list = new ArrayList();
				if (CollectionUtil.isNotEmpty(investList)) {
					for (int j = 0; j < investList.size(); j++) {
						Fal_Z03Entity z03invest = (Fal_Z03Entity) investList
								.get(j);
						Map mapSub = new HashMap();
						// Z0301 s,0,256 [投资者名称]不能超过256位字符
						if (StringUtil.isNotEmpty(z03invest.getZ0301())) {
							// Z0302 s,0,256 [投资者代码]不能超过256位字符
							if (StringUtil.isEmpty(z03invest.getZ0302())) {
								mapSub.put("Z0302", "[投资者代码] 不能为空 ");
							}
							// Z0303 s,0,3,3 [投资者所属国家/地区]不能为空且需在字典表中有定义 COUNTRY
							if (z03invest.getZ0303() == null) {
								mapSub.put("Z0303", "[投资者所属国家/地区] 不能为空 ");
							} else if (!verifyDictionaryValue(COUNTRY,
									z03invest.getZ0303())) {
								String value = getKey(z03invest.getZ0303(),
										COUNTRY);
								mapSub.put("Z0303", "[投资者所属国家/地区] [" + value
										+ "] 无效 ");
							}
							// Z0304 s,0,1,1 [投资者所属部门]
							if (z03invest.getZ0304() == null) {
								mapSub.put("Z0304", "[投资者所属部门] 不能为空 ");
							} else if (!verifyDictionaryValue(INVESTORINST,
									z03invest.getZ0304())) {
								String value = getKey(z03invest.getZ0304(),
										INVESTORINST);
								mapSub.put("Z0304", "[投资者所属部门] [" + value
										+ "] 无效 ");
							}
							// Z0305 n,0,24,2 [投资者表决权比例（%）]
							if (z03invest.getZ0305() == null) {
								mapSub.put("Z0305", "[投资者表决权比例（%）] 不能为空 ");
							} else if (z03invest.getZ0305().compareTo(
									new BigDecimal(10.00)) < 0
									|| z03invest.getZ0305().compareTo(
											new BigDecimal(100.00)) > 0) {
								mapSub.put("Z0305",
										"[投资者表决权比例（%）] 取值范围大于等于10并小于等于100 ");
							}
							// Z0306 s,0,256 [被投资机构名称]不能超过256位字符
							if (z03invest.getZ0306() == null) {
								mapSub.put("Z0306", "[被投资机构名称] 不能为空 ");
							}
							// Z0307 s,0,256 [被投资机构代码]不能超过256位字符
							if (z03invest.getZ0307() == null) {
								mapSub.put("Z0307", "[被投资机构代码] 不能为空 ");
							}
							// Z0308 s,0,3,3 [被投资机构所属国家/地区]需在字典表中有定义
							if (z03invest.getZ0308() == null) {
								mapSub.put("Z0308", "[被投资机构所属国家/地区] 不能为空 ");
							} else if (!verifyDictionaryValue(COUNTRY,
									z03invest.getZ0308())) {
								String value = getKey(z03invest.getZ0308(),
										COUNTRY);
								mapSub.put("Z0308", "[被投资机构所属国家/地区] [" + value
										+ "] 无效 ");
							}
							// Z0309 s,0,1,1 [被投资机构所属部门]需在字典表中有定义
							if (z03invest.getZ0309() == null) {
								mapSub.put("Z0309", "[被投资机构所属部门] 不能为空 ");
							} else if (!verifyDictionaryValue(INVESTORINST,
									z03invest.getZ0309())) {
								String value = getKey(z03invest.getZ0309(),
										INVESTORINST);
								mapSub.put("Z0309", "[被投资机构所属部门] [" + value
										+ "] 无效 ");
							} else if ("1".equals(z03invest.getZ0309())
									|| "2".equals(z03invest.getZ0309())
									|| "6".equals(z03invest.getZ0309())) {
								mapSub
										.put("Z0309",
												"[被投资机构所属部门] 本项不能选择1或2或6。 ");
							}
							// 问答四-1.1表内核查规则-
							if (z03invest.getZ0302() != null
									&& z03invest.getZ0307() != null
									&& z03invest.getZ0302().equals(
											z03invest.getZ0307())) {
								mapSub.put("Z0302", "[投资者代码] 不能与“被投资机构代码”相同 ");
								mapSub.put("Z0307", "[被投资机构代码] 不能与“投资者代码”相同 ");
							}
						} else {
							if (StringUtil.isEmpty(z03invest.getZ0301())
									&& StringUtil.isEmpty(z03invest.getZ0302())
									&& StringUtil.isEmpty(z03invest.getZ0303())
									&& StringUtil.isEmpty(z03invest.getZ0304())
									&& z03invest.getZ0305() == null
									&& StringUtil.isEmpty(z03invest.getZ0306())
									&& StringUtil.isEmpty(z03invest.getZ0307())
									&& StringUtil.isEmpty(z03invest.getZ0308())
									&& StringUtil.isEmpty(z03invest.getZ0309())) {
								mapSub.put("Z0301", "投资者、被投资者信息必须不能全为空");
							}
						}
						if (mapSub != null && !mapSub.isEmpty()) {
							mapSub.put(SUBID, z03invest.getSubid());
							mapSub.put(INNERTABLEID, "T_FAL_Z03_INVEST");
							list.add(mapSub);
						}
					}
					if (CollectionUtil.isNotEmpty(list)) {
						verifyModel.setChildren(list);
					}
				} else {
					map.put("T_FAL_Z03_INVEST", "[投资信息] 不能为空 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
