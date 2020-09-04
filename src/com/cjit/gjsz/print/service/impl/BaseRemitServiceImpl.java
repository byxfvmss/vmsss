package com.cjit.gjsz.print.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.MoneyUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.model.BaseRemit;
import com.cjit.gjsz.logic.model.CustomDeclare;
import com.cjit.gjsz.logic.model.DeclareRemit;
import com.cjit.gjsz.logic.model.FinanceRemit;
import com.cjit.gjsz.print.service.BasePrintService;

public class BaseRemitServiceImpl extends GenericServiceImpl implements
		BasePrintService{

	private SearchService searchService;
	private String interfaceVer = "";

	public Map generator(String businessid, String tableId, String interfaceVer){
		BaseRemit baseRemit = null;
		DeclareRemit declareRemit = null;
		FinanceRemit financeRemit = null;
		this.interfaceVer = interfaceVer;
		List children = null;
		Map map = new HashMap();
		baseRemit = (BaseRemit) searchService.getDataVerifyModel(tableId,
				businessid);
		if(baseRemit != null){
			setBaseRemit(map, baseRemit);
			declareRemit = (DeclareRemit) searchService.getDataVerifyModel(
					"t_decl_remit", businessid);
			if(declareRemit != null){
				setDeclareRemit(map, declareRemit, baseRemit.getTxccy());
			}
			financeRemit = (FinanceRemit) searchService.getDataVerifyModel(
					"t_fini_remit", businessid);
			if(financeRemit != null){
				setFinanceRemit(map, financeRemit);
				children = searchService.getChildren("t_customs_decl",
						businessid);
				if(children != null){
					setCustoms(map, children);
				}
			}
		}
		return map;
	}

	private void setBaseRemit(Map map, BaseRemit baseRemit){
		// 审核时间
		map.put("日期", baseRemit.getAuditdate() == null ? "" : DateUtils
				.toString(baseRemit.getAuditdate(), DateUtils.ORA_DATE_FORMAT));
		// METHOD 结算方式 字符型，1 T－电汇 D－票汇 M－信汇
		map.put("结算方式", StringUtil.cleanString(baseRemit.getMethod()));
		if(StringUtil.isNotEmpty(baseRemit.getRptno())){
			for(int i = 0; i < baseRemit.getRptno().length(); i++){
				map.put("申报号码" + i, baseRemit.getRptno().charAt(i) + "");
			}
		}
		map.put("银行业务编号", StringUtil.cleanString(baseRemit.getBuscode()));
		map.put("汇款币种及金额", StringUtil.cleanString(baseRemit.getTxccy()) + " "
				+ StringUtil.cleanBigInteger(baseRemit.getTxamt()));
		map.put("金额大写", MoneyUtil.toChinese(StringUtil
				.cleanBigInteger(baseRemit.getTxamt())));
		map.put("现汇金额", baseRemit.getFcyamt());
		// 现汇金额对应的账号——外汇帐号/银行卡号
		map.put("现汇金额账号", StringUtil.cleanString(baseRemit.getFcyacc()));
		map.put("购汇金额", baseRemit.getLcyamt());
		// 购汇金额对应的账号——人民币帐号/银行卡号
		map.put("购汇金额账号", StringUtil.cleanString(baseRemit.getLcyacc()));
		map.put("其他金额", baseRemit.getOthamt());
		// 其他金额对应的账号——其它帐号/银行卡号
		map.put("其他金额账号", StringUtil.cleanString(baseRemit.getOthacc()));
		// 目前只有名称
		map.put("汇款人名称地址", StringUtil.cleanString(baseRemit.getCustnm()));
		// 对公或对私。
		map.put("类型", StringUtil.cleanString(baseRemit.getCustype()));
		map.put("购汇汇率", StringUtil.cleanDouble(baseRemit.getExrate()));
		if(StringUtil.isNotEmpty(baseRemit.getCustcod())){
			for(int i = 0; i < baseRemit.getCustcod().length(); i++){
				map.put("组织机构代码" + i, baseRemit.getCustcod().charAt(i) + "");
			}
		}
		// TODO: 美工少加了
		map.put("个人身份证件号码", StringUtil.cleanString(baseRemit.getIdcode()));
		// 没有地址
		map.put("收款人名称地址", StringUtil.cleanString(baseRemit.getOppuser()));
	}

	private String getCurr(String txcode, String baseIncomeCurr){
		String curr = "";
		if(!"".equals(StringUtil.cleanString(txcode))){
			curr = StringUtil.cleanString(baseIncomeCurr) + " ";
		}
		return curr;
	}

	private void setDeclareRemit(Map map, DeclareRemit declareRemit,
			String baseIncomeCurr){
		if(declareRemit != null && declareRemit.getDatastatus() != null
				&& declareRemit.getDatastatus().intValue() != 0){
			String countryKey = this.searchService.getKey(declareRemit
					.getCountry());
			String countryName[] = null;
			if(countryKey != null){
				countryName = countryKey.split(" ");
			}
			if(countryName != null && countryName.length > 0){
				map.put("收款人常驻国家/地区名称", StringUtil.cleanString(countryName[0]));
			}else{
				map.put("收款人常驻国家/地区名称", "");
			}
			String countryBackupNum = StringUtil.cleanString(this.searchService
					.getBackupNum(declareRemit.getCountry()));
			if(!"1.2".equals(this.interfaceVer)){
				map.put("收款人常驻国家名称代码", countryBackupNum);
			}else{
				if(countryBackupNum != null && countryBackupNum.length() == 3){
					map.put("收款人常驻国家名称代码1", countryBackupNum.substring(0, 1));
					map.put("收款人常驻国家名称代码2", countryBackupNum.substring(1, 2));
					map.put("收款人常驻国家名称代码3", countryBackupNum.substring(2, 3));
				}
			}
			map.put("付款类型", StringUtil.cleanString(declareRemit.getPaytype()));
			String txcode = StringUtil.cleanString(declareRemit.getTxcode());
			if(!"1.2".equals(this.interfaceVer)){
				map.put("交易编码1", txcode);
			}else{
				if(txcode != null && txcode.length() == 6){
					map.put("交易编码11", txcode.substring(0, 1));
					map.put("交易编码12", txcode.substring(1, 2));
					map.put("交易编码13", txcode.substring(2, 3));
					map.put("交易编码14", txcode.substring(3, 4));
					map.put("交易编码15", txcode.substring(4, 5));
					map.put("交易编码16", txcode.substring(5, 6));
				}
			}
			String txcode2 = StringUtil.cleanString(declareRemit.getTxcode2());
			if(!"1.2".equals(this.interfaceVer)){
				map.put("交易编码2", txcode2);
			}else{
				if(txcode2 != null && txcode2.length() == 6){
					map.put("交易编码21", txcode2.substring(0, 1));
					map.put("交易编码22", txcode2.substring(1, 2));
					map.put("交易编码23", txcode2.substring(2, 3));
					map.put("交易编码24", txcode2.substring(3, 4));
					map.put("交易编码25", txcode2.substring(4, 5));
					map.put("交易编码26", txcode2.substring(5, 6));
				}
			}
			map.put("相应金额币种1", getCurr(StringUtil.cleanString(declareRemit
					.getTxcode()), baseIncomeCurr)
					+ StringUtil.cleanBigInteger(declareRemit.getTc1amt()));
			map.put("相应金额币种2", getCurr(StringUtil.cleanString(declareRemit
					.getTxcode2()), baseIncomeCurr)
					+ StringUtil.cleanBigInteger(declareRemit.getTc2amt()));
			map.put("交易附言1", StringUtil.cleanString(declareRemit.getTxrem()));
			map.put("交易附言2", StringUtil.cleanString(declareRemit.getTx2rem()));
			if(!"1.2".equals(this.interfaceVer)){
				map.put("是否进口核销项下付款", StringUtil.cleanString(declareRemit
						.getIsref()));
			}else{
				map.put("是否保税货物项下付款", StringUtil.cleanString(declareRemit
						.getIsref()));
			}
			map.put("申请人", StringUtil.cleanString(declareRemit.getCrtuser()));
			map.put("申请人电话", StringUtil.cleanString(declareRemit.getInptelc()));
			if("1.2".equals(this.interfaceVer)){
				map.put("外汇局批件号/备案表号/业务编号", StringUtil.cleanString(declareRemit
						.getRegno()));
			}
		}
	}

	private void setFinanceRemit(Map map, FinanceRemit financeRemit){
		if(financeRemit != null && financeRemit.getDatastatus() != null
				&& financeRemit.getDatastatus().intValue() != 0){
			map
					.put("最迟装运日期", StringUtil.cleanString(financeRemit
							.getImpdate()));
			map.put("合同号", StringUtil.cleanString(financeRemit.getContrno()));
			map.put("发票号", StringUtil.cleanString(financeRemit.getInvoino()));
			map.put("外汇局批件备案表号", StringUtil
					.cleanString(financeRemit.getRegno()));
			map.put("报关单经营单位代码", StringUtil.cleanString(financeRemit
					.getCusmno()));
		}
	}

	private void setCustoms(Map map, List children){
		if(CollectionUtil.isNotEmpty(children)){
			CustomDeclare customDeclare = null;
			// DFHL:处理多于两个报关单号开始
			for(int i = 0; i < children.size(); i++){
				customDeclare = (CustomDeclare) children.get(i);
				if(customDeclare != null){
					String num = String.valueOf(i + 1);
					map.put("报关单号" + num, StringUtil.cleanString(customDeclare
							.getCustomn()));
					map.put("报关单币种金额" + num, StringUtil
							.cleanString(customDeclare.getCustccy())
							+ " "
							+ StringUtil.cleanBigInteger(customDeclare
									.getCustamt()));
					map.put("本次核注金额" + num, StringUtil
							.cleanBigInteger(customDeclare.getOffamt()));
				}
			}
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < children.size(); i++){
				customDeclare = (CustomDeclare) children.get(i);
				if(customDeclare != null){
					String num = String.valueOf(i + 1);
					sb.append("单号"
							+ num
							+ ":"
							+ StringUtil
									.cleanString(customDeclare.getCustomn()));
					sb.append("&nbsp;币种金额"
							+ num
							+ ":"
							+ StringUtil
									.cleanString(customDeclare.getCustccy())
							+ " "
							+ StringUtil.cleanBigInteger(customDeclare
									.getCustamt()));
					sb.append("&nbsp;核注金额"
							+ num
							+ ":"
							+ StringUtil.cleanBigInteger(customDeclare
									.getOffamt()));
					if(i % 2 != 0)
						sb.append("<br>");
					else
						sb.append("&nbsp;&nbsp;");
				}
			}
			map.put("报关单号信息", sb.toString());
			// if (children.size() > 0) {
			// customDeclare = (CustomDeclare) children.get(0);
			// if (customDeclare != null) {
			// map.put("报关单号1",
			// StringUtil.cleanString(customDeclare.getCustomn()));
			// map.put("报关单币种金额1",
			// StringUtil.cleanString(customDeclare.getCustccy())+"
			// "+StringUtil.cleanBigInteger(customDeclare.getCustamt()));
			// map.put("本次核注金额1",
			// StringUtil.cleanBigInteger(customDeclare.getOffamt()));
			// }
			// }
			// if (children.size() > 1) {
			// customDeclare = (CustomDeclare) children.get(1);
			// if (customDeclare != null) {
			// map.put("报关单号2",
			// StringUtil.cleanString(customDeclare.getCustomn()));
			// map.put("报关单币种金额2",
			// StringUtil.cleanString(customDeclare.getCustccy())+"
			// "+StringUtil.cleanBigInteger(customDeclare.getCustamt()));
			// map.put("本次核注金额2",
			// StringUtil.cleanBigInteger(customDeclare.getOffamt()));
			// }
			// }
			// DFHL:处理多于两个报关单号开始
		}
	}

	public void setSearchService(SearchService searchService){
		this.searchService = searchService;
	}
}
