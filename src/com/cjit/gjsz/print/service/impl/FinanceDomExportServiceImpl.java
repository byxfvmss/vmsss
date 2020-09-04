/**
 * 
 */
package com.cjit.gjsz.print.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.model.BaseExport;
import com.cjit.gjsz.logic.model.ExportInfo;
import com.cjit.gjsz.logic.model.FinanceDomExport;
import com.cjit.gjsz.print.service.BasePrintService;

/**
 * 出口收汇核销专用联信息申报表（境内收入） 该表单有两联，银行，申报主体各一联。只要套打印一页即可。 涉及到的表操作： 基础信息 t_base_export
 * 核销信息 t_fini_dom_export 出口收汇核销单号码 t_export_info(全部取出)
 */
public class FinanceDomExportServiceImpl extends GenericServiceImpl implements
		BasePrintService{

	private SearchService searchService;
	private final static String FATHER = "t_base_export";
	private final static String CHILD1 = "t_fini_dom_export";
	private final static String CHILD2 = "t_export_info";

	public Map generator(String businessid, String tableId, String interfaceVer){
		BaseExport baseExport = null;
		FinanceDomExport financeDomExport = null;
		List children = null;
		Map map = new HashMap();
		baseExport = (BaseExport) searchService.getDataVerifyModel(FATHER,
				businessid);
		if(baseExport != null){
			financeDomExport = (FinanceDomExport) searchService
					.getDataVerifyModel(CHILD1, businessid);
			setBaseExport(map, baseExport);
			if(financeDomExport != null
					&& financeDomExport.getDatastatus() != null
					&& financeDomExport.getDatastatus().intValue() != 0){
				children = searchService.getChildren(CHILD2, businessid);
				if(children != null){
					setExportsInfos(map, children);
				}
				setFinanceDomExport(map, financeDomExport, baseExport
						.getTxccy(), baseExport.getTxccy(), children);
			}
		}
		return map;
	}

	private void setBaseExport(Map map, BaseExport baseExport){
		// 审核时间
		map
				.put("日期", baseExport.getAuditdate() == null ? "" : DateUtils
						.toString(baseExport.getAuditdate(),
								DateUtils.ORA_DATE_FORMAT));
		map.put("表单日期", DateUtils.toString(new Date(),
				DateUtils.ORA_DATE_FORMAT));
		map.put("银行业务编号", StringUtil.cleanString(baseExport.getBuscode()));
		map
				.put("国内银行扣费币种及金额", StringUtil.cleanString(baseExport
						.getInchargeccy())
						+ " "
						+ StringUtil.cleanBigInteger(baseExport
								.getInchargeamt()));
		map.put("国外银行扣费币种及金额", StringUtil.cleanString(baseExport
				.getOutchargeccy())
				+ " "
				+ StringUtil.cleanBigInteger(baseExport.getOutchargeamt()));
		map.put("个人身份证件号码", StringUtil.cleanString(baseExport.getIdcode()));
		map.put("收款人名称", StringUtil.cleanString(baseExport.getCustnm()));
		map.put("其它帐号银行卡号", StringUtil.cleanString(baseExport.getOthacc()));
		if(StringUtil.isNotEmpty(baseExport.getCustcod())){
			for(int i = 0; i < baseExport.getCustcod().length(); i++){
				map.put("组织机构代码" + i, baseExport.getCustcod().charAt(i) + "");
			}
		}
		map.put("结算方式", StringUtil.cleanString(baseExport.getMethod()));
		map.put("结汇汇率", StringUtil.cleanDouble(baseExport.getExrate()));
		map.put("收入款币种及金额", StringUtil.cleanString(baseExport.getTxccy()) + " "
				+ StringUtil.cleanBigInteger(baseExport.getTxamt()));
		map.put("结汇金额", StringUtil.cleanBigInteger(baseExport.getLcyamt()));
		map.put("结汇帐号银行卡号", StringUtil.cleanString(baseExport.getLcyacc()));
		map.put("现汇金额", baseExport.getFcyamt());
		map.put("现汇帐号银行卡号", StringUtil.cleanString(baseExport.getFcyacc()));
		if(StringUtil.isNotEmpty(baseExport.getRptno())){
			for(int i = 0; i < baseExport.getRptno().length(); i++){
				map.put("申报号码" + i, baseExport.getRptno().charAt(i) + "");
			}
		}
		map.put("其他金额", baseExport.getOthamt());
		map.put("收款人类型", StringUtil.cleanString(baseExport.getCustype()));
		map.put("付款人名称", StringUtil.cleanString(baseExport.getOppuser()));
	}

	private String getCurr(String txcode, String baseIncomeCurr){
		String curr = "";
		if(!"".equals(StringUtil.cleanString(txcode))){
			curr = StringUtil.cleanString(baseIncomeCurr) + " ";
		}
		return curr;
	}

	private String getCurr(List children, String baseIncomeCurr, String currNum){
		String curr = "";
		if(children != null && children.size() > 0){
			curr = StringUtil.cleanString(baseIncomeCurr) + " " + currNum;
		}
		return curr;
	}

	private void setFinanceDomExport(Map map,
			FinanceDomExport financeDomExport, String baseIncomeCurr,
			String curr, List children){
		if(financeDomExport != null
				&& financeDomExport.getDatastatus().intValue() != 0){
			map.put("数据状态", StringUtil.cleanString(financeDomExport
					.getDatastatus().toString()));
			map.put("境内收汇类型", StringUtil.cleanString(financeDomExport
					.getPayattr()));
			map.put("收款性质", StringUtil.cleanString(financeDomExport
					.getPaytype()));
			map.put("交易编码1", StringUtil.cleanString(financeDomExport
					.getTxcode()));
			map.put("相应金额1", getCurr(StringUtil.cleanString(financeDomExport
					.getTxcode()), baseIncomeCurr)
					+ StringUtil.cleanBigInteger(financeDomExport.getTc1amt()));
			map.put("交易附言1", StringUtil
					.cleanString(financeDomExport.getTxrem()));
			map.put("交易编码2", StringUtil.cleanString(financeDomExport
					.getTxcode2()));
			map.put("相应金额2", getCurr(StringUtil.cleanString(financeDomExport
					.getTxcode2()), baseIncomeCurr)
					+ StringUtil.cleanBigInteger(financeDomExport.getTc2amt()));
			map.put("交易附言2", StringUtil.cleanString(financeDomExport
					.getTx2rem()));
			map.put("是否出口核销项下收汇", StringUtil.cleanString(financeDomExport
					.getIsref()));
			map.put("填报人", StringUtil
					.cleanString(financeDomExport.getCrtuser()));
			map.put("填报人电话", StringUtil.cleanString(financeDomExport
					.getInptelc()));
			map.put("申报日期", StringUtil.cleanString(financeDomExport
					.getRptdate()));
			map.put("收汇总金额中用于出口核销的金额", getCurr(children, curr, StringUtil
					.cleanBigInteger(financeDomExport.getChkamt())));
		}
	}

	private void setExportsInfos(Map map, List children){
		if(CollectionUtil.isNotEmpty(children)){
			StringBuffer numbers = new StringBuffer();
			for(int i = 0; i < children.size(); i++){
				ExportInfo exportInfo = (ExportInfo) children.get(i);
				if((i + 1) % 8 == 0){
					numbers.append(exportInfo.getRefno()).append(";<br>");
				}else{
					numbers.append(exportInfo.getRefno()).append(";&nbsp;");
				}
				// numbers.append(exportInfo.getRefno()).append("<br>");
			}
			map.put("出口收汇核销单号码", numbers.toString());
		}
	}

	public void setSearchService(SearchService searchService){
		this.searchService = searchService;
	}
}
