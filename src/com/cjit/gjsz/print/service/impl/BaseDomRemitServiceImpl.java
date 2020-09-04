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
import com.cjit.gjsz.logic.model.BaseDomRemit;
import com.cjit.gjsz.logic.model.CustomDeclare;
import com.cjit.gjsz.logic.model.FinanceDomRemit;
import com.cjit.gjsz.print.service.BasePrintService;

public class BaseDomRemitServiceImpl extends GenericServiceImpl implements
		BasePrintService{

	private SearchService searchService;
	private final static String CHILD2 = "t_customs_decl";
	private String interfaceVer = "";

	public Map generator(String businessid, String tableId, String interfaceVer){
		BaseDomRemit baseDomRemit = null;
		FinanceDomRemit financeDomRemit = null;
		this.interfaceVer = interfaceVer;
		List children = null;
		Map map = new HashMap();
		baseDomRemit = (BaseDomRemit) searchService.getDataVerifyModel(tableId,
				businessid);
		if(baseDomRemit != null){
			financeDomRemit = (FinanceDomRemit) searchService
					.getDataVerifyModel("t_fini_dom_remit", businessid);
			if(financeDomRemit != null
					&& financeDomRemit.getDatastatus() != null
					&& financeDomRemit.getDatastatus().intValue() != 0){
				children = searchService.getChildren(CHILD2, businessid);
				if(children != null){
					setCustoms(map, children);
				}
				setFinanceDomRemit(map, financeDomRemit, baseDomRemit
						.getTxccy());
			}
			setBaseDomRemit(map, baseDomRemit);
		}
		return map;
	}

	private void setBaseDomRemit(Map map, BaseDomRemit baseDomRemit){
		// 审核时间
		map.put("日期", baseDomRemit.getAuditdate() == null ? "" : DateUtils
				.toString(baseDomRemit.getAuditdate(),
						DateUtils.ORA_DATE_FORMAT));
		// METHOD 结算方式 字符型，1 T－电汇 D－票汇 M－信汇
		map.put("结算方式", StringUtil.cleanString(baseDomRemit.getMethod()));
		if(StringUtil.isNotEmpty(baseDomRemit.getRptno())){
			for(int i = 0; i < baseDomRemit.getRptno().length(); i++){
				map.put("申报号码" + i, baseDomRemit.getRptno().charAt(i) + "");
			}
		}
		map.put("购汇汇率", StringUtil.cleanDouble(baseDomRemit.getExrate()));
		map.put("银行业务编号", StringUtil.cleanString(baseDomRemit.getBuscode()));
		map.put("汇款币种及金额", StringUtil.cleanString(baseDomRemit.getTxccy())
				+ " " + StringUtil.cleanBigInteger(baseDomRemit.getTxamt()));
		map.put("金额大写", MoneyUtil.toChinese(StringUtil
				.cleanBigInteger(baseDomRemit.getTxamt())));
		map.put("现汇金额", baseDomRemit.getFcyamt());
		// 现汇金额对应的账号——外汇帐号/银行卡号
		map.put("现汇金额账号", StringUtil.cleanString(baseDomRemit.getFcyacc()));
		map.put("购汇金额", baseDomRemit.getLcyamt());
		// 购汇金额对应的账号——人民币帐号/银行卡号
		map.put("购汇金额账号", StringUtil.cleanString(baseDomRemit.getLcyacc()));
		map.put("其他金额", baseDomRemit.getOthamt());
		// 其他金额对应的账号——其它帐号/银行卡号
		map.put("其他金额账号", StringUtil.cleanString(baseDomRemit.getOthacc()));
		// 目前只有名称
		map.put("汇款人名称地址", StringUtil.cleanString(baseDomRemit.getCustnm()));
		// 对公或对私。
		map.put("类型", StringUtil.cleanString(baseDomRemit.getCustype()));
		if(StringUtil.isNotEmpty(baseDomRemit.getCustcod())){
			for(int i = 0; i < baseDomRemit.getCustcod().length(); i++){
				map.put("组织机构代码" + i, baseDomRemit.getCustcod().charAt(i) + "");
			}
		}
		// TODO: 美工少加了
		map.put("个人身份证件号码", StringUtil.cleanString(baseDomRemit.getIdcode()));
		// 收款人帐号
		map.put("收款人帐号", StringUtil.cleanString(baseDomRemit.getOppacc()));
		// 没有地址
		map.put("收款人名称地址", StringUtil.cleanString(baseDomRemit.getOppuser()));
	}

	private String getCurr(String txcode, String baseIncomeCurr){
		String curr = "";
		if(!"".equals(StringUtil.cleanString(txcode))){
			curr = StringUtil.cleanString(baseIncomeCurr) + " ";
		}
		return curr;
	}

	private void setFinanceDomRemit(Map map, FinanceDomRemit financeDomRemit,
			String baseIncomeCurr){
		if(financeDomRemit != null && financeDomRemit.getDatastatus() != null
				&& financeDomRemit.getDatastatus().intValue() != 0){
			if(financeDomRemit.getCountry() != null){
				String countryName[] = this.searchService.getKey(
						financeDomRemit.getCountry()).split(" ");
				if(countryName.length > 0){
					map
							.put("收款人常驻国家名称", StringUtil
									.cleanString(countryName[0]));
				}
				map.put("收款人常驻国家代码", StringUtil.cleanString(this.searchService
						.getBackupNum(financeDomRemit.getCountry())));
			}
			map.put("付款类型", StringUtil
					.cleanString(financeDomRemit.getPaytype()));
			map.put("付汇性质", StringUtil
					.cleanString(financeDomRemit.getPayattr()));
			map.put("交易编码1", StringUtil
					.cleanString(financeDomRemit.getTxcode()));
			map.put("交易编码2", StringUtil.cleanString(financeDomRemit
					.getTxcode2()));
			map.put("相应币种及金额1", getCurr(StringUtil.cleanString(financeDomRemit
					.getTxcode()), baseIncomeCurr)
					+ StringUtil.cleanBigInteger(financeDomRemit.getTc1amt()));
			map.put("相应币种及金额2", getCurr(StringUtil.cleanString(financeDomRemit
					.getTxcode2()), baseIncomeCurr)
					+ StringUtil.cleanBigInteger(financeDomRemit.getTc2amt()));
			map
					.put("交易附言1", StringUtil.cleanString(financeDomRemit
							.getTxrem()));
			map.put("交易附言2", StringUtil
					.cleanString(financeDomRemit.getTx2rem()));
			if(!"1.2".equals(this.interfaceVer)){
				map.put("是否进口核销项下付款", StringUtil.cleanString(financeDomRemit
						.getIsref()));
			}else{
				map.put("是否为保税货物项下付款", StringUtil.cleanString(financeDomRemit
						.getIsref()));
			}
			map
					.put("申请人", StringUtil.cleanString(financeDomRemit
							.getCrtuser()));
			map.put("申请人电话", StringUtil.cleanString(financeDomRemit
					.getInptelc()));
			map
					.put("合同号", StringUtil.cleanString(financeDomRemit
							.getContrno()));
			map
					.put("发票号", StringUtil.cleanString(financeDomRemit
							.getInvoino()));
			map.put("最迟装运日期", StringUtil.cleanString(financeDomRemit
					.getImpdate()));
			map.put("外汇局批件备案表号", StringUtil.cleanString(financeDomRemit
					.getRegno()));
			map.put("报关单经营单位代码", StringUtil.cleanString(financeDomRemit
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
			//            
			//			
			//			
			// if (children.size() > 0) {
			// customDeclare = (CustomDeclare) children.get(0);
			// if (customDeclare != null) {
			// map.put("报关单号1",
			// StringUtil.cleanString(customDeclare.getCustomn()));
			// map.put("报关单币种金额1",StringUtil.cleanString(customDeclare.getCustccy())+"
			// "+ StringUtil.cleanBigInteger(customDeclare.getCustamt()));
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
			// "+StringUtil.cleanBigInteger(customDeclare.getCustamt())
			// );
			// map.put("本次核注金额2",
			// StringUtil.cleanBigInteger(customDeclare.getOffamt()));
			// }
			// }
			//
			// if (children.size() > 2) {
			// customDeclare = (CustomDeclare) children.get(2);
			// if (customDeclare != null) {
			// map.put("报关单号3",
			// StringUtil.cleanString(customDeclare.getCustomn()));
			// map.put("报关单币种金额3",
			// StringUtil.cleanString(customDeclare.getCustccy())+" "+
			// StringUtil.cleanBigInteger(customDeclare.getCustamt())
			// );
			// map.put("本次核注金额3",
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
