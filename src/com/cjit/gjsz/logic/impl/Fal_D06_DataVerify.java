package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
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
import com.cjit.gjsz.logic.model.Fal_D06Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class Fal_D06_DataVerify extends SelfDataVerify implements DataVerify {
	public Fal_D06_DataVerify() {
	}

	public Fal_D06_DataVerify(List dictionarys, List verifylList,
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
				Fal_D06Entity d06 = (Fal_D06Entity) verifylList.get(i);
				// 操作类型/删除原因
				if (!verifyDictionaryValue(ACTIONTYPE, d06.getActiontype())) {
					String value = getKey(d06.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				} else if (ACTIONTYPE_D.equalsIgnoreCase(d06.getActiontype())) {
					if (StringUtil.isEmpty(d06.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				} else if (!ACTIONTYPE_D.equalsIgnoreCase(d06.getActiontype())) {
					if (!isNull(d06.getActiondesc())) {
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 报告期 YYYYMM
				if (d06.getBuocmonth() == null) {
					map.put("BUOCMONTH", "[报告期] 不能为空 ");
				}
				// D0607 上月末应付利息余额 数值型，22.2 必填项，必须≥0。
				if (d06.getD0607() == null
						|| d06.getD0607().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0607", "[上月末应付利息余额] 不能为空且应当大于等于0 ");
				}
				// D0610 本月末应付利息余额 数值型，22.2 必填项，必须≥0。
				if (d06.getD0610() == null
						|| d06.getD0610().compareTo(new BigDecimal(0)) < 0) {
					map.put("D0610", "[本月末应付利息余额] 不能为空且应当大于等于0 ");
				}
				// D0611 本月非交易变动 数值型，22.2 必填项。
				if (d06.getD0611() == null) {
					map.put("D0611", "[本月非交易变动] 不能为空 ");
				}
				// 问答四-1.1表内核查规则-58
				if (d06.getD0607() != null
						&& d06.getD0607().compareTo(new BigDecimal(0)) == 0
						&& d06.getD0610() != null
						&& d06.getD0610().compareTo(new BigDecimal(0)) == 0
						&& d06.getD0611() != null
						&& d06.getD0611().compareTo(new BigDecimal(0)) == 0) {
					map.put("D0607",
							"每报告期同一外债编号（EXDEBTCODE）下，D0607、D0610和D0611不能同时为0");
					map.put("D0610",
							"每报告期同一外债编号（EXDEBTCODE）下，D0607、D0610和D0611不能同时为0");
					map.put("D0611",
							"每报告期同一外债编号（EXDEBTCODE）下，D0607、D0610和D0611不能同时为0");
				}
				// 问答四-1.1表内核查规则-57 同一外债编号（EXDEBTCODE）下，每报告期应汇总报送最多一条数据
				StringBuffer sbSearchCondition = new StringBuffer();
				sbSearchCondition.append(" BUOCMONTH = '").append(
						d06.getBuocmonth()).append("' and INSTCODE = '")
						.append(d06.getInstcode()).append(
								"' and BUSINESSID <> '").append(
								d06.getBusinessid()).append(
								"' and EXDEBTCODE = '").append(
								d06.getExdebtcode()).append(
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
				searchModel.setTableId("T_FAL_D06_1");
				searchModel.setSearchCondition(sbSearchCondition.toString());
				long count = service.getCount(searchModel);
				if (count > 0) {
					map.put("EXDEBTCODE", "同一外债编号下，每报告期应汇总报送最多一条数据 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc) {

	}
}
