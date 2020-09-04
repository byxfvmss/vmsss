/**
 * 
 */
package com.cjit.gjsz.print.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.MoneyUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.model.BaseDomPayment;
import com.cjit.gjsz.logic.model.FinanceDomPayment;
import com.cjit.gjsz.print.service.BasePrintService;

/**
 * @author yulubin 境内付款/承兑通知书 涉及到的表操作： 基础信息 t_base_dom_pay 核销信息 t_fini_dom_pay
 */
public class BaseDomPaymentServiceImpl extends GenericServiceImpl implements
		BasePrintService{

	private SearchService searchService;
	private String interfaceVer = "";

	public Map generator(String businessid, String tableId, String interfaceVer){
		BaseDomPayment baseDomPayment = null;
		FinanceDomPayment financeDomPayment = null;
		this.interfaceVer = interfaceVer;
		Map map = new HashMap();
		baseDomPayment = (BaseDomPayment) searchService.getDataVerifyModel(
				tableId, businessid);
		if(baseDomPayment != null){
			financeDomPayment = (FinanceDomPayment) searchService
					.getDataVerifyModel("t_fini_dom_pay", businessid);
			setBaseDomPayment(map, baseDomPayment);
			setFinanceDomPayment(map, financeDomPayment, baseDomPayment
					.getTxccy());
		}
		return map;
	}

	private void setBaseDomPayment(Map map, BaseDomPayment baseDomPayment){
		// 审核时间
		map.put("日期", baseDomPayment.getAuditdate() == null ? "" : DateUtils
				.toString(baseDomPayment.getAuditdate(),
						DateUtils.ORA_DATE_FORMAT));
		if(StringUtil.isNotEmpty(baseDomPayment.getRptno())){
			for(int i = 0; i < baseDomPayment.getRptno().length(); i++){
				map.put("申报号码" + i, baseDomPayment.getRptno().charAt(i) + "");
			}
		}
		map.put("付款人类型", StringUtil.cleanString(baseDomPayment.getCustype()));
		map.put("个人身份证件号码", StringUtil.cleanString(baseDomPayment.getIdcode()));
		if(StringUtil.isNotEmpty(baseDomPayment.getCustcod())){
			for(int i = 0; i < baseDomPayment.getCustcod().length(); i++){
				map.put("组织机构代码" + i, baseDomPayment.getCustcod().charAt(i)
						+ "");
			}
		}
		map.put("付款人名称", StringUtil.cleanString(baseDomPayment.getCustnm()));
		map.put("收款人名称", StringUtil.cleanString(baseDomPayment.getOppuser()));
		map.put("付款币种及金额", StringUtil.cleanString(baseDomPayment.getTxccy())
				+ " " + StringUtil.cleanBigInteger(baseDomPayment.getTxamt()));
		map.put("金额大写", MoneyUtil.toChinese(StringUtil
				.cleanBigInteger(baseDomPayment.getTxamt())));
		map.put("购汇汇率", StringUtil.cleanDouble(baseDomPayment.getExrate()));
		map.put("购汇金额", StringUtil.cleanBigInteger(baseDomPayment.getLcyamt()));
		map.put("购汇帐号银行卡号", StringUtil.cleanString(baseDomPayment.getLcyacc()));
		map.put("现汇金额", baseDomPayment.getFcyamt());
		map.put("现汇帐号银行卡号", StringUtil.cleanString(baseDomPayment.getFcyacc()));
		map.put("其他金额", baseDomPayment.getOthamt());
		map.put("其它帐号银行卡号", StringUtil.cleanString(baseDomPayment.getOthacc()));
		map.put("结算方式", StringUtil.cleanString(baseDomPayment.getMethod()));
		map.put("银行业务编号", StringUtil.cleanString(baseDomPayment.getBuscode()));
		map
				.put("实际付款币种及金额", StringUtil.cleanString(baseDomPayment
						.getActuccy())
						+ " "
						+ StringUtil.cleanBigInteger(baseDomPayment
								.getActuamt()));
		map.put("扣费币种及金额", StringUtil.cleanString(baseDomPayment
				.getOutchargeccy())
				+ " "
				+ StringUtil.cleanBigInteger(baseDomPayment.getOutchargeamt()));
		map.put("信用证/保函编号", StringUtil.cleanString(baseDomPayment.getLcbgno()));
		map.put("开证日期", StringUtil.cleanString(baseDomPayment.getIssdate()));
		map.put("期限", StringUtil.cleanLong(baseDomPayment.getTenor()));
	}

	private String getCurr(String txcode, String baseIncomeCurr){
		String curr = "";
		if(!"".equals(StringUtil.cleanString(txcode))){
			curr = StringUtil.cleanString(baseIncomeCurr) + " ";
		}
		return curr;
	}

	private void setFinanceDomPayment(Map map,
			FinanceDomPayment financeDomPayment, String baseIncomeCurr){
		if(financeDomPayment != null
				&& financeDomPayment.getDatastatus() != null
				&& financeDomPayment.getDatastatus().intValue() != 0){
			if(!"1.2".equals(this.interfaceVer)){
				map.put("是否进口核销项下付款", StringUtil.cleanString(financeDomPayment
						.getIsref()));
			}else{
				map.put("是否为保税货物项下付款", StringUtil.cleanString(financeDomPayment
						.getIsref()));
			}
			String countryName[] = this.searchService.getKey(
					financeDomPayment.getCountry()).split(" ");
			if(countryName.length > 0){
				map.put("收款人常驻国家/地区名称", StringUtil.cleanString(countryName[0]));
			}
			map.put("收款人常驻国家/地区代码", StringUtil.cleanString(this.searchService
					.getBackupNum(financeDomPayment.getCountry())));
			map.put("付款类型", StringUtil.cleanString(financeDomPayment
					.getPaytype()));
			map.put("付汇性质", StringUtil.cleanString(financeDomPayment
					.getPayattr()));
			map.put("交易编码1", StringUtil.cleanString(financeDomPayment
					.getTxcode()));
			map
					.put("相应金额1", getCurr(StringUtil
							.cleanString(financeDomPayment.getTxcode()),
							baseIncomeCurr)
							+ StringUtil.cleanBigInteger(financeDomPayment
									.getTc1amt()));
			map.put("交易编码2", StringUtil.cleanString(financeDomPayment
					.getTxcode2()));
			map
					.put("相应金额2", getCurr(StringUtil
							.cleanString(financeDomPayment.getTxcode2()),
							baseIncomeCurr)
							+ StringUtil.cleanBigInteger(financeDomPayment
									.getTc2amt()));
			map.put("最迟装运日期", StringUtil.cleanString(financeDomPayment
					.getImpdate()));
			map.put("合同号", StringUtil.cleanString(financeDomPayment
					.getContrno()));
			map.put("发票号", StringUtil.cleanString(financeDomPayment
					.getInvoino()));
			map.put("提运单号", StringUtil.cleanString(financeDomPayment
					.getBillno()));
			map.put("合同金额", StringUtil.cleanBigInteger(financeDomPayment
					.getContamt()));
			map.put("外汇局批件/备案表号", StringUtil.cleanString(financeDomPayment
					.getRegno()));
			map.put("填报人", StringUtil.cleanString(financeDomPayment
					.getCrtuser()));
			map.put("填报人电话", StringUtil.cleanString(financeDomPayment
					.getInptelc()));
			map.put("申报日期", StringUtil.cleanString(financeDomPayment
					.getRptdate()));
		}
	}

	public void setSearchService(SearchService searchService){
		this.searchService = searchService;
	}
}
