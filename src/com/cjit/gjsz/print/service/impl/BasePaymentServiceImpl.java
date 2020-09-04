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
import com.cjit.gjsz.logic.model.BasePayment;
import com.cjit.gjsz.logic.model.DeclarePayment;
import com.cjit.gjsz.logic.model.FinancePayment;
import com.cjit.gjsz.print.service.BasePrintService;

/**
 * 对外付款/承兑通知书 涉及到的表操作： 基础信息 t_base_payment 申报信息 t_decl_payment 核销信息
 * t_fini_payment；境外汇款申请书 涉及到的表操作： 管理信息 t_fini_payment
 * @author yulubin
 */
public class BasePaymentServiceImpl extends GenericServiceImpl implements
		BasePrintService{

	private SearchService searchService;
	private String interfaceVer = "";

	public Map generator(String businessid, String tableId, String interfaceVer){
		BasePayment basePayment = null;
		DeclarePayment declarePayment = null;
		FinancePayment financePayment = null;
		this.interfaceVer = interfaceVer;
		if(StringUtil.contains(tableId, "t_fini_payment")
				&& StringUtil.isNotEmpty(this.interfaceVer)){
			tableId = "t_base_payment";
		}
		Map map = new HashMap();
		basePayment = (BasePayment) searchService.getDataVerifyModel(tableId,
				businessid);
		if(basePayment != null){
			declarePayment = (DeclarePayment) searchService.getDataVerifyModel(
					"t_decl_payment", businessid);
			financePayment = (FinancePayment) searchService.getDataVerifyModel(
					"t_fini_payment", businessid);
			setBasePayment(map, basePayment);
			setDeclarePayment(map, declarePayment);
			setFinancePayment(map, financePayment);
		}
		return map;
	}

	private void setBasePayment(Map map, BasePayment basePayment){
		// 审核时间
		map.put("日期", basePayment.getAuditdate() == null ? ""
				: DateUtils.toString(basePayment.getAuditdate(),
						DateUtils.ORA_DATE_FORMAT));
		if(StringUtil.isNotEmpty(basePayment.getRptno())){
			for(int i = 0; i < basePayment.getRptno().length(); i++){
				map.put("申报号码" + i, basePayment.getRptno().charAt(i) + "");
			}
		}
		map.put("付款人类型", StringUtil.cleanString(basePayment.getCustype()));
		map.put("类型", StringUtil.cleanString(basePayment.getCustype()));// 管理信息-境外汇款申请书
		map.put("个人身份证件号码", StringUtil.cleanString(basePayment.getIdcode()));
		map.put("购汇汇率", StringUtil.cleanDouble(basePayment.getExrate()));
		map.put("购汇金额", StringUtil.cleanBigInteger(basePayment.getLcyamt()));
		map.put("购汇帐号银行卡号", StringUtil.cleanString(basePayment.getLcyacc()));
		map.put("购汇金额账号", StringUtil.cleanString(basePayment.getLcyacc()));// 管理信息-境外汇款申请书
		map.put("现汇金额", basePayment.getFcyamt());
		map.put("现汇帐号银行卡号", StringUtil.cleanString(basePayment.getFcyacc()));
		map.put("现汇金额账号", StringUtil.cleanString(basePayment.getFcyacc()));// 管理信息-境外汇款申请书
		map.put("其他金额", basePayment.getOthamt());
		map.put("其它帐号银行卡号", StringUtil.cleanString(basePayment.getOthacc()));
		map.put("其他金额账号", StringUtil.cleanString(basePayment.getOthacc()));// 管理信息-境外汇款申请书
		map.put("实际付款币种及金额", StringUtil.cleanString(basePayment.getActuccy())
				+ " " + StringUtil.cleanBigInteger(basePayment.getActuamt()));
		map.put("汇款币种及金额", StringUtil.cleanString(basePayment.getActuccy())
				+ " " + StringUtil.cleanBigInteger(basePayment.getActuamt()));// 管理信息-境外汇款申请书
		map.put("扣费币种及金额", StringUtil
				.cleanString(basePayment.getOutchargeccy())
				+ " "
				+ StringUtil.cleanBigInteger(basePayment.getOutchargeamt()));
		map.put("信用证/保函编号", StringUtil.cleanString(basePayment.getLcbgno()));
		map.put("开证日期", StringUtil.cleanString(basePayment.getIssdate()));
		map.put("期限", StringUtil.cleanLong(basePayment.getTenor()));
		map.put("银行业务编号", StringUtil.cleanString(basePayment.getBuscode()));
		map.put("付款人名称", StringUtil.cleanString(basePayment.getCustnm()));
		map.put("汇款人名称地址", StringUtil.cleanString(basePayment.getCustnm()));// 管理信息-境外汇款申请书
		if(StringUtil.isNotEmpty(basePayment.getCustcod())){
			for(int i = 0; i < basePayment.getCustcod().length(); i++){
				map.put("组织机构代码" + i, basePayment.getCustcod().charAt(i) + "");
			}
		}
		map.put("结算方式", StringUtil.cleanString(basePayment.getMethod()));
		map.put("付款币种及金额", StringUtil.cleanString(basePayment.getTxccy()) + " "
				+ StringUtil.cleanBigInteger(basePayment.getTxamt()));
		map.put("金额大写", MoneyUtil.toChinese(StringUtil
				.cleanBigInteger(basePayment.getTxamt())));
		map.put("收款人名称", StringUtil.cleanString(basePayment.getOppuser()));
		map.put("收款人名称地址", StringUtil.cleanString(basePayment.getOppuser()));// 管理信息-境外汇款申请书
	}

	private void setDeclarePayment(Map map, DeclarePayment declarePayment){
		if(declarePayment != null && declarePayment.getDatastatus() != null
				&& declarePayment.getDatastatus().intValue() != 0){
			String countryName[] = this.searchService.getKey(
					declarePayment.getCountry()).split(" ");
			if(countryName.length > 0){
				map.put("收款人常驻国家/地区名称", StringUtil.cleanString(countryName[0]));
			}
			String countryBackupNum = this.searchService
					.getBackupNum(declarePayment.getCountry());
			map.put("收款人常驻国家/地区代码", StringUtil.cleanString(countryBackupNum));
			if(countryBackupNum != null
					&& countryBackupNum.trim().length() == 3){
				map.put("收款人常驻国家名称代码1", countryBackupNum.substring(0, 1));// 管理信息-境外汇款申请书
				map.put("收款人常驻国家名称代码2", countryBackupNum.substring(1, 2));// 管理信息-境外汇款申请书
				map.put("收款人常驻国家名称代码3", countryBackupNum.substring(2, 3));// 管理信息-境外汇款申请书
			}
			map
					.put("付款类型", StringUtil.cleanString(declarePayment
							.getPaytype()));
			String txCode1 = StringUtil.cleanString(declarePayment.getTxcode());
			map.put("交易编码1", txCode1);
			if(txCode1 != null && txCode1.trim().length() == 6){
				map.put("交易编码11", txCode1.substring(0, 1));// 管理信息-境外汇款申请书
				map.put("交易编码12", txCode1.substring(1, 2));// 管理信息-境外汇款申请书
				map.put("交易编码13", txCode1.substring(2, 3));// 管理信息-境外汇款申请书
				map.put("交易编码14", txCode1.substring(3, 4));// 管理信息-境外汇款申请书
				map.put("交易编码15", txCode1.substring(4, 5));// 管理信息-境外汇款申请书
				map.put("交易编码16", txCode1.substring(5, 6));// 管理信息-境外汇款申请书
			}
			map.put("相应金额1", StringUtil.cleanBigInteger(declarePayment
					.getTc1amt()));
			map.put("相应金额币种1", StringUtil.cleanBigInteger(declarePayment
					.getTc1amt()));// 管理信息-境外汇款申请书
			map.put("交易附言1", StringUtil.cleanString(declarePayment.getTxrem()));
			String txCode2 = StringUtil
					.cleanString(declarePayment.getTxcode2());
			map.put("交易编码2", txCode2);
			if(txCode2 != null && txCode2.trim().length() == 6){
				map.put("交易编码21", txCode2.substring(0, 1));// 管理信息-境外汇款申请书
				map.put("交易编码22", txCode2.substring(1, 2));// 管理信息-境外汇款申请书
				map.put("交易编码23", txCode2.substring(2, 3));// 管理信息-境外汇款申请书
				map.put("交易编码24", txCode2.substring(3, 4));// 管理信息-境外汇款申请书
				map.put("交易编码25", txCode2.substring(4, 5));// 管理信息-境外汇款申请书
				map.put("交易编码26", txCode2.substring(5, 6));// 管理信息-境外汇款申请书
			}
			map.put("相应金额2", StringUtil.cleanBigInteger(declarePayment
					.getTc2amt()));
			map.put("相应金额币种2", StringUtil.cleanBigInteger(declarePayment
					.getTc2amt()));// 管理信息-境外汇款申请书
			map
					.put("交易附言2", StringUtil.cleanString(declarePayment
							.getTx2rem()));
			if(!"1.2".equals(this.interfaceVer)){
				map.put("是否进口核销项下收汇", StringUtil.cleanString(declarePayment
						.getIsref()));
			}else{
				map.put("是否为保税货物项下付款", StringUtil.cleanString(declarePayment
						.getIsref()));
				map.put("是否保税货物项下付款", StringUtil.cleanString(declarePayment
						.getIsref()));
			}
			map.put("联系人", StringUtil.cleanString(declarePayment.getCrtuser()));
			map.put("联系人电话", StringUtil
					.cleanString(declarePayment.getInptelc()));
			map
					.put("申报日期", StringUtil.cleanString(declarePayment
							.getRptdate()));
			if("1.2".equals(this.interfaceVer)){
				map.put("外汇局批件号/备案表号/业务编号", StringUtil
						.cleanString(declarePayment.getRegno()));
			}
		}
	}

	private void setFinancePayment(Map map, FinancePayment financePayment){
		if(financePayment != null && financePayment.getDatastatus() != null
				&& financePayment.getDatastatus().intValue() != 0){
			map.put("最迟装运日期", StringUtil.cleanString(financePayment
					.getImpdate()));
			map.put("合同号", StringUtil.cleanString(financePayment.getContrno()));
			map.put("发票号", StringUtil.cleanString(financePayment.getInvoino()));
			map.put("提运单号", StringUtil.cleanString(financePayment.getBillno()));
			map.put("合同金额", StringUtil.cleanBigInteger(financePayment
					.getContamt()));
			map.put("外汇局批件/备案表号", StringUtil.cleanString(financePayment
					.getRegno()));
			map.put("填报人", StringUtil.cleanString(financePayment.getCrtuser()));
			map.put("申请人", StringUtil.cleanString(financePayment.getCrtuser()));// 管理信息-境外汇款申请书
			map.put("填报人电话", StringUtil
					.cleanString(financePayment.getInptelc()));
			map.put("申请人电话", StringUtil
					.cleanString(financePayment.getInptelc()));// 管理信息-境外汇款申请书
			map
					.put("申报日期", StringUtil.cleanString(financePayment
							.getRptdate()));
		}
	}

	public void setSearchService(SearchService searchService){
		this.searchService = searchService;
	}
}
