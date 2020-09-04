package com.cjit.gjsz.logic.impl;

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
import com.cjit.gjsz.logic.model.Fal_Z02Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_Z02_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_Z02_DataVerify() {
	}

	public Fal_Z02_DataVerify(List dictionarys, List verifylList,
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
				Fal_Z02Entity z02 = (Fal_Z02Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, z02.getActiontype())) {
					String value = getKey(z02.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(z02.getActiontype())) {
					if (StringUtil.isEmpty(z02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(z02.getActiontype())) {
					if (!isNull(z02.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (z02.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// TABLECODE s,1,5 [表号]不能为空且不能超过5位字符
				if (z02.getTablecode() == null) {
					map.put("TABLECODE", "[表号] 不能为空 ");
				} else if (!verifyDictionaryValue(FAL_TABLE_INFO, z02
						.getTablecode())) {
					String value = getKey(z02.getTablecode(), FAL_TABLE_INFO);
					map.put("TABLECODE", "[表号] [" + value + "] 无效 ");
				}  else {
					if (("Z01".equalsIgnoreCase(z02.getTablecode()) || "Z02"
							.equalsIgnoreCase(z02.getTablecode()))
							&& !"1".equals(z02.getZ0201())) {
						// 依问答（第一期）无论初次还是后续各月参加数据报送，Z02表中与Z01相关的Z0201项目必须选择1(是)
						// 依问答（第四期）Z01和Z02表的Z0201必须是“1-是”
						map
								.put("TABLECODE",
										"[表号] 与Z01、Z01相关的Z0201项目必须选择1(是) ");
						map.put("Z0201",
								"[本机构是否有相关业务] 与Z01、Z01相关的Z0201项目必须选择1(是) ");
					}
				}
				// TABLENAME s,1,256 [表名]不能为空且不能超过256位字符
				if (z02.getTablename() == null) {
					map.put("TABLENAME", "[表名] 不能为空 ");
				}
				// Z0201 s,1,1,1 [本机构是否有相关业务]不能为空且需在字典表中有定义 ISORNOT
				if (!verifyDictionaryValue(ISORNOT, z02.getZ0201())) {
					String value = getKey(z02.getZ0201(), ISORNOT);
					map.put("Z0201", "[本机构是否有相关业务] [" + value + "] 无效 ");
				}
				List rptlist = null;
				if (z02.getTablecode() != null && z02.getBuocmonth() != null
						&& !"D08".equalsIgnoreCase(z02.getTablecode())
						&& !"F02".equalsIgnoreCase(z02.getTablecode())) {
					SearchModel searchModel = new SearchModel();
					searchModel.setTableCode(z02.getTablecode());
					searchModel.setSearchCondition(" BUOCMONTH = '"
							+ z02.getBuocmonth() + "' and DATASTATUS <> "
							+ DataUtil.DELETE_STATUS_NUM + " and OBJCODE = '"
							+ z02.getObjcode() + "' ");
					rptlist = service.search(searchModel);
				}
				if ("1".equals(z02.getZ0201())) {
					// 对应报表必须填报数据
					if (CollectionUtil.isEmpty(rptlist)) {
						// 对于Z01，可以没有当前报告期的数据，外管会自动从原有信息中抓取
						// 对于D08、F02，不需在FAL系统中生成报文，外管自动从外债业务中抓取
						if (!"Z01".equalsIgnoreCase(z02.getTablecode())
								&& !"D08".equalsIgnoreCase(z02.getTablecode())
								&& !"F02".equalsIgnoreCase(z02.getTablecode())) {
							map.put("TABLECODE", "[表号] 对应报表无填报数据 ");
							map.put("Z0201", "[本机构是否有相关业务] 对应报表无填报数据 ");
						}
					}
					// Z0202 s,1,256 [填报部门]不能超过256位字符
					if (z02.getZ0202() == null) {
						map.put("Z0202", "[填报部门] 不能为空 ");
					}
					// Z0203 s,1,256 [统计负责人]不能超过256位字符
					if (z02.getZ0203() == null) {
						map.put("Z0203", "[统计负责人] 不能为空 ");
					}
					// Z0204 s,1,256 [统计负责人电话]不能超过256位字符
					if (z02.getZ0204() == null) {
						map.put("Z0204", "[统计负责人电话] 不能为空 ");
					}
					// Z0205 s,1,256 [填表人]不能超过256位字符
					if (z02.getZ0205() == null) {
						map.put("Z0205", "[填表人] 不能为空 ");
					}
					// Z0206 s,1,256 [经济类型]不能超过256位字符
					if (z02.getZ0206() == null) {
						map.put("Z0206", "[经济类型] 不能为空 ");
					}
				} else {
					// 相关报表为空
					if (CollectionUtil.isNotEmpty(rptlist)) {
						map.put("TABLECODE", "[表号] 相关报表存在数据 ");
						map.put("Z0201", "[本机构是否有相关业务] 相关报表存在数据 ");
					}
					// Z0202 s,1,256 [填报部门]不能超过256位字符
					if (StringUtil.isNotEmpty(z02.getZ0202())) {
						map.put("Z0202", "[填报部门] 应为空 ");
					}
					// Z0203 s,1,256 [统计负责人]不能超过256位字符
					if (StringUtil.isNotEmpty(z02.getZ0203())) {
						map.put("Z0203", "[统计负责人] 应为空 ");
					}
					// Z0204 s,1,256 [统计负责人电话]不能超过256位字符
					if (StringUtil.isNotEmpty(z02.getZ0204())) {
						map.put("Z0204", "[统计负责人电话] 应为空 ");
					}
					// Z0205 s,1,256 [填表人]不能超过256位字符
					if (StringUtil.isNotEmpty(z02.getZ0205())) {
						map.put("Z0205", "[填表人] 应为空 ");
					}
					// Z0206 s,1,256 [经济类型]不能超过256位字符
					if (StringUtil.isNotEmpty(z02.getZ0206())) {
						map.put("Z0206", "[经济类型] 应为空 ");
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
