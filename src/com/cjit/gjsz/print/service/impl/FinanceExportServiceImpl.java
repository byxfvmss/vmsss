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
import com.cjit.gjsz.logic.model.BaseIncome;
import com.cjit.gjsz.logic.model.DeclareIncome;
import com.cjit.gjsz.logic.model.ExportInfo;
import com.cjit.gjsz.logic.model.FinanceExport;
import com.cjit.gjsz.print.service.BasePrintService;

/**
 * @author yulubin 出口收汇核销专用联信息申报表（境外收入）+出口收汇核销专用联（境外收入） 涉及到的表操作： 基础信息
 *         t_base_income 申报信息 t_decl_income 核销信息 t_fini_export 出口收汇核销单号码
 *         t_export_info(全部取出)
 */
public class FinanceExportServiceImpl extends GenericServiceImpl implements
		BasePrintService{

	private SearchService searchService;
	private final static String FATHER = "t_base_income";
	private final static String CHILD1 = "t_decl_income";
	private final static String CHILD2 = "t_fini_export";
	private final static String CHILD3 = "t_export_info";

	public Map generator(String businessid, String tableId, String interfaceVer){
		BaseIncome baseIncome = null;
		DeclareIncome declareIncome = null;
		FinanceExport financeExport = null;
		List children = null;
		Map map = new HashMap();
		baseIncome = (BaseIncome) searchService.getDataVerifyModel(FATHER,
				businessid);
		if(baseIncome != null){
			declareIncome = (DeclareIncome) searchService.getDataVerifyModel(
					CHILD1, businessid);
			financeExport = (FinanceExport) searchService.getDataVerifyModel(
					CHILD2, businessid);
			if(financeExport != null && financeExport.getDatastatus() != null
					&& financeExport.getDatastatus().intValue() != 0){
				children = searchService.getChildren(CHILD3, businessid);
			}
			setBaseIncome(map, baseIncome);
			setDeclareIncome(map, declareIncome, baseIncome.getTxccy());
			setFinanceExport(map, financeExport, baseIncome.getTxccy(),
					children);
			setExportsInfos(map, children);
		}
		return map;
	}

	private void setBaseIncome(Map map, BaseIncome baseIncome){
		// 审核时间
		map
				.put("日期", baseIncome.getAuditdate() == null ? "" : DateUtils
						.toString(baseIncome.getAuditdate(),
								DateUtils.ORA_DATE_FORMAT));
		map.put("表单日期", DateUtils.toString(new Date(),
				DateUtils.ORA_DATE_FORMAT));
		map.put("银行业务编号", StringUtil.cleanString(baseIncome.getBuscode()));
		map
				.put("国内银行扣费币种及金额", StringUtil.cleanString(baseIncome
						.getInchargeccy())
						+ " "
						+ StringUtil.cleanBigInteger(baseIncome
								.getInchargeamt()));
		map.put("个人身份证件号码", StringUtil.cleanString(baseIncome.getIdcode()));
		map.put("收款人名称", StringUtil.cleanString(baseIncome.getCustnm()));
		map.put("国外银行扣费币种及金额", StringUtil.cleanString(baseIncome
				.getOutchargeccy())
				+ " "
				+ StringUtil.cleanBigInteger(baseIncome.getOutchargeamt()));
		map.put("其它帐号银行卡号", StringUtil.cleanString(baseIncome.getOthacc()));
		if(StringUtil.isNotEmpty(baseIncome.getCustcod())){
			for(int i = 0; i < baseIncome.getCustcod().length(); i++){
				map.put("组织机构代码" + i, baseIncome.getCustcod().charAt(i) + "");
			}
		}
		map.put("结算方式", StringUtil.cleanString(baseIncome.getMethod()));
		map.put("结汇汇率", StringUtil.cleanDouble(baseIncome.getExrate()));
		map.put("收入款币种及金额", StringUtil.cleanString(baseIncome.getTxccy()) + " "
				+ StringUtil.cleanBigInteger(baseIncome.getTxamt()));
		map.put("结汇金额", StringUtil.cleanBigInteger(baseIncome.getLcyamt()));
		map.put("结汇帐号银行卡号", StringUtil.cleanString(baseIncome.getLcyacc()));
		map.put("现汇金额", baseIncome.getFcyamt());
		map.put("现汇帐号银行卡号", StringUtil.cleanString(baseIncome.getFcyacc()));
		if(StringUtil.isNotEmpty(baseIncome.getRptno())){
			for(int i = 0; i < baseIncome.getRptno().length(); i++){
				map.put("申报号码" + i, baseIncome.getRptno().charAt(i) + "");
			}
		}
		map.put("其他金额", baseIncome.getOthamt());
		map.put("收款人类型", StringUtil.cleanString(baseIncome.getCustype()));
		map.put("付款人名称", StringUtil.cleanString(baseIncome.getOppuser()));
	}

	/**
	 * @author zhangxin
	 * @since 2009-04-27
	 * @param 交易编码
	 * @param 币种
	 * @return
	 */
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

	private void setDeclareIncome(Map map, DeclareIncome declareIncome,
			String baseIncomeCurr){
		if(declareIncome != null && declareIncome.getDatastatus() != null
				&& declareIncome.getDatastatus().intValue() != 0){
			String countryName[] = this.searchService.getKey(
					declareIncome.getCountry()).split(" ");
			if(countryName.length > 0){
				map.put("付款人常驻国家/地区名称", StringUtil.cleanString(countryName[0]));
			}
			map.put("付款人常驻国家/地区代码", StringUtil.cleanString(this.searchService
					.getBackupNum(declareIncome.getCountry())));
			map.put("收款性质", StringUtil.cleanString(declareIncome.getPaytype()));
			map.put("交易编码1", StringUtil.cleanString(declareIncome.getTxcode()));
			map.put("相应金额1", getCurr(StringUtil.cleanString(declareIncome
					.getTxcode()), baseIncomeCurr)
					+ StringUtil.cleanBigInteger(declareIncome.getTc1amt()));
			map.put("交易附言1", StringUtil.cleanString(declareIncome.getTxrem()));
			map
					.put("交易编码2", StringUtil.cleanString(declareIncome
							.getTxcode2()));
			map.put("相应金额2", getCurr(StringUtil.cleanString(declareIncome
					.getTxcode2()), baseIncomeCurr)
					+ StringUtil.cleanBigInteger(declareIncome.getTc2amt()));
			map.put("交易附言2", StringUtil.cleanString(declareIncome.getTx2rem()));
			map.put("是否出口核销项下收汇", StringUtil.cleanString(declareIncome
					.getIsref()));
			map.put("外债编号", StringUtil.cleanString(declareIncome.getBillno()));
			map.put("填报人", StringUtil.cleanString(declareIncome.getCrtuser()));
			map
					.put("填报人电话", StringUtil.cleanString(declareIncome
							.getInptelc()));
			map.put("申报日期", StringUtil.cleanString(declareIncome.getRptdate()));
		}
	}

	private void setFinanceExport(Map map, FinanceExport financeExport,
			String curr, List children){
		if(financeExport != null && financeExport.getDatastatus() != null
				&& financeExport.getDatastatus().intValue() != 0){
			map.put("数据状态", StringUtil.cleanString(financeExport
					.getDatastatus().toString()));
			map.put("收汇类型", StringUtil.cleanString(financeExport.getPayattr()));
			map.put("已出具出口收汇核销专用联", StringUtil.cleanString(financeExport
					.getChkprtd()));
			map.put("余款金额", StringUtil
					.cleanBigInteger(financeExport.getOsamt()));
			map.put("收汇总金额中用于出口核销的金额", getCurr(children, curr, StringUtil
					.cleanBigInteger(financeExport.getChkamt())));
			map.put("填报人", StringUtil.cleanString(financeExport.getCrtuser()));
			map
					.put("填报人电话", StringUtil.cleanString(financeExport
							.getInptelc()));
			map.put("申报日期", StringUtil.cleanString(financeExport.getRptdate()));
		}
	}

	private void setExportsInfos(Map map, List children){
		if(CollectionUtil.isNotEmpty(children)){
			StringBuffer numbers = new StringBuffer();
			for(int i = 0; i < children.size(); i++){
				ExportInfo exportInfo = (ExportInfo) children.get(i);
				if((i + 1) % 5 == 0){
					numbers.append(exportInfo.getRefno()).append(";<br>");
				}else{
					numbers.append(exportInfo.getRefno()).append(";");
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
